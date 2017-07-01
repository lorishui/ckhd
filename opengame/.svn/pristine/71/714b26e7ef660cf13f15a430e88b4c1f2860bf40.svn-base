Drop TABLE IF EXISTS  ck_app_event;
CREATE TABLE ck_app_event (
	id VARCHAR (64) NOT NULL COMMENT 'id',
	sid VARCHAR (64) COMMENT '会话标识',
	sid_index INT COMMENT '当前会话序号',
	imei VARCHAR (64) COMMENT 'IMEI号',
	iccid VARCHAR (64) COMMENT 'ICCID号',
	type INT COMMENT '事件类型',
	ckappid VARCHAR (32) COMMENT '创酷appid',
	appid VARCHAR (32) COMMENT '当前运营商APPID',
	channelid VARCHAR (32) COMMENT '创酷统一渠道号',
	net_type VARCHAR (32) COMMENT '客户端当前网络类型',
	occur_time DATETIME COMMENT '客户端事件发生时间',
	version_name VARCHAR (32) COMMENT '版本号',
	phone_model VARCHAR (64) COMMENT '客户端机型',
	os_version VARCHAR (64) COMMENT '操作系统版本',
	lang VARCHAR (16) COMMENT '系统语言',
	sdk_version VARCHAR (32) COMMENT 'sdk版本号',
	exit_type VARCHAR (16) COMMENT '退出类型',
	pay_sdk VARCHAR (32) COMMENT '支付方式',
	pay_code VARCHAR (32) COMMENT '代码点、计费点',
	pay_number VARCHAR (32) COMMENT '购买数量',
	insert_time DATETIME COMMENT '入库时间',
	action_date DATE COMMENT '客户端事件发生日期',
	PRIMARY KEY (id)
) dbpartition BY HASH (imei) tbpartition BY MMDD (action_date) tbpartitions 365;


-- ----------------------------
-- 用户统计表
-- ----------------------------
Drop TABLE  IF EXISTS  ck_app_account;
CREATE TABLE ck_app_account (
	imei VARCHAR (64) NOT NULL COMMENT 'IMEI号',
	iccid VARCHAR (64) COMMENT 'ICCID号',
	ckappid VARCHAR (32) COMMENT '创酷appid',
	appid VARCHAR (32) COMMENT '当前运营商APPID',
	channelid VARCHAR (32) COMMENT '创酷统一渠道号',
	create_date DATETIME COMMENT '用户创建日期',
	last_login_date DATETIME COMMENT '用户最后登录时间',
	PRIMARY KEY (imei)
) dbpartition BY HASH (imei);

-- ----------------------------
-- 留存率统计表
-- ----------------------------
DROP TABLE IF EXISTS ck_stat_remain;
CREATE TABLE ck_stat_remain (
   channelid varchar(32) NOT NULL COMMENT '渠道ID',
  ckappid varchar(32) NOT NULL COMMENT '游戏ID',
  appid varchar(32) DEFAULT NULL COMMENT '运营商appid',
  stat_time date NOT NULL  COMMENT '用户新增时间',
  add_time timestamp NOT NULL  COMMENT '数据添加时间',
  dru int(11) NOT NULL COMMENT '每日注册用户',
  second_day decimal(11,2) DEFAULT NULL COMMENT '次日留存',
  third_day decimal(11,2) DEFAULT NULL COMMENT '3日留存',
  seventh_day decimal(11,2) DEFAULT NULL COMMENT '7日留存',
  fifteenth_day decimal(11,2) DEFAULT NULL COMMENT '15日留存',
  thirtieth_day decimal(11,2) DEFAULT NULL COMMENT '30日留存',
  PRIMARY KEY (channelid,ckappid,stat_time)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;