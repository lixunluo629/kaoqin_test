package org.springframework.plugin.core.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

/* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/support/BeanListFactoryBean.class */
public class BeanListFactoryBean<T> extends AbstractTypeAwareSupport<T> implements FactoryBean<List<T>> {
    private static final Comparator<Object> COMPARATOR = new AnnotationAwareOrderComparator();

    @Override // org.springframework.beans.factory.FactoryBean
    public List<T> getObject() {
        List<T> beans = new ArrayList<>();
        beans.addAll(getBeans());
        Collections.sort(beans, COMPARATOR);
        return beans;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return List.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }
}
