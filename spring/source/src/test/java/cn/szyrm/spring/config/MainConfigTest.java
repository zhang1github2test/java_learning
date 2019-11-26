package cn.szyrm.spring.config;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainConfigTest {
    ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
    @Test
    public void testImport(){
        printBeans(context);
        //工厂bean获取的是调用getObject方法获得的对象
        Object bearFactoryBean = context.getBean("bearFactoryBean");
        System.out.println(bearFactoryBean);
    }
    private void printBeans(ApplicationContext context){
        String[] names = context.getBeanDefinitionNames();
        for (String name:names
             ) {
            System.out.println(name);
        }


    }
}
