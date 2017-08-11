package me.ckhd.opengame.user.service;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.user.dao.UserInfoDao;
import me.ckhd.opengame.user.entity.UserInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.StringUtils;

@Service("UserInfoService")
@Transactional(readOnly = true)
public class UserInfoService extends CrudService<UserInfoDao, UserInfo>{

	public boolean isExist(String userAccount){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("n", userAccount.substring(userAccount.length()-1, userAccount.length()));
		map.put("userAccount", userAccount);
		int n = this.dao.isExist(map);
		return n==0?false:true;
	}
	
	public boolean isExist(String userAccount,String index){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("n", index);
		map.put("userAccount", userAccount);
		int n = this.dao.isExist(map);
		return n==0?false:true;
	}
	
	@Transactional(readOnly = false)
	public int update(UserInfo userInfo){
		return this.dao.update(userInfo);
	}
	
	public boolean isBindEmailOrMobile(String userAccount,int type){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("n", userAccount.substring(userAccount.length()-1, userAccount.length()));
		map.put("userAccount", userAccount);
		UserInfo userInfo = this.dao.isBindEmailOrMobile(map);
		if(type == 1){
			if(userInfo.getBindMobile() != null && userInfo.getBindMobile().length() > 0){
				return true;
			}else{
				return false;
			}
		}else if(type == 2){
			if(userInfo.getBindEmail() != null && userInfo.getBindEmail().length() > 0){
				return true;
			}else{
				return false;
			}
		}
		return true;
	}
	
	public boolean isBindEmailOrMobile(String userAccount,int type,String index){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("n", index);
		map.put("userAccount", userAccount);
		UserInfo userInfo = this.dao.isBindEmailOrMobile(map);
		if(type == 1){
			if(userInfo.getBindMobile() != null && userInfo.getBindMobile().length() > 0){
				return true;
			}else{
				return false;
			}
		}else if(type == 2){
			if(userInfo.getBindEmail() != null && userInfo.getBindEmail().length() > 0){
				return true;
			}else{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 账号和邮箱与手机是否 相配
	 * @param userAccount
	 * @param type
	 * @param index
	 * @param emailOrMobile
	 * @return
	 */
	public boolean EmailOrmobileMatchUserAccount(String userAccount,int type,String index,String emailOrMobile){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("n", index);
		map.put("userAccount", userAccount);
		UserInfo userInfo = this.dao.isBindEmailOrMobile(map);
		if(type == 1){
			if(userInfo.getBindMobile() != null && userInfo.getBindMobile().equals(emailOrMobile) ){
				return true;
			}else{
				return false;
			}
		}else if(type == 2){
			if(userInfo.getBindEmail() != null && userInfo.getBindEmail().equals(emailOrMobile) ){
				return true;
			}else{
				return false;
			}
		}
		return true;
	}
	
	@Transactional(readOnly = false)
	public int deleteEmail(String email){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("index", email.substring(0, 1));
		map.put("bindEmail", email);
		return this.dao.deleteEmail(map);
	}
	
	/**
	 * 解除绑定
	 * @param email
	 * @param userAccount
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean unBind(String userAccount,int type,String index){
		UserInfo userInfo = new UserInfo();
		int n = 0;
		userInfo.setIndex(index);
		userInfo.setUserAccount(userAccount);
		UserInfo u = get(userInfo);
		if( u != null ){
			if( 0==type && !StringUtils.isNullOrEmpty(u.getBindEmail())){
				userInfo.setBindEmail("");
				n = deleteEmail(u.getBindEmail());
			}else if( !StringUtils.isNullOrEmpty(u.getBindMobile()) ){
				userInfo.setBindMobile("");
				n = deleteMobile(u.getBindMobile());
			}
			int m = 0;
			if( n>0 ){
				m = update(userInfo);
			}
			if( m>0 ){
				return true;
			}
		}
		return false;
	}
	
	@Transactional(readOnly = false)
	public int deleteMobile(String mobile){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("index", mobile.substring(mobile.length()-1));
		map.put("bindMobile", mobile);
		return this.dao.deleteMobile(map);
	}
	
	@Transactional(readOnly = false)
	public int insertEmail(String userAccount,String email){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("index", email.substring(0, 1));
		map.put("bindEmail", email);
		map.put("userAccount", userAccount);
		return this.dao.insertEmail(map);
	}
	
	@Transactional(readOnly = false)
	public int insertMobile(String userAccount,String mobile){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("index", mobile.substring(mobile.length()-1));
		map.put("bindMobile", mobile);
		map.put("userAccount", userAccount);
		return this.dao.insertMobile(map);
	}
	
	public boolean isExistEmail(String email){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("index", email.substring(0, 1));
		map.put("bindEmail", email);
		int n = this.dao.isExistEmail(map);
		return n==0?false:true;
	}
	
	public boolean isExistMobile(String mobile){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("index", mobile.substring(mobile.length()-1));
		map.put("bindMobile", mobile);
		int n = this.dao.isExistMobile(map);
		return n==0?false:true;
	}
	
	@Transactional(readOnly = false,rollbackFor={RuntimeException.class, Exception.class})
	public int saveUserByBindEmail(UserInfo userInfo){
		int u = update(userInfo);
		int i = insertEmail(userInfo.getUserAccount(), userInfo.getBindEmail());
		return u+i;
	}
	
	@Transactional(readOnly = false,rollbackFor={RuntimeException.class, Exception.class})
	public int saveUserByUnBindEmail(UserInfo userInfo){
		int d = deleteEmail(userInfo.getBindEmail());
		userInfo.setBindEmail("");
		int u = update(userInfo);
		return u+d;
	}
	
	@Transactional(readOnly = false,rollbackFor={RuntimeException.class, Exception.class})
	public int saveUserByBindMobile(UserInfo userInfo){
		int u = update(userInfo);
		int i = insertMobile(userInfo.getUserAccount(), userInfo.getBindMobile());
		return u+i;
	}
	
	@Transactional(readOnly = false,rollbackFor={RuntimeException.class, Exception.class})
	public int saveUserByUnBindMobile(UserInfo userInfo){
		int d = deleteMobile(userInfo.getBindMobile());
		userInfo.setBindMobile("");
		int u = update(userInfo);
		return u+d;
	}
	
	public UserInfo getOne(UserInfo userInfo){
		return dao.getOne(userInfo);
	}
	
	/**
     * @Description 根据emial获取对应的账号
     * @param userInfo
     * @return
     */
    public UserInfo getUserByEmail(UserInfo userInfo){
        return this.dao.getUserByEmail(userInfo);
    }
    /**
     * @Description 根据手机号码获取对应的账号
     * @param userInfo
     * @return
     */
    public UserInfo getUserByMobile(UserInfo userInfo){
        return this.dao.getUserByMobile(userInfo);
    }
}
