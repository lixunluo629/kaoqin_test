package org.apache.xmlbeans.impl.common;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import javax.xml.stream.XMLStreamReader;
import net.dongliu.apk.parser.struct.xml.XmlCData;
import org.springframework.beans.PropertyAccessor;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/XmlStreamUtils.class */
public final class XmlStreamUtils {
    public static String printEvent(XMLStreamReader xmlr) {
        StringBuilder b = new StringBuilder();
        b.append("EVENT:[" + xmlr.getLocation().getLineNumber() + "][" + xmlr.getLocation().getColumnNumber() + "] ");
        b.append(getName(xmlr.getEventType()));
        b.append(" [");
        switch (xmlr.getEventType()) {
            case 1:
                b.append("<");
                printName(xmlr, b);
                for (int i = 0; i < xmlr.getNamespaceCount(); i++) {
                    b.append(SymbolConstants.SPACE_SYMBOL);
                    String n = xmlr.getNamespacePrefix(i);
                    if ("xmlns".equals(n)) {
                        b.append("xmlns=\"" + xmlr.getNamespaceURI(i) + SymbolConstants.QUOTES_SYMBOL);
                    } else {
                        b.append(Sax2Dom.XMLNS_STRING + n);
                        b.append("=\"");
                        b.append(xmlr.getNamespaceURI(i));
                        b.append(SymbolConstants.QUOTES_SYMBOL);
                    }
                }
                for (int i2 = 0; i2 < xmlr.getAttributeCount(); i2++) {
                    b.append(SymbolConstants.SPACE_SYMBOL);
                    printName(xmlr.getAttributePrefix(i2), xmlr.getAttributeNamespace(i2), xmlr.getAttributeLocalName(i2), b);
                    b.append("=\"");
                    b.append(xmlr.getAttributeValue(i2));
                    b.append(SymbolConstants.QUOTES_SYMBOL);
                }
                b.append(">");
                break;
            case 2:
                b.append("</");
                printName(xmlr, b);
                for (int i3 = 0; i3 < xmlr.getNamespaceCount(); i3++) {
                    b.append(SymbolConstants.SPACE_SYMBOL);
                    String n2 = xmlr.getNamespacePrefix(i3);
                    if ("xmlns".equals(n2)) {
                        b.append("xmlns=\"" + xmlr.getNamespaceURI(i3) + SymbolConstants.QUOTES_SYMBOL);
                    } else {
                        b.append(Sax2Dom.XMLNS_STRING + n2);
                        b.append("=\"");
                        b.append(xmlr.getNamespaceURI(i3));
                        b.append(SymbolConstants.QUOTES_SYMBOL);
                    }
                }
                b.append(">");
                break;
            case 3:
                String target = xmlr.getPITarget();
                if (target == null) {
                    target = "";
                }
                String data = xmlr.getPIData();
                if (data == null) {
                    data = "";
                }
                b.append("<?");
                b.append(target + SymbolConstants.SPACE_SYMBOL + data);
                b.append("?>");
                break;
            case 4:
            case 6:
                int start = xmlr.getTextStart();
                int length = xmlr.getTextLength();
                b.append(new String(xmlr.getTextCharacters(), start, length));
                break;
            case 5:
                b.append("<!--");
                if (xmlr.hasText()) {
                    b.append(xmlr.getText());
                }
                b.append("-->");
                break;
            case 7:
                b.append("<?xml");
                b.append(" version='" + xmlr.getVersion() + "'");
                b.append(" encoding='" + xmlr.getCharacterEncodingScheme() + "'");
                if (xmlr.isStandalone()) {
                    b.append(" standalone='yes'");
                } else {
                    b.append(" standalone='no'");
                }
                b.append("?>");
                break;
            case 9:
                b.append(xmlr.getLocalName() + SymbolConstants.EQUAL_SYMBOL);
                if (xmlr.hasText()) {
                    b.append(PropertyAccessor.PROPERTY_KEY_PREFIX + xmlr.getText() + "]");
                    break;
                }
                break;
            case 12:
                b.append(XmlCData.CDATA_START);
                if (xmlr.hasText()) {
                    b.append(xmlr.getText());
                }
                b.append(XmlCData.CDATA_END);
                break;
        }
        b.append("]");
        return b.toString();
    }

    private static void printName(String prefix, String uri, String localName, StringBuilder b) {
        if (uri != null && !"".equals(uri)) {
            b.append("['" + uri + "']:");
        }
        if (prefix != null && !"".equals(prefix)) {
            b.append(prefix + ":");
        }
        if (localName != null) {
            b.append(localName);
        }
    }

    private static void printName(XMLStreamReader xmlr, StringBuilder b) {
        if (xmlr.hasName()) {
            String prefix = xmlr.getPrefix();
            String uri = xmlr.getNamespaceURI();
            String localName = xmlr.getLocalName();
            printName(prefix, uri, localName, b);
        }
    }

    public static String getName(int eventType) {
        switch (eventType) {
            case 1:
                return "START_ELEMENT";
            case 2:
                return "END_ELEMENT";
            case 3:
                return "PROCESSING_INSTRUCTION";
            case 4:
                return "CHARACTERS";
            case 5:
                return "COMMENT";
            case 6:
                return "SPACE";
            case 7:
                return "START_DOCUMENT";
            case 8:
                return "END_DOCUMENT";
            case 9:
                return "ENTITY_REFERENCE";
            case 10:
                return "ATTRIBUTE";
            case 11:
                return "DTD";
            case 12:
                return "CDATA";
            case 13:
                return "NAMESPACE";
            default:
                return "UNKNOWN_EVENT_TYPE";
        }
    }

    public static int getType(String val) {
        if (val.equals("START_ELEMENT")) {
            return 1;
        }
        if (val.equals("SPACE")) {
            return 6;
        }
        if (val.equals("END_ELEMENT")) {
            return 2;
        }
        if (val.equals("PROCESSING_INSTRUCTION")) {
            return 3;
        }
        if (val.equals("CHARACTERS")) {
            return 4;
        }
        if (val.equals("COMMENT")) {
            return 5;
        }
        if (val.equals("START_DOCUMENT")) {
            return 7;
        }
        if (val.equals("END_DOCUMENT")) {
            return 8;
        }
        if (val.equals("ATTRIBUTE")) {
            return 10;
        }
        if (val.equals("DTD")) {
            return 11;
        }
        if (val.equals("CDATA")) {
            return 12;
        }
        if (val.equals("NAMESPACE")) {
            return 13;
        }
        return -1;
    }
}
