package me.ckhd.opengame.app.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.CfgParamFeilds;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.app.entity.Channel;
import me.ckhd.opengame.app.service.AppService;
import me.ckhd.opengame.app.service.CfgParamFeildsService;
import me.ckhd.opengame.app.service.CfgService;
import me.ckhd.opengame.app.service.CfgparamService;
import me.ckhd.opengame.app.service.ChannelService;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.app.utils.PropertyConversion;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.utils.excel.ExportExcel;
import me.ckhd.opengame.common.utils.excel.ImportExcel;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.sys.entity.Dict;
import me.ckhd.opengame.sys.service.DictService;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "${adminPath}/app")
public class CfgparamController  extends BaseController{
	
//	// 严格省份：广东19，上海09，北京01，海南21，江苏10，黑龙江08
//	public static Set<String> STRICT_PROVICES = new HashSet<String>();
//
//	static {
//		STRICT_PROVICES.add("19");
//		STRICT_PROVICES.add("09");
//		STRICT_PROVICES.add("01");
//		STRICT_PROVICES.add("21");
//		STRICT_PROVICES.add("10");
//		STRICT_PROVICES.add("08");
//	}

	@Autowired
	private AppService appService;
	
	@Autowired
	private CfgparamService cfgparamService;
	
	@Autowired
	private CfgParamFeildsService cfgParamFeildsService;
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private DictService dictService;
	
	@RequestMapping(value = "getCfgparam")
	@ResponseBody
	public Map<String,Object> getCfgparam(@RequestBody String code, HttpServletRequest request){
		logger.info(String.format("获取初始化数据,客户端上传的数据:[%s]", code));
		
		String clientIp = StringUtils.getRemoteAddr(request);
		
		Map<String,Object> result = new HashMap<String,Object>();
		try{
			if (StringUtils.isBlank(code)) {
				result.put("resultCode", -1);
				result.put("errMsg","获取初始化数据时,上传的数据为空!");
				String returnStr = (result == null ? null : JSONObject.toJSONString(result));
				logger.info(String.format("返回客户端的初始化数据信息:[%s]", returnStr));
				return  result;
			}
			Cfgparam cfgparam = new Cfgparam(JSONObject.parseObject(code));
			// 签名值转大写
			if(cfgparam.getSignMD5() != null){
				cfgparam.setSignMD5(cfgparam.getSignMD5().toUpperCase());
			}
			//判断是否上传游戏Id
			if(StringUtils.isBlank(cfgparam.getCkAppId())){
				result.put("resultCode", -1);
				result.put("errMsg","ckAppId is empty.");
				String returnStr = (result == null ? null : JSONObject.toJSONString(result));
				logger.info(String.format("返回客户端的初始化数据信息:[%s]", returnStr));
				return result;
			}
			// mmappid
			if (cfgparam.getMmAppId() == null) {
				cfgparam.setMmAppId("");
			}
			
//			String simNO = cfgparam.getSimNO();
//			String carriers = cfgparam.getCarriers();
//			
//			// 基站版本
//			boolean cellinfoVersionSwitch = DictUtils.getCellinfoSwitch() && (cfgparam.getMcc() != 460);
//			// 省份，基站
//			if(cfgparam.getMcc() == -100 || cellinfoVersionSwitch){
//				// -100是旧版本,或者没有数据的新版本开启了iccid，ip的规则
//				String provinceCode = null;
//				String ipProvince = IPUtils.getCityCode(StringUtils.getRemoteAddr(request));
//				if(StringUtils.isNotBlank(simNO) && simNO.trim().length() >=10 && StringUtils.isNotBlank(carriers)){
//					// ICCID省份
//					provinceCode = IccidUtils.getProvinceCodeBySimNO(simNO.trim(), carriers);
//					provinceCode = IccidUtils.getCmccProvinceCode(provinceCode,carriers);
//				}
//				if(provinceCode == null || "99".equals(provinceCode)){
//					// IP省份
//					provinceCode = ipProvince;
//				}
//				
//				if(provinceCode == null || STRICT_PROVICES.contains(provinceCode) || STRICT_PROVICES.contains(ipProvince)){
//					// 红区
//					cfgparam.setProvince("90");
//				} else {
//					// 白区
//					cfgparam.setProvince("70");
//				}
//			}else if(cfgparam.getMcc() != 460){
//				// 新版本数据没有获取到,红区
//				cfgparam.setProvince("90");
//			}else{
//				// 根据数据查询数据，解析红白区
//				try {
//					// 方法调用，返回红黄白区
//					CfgZoneLevel cfgZoneLevel = new CfgZoneLevel();
//					cfgZoneLevel.setCkappid(cfgparam.getCkAppId());
//					cfgZoneLevel.setChannelid(cfgparam.getCkChannelId());
//					
//					BaseStation baseStation = new BaseStation();
//					baseStation.setMnc(cfgparam.getMnc()+"");
//					baseStation.setLac(cfgparam.getLac()+"");
//					baseStation.setCi(cfgparam.getCi()+"");
//					
//					boolean isRedZone = baseStationService.isHit(cfgZoneLevelService.getCacheRedCfg(cfgZoneLevel), baseStation);
//
//					if(isRedZone){
//						cfgparam.setProvince("90");
//					}else{
//						boolean isYellowZone = baseStationService.isHit(cfgZoneLevelService.getCacheYellowCfg(cfgZoneLevel), baseStation);
//						if(isYellowZone){
//							cfgparam.setProvince("80");
//						}
//					}
//					cfgparam.setProvince("70");
//				} catch (Exception e) {
//					cfgparam.setProvince("90");
//					logger.error("cellinfo process error, please check it!!!", e);
//				}
//			}
			
			// 默认使用gameProcince
			String gameProcince = null;
			String feeProcince = null;
			String adProcince = null;
			
			// 计算省份
			if (DictUtils.isGreenArea(cfgparam.getImei()) 
					|| DictUtils.isGreenPhoneModel(cfgparam.getPhoneModel())) { // 绿区名单
//				cfgparam.setProvince("70");
				gameProcince = "70";
				feeProcince = "70";
				adProcince = "70";
			} else if (DictUtils.isYellowArea(cfgparam.getImei()) 
					|| DictUtils.isYellowPhoneModel(cfgparam.getPhoneModel())) { // 黄区名单
//				cfgparam.setProvince("80");
				gameProcince = "80";
				feeProcince = "80";
				adProcince = "80";
			}
			
//			else {
//				proviceCalcComponent.calcProvice(cfgparam, request);
//			}
			
			String rqTypes = cfgparam.getRqType();
			for(String rqType:rqTypes.split(",")){
				CfgService cfgService = null;
				try{
					rqType = rqType.trim();
					// 
					cfgparam.setRqType(rqType);
					
					if("mmextend".equals(rqType) || "andextend".equals(rqType) || "woextend".equals(rqType) || "egameextend".equals(rqType)){
						cfgparam.setProvince(feeProcince);
					}else if("ad".equals(rqType)|| "androidad".equals(rqType) || "androidadv2".equals(rqType)){
						cfgparam.setProvince(adProcince);
					} else{
						cfgparam.setProvince(gameProcince);
					}
					
					cfgService = SpringContextHolder.getBean(rqType.substring(0,1).toUpperCase()+rqType.substring(1) + "CfgService");
				}catch(Throwable t){
					// NULL DO
					continue;
				}
				Map<String, Object> cfgMap = cfgService.getCfg(cfgparam);
				
				cfgMap.put("netIp", clientIp);
				result.put(rqType, cfgMap);
			}
			result.put("resultCode", "0");
			result.put("errMsg", "");
		}catch(Throwable ex){
			logger.error("CfgparamController.getCfgparam() exception:", ex);
			result.put("resultCode", "-1");
			result.put("errMsg", "error");
		}
		logger.info("msg="+result.toString());
		return result;
	}
	
	/*@RequiresPermissions("sys:dict:view")*/
	@RequestMapping(value = "upload")
	@ResponseBody
	public Object uploadExcel(HttpServletRequest request,String type, Model model){
		 //创建一个通用的多部分解析器  
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求  
        if(multipartResolver.isMultipart(request)){  
            //转换成多部分request    
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
            //取得request中的所有文件名  
            Iterator<String> iter = multiRequest.getFileNames();  
            while(iter.hasNext()){ 
                //取得上传文件  
                MultipartFile file = multiRequest.getFile(iter.next());
                if(file != null){
	                try {
	                	//分解excel文件
	                	List<Map<String,Object>> list = splitExcelData(file);
	                	list = splitImportField(list);
                		if( list.size() > 0 ){
                			Cfgparam cfg = new Cfgparam();
                			cfg.setCkAppId(list.get(0).get("ckAppId").toString());
                			cfg.setRqType(list.get(0).get("rqType").toString());
                			int num = cfgparamService.bulkInsert(list,cfg);
                			if( num > 0) return "success";
                		}
					} catch (Exception e) {
						logger.error("import excel Error!",e);
					}
                }
            }
        }
		return "failure";
	}

	//解析excel数据
	private List<Map<String,Object>> splitExcelData(MultipartFile file)throws InvalidFormatException, IOException {
		ImportExcel excel = new ImportExcel(file, 0, 0);
		String sheetName = excel.getWb().getSheetName(0);
		String[] strSheet = sheetName.replace(" ", "").split("\\)");
		String rqType = "";
		String ckAppId="";
		if( strSheet.length > 0){
			String[] strA = strSheet[0].split("\\(");
			if(strA.length > 1) rqType=strA[1];
		}
		if(strSheet.length > 1){
			String[] strA = strSheet[1].split("\\(");
			if(strA.length > 1) ckAppId=strA[1];
		}
		String[] str = new String[excel.getLastCellNum()];
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		int splitIndex = 0; //分割位置
		for(int i = 0 ; i < excel.getLastCellNum() ; i ++){
			Object obj = excel.getCellValue(excel.getRow(0), i);
			if( obj != null ){
				String strHeader[] = obj.toString().split("\\(");
				if(PropertyConversion.split.getValue().equals(strHeader[0].toString())){
					splitIndex = i;
				}else{
					str[i] = PropertyConversion.getValue(strHeader[0]);
				}
			}
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("ckAppId", ckAppId);
		map.put("rqType", rqType);
		JSONObject json = new JSONObject();
		for(int n=1 ; n <= excel.getLastDataRowNum();n++){
			Map<String,Object> map1 = new HashMap<String, Object>();
			JSONObject json1 = new JSONObject();
			map1.putAll(map);
			json1.putAll(json);
			for(int i = 0 ; i < excel.getLastCellNum() ; i ++){
				Object obj = excel.getCellValue(excel.getRow(n), i);
				if( obj != null && obj.toString().trim().length() > 0 ){
					if( i < splitIndex ){
						if(str[i].equals(PropertyConversion.MMAPPID.getValue())){
							if(obj instanceof Double){
								double b = ((Double)obj);
								int num = (int)b;
								obj = num;
							}
						}
						map1.put(str[i], obj.toString());
					}else{
						json1.put(str[i], obj);
					}
				}
			}
			map1.put("exInfo", json1.toJSONString());
			json.putAll(json1);
			map.putAll(map1);
			list.add(map1);
		}
		return list;
	}
	
	//分割重要字段
	private List<Map<String,Object>> splitImportField(List<Map<String,Object>> list){
		List<Map<String,Object>> listNew = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map : list){
			//渠道分割
			Object channelName = map.get(PropertyConversion.渠道.getValue());
			String[] channelStr = null;
			if(channelName != null){
				channelStr = channelName.toString().split(",");
			}
			
			//时间段分割
			Object time = map.get(PropertyConversion.时间段.getValue());
			String[] str = null;
			if(time != null ){
				str = time.toString().split(",");
			}
			
			if(channelStr != null && channelStr.length > 0 ){
				for(String channelStr1 : channelStr){
					Map<String,Object> map1 = new HashMap<String, Object>();
					map1.putAll(map);
					Channel channel = channelService.getByName(channelStr1);
					if(channel != null){
						map1.put(PropertyConversion.渠道.getValue(), channel.getId());
					}else{
						map1.put(PropertyConversion.渠道.getValue(), channelStr1);
					}
					if(str != null && str.length>0){
						for(String str1:str){
							Map<String,Object> map2 = new HashMap<String, Object>();
							map2.putAll(map1);
							map2.put(PropertyConversion.时间段.getValue(), str1);
							listNew.add(map2);
						}
					}else{
						listNew.add(map1);
					}
				}
			}else{
				if(str != null && str.length>0){
					for(String str1:str){
						Map<String,Object> map1 = new HashMap<String, Object>();
						map1.putAll(map);
						map1.put(PropertyConversion.时间段.getValue(), str1);
						listNew.add(map1);
					}
				}else{
					listNew.add(map);
				}
			}
		}
		return listNew;
	}
	
	
	/*@RequiresPermissions("sys:dict:view")*/
	@RequestMapping(value = "download")
	public void downloadExcel(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="rqType",required=false)String rqType,@RequestParam(value="ckAppId",required=false)String ckAppId){
		
		Cfgparam cfg = new Cfgparam();
		cfg.setRqType(rqType);
		cfg.setCkAppId(ckAppId);
		StringBuffer sheetName = new StringBuffer();
		if( rqType != null){
			sheetName.append("配置类型("+rqType+") ");
		}
		APPCk appCK = AppCkUtils.getAppCkById(ckAppId);
		if(appCK != null ){
			sheetName.append(appCK.getName()+"("+ckAppId+")");
		}
		List<Cfgparam> list = this.cfgparamService.getExcelData(cfg);
		List<Map<String,Object>> exportData = this.getExportData(list);
		String[] title = this.getTitle(list);
		ExportExcel export = new ExportExcel("", title,sheetName.toString());
		export.setDataList(exportData, title);
		try {
			export.write(response, "扩展支付配置模板.xlsx");
		} catch (IOException e) {
			logger.error("导出出问题!!!", e);
		}
	}
	
	//组装数据
	private List<Map<String,Object>> getExportData(List<Cfgparam> list){
		List<Map<String,Object>> listArr = new ArrayList<Map<String,Object>>();
		for( Cfgparam cfg : list ){
			//渠道id转name
			StringBuffer channelName = new StringBuffer();
			if(cfg.getCkChannelId() != null){
				String[] str = cfg.getCkChannelId().split(",");
				for(String channelStr : str){
					Channel channel = channelService.get(channelStr);
					if(channel != null){
						channelName.append(channel.getName()).append(",");
					}
				} 
				if(channelName.length()>0){
					channelName.setLength(channelName.length()-1);
					cfg.setCkChannelId(channelName.toString());
				}
			}
			
			//解析exInfo
			Map<String,Object> map = cfg.getExInfoMap(); 
			if( map == null){
				map = new HashMap<String, Object>();
			}
			
			//组装成map格式
			Set<String> keySet = map.keySet();
			Map<String,Object> map1 = new HashMap<String, Object>();
			map1.putAll(map);
			for(String key : keySet){
				map1.put(PropertyConversion.getValue(key)+"("+key+")", map.get(key));
			}
			map1.put("MMAPPID", cfg.getMmAppId());
			map1.put(PropertyConversion.province.getValue()+"("+PropertyConversion.省份.getValue()+")", cfg.getProvince());
			map1.put(PropertyConversion.ckChannelId.getValue(), cfg.getCkChannelId());
			map1.put(PropertyConversion.time.getValue(), cfg.getTime());
			map1.put(PropertyConversion.versionName.getValue(), cfg.getVersionName());
			map1.put("配置", "");
			listArr.add(map1);	
		}
		return listArr;
	}
	
	//获取title
	private String[] getTitle(List<Cfgparam> list){
		Map<String,Object> map = null;
		if( list != null && list.size() > 0){
			map = list.get(0).getExInfoMap();
		}else{
			map = new HashMap<String, Object>();
		}
		String[] title = null;
		int n = 6;
		if(map != null){
			title = new String[6+ map.size()];
			Set<String> keySet = map.keySet();
			for(String key : keySet){
				title[n++] = PropertyConversion.getValue(key)+"("+key+")";
			}
		}else{
			title = new String[5];
		}
		title[0]="MMAPPID";
		title[1]=PropertyConversion.province.getValue()+"("+PropertyConversion.省份.getValue()+")";
		title[2]=PropertyConversion.ckChannelId.getValue();
		title[3]=PropertyConversion.time.getValue();
		title[4]=PropertyConversion.versionName.getValue();
		title[5]="配置";
		return title;
	}
	
	/*@RequiresPermissions("app:cfgparam:view")*/
	@RequestMapping(value = {"list", ""})
	public String list(Cfgparam cfgparam,HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Cfgparam> page = cfgparamService.findPage(new Page<Cfgparam>(request, response), cfgparam);
        model.addAttribute("page", page);
        model.addAttribute(cfgparam);
		return "modules/app/cfgparamList";	
	}
	
	/*@RequiresPermissions("app:cfgparam:view")*/
	@RequestMapping("view")
	public String wiew(String id, Model model) {
		Cfgparam cfg = new Cfgparam();
		cfg.setId(id);
		Cfgparam cfgparam = cfgparamService.get(cfg);
		model.addAttribute("cfgparam", cfgparam);
		Map<String,Object> map = null;
		if(cfgparam != null){
			map = cfgparam.getExInfoMap();
		}
		model.addAttribute("exInfo",map);
		return "modules/app/cfgparamView";	
	}
	
	@RequestMapping("exportData")
	public String exportData(Model model) {
		List<APPCk> ck = AppCkUtils.getAPPCkList();
		model.addAttribute("ck", ck);
		List<Dict> rq = DictUtils.getDictList("cfgtype");
		model.addAttribute("rq", rq);
		return "modules/app/cfgparamExportData";	
	}

	@RequestMapping("edit")
	public String edit(Model model,String id) {
		Cfgparam cfgparam = new Cfgparam();
		cfgparam.setId(id);
		Cfgparam cfg = this.cfgparamService.get(cfgparam);
		model.addAttribute("cfg", cfg);
		if(StringUtils.isBlank(cfgparam.getChildCkAppId())){
			cfgparam.setChildCkAppId("#");
		}
		//获取对应的key
		List<CfgParamFeilds> list = null;
		if(cfg != null && cfg.getRqType() != null){
			CfgParamFeilds cfgP = new CfgParamFeilds();
			cfgP.setType(cfg.getRqType());
			cfgP.setCkAppId(cfg.getCkAppId());
			list = cfgParamFeildsService.findList(cfgP);
			if( list == null || list.size() <= 0 ){
				cfgP.setCkAppId("#");
				list = cfgParamFeildsService.findList(cfgP);
			}
		}
		//解析数据
		JSONObject json = null;
		if(cfg != null){
			json = JSONObject.parseObject(cfg.getExInfo());
		}
//		Map<String, Object> data = new HashMap<String, Object>();
		List<JSONObject> data = new ArrayList<JSONObject>();
		StringBuffer type = new StringBuffer(); 
		if( json != null){
			for(CfgParamFeilds cfgpf:list){
				JSONObject e = new JSONObject();
				e.put("key", cfgpf.getValue());
				e.put("label", cfgpf.getLabel()+"("+cfgpf.getValue()+")");
				e.put("value", json.get(cfgpf.getValue()));
				e.put("desc", cfgpf.getDescription());
				data.add(e);
				if(cfgpf.getClassType() == 1){
					type.append(cfgpf.getValue()+"="+int.class.getSimpleName()+",");
				}
				if(cfgpf.getClassType() == 2){
					type.append(cfgpf.getValue()+"="+boolean.class.getSimpleName()+",");
				}
				if(cfgpf.getClassType() == 3){
					type.append(cfgpf.getValue()+"="+JSONObject.class.getSimpleName()+",");
				}
				if(cfgpf.getClassType() == 4){
					type.append(cfgpf.getValue()+"="+JSONArray.class.getSimpleName()+",");
				}
				if(cfgpf.getClassType() == 5){
					type.append(cfgpf.getValue()+"="+String.class.getSimpleName()+",");
				}
			}
		}
		
		if( type.length()>0 ) type.setLength(type.length()-1);
		model.addAttribute("map", data);
		model.addAttribute("type", type);
		return "modules/app/cfgparamEdit";	
	}
	 
	@RequestMapping("save")
	public String save(Model model,Cfgparam cfgparam,String jsonStr,String type,String[] pro,String[] channel,
			String[] carriesA,String[] mmapp,HttpServletRequest request,HttpServletResponse response) {
		JSONObject json = null;
		if(jsonStr != null){
			jsonStr = jsonStr.replace("&quot;", "\"");
			json = JSONObject.parseObject(jsonStr);
			StringBuffer sb = new StringBuffer();
			StringBuffer channelArr = new StringBuffer();
			StringBuffer carriesArr = new StringBuffer();
			StringBuffer mmappArr = new StringBuffer();
			if(pro != null){
				for(int i=0;i<pro.length;i++){
					if(pro[i].equals("#")){
						sb = new StringBuffer("#,"); break;
					}else{
						sb.append(pro[i]).append(",");
					}
				}
			}
			if(channel != null){
				for(int i=0;i<channel.length;i++){
					if(channel[i].equals("#")){
						channelArr = new StringBuffer("#,"); break;
					}else{
						channelArr.append(channel[i]).append(",");
					}
				}
			}
			if(carriesA != null){
				for(int i=0;i<carriesA.length;i++){
					if(carriesA[i].equals("#")){
						carriesArr = new StringBuffer("#,"); break;
					}else{
						carriesArr.append(carriesA[i]).append(",");
					}
				}
			}
			if(mmapp != null){
				for(int i=0;i<mmapp.length;i++){
					if(mmapp[i].equals("#")){
						mmappArr = new StringBuffer("#,"); break;
					}else{
						mmappArr.append(mmapp[i]).append(",");
					}
				}
			}
			sb.setLength(sb.length()>0?sb.length()-1:0);
			cfgparam.setProvince(sb.toString());
			channelArr.setLength(channelArr.length()>0?channelArr.length()-1:0);
			cfgparam.setCkChannelId(channelArr.toString());
			carriesArr.setLength(carriesArr.length()>0?carriesArr.length()-1:0);
			cfgparam.setCarriers(carriesArr.toString());
			mmappArr.setLength(mmappArr.length()>0?mmappArr.length()-1:0);
			cfgparam.setMmAppId(mmappArr.toString());
		}
		Map<String,String> typeData = new HashMap<String, String>();
		String[] strArr = null;
		if( type !=null && type.length() > 0 ){
			strArr = type.split(",");
			for(int i = 0 ; i<strArr.length ; i++ ){
				String[] strKey = strArr[i].split("=");
				if(strKey.length > 1){
					typeData.put(strKey[0], strKey[1]);
				}
			}
		}
		JSONObject exInfo = new JSONObject();
		if(json != null){
			Set<String> keySet = json.keySet();
			for(String str:keySet){
				Object obj = null;
				if( json.get(str) != null && StringUtils.isNotBlank(json.get(str).toString()) ){
					obj = json.get(str);
					/*if(obj.toString().trim().indexOf("{") == 0 ){
						obj = JSONObject.parse(obj.toString().trim());
					}else if(obj.toString().trim().indexOf("[") == 0){
						obj = JSONArray.parse(obj.toString().trim());
					}else{
						if(obj.toString().equals("true")){
							obj=true;
						}else if(obj.toString().equals("false")){
							obj=false;
						}
					}*/
					if( str.lastIndexOf("(") >= 0){
						str = str.substring(str.lastIndexOf("(")+1, str.lastIndexOf(")"));
					}
					if(typeData.get(str).equals(JSONObject.class.getSimpleName())){
						obj = JSONObject.parse(obj.toString().trim());
					}
					if(typeData.get(str).equals(JSONArray.class.getSimpleName())){
						obj = JSONArray.parse(obj.toString().trim());
					}
					if(typeData.get(str).equals(Boolean.class.getSimpleName()) || typeData.get(str).equals(boolean.class.getSimpleName())){
						obj = Boolean.parseBoolean(obj.toString().trim());
					}
					if(typeData.get(str).equals(String.class.getSimpleName())){
						obj = obj.toString().trim();
					}
					if(typeData.get(str).equals(Integer.class.getSimpleName()) || typeData.get(str).equals(int.class.getSimpleName())){
						obj = Integer.parseInt(obj.toString().trim());
					}		
				}
				if( str.indexOf("(") >= 0){
					String key = str.substring(str.lastIndexOf("(")+1, str.lastIndexOf(")"));
					exInfo.put(key, obj);
				}else{
					exInfo.put(str, obj);
				}
			}
		}
 		cfgparam.setExInfo(exInfo.toJSONString());
		this.cfgparamService.save(cfgparam);
		return list(cfgparam, request, response, model);	
	}
	
	@RequestMapping("insert")
	public String insert(Model model,String msg){
		Cfgparam cfg = new Cfgparam();
		/*CfgParamFeilds cfgP = new CfgParamFeilds();
		cfgP.setType(rqType);
		cfgP.setCkAppId(ckAppId);
		List<CfgParamFeilds> list = cfgParamFeildsService.findList(cfgP);
		cfg.setRqType(rqType);
		cfg.setCkAppId(ckAppId);
		model.addAttribute("map", list);*/
		model.addAttribute("cfg",cfg);
		return "modules/app/cfgparamInsert";
	}
	
	@RequestMapping("add")
	public String add(Model model,Cfgparam cfgparam,String classType,String data, RedirectAttributes redirectAttributes,
			String[] pro,String[] channel,String[] carriesA,String[] mmapp,HttpServletRequest request,HttpServletResponse response){
		String[] cType = null;
		if(null != classType && classType.length() > 1 ){
			cType = classType.split(",");
		}
		if(StringUtils.isBlank(cfgparam.getChildCkAppId())){
			cfgparam.setChildCkAppId("#");
		}
		JSONObject json = null;
		if(data != null){
			data = data.replace("&quot;", "\"");
			json = JSONObject.parseObject(data);
			StringBuffer sb = new StringBuffer();
			StringBuffer channelArr = new StringBuffer();
			for(int i=0;i<pro.length;i++){
				if(pro[i].equals("#")){
					sb = new StringBuffer("#,"); break;
				}else{
					sb.append(pro[i]).append(",");
				}
			}
			for(int i=0;i<channel.length;i++){
				if(channel[i].equals("#")){
					channelArr = new StringBuffer("#,"); break;
				}else{
					channelArr.append(channel[i]).append(",");
				}
			}
			sb.setLength(sb.length()>0?sb.length()-1:0);
			cfgparam.setProvince(sb.toString());
			channelArr.setLength(channelArr.length()>0?channelArr.length()-1:0);
			cfgparam.setCkChannelId(channelArr.toString());
		}
		Map<String,String> map = new HashMap<String, String>();
		if(cType != null){
			for(int i=0 ; i<cType.length ; i++ ){
				String[] strArr = cType[i].split("-");
				if( strArr.length > 1 ){
					map.put(strArr[0], strArr[1]);
				}
			}
		}
		Set<String> keySet = map.keySet();
		for(String str:keySet){
			if(map.get(str).equals("1")){
				json.put(str,json.getInteger(str));
			}
			if(map.get(str).equals("2")){
				json.put(str, json.getBoolean(str));
			}
			if(map.get(str).equals("3")){
				if(json.getString(str).trim().indexOf("{") == 0){
					JSONObject j3 = JSONObject.parseObject(json.getString(str));
					json.put(str, j3);
				}else{
					String r3 =  "{"+json.getString(str)+"}";
					json.put(str, JSONObject.parse(r3));
				}
			}
			if(map.get(str).equals("4")){
				if(json.getString(str).trim().indexOf("[") == 0){
					JSONArray j4 = JSONArray.parseArray(json.getString(str));
					json.put(str, j4);
				}else{
					String r4 =  "["+json.getString(str)+"]";
					json.put(str, JSONArray.parse(r4));
				}
			}
			if(map.get(str).equals("5")){
				json.put(str, json.getString(str));
			}
		}
		cfgparam.setExInfo(json.toJSONString());
		cfgparamService.save(cfgparam);
		addMessage(redirectAttributes, "保存成功");
		return list(cfgparam, request, response, model);
	}
	
	@RequestMapping("getFieldList")
	@ResponseBody
	public List<Map<String,Object>> getFieldList(String rqType,String ckAppId){
		if( rqType!= null){
			CfgParamFeilds cfgP = new CfgParamFeilds();
			cfgP.setType(rqType);
			cfgP.setCkAppId(ckAppId);
			if(ckAppId == null){
				cfgP.setCkAppId("#");
			}
			List<CfgParamFeilds> list = cfgParamFeildsService.findList(cfgP);
			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			if(list != null && list.size() > 0){
				getResult(list, result);
			}else{
				cfgP.setCkAppId("#");
				List<CfgParamFeilds> listNew = cfgParamFeildsService.findList(cfgP);
				if(listNew != null){
					getResult(listNew, result);
				}
				
			}
			return result;
		}
		return null;
	}

	private void getResult(List<CfgParamFeilds> list,
			List<Map<String, Object>> result) {
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("ckAppId", list.get(i).getCkAppId());
			map.put("type", list.get(i).getType());
			map.put("value", list.get(i).getValue());
			map.put("label", list.get(i).getLabel());
			map.put("description", list.get(i).getDescription());
			map.put("classType", list.get(i).getClassType());
			result.add(map);
		}
	}
	
	/**
	 * 复制配置信息
	 * @param payRules
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "copyCfg")
	public String copy(Cfgparam cfgparam, HttpServletRequest request,  HttpServletResponse response,Model model, RedirectAttributes redirectAttributes) {
		if(StringUtils.isNotBlank(cfgparam.getId())){
			cfgparam = this.cfgparamService.get(cfgparam);
			cfgparam.setId(null);
			cfgparam.setVersionName("x");
			cfgparamService.save(cfgparam);
			request.setAttribute("message", "复制配置数据成功");
		}else{
			request.setAttribute("message", "复制配置数据失败");
		}
		return list(cfgparam, request, response, model);
	}
}
