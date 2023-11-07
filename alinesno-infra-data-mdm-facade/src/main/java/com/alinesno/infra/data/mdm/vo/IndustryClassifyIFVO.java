package com.alinesno.infra.data.mdm.vo;

import java.io.Serializable;

/**
 * 功能名： 行业分类清单VO
 *
 * @author luoxiaodong
 * @since 1.0.0
 */
public class IndustryClassifyIFVO implements Serializable {
	private static final long serialVersionUID = 1L;
	// fields

	/**
	 * 行业分类ID
	 */
	private String classifyID;


	/**
	 * 行业分类标识
	 */
	private String identity;

	/**
	 * 行业分类名称
	 */
	private String name;


	/**
	 * 父分类ID
	 */
	private String parentId;

	/**
	 * 父分类
	 */
	private String parentName;

	// getter and setter

	public String getClassifyID() {
		return classifyID;
	}

	public void setClassifyID(String classifyID) {
		this.classifyID = classifyID;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
}
