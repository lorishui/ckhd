package me.ckhd.opengame.game.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.game.dao.GiftCodeDao;
import me.ckhd.opengame.game.dao.GiftDescDao;
import me.ckhd.opengame.game.entity.GiftCode;
import me.ckhd.opengame.game.entity.GiftDesc;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiftCodeService extends CrudService<GiftCodeDao, GiftCode>{
	
	@Autowired
	private GiftDescDao giftDescDao;
	
	@Transactional(readOnly=false)
	public void batchInsert(GiftDesc giftDesc){
		synchronized (giftDesc.getId()) {
			giftDesc = giftDescDao.get(giftDesc.getId());
			if(giftDesc.getIsGenerate() !=1){
				List<GiftCode> gift = new ArrayList<GiftCode>();
				for(int i=0;i<giftDesc.getNumber();i++){
					GiftCode one = new GiftCode();
					one.setCkAppId(giftDesc.getCkAppId());
					one.setCkChannelId(giftDesc.getCkChannelId());
					one.setCode(getRandStr());
					one.setGiftDescId(giftDesc.getId());
					one.setIsUse(0);
					one.setCreateDate(new Date());
					gift.add(one);
				}
				dao.batchInsert(gift);
				giftDescDao.updateGenerate(giftDesc);
			}
		}
	}
	
	private String getRandStr(){
		String rand = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<16;i++){
			sb.append(rand.charAt(RandomUtils.nextInt(rand.length()-1)));
		}
		return sb.toString();
	}
}
