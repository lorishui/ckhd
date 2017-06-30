package me.ckhd.opengame.app.entity;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.persistence.DataEntity;

//省份设置
public class PayRulesConfig extends DataEntity<PayRulesConfig>{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pr_id;
	private String provinceids;
	private String carriers;
	//电信
	private String money;
	private String totalmoney;
	
	private String carriersName;
	
	public PayRulesConfig() {
		super();
	}
	
	public PayRulesConfig(String pr_id, String provinceids,
			String carriers) {
		super();
		this.pr_id = pr_id;
		this.provinceids = provinceids;
		this.carriers = carriers;
	}
	
	public PayRulesConfig(HttpServletRequest request) {
		super();
		this.id = request.getParameter("id");//获取开始时间
		this.pr_id = request.getParameter("pr_id");//获取开始时间
		String _carriers = request.getParameter("carriers");
		this.carriers=_carriers;
		this.money = request.getParameter("money");
		this.totalmoney = request.getParameter("totalmoney");
	}
	
	public PayRulesConfig(String id,String pr_id, String provinceids,
			String carriers) {
		super();
		super.setId(id);;
		this.pr_id = pr_id;
		this.provinceids = provinceids;
		this.carriers = carriers;
	}
	public String getPr_id() {
		return pr_id;
	}
	public void setPr_id(String pr_id) {
		this.pr_id = pr_id;
	}
	public String getProvinceids() {
		return provinceids;
	}
	public void setProvinceids(String provinceids) {
		this.provinceids = provinceids;
	}
	public String getCarriers() {
		return carriers;
	}
	public void setCarriers(String carriers) {
		this.carriers = carriers;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getTotalmoney() {
		return totalmoney;
	}

	public void setTotalmoney(String totalmoney) {
		this.totalmoney = totalmoney;
	}

	public String getCarriersName() {
		return carriersName;
	}

	public void setCarriersName(String carriersName) {
		this.carriersName = carriersName;
	}
}
