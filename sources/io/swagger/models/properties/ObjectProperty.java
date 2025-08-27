package io.swagger.models.properties;

import io.swagger.models.Xml;
import java.util.Map;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/ObjectProperty.class */
public class ObjectProperty extends AbstractProperty implements Property {
    public static final String TYPE = "object";
    Map<String, Property> properties;

    public ObjectProperty() {
        this.type = "object";
    }

    public ObjectProperty(Map<String, Property> properties) {
        this.type = "object";
        this.properties = properties;
    }

    public static boolean isType(String type) {
        return "object".equals(type);
    }

    public static boolean isType(String type, String format) {
        return isType(type);
    }

    public ObjectProperty properties(Map<String, Property> properties) {
        setProperties(properties);
        return this;
    }

    public Map<String, Property> getProperties() {
        return this.properties;
    }

    public void setProperties(Map<String, Property> properties) {
        this.properties = properties;
    }

    public ObjectProperty xml(Xml xml) {
        setXml(xml);
        return this;
    }

    public ObjectProperty example(String example) {
        setExample(example);
        return this;
    }
}
