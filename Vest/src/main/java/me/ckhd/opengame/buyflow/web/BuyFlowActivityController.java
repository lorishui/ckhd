package me.ckhd.opengame.buyflow.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.ckhd.opengame.buyflow.entity.BuyFlowActivity;
import me.ckhd.opengame.buyflow.service.BuyFlowActivityService;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.sys.utils.DictUtils;
import me.ckhd.opengame.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/buyflow/activity")
public class BuyFlowActivityController extends BaseController {
	@Autowired
	private BuyFlowActivityService buyFlowActivityService;
	
	@RequiresPermissions("buyflow:activity:view")
	@RequestMapping(value = { "list", "" })
	public String list(BuyFlowActivity buyFlowActivity, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		if(buyFlowActivity.getStartDate() == null || buyFlowActivity.getEndDate() == null){
			buyFlowActivity.setEndDate(sdf.format(cal.getTime()));
			cal.add(Calendar.DATE, -5);
			buyFlowActivity.setStartDate(sdf.format(cal.getTime()));
		}

		//20170731，添加权限限制
		Set<String> mediaPermission = UserUtils.getMediaPermission();
		if( !mediaPermission.isEmpty() ) {
			buyFlowActivity.setPermissionMediaId(new ArrayList<String>(mediaPermission));
		}
		Set<String> gamePermission = UserUtils.getGamePermission();
		if( !gamePermission.isEmpty() ) {
			buyFlowActivity.setPermissionCkAppId(StringUtils.join(gamePermission, ","));
		}
		Set<String> gameChildPermission = UserUtils.getGameChildPermission();
		if( !gameChildPermission.isEmpty() ) {
			buyFlowActivity.setPermissionCkAppChildId(StringUtils.join(gameChildPermission, ","));
		}
		
		BuyFlowActivity clone = null;
		try {
			clone = (BuyFlowActivity)buyFlowActivity.clone();
			String endDate = buyFlowActivity.getEndDate();
			cal.add(Calendar.DATE, 1);
			buyFlowActivity.setEndDate(sdf.format(new Date(sdf.parse(endDate).getTime()+1000*3600*24)));
		} catch (Exception e) {
			logger.error("buyflowactivity错误:" +  e.getMessage());
		}
		Page<BuyFlowActivity> page = buyFlowActivityService.findPage(new Page<BuyFlowActivity>(request,
				response), buyFlowActivity);
		Set<String> actNames = buyFlowActivityService.getActivicyList();
		model.addAttribute("actNames", actNames);
		model.addAttribute("page", page);
		model.addAttribute("buyFlowActivity", clone);
		return "modules/buyflow/buyFlowActivityList";

	}

	@RequiresPermissions("buyflow:activity:view")
	@RequestMapping(value = "form")
	public String form(BuyFlowActivity buyFlowActivity, Model model) {
		BuyFlowActivity bfa = new BuyFlowActivity();
		if(StringUtils.isNotEmpty(buyFlowActivity.getId())){
			bfa = buyFlowActivityService.get(buyFlowActivity.getId());
		}
		String name = bfa.getName();
		if(StringUtils.isNotBlank(name)){
			int a = name.lastIndexOf("_");
			if(a != -1){
				bfa.setName(name.substring(0,a));
			}
		}
		model.addAttribute("buyFlowActivity", bfa);
		return "modules/buyflow/buyFlowActivityForm";
	}

	@RequiresPermissions("buyflow:activity:edit")
	@RequestMapping(value = "save")
	public String save(BuyFlowActivity buyFlowActivity, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, buyFlowActivity)) {
			return form(buyFlowActivity, model);
		}
		// 保存
		String adItems = buyFlowActivity.getAdItem();
		String[] adItemS = adItems.split(",");
		String head  = DictUtils.getDictValue(buyFlowActivity.getMediaId(),"buyFlow_url_head","");
		String tail = DictUtils.getDictValue(buyFlowActivity.getMediaId()+"_"+buyFlowActivity.getDeviceType(),"buyFlow_url_tail","");
		StringBuilder msg = new StringBuilder("保存推广点");
		int i = 0;
		for (String adItem : adItemS) {
			StringBuilder sb = new StringBuilder();
			sb.append(head)
				.append(buyFlowActivity.getCkappId()+",")
				.append(buyFlowActivity.getChildckappId()+",")
				.append(adItem)
				.append(tail);
				
			BuyFlowActivity bfa = new BuyFlowActivity();
			bfa.setDeviceType(buyFlowActivity.getDeviceType());
			bfa.setCkappId(buyFlowActivity.getCkappId());
			bfa.setChildckappId(buyFlowActivity.getChildckappId());
			bfa.setAdItem(adItem);
			
			BuyFlowActivity isExist = buyFlowActivityService.isExist(bfa);
			if(isExist != null){
				i++;
				msg.append(adItem+",");
				continue;
			}
			
			bfa.setId((StringUtils.isNotBlank(buyFlowActivity.getId())?buyFlowActivity.getId():""));
			bfa.setMediaId(buyFlowActivity.getMediaId());
			bfa.setName(buyFlowActivity.getName()+"_"+adItem);
			bfa.setAdUrl(sb.toString());
			buyFlowActivityService.save(bfa);
		}
		String message = msg.substring(0, msg.length()-1)+"失败";
		if(i == 0){
			message = "保存推广成功";
		}
		addMessage(redirectAttributes, message);
		return "redirect:" + adminPath + "/buyflow/activity/list?repage";
	}

	@RequiresPermissions("buyflow:activity:edit")
	@RequestMapping(value = "delete")
	public String delete(BuyFlowActivity buyFlowActivity, RedirectAttributes redirectAttributes) {
		buyFlowActivityService.delete(buyFlowActivity);
		addMessage(redirectAttributes, "删除推广成功");
		return "redirect:" + adminPath + "/buyflow/activity/list?repage";

	}
	
}
 