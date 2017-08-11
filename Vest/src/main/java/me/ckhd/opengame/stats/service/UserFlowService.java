package me.ckhd.opengame.stats.service;

import java.util.List;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.stats.dao.UserFlowDao;
import me.ckhd.opengame.stats.entity.UserFlow;

import org.springframework.stereotype.Service;

@Service
public class UserFlowService extends CrudService<UserFlowDao, UserFlow>{
    
    /**
     * @Description 按渠道统计用户的流向
     * @param userFlow
     * @return
     */
    public List<UserFlow> getCountByChannel(UserFlow userFlow){
        return dao.getCountByChannel(userFlow);
    }
}
