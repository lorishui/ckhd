/**
 * 新增初始化配置表
 */
DROP TABLE IF EXISTS `app_cfgparam`;
CREATE TABLE `app_cfgparam` (
  `id` varchar(64) NOT NULL COMMENT '标识id',
  `ckAppId` varchar(64) DEFAULT NULL COMMENT '创酷appid',
  `mmAppId` varchar(64) DEFAULT NULL COMMENT '运营商appid',
  `ckChannelId` varchar(64) DEFAULT NULL COMMENT '渠道id',
  `versionName` varchar(64) DEFAULT NULL COMMENT '版本号',
  `carriers` varchar(64) DEFAULT NULL COMMENT '运营商',
  `os` varchar(64) DEFAULT NULL COMMENT '操作系统',
  `province` varchar(64) DEFAULT NULL COMMENT '省份Id',
  `rqType` varchar(64) DEFAULT NULL COMMENT '初始化类型',
  `exInfo` varchar(255) DEFAULT NULL COMMENT '扩展参数',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标识',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;