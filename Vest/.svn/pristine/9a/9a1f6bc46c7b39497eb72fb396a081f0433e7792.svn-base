package me.ckhd.opengame.buyflow.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.buyflow.entity.BuyFlow;
import me.ckhd.opengame.buyflow.service.BuyFlowService;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

@RequestMapping(value = "buyflow")
@Controller
public class WeADBuyFlowController extends BaseController {

	private static final String WEAD = "WeAD";
	
	@Autowired
	private BuyFlowService buyFlowService;
	
	/**
	 * http://test.com/exposure.do?imei=__IMEIORI__&idfa=272742FD-
	 * 7EDC-4338-B737-2058110ED06D&time=1481197207865&callback=http%3A%2F%2Fcb.wead.mobi%2Fcallback%2Factivate.do%3Frid%3D20161201%3A%3A7fd04121-0dfc-4552-9d7f-5c0f3f891a91%3A593605369036
	 * @return
	 */
	@RequestMapping("wead/{appParam}")
	@ResponseBody
	public String handle(@PathVariable String appParam,
			@RequestParam String imei, @RequestParam String idfa,
			@RequestParam long time, @RequestParam String callback,
			HttpServletRequest request) {

		try {
			String[] appParams = appParam.split(",");
			String deviceId = imei;
			if ("__IMEIORI__".equals(deviceId)) {
				deviceId = idfa;
			}

			BuyFlow buyFlow = new BuyFlow();
			buyFlow.setCkAppId(appParams[0]);
			buyFlow.setChildCkAppId(appParams[1]);
			if(appParams.length>=3){
				buyFlow.setAdItem(appParams[2]);
			}
			buyFlow.setMedia(WEAD);
			buyFlow.setDeviceId(deviceId);
			// ip
			buyFlow.setIp(StringUtils.getRemoteAddr(request));
			buyFlow.setMonitorTime(new Date(time));
			buyFlow.setCallback(HtmlUtils.htmlUnescape(callback));
			
			if (!buyFlowService.existBuyFlow(buyFlow)) {
				buyFlow.setState(BuyFlow.STATE.NEW.getValue());
			} else {
				buyFlow.setState(BuyFlow.STATE.OLDREGISTER.getValue());
			}
			
			buyFlowService.save(buyFlow);
		} catch (Throwable t) {
			logger.error(WEAD, t);
		}
		return "succ";
	}

}
