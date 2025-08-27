package org.springframework.data.repository.core.support;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.springframework.data.annotation.Id;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/ReflectionEntityInformation.class */
public class ReflectionEntityInformation<T, ID extends Serializable> extends AbstractEntityInformation<T, ID> {
    private static final Class<Id> DEFAULT_ID_ANNOTATION = Id.class;
    private Field field;

    public ReflectionEntityInformation(Class<T> domainClass) {
        this(domainClass, DEFAULT_ID_ANNOTATION);
    }

    public ReflectionEntityInformation(Class<T> domainClass, final Class<? extends Annotation> annotation) throws IllegalArgumentException {
        super(domainClass);
        Assert.notNull(annotation, "Annotation must not be null!");
        ReflectionUtils.doWithFields(domainClass, new ReflectionUtils.FieldCallback() { // from class: org.springframework.data.repository.core.support.ReflectionEntityInformation.1
            @Override // org.springframework.util.ReflectionUtils.FieldCallback
            public void doWith(Field field) {
                if (field.getAnnotation(annotation) != null) {
                    ReflectionEntityInformation.this.field = field;
                }
            }
        });
        Assert.notNull(this.field, String.format("No field annotated with %s found!", annotation.toString()));
        ReflectionUtils.makeAccessible(this.field);
    }

    @Override // org.springframework.data.repository.core.EntityInformation
    public ID getId(Object entity) {
        if (entity == null) {
            return null;
        }
        return (ID) ReflectionUtils.getField(this.field, entity);
    }

    @Override // org.springframework.data.repository.core.EntityInformation
    public Class<ID> getIdType() {
        return (Class<ID>) this.field.getType();
    }
}
