package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.util.Assert;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/propertyeditors/InputStreamEditor.class */
public class InputStreamEditor extends PropertyEditorSupport {
    private final ResourceEditor resourceEditor;

    public InputStreamEditor() {
        this.resourceEditor = new ResourceEditor();
    }

    public InputStreamEditor(ResourceEditor resourceEditor) {
        Assert.notNull(resourceEditor, "ResourceEditor must not be null");
        this.resourceEditor = resourceEditor;
    }

    public void setAsText(String text) throws IllegalArgumentException {
        InputStream inputStream;
        this.resourceEditor.setAsText(text);
        Resource resource = (Resource) this.resourceEditor.getValue();
        if (resource != null) {
            try {
                inputStream = resource.getInputStream();
            } catch (IOException ex) {
                throw new IllegalArgumentException("Failed to retrieve InputStream for " + resource, ex);
            }
        } else {
            inputStream = null;
        }
        setValue(inputStream);
    }

    public String getAsText() {
        return null;
    }
}
