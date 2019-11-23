package cn.szyrm.spring;

import cn.szyrm.spring.compont.Person;
import cn.szyrm.spring.config.MainConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringContextTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext  context = new AnnotationConfigApplicationContext(MainConfig.class);
        Person person = context.getBean("person", Person.class);
        System.out.println(person);
        String[] beans = context.getBeanDefinitionNames();
        for (String name:beans
             ) {
            System.out.println(name);
        }
    }
}
