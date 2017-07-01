package me.ckhd.opengame.countly.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import me.ckhd.opengame.countly.task.OrderForwardCountlyBoot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderForwardCountlyStartHook extends HttpServlet{

	private static final long serialVersionUID = 1L;

	protected static Logger logger = LoggerFactory.getLogger(OrderForwardCountlyStartHook.class);
	
	@Override
	public void init() throws ServletException{
		
		logger.info("OrderForwardCountlyBoot.getInstance().startTask()");
		// 开启任务
		OrderForwardCountlyBoot.getInstance().startTask();
		
	}
	
	public void destroy(){
		logger.info("OrderForwardCountlyBoot.destroy()");
		OrderForwardCountlyBoot.getInstance().stopTask();
	}
}
