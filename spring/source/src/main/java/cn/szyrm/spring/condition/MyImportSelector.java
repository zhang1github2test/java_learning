package cn.szyrm.spring.condition;

import cn.szyrm.spring.bean.Horse;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{Horse.class.getName()};
    }
}
