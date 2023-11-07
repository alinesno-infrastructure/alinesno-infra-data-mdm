package com.alinesno.infra.data.mdm.enums;

import com.alinesno.infra.data.mdm.enums.base.BaseEnum;

import java.util.Arrays;
import java.util.Objects;

/**
 * 状态枚举类
 *
 * @author luoxiaodong
 */

public enum HasStatusEnum implements BaseEnum<Integer> {
	/**
	 * 在用,正常
	 */
	ISADOPT(0, "正常"),
	/**
	 * 停用
	 */
	NOTADOPT(1, "停用");

	private Integer code;
	private String desc;

	public static HasStatusEnum getEnum(Integer code) {
		return Arrays.stream(values()).filter(b -> Objects.equals(b.code, code)).findFirst().orElse(null);
	}

	public static String getEnumDesc(Integer code) {
		HasStatusEnum e = getEnum(code);
		return e != null ? e.desc : null;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	private HasStatusEnum(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

}
