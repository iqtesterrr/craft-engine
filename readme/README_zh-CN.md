<h1 align="center">
  <div style="text-align:center">
    <img src="https://github.com/user-attachments/assets/4e679094-303b-481d-859d-073efc61037c" alt="logo" style="width:100px; height:auto;">
  </div>
  CraftEngine
</h1>

<p align="center">
  <a href="https://momi.gtemc.cn/craftengine" alt="GitBook">
    <img src="https://img.shields.io/badge/%E6%96%87%E6%A1%A3-%E7%94%A8%E6%88%B7%E6%89%8B%E5%86%8C-D2691E" alt="Gitbook"/>
  </a>
  <a href="https://deepwiki.com/Xiao-MoMi/craft-engine">
    <img src="https://deepwiki.com/badge.svg" alt="询问DeepWiki">
  </a>
  <a href="https://github.com/Xiao-MoMi/craft-engine/">
    <img src="https://sloc.xyz/github/Xiao-MoMi/craft-engine/?category=codes" alt="SCC数量标识"/>
  </a>
</p>

<p align="center">
    <a target="_blank" href="/README.md">English</a> |
    <a target="_blank" href="/readme/README_zh-CN.md">简体中文</a> |
    <a target="_blank" href="/readme/README_zh-TW.md">繁體中文</a>
</p>

## 📌 关于 CraftEngine

CraftEngine 重新定义了 Minecraft 插件架构，作为下一代自定义内容实现的解决方案。通过 JVM 级别的注入，它提供了前所未有的性能、稳定性和可扩展性。该框架提供了一个代码优先的 API，用于注册原生集成的方块行为和物品交互逻辑。

## 构建
只要您安装了 JDK21，即可免费获取完整版 JAR。请按照以下指南进行构建。

### 🐚 命令行
+ 打开终端并切换到项目文件夹。
+ 执行 `./gradlew build`，构建产物将生成在 `/target` 文件夹中。

### 💻 IDE
+ 导入项目并执行 Gradle 构建操作。
+ 构建产物将生成在 `/target` 文件夹中。

## 安装

### 💻 环境要求
+ 确保您正在运行 [Paper](https://papermc.io/)（或其分支）1.20.1+ 服务器。CraftEngine 不支持 Spigot，且未来也不太可能支持。
+ 使用 JDK 21 来运行服务器。我相信这对你来说很简单。

### 🔍 安装方式
CraftEngine 提供了两种安装模式：标准安装和 Mod 模式。标准安装与传统插件安装方式相同，即将插件放入插件文件夹中。下面我们将详细介绍 Mod 模式的安装步骤。

### 🔧 安装服务端模组
- 下载最新的 [ignite.jar](https://github.com/vectrix-space/ignite/releases) 到服务器根目录
- 可以:
   - 将服务器 JAR 重命名为 `paper.jar` 并修改启动命令为: `-jar ignite.jar`
- 或者:
   - 使用高级启动参数
      - 对于 paper 或 folia: `-Dignite.locator=paper -Dignite.paper.jar=./server-xxx.jar -jar ignite.jar`
      - 对于特殊 Paper 分支 `-Dignite.locator=paper -Dignite.paper.target=cn.dreeam.leaper.QuantumLeaper -Dignite.paper.jar=./leaf-xxx.jar -jar ignite.jar`
- 启动服务器生成 `/mods` 目录
- 将最新的 [mod.jar](https://github.com/Xiao-MoMi/craft-engine/releases) 放入 `/mods` 目录
- 将插件 JAR 放入 `/plugins` 目录
- 最后执行两次重启:
   1. 首次重启以进行文件初始化
   2. 最后重启以激活所有组件

## 技术概述

### ⚙️ 方块
CraftEngine 使用运行时字节码生成技术，在服务器原生级别注册自定义方块，并结合客户端数据包修改以实现视觉同步。此架构提供了以下功能：

🧱 自定义原生方块
+ 动态注册方块，完全可控。
+ 物理属性：硬度、引燃几率、亮度等所有标准属性。
+ 自定义行为：通过 API 实现树苗、作物、下落的方块等。
+ 原版兼容性：完全保留原版方块机制（例如音符盒、绊线）。

📦 数据包集成
+ 定义自定义矿脉。
+ 生成自定义树木。
+ 配置自定义地形生成。

⚡ 性能优势
+ 比传统的 Bukkit 事件监听器更快、更稳定。
+ 策略性代码注入以最小化开销。

### 🥘 配方
CraftEngine 通过底层注入实现完全可定制的合成系统。与传统插件不同，它在处理 NBT 修改时不会失效，确保配方结果仅与唯一的物品标识符绑定。

### 🪑 家具
该插件使用核心实体来存储家具元数据，同时将碰撞实体和模块组件作为客户端数据包传输。此架构实现了显著的服务器端性能优化，同时支持通过多部分物品集成实现复合家具组装。

### 📝 模板
鉴于插件配置的广泛性和复杂性，CraftEngine 实现了模块化模板系统以分隔关键设置。这使得用户可以自定义配置格式，同时显著减少冗余的 YAML 定义。

### 🛠️ 模型
该插件通过配置实现模型继承和纹理覆盖，同时支持从 1.21.4 版本开始的[所有物品模型](https://misode.github.io/assets/item/)。它包含一个版本迁移系统，可以自动将 1.21.4+ 的物品模型降级为旧格式，以实现最大向后兼容性。

### 您必须了解的破坏性变更及可能与其他插件的不兼容性
- CraftEngine 注入 PalettedContainer 以确保插件方块数据的高效存储和同步。这可能会导致与一些直接修改调色盘的插件冲突。当使用 Spark 分析服务器性能时，调色盘操作开销将在分析结果中划归给 CraftEngine 插件。
- CraftEngine 注入 FurnaceBlockEntity 以修改其配方获取逻辑。
- CraftEngine 使用真服务端侧方块，任何依赖 Bukkit 的 Material 类的插件都将无法正确识别自定义方块类型。正确的方法是使用替代方案，如 BlockState#getBlock (mojmap) 而不是 Material 类。**(译者注: 对于不想直接使用nms的项目可以使用org.bukkit.block.Block#getBlockData来正确获取方块)**
- CraftEngine 通过继承某些 Minecraft 实体实现 0-tick 碰撞实体，确保硬碰撞在服务端侧正常工作（例如，让猪站在椅子上）。然而，一些反作弊插件在检测玩家移动时没有正确检查实体的 AABB（轴对齐包围盒），这可能导致误报。**(译者注: 还有可能是因为没有正确检查玩家接触的实体是否有硬碰撞箱导致的误报)**
- CraftEngine 的自定义配方处理可能与其他配方管理插件不完全兼容。

## 灵感来源
CraftEngine 从以下开源项目中汲取了灵感：
+ [Paper](https://github.com/PaperMC/Paper)
+ [LuckPerms](https://github.com/LuckPerms/LuckPerms)
+ [Fabric](https://github.com/FabricMC/fabric)
+ [packetevents](https://github.com/retrooper/packetevents)
+ [DataFixerUpper](https://github.com/Mojang/DataFixerUpper)
+ [ViaVersion](https://github.com/ViaVersion/ViaVersion)

### 核心依赖
CraftEngine 的实现依赖于以下基础库：
+ [ignite](https://github.com/vectrix-space/ignite)
+ [cloud-minecraft](https://github.com/Incendo/cloud-minecraft)
+ [rtag](https://github.com/saicone/rtag)
+ [adventure](https://github.com/KyoriPowered/adventure)
+ [byte-buddy](https://github.com/raphw/byte-buddy)

## 如何贡献

### 🔌 新功能与 Bug 修复
如果您提交的 PR 是关于 Bug 修复的，它很可能会被合并。如果您想提交新功能，请提前在 [Discord](https://discord.com/invite/WVKdaUPR3S) 上联系我。
您贡献的代码将遵循 GPLv3 许可证开源。如果您希望使用更宽松的许可证（例如 MIT），可以在文件顶部明确注明。

### 🌍 翻译
1. 克隆此仓库。
2. 在 `/common-files/src/main/resources/translations` 中创建一个新的语言文件。
3. 完成后，提交 **pull request** 以供审核。我们感谢您的贡献！

## Differences Between Versions
| 版本  | 官方支持 | 最大玩家数 | 开发版本 |
|-----|------|-------|------|
| 社区版 | ❌ 无  | 20    | ❌ 无  |
| 付费版 | ✔️ 有 | 无限制   | ✔️ 有 |

### 💖 支持开发者
如果您喜欢使用 CraftEngine，请考虑支持开发者！

- **Polymart**: [无]
- **BuiltByBit**: [无]
- **爱发电**: [通过爱发电支持](https://afdian.com/@xiaomomi/)

## CraftEngine API

```kotlin
repositories {
    maven("https://repo.momirealms.net/releases/")
    // 如果你的网络环境受限可以尝试下面的存储库地址
    // maven("https://repo-momi.gtemc.cn/releases/")
}
```
```kotlin
dependencies {
    compileOnly("net.momirealms:craft-engine-core:0.0.55")
    compileOnly("net.momirealms:craft-engine-bukkit:0.0.55")
}
```
