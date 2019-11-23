package cn.szyrm.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 后置处理器：初始化前后进行处理工作
 * 将后置处理器添加到容器中
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
   public  Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //可以断点查看调用栈
        System.out.println("postProcessBeforeInitialization   ... " + beanName);
        return bean;
    }

   public  Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
       System.out.println("postProcessAfterInitialization   ... " + beanName);
        return bean;
    }
}
