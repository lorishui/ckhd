package me.ckhd.opengame.online.dao;

import java.util.List;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.online.entity.TencentCallbackData;

import org.springframework.stereotype.Repository;

/**
 * tencent回掉数据Dao
 * @author leo
 *
 */
@Repository
public interface TencentCallbackDataDao extends CrudDao<TencentCallbackData> {
	public List<TencentCallbackData> getListData();
	
	public int updateByOrderNull(TencentCallbackData ten);
}
