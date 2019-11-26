package cn.szyrm.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
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
    @AfterThrowing(value = "pointCut()",throwing="throwable")
    public void logException(JoinPoint joinPoint , Throwable throwable){
        System.out.println(joinPoint.getSignature().getName()+" afterThrowing ... " + "异常信息为:" + throwable);
    }
    @Around(value = "pointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println(joinPoint.getSignature().getName() + "around before ...");
        Object proceed = joinPoint.proceed();
        System.out.println(proceed);
        System.out.println(joinPoint.getSignature().getName() +"around  after...  ");
        return proceed;
    }
}
