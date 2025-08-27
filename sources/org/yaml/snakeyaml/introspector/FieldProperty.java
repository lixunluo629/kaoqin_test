package org.yaml.snakeyaml.introspector;

import java.lang.reflect.Field;
import org.yaml.snakeyaml.error.YAMLException;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/introspector/FieldProperty.class */
public class FieldProperty extends GenericProperty {
    private final Field field;

    public FieldProperty(Field field) {
        super(field.getName(), field.getType(), field.getGenericType());
        this.field = field;
        field.setAccessible(true);
    }

    @Override // org.yaml.snakeyaml.introspector.Property
    public void set(Object object, Object value) throws Exception {
        this.field.set(object, value);
    }

    @Override // org.yaml.snakeyaml.introspector.Property
    public Object get(Object object) {
        try {
            return this.field.get(object);
        } catch (Exception e) {
            throw new YAMLException("Unable to access field " + this.field.getName() + " on object " + object + " : " + e);
        }
    }
}
