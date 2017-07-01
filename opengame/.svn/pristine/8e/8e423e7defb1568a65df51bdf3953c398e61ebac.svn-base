package me.ckhd.opengame.user.version;

import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.sys.service.SystemService;
import me.ckhd.opengame.user.entity.UserInfo;
import me.ckhd.opengame.user.model.DataRequest;
import me.ckhd.opengame.user.model.Result;
import me.ckhd.opengame.user.model.Session;
import me.ckhd.opengame.user.utils.Constant;
import me.ckhd.opengame.user.utils.ErrorCode;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("SDKVersion120")
public class SDKVersion120 extends SDKVersion110{
	
	@Override	
	public String login(DataRequest dataRequest) {
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		UserInfo user = new UserInfo();
		Session session = new Session();
		if( null != dataRequest.getUserInfo() && null != dataRequest.getUserInfo().getUserAccount()){
//			user.setPwd(dataRequest.getUserInfo().getPassword());
			String userAccount = dataRequest.getUserInfo().getUserAccount();
			user.setUserAccount(userAccount);
			setUserIndex(user, dataRequest);
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
	 * 17
	 */
	@Override
	public String oneClickRegister(DataRequest dataRequest) {
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		UserInfo user = new UserInfo();
		if( null != dataRequest.getUserInfo() && dataRequest.getUserInfo().getOperate()==5 &&
				StringUtils.isNotBlank(dataRequest.getUserInfo().getPassword()) &&
				dataRequest.getUserInfo().getPassword().length() == 32 &&
				StringUtils.isNotBlank(dataRequest.getUserInfo().getUserAccount()) ){
			setUserIndex(user, dataRequest);
			if(!userInfoService.isExist(dataRequest.getUserInfo().getUserAccount(),user.getIndex())){
				String newPassword = SystemService.entryptPassword(dataRequest.getUserInfo().getPassword());
				user.setPwd(newPassword);
				String userAccount = dataRequest.getUserInfo().getUserAccount();
				user.setUserAccount(userAccount);
				user.setUserId(userAccount);
				user.setIsUse(1);
				user.setCkAppId(dataRequest.getUserInfo().getCkAppId());
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
	
	/*
	 * 修改密码
	 * @param dataRequest
	 * @return
	 */
	public String changePassword(DataRequest dataRequest){
		JSONObject json = new JSONObject();
		Result result = new Result(ErrorCode.PARAM_IS_NULL);
		if( dataRequest.getUserInfo() != null && dataRequest.getVerify() != null ){
			UserInfo user = new UserInfo();
			user.setUserAccount(dataRequest.getUserInfo().getUserAccount());
			setUserIndex(user, dataRequest);
			String code = "";
			if( dataRequest.getVerify().getType() == 2 && userInfoService.EmailOrmobileMatchUserAccount(user.getUserAccount(),2,user.getIndex(),dataRequest.getUserInfo().getEmail() )){
				code = redisClientTemplate.get(Constant.EMAIL_CODE_VALUE_KEY+dataRequest.getUserInfo().getEmail());
			}else if( dataRequest.getVerify().getType() == 1 && userInfoService.EmailOrmobileMatchUserAccount(user.getUserAccount(),1,user.getIndex(),dataRequest.getUserInfo().getPhoneNumber()) ){
				code = redisClientTemplate.get(Constant.MOBILE_CODE_VALUE_KEY+dataRequest.getUserInfo().getPhoneNumber());
			}
			if( null != code && code.length() > 0 && dataRequest.getVerify().getCode() != null
					&& dataRequest.getVerify().getType() != 0 && dataRequest.getVerify().getCode().equals(code)
					&& dataRequest.getUserInfo().getPassword() != null){
				String newPassword = SystemService.entryptPassword(dataRequest.getUserInfo().getPassword());
				user.setPwd(newPassword);
				userInfoService.update(user);
				result = new Result(ErrorCode.SUCCESSS);
			}
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
				setUserIndex(user, dataRequest);
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
				setUserIndex(user, dataRequest);
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
					UserInfo user = new UserInfo();
					setUserIndex(user, dataRequest);
					if( !userInfoService.isBindEmailOrMobile(dataRequest.getUserInfo().getUserAccount(),dataRequest.getVerify().getType(),user.getIndex()) ){
							if( dataRequest.getVerify().getType() ==2 && dataRequest.getUserInfo().getEmail() != null ){
								if( !userInfoService.isExistEmail(dataRequest.getUserInfo().getEmail()) ){
									user.setBindEmail(dataRequest.getUserInfo().getEmail());
									user.setUserAccount(dataRequest.getUserInfo().getUserAccount());
									userInfoService.saveUserByBindEmail(user);
									result = new Result(ErrorCode.SUCCESSS);
								}else{
									result = new Result(ErrorCode.EMAIL_MOBILE_BIND);
								}
							}else if(dataRequest.getVerify().getType() ==1 && dataRequest.getUserInfo().getPhoneNumber() != null ){
								if(  !userInfoService.isExistMobile(dataRequest.getUserInfo().getPhoneNumber()) ){
									user.setBindMobile(dataRequest.getUserInfo().getPhoneNumber());
									user.setUserAccount(dataRequest.getUserInfo().getUserAccount());
									userInfoService.saveUserByBindMobile(user);
									result = new Result(ErrorCode.SUCCESSS);
								}else{
									result = new Result(ErrorCode.EMAIL_MOBILE_BIND);
								}
							}
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
				user.setUserAccount(dataRequest.getUserInfo().getUserAccount());
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
								user.setUserAccount(dataRequest.getUserInfo().getUserAccount());
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
						}
					}else if(dataRequest.getVerify().getType() == 1){
						if(dataRequest.getUserInfo().getPhoneNumber()!= null && dataRequest.getUserInfo().getPhoneNumber().length() > 0){
							
							if( userInfoService.isExistMobile(dataRequest.getUserInfo().getPhoneNumber())){
								UserInfo user = new UserInfo();
								user.setUserAccount(dataRequest.getUserInfo().getUserAccount());
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
	
	public String getIndex(char ch){
		StringBuffer sb = new StringBuffer();
		if(ch >=48 && ch <= 57 ){
			sb.append(ch);
		}else{
			int n=ch%10;
			sb.append(n);
		}
		return sb.toString();
	}
	
	public void setUserIndex(UserInfo user,DataRequest dataRequest){
		String userAccount = dataRequest.getUserInfo().getUserAccount();
		char ch = userAccount.charAt(userAccount.length()-1);
		user.setIndex(getIndex(ch));
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
	
	/**
	 * 注册
	 * @param dataRequest
	 * @return
	 */
	public String register(DataRequest dataRequest){
		return super.register(dataRequest);
	}
	
	/**
	 * 获取校验码
	 * @param dataRequest
	 * @return
	 */
	public String getCheckSum(DataRequest dataRequest){
		return super.getCheckSum(dataRequest);
	}
	
	/**
	 * 校验登陆token --- 12
	 * @param dataRequest
	 * @return
	 */
	public String checkToken(DataRequest dataRequest){
		return super.checkToken(dataRequest);
	}
	
	/**
	 * 角色信息收集 --- 10
	 * @param dataRequest
	 * @return
	 */
	public String roleMessageCollect(DataRequest dataRequest){
		return super.roleMessageCollect(dataRequest);
	}
	
	/**
	 * 校验图片 校验码   --- 14
	 * @return
	 */
	public String checkVerfyCode(DataRequest dataRequest){
		return super.checkVerfyCode(dataRequest);
	}
	
	public String logout(DataRequest dataRequest){
		return super.logout(dataRequest);
	}
	
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
}
