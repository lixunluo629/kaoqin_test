package org.apache.catalina.util;

import java.io.IOException;
import java.io.Writer;
import net.dongliu.apk.parser.struct.xml.XmlCData;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/util/XMLWriter.class */
public class XMLWriter {
    public static final int OPENING = 0;
    public static final int CLOSING = 1;
    public static final int NO_CONTENT = 2;
    protected StringBuilder buffer;
    protected final Writer writer;

    public XMLWriter() {
        this(null);
    }

    public XMLWriter(Writer writer) {
        this.buffer = new StringBuilder();
        this.writer = writer;
    }

    public String toString() {
        return this.buffer.toString();
    }

    public void writeProperty(String namespace, String name, String value) {
        writeElement(namespace, name, 0);
        this.buffer.append(value);
        writeElement(namespace, name, 1);
    }

    public void writeElement(String namespace, String name, int type) {
        writeElement(namespace, null, name, type);
    }

    public void writeElement(String namespace, String namespaceInfo, String name, int type) {
        if (namespace != null && namespace.length() > 0) {
            switch (type) {
                case 0:
                    if (namespaceInfo != null) {
                        this.buffer.append("<" + namespace + ":" + name + " xmlns:" + namespace + "=\"" + namespaceInfo + "\">");
                        break;
                    } else {
                        this.buffer.append("<" + namespace + ":" + name + ">");
                        break;
                    }
                case 1:
                    this.buffer.append("</" + namespace + ":" + name + ">\n");
                    break;
                case 2:
                default:
                    if (namespaceInfo != null) {
                        this.buffer.append("<" + namespace + ":" + name + " xmlns:" + namespace + "=\"" + namespaceInfo + "\"/>");
                        break;
                    } else {
                        this.buffer.append("<" + namespace + ":" + name + "/>");
                        break;
                    }
            }
        }
        switch (type) {
            case 0:
                this.buffer.append("<" + name + ">");
                break;
            case 1:
                this.buffer.append("</" + name + ">\n");
                break;
            case 2:
            default:
                this.buffer.append("<" + name + "/>");
                break;
        }
    }

    public void writeText(String text) {
        this.buffer.append(text);
    }

    public void writeData(String data) {
        this.buffer.append(XmlCData.CDATA_START + data + XmlCData.CDATA_END);
    }

    public void writeXMLHeader() {
        this.buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
    }

    public void sendData() throws IOException {
        if (this.writer != null) {
            this.writer.write(this.buffer.toString());
            this.buffer = new StringBuilder();
        }
    }
}
