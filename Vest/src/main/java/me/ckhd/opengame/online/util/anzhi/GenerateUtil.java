package me.ckhd.opengame.online.util.anzhi;



public class GenerateUtil{
	
	
	public static final String generateSessionToken(String uid,String loginTime,Integer loginType){
		return Base64.encodeToString(uid+"_"+loginTime+"_"+String.valueOf(loginType));
	}
	public static void main(String[] args) {
		System.out.println(generateSessionToken("20130328165207hlUept53TD","1368870185",1));
//		System.out.println(Base64.encodeToString("c318br6RLex12IeBs0Ta6wo1" + loginName + sid + app.getAppSecret()));
	}

}
