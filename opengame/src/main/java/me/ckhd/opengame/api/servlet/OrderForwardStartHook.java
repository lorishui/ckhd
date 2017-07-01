package me.ckhd.opengame.api.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import me.ckhd.opengame.api.task.FlowForwardBoot;
import me.ckhd.opengame.api.task.OrderForwardBoot;
import me.ckhd.opengame.app.task.CellInfoStatsTask;
import me.ckhd.opengame.ipip.IP;
import me.ckhd.opengame.ipip.IPUtils;
import me.ckhd.opengame.stats.task.GameReportTask;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderForwardStartHook extends HttpServlet{

	private static final long serialVersionUID = 1L;

	protected static Logger logger = LoggerFactory.getLogger(OrderForwardStartHook.class);
	
	@Override
	public void init() throws ServletException{
		
		logger.info("OrderForwardBoot.getInstance().startTask()");
		// 开启任务
		OrderForwardBoot.getInstance().startTask();
		
		FlowForwardBoot.getInstance().startTask();
		//ip定时刷新机制启动
		IPUtils.startUp();
		String value = DictUtils.getDictLabel("value", "auto_start", "0");
		if(value != null && value.equals("1")){
			IPUtils.init();
		}
		GameReportTask.getInstance().startTask();
		
		//
		CellInfoStatsTask.getInstance().start();
	}
	
	public void destroy(){
		logger.info("OrderForwardStartHook.destroy()");
		OrderForwardBoot.getInstance().stopTask();
		
		FlowForwardBoot.getInstance().stopTask();
		IPUtils.stopTask();
		IP.stopSchedule();
		GameReportTask.getInstance().stop();
		
		//
		CellInfoStatsTask.getInstance().shutdown();
	}
}
