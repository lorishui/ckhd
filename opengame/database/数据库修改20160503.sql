CREATE TABLE `app_cfgparam_feilds` (
  `id` varchar(64) NOT NULL,
  `ckAppId` varchar(20) NOT NULL COMMENT '应用id',
  `type` varchar(20) NOT NULL COMMENT '类型',
  `value` varchar(100) NOT NULL COMMENT '数据值',
  `label` varchar(50) NOT NULL COMMENT '标签名',
  `description` varchar(100) DEFAULT NULL COMMENT '详细描述',
  `classType` int(1) NOT NULL COMMENT '对应标签实际值的类型,1:int,2:boolean,3:jsonObject,4:jsonArray,5:string',
  `remarks` varchar(100) DEFAULT NULL COMMENT '备注信息',
  `sort` int(4) NOT NULL COMMENT '排序（升序）',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `index_ckappid` (`ckAppId`),
  KEY `index_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8