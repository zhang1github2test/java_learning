package cn.szyrm.spring.config;

import cn.szyrm.spring.bean.Dog;
import cn.szyrm.spring.bean.Pig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * bean的生命周期
 * bean 创建---初始化---销毁
 * 容器管理bean的生命周期
 * 我们可以自定义初始和销毁方法；容器在bean进行到当前生命周期的时候来调用我们自定义的初始化方法和销毁方法
 * spring 底层对BeanPostProcessor的使用
 *      bean赋值、注入其他组件，@Autowired 生命周期注解功能  ，@Async , XXXBeanPostProcessor
 */
@Configuration
@ComponentScan(basePackages = "cn.szyrm.spring.bean")
public class LifeCycleConfig {
    @Bean(initMethod = "init",destroyMethod = "destroy")
    public Pig pig(){
        return  new Pig();
    }


}
