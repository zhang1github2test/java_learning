package cn.szyrm.pattern.proxy.cglib;

import net.sf.cglib.proxy.Dispatcher;

import java.io.Serializable;


public  class StaticDispatcher  implements Dispatcher, Serializable {


    private Object target;

    public StaticDispatcher(Object target) {
        this.target = target;
    }

    @Override

    public Object loadObject() {
        System.out.println("CGLIB静态代理开始....");
        return this.target;
    }
}

