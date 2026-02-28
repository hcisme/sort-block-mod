package io.github.hcisme.sortblock.utils

import net.minecraft.component.DataComponentTypes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.ItemTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object ItemCategoryRegistry {
    // --- 0. 定义 Fabric 通用标签 ---
    private val C_CROPS = createTag("c", "crops")
    private val C_SEEDS = createTag("c", "seeds")
    private val C_FOODS = createTag("c", "foods")
    private val C_INGOTS = createTag("c", "ingots")
    private val C_RAW_MATERIALS = createTag("c", "raw_materials")
    private val C_ORES = createTag("c", "ores")
    private val C_STORAGE_BLOCKS = createTag("c", "storage_blocks")
    private val C_NUGGETS = createTag("c", "nuggets")
    private val C_GLASS = createTag("c", "glass_blocks")
    private val C_GLASS_PANES = createTag("c", "glass_panes")
    private val C_CHESTS = createTag("c", "chests")

    private val CATEGORY_RULES = mutableMapOf<Item, List<TagKey<Item>>>()

    // 1. 建筑石材集合
    private val STONE_VARIANTS = setOf(
        Items.STONE, Items.GRANITE, Items.POLISHED_GRANITE,
        Items.DIORITE, Items.POLISHED_DIORITE,
        Items.ANDESITE, Items.POLISHED_ANDESITE,
        Items.DEEPSLATE, Items.COBBLED_DEEPSLATE, Items.POLISHED_DEEPSLATE,
        Items.TUFF, Items.CALCITE, Items.BASALT, Items.POLISHED_BASALT, Items.SMOOTH_BASALT,
        Items.NETHERRACK, Items.BLACKSTONE, Items.POLISHED_BLACKSTONE, Items.GILDED_BLACKSTONE,
        Items.DRIPSTONE_BLOCK, Items.POINTED_DRIPSTONE, Items.AMETHYST_BLOCK, Items.AMETHYST_CLUSTER,
        Items.SMOOTH_STONE, Items.SOUL_SAND, Items.NETHER_BRICK, Items.MOSSY_COBBLESTONE, Items.ICE,
        Items.PACKED_ICE, Items.BLUE_ICE, Items.END_STONE, Items.SHROOMLIGHT, Items.GRAVEL, Items.SOUL_SOIL
    )

    // 2. 红石元件集合
    private val REDSTONE_COMPONENTS = setOf(
        Items.REPEATER,
        Items.COMPARATOR,
        Items.OBSERVER,
        Items.DROPPER,
        Items.DISPENSER,
        Items.HOPPER,
        Items.PISTON,
        Items.STICKY_PISTON,
        Items.REDSTONE_BLOCK,
        Items.REDSTONE_TORCH,
        Items.TARGET,
        Items.LEVER,
        Items.LIGHTNING_ROD,
        Items.DAYLIGHT_DETECTOR,
        Items.SCULK_SENSOR,
        Items.CALIBRATED_SCULK_SENSOR,
        Items.TRIPWIRE_HOOK,
        Items.LECTERN,
        // 所有压力板
        Items.STONE_PRESSURE_PLATE,
        Items.OAK_PRESSURE_PLATE,
        Items.SPRUCE_PRESSURE_PLATE,
        Items.BIRCH_PRESSURE_PLATE,
        Items.JUNGLE_PRESSURE_PLATE,
        Items.ACACIA_PRESSURE_PLATE,
        Items.DARK_OAK_PRESSURE_PLATE,
        Items.MANGROVE_PRESSURE_PLATE,
        Items.CHERRY_PRESSURE_PLATE,
        Items.BAMBOO_PRESSURE_PLATE,
        Items.CRIMSON_PRESSURE_PLATE,
        Items.WARPED_PRESSURE_PLATE,
        Items.POLISHED_BLACKSTONE_PRESSURE_PLATE,
        Items.HEAVY_WEIGHTED_PRESSURE_PLATE,
        Items.LIGHT_WEIGHTED_PRESSURE_PLATE
    )

    // 3. 动物装备集合
    private val ANIMAL_EQUIPMENT = setOf(
        Items.SADDLE, Items.LEATHER_HORSE_ARMOR, Items.IRON_HORSE_ARMOR,
        Items.GOLDEN_HORSE_ARMOR, Items.DIAMOND_HORSE_ARMOR, Items.WOLF_ARMOR, Items.COPPER_HORSE_ARMOR,
        Items.NETHERITE_HORSE_ARMOR
    )

    // 4. 木工/杂项集合 (解决 CAMPFIRES 找不到的问题)
    private val WOOD_MISC_ITEMS = setOf(
        Items.CAMPFIRE, Items.SOUL_CAMPFIRE,
        Items.LADDER, Items.TORCH, Items.SOUL_TORCH,
        Items.LANTERN, Items.SOUL_LANTERN,
        Items.ITEM_FRAME, Items.GLOW_ITEM_FRAME, Items.PAINTING,
        Items.ARMOR_STAND, Items.BOWL, Items.STICK,
        Items.PAPER, Items.BOOK, Items.WRITABLE_BOOK, Items.WRITTEN_BOOK,
        Items.FLOWER_POT, Items.COMPOSTER, Items.BARREL, Items.CRAFTING_TABLE
    )

    init {
        // --- 1. 🌾 农业与植物 ---
        register(
            Items.WHEAT,
            C_CROPS, C_SEEDS,
            ItemTags.VILLAGER_PLANTABLE_SEEDS,
            ItemTags.SAPLINGS,
            ItemTags.FLOWERS,
            ItemTags.LEAVES,
            ItemTags.WART_BLOCKS,
            createTag("c", "dyes") // 染料
        )

        // --- 2. 🍎 食物 ---
        register(
            Items.APPLE,
            C_FOODS,
            ItemTags.FISHES
        )

        // --- 3. 💎 矿产与资源 ---
        register(
            Items.IRON_INGOT,
            C_INGOTS, C_RAW_MATERIALS, C_ORES, C_STORAGE_BLOCKS, C_NUGGETS,
            ItemTags.BEACON_PAYMENT_ITEMS,
            ItemTags.COAL_ORES, ItemTags.REDSTONE_ORES, ItemTags.LAPIS_ORES,
            ItemTags.DIAMOND_ORES, ItemTags.GOLD_ORES, ItemTags.IRON_ORES, ItemTags.COPPER_ORES
        )

        // --- 4. 🛡️ 装备与防具 ---
        register(
            Items.IRON_CHESTPLATE,
            ItemTags.TRIMMABLE_ARMOR,
            ItemTags.HEAD_ARMOR,
            ItemTags.CHEST_ARMOR,
            ItemTags.LEG_ARMOR,
            ItemTags.FOOT_ARMOR
        )

        // --- 5. 🪓 工具 ---
        register(
            Items.IRON_AXE,
            ItemTags.AXES, ItemTags.HOES, ItemTags.PICKAXES, ItemTags.SHOVELS, ItemTags.SWORDS,
            ItemTags.ARROWS
        )

        // --- 6. 🧱 建筑材料 ---
        register(
            Items.COBBLESTONE,
            ItemTags.STONE_BRICKS,
            ItemTags.WOOL,
            ItemTags.WOOL_CARPETS,
            ItemTags.LOGS,
            ItemTags.PLANKS,
            ItemTags.STAIRS,
            ItemTags.SLABS,
            ItemTags.WALLS,
            ItemTags.FENCES,
            ItemTags.FENCE_GATES,
            ItemTags.TERRACOTTA,
            ItemTags.DIRT,
            ItemTags.SAND,
            ItemTags.CANDLES,
            ItemTags.BEDS,
            C_GLASS,
            C_GLASS_PANES
        )

        // --- 7. 🚂 交通 ---
        register(
            Items.MINECART,
            ItemTags.RAILS,
            ItemTags.BOATS,
            ItemTags.CHEST_BOATS,
            createTag("c", "minecarts")
        )

        // --- 8. 🔴 红石 ---
        register(
            Items.REDSTONE,
            ItemTags.REDSTONE_ORES,
            ItemTags.BUTTONS,
            ItemTags.DOORS,
            ItemTags.TRAPDOORS,
            createTag("c", "dusts")
        )

        // --- 9. 💀 怪物战利品 ---
        register(
            Items.ROTTEN_FLESH,
            createTag("c", "bones"),
            createTag("c", "rotten_flesh"),
            createTag("c", "slimeballs"),
            createTag("c", "strings"),
            createTag("c", "spider_eyes"),
            createTag("c", "gunpowder"),
            createTag("c", "ender_pearls"),
            createTag("c", "blaze_rods"),
            createTag("c", "ghast_tears"),
            createTag("c", "feathers"),
            createTag("c", "leather")
        )

        // --- 10. 🪵 木工/家具 ---
        register(
            Items.OAK_LOG,
            C_CHESTS,
            ItemTags.SIGNS,
            ItemTags.HANGING_SIGNS
        )

        // --- 11. 🧪 魔法 ---
        register(
            Items.GLASS_BOTTLE
        )
    }

    private fun register(representative: Item, vararg tags: TagKey<Item>) {
        CATEGORY_RULES[representative] = tags.toList()
    }

    fun isMatch(filterItem: Item, stack: ItemStack): Boolean {
        if (filterItem == stack.item) return true

        // 1. 查 Tag 表
        val tags = CATEGORY_RULES[filterItem]
        if (tags != null) {
            for (tag in tags) {
                if (stack.isIn(tag)) return true
            }
        }

        // 2. 查硬编码集合 (处理找不到 Tag 的物品)
        val item = stack.item

        // A. 建筑补充
        if (filterItem == Items.COBBLESTONE) {
            if (STONE_VARIANTS.contains(item)) return true
            if (item == Items.BRICK || item == Items.BRICK_STAIRS || item == Items.BRICK_SLAB) return true
        }

        // B. 红石补充
        if (filterItem == Items.REDSTONE) {
            if (REDSTONE_COMPONENTS.contains(item)) return true
        }

        // C. 交通补充
        if (filterItem == Items.MINECART) {
            if (ANIMAL_EQUIPMENT.contains(item)) return true
            if (item == Items.CARROT_ON_A_STICK || item == Items.WARPED_FUNGUS_ON_A_STICK ||
                item == Items.ELYTRA || item == Items.LEATHER
            ) return true
        }

        // D. 木工/杂项补充
        if (filterItem == Items.OAK_LOG) {
            if (WOOD_MISC_ITEMS.contains(item)) return true
        }

        // E. 矿产补充
        if (filterItem == Items.IRON_INGOT) {
            if (item == Items.FLINT || item == Items.INK_SAC || item == Items.GLOW_INK_SAC ||
                item == Items.AMETHYST_SHARD || item == Items.QUARTZ || item == Items.NETHER_QUARTZ_ORE
            ) return true
        }

        // F. 工具补充
        if (filterItem == Items.IRON_AXE) {
            if (item == Items.SHEARS || item == Items.FLINT_AND_STEEL ||
                item == Items.FISHING_ROD || item == Items.NAME_TAG ||
                item == Items.LEAD || item == Items.BUNDLE || item == Items.SHIELD
            ) return true
            // 2. 远程武器与弹药
            if (item == Items.BOW || item == Items.CROSSBOW || item == Items.TRIDENT) return true
            if (item == Items.ARROW || item == Items.SPECTRAL_ARROW || item == Items.TIPPED_ARROW) return true

            // 3. 1.21 新武器
            if (item == Items.MACE || item == Items.WIND_CHARGE) return true

            // 桶类
            if (item == Items.BUCKET || item == Items.WATER_BUCKET || item == Items.LAVA_BUCKET ||
                item == Items.MILK_BUCKET || item == Items.POWDER_SNOW_BUCKET || item == Items.AXOLOTL_BUCKET ||
                item == Items.TADPOLE_BUCKET
            ) return true
        }

        // G. 魔法补充
        if (filterItem == Items.GLASS_BOTTLE) {
            if (item == Items.ENCHANTED_BOOK || item == Items.EXPERIENCE_BOTTLE ||
                item == Items.LAPIS_LAZULI || item == Items.BREWING_STAND ||
                item == Items.CAULDRON || item == Items.CRYING_OBSIDIAN || item == Items.ENDER_EYE ||
                item == Items.POTION || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION
            ) return true
        }

        // H. 农业补充
        if (filterItem == Items.WHEAT) {
            if (item == Items.BONE_MEAL || item == Items.LILY_PAD ||
                item == Items.MOSS_BLOCK || item == Items.MOSS_CARPET ||
                item == Items.HONEYCOMB || item == Items.HONEYCOMB_BLOCK ||
                item == Items.BEEHIVE || item == Items.BEE_NEST
            ) return true
        }

        // I. 食物补充
        if (filterItem == Items.APPLE) {
            if (isFood(stack)) {
                if (item != Items.ROTTEN_FLESH && item != Items.SPIDER_EYE && item != Items.PUFFERFISH) return true
            }
            if (item == Items.EGG || item == Items.SUGAR || item == Items.CAKE) return true
        }

        // J. 怪物掉落补充
        if (filterItem == Items.ROTTEN_FLESH) {
            if (item == Items.RABBIT_HIDE || item == Items.SNOWBALL) return true
        }

        return false
    }

    private fun isFood(stack: ItemStack): Boolean = stack.contains(DataComponentTypes.FOOD)

    private fun createTag(namespace: String, path: String): TagKey<Item> {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(namespace, path))
    }
}
