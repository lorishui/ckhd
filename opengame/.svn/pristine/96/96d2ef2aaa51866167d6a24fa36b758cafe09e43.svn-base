package me.ckhd.opengame.adpush.dao;

import java.util.Date;
import java.util.List;

import me.ckhd.opengame.adpush.entity.AdPushCost;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdPushCostDao extends CrudDao<AdPushCost> {

	List<AdPushCost> findListByAdPushDetailId(String adPushDetailId);


	AdPushCost findByDateAndAdPushDetailId(@Param("date")Date date,@Param("adPushDetailId")String adPushDetailId);


	void deleteByAdPushDetailId(String adPushDetailId);

}
