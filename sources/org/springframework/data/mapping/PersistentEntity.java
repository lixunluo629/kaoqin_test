package org.springframework.data.mapping;

import java.lang.annotation.Annotation;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.util.TypeInformation;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/PersistentEntity.class */
public interface PersistentEntity<T, P extends PersistentProperty<P>> {
    String getName();

    PreferredConstructor<T, P> getPersistenceConstructor();

    boolean isConstructorArgument(PersistentProperty<?> persistentProperty);

    boolean isIdProperty(PersistentProperty<?> persistentProperty);

    boolean isVersionProperty(PersistentProperty<?> persistentProperty);

    P getIdProperty();

    P getVersionProperty();

    P getPersistentProperty(String str);

    P getPersistentProperty(Class<? extends Annotation> cls);

    boolean hasIdProperty();

    boolean hasVersionProperty();

    Class<T> getType();

    Object getTypeAlias();

    TypeInformation<T> getTypeInformation();

    void doWithProperties(PropertyHandler<P> propertyHandler);

    void doWithProperties(SimplePropertyHandler simplePropertyHandler);

    void doWithAssociations(AssociationHandler<P> associationHandler);

    void doWithAssociations(SimpleAssociationHandler simpleAssociationHandler);

    <A extends Annotation> A findAnnotation(Class<A> cls);

    PersistentPropertyAccessor getPropertyAccessor(Object obj);

    IdentifierAccessor getIdentifierAccessor(Object obj);
}
