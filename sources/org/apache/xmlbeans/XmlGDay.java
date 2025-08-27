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

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlGDay.class */
public interface XmlGDay extends XmlAnySimpleType {
    public static final SchemaType type = XmlBeans.getBuiltinTypeSystem().typeForHandle("_BI_gDay");

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

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/XmlGDay$Factory.class */
    public static final class Factory {
        public static XmlGDay newInstance() {
            return (XmlGDay) XmlBeans.getContextTypeLoader().newInstance(XmlGDay.type, null);
        }

        public static XmlGDay newInstance(XmlOptions options) {
            return (XmlGDay) XmlBeans.getContextTypeLoader().newInstance(XmlGDay.type, options);
        }

        public static XmlGDay newValue(Object obj) {
            return (XmlGDay) XmlGDay.type.newValue(obj);
        }

        public static XmlGDay parse(String s) throws XmlException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(s, XmlGDay.type, (XmlOptions) null);
        }

        public static XmlGDay parse(String s, XmlOptions options) throws XmlException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(s, XmlGDay.type, options);
        }

        public static XmlGDay parse(File f) throws XmlException, IOException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(f, XmlGDay.type, (XmlOptions) null);
        }

        public static XmlGDay parse(File f, XmlOptions options) throws XmlException, IOException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(f, XmlGDay.type, options);
        }

        public static XmlGDay parse(URL u) throws XmlException, IOException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(u, XmlGDay.type, (XmlOptions) null);
        }

        public static XmlGDay parse(URL u, XmlOptions options) throws XmlException, IOException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(u, XmlGDay.type, options);
        }

        public static XmlGDay parse(InputStream is) throws XmlException, IOException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(is, XmlGDay.type, (XmlOptions) null);
        }

        public static XmlGDay parse(InputStream is, XmlOptions options) throws XmlException, IOException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(is, XmlGDay.type, options);
        }

        public static XmlGDay parse(Reader r) throws XmlException, IOException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(r, XmlGDay.type, (XmlOptions) null);
        }

        public static XmlGDay parse(Reader r, XmlOptions options) throws XmlException, IOException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(r, XmlGDay.type, options);
        }

        public static XmlGDay parse(Node node) throws XmlException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(node, XmlGDay.type, (XmlOptions) null);
        }

        public static XmlGDay parse(Node node, XmlOptions options) throws XmlException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(node, XmlGDay.type, options);
        }

        public static XmlGDay parse(XMLInputStream xis) throws XMLStreamException, XmlException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(xis, XmlGDay.type, (XmlOptions) null);
        }

        public static XmlGDay parse(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(xis, XmlGDay.type, options);
        }

        public static XmlGDay parse(XMLStreamReader xsr) throws XmlException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(xsr, XmlGDay.type, (XmlOptions) null);
        }

        public static XmlGDay parse(XMLStreamReader xsr, XmlOptions options) throws XmlException {
            return (XmlGDay) XmlBeans.getContextTypeLoader().parse(xsr, XmlGDay.type, options);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlGDay.type, null);
        }

        public static XMLInputStream newValidatingXMLInputStream(XMLInputStream xis, XmlOptions options) throws XMLStreamException, XmlException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, XmlGDay.type, options);
        }

        private Factory() {
        }
    }
}
