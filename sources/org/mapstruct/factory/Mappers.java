package org.mapstruct.factory;

/* loaded from: mapstruct-1.0.0.CR1.jar:org/mapstruct/factory/Mappers.class */
public class Mappers {
    private static final String IMPLEMENTATION_SUFFIX = "Impl";

    private Mappers() {
    }

    public static <T> T getMapper(Class<T> cls) {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader == null) {
                contextClassLoader = Mappers.class.getClassLoader();
            }
            return (T) contextClassLoader.loadClass(cls.getName() + "Impl").newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
