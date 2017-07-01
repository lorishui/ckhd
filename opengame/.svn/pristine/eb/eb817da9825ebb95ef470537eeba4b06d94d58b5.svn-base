CREATE TABLE `t_qq_activity` (
  `id` varchar(64) NOT NULL,
  `ckAppId` int(4) NOT NULL COMMENT '游戏id',
  `imsi` varchar(32) DEFAULT NULL COMMENT '设备号',
  `qq` varchar(20) NOT NULL COMMENT 'qq',
  `sign` varchar(32) NOT NULL COMMENT '校验码',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_qq_ckAppId` (`qq`,`ckAppId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8