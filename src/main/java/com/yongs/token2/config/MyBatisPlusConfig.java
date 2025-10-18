package com.yongs.token2.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 功能：
 * 作者：YongS
 * 日期：2025/10/18 14:33
 */
@Configuration
@MapperScan(basePackages = "com.yongs.token2.mapper")
public class MyBatisPlusConfig {
}
