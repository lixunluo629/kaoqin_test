package org.apache.catalina.util;

import java.io.PrintWriter;
import java.io.Writer;
import net.dongliu.apk.parser.struct.xml.XmlCData;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/util/DOMWriter.class */
public class DOMWriter {
    private static final String PRINTWRITER_ENCODING = "UTF8";

    @Deprecated
    protected final PrintWriter out;

    @Deprecated
    protected final boolean canonical;

    public DOMWriter(Writer writer) {
        this(writer, true);
    }

    @Deprecated
    public DOMWriter(Writer writer, boolean canonical) {
        this.out = new PrintWriter(writer);
        this.canonical = canonical;
    }

    @Deprecated
    public static String getWriterEncoding() {
        return PRINTWRITER_ENCODING;
    }

    public void print(Node node) throws DOMException {
        if (node == null) {
            return;
        }
        int type = node.getNodeType();
        switch (type) {
            case 1:
                this.out.print('<');
                this.out.print(node.getLocalName());
                Attr[] attrs = sortAttributes(node.getAttributes());
                for (Attr attr : attrs) {
                    this.out.print(' ');
                    this.out.print(attr.getLocalName());
                    this.out.print("=\"");
                    this.out.print(normalize(attr.getNodeValue()));
                    this.out.print('\"');
                }
                this.out.print('>');
                printChildren(node);
                break;
            case 3:
                this.out.print(normalize(node.getNodeValue()));
                break;
            case 4:
                if (this.canonical) {
                    this.out.print(normalize(node.getNodeValue()));
                    break;
                } else {
                    this.out.print(XmlCData.CDATA_START);
                    this.out.print(node.getNodeValue());
                    this.out.print(XmlCData.CDATA_END);
                    break;
                }
            case 5:
                if (this.canonical) {
                    printChildren(node);
                    break;
                } else {
                    this.out.print('&');
                    this.out.print(node.getLocalName());
                    this.out.print(';');
                    break;
                }
            case 7:
                this.out.print("<?");
                this.out.print(node.getLocalName());
                String data = node.getNodeValue();
                if (data != null && data.length() > 0) {
                    this.out.print(' ');
                    this.out.print(data);
                }
                this.out.print("?>");
                break;
            case 9:
                if (!this.canonical) {
                    this.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                }
                print(((Document) node).getDocumentElement());
                this.out.flush();
                break;
        }
        if (type == 1) {
            this.out.print("</");
            this.out.print(node.getLocalName());
            this.out.print('>');
        }
        this.out.flush();
    }

    private void printChildren(Node node) {
        NodeList children = node.getChildNodes();
        if (children != null) {
            int len = children.getLength();
            for (int i = 0; i < len; i++) {
                print(children.item(i));
            }
        }
    }

    @Deprecated
    protected Attr[] sortAttributes(NamedNodeMap attrs) {
        if (attrs == null) {
            return new Attr[0];
        }
        int len = attrs.getLength();
        Attr[] array = new Attr[len];
        for (int i = 0; i < len; i++) {
            array[i] = (Attr) attrs.item(i);
        }
        for (int i2 = 0; i2 < len - 1; i2++) {
            String name = array[i2].getLocalName();
            int index = i2;
            for (int j = i2 + 1; j < len; j++) {
                String curName = array[j].getLocalName();
                if (curName.compareTo(name) < 0) {
                    name = curName;
                    index = j;
                }
            }
            if (index != i2) {
                Attr temp = array[i2];
                array[i2] = array[index];
                array[index] = temp;
            }
        }
        return array;
    }

    @Deprecated
    protected String normalize(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char ch2 = s.charAt(i);
            switch (ch2) {
                case '\n':
                case '\r':
                    if (this.canonical) {
                        str.append("&#");
                        str.append(Integer.toString(ch2));
                        str.append(';');
                    }
                    break;
                case '\"':
                    str.append("&quot;");
                    continue;
                case '&':
                    str.append("&amp;");
                    continue;
                case '<':
                    str.append("&lt;");
                    continue;
                case '>':
                    str.append("&gt;");
                    continue;
            }
            str.append(ch2);
        }
        return str.toString();
    }
}
