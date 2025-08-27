package org.springframework.data.mapping.context;

import java.util.Collection;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.util.TypeInformation;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/MappingContext.class */
public interface MappingContext<E extends PersistentEntity<?, P>, P extends PersistentProperty<P>> {
    Collection<E> getPersistentEntities();

    E getPersistentEntity(Class<?> cls);

    boolean hasPersistentEntityFor(Class<?> cls);

    E getPersistentEntity(TypeInformation<?> typeInformation);

    E getPersistentEntity(P p);

    PersistentPropertyPath<P> getPersistentPropertyPath(PropertyPath propertyPath);

    PersistentPropertyPath<P> getPersistentPropertyPath(String str, Class<?> cls);

    PersistentPropertyPath<P> getPersistentPropertyPath(InvalidPersistentPropertyPath invalidPersistentPropertyPath);

    Collection<TypeInformation<?>> getManagedTypes();
}
