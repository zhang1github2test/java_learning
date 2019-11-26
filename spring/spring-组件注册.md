### spring 注册组件（bean）的几种方式

* 通过包扫描 + @Component/@Service/@Controller等

* 通过@Bean注解

* 通过@Import

  * 1）、@import（要导入到容器中的组件）：容器中就会自动注册这个组件，id默认是全类名

    @Import(value = {Pig.class})

  * 2）、ImportSelector：返回需要导入的组件的全类名数据组

    ```java
    package cn.szyrm.spring.condition;
    
    import cn.szyrm.spring.bean.Horse;
    import org.springframework.context.annotation.ImportSelector;
    import org.springframework.core.type.AnnotationMetadata;
    
    public class MyImportSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {
            return new String[]{Horse.class.getName()};
        }
    }
    ```

    

  * 3）、ImportBeanDefinitionRegistrar:手动注册bean到容器中

    

    配置类：

    ```java
    /**
     * pig会被作为一个组件导入到容器中，name为类的全路径名
     */
    @Configuration //告诉spring 这是一个配置类
    @ComponentScan(basePackageClasses = {Cat.class})
    @Import(value = {Pig.class, MyImportSelector.class})
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
    ```

    通过@import注解导入的类：

    ```java
    public class Pig {
        public Pig() {
            System.out.println("Pig  constructor ....");
        }
        public void init(){
            System.out.println("Pig .... init ........");
        }
        public void destroy(){
            System.out.println("Pig ... destroy .... ");
        }
    }
    ```

    测试类：

    ```java
    
    public class MainConfigTest {
        ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        @Test
        public void testImport(){
            printBeans(context);
        }
        private void printBeans(ApplicationContext context){
            String[] names = context.getBeanDefinitionNames();
            for (String name:names
                 ) {
                System.out.println(name);
            }
    
    
        }
    }
    
    ```

    

* @使用spring 提供的FactoryBean

  * 1）、默认获取到的是工厂bean调用getObject创建的对象
  * 2）、要获取工厂Bean本身，我们需要给ID前添加一个&符号

  完整的配置类：

  ```java
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
  ```

  

  FactoryBean的一个实现

 ```java
@Component
public class BearFactoryBean implements FactoryBean<Bear> {
    @Override
    public Bear getObject() throws Exception {
        return new Bear();
    }

    @Override
    public Class<?> getObjectType() {
        return Bear.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
 ```

```java
/**
* 测试Factorybean的生成
*/

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

```

测试结果：

```txt
cat constructor .... 
 @PostConstruct exec ... 
Dog constructor
Dog init .... 
Pig  constructor ....

org.springframework.context.annotation.internalConfigurationAnnotationProcessor
org.springframework.context.annotation.internalAutowiredAnnotationProcessor
org.springframework.context.annotation.internalCommonAnnotationProcessor
org.springframework.context.event.internalEventListenerProcessor
org.springframework.context.event.internalEventListenerFactory
mainConfig
bearFactoryBean
cat
dog
cn.szyrm.spring.bean.Pig
cn.szyrm.spring.bean.Horse
deer
cn.szyrm.spring.bean.Bear@2abf4075



Process finished with exit code 0

```

