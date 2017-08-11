package me.ckhd.opengame.online.handle.common.lenovo;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class CpTransSyncSignValid{
	public static final String CHARSET = "utf-8";
   
	public static Boolean validSign(String transdata, String sign, String privateKey){
		String tmp = Tools.sign(transdata, privateKey, "utf-8");
		if (sign.equals(tmp)) {
			return Boolean.valueOf(true);
		}
		try{
			if (tmp.equals(URLDecoder.decode(sign, "utf-8"))) {
				return Boolean.valueOf(true);
			}
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return Boolean.valueOf(false);
	}
}
