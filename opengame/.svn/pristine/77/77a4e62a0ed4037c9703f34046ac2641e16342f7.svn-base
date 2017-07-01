package me.ckhd.opengame.user.model;

import me.ckhd.opengame.user.utils.JSONInterface;

import com.alibaba.fastjson.JSONObject;

public class Verify extends JSONInterface{
	private static String index_code="a";
	private static String index_type="b";
	
	private String code;
	private int type;
	
	@Override
	public void pareJSON(Object obj) {
		JSONObject json = null;
		if(obj != null){
			if(obj.getClass().getSimpleName().equals(JSONObject.class.getSimpleName())){
				json = (JSONObject)obj;
				this.setCode(json.getString(index_code));
				this.setType(json.getInteger(index_type));
			}
			if(obj.getClass().getSimpleName().equals(String.class.getSimpleName())){
				json = JSONObject.parseObject(obj.toString());
				this.setCode(json.getString(index_code));
				this.setType(json.getInteger(index_type));
			}
		}
	}
	
	@Override
	public JSONObject buildJSON() {
		JSONObject json = new JSONObject();
		json.put(index_code, this.getCode());
		json.put(index_type, this.getType());
		return json;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
