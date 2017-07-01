CREATE TABLE `game_report` (
  `id` VARCHAR(64) NOT NULL,
  `ckappid` VARCHAR(64) NOT NULL,
  `date` VARCHAR(10) NOT NULL,
  `create_date` DATETIME NOT NULL,
  `data` BLOB NULL,
  `status` VARCHAR(10) NOT NULL DEFAULT '0' COMMENT '0-新建任务处理中,1-任务成功完成,2-任务失败',
  `del_flag` CHAR(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8
COMMENT = '游戏报表';
