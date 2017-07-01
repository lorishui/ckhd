package me.ckhd.opengame.stats.web;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.andapi.entity.AndAPPOrder;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.mmapi.entity.MmAppOrder;
import me.ckhd.opengame.stats.entity.OrderQry;
import me.ckhd.opengame.stats.service.AndOrderStatsService;
import me.ckhd.opengame.stats.service.MmOrderStatsService;
import me.ckhd.opengame.sys.entity.Dict;
import me.ckhd.opengame.sys.utils.DictUtils;
import me.ckhd.opengame.sys.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("${adminPath}/stats/SuccessMoney")
public class MMAndOrderStatsController extends BaseController{
	
	@Autowired
	private AndOrderStatsService andOrderService;
	
	@Autowired
	private MmOrderStatsService mmOrderStatsService;
	
	@RequestMapping("states")
	public String states(OrderQry orderQry,Model model,@RequestParam(value="carriesType",required=false) Integer carriesType){
		List<Map<String,Object>> list = null;
		if(StringUtils.isNotBlank(orderQry.getStartDate()) && StringUtils.isNotBlank(orderQry.getEndDate()) 
				&& StringUtils.isNotBlank(orderQry.getCkAppId())){
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(carriesType == null || carriesType == 1){
					MmAppOrder mm = new MmAppOrder();
					mm.setStartDate(orderQry.getStartDate().replace("-", "").replace(" ", "")+"000000");
					mm.setEndDate(orderQry.getEndDate().replace("-", "").replace(" ", "")+"235959");
					mm.setCkappId(orderQry.getCkAppId());
					List<Dict> filterRoles = DictUtils.getDictList("filter_data_role");
					mm.setFilterRole(UserUtils.getUser().getRoleList(),filterRoles);
					mm.setFilterRate(DictUtils.getFilterRate(orderQry.getCkAppId()));
					list = mmOrderStatsService.statsByCkApp(mm);
				}else{
					AndAPPOrder and = new AndAPPOrder();
					and.setBeginDate(sdf.parse(orderQry.getStartDate()+" 00:00:00"));
					and.setEndDate(sdf.parse(orderQry.getEndDate()+" 23:59:59"));
					and.setCkappId(orderQry.getCkAppId());
					List<Dict> filterRoles = DictUtils.getDictList("filter_data_role");
					and.setFilterRole(UserUtils.getUser().getRoleList(),filterRoles);
					and.setFilterRate(DictUtils.getFilterRate(orderQry.getCkAppId()));
					list = andOrderService.statsByCkApp(and);
				}
			} catch (Throwable e) {
				logger.error("日期转换格式错误！", e);
			}
		}else{
			orderQry.setStartDate(DateUtils.getDate("yyyy-MM-dd"));
			orderQry.setEndDate(DateUtils.getDate("yyyy-MM-dd"));
		}
		model.addAttribute("data", list);
		model.addAttribute("orderQry", orderQry);
		model.addAttribute("carriesType", carriesType);
		return "modules/stats/SuccessMoneyCount";
	}
}