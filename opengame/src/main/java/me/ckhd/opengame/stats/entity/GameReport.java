/*
 * 创酷互动® 2016
 */
package me.ckhd.opengame.stats.entity;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.persistence.DataEntity;

/**
 * @author qibiao
 * @date 2016-06-13
 */
public class GameReport extends DataEntity<GameReport>{
	
	private static final long serialVersionUID = 1L;
	
	private String ckAppId;

	private String date;

	private String status;
	
	// 报表数据序列化成blob
	private byte[] data;
	
	private List<Map<String,Object>> dataList;
	
	public String getCkAppId() {
		return ckAppId;
	}

	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public List<Map<String,Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String,Object>> dataList) {
		this.dataList = dataList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
