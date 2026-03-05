package io.github.hcisme.sortblock.utils

import io.github.hcisme.sortblock.SortBlockMod.MOD_ID
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

/**
 * 注册方块的辅助方法
 */
fun <T : Block> registerBlockItem(
    name: String,
    blockFactory: (AbstractBlock.Settings) -> T,
    settings: AbstractBlock.Settings,
    shouldRegisterItem: Boolean = true
): T {
    val blockId = Identifier.of(MOD_ID, name)
    val blockKey = RegistryKey.of(RegistryKeys.BLOCK, blockId)
    val block = blockFactory(settings.registryKey(blockKey))

    if (shouldRegisterItem) {
        val itemKey = RegistryKey.of(RegistryKeys.ITEM, blockId)
        val blockItem = BlockItem(
            block,
            Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey()
        )
        Registry.register(Registries.ITEM, itemKey, blockItem)
    }

    return Registry.register(Registries.BLOCK, blockKey, block)
}
