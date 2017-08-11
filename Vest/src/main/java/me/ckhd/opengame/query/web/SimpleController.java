package me.ckhd.opengame.query.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public class SimpleController extends BaseController{
	
	@Autowired
	private QueryService queryService;
	
	@RequiresPermissions("stats:appOnlinePay:view")
	@RequestMapping(value = "simple")
	public String simple(Param param, Model model) {
		 
		if (!Verdict.isAllow(param.get("ckAppId"))) {
			return "403";
		}
		
		List<Map<String, Object>> data = queryService.selectList(param.getQname(), param.getMap());
		
		model.addAttribute("param", param);
		model.addAttribute("data", data);
		
		return "modules/" + param.getVname();
	}
	
	public static class Param {
		
		public Param() {}
		
		public Param(Map<String, String> map) { this.map = map; }
		
		private Map<String, String> map = new LinkedHashMap<String, String>();

		public Map<String, String> getMap() { return map; }

		public void setMap(Map<String, String> map) { this.map = map; }
		
		public String get(String key) { return map.get(key); }
		
		private String qname;
		private String vname;

		public String getQname() {
			return qname;
		}

		public void setQname(String qname) {
			this.qname = qname;
		}

		public String getVname() {
			return vname;
		}

		public void setVname(String vname) {
			this.vname = vname;
		}
	}
}