package springfox.documentation.spring.web.readers.parameter;

import java.lang.reflect.Field;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/readers/parameter/ModelAttributeField.class */
public class ModelAttributeField {
    private final Class<?> fieldType;
    private final Field field;

    public ModelAttributeField(Class<?> fieldType, Field field) {
        this.fieldType = fieldType;
        this.field = field;
    }

    public Class<?> getFieldType() {
        return this.fieldType;
    }

    public Field getField() {
        return this.field;
    }
}
