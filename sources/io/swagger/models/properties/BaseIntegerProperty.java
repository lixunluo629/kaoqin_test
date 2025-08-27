package io.swagger.models.properties;

/* loaded from: swagger-models-1.5.3.jar:io/swagger/models/properties/BaseIntegerProperty.class */
public class BaseIntegerProperty extends AbstractNumericProperty {
    public static final String TYPE = "integer";

    public BaseIntegerProperty() {
        this(null);
    }

    public BaseIntegerProperty(String format) {
        this.type = "integer";
        this.format = format;
    }

    public static boolean isType(String type, String format) {
        return "integer".equals(type) && format == null;
    }
}
