# 📦 使用手册

## 简介
物品收集与分类设备。它不仅能自动吸取掉落在它上方的物品，还能通过扫描周围的容器，智能地将物品分发到对应的箱子中。

它最大的特点是支持“视觉化分类” ：你只需要用**物品展示框**或**告示牌**标记箱子，机器就会自动理解你的分类意图。

---

## 🛠️ 快速开始

版本说明（Java版本）

基于 Minecraft 1.21.11 版本设计，使用了Fabric API。


1.  **放置机器**：将智能归档单元放置在地面上。
2.  **布置存储区**：在机器周围放置箱子容器（支持范围看边框线条特效）。
3.  **标记箱子**：使用物品展示框或告示牌贴在箱子上，定义它们的用途（详见“分类规则”）。
4.  **投入物品**：将物品扔在机器**正上方** 机器会产生粒子光束，将物品传输到目标箱子。

---

## 📋 分类规则与优先级

机器会按照 **优先级 (Priority)** 从高到低尝试存入物品。

### 🥇 优先级 1：标签过滤器 (Filter Chest)
*   **如何设置**：在箱子上贴一个**物品展示框**，并在框内放入**指定的物品**（例如：苹果）。
*   **行为逻辑**：机器会检查掉落物是展示框内的物品分类。
    *   ✅ **是**：物品存入该箱子。
    *   ❌ **否**：跳过，寻找下一个箱子。
*   **用途**：用于存放大量特定资源（如矿物、农作物）。

### 🥈 优先级 2：自动学习单元 (Auto-Learning Chest)
*   **如何设置**：在箱子上贴一个**空的物品展示框**。
*   **行为逻辑**：
    1.  机器检测到展示框是空的。
    2.  机器会将当前处理的物品（例如：原木）存入箱子。
    3.  ✨ **自动贴标**：机器会自动从箱子里取出一个原木，贴在展示框上。
    4.  **结果**：该箱子从此升级为 **优先级 1 (专用过滤器)**，以后专门接收原木。
*   **用途**：无需手动设置过滤器，让系统自动根据流入的物品建立分类。

### 🥉 优先级 3：杂物/溢出箱 (Sundries/Dump Chest)
*   **如何设置**：在箱子上贴一个**告示牌**，并在告示牌的任意一行写上 `Sundries` (不区分大小写)。
*   **行为逻辑**：无视物品类型，只要箱子没满，什么都收。
*   **用途**：
    *   存放没有被归类的杂乱物品。

---

## 📏 参数

*   **有效工作范围**：
    *   以机器为中心。
    *   **X轴 (东西向)**: 30 格
    *   **Y轴 (高度)**: 5 格
    *   **Z轴 (南北向)**: 8 格
    *   *可自行修改*

---

# 物品分类系统文档

将 Minecraft 中的物品划分为多个逻辑分类，每个分类由一个**代表物品**标识，并通过**标签（Tag）**、**硬编码集合**和**自定义匹配逻辑**共同定义该分类包含哪些物品。分类用于物品筛选、整理或其他功能（如 JEI 分类、创造模式物品栏等）。

---

## 分类概览

| 分类 | 代表物品 | 说明 |
|------|----------|------|
| 农业与植物 | 小麦 (Wheat) | 农作物、种子、树苗、花朵、树叶等 |
| 食物 | 苹果 (Apple) | 所有可食用物品（含特殊排除） |
| 矿产与资源 | 铁锭 (Iron Ingot) | 矿物、锭、原矿、矿石块、粒等 |
| 装备与防具 | 铁胸甲 (Iron Chestplate) | 盔甲及可锻造装备 |
| 工具 | 铁斧 (Iron Axe) | 工具、武器、箭、桶等 |
| 建筑材料 | 圆石 (Cobblestone) | 各类建筑方块（石质、木质、玻璃等） |
| 交通 | 矿车 (Minecart) | 矿车、铁轨、船及相关物品 |
| 红石 | 红石粉 (Redstone) | 红石元件及红石矿石 |
| 怪物战利品 | 腐肉 (Rotten Flesh) | 怪物掉落物（包括皮革、羽毛等） |
| 木工/家具 | 橡木原木 (Oak Log) | 木质装饰、工作方块、家具等 |
| 魔法 | 玻璃瓶 (Glass Bottle) | 魔法相关物品（药水、附魔、酿造等） |

---

## 详细分类说明

### 1. 农业与植物
**代表物品**：`小麦` (Wheat)  
**关联标签**：
- `c:crops` (作物)
- `c:seeds` (种子)
- `villager_plantable_seeds` (村民可种植种子)
- `saplings` (树苗)
- `flowers` (花)
- `leaves` (树叶)
- `wart_blocks` (疣块)
- `c:dyes` (染料)

**额外匹配物品**（硬编码集合 `FARMING_MISC`）：
- 骨粉 (Bone Meal)
- 睡莲 (Lily Pad)
- 苔藓块 (Moss Block)
- 苔藓地毯 (Moss Carpet)
- 蜜脾 (Honeycomb)
- 蜜脾块 (Honeycomb Block)
- 蜂箱 (Beehive)
- 蜂巢 (Bee Nest)

### 2. 食物
**代表物品**：`苹果` (Apple)  
**关联标签**：
- `c:foods` (食物)
- `fishes` (鱼类)

**排除物品**（黑名单）：
- 腐肉 (Rotten Flesh)
- 蜘蛛眼 (Spider Eye)
- 河豚 (Pufferfish)
- 毒马铃薯 (Poisonous Potato)

**额外匹配物品**（`FOOD_MISC` + 所有带有 `food` 组件的物品）：
- 鸡蛋 (Egg)
- 糖 (Sugar)
- 蛋糕 (Cake)

### 3. 矿产与资源
**代表物品**：`铁锭` (Iron Ingot)  
**关联标签**：
- `c:ingots` (锭)
- `c:raw_materials` (原材料)
- `c:ores` (矿石)
- `c:storage_blocks` (存储方块)
- `c:nuggets` (粒)
- `beacon_payment_items` (信标支付物品)
- `coal_ores` (煤矿石)
- `redstone_ores` (红石矿石)
- `lapis_ores` (青金石矿石)
- `diamond_ores` (钻石矿石)
- `gold_ores` (金矿石)
- `iron_ores` (铁矿石)
- `copper_ores` (铜矿石)

**额外匹配物品**（`MINERAL_MISC`）：
- 燧石 (Flint)
- 墨囊 (Ink Sac)
- 荧光墨囊 (Glow Ink Sac)
- 紫水晶碎片 (Amethyst Shard)
- 下界石英 (Quartz)
- 下界石英矿石 (Nether Quartz Ore)

### 4. 装备与防具
**代表物品**：`铁胸甲` (Iron Chestplate)  
**关联标签**：
- `trimmable_armor` (可锻造盔甲)
- `head_armor` (头盔)
- `chest_armor` (胸甲)
- `leg_armor` (护腿)
- `foot_armor` (靴子)

无额外硬编码物品（由标签覆盖）。

### 5. 工具
**代表物品**：`铁斧` (Iron Axe)  
**关联标签**：
- `axes` (斧)
- `hoes` (锄)
- `pickaxes` (镐)
- `shovels` (锹)
- `swords` (剑)
- `arrows` (箭)

**额外匹配物品**（`TOOL_MISC`）：
- 剪刀 (Shears)
- 打火石 (Flint and Steel)
- 钓鱼竿 (Fishing Rod)
- 命名牌 (Name Tag)
- 拴绳 (Lead)
- 收纳袋 (Bundle)
- 盾牌 (Shield)
- 弓 (Bow)
- 弩 (Crossbow)
- 三叉戟 (Trident)
- 箭 (Arrow)
- 光灵箭 (Spectral Arrow)
- 药箭 (Tipped Arrow)
- 重锤 (Mace)
- 风弹 (Wind Charge)
- 桶 (Bucket)
- 水桶 (Water Bucket)
- 熔岩桶 (Lava Bucket)
- 奶桶 (Milk Bucket)
- 细雪桶 (Powder Snow Bucket)
- 美西螈桶 (Axolotl Bucket)
- 蝌蚪桶 (Tadpole Bucket)

### 6. 建筑材料
**代表物品**：`圆石` (Cobblestone)  
**关联标签**：
- `stone_bricks` (石砖)
- `wool` (羊毛)
- `wool_carpets` (羊毛地毯)
- `logs` (原木)
- `planks` (木板)
- `stairs` (楼梯)
- `slabs` (台阶)
- `walls` (墙)
- `fences` (栅栏)
- `fence_gates` (栅栏门)
- `terracotta` (陶瓦)
- `dirt` (泥土)
- `sand` (沙)
- `candles` (蜡烛)
- `beds` (床)
- `c:glass_blocks` (玻璃块)
- `c:glass_panes` (玻璃板)

**硬编码集合** `STONE_VARIANTS`（所有石质变种）：
- 石头 (Stone)
- 花岗岩 (Granite)
- 磨制花岗岩 (Polished Granite)
- 闪长岩 (Diorite)
- 磨制闪长岩 (Polished Diorite)
- 安山岩 (Andesite)
- 磨制安山岩 (Polished Andesite)
- 深板岩 (Deepslate)
- 深板岩圆石 (Cobbled Deepslate)
- 磨制深板岩 (Polished Deepslate)
- 凝灰岩 (Tuff)
- 方解石 (Calcite)
- 玄武岩 (Basalt)
- 磨制玄武岩 (Polished Basalt)
- 平滑玄武岩 (Smooth Basalt)
- 下界岩 (Netherrack)
- 黑石 (Blackstone)
- 磨制黑石 (Polished Blackstone)
- 镶金黑石 (Gilded Blackstone)
- 滴水石块 (Dripstone Block)
- 滴水石锥 (Pointed Dripstone)
- 紫水晶块 (Amethyst Block)
- 紫水晶簇 (Amethyst Cluster)
- 平滑石头 (Smooth Stone)
- 灵魂沙 (Soul Sand)
- 下界砖块 (Nether Bricks)
- 苔石 (Mossy Cobblestone)
- 冰 (Ice)
- 浮冰 (Packed Ice)
- 蓝冰 (Blue Ice)
- 末地石 (End Stone)
- 菌光体 (Shroomlight)
- 砂砾 (Gravel)
- 灵魂土 (Soul Soil)
- 圆石 (Cobblestone)
- 石砖 (Stone Bricks)
- 苔石砖 (Mossy Stone Bricks)
- 裂纹石砖 (Cracked Stone Bricks)
- 雕纹石砖 (Chiseled Stone Bricks)
- 深板岩砖 (Deepslate Bricks)
- 裂纹深板岩砖 (Cracked Deepslate Bricks)
- 深板岩瓦 (Deepslate Tiles)
- 裂纹深板岩瓦 (Cracked Deepslate Tiles)
- 雕纹深板岩 (Chiseled Deepslate)
- 磨制黑石砖 (Polished Blackstone Bricks)
- 裂纹磨制黑石砖 (Cracked Polished Blackstone Bricks)
- 雕纹磨制黑石 (Chiseled Polished Blackstone)
- 红色下界砖块 (Red Nether Bricks)
- 末地石砖 (End Stone Bricks)

**额外固定物品**：
- 砖块 (Bricks)
- 砖楼梯 (Brick Stairs)
- 砖台阶 (Brick Slab)

### 7. 交通
**代表物品**：`矿车` (Minecart)  
**关联标签**：
- `rails` (铁轨)
- `boats` (船)
- `chest_boats` (运输船)
- `c:minecarts` (矿车)

**额外匹配物品**（`TRANSPORT_MISC`，包含 `ANIMAL_EQUIPMENT` + 以下物品）：
- 鞍 (Saddle)
- 皮革马铠 (Leather Horse Armor)
- 铁马铠 (Iron Horse Armor)
- 金马铠 (Golden Horse Armor)
- 钻石马铠 (Diamond Horse Armor)
- 狼铠 (Wolf Armor)
- 铜马铠 (Copper Horse Armor)
- 下界合金马铠 (Netherite Horse Armor)
- 胡萝卜钓竿 (Carrot on a Stick)
- 诡异菌钓竿 (Warped Fungus on a Stick)
- 鞘翅 (Elytra)
- 皮革 (Leather)

### 8. 红石
**代表物品**：`红石粉` (Redstone)  
**关联标签**：
- `redstone_ores` (红石矿石)
- `buttons` (按钮)
- `doors` (门)
- `trapdoors` (活板门)
- `c:dusts` (粉末)

**硬编码集合** `REDSTONE_COMPONENTS`：
- 红石中继器 (Repeater)
- 红石比较器 (Comparator)
- 侦测器 (Observer)
- 投掷器 (Dropper)
- 发射器 (Dispenser)
- 漏斗 (Hopper)
- 活塞 (Piston)
- 粘性活塞 (Sticky Piston)
- 红石块 (Redstone Block)
- 红石火把 (Redstone Torch)
- 标靶 (Target)
- 拉杆 (Lever)
- 避雷针 (Lightning Rod)
- 阳光探测器 (Daylight Detector)
- 幽匿感测体 (Sculk Sensor)
- 校频幽匿感测体 (Calibrated Sculk Sensor)
- 绊线钩 (Tripwire Hook)
- 讲台 (Lectern)
- 石质压力板 (Stone Pressure Plate)
- 橡木压力板 (Oak Pressure Plate)
- 云杉木压力板 (Spruce Pressure Plate)
- 白桦木压力板 (Birch Pressure Plate)
- 从林木压力板 (Jungle Pressure Plate)
- 金合欢木压力板 (Acacia Pressure Plate)
- 深色橡木压力板 (Dark Oak Pressure Plate)
- 红树木压力板 (Mangrove Pressure Plate)
- 樱花木压力板 (Cherry Pressure Plate)
- 竹压力板 (Bamboo Pressure Plate)
- 绯红木压力板 (Crimson Pressure Plate)
- 诡异木压力板 (Warped Pressure Plate)
- 磨制黑石压力板 (Polished Blackstone Pressure Plate)
- 重质测重压力板 (Heavy Weighted Pressure Plate)
- 轻质测重压力板 (Light Weighted Pressure Plate)
- 石按钮 (Stone Button)
- 橡木按钮 (Oak Button)
- 云杉木按钮 (Spruce Button)
- 白桦木按钮 (Birch Button)
- 从林木按钮 (Jungle Button)
- 金合欢木按钮 (Acacia Button)
- 深色橡木按钮 (Dark Oak Button)
- 红树木按钮 (Mangrove Button)
- 樱花木按钮 (Cherry Button)
- 竹按钮 (Bamboo Button)
- 绯红木按钮 (Crimson Button)
- 诡异木按钮 (Warped Button)
- 磨制黑石按钮 (Polished Blackstone Button)
- 红石灯 (Redstone Lamp)
- 音符盒 (Note Block)
- 陷阱箱 (Trapped Chest)
- 动力铁轨 (Powered Rail)
- 探测铁轨 (Detector Rail)
- 激活铁轨 (Activator Rail)
- 红石矿石 (Redstone Ore)
- 深层红石矿石 (Deepslate Redstone Ore)

### 9. 怪物战利品
**代表物品**：`腐肉` (Rotten Flesh)  
**关联标签**：
- `c:bones` (骨头)
- `c:rotten_flesh` (腐肉)
- `c:slimeballs` (粘液球)
- `c:strings` (线)
- `c:spider_eyes` (蜘蛛眼)
- `c:gunpowder` (火药)
- `c:ender_pearls` (末影珍珠)
- `c:blaze_rods` (烈焰棒)
- `c:ghast_tears` (恶魂之泪)
- `c:feathers` (羽毛)
- `c:leather` (皮革)

**额外匹配物品**（`MOB_DROPS_MISC`）：
- 兔子皮 (Rabbit Hide)
- 雪球 (Snowball)

### 10. 木工/家具
**代表物品**：`橡木原木` (Oak Log)  
**关联标签**：
- `c:chests` (箱子)
- `signs` (告示牌)
- `hanging_signs` (悬挂式告示牌)

**硬编码集合** `WOOD_MISC_ITEMS`：
- 营火 (Campfire)
- 灵魂营火 (Soul Campfire)
- 梯子 (Ladder)
- 火把 (Torch)
- 灵魂火把 (Soul Torch)
- 灯笼 (Lantern)
- 灵魂灯笼 (Soul Lantern)
- 物品展示框 (Item Frame)
- 荧光物品展示框 (Glow Item Frame)
- 画 (Painting)
- 盔甲架 (Armor Stand)
- 碗 (Bowl)
- 木棍 (Stick)
- 纸 (Paper)
- 书 (Book)
- 书与笔 (Writable Book)
- 写好的书 (Written Book)
- 花盆 (Flower Pot)
- 堆肥桶 (Composter)
- 木桶 (Barrel)
- 工作台 (Crafting Table)
- 箱子 (Chest)
- 脚手架 (Scaffolding)
- 织布机 (Loom)
- 制图台 (Cartography Table)
- 制箭台 (Fletching Table)
- 锻造台 (Smithing Table)
- 砂轮 (Grindstone)
- 蜂箱 (Beehive)
- 书架 (Bookshelf)

### 11. 魔法
**代表物品**：`玻璃瓶` (Glass Bottle)  
**关联标签**：无（由额外匹配器处理）

**额外匹配物品**（`MAGIC_MISC`）：
- 附魔书 (Enchanted Book)
- 经验瓶 (Experience Bottle)
- 青金石 (Lapis Lazuli)
- 酿造台 (Brewing Stand)
- 炼药锅 (Cauldron)
- 哭泣的黑曜石 (Crying Obsidian)
- 末影之眼 (Ender Eye)
- 药水 (Potion)
- 喷溅药水 (Splash Potion)
- 滞留药水 (Lingering Potion)

---

## 匹配逻辑说明

对于给定的过滤物品（代表物）和待检测物品，系统按以下顺序判断是否匹配：

1. 如果两者相同 → 匹配。
2. 如果代表物没有对应分类 → 不匹配。
3. 如果待检测物品在代表物分类的**排除列表**中 → 不匹配。
4. 如果待检测物品包含分类关联的任意**标签** → 匹配。
5. 如果待检测物品在分类的**硬编码集合**中 → 匹配。
6. 如果分类的**额外匹配器**返回 `true` → 匹配。
7. 否则不匹配。

此设计确保了分类的灵活性和可扩展性，既利用了原版的标签系统，又通过硬编码集合和自定义逻辑补充了标签未覆盖的物品。

---

文档最后更新：2026年3月2日