/*
Navicat MySQL Data Transfer

Source Server         : 192.168.0.200
Source Server Version : 50173
Source Host           : 192.168.0.200:3306
Source Database       : longmarch

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2018-12-20 14:14:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_log_login
-- ----------------------------
DROP TABLE IF EXISTS `t_log_login`;
CREATE TABLE `t_log_login` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(100) DEFAULT NULL,
  `loginTime` datetime DEFAULT NULL COMMENT '登录日志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for t_u_consume
-- ----------------------------
DROP TABLE IF EXISTS `t_u_consume`;
CREATE TABLE `t_u_consume` (
  `openId` varchar(100) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `itemId` bigint(255) NOT NULL,
  PRIMARY KEY (`openId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_u_family
-- ----------------------------
DROP TABLE IF EXISTS `t_u_family`;
CREATE TABLE `t_u_family` (
  `id` varchar(50) NOT NULL,
  `step` int(11) DEFAULT NULL COMMENT '步数',
  `memberJson` text,
  `ownerId` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `totalStep` int(11) DEFAULT '0' COMMENT '总步数',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_u_player
-- ----------------------------
DROP TABLE IF EXISTS `t_u_player`;
CREATE TABLE `t_u_player` (
  `openId` varchar(100) NOT NULL COMMENT '用户ID',
  `nickName` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `level` int(11) DEFAULT NULL COMMENT '等级',
  `step` int(11) DEFAULT NULL COMMENT '步数',
  `iconUrl` varchar(500) DEFAULT NULL COMMENT '头像',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创角时间',
  `loginTime` bigint(20) DEFAULT NULL,
  `familyId` varchar(50) DEFAULT NULL COMMENT '群ID',
  `signTime` bigint(20) DEFAULT NULL COMMENT '签到时间',
  `totalStep` int(11) DEFAULT '0' COMMENT '总步数',
  `todayTransStep` int(11) DEFAULT '0' COMMENT '今日兑换步数',
  PRIMARY KEY (`openId`),
  UNIQUE KEY `idx_opend_id` (`openId`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_u_serverdata
-- ----------------------------
DROP TABLE IF EXISTS `t_u_serverdata`;
CREATE TABLE `t_u_serverdata` (
  `id` int(11) NOT NULL,
  `server_data` longblob COMMENT '服务器全局数据',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
