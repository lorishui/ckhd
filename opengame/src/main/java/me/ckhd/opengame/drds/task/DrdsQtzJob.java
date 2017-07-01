package me.ckhd.opengame.drds.task;

import me.ckhd.opengame.api.task.SendOrderScheduleBoot;
import me.ckhd.opengame.drds.service.EventService;
import me.ckhd.opengame.stats.task.AndCountMoneyTask;
import me.ckhd.opengame.stats.task.MMCountMoneyTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DrdsQtzJob
{
  Logger logger = LoggerFactory.getLogger(DrdsQtzJob.class);

  @Autowired
  EventService eventService;

  protected void execute()
  {
	  if(!SendOrderScheduleBoot.getInstance().isStart()){
		  return;
	  }
	  MMCountMoneyTask.countMMMoney();
	  AndCountMoneyTask.countMMMoney();
	  
//	  logger.info("开始保存昨日新增");
//	  //eventService.insertStat();
//	  logger.info("保存昨日新增结束");
//	  
//	  
//	  logger.info("开始更新2日留存");
//	  //eventService.updateStat(Stat.SECOND);
//	  logger.info("更新2日留存结束");
//	  
//	  
//	  logger.info("开始更新3日留存");
//	 // eventService.updateStat(Stat.THIRD);
//	  logger.info("更新3日留存结束");
//	  
//	  
//	  logger.info("开始更新7日留存");
//	 // eventService.updateStat(Stat.SEVENTH);
//	  logger.info("更新7日留存结束");
//	  
//	  logger.info("开始更新15日留存");
//	 // eventService.updateStat(Stat.FIFTEENTH);
//	  logger.info("更新15日留存结束");
//	  
//	  
//	  logger.info("开始更新30日留存");
//	 // eventService.updateStat(Stat.THIRTIETH);
//	  logger.info("更新30日留存结束");
  }
}