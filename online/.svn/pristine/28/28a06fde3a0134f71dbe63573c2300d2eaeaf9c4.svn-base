package me.ckhd.opengame.online.sendOrder.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import me.ckhd.opengame.ipip.IPUtils;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.task.TencentSendOrderScheduleBoot;
import me.ckhd.opengame.reyun.task.RenyunTaskBoot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderSenderStartHook extends HttpServlet{

	private static final long serialVersionUID = 1L;

	protected static Logger logger = LoggerFactory.getLogger(OrderSenderStartHook.class);
	
	@Override
	public void init() throws ServletException{
		
		logger.info("OrderSenderBoot.getInstance().startTask()");
		// 开启任务
		OrderSenderBoot.getInstance().startTask();
		//ip定时刷新机制启动
		IPUtils.startUp();
		//开启ip
		IPUtils.init();
		
		TencentSendOrderScheduleBoot.getInstance().init();
		//启动热云上报任务
		RenyunTaskBoot.startUp();
	}
	
	public void destroy(){
		logger.info("OrderSenderStartHook.destroy()");
		OrderSenderBoot.getInstance().stopTask();
		
		TencentSendOrderScheduleBoot.getInstance().stopSchedule();
		//热云上报接口启动器关闭
		RenyunTaskBoot.shutdown();
	}
}
