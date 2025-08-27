package net.dongliu.apk.parser.struct.xml;

import javax.annotation.Nullable;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/xml/Attributes.class */
public class Attributes {
    private final Attribute[] attributes;

    public Attributes(int size) {
        this.attributes = new Attribute[size];
    }

    public void set(int i, Attribute attribute) {
        this.attributes[i] = attribute;
    }

    @Nullable
    public Attribute get(String name) {
        for (Attribute attribute : this.attributes) {
            if (attribute.getName().equals(name)) {
                return attribute;
            }
        }
        return null;
    }

    @Nullable
    public String getString(String name) {
        Attribute attribute = get(name);
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    public int size() {
        return this.attributes.length;
    }

    public boolean getBoolean(String name, boolean b) {
        String value = getString(name);
        return value == null ? b : Boolean.parseBoolean(value);
    }

    @Nullable
    public Integer getInt(String name) {
        String value = getString(name);
        if (value == null) {
            return null;
        }
        if (value.startsWith("0x")) {
            return Integer.valueOf(value.substring(2), 16);
        }
        return Integer.valueOf(value);
    }

    @Nullable
    public Long getLong(String name) {
        String value = getString(name);
        if (value == null) {
            return null;
        }
        if (value.startsWith("0x")) {
            return Long.valueOf(value.substring(2), 16);
        }
        return Long.valueOf(value);
    }

    public Attribute[] values() {
        return this.attributes;
    }
}
