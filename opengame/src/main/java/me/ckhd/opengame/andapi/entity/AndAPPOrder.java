package me.ckhd.opengame.andapi.entity;

import java.util.Date;
import java.util.List;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.sys.entity.Dict;
import me.ckhd.opengame.sys.entity.Role;

public class AndAPPOrder extends  DataEntity<AndAPPOrder>{

 
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户伪码
	 */
	private String userId;
	/**
	 * 计费代码
	 */
	private String contentId;
	/**
	 * 道具计费代码
	 */
	private String consumeCode;
	/**
	 * 合作代码
	 */
	private String cpId;
	/**
	 * 平台计费结果（状态码外码）0-成功 其他-失败
	 */
	private String hRet;
	/**
	 * 返回状态（内码），详见5.2章节
	 */
	private String status;
	/**
	 * 版本号2_0_0, 统一填写2_0_0
	 */
	private String versionId;
	/**
	 * CP透传参数，同2.7.2章节中的付费接口参数一致。
	 */
	private String cpparam;
	/**
	 * 套餐包ID(非局数据ID)
	 */
	private String packageID;
	
	
	/**
	 * 渠道号
	 */
	private  String channelId;
	
	/**
	 * price 
	 */
	private String price;
	
	private Date beginDate;		// 开始日期
	private Date endDate;		// 结束日期
	
	/**
	 * ckappid
	 */
	private String ckappId;
	
	private String provinceId;
	
	//查询条件
	private int random;
	
	private String filterRole;
		
	private int filterRate;
	
	private String time;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getConsumeCode() {
		return consumeCode;
	}
	public void setConsumeCode(String consumeCode) {
		this.consumeCode = consumeCode;
	}
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public String gethRet() {
		return hRet;
	}
	public void sethRet(String hRet) {
		this.hRet = hRet;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getCpparam() {
		return cpparam;
	}
	public void setCpparam(String cpparam) {
		this.cpparam = cpparam;
	}
	public String getPackageID() {
		return packageID;
	}
	public void setPackageID(String packageID) {
		this.packageID = packageID;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	 
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getCkappId() {
		return ckappId;
	}
	public void setCkappId(String ckappId) {
		this.ckappId = ckappId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	
	public int getRandom() {
		return random;
	}

	public void setRandom(int random) {
		this.random = random;
	}

	public String getFilterRole() {
		return filterRole;
	}

	public void setFilterRole(List<Role> roles,List<Dict> filterRoles) {
//		if(filterRole == null){
			for(Role r:roles){
//				System.out.println(r.getId());
				for(Dict filterRole:filterRoles){
					if(r.getName().equals(filterRole.getValue())){
						this.filterRole = "10";//此角色数据已被过滤
						return;
					}
				}
			}
//		}
	}

	public int getFilterRate() {
		return filterRate;
	}

	public void setFilterRate(int filterRate) {
		this.filterRate = filterRate;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
