/*
Navicat MySQL Data Transfer

Source Server         : 192.168.0.200
Source Server Version : 50173
Source Host           : 192.168.0.200:3306
Source Database       : medicinedb

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2018-11-21 18:41:53
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
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8;

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
  PRIMARY KEY (`openId`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_u_family
-- ----------------------------
DROP TABLE IF EXISTS `t_u_family`;
CREATE TABLE `t_u_family` (
  `id` varchar(50) NOT NULL,
  `score` int(11) DEFAULT NULL,
  `memberJson` text,
  `ownerId` varchar(50) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `totalScore` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_u_player
-- ----------------------------
DROP TABLE IF EXISTS `t_u_player`;
CREATE TABLE `t_u_player` (
  `openId` varchar(100) NOT NULL COMMENT '用户ID',
  `nickName` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `level` int(11) DEFAULT NULL COMMENT '等级',
  `totalQuestions` int(11) DEFAULT NULL COMMENT '总答题数',
  `answerSuccess` int(11) DEFAULT NULL COMMENT '总正确数',
  `historyCatergorysStr` text COMMENT '历史答题类型记录',
  `historyQuestionsStr` text COMMENT '历史答题ID',
  `score` int(11) DEFAULT NULL COMMENT '经验',
  `iconUrl` varchar(500) DEFAULT NULL COMMENT '头像',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创角时间',
  `loginTime` bigint(20) DEFAULT NULL,
  `historyRaceStr` text COMMENT '排位赛记录',
  `familyId` varchar(50) DEFAULT NULL COMMENT '群ID',
  `invitationFamilysStr` text COMMENT '邀请群列表',
  `signTime` bigint(20) DEFAULT NULL COMMENT '签到时间',
  `totalScore` int(11) DEFAULT '0' COMMENT '总积分',
  PRIMARY KEY (`openId`),
  UNIQUE KEY `idx_opend_id` (`openId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_u_serverdata
-- ----------------------------
DROP TABLE IF EXISTS `t_u_serverdata`;
CREATE TABLE `t_u_serverdata` (
  `id` int(11) NOT NULL,
  `server_data` longblob COMMENT '服务器全局数据',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
