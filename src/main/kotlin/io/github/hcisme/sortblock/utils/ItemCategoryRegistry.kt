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
    private val C_ARMORS = createTag("c", "armors")  // 所有盔甲
    private val C_SPEARS = createTag("c", "spears")  // 矛类武器

    // 农业硬编码
    private val FARMING_HARD_CODE = setOf(
        Items.BONE_MEAL,
        Items.LILY_PAD,
        Items.MOSS_BLOCK,
        Items.MOSS_CARPET,
        Items.HONEYCOMB,
        Items.HONEYCOMB_BLOCK,
        Items.BEEHIVE,
        Items.BEE_NEST,
        Items.BAMBOO,
        Items.CRIMSON_FUNGUS,
        Items.WARPED_FUNGUS,
        Items.CRIMSON_ROOTS,
        Items.WARPED_ROOTS,
        Items.AZALEA,                     // 杜鹃花丛
        Items.FLOWERING_AZALEA,           // 盛开的杜鹃花丛
        Items.ROOTED_DIRT,                 // 缠根泥土
        Items.SPORE_BLOSSOM,                // 孢子花
        Items.SMALL_DRIPLEAF,               // 小型垂滴叶
        Items.BIG_DRIPLEAF,                  // 大型垂滴叶
        Items.KELP,                          // 海带
        Items.SEAGRASS,                      // 海草
        Items.SWEET_BERRIES                   // 甜浆果
    )

    // 食物硬编码
    private val FOOD_HARD_CODE = setOf(Items.EGG, Items.SUGAR, Items.CAKE)

    // 矿产资源相关硬编码
    private val MINERAL_HARD_CODE = setOf(
        Items.FLINT,
        Items.AMETHYST_SHARD,
        Items.QUARTZ,
        Items.NETHER_QUARTZ_ORE,
        Items.ANCIENT_DEBRIS,      // 远古残骸
        Items.NETHERITE_SCRAP,      // 下界合金碎片
        Items.NETHERITE_INGOT,      // 下界合金锭
        Items.NETHERITE_BLOCK,     // 下界合金块
        // 粗矿块
        Items.RAW_IRON_BLOCK,
        Items.RAW_GOLD_BLOCK,
        Items.RAW_COPPER_BLOCK
    )

    // 工具相关硬编码
    private val TOOL_HARD_CODE = setOf(
        Items.SHEARS, Items.FLINT_AND_STEEL, Items.FISHING_ROD, Items.NAME_TAG,
        Items.LEAD, Items.BUNDLE, Items.SHIELD, Items.BOW, Items.CROSSBOW,
        Items.TRIDENT, Items.ARROW, Items.SPECTRAL_ARROW, Items.TIPPED_ARROW,
        Items.MACE, Items.WIND_CHARGE, Items.BUCKET, Items.WATER_BUCKET,
        Items.LAVA_BUCKET, Items.MILK_BUCKET, Items.POWDER_SNOW_BUCKET,
        Items.AXOLOTL_BUCKET, Items.TADPOLE_BUCKET,
        // 矛
        Items.STONE_SPEAR,
        Items.IRON_SPEAR,
        Items.COPPER_SPEAR,
        Items.GOLDEN_SPEAR,
        Items.DIAMOND_SPEAR,
        Items.NETHERITE_SPEAR,
        Items.WOODEN_SPEAR,
        Items.BRUSH,                         // 刷子
        Items.FIRE_CHARGE,                    // 火焰弹（也可归红石）
        Items.DEBUG_STICK                      // 调试棒（可选）
    )

    // 建筑方块相关硬编码
    private val STONE_VARIANTS_HARD_CODE = setOf(
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
        Items.END_STONE_BRICKS, // 末地石砖
        Items.BRICKS,
        Items.BRICK_STAIRS,
        Items.BRICK_SLAB,
        Items.GLOWSTONE, // 萤石
        // 所有陶瓦
        Items.TERRACOTTA,
        Items.WHITE_TERRACOTTA,
        Items.ORANGE_TERRACOTTA,
        Items.MAGENTA_TERRACOTTA,
        Items.LIGHT_BLUE_TERRACOTTA,
        Items.YELLOW_TERRACOTTA,
        Items.LIME_TERRACOTTA,
        Items.PINK_TERRACOTTA,
        Items.GRAY_TERRACOTTA,
        Items.LIGHT_GRAY_TERRACOTTA,
        Items.CYAN_TERRACOTTA,
        Items.PURPLE_TERRACOTTA,
        Items.BLUE_TERRACOTTA,
        Items.BROWN_TERRACOTTA,
        Items.GREEN_TERRACOTTA,
        Items.RED_TERRACOTTA,
        Items.BLACK_TERRACOTTA,
        // 所有带釉陶瓦（新增）
        Items.WHITE_GLAZED_TERRACOTTA,
        Items.ORANGE_GLAZED_TERRACOTTA,
        Items.MAGENTA_GLAZED_TERRACOTTA,
        Items.LIGHT_BLUE_GLAZED_TERRACOTTA,
        Items.YELLOW_GLAZED_TERRACOTTA,
        Items.LIME_GLAZED_TERRACOTTA,
        Items.PINK_GLAZED_TERRACOTTA,
        Items.GRAY_GLAZED_TERRACOTTA,
        Items.LIGHT_GRAY_GLAZED_TERRACOTTA,
        Items.CYAN_GLAZED_TERRACOTTA,
        Items.PURPLE_GLAZED_TERRACOTTA,
        Items.BLUE_GLAZED_TERRACOTTA,
        Items.BROWN_GLAZED_TERRACOTTA,
        Items.GREEN_GLAZED_TERRACOTTA,
        Items.RED_GLAZED_TERRACOTTA,
        Items.BLACK_GLAZED_TERRACOTTA,
        // 所有混凝土块（16色）
        Items.WHITE_CONCRETE,
        Items.ORANGE_CONCRETE,
        Items.MAGENTA_CONCRETE,
        Items.LIGHT_BLUE_CONCRETE,
        Items.YELLOW_CONCRETE,
        Items.LIME_CONCRETE,
        Items.PINK_CONCRETE,
        Items.GRAY_CONCRETE,
        Items.LIGHT_GRAY_CONCRETE,
        Items.CYAN_CONCRETE,
        Items.PURPLE_CONCRETE,
        Items.BLUE_CONCRETE,
        Items.BROWN_CONCRETE,
        Items.GREEN_CONCRETE,
        Items.RED_CONCRETE,
        Items.BLACK_CONCRETE,
        // 所有混凝土粉末（16色）
        Items.WHITE_CONCRETE_POWDER,
        Items.ORANGE_CONCRETE_POWDER,
        Items.MAGENTA_CONCRETE_POWDER,
        Items.LIGHT_BLUE_CONCRETE_POWDER,
        Items.YELLOW_CONCRETE_POWDER,
        Items.LIME_CONCRETE_POWDER,
        Items.PINK_CONCRETE_POWDER,
        Items.GRAY_CONCRETE_POWDER,
        Items.LIGHT_GRAY_CONCRETE_POWDER,
        Items.CYAN_CONCRETE_POWDER,
        Items.PURPLE_CONCRETE_POWDER,
        Items.BLUE_CONCRETE_POWDER,
        Items.BROWN_CONCRETE_POWDER,
        Items.GREEN_CONCRETE_POWDER,
        Items.RED_CONCRETE_POWDER,
        Items.BLACK_CONCRETE_POWDER,
        // 海晶石系列（新增）
        Items.PRISMARINE,
        Items.PRISMARINE_BRICKS,
        Items.DARK_PRISMARINE,
        Items.PRISMARINE_STAIRS,
        Items.PRISMARINE_BRICK_STAIRS,
        Items.DARK_PRISMARINE_STAIRS,
        Items.PRISMARINE_SLAB,
        Items.PRISMARINE_BRICK_SLAB,
        Items.DARK_PRISMARINE_SLAB,
        // 紫珀块系列（新增）
        Items.PURPUR_BLOCK,
        Items.PURPUR_PILLAR,
        Items.PURPUR_STAIRS,
        Items.PURPUR_SLAB,
        // 石英系列（新增）
        Items.QUARTZ_BLOCK,
        Items.QUARTZ_BRICKS,
        Items.QUARTZ_PILLAR,
        Items.SMOOTH_QUARTZ,
        Items.QUARTZ_STAIRS,
        Items.SMOOTH_QUARTZ_STAIRS,
        Items.QUARTZ_SLAB,
        Items.SMOOTH_QUARTZ_SLAB,
        // 铜块系列（基础变种，氧化变种通过标签覆盖）
        Items.COPPER_BLOCK,
        Items.CUT_COPPER,
        Items.CUT_COPPER_STAIRS,
        Items.CUT_COPPER_SLAB,
        Items.CHISELED_COPPER,
        // 凝灰岩系列（新增，1.21）
        Items.TUFF_STAIRS,
        Items.TUFF_SLAB,
        Items.TUFF_WALL,
        Items.POLISHED_TUFF,
        Items.POLISHED_TUFF_STAIRS,
        Items.POLISHED_TUFF_SLAB,
        Items.POLISHED_TUFF_WALL,
        Items.TUFF_BRICKS,
        Items.TUFF_BRICK_STAIRS,
        Items.TUFF_BRICK_SLAB,
        Items.TUFF_BRICK_WALL,
        // 下界砖系列楼梯/台阶/墙（部分可能已被标签覆盖，但硬编码确保）
        Items.NETHER_BRICK_STAIRS,
        Items.NETHER_BRICK_SLAB,
        Items.NETHER_BRICK_WALL,
        Items.RED_NETHER_BRICK_STAIRS,
        Items.RED_NETHER_BRICK_SLAB,
        Items.RED_NETHER_BRICK_WALL,
        // 末地石砖墙
        Items.END_STONE_BRICK_WALL,
        // 深板岩系列楼梯/台阶/墙（部分可能已被标签覆盖）
        Items.COBBLED_DEEPSLATE_STAIRS,
        Items.COBBLED_DEEPSLATE_SLAB,
        Items.COBBLED_DEEPSLATE_WALL,
        Items.POLISHED_DEEPSLATE_STAIRS,
        Items.POLISHED_DEEPSLATE_SLAB,
        Items.POLISHED_DEEPSLATE_WALL,
        Items.DEEPSLATE_BRICK_STAIRS,
        Items.DEEPSLATE_BRICK_SLAB,
        Items.DEEPSLATE_BRICK_WALL,
        Items.DEEPSLATE_TILE_STAIRS,
        Items.DEEPSLATE_TILE_SLAB,
        Items.DEEPSLATE_TILE_WALL,
        // 黑石系列楼梯/台阶/墙
        Items.BLACKSTONE_STAIRS,
        Items.BLACKSTONE_SLAB,
        Items.BLACKSTONE_WALL,
        Items.POLISHED_BLACKSTONE_STAIRS,
        Items.POLISHED_BLACKSTONE_SLAB,
        Items.POLISHED_BLACKSTONE_WALL,
        Items.POLISHED_BLACKSTONE_BRICK_STAIRS,
        Items.POLISHED_BLACKSTONE_BRICK_SLAB,
        Items.POLISHED_BLACKSTONE_BRICK_WALL,
        // 其他杂项建筑方块
        Items.BONE_BLOCK,
        Items.COAL_BLOCK,
        // 基础石头及其变种的楼梯、台阶、墙
        Items.STONE_STAIRS,
        Items.STONE_SLAB,
        Items.GRANITE_STAIRS,
        Items.GRANITE_SLAB,
        Items.GRANITE_WALL,
        Items.DIORITE_STAIRS,
        Items.DIORITE_SLAB,
        Items.DIORITE_WALL,
        Items.ANDESITE_STAIRS,
        Items.ANDESITE_SLAB,
        Items.ANDESITE_WALL,
        // 砂岩及其变种（块、楼梯、台阶、墙）
        Items.SANDSTONE,
        Items.SANDSTONE_STAIRS,
        Items.SANDSTONE_SLAB,
        Items.SANDSTONE_WALL,
        Items.RED_SANDSTONE,
        Items.RED_SANDSTONE_STAIRS,
        Items.RED_SANDSTONE_SLAB,
        Items.RED_SANDSTONE_WALL,
        Items.SMOOTH_SANDSTONE,
        Items.SMOOTH_SANDSTONE_STAIRS,
        Items.SMOOTH_SANDSTONE_SLAB,
        Items.SMOOTH_RED_SANDSTONE,
        Items.SMOOTH_RED_SANDSTONE_STAIRS,
        Items.SMOOTH_RED_SANDSTONE_SLAB,
        Items.CUT_SANDSTONE,
        Items.CUT_SANDSTONE_SLAB,
        Items.CUT_RED_SANDSTONE,
        Items.CUT_RED_SANDSTONE_SLAB,
        Items.CHISELED_SANDSTONE,
        Items.CHISELED_RED_SANDSTONE,
        // 石砖及其变种（楼梯、台阶、墙）
        Items.STONE_BRICK_STAIRS,
        Items.STONE_BRICK_SLAB,
        Items.STONE_BRICK_WALL,
        Items.MOSSY_STONE_BRICK_STAIRS,
        Items.MOSSY_STONE_BRICK_SLAB,
        Items.MOSSY_STONE_BRICK_WALL,
        // 圆石与苔石（楼梯、台阶、墙）
        Items.COBBLESTONE_STAIRS,
        Items.COBBLESTONE_SLAB,
        Items.COBBLESTONE_WALL,
        Items.MOSSY_COBBLESTONE_STAIRS,
        Items.MOSSY_COBBLESTONE_SLAB,
        Items.MOSSY_COBBLESTONE_WALL,
        // 末地石砖楼梯、台阶
        Items.END_STONE_BRICK_STAIRS,
        Items.END_STONE_BRICK_SLAB,
        // 海晶石墙
        Items.PRISMARINE_WALL,
        // 砖块墙
        Items.BRICK_WALL,
        // 铁栏杆
        Items.IRON_BARS,
        // 石英雕纹块
        Items.CHISELED_QUARTZ_BLOCK
    )

    // 交通运输相关硬编码
    private val TRANSPORT_HARD_CODE = setOf(
        Items.CARROT_ON_A_STICK,
        Items.WARPED_FUNGUS_ON_A_STICK,
        Items.ELYTRA,
        Items.SADDLE, // 鞍
        Items.LEATHER_HORSE_ARMOR, // 皮革马铠
        Items.IRON_HORSE_ARMOR, // 铁马铠
        Items.GOLDEN_HORSE_ARMOR, // 金马铠
        Items.DIAMOND_HORSE_ARMOR, // 钻石马铠
        Items.WOLF_ARMOR, // 狼铠
        Items.COPPER_HORSE_ARMOR, // 铜马铠
        Items.NETHERITE_HORSE_ARMOR, // 下界合金马铠
        Items.CHEST_MINECART,
        Items.FURNACE_MINECART,
        Items.TNT_MINECART,
        Items.HOPPER_MINECART,
        Items.COMMAND_BLOCK_MINECART
    )

    // 红石元件相关硬编码
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
        Items.DEEPSLATE_REDSTONE_ORE, // 深层红石矿石
        Items.GUNPOWDER // 火药
    )

    // 怪物掉落相关硬编码
    private val MOB_DROPS_HARD_CODE = setOf(
        Items.RABBIT_HIDE,
        Items.PHANTOM_MEMBRANE     // 幻翼膜
    )

    // 木工/杂项相关硬编码
    private val WOOD_HARD_CODE = setOf(
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
        Items.BOOKSHELF, // 书架
        // 所有潜影盒
        Items.SHULKER_BOX,
        Items.WHITE_SHULKER_BOX,
        Items.ORANGE_SHULKER_BOX,
        Items.MAGENTA_SHULKER_BOX,
        Items.LIGHT_BLUE_SHULKER_BOX,
        Items.YELLOW_SHULKER_BOX,
        Items.LIME_SHULKER_BOX,
        Items.PINK_SHULKER_BOX,
        Items.GRAY_SHULKER_BOX,
        Items.LIGHT_GRAY_SHULKER_BOX,
        Items.CYAN_SHULKER_BOX,
        Items.PURPLE_SHULKER_BOX,
        Items.BLUE_SHULKER_BOX,
        Items.BROWN_SHULKER_BOX,
        Items.GREEN_SHULKER_BOX,
        Items.RED_SHULKER_BOX,
        Items.BLACK_SHULKER_BOX,
        // 所有木质楼梯
        Items.OAK_STAIRS,
        Items.SPRUCE_STAIRS,
        Items.BIRCH_STAIRS,
        Items.JUNGLE_STAIRS,
        Items.ACACIA_STAIRS,
        Items.DARK_OAK_STAIRS,
        Items.MANGROVE_STAIRS,
        Items.CHERRY_STAIRS,
        Items.BAMBOO_STAIRS,
        Items.BAMBOO_MOSAIC_STAIRS,
        Items.CRIMSON_STAIRS,
        Items.WARPED_STAIRS,
        // 所有木质台阶
        Items.OAK_SLAB,
        Items.SPRUCE_SLAB,
        Items.BIRCH_SLAB,
        Items.JUNGLE_SLAB,
        Items.ACACIA_SLAB,
        Items.DARK_OAK_SLAB,
        Items.MANGROVE_SLAB,
        Items.CHERRY_SLAB,
        Items.BAMBOO_SLAB,
        Items.BAMBOO_MOSAIC_SLAB,
        Items.CRIMSON_SLAB,
        Items.WARPED_SLAB,
        Items.CHISELED_BOOKSHELF
    )

    // 魔法相关硬编码
    private val MAGIC_HARD_CODE = setOf(
        Items.ENCHANTED_BOOK, Items.EXPERIENCE_BOTTLE, Items.LAPIS_LAZULI,
        Items.BREWING_STAND, Items.CAULDRON, Items.CRYING_OBSIDIAN, Items.ENDER_EYE,
        Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION
    )

    // 装备硬编码
    private val ARMOR_HARD_CODE = setOf(
        Items.CARVED_PUMPKIN,
        // 头颅类（可佩戴）
        Items.SKELETON_SKULL,
        Items.WITHER_SKELETON_SKULL,
        Items.ZOMBIE_HEAD,
        Items.PLAYER_HEAD,
        Items.CREEPER_HEAD,
        Items.PIGLIN_HEAD,
        Items.DRAGON_HEAD
    )

    // 分类枚举
    enum class Category(
        val representative: Item,
        val tags: List<TagKey<Item>> = emptyList(),
        val hardcodedItems: Set<Item> = emptySet(),
        // 用于提前排除（如苹果的黑名单）
        val excludedItems: Set<Item> = emptySet(),
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
            hardcodedItems = FARMING_HARD_CODE,
            excludedItems = setOf(Items.CARROT)
        ),

        // 2. 食物
        APPLE(
            // 苹果
            representative = Items.APPLE,
            tags = listOf(C_FOODS, ItemTags.FISHES),
            hardcodedItems = FOOD_HARD_CODE,
            excludedItems = setOf(
                Items.ROTTEN_FLESH, Items.SPIDER_EYE,
                Items.PUFFERFISH, Items.POISONOUS_POTATO
            )
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
            hardcodedItems = MINERAL_HARD_CODE
        ),

        // 4. 装备与防具
        IRON_CHESTPLATE(
            // 铁胸甲
            representative = Items.IRON_CHESTPLATE,
            tags = listOf(
                C_ARMORS,
                ItemTags.TRIMMABLE_ARMOR,
                ItemTags.HEAD_ARMOR,
                ItemTags.CHEST_ARMOR,
                ItemTags.LEG_ARMOR,
                ItemTags.FOOT_ARMOR
            ),
            hardcodedItems = ARMOR_HARD_CODE
        ),

        // 5. 工具
        IRON_AXE(
            // 铁斧
            representative = Items.IRON_AXE,
            tags = listOf(
                ItemTags.AXES, ItemTags.HOES, ItemTags.PICKAXES,
                ItemTags.SHOVELS, ItemTags.SWORDS, ItemTags.ARROWS,
                C_SPEARS
            ),
            hardcodedItems = TOOL_HARD_CODE
        ),

        // 6. 建筑材料
        COBBLESTONE(
            // 圆石
            representative = Items.COBBLESTONE,
            tags = listOf(
                ItemTags.STONE_BRICKS,
                ItemTags.WOOL,
                ItemTags.WOOL_CARPETS,
                ItemTags.WALLS,
                ItemTags.TERRACOTTA,
                ItemTags.DIRT,
                ItemTags.SAND,
                ItemTags.CANDLES,
                ItemTags.BEDS,
                C_GLASS,
                C_GLASS_PANES
            ),
            hardcodedItems = STONE_VARIANTS_HARD_CODE
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
            hardcodedItems = TRANSPORT_HARD_CODE
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
                createTag("c", "ender_pearls"),
                createTag("c", "blaze_rods"),
                createTag("c", "ghast_tears"),
                createTag("c", "feathers"),
                createTag("c", "leather")
            ),
            hardcodedItems = MOB_DROPS_HARD_CODE
        ),

        // 10. 木工/家具
        OAK_LOG(
            // 橡木原木
            representative = Items.OAK_LOG,
            tags = listOf(
                C_CHESTS,
                ItemTags.SIGNS,
                ItemTags.HANGING_SIGNS,
                ItemTags.LOGS,                       // 原木
                ItemTags.PLANKS,                      // 木板
                ItemTags.WOODEN_DOORS,                 // 木门
                ItemTags.WOODEN_TRAPDOORS,              // 木活板门
                ItemTags.WOODEN_PRESSURE_PLATES,         // 木压力板
                ItemTags.WOODEN_BUTTONS,                 // 木按钮
                ItemTags.WOODEN_FENCES,                  // 木栅栏
                ItemTags.FENCE_GATES                    // 栅栏门（全为木质）
            ),
            hardcodedItems = WOOD_HARD_CODE
        ),

        // 11. 魔法
        GLASS_BOTTLE(
            // 玻璃瓶
            representative = Items.GLASS_BOTTLE,
            hardcodedItems = MAGIC_HARD_CODE
        );

        companion object {
            private val BY_ITEM = entries.associateBy { it.representative }
            fun fromItem(item: Item): Category? = BY_ITEM[item]
        }
    }

    /**
     * 判断物品 [stack] 是否匹配过滤物品 [filterItem] 所代表的分类。
     * 匹配顺序：
     * 1. 如果物品就是代表物品本身 → true
     * 2. 如果代表物无对应分类 → false
     * 3. 如果物品在排除集中 → false
     * 4. 如果物品拥有分类的任意标签 → true
     * 5. 如果物品在硬编码集合中 → true
     * 6. 特殊处理：当过滤物品为苹果且物品可食用 → true
     */
    fun isMatch(filterItem: Item, stack: ItemStack): Boolean {
        val item = stack.item
        // 如果就是代表物品本身，直接匹配
        if (filterItem == item) return true

        val category = Category.fromItem(filterItem) ?: return false

        // 检查排除集（例如苹果分类的黑名单逻辑）
        if (category.excludedItems.contains(item)) return false

        // 检查标签
        for (tag in category.tags) {
            if (stack.isIn(tag)) return true
        }

        // 检查硬编码集合
        if (category.hardcodedItems.contains(item)) return true

        // 食物吃的特殊处理
        if (category == Category.APPLE && isFood(stack)) return true

        return false
    }

    private fun isFood(stack: ItemStack): Boolean = stack.contains(DataComponentTypes.FOOD)

    private fun createTag(namespace: String, path: String): TagKey<Item> {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(namespace, path))
    }
}
