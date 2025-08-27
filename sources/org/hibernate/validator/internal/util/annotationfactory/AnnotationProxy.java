package org.hibernate.validator.internal.util.annotationfactory;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethod;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredMethods;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/annotationfactory/AnnotationProxy.class */
class AnnotationProxy implements Annotation, InvocationHandler, Serializable {
    private static final long serialVersionUID = 6907601010599429454L;
    private static final Log log = LoggerFactory.make();
    private final Class<? extends Annotation> annotationType;
    private final Map<String, Object> values;
    private final int hashCode = calculateHashCode();

    AnnotationProxy(AnnotationDescriptor<?> descriptor) {
        this.annotationType = descriptor.type();
        this.values = Collections.unmodifiableMap(getAnnotationValues(descriptor));
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.values.containsKey(method.getName())) {
            return this.values.get(method.getName());
        }
        return method.invoke(this, args);
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return this.annotationType;
    }

    @Override // java.lang.annotation.Annotation
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !this.annotationType.isInstance(obj)) {
            return false;
        }
        Annotation other = this.annotationType.cast(obj);
        for (Map.Entry<String, Object> member : this.values.entrySet()) {
            Object value = member.getValue();
            Object otherValue = getAnnotationMemberValue(other, member.getKey());
            if (!areEqual(value, otherValue)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.lang.annotation.Annotation
    public int hashCode() {
        return this.hashCode;
    }

    @Override // java.lang.annotation.Annotation
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('@').append(this.annotationType.getName()).append('(');
        for (String s : getRegisteredMethodsInAlphabeticalOrder()) {
            result.append(s).append('=').append(this.values.get(s)).append(", ");
        }
        if (this.values.size() > 0) {
            result.delete(result.length() - 2, result.length());
            result.append(")");
        } else {
            result.delete(result.length() - 1, result.length());
        }
        return result.toString();
    }

    private Map<String, Object> getAnnotationValues(AnnotationDescriptor<?> descriptor) {
        Map<String, Object> result = CollectionHelper.newHashMap();
        int processedValuesFromDescriptor = 0;
        Method[] declaredMethods = (Method[]) run(GetDeclaredMethods.action(this.annotationType));
        for (Method m : declaredMethods) {
            if (descriptor.containsElement(m.getName())) {
                result.put(m.getName(), descriptor.valueOf(m.getName()));
                processedValuesFromDescriptor++;
            } else if (m.getDefaultValue() != null) {
                result.put(m.getName(), m.getDefaultValue());
            } else {
                throw log.getNoValueProvidedForAnnotationParameterException(m.getName(), this.annotationType.getSimpleName());
            }
        }
        if (processedValuesFromDescriptor != descriptor.numberOfElements()) {
            Set<String> unknownParameters = descriptor.getElements().keySet();
            unknownParameters.removeAll(result.keySet());
            throw log.getTryingToInstantiateAnnotationWithUnknownParametersException(this.annotationType, unknownParameters);
        }
        return result;
    }

    private int calculateHashCode() {
        int iHashCode;
        int hashCode = 0;
        for (Map.Entry<String, Object> member : this.values.entrySet()) {
            Object value = member.getValue();
            int nameHashCode = member.getKey().hashCode();
            if (!value.getClass().isArray()) {
                iHashCode = value.hashCode();
            } else if (value.getClass() == boolean[].class) {
                iHashCode = Arrays.hashCode((boolean[]) value);
            } else if (value.getClass() == byte[].class) {
                iHashCode = Arrays.hashCode((byte[]) value);
            } else if (value.getClass() == char[].class) {
                iHashCode = Arrays.hashCode((char[]) value);
            } else if (value.getClass() == double[].class) {
                iHashCode = Arrays.hashCode((double[]) value);
            } else if (value.getClass() == float[].class) {
                iHashCode = Arrays.hashCode((float[]) value);
            } else if (value.getClass() == int[].class) {
                iHashCode = Arrays.hashCode((int[]) value);
            } else if (value.getClass() == long[].class) {
                iHashCode = Arrays.hashCode((long[]) value);
            } else if (value.getClass() == short[].class) {
                iHashCode = Arrays.hashCode((short[]) value);
            } else {
                iHashCode = Arrays.hashCode((Object[]) value);
            }
            int valueHashCode = iHashCode;
            hashCode += (127 * nameHashCode) ^ valueHashCode;
        }
        return hashCode;
    }

    private SortedSet<String> getRegisteredMethodsInAlphabeticalOrder() {
        SortedSet<String> result = new TreeSet<>();
        result.addAll(this.values.keySet());
        return result;
    }

    private boolean areEqual(Object o1, Object o2) {
        return !o1.getClass().isArray() ? o1.equals(o2) : o1.getClass() == boolean[].class ? Arrays.equals((boolean[]) o1, (boolean[]) o2) : o1.getClass() == byte[].class ? Arrays.equals((byte[]) o1, (byte[]) o2) : o1.getClass() == char[].class ? Arrays.equals((char[]) o1, (char[]) o2) : o1.getClass() == double[].class ? Arrays.equals((double[]) o1, (double[]) o2) : o1.getClass() == float[].class ? Arrays.equals((float[]) o1, (float[]) o2) : o1.getClass() == int[].class ? Arrays.equals((int[]) o1, (int[]) o2) : o1.getClass() == long[].class ? Arrays.equals((long[]) o1, (long[]) o2) : o1.getClass() == short[].class ? Arrays.equals((short[]) o1, (short[]) o2) : Arrays.equals((Object[]) o1, (Object[]) o2);
    }

    private Object getAnnotationMemberValue(Annotation annotation, String name) {
        try {
            return ((Method) run(GetDeclaredMethod.action(annotation.annotationType(), name, new Class[0]))).invoke(annotation, new Object[0]);
        } catch (IllegalAccessException e) {
            throw log.getUnableToRetrieveAnnotationParameterValueException(e);
        } catch (IllegalArgumentException e2) {
            throw log.getUnableToRetrieveAnnotationParameterValueException(e2);
        } catch (InvocationTargetException e3) {
            throw log.getUnableToRetrieveAnnotationParameterValueException(e3);
        }
    }

    private <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
