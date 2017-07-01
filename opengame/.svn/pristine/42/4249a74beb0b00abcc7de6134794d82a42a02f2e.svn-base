/*
Navicat MySQL Data Transfer

Source Server         : devmac
Source Server Version : 50625
Source Host           : devmac:3306
Source Database       : open_game

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2017-03-27 19:34:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_gamecode_log
-- ----------------------------
DROP TABLE IF EXISTS `t_gamecode_log`;
CREATE TABLE `t_gamecode_log` (
  `id` varchar(64) NOT NULL,
  `ckapp_id` varchar(255) DEFAULT NULL,
  `code` varchar(30) DEFAULT NULL,
  `phone_num` varchar(20) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL COMMENT '0 成功 1 失败',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
