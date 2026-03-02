package com.cobazaar.common.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

/**
 * MyBatis Plus 代码生成器
 * 用于快速生成 Entity、Mapper、Service、Controller 等代码
 *
 * @author cobazaar
 * @version 1.0.0
 * @since 2026-02-05
 */
@Slf4j
public class CodeGenerator {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://127.0.0.1:3307/demo?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true",
                    "root",
                    "root");

    /**
     * 执行代码生成
     *
     * @param moduleName 模块名称
     * @param tableName 表名
     * @param tablePrefix 表前缀（可选）
     */
    public static void execute(String moduleName, String tableName, String tablePrefix) {
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                // 全局配置
                .globalConfig(builder -> builder
                        .author("cobazaar")
                        .outputDir(System.getProperty("user.dir") + "/" + moduleName + "/src/main/java")
                        .commentDate("yyyy-MM-dd")
                        .dateType(DateType.TIME_PACK)
                        .disableOpenDir()
                )
                // 包配置
                .packageConfig(builder -> builder
                        .parent("com.cobazaar." + moduleName)
                        .moduleName("")
                        .entity("entity")
                        .service("service")
                        .serviceImpl("service.impl")
                        .mapper("mapper")
                        .xml("mapper.xml")
                        .controller("controller")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, 
                                System.getProperty("user.dir") + "/" + moduleName + "/src/main/resources/mapper"))
                )
                // 策略配置
                .strategyConfig(builder -> builder
                        .addInclude(tableName)
                        .addTablePrefix(tablePrefix)
                        
                        // Entity策略配置
                        .entityBuilder()
                        .enableLombok()
                        .enableTableFieldAnnotation()
                        .naming(NamingStrategy.underline_to_camel)
                        .columnNaming(NamingStrategy.underline_to_camel)
                        .superClass("com.cobazaar.common.entity.BaseEntity")
                        .enableActiveRecord()
                        .logicDeleteColumnName("del_flag")
                        .versionColumnName("version")
                        
                        // Mapper策略配置
                        .mapperBuilder()
                        .enableMapperAnnotation()
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                        
                        // Service策略配置
                        .serviceBuilder()
                        .superServiceClass("com.cobazaar.common.service.BaseService")
                        .superServiceImplClass("com.cobazaar.common.service.impl.BaseServiceImpl")
                        
                        // Controller策略配置
                        .controllerBuilder()
                        .enableRestStyle()
                        .enableHyphenStyle()
                )
                // 使用Freemarker引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
        
        log.info("代码生成完成：模块={}, 表名={}", moduleName, tableName);
    }

    /**
     * 简化版代码生成（使用默认配置）
     *
     * @param moduleName 模块名称
     * @param tableName 表名
     */
    public static void execute(String moduleName, String tableName) {
        execute(moduleName, tableName, "");
    }

    /**
     * 使用示例
     */
    public static void main(String[] args) {
        // 生成用户表相关代码
        // execute("user", "sys_user", "sys_");
        
        // 生成订单表相关代码
        // execute("order", "oms_order");
        
        log.info("请在main方法中调用execute方法生成代码");
    }
}