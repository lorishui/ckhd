package me.ckhd.opengame.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.dao.QQActivityDao;
import me.ckhd.opengame.app.entity.QQActivity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QQActivityService {
	
	@Autowired
	private QQActivityDao qqActivityDao;
	
	public int add(QQActivity qq){
		qq.preInsert();
		return qqActivityDao.insert(qq);
	}
	
	public List<Map<String,Object>> getQQDataByTimeAndCkAppId(Map<String,Object> map){
		return qqActivityDao.getQQDataByTimeAndCkAppId(map);
	}
	
	public Integer existImsi(String imsi,String ckAppId){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("imsi", imsi);
		map.put("ckAppId", ckAppId);
		return qqActivityDao.existImsi(map);
	}
	
	public Integer existQQ(String qq,String ckAppId){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("qq", qq);
		map.put("ckAppId", ckAppId);
		return qqActivityDao.existQQ(map);
	}
}
