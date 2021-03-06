package me.ckhd.opengame.buyflow.web;

import java.net.URLDecoder;
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
public class HuiChuanBuyFlowController extends BaseController {

	private static final String HUICHUAN = "huichuan";
	
	@Autowired
	private BuyFlowService buyFlowService;
	
	/**
	 * http://ol.haifurong.cn/buyflow/huichuan/1,1,1?idfa={IDFA_SUM}&time={TS}&callback={CALLBACK}
	 *
	 */
	@RequestMapping("huichuan/{appParam}")
	@ResponseBody
	public String handle(@PathVariable String appParam,
			@RequestParam(value="imei", required=false) String imei, @RequestParam(value="idfa", required=false) String idfa,
			@RequestParam long time, @RequestParam String callback,
			HttpServletRequest request) {

		try {
			String[] appParams = appParam.split(",");
			String deviceId = imei;
			if (StringUtils.isBlank(deviceId)) {
				deviceId = idfa;
				if(StringUtils.isBlank(deviceId)){
					return "succ";
				}
			}

			BuyFlow buyFlow = new BuyFlow();
			buyFlow.setCkAppId(appParams[0]);
			buyFlow.setChildCkAppId(appParams[1]);
			if(appParams.length>=3){
				buyFlow.setAdItem(appParams[2]);
			}
			buyFlow.setMedia(HUICHUAN);
			buyFlow.setDeviceId(deviceId);
			buyFlow.setMD5DeviceId(deviceId);
			// ip
			buyFlow.setIp(StringUtils.getRemoteAddr(request));
			buyFlow.setMonitorTime(new Date(time));
			if(!"{CALLBACK}".equals(callback)){
				buyFlow.setCallback(HtmlUtils.htmlUnescape(URLDecoder.decode(callback,"UTF-8")));
			}
			
			if (!buyFlowService.existBuyFlow(buyFlow)) {
				buyFlow.setState(BuyFlow.STATE.NEW.getValue());
			} else {
				buyFlow.setState(BuyFlow.STATE.OLDREGISTER.getValue());
			}
			
			buyFlowService.save(buyFlow);
		} catch (Throwable t) {
			logger.error(HUICHUAN, t);
		}
		return "succ";
	}

}
