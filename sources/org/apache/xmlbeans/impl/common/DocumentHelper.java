package org.apache.xmlbeans.impl.common;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xmlbeans.XmlOptionsBean;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/DocumentHelper.class */
public final class DocumentHelper {
    private static long lastLog;
    private static XBLogger logger = XBLogFactory.getLogger((Class<?>) DocumentHelper.class);
    private static final DocumentBuilder documentBuilderSingleton = newDocumentBuilder(new XmlOptionsBean());

    private DocumentHelper() {
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/DocumentHelper$DocHelperErrorHandler.class */
    private static class DocHelperErrorHandler implements ErrorHandler {
        private DocHelperErrorHandler() {
        }

        @Override // org.xml.sax.ErrorHandler
        public void warning(SAXParseException exception) throws SAXException {
            printError(5, exception);
        }

        @Override // org.xml.sax.ErrorHandler
        public void error(SAXParseException exception) throws SAXException {
            printError(7, exception);
        }

        @Override // org.xml.sax.ErrorHandler
        public void fatalError(SAXParseException exception) throws SAXException {
            printError(9, exception);
            throw exception;
        }

        private void printError(int type, SAXParseException ex) {
            StringBuilder sb = new StringBuilder();
            String systemId = ex.getSystemId();
            if (systemId != null) {
                int index = systemId.lastIndexOf(47);
                if (index != -1) {
                    systemId = systemId.substring(index + 1);
                }
                sb.append(systemId);
            }
            sb.append(':');
            sb.append(ex.getLineNumber());
            sb.append(':');
            sb.append(ex.getColumnNumber());
            sb.append(": ");
            sb.append(ex.getMessage());
            DocumentHelper.logger.log(type, sb.toString(), ex);
        }
    }

    public static DocumentBuilder newDocumentBuilder(XmlOptionsBean xmlOptions) throws ParserConfigurationException {
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory(xmlOptions).newDocumentBuilder();
            documentBuilder.setEntityResolver(SAXHelper.IGNORING_ENTITY_RESOLVER);
            documentBuilder.setErrorHandler(new DocHelperErrorHandler());
            return documentBuilder;
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("cannot create a DocumentBuilder", e);
        }
    }

    private static final DocumentBuilderFactory documentBuilderFactory(XmlOptionsBean options) throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setValidating(false);
        trySetFeature(documentBuilderFactory, "http://javax.xml.XMLConstants/feature/secure-processing", true);
        trySetFeature(documentBuilderFactory, XMLBeansConstants.FEATURE_LOAD_DTD_GRAMMAR, options.isLoadDTDGrammar());
        trySetFeature(documentBuilderFactory, XMLBeansConstants.FEATURE_LOAD_EXTERNAL_DTD, options.isLoadExternalDTD());
        trySetXercesSecurityManager(documentBuilderFactory, options);
        return documentBuilderFactory;
    }

    private static void trySetFeature(DocumentBuilderFactory dbf, String feature, boolean enabled) throws ParserConfigurationException {
        try {
            dbf.setFeature(feature, enabled);
        } catch (AbstractMethodError ame) {
            logger.log(5, "Cannot set SAX feature because outdated XML parser in classpath", feature, ame);
        } catch (Exception e) {
            logger.log(5, "SAX Feature unsupported", feature, e);
        }
    }

    private static void trySetXercesSecurityManager(DocumentBuilderFactory dbf, XmlOptionsBean options) {
        String[] arr$ = {"org.apache.xerces.util.SecurityManager"};
        for (String securityManagerClassName : arr$) {
            try {
                Object mgr = Class.forName(securityManagerClassName).newInstance();
                Method setLimit = mgr.getClass().getMethod("setEntityExpansionLimit", Integer.TYPE);
                setLimit.invoke(mgr, Integer.valueOf(options.getEntityExpansionLimit()));
                dbf.setAttribute(XMLBeansConstants.SECURITY_MANAGER, mgr);
                return;
            } catch (ClassNotFoundException e) {
            } catch (Throwable e2) {
                if (System.currentTimeMillis() > lastLog + TimeUnit.MINUTES.toMillis(5L)) {
                    logger.log(5, "DocumentBuilderFactory Security Manager could not be setup [log suppressed for 5 minutes]", e2);
                    lastLog = System.currentTimeMillis();
                }
            }
        }
        try {
            dbf.setAttribute(XMLBeansConstants.ENTITY_EXPANSION_LIMIT, Integer.valueOf(options.getEntityExpansionLimit()));
        } catch (Throwable e3) {
            if (System.currentTimeMillis() > lastLog + TimeUnit.MINUTES.toMillis(5L)) {
                logger.log(5, "DocumentBuilderFactory Entity Expansion Limit could not be setup [log suppressed for 5 minutes]", e3);
                lastLog = System.currentTimeMillis();
            }
        }
    }

    public static Document readDocument(XmlOptionsBean xmlOptions, InputStream inp) throws SAXException, IOException {
        return newDocumentBuilder(xmlOptions).parse(inp);
    }

    public static Document readDocument(XmlOptionsBean xmlOptions, InputSource inp) throws SAXException, IOException {
        return newDocumentBuilder(xmlOptions).parse(inp);
    }

    public static Document createDocument() {
        return documentBuilderSingleton.newDocument();
    }
}
