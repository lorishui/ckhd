package me.ckhd.opengame.game.service;

import java.util.List;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.game.dao.GoodsDao;
import me.ckhd.opengame.game.entity.Goods;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodsService extends CrudService<GoodsDao, Goods>{
	public List<Goods> findList(Goods goods){
		return dao.findList(goods);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void save(Goods entity) {
		if (!StringUtils.isNotBlank(entity.getId())){
			dao.insert(entity);
		}else{
			dao.update(entity);
		}
	}
	
	public int use(Goods goods){
		return dao.use(goods);
	}
}
