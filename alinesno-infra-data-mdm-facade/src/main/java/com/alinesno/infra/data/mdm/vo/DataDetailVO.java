package com.alinesno.infra.data.mdm.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * 功能名： 数据明细导入VO
 *
 * @author luoxiaodong
 * @since 1.0.0
 */
public class DataDetailVO implements Serializable {
	// fields

	/**
	 * 数据目录
	 */
	@Excel(name = "数据目录")
	@TableField("cata_id")
	private String cataName;


	@Excel(name = "标识")
	@TableField("identity")
	private String identity;

	/**
	 * 数据标准名称
	 */

	@Excel(name = "名称")
	@TableField("standard_name")
	private String standardName;

	/**
	 * 编码
	 */
	@Excel(name = "编码")
	@TableField("code")
	private String code;

	/**
	 * 说明
	 */
	@Excel(name = "说明")
	@TableField("standard_desc")
	private String standarDesc;


	@Excel(name = "类型")
	@TableField("type")
	private String type;


	@Excel(name = "长度")
	@TableField("length")
	private String length;

	@Excel(name = "质量标准")
	@TableField("quality")
	private String quality;



	/**
	 * 备注
	 */
	@Excel(name = "备注")
	@TableField("remark")
	private String remark;

	// getter and setter

	public String getCataName() {
		return cataName;
	}

	public void setCataName(String cataName) {
		this.cataName = cataName;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getStandardName() {
		return standardName;
	}

	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}

	public String getStandardCode() {
		return code;
	}

	public void setStandardCode(String code) {
		this.code = code;
	}

	public String getStandarDesc() {
		return standarDesc;
	}

	public void setStandarDesc(String standarDesc) {
		this.standarDesc = standarDesc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
