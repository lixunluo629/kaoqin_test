package org.apache.ibatis.reflection.property;

import java.lang.reflect.Field;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/property/PropertyCopier.class */
public final class PropertyCopier {
    private PropertyCopier() {
    }

    public static void copyBeanProperties(Class<?> type, Object sourceBean, Object destinationBean) throws IllegalAccessException, IllegalArgumentException {
        Class<?> superclass = type;
        while (true) {
            Class<?> parent = superclass;
            if (parent != null) {
                Field[] fields = parent.getDeclaredFields();
                for (Field field : fields) {
                    try {
                        field.setAccessible(true);
                        field.set(destinationBean, field.get(sourceBean));
                    } catch (Exception e) {
                    }
                }
                superclass = parent.getSuperclass();
            } else {
                return;
            }
        }
    }
}
