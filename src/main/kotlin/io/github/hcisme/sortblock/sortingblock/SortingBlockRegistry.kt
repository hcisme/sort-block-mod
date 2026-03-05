package io.github.hcisme.sortblock.sortingblock

import io.github.hcisme.sortblock.SortBlockMod.MOD_ID
import io.github.hcisme.sortblock.utils.registerBlockItem
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.AbstractBlock
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object SortingBlockRegistry {
    private const val SORTING_BLOCK_NAME = "sorting_block"

    val SORTING_BLOCK = registerBlockItem(
        name = SORTING_BLOCK_NAME,
        blockFactory = ::SortingBlock,
        settings = AbstractBlock.Settings.create().strength(0.5f, 6.0f)
    )

    fun init() {
        val blockId = Identifier.of(MOD_ID, SORTING_BLOCK_NAME)
        SortingBlockEntity.TYPE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            blockId,
            FabricBlockEntityTypeBuilder.create(::SortingBlockEntity, SORTING_BLOCK).build()
        )

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register { content ->
            content.add(SORTING_BLOCK)
        }
    }
}
