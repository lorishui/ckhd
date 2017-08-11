package me.ckhd.opengame.user.utils;

public enum ErrorCode {
	USER_MEG_NOT_SUBMIT(2025,"请求中未提交用户相关信息!"),
	EMAIL_MOBILE_NOT_EXIST(2024,"检查手机或邮箱是否正确后,重新按照指引操作!"),
	SEND_SMS_FREQUENTLY(2023,"获取验证码频繁,请3分钟后再获取!"),
	INVALID_REQUEST(2022,"无效请求"),
	SESSION_INVALID(2021,"会话已失效或无效！"),
	EMAIL_MOBILE_BIND(2020,"邮箱或手机号已经绑定"),
	REQUEST_FAILURE(2019,"获取验证码失败频繁,请3分钟后再获取!"),
	CHECK_CODE_ERROR(2018,"校验码错误"),
	USER_IS_EXIST(2017,"用户名已存在"),
	VERIFY_CODE_ERROR(2016,"验证码错误"),
	MOBILE_WRONG(2015,"手机号码格式错误,请输入正确手机号"),
	EMAIL_WRONG(2014,"邮箱格式错误,请输入正确邮箱"),
	USERACCOUNT_WRONG(2013,"账号格式错误,请输入正确账号"),
	NOT_BOUND(2012,"未绑定邮箱/手机号"),
	NOT_EXIST_PARAMETER (2011,"必要参数缺失,请联系客服"),
	ALREADY_BOUND(2010,"手机号码或邮箱已绑定"),
	LIMIT_EXCEEDED(2009,"校验码发送次数超限"),
	FAILURE_CODES(2008,"校验码验证失败"),
	INTERNAL_ERROR(2007,"内部错误,请联系客服"),
	USER_PWD_ERROR(2006,"用户名或密码错误"),
	NOT_LOGIN(2005,"用户已登录"),
	USER_NOT_EXIST(2004,"用户不存在"),
	PARAM_IS_NULL(2003,"必要参数缺失,请联系客服"),
	GET_EMIAL_CODE_FAIL(2002,"获取邮箱校验码失败"),
	LOGIN_FAIL(2001,"登陆失败"),
	SUCCESSS(2000,"SUCCESS");
	private int code;
	private String msg;
	private ErrorCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
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
