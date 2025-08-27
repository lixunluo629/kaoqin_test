package org.apache.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Calendar;
import javax.xml.stream.XMLStreamReader;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.w3c.dom.Node;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlGMonth.class */
public interface XmlGMonth extends XmlAnySimpleType {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_gMonth");

    Calendar getCalendarValue();

    void setCalendarValue(Calendar calendar);

    GDate getGDateValue();

    void setGDateValue(GDate gDate);

    int getIntValue();

    void setIntValue(int i);

    Calendar calendarValue();

    void set(Calendar calendar);

    GDate gDateValue();

    void set(GDateSpecification gDateSpecification);

    int intValue();

    void set(int i);

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlGMonth$Factory.class */
    public static final class Factory {
        public static XmlGMonth newInstance() {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().newInstance(XmlGMonth.type, null);
        }

        public static XmlGMonth newInstance(XmlOptions options) {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().newInstance(XmlGMonth.type, options);
        }

        public static XmlGMonth newValue(Object obj) {
            return (XmlGMonth) XmlGMonth.type.newValue(obj);
        }

        public static XmlGMonth parse(String s) throws XmlException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(s, XmlGMonth.type, (XmlOptions) null);
        }

        public static XmlGMonth parse(String s, XmlOptions options) throws XmlException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(s, XmlGMonth.type, options);
        }

        public static XmlGMonth parse(File f) throws XmlException, IOException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(f, XmlGMonth.type, (XmlOptions) null);
        }

        public static XmlGMonth parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(f, XmlGMonth.type, options);
        }

        public static XmlGMonth parse(URL u) throws XmlException, IOException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(u, XmlGMonth.type, (XmlOptions) null);
        }

        public static XmlGMonth parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(u, XmlGMonth.type, options);
        }

        public static XmlGMonth parse(InputStream is) throws XmlException, IOException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(is, XmlGMonth.type, (XmlOptions) null);
        }

        public static XmlGMonth parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(is, XmlGMonth.type, options);
        }

        public static XmlGMonth parse(Reader r) throws XmlException, IOException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(r, XmlGMonth.type, (XmlOptions) null);
        }

        public static XmlGMonth parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(r, XmlGMonth.type, options);
        }

        public static XmlGMonth parse(Node node) throws XmlException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(node, XmlGMonth.type, (XmlOptions) null);
        }

        public static XmlGMonth parse(Node node, XmlOptions options) throws XmlException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(node, XmlGMonth.type, options);
        }

        public static XmlGMonth parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(xis, XmlGMonth.type, (XmlOptions) null);
        }

        public static XmlGMonth parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(xis, XmlGMonth.type, options);
        }

        public static XmlGMonth parse(XMLStreamReader xsr) throws XmlException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(xsr, XmlGMonth.type, (XmlOptions) null);
        }

        public static XmlGMonth parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlGMonth) XmlBeans.getContextTypeLoader().parse(xsr, XmlGMonth.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlGMonth.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlGMonth.type, options);
        }

        private Factory() {
        }
    }
}
