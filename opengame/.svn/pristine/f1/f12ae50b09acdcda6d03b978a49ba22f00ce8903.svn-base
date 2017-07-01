package me.ckhd.opengame.app.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PaymentWay;
import me.ckhd.opengame.app.service.PaymentWayService;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.ipip.IPUtils;

import org.apache.commons.collections.MapUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 支付通道配置  Controller
 * @author wesley
 * @version 2015-06-30
 */
@Controller
@RequestMapping(value = "${adminPath}/app/paymentway")
public class PaymentWayController extends BaseController{
	
	
	@Autowired
	private PaymentWayService paymentWayService;
	
	@ModelAttribute
	public  PaymentWay get(@RequestParam(required=false) String id){
		if (StringUtils.isNotBlank(id)) {
			return  paymentWayService.get(id);
		}else{
			return  new PaymentWay();
		}
	}
	
	
	@RequiresPermissions("app:paymentway:view")
	@RequestMapping(value = {"list", ""})
	public String list(PaymentWay paymentWay,HttpServletRequest request, HttpServletResponse response, Model model) {
		
		if(request.getAttribute("ckappId") != null){
			paymentWay.setCkappId((String)request.getAttribute("ckappId"));
			paymentWay.setVersion((String)request.getAttribute("version"));
			paymentWay.setChannelId((String)request.getAttribute("channelId"));
		}
		
		Page<PaymentWay> page = paymentWayService.findPage(new Page<PaymentWay>(request, response), paymentWay);
        model.addAttribute("page", page);
		return "modules/app/paymentwayList";
		
	}
	
	@RequiresPermissions("app:paymentway:view")
	@RequestMapping(value = "form")
	public String form(PaymentWay paymentway, Model model) {
		model.addAttribute("paymentway", paymentway);
		return "modules/app/paymentwayForm";
	}
	
	@RequiresPermissions("app:paymentway:edit")
	@RequestMapping(value = "save")
	public String save(PaymentWay paymentWay, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, paymentWay)){
			return form(paymentWay, model);
		}
		paymentWay.setVersion(request.getParameter("version"));
		paymentWay.setChannelId(request.getParameter("channelId"));
		paymentWay.setProvinceCode(request.getParameter("provinceCode"));
		
		// 保存 
		paymentWayService.save(paymentWay); 
//		addMessage(redirectAttributes, "保存支付通道'" + paymentWay.getPayWaySign() + "'成功");
		
		redirectAttributes.addAttribute("ckappId", request.getParameter("ckappId"));
		redirectAttributes.addAttribute("version", request.getParameter("version"));
		
		return "redirect:" + adminPath + "/app/paymentway/list?repage";
	}
	
	@RequiresPermissions("app:paymentway:edit")
	@RequestMapping(value = "delete")
	public String delete(PaymentWay paymentWay, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		paymentWayService.delete(paymentWay);
		return "redirect:" + adminPath + "/app/paymentway/list?repage";
	}
	
	@RequestMapping(value = "change")
	@RequiresPermissions("app:paymentway:edit")
	public  String change(PaymentWay paymentWay , RedirectAttributes redirectAttributes, HttpServletRequest request){
		if("0".equals(paymentWay.getDelFlag())){
			paymentWay.setDelFlag("1");
		}else{
			paymentWay.setDelFlag("0");
		}
		paymentWayService.save(paymentWay);
//		addMessage(redirectAttributes, "改变支付通道配置成功");
		
		redirectAttributes.addAttribute("ckappId", request.getParameter("conCkappId"));
		redirectAttributes.addAttribute("version", request.getParameter("conVersion"));
		redirectAttributes.addAttribute("channelId", request.getParameter("conChannelId"));
		
		return  "redirect:" + adminPath + "/app/paymentway/list?repage";
		
	}
	
	/**
	 * 缓存避免大并发
	 * @param paymentWay
	 * @return
	 */
	@RequestMapping(value = "payway")
	@ResponseBody
	public Map<String,Object> payway(PaymentWay paymentWay,HttpServletRequest request){
		
		logger.debug("SimNO = " + paymentWay.getSimNO());
		logger.debug("Carriers = " + paymentWay.getCarriers());
		logger.debug("AppIds = " + paymentWay.getAppIds());
		logger.debug("Version = " + paymentWay.getVersion());
		logger.debug("ChannelId = " + paymentWay.getChannelId());
		
		Map<String,Object> result = new HashMap<String,Object>();
		try{
			String simNO = paymentWay.getSimNO();
			if(simNO != null && simNO.trim().length() >=10){
				paymentWay.setProvinceCode(simNO.trim().substring(8, 10));
			}else{
				paymentWay.setProvinceCode(IPUtils.getCode(StringUtils.getRemoteAddr(request)));
			}
			
			String carriers = paymentWay.getCarriers();
			String appIds = paymentWay.getAppIds();
			if(carriers == null || carriers.length() == 0){
				return result;
			}
			if(appIds == null || appIds.length() == 0){
				return result;
			} else {
				int index = appIds.indexOf(",");
				if(index >= 0){
					// 只取mm
					appIds = appIds.substring(0,index);
					paymentWay.setAppIds(appIds);
				}
				index = appIds.indexOf(";");
				if(index >= 0){
					// 只取mm
					appIds = appIds.substring(0,index);
					paymentWay.setAppIds(appIds);
				}
			}
			// cacheKey是ehcache的key
			String cacheKey = "payway_carriers_"+ carriers + ",appIds_" + appIds;
			// indexKey是ehcache缓存的value(是map)取值的key，版本+渠道+省份
			String indexKey = "version_" + paymentWay.getVersion() + ",channel_id_" + paymentWay.getChannelId() + ",province_code_" + paymentWay.getProvinceCode();
			String payway = getCachePayway(paymentWay, cacheKey, indexKey);
			if(payway == null){
				// 版本+渠道
				indexKey = "version_" + paymentWay.getVersion() + ",channel_id_" + paymentWay.getChannelId() + ",province_code_";
				payway = getCachePayway(paymentWay, cacheKey, indexKey);
			}
			if(payway == null) {
				// 省份+版本
				indexKey = "province_code_" + paymentWay.getProvinceCode() + ",version_" + paymentWay.getVersion();
				payway = getCachePayway(paymentWay, cacheKey, indexKey);
			}
			if(payway == null) {
				// 仅省份
				indexKey = "province_code_" + paymentWay.getProvinceCode() + ",version_";
				payway = getCachePayway(paymentWay, cacheKey, indexKey);
			}
			if(payway == null){
				// 版本
				indexKey = "version_" + paymentWay.getVersion() + ",channel_id_,province_code_";
				payway = getCachePayway(paymentWay, cacheKey, indexKey);
			}
			if(payway == null) {
				indexKey = "version_,channel_id_,province_code_";
				payway = getCachePayway(paymentWay, cacheKey, indexKey);
			}
			if(payway != null) {
				result.put("payway", payway);
			}
		}catch(Throwable t){
			logger.error("payway error:", t);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private String getCachePayway(PaymentWay paymentWay, String cacheKey, String indexKey){
		Map<String, String> map = (Map<String, String>) CacheUtils.get(cacheKey);
		if (map != null) {
			return map.get(indexKey);
		}
		if (lockMap.get(cacheKey) == null) {
			lockMap.putIfAbsent(cacheKey, new byte[0]);
		}

		synchronized (lockMap.get(cacheKey)) {
			map = (Map<String, String>) CacheUtils.get(cacheKey);
			if (map != null) {
				return map.get(indexKey);
			}

			List<Map<String,String>> paywayList = paymentWayService.paywayList(paymentWay);
			if(paywayList == null){
				map = MapUtils.EMPTY_MAP;
			} else {
				map = new ConcurrentHashMap<String, String>();
				for(Map<String,String> paywayMap:paywayList){
					map.put(paywayMap.get("key"),paywayMap.get("pay_way_sign"));
				}
			}
			CacheUtils.put(cacheKey, map);
			return map.get(indexKey);
		}
	}
	
	// 缓存锁
	private static ConcurrentMap<String,byte[]> lockMap = new ConcurrentHashMap<String,byte[]>();
}
