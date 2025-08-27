package net.dongliu.apk.parser.struct.xml;

import java.util.Locale;
import java.util.Map;
import net.dongliu.apk.parser.struct.ResourceValue;
import net.dongliu.apk.parser.struct.resource.ResourceTable;
import net.dongliu.apk.parser.utils.ResourceLoader;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/xml/Attribute.class */
public class Attribute {
    private String namespace;
    private String name;
    private String rawValue;
    private ResourceValue typedValue;
    private String value;

    public String toStringValue(ResourceTable resourceTable, Locale locale) {
        if (this.rawValue != null) {
            return this.rawValue;
        }
        if (this.typedValue != null) {
            return this.typedValue.toStringValue(resourceTable, locale);
        }
        return "";
    }

    /* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/xml/Attribute$AttrIds.class */
    public static class AttrIds {
        private static final Map<Integer, String> ids = ResourceLoader.loadSystemAttrIds();

        public static String getString(long id) {
            String value = ids.get(Integer.valueOf((int) id));
            if (value == null) {
                value = "AttrId:0x" + Long.toHexString(id);
            }
            return value;
        }
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRawValue() {
        return this.rawValue;
    }

    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }

    public ResourceValue getTypedValue() {
        return this.typedValue;
    }

    public void setTypedValue(ResourceValue typedValue) {
        this.typedValue = typedValue;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "Attribute{name='" + this.name + "', namespace='" + this.namespace + "'}";
    }
}
