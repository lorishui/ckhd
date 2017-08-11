package me.ckhd.opengame.stats.dao;

import java.util.List;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.stats.entity.UserFlow;

import org.springframework.stereotype.Repository;

@Repository
public interface UserFlowDao extends CrudDao<UserFlow>{
    
    /**
     * @Description 按渠道统计用户的流向
     * @param userFlow
     * @return
     */
    List<UserFlow> getCountByChannel(UserFlow userFlow);
}
