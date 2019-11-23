package cn.szyrm.spring.config;

import cn.szyrm.spring.compont.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration //告诉spring 这是一个配置类
@ComponentScan(basePackages = "cn.szyrm.spring")
public class MainConfig {
    /**
     * bean的名称 为方法名
     * @return
     */
   // @Bean
    public Person person(){
        return new  Person("小白龙",20);
    }
}
