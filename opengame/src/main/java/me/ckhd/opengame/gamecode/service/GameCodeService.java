package me.ckhd.opengame.gamecode.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.common.utils.SendMailUtil;
import me.ckhd.opengame.common.utils.SendMailUtils;
import me.ckhd.opengame.common.utils.SendSmsOtherUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.gamecode.dao.GameCodeDao;
import me.ckhd.opengame.gamecode.entity.GameCode;
import me.ckhd.opengame.gamecode.entity.GameCodeLog;
import me.ckhd.opengame.gamecode.entity.GameCodeVer;
import me.ckhd.opengame.gamecode.utils.SendTextUtil;
import me.ckhd.opengame.sys.entity.Dict;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

@Service
@Transactional(readOnly = true)
public class GameCodeService extends CrudService<GameCodeDao, GameCode> {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public GameCode findGameCodeByCodeAndCkAppId(String code,String ckAppId) {
		return dao.findGameCodeByCodeAndCkAppId(code,ckAppId);
	}
	
	
	@Transactional(readOnly = false)
	public GameCode getCode(String ckAppId) {
		GameCode gameCode = dao.getCode(new Random().nextInt(4),ckAppId);
		if(gameCode == null){
			return null;
		}
		int i = dao.delete(gameCode);
		if(i == 0){
			return getCode(ckAppId);
		}
		return gameCode;
	}
	
	public List<Map<String, String>> getAllCkApp() {
		return dao.findAllCkApp();
	}
	@Transactional(readOnly = false)
	public String send(String phoneNum, String ckAppId) {
		JSONObject jo = new JSONObject();
		
		GameCodeLog gameCodeLog = new GameCodeLog();
		gameCodeLog.setPhoneNum(phoneNum);
		gameCodeLog.setCkAppId(ckAppId);
		gameCodeLog.setDate(new Date());
		GameCodeLog boo = dao.checkPhoneNum(gameCodeLog);	//检查是否重复领取
		if(boo == null){
			GameCode gameCode = getCode(ckAppId);
			if(gameCode == null){
				jo.put("result", "3");		//3礼包发完
				return jo.toJSONString();
			}
			//短信发送1 
			boolean b = sendText(ckAppId,phoneNum,gameCode.getCode());
			if(!b){
				//短信发送2 ali
				b = SendTextUtil.send(ckAppId,phoneNum, gameCode.getCode());
			}
			if(b){
				log.info("手机号"+phoneNum+"礼包发送成功");
			}else{
				log.error("手机号"+phoneNum+"ali短信发送失败");
			}
			GameCodeLog gcl = new GameCodeLog(gameCode,b,phoneNum);
			gcl.setId(UUID.randomUUID().toString());
			gcl.setDate(new Date());
			dao.log(gcl);
			jo.put("result", b?"0":"1");	//0成功  1失败
		}
		else {
			jo.put("result", "2");			//2重复领取
		} 
		return jo.toJSONString();
	}


	private boolean sendText(String ckAppId, String phoneNum, String code) {
		String content = "";
		List<Dict> dictList = DictUtils.getDictList("sms_temp");
		for (Dict dict : dictList) {
			if("text".equals(dict.getValue())&& ckAppId.equals(dict.getLabel())){
				content = dict.getRemarks();
			}
		}
		if(StringUtils.isNotBlank(content)){
			content = String.format(content, code);
			return SendSmsOtherUtils.send(phoneNum, content);
		}
		return false;
	}


	public void saveGameCodeVer(GameCodeVer gcv) {
		dao.saveGameCodeVer(gcv);
	}


	public Page<GameCodeVer> findGCVPage(Page<GameCodeVer> page, GameCodeVer gcv) {
		gcv.setPage(page);
		page.setList(dao.findGCVList(gcv));
		return page;
	}


	public void updateGCV(GameCodeVer gcv) {
		dao.updateGCV(gcv);
	}


	public List<Object> getUsedNum(String string) {
		return dao.getUsedNum(string);
	}

	@Transactional(readOnly = false)
	public String sendEmail(String eaddr, String ckAppId) {
		JSONObject jo = new JSONObject();
		
		GameCodeLog gameCodeLog = new GameCodeLog();
		gameCodeLog.setPhoneNum(eaddr);
		gameCodeLog.setCkAppId(ckAppId);
		gameCodeLog.setDate(new Date());
		GameCodeLog boo = dao.checkPhoneNum(gameCodeLog);	//检查是否重复领取
		if (boo == null) {
			GameCode gameCode = getCode(ckAppId);
			if (gameCode == null) {
				jo.put("result", "3");		//3礼包发完
				return jo.toJSONString();
			}
			boolean b = email(eaddr, ckAppId, gameCode.getCode());
			if (b) {
				log.info("邮箱"+eaddr+"礼包发送成功");
			} else {
				log.error("邮箱"+eaddr+"礼包发送失败");
			}
			GameCodeLog gcl = new GameCodeLog(gameCode,b,eaddr);
			gcl.setId(UUID.randomUUID().toString());
			gcl.setDate(new Date());
			dao.log(gcl);
			jo.put("result", b?"0":"1");	//0成功  1失败
		}
		else {
			jo.put("result", "2");			//2重复领取
		} 
		return jo.toJSONString();
	}
	
	/**
	 * 
	 * @param eaddr	 邮件接收地址
	 * @param ckAppId	
	 * @param giftCode	游戏码
	 */
	public boolean email(String eaddr,String ckAppId,String giftCode) {
		String content = "";
		String subject = "";
		List<Dict> dictList = DictUtils.getDictList("sms_temp");
		for (Dict dict : dictList) {
			if("email".equals(dict.getValue())&& ckAppId.equals(dict.getLabel())){
				content = dict.getRemarks();
				subject = dict.getDescription();
			}
		}
		if (StringUtils.isNotBlank(content)&&StringUtils.isNotBlank(subject)) {
			content = String.format(content, giftCode);
			try {
				content = StringEscapeUtils.unescapeHtml(content);
				SendMailUtils.sendHtmlMail(SendMailUtils.getAliyunMailConfig(), eaddr, subject, content);
				return true;
			} catch (EmailException e) {
				log.error("邮件1发送失败："+e.getMessage(), e);
				return SendMailUtil.sendCommonMail(eaddr,subject, content);
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		boolean send = SendSmsOtherUtils.send("13066451353", "测试短信创酷互动");
		System.out.println("发送"+ (send ? "成功":"失败"));
	}
}