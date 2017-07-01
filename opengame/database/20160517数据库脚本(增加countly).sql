DROP TABLE IF EXISTS `app_order_countly_forward`;
CREATE TABLE `app_order_countly_forward` (
  `id` varchar(64) NOT NULL COMMENT '标识id',
  `ck_appid` varchar(45) DEFAULT NULL COMMENT '创酷appid',
  `app_id` varchar(45) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL COMMENT '订单类型',
  `content` varchar(4000) DEFAULT NULL COMMENT '订单原始内容（xml)',
  `send_num` int(12) DEFAULT '1' COMMENT '转发次数，7次后不再转发',
  `next_send_time` datetime DEFAULT NULL COMMENT '下次转发时间',
  `status` int(12) DEFAULT '0' COMMENT '0-发送完成;1-等待重发或发送中;2-七次重发失效',
  `create_by` varchar(64) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单信息转发到countly';
