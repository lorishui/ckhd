ALTER TABLE sys_dict   
  ADD COLUMN `classType` INT(1) NULL  COMMENT '对应标签实际值的类型' AFTER `type`;
  
CREATE TABLE `opengame`.`app_cfgparam_feilds`(  
  `id` VARCHAR(64) NOT NULL,
  `ckAppId` VARCHAR(20) NOT NULL COMMENT '应用id',
  `type` VARCHAR(20) NOT NULL COMMENT '类型',
  `value` VARCHAR(100) NOT NULL COMMENT '数据值',
  `label` VARCHAR(50) NOT NULL COMMENT '标签名',
  `description` VARCHAR(100) COMMENT '详细描述',
  `classType` INT(1) NOT NULL COMMENT '对应标签实际值的类型,1:int,2:boolean,3:jsonObject,4:jsonArray,5:string',
  `remarks` VARCHAR(100) COMMENT '备注信息',
  `sort` INT(4) NOT NULL COMMENT '排序（升序）',
  `del_flag` CHAR(1) NOT NULL DEFAULT '0'  COMMENT '删除标记',
  PRIMARY KEY (`id`)
);
