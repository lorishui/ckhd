--天翼订单请求
CREATE TABLE `tianyi_order` (
  `id` varchar(64) COMMENT '主键Id',
  `ver` varchar(20) DEFAULT NULL COMMENT '协议版本号',
  `requestId` varchar(20) DEFAULT NULL COMMENT '请求事务号',
  `orderid` varchar(20) DEFAULT NULL COMMENT '请求事务号',
  `channel` varchar(20) DEFAULT NULL COMMENT '请求事务号',
  `imsi` varchar(20) DEFAULT NULL COMMENT '请求事务号',
  `apName` varchar(20) DEFAULT NULL COMMENT '请求事务号',
  `appName` varchar(20) DEFAULT NULL COMMENT '请求事务号',
  `chargeName` varchar(20) DEFAULT NULL COMMENT '请求事务号',
  `price` varchar(20) DEFAULT NULL COMMENT '请求事务号',
  `priceType` varchar(20) DEFAULT NULL COMMENT '计费类型：0-按次计费（默认）；1-包月计费，可空',
  `payType` varchar(20) DEFAULT NULL COMMENT '付费运营商：1-联通计费； 2-移动计费；3-电信计费',
  `sig` varchar(20) DEFAULT NULL COMMENT '签名验证，MD5(orderId+channel+imsi+requestId+key)',
  `subOpt` varchar(20) DEFAULT NULL COMMENT '',
  `sessionId` varchar(20) DEFAULT NULL COMMENT '移动二次请求sessionId',
  `verifyCode` varchar(20) DEFAULT NULL COMMENT '移动二次请求验证码',
  `status` int COMMENT '状态，0-一次请求，1-移动二次确认，2-电信的',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='和游戏订单接口表';