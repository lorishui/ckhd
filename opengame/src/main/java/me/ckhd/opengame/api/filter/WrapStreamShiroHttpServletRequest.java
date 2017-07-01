/*
 * 
 */
package me.ckhd.opengame.api.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;

/**
 * 
 * @author qibiao
 *
 */
public class WrapStreamShiroHttpServletRequest extends ShiroHttpServletRequest {

	private ServletInputStream servletInputStream;

	public WrapStreamShiroHttpServletRequest(HttpServletRequest wrapped,
			ServletContext servletContext, boolean httpSessions,
			InputStream inputStream) {
		super(wrapped, servletContext, httpSessions);
		this.servletInputStream = new WrapStreamServletInputStream(inputStream);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return servletInputStream;
	}

}
