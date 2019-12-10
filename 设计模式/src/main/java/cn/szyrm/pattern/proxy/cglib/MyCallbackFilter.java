package cn.szyrm.pattern.proxy.cglib;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

public class MyCallbackFilter implements CallbackFilter {
    @Override
    public int accept(Method method) {
        return 0;
    }
}
