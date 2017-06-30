package me.ckhd.opengame.online.task;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayCodeConfig;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.service.OnlineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 与SDK服务端对接
 * @author leo
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/online/channel")
public class ChannelPayApi extends BaseController {
	
	//private String loginCallbackpackagerequest="me.ckhd.opengame.online.request.%s.LoginCallBackRequest";
	//private String loginCallbackpackageresponse="me.ckhd.opengame.online.response.%s.LoginCallBackResponse";

	@Autowired
	private OnlineService onlineService;
	/**
	 * 前端调用后台验证订单状态
	 * @return
	 */
	@RequestMapping(value = "payValidate/{name}")
	@ResponseBody
	public void payValidate(@RequestBody String payCode, @PathVariable String name){
		
	}
	
	/**
	 * 回调转发,为了解决类似于优酷不能传递带参数的回调地址
	 * @param request
	 * @param response
	 * @param channelName
	 * @param ckappid
	 * @param channelid
	 */
	@RequestMapping(value = "callBack/{channelName}/{ckappid}/{channelid}")
	public void doPost(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String channelName,
			@PathVariable String ckappid, @PathVariable String channelid) {
		try {
			String url = "/ck/online/channel/payCallBack/"+channelName+"?ckappid="+ckappid+"&channelid="+channelid+"";
			request.getRequestDispatcher(url).forward(request, response);
		}  catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 渠道登陆回调
	 * @param payCode
	 * @return
	 */
	@RequestMapping(value = "loginCallBack/{ckappid}")
	@ResponseBody
	public String loginCallBack(@RequestBody String loginCode,HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,@PathVariable String ckappid){
		logger.info(String.format("渠道登陆回调的数据:[%s]", loginCode));
		return "0";
	}
		
	public PayInfoConfig getPayInfo(OnlinePay onlinePay) {
		PayInfoConfig payInfo = new PayInfoConfig();
		payInfo.setCkAppId(onlinePay.getCkAppId());
		payInfo.setChannelId(onlinePay.getChannelId());
		payInfo.setPaytype(onlinePay.getPayType());
		payInfo.setCarrierAppId(onlinePay.getAppId());
		return  onlineService.getPayInfo(payInfo);
	}

	public PayCodeConfig getPayCodeConfig(OnlinePay onlinePay){
		PayCodeConfig payCodeConfig = new PayCodeConfig();
		payCodeConfig.setCkAppId(onlinePay.getCkAppId());
		payCodeConfig.setChannelId(onlinePay.getChannelId());
		payCodeConfig.setPaytype(onlinePay.getPayType());
		if( StringUtils.isNotBlank(onlinePay.getPayType())){
			Integer payType = Integer.valueOf(onlinePay.getPayType());
			if( payType > 120){
				payCodeConfig.setPaytype("141");
			}else{
				payCodeConfig.setPaytype(onlinePay.getPayType()==null?"":onlinePay.getPayType());
			}
		}else{
			payCodeConfig.setPaytype("");
		}
		payCodeConfig.setProductId(onlinePay.getProductId());
		return onlineService.getPayCode(payCodeConfig);
	}
		
}
