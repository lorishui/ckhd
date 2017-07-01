package me.ckhd.opengame.user.task;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.user.model.DataRequest;
import me.ckhd.opengame.user.model.Result;
import me.ckhd.opengame.user.utils.Constant;
import me.ckhd.opengame.user.utils.ErrorCode;
import me.ckhd.opengame.user.utils.ErrorCodeTW;
import me.ckhd.opengame.user.utils.RedisClientTemplate;
import me.ckhd.opengame.user.utils.VerifyCodeUtils;
import me.ckhd.opengame.user.version.SDKVersion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "${adminPath}/user/app")
public class UserApi{
    @Autowired
    private RedisClientTemplate redisClientTemplate;
    
    static Logger log = LoggerFactory.getLogger(UserApi.class);
    private final static String className = "SDKVersion";
    public static Map<String,String> map = new HashMap<String,String>();
    
    static{
        map.put("1", "login");
        map.put("2", "");
        map.put("3", "");
        map.put("4", "getCheckSum");
        map.put("5", "register");
        map.put("6", "changePwdByOldPwd");
        map.put("7", "changePassword");
        map.put("8", "bindMobileOrEmail");
        map.put("9", "unbindMobileOrEmail");
        map.put("10", "roleMessageCollect");
        map.put("11", "logout");
        map.put("12", "checkToken");
        map.put("13", "getBindingMsg");
        map.put("14", "checkVerfyCode");
        map.put("15", "getBindingMsgByUnbin");
        map.put("16", "oneClickGetMsg");
        map.put("17", "oneClickRegister");
    }
    
    @RequestMapping(value = "{command}/{isEncry}/{isCompress}",produces={"application/json;charset=utf-8"})
    @ResponseBody
    public String command(@RequestBody String data,@PathVariable String command,@PathVariable Integer isEncry,
            @PathVariable Integer isCompress,HttpServletRequest request,HttpServletResponse response){
        JSONObject json = new JSONObject();
        Result result = new Result(ErrorCode.INTERNAL_ERROR);
        String resultMsg = "";
        //分解数据
        DataRequest dataRequest = new DataRequest();
        try {
            log.info("user api "+map.get(command)+" data="+data);
            dataRequest.encode(data,request,response);
            if(dataRequest.getUserInfo() != null){
                if( dataRequest.getUserInfo().getUserAccount() != null){
                    String patternStr="^ck\\d{8}$"; 
                    boolean is = Pattern.matches(patternStr, dataRequest.getUserInfo().getUserAccount());
                    String pattenS = "^[a-zA-Z\\d][a-zA-Z\\d\\-\\@\\.\\_]{5,49}$";
                    boolean isSelf = Pattern.matches(pattenS, dataRequest.getUserInfo().getUserAccount());
                    if(!is && !isSelf){
                        result = new Result(ErrorCode.USERACCOUNT_WRONG);
                        json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
                        return json.toJSONString();
                    }
                }
                if( dataRequest.getUserInfo().getEmail() != null ){
                    String patternStr="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"; 
                    boolean is = Pattern.matches(patternStr, dataRequest.getUserInfo().getEmail());
                    if(!is){
                        result = new Result(ErrorCode.EMAIL_WRONG);
                        json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
                        return json.toJSONString();
                    }
                }
                if( dataRequest.getUserInfo().getPhoneNumber() != null){
                    String patternStr="^1[3-5,7-9]\\d{9}$"; 
                    boolean is = Pattern.matches(patternStr, dataRequest.getUserInfo().getPhoneNumber());
                    if(!is){
                        result = new Result(ErrorCode.MOBILE_WRONG);
                        json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
                        return json.toJSONString();
                    }
                }
            }
            //装载方法
            if( dataRequest.getVersion() != null && dataRequest.getVersion().getNumber() != null){
                SDKVersion sdkVersion = SpringContextHolder.getBean(className+dataRequest.getVersion().getNumber().replace(".", ""));
                String methodName = map.get(command);
                if(methodName != null && methodName.length() >0){
                    Method method = sdkVersion.getClass().getDeclaredMethod(map.get(command), DataRequest.class);
                    resultMsg = (String) method.invoke(sdkVersion, dataRequest);
                }else{
                    result = new Result(ErrorCode.INTERNAL_ERROR);
                    json.put(result.getClass().getSimpleName().toLowerCase(),result.buildJSON() );
                    resultMsg = json.toJSONString();
                }
            }else{
                Result resultObj = new Result(ErrorCode.INTERNAL_ERROR);
                json.put(resultObj.getClass().getSimpleName().toLowerCase(),resultObj.buildJSON() );
                resultMsg = json.toJSONString();
            }
            log.info("user api "+map.get(command)+" response result="+result.toString());
        } catch (IllegalAccessException e) {
            log.error("user api cast class failure", e);
        } catch (NoSuchMethodException e) {
            log.error("user api not method ", e);
        } catch (SecurityException e) {
            log.error("user api not method ", e);
        } catch (IllegalArgumentException e) {
            log.error("user invoke method failure", e);
        } catch (InvocationTargetException e) {
            log.error("user user  method failure", e);
        }
        if(StringUtils.isBlank(resultMsg)){
            json.put(Result.class.getSimpleName().toLowerCase(), result.buildJSON());
            if( dataRequest.getVersion() != null && dataRequest.getVersion().getLanguage() != null
                    && "zh_TW".equals(dataRequest.getVersion().getLanguage())){
                JSONObject re1 = json.getJSONObject(Result.class.getName().toLowerCase());
                re1.put("b", ErrorCodeTW.getErrorCode(re1.getInteger("a")));
            }
            resultMsg = json.toJSONString();
        }
        log.info("result="+resultMsg);
        return resultMsg;
    }
    
    @RequestMapping(value = "vcImage")
    public void vcImage(@RequestBody String data,HttpServletRequest request,HttpServletResponse response){
        log.info("user api vcImage data="+data);
        String key = request.getSession().getId();
        //分解数据
        DataRequest dataRequest = new DataRequest();
        dataRequest.encode(data,request,response);
        if( null != dataRequest.getUserInfo() && null != dataRequest.getUserInfo().getUserAccount() ){
            key = dataRequest.getUserInfo().getUserAccount();
        }
        int w = 200, h = 80;  
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            VerifyCodeUtils.outputImage(w, h, out, verifyCode);
//          RedisUtils.add(Constant.VERIFY_CODE_VALUE_KEY+key, verifyCode, 6000);
            redisClientTemplate.set(Constant.VERIFY_CODE_VALUE_KEY+key, verifyCode, 90);
        } catch (IOException e) {
            log.info(" user api response error!", e);
        }finally{
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    log.info(" user api response error!", e);
                }
            }
        } 
    }
}