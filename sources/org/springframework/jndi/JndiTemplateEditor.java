package org.springframework.jndi;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.propertyeditors.PropertiesEditor;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jndi/JndiTemplateEditor.class */
public class JndiTemplateEditor extends PropertyEditorSupport {
    private final PropertiesEditor propertiesEditor = new PropertiesEditor();

    public void setAsText(String text) throws IOException, IllegalArgumentException {
        if (text == null) {
            throw new IllegalArgumentException("JndiTemplate cannot be created from null string");
        }
        if ("".equals(text)) {
            setValue(new JndiTemplate());
            return;
        }
        this.propertiesEditor.setAsText(text);
        Properties props = (Properties) this.propertiesEditor.getValue();
        setValue(new JndiTemplate(props));
    }
}
