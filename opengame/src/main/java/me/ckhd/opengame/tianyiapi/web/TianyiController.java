package me.ckhd.opengame.tianyiapi.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.tianyiapi.entity.TianyiOrder;
import me.ckhd.opengame.tianyiapi.service.TianyiOrderService;
import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.util.OrderStatus;
import me.ckhd.opengame.online.util.baidu.Sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * 三网回调
 * @author yong
 * @date 2016-01-19
 */
@Controller
@RequestMapping("ckhd")
public class TianyiController {
	private static Logger logger = LoggerFactory.getLogger(TianyiController.class);
	
	private TianyiOrderService appThreeNetService;
	
	@Autowired
	private OnlineService onlineService;

	public TianyiOrderService getAppThreeNetService() {
		return appThreeNetService;
	}

	public void setAppThreeNetService(TianyiOrderService appThreeNetService) {
		this.appThreeNetService = appThreeNetService;
	}

	@RequestMapping(value = "getAppThreeNet")
	@ResponseBody
	public Map<String, Object> getAppThreeNet(@RequestBody String code,HttpServletRequest request){
		logger.info(String.format("三网合一计费回调传入数据:[%s]", code));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(code==null){
			resultMap.put("resultCode", "-1");
			resultMap.put("resultDesc", "没有传入回调参数");
			return resultMap;
		}
		Map<String, Object> map = MyJsonUtils.jsonStr2Map(code);
		TianyiOrder three = new TianyiOrder();
		three.setChargeResult(Integer.parseInt(map.get("chargeResult")==null?"":map.get("chargeResult").toString()));
		three.setOrderId(map.get("orderId")==null?"":map.get("orderId").toString());
		three.setPayType(Integer.parseInt(map.get("payType")==null?"":map.get("payType").toString()));
		three.setPayTime(map.get("payTime")==null?"":map.get("payTime").toString());
		three.setIMSI(map.get("IMSI")==null?"":map.get("IMSI").toString());
		three.setChannel(map.get("channel")==null?"":map.get("channel").toString());
		three.setPrice(map.get("price")==null?"":map.get("price").toString());
		three.setVersion(map.get("version")==null?"":map.get("version").toString());
		three.setSig(map.get("sig")==null?"":map.get("sig").toString());
		try {
			appThreeNetService.save(three);
			logger.info("三网合一计费入库成功");
			
			OnlinePay onlinePay = null;
//					onlineService
//					.getOrderByOrderId(mmAppOrder.getExtendData());
			
			onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
			onlinePay.setErrMsg("");
//			onlinePay.setChannelOrderId();
			onlinePay.setSendStatus(OrderStatus.SEND_DOWN_ING);
			onlinePay.setSendNum(0);
			
			onlineService.savePayInfo(onlinePay);
			OrderSenderBoot.getInstance().add(onlinePay);
			
		} catch (Exception e) {
			logger.info("三网合一计费入库失败");
			e.printStackTrace();
		}
		String sig = Sdk.md5(three.getOrderId()+three.getChannel()+three.getIMSI()+"c723e9f2ae4e4c7188094526013b1b80");
		if(sig.equals(three.getSig())){
			resultMap.put("resultCode", "0000");
			resultMap.put("resultDesc", "成功");
		}else{
			resultMap.put("resultCode", "0000");
			resultMap.put("resultDesc", "成功");
			/*resultMap.put("resultCode", "fail");
			resultMap.put("resultDesc", "传入参数错误");*/
		}
		logger.info(String.format("三网合一计费回调返回数据:[%s]", JSONObject.toJSONString(resultMap)));
		return resultMap;
	}
}
