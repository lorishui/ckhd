package me.ckhd.opengame.gamecode.web;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import me.ckhd.opengame.common.beanvalidator.BeanValidators;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.utils.excel.ExportExcel;
import me.ckhd.opengame.common.utils.excel.ImportExcel;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.gamecode.entity.GameCode;
import me.ckhd.opengame.gamecode.entity.GameCodeVer;
import me.ckhd.opengame.gamecode.service.GameCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
/**
 * 官网礼包码的导入，短信发送接口
 * @author llbas
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/game/gamecode")
public class GameCodeController extends BaseController {
	@Autowired
	private GameCodeService gameCodeService;
	
	/**
	 * 跳转官网码导入页面
	 * @param gameCode
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="importweb")
	public String getweb(GameCode gameCode,HttpServletRequest request,HttpServletResponse response, Model model) {
		Page<GameCode> page = gameCodeService.findPage(new Page<GameCode>(request, response), gameCode);
		model.addAttribute("page", page);
		//获取总量与已使用数量
		List<Object> data = gameCodeService.getUsedNum(gameCode.getCkAppId());
		model.addAttribute("data", data);
		List<Map<String, String>> allCkAppId = gameCodeService.getAllCkApp();
		model.addAttribute("allCkApp", allCkAppId);
		return "modules/app/gameCodeUpload";
	}
	
	/**
	 * 导入用户数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "import")
	public String importFile(@RequestParam("ckAppId")String ckAppId,@RequestParam("remark")String mark,@RequestParam("file")MultipartFile file,
			RedirectAttributes redirectAttributes) {
		try {
			 String remark = URLDecoder.decode(mark,"utf-8");
			if(StringUtils.isNotBlank(ckAppId)){
				Date date = new Date();
				GameCodeVer gcv = new GameCodeVer();
				String id = UUID.randomUUID().toString();
				gcv.setId(id);
				gcv.setCkAppId(ckAppId);
				gcv.setDate(date);
				gcv.setRemark(remark);
				gcv.setStatus("0");	//0 有效  1无效
				gameCodeService.saveGameCodeVer(gcv);
				
				int successNum = 0;
				int failureNum = 0;
				StringBuilder failureMsg = new StringBuilder();
				ImportExcel ei = new ImportExcel(file, 1, 0);
				List<GameCode> list = ei.getDataList(GameCode.class);
				for (GameCode gameCode : list) {
					try {
						gameCode.setStatus("0");	//未发放
						gameCode.setValid("0");		//未过期
						gameCode.setCkAppId(ckAppId);
						gameCode.setDate(date);
						gameCode.setVerId(id);
//					GameCode gc = gameCodeService.findGameCodeByCodeAndCkAppId(gameCode.getCode(),ckAppId);
//					if(gc != null){
//						continue;
//					}
						gameCodeService.save(gameCode);
						successNum++;
					} catch (ConstraintViolationException ex) {
						failureMsg.append("<br/>礼包码" +gameCode.getCode()
								+ " 导入失败：");
						List<String> messageList = BeanValidators
								.extractPropertyAndMessageAsList(ex, ": ");
						for (String message : messageList) {
							failureMsg.append(message + "; ");
							failureNum++;
						}
					} catch (Exception ex) {
						failureMsg.append("<br/>礼包码" +gameCode.getCode()
								+ " 导入失败：" + ex.getMessage());
					}
				}
				if (failureNum > 0) {
					failureMsg.insert(0, "，失败 " + failureNum + " 条礼品码，导入信息如下：");
				}
				addMessage(redirectAttributes, "已成功导入 " + successNum + " 条礼品码"
						+ failureMsg);
			}else{
				addMessage(redirectAttributes, "未选择游戏!");
			}
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入礼品码失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/game/gamecode/importweb";
	}

	/**
	 * 下载导入消耗数据模板
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			
			String fileName = "礼品码导入模板.xlsx";
			List<GameCode> list = Lists.newArrayList();
			list.add(gameCodeService.findList(new GameCode()).get(0));
			new ExportExcel("礼包码", GameCode.class, 2).setDataList(list)
					.write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + adminPath + "/game/gamecode/importweb";
	}
	
	
	/**
	 * 短信发送礼包码接口
	 * 返回值 0：成功  1短信发送失败 2重复领取 3礼包发完 4参数错误
	 * @param phoneNum
	 * @param ckAppId
	 * @param callback
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "sendtext",produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String sendtext(@RequestParam("phoneNum") String phoneNum,@RequestParam("ckAppId") String ckAppId,@RequestParam("callback")String callback,HttpServletRequest request) {
		JSONObject jo = new JSONObject();
		String result = "";
		if(StringUtils.isNoneBlank(phoneNum)&&StringUtils.isNotBlank(ckAppId)){
				result = gameCodeService.send(phoneNum,ckAppId);
		}else{
			jo.put("result", "4");			//参数错误
			result = jo.toJSONString();
		}
		return callback+"("+result+")";
	}
	/**
	 * 邮件发送礼包码接口
	 * 返回值 0：邮件已发送 2重复领取 3礼包发完 4参数错误
	 * @param phoneNum
	 * @param ckAppId
	 * @param callback
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "sendEmail",produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String sendEmail(@RequestParam("Eaddr") String Eaddr,@RequestParam("ckAppId") String ckAppId,@RequestParam("callback")String callback,HttpServletRequest request) {
		JSONObject jo = new JSONObject();
		String result = "";
		if(StringUtils.isNoneBlank(Eaddr)&&StringUtils.isNotBlank(ckAppId)){
			result = gameCodeService.sendEmail(Eaddr,ckAppId);
		}else{
			jo.put("result", "4");			//参数错误
			result = jo.toJSONString();
		}
		return callback+"("+result+")";
	}
	
	/**
	 * 跳转官网码版本页面
	 * @param gcv
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value="validweb")
	public String getvalidweb(GameCodeVer gcv,HttpServletRequest request,HttpServletResponse response, Model model) {
		Page<GameCodeVer> page = gameCodeService.findGCVPage(new Page<GameCodeVer>(request, response), gcv);
		model.addAttribute("page", page);
		
		List<Map<String, String>> allCkAppId = gameCodeService.getAllCkApp();
		model.addAttribute("allCkApp", allCkAppId);
		return "modules/app/gameCodeValid";
	}
	
	@RequestMapping(value="changeVer")
	public String changeVer(@RequestParam("id")String id,@RequestParam("status")String status){
		GameCodeVer gcv = new GameCodeVer();
		gcv.setId(id);
		gcv.setStatus(status.equals("0")?"1":"0");
		gameCodeService.updateGCV(gcv);
		return "redirect:" + adminPath + "/game/gamecode/validweb";
	}
	
	
}