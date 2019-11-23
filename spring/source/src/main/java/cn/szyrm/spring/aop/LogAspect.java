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
        System.out.println("afterThrowing ... ");
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
