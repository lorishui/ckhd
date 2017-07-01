package me.ckhd.opengame.stats.entity;

import java.util.List;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.sys.entity.Dict;
import me.ckhd.opengame.sys.entity.Role;

public class MmChannelProvince extends DataEntity<MmChannelProvince> implements Cloneable {

	private static final long serialVersionUID = 3993384149871901651L;
	// 查询条件
	private String ckappId;
	private String appId;
	private String provinceId;
	private String channelId;
	private int orderType;
	private String startDate;
	private String endDate;

	private String filterRole;
	private int filterRate;
	// 统计结果
	private int totalNum;
	private int succNum;
	private double totalPrice;
	private double succPrice;

	private String orderDire = "Desc";
	private int orderByTP;		//按总金额排序  0 no  1 yes
	private int orderBySP;		//按成功金额排序  0 no  1 yes
	
	
	
	
	public int getSuccNum() {
		return succNum;
	}

	public void setSuccNum(int succNum) {
		this.succNum = succNum;
	}

	public double getSuccPrice() {
		return succPrice;
	}

	public void setSuccPrice(double succPrice) {
		this.succPrice = succPrice;
	}


	public String getOrderDire() {
		return orderDire;
	}

	public void setOrderDire(String orderDire) {
		this.orderDire = orderDire;
	}

	public int getOrderByTP() {
		return orderByTP;
	}

	public void setOrderByTP(int orderByTP) {
		this.orderByTP = orderByTP;
	}

	public int getOrderBySP() {
		return orderBySP;
	}

	public void setOrderBySP(int orderBySP) {
		this.orderBySP = orderBySP;
	}

	public void setFilterRole(String filterRole) {
		this.filterRole = filterRole;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}


	public String getCkappId() {
		return ckappId;
	}

	public void setCkappId(String ckappId) {
		this.ckappId = ckappId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
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

	public String getFilterRole() {
		return filterRole;
	}

	public void setFilterRole(List<Role> roles, List<Dict> filterRoles) {
		// if(filterRole == null){
		for (Role r : roles) {
			// System.out.println(r.getId());
			for (Dict filterRole : filterRoles) {
				if (r.getName().equals(filterRole.getValue())) {
					this.filterRole = "10";// 此角色数据已被过滤
					return;
				}
			}
		}
		// }
	}

	public int getFilterRate() {
		return filterRate;
	}

	public void setFilterRate(int filterRate) {
		this.filterRate = filterRate;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}


	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
