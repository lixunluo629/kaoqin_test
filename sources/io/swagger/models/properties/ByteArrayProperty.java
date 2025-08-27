package io.swagger.models.properties;

import io.swagger.models.Xml;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/ByteArrayProperty.class */
public class ByteArrayProperty extends AbstractProperty implements Property {
    public ByteArrayProperty() {
        this.type = StringProperty.TYPE;
        this.format = "binary";
    }

    public static boolean isType(String type, String format) {
        if (StringProperty.TYPE.equals(type) && "binary".equals(format)) {
            return true;
        }
        return false;
    }

    public ByteArrayProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public ByteArrayProperty example(String example) {
        setExample(example);
        return this;
    }
}
