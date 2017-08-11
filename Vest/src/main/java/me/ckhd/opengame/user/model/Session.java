package me.ckhd.opengame.user.model;

import me.ckhd.opengame.user.utils.JSONInterface;

import com.alibaba.fastjson.JSONObject;

public class Session extends JSONInterface{
	private static String index_token="a";
	private static String index_time="b";
	private static String index_code="c";
	
	private String token;
	private long time;
	private String code;
	
	@Override
	public void pareJSON(Object obj) {
		JSONObject json = null;
		if(obj != null){
			if(obj.getClass().getSimpleName().equals(JSONObject.class.getSimpleName())){
				json = (JSONObject)obj;
				this.setToken(json.getString(index_token));
				this.setTime(json.getLongValue(index_time));
				this.setCode(json.getString(index_code));
			}
			if(obj.getClass().getSimpleName().equals(String.class.getSimpleName())){
				json = JSONObject.parseObject(obj.toString());
				this.setToken(json.getString(index_token));
				this.setTime(json.getLongValue(index_time));
				this.setCode(json.getString(index_code));
			}
		}
	}
	
	@Override
	public JSONObject buildJSON() {
		JSONObject json = new JSONObject();
		json.put(index_token, this.getToken());
		json.put(index_time, this.getTime());
		json.put(index_code, this.getCode());
		return json;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
