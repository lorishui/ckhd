package me.ckhd.opengame.game.service;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.game.dao.GiftDao;
import me.ckhd.opengame.game.entity.Gift;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiftService extends CrudService<GiftDao, Gift>{
	@Override
	@Transactional(readOnly = false)
	public void save(Gift gift) {
		String goodsId = gift.getGoodsId();
		String num = gift.getNum();
		Gift jk = new Gift();
		jk.setParentNode(gift.getNewId());
		jk.setCkAppId(gift.getCkAppId());
		dao.delete(jk);
		if(StringUtils.isNotBlank(goodsId)&&StringUtils.isNotBlank(num)){
			String[] idArr = goodsId.split(",");
			String[] numArr = num.split(",");
			for(int i=0;i<idArr.length;i++){
				Gift one = new Gift();
				one.setCkAppId(gift.getCkAppId());
				one.setName(gift.getName());
				one.setGoodsId(idArr[i]);
				if(numArr.length>i){
					one.setGoodsNumber(Integer.parseInt(numArr[i]));
				}
				one.setParentNode(gift.getNewId());
				one.preInsert();
				one.setNewId(one.getId());
				dao.insert(one);
			}
		}
		if (!StringUtils.isNotBlank(gift.getId())){
			gift.setParentNode("0");
			gift.setGoodsId("");
			dao.insert(gift);
		}else{
			gift.setGoodsId("");
			dao.update(gift);
		}
	}
	
	/**
	 * 删除数据
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void delete(Gift gift) {
		dao.delete(gift);
		Gift one = new Gift();
		one.setParentNode(gift.getId());
		dao.delete(one);
	}
	
	public List<Map<String,Object>> getSub(Gift gift){
		return dao.getSub(gift);
	}
	
	public List<Gift> findList(Gift gift){
		return dao.findList(gift);
	}
	
	public String getDesc(String parentNode,String ckAppId){
		Gift gift = new Gift();
		gift.setParentNode(parentNode);
		gift.setCkAppId(ckAppId);
		return dao.getDesc(gift);
	}
}
