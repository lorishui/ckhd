package me.ckhd.opengame.game.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.game.entity.UpdateVersion;
import me.ckhd.opengame.game.service.UpdateVersionService;

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

@Controller
@RequestMapping(value = "${adminPath}/game/updateVersion")
public class UpdateVersionController extends BaseController{
	@Autowired
	private UpdateVersionService updateVersionService;
	
	@ModelAttribute
	public  UpdateVersion get(@RequestParam(required=false) String id){
		if (StringUtils.isNotBlank(id)) {
			return  updateVersionService.get(id);
		}else{
			return  new UpdateVersion();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(UpdateVersion updateVersion,HttpServletRequest request, HttpServletResponse response, Model model) {
		if (!Verdict.isAllow(updateVersion.getCkAppId())) {
			return "403";
		}
		Page<UpdateVersion> page = updateVersionService.findPage(new Page<UpdateVersion>(request, response), updateVersion);
        model.addAttribute("page", page);
		return "modules/game/updateVersionList";
		
	}
	
	@RequiresPermissions("game:updateVersion:edit")
	@RequestMapping(value = "save")
	public String save(UpdateVersion updateVersion, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if (!Verdict.isAllow(updateVersion.getCkAppId())) {
			return "403";
		}
		if (!beanValidator(model, updateVersion)){
			return form(updateVersion, model);
		}
		addMessage(redirectAttributes, "上传失败");
		String dir = Global.getConfig("cdnDir")+updateVersion.getCkAppId()+"/"+updateVersion.getCkChannelId()+"/"+updateVersion.getVersionName()+"/";
//		String url = Global.getConfig("cdnUrl")+updateVersion.getCkAppId()+"/"+updateVersion.getCkChannelId()+"/";
        File files = new File(dir);
        String path = "";
        
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
                if(file != null){  
                    //取得当前上传文件的文件名称   类型
                    String myFileName = file.getOriginalFilename();
                    //生成文件名
                    String extName = updateVersion.getCkAppId()+"_"+updateVersion.getCkChannelId()
                    		+"_"+updateVersion.getVersionName()+"_"+System.currentTimeMillis();
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
                    if(myFileName.trim() !=""){  
                    	System.out.println(file.getContentType());
                        //重命名上传后的文件名  
                        String fileName = extName + myFileName.substring(myFileName.lastIndexOf("."), myFileName.length());
                        
                        //定义上传路径  
                        path =  dir + fileName;  
                        File localFile = new File(path);  
                        try {
                        	updateVersion.setSize(file.getSize());
                            file.transferTo(localFile);
                            updateVersion.setPath(dir);
                            updateVersion.setFileName(fileName);
                            updateVersion.setCreateDate(new Date());
                         // 保存 
                            updateVersionService.save(updateVersion); 
                    		addMessage(redirectAttributes, "保存成功");
                        } catch (IllegalStateException e) {
                            logger.error("响应异常", e);
                        } catch (IOException e) {
                        	logger.error("io流异常", e);
                        }  
                    }  
                }   
            } 
        }
		// 保存 
		return "redirect:" + adminPath + "/game/updateVersion/list?repage";
	}
	
	@RequestMapping(value = "delete")
	@RequiresPermissions("game:updateVersion:edit")
	public  String delete(UpdateVersion updateVersion , RedirectAttributes redirectAttributes){
		if (!Verdict.isAllow(updateVersion.getCkAppId())) {
			return "403";
		}
		updateVersionService.delete(updateVersion); 
		addMessage(redirectAttributes, "删除运营商app信息成功");
		return  "redirect:" + adminPath + "/game/updateVersion/list?repage";
		
	}
	
	@RequiresPermissions("game:updateVersion:view")
	@RequestMapping(value = "form")
	public String form(UpdateVersion updateVersion, Model model) {
		model.addAttribute("updateVersion", updateVersion);
		return "modules/game/updateVersionForm";
	}
	
}
