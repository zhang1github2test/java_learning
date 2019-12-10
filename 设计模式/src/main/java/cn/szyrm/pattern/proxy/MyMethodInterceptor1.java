package cn.szyrm.pattern.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyMethodInterceptor1 implements MethodInterceptor {
    private Object target;

    public MyMethodInterceptor1(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("cglib代理开始1.......");
        Object invoke = method.invoke(target, args);
        System.out.println("cglib代理结束.1......");
        return invoke;
    }
}
