package me.ckhd.opengame.query.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.query.service.QueryService;

@RequestMapping(value = "${adminPath}/query")
@Controller
public class StatsNoviceTaskRatioController extends BaseController{
	
	@Autowired
	private QueryService queryService;
	
	@RequiresPermissions("stats:appOnlinePay:view")
	@RequestMapping(value = "stats/noviceTaskRatio")
	public String test(final NoviceTaskRatioParam param, Model model) throws Exception{
		
		if (!Verdict.isAllow(param.getCkAppId())) {
			return "403";
		}
		
		List<NoviceTaskRatioResult> data = new ArrayList<NoviceTaskRatioResult>();
		
		if( !StringUtils.isBlank(param.getCkAppId()) 
				&& !StringUtils.isBlank(param.getCkChannelId()) 
				&& !StringUtils.isBlank(param.getBegTime()) 
				&& !StringUtils.isBlank(param.getEndTime()) ) {
			data = queryService.selectList("noviceTaskRatio", param);
		}
		
		model.addAttribute("param",param);
		model.addAttribute("data", data);
		
		return "modules/stats/statNoviceTaskRatio";
	}
	
	public static class NoviceTaskRatioResult {
		private String cd;
		private String ctRole;
		private String ctEvent;
		private String ratio;
		
		public String getCd() {
			return cd;
		}
		public void setCd(String cd) {
			this.cd = cd;
		}
		public String getCtRole() {
			return ctRole;
		}
		public void setCtRole(String ctRole) {
			this.ctRole = ctRole;
		}
		public String getCtEvent() {
			return ctEvent;
		}
		public void setCtEvent(String ctEvent) {
			this.ctEvent = ctEvent;
		}
		public String getRatio() {
			return ratio;
		}
		public void setRatio(String ratio) {
			this.ratio = ratio;
		}
	}
	public static class NoviceTaskRatioParam {
		private String ckAppId;
		private String ckAppChildId;
		private String ckChannelId;
		private String begTime;
		private String endTime;
		
		public String getCkAppId() {
			return ckAppId;
		}
		public void setCkAppId(String ckAppId) {
			this.ckAppId = ckAppId;
		}
		public String getCkAppChildId() {
			return ckAppChildId;
		}
		public void setCkAppChildId(String ckAppChildId) {
			this.ckAppChildId = ckAppChildId;
		}
		public String getCkChannelId() {
			return ckChannelId;
		}
		public void setCkChannelId(String ckChannelId) {
			this.ckChannelId = ckChannelId;
		}
		public String getBegTime() {
			return begTime;
		}
		public void setBegTime(String begTime) {
			this.begTime = begTime;
		}
		public String getEndTime() {
			return endTime;
		}
		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
	}
}
