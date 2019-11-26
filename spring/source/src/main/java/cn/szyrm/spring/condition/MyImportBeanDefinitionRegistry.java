package cn.szyrm.spring.condition;


import cn.szyrm.spring.bean.Deer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public  class MyImportBeanDefinitionRegistry implements ImportBeanDefinitionRegistrar {
    /**
     * AnnotationMetadata:当前类的注解信息
     * BeanDefinitionRegistry：BeanDefinition 注册类
     *              把所有需要添加到容器中的bean通过调用BeanDefinitionRegistry.registryBeanDefinition来进行手工注册
     * @param importingClassMetadata
     * @param registry
     */
    @Override
     public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean definition = registry.containsBeanDefinition("cn.szyrm.spring.bean.Pig");
        boolean definition1 = registry.containsBeanDefinition("cn.szyrm.spring.bean.Horse");
        if(definition && definition1){
            RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Deer.class);
            registry.registerBeanDefinition("deer",rootBeanDefinition);
        }


    }
}
