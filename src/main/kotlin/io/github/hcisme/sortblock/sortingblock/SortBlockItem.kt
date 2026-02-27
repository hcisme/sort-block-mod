package io.github.hcisme.sortblock.sortingblock

import io.github.hcisme.sortblock.SortBlockMod.MOD_ID
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.AbstractBlock
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

object SortBlockItem {
    // 必须在创建对象之前就确定好 ID
    private val BLOCK_ID = Identifier.of(MOD_ID, "sorting_block")

    // 创建方块的 Key
    private val BLOCK_KEY = RegistryKey.of(RegistryKeys.BLOCK, BLOCK_ID)

    // 创建物品的 Key (通常和方块同名)
    private val ITEM_KEY = RegistryKey.of(RegistryKeys.ITEM, BLOCK_ID)

    private val SORTING_BLOCK = SortingBlock(
        AbstractBlock.Settings
            .create()
            .strength(0.5f, 6.0f)
            .registryKey(BLOCK_KEY)
    )

    fun init() {
        // 注册方块
        // 使用 register 的重载方法，直接传 Key
        Registry.register(Registries.BLOCK, BLOCK_KEY, SORTING_BLOCK)

        // 注册对应的物品 (BlockItem)
        val blockItem = BlockItem(
            SORTING_BLOCK,
            Item.Settings().registryKey(ITEM_KEY)
        )
        Registry.register(Registries.ITEM, ITEM_KEY, blockItem)

        // 注册 BlockEntity
        SortingBlockEntity.Companion.TYPE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            BLOCK_ID,
            FabricBlockEntityTypeBuilder.create(::SortingBlockEntity, SORTING_BLOCK).build()
        )

        // 添加到创造模式
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register { content ->
            content.add(blockItem)
        }
    }
}
