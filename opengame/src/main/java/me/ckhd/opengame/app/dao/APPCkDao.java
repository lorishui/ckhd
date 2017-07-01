package me.ckhd.opengame.app.dao;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;
/**
 * ckapp DAO接口
 * @author wesley
 * @version 2015-06-29
 */
@Repository
public interface APPCkDao extends  CrudDao<APPCk>{
	
	/**
	 * 查询全部app数目
	 * @return
	 */
	public long findAllCount(APPCk aPPCk);
	
	/**
	 * 根据名称获取app信息
	 * @param aPPCk
	 * @return
	 */
	public APPCk getByName(APPCk aPPCk);
	
	/**
	 * 根据appid获取app信息
	 * @param aPPCk
	 * @return
	 */
	public APPCk getByAppId(APPCk aPPCk);
	/**
	 * 根据ckappid获取name信息
	 * @param ckappId
	 * @return
	 */
	public String getCkAppName(String ckappId);


}