package me.ckhd.opengame.reyun.task.base;

import com.alibaba.fastjson.JSONObject;

/**
 * 热云事件处理接口
 * @ClassName IReyunSDK
 * @Description 热云接口处理类
 * @author liupei
 * @Date 2017年6月7日 上午10:50:09
 * @version 1.0.0
 */
public interface IReyunSDK {
    
    /**
     * 热云的报备地址
     */
    String url = "http://log.reyun.com";
    
    /**
     * @Description 支付成功向热云报备接口
     * @param json
     * @return 1:成功,2请求失败(重新请求3次),3错误提示
     */
    Integer payment(JSONObject json);
    
}
