
-- ----------------------------
-- Table structure for t_ad_push
-- ----------------------------
DROP TABLE IF EXISTS `t_ad_push`;
CREATE TABLE `t_ad_push` (
  `id` varchar(64) NOT NULL,
  `app_id` varchar(64) DEFAULT NULL,
  `platform` varchar(30) DEFAULT NULL,
  `media_name` varchar(100) DEFAULT NULL,
  `account` varchar(100) DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `del_flag` varchar(2) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for t_ad_push_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_ad_push_detail`;
CREATE TABLE `t_ad_push_detail` (
  `id` varchar(64) NOT NULL,
  `ad_push_id` varchar(64) DEFAULT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  `ad_place` varchar(255) DEFAULT NULL,
  `ad_url` varchar(255) DEFAULT NULL,
  `description` text,
  `create_by` varchar(64) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `del_flag` varchar(2) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_ad_push_cost
-- ----------------------------
DROP TABLE IF EXISTS `t_ad_push_cost`;
CREATE TABLE `t_ad_push_cost` (
  `id` varchar(64) NOT NULL,
  `ad_push_detail_id` varchar(64) DEFAULT NULL,
  `date` date NOT NULL COMMENT '消耗日期',
  `day_cost` double DEFAULT NULL COMMENT '当日消耗',
  `day_earn` double DEFAULT NULL,
  `regist_num` int(11) DEFAULT '1',
  `create_by` varchar(64) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `del_flag` varchar(2) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;