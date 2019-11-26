package cn.szyrm.spring.config;

import cn.szyrm.spring.bean.Cat;
import cn.szyrm.spring.condition.MyImportBeanDefinitionRegistry;
import cn.szyrm.spring.condition.MyImportSelector;
import cn.szyrm.spring.bean.Pig;
import cn.szyrm.spring.compont.Person;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * pig会被作为一个组件导入到容器中，name为类的全路径名
 */
@Configuration //告诉spring 这是一个配置类
@ComponentScan(basePackageClasses = {Cat.class})
@Import(value = {MyImportBeanDefinitionRegistry.class,Pig.class, MyImportSelector.class})
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
