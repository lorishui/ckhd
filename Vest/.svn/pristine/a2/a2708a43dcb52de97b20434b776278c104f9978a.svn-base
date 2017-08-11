package me.ckhd.opengame.buyflow.dao;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.buyflow.entity.AdQueryEntity;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;
@Repository
public interface AdStatisticsDao extends CrudDao<AdQueryEntity> {

	List<AdQueryEntity> findMediaStaList(AdQueryEntity adQueryEntity);

	List<Map<String, String>> getAllOperators(AdQueryEntity adQueryEntity);

	List<AdQueryEntity> findOperatorStaList(AdQueryEntity adQueryEntity);

	List<AdQueryEntity> findUrlStaList(AdQueryEntity adQueryEntity);

	List<AdQueryEntity> findMainPage(AdQueryEntity adQueryEntity);

}
