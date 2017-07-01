/********************************创建表过程**************************************/

DELIMITER $$

USE `open_game`$$

DROP PROCEDURE IF EXISTS `create_table_4Event`$$

CREATE /*DEFINER=`root`@`10.0.0.%`*/ PROCEDURE `create_table_4Event`()
BEGIN
    
	DECLARE f_ckappId  VARCHAR(12); -- ckapp id 
	DECLARE done INT;               -- 是否完成 
	DECLARE sqlstr VARCHAR(4000);    -- 建表语句
	-- 定义遍历创酷appid的游标 
	DECLARE ckappid_cursor CURSOR FOR SELECT DISTINCT  ckapp_id  FROM app_ck WHERE del_flag='0' AND autoCreateForEvent='1';
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;
	
	-- 生成的创建语句值置空，不然为null 
        SET sqlstr='';	
	
	OPEN ckappid_cursor; 
	cursor_loop:LOOP
	FETCH ckappid_cursor INTO f_ckappId ; -- 取数据 
	IF done=1 THEN
		LEAVE cursor_loop;
	END IF;	
	
	-- 生成创建表语句
	-- set sqlstr =  ();
	SET sqlstr =  CONCAT('CREATE  TABLE ',CONCAT_WS('_','app_event',f_ckappId,DATE_FORMAT(NOW(),'%Y%m%d')),' LIKE app_event;');
    -- 动态创建表
	 SET @excuteSql=sqlstr;
         PREPARE tempSql FROM @excuteSql;
	 EXECUTE tempSql;
    
    
	END LOOP cursor_loop;
	CLOSE ckappid_cursor; 
	
	SELECT  sqlstr;
	 DEALLOCATE PREPARE tempSql; 
    END$$

DELIMITER ;







/***************************event below *****************************/


DELIMITER $$

ALTER DEFINER = 
  /*`root` @`10.0.0.%`*/ EVENT `e_createTable4AppEvent` 
  ON SCHEDULE EVERY 1 DAY STARTS '2015-07-12 03:30:00' ENDS '2035-01-01 00:00:00' 
  ON COMPLETION PRESERVE ENABLE COMMENT '自动创建事件采集表' DO 
  BEGIN
    CALL create_table_4Event ;
END $$

DELIMITER ;















