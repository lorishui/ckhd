package me.ckhd.opengame.rdapi.web;

import java.util.Map;

import me.ckhd.opengame.app.entity.ChannelCarriers;
import me.ckhd.opengame.app.service.ChannelCarriersService;
import me.ckhd.opengame.app.utils.AppCarriersUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.utils.XmlUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.mm.PayCallBackResponse;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.util.OrderStatus;
import me.ckhd.opengame.rdapi.entity.RdAppOrder;
import me.ckhd.opengame.rdapi.service.RdAppOrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("ckhd")
public class RdController extends BaseController {

	static Logger log = LoggerFactory.getLogger(RdController.class);
	
	@Autowired
	private OnlineService onlineService;
	
	@Autowired
	private RdAppOrderService rdAppOrderService;
	
	@Autowired
	private ChannelCarriersService channelCarriersService;

	@RequestMapping(value = "rdiap", method = RequestMethod.POST)
	@ResponseBody
	public String rd(@RequestBody String req) {
		try {
			log.info("rd callbackeStr="+req);
			// 收到回调，更新订单信息，通知cp下发道具。
			String msg = "";
			Map<String, Object> map = XmlUtils.Str2Map(req);
			RdAppOrder vo = new RdAppOrder();
			vo.setValue(map);
			//元变分
			if(vo.getPrice() !=null ){
				vo.setPrice(((int)(Double.parseDouble(vo.getPrice())*100))+"");
			}
			//app_paycode
			if(!rdAppOrderService.isExitOrderId(vo.getUniqueid())){
				OnlinePay onlinePay = onlineService.getRdOrderByOrderId(vo.getCpparam());
				//判断是否存在orderId信息
				vo.setOrderId(vo.getCpparam());
				if( vo.getPaycode() != null && vo.getPaycode().length()>=12 ){
					vo.setRdAppId( vo.getPaycode().substring(0, 12) );
				}
				if( onlinePay != null ){ 
					vo.setCkChannelId(onlinePay.getChannelId());
					vo.setCkAppId(onlinePay.getCkAppId());
//					vo.setRdAppId(onlinePay.getAppId());
					vo.setMmAppId(onlinePay.getAppId());
					vo.setIsOnline(onlinePay.getGameOnline());
					vo.setPayType(onlinePay.getPayType());
					if( StringUtils.isNoneBlank(onlinePay.getAppPayContent()) ){
						JSONObject json = JSONObject.parseObject(onlinePay.getAppPayContent());
						vo.setAcType(json.getString("ackType"));
						vo.setPayCount(json.getInteger("payCount"));
					}
					if ( vo.gethRet() != null && "1".equals(vo.gethRet())) {
						onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
						onlinePay.setErrMsg("");
						onlinePay.setSendStatus(OrderStatus.SEND_DOWN_ING);
					} else {
						onlinePay.setOrderStatus(OrderStatus.PAY_FAIL);
						onlinePay.setErrMsg("");
					}
					onlinePay.setChannelOrderId(vo.getUniqueid());
	
					PayCallBackResponse mmpayCallBackResponse = new PayCallBackResponse(onlinePay);
					onlinePay.setContent(mmpayCallBackResponse.getSendOrder().get("content").toString());
					onlineService.savePayInfo(onlinePay);
					// 网游
					if( onlinePay != null && "1".equals(onlinePay.getSdkType()) && "1".equals(vo.gethRet())){
						OrderSenderBoot.getInstance().add(onlinePay);
					}
				}else{
					//根据payCode的信息进行查询 截取前面12
					if( vo.getPaycode() != null && vo.getPaycode().length()>=12 ){
						vo.setCkAppId(AppCarriersUtils.getCkAppIdByAppId(vo.getPaycode().substring(0, 12)));
					}
					ChannelCarriers cc = channelCarriersService.getByCarriersChannelidAndCarriersType(vo.getChannelid(),"MM");
					if( cc != null){
						vo.setCkChannelId(cc.getChannelId());
					}
				}
				rdAppOrderService.save(vo);//保存容大信息
				if( vo.gethRet() != null && "1".equals(vo.gethRet()) ){
					msg = getResponseSuccess();
				}else{
					msg = getResponseFail();
				}
			} else {
				msg = getResponseSuccess();
			}
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