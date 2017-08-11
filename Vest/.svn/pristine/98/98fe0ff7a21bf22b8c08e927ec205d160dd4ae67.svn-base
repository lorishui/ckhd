package me.ckhd.opengame.user.version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.user.model.DataRequest;

public abstract class SDKVersion {
	public Logger log = LoggerFactory.getLogger(this.getClass());
	public abstract String register(DataRequest dataRequest);
	public abstract String login(DataRequest dataRequest);
	public abstract String getCheckSum(DataRequest dataRequest);
	public abstract String getBindingMsg(DataRequest dataRequest);
	public abstract String changePassword(DataRequest dataRequest);
	public abstract String bindMobileOrEmail(DataRequest dataRequest);
	public abstract String unbindMobileOrEmail(DataRequest dataRequest);
	public abstract String checkToken(DataRequest dataRequest);
	public abstract String roleMessageCollect(DataRequest dataRequest);
	public abstract String changePwdByOldPwd(DataRequest dataRequest);
}
