ALTER TABLE `open_game`.`app_online_pay`   
  ADD COLUMN `clientIp` VARCHAR(30) NULL  COMMENT '客户端的ip' AFTER `appPayContent`;
  
 ALTER TABLE `opengame`.`app_online_pay` ADD COLUMN `payResInfo` VARCHAR(100) NULL COMMENT '下单支付的预留信息' AFTER `callBackContent`; 