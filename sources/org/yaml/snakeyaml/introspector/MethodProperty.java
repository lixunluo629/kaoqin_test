package org.yaml.snakeyaml.introspector;

import java.beans.PropertyDescriptor;
import org.yaml.snakeyaml.error.YAMLException;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/introspector/MethodProperty.class */
public class MethodProperty extends GenericProperty {
    private final PropertyDescriptor property;
    private final boolean readable;
    private final boolean writable;

    public MethodProperty(PropertyDescriptor property) {
        super(property.getName(), property.getPropertyType(), property.getReadMethod() == null ? null : property.getReadMethod().getGenericReturnType());
        this.property = property;
        this.readable = property.getReadMethod() != null;
        this.writable = property.getWriteMethod() != null;
    }

    @Override // org.yaml.snakeyaml.introspector.Property
    public void set(Object object, Object value) throws Exception {
        this.property.getWriteMethod().invoke(object, value);
    }

    @Override // org.yaml.snakeyaml.introspector.Property
    public Object get(Object object) {
        try {
            this.property.getReadMethod().setAccessible(true);
            return this.property.getReadMethod().invoke(object, new Object[0]);
        } catch (Exception e) {
            throw new YAMLException("Unable to find getter for property '" + this.property.getName() + "' on object " + object + ":" + e);
        }
    }

    @Override // org.yaml.snakeyaml.introspector.Property
    public boolean isWritable() {
        return this.writable;
    }

    @Override // org.yaml.snakeyaml.introspector.Property
    public boolean isReadable() {
        return this.readable;
    }
}
