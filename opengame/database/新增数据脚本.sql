--app_online_pay表新增sdkType字段
alter table app_online_pay add sdkType varchar(64) DEFAULT null COMMENT 'sdk类型' after payType;


alter table app_carriers add onlineType int(12) DEFAULT 0 COMMENT '是否联网支付' after cp_server_url;

alter table and_app_order add provinceId varchar(10) COMMENT '省份' after packageID;
alter table and_app_order add channelId varchar(64) COMMENT '渠道' after provinceId;

ALTER TABLE `app_cfgparam` CHANGE COLUMN `exInfo` `exInfo` VARCHAR(4096) NULL DEFAULT NULL COMMENT '扩展参数' ;

alter table app_cfgparam add time varchar(20) COMMENT '生效时间段' after rqType;


#alter table rd_app_order add ackType varchar(8) COMMENT '计费方式' after payType;

#alter table rd_app_order add payCount int(8) COMMENT '当前计费次数' after ackType;


alter table app_carriers add flow_server_url varchar(200) COMMENT '导量商服务器' after cp_server_url;
alter table app_carriers add paycodes varchar(1000) COMMENT '导量商转发计费点列表' after flow_server_url;
