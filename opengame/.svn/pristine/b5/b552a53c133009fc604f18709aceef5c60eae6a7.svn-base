/*
Navicat MySQL Data Transfer

Source Server         : devmac
Source Server Version : 50625
Source Host           : devmac:3306
Source Database       : open_game

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2017-03-27 19:33:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_gamecode
-- ----------------------------
DROP TABLE IF EXISTS `t_gamecode`;
CREATE TABLE `t_gamecode` (
  `id` varchar(64) NOT NULL,
  `ckAppID` varchar(64) DEFAULT NULL,
  `code` varchar(32) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_by` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
