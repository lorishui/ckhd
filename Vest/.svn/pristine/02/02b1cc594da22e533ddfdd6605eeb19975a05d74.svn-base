package me.ckhd.opengame.app.service;

import java.util.Date;

import me.ckhd.opengame.app.dao.SendMobileMessageDao;
import me.ckhd.opengame.app.entity.SendMobileMessage;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.stereotype.Service;

/**
 * @ClassName SendMobileMessageService
 * @Description 发送短信结果收集的相关逻辑
 * @author ASUS
 * @Date 2017年6月12日 上午11:48:43
 * @version 1.0.0
 */
@Service("sendMobileMessageService")
public class SendMobileMessageService extends CrudService<SendMobileMessageDao, SendMobileMessage>{
    
    /**
     * @Description 插入一条短信发送成功的记录
     * @param phoneNumbei 手机号
     * @param mark 调用的哪一个公司接口的标识
     * @return
     */
    public int add(String phoneNumbei,String mark){
        SendMobileMessage send = new SendMobileMessage();
        send.setMobile(phoneNumbei);
        send.setMark(mark);
        send.setCreateDate(new Date());
        return this.dao.insert(send);
    }
}
