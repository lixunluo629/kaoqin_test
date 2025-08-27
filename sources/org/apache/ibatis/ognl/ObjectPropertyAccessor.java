package org.apache.ibatis.ognl;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.apache.ibatis.ognl.enhance.ExpressionCompiler;
import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/ObjectPropertyAccessor.class */
public class ObjectPropertyAccessor implements PropertyAccessor {
    public Object getPossibleProperty(Map context, Object target, String name) throws OgnlException {
        OgnlContext ognlContext = (OgnlContext) context;
        try {
            Object methodValue = OgnlRuntime.getMethodValue(ognlContext, target, name, true);
            Object result = methodValue;
            if (methodValue == OgnlRuntime.NotFound) {
                result = OgnlRuntime.getFieldValue(ognlContext, target, name, true);
            }
            return result;
        } catch (Exception ex) {
            throw new OgnlException(name, ex);
        } catch (IntrospectionException ex2) {
            throw new OgnlException(name, ex2);
        } catch (OgnlException ex3) {
            throw ex3;
        }
    }

    public Object setPossibleProperty(Map context, Object target, String name, Object value) throws IllegalAccessException, OgnlException, IllegalArgumentException, InvocationTargetException {
        Method m;
        Object result = null;
        OgnlContext ognlContext = (OgnlContext) context;
        try {
            if (!OgnlRuntime.setMethodValue(ognlContext, target, name, value, true)) {
                result = OgnlRuntime.setFieldValue(ognlContext, target, name, value) ? null : OgnlRuntime.NotFound;
            }
            if (result == OgnlRuntime.NotFound && (m = OgnlRuntime.getWriteMethod(target.getClass(), name)) != null) {
                result = m.invoke(target, value);
            }
            return result;
        } catch (OgnlException ex) {
            throw ex;
        } catch (IntrospectionException ex2) {
            throw new OgnlException(name, ex2);
        } catch (Exception ex3) {
            throw new OgnlException(name, ex3);
        }
    }

    public boolean hasGetProperty(OgnlContext context, Object target, Object oname) throws OgnlException {
        try {
            return OgnlRuntime.hasGetProperty(context, target, oname);
        } catch (IntrospectionException ex) {
            throw new OgnlException("checking if " + target + " has gettable property " + oname, ex);
        }
    }

    public boolean hasGetProperty(Map context, Object target, Object oname) throws OgnlException {
        return hasGetProperty((OgnlContext) context, target, oname);
    }

    public boolean hasSetProperty(OgnlContext context, Object target, Object oname) throws OgnlException {
        try {
            return OgnlRuntime.hasSetProperty(context, target, oname);
        } catch (IntrospectionException ex) {
            throw new OgnlException("checking if " + target + " has settable property " + oname, ex);
        }
    }

    public boolean hasSetProperty(Map context, Object target, Object oname) throws OgnlException {
        return hasSetProperty((OgnlContext) context, target, oname);
    }

    @Override // org.apache.ibatis.ognl.PropertyAccessor
    public Object getProperty(Map context, Object target, Object oname) throws OgnlException {
        String name = oname.toString();
        Object result = getPossibleProperty(context, target, name);
        if (result == OgnlRuntime.NotFound) {
            throw new NoSuchPropertyException(target, name);
        }
        return result;
    }

    @Override // org.apache.ibatis.ognl.PropertyAccessor
    public void setProperty(Map context, Object target, Object oname, Object value) throws IllegalAccessException, OgnlException, IllegalArgumentException, InvocationTargetException {
        String name = oname.toString();
        Object result = setPossibleProperty(context, target, name, value);
        if (result == OgnlRuntime.NotFound) {
            throw new NoSuchPropertyException(target, name);
        }
    }

    public Class getPropertyClass(OgnlContext context, Object target, Object index) {
        try {
            Method m = OgnlRuntime.getReadMethod(target.getClass(), index.toString());
            if (m == null) {
                if (String.class.isAssignableFrom(index.getClass()) && !target.getClass().isArray()) {
                    String indexStr = (String) index;
                    String key = indexStr.indexOf(34) >= 0 ? indexStr.replaceAll(SymbolConstants.QUOTES_SYMBOL, "") : indexStr;
                    try {
                        Field f = target.getClass().getField(key);
                        if (f != null) {
                            return f.getType();
                        }
                        return null;
                    } catch (NoSuchFieldException e) {
                        return null;
                    }
                }
                return null;
            }
            return m.getReturnType();
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    @Override // org.apache.ibatis.ognl.PropertyAccessor
    public String getSourceAccessor(OgnlContext context, Object target, Object index) {
        Field f;
        try {
            String indexStr = index.toString();
            String methodName = indexStr.indexOf(34) >= 0 ? indexStr.replaceAll(SymbolConstants.QUOTES_SYMBOL, "") : indexStr;
            Method m = OgnlRuntime.getReadMethod(target.getClass(), methodName);
            if (m == null && context.getCurrentObject() != null) {
                String currentObjectStr = context.getCurrentObject().toString();
                m = OgnlRuntime.getReadMethod(target.getClass(), currentObjectStr.indexOf(34) >= 0 ? currentObjectStr.replaceAll(SymbolConstants.QUOTES_SYMBOL, "") : currentObjectStr);
            }
            if (m == null) {
                try {
                    if (String.class.isAssignableFrom(index.getClass()) && !target.getClass().isArray() && (f = target.getClass().getField(methodName)) != null) {
                        context.setCurrentType(f.getType());
                        context.setCurrentAccessor(f.getDeclaringClass());
                        return "." + f.getName();
                    }
                    return "";
                } catch (NoSuchFieldException e) {
                    return "";
                }
            }
            context.setCurrentType(m.getReturnType());
            context.setCurrentAccessor(OgnlRuntime.getCompiler().getSuperOrInterfaceClass(m, m.getDeclaringClass()));
            return "." + m.getName() + "()";
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }

    @Override // org.apache.ibatis.ognl.PropertyAccessor
    public String getSourceSetter(OgnlContext context, Object target, Object index) {
        String conversion;
        try {
            String indexStr = index.toString();
            String methodName = indexStr.indexOf(34) >= 0 ? indexStr.replaceAll(SymbolConstants.QUOTES_SYMBOL, "") : indexStr;
            Method m = OgnlRuntime.getWriteMethod(target.getClass(), methodName);
            if (m == null && context.getCurrentObject() != null && context.getCurrentObject().toString() != null) {
                String currentObjectStr = context.getCurrentObject().toString();
                m = OgnlRuntime.getWriteMethod(target.getClass(), currentObjectStr.indexOf(34) >= 0 ? currentObjectStr.replaceAll(SymbolConstants.QUOTES_SYMBOL, "") : currentObjectStr);
            }
            if (m == null || m.getParameterTypes() == null || m.getParameterTypes().length <= 0) {
                throw new UnsupportedCompilationException("Unable to determine setting expression on " + context.getCurrentObject() + " with index of " + index);
            }
            Class parm = m.getParameterTypes()[0];
            if (m.getParameterTypes().length > 1) {
                throw new UnsupportedCompilationException("Object property accessors can only support single parameter setters.");
            }
            if (parm.isPrimitive()) {
                Class wrapClass = OgnlRuntime.getPrimitiveWrapperClass(parm);
                conversion = OgnlRuntime.getCompiler().createLocalReference(context, "((" + wrapClass.getName() + ")ognl.OgnlOps#convertValue($3," + wrapClass.getName() + ".class, true))." + OgnlRuntime.getNumericValueGetter(wrapClass), parm);
            } else if (parm.isArray()) {
                conversion = OgnlRuntime.getCompiler().createLocalReference(context, "(" + ExpressionCompiler.getCastString(parm) + ")ognl.OgnlOps#toArray($3," + parm.getComponentType().getName() + ".class)", parm);
            } else {
                conversion = OgnlRuntime.getCompiler().createLocalReference(context, "(" + parm.getName() + ")ognl.OgnlOps#convertValue($3," + parm.getName() + ".class)", parm);
            }
            context.setCurrentType(m.getReturnType());
            context.setCurrentAccessor(OgnlRuntime.getCompiler().getSuperOrInterfaceClass(m, m.getDeclaringClass()));
            return "." + m.getName() + "(" + conversion + ")";
        } catch (Throwable t) {
            throw OgnlOps.castToRuntime(t);
        }
    }
}
