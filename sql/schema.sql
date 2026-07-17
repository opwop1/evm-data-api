-- ============================================================
-- EVM Data API 数据库表结构初始化脚本
-- 适用: MySQL 8.0+, 字符集 utf8mb4
-- 说明: 仅包含表结构定义,不含业务数据
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for api_logs
-- ----------------------------
DROP TABLE IF EXISTS `api_logs`;
CREATE TABLE `api_logs`  (
  `id` bigint NOT NULL,
  `request_time` datetime NOT NULL COMMENT '请求时间',
  `request_method` varchar(10) NOT NULL COMMENT '请求方法',
  `full_url` varchar(255) NOT NULL COMMENT '完整请求路径',
  `url` varchar(255) NOT NULL COMMENT '请求路径',
  `client_ip` varchar(32) NOT NULL COMMENT '客户端IP',
  `param` varchar(5000) NULL DEFAULT NULL COMMENT '请求参数',
  `response_time` int NOT NULL DEFAULT 0 COMMENT '响应耗时 (毫秒)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `request_time`(`request_time` ASC) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for game_online_player
-- ----------------------------
DROP TABLE IF EXISTS `game_online_player`;
CREATE TABLE `game_online_player`  (
  `tmp_id` int NULL DEFAULT NULL COMMENT 'TMP编号',
  `tmp_name` varchar(50) NULL DEFAULT NULL COMMENT 'TMP名称',
  `server_player_id` int NULL DEFAULT NULL COMMENT '服务器玩家ID',
  `server_id` int NULL DEFAULT NULL COMMENT '服务器ID',
  `axis_x` int NULL DEFAULT NULL COMMENT '坐标轴X',
  `axis_y` int NULL DEFAULT NULL COMMENT '坐标轴Y',
  `heading` decimal(3, 2) NULL DEFAULT NULL COMMENT '航向',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  INDEX `axis`(`axis_x`, `axis_y`) USING BTREE,
  INDEX `tmpId`(`tmp_id`) USING HASH
) ENGINE = MEMORY COMMENT = '在线玩家' ROW_FORMAT = Fixed;

-- ----------------------------
-- Table structure for game_online_player_history
-- ----------------------------
DROP TABLE IF EXISTS `game_online_player_history`;
CREATE TABLE `game_online_player_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tmp_id` int NULL DEFAULT NULL COMMENT 'TMP编号',
  `server_id` int NULL DEFAULT NULL COMMENT '服务器ID',
  `axis_x` int NULL DEFAULT NULL COMMENT '坐标轴X',
  `axis_y` int NULL DEFAULT NULL COMMENT '坐标轴Y',
  `heading` decimal(3, 2) NULL DEFAULT NULL COMMENT '航向',
  `distance` int NULL DEFAULT NULL COMMENT '行驶里程 (米)',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`, `update_time`) USING BTREE,
  INDEX `tmpId`(`tmp_id` ASC) USING BTREE,
  INDEX `update_time`(`update_time` ASC) USING BTREE,
  INDEX `server_id`(`server_id` ASC) USING BTREE,
  INDEX `axis`(`axis_x` ASC, `axis_y` ASC) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic
-- 分区说明: 按 update_time 按天 RANGE 分区,由 DBJob 每天 00:00 自动创建次日分区。
-- 此处预建 14 天分区(以 2026-07-17 为基准),部署后由 DBJob 滚动续建;
-- 若部署日期已超出此范围,请按"每日建次日分区"的规则补建后再启动。
PARTITION BY RANGE COLUMNS(`update_time`) (
  PARTITION `p20260717` VALUES LESS THAN ('2026-07-18 00:00:00'),
  PARTITION `p20260718` VALUES LESS THAN ('2026-07-19 00:00:00'),
  PARTITION `p20260719` VALUES LESS THAN ('2026-07-20 00:00:00'),
  PARTITION `p20260720` VALUES LESS THAN ('2026-07-21 00:00:00'),
  PARTITION `p20260721` VALUES LESS THAN ('2026-07-22 00:00:00'),
  PARTITION `p20260722` VALUES LESS THAN ('2026-07-23 00:00:00'),
  PARTITION `p20260723` VALUES LESS THAN ('2026-07-24 00:00:00'),
  PARTITION `p20260724` VALUES LESS THAN ('2026-07-25 00:00:00'),
  PARTITION `p20260725` VALUES LESS THAN ('2026-07-26 00:00:00'),
  PARTITION `p20260726` VALUES LESS THAN ('2026-07-27 00:00:00'),
  PARTITION `p20260727` VALUES LESS THAN ('2026-07-28 00:00:00'),
  PARTITION `p20260728` VALUES LESS THAN ('2026-07-29 00:00:00'),
  PARTITION `p20260729` VALUES LESS THAN ('2026-07-30 00:00:00'),
  PARTITION `p20260730` VALUES LESS THAN ('2026-07-31 00:00:00')
);

-- ----------------------------
-- Table structure for game_player
-- ----------------------------
DROP TABLE IF EXISTS `game_player`;
CREATE TABLE `game_player`  (
  `tmp_id` int NOT NULL COMMENT 'TMP编号',
  `name` varchar(100) NULL DEFAULT NULL COMMENT '玩家名称',
  `avatar_url` varchar(255) NULL DEFAULT NULL COMMENT '头像地址',
  `small_avatar_url` varchar(255) NULL DEFAULT NULL COMMENT '小尺寸头像地址',
  `register_time` datetime NULL DEFAULT NULL COMMENT '注册时间',
  `steam_id` varchar(30) NULL DEFAULT NULL COMMENT 'Steam ID',
  `group_name` varchar(50) NULL DEFAULT NULL COMMENT '用户组名称',
  `group_color` varchar(6) NULL DEFAULT NULL COMMENT '用户组颜色，16进制值',
  `is_join_vtc` tinyint(1) NULL DEFAULT NULL COMMENT '是否加入VTC (1:是; 0:否; )',
  `vtc_member_id` int NULL DEFAULT NULL COMMENT 'VTC成员ID',
  `vtc_id` int NULL DEFAULT NULL COMMENT 'VTC编号',
  `vtc_name` varchar(100) NULL DEFAULT NULL COMMENT 'VTC名称',
  `vtc_role` varchar(100) NULL DEFAULT NULL COMMENT 'VTC角色',
  `vtc_join_time` datetime NULL DEFAULT NULL COMMENT 'VTC加入时间',
  `is_ban` tinyint(1) NULL DEFAULT 0 COMMENT '是否封禁 (1:是; 0:否; )',
  `ban_until` datetime NULL DEFAULT NULL COMMENT '封禁截止时间',
  `ban_count` int NULL DEFAULT 0 COMMENT '封禁次数',
  `ban_reason` varchar(255) NULL DEFAULT NULL COMMENT '封禁原因',
  `ban_reason_zh` varchar(255) NULL DEFAULT NULL COMMENT '封禁原因，中文',
  `ban_hide` tinyint(1) NULL DEFAULT 0 COMMENT '隐藏封禁信息 (1:是; 0:否; )',
  `is_sponsor` tinyint(1) NULL DEFAULT 0 COMMENT '是否赞助 (1:是; 0:否; )',
  `sponsor_amount` int NULL DEFAULT NULL COMMENT '赞助金额 (美分)',
  `sponsor_cumulative_amount` int NULL DEFAULT NULL COMMENT '累计赞助金额 (美分)',
  `sponsor_level_color` varchar(6) NULL DEFAULT NULL COMMENT '赞助级别颜色',
  `sponsor_hide` tinyint(1) NULL DEFAULT NULL COMMENT '隐藏赞助信息 (1:是; 0:否; )',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`tmp_id`) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for game_player_mileage
-- ----------------------------
DROP TABLE IF EXISTS `game_player_mileage`;
CREATE TABLE `game_player_mileage`  (
  `id` bigint NOT NULL,
  `tmp_id` int NOT NULL COMMENT 'TMP编号',
  `tmp_name` varchar(100) NOT NULL COMMENT '玩家名称',
  `server_id` int NULL DEFAULT NULL COMMENT '服务器ID',
  `axis_x` int NULL DEFAULT NULL COMMENT '坐标轴X',
  `axis_y` int NULL DEFAULT NULL COMMENT '坐标轴Y',
  `heading` decimal(3, 2) NULL DEFAULT NULL COMMENT '航向',
  `distance` bigint NOT NULL DEFAULT 0 COMMENT '总里程 (米)',
  `today_distance` bigint NOT NULL DEFAULT 0 COMMENT '当日里程 (米)',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `tmp_id`(`tmp_id` ASC) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for game_player_vtc_history
-- ----------------------------
DROP TABLE IF EXISTS `game_player_vtc_history`;
CREATE TABLE `game_player_vtc_history`  (
  `tmp_id` int NOT NULL COMMENT 'TMP编号',
  `vtc_id` int NOT NULL COMMENT 'VTC编号',
  `vtc_name` varchar(100) NULL DEFAULT NULL COMMENT 'VTC名称',
  `join_date` date NULL DEFAULT NULL COMMENT '加入日期',
  `quit_date` date NULL DEFAULT NULL COMMENT '退出日期',
  INDEX `base`(`tmp_id` ASC, `vtc_id` ASC) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for game_server
-- ----------------------------
DROP TABLE IF EXISTS `game_server`;
CREATE TABLE `game_server`  (
  `server_id` int NOT NULL,
  `server_ip` varchar(15) NULL DEFAULT NULL COMMENT 'IP',
  `server_port` int NULL DEFAULT NULL COMMENT '端口',
  `server_name` varchar(100) NULL DEFAULT NULL COMMENT '名称',
  `short_name` varchar(50) NULL DEFAULT NULL COMMENT '短名称',
  `is_online` tinyint(1) NULL DEFAULT NULL COMMENT '是否在线 (1:是; 0:否; )',
  `player_count` int NOT NULL DEFAULT 0 COMMENT '玩家数量',
  `queue_count` int NOT NULL DEFAULT 0 COMMENT '排队玩家数量',
  `max_player` int NOT NULL DEFAULT 0 COMMENT '最大玩家数量',
  `speed_limiter_enable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '开启限速 (1:是; 0:否; )',
  `collisions_enable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '开启碰撞 (1:是; 0:否; )',
  `police_car_enable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '开启警车 (1:是; 0:否; )',
  `afk_enable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '开启挂机检测 (1:是; 0:否; )',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`server_id`) USING HASH
) ENGINE = MEMORY COMMENT = '服务器' ROW_FORMAT = Fixed;

-- ----------------------------
-- Table structure for game_server_history
-- ----------------------------
DROP TABLE IF EXISTS `game_server_history`;
CREATE TABLE `game_server_history`  (
  `id` bigint NOT NULL,
  `server_id` int NOT NULL COMMENT '服务器 ID',
  `player_count` int NOT NULL DEFAULT 0 COMMENT '玩家数量',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `server_id`(`server_id` ASC) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for game_steam_dlc
-- ----------------------------
DROP TABLE IF EXISTS `game_steam_dlc`;
CREATE TABLE `game_steam_dlc`  (
  `id` bigint NOT NULL,
  `app_id` int NOT NULL COMMENT 'Steam App Id',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `type` tinyint NULL DEFAULT NULL COMMENT '类型',
  `desc` text NULL COMMENT '说明',
  `header_image_url` varchar(255) NULL DEFAULT NULL COMMENT '标题图片地址',
  `background_image_url` varchar(255) NULL DEFAULT NULL COMMENT '背景图片地址',
  `original_price` int NOT NULL DEFAULT 0 COMMENT '原价 (分)',
  `final_price` int NOT NULL DEFAULT 0 COMMENT '现价 (分)',
  `discount` int NOT NULL DEFAULT 0 COMMENT '折扣百分比',
  `internal_id` varchar(50) NULL DEFAULT NULL COMMENT '内部代号',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `app_id`(`app_id` ASC) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for game_tmp_file
-- ----------------------------
DROP TABLE IF EXISTS `game_tmp_file`;
CREATE TABLE `game_tmp_file`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` tinyint(1) NOT NULL COMMENT '类型 (1:系统; 2:欧卡; 3:美卡; )',
  `md5` char(32) NOT NULL COMMENT '文件 MD5',
  `file_size` bigint NULL DEFAULT NULL COMMENT '文件大小, 字节',
  `file_path` varchar(255) NOT NULL COMMENT '文件路径',
  `oss_object_key` varchar(100) NOT NULL COMMENT 'OSS Object Key',
  `oss_url` varchar(255) NOT NULL COMMENT 'OSS 文件下载地址',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = 'TMP文件信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for game_tmp_version
-- ----------------------------
DROP TABLE IF EXISTS `game_tmp_version`;
CREATE TABLE `game_tmp_version`  (
  `tmp_version` varchar(50) NOT NULL COMMENT 'TMP 插件版本',
  `support_game_version` varchar(50) NOT NULL COMMENT '兼容游戏版本',
  `official_game_version` varchar(50) NOT NULL COMMENT '官方游戏版本'
) ENGINE = InnoDB ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for game_vtc
-- ----------------------------
DROP TABLE IF EXISTS `game_vtc`;
CREATE TABLE `game_vtc`  (
  `vtc_id` int NOT NULL COMMENT 'VTC ID',
  `name` varchar(100) NOT NULL COMMENT 'VTC名称',
  `level` tinyint(1) NOT NULL DEFAULT 1 COMMENT '认证等级 (1:普通; 2:橙名; 3:红名; )',
  `owner_tmp_id` int NULL DEFAULT NULL COMMENT '所有者TMP ID',
  `logo_url` varchar(255) NULL DEFAULT NULL COMMENT 'Logo地址',
  `cover_url` varchar(255) NULL DEFAULT NULL COMMENT '封面地址',
  `member_count` int NOT NULL DEFAULT 0 COMMENT '成员数量',
  `create_time` datetime NOT NULL COMMENT '车队创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`vtc_id`) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for map_marker
-- ----------------------------
DROP TABLE IF EXISTS `map_marker`;
CREATE TABLE `map_marker`  (
  `id` bigint NOT NULL,
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父级ID',
  `name` varchar(100) NULL DEFAULT NULL COMMENT '名称',
  `map_type` tinyint(1) NOT NULL COMMENT '地图类型 (1:ets: 2:promods; )',
  `type` tinyint NOT NULL COMMENT '类型 (1:国家; 2:城市; 3:货场; 4:卡车销售商; 5:停车场; 6:修理厂; 7:司机介绍所; 8:渡轮; 9:车库; 10:加油站; 11:火车站; 12:观景点; 13:称重站; 14:巴士站; 15:收费站; 99:其他; )',
  `axis_x` decimal(10, 2) NOT NULL COMMENT '坐标轴X',
  `axis_y` decimal(10, 2) NOT NULL COMMENT '坐标轴Y',
  `icon_url` varchar(255) NULL DEFAULT NULL COMMENT '图标地址',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `type`(`type` ASC) USING BTREE,
  INDEX `axis`(`axis_x` ASC, `axis_y` ASC) USING BTREE,
  INDEX `map_type`(`map_type` ASC) USING BTREE
) ENGINE = InnoDB COMMENT = '地图标点' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint NOT NULL,
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text NULL COMMENT '配置值',
  `description` varchar(255) NULL DEFAULT NULL COMMENT '配置说明',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB COMMENT = '系统配置' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
