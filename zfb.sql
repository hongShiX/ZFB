/*
 Navicat Premium Data Transfer

 Source Server         : hh_mysql
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : localhost:3306
 Source Schema         : zfb

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 06/07/2023 16:23:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for b_bid_info
-- ----------------------------
DROP TABLE IF EXISTS `b_bid_info`;
CREATE TABLE `b_bid_info`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `prod_id` int NULL DEFAULT NULL COMMENT '产品id',
  `uid` int NULL DEFAULT NULL COMMENT '用户id',
  `bid_money` decimal(11, 2) NULL DEFAULT NULL COMMENT '投资金额',
  `bid_time` datetime NULL DEFAULT NULL COMMENT '投资时间',
  `bid_status` int NULL DEFAULT NULL COMMENT '投资状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `bbi_prod_id`(`prod_id` ASC) USING BTREE,
  INDEX `bbi_user_id`(`uid` ASC) USING BTREE,
  CONSTRAINT `bbi_prod_id` FOREIGN KEY (`prod_id`) REFERENCES `b_recharge_record` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `bbi_user_id` FOREIGN KEY (`uid`) REFERENCES `u_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_bid_info
-- ----------------------------

-- ----------------------------
-- Table structure for b_income_record
-- ----------------------------
DROP TABLE IF EXISTS `b_income_record`;
CREATE TABLE `b_income_record`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` int NULL DEFAULT NULL COMMENT '用户id',
  `prod_id` int NULL DEFAULT NULL COMMENT '产品id',
  `bid_id` int NULL DEFAULT NULL COMMENT '投资id',
  `bid_money` decimal(11, 2) NULL DEFAULT NULL COMMENT '投资金额',
  `income_date` date NULL DEFAULT NULL COMMENT '到期时间',
  `income_money` decimal(11, 2) NULL DEFAULT NULL COMMENT '收益金额',
  `income_status` int NULL DEFAULT NULL COMMENT '收益状态:0未返，1已反',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `bpi_uid`(`uid` ASC) USING BTREE,
  INDEX `bpi_prod_id`(`prod_id` ASC) USING BTREE,
  CONSTRAINT `bpi_prod_id` FOREIGN KEY (`prod_id`) REFERENCES `b_product_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `bpi_uid` FOREIGN KEY (`uid`) REFERENCES `u_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_income_record
-- ----------------------------

-- ----------------------------
-- Table structure for b_product_info
-- ----------------------------
DROP TABLE IF EXISTS `b_product_info`;
CREATE TABLE `b_product_info`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `product_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品名称',
  `rate` decimal(11, 2) NULL DEFAULT NULL COMMENT '产品年利率',
  `cycle` int NULL DEFAULT NULL COMMENT '产品周期',
  `release_time` date NULL DEFAULT NULL COMMENT '上架时间',
  `product_type` int NULL DEFAULT NULL COMMENT '产品类型0新手宝，1优选产品，2散标产品',
  `product_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品编号',
  `product_money` decimal(11, 2) NULL DEFAULT NULL COMMENT '产品金额',
  `left_product_money` decimal(11, 2) NULL DEFAULT NULL COMMENT '产品剩余金额',
  `bid_min_limit` decimal(11, 2) NULL DEFAULT NULL COMMENT '最低投资金额',
  `bid_max_limit` decimal(11, 2) NULL DEFAULT NULL COMMENT '最高投资金额',
  `product_status` int NULL DEFAULT NULL COMMENT '产品状态：0未满标，1已满标，2满标已生成收益计划',
  `product_full_time` datetime NULL DEFAULT NULL COMMENT '产品投资满标时间',
  `product_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_product_info
-- ----------------------------

-- ----------------------------
-- Table structure for b_recharge_record
-- ----------------------------
DROP TABLE IF EXISTS `b_recharge_record`;
CREATE TABLE `b_recharge_record`  (
  `id` int NOT NULL COMMENT 'id（自增）',
  `uid` int NULL DEFAULT NULL COMMENT '用户id',
  `recharge_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '充值订单号',
  `recharge_status` int NULL DEFAULT NULL COMMENT '充值订单状态：0充值中，1充值成功，2充值失败',
  `recharge_money` decimal(11, 2) NULL DEFAULT NULL COMMENT '充值金额',
  `time` datetime NULL DEFAULT NULL COMMENT '充值时间',
  `recharge_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '充值描述',
  `channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '充值渠道',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `b_recharge_record_uid`(`uid` ASC) USING BTREE,
  CONSTRAINT `b_recharge_record_uid` FOREIGN KEY (`uid`) REFERENCES `u_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_recharge_record
-- ----------------------------

-- ----------------------------
-- Table structure for u_finance_account
-- ----------------------------
DROP TABLE IF EXISTS `u_finance_account`;
CREATE TABLE `u_finance_account`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id（自增）',
  `uid` int NULL DEFAULT NULL COMMENT '用户的id(关联user中外键)',
  `available_money` decimal(11, 2) NULL DEFAULT NULL COMMENT '用户余额',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `u_finace_account_uid`(`uid` ASC) USING BTREE,
  CONSTRAINT `u_finace_account_uid` FOREIGN KEY (`uid`) REFERENCES `u_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of u_finance_account
-- ----------------------------

-- ----------------------------
-- Table structure for u_user
-- ----------------------------
DROP TABLE IF EXISTS `u_user`;
CREATE TABLE `u_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id（自增）',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '电话',
  `login_password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '姓名',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '身份证号',
  `add_time` datetime NULL DEFAULT NULL COMMENT '注册时间',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最近登陆时间',
  `header_image` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of u_user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
