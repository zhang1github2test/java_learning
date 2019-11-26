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

   通过`@EnableAspectJAutoProxy` 注解来导入 ` AspectJAutoProxyRegistrar` 组件。

  该组件通过调用`org.springframework.context.annotation.AspectJAutoProxyRegistrar#registerBeanDefinitions ` 方法来注册一个 `AnnotationAwareAspectJAutoProxyCreator` 后置处理器，其在容器的名称为：`org.springframework.aop.config.internalAutoProxyCreator` 

  ```java
  	AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);
  
  		AnnotationAttributes enableAspectJAutoProxy =
  				AnnotationConfigUtils.attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
  		if (enableAspectJAutoProxy != null) {
  			if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
  				AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
  			}
  			if (enableAspectJAutoProxy.getBoolean("exposeProxy")) {
  				AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
  			}
  		}
  ```

  真正执行注册`AnnotationAwareAspectJAutoProxyCreator` 组件的方法：

```java

	@Nullable
	private static BeanDefinition registerOrEscalateApcAsRequired(
			Class<?> cls, BeanDefinitionRegistry registry, @Nullable Object source) {

		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");

		if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
			BeanDefinition apcDefinition = registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
			if (!cls.getName().equals(apcDefinition.getBeanClassName())) {
				int currentPriority = findPriorityForClass(apcDefinition.getBeanClassName());
				int requiredPriority = findPriorityForClass(cls);
				if (currentPriority < requiredPriority) {
					apcDefinition.setBeanClassName(cls.getName());
				}
			}
			return null;
		}

		RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
		beanDefinition.setSource(source);
		beanDefinition.getPropertyValues().add("order", Ordered.HIGHEST_PRECEDENCE);
		beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		registry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition);
		return beanDefinition;
	}
```



`AnnotationAwareAspectJAutoProxyCreator` 类的继承结构如下：

![1574693189114](assets/1574693189114.png)

  从上面`UML` 图可以看出，其实现了`BenPostProcessor` 接口和`BeanFactoryAwre` 接口。必然会有  ` setBeanFacotry`方法 及与`beanPostProcessor` 接口相关的方法。在这些方法上打上断点然后调试

![1574694132279](assets/1574694132279.png)

 整个的流程如下：

* 1)、：传入配置类，创建`IOC` 容器 `new AnnotationConfigApplicationContext(AopConfig.class);`  

* 2)、注册配置类，调用refresh()刷新容器

* 3）、注册bean的后置处理器来拦截bean的创建 ：`registerBeanPostProcessors(beanFactory);` 
  * 1、先获取`IOC` 容器中的所有的`BeanPostProcessor` 的定义的名称
  * 2、给容器中加别BeanPostProcessor
  * 3、优先注册实现了PriorityOrdered接口的BeanPostPrcessor
  * 4、再给容器总注册实现了Order接口的BeanPostProcessor
  * 5、注册没实现order接口的BeanPostProcessor
  * 6、注册`BeanPostProcessor` ，实际上就是创建BeanPostProcessor对象，保存在容器中；
    * 1）、创建`internalAutoProxyCreator`  

