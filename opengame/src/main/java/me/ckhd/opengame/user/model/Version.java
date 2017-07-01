package me.ckhd.opengame.user.model;

import me.ckhd.opengame.user.utils.JSONInterface;

import com.alibaba.fastjson.JSONObject;

public class Version extends JSONInterface{
	private static String index_number="a";
	private static String index_language="b";
	private String number;
	private String language;//zh_TW,zh_CN

	@Override
	public void pareJSON(Object obj) {
		JSONObject json = null;
		if(obj != null){
			if(obj.getClass().getSimpleName().equals(JSONObject.class.getSimpleName())){
				json = (JSONObject)obj;
				this.setNumber(json.getString(index_number));
				if(json.containsKey(index_language)){
					this.setLanguage(json.getString(index_language));
				}
			}
			if(obj.getClass().getSimpleName().equals(String.class.getSimpleName())){
				json = JSONObject.parseObject(obj.toString());
				this.setNumber(json.getString(index_number));
				if(json.containsKey(index_language)){
					this.setLanguage(json.getString(index_language));
				}
			}
		}
	}

	@Override
	public JSONObject buildJSON() {
		JSONObject json = new JSONObject();
		json.put(index_number, this.getNumber());
		return json;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	
	public String getLanguage() {
		return language;
	}

	
	public void setLanguage(String language) {
		this.language = language;
	}

}
