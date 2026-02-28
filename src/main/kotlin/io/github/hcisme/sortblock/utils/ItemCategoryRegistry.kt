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
    private val C_CROPS = createTag("c", "crops")
    private val C_SEEDS = createTag("c", "seeds")
    private val C_FOODS = createTag("c", "foods")
    private val C_INGOTS = createTag("c", "ingots")
    private val C_RAW_MATERIALS = createTag("c", "raw_materials")
    private val C_ORES = createTag("c", "ores")
    private val C_STORAGE_BLOCKS = createTag("c", "storage_blocks")
    private val CATEGORY_RULES = mutableMapOf<Item, List<TagKey<Item>>>()

    init {
        // --- 1. 🌾 农业与植物 ---
        // 代表物品：小麦 (Wheat)
        register(
            Items.WHEAT,
            C_CROPS,                // Fabric 通用作物 (如：胡萝卜、马铃薯、甜菜根、模组作物)
            C_SEEDS,                // Fabric 通用种子 (如：小麦种子、南瓜种子)
            ItemTags.VILLAGER_PLANTABLE_SEEDS, // 原版村民能播种的种子
            ItemTags.SAPLINGS,      // 所有树苗 (橡木、云杉、丛林树苗等)
            ItemTags.FLOWERS,       // 所有花朵 (包括两格高的高花和一格高的小花)
            ItemTags.LEAVES         // 所有树叶
        )

        // --- 2. 🍎 食物 ---
        // 代表物品：苹果 (Apple)
        register(
            Items.APPLE,
            C_FOODS,                // Fabric 通用食物标签 (包含肉类、面包、金苹果等)
            ItemTags.FISHES         // 所有鱼类 (生鱼、熟鱼、河豚等)
        )

        // --- 3. 💎 矿产与资源 ---
        // 代表物品：铁锭 (Iron Ingot)
        register(
            Items.IRON_INGOT,
            C_INGOTS,           // Fabric 通用锭 (铁锭、金锭、铜锭、模组锡锭等)
            C_RAW_MATERIALS,    // Fabric 通用粗矿 (粗铁、粗金、粗铜)
            C_ORES,             // Fabric 通用矿石块 (煤矿、铁矿、钻石矿等)
            // 原版信标支付物品 Tag，它完美涵盖了：铁锭、金锭、钻石、绿宝石、下界合金锭
            C_STORAGE_BLOCKS,   //  存储块 (这里就包含了铁块、金块、钻石块)
            ItemTags.BEACON_PAYMENT_ITEMS,
            ItemTags.COAL_ORES,     // 煤矿
            ItemTags.REDSTONE_ORES, // 红石矿
            ItemTags.LAPIS_ORES,    // 青金石矿
            ItemTags.DIAMOND_ORES,  // 钻石矿
            ItemTags.GOLD_ORES,     // 金矿
            ItemTags.IRON_ORES,     // 铁矿
            ItemTags.COPPER_ORES    // 铜矿
        )

        // --- 4. 🛡️ 装备与防具 ---
        // 代表物品：铁胸甲 (Iron Chestplate)
        register(
            Items.IRON_CHESTPLATE,
            ItemTags.TRIMMABLE_ARMOR, // 1.20+ 新标签，覆盖了几乎所有可纹饰的盔甲 (钻、铁、下界合金等)
            ItemTags.HEAD_ARMOR,      // 头盔类
            ItemTags.CHEST_ARMOR,     // 胸甲类
            ItemTags.LEG_ARMOR,       // 护腿类
            ItemTags.FOOT_ARMOR       // 靴子类
        )

        // --- 5. 🪓 工具与武器 ---
        // 代表物品：铁斧 (Iron Axe)
        register(
            Items.IRON_AXE,
            ItemTags.AXES,      // 斧头
            ItemTags.HOES,      // 锄头
            ItemTags.PICKAXES,  // 镐子
            ItemTags.SHOVELS,   // 铲子
            ItemTags.SWORDS,    // 剑
            ItemTags.SPEARS,
            ItemTags.TRIDENT_ENCHANTABLE,
            ItemTags.MACE_ENCHANTABLE
        )

        // --- 6. 🧱 建筑材料 ---
        // 代表物品：圆石 (Stone Bricks)
        register(
            Items.COBBLESTONE,
            ItemTags.STONE_BRICKS, // 石砖类
            ItemTags.LOGS,         // 所有原木
            ItemTags.PLANKS,       // 所有木板
            ItemTags.WOOL,         // 所有羊毛
            ItemTags.WALLS,        // 墙类 (圆石墙等)
            ItemTags.FENCES        // 栅栏类
        )

        // --- 7. 🚂 交通与运输 ---
        // 代表物品：矿车 (Minecart)
        register(
            Items.MINECART,
            ItemTags.RAILS,          // 所有轨道 (普通、动力、探测、激活铁轨)
            ItemTags.BOATS,          // 所有船 (橡木船、竹筏、运输船等)
            ItemTags.CHEST_BOATS,    // 带箱子的船
            // 以下 Tag 需要手动定义 (见下文辅助方法) 或直接引用 ItemTags
            createTag("c", "minecarts"), // 模组通用的矿车标签
            // 原版没有统一的“马具”标签，这里手动列出关键物品的 Tag
            // 如果想偷懒，可以只用 ItemTags.SADDLES (虽然原版可能没这个tag，建议用硬编码补充)
            // 但为了代码整洁，我们假设你后续会加一个 isHorseArmor() 的判断，或者用以下近似 Tag:
            ItemTags.TRIMMABLE_ARMOR // 马铠通常不在这里面，得靠硬编码
        )

        // --- 8. 🔴 红石与逻辑 ---
        // 代表物品：红石粉 (Redstone Dust)
        register(
            Items.REDSTONE,
            ItemTags.REDSTONE_ORES,   // 红石矿 (虽然矿产里有了，但这里也算逻辑通)
            ItemTags.BUTTONS,         // 所有按钮
            ItemTags.DOORS,           // 所有门
            ItemTags.TRAPDOORS,       // 所有活板门
            ItemTags.FENCE_GATES,     // 栅栏门
            // Fabric 通用标签
            createTag("c", "dusts"),  // 红石粉、萤石粉、火药
            // 原版红石元件没有统一 Tag，建议补充硬编码：
            // 比较器、中继器、侦测器、投掷器、发射器、漏斗、活塞、粘性活塞、红石块、红石火把、靶子
            // (见下文 isMatch 方法里的特殊处理)
        )

        // --- 9. 💀 怪物战利品 ---
        // 代表物品：腐肉 (Rotten Flesh)
        register(
            Items.ROTTEN_FLESH,
            createTag("c", "bones"),        // 骨头
            createTag("c", "rotten_flesh"), // 腐肉
            createTag("c", "slimeballs"),   // 史莱姆球
            createTag("c", "strings"),      // 线
            createTag("c", "spider_eyes"),  // 蜘蛛眼
            createTag("c", "gunpowder"),    // 火药
            createTag("c", "ender_pearls"), // 末影珍珠
            createTag("c", "blaze_rods"),   // 烈焰棒
            createTag("c", "ghast_tears"),  // 恶魂之泪
            createTag("c", "feathers"),     // 羽毛
            createTag("c", "leather")       // 皮革
        )
    }

    private fun register(representative: Item, vararg tags: TagKey<Item>) {
        CATEGORY_RULES[representative] = tags.toList()
    }

    fun isMatch(filterItem: Item, stack: ItemStack): Boolean {
        // 1. 精准匹配
        if (filterItem == stack.item) return true

        // 2. Tag 匹配
        val tags = CATEGORY_RULES[filterItem]
        if (tags != null) {
            for (tag in tags) {
                if (stack.isIn(tag)) {
                    return true
                }
            }
        }

        // 3. 特殊硬编码逻辑 (补全覆盖不到Tag)

        // A. 🍎 食物补充 (苹果代表食物，但不包含怪物掉落的“恶心食物”)
        if (filterItem == Items.APPLE) {
            // 1. 必须是食物
            if (isFood(stack)) {
                val item = stack.item
                // 2. 黑名单：排除腐肉、蜘蛛眼、河豚 (这些应该去怪物掉落或钓鱼箱)
                if (item != Items.ROTTEN_FLESH &&
                    item != Items.SPIDER_EYE &&
                    item != Items.PUFFERFISH) {
                    return true
                }
            }
        }

        // B. 🚂 交通补充 (矿车代表马铠、鞍)
        if (filterItem == Items.MINECART) {
            val item = stack.item

            // 2. 检查具体的物品 (鞍、钓竿等)
            if (item == Items.SADDLE ||
                item == Items.CARROT_ON_A_STICK ||
                item == Items.WARPED_FUNGUS_ON_A_STICK ||
                item == Items.ELYTRA // 鞘翅也可以算交通工具
            ) {
                return true
            }
        }

        // C. 🔴 红石补充 (红石代表所有红石元件)
        if (filterItem == Items.REDSTONE) {
            val item = stack.item
            // 常见红石元件
            if (item == Items.REPEATER || item == Items.COMPARATOR || item == Items.OBSERVER ||
                item == Items.DROPPER || item == Items.DISPENSER || item == Items.HOPPER ||
                item == Items.PISTON || item == Items.STICKY_PISTON || item == Items.REDSTONE_BLOCK ||
                item == Items.REDSTONE_TORCH || item == Items.TARGET || item == Items.LEVER ||
                item == Items.DAYLIGHT_DETECTOR || item == Items.SCULK_SENSOR || item == Items.TRIPWIRE_HOOK
            ) {
                return true
            }
        }

        return false
    }

    private fun isFood(stack: ItemStack): Boolean = stack.contains(DataComponentTypes.FOOD)

    private fun createTag(namespace: String, path: String): TagKey<Item> {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(namespace, path))
    }
}
