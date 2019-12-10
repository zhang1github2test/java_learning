package cn.szyrm.pattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理工厂类
 */
public class ProxyFactory {
    private Object target;

    public ProxyFactory(Object target) {
        this.target = target;
    }

    /**
     * ClassLoader loader,  指定当前木匾对象使用的类加载器
     * Class<?>[] interfaces, 目标对象实现的接口类型，使用范型方法确认类型
     * InvocationHandler h：事件处理，执行目标的方法时，会触发时间处理器的方法
     * @return
     */
    public Object getProxyInstance(){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("使用JDK动态代理...方法调用前");
                //利用反射调用目标对象的方法
                Object invoke = method.invoke(target, args);
                System.out.println("使用JDK动态代理...方法调用后");
                return invoke;
            }
        });
    }
}
