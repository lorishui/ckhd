/**
 * 新增联通回调订单统计表
 */
DROP TABLE IF EXISTS `wo_app_order`;
CREATE TABLE `wo_app_order` (
  `id` varchar(64) NOT NULL COMMENT 'id',
  `ckapp_id` varchar(64) DEFAULT NULL COMMENT '统一appid',
  `orderid` varchar(64) DEFAULT NULL COMMENT '订单号',
  `ordertime` varchar(64) DEFAULT NULL COMMENT '订单交易时间',
  `cpid` varchar(32) DEFAULT NULL COMMENT '开发者/开发商ID',
  `appid` varchar(32) DEFAULT NULL COMMENT '应用ID',
  `fid` varchar(32) DEFAULT NULL COMMENT '渠道ID',
  `consumeCode` varchar(64) DEFAULT NULL COMMENT '计费点ID',
  `payfee` varchar(16) DEFAULT NULL COMMENT '支付金额，分',
  `payType` varchar(16) DEFAULT NULL COMMENT '支付方式',
  `hRet` varchar(16) DEFAULT NULL COMMENT '支付结果',
  `status` varchar(32) DEFAULT NULL COMMENT '状态码',
  `callBackContent` text DEFAULT NULL COMMENT '联通调用的XML',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `IDX_AND_APP_ORDER_CREATEDATE` (`create_date`),
  KEY `IDX_AND_APP_ORDER_CKAPPID` (`appid`) USING BTREE,
  KEY `IDX_AND_APP_ORDER_USER_ID` (`orderid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联通订单接口表';