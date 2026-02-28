package io.github.hcisme.sortblock

import io.github.hcisme.sortblock.sortingblock.SortBlockItem
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object SortBlockMod : ModInitializer {
    const val MOD_ID = "sort-block-mod"
    private val logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        SortBlockItem.init()
    }
}
