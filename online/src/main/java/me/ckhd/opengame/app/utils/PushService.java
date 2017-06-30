package me.ckhd.opengame.app.utils;

import javapns.back.PushNotificationManager;
import javapns.back.SSLConnectionHelper;
import javapns.data.Device;
import javapns.data.PayLoad;

public class PushService {
	public static void main(String[] args) {
        
		try {
			String deviceToken = "2aa0a98c7b744306932fe95f53d80d0871b083b8088da0cf2e1a7c83d8e32e0e";
//			String deviceT2 = "270491ba 72351afb 0b610e4a c83fe68e 6ade6a8e 6cb0cc77 495cef21 60beb827";
			//被推送的iphone应用程序标示符      
			PayLoad payLoad = new PayLoad();
			payLoad.addAlert("测试我的push消息");
			payLoad.addBadge(1);
			payLoad.addSound("default");
          
			PushNotificationManager pushManager = PushNotificationManager.getInstance();
			pushManager.addDevice("iphone", deviceToken);
         
            //测试推送服务器地址：gateway.sandbox.push.apple.com /2195 
			//产品推送服务器地址：gateway.push.apple.com / 2195 
			String host="gateway.sandbox.push.apple.com";  //测试用的苹果推送服务器
			int port = 2195;
			String certificatePath = "D:/sert/ckpush.p12"; //刚才在mac系统下导出的证书
         
			String certificatePassword= "123";
        
			pushManager.initializeConnection(host, port, certificatePath,certificatePassword, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
                   
			//Send Push
			Device client = pushManager.getDevice("iphone");
			System.out.println("start="+System.currentTimeMillis());
			for(int i=0;i<10;i++){
				
				pushManager.sendNotification(client, payLoad); //推送消息
			}
			System.out.println("end="+System.currentTimeMillis());
			pushManager.stopConnection();
			pushManager.removeDevice("iphone");
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("push faild!");
			return;
        }
        System.out.println("push succeed!");
	}
	
	/*public static void main(String[] args) {
		long start = 1468833487753l;
		long end = 1468833490671l;
		System.out.println(end-start);
	}*/
}
