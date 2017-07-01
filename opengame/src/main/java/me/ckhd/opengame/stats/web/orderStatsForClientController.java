package me.ckhd.opengame.stats.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import me.ckhd.opengame.andapi.entity.AndAPPOrder;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.drds.service.EventService;
import me.ckhd.opengame.evnet.entity.AppEventStat;
import me.ckhd.opengame.mmapi.entity.MmAppOrder;
import me.ckhd.opengame.stats.service.AndOrderStatsService;
import me.ckhd.opengame.stats.service.MmOrderStatsService;
import me.ckhd.opengame.sys.entity.Dict;
import me.ckhd.opengame.sys.service.DictService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("ck/statsforclient")
public class orderStatsForClientController {

	@Autowired
	private MmOrderStatsService mmOrderStatsService;
	
	@Autowired
	private AndOrderStatsService andOrderStatsService;

	@Autowired
	private EventService eventService;
	
	@Autowired
	private DictService dictService;
	
	@ModelAttribute("order")
	public MmAppOrder get(@RequestParam(required = false) String id) {
		return new MmAppOrder();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("order")
	public String stats(MmAppOrder mmAppOrder, Model model) {
		if (!Verdict.isAllow(mmAppOrder.getCkappId())) {
			return "403";
		}

		String ckappId = mmAppOrder.getCkappId();
		String startDate = mmAppOrder.getStartDate();
		String endDate = mmAppOrder.getEndDate();
		mmAppOrder.setOrderType(1);
		if (!StringUtils.isEmpty(ckappId) && !StringUtils.isEmpty(startDate)
				&& !StringUtils.isEmpty(endDate)) {
			String startDateTime = mmAppOrder.getStartDate();
			String endDateTime = mmAppOrder.getEndDate();
			startDateTime+=" 00:00:00";
			endDateTime+=" 23:59:59";
			mmAppOrder.setStartDate(startDateTime.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			mmAppOrder.setEndDate(endDateTime.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
			List<Object> mmAppOrders = mmOrderStatsService.stats(mmAppOrder);
			AndAPPOrder andapporder = new AndAPPOrder();
			andapporder.setCkappId(ckappId);
			andapporder.setChannelId(mmAppOrder.getChannelId());
			andapporder.setBeginDate(DateUtils.parseDate(startDateTime));
			andapporder.setEndDate(DateUtils.parseDate(endDateTime));
			
			List<Map<String, Object>> andorders = andOrderStatsService.stats(andapporder);
			Dict entity = new Dict();
			entity.setType("convert");
			List<Dict> dicts=dictService.findList(entity);
			double conver=1;
			if(dicts!=null && dicts.size()>0){
				Dict dict = dicts.get(0);
				conver = Double.parseDouble((dict==null?"1":dict.getValue()));
			}
			
			List<Map<String,Object>> result= new ArrayList<Map<String,Object>>();
			double total_price = 0.0;
			double total_mm_price=0.0;
			double total_and_price=0.0;
//			int total_new_account = 0;
			int mm_user_num = 0;
			int and_user_num = 0;
			for (int i=0;i<mmAppOrders.size();i++) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				Object obj = mmAppOrders.get(i);
				Map<String, Object> mmAppOrderMap = (Map<String, Object>) obj;
				Double totalPrice =  Double.parseDouble(mmAppOrderMap.get("succ_price").toString()) * conver;
				int user_num = Integer.parseInt(mmAppOrderMap.get("succ_user_num")==null?"0":mmAppOrderMap.get("succ_user_num").toString());
				if(!"总计".equals(mmAppOrderMap.get("mdate"))){
					mm_user_num += user_num;
					total_mm_price += Double.parseDouble(mmAppOrderMap.get("succ_price").toString()) * conver;
					total_mm_price =  Double.parseDouble(String.format("%.1f",total_mm_price));
					resultMap.put("mdate", mmAppOrderMap.get("mdate"));
					double mm_total_price = Double.parseDouble(mmAppOrderMap.get("total_price").toString()) * conver;
					resultMap.put("mm_total_price", String.format("%.1f",mm_total_price) + '0');
					resultMap.put("mm_succ_price", Double.parseDouble(mmAppOrderMap.get("succ_price").toString()) * conver);
					resultMap.put("mm_fail_price", Double.parseDouble(mmAppOrderMap.get("fail_price").toString()) * conver);
					resultMap.put("total_price", totalPrice);
					resultMap.put("mm_user_num", user_num);
					result.add(resultMap);
				}
			}
			
			for(Map<String, Object> map:andorders){
				boolean flag = true;
				for(Map<String,Object> resultMap:result){
					String resultMdate = resultMap.get("mdate").toString();
					String andMdate = map.get("mdate").toString();
					if((!"总计".equals(resultMap.get("mdate"))) && resultMdate!=null && resultMdate.equals(andMdate)){
						Double totalPrice = Double.parseDouble(resultMap.get("total_price").toString());
						totalPrice = Double.parseDouble(String.format("%.1f",totalPrice));
						Double price =Double.parseDouble(map.get("succ_price").toString())*conver;
						totalPrice+=price;
						int user = Integer.parseInt(map.get("succ_user_num").toString());
						and_user_num+=user;
						total_and_price+=price;
						resultMap.put("and_total_price", Double.parseDouble(map.get("total_price").toString())*conver);
						resultMap.put("and_succ_price", price);
						resultMap.put("and_fail_price", Double.parseDouble(map.get("fail_price").toString())*conver);
						resultMap.put("total_price", String.format("%.1f",totalPrice) + '0');
						resultMap.put("and_user_num", user);
						flag=false;
						break;
					}
				}
				if((!"总计".equals(map.get("mdate"))) && flag ){
					Map<String, Object> resultMap = new HashMap<String, Object>();
					Double totalPrice =Double.parseDouble(map.get("succ_price").toString())*conver;
					int user = Integer.parseInt(map.get("user_num").toString());
					and_user_num+=user;
					total_and_price+=totalPrice;
					resultMap.put("mdate", map.get("mdate"));
					resultMap.put("and_total_price", Double.parseDouble(map.get("total_price").toString())*conver);
					resultMap.put("and_succ_price", Double.parseDouble(map.get("succ_price").toString())*conver);
					resultMap.put("and_fail_price", Double.parseDouble(map.get("fail_price").toString())*conver);
					resultMap.put("total_price", totalPrice);
					resultMap.put("and_user_num", user);
					result.add(resultMap);
				}
			}
//			List<Map<String,Object>> newAccounts =  new ArrayList<Map<String,Object>>();
			AppEventStat appEventStat = new AppEventStat();
			appEventStat.setCkAppId(ckappId);
			appEventStat.setCkChannelId(mmAppOrder.getChannelId());
			appEventStat.setStartDate(mmAppOrder.getStartDate());
			appEventStat.setEndDate(mmAppOrder.getEndDate());
//			boolean isSameDate = mmAppOrder.getStartDate().substring(0,8).equals( mmAppOrder.getEndDate().substring(0,8));
			try {
//				newAccounts = eventService.getNewAccout(appEventStat);
//				if(isSameDate){
//					int num = 0;
//					for(Map<String,Object> map:newAccounts){
//						num+=Integer.valueOf(map.get("num").toString());
//					}
//					String newAccountDate = startDateTime.substring(0,startDateTime.indexOf(" "));
//					total_new_account+=num;
//					boolean flag = true;
//					for(Map<String,Object> resultMap:result){
//						String resultMdate = resultMap.get("mdate").toString();
//						if((!"总计".equals(resultMap.get("mdate"))) && resultMdate!=null && resultMdate.equals(newAccountDate)){
//							resultMap.put("new_account", num);
//							flag=false;
//							break;
//						}
//					}
//					if(flag){
//						Map<String, Object> resultMap = new HashMap<String, Object>();
//						resultMap.put("mdate", newAccountDate);
//						resultMap.put("new_account", num);
//						result.add(resultMap);
//					}
//				}else{
//					for(Map<String,Object> map:newAccounts){
//						boolean flag = true;
//						int num = Integer.valueOf(map.get("num").toString());
//						total_new_account+=num;
//						for(Map<String,Object> resultMap:result){
//							String resultMdate = resultMap.get("mdate").toString();
//							String newAccountDate = map.get("date").toString();
//							if((!"总计".equals(resultMap.get("mdate"))) && resultMdate!=null && resultMdate.equals(newAccountDate)){
//								resultMap.put("new_account", num);
//								flag=false;
//								break;
//							}
//						}
//						if(flag){
//							Map<String, Object> resultMap = new HashMap<String, Object>();
//							resultMap.put("mdate", map.get("date"));
//							resultMap.put("new_account", num);
//							result.add(resultMap);
//						}
//					}
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			Map<String, Object> totalMap = new HashMap<String, Object>();
			totalMap.put("mdate", "总计");
			total_price =(total_mm_price+total_and_price);
			totalMap.put("total_price",String.format("%.1f",total_price));
			totalMap.put("mm_succ_price", total_mm_price);
			totalMap.put("and_succ_price", total_and_price);
//			totalMap.put("new_account", total_new_account);
			totalMap.put("mm_user_num", mm_user_num);
			totalMap.put("and_user_num", and_user_num);
			result.add(totalMap);
			model.addAttribute("orderList", result);
			mmAppOrder.setStartDate(startDateTime.substring(0,startDateTime.lastIndexOf(" ")));
			mmAppOrder.setEndDate(endDateTime.substring(0,endDateTime.lastIndexOf(" ")));
		} else {
			mmAppOrder.setOrderType(1);
			Calendar c = Calendar.getInstance();
			String startDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd");
			mmAppOrder.setStartDate(startDateTime);
			String endDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd");
			mmAppOrder.setEndDate(endDateTime);
		}

		model.addAttribute("apporder", mmAppOrder);

		return "modules/stats/orderStatsClient";

	}

	/*private boolean isAllow(MmAppOrder mmAppOrder) {
		User user = UserUtils.getUser();
		if (user == null) {
			return false;
		}
		String ckappId = mmAppOrder.getCkappId();
		// 单游戏权限
		if (ckappId != null && user.getNo() != null
				&& user.getNo().startsWith("game")) {
			String gameId = user.getNo().substring(4);
			if (!ckappId.equals(gameId)) {
				LOG.warn("Illegal access stats. The User LoginName is "+ user.getLoginName());
				return false;
			}
		}
		return true;
	}*/

}