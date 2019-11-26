package cn.szyrm.spring.bean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public class BearFactoryBean implements FactoryBean<Bear> {
    @Override
    public Bear getObject() throws Exception {
        return new Bear();
    }

    @Override
    public Class<?> getObjectType() {
        return Bear.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
