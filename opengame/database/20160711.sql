ALTER TABLE `mm_app_order`   
  ADD COLUMN `random` INT(4) NULL  COMMENT '随机数过滤' AFTER `remarks`, 
  ADD  INDEX `IDX_RANDOM` (`random`);

ALTER TABLE `and_app_order`   
  ADD COLUMN `random` INT(4) NULL  COMMENT '随机数过滤' AFTER `remarks`, 
  ADD  INDEX `IDX_RANDOM` (`random`);