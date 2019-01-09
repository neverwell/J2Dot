/*
Navicat MySQL Data Transfer

Source Server         : 192.168.5.66
Source Server Version : 50554
Source Host           : localhost:3306
Source Database       : tw_s2

Target Server Type    : MYSQL
Target Server Version : 50554
File Encoding         : 65001

Date: 2018-06-13 22:24:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for p_bag
-- ----------------------------
DROP TABLE IF EXISTS `p_bag`;
CREATE TABLE `p_bag` (
  `id` bigint(20) NOT NULL,
  `data` mediumblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for p_card
-- ----------------------------
DROP TABLE IF EXISTS `p_card`;
CREATE TABLE `p_card` (
  `id` bigint(20) NOT NULL,
  `rid` bigint(20) DEFAULT NULL,
  `card` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for p_count
-- ----------------------------
DROP TABLE IF EXISTS `p_count`;
CREATE TABLE `p_count` (
  `id` bigint(20) NOT NULL,
  `data` mediumblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for p_mail
-- ----------------------------
DROP TABLE IF EXISTS `p_mail`;
CREATE TABLE `p_mail` (
  `id` bigint(20) NOT NULL,
  `uid` bigint(20) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `items` varchar(255) DEFAULT NULL,
  `time` int(11) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for p_name
-- ----------------------------
DROP TABLE IF EXISTS `p_name`;
CREATE TABLE `p_name` (
  `roleId` bigint(20) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for p_role
-- ----------------------------
DROP TABLE IF EXISTS `p_role`;
CREATE TABLE `p_role` (
  `id` bigint(20) NOT NULL,
  `data` mediumblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for p_user
-- ----------------------------
DROP TABLE IF EXISTS `p_user`;
CREATE TABLE `p_user` (
  `id` bigint(20) NOT NULL,
  `loginName` varchar(64) DEFAULT NULL,
  `roleName` varchar(64) DEFAULT NULL,
  `pid` smallint(6) DEFAULT NULL,
  `sid` int(11) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `client` varchar(20) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `IDNumber` varchar(18) DEFAULT NULL,
  `regTime` bigint(20) DEFAULT NULL,
  `qudao` varchar(20) DEFAULT NULL,
  `channel` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for s_announce
-- ----------------------------
DROP TABLE IF EXISTS `s_announce`;
CREATE TABLE `s_announce` (
  `id` bigint(20) NOT NULL,
  `uniqueId` int(11) DEFAULT NULL,
  `startTime` int(11) DEFAULT NULL,
  `endTime` int(11) DEFAULT NULL,
  `period` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `content` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for s_data
-- ----------------------------
DROP TABLE IF EXISTS `s_data`;
CREATE TABLE `s_data` (
  `id` bigint(20) NOT NULL,
  `data` mediumblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for s_dync_activity
-- ----------------------------
DROP TABLE IF EXISTS `s_dync_activity`;
CREATE TABLE `s_dync_activity` (
  `id` int(11) NOT NULL,
  `param` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for s_dync_goal
-- ----------------------------
DROP TABLE IF EXISTS `s_dync_goal`;
CREATE TABLE `s_dync_goal` (
  `id` varchar(255) NOT NULL,
  `param` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for s_mail
-- ----------------------------
DROP TABLE IF EXISTS `s_mail`;
CREATE TABLE `s_mail` (
  `id` bigint(20) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `param` int(11) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `itemStr` varchar(255) DEFAULT NULL,
  `sendTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for s_oper
-- ----------------------------
DROP TABLE IF EXISTS `s_oper`;
CREATE TABLE `s_oper` (
  `pid` int(11) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for s_order
-- ----------------------------
DROP TABLE IF EXISTS `s_order`;
CREATE TABLE `s_order` (
  `id` bigint(20) NOT NULL,
  `orderId` varchar(255) DEFAULT NULL,
  `uid` bigint(20) DEFAULT NULL,
  `loginName` varchar(255) DEFAULT NULL,
  `client` int(11) DEFAULT NULL,
  `roleName` varchar(255) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `yuanbao` int(11) DEFAULT NULL,
  `bindYuanbao` int(11) DEFAULT NULL,
  `time` int(11) DEFAULT NULL,
  `qudao` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for s_rank
-- ----------------------------
DROP TABLE IF EXISTS `s_rank`;
CREATE TABLE `s_rank` (
  `uid` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `roleLevel` int(11) DEFAULT '0',
  `roleRein` int(11) DEFAULT '0',
  `roleExp` bigint(20) DEFAULT '0',
  `sex` int(11) DEFAULT NULL,
  `career` int(11) NOT NULL,
  `fightPower` bigint(20) DEFAULT '0',
  `heroFightPower` bigint(20) DEFAULT '0',
  `junxian` int(11) DEFAULT '0',
  `honor` bigint(20) DEFAULT '0',
  `wingFightPower` bigint(20) DEFAULT '0',
  `weiwang` int(11) DEFAULT '0',
  `vipLevel` int(11) DEFAULT '0',
  `weiwangLevel` int(11) NOT NULL DEFAULT '0',
  `barrier` int(11) NOT NULL DEFAULT '0',
  `searchPk` int(11) DEFAULT '0',
  `lastLoginTime` int(11) DEFAULT '0',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for s_union
-- ----------------------------
DROP TABLE IF EXISTS `s_union`;
CREATE TABLE `s_union` (
  `id` bigint(20) NOT NULL,
  `data` mediumblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
