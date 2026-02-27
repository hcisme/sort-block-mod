package io.github.hcisme.sortblock

import io.github.hcisme.sortblock.sortingblock.SortBlockItem
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object SortBlockMod : ModInitializer {
    private val logger = LoggerFactory.getLogger("sort-block-mod")

    const val MOD_ID = "sort-block-mod"

    override fun onInitialize() {
        SortBlockItem.init()
    }
}
