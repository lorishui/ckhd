package me.ckhd.opengame.drds.task;

import java.net.InetAddress;
import java.net.UnknownHostException;

import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.stats.task.ActUsersCountTask;
//import me.ckhd.opengame.stats.task.AndCountMoneyTask;
//import me.ckhd.opengame.stats.task.MMCountMoneyTask;
import me.ckhd.opengame.stats.task.NewUsersCountTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountJob {
	
	Logger logger = LoggerFactory.getLogger(DrdsQtzJob.class);
	
	protected void execute(){
	    logger.info("统计start!");
		try {
			String localHost = InetAddress.getLocalHost().getHostAddress();
			if(!localHost.endsWith(Global.getConfig("count.serverIp"))){
			    logger.info("统计close!");
				return ;
			}
			logger.info("统计open!");
//			MMCountMoneyTask.countMMMoney();
//			AndCountMoneyTask.countMMMoney();
			NewUsersCountTask.countNewUsers();
			ActUsersCountTask.countActUsers();  
		} catch (Throwable e) {
			logger.error("统计 count is error!", e);
		}
	}
}
