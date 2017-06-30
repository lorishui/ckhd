package me.ckhd.opengame.app.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import me.ckhd.opengame.app.entity.AppCarriers;
import me.ckhd.opengame.app.entity.Paycode;
import me.ckhd.opengame.app.service.AppCarriersService;
import me.ckhd.opengame.common.beanvalidator.BeanValidators;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;

/**
 * AppCarriersController
 * @author wesley
 * @version 2015-07-03
 */
@Controller
@RequestMapping(value = "${adminPath}/app/appcarriers")
public class AppCarriersController extends BaseController{
	@Autowired
	private AppCarriersService appCarriersService;
	
	@ModelAttribute
	public  AppCarriers get(@RequestParam(required=false) String id){
		if (StringUtils.isNotBlank(id)) {
			return  appCarriersService.get(id);
		}else{
			return  new AppCarriers();
		}
	}
	
	
	@RequiresPermissions("app:appcarriers:view")
	@RequestMapping(value = {"list", ""})
	public String list(AppCarriers appCarriers,HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!Verdict.isAllow(appCarriers.getCkappId())) {
			return "403";
		}
		Page<AppCarriers> page = appCarriersService.findPage(new Page<AppCarriers>(request, response), appCarriers);
        model.addAttribute("page", page);
		return "modules/app/appcarriersList";
		
	}
	
	@RequiresPermissions("app:appCarriers:view")
	@RequestMapping(value = "form")
	public String form(AppCarriers appCarriers, Model model) {
		model.addAttribute("appCarriers", appCarriers);
		return "modules/app/appcarriersForm";
	}
	
	
	
	@RequestMapping("queryAppCarriers")
	@ResponseBody
	public List<Map<String, String>> stats(@RequestParam("ckAppId") String ckAppId,@RequestParam("carriersType") String carriersType) {
		AppCarriers appCarriers = new AppCarriers();
		appCarriers.setCkappId(ckAppId);
		appCarriers.setCarriersType(carriersType);
		
		List<AppCarriers> queryList = appCarriersService.findList(appCarriers);
		List<Map<String, String>> rst = new ArrayList<Map<String, String>>();
		for(AppCarriers vo : queryList){
			Map<String, String> map = new HashMap<String, String>();
			
			map.put("appId", vo.getAppId());
			map.put("appName", vo.getAppName());
			
			rst.add(map);
		}
		return rst;
	}
	
	@RequiresPermissions("app:appCarriers:edit")
	@RequestMapping(value = "save")
	public String save(AppCarriers appCarriers, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!Verdict.isAllow(appCarriers.getCkappId())) {
			return "403";
		}
		if (!beanValidator(model, appCarriers)){
			return form(appCarriers, model);
		}
		// 保存 
		appCarriersService.save(appCarriers); 
		addMessage(redirectAttributes, "保存运营商app'" + appCarriers.getAppName() + "'成功");
		return "redirect:" + adminPath + "/app/appcarriers/list?repage";
	}
	
	
	@RequestMapping(value = "delete")
	@RequiresPermissions("app:appCarriers:edit")
	public  String delete(AppCarriers appCarriers , RedirectAttributes redirectAttributes){
		if (!Verdict.isAllow(appCarriers.getCkappId())) {
			return "403";
		}
		appCarriersService.delete(appCarriers); 
		addMessage(redirectAttributes, "删除运营商app信息成功");
		return  "redirect:" + adminPath + "/app/appcarriers/list?repage";
		
	}
	
	/****************************************paycode**********************************************************/
	@RequiresPermissions("app:appCarriers:edit")
	@RequestMapping(value = {"paycode/list", "paycode"})
	public String listPaycode(Paycode paycode,HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute(paycode);
		Page<Paycode> page = appCarriersService.findPaycodePage(new Page<Paycode>(request, response), paycode);
        model.addAttribute("page", page);
		return "modules/app/paycodeList";
		
	}
	
	@RequiresPermissions("app:appCarriers:view")
	@RequestMapping(value = "paycode/form")
	public String formPaycode(Paycode paycode, Model model) {
		if (StringUtils.isNotBlank(paycode.getId())) {
			paycode =   appCarriersService.getPaycode(paycode.getId());
		} 
		model.addAttribute("paycode", paycode);
		return "modules/app/paycodeForm";
	}
	
	
	@RequiresPermissions("app:appCarriers:edit")
	@RequestMapping(value = "paycode/save")
	public String savePaycode(Paycode paycode, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, paycode)){
			return formPaycode(paycode, model);
		}
		if(!checkPaycode(paycode)){
			// 保存 
			appCarriersService.savePaycode(paycode); 
			addMessage(redirectAttributes, "保存计费点'" + paycode.getName() + "'成功");
		}else{
			addMessage(redirectAttributes, "保存失败,失败原因 ：'" + paycode.getPaycode() + "'计费编码已经存在");
		}
		
		return "redirect:" + adminPath + "/app/appcarriers/paycode/list?repage";
	}
	
	
 
	public boolean checkPaycode(Paycode paycode) {
		return appCarriersService.getByPaycodeAndCarriersType(paycode); 
	}
	
	
	@RequiresPermissions("app:appCarriers:edit")
	@RequestMapping(value = "paycode/delete")
	public String deletePaycode(Paycode paycode , RedirectAttributes redirectAttributes){
		appCarriersService.deletePaycode(paycode); 
		addMessage(redirectAttributes, "删除运营商app计费点信息成功");
		return  "redirect:" + adminPath + "/app/appcarriers/paycode/list?repage";
		
	}
	
	
	
	/**
	 * 下载导入计费点数据模板
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
    @RequestMapping(value = "paycode/import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "计费点数据导入模板.xlsx";
            Paycode paycode;
            if(appCarriersService.getPaycode("1") == null){
            	paycode = new Paycode();
            }else{
            	paycode =   appCarriersService.getPaycode("1");
            }
    		List<Paycode> list = Lists.newArrayList(); list.add(paycode);
    		new ExportExcel("计费点数据", Paycode.class, 2).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/app/appcarriers/paycode/list?repage";
    }
	
	
	
    /**
	 * 导入计费点数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
    @RequestMapping(value = "paycode/import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Paycode> list = ei.getDataList(Paycode.class);
			boolean valid = true;
			for (Paycode paycode : list) {
				if (paycode.getAppId() == null
						|| paycode.getAppId().indexOf('.') >= 0 || paycode.getAppId().indexOf('E') >= 0) {
					// 格式不对
					valid = false;
					break;
				}
			}
			if(!valid){
				throw new Exception("AppId格式不对，请设置成字符型");
			}
			for (Paycode paycode : list){
				try{
					 	appCarriersService.savePaycode(paycode);
						successNum++;
					 
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>计费点 "+paycode.getPaycode()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>计费点 "+paycode.getPaycode()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条计费点信息。"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入计费点失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/app/appcarriers/paycode/list?repage";
    }
    
    @RequestMapping("getList")
    @ResponseBody
    public Object getList(String ckAppId) {
    	List<Map<String,String>> data = new ArrayList<Map<String,String>>();
    	if(StringUtils.isNotBlank(ckAppId)){
    		AppCarriers app = new AppCarriers();
    		app.setCkappId(ckAppId);
    		List<AppCarriers> list = appCarriersService.findList(app);
    		for(int i=0;i<list.size();i++){
    			Map<String,String> map = new HashMap<String, String>();
    			map.put("id", list.get(i).getAppId());
    			map.put("name", list.get(i).getAppName());
    			data.add(map);
    		}
    	}
		return data;
	}
}
