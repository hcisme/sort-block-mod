package io.github.hcisme.sortblock.sortingblock

import io.github.hcisme.sortblock.utils.ItemCategoryRegistry
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.SignBlockEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.DustParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import kotlin.math.sqrt

class SortingBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(TYPE, pos, state) {
    companion object {
        lateinit var TYPE: BlockEntityType<SortingBlockEntity>
        private const val RANGE_X = 30
        private const val RANGE_Y = 5
        private const val RANGE_Z = 8
        private const val SUNDRIES = "sundries"

        fun tick(world: World, pos: BlockPos, state: BlockState, entity: SortingBlockEntity) {
            entity.tickServer(world, pos)
        }
    }

    private val cachedInventories = mutableListOf<ChestTarget>()
    private var scanCooldown = 0

    private fun tickServer(world: World, pos: BlockPos) {
        if (world.isClient) return
        if (world.time % 40 == 0L) drawSelectionBox(world, pos)

        if (scanCooldown > 0) {
            scanCooldown -= 1
        } else {
            rescanArea(world, pos)
            // 100 ticks (5秒) 扫描一次
            scanCooldown = 100
            // scanCooldown = 20
        }

        val searchBox = Box(pos.up())
        val droppedItems = world.getEntitiesByClass(ItemEntity::class.java, searchBox) { true }
        if (droppedItems.isEmpty() || cachedInventories.isEmpty()) return

        for (itemEntity in droppedItems) {
            val stack = itemEntity.stack
            if (stack.isEmpty) continue

            val success = trySortItem(world, pos, stack)
            if (success) {
                // 如果整组都存进去了，销毁掉落物实体
                if (stack.isEmpty) {
                    itemEntity.discard()
                }
                // 播放粒子效果
                spawnParticles(world, pos)
            }
        }
    }

    private fun trySortItem(world: World, center: BlockPos, stack: ItemStack): Boolean {
        val itemVariant = ItemVariant.of(stack)
        val originalCount = stack.count.toLong()

        // 遍历缓存的箱子列表 (已经按优先级 1->2->3 排序过)
        for (target in cachedInventories) {
            // 优先级1
            if (target.priority == 1) {
                // 获取展示框里的物品
                val filter = target.filterItem

                if (filter != null && !ItemCategoryRegistry.isMatch(filter, stack)) {
                    continue
                }
            }

            // 优先级2空框 和 3杂物 无条件尝试存入
            val targetStorage = ItemStorage.SIDED.find(world, target.pos, Direction.UP) ?: continue

            var success = false
            Transaction.openOuter().use { tx ->
                // 尝试插入物品 simulate = false (默认), 表示真实插入
                val inserted = targetStorage.insert(itemVariant, originalCount, tx)
                if (inserted > 0) {
                    stack.decrement(inserted.toInt())
                    tx.commit()
                    success = true
                    spawnLineParticles(world, center, target.pos)
                }
            }

            if (success) {
                // 如果是空展示框 需要自动给展示框贴上这个物品
                if (target.priority == 2) {
                    val isFramed = updateEmptyFrame(world, target.pos, itemVariant.toStack(), targetStorage)
                    if (isFramed) {
                        target.priority = 1
                        target.filterItem = itemVariant.item
                        cachedInventories.sort()
                    }
                }
                return true
            }
        }
        return false
    }

    private fun rescanArea(world: World, center: BlockPos) {
        cachedInventories.clear()

        val area = BlockPos.iterate(
            center,
            center.add(RANGE_X, RANGE_Y, RANGE_Z)
        )
        area.forEach { currentPos ->
            if (currentPos == center) return@forEach
            val storage = ItemStorage.SIDED.find(world, currentPos, Direction.UP) ?: return@forEach
            if (!storage.supportsInsertion()) return@forEach

            val immutablePos = currentPos.toImmutable()
            val frame = findAttachedItemFrame(world, immutablePos)
            if (frame != null) {
                val frameStack = frame.heldItemStack
                if (!frameStack.isEmpty) {
                    // 情况1: 展示框里有东西 -> 优先级 1
                    cachedInventories.add(ChestTarget(immutablePos, 1, frameStack.item))
                } else {
                    // 情况2: 展示框是空的 -> 优先级 2
                    cachedInventories.add(ChestTarget(immutablePos, 2, null))
                }
                // 如果找到了展示框，就不需要再找告示牌了（展示框优先）
                return@forEach
            }

            // 情况3. 检测杂物箱告示牌 (优先级 3)
            if (checkSignForSundries(world, immutablePos)) {
                cachedInventories.add(ChestTarget(immutablePos, 3, null))
            }
        }

        cachedInventories.sort()
    }

    /**
     * 查找贴在目标方块上的物品展示框
     */
    private fun findAttachedItemFrame(world: World, targetPos: BlockPos): ItemFrameEntity? {
        val frame = world.getEntitiesByClass(
            ItemFrameEntity::class.java,
            Box(targetPos).expand(0.2)
        ) { frame ->
            val attachedPos = frame.blockPos.offset(frame.horizontalFacing.opposite)
            attachedPos == targetPos
        }
        return frame.firstOrNull()
    }

    /**
     * 检查箱子周围有没有贴着写了 "sundries" 的告示牌
     */
    private fun checkSignForSundries(world: World, pos: BlockPos): Boolean {
        // 扫描箱子六个面
        for (dir in Direction.entries) {
            val offsetPos = pos.offset(dir)
            val be = world.getBlockEntity(offsetPos)
            if (be is SignBlockEntity) {
                // 检查告示牌的正面和背面文本
                val textFront = be.frontText.getMessage(0, false).string.lowercase()
                val textBack = be.backText.getMessage(0, false).string.lowercase()

                if (textFront.contains(SUNDRIES) || textBack.contains(SUNDRIES)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 找到箱子上的空展示框，并把物品贴上去
     */
    private fun updateEmptyFrame(
        world: World,
        pos: BlockPos,
        sampleStack: ItemStack,
        targetStorage: Storage<ItemVariant>
    ): Boolean {
        // 查找贴在这个方块上的空展示框
        val frames = world.getEntitiesByClass(ItemFrameEntity::class.java, Box(pos).expand(1.0)) { frame ->
            val attachedPos = frame.blockPos.offset(frame.horizontalFacing.opposite)
            attachedPos == pos && frame.heldItemStack.isEmpty
        }

        if (frames.isNotEmpty()) {
            val frame = frames[0]

            val tx = Transaction.openOuter()
            tx.use {
                val extractedCount = targetStorage.extract(
                    ItemVariant.of(sampleStack),
                    1,
                    tx
                )

                if (extractedCount == 1L) {
                    // 成功取出了 1 个
                    tx.commit() // 确认操作

                    // 把这个真实的物品贴到展示框上
                    val displayStack = sampleStack.copy()
                    displayStack.count = 1
                    frame.heldItemStack = displayStack

                    // 播放音效
                    frame.playSound(SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, 1.0f, 1.0f)
                    return true
                } else {
                    tx.abort()
                }
            }
        }
        return false
    }

    /**
     * 绘制能够扫描的区域
     */
    private fun drawSelectionBox(world: World, center: BlockPos) {
        if (world !is ServerWorld) return

        // 计算边界坐标 (double 类型，方便粒子定位)
        val minX = center.x.toDouble()
        val minY = center.y.toDouble()
        val minZ = center.z.toDouble()

        val maxX = center.x.toDouble() + RANGE_X.toDouble() + 1.0
        val maxY = center.y.toDouble() + RANGE_Y.toDouble() + 1.0
        val maxZ = center.z.toDouble() + RANGE_Z.toDouble() + 1.0

        // 定义粒子颜色
        val dustEffect = DustParticleEffect(0x00FFFF, 1.0f)

        // 绘制 12 条棱
        // 底面 4 条
        spawnLine(world, dustEffect, minX, minY, minZ, maxX, minY, minZ)
        spawnLine(world, dustEffect, minX, minY, maxZ, maxX, minY, maxZ)
        spawnLine(world, dustEffect, minX, minY, minZ, minX, minY, maxZ)
        spawnLine(world, dustEffect, maxX, minY, minZ, maxX, minY, maxZ)

        // 顶面 4 条
        spawnLine(world, dustEffect, minX, maxY, minZ, maxX, maxY, minZ)
        spawnLine(world, dustEffect, minX, maxY, maxZ, maxX, maxY, maxZ)
        spawnLine(world, dustEffect, minX, maxY, minZ, minX, maxY, maxZ)
        spawnLine(world, dustEffect, maxX, maxY, minZ, maxX, maxY, maxZ)

        // 垂直 4 条
        spawnLine(world, dustEffect, minX, minY, minZ, minX, maxY, minZ)
        spawnLine(world, dustEffect, maxX, minY, minZ, maxX, maxY, minZ)
        spawnLine(world, dustEffect, minX, minY, maxZ, minX, maxY, maxZ)
        spawnLine(world, dustEffect, maxX, minY, maxZ, maxX, maxY, maxZ)
    }

    /**
     * 画一条直线粒子
     */
    private fun spawnLine(
        world: ServerWorld,
        effect: ParticleEffect,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double
    ) {
        val distance = sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1))
        val steps = (distance * 2).toInt() // 每格 2 个粒子

        for (i in 0..steps) {
            val t = i.toDouble() / steps
            val px = x1 + (x2 - x1) * t
            val py = y1 + (y2 - y1) * t
            val pz = z1 + (z2 - z1) * t

            // force=true 确保远处的玩家也能看到
            world.spawnParticles(effect, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0)
        }
    }

    /**
     * 物品落在排序方块上后消失的特效
     */
    private fun spawnParticles(world: World, pos: BlockPos) {
        if (world is ServerWorld) {
            world.spawnParticles(
                ParticleTypes.POOF,
                pos.x + 0.5,
                pos.up().y + 0.2,
                pos.z + 0.5,
                5,
                0.2,
                0.2,
                0.2,
                0.0
            )
        }
    }

    /**
     * 在两点之间生成粒子连线
     */
    private fun spawnLineParticles(world: World, start: BlockPos, end: BlockPos) {
        if (world is ServerWorld) {
            val x1 = start.x + 0.5
            val y1 = start.y + 0.5
            val z1 = start.z + 0.5
            val x2 = end.x + 0.5
            val y2 = end.y + 0.5
            val z2 = end.z + 0.5

            val steps = 10 // 粒子密度
            for (i in 0..steps) {
                val t = i.toDouble() / steps
                val px = x1 + (x2 - x1) * t
                val py = y1 + (y2 - y1) * t
                val pz = z1 + (z2 - z1) * t

                // 生成绿色快乐粒子
                world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0)
            }
        }
    }
}