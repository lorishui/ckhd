package me.ckhd.opengame.drds.task;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.Reflections;
import me.ckhd.opengame.drds.entity.EventEntity;
import me.ckhd.opengame.drds.entity.MyAnnotation;
import me.ckhd.opengame.drds.service.EventService;
import me.ckhd.opengame.evnet.entity.AppEventStat;
import me.ckhd.opengame.ipip.IP;
import me.ckhd.opengame.sys.entity.Dict;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
public class EventController {

	protected static Logger logger = LoggerFactory.getLogger(EventController.class);

	@Autowired
	private EventService eventService;
	
	@RequestMapping("event")
	@ResponseBody
	public String event(@RequestBody String code, HttpServletRequest request) {
		// 解析事件，存储事件
		logger.info("收到是事件数据：" + code);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> list = MyJsonUtils.jsonArrayStr2List(code);
			int uploadTime=getUploadTime();
			for(Map<String, Object> map:list){
				EventEntity event = getEntity(map);
				if(StringUtils.isNotBlank(event.getAndroidid())){
					if("unknown".equalsIgnoreCase(event.getImei()) || "0".equals(event.getImei()) || StringUtils.isBlank(event.getImei())){
						// 这个时候imei存androidid做pk
						event.setImei(event.getAndroidid().trim());
					}
				}
				// 转大写
				if(event.getSignMD5() != null){
					event.setSignMD5(event.getSignMD5().toUpperCase());
				}
				
				if("200".equals(event.getChannelid())){
					// 苹果全部是正版 update@2016-12-26
					event.setTrustSign(1);
				} else {
					String signMD5 = event.getSignMD5();
					if (StringUtils.isBlank(signMD5)) {
						event.setTrustSign(2);
					} else if (DictUtils.isTrustSignMd5(signMD5)) {
						event.setTrustSign(1);
					} else {
						event.setTrustSign(0);
					}
				}
				// ip
				String ip = me.ckhd.opengame.common.utils.StringUtils.getRemoteAddr(request);
				event.setIp(ip);
				try {
					String[] ipArr = IP.find(ip);
					if (ipArr.length >= 2) {
						event.setProviceName(ipArr[1]);
						event.setCityName(ipArr[2]);
					}
				} catch (Throwable t) {
					// NOP
				}
				eventService.saveEventData(event);
				if(event.getType()==1){
					eventService.saveAcount(event);
				}
			}
			resultMap.put("resultCode", 0);
			resultMap.put("errMsg", "");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("time", uploadTime);
			resultMap.put("result", map);
		} catch (Throwable t) {
//			e.printStackTrace();
			logger.error("event save error,event is:\r\n" + code, t);
			resultMap.put("resultCode", -1);
			resultMap.put("errMsg", "事件保存失败");
		}
		String result = JSONObject.toJSONString(resultMap);
		logger.info(String.format("事件保存完成,返回数据到客户端:[%s]",result));
		return result;
	}
	
	private Integer getUploadTime(){
		List<Dict> dicts = DictUtils.getDictList("uploadTime");
		if(dicts==null || dicts.size()<=0){
			return 0;
		}
		Dict dict = dicts.get(0);
		Integer uploadTime = (dict.getValue()==null?0:Integer.parseInt(dict.getValue()));
		return uploadTime;
	}
	
	//根据反射获取对象,并给对象赋值
	private EventEntity getEntity(Map<String, Object> map) {
		try {
			Class<EventEntity> cla = EventEntity.class;
			EventEntity baseAppEvent = (EventEntity)cla.newInstance();
			Field[] fields = cla.getDeclaredFields();
			setValue(baseAppEvent,fields,map);

			// 苹果fix ad_type
			if ("200".equals(baseAppEvent.getChannelid())) {
				Object adType = map.get("ad_type");
				if (adType != null) {
					// banner=0, interstitial=1, video=2;
					if (adType.toString().equals("banner")) {
						baseAppEvent.setAdType(0);
					} else if (adType.toString().equals("interstitial")) {
						baseAppEvent.setAdType(1);
					} else if (adType.toString().equals("video")) {
						baseAppEvent.setAdType(2);
					}
				}
			}

			return baseAppEvent;
		} catch (InstantiationException e) {
			logger.warn("event data exception", e);
		} catch (Exception e) {
			logger.warn("event data exception", e);
		}
		return null;
	}
	private void setValue(Object obj,Field[] fields,Map<String,Object> map){
		if(fields!=null){
			for(Field field:fields){
				MyAnnotation myAnnotation = field.getAnnotation(MyAnnotation.class);
				if(myAnnotation!=null){
					String key=myAnnotation.name();
					
					Object o = map.get(key.toLowerCase());
					if(o!=null){
						if("java.lang.String".equals(field.getType().getName())){
							Reflections.setFieldValue(obj, field.getName(), o.toString());
						}else if("int".equals(field.getType().getName())){
							try{
								Reflections.setFieldValue(obj, field.getName(), Integer.valueOf(o.toString()));
							}catch(Throwable t){
								Reflections.setFieldValue(obj, field.getName(), 0);
							}
						}else if("long".equals(field.getType().getName())){
							try{
								Reflections.setFieldValue(obj, field.getName(), Long.valueOf(o.toString()));
							}catch(Throwable t){
								Reflections.setFieldValue(obj, field.getName(), 0L);
							}
						}else if("boolean".equals(field.getType().getName())){
							try{
								Reflections.setFieldValue(obj, field.getName(), (Boolean)o);
							}catch(Throwable t){
								Reflections.setFieldValue(obj, field.getName(), false);
							}
						}
					}
				}
			}
		}
	}
	
	@RequestMapping("${adminPath}/list")
	public String list(AppEventStat appEventStat,Model model){
		if(StringUtils.isNotBlank(appEventStat.getCkAppId()) && StringUtils.isNotBlank(appEventStat.getStartDate()) 
				&& StringUtils.isNotBlank(appEventStat.getEndDate())){
			model.addAttribute("data",eventService.crackSDKNum(appEventStat));
		}
		model.addAttribute("appevent",appEventStat);
		return "modules/stats/eventList";
	}
}