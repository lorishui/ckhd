package me.ckhd.opengame.common.utils;

import java.security.MessageDigest;

public class MD5Util {
	/*** 
     * MD5加码 生成32位md5码 (中文不行) 启用
     */  
    public static String string2MD5(String inStr){  
        MessageDigest md5 = null;  
        try{  
            md5 = MessageDigest.getInstance("MD5");  
        }catch (Exception e){  
            System.out.println(e.toString());  
            e.printStackTrace();  
            return "";  
        }  
        char[] charArray = inStr.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
  
        for (int i = 0; i < charArray.length; i++)  
            byteArray[i] = (byte) charArray[i];  
        byte[] md5Bytes = md5.digest(byteArray);  
        StringBuffer hexValue = new StringBuffer();  
        for (int i = 0; i < md5Bytes.length; i++){  
            int val = ((int) md5Bytes[i]) & 0xff;  
            if (val < 16)  
                hexValue.append("0");  
            hexValue.append(Integer.toHexString(val));  
        }  
        return hexValue.toString();  
  
    }  
    
    public static void main(String[] args) {
		System.out.println(string2MD5("body=60魂玉&mch_create_ip=192.168.0.119&mch_id=7502000040&nonce_str=ZTrsYyQ7suO3eaYo&notify_url=http://154v61q180.imwork.net/ck/online/channel/callBack/unionpay/1029/52&out_trade_no=29160811000ca895&service=unified.trade.pay&sign_type=MD5&total_fee=1&version=1.0&key=a71f862ee4624844545ed9eb016d7d2d"));
	}
    /** 
     * 加密解密算法 执行一次加密，两次解密 
     */   
    public static String convertMD5(String inStr){  
  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);  
        return s;  
  
    }
}
