package cn.szyrm.spring.aop;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
public class Car {
    public long driver(long time) throws InterruptedException {
        System.out.println("start driver");
        Thread.sleep(500);
        int i = 10/0;
        return  System.currentTimeMillis() - time;
    }
}
