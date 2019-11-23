package cn.szyrm.spring.config;

import cn.szyrm.spring.aop.Car;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AopTest {
    @Test
    public void test() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AopConfig.class);
        Car car = context.getBean("car", Car.class);

        long driver = car.driver(System.currentTimeMillis());


    }
}
