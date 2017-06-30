package me.ckhd.opengame.online.request.andgame;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.request.BaseLoginCallBackRequest;

public class LoginCallBackRequest extends BaseLoginCallBackRequest{
	
	public LoginCallBackRequest(String code,HttpServletRequest request,PayInfoConfig _payInfoConfig) {
		super(code,request);
		map.put("userId", request.getParameter("userId"));
		map.put("key", request.getParameter("key"));
		map.put("cpId", request.getParameter("cpId"));
		map.put("cpServiceId", request.getParameter("cpServiceId"));
		map.put("channelId", request.getParameter("channelId"));
		map.put("p", request.getParameter("p"));
		map.put("region", request.getParameter("region"));
		map.put("Ua", request.getParameter("Ua"));
	}
}
