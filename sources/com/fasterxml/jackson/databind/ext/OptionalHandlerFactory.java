package com.fasterxml.jackson.databind.ext;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.Serializable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/* loaded from: jackson-databind-2.11.2.jar:com/fasterxml/jackson/databind/ext/OptionalHandlerFactory.class */
public class OptionalHandlerFactory implements Serializable {
    private static final long serialVersionUID = 1;
    private static final String PACKAGE_PREFIX_JAVAX_XML = "javax.xml.";
    private static final String SERIALIZERS_FOR_JAVAX_XML = "com.fasterxml.jackson.databind.ext.CoreXMLSerializers";
    private static final String DESERIALIZERS_FOR_JAVAX_XML = "com.fasterxml.jackson.databind.ext.CoreXMLDeserializers";
    private static final String SERIALIZER_FOR_DOM_NODE = "com.fasterxml.jackson.databind.ext.DOMSerializer";
    private static final String DESERIALIZER_FOR_DOM_DOCUMENT = "com.fasterxml.jackson.databind.ext.DOMDeserializer$DocumentDeserializer";
    private static final String DESERIALIZER_FOR_DOM_NODE = "com.fasterxml.jackson.databind.ext.DOMDeserializer$NodeDeserializer";
    private static final Class<?> CLASS_DOM_NODE = Node.class;
    private static final Class<?> CLASS_DOM_DOCUMENT = Document.class;
    private static final Java7Handlers _jdk7Helper;
    public static final OptionalHandlerFactory instance;

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 7 */
    static {
        Java7Handlers x = null;
        try {
            x = Java7Handlers.instance();
        } catch (Throwable th) {
        }
        _jdk7Helper = x;
        instance = new OptionalHandlerFactory();
    }

    protected OptionalHandlerFactory() {
    }

    public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
        Object ob;
        JsonSerializer<?> ser;
        Class<?> rawType = type.getRawClass();
        if (CLASS_DOM_NODE != null && CLASS_DOM_NODE.isAssignableFrom(rawType)) {
            return (JsonSerializer) instantiate(SERIALIZER_FOR_DOM_NODE);
        }
        if (_jdk7Helper != null && (ser = _jdk7Helper.getSerializerForJavaNioFilePath(rawType)) != null) {
            return ser;
        }
        String className = rawType.getName();
        if ((!className.startsWith(PACKAGE_PREFIX_JAVAX_XML) && !hasSuperClassStartingWith(rawType, PACKAGE_PREFIX_JAVAX_XML)) || (ob = instantiate(SERIALIZERS_FOR_JAVAX_XML)) == null) {
            return null;
        }
        return ((Serializers) ob).findSerializer(config, type, beanDesc);
    }

    public JsonDeserializer<?> findDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        Object ob;
        JsonDeserializer<?> deser;
        Class<?> rawType = type.getRawClass();
        if (_jdk7Helper != null && (deser = _jdk7Helper.getDeserializerForJavaNioFilePath(rawType)) != null) {
            return deser;
        }
        if (CLASS_DOM_NODE != null && CLASS_DOM_NODE.isAssignableFrom(rawType)) {
            return (JsonDeserializer) instantiate(DESERIALIZER_FOR_DOM_NODE);
        }
        if (CLASS_DOM_DOCUMENT != null && CLASS_DOM_DOCUMENT.isAssignableFrom(rawType)) {
            return (JsonDeserializer) instantiate(DESERIALIZER_FOR_DOM_DOCUMENT);
        }
        String className = rawType.getName();
        if ((!className.startsWith(PACKAGE_PREFIX_JAVAX_XML) && !hasSuperClassStartingWith(rawType, PACKAGE_PREFIX_JAVAX_XML)) || (ob = instantiate(DESERIALIZERS_FOR_JAVAX_XML)) == null) {
            return null;
        }
        return ((Deserializers) ob).findBeanDeserializer(type, config, beanDesc);
    }

    public boolean hasDeserializerFor(Class<?> valueType) {
        if (CLASS_DOM_NODE != null && CLASS_DOM_NODE.isAssignableFrom(valueType)) {
            return true;
        }
        if (CLASS_DOM_DOCUMENT != null && CLASS_DOM_DOCUMENT.isAssignableFrom(valueType)) {
            return true;
        }
        String className = valueType.getName();
        if (className.startsWith(PACKAGE_PREFIX_JAVAX_XML) || hasSuperClassStartingWith(valueType, PACKAGE_PREFIX_JAVAX_XML)) {
            return true;
        }
        return false;
    }

    private Object instantiate(String className) {
        try {
            return ClassUtil.createInstance(Class.forName(className), false);
        } catch (Exception | LinkageError e) {
            return null;
        }
    }

    private boolean hasSuperClassStartingWith(Class<?> rawType, String prefix) {
        Class<?> superclass = rawType.getSuperclass();
        while (true) {
            Class<?> supertype = superclass;
            if (supertype == null || supertype == Object.class) {
                return false;
            }
            if (!supertype.getName().startsWith(prefix)) {
                superclass = supertype.getSuperclass();
            } else {
                return true;
            }
        }
    }
}
