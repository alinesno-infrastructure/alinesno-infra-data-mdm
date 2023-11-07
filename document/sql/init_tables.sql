SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for business_system
-- ----------------------------
DROP TABLE IF EXISTS `business_system`;
CREATE TABLE `business_system`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `identity` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务系统标识',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务系统名称',
  `remark` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `id` bigint NOT NULL COMMENT '主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_catagory
-- ----------------------------
DROP TABLE IF EXISTS `data_catagory`;
CREATE TABLE `data_catagory`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `identity` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据目录标识',
  `cata_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主数据目录名称',
  `naming_convention` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '命名规范',
  `sc_sys_id`  bigint DEFAULT NULL COMMENT '来源系统',
  `classify_id` bigint DEFAULT NULL COMMENT '行业分类',
  `parent_cata_id` bigint DEFAULT NULL COMMENT '父目录ID',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `id` bigint NOT NULL COMMENT '主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_change_log
-- ----------------------------
DROP TABLE IF EXISTS `data_change_log`;
CREATE TABLE `data_change_log`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `identity` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据目录标识',
  `cata_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主数据目录名称',
  `naming_convention` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '命名规范',
  `sc_sys_id` bigint DEFAULT NULL COMMENT '来源系统',
  `classify_id` bigint DEFAULT NULL COMMENT '行业分类',
  `parent_cata_id` bigint DEFAULT NULL COMMENT '父目录ID',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `old_id` bigint DEFAULT NULL COMMENT '原ID',
  `id` bigint NOT NULL COMMENT '主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_detail
-- ----------------------------
DROP TABLE IF EXISTS `data_detail`;
CREATE TABLE `data_detail`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `classify_id` bigint DEFAULT NULL COMMENT '行业分类ID',
  `cata_id` bigint DEFAULT NULL COMMENT '数据目录_id',
  `identity` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标识',
  `short_name` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简称',
  `standard_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代码',
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `length` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '长度',
  `standard_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明',
  `quality` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '质量标准',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `id` bigint NOT NULL COMMENT '主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_detail_log
-- ----------------------------
DROP TABLE IF EXISTS `data_detail_log`;
CREATE TABLE `data_detail_log`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `identity` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标识',
  `short_name` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简称',
  `standard_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代码',
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `length` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '长度',
  `standard_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明',
  `quality` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '质量标准',
  `standard_status` bigint(20) NULL DEFAULT NULL COMMENT '数据标准状态',
  `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `classify_id` bigint DEFAULT NULL COMMENT '分类ID',
  `cata_id` bigint DEFAULT NULL COMMENT '主数据目录_id',
  `old_id` bigint DEFAULT NULL COMMENT '原ID',
  `id` bigint NOT NULL COMMENT '主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for industry_classify
-- ----------------------------
DROP TABLE IF EXISTS `industry_classify`;
CREATE TABLE `industry_classify`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `identity` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行业分类标识',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行业分类名称',
  `remark` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `parent_id` bigint DEFAULT NULL COMMENT '父类ID',
  `id` bigint NOT NULL COMMENT '主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;



SET FOREIGN_KEY_CHECKS = 1;
