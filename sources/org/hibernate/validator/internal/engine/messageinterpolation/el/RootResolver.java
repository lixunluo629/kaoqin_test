package org.hibernate.validator.internal.engine.messageinterpolation.el;

import java.beans.FeatureDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.validation.ValidationException;
import org.hibernate.validator.internal.engine.messageinterpolation.FormatterWrapper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/engine/messageinterpolation/el/RootResolver.class */
public class RootResolver extends ELResolver {
    public static final String FORMATTER = "formatter";
    private static final String FORMAT = "format";
    private final Map<String, Object> map = Collections.synchronizedMap(new HashMap());

    @Override // javax.el.ELResolver
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base == null) {
            return String.class;
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    @Override // javax.el.ELResolver
    public Class<?> getType(ELContext context, Object base, Object property) {
        if (resolve(context, base, property)) {
            return Object.class;
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public Object getValue(ELContext context, Object base, Object property) {
        if (resolve(context, base, property)) {
            if (!isProperty((String) property)) {
                throw new PropertyNotFoundException("Cannot find property " + property);
            }
            return getProperty((String) property);
        }
        return null;
    }

    @Override // javax.el.ELResolver
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return false;
    }

    @Override // javax.el.ELResolver
    public void setValue(ELContext context, Object base, Object property, Object value) {
        if (resolve(context, base, property)) {
            setProperty((String) property, value);
        }
    }

    @Override // javax.el.ELResolver
    public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params) {
        if (resolve(context, base, method)) {
            throw new ValidationException("Invalid property");
        }
        Object returnValue = null;
        if (base instanceof FormatterWrapper) {
            returnValue = evaluateFormatExpression(context, method, params);
        }
        return returnValue;
    }

    private Object evaluateFormatExpression(ELContext context, Object method, Object[] params) {
        if (!"format".equals(method)) {
            throw new ELException("Wrong method name 'formatter#" + method + "' does not exist. Only formatter#format is supported.");
        }
        if (params.length == 0) {
            throw new ELException("Invalid number of arguments to Formatter#format");
        }
        if (!(params[0] instanceof String)) {
            throw new ELException("The first argument to Formatter#format must be String");
        }
        FormatterWrapper formatterWrapper = (FormatterWrapper) context.getVariableMapper().resolveVariable(FORMATTER).getValue(context);
        Object[] formattingParameters = new Object[params.length - 1];
        System.arraycopy(params, 1, formattingParameters, 0, params.length - 1);
        try {
            Object returnValue = formatterWrapper.format((String) params[0], formattingParameters);
            context.setPropertyResolved(true);
            return returnValue;
        } catch (IllegalFormatException e) {
            throw new ELException("Error in Formatter#format call", e);
        }
    }

    private Object getProperty(String property) {
        return this.map.get(property);
    }

    private void setProperty(String property, Object value) {
        this.map.put(property, value);
    }

    private boolean isProperty(String property) {
        return this.map.containsKey(property);
    }

    private boolean resolve(ELContext context, Object base, Object property) {
        context.setPropertyResolved(base == null && (property instanceof String));
        return context.isPropertyResolved();
    }
}
