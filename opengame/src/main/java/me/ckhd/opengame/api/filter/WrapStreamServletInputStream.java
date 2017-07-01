/*
 * www.ckhd.me
 */
package me.ckhd.opengame.api.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;
/**
 * 包装流
 * @author qibiao
 *
 */
public class WrapStreamServletInputStream extends ServletInputStream{

	private InputStream inputStream;
	
	public WrapStreamServletInputStream(InputStream inputStream){
		this.inputStream = inputStream;
	}
	
	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

}
