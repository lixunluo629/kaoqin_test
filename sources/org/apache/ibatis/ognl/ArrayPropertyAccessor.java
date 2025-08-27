package org.apache.ibatis.ognl;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ArrayPropertyAccessor.class */
public class ArrayPropertyAccessor extends ObjectPropertyAccessor implements PropertyAccessor {
    @Override // org.apache.ibatis.ognl.ObjectPropertyAccessor, org.apache.ibatis.ognl.PropertyAccessor
    public Object getProperty(Map context, Object target, Object name) throws OgnlException, NegativeArraySizeException {
        Object result = null;
        if (name instanceof String) {
            if (name.equals("length")) {
                result = new Integer(Array.getLength(target));
            } else {
                result = super.getProperty(context, target, name);
            }
        } else {
            Object index = name;
            if (index instanceof DynamicSubscript) {
                int len = Array.getLength(target);
                switch (((DynamicSubscript) index).getFlag()) {
                    case 0:
                        index = new Integer(len > 0 ? 0 : -1);
                        break;
                    case 1:
                        index = new Integer(len > 0 ? len / 2 : -1);
                        break;
                    case 2:
                        index = new Integer(len > 0 ? len - 1 : -1);
                        break;
                    case 3:
                        result = Array.newInstance(target.getClass().getComponentType(), len);
                        System.arraycopy(target, 0, result, 0, len);
                        break;
                }
            }
            if (result == null) {
                if (index instanceof Number) {
                    int i = ((Number) index).intValue();
                    result = i >= 0 ? Array.get(target, i) : null;
                } else {
                    throw new NoSuchPropertyException(target, index);
                }
            }
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.ObjectPropertyAccessor, org.apache.ibatis.ognl.PropertyAccessor
    public void setProperty(Map context, Object target, Object name, Object value) throws IllegalAccessException, OgnlException, ArrayIndexOutOfBoundsException, IllegalArgumentException, InvocationTargetException {
        boolean isNumber = name instanceof Number;
        if (isNumber || (name instanceof DynamicSubscript)) {
            TypeConverter converter = ((OgnlContext) context).getTypeConverter();
            Object convertedValue = converter.convertValue(context, target, null, name.toString(), value, target.getClass().getComponentType());
            if (isNumber) {
                int i = ((Number) name).intValue();
                if (i >= 0) {
                    Array.set(target, i, convertedValue);
                    return;
                }
                return;
            }
            int len = Array.getLength(target);
            switch (((DynamicSubscript) name).getFlag()) {
                case 0:
                    new Integer(len > 0 ? 0 : -1);
                    return;
                case 1:
                    new Integer(len > 0 ? len / 2 : -1);
                    return;
                case 2:
                    new Integer(len > 0 ? len - 1 : -1);
                    return;
                case 3:
                    System.arraycopy(target, 0, convertedValue, 0, len);
                    return;
                default:
                    return;
            }
        }
        if (!(name instanceof String)) {
            throw new NoSuchPropertyException(target, name);
        }
        super.setProperty(context, target, name, value);
    }

    @Override // org.apache.ibatis.ognl.ObjectPropertyAccessor, org.apache.ibatis.ognl.PropertyAccessor
    public String getSourceAccessor(OgnlContext context, Object target, Object index) {
        String indexStr = index.toString();
        if (context.getCurrentType() != null && !context.getCurrentType().isPrimitive() && Number.class.isAssignableFrom(context.getCurrentType())) {
            indexStr = indexStr + "." + OgnlRuntime.getNumericValueGetter(context.getCurrentType());
        } else if (context.getCurrentObject() != null && Number.class.isAssignableFrom(context.getCurrentObject().getClass()) && !context.getCurrentType().isPrimitive()) {
            String toString = (!String.class.isInstance(index) || context.getCurrentType() == Object.class) ? ".toString()" : "";
            indexStr = "org.apache.ibatis.ognl.OgnlOps#getIntValue(" + indexStr + toString + ")";
        }
        context.setCurrentAccessor(target.getClass());
        context.setCurrentType(target.getClass().getComponentType());
        return org.springframework.beans.PropertyAccessor.PROPERTY_KEY_PREFIX + indexStr + "]";
    }

    @Override // org.apache.ibatis.ognl.ObjectPropertyAccessor, org.apache.ibatis.ognl.PropertyAccessor
    public String getSourceSetter(OgnlContext context, Object target, Object index) {
        String indexStr = index.toString();
        if (context.getCurrentType() != null && !context.getCurrentType().isPrimitive() && Number.class.isAssignableFrom(context.getCurrentType())) {
            indexStr = indexStr + "." + OgnlRuntime.getNumericValueGetter(context.getCurrentType());
        } else if (context.getCurrentObject() != null && Number.class.isAssignableFrom(context.getCurrentObject().getClass()) && !context.getCurrentType().isPrimitive()) {
            String toString = (!String.class.isInstance(index) || context.getCurrentType() == Object.class) ? ".toString()" : "";
            indexStr = "org.apache.ibatis.ognl.OgnlOps#getIntValue(" + indexStr + toString + ")";
        }
        Class type = target.getClass().isArray() ? target.getClass().getComponentType() : target.getClass();
        context.setCurrentAccessor(target.getClass());
        context.setCurrentType(target.getClass().getComponentType());
        if (type.isPrimitive()) {
            Class wrapClass = OgnlRuntime.getPrimitiveWrapperClass(type);
            return org.springframework.beans.PropertyAccessor.PROPERTY_KEY_PREFIX + indexStr + "]=((" + wrapClass.getName() + ")ognl.OgnlOps.convertValue($3," + wrapClass.getName() + ".class, true))." + OgnlRuntime.getNumericValueGetter(wrapClass);
        }
        return org.springframework.beans.PropertyAccessor.PROPERTY_KEY_PREFIX + indexStr + "]=ognl.OgnlOps.convertValue($3," + type.getName() + ".class)";
    }
}
