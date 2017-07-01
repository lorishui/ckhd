package me.ckhd.opengame.app.utils;

public enum PropertyConversion {
	MMAPPID("mmAppId"),
	split("配置"),
	扩展参数("exInfo"),
	运营商("carriers"),
	carriers("运营商"),
	二次确认支付次数("twoAckTimes"),
	twoAckTimes("二次确认支付次数"),
	一次确认支付次数("oneAckTimes"),
	oneAckTimes("一次确认支付次数"),
	SDK名称("mmSdkName"),
	mmSdkName("SDK名称"),
	版本号("versionName"),
	versionName("版本号"),
	时间段("time"),
	time("时间段"),
	渠道("ckChannelId"),
	ckChannelId("渠道"),
	省份("province"),
	province("省份"),
	类型("ackType"),
	ackType("类型");
	
	private String value;
	private PropertyConversion(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public static String getValue(String key){
		String str = "";
		for(PropertyConversion p : PropertyConversion.values()){
			if(p.name().equals(key)){
				str = p.getValue();
				break;
			}
		}
		return str;
	}
}
