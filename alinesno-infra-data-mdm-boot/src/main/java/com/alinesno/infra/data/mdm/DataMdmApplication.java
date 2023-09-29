package com.alinesno.infra.data.mdm;

import com.alinesno.infra.common.web.adapter.sso.enable.EnableInfraSsoApi;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 集成一个Java开发示例工具
 * @author LuoAnDong
 * @since 2023年8月3日 上午6:23:43
 */
@EnableInfraSsoApi
@MapperScan("com.alinesno.infra.data.mdm.mapper")
@SpringBootApplication
public class DataMdmApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataMdmApplication.class, args);
	}

}