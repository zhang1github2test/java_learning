package cn.szyrm.spring.config;


import cn.szyrm.spring.tx.TxConfig;
import cn.szyrm.spring.tx.UserDao;
import cn.szyrm.spring.tx.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TxTest {
    @Test
    public void test() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TxConfig.class);
        printBeans(context);
        UserService userService = context.getBean(UserService.class);
        userService.insert();


    }
    private void printBeans(ApplicationContext context){
        String[] names = context.getBeanDefinitionNames();
        for (String name:names
        ) {
            System.out.println(name);
        }


    }
}
