package io.github.hcisme.sortblock.sortingblock

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

/**
 * SortingBlockEntity (排序逻辑实体)
 * 作用：处理物品检测、箱子扫描、展示框识别和物品传输。
 */
class SortingBlockEntity(pos: BlockPos, state: BlockState) : BlockEntity(TYPE, pos, state) {
    companion object {
        // 定义这个实体在注册表里的类型，稍后在主类中赋值
        lateinit var TYPE: BlockEntityType<SortingBlockEntity>
        private const val RANGE_X = 30
        private const val RANGE_Y = 5
        private const val RANGE_Z = 10

        // 核心 Tick 方法：每 1 Tick 运行一次
        fun tick(world: World, pos: BlockPos, state: BlockState, entity: SortingBlockEntity) {
            if (world.isClient) return // 客户端不处理逻辑
            // --- 视觉反馈：绘制边框 ---
            // 每 20 tick (1秒) 绘制一次，避免粒子太多卡顿
            if (world.time % 20 == 0L) {
                entity.drawSelectionBox(world, pos)
            }

            // 1. 扫描头顶：检测方块上方 1 格内的掉落物
            // Box 参数：x1, y1, z1, x2, y2, z2
            val searchBox = Box(pos.up())
            val droppedItems = world.getEntitiesByClass(ItemEntity::class.java, searchBox) { true }

            if (droppedItems.isEmpty()) return

            // 2. 如果有掉落物，开始处理每一个
            for (itemEntity in droppedItems) {
                // 为了防止还没被捡起就消失，重置它的消失时间
//                 itemEntity.resetPickupDelay()

                // 获取掉落物里的物品堆 (ItemStack)
                val stack = itemEntity.stack
                if (stack.isEmpty) continue

                // 3. 执行分拣逻辑
                val success = entity.trySortItem(world, pos, stack)

                // 4. 如果分拣成功，消耗掉地上的物品，并播放特效
                if (success) {
                    // 如果整组都存进去了，销毁掉落物实体
                    if (stack.isEmpty) {
                        itemEntity.discard()
                    }
                    // 播放粒子效果 (视觉反馈)
                    entity.spawnParticles(world, pos)
                }
            }
        }
    }

    /**
     * 尝试分拣单个物品
     * 返回值：是否成功处理了至少 1 个物品
     */
    private fun trySortItem(world: World, center: BlockPos, stack: ItemStack): Boolean {
        val itemVariant = ItemVariant.of(stack)

        // 扫描范围内的所有方块
        // BlockPos.iterate 是一个懒加载迭代器，性能较好
        val area = BlockPos.iterate(
            center, // 起点：方块自身位置 (包含)
            center.add(RANGE_X, RANGE_Y, RANGE_Z) // 终点：向 X+ Z+ 方向延伸
        )

        // 优先级队列逻辑
        // 我们不能扫到一个箱子就处理，必须先找"完美匹配"，再找"空展示框"，最后"杂物"
        // 为了效率，我们在一次遍历中收集三种目标

        var matchFrameTarget: BlockPos? = null // 目标1: 已有相同物品的展示框
        var emptyFrameTarget: BlockPos? = null // 目标2: 空展示框
        var sundriesTarget: BlockPos? = null   // 目标3: 杂物箱

        for (targetPos in area) {
            // 跳过自己
            if (targetPos == center) continue

            // 检查这个位置是不是一个容器 (箱子/桶)
            // Fabric API: ItemStorage.SIDED.find 负责寻找任何模组的容器
            val storage = ItemStorage.SIDED.find(world, targetPos, Direction.UP) ?: continue

            // --- 检查 A: 物品展示框 ---
            // 获取贴在这个方块上的所有展示框
            // 过滤条件：展示框的坐标必须贴合当前箱子 (AttachedPos)
            val frames =
                world.getEntitiesByClass(ItemFrameEntity::class.java, Box(targetPos).expand(1.0)) { frame ->
                    // 关键检查：展示框是不是贴在这个方块上的？
                    // facing.opposite 是展示框背后的方块方向
                    val attachedPos = frame.blockPos.offset(frame.horizontalFacing.opposite)
                    attachedPos == targetPos
                }

            for (frame in frames) {
                val frameStack = frame.heldItemStack

                if (!frameStack.isEmpty && frameStack.item == stack.item) {
                    // 命中优先级 1: 展示框里有一样的物品
                    matchFrameTarget = targetPos.toImmutable()
                    break
                } else if (frameStack.isEmpty) {
                    // 命中优先级 2: 展示框是空的
                    // 只有当还没有找到优先级 1 时，才记录优先级 2
                    if (emptyFrameTarget == null) emptyFrameTarget = targetPos.toImmutable()
                }
            }

            if (matchFrameTarget != null) break // 找到最高优先级，直接停止扫描

            // --- 检查 B: 告示牌 (杂物) ---
            // 只有当还没找到任何展示框目标时，才检查告示牌
            if (emptyFrameTarget == null && sundriesTarget == null) {
                if (checkSignForSundries(world, targetPos)) {
                    sundriesTarget = targetPos.toImmutable()
                }
            }
        }

        // 决策阶段：按优先级执行传输
        val finalTarget = matchFrameTarget ?: emptyFrameTarget ?: sundriesTarget

        if (finalTarget != null) {
            val targetStorage = ItemStorage.SIDED.find(world, finalTarget, Direction.UP) ?: return false

            // 使用 Fabric Transaction API 安全传输物品
            // 类似于数据库事务：要么全成功，要么回滚
            val tx = Transaction.openOuter()
            tx.use {
                // 尝试插入物品
                val inserted = targetStorage.insert(itemVariant, stack.count.toLong(), tx)

                if (inserted > 0) {
                    // 真正的扣除物品
                    stack.decrement(inserted.toInt())
                    tx.commit() // 提交事务

                    // 特殊处理：如果是"空展示框" (Priority 2)，需要自动贴标
                    if (finalTarget == emptyFrameTarget) {
                        updateEmptyFrame(world, finalTarget, itemVariant.toStack(), targetStorage)
                    }

                    // 绘制连接线粒子 (视觉反馈)
                    spawnLineParticles(world, center, finalTarget)
                    return true
                }
            }
        }

        return false
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

                if (textFront.contains("sundries") || textBack.contains("sundries")) {
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
    ) {
        val frames = world.getEntitiesByClass(ItemFrameEntity::class.java, Box(pos).expand(1.0)) { frame ->
            val attachedPos = frame.blockPos.offset(frame.horizontalFacing.opposite)
            attachedPos == pos && frame.heldItemStack.isEmpty
        }

        if (frames.isNotEmpty()) {
            val frame = frames[0]

            // 核心逻辑：从箱子里取出一个物品贴上去
            // 我们需要再次开启一个事务来操作箱子
            val tx = Transaction.openOuter()
            tx.use {
                // extract: 从箱子里提取 1 个 sampleStack 类型的物品
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
                } else {
                    // 如果箱子里竟然取不出来（极其罕见的情况），那就不贴了，避免刷物品 bug
                    tx.abort()
                }
            }
        }
    }

    /**
     * 视觉反馈：在两点之间生成粒子连线
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

    private fun drawSelectionBox(world: World, center: BlockPos) {
        if (world !is ServerWorld) return

        // 计算边界坐标 (double 类型，方便粒子定位)
        val minX = center.x.toDouble()
        val minY = center.y.toDouble()
        val minZ = center.z.toDouble()

        val maxX = center.x.toDouble() + RANGE_X.toDouble() + 1.0
        val maxY = center.y.toDouble() + RANGE_Y.toDouble() + 1.0
        val maxZ = center.z.toDouble() + RANGE_Z.toDouble() + 1.0

        // 定义粒子颜色 (比如青色激光)
        // Vector3f(R, G, B) 范围 0.0 - 1.0
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

    // 辅助方法：画一条直线粒子
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
}