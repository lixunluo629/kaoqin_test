package org.apache.xmlbeans.impl.common;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xmlbeans.XmlOptionsBean;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/SAXHelper.class */
public final class SAXHelper {
    private static long lastLog;
    private static final XBLogger logger = XBLogFactory.getLogger((Class<?>) SAXHelper.class);
    public static final EntityResolver IGNORING_ENTITY_RESOLVER = new EntityResolver() { // from class: org.apache.xmlbeans.impl.common.SAXHelper.1
        @Override // org.xml.sax.EntityResolver
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return new InputSource(new StringReader(""));
        }
    };

    private SAXHelper() {
    }

    public static XMLReader newXMLReader(XmlOptionsBean options) throws ParserConfigurationException, SAXException {
        XMLReader xmlReader = saxFactory(options).newSAXParser().getXMLReader();
        xmlReader.setEntityResolver(IGNORING_ENTITY_RESOLVER);
        trySetSAXFeature(xmlReader, "http://javax.xml.XMLConstants/feature/secure-processing");
        trySetXercesSecurityManager(xmlReader, options);
        return xmlReader;
    }

    static SAXParserFactory saxFactory() {
        return saxFactory(new XmlOptionsBean());
    }

    static SAXParserFactory saxFactory(XmlOptionsBean options) throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        saxFactory.setValidating(false);
        saxFactory.setNamespaceAware(true);
        trySetSAXFeature(saxFactory, "http://javax.xml.XMLConstants/feature/secure-processing", true);
        trySetSAXFeature(saxFactory, XMLBeansConstants.FEATURE_LOAD_DTD_GRAMMAR, options.isLoadDTDGrammar());
        trySetSAXFeature(saxFactory, XMLBeansConstants.FEATURE_LOAD_EXTERNAL_DTD, options.isLoadExternalDTD());
        return saxFactory;
    }

    private static void trySetSAXFeature(SAXParserFactory spf, String feature, boolean flag) throws SAXNotRecognizedException, SAXNotSupportedException, ParserConfigurationException {
        try {
            spf.setFeature(feature, flag);
        } catch (AbstractMethodError ame) {
            logger.log(5, "Cannot set SAX feature because outdated XML parser in classpath", feature, ame);
        } catch (Exception e) {
            logger.log(5, "SAX Feature unsupported", feature, e);
        }
    }

    private static void trySetSAXFeature(XMLReader xmlReader, String feature) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            xmlReader.setFeature(feature, true);
        } catch (AbstractMethodError ame) {
            logger.log(5, "Cannot set SAX feature because outdated XML parser in classpath", feature, ame);
        } catch (Exception e) {
            logger.log(5, "SAX Feature unsupported", feature, e);
        }
    }

    private static void trySetXercesSecurityManager(XMLReader xmlReader, XmlOptionsBean options) throws SAXNotRecognizedException, SAXNotSupportedException {
        String[] arr$ = {"org.apache.xerces.util.SecurityManager"};
        for (String securityManagerClassName : arr$) {
            try {
                Object mgr = Class.forName(securityManagerClassName).newInstance();
                Method setLimit = mgr.getClass().getMethod("setEntityExpansionLimit", Integer.TYPE);
                setLimit.invoke(mgr, Integer.valueOf(options.getEntityExpansionLimit()));
                xmlReader.setProperty(XMLBeansConstants.SECURITY_MANAGER, mgr);
                return;
            } catch (Throwable e) {
                if (System.currentTimeMillis() > lastLog + TimeUnit.MINUTES.toMillis(5L)) {
                    logger.log(5, "SAX Security Manager could not be setup [log suppressed for 5 minutes]", e);
                    lastLog = System.currentTimeMillis();
                }
            }
        }
        try {
            xmlReader.setProperty(XMLBeansConstants.ENTITY_EXPANSION_LIMIT, Integer.valueOf(options.getEntityExpansionLimit()));
        } catch (SAXException e2) {
            if (System.currentTimeMillis() > lastLog + TimeUnit.MINUTES.toMillis(5L)) {
                logger.log(5, "SAX Security Manager could not be setup [log suppressed for 5 minutes]", e2);
                lastLog = System.currentTimeMillis();
            }
        }
    }
}
