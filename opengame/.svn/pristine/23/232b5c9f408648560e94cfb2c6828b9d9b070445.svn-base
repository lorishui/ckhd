/**
 * Copyright &copy; 2015-2018 <a href="http://www.ckhd.me/">创酷互动</a> All rights reserved.
 */
package me.ckhd.opengame.app.entity;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.common.utils.excel.annotation.ExcelField;

import org.hibernate.validator.constraints.Length;

/**
 * 省份代码 Entity
 * @author ckhd
 * @version 2015-06-25
 */
public class Province extends DataEntity<Province> {

	private static final long serialVersionUID = 1L;
	private String code;     // 省份代码
	private String name;	// 名称
	private String oldName; // 原名
	

	public Province() {
		super();
	}
	public Province(String id){
		super(id);
	}

	public Province(String id, String name){
		super(id);
		this.name = name;
	}
	public Province(String id,String code, String name){
		super(id);
		this.code = code;
		this.name = name;
	}


	 

	@ExcelField(title="ID", type=1, align=2, sort=1)
	public String getId() {
		return id;
	}
	 

	@Length(min=1, max=20, message="省份名称长度必须介于 1 和 20 之间")
	@ExcelField(title="省份名称", align=2, sort=40)
	public String getName() {
		return name;
	}
	
	@Length(min=1, max=45, message="省份代码长度必须介于 1 和 45 之间")
	@ExcelField(title="省份代码", align=2, sort=45)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}
	@ExcelField(title="备注", align=1, sort=900)
	public String getRemarks() {
		return remarks;
	}
	
	 
 

	 
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	@Override
	public String toString() {
		return id;
	}
}