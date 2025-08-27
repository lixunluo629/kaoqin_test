package org.springframework.data.mapping.model;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentPropertyAccessor;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/BeanWrapperPropertyAccessorFactory.class */
enum BeanWrapperPropertyAccessorFactory implements PersistentPropertyAccessorFactory {
    INSTANCE;

    @Override // org.springframework.data.mapping.model.PersistentPropertyAccessorFactory
    public PersistentPropertyAccessor getPropertyAccessor(PersistentEntity<?, ?> entity, Object bean) {
        return new BeanWrapper(bean);
    }

    @Override // org.springframework.data.mapping.model.PersistentPropertyAccessorFactory
    public boolean isSupported(PersistentEntity<?, ?> entity) {
        return true;
    }
}
