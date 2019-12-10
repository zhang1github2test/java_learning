package cn.szyrm.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 搭建基本环境
 * 1、导入数据库文件 创建出
 * 2、创建javaBean封装数据
 * 3、整合mybatis操作数据
 *      1、配置数据源信息
 *      2.
 *  二、快速体验缓存
 *      1、在配置类上添加@EnableCaching
 *      2、标注缓存注解
 */
@SpringBootApplication
@MapperScan("cn.szyrm.cache.mapper")
@EnableCaching
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
