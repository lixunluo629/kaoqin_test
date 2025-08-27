package io.swagger.models.properties;

import io.swagger.models.Xml;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/DateTimeProperty.class */
public class DateTimeProperty extends AbstractProperty implements Property {
    public DateTimeProperty() {
        this.type = StringProperty.TYPE;
        this.format = "date-time";
    }

    public static boolean isType(String type, String format) {
        if (StringProperty.TYPE.equals(type) && "date-time".equals(format)) {
            return true;
        }
        return false;
    }

    public DateTimeProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public DateTimeProperty example(String example) {
        setExample(example);
        return this;
    }
}
