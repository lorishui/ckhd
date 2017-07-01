package me.ckhd.opengame.online.version;

import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service("onlineVersion120")
public class OnlineVersion120 extends OnlineVersion110 {

	public String pay(JSONObject codeJson, HttpServletRequest request) {
		return super.pay(codeJson, request);
	}
}
