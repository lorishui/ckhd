package me.ckhd.opengame.user.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public class DataRequest {
	
	private UserInfo userInfo = null;
	private Session session = null;
	private Verify verify = null;
	private Role role = null;
	private Version version = null;
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private Application application = new Application();
	
	public DataRequest encode(String data,HttpServletRequest request,HttpServletResponse response){
		this.setRequest(request);
		this.setResponse(response);
		if(data != null && data.length()>0){
			JSONObject json = JSONObject.parseObject(data);
			if(json.containsKey("user")){
				userInfo = new UserInfo();
				userInfo.pareJSON(json.get("user"));
			}
			if(json.containsKey("session")){
				session = new Session();
				session.pareJSON(json.get("session"));
			}
			if(json.containsKey("verify")){
				verify = new Verify();
				verify.pareJSON(json.get("verify"));
			}
			if(json.containsKey("role")){
				role = new Role();
				role.pareJSON(json.get("role"));
			}
			if(json.containsKey("version")){
				version = new Version();
				version.pareJSON(json.get("version"));
			}
			if (json.containsKey("application")) {
			    application.pareJSON(json.get("application"));
			}
		}
		return this;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Verify getVerify() {
		return verify;
	}

	public void setVerify(Verify verify) {
		this.verify = verify;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
}
