package org.springframework.data.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/TypeInformation.class */
public interface TypeInformation<S> {
    List<TypeInformation<?>> getParameterTypes(Constructor<?> constructor);

    TypeInformation<?> getProperty(String str);

    boolean isCollectionLike();

    TypeInformation<?> getComponentType();

    boolean isMap();

    TypeInformation<?> getMapValueType();

    Class<S> getType();

    ClassTypeInformation<?> getRawTypeInformation();

    TypeInformation<?> getActualType();

    TypeInformation<?> getReturnType(Method method);

    List<TypeInformation<?>> getParameterTypes(Method method);

    TypeInformation<?> getSuperTypeInformation(Class<?> cls);

    boolean isAssignableFrom(TypeInformation<?> typeInformation);

    List<TypeInformation<?>> getTypeArguments();

    TypeInformation<?> specialize(ClassTypeInformation<?> classTypeInformation);
}
