package me.ckhd.opengame.online.sendOrder.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import me.ckhd.opengame.online.entity.TencentCallbackData;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.task.TencentSendOrderScheduleBoot;

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
		
		TencentSendOrderScheduleBoot.getInstance().init();
	}
	
	public void destroy(){
		logger.info("OrderSenderStartHook.destroy()");
		OrderSenderBoot.getInstance().stopTask();
		
		TencentSendOrderScheduleBoot.getInstance().stopSchedule();
	}
}
