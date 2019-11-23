# spring AOP 学习（基于注解）

AOP【面向切面编程】：【底层实现：动态代理】 指在程序运行期间动态的将某段代码切入到指定方法指定位置进行运行的编程方式；

### 一、环境搭建

maven依赖：

```xml
  <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.2.1.RELEASE</version>
  </dependency>
  <dependency>
      <groupId>javax.annotation</groupId>
      <artifactId>javax.annotation-api</artifactId>
      <version>1.3.1</version>
  </dependency>
<dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-aspects</artifactId>
      <version>5.2.1.RELEASE</version>
  </dependency>
  <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
  </dependency>
```

### 二、操作步骤

* 将业务逻辑组件添加到容器中

  ```java
  @Component
  public class Car {
      public long driver(long time) throws InterruptedException {
          System.out.println("start driver");
          Thread.sleep(500);
          return  System.currentTimeMillis() - time;
      }
  }
  ```

* 编写切面类并加入到容器中，在切面类上的每一个通知方法上标注通知注解，告诉spring何时何地运行

  ```java
  package cn.szyrm.spring.aop;
  
  import org.aspectj.lang.JoinPoint;
  import org.aspectj.lang.ProceedingJoinPoint;
  import org.aspectj.lang.annotation.*;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.stereotype.Component;
  
  @Component
  @Aspect
  public class LogAspect {
      @Pointcut("execution(public  *  cn.szyrm.spring.aop.Car.driver(long))")
      public  void pointCut(){
  
      }
      @Before(value = "execution(public  *  cn.szyrm.spring.aop.Car.driver(long))")
      public  void logStart(){
          System.out.println("before....");
      }
       @After(value = "pointCut()")
      public  void logEnd(){
           System.out.println("after .... ");
      }
      @AfterReturning(value = "pointCut()")
      public void logReturn(){
          System.out.println("afterReturning .... ");
      }
      @AfterThrowing(value = "pointCut()")
      public void logException(){
  
      }
      @Around(value = "pointCut()")
      public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
          System.out.println("around before ...");
          Object proceed = joinPoint.proceed();
          System.out.println(proceed);
          System.out.println("around  after...  ");
          return proceed;
      }
  }
  ```

  

* 开启基于注解的aop模式：`@EnableAspectJAutoProxy` 

```java
@Configuration
@ComponentScan(basePackages = "cn.szyrm.spring.aop")
@EnableAspectJAutoProxy
public class AopConfig {
}
```

* 测试是否正确

  ```java
  public class AopTest {
      @Test
      public void test() throws InterruptedException {
          AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AopConfig.class);
          Car car = context.getBean("car", Car.class);
  
          long driver = car.driver(System.currentTimeMillis());
  
  
      }
  }
  ```

  ```txt
  around before ...
  before....
  start driver
  516
  around  after...  
  after .... 
  afterReturning .... 
  ```

  ### 三、`aop原理` 

