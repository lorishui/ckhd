package me.ckhd.opengame.reyun.task;

import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.reyun.task.base.IReyunSDK;
import me.ckhd.opengame.reyun.utils.PayTypeEnum;
import me.ckhd.opengame.util.HttpClientUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * 热云事件处理实现类
 * 
 * @ClassName RenyunSDKImpl
 * @Description 热云事件处理实现类
 * @author liupei
 * @Date 2017年6月7日 上午10:58:57
 * @version 1.0.0
 */
@Component("RenyunSDKImpl")
public class RenyunSDKImpl implements IReyunSDK {
    
    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public Integer payment(JSONObject json) {
        if(json!=null){
            log.info("renyun payment param:"+json.toJSONString());
        }else{
            log.info("renyun payment param is null");
        }
        String payUrl = url + "/receive/track/payment";
        JSONObject data = dealWithData(json);
        log.info("renyun payment request data:"+data.toJSONString());
        String respData = HttpClientUtils.post(payUrl, data.toJSONString(), 10000, 10000,
                "Content-Type=application/json");
        log.info("renyun payment response data:"+respData);
        if (StringUtils.isNotBlank(respData)) {
            JSONObject respJSON = JSONObject.parseObject(respData);
            if (respJSON.containsKey("status") && "0".equals(respJSON.get("status"))) {
                return 1;
            }else{
                return 3;
            }
        }else{
            return 2;
        }
    }

    /**
     * @Description 处理数据
     * @return
     */
    JSONObject dealWithData(JSONObject json) {
        JSONObject data = new JSONObject();
        data.put("appid", json.getString("renyunKey"));
        if (json.containsKey("userId")) {
            data.put("who", CoderUtils.md5(json.getString("userId"), "utf-8"));
        }
        JSONObject context = new JSONObject();
        if (json.containsKey("currencyAmount")) {
            context.put("currencyAmount", json.getString("currencyAmount"));
        }
        context.put("currencyType", "CNY");
        if (json.containsKey("deviceid")) {
            context.put("deviceid", json.getString("deviceid"));
        }
        if (json.containsKey("paymentType")) {
            context.put("paymentType", PayTypeEnum.getShortCodeName(json.getString("paymentType")));
        }
        if (json.containsKey("orderId")) {
            context.put("transactionId", json.getString("orderId"));
        }
        data.put("context", context);
        return data;
    }
}
