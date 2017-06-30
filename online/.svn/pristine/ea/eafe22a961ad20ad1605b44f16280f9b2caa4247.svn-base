package me.ckhd.opengame.app.dao;

import me.ckhd.opengame.app.entity.Province;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

/**
 * 省份DAO接口
 * @author wesley
 * @version 2015-06-25
 */
@Repository
public interface ProvinceDao  extends CrudDao<Province> {
	/**
	 * 根据省份代码获取省份信息
	 * @param province
	 * @return
	 */
	Province  getByCode(Province province);
	/**
	 * 根据名称获取省份信息
	 * @param province
	 * @return
	 */
	Province  getByName(Province province);
	
	/**
	 * 查询全部省份数目
	 * @return
	 */
	public long findAllCount(Province province);
	
	

}
