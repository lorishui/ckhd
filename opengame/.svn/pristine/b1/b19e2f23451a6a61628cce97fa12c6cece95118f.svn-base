package me.ckhd.opengame.app.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.app.service.ProvinceLevelCfgService;
import me.ckhd.opengame.app.utils.IccidUtils;
import me.ckhd.opengame.blacklist.BlackListUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.ipip.IPUtils;
import me.ckhd.opengame.util.CarriersUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "${adminPath}/app")
public class ProvinceLevelController extends BaseController{

	@Autowired
	private ProvinceLevelCfgService provinceLevelCfgService;
	
	@RequestMapping(value = "provincelevel")
	@ResponseBody
	public Map<String,Object> getCfg(@RequestParam(required=false) String ckAppId, @RequestParam(required=false) String ckChannelId, @RequestParam(required=false) String versionName, @RequestParam(required=false) String simNO, @RequestParam(required=false) String imsi, HttpServletRequest request){
		logger.debug("ckAppId=" + ckAppId + ",simNO=" + simNO + ",imsi=" + imsi);
		Map<String,Object> result = new HashMap<String,Object>();
		
		result.put("resultCode", 0);
		if(StringUtils.isNotBlank(imsi) && BlackListUtils.getInstance().isBlackList(imsi)){
			result.put("provinceLevel", 0);
			return result;
		}
		try{
			String carriersType = CarriersUtils.getCarriersType(simNO);
			String iccidCode = IccidUtils.getCmccProvinceCode(IccidUtils.getProvinceCodeBySimNO(simNO,carriersType), carriersType);
			if(iccidCode==null ||"99".equals(iccidCode)){
				iccidCode = IPUtils.getCode(StringUtils.getRemoteAddr(request));
				if(iccidCode == null || "".equals(iccidCode.trim())){
					iccidCode = "99";
				}
			}
			
			Cfgparam cfgparam = new Cfgparam();
			// 默认开心战机1023
			if(ckAppId == null){
				ckAppId = "1023";
			}
			cfgparam.setCkAppId(ckAppId);
			cfgparam.setCarriers(carriersType);
			cfgparam.setCkChannelId(ckChannelId);
			cfgparam.setVersionName(versionName);
			cfgparam.setProvince(iccidCode);
			cfgparam.setRqType("provinceLevel");
			
			Map<String, Object> levelMap = provinceLevelCfgService.getCfg(cfgparam);
			int level = 0;
			try{
				if(levelMap != null){
					level = (Integer)levelMap.get("level");
				}
			}catch(Throwable t){
				//
			}
			result.put("provinceLevel", level);
		}catch(Throwable t){
			result.put("resultCode", -1);
			result.put("errMsg", t.getMessage());
			logger.error("", t);
		}
		return result;
	}
	
}
