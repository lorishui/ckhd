package me.ckhd.opengame.app.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.PayRules;
import me.ckhd.opengame.app.entity.PayRulesConfig;
import me.ckhd.opengame.app.service.AppService;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.app.utils.IccidUtils;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.utils.CacheUtils;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.ipip.IPUtils;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping(value = "${adminPath}/app/payrules")
public class PayRulesController extends BaseController {

	@Autowired
	private AppService appService;
	
	/**
	 * 配置列表界面
	 */
	@RequiresPermissions("app:payrules:view")
	@RequestMapping(value = "list")
	public String list(PayRules payRules,HttpServletRequest request, HttpServletResponse response, Model model){
//		Page<PayRules> page = appService.findPayRules(new Page<PayRules>(request, response), payRules);
//		model.addAttribute("page", page);
		return "modules/app/payChangeWayList";
	}
	/**
	 * 获取配置列表界面
	 */
	@RequiresPermissions("app:payrules:view")
	@RequestMapping(value = "search")
	public String search(PayRules payRules,HttpServletRequest request, HttpServletResponse response, Model model){
		Page<PayRules> page = appService.findPayRules(new Page<PayRules>(request, response), payRules);
		model.addAttribute("page", page);
		return "modules/app/payChangeWayList";
	}
	
	/**
	 * 网络支付省份设置界面
	 * @param payRules
	 * @param model
	 * @return
	 */
	@RequiresPermissions("app:payrules:edit")
	@RequestMapping(value = "getProvinceConfig")
	@ResponseBody
	public String getProvinceConfig(HttpServletRequest request,  HttpServletResponse response,Model model){
		String payRulesId = request.getParameter("payRulesId");
		String carrier = request.getParameter("carrier");
		PayRules payRules = new PayRules();
		payRules.setId(payRulesId);
		payRules= appService.getRulesAndConfig(payRules);
		String provinceids= "";
		for(PayRulesConfig config :payRules.getConfigs()){
			if (carrier.substring(0,carrier.indexOf("_")).equals(config.getCarriers().substring(0,carrier.indexOf("_")))) {
				if(!"".equals(provinceids)){
					provinceids+=",";
				}
				provinceids+=config.getProvinceids();
			}
		}
		
		return provinceids;
	}
	
	/**
	 * 网络支付省份设置界面
	 * @param payRules
	 * @param model
	 * @return
	 */
	@RequiresPermissions("app:payrules:edit")
	@RequestMapping(value = "provinceConfig")
	public String provinceConfig(PayRules payRules, Model model){
		if (payRules.getId()!=null) {
			payRules= appService.getRulesAndConfig(payRules);
		}

		if(payRules!=null && payRules.getConfigs()!=null && payRules.getConfigs().size()>0){
			String cucc_provinceIds="";
			String ctcc_provinceIds="";
			String cmcc_provinceIds="";
			for(PayRulesConfig config:payRules.getConfigs()){
				if(config.getCarriers().startsWith("CMCC")){
					if(!"".equals(cmcc_provinceIds)){
						cmcc_provinceIds+=",";
					}
					cmcc_provinceIds+=config.getProvinceids();
				}else if(config.getCarriers().startsWith("CTCC")){
					if(!"".equals(ctcc_provinceIds)){
						ctcc_provinceIds+=",";
					}
					ctcc_provinceIds+=config.getProvinceids();
				}else if(config.getCarriers().startsWith("CUCC")){
					if(!"".equals(cucc_provinceIds)){
						cucc_provinceIds+=",";
					}
					cucc_provinceIds+=config.getProvinceids();
				}
				
			}
			payRules.setCucc_provinceIds(cucc_provinceIds);
			payRules.setCtcc_provinceIds(ctcc_provinceIds);
			payRules.setCmcc_provinceIds(cmcc_provinceIds);
			payRules.setProvinceIds(cmcc_provinceIds);
		}
		model.addAttribute("payRules", payRules);
		return "modules/app/payChangeWayProvince";
	}
	
	/**
	 * 保存配置信息
	 * @param payRules
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("app:payrules:edit")
	@RequestMapping(value = "save")
	public String save(PayRules payRules, HttpServletRequest request,  HttpServletResponse response,Model model, RedirectAttributes redirectAttributes) {
		PayRules newPayRules=null;
		try {
			newPayRules = (PayRules) payRules.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		//String id = payRules.getId();
		if(newPayRules.getStartTime()==null || "".equals(newPayRules.getStartTime())){
			newPayRules.setStartTime(DateUtils.getDateTime());
		}
		if (StringUtils.isNotBlank(payRules.getType()) && "0".equals(payRules.getType())) {
			if(!checkTime(newPayRules))
			{
				request.setAttribute("message","支付通道切换配置:'" + newPayRules.getStartTime()+(newPayRules.getIsLenght()?"":"至"+newPayRules.getEndTime())+"'时间段数据已经存在!");
			    return search(newPayRules, request, response, model);
			}
			newPayRules.setId("");
		}
		// 保存 
		appService.savePayRules(newPayRules); 
		
		/*if(StringUtils.isNotBlank(payRules.getType()) && "0".equals(payRules.getType())){
			PayRulesConfig payRulesConfig = new PayRulesConfig();
			payRulesConfig.setPr_id(id);
			List<PayRulesConfig> payrulesConfigs = appService.getPayRulesConfig(payRulesConfig);
			for(PayRulesConfig payrulesConfig:payrulesConfigs){
				payrulesConfig.setPr_id(newPayRules.getId());
				payrulesConfig.setId("");
				appService.saveConfig(payrulesConfig);
			}
		}*/
		request.setAttribute("message", "保存支付通道切换配置:'" + newPayRules.getStartTime()+"-"+(newPayRules.getIsLenght()?"至今":newPayRules.getEndTime())+"'成功");
		return search(newPayRules, request, response, model);
	}
	
	/**
	 * 保存网络支付方式
	 * @param payRules
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("app:payrules:edit")
	@RequestMapping(value = "saveIntenerPay")
	public String saveIntenerPay(PayRules payRules, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		String internetPay = request.getParameter("internetPay");
		String id = request.getParameter("pr_id");
		payRules.setInternetPay(internetPay);
		payRules.setId(id);
		// 保存 
		appService.saveIntenerPay(payRules); 
		return provinceConfig(payRules, model);
	}
	
	/**
	 * 更新配置信息
	 * @param payRules
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("app:payrules:edit")
	@RequestMapping(value = "update")
	public String update(PayRules payRules, HttpServletRequest request, Model model, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, payRules)){
			request.setAttribute("message",  "修改支付通道切换:'" + payRules.getStartTime()+"-"+payRules.getEndTime()+"'失败");
			return  search(new PayRules(), request, response, model);
		}
		// 保存 
		appService.savePayRules(payRules); 
		request.setAttribute("message",  "修改支付通道切换:'" + payRules.getStartTime()+"-"+payRules.getEndTime()+"'成功");
		return search(new PayRules(), request, response, model);
	}
	
	@RequiresPermissions("app:payrules:view")
	@RequestMapping(value = "updateForm")
	public  String updateForm(PayRules payRules,HttpServletRequest request, HttpServletResponse response, Model model){
		payRules = appService.getPayRules(payRules);
		model.addAttribute("payRules", payRules);
		return  search(payRules, request, response, model);
	}
	
	/**
	 * 配置列表界面
	 */
	@RequiresPermissions("app:payrules:view")
	@RequestMapping(value = "back")
	public String back(PayRules payRules,HttpServletRequest request, HttpServletResponse response, Model model){
		Page<PayRules> page = appService.findPayRules(new Page<PayRules>(request, response), payRules);
		model.addAttribute("page", page);
        model.addAttribute("payRules", new PayRules(payRules.getCkappId()));
        return search(new PayRules(payRules.getCkappId()), request, response, model);
	}
	
	/**
	 * 删除配置信息
	 * @param payRules
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("app:payrules:edit")
	@RequestMapping(value = "delete")
	public String delete(PayRules payRules, HttpServletRequest request, Model model,  HttpServletResponse response,RedirectAttributes redirectAttributes) {
		//删除
		appService.deletePayRules(payRules); 
		request.setAttribute("message", "删除支付通道切换配置成功!");
		Page<PayRules> page = appService.findPayRules(new Page<PayRules>(request, response), new PayRules(payRules.getCkappId()));
		model.addAttribute("page", page);
        model.addAttribute("payRules", new PayRules(payRules.getCkappId()));
        return search(new PayRules(payRules.getCkappId()), request, response, model);
	}
	
	/**
	 * 检查数据是否存在
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private boolean checkTime(PayRules payRules) {
		if (payRules.getStartTime() !=null && payRules.getEndTime()!=null && null == appService.checkPayRulesByTime(payRules) ) {
			return true;
		}
		return false;
	}
	
	@RequiresPermissions("app:payrules:edit")
	@RequestMapping(value = "saveConfig")
	public String saveConfig(PayRules payRules, HttpServletRequest request,HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		
		String id = request.getParameter("id");
		String pr_id = request.getParameter("pr_id");//获取payrules的ID
	   	String startTime=request.getParameter("startTime");//获取开始时间
	   	String endTime = request.getParameter("endTime");//获取结束时间
		String provinceids = request.getParameter("provinceIds");//获取省份ID
		String carriers = request.getParameter("carriers");//获取运营商id
		String money = "3500";
		String totalMoney = "20000";
		PayRulesConfig payRulesConfig = new PayRulesConfig(id,pr_id, provinceids,carriers) ;
		//设置金额默认值
		payRulesConfig.setMoney(money);
		payRulesConfig.setTotalmoney(totalMoney);
		payRules.setId(pr_id);
		payRules.setStartTime(startTime);
		payRules.setEndTime(endTime);
		appService.saveConfig(payRulesConfig);
		request.setAttribute("isAdd", true);
		return provinceConfig(payRules, model);
	}
	
	@RequiresPermissions("app:payrules:edit")
	@RequestMapping(value = "updateConfig")
	public String updateConfig(PayRules payRules, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {

	   String id = request.getParameter("id");//获取省份配置id
	   String pr_id = request.getParameter("pr_id");//获取payrules的ID
	   String provinceids =null;
	   String carriers = null;
	   int type = request.getParameter("type")!=null?Integer.valueOf(request.getParameter("type")):-1;
	   String data = request.getParameter("data");
	   switch (type) {
		case 0:
		case 4:
			provinceids=data;
			break;
		case 3:
			carriers=data;
			break;
		}
	   PayRulesConfig payRulesConfig = new PayRulesConfig(id,pr_id, provinceids,carriers) ;
	   payRules.setId(pr_id);
	   appService.saveConfig(payRulesConfig);
	   return provinceConfig(payRules, model);
	}
	
	@RequiresPermissions("app:payrules:edit")
	@RequestMapping(value = "updateOther")
	public String updateOther(PayRules payRules, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
	   String pr_id = request.getParameter("pr_id");//获取开始时间
	   PayRulesConfig payRulesConfig = new PayRulesConfig(request) ;
	   payRules.setId(pr_id);
	   appService.saveConfig(payRulesConfig);
	   return provinceConfig(payRules, model);
	}
	
	
	@RequiresPermissions("app:payrules:edit")
	@RequestMapping(value = "delConfig")
	public String delConfig(PayRules payRules, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		String pr_id = request.getParameter("pr_id");
		String id = request.getParameter("id");
		PayRulesConfig config = new PayRulesConfig();
		config.setId(id);
		payRules.setId(pr_id);
		//删除
		appService.deletePayRulesConfig(config); 
		return provinceConfig(payRules, model);
	}
	
	/**
	 * 前台获取json串
	 * @param payRules
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "getAllPayway")
	@ResponseBody
	public String getAllPayway(PayRules payRules,HttpServletResponse response){
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PayRules payRule = appService.getRulesAndConfig(payRules);
		APPCk appCk = AppCkUtils.getAppCkById(payRule.getCkappId());
		Map<String, Object> map= getProvince(payRule,appCk.getDiscount());
		return  JSONObject.toJSONString(map);
	}
	
	
	@SuppressWarnings("unchecked")
	private String getCachePayway(PayRules payRules, String cacheKey, String indexKey){
		Map<String, Object> map = (Map<String, Object>) CacheUtils.get(cacheKey);
		if (map != null) {
			return map.get(indexKey)==null?"":JSONObject.toJSONString(map.get(indexKey));
		}
		if (lockMap.get(cacheKey) == null) {
			lockMap.putIfAbsent(cacheKey, new byte[0]);
		}

		synchronized (lockMap.get(cacheKey)) {
			map = (Map<String, Object>)CacheUtils.get(cacheKey);
			if (map != null) {
				return map.get(indexKey)==null?"":JSONObject.toJSONString(map.get(indexKey));
			}
			List<PayRules> rules = appService.getRulesAndConfigByTime(payRules);
			for(PayRules payRule : rules){
				if(payRule == null){
					map = MapUtils.EMPTY_MAP;
				} else {
					APPCk appCk=AppCkUtils.getAppCkById(payRule.getCkappId());
					map = new ConcurrentHashMap<String, Object>();
					map=getProvince(payRule,appCk.getDiscount());
				}
				String key = "ckappid_" + getString(payRule.getCkappId())+",channel_id_" + getString(payRule.getChannelId()) + ",version_"+getString(payRule.getVersion())+",appid_"+getString(payRule.getAppid());
				CacheUtils.put(key, map);
			}
			map = (Map<String, Object>) CacheUtils.get(cacheKey);
			if(map==null){
				return "";
			}
			return map.get(indexKey)==null?"":JSONObject.toJSONString(map.get(indexKey));
		}
	}
	
	@RequestMapping(value = "getPayway")
	@ResponseBody
	public Map<String,Object> getPayway(PayRules payRules,HttpServletRequest request){
		logger.info("省份切换_客户端上传的数据为:"+payRules.toString());
		Map<String,Object> result = new HashMap<String,Object>();
		try{
			String carriers = payRules.getCarriers();
			String ckappid = payRules.getCkappId();

			// 适配前端传的simNo
			if(payRules.getSimNO() == null && request.getParameter("simNo") != null){
				payRules.setSimNO(request.getParameter("simNo"));
			}
			
			String simNO = payRules.getSimNO();
			
			// 四网切换的策略是否使用ip的省份
			boolean net4IpSwitch = DictUtils.getNet4IpSwitch();
			if(net4IpSwitch){
				//
				String code = IPUtils.getCode(me.ckhd.opengame.common.utils.StringUtils.getRemoteAddr(request));
				if(code == null && StringUtils.isNotBlank(simNO)){
					String provinceCode = IccidUtils.getProvinceCodeBySimNO(simNO.trim(), carriers);
					code = IccidUtils.getCmccProvinceCode(provinceCode,carriers);
				}
				if(code == null || code.trim().length() == 0){
					code = "default";
				}
				payRules.setProvinceCode(code);
			}else {
			
			if(StringUtils.isNotBlank(simNO) && simNO.trim().length() >=10){
				String provinceCode = IccidUtils.getProvinceCodeBySimNO(simNO.trim(), carriers);
				payRules.setProvinceCode(IccidUtils.getCmccProvinceCode(provinceCode,carriers));
			}else{
				String code = IPUtils.getCode(me.ckhd.opengame.common.utils.StringUtils.getRemoteAddr(request));
				if(code == null || code.trim().length() == 0){
					code = "default";
				}
				payRules.setProvinceCode(code);
				logger.info("getPayway ip code="+payRules.getProvinceCode());
			}
			
			}
			
			if(ckappid == null || ckappid.length() == 0){
				result.put("resultCode", -1);
				result.put("errMsg","省份切换_游戏id为空!");
				return  result;
			}
			String appIds = payRules.getAppIds();
			if(StringUtils.isNotBlank(appIds)) {
				int index = appIds.indexOf(",")<0?appIds.indexOf(";"):appIds.indexOf(",");
				if(index >= 0 ){
					// 只取mm
					appIds = appIds.substring(0,index);
					payRules.setAppid(appIds);
				}
				payRules.setAppid(appIds);
			}
			// cacheKey是ehcache的key
			String cacheKey = "ckappid_" + getString(ckappid)+",channel_id_" +  getString(payRules.getChannelId()) + ",version_"+ getString(payRules.getVersion())+",appid_"+ getString(payRules.getAppid());
			// indexKey是ehcache缓存的value(是map)取值的key
			String indexKey = carriers + payRules.getProvinceCode();
			/*if("default".equals(payRules.getProvinceCode())){
				indexKey="default";
			}*/
			String payway = getCachePayway(payRules, cacheKey, indexKey);
			if(payway == null || "".equals(payway)){
				cacheKey = "ckappid_" + getString(ckappid)+",channel_id_,version_"+ getString(payRules.getVersion())+",appid_"+ getString(payRules.getAppid());
				payway = getCachePayway(payRules, cacheKey, indexKey);
			}
			
			if(payway == null || "".equals(payway)){
				cacheKey = "ckappid_" + getString(ckappid)+",channel_id_" +  getString(payRules.getChannelId()) + ",version_"+ getString(payRules.getVersion())+",appid_";
				payway = getCachePayway(payRules, cacheKey, indexKey);
			}
			
			if(payway == null || "".equals(payway)){
				cacheKey =   "ckappid_" + getString(ckappid)+",channel_id_,version_"+getString(payRules.getVersion())+",appid_";
				payway = getCachePayway(payRules, cacheKey, indexKey);
			}
			
			if(payway == null || "".equals(payway)) {
				cacheKey =  "ckappid_" + getString(ckappid)+",channel_id_,version_,appid_";
				payway = getCachePayway(payRules, cacheKey, indexKey);
			}
			if(StringUtils.isNotBlank(payway)) {
				result.put("resultCode", 0);
				result.put("result", payway);
			}else{
				result.put("resultCode", -1);
				result.put("errMsg","省份切换未获取到相关数据");
			}
		}catch(Throwable t){
			logger.error("payway error:", t);
		}
		logger.debug(String.format("返回结果为:[%s]", result));
		return  result;
	}
	
	private String getString(String str){
		if(StringUtils.isNotBlank(str)){
			return str;
		}
		return "";
	}
	
	/**
	 * 拼装省份数据
	 * @param result
	 * @return
	 */
	private Map<String,Object> getProvince(PayRules result,Double dicount){
		Map<String,Object> map = new HashMap<String, Object>();
		for(PayRulesConfig config:result.getConfigs()){
			if(config.getProvinceids()==null || "".equals(config.getProvinceids())){
				return map;
			}
			String[] provinces = config.getProvinceids().split(",");
			String sdktype = config.getCarriers();
			for(String province:provinces){
				Map<String, Object> resultMap = getMap(config, sdktype);
				resultMap.put("onlinePayType", result.getInternetPay());
				resultMap.put("discount", dicount);
				map.put(sdktype.substring(0,sdktype.indexOf("_"))+province, resultMap);
			}
		}
		//后台暂时不返回默认配置,当不返回时,客户端默认获取default.json的数据
		//map.putAll(getDefault(result,dicount));
		return map;
	}
	
	/**
	 * 拼装各个省份对应的联通,电信,移动的map数据
	 * @param config
	 * @param type
	 * @return
	 */
	private Map<String,Object> getMap(PayRulesConfig config,String type){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("money", config.getMoney()==null?0:config.getMoney());
		map.put("totalMoney", config.getTotalmoney()==null?0:config.getTotalmoney());
		map.put("sdktype", type);
		return map;
	}
	
	// 缓存锁
	private static ConcurrentMap<String,byte[]> lockMap = new ConcurrentHashMap<String,byte[]>();
	
	/**
	 * 复制配置信息
	 * @param payRules
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("app:payrules:edit")
	@RequestMapping(value = "copy")
	public String copy(PayRules payRules, HttpServletRequest request,  HttpServletResponse response,Model model, RedirectAttributes redirectAttributes) {
		PayRules newPay = null;
		PayRulesConfig newConfig = new PayRulesConfig();
		if( StringUtils.isNotBlank(payRules.getId()) ){
			newPay = appService.getPayRules(payRules); 
			newConfig.setPr_id(payRules.getId());
			List<PayRulesConfig> list = appService.getPayRulesConfig(newConfig);
			newPay.setId(null);
			newPay.setVersion("x");
			appService.savePayRules(newPay);
			for(PayRulesConfig pay:list){
				pay.setId(null);
				pay.setPr_id(newPay.getId());
				appService.saveConfig(pay);
			}
		}	
		request.setAttribute("message", "复制支付通道切换配置:'" + newPay.getStartTime()+"-"+(newPay.getIsLenght()?"至今":newPay.getEndTime())+"'成功");
		newPay.setId("");
		return search(newPay, request, response, model);
	}
}
