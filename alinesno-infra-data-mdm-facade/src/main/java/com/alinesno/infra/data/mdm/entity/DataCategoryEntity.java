package com.alinesno.infra.data.mdm.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 功能名：数据目录
 * 数据表：data_catagory
 * 表备注：
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 *@author luoxiaodong
 *@version 1.0.0
 */

@TableName("data_catagory")
public class DataCategoryEntity extends InfraBaseEntity {

    // 数据目录标识
    @TableField("identity")
    private String identity;

    // 主数据目录名称
    @TableField("cata_name")
    private String cataName;

    // 命名规范
    @TableField("naming_convention")
    private String namingConvention;

    // 来源系统
    @TableField("sc_sys_id")
    private Long sourceSystemId;

    // 行业分类
    @TableField("classify_id")
    private Long classifyId;

    // 父目录ID
    @TableField("parent_cata_id")
    private Long parentCategoryId;

    // 备注
    @TableField("remark")
    private String remark;

    // 主键
    @TableField("id")
    private Long id;

    // Getter and Setter methods
    // ...
}
