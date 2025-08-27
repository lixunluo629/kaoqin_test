package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/propertyeditors/ByteArrayPropertyEditor.class */
public class ByteArrayPropertyEditor extends PropertyEditorSupport {
    public void setAsText(String text) {
        setValue(text != null ? text.getBytes() : null);
    }

    public String getAsText() {
        byte[] value = (byte[]) getValue();
        return value != null ? new String(value) : "";
    }
}
