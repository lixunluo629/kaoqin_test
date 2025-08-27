package org.springframework.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import org.springframework.beans.AbstractNestablePropertyAccessor;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.Assert;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/BeanWrapperImpl.class */
public class BeanWrapperImpl extends AbstractNestablePropertyAccessor implements BeanWrapper {
    private CachedIntrospectionResults cachedIntrospectionResults;
    private AccessControlContext acc;

    public BeanWrapperImpl() {
        this(true);
    }

    public BeanWrapperImpl(boolean registerDefaultEditors) {
        super(registerDefaultEditors);
    }

    public BeanWrapperImpl(Object object) {
        super(object);
    }

    public BeanWrapperImpl(Class<?> clazz) {
        super(clazz);
    }

    public BeanWrapperImpl(Object object, String nestedPath, Object rootObject) {
        super(object, nestedPath, rootObject);
    }

    private BeanWrapperImpl(Object object, String nestedPath, BeanWrapperImpl parent) {
        super(object, nestedPath, (AbstractNestablePropertyAccessor) parent);
        setSecurityContext(parent.acc);
    }

    public void setBeanInstance(Object object) {
        this.wrappedObject = object;
        this.rootObject = object;
        this.typeConverterDelegate = new TypeConverterDelegate(this, this.wrappedObject);
        setIntrospectionClass(object.getClass());
    }

    @Override // org.springframework.beans.AbstractNestablePropertyAccessor
    public void setWrappedInstance(Object object, String nestedPath, Object rootObject) {
        super.setWrappedInstance(object, nestedPath, rootObject);
        setIntrospectionClass(getWrappedClass());
    }

    protected void setIntrospectionClass(Class<?> clazz) {
        if (this.cachedIntrospectionResults != null && this.cachedIntrospectionResults.getBeanClass() != clazz) {
            this.cachedIntrospectionResults = null;
        }
    }

    private CachedIntrospectionResults getCachedIntrospectionResults() {
        Assert.state(getWrappedInstance() != null, "BeanWrapper does not hold a bean instance");
        if (this.cachedIntrospectionResults == null) {
            this.cachedIntrospectionResults = CachedIntrospectionResults.forClass(getWrappedClass());
        }
        return this.cachedIntrospectionResults;
    }

    public void setSecurityContext(AccessControlContext acc) {
        this.acc = acc;
    }

    public AccessControlContext getSecurityContext() {
        return this.acc;
    }

    public Object convertForProperty(Object value, String propertyName) throws TypeMismatchException {
        CachedIntrospectionResults cachedIntrospectionResults = getCachedIntrospectionResults();
        PropertyDescriptor pd = cachedIntrospectionResults.getPropertyDescriptor(propertyName);
        if (pd == null) {
            throw new InvalidPropertyException(getRootClass(), getNestedPath() + propertyName, "No property '" + propertyName + "' found");
        }
        TypeDescriptor td = cachedIntrospectionResults.getTypeDescriptor(pd);
        if (td == null) {
            td = cachedIntrospectionResults.addTypeDescriptor(pd, new TypeDescriptor(property(pd)));
        }
        return convertForProperty(propertyName, null, value, td);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Property property(PropertyDescriptor pd) {
        GenericTypeAwarePropertyDescriptor gpd = (GenericTypeAwarePropertyDescriptor) pd;
        return new Property(gpd.getBeanClass(), gpd.getReadMethod(), gpd.getWriteMethod(), gpd.getName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.beans.AbstractNestablePropertyAccessor
    public BeanPropertyHandler getLocalPropertyHandler(String propertyName) {
        PropertyDescriptor pd = getCachedIntrospectionResults().getPropertyDescriptor(propertyName);
        if (pd != null) {
            return new BeanPropertyHandler(pd);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.beans.AbstractNestablePropertyAccessor
    public BeanWrapperImpl newNestedPropertyAccessor(Object object, String nestedPath) {
        return new BeanWrapperImpl(object, nestedPath, this);
    }

    @Override // org.springframework.beans.AbstractNestablePropertyAccessor
    protected NotWritablePropertyException createNotWritablePropertyException(String propertyName) {
        PropertyMatches matches = PropertyMatches.forProperty(propertyName, getRootClass());
        throw new NotWritablePropertyException(getRootClass(), getNestedPath() + propertyName, matches.buildErrorMessage(), matches.getPossibleMatches());
    }

    @Override // org.springframework.beans.BeanWrapper
    public PropertyDescriptor[] getPropertyDescriptors() {
        return getCachedIntrospectionResults().getPropertyDescriptors();
    }

    @Override // org.springframework.beans.BeanWrapper
    public PropertyDescriptor getPropertyDescriptor(String propertyName) throws InvalidPropertyException {
        BeanWrapperImpl nestedBw = (BeanWrapperImpl) getPropertyAccessorForPropertyPath(propertyName);
        String finalPath = getFinalPath(nestedBw, propertyName);
        PropertyDescriptor pd = nestedBw.getCachedIntrospectionResults().getPropertyDescriptor(finalPath);
        if (pd == null) {
            throw new InvalidPropertyException(getRootClass(), getNestedPath() + propertyName, "No property '" + propertyName + "' found");
        }
        return pd;
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/BeanWrapperImpl$BeanPropertyHandler.class */
    private class BeanPropertyHandler extends AbstractNestablePropertyAccessor.PropertyHandler {
        private final PropertyDescriptor pd;

        public BeanPropertyHandler(PropertyDescriptor pd) {
            super(pd.getPropertyType(), pd.getReadMethod() != null, pd.getWriteMethod() != null);
            this.pd = pd;
        }

        @Override // org.springframework.beans.AbstractNestablePropertyAccessor.PropertyHandler
        public ResolvableType getResolvableType() {
            return ResolvableType.forMethodReturnType(this.pd.getReadMethod());
        }

        @Override // org.springframework.beans.AbstractNestablePropertyAccessor.PropertyHandler
        public TypeDescriptor toTypeDescriptor() {
            return new TypeDescriptor(BeanWrapperImpl.this.property(this.pd));
        }

        @Override // org.springframework.beans.AbstractNestablePropertyAccessor.PropertyHandler
        public TypeDescriptor nested(int level) {
            return TypeDescriptor.nested(BeanWrapperImpl.this.property(this.pd), level);
        }

        @Override // org.springframework.beans.AbstractNestablePropertyAccessor.PropertyHandler
        public Object getValue() throws Exception {
            final Method readMethod = this.pd.getReadMethod();
            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers()) && !readMethod.isAccessible()) {
                if (System.getSecurityManager() != null) {
                    AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.springframework.beans.BeanWrapperImpl.BeanPropertyHandler.1
                        @Override // java.security.PrivilegedAction
                        public Object run() {
                            readMethod.setAccessible(true);
                            return null;
                        }
                    });
                } else {
                    readMethod.setAccessible(true);
                }
            }
            if (System.getSecurityManager() != null) {
                try {
                    return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: org.springframework.beans.BeanWrapperImpl.BeanPropertyHandler.2
                        @Override // java.security.PrivilegedExceptionAction
                        public Object run() throws Exception {
                            return readMethod.invoke(BeanWrapperImpl.this.getWrappedInstance(), (Object[]) null);
                        }
                    }, BeanWrapperImpl.this.acc);
                } catch (PrivilegedActionException pae) {
                    throw pae.getException();
                }
            }
            return readMethod.invoke(BeanWrapperImpl.this.getWrappedInstance(), (Object[]) null);
        }

        @Override // org.springframework.beans.AbstractNestablePropertyAccessor.PropertyHandler
        public void setValue(final Object object, final Object valueToApply) throws Exception {
            Method writeMethod;
            if (this.pd instanceof GenericTypeAwarePropertyDescriptor) {
                writeMethod = ((GenericTypeAwarePropertyDescriptor) this.pd).getWriteMethodForActualAccess();
            } else {
                writeMethod = this.pd.getWriteMethod();
            }
            final Method writeMethod2 = writeMethod;
            if (!Modifier.isPublic(writeMethod2.getDeclaringClass().getModifiers()) && !writeMethod2.isAccessible()) {
                if (System.getSecurityManager() != null) {
                    AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.springframework.beans.BeanWrapperImpl.BeanPropertyHandler.3
                        @Override // java.security.PrivilegedAction
                        public Object run() {
                            writeMethod2.setAccessible(true);
                            return null;
                        }
                    });
                } else {
                    writeMethod2.setAccessible(true);
                }
            }
            if (System.getSecurityManager() != null) {
                try {
                    AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: org.springframework.beans.BeanWrapperImpl.BeanPropertyHandler.4
                        @Override // java.security.PrivilegedExceptionAction
                        public Object run() throws Exception {
                            writeMethod2.invoke(object, valueToApply);
                            return null;
                        }
                    }, BeanWrapperImpl.this.acc);
                    return;
                } catch (PrivilegedActionException ex) {
                    throw ex.getException();
                }
            }
            writeMethod2.invoke(BeanWrapperImpl.this.getWrappedInstance(), valueToApply);
        }
    }
}
