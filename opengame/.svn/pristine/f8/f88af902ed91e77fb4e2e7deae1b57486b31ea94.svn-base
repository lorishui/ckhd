package me.ckhd.opengame.ad.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.ad.entity.AdResource;
import me.ckhd.opengame.ad.entity.AdUser;
import me.ckhd.opengame.ad.service.AdResourceService;
import me.ckhd.opengame.ad.service.AdUserService;
import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.FileTypeUtils;
import me.ckhd.opengame.common.utils.FreeMarkerTest;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.game.entity.UpdateVersion;
import me.ckhd.opengame.game.service.UpdateVersionService;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping(value = "${adminPath}/ad/adResource")
public class AdResourceController extends BaseController{
	
	@Autowired
	private AdResourceService adResourceService;
	
	@Autowired
	private UpdateVersionService updateVersionService;
	
	@Autowired
	private AdUserService adUserService;
	
	@ModelAttribute
	public  AdResource get(@RequestParam(required=false) String id){
		if (StringUtils.isNotBlank(id)) {
			return  adResourceService.get(id);
		}else{
			return  new AdResource();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(AdResource adResource,HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!Verdict.isAllow(adResource.getCkAppId())) {
			return "403";
		}
		Page<AdResource> page = adResourceService.findPage(new Page<AdResource>(request, response), adResource);
        model.addAttribute("page", page);
		return "modules/ad/adResourceList";	
	}
	
	@RequestMapping(value = {"list1", ""})
	public String list1(AdResource adResource,HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!Verdict.isAllow(adResource.getCkAppId())) {
			return "403";
		}
		Page<AdResource> page = adResourceService.findPage(new Page<AdResource>(request, response), adResource);
        model.addAttribute("page", page);
        AdUser adUser = new AdUser();
		model.addAttribute("adUser", adUserService.findList(adUser));
		return "modules/ad/adResourceList1";	
	}
	
	@RequiresPermissions("ad:adResource:edit")
	@RequestMapping(value = "save")
	public String save(AdResource adResource, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!Verdict.isAllow(adResource.getCkAppId())) {
			return "403";
		}
		if (!beanValidator(model, adResource)){
			return form(adResource, model);
		}
		adResourceService.save(adResource);
		// 保存 
		return "redirect:" + adminPath + "/ad/adResource/list?repage";
	}
	
	@RequiresPermissions("ad:adResource:edit")
	@RequestMapping(value = "save1")
	public String save1(AdResource adResource, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!Verdict.isAllow(adResource.getCkAppId())) {
			return "403";
		}
		if (!beanValidator(model, adResource)){
			return form(adResource, model);
		}
		adResourceService.save(adResource);
		// 保存 
		return "redirect:" + adminPath + "/ad/adResource/list1?repage";
	}
	
	@RequestMapping(value = "delete")
	@RequiresPermissions("ad:adResource:edit")
	public  String delete(AdResource adResource , RedirectAttributes redirectAttributes){
		if (!Verdict.isAllow(adResource.getCkAppId())) {
			return "403";
		}
		adResourceService.delete(adResource); 
		addMessage(redirectAttributes, "删除运营商app信息成功");
		return  "redirect:" + adminPath + "/ad/adResource/list?repage";
		
	}
	
	@RequestMapping(value = "delete1")
	@RequiresPermissions("ad:adResource:edit")
	public  String delete1(AdResource adResource , RedirectAttributes redirectAttributes){
		if (!Verdict.isAllow(adResource.getCkAppId())) {
			return "403";
		}
		adResourceService.delete(adResource); 
		addMessage(redirectAttributes, "删除运营商app信息成功");
		return  "redirect:" + adminPath + "/ad/adResource/list1?repage";
		
	}
	
	@RequiresPermissions("ad:adResource:view")
	@RequestMapping(value = "form")
	public String form(AdResource adResource, Model model) {
		model.addAttribute("adResource", adResource);
		return "modules/ad/adResourceForm";
	}
	
	@RequiresPermissions("ad:adResource:view")
	@RequestMapping(value = "form1")
	public String form1(AdResource adResource, Model model) {
		model.addAttribute("adResource", adResource);
		AdUser adUser = new AdUser();
		model.addAttribute("adUser", adUserService.findList(adUser));
		return "modules/ad/adResourceForm1";
	}
	
	@RequestMapping(value = "generateTemplate")
	public String generateTemplate(Model model,UpdateVersion updateVersion) {
		model.addAttribute("updateVersion", updateVersion);
		return "modules/ad/generateTemplate";
	}
	
	@RequestMapping(value = "generate")
	public String generate(Model model,UpdateVersion updateVersion,HttpServletRequest request, HttpServletResponse response) {
		if (!Verdict.isAllow(updateVersion.getCkAppId())) {
			return "403";
		}
		if (!beanValidator(model, updateVersion)){
			return "failure";
		}
		String rootPath=System.getProperty(WebUtils.DEFAULT_WEB_APP_ROOT_KEY);;
		String dir = rootPath +"a/";
        File files = new File(rootPath+"a");
        System.out.println(dir);
        if(!files.exists()){
            files.mkdir();
        }
         //创建一个通用的多部分解析器  
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
        
        //判断 request 是否有文件上传,即多部分请求  
        if(multipartResolver.isMultipart(request)){  
            //转换成多部分request    
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;  
            //取得request中的所有文件名  
            Iterator<String> iter = multiRequest.getFileNames();  
            while(iter.hasNext()){  
                //记录上传过程起始时的时间，用来计算上传时间  
//                int pre = (int) System.currentTimeMillis();
                //取得上传文件  
                MultipartFile file = multiRequest.getFile(iter.next());  
                if(file != null && file.getSize() <= 10*1024*1024){ 
                    //取得当前上传文件的文件名称   类型
                    String myFileName = file.getOriginalFilename();
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
                    if(myFileName.trim() !=""){  
                        String fileName = System.currentTimeMillis()+updateVersion.getCkAppId()
                        		+(StringUtils.isNotBlank(updateVersion.getCkChannelId())?updateVersion.getCkChannelId():"")+".html";
                        try {
                        	Map<String,Object> map = new HashMap<String, Object>();
                        	map.put("content", Base64.encodeBase64String(file.getBytes()));
                        	map.put("type",FileTypeUtils.getFileType(file.getBytes()));
                        	FreeMarkerTest test = new FreeMarkerTest();
                        	test.geneHtmlFile("insert.ftl",map, dir,fileName);
                        	updateVersion.setSize(file.getSize());
                            updateVersion.setPath("");
                            updateVersion.setFileName(fileName);
                            updateVersion.setCreateDate(new Date());
                            String adUrl = Global.getConfig("adUrl");
                            updateVersion.setUrl(adUrl+"a/"+fileName);
                            updateVersion.setType(2);
                         // 保存 
                            updateVersionService.save(updateVersion); 
                        } catch (IllegalStateException e) {
                            logger.error("响应异常", e);
                        } catch (IOException e) {
                        	logger.error("io流异常", e);
                        }  
                    }  
                }   
            } 
        }
        model.addAttribute("updateVersion", updateVersion);
		return templateList(updateVersion, request, response, model);
	}
	
	@RequestMapping(value = {"templateList", ""})
	public String templateList(UpdateVersion updateVersion,HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!Verdict.isAllow(updateVersion.getCkAppId())) {
			return "403";
		}
		updateVersion.setType(2);
		Page<UpdateVersion> page = updateVersionService.findPage(new Page<UpdateVersion>(request, response), updateVersion);
        model.addAttribute("page", page);
		return "modules/ad/templateList";	
	}
}
