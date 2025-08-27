package io.swagger.models.properties;

import io.swagger.models.Xml;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/FileProperty.class */
public class FileProperty extends AbstractProperty implements Property {
    public FileProperty() {
        this.type = "file";
    }

    public static boolean isType(String type, String format) {
        if (type != null && "file".equals(type.toLowerCase())) {
            return true;
        }
        return false;
    }

    public FileProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }
}
