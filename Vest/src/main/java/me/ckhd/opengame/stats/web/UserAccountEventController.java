package me.ckhd.opengame.stats.web;

import java.util.Date;

import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.stats.entity.EventUserAccount;
import me.ckhd.opengame.stats.service.EventUserAccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@RequestMapping(value = "${adminPath}/user/event")
@Controller
public class UserAccountEventController {
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private EventUserAccountService eventUserAccountService;

    @RequestMapping("add")
    @ResponseBody
    public String save(@RequestBody String data) {
        if (StringUtils.isNotBlank(data)) {
            try {
                JSONObject json = JSONObject.parseObject(data);
                String type = json.containsKey("type") ? json.getString("type") : "1";
                long time = json.containsKey("time") ? json.getLong("time") : System.currentTimeMillis();
                String ip = json.containsKey("ip") ? json.getString("ip") : "";
                JSONObject respData = json.containsKey("respData") ? json.getJSONObject("respData") : null;
                JSONObject requestData = json.containsKey("requestData") ? json.getJSONObject("requestData") : null;
                String executeTime = json.containsKey("executeTime") ? json.getString("executeTime") : "0";
                Date createTime = new Date(time);
                EventUserAccount user = new EventUserAccount();
                user.setCreateDate(createTime);
                user.setType(type);
                user.setClientIp(ip);
                user.setRequestData(requestData.toJSONString());
                user.setResponseData(respData.toJSONString());
                user.setExecuteTime(executeTime);
                if (respData.getJSONObject("user").containsKey("a")) { 
                	user.setUserAccount(respData.getJSONObject("user").getString("a"));
                } else {
                	user.setUserAccount(requestData.getJSONObject("user").getString("a"));
                }
                user.setCkappId(requestData.getJSONObject("user").getString("h"));
                setApplication(user, requestData);
                eventUserAccountService.save(user);
            } catch (Throwable e) {
                logger.error("user account save failure!", e);
            }
        }
        return "SUCCESS";
    }

    private void setApplication(EventUserAccount user, JSONObject requestData) {
        if (requestData.containsKey("appliaction")) {
            JSONObject app = requestData.getJSONObject("appliaction");
            user.setCkappId(app.containsKey("a") ? requestData.getString("a") : user.getCkappId());
            user.setChildAppId(app.containsKey("b") ? requestData.getString("b") : null);
            user.setChannelId(app.containsKey("c") ? requestData.getString("c") : null);
            user.setChildChannelId(app.containsKey("d") ? requestData.getString("d") : null);
            user.setPlatform(app.containsKey("e") ? requestData.getString("e") : null);
            user.setDeviceId(app.containsKey("f") ? requestData.getString("f") : null);
            user.setPhoneModel(app.containsKey("g") ? requestData.getString("g") : null);
            user.setPackageName(app.containsKey("h") ? requestData.getString("h") : null);
            user.setIdfv(app.containsKey("i") ? requestData.getString("i") : null);
            user.setOsVersion(app.containsKey("j") ? requestData.getString("j") : null);
        }
    }
    
}
