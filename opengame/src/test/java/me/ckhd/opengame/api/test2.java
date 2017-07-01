package me.ckhd.opengame.api;

import java.net.InetAddress;

import org.apache.commons.lang.math.RandomUtils;

public class test2 {
	 private static String remoteInetAddr = "https://www.baidu.com/";//需要连接的IP地址
	 
       /**
       * 传入需要连接的IP，返回是否连接成功
       * @param remoteInetAddr
       * @return
       */
      public static boolean isReachable() {
          boolean reachable = false; 
          try {   
              InetAddress address = InetAddress.getByName(remoteInetAddr); 
              reachable = address.isReachable(5000);  
              } catch (Exception e) {  
              e.printStackTrace();  
              }  
          return reachable;
      }
      
      public static void main(String[] args) {
//		System.out.println(isReachable());
    	  for(int i=0;i<1000;i++){
    		 System.out.println(RandomUtils.nextInt(1));
    	  }
	}
}
