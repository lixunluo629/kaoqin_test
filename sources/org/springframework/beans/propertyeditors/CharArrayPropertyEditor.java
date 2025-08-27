package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/propertyeditors/CharArrayPropertyEditor.class */
public class CharArrayPropertyEditor extends PropertyEditorSupport {
    public void setAsText(String text) {
        setValue(text != null ? text.toCharArray() : null);
    }

    public String getAsText() {
        char[] value = (char[]) getValue();
        return value != null ? new String(value) : "";
    }
}
