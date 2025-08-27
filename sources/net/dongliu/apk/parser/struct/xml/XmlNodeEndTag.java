package net.dongliu.apk.parser.struct.xml;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/struct/xml/XmlNodeEndTag.class */
public class XmlNodeEndTag {
    private String namespace;
    private String name;

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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("</");
        if (this.namespace != null) {
            sb.append(this.namespace).append(":");
        }
        sb.append(this.name).append('>');
        return sb.toString();
    }
}
