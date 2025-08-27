package org.apache.poi.util;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xmlbeans.impl.common.XMLBeansConstants;

/* loaded from: poi-3.17.jar:org/apache/poi/util/XMLHelper.class */
public final class XMLHelper {
    private static POILogger logger = POILogFactory.getLogger((Class<?>) XMLHelper.class);

    public static DocumentBuilderFactory getDocumentBuilderFactory() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setExpandEntityReferences(false);
        trySetSAXFeature(factory, "http://javax.xml.XMLConstants/feature/secure-processing", true);
        trySetSAXFeature(factory, "http://xml.org/sax/features/external-general-entities", false);
        trySetSAXFeature(factory, "http://xml.org/sax/features/external-parameter-entities", false);
        trySetSAXFeature(factory, XMLBeansConstants.FEATURE_LOAD_EXTERNAL_DTD, false);
        trySetSAXFeature(factory, XMLBeansConstants.FEATURE_LOAD_DTD_GRAMMAR, false);
        return factory;
    }

    private static void trySetSAXFeature(DocumentBuilderFactory documentBuilderFactory, String feature, boolean enabled) throws ParserConfigurationException {
        try {
            documentBuilderFactory.setFeature(feature, enabled);
        } catch (AbstractMethodError ame) {
            logger.log(5, "Cannot set SAX feature because outdated XML parser in classpath", feature, ame);
        } catch (Exception e) {
            logger.log(5, "SAX Feature unsupported", feature, e);
        }
    }
}
