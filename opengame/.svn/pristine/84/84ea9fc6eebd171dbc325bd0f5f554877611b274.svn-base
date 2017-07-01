package me.ckhd.opengame.app.utils;

import java.io.IOException;
import java.util.List;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;
import javapns.data.Device;
import javapns.data.PayLoad;
import me.ckhd.opengame.app.entity.AppleUser;
import me.ckhd.opengame.app.service.AppleUserService;
import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 苹果推送功能
 * @author lp
 */
public class AppleMsgPushUtils {
	static Logger log = LoggerFactory.getLogger(AppleMsgPushUtils.class);
	
	public static String push(String ckAppId,List<AppleUser> list,String content,String version,
			AppleUserService appleUserService){
		PushNotificationManager pushManager = null;
		try {
         
            //测试推送服务器地址：gateway.sandbox.push.apple.com /2195 
			//产品推送服务器地址：gateway.push.apple.com / 2195 
			String value = DictUtils.getDictValue(ckAppId, "ios_push", "");
			String host = null;
			String certificatePath = null;
			if(value.equals(version)){
				host = "gateway.sandbox.push.apple.com";  //测试用的苹果推送服务器
				certificatePath = Global.getConfig("IOS_PUSH_CERT_ADDRESS_TEST"); //刚才在mac系统下导出的证书
			}else{
				host = "gateway.push.apple.com";
				certificatePath = Global.getConfig("IOS_PUSH_CERT_ADDRESS"); //刚才在mac系统下导出的证书
			}
			int port = 2195;
			certificatePath = certificatePath+ckAppId+".p12";
			String certificatePassword= "123";
			
			pushManager = PushNotificationManager.getInstance();
			pushManager.initializeConnection(host, port, certificatePath,certificatePassword, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
			//推送start
			addPush(list,content, pushManager,appleUserService);
			//end
			/*pushManager.stopConnection();
			pushManager.removeDevice("iphone");*/
		}catch (Exception e) {
			log.error("apple推送出错!", e);
			return "FAIL";
        }finally{
        	if(pushManager != null ){
        		try {
					pushManager.stopConnection();
				} catch (IOException e) {
					log.error("PushNotificationManager stop failure!",e);
				} 
        	}
        }
        return "SUCCESS";
	}

	private static void addPush(List<AppleUser> list,String content,
			PushNotificationManager pushManager,AppleUserService appleUserService) throws Exception{
		try{
			for(AppleUser apple:list){
				//被推送的iphone应用程序标示符      
				PayLoad payLoad = new PayLoad();
				payLoad.addAlert(content);
				payLoad.addBadge(apple.getBadge()+1);
				payLoad.addSound("default");  
				pushManager.addDevice("iphone", apple.getDeviceToken());
				//Send Push
				Device client = pushManager.getDevice("iphone");
				pushManager.sendNotification(client, payLoad); //推送消息
				apple.setBadge(apple.getBadge()+1);
				pushManager.removeDevice("iphone");
			}
			for(AppleUser apple:list){
				appleUserService.update(apple);
			}
		}catch(Exception e){
			log.error("ios 推送错误！！！", e);
			throw e;
		}
	}
	
}
