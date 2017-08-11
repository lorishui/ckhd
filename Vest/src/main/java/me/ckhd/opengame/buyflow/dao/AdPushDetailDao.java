package me.ckhd.opengame.buyflow.dao;

import java.util.List;

import me.ckhd.opengame.buyflow.entity.AdPushDetail;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdPushDetailDao extends CrudDao<AdPushDetail> {

	List<AdPushDetail> findListByAdPushId(@Param("adPushId")String adPushId,@Param("userId")String userId);

	void deleteByAdPushId(String adPushId);

}
