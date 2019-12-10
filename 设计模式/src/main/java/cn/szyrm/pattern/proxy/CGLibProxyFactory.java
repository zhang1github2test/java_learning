package cn.szyrm.pattern.proxy;

import cn.szyrm.pattern.proxy.cglib.MyCallbackFilter;
import cn.szyrm.pattern.proxy.cglib.StaticDispatcher;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLibProxyFactory  implements MethodInterceptor {
    private Object target;

    public CGLibProxyFactory(Object target) {
        this.target = target;
    }

    /**
     *
     * @return
     */
   public Object getProxyInstance(){
       Enhancer enhancer = new Enhancer();
       enhancer.setSuperclass(target.getClass());

       Callback[] callback = new Callback []{this};

       enhancer.setCallbacks(callback);
       enhancer.setCallbackFilter(new MyCallbackFilter());

        return  enhancer.create();
    }

    public Object getProxyInstance2(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());

        Callback[] callback = new Callback []{new StaticDispatcher(target)};

        enhancer.setCallbacks(callback);
        enhancer.setCallbackFilter(new MyCallbackFilter());

        return  enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String name = method.getName();
        System.out.println(name);
        System.out.println("cglib代理开始.......");
        Object invoke = method.invoke(target, objects);
        System.out.println("cglib代理结束.......");
        return invoke;
    }
}
