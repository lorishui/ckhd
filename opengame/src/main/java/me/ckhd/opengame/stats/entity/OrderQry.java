package me.ckhd.opengame.stats.entity;

public class OrderQry {

	private String ckAppId;
	
	// yyyyMMdd
	private String startDate;
	
	// yyyyMMdd
	private String endDate;
	
	public String getCkAppId() {
		return ckAppId;
	}

	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
}
