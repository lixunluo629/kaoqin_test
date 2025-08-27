package org.springframework.data.convert;

import org.springframework.data.util.TypeInformation;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/convert/TypeMapper.class */
public interface TypeMapper<S> {
    TypeInformation<?> readType(S s);

    <T> TypeInformation<? extends T> readType(S s, TypeInformation<T> typeInformation);

    void writeType(Class<?> cls, S s);

    void writeType(TypeInformation<?> typeInformation, S s);
}
