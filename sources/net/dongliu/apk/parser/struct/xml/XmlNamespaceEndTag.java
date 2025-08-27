package net.dongliu.apk.parser.struct.xml;

import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/xml/XmlNamespaceEndTag.class */
public class XmlNamespaceEndTag {
    private String prefix;
    private String uri;

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String toString() {
        return this.prefix + SymbolConstants.EQUAL_SYMBOL + this.uri;
    }
}
