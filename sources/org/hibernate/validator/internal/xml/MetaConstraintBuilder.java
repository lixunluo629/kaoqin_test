package org.hibernate.validator.internal.xml;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import javax.validation.Payload;
import javax.validation.ValidationException;
import javax.xml.bind.JAXBElement;
import org.apache.xmlbeans.XmlErrorCodes;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.metadata.core.MetaConstraint;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.annotationfactory.AnnotationDescriptor;
import org.hibernate.validator.internal.util.annotationfactory.AnnotationFactory;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/xml/MetaConstraintBuilder.class */
class MetaConstraintBuilder {
    private static final Log log = LoggerFactory.make();
    private static final Pattern IS_ONLY_WHITESPACE = Pattern.compile("\\s*");
    private static final String MESSAGE_PARAM = "message";
    private static final String GROUPS_PARAM = "groups";
    private static final String PAYLOAD_PARAM = "payload";
    private final ClassLoadingHelper classLoadingHelper;
    private final ConstraintHelper constraintHelper;

    MetaConstraintBuilder(ClassLoadingHelper classLoadingHelper, ConstraintHelper constraintHelper) {
        this.classLoadingHelper = classLoadingHelper;
        this.constraintHelper = constraintHelper;
    }

    <A extends Annotation> MetaConstraint<A> buildMetaConstraint(ConstraintLocation constraintLocation, ConstraintType constraint, java.lang.annotation.ElementType type, String defaultPackage, ConstraintDescriptorImpl.ConstraintType constraintType) {
        try {
            Class<?> clsLoadClass = this.classLoadingHelper.loadClass(constraint.getAnnotation(), defaultPackage);
            AnnotationDescriptor<A> annotationDescriptor = new AnnotationDescriptor<>(clsLoadClass);
            if (constraint.getMessage() != null) {
                annotationDescriptor.setValue("message", constraint.getMessage());
            }
            annotationDescriptor.setValue("groups", getGroups(constraint.getGroups(), defaultPackage));
            annotationDescriptor.setValue("payload", getPayload(constraint.getPayload(), defaultPackage));
            for (ElementType elementType : constraint.getElement()) {
                String name = elementType.getName();
                checkNameIsValid(name);
                Class<?> returnType = getAnnotationParameterType(clsLoadClass, name);
                Object elementValue = getElementValue(elementType, returnType, defaultPackage);
                annotationDescriptor.setValue(name, elementValue);
            }
            try {
                ConstraintDescriptorImpl<A> constraintDescriptor = new ConstraintDescriptorImpl<>(this.constraintHelper, constraintLocation.getMember(), AnnotationFactory.create(annotationDescriptor), type, constraintType);
                return new MetaConstraint<>(constraintDescriptor, constraintLocation);
            } catch (RuntimeException e) {
                throw log.getUnableToCreateAnnotationForConfiguredConstraintException(e);
            }
        } catch (ValidationException e2) {
            throw log.getUnableToLoadConstraintAnnotationClassException(constraint.getAnnotation(), e2);
        }
    }

    private <A extends Annotation> Annotation buildAnnotation(AnnotationType annotationType, Class<A> returnType, String defaultPackage) {
        AnnotationDescriptor<A> annotationDescriptor = new AnnotationDescriptor<>(returnType);
        for (ElementType elementType : annotationType.getElement()) {
            String name = elementType.getName();
            Class<?> parameterType = getAnnotationParameterType(returnType, name);
            Object elementValue = getElementValue(elementType, parameterType, defaultPackage);
            annotationDescriptor.setValue(name, elementValue);
        }
        return AnnotationFactory.create(annotationDescriptor);
    }

    private static void checkNameIsValid(String name) {
        if ("message".equals(name) || "groups".equals(name)) {
            throw log.getReservedParameterNamesException("message", "groups", "payload");
        }
    }

    private static <A extends Annotation> Class<?> getAnnotationParameterType(Class<A> annotationClass, String name) {
        Method m = (Method) run(GetMethod.action(annotationClass, name));
        if (m == null) {
            throw log.getAnnotationDoesNotContainAParameterException(annotationClass.getName(), name);
        }
        return m.getReturnType();
    }

    private Object getElementValue(ElementType elementType, Class<?> returnType, String defaultPackage) {
        removeEmptyContentElements(elementType);
        boolean isArray = returnType.isArray();
        if (!isArray) {
            if (elementType.getContent().size() == 0) {
                if (returnType == String.class) {
                    return "";
                }
                throw log.getEmptyElementOnlySupportedWhenCharSequenceIsExpectedExpection();
            }
            if (elementType.getContent().size() > 1) {
                throw log.getAttemptToSpecifyAnArrayWhereSingleValueIsExpectedException();
            }
            return getSingleValue(elementType.getContent().get(0), returnType, defaultPackage);
        }
        List<Object> values = CollectionHelper.newArrayList();
        for (Serializable s : elementType.getContent()) {
            values.add(getSingleValue(s, returnType.getComponentType(), defaultPackage));
        }
        return values.toArray((Object[]) Array.newInstance(returnType.getComponentType(), values.size()));
    }

    private static void removeEmptyContentElements(ElementType elementType) {
        Iterator<Serializable> contentIterator = elementType.getContent().iterator();
        while (contentIterator.hasNext()) {
            Serializable content = contentIterator.next();
            if ((content instanceof String) && IS_ONLY_WHITESPACE.matcher((String) content).matches()) {
                contentIterator.remove();
            }
        }
    }

    private Object getSingleValue(Serializable serializable, Class<?> returnType, String defaultPackage) {
        Object returnValue;
        if (serializable instanceof String) {
            String value = (String) serializable;
            returnValue = convertStringToReturnType(returnType, value, defaultPackage);
        } else if ((serializable instanceof JAXBElement) && ((JAXBElement) serializable).getDeclaredType().equals(String.class)) {
            JAXBElement<?> elem = (JAXBElement) serializable;
            String value2 = (String) elem.getValue();
            returnValue = convertStringToReturnType(returnType, value2, defaultPackage);
        } else if ((serializable instanceof JAXBElement) && ((JAXBElement) serializable).getDeclaredType().equals(AnnotationType.class)) {
            JAXBElement<?> elem2 = (JAXBElement) serializable;
            AnnotationType annotationType = (AnnotationType) elem2.getValue();
            try {
                returnValue = buildAnnotation(annotationType, returnType, defaultPackage);
            } catch (ClassCastException e) {
                throw log.getUnexpectedParameterValueException(e);
            }
        } else {
            throw log.getUnexpectedParameterValueException();
        }
        return returnValue;
    }

    private Object convertStringToReturnType(Class<?> returnType, String value, String defaultPackage) {
        Object returnValue;
        if (returnType == Byte.TYPE) {
            try {
                returnValue = Byte.valueOf(Byte.parseByte(value));
            } catch (NumberFormatException e) {
                throw log.getInvalidNumberFormatException("byte", e);
            }
        } else if (returnType == Short.TYPE) {
            try {
                returnValue = Short.valueOf(Short.parseShort(value));
            } catch (NumberFormatException e2) {
                throw log.getInvalidNumberFormatException("short", e2);
            }
        } else if (returnType == Integer.TYPE) {
            try {
                returnValue = Integer.valueOf(Integer.parseInt(value));
            } catch (NumberFormatException e3) {
                throw log.getInvalidNumberFormatException(XmlErrorCodes.INT, e3);
            }
        } else if (returnType == Long.TYPE) {
            try {
                returnValue = Long.valueOf(Long.parseLong(value));
            } catch (NumberFormatException e4) {
                throw log.getInvalidNumberFormatException(XmlErrorCodes.LONG, e4);
            }
        } else if (returnType == Float.TYPE) {
            try {
                returnValue = Float.valueOf(Float.parseFloat(value));
            } catch (NumberFormatException e5) {
                throw log.getInvalidNumberFormatException(XmlErrorCodes.FLOAT, e5);
            }
        } else if (returnType == Double.TYPE) {
            try {
                returnValue = Double.valueOf(Double.parseDouble(value));
            } catch (NumberFormatException e6) {
                throw log.getInvalidNumberFormatException(XmlErrorCodes.DOUBLE, e6);
            }
        } else if (returnType == Boolean.TYPE) {
            returnValue = Boolean.valueOf(Boolean.parseBoolean(value));
        } else if (returnType == Character.TYPE) {
            if (value.length() != 1) {
                throw log.getInvalidCharValueException(value);
            }
            returnValue = Character.valueOf(value.charAt(0));
        } else if (returnType == String.class) {
            returnValue = value;
        } else if (returnType == Class.class) {
            returnValue = this.classLoadingHelper.loadClass(value, defaultPackage);
        } else {
            try {
                returnValue = Enum.valueOf(returnType, value);
            } catch (ClassCastException e7) {
                throw log.getInvalidReturnTypeException(returnType, e7);
            }
        }
        return returnValue;
    }

    private Class<?>[] getGroups(GroupsType groupsType, String defaultPackage) {
        if (groupsType == null) {
            return new Class[0];
        }
        List<Class<?>> groupList = CollectionHelper.newArrayList();
        for (String groupClass : groupsType.getValue()) {
            groupList.add(this.classLoadingHelper.loadClass(groupClass, defaultPackage));
        }
        return (Class[]) groupList.toArray(new Class[groupList.size()]);
    }

    private Class<? extends Payload>[] getPayload(PayloadType payloadType, String defaultPackage) {
        if (payloadType == null) {
            return new Class[0];
        }
        ArrayList arrayListNewArrayList = CollectionHelper.newArrayList();
        for (String groupClass : payloadType.getValue()) {
            Class<?> payload = this.classLoadingHelper.loadClass(groupClass, defaultPackage);
            if (!Payload.class.isAssignableFrom(payload)) {
                throw log.getWrongPayloadClassException(payload.getName());
            }
            arrayListNewArrayList.add(payload);
        }
        return (Class[]) arrayListNewArrayList.toArray(new Class[arrayListNewArrayList.size()]);
    }

    private static <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
