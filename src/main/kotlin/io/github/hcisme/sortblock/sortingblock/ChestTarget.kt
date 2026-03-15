package io.github.hcisme.sortblock.sortingblock

import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos

/**
 * 缓存箱子坐标的类
 */
data class ChestTarget(
    val pos: BlockPos,
    /** 1=分类, 2=精准, 3=空框, 4=杂物 **/
    var priority: Int,
    /** 只有优先级1需要这个，用于记录展示框里是啥 **/
    var filterItem: Item? = null
) : Comparable<ChestTarget> {
    // 按优先级排序 (1 -> 2 -> 3 -> 4)
    override fun compareTo(other: ChestTarget): Int {
        return this.priority.compareTo(other.priority)
    }
}
