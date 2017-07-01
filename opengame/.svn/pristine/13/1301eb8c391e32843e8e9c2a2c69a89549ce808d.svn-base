package me.ckhd.opengame.hsapi.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.app.service.ChannelCarriersService;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.hsapi.entity.HsAppOrder;
import me.ckhd.opengame.hsapi.service.HsAppOrderService;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.hs.PayCallBackResponse;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.util.OrderStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("ckhd")
public class HsController extends BaseController {

	static Logger log = LoggerFactory.getLogger(HsController.class);
	
	@Autowired
	private OnlineService onlineService;
	
	@Autowired
	private HsAppOrderService hsAppOrderService;
	
	@Autowired
	private ChannelCarriersService channelCarriersService;

	@RequestMapping(value = "hsiap")
	@ResponseBody
	public String rd(HttpServletRequest request) {
		try {
			request.setCharacterEncoding("utf-8");
			Map<String,Object> map = new HashMap<String, Object>();
			for(Object key:request.getParameterMap().keySet()){
				map.put(key.toString(), request.getParameter(key.toString()));
			}
			log.info("hs callbackeStr:"+map.toString());
			// 收到回调，更新订单信息，通知cp下发道具。
			String msg = "";
			HsAppOrder vo = new HsAppOrder();
			vo.setValue(map);
			//app_paycode
			if(!hsAppOrderService.isExitOrderId(vo.getTradeId())){
				OnlinePay onlinePay = onlineService.getOrderByOrderId(vo.getTradeId());
				//判断是否存在orderId信息
				if( onlinePay != null ){ 
					vo.setCkChannelId(onlinePay.getChannelId());
					vo.setCkAppId(onlinePay.getCkAppId());
//					vo.setRdAppId(onlinePay.getAppId());
					vo.setMmAppId(onlinePay.getAppId());
					vo.setIsOnline(onlinePay.getGameOnline());
//					vo.setChannelId(onlinePay.getChannelId());
//					vo.setPayType(onlinePay.getPayType());
					if( onlinePay.getAppPayContent() != null && onlinePay.getAppPayContent().trim().length() > 0 ){
						JSONObject json = JSONObject.parseObject(onlinePay.getAppPayContent());
						vo.setAckType(json.getInteger("ackType"));
						vo.setPayCount(json.getInteger("payCount"));
					}
					//设置onlinePay
					onlinePay.setChannelOrderId(vo.getOrderid());
					onlinePay.setActualAmount(vo.getFee()+"");
					if ( vo.getPaystatus() != null && "1".equals(vo.getPaystatus())) {
						onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
						onlinePay.setErrMsg("");
						onlinePay.setSendStatus(OrderStatus.SEND_DOWN_ING);
					} else {
						onlinePay.setOrderStatus(OrderStatus.PAY_FAIL);
						onlinePay.setErrMsg("");
					}
	
					PayCallBackResponse hsPayCallBackResponse = new PayCallBackResponse(onlinePay);
					onlinePay.setContent(hsPayCallBackResponse.getSendOrder().get("content").toString());
					onlineService.savePayInfo(onlinePay);
					// 网游
					if( onlinePay != null && "1".equals(onlinePay.getSdkType()) && "1".equals(vo.getPaystatus())){
						OrderSenderBoot.getInstance().add(onlinePay);
					}
				}
				hsAppOrderService.save(vo);//保存容大信息
			} 
			msg = getResponseSuccess();
			log.info("hs iap result="+msg);
			return msg;
		} catch (Exception ex) {
			log.error(this.getClass().getName()+" Operation Error", ex);
			String fail = getResponseFail();
			return fail;
		}
	}
	
	public String getResponseSuccess(){
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<response>\r\n\t<returnMsg>ok</returnMsg>\r\n</response>";
	}
	
	public String getResponseFail(){
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<response>\r\n\t<returnMsg>fail</returnMsg>\r\n</response>";
	}
}