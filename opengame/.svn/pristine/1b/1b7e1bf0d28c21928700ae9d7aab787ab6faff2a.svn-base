package me.ckhd.opengame.app.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

import com.alibaba.fastjson.JSONObject;

public class BaseStation extends DataEntity<BaseStation> {

	private static final long serialVersionUID = -8254586551341372919L;
	private int mnc;
	private int lac;
	private int ci;
	private String province;
	private String city;
	private String district;
	
	private int ciMin;
	private int ciMax;
	private int ciMod65536;

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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
	
	// public BaseStation(){}
	// public BaseStation(JSONObject json){
	// if(null != json){
	// if(json.containsKey("mnc")){
	// this.setMnc(json.getString("mnc"));
	// }
	// if(json.containsKey("lac")){
	// this.setLac(json.getString("lac"));
	// }
	// if(json.containsKey("ci")){
	// this.setCi(json.getString("ci"));
	// }
	// if(json.containsKey("sid")){
	// this.setMnc(json.getString("mnc"));
	// }
	// if(json.containsKey("nid")){
	// this.setLac(json.getString("lac"));
	// }
	// if(json.containsKey("bid")){
	// this.setCi(json.getString("ci"));
	// }
	// }
	// }

	public int getCiMin() {
		return ciMin;
	}

	public void setCiMin(int ciMin) {
		this.ciMin = ciMin;
	}

	public int getCiMax() {
		return ciMax;
	}

	public void setCiMax(int ciMax) {
		this.ciMax = ciMax;
	}

	public int getCiMod65536() {
		return ciMod65536;
	}

	public void setCiMod65536(int ciMod65536) {
		this.ciMod65536 = ciMod65536;
	}

	public JSONObject bulidJson() {
		JSONObject json = new JSONObject();
		json.put("province", this.getProvince());
		json.put("city", this.getCity());
		json.put("district", this.getDistrict());
		return json;
	}
}
