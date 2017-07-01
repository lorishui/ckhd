package me.ckhd.opengame.adpush.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.adpush.dao.AdStatisticsDao;
import me.ckhd.opengame.adpush.entity.AdQueryEntity;
import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.sys.entity.Role;
import me.ckhd.opengame.sys.entity.User;
import me.ckhd.opengame.sys.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdStatisticsService extends CrudService<AdStatisticsDao, AdQueryEntity> {
	@Autowired
	private AdStatisticsDao adstatisticsDao;
	
	public List<AdQueryEntity> findMainPage(AdQueryEntity adQueryEntity) {
		User user = UserUtils.getUser();
		List<Role> roleList = user.getRoleList();
		adQueryEntity.setOperator(user.getId());
		for (Role role : roleList) {
			if("ad_admin".equalsIgnoreCase(role.getEnname())){
				adQueryEntity.setOperator(null);
			}
		}
		
		return adstatisticsDao.findMainPage(adQueryEntity);
	}

	public Page<AdQueryEntity> findMediaStaList(HttpServletRequest request, HttpServletResponse response, AdQueryEntity adQueryEntity) {
		Page<AdQueryEntity> page = new Page<AdQueryEntity>();
		if(StringUtils.isNotBlank(adQueryEntity.getSum())){
			page = new Page<AdQueryEntity>(request,response);
		}
		adQueryEntity.setPage(page);
		User user = UserUtils.getUser();
		List<Role> roleList = user.getRoleList();
		adQueryEntity.setOperator(user.getId());
		for (Role role : roleList) {
			if("ad_admin".equalsIgnoreCase(role.getEnname())){
				adQueryEntity.setOperator(null);
			}
		}
		page.setList(adstatisticsDao.findMediaStaList(adQueryEntity));
		return page;
	}

	public Page<AdQueryEntity> findOperatorStaList(HttpServletRequest request, HttpServletResponse response,AdQueryEntity adQueryEntity) {
		Page<AdQueryEntity> page = new Page<AdQueryEntity>();
		if(StringUtils.isNotBlank(adQueryEntity.getSum())){
			page = new Page<AdQueryEntity>(request,response);
		}
		adQueryEntity.setPage(page);
		if(adQueryEntity.getOperator() == null){
			User user = UserUtils.getUser();
			List<Role> roleList = user.getRoleList();
			adQueryEntity.setOperator(user.getId());
			for (Role role : roleList) {
				if("ad_admin".equalsIgnoreCase(role.getEnname())){
					adQueryEntity.setOperator(null);
				}
			}
		}
		page.setList(adstatisticsDao.findOperatorStaList(adQueryEntity));
		return page;
	}

	public Page<AdQueryEntity> findUrlStaList(HttpServletRequest request, HttpServletResponse response,AdQueryEntity adQueryEntity) {
		Page<AdQueryEntity> page = new Page<AdQueryEntity>();
		if(StringUtils.isNotBlank(adQueryEntity.getSum())){
			page = new Page<AdQueryEntity>(request,response);
		}
		adQueryEntity.setPage(page);
		
		if(adQueryEntity.getOperator() == null){
			User user = UserUtils.getUser();
			List<Role> roleList = user.getRoleList();
			adQueryEntity.setOperator(user.getId());
			for (Role role : roleList) {
				if("ad_admin".equalsIgnoreCase(role.getEnname())){
					adQueryEntity.setOperator(null);
				}
			}
		}
		page.setList(adstatisticsDao.findUrlStaList(adQueryEntity));
		return page;
	}

	public List<Map<String, String>> getAllOperators() {
		return adstatisticsDao.getAllOperators();
	}
	
}
