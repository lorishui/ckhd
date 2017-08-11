package me.ckhd.opengame.online.dao;

import org.springframework.stereotype.Repository;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.online.entity.OnlineUser;

/**
 * 网游登陆Dao
 * 
 * @author leo
 *
 */
@Repository
public interface OnlineLoginDao extends CrudDao<OnlineUser> {
	
	//获取用户数量
	public int getCount(OnlineUser user);
	
	//根据验证信息获取用户信息
	public OnlineUser getValidateUser(OnlineUser validateUser);
}
