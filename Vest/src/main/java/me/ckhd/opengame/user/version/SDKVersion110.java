package me.ckhd.opengame.user.version;

import java.util.Date;
import java.util.regex.Pattern;

import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.SendMailUtil;
import me.ckhd.opengame.common.utils.SendMailUtils;
import me.ckhd.opengame.common.utils.SendSmsOtherUtils;
import me.ckhd.opengame.common.utils.SendSmsUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.sys.service.SystemService;
import me.ckhd.opengame.sys.utils.DictUtils;
import me.ckhd.opengame.user.entity.RoleInfo;
import me.ckhd.opengame.user.entity.UserInfo;
import me.ckhd.opengame.user.model.DataRequest;
import me.ckhd.opengame.user.model.Result;
import me.ckhd.opengame.user.model.Session;
import me.ckhd.opengame.user.service.RoleService;
import me.ckhd.opengame.user.service.UserInfoService;
import me.ckhd.opengame.user.utils.Constant;
import me.ckhd.opengame.user.utils.ErrorCode;
import me.ckhd.opengame.user.utils.RedisClientTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("SDKVersion110")
public class SDKVersion110 extends SDKVersion{
	
	@Autowired
	public UserInfoService userInfoService;
	
	@Autowired
	public RoleService roleInfoService;
	
	@Autowired
	public RedisClientTemplate redisClientTemplate ;
	
	/**
	 * 注册
	 * @param dataRequest
	 * @return
	 */
	public String register(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		UserInfo user = new UserInfo();
		if( null != dataRequest.getUserInfo() && null != dataRequest.getUserInfo().getPassword()){
			String newPassword = SystemService.entryptPassword(dataRequest.getUserInfo().getPassword());
			user.setPwd(newPassword);
			String userAccount = getGenAccount();
			String userId = "";
			while(userInfoService.isExist(userAccount)){
				userAccount = getGenAccount();
			}
			userId = userAccount.substring(2, userAccount.length());
			user.setUserAccount(userAccount);
			user.setUserId(userId);
			user.setIsUse(1);
			user.setCkAppId(dataRequest.getUserInfo().getCkAppId());
			user.setIndex(userAccount.substring(userAccount.length()-1, userAccount.length()));
			dataRequest.getUserInfo().setUserAccount(userAccount);
			dataRequest.getUserInfo().setOperate(2);
			userInfoService.save(user);
//			result = new Result(ErrorCode.SUCCESSS);
			return login(dataRequest);
		}
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
	
	/**
	 * 登陆
	 * @param dataRequest
	 * @return
	 */
	public String login(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		UserInfo user = new UserInfo();
		Session session = new Session();
		if( null != dataRequest.getUserInfo() && null != dataRequest.getUserInfo().getUserAccount()){
//			user.setPwd(dataRequest.getUserInfo().getPassword());
			String userAccount = dataRequest.getUserInfo().getUserAccount();
			user.setUserAccount(userAccount);
			user.setIndex(userAccount.substring(userAccount.length()-1, userAccount.length()));
			UserInfo re = userInfoService.get(user);
			if(re == null){
				result = new Result(ErrorCode.USER_PWD_ERROR); 
			}else{
				boolean is = SystemService.validatePassword(dataRequest.getUserInfo().getPassword(),re.getPwd());
				if(is){
					long time = System.currentTimeMillis();
					String token = CoderUtils.md5(time+dataRequest.getUserInfo().getUserAccount()+dataRequest.getUserInfo().getPassword(), "utf-8");
					redisClientTemplate.set(Constant.SESSION_TOKEN_KEY+dataRequest.getUserInfo().getUserAccount(),token, 24*60*60);
					session.setToken(token);
					session.setTime(time);
					result = new Result(ErrorCode.SUCCESSS);
				}else{
					result = new Result(ErrorCode.USER_PWD_ERROR); 
				}
			}
		}
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		json.put(dataRequest.getUserInfo().getShortName(), dataRequest.getUserInfo().buildJSON());
		json.put(Session.class.getSimpleName().toLowerCase(), session.buildJSON());
		return json.toJSONString();
	}
	
	/**
	 * 获取校验码
	 * @param dataRequest
	 * @return
	 */
	public String getCheckSum(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		if( null != dataRequest.getUserInfo()){
			if(dataRequest.getUserInfo().getEmail() != null){
				String patternStr="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"; 
				boolean is = Pattern.matches(patternStr, dataRequest.getUserInfo().getEmail());
				if(is){
					if(isHasNusm(dataRequest.getUserInfo().getEmail(),1)){
						String code = getRandCode();
						redisClientTemplate.set(Constant.EMAIL_CODE_VALUE_KEY+dataRequest.getUserInfo().getEmail(), code , 1800);
						String message = "亲爱的用户您好，你的验证码是："+code+"。【创酷互动】";
						try {
						    SendMailUtils.sendMail(dataRequest.getUserInfo().getEmail(), "手游账号验证码", message);//先阿里的
						} catch (Throwable e) {
						    log.error("email send failure!", e);
						    SendMailUtil.sendCommonMail(dataRequest.getUserInfo().getEmail(), "手游账号验证码", message);//在网易的
						}
						setSendTime(dataRequest.getUserInfo().getEmail(), 1);//添加次数
						result = new Result(ErrorCode.SUCCESSS);
					}else{
						result = new Result(ErrorCode.LIMIT_EXCEEDED);
					}
				}else{
					result = new Result(ErrorCode.EMAIL_WRONG);
				}
			}else if(dataRequest.getUserInfo().getPhoneNumber() != null){
				String patternStr="^1[3-5,7-9]\\d{9}$"; 
				boolean is = Pattern.matches(patternStr, dataRequest.getUserInfo().getPhoneNumber());
				if(is){
					if(isHasNusm(dataRequest.getUserInfo().getPhoneNumber(),2)){
						String code = getRandCode();
						redisClientTemplate.set(Constant.MOBILE_CODE_VALUE_KEY+dataRequest.getUserInfo().getPhoneNumber(), code , 1800);
//						boolean isSend = SendSmsUtils.send(dataRequest.getUserInfo().getPhoneNumber(), code);
						/*int i=0;
						while( !isSend && i<3 ){
							isSend = SendSmsUtils.send(dataRequest.getUserInfo().getPhoneNumber(), code);
							i++;
						}*/
						//加入其他的短信通道
                        String msg = DictUtils.getDictValue("1", "sms_template", "【创酷互动】验证码为%s，感谢您使用创酷互动服务，若非本人操作，请忽略此信息。");
                        boolean isSend = SendSmsOtherUtils.send(dataRequest.getUserInfo().getPhoneNumber(), String.format(msg, code));
						if(isSend){
							setSendTime(dataRequest.getUserInfo().getPhoneNumber(), 2);
							result = new Result(ErrorCode.SUCCESSS);
						}else{
						    //加入其他的短信通道
						    boolean otherSend = SendSmsUtils.send(dataRequest.getUserInfo().getPhoneNumber(), code);
						    if(otherSend){
						        setSendTime(dataRequest.getUserInfo().getPhoneNumber(), 2);
	                            result = new Result(ErrorCode.SUCCESSS);
						    }else{
						        result = new Result(ErrorCode.REQUEST_FAILURE);
						    }
						}
					}else{
						result = new Result(ErrorCode.LIMIT_EXCEEDED);
					}
				}else{//MOBILE_WRONG
					result = new Result(ErrorCode.MOBILE_WRONG);
				}
			}else{
				result = new Result(ErrorCode.NOT_EXIST_PARAMETER);
			}
		}else{
			result = new Result(ErrorCode.USER_MEG_NOT_SUBMIT);
		} 
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
	
	/**
	 * 获取用户绑定信息(找回密码) --13
	 * @param dataRequest
	 * @return
	 */
	public String getBindingMsg(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		if( dataRequest.getUserInfo() != null && dataRequest.getVerify() != null ){
			String code = redisClientTemplate.get(Constant.VERIFY_CODE_VALUE_KEY+dataRequest.getUserInfo().getUserAccount());
			if( null == code ){
				code = redisClientTemplate.get(Constant.VERIFY_CODE_VALUE_KEY+dataRequest.getRequest().getSession().getId());
			}
			if( dataRequest.getVerify().getCode() != null && dataRequest.getVerify().getType() == 3 && dataRequest.getVerify().getCode().toUpperCase().equals(code) && code != null){
				UserInfo user = new UserInfo();
				user.setUserAccount(dataRequest.getUserInfo().getUserAccount());
				user.setIndex(dataRequest.getUserInfo().getUserAccount().substring(dataRequest.getUserInfo().getUserAccount().length()-1, dataRequest.getUserInfo().getUserAccount().length()));
				UserInfo re = userInfoService.get(user);
				if( re != null ){
					dataRequest.getUserInfo().setEmail(re.getBindEmail());
					dataRequest.getUserInfo().setPhoneNumber(re.getBindMobile());
					result = new Result(ErrorCode.SUCCESSS);
					json.put(dataRequest.getUserInfo().getShortName(), dataRequest.getUserInfo().buildJSON());
				}else{
					result = new Result(ErrorCode.USER_NOT_EXIST);
				}
			}
			if(code == null){
				result = new Result(ErrorCode.FAILURE_CODES);
			}
		}
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
	
	/**
	 * 获取用户绑定信息(解除绑定) --15
	 * @param dataRequest
	 * @return
	 */
	public String getBindingMsgByUnbin(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		if( dataRequest.getUserInfo() != null && dataRequest.getSession() != null ){
			if( checkSessionToken(dataRequest) ){
				UserInfo user = new UserInfo();
				user.setUserAccount(dataRequest.getUserInfo().getUserAccount());
				user.setIndex(dataRequest.getUserInfo().getUserAccount().substring(dataRequest.getUserInfo().getUserAccount().length()-1, dataRequest.getUserInfo().getUserAccount().length()));
				UserInfo re = userInfoService.get(user);
				if( re != null ){
					dataRequest.getUserInfo().setEmail(re.getBindEmail());
					dataRequest.getUserInfo().setPhoneNumber(re.getBindMobile());
					result = new Result(ErrorCode.SUCCESSS);
					json.put(dataRequest.getUserInfo().getShortName(), dataRequest.getUserInfo().buildJSON());
				}else{
					result = new Result(ErrorCode.USER_NOT_EXIST);
				}
			}else{
				result = new Result(ErrorCode.NOT_LOGIN);
			}
		}
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
	
	/**
	 * 修改密码
	 * @param dataRequest
	 * @return
	 */
	public String changePassword(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.CHECK_CODE_ERROR);
		if( dataRequest.getUserInfo() != null && dataRequest.getVerify() != null ){
			String code = "";
			String index = dataRequest.getUserInfo().getUserAccount().substring(dataRequest.getUserInfo().getUserAccount().length()-1, dataRequest.getUserInfo().getUserAccount().length());
			if( dataRequest.getVerify().getType() == 2 && 
					userInfoService.EmailOrmobileMatchUserAccount(dataRequest.getUserInfo().getUserAccount(),dataRequest.getVerify().getType(),
							index,dataRequest.getUserInfo().getEmail()) ){
				code = redisClientTemplate.get(Constant.EMAIL_CODE_VALUE_KEY+dataRequest.getUserInfo().getEmail());
			}else if( dataRequest.getVerify().getType() == 1 && 
					userInfoService.EmailOrmobileMatchUserAccount(dataRequest.getUserInfo().getUserAccount(),dataRequest.getVerify().getType(),
							index,dataRequest.getUserInfo().getPhoneNumber() ) ){
				code = redisClientTemplate.get(Constant.MOBILE_CODE_VALUE_KEY+dataRequest.getUserInfo().getPhoneNumber());
			}
			if( null != code && code.length() > 0 && dataRequest.getVerify().getCode() != null
					&& dataRequest.getVerify().getType() != 0 && dataRequest.getVerify().getCode().equals(code)
					&& dataRequest.getUserInfo().getPassword() != null){
				UserInfo user = new UserInfo();
				user.setUserAccount(dataRequest.getUserInfo().getUserAccount());
				String newPassword = SystemService.entryptPassword(dataRequest.getUserInfo().getPassword());
				user.setPwd(newPassword);
				user.setIndex(dataRequest.getUserInfo().getUserAccount().substring(dataRequest.getUserInfo().getUserAccount().length()-1, dataRequest.getUserInfo().getUserAccount().length()));
				userInfoService.update(user);
				result = new Result(ErrorCode.SUCCESSS);
			}
		}
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
	
	/**
	 * 绑定邮箱或手机 --8
	 * @param dataRequest
	 * @return
	 */
	public String bindMobileOrEmail(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		if( dataRequest.getUserInfo() != null && dataRequest.getVerify() != null && dataRequest.getSession() != null){
			if(checkSessionToken(dataRequest)){
				String code = "";
				if( dataRequest.getVerify().getType() == 2){
					code = redisClientTemplate.get(Constant.EMAIL_CODE_VALUE_KEY+dataRequest.getUserInfo().getEmail());
				}else if( dataRequest.getVerify().getType() == 1){
					code = redisClientTemplate.get(Constant.MOBILE_CODE_VALUE_KEY+dataRequest.getUserInfo().getPhoneNumber());
				}
				if( dataRequest.getVerify().getCode() != null && dataRequest.getVerify().getType() != 0 && dataRequest.getVerify().getCode().equals(code)){
					if(!userInfoService.isBindEmailOrMobile(dataRequest.getUserInfo().getUserAccount(),dataRequest.getVerify().getType()) && !userInfoService.isExistEmail(dataRequest.getUserInfo().getEmail())){
						UserInfo user = new UserInfo();
						setUserIndex(user, dataRequest);
						if( dataRequest.getVerify().getType() ==2 && dataRequest.getUserInfo().getEmail() != null){
							user.setBindEmail(dataRequest.getUserInfo().getEmail());
							userInfoService.saveUserByBindEmail(user);
						}else if(dataRequest.getVerify().getType() ==1 && dataRequest.getUserInfo().getPhoneNumber() != null){
							user.setBindMobile(dataRequest.getUserInfo().getPhoneNumber());
							userInfoService.saveUserByBindMobile(user);
						}
						result = new Result(ErrorCode.SUCCESSS);
					}else{
						result = new Result(ErrorCode.ALREADY_BOUND);
					}
				}else{
					result = new Result(ErrorCode.VERIFY_CODE_ERROR);
				}
			}else{
				result = new Result(ErrorCode.NOT_LOGIN);
			}
		}
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
	
	/**
	 * 解除绑定邮箱或手机--9
	 * @param dataRequest
	 * @return
	 */
	public String unbindMobileOrEmail(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		if( dataRequest.getUserInfo() != null && dataRequest.getVerify() != null && dataRequest.getSession() != null){
			if(checkSessionToken(dataRequest)){
				String code = "";
				if( dataRequest.getVerify().getType() == 2){
					code = redisClientTemplate.get(Constant.EMAIL_CODE_VALUE_KEY+dataRequest.getUserInfo().getEmail());
				}else if( dataRequest.getVerify().getType() == 1){
					code = redisClientTemplate.get(Constant.MOBILE_CODE_VALUE_KEY+dataRequest.getUserInfo().getPhoneNumber());
				}
				if( dataRequest.getVerify().getCode() != null && dataRequest.getVerify().getType() != 0 && dataRequest.getVerify().getCode().equals(code)){
					if( dataRequest.getVerify().getType() == 2 ){
						if(dataRequest.getUserInfo().getEmail() != null && dataRequest.getUserInfo().getEmail().length() > 0){
							
							if( userInfoService.isExistEmail(dataRequest.getUserInfo().getEmail())){
								UserInfo user = new UserInfo();
								setUserIndex(user, dataRequest);
								if( dataRequest.getVerify().getType() == 2 && dataRequest.getUserInfo().getEmail() != null){
									user.setBindEmail(dataRequest.getUserInfo().getEmail());
									userInfoService.saveUserByUnBindEmail(user);
								}else if( dataRequest.getVerify().getType() == 1 && dataRequest.getUserInfo().getPhoneNumber() != null ){
									user.setBindMobile(dataRequest.getUserInfo().getPhoneNumber());
								}
								result = new Result(ErrorCode.SUCCESSS);
							}else{
								result = new Result(ErrorCode.NOT_BOUND);
							}
						}else{
							result = new Result(ErrorCode.EMAIL_WRONG);
						}
					}else if(dataRequest.getVerify().getType() == 1){
						if(dataRequest.getUserInfo().getPhoneNumber()!= null && dataRequest.getUserInfo().getPhoneNumber().length() > 0){
							
							if( userInfoService.isExistMobile(dataRequest.getUserInfo().getPhoneNumber())){
								UserInfo user = new UserInfo();
								setUserIndex(user, dataRequest);
								if( dataRequest.getVerify().getType() == 1 && dataRequest.getUserInfo().getPhoneNumber() != null){
									user.setBindMobile(dataRequest.getUserInfo().getPhoneNumber());
									userInfoService.saveUserByUnBindMobile(user);
								}else if( dataRequest.getVerify().getType() == 1 && dataRequest.getUserInfo().getPhoneNumber() != null ){
									user.setBindMobile(dataRequest.getUserInfo().getPhoneNumber());
								}
								result = new Result(ErrorCode.SUCCESSS);
							}else{
								result = new Result(ErrorCode.NOT_BOUND);
							}
						}else{
							result = new Result(ErrorCode.MOBILE_WRONG);
						}
					}
				}else{
					result = new Result(ErrorCode.CHECK_CODE_ERROR);
				}
			}else{
				result = new Result(ErrorCode.NOT_LOGIN);
			}
		}
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
	
	/**
	 * 校验登陆token --- 12
	 * @param dataRequest
	 * @return
	 */
	public String checkToken(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		if( dataRequest.getUserInfo() != null && dataRequest.getSession() != null){
			if(checkSessionToken(dataRequest)){
				result = new Result(ErrorCode.SUCCESSS);
			}else{
				result = new Result(ErrorCode.NOT_LOGIN);
			}
		}
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
	
	/**
	 * 角色信息收集 --- 10
	 * @param dataRequest
	 * @return
	 */
	public String roleMessageCollect(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		try{
		if( dataRequest.getRole() != null){
			if(StringUtils.isNotBlank(dataRequest.getRole().getCkAppId()) &&
					StringUtils.isNotBlank(dataRequest.getRole().getServerId()) &&
					StringUtils.isNotBlank(dataRequest.getRole().getRoleId()) &&
					StringUtils.isNotBlank(dataRequest.getRole().getUserId()) &&
					StringUtils.isNotBlank(dataRequest.getRole().getCkAppId())){
				RoleInfo role = new RoleInfo();
				setRole(role, dataRequest.getRole());
				log.info("service:"+roleInfoService);
				if(roleInfoService.isExist(role)){
					roleInfoService.update(role);
				}else{
					roleInfoService.insert(role);
				}
				result = new Result(ErrorCode.SUCCESSS);
			}else{
				result = new Result(ErrorCode.PARAM_IS_NULL);
			}
		}
		}catch(Exception e){
			log.error("数据库异常", e);
		}
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
	
	/**
	 * 修改密码通过旧密码  --6
	 * @param dataRequest
	 * @return
	 */
	public String changePwdByOldPwd(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		if( dataRequest.getUserInfo() != null && dataRequest.getSession() != null ){
			if( checkSessionToken(dataRequest) ){
				UserInfo user = new UserInfo();
				setUserIndex(user, dataRequest);
				UserInfo userInfo = userInfoService.get(user);
				boolean is = SystemService.validatePassword(dataRequest.getUserInfo().getOldPassword(),userInfo.getPwd() );
				if(is){
					String newPwd = SystemService.entryptPassword(dataRequest.getUserInfo().getPassword());
					user.setPwd(newPwd);
					userInfoService.update(user);
					result = new Result(ErrorCode.SUCCESSS);
				}else{
					result = new Result(ErrorCode.USER_PWD_ERROR);
				}
			}else{
				result = new Result(ErrorCode.NOT_LOGIN);
			}
		}
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
	
	/**
	 * 校验图片 校验码   --- 14
	 * @return
	 */
	public String checkVerfyCode(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		String code = null;
		if( dataRequest.getUserInfo() != null && dataRequest.getUserInfo().getUserAccount() != null){
			code = redisClientTemplate.get(Constant.VERIFY_CODE_VALUE_KEY+dataRequest.getUserInfo().getUserAccount());
		}
		if( code == null ){
			code = redisClientTemplate.get(Constant.VERIFY_CODE_VALUE_KEY+dataRequest.getRequest().getSession().getId());
		}
		if(code == null){	
			result = new Result(ErrorCode.FAILURE_CODES);
		}else{
			if(dataRequest.getVerify() != null && dataRequest.getVerify().getCode() != null && dataRequest.getVerify().getCode().toUpperCase().equals(code)){
				result = new Result(ErrorCode.SUCCESSS);
			}else{
				result = new Result(ErrorCode.VERIFY_CODE_ERROR);
			}
		}
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
	
	public String logout(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.SUCCESSS);
		if( dataRequest.getUserInfo() != null && dataRequest.getSession() != null){
			if(checkSessionToken(dataRequest)){
				String key = Constant.SESSION_TOKEN_KEY+dataRequest.getUserInfo().getUserAccount();
				redisClientTemplate.delete(key);
				result = new Result(ErrorCode.SUCCESSS);
			}/*else{
				result = new Result(ErrorCode.SESSION_INVALID);
			}*/
		}/*else{
			result = new Result(ErrorCode.INVALID_REQUEST);
		}*/
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
	
	public String getGenAccount(){
		StringBuffer sb = new StringBuffer("ck");
		for(int i=0;i<8;i++){
			int n = (int) (Math.random()*10);
			sb.append(n);
		}
		return sb.toString();
	}

	public String getRandCode(){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<6;i++){
			int n = (int) (Math.random()*10);
			sb.append(n);
		}
		return sb.toString();
	}
	
	public boolean isHasNusm(String data,int type){
		String obj = null;
		if(type == 1){
			obj = redisClientTemplate.get(Constant.EMAIL_CODE_TIME_KEY+data);
		}else if(type == 2){
			obj = redisClientTemplate.get(Constant.PHONE_CODE_TIME_KEY+data);
		}
		if( obj != null){
			if( Integer.parseInt(obj) <= 10 ){
				return true;
			}
		}else{
			long n =  DateUtils.leftSeconds();
			if(type == 1){
				redisClientTemplate.set(Constant.EMAIL_CODE_TIME_KEY+data,1+"",(int)n);
				return true;
			}else if(type == 2){
				redisClientTemplate.set(Constant.PHONE_CODE_TIME_KEY+data,1+"",(int)n);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 设置邮箱/手机正确发送次数
	 * @param data
	 * @param type
	 */
	private void setSendTime(String data,int type){
		long n =  DateUtils.leftSeconds();
		if(type == 1){
			String obj = redisClientTemplate.get(Constant.EMAIL_CODE_TIME_KEY+data);
			redisClientTemplate.set(Constant.EMAIL_CODE_TIME_KEY+data,(Integer.parseInt(obj.toString())+1)+"",(int)n);
		}else if(type == 2){
			String obj = redisClientTemplate.get(Constant.PHONE_CODE_TIME_KEY+data);
			redisClientTemplate.set(Constant.PHONE_CODE_TIME_KEY+data,(Integer.parseInt(obj.toString())+1)+"",(int)n);
		}
	}
	
	/**
	 * 验证session Token
	 * @param dataRequest
	 * @return
	 */
	public boolean checkSessionToken(DataRequest dataRequest){
		String token = redisClientTemplate.get(Constant.SESSION_TOKEN_KEY+dataRequest.getUserInfo().getUserAccount());
		if( null != token && token.equals(dataRequest.getSession().getToken())){
			return true;
		}
		return false;
	}
	
	public void setUserIndex(UserInfo user,DataRequest dataRequest){
		String userAccount = dataRequest.getUserInfo().getUserAccount();
		user.setUserAccount(userAccount);
		user.setIndex(userAccount.substring(userAccount.length()-1, userAccount.length()));
	}
	
	public void setRole(RoleInfo role,me.ckhd.opengame.user.model.Role oldRole){
		role.setUserId(oldRole.getUserId());
		role.setCkChannelId(oldRole.getCkChannelId());
		role.setCkAppId(oldRole.getCkAppId());
		role.setRoleId(oldRole.getRoleId());
		role.setRoleLevel(oldRole.getRoleLevel());
		role.setRoleName(oldRole.getRoleName());
		role.setServerId(oldRole.getServerId());
		role.setServerName(oldRole.getServerName());
		role.setUuid(oldRole.getUuid());
		role.setCreateDate(new Date());
		role.setUpdateDate(new Date());
	}
	
	/*private String replaceEmailOrMObile(String data){
		StringBuffer sb = new StringBuffer();
		if( data != null && data.length() > 6){
			sb.append(data.substring(0,4));
			for(int i=0;i<data.length()-6;i++){
				sb.append("*");
			}
			sb.substring(data.length()-2);
			data = sb.toString();
		}
		return data;
	}*/
	
	/**
	 * 一键注册获取账号密码-16
	 * @param dataRequest
	 * @return
	 */
	public String oneClickGetMsg(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		if( null != dataRequest.getUserInfo() && dataRequest.getUserInfo().getOperate()==5){
			String pass = getRandCode();//随机六位密码
			String userAccount = getGenAccount();
			while(userInfoService.isExist(userAccount)){
				userAccount = getGenAccount();
			}
			dataRequest.getUserInfo().setUserAccount(userAccount);
			dataRequest.getUserInfo().setOperate(5);
			dataRequest.getUserInfo().setShowPassword(pass);
			dataRequest.getUserInfo().setPassword(pass);
			json.put(dataRequest.getUserInfo().getShortName(), dataRequest.getUserInfo().buildJSON());
			result = new Result(ErrorCode.SUCCESSS);
		}
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
	
	/**
	 * 一键注册-注册-17
	 * @param dataRequest
	 * @return
	 */
	public String oneClickRegister(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		UserInfo user = new UserInfo();
		if( null != dataRequest.getUserInfo() && dataRequest.getUserInfo().getOperate()==5 &&
				StringUtils.isNotBlank(dataRequest.getUserInfo().getPassword()) &&
				dataRequest.getUserInfo().getPassword().length() == 32 &&
				StringUtils.isNotBlank(dataRequest.getUserInfo().getUserAccount()) ){
			if(!userInfoService.isExist(dataRequest.getUserInfo().getUserAccount())){
				String newPassword = SystemService.entryptPassword(dataRequest.getUserInfo().getPassword());
				user.setPwd(newPassword);
				String userAccount = dataRequest.getUserInfo().getUserAccount();
				String userId = "";
				userId = userAccount.substring(2, userAccount.length());
				user.setUserAccount(userAccount);
				user.setUserId(userId);
				user.setIsUse(1);
				user.setCkAppId(dataRequest.getUserInfo().getCkAppId());
				user.setIndex(userAccount.substring(userAccount.length()-1, userAccount.length()));
				dataRequest.getUserInfo().setUserAccount(userAccount);
				dataRequest.getUserInfo().setOperate(2);
				userInfoService.save(user);
				result = new Result(ErrorCode.SUCCESSS);
				return login(dataRequest);
			}else{
				result = new Result(ErrorCode.USER_IS_EXIST);
			}
		}
		json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
		return json.toJSONString();
	}
}
