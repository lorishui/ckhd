package me.ckhd.opengame.buyflow.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.buyflow.entity.BuyFlowActivity;
import me.ckhd.opengame.buyflow.service.BuyFlowActivityService;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "${adminPath}/buyflow/activity")
public class BuyFlowActivityController extends BaseController {
	@Autowired
	private BuyFlowActivityService buyFlowActivityService;
	
	@RequiresPermissions("buyflow:activity:view")
	@RequestMapping(value = { "list", "" })
	public String list(BuyFlowActivity buyFlowActivity, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Page<BuyFlowActivity> page = buyFlowActivityService.findPage(new Page<BuyFlowActivity>(request,
				response), buyFlowActivity);
		model.addAttribute("page", page);
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
		int a = name.lastIndexOf("_");
		bfa.setName(name.substring(0,a));
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
		String head  = DictUtils.getDictValue(buyFlowActivity.getMediaId(),"buyFlow_url_head","http://ol.haifurong.cn/buyflow/wead/");
		String tail = DictUtils.getDictValue(buyFlowActivity.getMediaId()+"_"+buyFlowActivity.getDeviceType(),"buyFlow_url_tail","?imei=__IMEIORI__&idfa=__IDFA__&time=__TS__");
		for (String adItem : adItemS) {
			StringBuilder sb = new StringBuilder();
			sb.append(head)
				.append(buyFlowActivity.getCkappId()+",")
				.append(buyFlowActivity.getChildckappId()+",")
				.append(adItem)
				.append(tail);
				
			BuyFlowActivity bfa = new BuyFlowActivity();
			bfa.setId((StringUtils.isNotBlank(buyFlowActivity.getId())?buyFlowActivity.getId():""));
			bfa.setDeviceType(buyFlowActivity.getDeviceType());
			bfa.setCkappId(buyFlowActivity.getCkappId());
			bfa.setChildckappId(buyFlowActivity.getChildckappId());
			bfa.setMediaId(buyFlowActivity.getMediaId());
			bfa.setAdItem(adItem);
			bfa.setName(buyFlowActivity.getName()+"_"+adItem);
			bfa.setAdUrl(sb.toString());
			buyFlowActivityService.save(bfa);
		}
		
		addMessage(redirectAttributes, "保存推广成功");
		return "redirect:" + adminPath + "/buyflow/activity/list?repage";
	}

	@RequiresPermissions("buyflow:activity:edit")
	@RequestMapping(value = "delete")
	public String delete(BuyFlowActivity buyFlowActivity, RedirectAttributes redirectAttributes) {
		buyFlowActivityService.delete(buyFlowActivity);
		addMessage(redirectAttributes, "删除推广成功");
		return "redirect:" + adminPath + "/buyflow/activity/list?repage";

	}
	
//	@RequestMapping(value = "createUrl")
//	@ResponseBody
//	public String createUrl(BuyFlowActivity buyFlowActivity, RedirectAttributes redirectAttributes) {
//		StringBuilder sb = new StringBuilder(DictUtils.getDictValue("url_head", "WeAD", "http://ol.haifurong.cn/buyflow/wead/"));
//		sb.append(buyFlowActivity.getCkappId()+",").append(buyFlowActivity.getChildckappId()+",").append(buyFlowActivity.getAdItem())
//			.append(DictUtils.getDictValue("url_tail","WeAD", "?imei=__IMEIORI__&idfa=__IDFA__&time=__TS__"));
//		return sb.toString();
//	}
}
 