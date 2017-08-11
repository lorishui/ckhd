package me.ckhd.opengame.stats.service;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.common.utils.SnowflakeIdWorker;
import me.ckhd.opengame.stats.dao.EventUserAccountDao;
import me.ckhd.opengame.stats.entity.EventUserAccount;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName EventUserAccounDao
 * @Description 用户交互事件数据访问类型
 * @author liupei
 * @Date 2017年7月7日 上午10:07:59
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class EventUserAccountService extends CrudService<EventUserAccountDao, EventUserAccount>{
    
    /**
     * 保存数据（插入或更新）
     * @param entity
     */
    @Transactional(readOnly = false)
    public void save(EventUserAccount EventUserAccount) {
        EventUserAccount.preInsert();
        EventUserAccount.setId(SnowflakeIdWorker.getBigIntId() + "");
        dao.insert(EventUserAccount);
    }
    
}
