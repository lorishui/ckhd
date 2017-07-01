package me.ckhd.opengame.adpush.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import me.ckhd.opengame.adpush.entity.AdQueryEntity;
import me.ckhd.opengame.common.persistence.CrudDao;
@Repository
public interface AdStatisticsDao extends CrudDao<AdQueryEntity> {

	List<AdQueryEntity> findMediaStaList(AdQueryEntity adQueryEntity);

	List<Map<String, String>> getAllOperators();

	List<AdQueryEntity> findOperatorStaList(AdQueryEntity adQueryEntity);

	List<AdQueryEntity> findUrlStaList(AdQueryEntity adQueryEntity);

	List<AdQueryEntity> findMainPage(AdQueryEntity adQueryEntity);

}
