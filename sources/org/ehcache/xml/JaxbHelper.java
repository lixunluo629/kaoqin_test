package org.ehcache.xml;

import java.lang.reflect.Field;
import javax.xml.bind.annotation.XmlElement;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/JaxbHelper.class */
final class JaxbHelper {
    JaxbHelper() {
    }

    public static String findDefaultValue(Object jaxbObject, String fieldName) throws NoSuchFieldException {
        Field declaredField = null;
        Class<?> clazz = jaxbObject.getClass();
        do {
            try {
                declaredField = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        } while (!clazz.equals(Object.class));
        if (declaredField == null) {
            throw new IllegalArgumentException("No such field '" + fieldName + "' in JAXB class " + jaxbObject.getClass().getName());
        }
        XmlElement annotation = declaredField.getAnnotation(XmlElement.class);
        if (annotation != null) {
            return annotation.defaultValue();
        }
        return null;
    }
}
