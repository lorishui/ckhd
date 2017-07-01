ALTER TABLE `app_ck`
ADD COLUMN `secretKey`  varchar(255) NULL COMMENT '密钥' AFTER `update_by`;


DROP TABLE IF EXISTS `app_online_orderindex`;
CREATE TABLE `app_online_orderindex` (
  `id` int(11) DEFAULT NULL COMMENT '主键Id',
  `orderId` varchar(255) DEFAULT NULL COMMENT '订单编号',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;