package org.springframework.beans;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/PropertyValues.class */
public interface PropertyValues {
    PropertyValue[] getPropertyValues();

    PropertyValue getPropertyValue(String str);

    PropertyValues changesSince(PropertyValues propertyValues);

    boolean contains(String str);

    boolean isEmpty();
}
