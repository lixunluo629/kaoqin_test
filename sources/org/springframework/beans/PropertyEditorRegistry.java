package org.springframework.beans;

import java.beans.PropertyEditor;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/PropertyEditorRegistry.class */
public interface PropertyEditorRegistry {
    void registerCustomEditor(Class<?> cls, PropertyEditor propertyEditor);

    void registerCustomEditor(Class<?> cls, String str, PropertyEditor propertyEditor);

    PropertyEditor findCustomEditor(Class<?> cls, String str);
}
