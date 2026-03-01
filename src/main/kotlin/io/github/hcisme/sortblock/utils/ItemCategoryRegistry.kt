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
    // Fabric 通用标签（保持不变）
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

    // 1. 建筑方块集合
    private val STONE_VARIANTS = setOf(
        Items.STONE, // 石头
        Items.GRANITE, // 花岗岩
        Items.POLISHED_GRANITE, // 磨制花岗岩
        Items.DIORITE, // 闪长岩
        Items.POLISHED_DIORITE, // 磨制闪长岩
        Items.ANDESITE, // 安山岩
        Items.POLISHED_ANDESITE, // 磨制安山岩
        Items.DEEPSLATE, // 深板岩
        Items.COBBLED_DEEPSLATE, // 深板岩圆石
        Items.POLISHED_DEEPSLATE, // 磨制深板岩
        Items.TUFF, // 凝灰岩
        Items.CALCITE, // 方解石
        Items.BASALT, // 玄武岩
        Items.POLISHED_BASALT, // 磨制玄武岩
        Items.SMOOTH_BASALT, // 平滑玄武岩
        Items.NETHERRACK, // 下界岩
        Items.BLACKSTONE, // 黑石
        Items.POLISHED_BLACKSTONE, // 磨制黑石
        Items.GILDED_BLACKSTONE, // 镶金黑石
        Items.DRIPSTONE_BLOCK, // 滴水石块
        Items.POINTED_DRIPSTONE, // 滴水石锥
        Items.AMETHYST_BLOCK, // 紫水晶块
        Items.AMETHYST_CLUSTER, // 紫水晶簇
        Items.SMOOTH_STONE, // 平滑石头
        Items.SOUL_SAND, // 灵魂沙
        Items.NETHER_BRICKS, // 下界砖块
        Items.MOSSY_COBBLESTONE, // 苔石
        Items.ICE, // 冰
        Items.PACKED_ICE, // 浮冰
        Items.BLUE_ICE, // 蓝冰
        Items.END_STONE, // 末地石
        Items.SHROOMLIGHT, // 菌光体
        Items.GRAVEL, // 砂砾
        Items.SOUL_SOIL, // 灵魂土
        Items.COBBLESTONE, // 圆石
        Items.STONE_BRICKS, // 石砖
        Items.MOSSY_STONE_BRICKS, // 苔石砖
        Items.CRACKED_STONE_BRICKS, // 裂纹石砖
        Items.CHISELED_STONE_BRICKS, // 雕纹石砖
        Items.DEEPSLATE_BRICKS, // 深板岩砖
        Items.CRACKED_DEEPSLATE_BRICKS, // 裂纹深板岩砖
        Items.DEEPSLATE_TILES, // 深板岩瓦
        Items.CRACKED_DEEPSLATE_TILES, // 裂纹深板岩瓦
        Items.CHISELED_DEEPSLATE, // 雕纹深板岩
        Items.POLISHED_BLACKSTONE_BRICKS, // 磨制黑石砖
        Items.CRACKED_POLISHED_BLACKSTONE_BRICKS, // 裂纹磨制黑石砖
        Items.CHISELED_POLISHED_BLACKSTONE, // 雕纹磨制黑石
        Items.RED_NETHER_BRICKS, // 红色下界砖块
        Items.END_STONE_BRICKS // 末地石砖
    )

    // 2. 红石元件集合
    private val REDSTONE_COMPONENTS = setOf(
        Items.REPEATER, // 红石中继器
        Items.COMPARATOR, // 红石比较器
        Items.OBSERVER, // 侦测器
        Items.DROPPER, // 投掷器
        Items.DISPENSER, // 发射器
        Items.HOPPER, // 漏斗
        Items.PISTON, // 活塞
        Items.STICKY_PISTON, // 粘性活塞
        Items.REDSTONE_BLOCK, // 红石块
        Items.REDSTONE_TORCH, // 红石火把
        Items.TARGET, // 标靶
        Items.LEVER, // 拉杆
        Items.LIGHTNING_ROD, // 避雷针
        Items.DAYLIGHT_DETECTOR, // 阳光探测器
        Items.SCULK_SENSOR, // 幽匿感测体
        Items.CALIBRATED_SCULK_SENSOR, // 校频幽匿感测体
        Items.TRIPWIRE_HOOK, // 绊线钩
        Items.LECTERN, // 讲台
        Items.STONE_PRESSURE_PLATE, // 石质压力板
        Items.OAK_PRESSURE_PLATE, // 橡木压力板
        Items.SPRUCE_PRESSURE_PLATE, // 云杉木压力板
        Items.BIRCH_PRESSURE_PLATE, // 白桦木压力板
        Items.JUNGLE_PRESSURE_PLATE, // 从林木压力板
        Items.ACACIA_PRESSURE_PLATE, // 金合欢木压力板
        Items.DARK_OAK_PRESSURE_PLATE, // 深色橡木压力板
        Items.MANGROVE_PRESSURE_PLATE, // 红树木压力板
        Items.CHERRY_PRESSURE_PLATE, // 樱花木压力板
        Items.BAMBOO_PRESSURE_PLATE, // 竹压力板
        Items.CRIMSON_PRESSURE_PLATE, // 绯红木压力板
        Items.WARPED_PRESSURE_PLATE, // 诡异木压力板
        Items.POLISHED_BLACKSTONE_PRESSURE_PLATE, // 磨制黑石压力板
        Items.HEAVY_WEIGHTED_PRESSURE_PLATE, // 重质测重压力板
        Items.LIGHT_WEIGHTED_PRESSURE_PLATE, // 轻质测重压力板
        Items.STONE_BUTTON, // 石按钮
        Items.OAK_BUTTON, // 橡木按钮
        Items.SPRUCE_BUTTON, // 云杉木按钮
        Items.BIRCH_BUTTON, // 白桦木按钮
        Items.JUNGLE_BUTTON, // 从林木按钮
        Items.ACACIA_BUTTON, // 金合欢木按钮
        Items.DARK_OAK_BUTTON, // 深色橡木按钮
        Items.MANGROVE_BUTTON, // 红树木按钮
        Items.CHERRY_BUTTON, // 樱花木按钮
        Items.BAMBOO_BUTTON, // 竹按钮
        Items.CRIMSON_BUTTON, // 绯红木按钮
        Items.WARPED_BUTTON, // 诡异木按钮
        Items.POLISHED_BLACKSTONE_BUTTON, // 磨制黑石按钮
        Items.REDSTONE_LAMP, // 红石灯
        Items.NOTE_BLOCK, // 音符盒
        Items.TRAPPED_CHEST, // 陷阱箱
        Items.POWERED_RAIL, // 动力铁轨
        Items.DETECTOR_RAIL, // 探测铁轨
        Items.ACTIVATOR_RAIL, // 激活铁轨
        Items.REDSTONE_ORE, // 红石矿石
        Items.DEEPSLATE_REDSTONE_ORE // 深层红石矿石
    )

    // 3. 动物装备集合
    private val ANIMAL_EQUIPMENT = setOf(
        Items.SADDLE, // 鞍
        Items.LEATHER_HORSE_ARMOR, // 皮革马铠
        Items.IRON_HORSE_ARMOR, // 铁马铠
        Items.GOLDEN_HORSE_ARMOR, // 金马铠
        Items.DIAMOND_HORSE_ARMOR, // 钻石马铠
        Items.WOLF_ARMOR, // 狼铠
        Items.COPPER_HORSE_ARMOR, // 铜马铠
        Items.NETHERITE_HORSE_ARMOR // 下界合金马铠
    )

    // 4. 木工/杂项集合
    private val WOOD_MISC_ITEMS = setOf(
        Items.CAMPFIRE, // 营火
        Items.SOUL_CAMPFIRE, // 灵魂营火
        Items.LADDER, // 梯子
        Items.TORCH, // 火把
        Items.SOUL_TORCH, // 灵魂火把
        Items.LANTERN, // 灯笼
        Items.SOUL_LANTERN, // 灵魂灯笼
        Items.ITEM_FRAME, // 物品展示框
        Items.GLOW_ITEM_FRAME, // 荧光物品展示框
        Items.PAINTING, // 画
        Items.ARMOR_STAND, // 盔甲架
        Items.BOWL, // 碗
        Items.STICK, // 木棍
        Items.PAPER, // 纸
        Items.BOOK, // 书
        Items.WRITABLE_BOOK, // 书与笔
        Items.WRITTEN_BOOK, // 写好的书
        Items.FLOWER_POT, // 花盆
        Items.COMPOSTER, // 堆肥桶
        Items.BARREL, // 木桶
        Items.CRAFTING_TABLE, // 工作台
        Items.CHEST, // 箱子
        Items.SCAFFOLDING, // 脚手架
        Items.LOOM, // 织布机
        Items.CARTOGRAPHY_TABLE, // 制图台
        Items.FLETCHING_TABLE, // 制箭台
        Items.SMITHING_TABLE, // 锻造台
        Items.GRINDSTONE, // 砂轮
        Items.BEEHIVE, // 蜂箱
        Items.BOOKSHELF // 书架
    )

    // 为其他分类创建的辅助集合
    private val MINERAL_MISC = setOf(
        Items.FLINT, Items.INK_SAC, Items.GLOW_INK_SAC, Items.AMETHYST_SHARD,
        Items.QUARTZ, Items.NETHER_QUARTZ_ORE
    )
    private val TOOL_MISC = setOf(
        Items.SHEARS, Items.FLINT_AND_STEEL, Items.FISHING_ROD, Items.NAME_TAG,
        Items.LEAD, Items.BUNDLE, Items.SHIELD, Items.BOW, Items.CROSSBOW,
        Items.TRIDENT, Items.ARROW, Items.SPECTRAL_ARROW, Items.TIPPED_ARROW,
        Items.MACE, Items.WIND_CHARGE, Items.BUCKET, Items.WATER_BUCKET,
        Items.LAVA_BUCKET, Items.MILK_BUCKET, Items.POWDER_SNOW_BUCKET,
        Items.AXOLOTL_BUCKET, Items.TADPOLE_BUCKET
    )
    private val FARMING_MISC = setOf(
        Items.BONE_MEAL, Items.LILY_PAD, Items.MOSS_BLOCK, Items.MOSS_CARPET,
        Items.HONEYCOMB, Items.HONEYCOMB_BLOCK, Items.BEEHIVE, Items.BEE_NEST
    )
    private val FOOD_MISC = setOf(Items.EGG, Items.SUGAR, Items.CAKE)
    private val MOB_DROPS_MISC = setOf(Items.RABBIT_HIDE, Items.SNOWBALL)
    private val MAGIC_MISC = setOf(
        Items.ENCHANTED_BOOK, Items.EXPERIENCE_BOTTLE, Items.LAPIS_LAZULI,
        Items.BREWING_STAND, Items.CAULDRON, Items.CRYING_OBSIDIAN, Items.ENDER_EYE,
        Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION
    )
    private val TRANSPORT_MISC = ANIMAL_EQUIPMENT + setOf(
        Items.CARROT_ON_A_STICK, Items.WARPED_FUNGUS_ON_A_STICK, Items.ELYTRA, Items.LEATHER
    )

    // 分类枚举（数据驱动核心）
    private enum class Category(
        val representative: Item,
        val tags: List<TagKey<Item>> = emptyList(),
        val hardcodedItems: Set<Item> = emptySet(),
        val excludedItems: Set<Item> = emptySet(), // 用于提前排除（如苹果的黑名单）
        val extraMatcher: (ItemStack) -> Boolean = { false }
    ) {
        // 1. 农业与植物
        WHEAT(
            // 小麦
            representative = Items.WHEAT,
            tags = listOf(
                C_CROPS, C_SEEDS,
                ItemTags.VILLAGER_PLANTABLE_SEEDS,
                ItemTags.SAPLINGS,
                ItemTags.FLOWERS,
                ItemTags.LEAVES,
                ItemTags.WART_BLOCKS,
                createTag("c", "dyes")
            ),
            extraMatcher = { stack -> FARMING_MISC.contains(stack.item) }
        ),

        // 2. 食物
        APPLE(
            // 苹果
            representative = Items.APPLE,
            tags = listOf(C_FOODS, ItemTags.FISHES),
            excludedItems = setOf(
                Items.ROTTEN_FLESH, Items.SPIDER_EYE,
                Items.PUFFERFISH, Items.POISONOUS_POTATO
            ),
            extraMatcher = { stack ->
                isFood(stack) || FOOD_MISC.contains(stack.item)
            }
        ),

        // 3. 矿产与资源
        IRON_INGOT(
            // 铁锭
            representative = Items.IRON_INGOT,
            tags = listOf(
                C_INGOTS, C_RAW_MATERIALS, C_ORES, C_STORAGE_BLOCKS, C_NUGGETS,
                ItemTags.BEACON_PAYMENT_ITEMS,
                ItemTags.COAL_ORES, ItemTags.REDSTONE_ORES, ItemTags.LAPIS_ORES,
                ItemTags.DIAMOND_ORES, ItemTags.GOLD_ORES, ItemTags.IRON_ORES,
                ItemTags.COPPER_ORES
            ),
            extraMatcher = { stack -> MINERAL_MISC.contains(stack.item) }
        ),

        // 4. 装备与防具
        IRON_CHESTPLATE(
            // 铁胸甲
            representative = Items.IRON_CHESTPLATE,
            tags = listOf(
                ItemTags.TRIMMABLE_ARMOR,
                ItemTags.HEAD_ARMOR,
                ItemTags.CHEST_ARMOR,
                ItemTags.LEG_ARMOR,
                ItemTags.FOOT_ARMOR
            )
        ),

        // 5. 工具
        IRON_AXE(
            // 铁斧
            representative = Items.IRON_AXE,
            tags = listOf(
                ItemTags.AXES, ItemTags.HOES, ItemTags.PICKAXES,
                ItemTags.SHOVELS, ItemTags.SWORDS, ItemTags.ARROWS
            ),
            extraMatcher = { stack -> TOOL_MISC.contains(stack.item) }
        ),

        // 6. 建筑材料
        COBBLESTONE(
            // 圆石
            representative = Items.COBBLESTONE,
            tags = listOf(
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
            ),
            hardcodedItems = STONE_VARIANTS + setOf(Items.BRICKS, Items.BRICK_STAIRS, Items.BRICK_SLAB)
        ),

        // 7. 交通
        MINECART(
            // 矿车
            representative = Items.MINECART,
            tags = listOf(
                ItemTags.RAILS,
                ItemTags.BOATS,
                ItemTags.CHEST_BOATS,
                createTag("c", "minecarts")
            ),
            extraMatcher = { stack -> TRANSPORT_MISC.contains(stack.item) }
        ),

        // 8. 红石
        REDSTONE(
            // 红石粉
            representative = Items.REDSTONE,
            tags = listOf(
                ItemTags.REDSTONE_ORES,
                ItemTags.BUTTONS,
                ItemTags.DOORS,
                ItemTags.TRAPDOORS,
                createTag("c", "dusts")
            ),
            hardcodedItems = REDSTONE_COMPONENTS
        ),

        // 9. 怪物战利品
        ROTTEN_FLESH(
            // 腐肉
            representative = Items.ROTTEN_FLESH,
            tags = listOf(
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
            ),
            extraMatcher = { stack -> MOB_DROPS_MISC.contains(stack.item) }
        ),

        // 10. 木工/家具
        OAK_LOG(
            // 橡木原木
            representative = Items.OAK_LOG,
            tags = listOf(C_CHESTS, ItemTags.SIGNS, ItemTags.HANGING_SIGNS),
            hardcodedItems = WOOD_MISC_ITEMS
        ),

        // 11. 魔法
        GLASS_BOTTLE(
            // 玻璃瓶
            representative = Items.GLASS_BOTTLE,
            extraMatcher = { stack -> MAGIC_MISC.contains(stack.item) }
        );

        companion object {
            private val BY_ITEM = entries.associateBy { it.representative }
            fun fromItem(item: Item): Category? = BY_ITEM[item]
        }
    }

    fun isMatch(filterItem: Item, stack: ItemStack): Boolean {
        val item = stack.item
        // 如果就是代表物品本身，直接匹配
        if (filterItem == item) return true

        val category = Category.fromItem(filterItem) ?: return false

        // 检查排除集（原苹果的黑名单逻辑）
        if (category.excludedItems.contains(item)) return false

        // 检查标签
        for (tag in category.tags) {
            if (stack.isIn(tag)) return true
        }

        // 检查硬编码集合
        if (category.hardcodedItems.contains(item)) return true

        // 检查额外匹配器
        if (category.extraMatcher(stack)) return true

        return false
    }

    private fun isFood(stack: ItemStack): Boolean = stack.contains(DataComponentTypes.FOOD)

    private fun createTag(namespace: String, path: String): TagKey<Item> {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(namespace, path))
    }
}
