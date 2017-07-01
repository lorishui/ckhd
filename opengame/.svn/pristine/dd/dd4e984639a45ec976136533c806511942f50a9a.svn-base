package me.ckhd.opengame.adpush.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import me.ckhd.opengame.adpush.entity.AdPushDetail;
import me.ckhd.opengame.common.persistence.CrudDao;

@Repository
public interface AdPushDetailDao extends CrudDao<AdPushDetail> {

	List<AdPushDetail> findListByAdPushId(@Param("adPushId")String adPushId,@Param("userId")String userId);

	void deleteByAdPushId(String adPushId);

}
