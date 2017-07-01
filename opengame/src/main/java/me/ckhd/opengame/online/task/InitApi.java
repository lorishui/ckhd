package me.ckhd.opengame.online.task;

import java.util.Map;

import me.ckhd.opengame.app.entity.AppPayList;
import me.ckhd.opengame.app.entity.BaseStation;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.app.service.AppPayListService;
import me.ckhd.opengame.app.service.BaseStationService;
import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.user.utils.RedisClientTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "${adminPath}/online/init/")
public class InitApi extends BaseController{
	@Autowired
	private OnlineService onlineService;
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	@Autowired
	private AppPayListService appPayListService;
	@Autowired
	private BaseStationService baseStationService;
	/**
	 * 获取支付配置参数
	 * 
	 * @return
	 */
	@RequestMapping(value = "getWxParam")
	@ResponseBody
	public String getParam(@RequestParam(required=false) String a,@RequestParam(required=false) String b,
			@RequestParam(required=false) String c) {
		logger.info(String.format("获取参数上传的数据:[%s]", "ckappid="+a+",ckchannelid="+b+",paytype="+c));
		PayInfoConfig payInfo = new PayInfoConfig();
		if(StringUtils.isNotBlank(a)){
			payInfo.setAddCkAppId(a);
		}
		if(StringUtils.isNotBlank(b)){
			payInfo.setChannelId(b);
		}
		if(StringUtils.isNotBlank(c)){
			payInfo.setPaytype(c);
		}
		PayInfoConfig payInfoConfig = getPayInfo(payInfo);
		JSONObject data = new JSONObject();
		JSONObject json = new JSONObject();
		if(payInfoConfig != null){
			json.put("appid", payInfoConfig.getAppid());
//			json.put("", payInfoConfig.getExInfoMap().get("mch_id"));
			data.put("payInfo", json);
			data.put("resultCode", 0);
			data.put("resultMsg", "");
		}else{
			data.put("resultCode", -1);
			data.put("resultMsg", "");
		}
		return data.toJSONString();
	}
	
	public PayInfoConfig getPayInfo(PayInfoConfig payInfoConfig) {
		return  onlineService.getPayInfo(payInfoConfig);
	}
	
	/**
	 * 获取支付配置参数
	 * 
	 * @return
	 */
	@RequestMapping(value = "getPayList",produces=" application/json;charset=UTF-8")
	@ResponseBody
	public String getPayList(@RequestBody String code) {
		logger.info(String.format("获取参数上传的数据:[%s]", code));
		JSONObject result = new JSONObject();
		try{
			if(StringUtils.isNotBlank(code)){
				Map<String,Object> map = MyJsonUtils.jsonStr2Map(code);
				AppPayList app = new AppPayList();
				app.setCkAppId(map.containsKey("ckAppId")?map.get("ckAppId").toString():null);
				app.setCkChannelId(map.containsKey("ckChannelId")?map.get("ckChannelId").toString():null);
				Map<String,Object> data = appPayListService.findOne(app);
				if(data != null && data.size() > 0){
					result.put("resultCode", 0);
					result.put("msg", "SUCCESS");
					result.put("data", new JSONObject(data));
				}else{
					result.put("resultCode", 2050);
					result.put("msg", "没有数据或者未启用");
				}
			}else{
				result.put("resultCode", 2052);
				result.put("msg", "请求参数不全");
			}
		}catch(Exception e){
			result.put("resultCode", 2051);
			result.put("msg", "内部错误");
		}
		return result.toJSONString();
	}
	
	/**
	 * 获取支付配置参数
	 * 
	 * @return
	 */
	@RequestMapping(value = "getAddr",produces=" application/json;charset=UTF-8")
	@ResponseBody
	public String getAddr(@RequestBody String code) {
		logger.info(String.format("获取参数上传的数据:[%s]", code));
		JSONObject result = new JSONObject();
		try{
			BaseStation base = new BaseStation();
			String data = baseStationService.getBaseStation(base);
			result.put("Addr", data);
		}catch(Exception e){
			result.put("resultCode", 2051);
			result.put("msg", "内部错误");
		}
		return result.toJSONString();
	}
	
}