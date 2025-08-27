package io.swagger.models.properties;

import io.swagger.models.Xml;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/DateProperty.class */
public class DateProperty extends AbstractProperty implements Property {
    public DateProperty() {
        this.type = StringProperty.TYPE;
        this.format = "date";
    }

    public static boolean isType(String type, String format) {
        if (StringProperty.TYPE.equals(type) && "date".equals(format)) {
            return true;
        }
        return false;
    }

    public DateProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public DateProperty example(String example) {
        setExample(example);
        return this;
    }
}
