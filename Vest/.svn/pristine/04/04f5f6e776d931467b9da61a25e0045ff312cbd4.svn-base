package me.ckhd.opengame.buyflow.service;

import java.text.DecimalFormat;
import java.util.List;

import me.ckhd.opengame.buyflow.dao.BuyFlowStatDao;
import me.ckhd.opengame.buyflow.entity.BuyFlowStat;
import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.common.utils.StringUtils;

import org.springframework.stereotype.Service;

@Service
public class BuyFlowStatService extends CrudService<BuyFlowStatDao, BuyFlowStat> {

	public List<BuyFlowStat> getData(BuyFlowStat buyFlowStat) {
		return dao.getData(buyFlowStat);
	}

	public List<BuyFlowStat> getDayData(BuyFlowStat buyFlowStat) {
		return dao.getDayData(buyFlowStat);
	}

	public List<BuyFlowStat> getMediaData(BuyFlowStat buyFlowStat) {
		return dao.getMediaData(buyFlowStat);
	}

	public List<BuyFlowStat> getDayMediaData(BuyFlowStat buyFlowStat) {
		return dao.getDayMediaData(buyFlowStat);
	}

	public List<BuyFlowStat> getList(BuyFlowStat buyFlowStat) {
		int group = buyFlowStat.getGroup();
		int groupByDay = buyFlowStat.getGroupByDay();
		DecimalFormat df = new DecimalFormat("#.##");
		List<BuyFlowStat> baseList = dao.findBaseList(buyFlowStat);
		List<BuyFlowStat> retainList = dao.findRetainList(buyFlowStat);
		for (BuyFlowStat base : baseList) {
			for (BuyFlowStat retain : retainList) {
				if(groupByDay == 1){
					//group by stats_date
					if(StringUtils.isNotBlank(base.getStatsDate()) && base.getStatsDate().equals(retain.getStatsDate())){
						combineDate(base,retain);
					}
				}else{
					if(group == 0){
						//group by media_id
						if(StringUtils.isNotBlank(base.getMedia()) && base.getMedia().equals(retain.getMedia())){
							combineDate(base,retain);
						}
					}else{
						//group by buyFlowName
						if(StringUtils.isNotBlank(base.getBuyFlowName()) && base.getBuyFlowName().equals(retain.getBuyFlowName())){
							combineDate(base,retain);
						}
					}
				}
			}
			base.setActiveRate(base.getDeviceClickNum()!=0?df.format(base.getActiveNum()*1.0/base.getClickNum()):0 + "%");
			base.setRegisterRate(base.getActiveNum()!=0?df.format(base.getRegisterNum()*1.0/base.getActiveNum()):0 +"%");
		}
		return baseList;
	}

	private void combineDate(BuyFlowStat base, BuyFlowStat retain) {
		base.setRetain0(retain.getRetain0());
		base.setRetain1(retain.getRetain1());
		base.setRetain3(retain.getRetain3());
		base.setRetain7(retain.getRetain7());
		base.setRetain30(retain.getRetain30());
		base.setRetainMoney0(retain.getRetainMoney0());
		base.setRetainMoney1(retain.getRetainMoney1());
		base.setRetainMoney7(retain.getRetainMoney7());
		base.setRetainMoney30(retain.getRetainMoney30());
		base.setRetainMoney60(retain.getRetainMoney60());
		base.setActiveDeviceNum(retain.getActiveDeviceNum());
	}

}
