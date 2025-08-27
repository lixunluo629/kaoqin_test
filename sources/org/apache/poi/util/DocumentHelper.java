package org.apache.poi.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.events.Namespace;
import org.apache.xmlbeans.impl.common.Sax2Dom;
import org.apache.xmlbeans.impl.common.XMLBeansConstants;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/util/DocumentHelper.class */
public final class DocumentHelper {
    private static POILogger logger = POILogFactory.getLogger((Class<?>) DocumentHelper.class);
    private static final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    private static final DocumentBuilder documentBuilderSingleton;

    static {
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setValidating(false);
        trySetSAXFeature(documentBuilderFactory, "http://javax.xml.XMLConstants/feature/secure-processing", true);
        trySetXercesSecurityManager(documentBuilderFactory);
        documentBuilderSingleton = newDocumentBuilder();
    }

    private DocumentHelper() {
    }

    /* loaded from: poi-ooxml-3.17.jar:org/apache/poi/util/DocumentHelper$DocHelperErrorHandler.class */
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

    public static synchronized DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            documentBuilder.setEntityResolver(SAXHelper.IGNORING_ENTITY_RESOLVER);
            documentBuilder.setErrorHandler(new DocHelperErrorHandler());
            return documentBuilder;
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("cannot create a DocumentBuilder", e);
        }
    }

    private static void trySetSAXFeature(DocumentBuilderFactory dbf, String feature, boolean enabled) throws ParserConfigurationException {
        try {
            dbf.setFeature(feature, enabled);
        } catch (AbstractMethodError ame) {
            logger.log(5, "Cannot set SAX feature because outdated XML parser in classpath", feature, ame);
        } catch (Exception e) {
            logger.log(5, "SAX Feature unsupported", feature, e);
        }
    }

    private static void trySetXercesSecurityManager(DocumentBuilderFactory dbf) {
        String[] arr$ = {"com.sun.org.apache.xerces.internal.util.SecurityManager", "org.apache.xerces.util.SecurityManager"};
        for (String securityManagerClassName : arr$) {
            try {
                Object mgr = Class.forName(securityManagerClassName).newInstance();
                Method setLimit = mgr.getClass().getMethod("setEntityExpansionLimit", Integer.TYPE);
                setLimit.invoke(mgr, 4096);
                dbf.setAttribute(XMLBeansConstants.SECURITY_MANAGER, mgr);
                return;
            } catch (Throwable e) {
                logger.log(5, "SAX Security Manager could not be setup", e);
            }
        }
    }

    public static Document readDocument(InputStream inp) throws SAXException, IOException {
        return newDocumentBuilder().parse(inp);
    }

    public static Document readDocument(InputSource inp) throws SAXException, IOException {
        return newDocumentBuilder().parse(inp);
    }

    public static synchronized Document createDocument() {
        return documentBuilderSingleton.newDocument();
    }

    public static void addNamespaceDeclaration(Element element, String namespacePrefix, String namespaceURI) throws DOMException {
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", Sax2Dom.XMLNS_STRING + namespacePrefix, namespaceURI);
    }

    public static void addNamespaceDeclaration(Element element, Namespace namespace) throws DOMException {
        addNamespaceDeclaration(element, namespace.getPrefix(), namespace.getNamespaceURI());
    }
}
