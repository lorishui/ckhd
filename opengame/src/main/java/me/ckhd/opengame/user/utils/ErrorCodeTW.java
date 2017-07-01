package me.ckhd.opengame.user.utils;

/**
 * 台湾繁体中文错误码
 * @author ASUS
 *
 */
public enum ErrorCodeTW {
	USER_MEG_NOT_SUBMIT(2025,"請求中未提交用戶相關信息!"),
	EMAIL_MOBILE_NOT_EXIST(2024,"檢查手機或郵箱是否正確後,重新按照指引操作!"),
	SEND_SMS_FREQUENTLY(2023,"獲取驗證碼頻繁,請3分鐘後再獲取!"),
	INVALID_REQUEST(2022,"無效請求"),
	SESSION_INVALID(2021,"會話已失效或無效！"),
	EMAIL_MOBILE_BIND(2020,"郵箱或手機號已經綁定"),
	REQUEST_FAILURE(2019,"獲取驗證碼失敗頻繁,請3分鐘後再獲取!"),
	CHECK_CODE_ERROR(2018,"校驗碼錯誤"),
	USER_IS_EXIST(2017,"用戶名已存在"),
	VERIFY_CODE_ERROR(2016,"驗證碼錯誤"),
	MOBILE_WRONG(2015,"手機號碼格式錯誤,請輸入正確手機號"),
	EMAIL_WRONG(2014,"郵箱格式錯誤,請輸入正確郵箱"),
	USERACCOUNT_WRONG(2013,"賬號格式錯誤,請輸入正確賬號"),
	NOT_BOUND(2012,"未綁定郵箱/手機號"),
	NOT_EXIST_PARAMETER (2011,"必要參數缺失,請聯系客服"),
	ALREADY_BOUND(2010,"手機號碼或郵箱已綁定"),
	LIMIT_EXCEEDED(2009,"校驗碼發送次數超限"),
	FAILURE_CODES(2008,"校驗碼驗證失敗"),
	INTERNAL_ERROR(2007,"內部錯誤,請聯系客服"),
	USER_PWD_ERROR(2006,"用戶名或密碼錯誤"),
	NOT_LOGIN(2005,"用戶已登錄"),
	USER_NOT_EXIST(2004,"用戶不存在"),
	PARAM_IS_NULL(2003,"必要參數缺失,請聯系客服"),
	GET_EMIAL_CODE_FAIL(2002,"獲取郵箱校驗碼失敗"),
	LOGIN_FAIL(2001,"登陸失敗"),
	SUCCESSS(2000,"SUCCESS");
	private int code;
	private String msg;
	private ErrorCodeTW(int code, String msg) {
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
	
	public static String getErrorCode(Integer code){
		for (int i = 0; i < ErrorCodeTW.values().length; i++) { 
			if( code == ErrorCodeTW.values()[i].getCode()){
				return ErrorCodeTW.values()[i].getMsg();
			}
		}
		return "";
	}
}
