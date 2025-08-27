package com.adobe.xmp.impl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.options.ParseOptions;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xmlbeans.impl.common.XMLBeansConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/XMPMetaParser.class */
public class XMPMetaParser {
    private static final Object XMP_RDF = new Object();
    private static DocumentBuilderFactory factory = createDocumentBuilderFactory();

    private XMPMetaParser() {
    }

    public static XMPMeta parse(Object obj, ParseOptions parseOptions) throws XMPException {
        ParameterAsserts.assertNotNull(obj);
        ParseOptions parseOptions2 = parseOptions != null ? parseOptions : new ParseOptions();
        Object[] objArrFindRootNode = findRootNode(parseXml(obj, parseOptions2), parseOptions2.getRequireXMPMeta(), new Object[3]);
        if (objArrFindRootNode == null || objArrFindRootNode[1] != XMP_RDF) {
            return new XMPMetaImpl();
        }
        XMPMetaImpl xMPMetaImpl = ParseRDF.parse((Node) objArrFindRootNode[0]);
        xMPMetaImpl.setPacketHeader((String) objArrFindRootNode[2]);
        return !parseOptions2.getOmitNormalization() ? XMPNormalizer.process(xMPMetaImpl, parseOptions2) : xMPMetaImpl;
    }

    private static Document parseXml(Object obj, ParseOptions parseOptions) throws XMPException {
        return obj instanceof InputStream ? parseXmlFromInputStream((InputStream) obj, parseOptions) : obj instanceof byte[] ? parseXmlFromBytebuffer(new ByteBuffer((byte[]) obj), parseOptions) : parseXmlFromString((String) obj, parseOptions);
    }

    private static Document parseXmlFromInputStream(InputStream inputStream, ParseOptions parseOptions) throws XMPException {
        if (!parseOptions.getAcceptLatin1() && !parseOptions.getFixControlChars()) {
            return parseInputSource(new InputSource(inputStream));
        }
        try {
            return parseXmlFromBytebuffer(new ByteBuffer(inputStream), parseOptions);
        } catch (IOException e) {
            throw new XMPException("Error reading the XML-file", 204, e);
        }
    }

    private static Document parseXmlFromBytebuffer(ByteBuffer byteBuffer, ParseOptions parseOptions) throws XMPException {
        InputSource inputSource = new InputSource(byteBuffer.getByteStream());
        try {
            if (parseOptions.getDisallowDoctype()) {
                try {
                    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                } catch (Throwable th) {
                }
            }
            return parseInputSource(inputSource);
        } catch (XMPException e) {
            if (e.getErrorCode() != 201 && e.getErrorCode() != 204) {
                throw e;
            }
            if (parseOptions.getAcceptLatin1()) {
                byteBuffer = Latin1Converter.convert(byteBuffer);
            }
            if (!parseOptions.getFixControlChars()) {
                return parseInputSource(new InputSource(byteBuffer.getByteStream()));
            }
            try {
                return parseInputSource(new InputSource(new FixASCIIControlsReader(new InputStreamReader(byteBuffer.getByteStream(), byteBuffer.getEncoding()))));
            } catch (UnsupportedEncodingException e2) {
                throw new XMPException("Unsupported Encoding", 9, e);
            }
        }
    }

    private static Document parseXmlFromString(String str, ParseOptions parseOptions) throws XMPException {
        new InputSource(new StringReader(str));
        try {
            if (parseOptions.getDisallowDoctype()) {
                try {
                    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                } catch (Throwable th) {
                }
            }
            return parseInputSource(new InputSource(new StringReader(str)));
        } catch (XMPException e) {
            if (e.getErrorCode() == 201 && parseOptions.getFixControlChars()) {
                return parseInputSource(new InputSource(new FixASCIIControlsReader(new StringReader(str))));
            }
            throw e;
        }
    }

    private static Document parseInputSource(InputSource inputSource) throws ParserConfigurationException, XMPException {
        try {
            DocumentBuilder documentBuilderNewDocumentBuilder = factory.newDocumentBuilder();
            documentBuilderNewDocumentBuilder.setErrorHandler(null);
            return documentBuilderNewDocumentBuilder.parse(inputSource);
        } catch (IOException e) {
            throw new XMPException("Error reading the XML-file", 204, e);
        } catch (ParserConfigurationException e2) {
            throw new XMPException("XML Parser not correctly configured", 0, e2);
        } catch (SAXException e3) {
            throw new XMPException("XML parsing failure", 201, e3);
        }
    }

    private static Object[] findRootNode(Node node, boolean z, Object[] objArr) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node nodeItem = childNodes.item(i);
            if (7 == nodeItem.getNodeType() && "xpacket".equals(((ProcessingInstruction) nodeItem).getTarget())) {
                if (objArr != null) {
                    objArr[2] = ((ProcessingInstruction) nodeItem).getData();
                }
            } else if (3 != nodeItem.getNodeType() && 7 != nodeItem.getNodeType()) {
                String namespaceURI = nodeItem.getNamespaceURI();
                String localName = nodeItem.getLocalName();
                if (("xmpmeta".equals(localName) || "xapmeta".equals(localName)) && "adobe:ns:meta/".equals(namespaceURI)) {
                    return findRootNode(nodeItem, false, objArr);
                }
                if (!z && "RDF".equals(localName) && "http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(namespaceURI)) {
                    if (objArr != null) {
                        objArr[0] = nodeItem;
                        objArr[1] = XMP_RDF;
                    }
                    return objArr;
                }
                Object[] objArrFindRootNode = findRootNode(nodeItem, z, objArr);
                if (objArrFindRootNode != null) {
                    return objArrFindRootNode;
                }
            }
        }
        return null;
    }

    private static DocumentBuilderFactory createDocumentBuilderFactory() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactoryNewInstance = DocumentBuilderFactory.newInstance();
        documentBuilderFactoryNewInstance.setNamespaceAware(true);
        documentBuilderFactoryNewInstance.setIgnoringComments(true);
        try {
            documentBuilderFactoryNewInstance.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
            documentBuilderFactoryNewInstance.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            documentBuilderFactoryNewInstance.setFeature("http://xml.org/sax/features/external-general-entities", false);
            documentBuilderFactoryNewInstance.setFeature("http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl", false);
            documentBuilderFactoryNewInstance.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            documentBuilderFactoryNewInstance.setFeature("http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities", false);
            documentBuilderFactoryNewInstance.setFeature(XMLBeansConstants.FEATURE_LOAD_EXTERNAL_DTD, false);
            documentBuilderFactoryNewInstance.setXIncludeAware(false);
            documentBuilderFactoryNewInstance.setExpandEntityReferences(false);
        } catch (Exception e) {
        }
        return documentBuilderFactoryNewInstance;
    }
}
