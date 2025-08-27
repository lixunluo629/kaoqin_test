package io.swagger.models.properties;

import io.swagger.models.Xml;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/DecimalProperty.class */
public class DecimalProperty extends AbstractNumericProperty {
    public static final String TYPE = "number";

    public DecimalProperty() {
        this(null);
    }

    public DecimalProperty(String format) {
        this.type = TYPE;
        this.format = format;
    }

    public static boolean isType(String type, String format) {
        return TYPE.equals(type) && format == null;
    }

    public DecimalProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public DecimalProperty example(String example) {
        setExample(example);
        return this;
    }
}
