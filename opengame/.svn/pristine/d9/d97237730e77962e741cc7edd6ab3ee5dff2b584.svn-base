package me.ckhd.opengame.api.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;

public class PostFilter implements Filter {

	private ServletContext servletContext;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
	}

	/**
	 * 订单信息下发给CP，spring读取之后不能再次读取。故wrap之。
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request instanceof ShiroHttpServletRequest) {
			ShiroHttpServletRequest currRequest = (ShiroHttpServletRequest) request;

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					request.getInputStream(),"utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			request.setAttribute("POST_STR", sb.toString());
			InputStream inputStream = new ByteArrayInputStream(sb.toString()
					.getBytes("utf-8"));

			chain.doFilter(new WrapStreamShiroHttpServletRequest(currRequest,
					servletContext, currRequest.isHttpSessions(), inputStream),
					response);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {

	}

}
