## spring 中bean的生命周期

spring中的bean的生命周期主要有： bean 的创建-->初始化-->销毁

有spring 容器的管理的bean.我们可以通过自定义初始化和销毁方法；定义好后容器会自动调用我们的自定义方法

* 对象创建

  单实例：在容器启动的时候创建对象

  多实例：在每次获取对象的时候创建（定义的销毁方法不会产生作用）

* 初始化方法(对象创建完成并完成赋值操作)和销毁方法(容器关闭)

  * 通过 @Bean注解的initMethod属性和destroyMethod属性来指定  

  ```java
  @Configuration
  @ComponentScan(basePackages = "cn.szyrm.spring.bean")
  public class LifeCycleConfig {
      @Bean(initMethod = "init",destroyMethod = "destroy")
      public Pig pig(){
          return  new Pig();
      }
  }
  ```

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

  ```java
  public class LifeCycleTest {
      @Test
      public void test(){
          AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
          System.out.println("容器创建完成");
          context.close();
      }
  }
  ```

  执行上述的test方法,控制台打印如下：

  ```txt
  Pig  constructor ....
  Pig .... init ........
  容器创建完成
  Pig ... destroy .... 
  ```

  可以看到,pig先进行创建，然后执行init方法-->执行销毁方法

  * 通过让Beans实现InitializingBean(初始化), DisposableBean(销毁)

    添加一个Dog的bean，定义如下：

    ```java
    @Component
    public class Dog implements InitializingBean, DisposableBean {
        public Dog() {
            System.out.println("Dog constructor");
        }
    
        @Override
        public void destroy() throws Exception {
            System.out.println("Dog  destroy ... ");
    
        }
    
        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("Dog init .... ");
        }
    }
    ```

    执行LifeCycle中的测试方法,打印如下：

    ```txt
    
    Dog constructor
    Dog init .... 
    Pig  constructor ....
    Pig .... init ........
    容器创建完成
    Pig ... destroy .... 
    Dog  destroy ... 
    ```

  * JSR250：@PostConstruct：在bean创建完成并且完成赋值后的

    ​		@PreDestroy :bean销毁的时候执行

    ```java
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
    ```

  * BeanPostProcessor:bean的后置处理器

    ​	bean在初始化前后的进行一些处理工作

    ​	`postProcessBeforeInitialization` :bean初始化之前所作的工作

    ​	postProcessAfterInitialization:bean初始化工作之后

```java

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

```

