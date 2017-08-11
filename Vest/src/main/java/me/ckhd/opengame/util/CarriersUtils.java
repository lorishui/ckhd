package me.ckhd.opengame.util;

public class CarriersUtils {

	// 三大运营商前缀
	public final static String CHINA_MOBILE = "CMCC";
	public final static String CHINA_TELECOM = "CTCC";
	public final static String CHINA_UNICOM = "CUCC";

	public static String getCarriersType(String iccid) {
		if (iccid == null) {
			return "unknown";
		}
		if (iccid.startsWith("898600") || iccid.startsWith("898602")) {
			return CHINA_MOBILE;
		} else if (iccid.startsWith("898601") || iccid.startsWith("898609")) {
			// 中国联通
			return CHINA_UNICOM;
		} else if (iccid.startsWith("898603") || iccid.startsWith("898606")) {
			// 中国电信
			return CHINA_TELECOM;
		} else {
			return "unknown";
		}
	}

}