package org.apache.ibatis.ognl;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.web.servlet.tags.form.InputTag;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ListPropertyAccessor.class */
public class ListPropertyAccessor extends ObjectPropertyAccessor implements PropertyAccessor {
    @Override // org.apache.ibatis.ognl.ObjectPropertyAccessor, org.apache.ibatis.ognl.PropertyAccessor
    public Object getProperty(Map context, Object target, Object name) throws OgnlException {
        Object result;
        List list = (List) target;
        if (name instanceof String) {
            if (name.equals(InputTag.SIZE_ATTRIBUTE)) {
                result = new Integer(list.size());
            } else if (name.equals("iterator")) {
                result = list.iterator();
            } else if (name.equals("isEmpty") || name.equals("empty")) {
                result = list.isEmpty() ? Boolean.TRUE : Boolean.FALSE;
            } else {
                result = super.getProperty(context, target, name);
            }
            return result;
        }
        if (name instanceof Number) {
            return list.get(((Number) name).intValue());
        }
        if (name instanceof DynamicSubscript) {
            int len = list.size();
            switch (((DynamicSubscript) name).getFlag()) {
                case 0:
                    if (len > 0) {
                        return list.get(0);
                    }
                    return null;
                case 1:
                    if (len > 0) {
                        return list.get(len / 2);
                    }
                    return null;
                case 2:
                    if (len > 0) {
                        return list.get(len - 1);
                    }
                    return null;
                case 3:
                    return new ArrayList(list);
            }
        }
        throw new NoSuchPropertyException(target, name);
    }

    @Override // org.apache.ibatis.ognl.ObjectPropertyAccessor, org.apache.ibatis.ognl.PropertyAccessor
    public void setProperty(Map context, Object target, Object name, Object value) throws IllegalAccessException, OgnlException, IllegalArgumentException, InvocationTargetException {
        if ((name instanceof String) && ((String) name).indexOf(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) < 0) {
            super.setProperty(context, target, name, value);
            return;
        }
        List list = (List) target;
        if (name instanceof Number) {
            list.set(((Number) name).intValue(), value);
            return;
        }
        if (name instanceof DynamicSubscript) {
            int len = list.size();
            switch (((DynamicSubscript) name).getFlag()) {
                case 0:
                    if (len > 0) {
                        list.set(0, value);
                        return;
                    }
                    return;
                case 1:
                    if (len > 0) {
                        list.set(len / 2, value);
                        return;
                    }
                    return;
                case 2:
                    if (len > 0) {
                        list.set(len - 1, value);
                        return;
                    }
                    return;
                case 3:
                    if (!(value instanceof Collection)) {
                        throw new OgnlException("Value must be a collection");
                    }
                    list.clear();
                    list.addAll((Collection) value);
                    return;
            }
        }
        throw new NoSuchPropertyException(target, name);
    }

    @Override // org.apache.ibatis.ognl.ObjectPropertyAccessor
    public Class getPropertyClass(OgnlContext context, Object target, Object index) {
        if (index instanceof String) {
            String indexStr = (String) index;
            String key = indexStr.indexOf(34) >= 0 ? indexStr.replaceAll(SymbolConstants.QUOTES_SYMBOL, "") : indexStr;
            if (key.equals(InputTag.SIZE_ATTRIBUTE)) {
                return Integer.TYPE;
            }
            if (key.equals("iterator")) {
                return Iterator.class;
            }
            if (key.equals("isEmpty") || key.equals("empty")) {
                return Boolean.TYPE;
            }
            return super.getPropertyClass(context, target, index);
        }
        if (index instanceof Number) {
            return Object.class;
        }
        return null;
    }

    @Override // org.apache.ibatis.ognl.ObjectPropertyAccessor, org.apache.ibatis.ognl.PropertyAccessor
    public String getSourceAccessor(OgnlContext context, Object target, Object index) {
        String indexStr = index.toString();
        if (indexStr.indexOf(34) >= 0) {
            indexStr = indexStr.replaceAll(SymbolConstants.QUOTES_SYMBOL, "");
        }
        if (String.class.isInstance(index)) {
            if (indexStr.equals(InputTag.SIZE_ATTRIBUTE)) {
                context.setCurrentAccessor(List.class);
                context.setCurrentType(Integer.TYPE);
                return ".size()";
            }
            if (indexStr.equals("iterator")) {
                context.setCurrentAccessor(List.class);
                context.setCurrentType(Iterator.class);
                return ".iterator()";
            }
            if (indexStr.equals("isEmpty") || indexStr.equals("empty")) {
                context.setCurrentAccessor(List.class);
                context.setCurrentType(Boolean.TYPE);
                return ".isEmpty()";
            }
        }
        if (context.getCurrentObject() != null && !Number.class.isInstance(context.getCurrentObject())) {
            try {
                Method m = OgnlRuntime.getReadMethod(target.getClass(), indexStr);
                if (m != null) {
                    return super.getSourceAccessor(context, target, index);
                }
            } catch (Throwable t) {
                throw OgnlOps.castToRuntime(t);
            }
        }
        context.setCurrentAccessor(List.class);
        if (!context.getCurrentType().isPrimitive() && Number.class.isAssignableFrom(context.getCurrentType())) {
            indexStr = indexStr + "." + OgnlRuntime.getNumericValueGetter(context.getCurrentType());
        } else if (context.getCurrentObject() != null && Number.class.isAssignableFrom(context.getCurrentObject().getClass()) && !context.getCurrentType().isPrimitive()) {
            String toString = (!String.class.isInstance(index) || context.getCurrentType() == Object.class) ? ".toString()" : "";
            indexStr = "org.apache.ibatis.ognl.OgnlOps#getIntValue(" + indexStr + toString + ")";
        }
        context.setCurrentType(Object.class);
        return ".get(" + indexStr + ")";
    }

    @Override // org.apache.ibatis.ognl.ObjectPropertyAccessor, org.apache.ibatis.ognl.PropertyAccessor
    public String getSourceSetter(OgnlContext context, Object target, Object index) {
        String indexStr = index.toString();
        if (indexStr.indexOf(34) >= 0) {
            indexStr = indexStr.replaceAll(SymbolConstants.QUOTES_SYMBOL, "");
        }
        if (context.getCurrentObject() != null && !Number.class.isInstance(context.getCurrentObject())) {
            try {
                Method m = OgnlRuntime.getWriteMethod(target.getClass(), indexStr);
                if (m != null || !context.getCurrentType().isPrimitive()) {
                    System.out.println("super source setter returned: " + super.getSourceSetter(context, target, index));
                    return super.getSourceSetter(context, target, index);
                }
            } catch (Throwable t) {
                throw OgnlOps.castToRuntime(t);
            }
        }
        context.setCurrentAccessor(List.class);
        if (!context.getCurrentType().isPrimitive() && Number.class.isAssignableFrom(context.getCurrentType())) {
            indexStr = indexStr + "." + OgnlRuntime.getNumericValueGetter(context.getCurrentType());
        } else if (context.getCurrentObject() != null && Number.class.isAssignableFrom(context.getCurrentObject().getClass()) && !context.getCurrentType().isPrimitive()) {
            String toString = (!String.class.isInstance(index) || context.getCurrentType() == Object.class) ? ".toString()" : "";
            indexStr = "org.apache.ibatis.ognl.OgnlOps#getIntValue(" + indexStr + toString + ")";
        }
        context.setCurrentType(Object.class);
        return ".set(" + indexStr + ", $3)";
    }
}
