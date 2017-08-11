package me.ckhd.opengame.stats.entity;

import java.util.List;

import me.ckhd.opengame.common.persistence.DataEntity;

/**
 * @ClassName UserFlow
 * @Description 查看用户流向
 * @author ASUS
 * @Date 2017年6月8日 下午6:46:04
 * @version 1.0.0
 */
public class UserFlow extends DataEntity<UserFlow> implements Cloneable{

    private static final long serialVersionUID = 5330667718176413771L;
    
    private String ckAppId;
    private String childCkAppId;
    /** 初始渠道，来源渠道 */
    private String fromChannel;
    /** 流向的渠道 */
    private String toChannel;
    /** 流通的设备*/
    private String deviceId;
    /** 查询条件，开始时间 */
    private String startTime;
    /** 查询条件，结束时间 */
    private String endTime;
    /** 流通设备的总量 */
    private Integer num;
    /** 流通设备产生的金额 */
    private Integer totalMoney;

	/** 用于过滤游戏权限 */
	private String permissionCkAppId;
	private List<String> permissionCkAppChildId;
	/** 用于过滤渠道权限 */
	private String permissionChannelId;
	
    public String getCkAppId() {
        return ckAppId;
    }
    
    public void setCkAppId(String ckAppId) {
        this.ckAppId = ckAppId;
    }
    
    public String getChildCkAppId() {
        return childCkAppId;
    }
    
    public void setChildCkAppId(String childCkAppId) {
        this.childCkAppId = childCkAppId;
    }
    
    public String getFromChannel() {
        return fromChannel;
    }
    
    public void setFromChannel(String fromChannel) {
        this.fromChannel = fromChannel;
    }
    
    public String getToChannel() {
        return toChannel;
    }
    
    public void setToChannel(String toChannel) {
        this.toChannel = toChannel;
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    public String getStartTime() {
        return startTime;
    }

    
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }
    
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getTotalMoney() {
        return totalMoney;
    }
 
    public void setTotalMoney(Integer totalMoney) {
        this.totalMoney = totalMoney;
    }

	public String getPermissionCkAppId() {
		return permissionCkAppId;
	}

	public void setPermissionCkAppId(String permissionCkAppId) {
		this.permissionCkAppId = permissionCkAppId;
	}

	public List<String> getPermissionCkAppChildId() {
		return permissionCkAppChildId;
	}

	public void setPermissionCkAppChildId(List<String> permissionCkAppChildId) {
		this.permissionCkAppChildId = permissionCkAppChildId;
	}

	public String getPermissionChannelId() {
		return permissionChannelId;
	}

	public void setPermissionChannelId(String permissionChannelId) {
		this.permissionChannelId = permissionChannelId;
	}
    
}
