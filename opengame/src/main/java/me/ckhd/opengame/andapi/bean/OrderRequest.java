package me.ckhd.opengame.andapi.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import me.ckhd.opengame.andapi.entity.AndAPPOrder;

import org.springframework.cglib.beans.BeanCopier;


/**
 * 移动和游戏支付服务器通知支付结果信息bean
 * <p>
 * 对于需要校验的字段，可以在对应属性名称的set方法中进行
 * </p>
 * 
 * <?xml version="1.0" encoding="UTF-8"?>
	<request>
	<userId>xxx</userId>
	<contentId>000000000000</contentId>
	<consumeCode>000000000000</consumeCode>
	<cpid>701010</cpid>
	<hRet>0</hRet>
	<status>1800</status>
	<versionId>xxx</versionId>
	<cpparam>xxx</cpparam>
	<packageID></packageID>
	</request>
 * 
 * 
 */
@XmlRootElement(name = "request")
public class OrderRequest implements Serializable {
 
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
	private String cpid;
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
	
	private String provinceId;
	
	private String channelId;
	
	public String getUserId() {
		return userId;
	}


	@XmlElement(name = "userId")
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContentId() {
		return contentId;
	}
	@XmlElement(name = "contentId")
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
 
	public String getConsumeCode() {
		return consumeCode;
	}

	@XmlElement(name = "consumeCode")
	public void setConsumeCode(String consumeCode) {
		this.consumeCode = consumeCode;
	}
 
	public String getCpId() {
		return cpid;
	}
	@XmlElement(name = "cpid")
	public void setCpid(String cpid) {
		this.cpid = cpid;
	}
	public String gethRet() {
		return hRet;
	}
	@XmlElement(name = "hRet")
	public void sethRet(String hRet) {
		this.hRet = hRet;
	}
	public String getStatus() {
		return status;
	}
	@XmlElement(name = "status")
	public void setStatus(String status) {
		this.status = status;
	}
 
	public String getVersionId() {
		return versionId;
	}

	@XmlElement(name = "versionId")
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
 
	public String getCpparam() {
		return cpparam;
	}
	@XmlElement(name = "cpparam")
	public void setCpparam(String cpparam) {
		this.cpparam = cpparam;
	}
 
	public String getPackageID() {
		return packageID;
	}
	@XmlElement(name = "packageID")
	public void setPackageID(String packageID) {
		this.packageID = packageID;
	}
	
	public String getProvinceId() {
		return provinceId;
	}
	@XmlElement(name = "provinceId")
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getChannelId() {
		return channelId;
	}

	@XmlElement(name = "channelId")
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * 转成对应entity
	 * @return
	 */
	
	public AndAPPOrder genEntity(){
		// BeanUtils
		AndAPPOrder andAPPOrder = new AndAPPOrder();
		BeanCopier copier = BeanCopier.create(this.getClass(), AndAPPOrder.class,false );
		copier.copy(this, andAPPOrder, null);
		return andAPPOrder;
	}
	
	

}
