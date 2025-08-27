package org.springframework.data.mapping.context;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.PersistentProperty;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/context/PersistentPropertyPath.class */
public interface PersistentPropertyPath<T extends PersistentProperty<T>> extends Iterable<T> {
    String toDotPath();

    String toDotPath(Converter<? super T, String> converter);

    String toPath(String str);

    String toPath(String str, Converter<? super T, String> converter);

    T getLeafProperty();

    T getBaseProperty();

    boolean isBasePathOf(PersistentPropertyPath<T> persistentPropertyPath);

    PersistentPropertyPath<T> getExtensionForBaseOf(PersistentPropertyPath<T> persistentPropertyPath);

    PersistentPropertyPath<T> getParentPath();

    int getLength();

    boolean isEmpty();
}
