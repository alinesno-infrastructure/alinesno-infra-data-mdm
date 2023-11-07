package com.alinesno.infra.data.mdm.entity;

import com.alinesno.infra.common.facade.mapper.entity.InfraBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnComment;
import com.gitee.sunchenbin.mybatis.actable.annotation.ColumnType;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 功能名：数据详情日志
 * 数据表：data_detail_log
 * 表备注：
 *
 * @TableName 表名注解，用于指定数据库表名
 * @TableField 字段注解，用于指定数据库字段名
 *
 *@author luoxiaodong
 *@version 1.0.0
 */

@EqualsAndHashCode(callSuper = true)
@TableName("data_detail_log")
@Data
public class DataDetailLogEntity extends InfraBaseEntity {

    // 标识
    @ColumnType(length = 64)
    @ColumnComment("标识")
    @TableField("identity")
    private String identity;

    // 简称
    @ColumnType(length = 36)
    @ColumnComment("简称")
    @TableField("short_name")
    private String shortName;

    // 名称
    @ColumnType(length = 200)
    @ColumnComment("名称")
    @TableField("standard_name")
    private String standardName;

    // 代码
    @ColumnType(length = 200)
    @ColumnComment("代码")
    @TableField("code")
    private String code;

    // 类型
    @ColumnType(length = 20)
    @ColumnComment("类型")
    @TableField("type")
    private String type;

    // 长度
    @ColumnType(length = 10)
    @ColumnComment("长度")
    @TableField("length")
    private String length;

    // 说明
    @ColumnType(length = 256)
    @ColumnComment("说明")
    @TableField("standard_desc")
    private String standardDesc;

    // 质量标准
    @ColumnType(length = 256)
    @ColumnComment("质量标准")
    @TableField("quality")
    private String quality;

    // 数据标准状态
    @TableField("standard_status")
    private Long standardStatus;

    // 备注
    @ColumnType(length = 512)
    @ColumnComment("备注")
    @TableField("remark")
    private String remark;

    // 分类ID
    @ColumnType(length = 64 , value= MySqlTypeConstant.BIGINT)
    @ColumnComment("行业分类ID")
    @TableField("classify_id")
    private Long classifyId;

    // 主数据目录_id
    @ColumnType(length = 64 , value= MySqlTypeConstant.BIGINT)
    @ColumnComment("数据目录ID")
    @TableField("cata_id")
    private Long cataId;

    // 原ID
    @ColumnType(length = 64 , value= MySqlTypeConstant.BIGINT)
    @ColumnComment("原ID")
    @TableField("old_id")
    private Long oldId;

    // 主键
//    @TableField("id")
//    private Long id;

    // Getter and Setter methods
    // ...
}
