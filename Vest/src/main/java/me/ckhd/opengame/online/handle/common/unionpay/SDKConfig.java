/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28       MPI基本参数工具类
 * =============================================================================
 */
package me.ckhd.opengame.online.handle.common.unionpay;

import java.util.Properties;

import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 软件开发工具包 配制
 * 
 * @author xuyaowen
 * 
 */
public class SDKConfig {
	
	static Logger log = LoggerFactory.getLogger(SDKConfig.class);

	public static final String FILE_NAME = "acp_sdk.properties";

	/** 前台请求URL. */
	private String frontRequestUrl;
	/** 后台请求URL. */
	private String backRequestUrl;
	/** 单笔查询 */
	private String singleQueryUrl;
	/** 批量查询 */
	private String batchQueryUrl;
	/** 批量交易 */
	private String batchTransUrl;
	/** 文件传输 */
	private String fileTransUrl;
	/** 签名证书路径. */
	private String signCertPath;
	/** 签名证书密码. */
	private String signCertPwd;
	/** 签名证书类型. */
	private String signCertType;
	/** 加密公钥证书路径. */
	private String encryptCertPath;
	/** 验证签名公钥证书目录. */
	private String validateCertDir;
	/** 按照商户代码读取指定签名证书目录. */
	private String signCertDir;
	/** 磁道加密证书路径. */
	private String encryptTrackCertPath;
	/** 有卡交易. */
	private String cardRequestUrl;
	/** app交易 */
	private String appRequestUrl;
	/** 证书使用模式(单证书/多证书) */
	private String singleMode;


	/** 配置文件中的前台URL常量. */
	public static final String SDK_FRONT_URL = "acpsdk.frontTransUrl";
	/** 配置文件中的后台URL常量. */
	public static final String SDK_BACK_URL = "acpsdk.backTransUrl";
	/** 配置文件中的单笔交易查询URL常量. */
	public static final String SDK_SIGNQ_URL = "acpsdk.singleQueryUrl";
	/** 配置文件中的批量交易查询URL常量. */
	public static final String SDK_BATQ_URL = "acpsdk.batchQueryUrl";
	/** 配置文件中的批量交易URL常量. */
	public static final String SDK_BATTRANS_URL = "acpsdk.batchTransUrl";
	/** 配置文件中的文件类交易URL常量. */
	public static final String SDK_FILETRANS_URL = "acpsdk.fileTransUrl";
	/** 配置文件中的有卡交易URL常量. */
	public static final String SDK_CARD_URL = "acpsdk.cardTransUrl";
	/** 配置文件中的app交易URL常量. */
	public static final String SDK_APP_URL = "acpsdk.appTransUrl";

	/** 配置文件中签名证书路径常量. */
	public static final String SDK_SIGNCERT_PATH = "acpsdk.signCert.path";
	/** 配置文件中签名证书密码常量. */
	public static final String SDK_SIGNCERT_PWD = "acpsdk.signCert.pwd";
	/** 配置文件中签名证书类型常量. */
	public static final String SDK_SIGNCERT_TYPE = "acpsdk.signCert.type";
	/** 配置文件中密码加密证书路径常量. */
	public static final String SDK_ENCRYPTCERT_PATH = "acpsdk.encryptCert.path";
	/** 配置文件中磁道加密证书路径常量. */
	public static final String SDK_ENCRYPTTRACKCERT_PATH = "acpsdk.encryptTrackCert.path";
	/** 配置文件中验证签名证书目录常量. */
	public static final String SDK_VALIDATECERT_DIR = "acpsdk.validateCert.dir";

	/** 配置文件中是否加密cvn2常量. */
	public static final String SDK_CVN_ENC = "acpsdk.cvn2.enc";
	/** 配置文件中是否加密cvn2有效期常量. */
	public static final String SDK_DATE_ENC = "acpsdk.date.enc";
	/** 配置文件中是否加密卡号常量. */
	public static final String SDK_PAN_ENC = "acpsdk.pan.enc";
	/** 配置文件中证书使用模式 */
	public static final String SDK_SINGLEMODE = "acpsdk.singleMode";
	/** 操作对象. */
	private static SDKConfig config;
	/** 属性文件对象. */
	private Properties properties;
	

	private SDKConfig() {
		super();
		init();
	}

	/**
	 * 获取config对象.
	 * 
	 * @return
	 */
	public static SDKConfig getConfig() {
		if (null == config) {
			config = new SDKConfig();
		}
		return config;
	}

	/**
	 * 根据传入的 {@link #load(java.util.Properties)}对象设置配置参数
	 * 
	 * @param pro
	 */
	public void init() {
		String value = Global.getConfig(SDK_SINGLEMODE);
		this.singleMode = SDKConstants.TRUE_STRING;
		log.error("SingleCertMode:[" + this.singleMode + "]");
		try{
			// 单证书模式
			value = Global.getConfig(SDK_SIGNCERT_PATH);
			if (!StringUtils.isEmpty(value)) {
				this.signCertPath = Thread.currentThread().getContextClassLoader().getResource(value.trim()).getFile();
			}
			value = Global.getConfig(SDK_SIGNCERT_PWD);
			if (!StringUtils.isEmpty(value)) {
				this.signCertPwd = value.trim();
			}
			value = Global.getConfig(SDK_SIGNCERT_TYPE);
			if (!StringUtils.isEmpty(value)) {
				this.signCertType = value.trim();
			}
			value = Global.getConfig(SDK_ENCRYPTCERT_PATH);
			if (!StringUtils.isEmpty(value)) {
				this.encryptCertPath = Thread.currentThread().getContextClassLoader().getResource(value.trim()).getFile();
			}
			value = Global.getConfig(SDK_VALIDATECERT_DIR);
			if (!StringUtils.isEmpty(value)) {
				log.info("path="+value);
				this.validateCertDir = value.trim();
			}
			value = Global.getConfig(SDK_FRONT_URL);
			if (!StringUtils.isEmpty(value)) {
				this.frontRequestUrl = value.trim();
			}
			value = Global.getConfig(SDK_BACK_URL);
			if (!StringUtils.isEmpty(value)) {
				this.backRequestUrl = value.trim();
			}
			value = Global.getConfig(SDK_BATQ_URL);
			if (!StringUtils.isEmpty(value)) {
				this.batchQueryUrl = value.trim();
			}
			value = Global.getConfig(SDK_BATTRANS_URL);
			if (!StringUtils.isEmpty(value)) {
				this.batchTransUrl = value.trim();
			}
			value = Global.getConfig(SDK_FILETRANS_URL);
			if (!StringUtils.isEmpty(value)) {
				this.fileTransUrl = value.trim();
			}
			value = Global.getConfig(SDK_SIGNQ_URL);
			if (!StringUtils.isEmpty(value)) {
				this.singleQueryUrl = value.trim();
			}
			value = Global.getConfig(SDK_CARD_URL);
			if (!StringUtils.isEmpty(value)) {
				this.cardRequestUrl = value.trim();
			}
			value = Global.getConfig(SDK_APP_URL);
			if (!StringUtils.isEmpty(value)) {
				this.appRequestUrl = value.trim();
			}
			value = Global.getConfig(SDK_ENCRYPTTRACKCERT_PATH);
			if (!StringUtils.isEmpty(value)) {
				this.encryptTrackCertPath = Thread.currentThread().getContextClassLoader().getResource(value.trim()).getFile();
			}
		}catch(Exception e){
			log.error("银联错误1111", e);
		}
	}


	public String getFrontRequestUrl() {
		return frontRequestUrl;
	}

	public void setFrontRequestUrl(String frontRequestUrl) {
		this.frontRequestUrl = frontRequestUrl;
	}

	public String getBackRequestUrl() {
		return backRequestUrl;
	}

	public void setBackRequestUrl(String backRequestUrl) {
		this.backRequestUrl = backRequestUrl;
	}

	public String getSignCertPath() {
		return signCertPath;
	}

	public void setSignCertPath(String signCertPath) {
		this.signCertPath = signCertPath;
	}

	public String getSignCertPwd() {
		return signCertPwd;
	}

	public void setSignCertPwd(String signCertPwd) {
		this.signCertPwd = signCertPwd;
	}

	public String getSignCertType() {
		return signCertType;
	}

	public void setSignCertType(String signCertType) {
		this.signCertType = signCertType;
	}

	public String getEncryptCertPath() {
		return encryptCertPath;
	}

	public void setEncryptCertPath(String encryptCertPath) {
		this.encryptCertPath = encryptCertPath;
	}
	
	public String getValidateCertDir() {
		return validateCertDir;
	}

	public void setValidateCertDir(String validateCertDir) {
		this.validateCertDir = validateCertDir;
	}

	public String getSingleQueryUrl() {
		return singleQueryUrl;
	}

	public void setSingleQueryUrl(String singleQueryUrl) {
		this.singleQueryUrl = singleQueryUrl;
	}

	public String getBatchQueryUrl() {
		return batchQueryUrl;
	}

	public void setBatchQueryUrl(String batchQueryUrl) {
		this.batchQueryUrl = batchQueryUrl;
	}

	public String getBatchTransUrl() {
		return batchTransUrl;
	}

	public void setBatchTransUrl(String batchTransUrl) {
		this.batchTransUrl = batchTransUrl;
	}

	public String getFileTransUrl() {
		return fileTransUrl;
	}

	public void setFileTransUrl(String fileTransUrl) {
		this.fileTransUrl = fileTransUrl;
	}

	public String getSignCertDir() {
		return signCertDir;
	}

	public void setSignCertDir(String signCertDir) {
		this.signCertDir = signCertDir;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getCardRequestUrl() {
		return cardRequestUrl;
	}

	public void setCardRequestUrl(String cardRequestUrl) {
		this.cardRequestUrl = cardRequestUrl;
	}

	public String getAppRequestUrl() {
		return appRequestUrl;
	}

	public void setAppRequestUrl(String appRequestUrl) {
		this.appRequestUrl = appRequestUrl;
	}
	
	public String getEncryptTrackCertPath() {
		return encryptTrackCertPath;
	}

	public void setEncryptTrackCertPath(String encryptTrackCertPath) {
		this.encryptTrackCertPath = encryptTrackCertPath;
	}
	
	public String getSingleMode() {
		return singleMode;
	}

	public void setSingleMode(String singleMode) {
		this.singleMode = singleMode;
	}

}
