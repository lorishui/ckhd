/*
 * www.szckhd.com
 */
package me.ckhd.opengame.app.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

/**
 * 
 * @author qibiao
 *
 */
public class MetaCellinfoZone extends DataEntity<MetaCellinfoZone> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pid;
	private String name;
	private String leaf;
	private String sort;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getLeaf() {
		return leaf;
	}

	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}

}
