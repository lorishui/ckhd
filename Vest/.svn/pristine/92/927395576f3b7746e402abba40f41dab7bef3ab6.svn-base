package me.ckhd.opengame.stats.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import me.ckhd.opengame.app.entity.AppErrorLog;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.query.service.QueryService;

@RequestMapping(value = "${adminPath}/stats/error")
@Controller
public class StatErrorController extends BaseController {

	@Autowired
	private QueryService queryService;
	
	@RequestMapping("count")
	public String count(CountParam param, Model model ) {
		
		if ( !Verdict.isAllow(param.getCkAppId())) {
			return "403";
		}
		
		List<CountResult> data = queryService.selectList("statErrorCount", param);
		
		model.addAttribute("param", param);
		model.addAttribute("data", data);
		
		return "modules/stats/statErrorCount";
	}
	@RequestMapping("list")
	public String list(CountParam param, Model model ) {
		
		if ( !Verdict.isAllow(param.getCkAppId())) {
			return "403";
		}
		
		List<AppErrorLog> data = queryService.selectList("statErrorList", param);
		
		model.addAttribute("param", param);
		model.addAttribute("data", data);
		
		return "modules/stats/statErrorList";
	}
	public static class CountParam {
		private String ckAppId;
		private String ckChannelId;
		private Integer begDate;
		private Integer endDate;
		
		private String errorClassName;
		private String errorLineNumber;
		private String exceptionClassName;
		
		public String getCkAppId() {
			return ckAppId;
		}
		public void setCkAppId(String ckAppId) {
			this.ckAppId = ckAppId;
		}
		public String getCkChannelId() {
			return ckChannelId;
		}
		public void setCkChannelId(String ckChannelId) {
			this.ckChannelId = ckChannelId;
		}
		public Integer getBegDate() {
			return begDate;
		}
		public void setBegDate(Integer begDate) {
			this.begDate = begDate;
		}
		public Integer getEndDate() {
			return endDate;
		}
		public void setEndDate(Integer endDate) {
			this.endDate = endDate;
		}
		public String getErrorClassName() {
			return errorClassName;
		}
		public void setErrorClassName(String errorClassName) {
			this.errorClassName = errorClassName;
		}
		public String getErrorLineNumber() {
			return errorLineNumber;
		}
		public void setErrorLineNumber(String errorLineNumber) {
			this.errorLineNumber = errorLineNumber;
		}
		public String getExceptionClassName() {
			return exceptionClassName;
		}
		public void setExceptionClassName(String exceptionClassName) {
			this.exceptionClassName = exceptionClassName;
		}
	}
	public static class CountResult {
		private Integer occurDate;
		private String errorClassName;
		private String errorLineNumber;
		private String exceptionClassName;
		private Integer total;
		public Integer getOccurDate() {
			return occurDate;
		}
		public void setOccurDate(Integer occurDate) {
			this.occurDate = occurDate;
		}
		public String getErrorClassName() {
			return errorClassName;
		}
		public void setErrorClassName(String errorClassName) {
			this.errorClassName = errorClassName;
		}
		public String getErrorLineNumber() {
			return errorLineNumber;
		}
		public void setErrorLineNumber(String errorLineNumber) {
			this.errorLineNumber = errorLineNumber;
		}
		public String getExceptionClassName() {
			return exceptionClassName;
		}
		public void setExceptionClassName(String exceptionClassName) {
			this.exceptionClassName = exceptionClassName;
		}
		public Integer getTotal() {
			return total;
		}
		public void setTotal(Integer total) {
			this.total = total;
		}
	}
}
