package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/propertyeditors/CustomMapEditor.class */
public class CustomMapEditor extends PropertyEditorSupport {
    private final Class<? extends Map> mapType;
    private final boolean nullAsEmptyMap;

    public CustomMapEditor(Class<? extends Map> mapType) {
        this(mapType, false);
    }

    public CustomMapEditor(Class<? extends Map> mapType, boolean nullAsEmptyMap) {
        if (mapType == null) {
            throw new IllegalArgumentException("Map type is required");
        }
        if (!Map.class.isAssignableFrom(mapType)) {
            throw new IllegalArgumentException("Map type [" + mapType.getName() + "] does not implement [java.util.Map]");
        }
        this.mapType = mapType;
        this.nullAsEmptyMap = nullAsEmptyMap;
    }

    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text);
    }

    public void setValue(Object value) {
        if (value == null && this.nullAsEmptyMap) {
            super.setValue(createMap(this.mapType, 0));
            return;
        }
        if (value == null || (this.mapType.isInstance(value) && !alwaysCreateNewMap())) {
            super.setValue(value);
            return;
        }
        if (value instanceof Map) {
            Map<?, ?> source = (Map) value;
            Map<Object, Object> target = createMap(this.mapType, source.size());
            for (Map.Entry<?, ?> entry : source.entrySet()) {
                target.put(convertKey(entry.getKey()), convertValue(entry.getValue()));
            }
            super.setValue(target);
            return;
        }
        throw new IllegalArgumentException("Value cannot be converted to Map: " + value);
    }

    protected Map<Object, Object> createMap(Class<? extends Map> mapType, int initialCapacity) {
        if (!mapType.isInterface()) {
            try {
                return mapType.newInstance();
            } catch (Throwable ex) {
                throw new IllegalArgumentException("Could not instantiate map class: " + mapType.getName(), ex);
            }
        }
        if (SortedMap.class == mapType) {
            return new TreeMap();
        }
        return new LinkedHashMap(initialCapacity);
    }

    protected boolean alwaysCreateNewMap() {
        return false;
    }

    protected Object convertKey(Object key) {
        return key;
    }

    protected Object convertValue(Object value) {
        return value;
    }

    public String getAsText() {
        return null;
    }
}
