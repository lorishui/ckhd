package me.ckhd.opengame.adpush.service;

import java.util.List;

import me.ckhd.opengame.adpush.dao.AdPushCostDao;
import me.ckhd.opengame.adpush.dao.AdPushDetailDao;
import me.ckhd.opengame.adpush.entity.AdPushDetail;
import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.sys.entity.Role;
import me.ckhd.opengame.sys.entity.User;
import me.ckhd.opengame.sys.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdPushDetailService extends CrudService<AdPushDetailDao, AdPushDetail> {
	@Autowired
	private AdPushDetailDao adPushDetailDao;
	@Autowired
	private AdPushCostDao adPushCostDao;

	public List<AdPushDetail> findListByAdPushId(String adPushId) {
		User user = UserUtils.getUser();
		List<Role> roleList = user.getRoleList();
		for (Role role : roleList) {
			if("ad_admin".equalsIgnoreCase(role.getEnname())){
				user = new User();
			}
		}
		return dao.findListByAdPushId(adPushId,user.getId());
	}
	@Override
	public void delete(AdPushDetail entity) {
		super.delete(entity);
		adPushCostDao.deleteByAdPushDetailId(entity.getId());
	}
}
