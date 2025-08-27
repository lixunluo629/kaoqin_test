package org.springframework.beans;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.CollectionFactory;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.UsesJava8;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/AbstractNestablePropertyAccessor.class */
public abstract class AbstractNestablePropertyAccessor extends AbstractPropertyAccessor {
    private static final Log logger = LogFactory.getLog(AbstractNestablePropertyAccessor.class);
    private static Class<?> javaUtilOptionalClass;
    private int autoGrowCollectionLimit;
    Object wrappedObject;
    private String nestedPath;
    Object rootObject;
    private Map<String, AbstractNestablePropertyAccessor> nestedPropertyAccessors;

    protected abstract PropertyHandler getLocalPropertyHandler(String str);

    protected abstract AbstractNestablePropertyAccessor newNestedPropertyAccessor(Object obj, String str);

    protected abstract NotWritablePropertyException createNotWritablePropertyException(String str);

    static {
        javaUtilOptionalClass = null;
        try {
            javaUtilOptionalClass = ClassUtils.forName("java.util.Optional", AbstractNestablePropertyAccessor.class.getClassLoader());
        } catch (ClassNotFoundException e) {
        }
    }

    protected AbstractNestablePropertyAccessor() {
        this(true);
    }

    protected AbstractNestablePropertyAccessor(boolean registerDefaultEditors) {
        this.autoGrowCollectionLimit = Integer.MAX_VALUE;
        this.nestedPath = "";
        if (registerDefaultEditors) {
            registerDefaultEditors();
        }
        this.typeConverterDelegate = new TypeConverterDelegate(this);
    }

    protected AbstractNestablePropertyAccessor(Object object) {
        this.autoGrowCollectionLimit = Integer.MAX_VALUE;
        this.nestedPath = "";
        registerDefaultEditors();
        setWrappedInstance(object);
    }

    protected AbstractNestablePropertyAccessor(Class<?> clazz) {
        this.autoGrowCollectionLimit = Integer.MAX_VALUE;
        this.nestedPath = "";
        registerDefaultEditors();
        setWrappedInstance(BeanUtils.instantiateClass(clazz));
    }

    protected AbstractNestablePropertyAccessor(Object object, String nestedPath, Object rootObject) {
        this.autoGrowCollectionLimit = Integer.MAX_VALUE;
        this.nestedPath = "";
        registerDefaultEditors();
        setWrappedInstance(object, nestedPath, rootObject);
    }

    protected AbstractNestablePropertyAccessor(Object object, String nestedPath, AbstractNestablePropertyAccessor parent) {
        this.autoGrowCollectionLimit = Integer.MAX_VALUE;
        this.nestedPath = "";
        setWrappedInstance(object, nestedPath, parent.getWrappedInstance());
        setExtractOldValueForEditor(parent.isExtractOldValueForEditor());
        setAutoGrowNestedPaths(parent.isAutoGrowNestedPaths());
        setAutoGrowCollectionLimit(parent.getAutoGrowCollectionLimit());
        setConversionService(parent.getConversionService());
    }

    public void setAutoGrowCollectionLimit(int autoGrowCollectionLimit) {
        this.autoGrowCollectionLimit = autoGrowCollectionLimit;
    }

    public int getAutoGrowCollectionLimit() {
        return this.autoGrowCollectionLimit;
    }

    public void setWrappedInstance(Object object) {
        setWrappedInstance(object, "", null);
    }

    public void setWrappedInstance(Object object, String nestedPath, Object rootObject) {
        Assert.notNull(object, "Target object must not be null");
        if (object.getClass() == javaUtilOptionalClass) {
            this.wrappedObject = OptionalUnwrapper.unwrap(object);
        } else {
            this.wrappedObject = object;
        }
        this.nestedPath = nestedPath != null ? nestedPath : "";
        this.rootObject = !"".equals(this.nestedPath) ? rootObject : this.wrappedObject;
        this.nestedPropertyAccessors = null;
        this.typeConverterDelegate = new TypeConverterDelegate(this, this.wrappedObject);
    }

    public final Object getWrappedInstance() {
        return this.wrappedObject;
    }

    public final Class<?> getWrappedClass() {
        if (this.wrappedObject != null) {
            return this.wrappedObject.getClass();
        }
        return null;
    }

    public final String getNestedPath() {
        return this.nestedPath;
    }

    public final Object getRootInstance() {
        return this.rootObject;
    }

    public final Class<?> getRootClass() {
        if (this.rootObject != null) {
            return this.rootObject.getClass();
        }
        return null;
    }

    @Override // org.springframework.beans.AbstractPropertyAccessor, org.springframework.beans.PropertyAccessor
    public void setPropertyValue(String propertyName, Object value) throws BeansException, ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        try {
            AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
            PropertyTokenHolder tokens = getPropertyNameTokens(getFinalPath(nestedPa, propertyName));
            nestedPa.setPropertyValue(tokens, new PropertyValue(propertyName, value));
        } catch (NotReadablePropertyException ex) {
            throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName, "Nested property in path '" + propertyName + "' does not exist", ex);
        }
    }

    @Override // org.springframework.beans.AbstractPropertyAccessor, org.springframework.beans.PropertyAccessor
    public void setPropertyValue(PropertyValue pv) throws BeansException, ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        PropertyTokenHolder tokens = (PropertyTokenHolder) pv.resolvedTokens;
        if (tokens == null) {
            String propertyName = pv.getName();
            try {
                AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
                PropertyTokenHolder tokens2 = getPropertyNameTokens(getFinalPath(nestedPa, propertyName));
                if (nestedPa == this) {
                    pv.getOriginalPropertyValue().resolvedTokens = tokens2;
                }
                nestedPa.setPropertyValue(tokens2, pv);
                return;
            } catch (NotReadablePropertyException ex) {
                throw new NotWritablePropertyException(getRootClass(), this.nestedPath + propertyName, "Nested property in path '" + propertyName + "' does not exist", ex);
            }
        }
        setPropertyValue(tokens, pv);
    }

    protected void setPropertyValue(PropertyTokenHolder tokens, PropertyValue pv) throws BeansException, ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        if (tokens.keys != null) {
            processKeyedProperty(tokens, pv);
        } else {
            processLocalProperty(tokens, pv);
        }
    }

    private void processKeyedProperty(PropertyTokenHolder tokens, PropertyValue pv) throws BeansException, ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        Object propValue = getPropertyHoldingValue(tokens);
        String lastKey = tokens.keys[tokens.keys.length - 1];
        if (propValue.getClass().isArray()) {
            PropertyHandler ph = getLocalPropertyHandler(tokens.actualName);
            Class<?> requiredType = propValue.getClass().getComponentType();
            int arrayIndex = Integer.parseInt(lastKey);
            Object oldValue = null;
            try {
                if (isExtractOldValueForEditor() && arrayIndex < Array.getLength(propValue)) {
                    oldValue = Array.get(propValue, arrayIndex);
                }
                Object convertedValue = convertIfNecessary(tokens.canonicalName, oldValue, pv.getValue(), requiredType, ph.nested(tokens.keys.length));
                int length = Array.getLength(propValue);
                if (arrayIndex >= length && arrayIndex < this.autoGrowCollectionLimit) {
                    Class<?> componentType = propValue.getClass().getComponentType();
                    Object newArray = Array.newInstance(componentType, arrayIndex + 1);
                    System.arraycopy(propValue, 0, newArray, 0, length);
                    setPropertyValue(tokens.actualName, newArray);
                    propValue = getPropertyValue(tokens.actualName);
                }
                Array.set(propValue, arrayIndex, convertedValue);
                return;
            } catch (IndexOutOfBoundsException ex) {
                throw new InvalidPropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Invalid array index in property path '" + tokens.canonicalName + "'", ex);
            }
        }
        if (propValue instanceof List) {
            PropertyHandler ph2 = getPropertyHandler(tokens.actualName);
            Class<?> requiredType2 = ph2.getCollectionType(tokens.keys.length);
            List<Object> list = (List) propValue;
            int index = Integer.parseInt(lastKey);
            Object oldValue2 = null;
            if (isExtractOldValueForEditor() && index < list.size()) {
                oldValue2 = list.get(index);
            }
            Object convertedValue2 = convertIfNecessary(tokens.canonicalName, oldValue2, pv.getValue(), requiredType2, ph2.nested(tokens.keys.length));
            int size = list.size();
            if (index >= size && index < this.autoGrowCollectionLimit) {
                for (int i = size; i < index; i++) {
                    try {
                        list.add(null);
                    } catch (NullPointerException e) {
                        throw new InvalidPropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Cannot set element with index " + index + " in List of size " + size + ", accessed using property path '" + tokens.canonicalName + "': List does not support filling up gaps with null elements");
                    }
                }
                list.add(convertedValue2);
                return;
            }
            try {
                list.set(index, convertedValue2);
                return;
            } catch (IndexOutOfBoundsException ex2) {
                throw new InvalidPropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Invalid list index in property path '" + tokens.canonicalName + "'", ex2);
            }
        }
        if (propValue instanceof Map) {
            PropertyHandler ph3 = getLocalPropertyHandler(tokens.actualName);
            Class<?> mapKeyType = ph3.getMapKeyType(tokens.keys.length);
            Class<?> mapValueType = ph3.getMapValueType(tokens.keys.length);
            Map<Object, Object> map = (Map) propValue;
            TypeDescriptor typeDescriptor = TypeDescriptor.valueOf(mapKeyType);
            Object convertedMapKey = convertIfNecessary(null, null, lastKey, mapKeyType, typeDescriptor);
            Object oldValue3 = null;
            if (isExtractOldValueForEditor()) {
                oldValue3 = map.get(convertedMapKey);
            }
            Object convertedMapValue = convertIfNecessary(tokens.canonicalName, oldValue3, pv.getValue(), mapValueType, ph3.nested(tokens.keys.length));
            map.put(convertedMapKey, convertedMapValue);
            return;
        }
        throw new InvalidPropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Property referenced in indexed property path '" + tokens.canonicalName + "' is neither an array nor a List nor a Map; returned value was [" + propValue + "]");
    }

    private Object getPropertyHoldingValue(PropertyTokenHolder tokens) throws BeansException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        PropertyTokenHolder getterTokens = new PropertyTokenHolder();
        getterTokens.canonicalName = tokens.canonicalName;
        getterTokens.actualName = tokens.actualName;
        getterTokens.keys = new String[tokens.keys.length - 1];
        System.arraycopy(tokens.keys, 0, getterTokens.keys, 0, tokens.keys.length - 1);
        try {
            Object propValue = getPropertyValue(getterTokens);
            if (propValue == null) {
                if (isAutoGrowNestedPaths()) {
                    int lastKeyIndex = tokens.canonicalName.lastIndexOf(91);
                    getterTokens.canonicalName = tokens.canonicalName.substring(0, lastKeyIndex);
                    propValue = setDefaultValue(getterTokens);
                } else {
                    throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + tokens.canonicalName, "Cannot access indexed value in property referenced in indexed property path '" + tokens.canonicalName + "': returned null");
                }
            }
            return propValue;
        } catch (NotReadablePropertyException ex) {
            throw new NotWritablePropertyException(getRootClass(), this.nestedPath + tokens.canonicalName, "Cannot access indexed value in property referenced in indexed property path '" + tokens.canonicalName + "'", ex);
        }
    }

    private void processLocalProperty(PropertyTokenHolder tokens, PropertyValue pv) {
        PropertyHandler ph = getLocalPropertyHandler(tokens.actualName);
        if (ph == null || !ph.isWritable()) {
            if (pv.isOptional()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Ignoring optional value for property '" + tokens.actualName + "' - property not found on bean class [" + getRootClass().getName() + "]");
                    return;
                }
                return;
            }
            throw createNotWritablePropertyException(tokens.canonicalName);
        }
        Object oldValue = null;
        try {
            Object originalValue = pv.getValue();
            Object valueToApply = originalValue;
            if (!Boolean.FALSE.equals(pv.conversionNecessary)) {
                if (pv.isConverted()) {
                    valueToApply = pv.getConvertedValue();
                } else {
                    if (isExtractOldValueForEditor() && ph.isReadable()) {
                        try {
                            oldValue = ph.getValue();
                        } catch (Exception e) {
                            ex = e;
                            if (ex instanceof PrivilegedActionException) {
                                ex = ((PrivilegedActionException) ex).getException();
                            }
                            if (logger.isDebugEnabled()) {
                                logger.debug("Could not read previous value of property '" + this.nestedPath + tokens.canonicalName + "'", ex);
                            }
                        }
                    }
                    valueToApply = convertForProperty(tokens.canonicalName, oldValue, originalValue, ph.toTypeDescriptor());
                }
                pv.getOriginalPropertyValue().conversionNecessary = Boolean.valueOf(valueToApply != originalValue);
            }
            ph.setValue(this.wrappedObject, valueToApply);
        } catch (InvocationTargetException ex) {
            PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this.rootObject, this.nestedPath + tokens.canonicalName, oldValue, pv.getValue());
            if (ex.getTargetException() instanceof ClassCastException) {
                throw new TypeMismatchException(propertyChangeEvent, ph.getPropertyType(), ex.getTargetException());
            }
            Throwable cause = ex.getTargetException();
            if (cause instanceof UndeclaredThrowableException) {
                cause = cause.getCause();
            }
            throw new MethodInvocationException(propertyChangeEvent, cause);
        } catch (TypeMismatchException ex2) {
            throw ex2;
        } catch (Exception ex3) {
            PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + tokens.canonicalName, oldValue, pv.getValue());
            throw new MethodInvocationException(pce, ex3);
        }
    }

    @Override // org.springframework.beans.AbstractPropertyAccessor, org.springframework.beans.PropertyEditorRegistrySupport, org.springframework.beans.PropertyAccessor
    public Class<?> getPropertyType(String propertyName) throws BeansException {
        try {
            PropertyHandler ph = getPropertyHandler(propertyName);
            if (ph != null) {
                return ph.getPropertyType();
            }
            Object value = getPropertyValue(propertyName);
            if (value != null) {
                return value.getClass();
            }
            Class<?> editorType = guessPropertyTypeFromEditors(propertyName);
            if (editorType != null) {
                return editorType;
            }
            return null;
        } catch (InvalidPropertyException e) {
            return null;
        }
    }

    @Override // org.springframework.beans.PropertyAccessor
    public TypeDescriptor getPropertyTypeDescriptor(String propertyName) throws BeansException {
        try {
            AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
            String finalPath = getFinalPath(nestedPa, propertyName);
            PropertyTokenHolder tokens = getPropertyNameTokens(finalPath);
            PropertyHandler ph = nestedPa.getLocalPropertyHandler(tokens.actualName);
            if (ph != null) {
                if (tokens.keys != null) {
                    if (ph.isReadable() || ph.isWritable()) {
                        return ph.nested(tokens.keys.length);
                    }
                    return null;
                }
                if (ph.isReadable() || ph.isWritable()) {
                    return ph.toTypeDescriptor();
                }
                return null;
            }
            return null;
        } catch (InvalidPropertyException e) {
            return null;
        }
    }

    @Override // org.springframework.beans.PropertyAccessor
    public boolean isReadableProperty(String propertyName) throws BeansException {
        try {
            PropertyHandler ph = getPropertyHandler(propertyName);
            if (ph != null) {
                return ph.isReadable();
            }
            getPropertyValue(propertyName);
            return true;
        } catch (InvalidPropertyException e) {
            return false;
        }
    }

    @Override // org.springframework.beans.PropertyAccessor
    public boolean isWritableProperty(String propertyName) throws BeansException {
        try {
            PropertyHandler ph = getPropertyHandler(propertyName);
            if (ph != null) {
                return ph.isWritable();
            }
            getPropertyValue(propertyName);
            return true;
        } catch (InvalidPropertyException e) {
            return false;
        }
    }

    private Object convertIfNecessary(String propertyName, Object oldValue, Object newValue, Class<?> requiredType, TypeDescriptor td) throws TypeMismatchException {
        try {
            return this.typeConverterDelegate.convertIfNecessary(propertyName, oldValue, newValue, requiredType, td);
        } catch (IllegalArgumentException ex) {
            PropertyChangeEvent pce = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
            throw new TypeMismatchException(pce, requiredType, (Throwable) ex);
        } catch (IllegalStateException ex2) {
            PropertyChangeEvent pce2 = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
            throw new ConversionNotSupportedException(pce2, requiredType, (Throwable) ex2);
        } catch (ConverterNotFoundException ex3) {
            PropertyChangeEvent pce3 = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
            throw new ConversionNotSupportedException(pce3, td.getType(), (Throwable) ex3);
        } catch (ConversionException ex4) {
            PropertyChangeEvent pce4 = new PropertyChangeEvent(this.rootObject, this.nestedPath + propertyName, oldValue, newValue);
            throw new TypeMismatchException(pce4, requiredType, (Throwable) ex4);
        }
    }

    protected Object convertForProperty(String propertyName, Object oldValue, Object newValue, TypeDescriptor td) throws TypeMismatchException {
        return convertIfNecessary(propertyName, oldValue, newValue, td.getType(), td);
    }

    @Override // org.springframework.beans.AbstractPropertyAccessor, org.springframework.beans.PropertyAccessor
    public Object getPropertyValue(String propertyName) throws BeansException {
        AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
        PropertyTokenHolder tokens = getPropertyNameTokens(getFinalPath(nestedPa, propertyName));
        return nestedPa.getPropertyValue(tokens);
    }

    protected Object getPropertyValue(PropertyTokenHolder tokens) throws BeansException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        String propertyName = tokens.canonicalName;
        String actualName = tokens.actualName;
        PropertyHandler ph = getLocalPropertyHandler(actualName);
        if (ph == null || !ph.isReadable()) {
            throw new NotReadablePropertyException(getRootClass(), this.nestedPath + propertyName);
        }
        try {
            Object value = ph.getValue();
            if (tokens.keys != null) {
                if (value == null) {
                    if (isAutoGrowNestedPaths()) {
                        value = setDefaultValue(tokens.actualName);
                    } else {
                        throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName, "Cannot access indexed value of property referenced in indexed property path '" + propertyName + "': returned null");
                    }
                }
                String indexedPropertyName = tokens.actualName;
                for (int i = 0; i < tokens.keys.length; i++) {
                    String key = tokens.keys[i];
                    if (value == null) {
                        throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + propertyName, "Cannot access indexed value of property referenced in indexed property path '" + propertyName + "': returned null");
                    }
                    if (value.getClass().isArray()) {
                        int index = Integer.parseInt(key);
                        value = Array.get(growArrayIfNecessary(value, index, indexedPropertyName), index);
                    } else if (value instanceof List) {
                        int index2 = Integer.parseInt(key);
                        List<Object> list = (List) value;
                        growCollectionIfNecessary(list, index2, indexedPropertyName, ph, i + 1);
                        value = list.get(index2);
                    } else if (value instanceof Set) {
                        Set<Object> set = (Set) value;
                        int index3 = Integer.parseInt(key);
                        if (index3 < 0 || index3 >= set.size()) {
                            throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Cannot get element with index " + index3 + " from Set of size " + set.size() + ", accessed using property path '" + propertyName + "'");
                        }
                        Iterator<Object> it = set.iterator();
                        int j = 0;
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            Object elem = it.next();
                            if (j != index3) {
                                j++;
                            } else {
                                value = elem;
                                break;
                            }
                        }
                    } else if (value instanceof Map) {
                        Map<Object, Object> map = (Map) value;
                        Class<?> mapKeyType = ph.getResolvableType().getNested(i + 1).asMap().resolveGeneric(0);
                        TypeDescriptor typeDescriptor = TypeDescriptor.valueOf(mapKeyType);
                        Object convertedMapKey = convertIfNecessary(null, null, key, mapKeyType, typeDescriptor);
                        value = map.get(convertedMapKey);
                    } else {
                        throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Property referenced in indexed property path '" + propertyName + "' is neither an array nor a List nor a Set nor a Map; returned value was [" + value + "]");
                    }
                    indexedPropertyName = indexedPropertyName + PropertyAccessor.PROPERTY_KEY_PREFIX + key + "]";
                }
            }
            return value;
        } catch (IndexOutOfBoundsException ex) {
            throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Index of out of bounds in property path '" + propertyName + "'", ex);
        } catch (NumberFormatException ex2) {
            throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Invalid index in property path '" + propertyName + "'", ex2);
        } catch (InvocationTargetException ex3) {
            throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Getter for property '" + actualName + "' threw exception", ex3);
        } catch (TypeMismatchException ex4) {
            throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Invalid index in property path '" + propertyName + "'", ex4);
        } catch (Exception ex5) {
            throw new InvalidPropertyException(getRootClass(), this.nestedPath + propertyName, "Illegal attempt to get property '" + actualName + "' threw exception", ex5);
        }
    }

    protected PropertyHandler getPropertyHandler(String propertyName) throws BeansException {
        Assert.notNull(propertyName, "Property name must not be null");
        AbstractNestablePropertyAccessor nestedPa = getPropertyAccessorForPropertyPath(propertyName);
        return nestedPa.getLocalPropertyHandler(getFinalPath(nestedPa, propertyName));
    }

    private Object growArrayIfNecessary(Object array, int index, String name) throws BeansException, ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        if (!isAutoGrowNestedPaths()) {
            return array;
        }
        int length = Array.getLength(array);
        if (index >= length && index < this.autoGrowCollectionLimit) {
            Class<?> componentType = array.getClass().getComponentType();
            Object newArray = Array.newInstance(componentType, index + 1);
            System.arraycopy(array, 0, newArray, 0, length);
            for (int i = length; i < Array.getLength(newArray); i++) {
                Array.set(newArray, i, newValue(componentType, null, name));
            }
            setPropertyValue(name, newArray);
            return getPropertyValue(name);
        }
        return array;
    }

    private void growCollectionIfNecessary(Collection<Object> collection, int index, String name, PropertyHandler ph, int nestingLevel) {
        Class<?> elementType;
        if (!isAutoGrowNestedPaths()) {
            return;
        }
        int size = collection.size();
        if (index >= size && index < this.autoGrowCollectionLimit && (elementType = ph.getResolvableType().getNested(nestingLevel).asCollection().resolveGeneric(new int[0])) != null) {
            for (int i = collection.size(); i < index + 1; i++) {
                collection.add(newValue(elementType, null, name));
            }
        }
    }

    protected String getFinalPath(AbstractNestablePropertyAccessor pa, String nestedPath) {
        if (pa == this) {
            return nestedPath;
        }
        return nestedPath.substring(PropertyAccessorUtils.getLastNestedPropertySeparatorIndex(nestedPath) + 1);
    }

    protected AbstractNestablePropertyAccessor getPropertyAccessorForPropertyPath(String propertyPath) throws BeansException, ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        int pos = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex(propertyPath);
        if (pos > -1) {
            String nestedProperty = propertyPath.substring(0, pos);
            String nestedPath = propertyPath.substring(pos + 1);
            AbstractNestablePropertyAccessor nestedPa = getNestedPropertyAccessor(nestedProperty);
            return nestedPa.getPropertyAccessorForPropertyPath(nestedPath);
        }
        return this;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x009f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private org.springframework.beans.AbstractNestablePropertyAccessor getNestedPropertyAccessor(java.lang.String r7) throws org.springframework.beans.BeansException, java.lang.ArrayIndexOutOfBoundsException, java.lang.IllegalArgumentException, java.lang.NegativeArraySizeException {
        /*
            Method dump skipped, instructions count: 326
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.springframework.beans.AbstractNestablePropertyAccessor.getNestedPropertyAccessor(java.lang.String):org.springframework.beans.AbstractNestablePropertyAccessor");
    }

    private Object setDefaultValue(String propertyName) {
        PropertyTokenHolder tokens = new PropertyTokenHolder();
        tokens.actualName = propertyName;
        tokens.canonicalName = propertyName;
        return setDefaultValue(tokens);
    }

    private Object setDefaultValue(PropertyTokenHolder tokens) throws BeansException, ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        PropertyValue pv = createDefaultPropertyValue(tokens);
        setPropertyValue(tokens, pv);
        return getPropertyValue(tokens);
    }

    private PropertyValue createDefaultPropertyValue(PropertyTokenHolder tokens) throws BeansException {
        TypeDescriptor desc = getPropertyTypeDescriptor(tokens.canonicalName);
        Class<?> type = desc.getType();
        if (type == null) {
            throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + tokens.canonicalName, "Could not determine property type for auto-growing a default value");
        }
        Object defaultValue = newValue(type, desc, tokens.canonicalName);
        return new PropertyValue(tokens.canonicalName, defaultValue);
    }

    private Object newValue(Class<?> type, TypeDescriptor desc, String name) {
        try {
            if (type.isArray()) {
                Class<?> componentType = type.getComponentType();
                if (componentType.isArray()) {
                    Object array = Array.newInstance(componentType, 1);
                    Array.set(array, 0, Array.newInstance(componentType.getComponentType(), 0));
                    return array;
                }
                return Array.newInstance(componentType, 0);
            }
            if (Collection.class.isAssignableFrom(type)) {
                TypeDescriptor elementDesc = desc != null ? desc.getElementTypeDescriptor() : null;
                return CollectionFactory.createCollection(type, elementDesc != null ? elementDesc.getType() : null, 16);
            }
            if (Map.class.isAssignableFrom(type)) {
                TypeDescriptor keyDesc = desc != null ? desc.getMapKeyTypeDescriptor() : null;
                return CollectionFactory.createMap(type, keyDesc != null ? keyDesc.getType() : null, 16);
            }
            return BeanUtils.instantiate(type);
        } catch (Throwable ex) {
            throw new NullValueInNestedPathException(getRootClass(), this.nestedPath + name, "Could not instantiate property type [" + type.getName() + "] to auto-grow nested property path", ex);
        }
    }

    private PropertyTokenHolder getPropertyNameTokens(String propertyName) {
        int keyEnd;
        PropertyTokenHolder tokens = new PropertyTokenHolder();
        String actualName = null;
        List<String> keys = new ArrayList<>(2);
        int searchIndex = 0;
        while (searchIndex != -1) {
            int keyStart = propertyName.indexOf(PropertyAccessor.PROPERTY_KEY_PREFIX, searchIndex);
            searchIndex = -1;
            if (keyStart != -1 && (keyEnd = propertyName.indexOf("]", keyStart + PropertyAccessor.PROPERTY_KEY_PREFIX.length())) != -1) {
                if (actualName == null) {
                    actualName = propertyName.substring(0, keyStart);
                }
                String key = propertyName.substring(keyStart + PropertyAccessor.PROPERTY_KEY_PREFIX.length(), keyEnd);
                if ((key.length() > 1 && key.startsWith("'") && key.endsWith("'")) || (key.startsWith(SymbolConstants.QUOTES_SYMBOL) && key.endsWith(SymbolConstants.QUOTES_SYMBOL))) {
                    key = key.substring(1, key.length() - 1);
                }
                keys.add(key);
                searchIndex = keyEnd + "]".length();
            }
        }
        tokens.actualName = actualName != null ? actualName : propertyName;
        tokens.canonicalName = tokens.actualName;
        if (!keys.isEmpty()) {
            tokens.canonicalName += PropertyAccessor.PROPERTY_KEY_PREFIX + StringUtils.collectionToDelimitedString(keys, "][") + "]";
            tokens.keys = StringUtils.toStringArray(keys);
        }
        return tokens;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        if (this.wrappedObject != null) {
            sb.append(": wrapping object [").append(ObjectUtils.identityToString(this.wrappedObject)).append("]");
        } else {
            sb.append(": no wrapped object set");
        }
        return sb.toString();
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/AbstractNestablePropertyAccessor$PropertyHandler.class */
    protected static abstract class PropertyHandler {
        private final Class<?> propertyType;
        private final boolean readable;
        private final boolean writable;

        public abstract TypeDescriptor toTypeDescriptor();

        public abstract ResolvableType getResolvableType();

        public abstract TypeDescriptor nested(int i);

        public abstract Object getValue() throws Exception;

        public abstract void setValue(Object obj, Object obj2) throws Exception;

        public PropertyHandler(Class<?> propertyType, boolean readable, boolean writable) {
            this.propertyType = propertyType;
            this.readable = readable;
            this.writable = writable;
        }

        public Class<?> getPropertyType() {
            return this.propertyType;
        }

        public boolean isReadable() {
            return this.readable;
        }

        public boolean isWritable() {
            return this.writable;
        }

        public Class<?> getMapKeyType(int nestingLevel) {
            return getResolvableType().getNested(nestingLevel).asMap().resolveGeneric(0);
        }

        public Class<?> getMapValueType(int nestingLevel) {
            return getResolvableType().getNested(nestingLevel).asMap().resolveGeneric(1);
        }

        public Class<?> getCollectionType(int nestingLevel) {
            return getResolvableType().getNested(nestingLevel).asCollection().resolveGeneric(new int[0]);
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/AbstractNestablePropertyAccessor$PropertyTokenHolder.class */
    protected static class PropertyTokenHolder {
        public String canonicalName;
        public String actualName;
        public String[] keys;

        protected PropertyTokenHolder() {
        }
    }

    @UsesJava8
    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/AbstractNestablePropertyAccessor$OptionalUnwrapper.class */
    private static class OptionalUnwrapper {
        private OptionalUnwrapper() {
        }

        public static Object unwrap(Object optionalObject) {
            Optional<?> optional = (Optional) optionalObject;
            Assert.isTrue(optional.isPresent(), "Optional value must be present");
            Object result = optional.get();
            Assert.isTrue(!(result instanceof Optional), "Multi-level Optional usage not supported");
            return result;
        }

        public static boolean isEmpty(Object optionalObject) {
            return !((Optional) optionalObject).isPresent();
        }
    }
}
