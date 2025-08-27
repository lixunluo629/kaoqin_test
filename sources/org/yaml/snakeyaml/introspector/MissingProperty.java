package org.yaml.snakeyaml.introspector;

/* loaded from: snakeyaml-1.17.jar:org/yaml/snakeyaml/introspector/MissingProperty.class */
public class MissingProperty extends Property {
    public MissingProperty(String name) {
        super(name, Object.class);
    }

    @Override // org.yaml.snakeyaml.introspector.Property
    public Class<?>[] getActualTypeArguments() {
        return new Class[0];
    }

    @Override // org.yaml.snakeyaml.introspector.Property
    public void set(Object object, Object value) throws Exception {
    }

    @Override // org.yaml.snakeyaml.introspector.Property
    public Object get(Object object) {
        return object;
    }
}
