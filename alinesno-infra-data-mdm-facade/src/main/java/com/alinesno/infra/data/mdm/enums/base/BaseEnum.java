package com.alinesno.infra.data.mdm.enums.base;

/**
 * 枚举定义接口，实现了统一接口的枚举方便做统一处理
 *
 * @author luoxiaodong
 */
public interface BaseEnum<T> {

	/**
	 * 获取枚举编码
	 *
	 * @return 枚举编码
	 */
	T getCode();

	/**
	 * 获取枚举显示说明
	 *
	 * @return 枚举说明
	 */
	String getDesc();

}
