package org.springframework.beans;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.propertyeditors.PropertiesEditor;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/PropertyValuesEditor.class */
public class PropertyValuesEditor extends PropertyEditorSupport {
    private final PropertiesEditor propertiesEditor = new PropertiesEditor();

    public void setAsText(String text) throws IOException, IllegalArgumentException {
        this.propertiesEditor.setAsText(text);
        Properties props = (Properties) this.propertiesEditor.getValue();
        setValue(new MutablePropertyValues(props));
    }
}
