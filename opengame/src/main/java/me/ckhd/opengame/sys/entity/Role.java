/**
 * Copyright &copy; 2015-2018 <a href="http://www.ckhd.me/">创酷互动</a> All rights reserved.
 */
package me.ckhd.opengame.sys.entity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.persistence.DataEntity;

/**
 * 用户Entity
 * @author ckhd
 * @version 2015-06-18
 */
public class Role extends DataEntity<Role> {
	
	private static final long serialVersionUID = 1L;
	private String name; 	// 角色名称
	private String enname;	// 英文名称
	private String roleType;// 权限类型
	private String dataScope;// 数据范围
	
	private String oldName; 	// 原角色名称
	private String oldEnname;	// 原英文名称
	private String isabled; 	//是否是可用
	private String ckappIds; 	//可访问游戏
	private String channelIds;  //可访问渠道
	private User user;		// 根据用户ID查询角色列表

	private List<Resource> resourceList = Lists.newArrayList(); // 拥有菜单列表

	// 数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；4：所在部门及以下数据；5：所在部门数据；8：仅本人数据；9：按明细设置）
	public static final String DATA_SCOPE_ALL = "1";
	public static final String DATA_SCOPE_COMPANY_AND_CHILD = "2";
	public static final String DATA_SCOPE_COMPANY = "3";
	public static final String DATA_SCOPE_OFFICE_AND_CHILD = "4";
	public static final String DATA_SCOPE_OFFICE = "5";
	public static final String DATA_SCOPE_SELF = "8";
	public static final String DATA_SCOPE_CUSTOM = "9";
	
	public Role() {
		super();
		this.dataScope = DATA_SCOPE_SELF;
		this.isabled=Global.YES;
	}
	
	public Role(String id){
		super(id);
	}
	
	public Role(User user) {
		this();
		this.user = user;
	}

	 

	 
 

	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=1, max=100)
	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}
	
	@Length(min=1, max=100)
	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getIsabled() {
		return isabled;
	}

	public String getCkappIds() {
		return ckappIds;
	}

	public void setCkappIds(String ckappIds) {
		this.ckappIds = ckappIds;
	}

	public void setIsabled(String isabled) {
		this.isabled = isabled;
	}

	public String getOldEnname() {
		return oldEnname;
	}

	public void setOldEnname(String oldEnname) {
		this.oldEnname = oldEnname;
	}

	public String getChannelIds() {
		return channelIds;
	}

	public void setChannelIds(String channelIds) {
		this.channelIds = channelIds;
	}

	public List<Resource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}

	public List<String> getResourceIdList() {
		List<String> resourceIdList = Lists.newArrayList();
		for (Resource resource : resourceList) {
			resourceIdList.add(resource.getId());
		}
		return resourceIdList;
	}

	public void setResourceIdList(List<String> resourceIdList) {
		resourceList = Lists.newArrayList();
		for (String resourceId : resourceIdList) {
			Resource resource = new Resource();
			resource.setId(resourceId);
			resourceList.add(resource);
		}
	}

	public String getResourceIds() {
		return StringUtils.join(getResourceIdList(), ",");
	}
	
	public void setResourceIds(String resourceIds) {
		System.out.println("XXXX"+resourceIds);
		resourceList = Lists.newArrayList();
		if (resourceIds != null){
			String[] ids = StringUtils.split(resourceIds, ",");
			setResourceIdList(Lists.newArrayList(ids));
		}
	}
 
 
	 
	
	/**
	 * 获取权限字符串列表
	 */
	public List<String> getPermissions() {
		List<String> permissions = Lists.newArrayList();
		for (Resource resource : resourceList) {
			if (resource.getPermission()!=null && !"".equals(resource.getPermission())){
				permissions.add(resource.getPermission());
			}
		}
		return permissions;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
