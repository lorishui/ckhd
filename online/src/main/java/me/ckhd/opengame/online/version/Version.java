package me.ckhd.opengame.online.version;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.handle.BaseHandle;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.util.OrderStatus;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;


public abstract class Version {
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public abstract String login(JSONObject codeJson,HttpServletRequest request);

	public abstract String pay(JSONObject codeJson,HttpServletRequest request);
	
	public abstract String callback(String code,String engName,HttpServletRequest request,HttpServletResponse response);
	
	public abstract String callbackbc(String code,String engName,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 同步发货给cp
	 */
	public Boolean syncCp(OnlinePay newPay,BaseHandle handle) {
		APPCk appCk =AppCkUtils.getAppCkByIdAndChild(newPay.getCkAppId(),newPay.getChildCkAppId());
		//5.判断是否为网游或者是否配置下发地址,如果非网游或者没有设置下发地址则不下发,反之下发到游戏
		if( StringUtils.isNotBlank(appCk.getPayCallbackUrl()) || StringUtils.isNotBlank(newPay.getCallBackUrl()) ){
			newPay.setSercetKey(appCk.getSecretKey());
			Map<String, Object> map = handle.getSendOrder(newPay);
			newPay.setSendNum(Integer.valueOf(map.get("sendNum")==null?"0":map.get("sendNum").toString()));
			newPay.setOrderStatus(OrderStatus.PAY_SUCCESS);
			newPay.setSendStatus(OrderStatus.SEND_DOWN_ING);
			newPay.setErrMsg("");
			newPay.setContent(map.get("content").toString());
			//加入发货队列
			OrderSenderBoot.getInstance().add(newPay);
			return true;
		}
		return false;
	}
}
