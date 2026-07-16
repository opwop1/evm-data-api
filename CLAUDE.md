# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

EVM Data API — A Spring Boot 3 based REST API service providing interfaces for ETS2/TruckersMP game data. Main features include player info queries, map data, server status, VTC management, DLC info, and statistics.

## Build & Run

```bash
# Build (skip tests)
mvn clean package -Dmaven.test.skip=true -U -B

# Run
mvn spring-boot:run

# Run from jar
java -jar target/evm-data-api.jar
```

- Java 17, Spring Boot 3.4.7, Maven build
- Default port: 9100
- Profiles: `dev` / `prod` (switch via `spring.profiles.active`)

## Architecture

Standard Spring Boot layered architecture:

```
controller/   → REST endpoints (@CrossOrigin open to all origins)
service/      → Business logic interfaces
service/impl/ → Business logic implementations
mapper/       → MyBatis-Plus data access (maps to resources/mapper/*.xml)
domain/       → Entities, DTOs, VOs, request params (Param)
api/          → External API clients (Forest HTTP declarative calls)
config/       → Configuration classes (exception handling, MyBatis, OSS, scheduling, etc.)
common/       → Utilities, constants, exception definitions, AOP aspects
forest/       → Forest client config (rate-limit proxy interceptor)
schedule/     → Scheduled sync tasks
componet/     → Components (note: directory is misspelled as "componet")
```

### Core Conventions

- **Unified response**: All APIs return `R<T>` wrapper (`common/util/R.java`), code=200 for success
- **Exception handling**: `BaseException` base class + `GlobalExceptionHandler` global catch
- **ORM**: MyBatis-Plus, ID strategy `assign_id` (snowflake), column names wrapped in backticks
- **External HTTP**: Forest declarative client with `RateLimitProxyInterceptor` for rate-limited proxy
- **Cache**: Redisson Redis integration
- **Object mapping**: Lombok @Data for getters/setters

### External Service Integrations

| Service | Purpose | API Class |
|---------|---------|-----------|
| TruckersMP | Player/VTC/server data | `TruckersMpApi`, `TruckersMpUpdateApi` |
| Steam | DLC/app info | `SteamAppDetailsApi` |
| Baidu Translate | Ban reason translation | `BaiduTranslateApi` |
| TruckersMap | Map player data | `TruckersMapApi` |
| Aliyun OSS | File storage | `AliyunOssConfig` |
| OpenRouter | AI features | Spring AI starter |

### API Endpoints

Main routes: `/player`, `/map`, `/server`, `/vtc`, `/dlc`, `/statistics`, `/other`

### Scheduled Tasks

- `OnlinePlayerSyncJob` — Online player sync
- `ServerSyncJob` — Server status sync
- `SteamDlcSyncJob` — Steam DLC sync
- `TmpFileSyncJob` — TMP file sync
- `TmpVersionSyncJob` — TMP version sync
- `DBJob` — Database maintenance tasks
