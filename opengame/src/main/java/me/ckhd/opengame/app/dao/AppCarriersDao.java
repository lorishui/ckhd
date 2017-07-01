package me.ckhd.opengame.app.dao;

import java.util.List;

import me.ckhd.opengame.app.entity.AppCarriers;
import me.ckhd.opengame.app.entity.Paycode;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;
/**
 * appcarriers  DAO接口
 * @author wesley
 * @version 2015-07-03
 */
@Repository
public interface AppCarriersDao extends  CrudDao<AppCarriers>{
	
	/**
	 * 查询全部数目
	 * @return
	 */
	public long findAllCount(AppCarriers AppCarriers);
	
	
	/**
	 * 查询paycodeList
	 * @return
	 */
	public List<Paycode> findPaycodeList(Paycode paycode);

	/**
	 * 删除paycode
	 * @param paycode
	 */
	public void deletePaycode(Paycode paycode);

	/**
	 * 新增paycode
	 * @param paycode
	 */
	public void insertPaycode(Paycode paycode);

	/**
	 * 修改paycode
	 * @param paycode
	 */
	public void updatePaycode(Paycode paycode);
	
	
	/**
	 * 根据id获取paycode
	 * @param id
	 * @return
	 */
	public Paycode getPaycode(String id);
	
	/**
	 * 验证paycode
	 * @param paycode
	 * @return
	 */
	public boolean getByPaycodeAndCarriersType(Paycode paycode);


	public String getPaycodeName(Paycode paycode);
	

}
