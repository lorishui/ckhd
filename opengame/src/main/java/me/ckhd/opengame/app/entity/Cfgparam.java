package me.ckhd.opengame.app.entity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.common.utils.MyJsonUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class Cfgparam  extends  DataEntity<Cfgparam>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Logger log = LoggerFactory.getLogger(Cfgparam.class);

	private String ckAppId; // 创酷Appid

	private String mmAppId; // 运营商Appid

	private String ckChannelId; // 创酷渠道号

	private String province; // 省份Id，字典province_iccid中的value
	@Deprecated
	private String provinceName; // 省份名称
	
	private String simNO; // ICCID

	private String versionName; // 版本名称

	private String carriers; // 运营商

	private String os; // 系统

	private String rqType; // 初始化数据类型
	
	private String exInfo; //扩展参数
	
	private String time;//时间段
	
	private String signMD5; // 签名md5值
	
	private String imei;
	
	private String imsi;
	
	private Map<String,Object> exInfoMap = new HashMap<String,Object>(); //扩展参数的map
	
	private String payIndex;

	// 基站信息
	private int mcc;
	
	private int mnc;
	
	private int lac;
	
	private int ci;
	
	// 机型
	private String phoneModel;
	
	public Cfgparam(){}
	
	public Cfgparam(JSONObject jsonObject){
		this.ckAppId=jsonObject.getString("ckAppId");
		this.mmAppId=jsonObject.getString("mmAppId");
		this.ckChannelId=jsonObject.getString("ckChannelId");
		this.province=jsonObject.getString("province");
		this.simNO=jsonObject.getString("simNO");
		this.versionName=jsonObject.getString("versionName");
		if(jsonObject.get("carriers") != null && jsonObject.getString("carriers").length() > 4){
			this.carriers=jsonObject.getString("carriers").substring(0, 4);
		}else{
			this.carriers=jsonObject.getString("carriers");
		}
		this.os=jsonObject.getString("os");
		this.rqType=jsonObject.getString("rqType");
		this.payIndex=jsonObject.getString("payIndex");
		this.signMD5=jsonObject.getString("signMD5");
		this.imei=jsonObject.getString("imei");
		this.imsi=jsonObject.getString("imsi");
		
		this.phoneModel = jsonObject.getString("phone_model");
		
		// 基站数据
		String dataMcc = jsonObject.getString("mcc");
		String dataMnc = jsonObject.getString("mnc");
		String dataLac = jsonObject.getString("lac");
		String dataCi = jsonObject.getString("ci");
		String dataSid = jsonObject.getString("sid");
		String dataNid = jsonObject.getString("nid");
		String dataBid = jsonObject.getString("bid");

		// 基站数据
		if (dataMcc != null || dataMnc != null || dataSid != null) {
			try {
				mcc = Integer.parseInt(dataMcc);
				mnc = Integer.parseInt(dataMnc);
				lac = Integer.parseInt(dataLac);
				ci = Integer.parseInt(dataCi);

				if (dataSid != null && dataNid != null && dataBid != null) {
					mcc = 460;
					mnc = Integer.parseInt(dataSid);
					lac = Integer.parseInt(dataNid);
					ci = Integer.parseInt(dataBid);
				}

			} catch (Throwable t) {
				// 新包，异常数据或者用户未授权
				mcc = -1;
				mnc = 0;
				lac = 0;
				ci = 0;
			}
		} else {
			// 旧包
			mcc = -100;
		}
	}
	
	public String getCkAppId() {
		return ckAppId;
	}

	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}


	public String getCkChannelId() {
		return ckChannelId;
	}

	public void setCkChannelId(String ckChannelId) {
		this.ckChannelId = ckChannelId;
	}

	public String getSimNO() {
		return simNO;
	}

	public void setSimNO(String simNO) {
		this.simNO = simNO;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getCarriers() {
		return carriers;
	}

	public void setCarriers(String carriers) {
		this.carriers = carriers;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getRqType() {
		return rqType;
	}

	public void setRqType(String rqType) {
		this.rqType = rqType;
	}	
	
	public String getExInfo() {
		return exInfo;
	}

	public void setExInfo(String exInfo) {
		this.exInfo = exInfo;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Map<String, Object> getExInfoMap() {
		exInfoMap=MyJsonUtils.jsonStr2Map(exInfo);
		return exInfoMap;
	}

	@Override
	public String toString() {
		return "Cfgparam [ckAppId=" + ckAppId + ", mmappId=" + mmAppId
				+ ", ckChannelId=" + ckChannelId + ", simNO=" + simNO
				+ ", versionName=" + versionName + ", carriers=" + carriers
				+ ", os=" + os + ", rqType=" + rqType + ", exInfo=" + exInfo
				+ ", exInfoMap=" + exInfoMap + "]";
	}

	public String getMmAppId() {
		return mmAppId;
	}

	public void setMmAppId(String mmAppId) {
		this.mmAppId = mmAppId;
	}


	public String getProvince() {
		return province;
	}


	public void setProvince(String province) {
		this.province = province;
	}

	public String getPayIndex() {
		return payIndex;
	}

	public void setPayIndex(String payIndex) {
		this.payIndex = payIndex;
	}
	
	public String getSignMD5() {
		return signMD5;
	}

	public void setSignMD5(String signMD5) {
		this.signMD5 = signMD5;
	}
	@Deprecated
	public String getProvinceName() {
		return provinceName;
	}
	@Deprecated
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public int getMcc() {
		return mcc;
	}

	public void setMcc(int mcc) {
		this.mcc = mcc;
	}

	public int getMnc() {
		return mnc;
	}

	public void setMnc(int mnc) {
		this.mnc = mnc;
	}

	public int getLac() {
		return lac;
	}

	public void setLac(int lac) {
		this.lac = lac;
	}

	public int getCi() {
		return ci;
	}

	public void setCi(int ci) {
		this.ci = ci;
	}

	public String getPhoneModel() {
		return phoneModel;
	}

	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}

	public void setValue(Map<String,Object> map){
		Field[] field = this.getClass().getDeclaredFields();
		for(Field f : field){
			if( map.get(f.getName()) != null && map.get(f.getName()).toString().trim().length() > 0){
				try{
					f.setAccessible(true);
					if( f.getType().equals(Integer.class.getName()) || f.getType().equals(int.class) ){
						f.setInt(this, Integer.parseInt(map.get(f.getName()).toString()));
					}else if( f.getType().equals(Float.class.getName()) || f.getType().equals(float.class) ){
						f.setFloat(this, (float)map.get(f.getName()));
					}else if( f.getType().equals(Double.class.getName()) || f.getType().equals(double.class) ){
						f.setDouble(this, (double)map.get(f.getName()));
					}else if( f.getType().equals(Long.class.getName()) || f.getType().equals(long.class) ){
						f.setLong(this, (long)map.get(f.getName()));
					}else {
						f.set(this, (String)map.get(f.getName()));
					}
				}catch(Exception e){
					log.error("map change "+this.getClass().getName()+" failure!!!", e);
				}
			}
		}
	}
}
