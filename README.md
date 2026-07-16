# EVM Data API

<p align="left">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4.7-6DB33F?logo=springboot&logoColor=white" alt="Spring Boot 3.4.7"/>
  <img src="https://img.shields.io/badge/MyBatis--Plus-3.5.12-blue" alt="MyBatis-Plus"/>
  <img src="https://img.shields.io/badge/Forest-1.7.2-41b883" alt="Forest"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow" alt="MIT License"/>
</p>

> 基于 Spring Boot 3 构建的 REST API 服务，聚合 **Euro Truck Simulator 2 / TruckersMP** 相关的游戏数据，提供玩家信息、实时地图、服务器状态、VTC 管理、DLC 信息与里程统计等开箱即用的接口。

## ✨ 功能特性

- 🎮 **玩家数据** — 玩家基础信息、行驶里程查询（支持批量）
- 🗺️ **实时地图** — 在线玩家位置、地图标点、玩家历史轨迹
- 🖥️ **服务器状态** — TMP 服务器列表与详情、历史在线记录
- 🚛 **VTC 管理** — 车队（Virtual Truck Company）信息与成员列表
- 🎫 **DLC 信息** — Steam DLC 数据查询与自动同步
- 📊 **里程统计** — 基于实时坐标的行驶里程计算与排行榜
- 🌐 **限流代理** — 内置可配置的限流代理拦截器，自动绕过外部 API 的请求频率限制
- 🤖 **AI 能力** — 封禁原因翻译（百度翻译 / OpenRouter）

## 🧱 技术栈

| 分类 | 选型 |
|------|------|
| 框架 | Spring Boot 3.4.7、Spring AOP、Spring Validation |
| ORM | MyBatis-Plus 3.5.12 |
| 数据库 | MySQL + Druid 连接池 |
| 缓存 | Redisson 3.50.0 |
| HTTP 客户端 | Forest 1.7.2（声明式 + 限流代理拦截器） |
| 工具集 | Hutool 5.8.38、Fastjson2、Lombok |
| 对象存储 | 阿里云 OSS |
| AI | LangChain4j 1.14.1（OpenAI 兼容接口） |
| 其他 | zt-exec、sensitive-word（敏感词过滤） |

## 📂 项目结构

```
src/main/java/link/vtcm
├── EvmDataApiApplication.java   # 启动类
├── controller/                  # REST 接口
├── service/                     # 业务逻辑接口
│   └── impl/                    # 业务实现
├── mapper/                      # MyBatis-Plus 数据访问层
├── domain/                      # 实体 / DTO / VO / Param
├── api/                         # 外部 API 声明式客户端（Forest）
├── config/                      # 配置类（异常、MyBatis、调度等）
├── common/                      # 工具、常量、异常、AOP
├── forest/                      # Forest 配置（限流代理拦截器）
├── schedule/                    # 定时同步任务
├── ai/                          # AI 助手
└── componet/                    # 组件
```

## 🚀 快速开始

### 环境要求

- **JDK 17** 及以上
- **Maven 3.6+**
- **MySQL 8.x**
- **Redis** 6.x 及以上

### 构建与运行

```bash
# 1. 克隆仓库
git clone https://github.com/<your-name>/evm-data-api.git
cd evm-data-api

# 2. 构建（跳过测试）
mvn clean package -Dmaven.test.skip=true -U -B

# 3. 方式一：Maven 直接运行
mvn spring-boot:run

# 3. 方式二：通过 jar 运行
java -jar target/evm-data-api.jar
```

启动成功后访问 `http://localhost:9100`，服务默认端口为 **9100**。

### 配置

配置文件位于 `src/main/resources/`：

| 文件 | 说明 |
|------|------|
| `application.yml` | 公共配置（服务端口、MyBatis-Plus、Forest 拦截器） |
| `application-example.yml` | 环境配置示例（数据源、Redis、OSS、AI、百度翻译、DepotDownloader、限流代理），所有敏感项均为 `<xxx>` 占位符，字段含义见文件内注释 |

使用步骤（外部配置方式，无需指定 profile）：

1. 参考 `application-example.yml` 的内容，准备一个外部的 `application.yml` 并填入你自己的真实值；
2. 将其放在 jar 同级目录（或 `config/` 子目录），Spring Boot 启动时会自动加载并覆盖 jar 内的默认配置。

> 也可通过启动参数显式指定外部配置路径：`--spring.config.additional-location=file:/path/to/config/`

> ⚠️ **安全提示**：`application-example.yml` 中均为占位符。请勿将含真实凭据的本地配置文件提交到仓库，建议将其加入 `.gitignore`。

> 💡 `depot-downloader.enabled` 默认为 `false`：关闭后「TMP 版本同步」任务仍会照常更新 TMP 版本与兼容游戏版本，仅跳过依赖 Steam 账号的实际游戏版本获取。

数据库表结构请根据 `domain/` 目录下的实体类（如 `Player`、`Server`、`OnlinePlayer`、`PlayerMileage` 等）自行创建。

## 📡 API 接口

所有接口统一返回 `R<T>` 包装结构，`code = 200` 表示成功：

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": { }
}
```

| 模块 | 方法 & 路径 | 参数 | 说明 |
|------|------------|------|------|
| 玩家 | `GET /player/info` | `tmpId` | 玩家基础信息 |
| 玩家 | `GET /player/mileage` | `tmpIdList`（逗号分隔） | 玩家行驶里程（批量） |
| 地图 | `GET /map/playerList` | — | 在线玩家列表 |
| 地图 | `GET /map/marker` | `MapMarkerParam` | 地图标点数据 |
| 地图 | `GET /map/playerHistory` | `MapPlayerHistoryParam` | 玩家历史轨迹 |
| 服务器 | `GET /server/list` | `ServerListParam` | 服务器列表 |
| 服务器 | `GET /server/info` | `serverId` | 服务器详情 |
| VTC | `GET /vtc/info` | `vtcId` | 车队信息 |
| VTC | `GET /vtc/memberAll` | `vtcId` | 车队成员列表 |
| DLC | `GET /dlc/list` | `DlcListParam` | DLC 列表 |
| 统计 | `GET /statistics/mileageRankingList` | `MileageRankingParam` | 里程排行榜 |
| 其他 | `GET /other/tmpVersion` | — | TMP 版本信息 |
| 其他 | `GET /other/translation` | `sceneType`, `text` | 文本翻译 |

所有接口默认开启跨域（`@CrossOrigin(originPatterns = "*")`）。

> ⚠️ **安全提示**：本项目未内置鉴权，所有接口对外开放。公网部署请自行通过网关、限流或鉴权中间件进行保护，避免背后的外部 API（百度翻译、OpenRouter、Steam 等）被恶意调用消耗。

## ⏰ 定时任务

| 任务类 | 职责 |
|--------|------|
| `OnlinePlayerSyncJob` | 高频同步在线玩家数据，计算实时行驶里程 |
| `ServerSyncJob` | 同步 TMP 服务器状态 |
| `ServerHistoryJob` | 记录服务器历史在线数据 |
| `SteamDlcSyncJob` | 同步 Steam DLC 信息 |
| `TmpFileSyncJob` | 同步 TMP 文件资源 |
| `TmpVersionSyncJob` | 同步 TMP 版本信息 |
| `DBJob` | 数据库维护任务 |

## 🔌 外部服务依赖

| 服务 | 用途 | 接口类 |
|------|------|--------|
| TruckersMP | 玩家 / VTC / 服务器 / 版本数据 | `TruckersMpApi`、`TruckersMpUpdateApi` |
| TruckersMap | 地图实时玩家位置 | `TruckersMapApi` |
| Steam | DLC / 应用详情 | `SteamAppDetailsApi` |
| 百度翻译 | 封禁原因翻译 | `BaiduTranslateApi` |
| 阿里云 OSS | 文件存储 | `AliyunOssConfig` |
| OpenRouter | AI 能力 | LangChain4j 配置 |

### 限流代理机制

调用 TruckersMP / TruckersMap 等外部 API 时容易触发频率限制。本项目通过 Forest 拦截器 `RateLimitProxyInterceptor` 配合 `@RateLimitProxy` 注解实现：

- **URL 前缀代理**（`URL_PREFIX`）— 在请求 URL 前拼接代理地址
- **路径代理**（`PATH_PROXY`）— 将请求路由到指定代理节点
- 支持按**限流组**、**时间周期**（`periodInSeconds`）、**请求次数**（`requests`）精细化控制

## 🤝 贡献

欢迎提交 Issue 与 Pull Request。提 PR 前请确保：

1. 代码风格与现有代码保持一致
2. 新增功能有相应的注释说明
3. 构建可通过 `mvn clean package -Dmaven.test.skip=true`

## 📄 开源协议

本项目基于 [MIT License](LICENSE) 开源，欢迎自由使用与二次开发。

## 🙏 致谢

- [TruckersMP](https://truckersmp.com/) — 多人模组
- [TruckersMP Map](https://map.truckersmp.com/) — 实时地图
- [Euro Truck Simulator 2](https://eurotrucksimulator2.com/) — SCS Software
