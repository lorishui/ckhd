package me.ckhd.opengame.online.version;

import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Version {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public abstract String login(JSONObject paramJSONObject, HttpServletRequest paramHttpServletRequest);

	public abstract String pay(JSONObject paramJSONObject, HttpServletRequest paramHttpServletRequest);

	public abstract String callback(String paramString1, String paramString2, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
}
