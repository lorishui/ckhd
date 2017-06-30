package me.ckhd.opengame.online.handle.common.lenovo;

public class Tools {

	public static String sign(String content, String privateKey, String input_charset){
		return Rsa.sign(content, privateKey, input_charset);
	}
	  
}
