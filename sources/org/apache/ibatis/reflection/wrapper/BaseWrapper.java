package org.apache.ibatis.reflection.wrapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.property.PropertyTokenizer;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/wrapper/BaseWrapper.class */
public abstract class BaseWrapper implements ObjectWrapper {
    protected static final Object[] NO_ARGUMENTS = new Object[0];
    protected final MetaObject metaObject;

    protected BaseWrapper(MetaObject metaObject) {
        this.metaObject = metaObject;
    }

    protected Object resolveCollection(PropertyTokenizer prop, Object object) {
        if ("".equals(prop.getName())) {
            return object;
        }
        return this.metaObject.getValue(prop.getName());
    }

    protected Object getCollectionValue(PropertyTokenizer prop, Object collection) throws NumberFormatException {
        if (collection instanceof Map) {
            return ((Map) collection).get(prop.getIndex());
        }
        int i = Integer.parseInt(prop.getIndex());
        if (collection instanceof List) {
            return ((List) collection).get(i);
        }
        if (collection instanceof Object[]) {
            return ((Object[]) collection)[i];
        }
        if (collection instanceof char[]) {
            return Character.valueOf(((char[]) collection)[i]);
        }
        if (collection instanceof boolean[]) {
            return Boolean.valueOf(((boolean[]) collection)[i]);
        }
        if (collection instanceof byte[]) {
            return Byte.valueOf(((byte[]) collection)[i]);
        }
        if (collection instanceof double[]) {
            return Double.valueOf(((double[]) collection)[i]);
        }
        if (collection instanceof float[]) {
            return Float.valueOf(((float[]) collection)[i]);
        }
        if (collection instanceof int[]) {
            return Integer.valueOf(((int[]) collection)[i]);
        }
        if (collection instanceof long[]) {
            return Long.valueOf(((long[]) collection)[i]);
        }
        if (collection instanceof short[]) {
            return Short.valueOf(((short[]) collection)[i]);
        }
        throw new ReflectionException("The '" + prop.getName() + "' property of " + collection + " is not a List or Array.");
    }

    protected void setCollectionValue(PropertyTokenizer prop, Object collection, Object value) throws NumberFormatException {
        if (collection instanceof Map) {
            ((Map) collection).put(prop.getIndex(), value);
            return;
        }
        int i = Integer.parseInt(prop.getIndex());
        if (collection instanceof List) {
            ((List) collection).set(i, value);
            return;
        }
        if (collection instanceof Object[]) {
            ((Object[]) collection)[i] = value;
            return;
        }
        if (collection instanceof char[]) {
            ((char[]) collection)[i] = ((Character) value).charValue();
            return;
        }
        if (collection instanceof boolean[]) {
            ((boolean[]) collection)[i] = ((Boolean) value).booleanValue();
            return;
        }
        if (collection instanceof byte[]) {
            ((byte[]) collection)[i] = ((Byte) value).byteValue();
            return;
        }
        if (collection instanceof double[]) {
            ((double[]) collection)[i] = ((Double) value).doubleValue();
            return;
        }
        if (collection instanceof float[]) {
            ((float[]) collection)[i] = ((Float) value).floatValue();
            return;
        }
        if (collection instanceof int[]) {
            ((int[]) collection)[i] = ((Integer) value).intValue();
        } else if (collection instanceof long[]) {
            ((long[]) collection)[i] = ((Long) value).longValue();
        } else {
            if (collection instanceof short[]) {
                ((short[]) collection)[i] = ((Short) value).shortValue();
                return;
            }
            throw new ReflectionException("The '" + prop.getName() + "' property of " + collection + " is not a List or Array.");
        }
    }
}
