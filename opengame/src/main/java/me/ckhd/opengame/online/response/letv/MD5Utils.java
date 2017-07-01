package me.ckhd.opengame.online.response.letv;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


public class MD5Utils {
	
	
	public static final Map<String, String> keyMap = new HashMap<String, String>();
	
	static {
		keyMap.put("1", "ce0806981627d00d4b96beb051a2b629");
		keyMap.put("2", "0e641f0a1fc427a861c60e383d85ca5f");
		keyMap.put("3", "e24fbcba30ae09203587a54d0fecd6b7");
		keyMap.put("4", "8152d310121867bb4acaa7a518edacba");
		keyMap.put("5", "073b505b3779e36b15a50a53b45eb9ed");
		keyMap.put("6", "5a1802fe5bcb78e41c3f56508595bb7f");
		keyMap.put("7", "15230303fbac7623781be9d88ed0ba5e");
		keyMap.put("8", "1ac2470b3ba5f8a2fbe4717de9a44643");
		keyMap.put("9", "de2e6abd93d2711b1c94bae6d7c7247d");
		keyMap.put("10", "f5ddcc513883d9c506a933f7cc2debf1");
		keyMap.put("12", "f2992ff7bb875889f42b9c253b0909b3");
		keyMap.put("20", "b3863b97bf9c048948dee589942366c6");
		keyMap.put("21", "282b658be928568b642fab22c7c4470d");
	}
	
	public static String getSpVerifySign(String corderid,String linkid,String companyid){
//		String signedSecKey = MD5Encode(companyid + SECKEY_SUFFIX, "UTF-8");
		String signedSecKey = keyMap.get(companyid);
		String validateStr = "corderid=" + corderid + "&linkid=" + linkid + "&companyid=" + companyid + "&" + signedSecKey;
		String MD5Value = MD5Encode(validateStr,"UTF-8");
		return MD5Value;
	}
	
	public static String getUnbindSign(String companyid,String bindid,String userid){
//		String signedSecKey = MD5Encode(companyid + SECKEY_SUFFIX, "UTF-8");
		String signedSecKey = keyMap.get(companyid);
		String validateStr = "bindid=" + bindid + "&userid=" + userid + "&companyid=" + companyid + "&" + signedSecKey;
		String MD5Value = MD5Encode(validateStr,"UTF-8");
		return MD5Value;
	}
	
	public static String getRefundSign(String corderid,String companyid){
//		String signedSecKey = MD5Encode(companyid + SECKEY_SUFFIX, "UTF-8");
		String signedSecKey = keyMap.get(companyid);
		String validateStr = "corderid=" + corderid +  "&" + signedSecKey + "&companyid="+companyid;
		String MD5Value = MD5Encode(validateStr,"UTF-8");
		
		return MD5Value;
	}
	
	public static String getPaySign(String corderid,String userid,String price, String companyid,String deptid,String pid){
//		String signedSecKey = MD5Encode(companyid + SECKEY_SUFFIX, "UTF-8");
		String signedSecKey = keyMap.get(companyid);
		
		String validateStr = "corderid=" + corderid + "&userid=" + userid + "&price=" + price + "&companyid=" + companyid 
				+ "&" + signedSecKey + "&deptid=" + deptid + "&pid=" + pid;
		String MD5Value = MD5Encode(validateStr,"UTF-8");
		
		return MD5Value;
	}
	
	public static String getPaySign(String corderid,String companyid){
//		String signedSecKey = MD5Encode(companyid + SECKEY_SUFFIX, "UTF-8");
		String signedSecKey = keyMap.get(companyid);
		
		String validateStr = "corderid=" + corderid + "&companyid=" + companyid + "&" + signedSecKey;
		String MD5Value = MD5Encode(validateStr,"UTF-8");
		
		return MD5Value;
	}
	
	public static String getPaySign(String corderid,String companyid,String smscode){
//		String signedSecKey = MD5Encode(companyid + SECKEY_SUFFIX, "UTF-8");
		String signedSecKey = keyMap.get(companyid);
		
		String validateStr = "corderid=" + corderid + "&companyid=" + companyid + "&smscode=" + smscode + "&" + signedSecKey;
		String MD5Value = MD5Encode(validateStr,"UTF-8");
		
		return MD5Value;
	}
	
	public static String getRebindSign(String corderid, String companyid, String mobileno, String serviceid){
//		String signedSecKey = MD5Encode(companyid + SECKEY_SUFFIX, "UTF-8");
		String signedSecKey = keyMap.get(companyid);
		
		String validateStr = "corderid=" + corderid + "&companyid=" + companyid + "&mobileno=" + mobileno + "&serviceid=" + serviceid 
				+ "&" + signedSecKey;
		String MD5Value = MD5Encode(validateStr,"UTF-8");
		
		return MD5Value;
	}
	
	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
//		System.out.println("md5 source:"+origin);
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}
	
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}
	
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
	
	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	public static void main(String[] args) {
		String MD5Value = MD5Encode("12" + "@quickpay!@!12","UTF-8");
		System.out.println(MD5Value);
	}
	
}