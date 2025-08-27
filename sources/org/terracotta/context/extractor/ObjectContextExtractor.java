package org.terracotta.context.extractor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.terracotta.context.ContextElement;
import org.terracotta.context.annotations.ContextAttribute;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extractor/ObjectContextExtractor.class */
public final class ObjectContextExtractor {
    private ObjectContextExtractor() {
    }

    public static ContextElement extract(Object from) {
        Map<String, AttributeGetter<? extends Object>> attributes = new HashMap<>();
        attributes.putAll(extractInstanceAttribute(from));
        attributes.putAll(extractMethodAttributes(from));
        attributes.putAll(extractFieldAttributes(from));
        return new LazyContextElement(from.getClass(), attributes);
    }

    private static Map<? extends String, ? extends AttributeGetter<? extends Object>> extractInstanceAttribute(Object from) {
        ContextAttribute annotation = (ContextAttribute) from.getClass().getAnnotation(ContextAttribute.class);
        if (annotation == null) {
            return Collections.emptyMap();
        }
        return Collections.singletonMap(annotation.value(), new WeakAttributeGetter(from));
    }

    private static Map<String, AttributeGetter<? extends Object>> extractMethodAttributes(Object from) throws SecurityException {
        ContextAttribute annotation;
        Map<String, AttributeGetter<? extends Object>> attributes = new HashMap<>();
        Method[] arr$ = from.getClass().getMethods();
        for (Method m : arr$) {
            if (m.getParameterTypes().length == 0 && m.getReturnType() != Void.TYPE && (annotation = (ContextAttribute) m.getAnnotation(ContextAttribute.class)) != null) {
                attributes.put(annotation.value(), new WeakMethodAttributeGetter<>(from, m));
            }
        }
        return attributes;
    }

    private static Map<String, AttributeGetter<? extends Object>> extractFieldAttributes(Object from) {
        Map<String, AttributeGetter<? extends Object>> attributes = new HashMap<>();
        Class superclass = from.getClass();
        while (true) {
            Class c = superclass;
            if (c != null) {
                Field[] arr$ = c.getDeclaredFields();
                for (Field f : arr$) {
                    ContextAttribute annotation = (ContextAttribute) f.getAnnotation(ContextAttribute.class);
                    if (annotation != null) {
                        attributes.put(annotation.value(), createFieldAttributeGetter(from, f));
                    }
                }
                superclass = c.getSuperclass();
            } else {
                return attributes;
            }
        }
    }

    private static AttributeGetter<? extends Object> createFieldAttributeGetter(Object from, Field f) {
        f.setAccessible(true);
        if (Modifier.isFinal(f.getModifiers())) {
            try {
                return new DirectAttributeGetter(f.get(from));
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalArgumentException ex2) {
                throw new RuntimeException(ex2);
            }
        }
        return new WeakFieldAttributeGetter(from, f);
    }
}
