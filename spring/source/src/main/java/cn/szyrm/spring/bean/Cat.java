package cn.szyrm.spring.bean;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class Cat {
    public Cat() {
        System.out.println("cat constructor .... ");
    }
    //
    @PostConstruct
    public void init(){
        System.out.println(" @PostConstruct exec ... ");
    }
    @PreDestroy
    public void destroy(){
        System.out.println(" @PreDestroy  ...");
    }
}
