package io.github.hcisme.sortblock.sortingblock

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * SortingBlock
 * 作用：这是放置在世界中的物理方块。
 * 继承 BlockWithEntity 是因为我们需要它携带数据和逻辑 (BlockEntity)。
 */
class SortingBlock(settings: Settings) : BlockWithEntity(settings) {
    companion object {
        private val CODEC: MapCodec<SortingBlock> = createCodec(::SortingBlock)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return SortingBlockEntity(pos, state)
    }

    override fun <T : BlockEntity?> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        // 只有在服务端 (Server) 才运行逻辑，客户端只负责渲染
        // 确保我们只运行在这个方块对应的实体上
        if (type == SortingBlockEntity.TYPE) {
            return BlockEntityTicker { world, pos, state, entity ->
                SortingBlockEntity.tick(world, pos, state, entity as SortingBlockEntity)
            }
        }
        return null
    }

    // 渲染模式 必须设置，否则方块会变成隐形的
    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL

    override fun getCodec(): MapCodec<out BlockWithEntity?> = CODEC
}
