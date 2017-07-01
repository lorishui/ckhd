package com.chkd.count.count.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.chkd.count.common.utils.LogUtils;
import com.chkd.count.count.api.LightWeightTaskApi;

public class CountServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException {
		LogUtils.log.info("统计开始");
		LightWeightTaskApi.getInstance().count();
	}

	@Override
	public void destroy() {
		LogUtils.log.info("统计进程关闭开始");
		LightWeightTaskApi.getInstance().shutdown();
	}
}
