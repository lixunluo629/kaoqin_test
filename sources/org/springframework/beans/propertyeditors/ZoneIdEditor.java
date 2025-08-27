package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.time.ZoneId;
import org.springframework.lang.UsesJava8;

@UsesJava8
/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/propertyeditors/ZoneIdEditor.class */
public class ZoneIdEditor extends PropertyEditorSupport {
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(ZoneId.of(text));
    }

    public String getAsText() {
        ZoneId value = (ZoneId) getValue();
        return value != null ? value.getId() : "";
    }
}
