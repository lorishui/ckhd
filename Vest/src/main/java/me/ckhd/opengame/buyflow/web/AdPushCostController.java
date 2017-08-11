package me.ckhd.opengame.buyflow.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import me.ckhd.opengame.buyflow.entity.AdPushCost;
import me.ckhd.opengame.buyflow.service.AdPushCostService;
import me.ckhd.opengame.common.beanvalidator.BeanValidators;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.utils.excel.ExportExcel;
import me.ckhd.opengame.common.utils.excel.ImportExcel;
import me.ckhd.opengame.common.web.BaseController;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;

@Controller
@RequestMapping(value = "${adminPath}/app/adPushCost")
public class AdPushCostController extends BaseController {

	@Autowired
	private AdPushCostService adPushCostService;

	@ModelAttribute
	public AdPushCost get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return adPushCostService.get(id);
		} else {
			return new AdPushCost();
		}
	}

	@RequiresPermissions("app:adPushCost:view")
	@RequestMapping(value = { "list", "" })
	public String list(@RequestParam("adPushDetailId") String adPushDetailId,
			AdPushCost adPushCost, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		adPushCost.setAdPushDetailId(adPushDetailId);
		Page<AdPushCost> page = adPushCostService.findPage(
				new Page<AdPushCost>(request, response), adPushCost);
		model.addAttribute("page", page);
		model.addAttribute("adPushDetailId", adPushDetailId);
		model.addAttribute("startDate", adPushCost.getStartDate());
		model.addAttribute("endDate", adPushCost.getEndDate());
		return "modules/buyflow/adPushCostList";

	}

	/*@RequiresPermissions("app:adPushCost:view")
	@RequestMapping(value = { "list", "" })
	public String findListByAdPushDetailId(
			@RequestParam("adPushDetailId") String adPushDetailId,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		List<AdPushCost> list = adPushCostService
				.findListByAdPushDetailId(adPushDetailId);
		model.addAttribute("list", list);
		model.addAttribute("adPushDetailId", adPushDetailId);
		return "modules/app/adPushCostList";

	}*/

	@RequiresPermissions("app:adPushCost:view")
	@RequestMapping(value = "form")
	public String form(@RequestParam("adPushDetailId") String adPushDetailId,
			AdPushCost adPushCost, Model model) {
		AdPushCost promotion = new AdPushCost();
		if (StringUtils.isNotEmpty(adPushCost.getId())) {
			promotion = adPushCostService.get(adPushCost.getId());
		}
		promotion.setAdPushDetailId(adPushDetailId);
		model.addAttribute("adPushCost", promotion);
		/*
		 * List<Map<String, String>> allGames = adPushCostService.getAllGames();
		 * 
		 * model.addAttribute("allGames",allGames);
		 */
		return "modules/buyflow/adPushCostForm";
	}

	@RequiresPermissions("app:adPushCost:edit")
	@RequestMapping(value = "save")
	public String save(AdPushCost adPushCost, HttpServletRequest request,
			Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, adPushCost)) {
			return form(adPushCost.getAdPushDetailId(), adPushCost, model);
		}
		AdPushCost apc = adPushCostService.findByDateAndAdPushDetailId(adPushCost.getDate(),adPushCost.getAdPushDetailId());
		if (apc != null) {
			adPushCost.setId(apc.getId());
		}
		// 保存
		adPushCostService.save(adPushCost);
		addMessage(redirectAttributes, "保存消耗数据成功");
		return "redirect:" + adminPath + "/app/adPushCost/list?adPushDetailId="
				+ adPushCost.getAdPushDetailId();
	}

	@RequestMapping(value = "delete")
	@RequiresPermissions("app:adPushCost:edit")
	public String delete(AdPushCost adPushCost,
			RedirectAttributes redirectAttributes) {
		adPushCostService.delete(adPushCost);
		addMessage(redirectAttributes, "删除消耗数据成功");
		return "redirect:" + adminPath + "/app/adPushCost/list?adPushDetailId="
				+ adPushCost.getAdPushDetailId();

	}

	/**
	 * 导出消耗数据
	 * 
	 * @param adPushCost
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	/*@RequiresPermissions("app:adPushCost:view")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(AdPushCost adPushCost, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "消耗数据" + DateUtils.getDate("yyyyMMddHHmmss")
					+ ".xlsx";
			Page<AdPushCost> page = adPushCostService.findPage(
					new Page<AdPushCost>(request, response, -1), adPushCost);
			new ExportExcel("消耗数据", AdPushCost.class)
					.setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出消耗数据失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/app/adPushCost/list?adPushDetailId="
				+ adPushCost.getAdPushDetailId();
	}*/

	/**
	 * 导入用户数据
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("app:adPushCost:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(@RequestParam("adPushDetailId")String adPushDetailId,MultipartFile file,
			RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<AdPushCost> list = ei.getDataList(AdPushCost.class);
			for (AdPushCost adPushCost : list) {
				try {
					adPushCost.setAdPushDetailId(adPushDetailId);
					AdPushCost apc = adPushCostService.findByDateAndAdPushDetailId(adPushCost
							.getDate(),adPushDetailId);
					if (apc != null) {
						adPushCost.setId(apc.getId());
					}
					adPushCostService.save(adPushCost);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>日期 " + adPushCost.getDate()
							+ " 导入失败：");
					List<String> messageList = BeanValidators
							.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>日期 " + adPushCost.getDate()
							+ " 导入失败：" + ex.getMessage());
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条用户"
					+ failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/app/adPushCost/list?adPushDetailId=" + adPushDetailId;
	}

	/**
	 * 下载导入消耗数据模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("app:adPushCost:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(@RequestParam("adPushDetailId")String adPushDetailId,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			
			String fileName = "用户数据导入模板.xlsx";
			List<AdPushCost> list = Lists.newArrayList();
			list.add(adPushCostService.findList(new AdPushCost()).get(0));
			new ExportExcel("消耗数据", AdPushCost.class, 2).setDataList(list)
					.write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath
				+ "/app/adPushCost/list?adPushDetailId=" + adPushDetailId;
	}

}
