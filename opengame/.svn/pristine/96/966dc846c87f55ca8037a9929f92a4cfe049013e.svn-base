package me.ckhd.opengame.game.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.app.utils.ChannelUtils;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.utils.excel.ExportExcel;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.game.entity.GiftCode;
import me.ckhd.opengame.game.service.GiftCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "${adminPath}/game/giftCode")
public class GiftCodeController extends BaseController{
	@Autowired
	private GiftCodeService giftCodeService;
	
	@ModelAttribute
	public  GiftCode get(@RequestParam(required=false) String id){
		if (StringUtils.isNotBlank(id)) {
			return  giftCodeService.get(id);
		}else{
			return  new GiftCode();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(GiftCode giftCode,HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!Verdict.isAllow(giftCode.getCkAppId())) {
			return "403";
		}
		Page<GiftCode> page = null;
		if( StringUtils.isNotBlank(giftCode.getCkAppId()) ){
		    page = giftCodeService.findPage(new Page<GiftCode>(request, response), giftCode);
		}
        model.addAttribute("page", page);
		return "modules/game/giftCodeList";
		
	}
	
	
	@RequestMapping(value = "download")
	@ResponseBody
	public void downloadExcel(HttpServletRequest request,HttpServletResponse response,
			GiftCode giftCode){
		if (!Verdict.isAllow(giftCode.getCkAppId())) {
			return;
		}
		StringBuffer name = new StringBuffer();
		if(giftCode.getBatch() != null){
			giftCode.setIsUse(3);
			List<GiftCode> list = this.giftCodeService.findList(giftCode);
			APPCk appCK = AppCkUtils.getAppCkById(giftCode.getCkAppId());
			name.append(appCK.getName());
			if(StringUtils.isNotBlank(giftCode.getCkChannelId())){
				name.append("-").append(ChannelUtils.getChannelName(giftCode.getCkChannelId(), ""));
			}
			List<Map<String,Object>> exportData = this.getExportData(list);
			String[] title = {"兑换码"};
			name.append("-"+giftCode.getBatch());
			ExportExcel export = new ExportExcel("", title,"sheet1");
			export.setDataList(exportData, title);
			try {
				export.write(response, name.toString()+".xlsx");
			} catch (IOException e) {
				logger.error("导出出问题!!!", e);
			}
		}
	}
	
	private List<Map<String,Object>> getExportData(List<GiftCode> list) {
		List<Map<String,Object>> map = new ArrayList<Map<String,Object>>();
		for(GiftCode obj:list){
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("兑换码", obj.getCode());
			map.add(m);
		}
		return map;
	}
}
