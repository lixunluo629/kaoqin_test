package net.dongliu.apk.parser.struct.xml;

import java.util.Locale;
import net.dongliu.apk.parser.struct.ResourceValue;
import net.dongliu.apk.parser.struct.resource.ResourceTable;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/xml/XmlCData.class */
public class XmlCData {
    public static final String CDATA_START = "<![CDATA[";
    public static final String CDATA_END = "]]>";
    private String data;
    private ResourceValue typedData;
    private String value;

    public String toStringValue(ResourceTable resourceTable, Locale locale) {
        if (this.data != null) {
            return CDATA_START + this.data + CDATA_END;
        }
        return CDATA_START + this.typedData.toStringValue(resourceTable, locale) + CDATA_END;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ResourceValue getTypedData() {
        return this.typedData;
    }

    public void setTypedData(ResourceValue typedData) {
        this.typedData = typedData;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "XmlCData{data='" + this.data + "', typedData=" + this.typedData + '}';
    }
}
