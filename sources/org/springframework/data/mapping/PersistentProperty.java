package org.springframework.data.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.util.TypeInformation;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/PersistentProperty.class */
public interface PersistentProperty<P extends PersistentProperty<P>> {
    PersistentEntity<?, P> getOwner();

    String getName();

    Class<?> getType();

    TypeInformation<?> getTypeInformation();

    Iterable<? extends TypeInformation<?>> getPersistentEntityType();

    Method getGetter();

    Method getSetter();

    Field getField();

    String getSpelExpression();

    Association<P> getAssociation();

    boolean isEntity();

    boolean isIdProperty();

    boolean isVersionProperty();

    boolean isCollectionLike();

    boolean isMap();

    boolean isArray();

    boolean isTransient();

    boolean isWritable();

    boolean isAssociation();

    Class<?> getComponentType();

    Class<?> getRawType();

    Class<?> getMapValueType();

    Class<?> getActualType();

    <A extends Annotation> A findAnnotation(Class<A> cls);

    <A extends Annotation> A findPropertyOrOwnerAnnotation(Class<A> cls);

    boolean isAnnotationPresent(Class<? extends Annotation> cls);

    boolean usePropertyAccess();
}
