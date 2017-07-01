package me.ckhd.opengame.user.model;

import me.ckhd.opengame.user.utils.ErrorCode;
import me.ckhd.opengame.user.utils.JSONInterface;

import com.alibaba.fastjson.JSONObject;

public class Result extends JSONInterface{
	private static String index_code="a";
	private static String index_msg="b";
	
	private int code;
	private String msg;
	
	@Override
	public void pareJSON(Object obj) {
		JSONObject json = null;
		if(obj != null){
			if(obj.getClass().getSimpleName().equals(JSONObject.class.getSimpleName())){
				json = (JSONObject)obj;
				this.setCode(json.getIntValue(index_code));
				this.setMsg(json.getString(index_msg));
			}
			if(obj.getClass().getSimpleName().equals(String.class.getSimpleName())){
				json = JSONObject.parseObject(obj.toString());
				this.setCode(json.getIntValue(index_code));
				this.setMsg(json.getString(index_msg));
			}
		}
	}
	
	@Override
	public JSONObject buildJSON() {
		JSONObject json = new JSONObject();
		json.put(index_code, this.getCode());
		json.put(index_msg, this.getMsg());
		return json;
	}

	public Result(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	public Result(ErrorCode code) {
		this.code = code.getCode();
		this.msg = code.getMsg();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
