CREATE TABLE `meta_cellinfo_zone` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `pid` varchar(45) NOT NULL COMMENT '父节点',
  `name` varchar(60) NOT NULL COMMENT '名称',
  `sort` int(4) DEFAULT 0 COMMENT '排序',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `IDX_META_CELLINFO_ZONE_PID` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='元数据_基站数据_行政区划';

CREATE TABLE `cfg_zone_level` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `ckappid` varchar(60) NOT NULL COMMENT '创酷APPID',
  `channelid` varchar(45) NOT NULL COMMENT '渠道号',
  `redzonelist` varchar(2048) DEFAULT NULL COMMENT '红区区域代码列表',
  `yellowzonelist` varchar(2048) DEFAULT NULL COMMENT '黄区区域代码列表',
  `sort` int(4) DEFAULT 0 COMMENT '排序',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(45) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(45) DEFAULT NULL COMMENT '修改人',
  `update_date` datetime DEFAULT NULL COMMENT '修改时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  KEY `IDX_CFG_ZONE_LEVEL_CKAPPID` (`ckappid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置数据_行政区划_级别';
