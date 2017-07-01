ALTER TABLE `opengame`.`app_online_pay`   
  ADD COLUMN `callBackUrl` VARCHAR(100) NULL  COMMENT '发货地址' AFTER `callBackContent`;