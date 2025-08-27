package org.apache.xmlbeans.impl.common;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import org.apache.xmlbeans.XmlOptionsBean;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/StaxHelper.class */
public final class StaxHelper {
    private static final XBLogger logger = XBLogFactory.getLogger((Class<?>) StaxHelper.class);

    private StaxHelper() {
    }

    public static XMLInputFactory newXMLInputFactory(XmlOptionsBean options) {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        trySetProperty(factory, XMLInputFactory.IS_NAMESPACE_AWARE, true);
        trySetProperty(factory, XMLInputFactory.IS_VALIDATING, false);
        trySetProperty(factory, XMLInputFactory.SUPPORT_DTD, options.isLoadDTDGrammar());
        trySetProperty(factory, XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, options.isLoadExternalDTD());
        return factory;
    }

    public static XMLOutputFactory newXMLOutputFactory(XmlOptionsBean options) {
        XMLOutputFactory factory = XMLOutputFactory.newFactory();
        trySetProperty(factory, XMLOutputFactory.IS_REPAIRING_NAMESPACES, true);
        return factory;
    }

    public static XMLEventFactory newXMLEventFactory(XmlOptionsBean options) {
        return XMLEventFactory.newFactory();
    }

    private static void trySetProperty(XMLInputFactory factory, String feature, boolean flag) {
        try {
            factory.setProperty(feature, Boolean.valueOf(flag));
        } catch (AbstractMethodError ame) {
            logger.log(5, "Cannot set StAX property because outdated StAX parser in classpath", feature, ame);
        } catch (Exception e) {
            logger.log(5, "StAX Property unsupported", feature, e);
        }
    }

    private static void trySetProperty(XMLOutputFactory factory, String feature, boolean flag) {
        try {
            factory.setProperty(feature, Boolean.valueOf(flag));
        } catch (AbstractMethodError ame) {
            logger.log(5, "Cannot set StAX property because outdated StAX parser in classpath", feature, ame);
        } catch (Exception e) {
            logger.log(5, "StAX Property unsupported", feature, e);
        }
    }
}
