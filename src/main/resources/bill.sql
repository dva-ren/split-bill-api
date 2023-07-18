/*
Navicat MySQL Data Transfer

Source Server         : 本地mysql
Source Server Version : 80023
Source Host           : localhost:3306
Source Database       : bill

Target Server Type    : MYSQL
Target Server Version : 80023
File Encoding         : 65001

Date: 2023-07-18 13:58:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for activities
-- ----------------------------
DROP TABLE IF EXISTS `activities`;
CREATE TABLE `activities` (
  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者ID',
  `name` varchar(255) DEFAULT NULL COMMENT '活动名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_delete` int NOT NULL DEFAULT '0' COMMENT '逻辑删除字段(0正常 1删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for activity_participants
-- ----------------------------
DROP TABLE IF EXISTS `activity_participants`;
CREATE TABLE `activity_participants` (
  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `activity_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动ID',
  `user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_delete` int NOT NULL DEFAULT '0' COMMENT '逻辑删除字段(0正常 1删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for bills
-- ----------------------------
DROP TABLE IF EXISTS `bills`;
CREATE TABLE `bills` (
  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单ID',
  `money` decimal(12,2) NOT NULL COMMENT '金额',
  `category` varchar(255) NOT NULL COMMENT '账单分类',
  `remark` varchar(255) DEFAULT NULL COMMENT '账单备注',
  `creator_id` varchar(100) NOT NULL COMMENT '创建人ID',
  `date` datetime DEFAULT NULL,
  `activity_id` varchar(100) NOT NULL COMMENT '活动ID',
  `description` varchar(255) DEFAULT NULL COMMENT '账单描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_delete` int NOT NULL DEFAULT '0' COMMENT '逻辑删除字段(0正常 1删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for bill_participants
-- ----------------------------
DROP TABLE IF EXISTS `bill_participants`;
CREATE TABLE `bill_participants` (
  `id` varchar(100) NOT NULL,
  `bill_id` varchar(100) NOT NULL COMMENT '账单ID',
  `user_id` varchar(100) NOT NULL COMMENT '用户ID',
  `pay_to_user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付给用户ID',
  `split_money` decimal(10,2) NOT NULL COMMENT '分摊金额',
  `activity_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动id',
  `fixed` int NOT NULL DEFAULT '1' COMMENT '是否固定金额',
  `paid` int NOT NULL DEFAULT '0' COMMENT '是否已支付',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_delete` int NOT NULL DEFAULT '0' COMMENT '逻辑删除字段',
  PRIMARY KEY (`id`,`pay_to_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` varchar(255) NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `open_id` varchar(200) DEFAULT NULL COMMENT '微信open ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_delete` int NOT NULL DEFAULT '0' COMMENT '逻辑删除字段(0正常 1删除)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
