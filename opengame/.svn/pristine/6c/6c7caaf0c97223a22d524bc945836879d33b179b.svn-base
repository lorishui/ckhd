CREATE TABLE `app_three_net` (
  `id` varchar(64) NOT NULL DEFAULT '',
  `chargeResult` int(11) DEFAULT NULL COMMENT '计费结果 0:购买成功;非0:购买失败。',
  `orderId` varchar(20) DEFAULT NULL COMMENT '计费时传给的订单编号',
  `payType` int(11) DEFAULT NULL COMMENT '1：点播（按次）；2：包月',
  `payTime` varchar(64) DEFAULT NULL COMMENT '时间格式yyyy-MM-dd HH:mm:ss',
  `IMSI` varchar(64) DEFAULT NULL COMMENT '手机对应的IMSI号',
  `channel` varchar(20) DEFAULT NULL COMMENT '接入渠道号',
  `price` varchar(64) DEFAULT NULL COMMENT '价格，单位分',
  `version` varchar(255) DEFAULT NULL COMMENT '版本号，例如1.0',
  `sig` varchar(255) DEFAULT NULL COMMENT '签名MD5(orderId+channel+imsi+key)',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
