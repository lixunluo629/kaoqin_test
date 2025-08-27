package com.itextpdf.kernel.xmp.impl;

import com.itextpdf.kernel.xmp.XMPConst;
import com.itextpdf.kernel.xmp.XMPError;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.kernel.xmp.XMPSchemaRegistry;
import com.itextpdf.kernel.xmp.options.PropertyOptions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/ParseRDF.class */
public class ParseRDF implements XMPError, XMPConst {
    public static final int RDFTERM_OTHER = 0;
    public static final int RDFTERM_RDF = 1;
    public static final int RDFTERM_ID = 2;
    public static final int RDFTERM_ABOUT = 3;
    public static final int RDFTERM_PARSE_TYPE = 4;
    public static final int RDFTERM_RESOURCE = 5;
    public static final int RDFTERM_NODE_ID = 6;
    public static final int RDFTERM_DATATYPE = 7;
    public static final int RDFTERM_DESCRIPTION = 8;
    public static final int RDFTERM_LI = 9;
    public static final int RDFTERM_ABOUT_EACH = 10;
    public static final int RDFTERM_ABOUT_EACH_PREFIX = 11;
    public static final int RDFTERM_BAG_ID = 12;
    public static final int RDFTERM_FIRST_CORE = 1;
    public static final int RDFTERM_LAST_CORE = 7;
    public static final int RDFTERM_FIRST_SYNTAX = 1;
    public static final int RDFTERM_LAST_SYNTAX = 9;
    public static final int RDFTERM_FIRST_OLD = 10;
    public static final int RDFTERM_LAST_OLD = 12;
    public static final String DEFAULT_PREFIX = "_dflt";
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ParseRDF.class.desiredAssertionStatus();
    }

    static XMPMetaImpl parse(Node xmlRoot) throws XMPException {
        XMPMetaImpl xmp = new XMPMetaImpl();
        rdf_RDF(xmp, xmlRoot);
        return xmp;
    }

    static void rdf_RDF(XMPMetaImpl xmp, Node rdfRdfNode) throws XMPException, DOMException {
        if (rdfRdfNode.hasAttributes()) {
            rdf_NodeElementList(xmp, xmp.getRoot(), rdfRdfNode);
            return;
        }
        throw new XMPException("Invalid attributes of rdf:RDF element", 202);
    }

    private static void rdf_NodeElementList(XMPMetaImpl xmp, XMPNode xmpParent, Node rdfRdfNode) throws XMPException, DOMException {
        for (int i = 0; i < rdfRdfNode.getChildNodes().getLength(); i++) {
            Node child = rdfRdfNode.getChildNodes().item(i);
            if (!isWhitespaceNode(child)) {
                rdf_NodeElement(xmp, xmpParent, child, true);
            }
        }
    }

    private static void rdf_NodeElement(XMPMetaImpl xmp, XMPNode xmpParent, Node xmlNode, boolean isTopLevel) throws XMPException, DOMException {
        int nodeTerm = getRDFTermKind(xmlNode);
        if (nodeTerm != 8 && nodeTerm != 0) {
            throw new XMPException("Node element must be rdf:Description or typed node", 202);
        }
        if (isTopLevel && nodeTerm == 0) {
            throw new XMPException("Top level typed node not allowed", 203);
        }
        rdf_NodeElementAttrs(xmp, xmpParent, xmlNode, isTopLevel);
        rdf_PropertyElementList(xmp, xmpParent, xmlNode, isTopLevel);
    }

    private static void rdf_NodeElementAttrs(XMPMetaImpl xmp, XMPNode xmpParent, Node xmlNode, boolean isTopLevel) throws XMPException {
        int exclusiveAttrs = 0;
        for (int i = 0; i < xmlNode.getAttributes().getLength(); i++) {
            Node attribute = xmlNode.getAttributes().item(i);
            if (!"xmlns".equals(attribute.getPrefix()) && (attribute.getPrefix() != null || !"xmlns".equals(attribute.getNodeName()))) {
                int attrTerm = getRDFTermKind(attribute);
                switch (attrTerm) {
                    case 0:
                        addChildNode(xmp, xmpParent, attribute, attribute.getNodeValue(), isTopLevel);
                        break;
                    case 1:
                    case 4:
                    case 5:
                    default:
                        throw new XMPException("Invalid nodeElement attribute", 202);
                    case 2:
                    case 3:
                    case 6:
                        if (exclusiveAttrs > 0) {
                            throw new XMPException("Mutally exclusive about, ID, nodeID attributes", 202);
                        }
                        exclusiveAttrs++;
                        if (!isTopLevel || attrTerm != 3) {
                            break;
                        } else if (xmpParent.getName() != null && xmpParent.getName().length() > 0) {
                            if (!xmpParent.getName().equals(attribute.getNodeValue())) {
                                throw new XMPException("Mismatched top level rdf:about values", 203);
                            }
                            break;
                        } else {
                            xmpParent.setName(attribute.getNodeValue());
                            break;
                        }
                }
            }
        }
    }

    private static void rdf_PropertyElementList(XMPMetaImpl xmp, XMPNode xmpParent, Node xmlParent, boolean isTopLevel) throws XMPException, DOMException {
        for (int i = 0; i < xmlParent.getChildNodes().getLength(); i++) {
            Node currChild = xmlParent.getChildNodes().item(i);
            if (!isWhitespaceNode(currChild)) {
                if (currChild.getNodeType() != 1) {
                    throw new XMPException("Expected property element node not found", 202);
                }
                rdf_PropertyElement(xmp, xmpParent, currChild, isTopLevel);
            }
        }
    }

    private static void rdf_PropertyElement(XMPMetaImpl xmp, XMPNode xmpParent, Node xmlNode, boolean isTopLevel) throws XMPException, DOMException {
        int nodeTerm = getRDFTermKind(xmlNode);
        if (!isPropertyElementName(nodeTerm)) {
            throw new XMPException("Invalid property element name", 202);
        }
        NamedNodeMap attributes = xmlNode.getAttributes();
        List<String> nsAttrs = null;
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            if ("xmlns".equals(attribute.getPrefix()) || (attribute.getPrefix() == null && "xmlns".equals(attribute.getNodeName()))) {
                if (nsAttrs == null) {
                    nsAttrs = new ArrayList();
                }
                nsAttrs.add(attribute.getNodeName());
            }
        }
        if (nsAttrs != null) {
            for (String ns : nsAttrs) {
                attributes.removeNamedItem(ns);
            }
        }
        if (attributes.getLength() > 3) {
            rdf_EmptyPropertyElement(xmp, xmpParent, xmlNode, isTopLevel);
            return;
        }
        for (int i2 = 0; i2 < attributes.getLength(); i2++) {
            Node attribute2 = attributes.item(i2);
            String attrLocal = attribute2.getLocalName();
            String attrNS = attribute2.getNamespaceURI();
            String attrValue = attribute2.getNodeValue();
            if (!"xml:lang".equals(attribute2.getNodeName()) || ("ID".equals(attrLocal) && "http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(attrNS))) {
                if ("datatype".equals(attrLocal) && "http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(attrNS)) {
                    rdf_LiteralPropertyElement(xmp, xmpParent, xmlNode, isTopLevel);
                    return;
                }
                if (!"parseType".equals(attrLocal) || !"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(attrNS)) {
                    rdf_EmptyPropertyElement(xmp, xmpParent, xmlNode, isTopLevel);
                    return;
                }
                if ("Literal".equals(attrValue)) {
                    rdf_ParseTypeLiteralPropertyElement();
                    return;
                }
                if ("Resource".equals(attrValue)) {
                    rdf_ParseTypeResourcePropertyElement(xmp, xmpParent, xmlNode, isTopLevel);
                    return;
                } else if ("Collection".equals(attrValue)) {
                    rdf_ParseTypeCollectionPropertyElement();
                    return;
                } else {
                    rdf_ParseTypeOtherPropertyElement();
                    return;
                }
            }
        }
        if (xmlNode.hasChildNodes()) {
            for (int i3 = 0; i3 < xmlNode.getChildNodes().getLength(); i3++) {
                Node currChild = xmlNode.getChildNodes().item(i3);
                if (currChild.getNodeType() != 3) {
                    rdf_ResourcePropertyElement(xmp, xmpParent, xmlNode, isTopLevel);
                    return;
                }
            }
            rdf_LiteralPropertyElement(xmp, xmpParent, xmlNode, isTopLevel);
            return;
        }
        rdf_EmptyPropertyElement(xmp, xmpParent, xmlNode, isTopLevel);
    }

    private static void rdf_ResourcePropertyElement(XMPMetaImpl xmp, XMPNode xmpParent, Node xmlNode, boolean isTopLevel) throws XMPException {
        if (isTopLevel && "iX:changes".equals(xmlNode.getNodeName())) {
            return;
        }
        XMPNode newCompound = addChildNode(xmp, xmpParent, xmlNode, "", isTopLevel);
        for (int i = 0; i < xmlNode.getAttributes().getLength(); i++) {
            Node attribute = xmlNode.getAttributes().item(i);
            if (!"xmlns".equals(attribute.getPrefix()) && (attribute.getPrefix() != null || !"xmlns".equals(attribute.getNodeName()))) {
                String attrLocal = attribute.getLocalName();
                String attrNS = attribute.getNamespaceURI();
                if ("xml:lang".equals(attribute.getNodeName())) {
                    addQualifierNode(newCompound, "xml:lang", attribute.getNodeValue());
                } else if (!"ID".equals(attrLocal) || !"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(attrNS)) {
                    throw new XMPException("Invalid attribute for resource property element", 202);
                }
            }
        }
        boolean found = false;
        for (int i2 = 0; i2 < xmlNode.getChildNodes().getLength(); i2++) {
            Node currChild = xmlNode.getChildNodes().item(i2);
            if (!isWhitespaceNode(currChild)) {
                if (currChild.getNodeType() == 1 && !found) {
                    boolean isRDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(currChild.getNamespaceURI());
                    String childLocal = currChild.getLocalName();
                    if (isRDF && "Bag".equals(childLocal)) {
                        newCompound.getOptions().setArray(true);
                    } else if (isRDF && "Seq".equals(childLocal)) {
                        newCompound.getOptions().setArray(true).setArrayOrdered(true);
                    } else if (isRDF && "Alt".equals(childLocal)) {
                        newCompound.getOptions().setArray(true).setArrayOrdered(true).setArrayAlternate(true);
                    } else {
                        newCompound.getOptions().setStruct(true);
                        if (!isRDF && !"Description".equals(childLocal)) {
                            String typeName = currChild.getNamespaceURI();
                            if (typeName == null) {
                                throw new XMPException("All XML elements must be in a namespace", 203);
                            }
                            addQualifierNode(newCompound, "rdf:type", typeName + ':' + childLocal);
                        }
                    }
                    rdf_NodeElement(xmp, newCompound, currChild, false);
                    if (newCompound.getHasValueChild()) {
                        fixupQualifiedNode(newCompound);
                    } else if (newCompound.getOptions().isArrayAlternate()) {
                        XMPNodeUtils.detectAltText(newCompound);
                    }
                    found = true;
                } else {
                    if (found) {
                        throw new XMPException("Invalid child of resource property element", 202);
                    }
                    throw new XMPException("Children of resource property element must be XML elements", 202);
                }
            }
        }
        if (!found) {
            throw new XMPException("Missing child of resource property element", 202);
        }
    }

    private static void rdf_LiteralPropertyElement(XMPMetaImpl xmp, XMPNode xmpParent, Node xmlNode, boolean isTopLevel) throws XMPException {
        XMPNode newChild = addChildNode(xmp, xmpParent, xmlNode, null, isTopLevel);
        for (int i = 0; i < xmlNode.getAttributes().getLength(); i++) {
            Node attribute = xmlNode.getAttributes().item(i);
            if (!"xmlns".equals(attribute.getPrefix()) && (attribute.getPrefix() != null || !"xmlns".equals(attribute.getNodeName()))) {
                String attrNS = attribute.getNamespaceURI();
                String attrLocal = attribute.getLocalName();
                if ("xml:lang".equals(attribute.getNodeName())) {
                    addQualifierNode(newChild, "xml:lang", attribute.getNodeValue());
                } else if (!"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(attrNS) || (!"ID".equals(attrLocal) && !"datatype".equals(attrLocal))) {
                    throw new XMPException("Invalid attribute for literal property element", 202);
                }
            }
        }
        String textValue = "";
        for (int i2 = 0; i2 < xmlNode.getChildNodes().getLength(); i2++) {
            Node child = xmlNode.getChildNodes().item(i2);
            if (child.getNodeType() == 3) {
                textValue = textValue + child.getNodeValue();
            } else {
                throw new XMPException("Invalid child of literal property element", 202);
            }
        }
        newChild.setValue(textValue);
    }

    private static void rdf_ParseTypeLiteralPropertyElement() throws XMPException {
        throw new XMPException("ParseTypeLiteral property element not allowed", 203);
    }

    private static void rdf_ParseTypeResourcePropertyElement(XMPMetaImpl xmp, XMPNode xmpParent, Node xmlNode, boolean isTopLevel) throws XMPException {
        XMPNode newStruct = addChildNode(xmp, xmpParent, xmlNode, "", isTopLevel);
        newStruct.getOptions().setStruct(true);
        for (int i = 0; i < xmlNode.getAttributes().getLength(); i++) {
            Node attribute = xmlNode.getAttributes().item(i);
            if (!"xmlns".equals(attribute.getPrefix()) && (attribute.getPrefix() != null || !"xmlns".equals(attribute.getNodeName()))) {
                String attrLocal = attribute.getLocalName();
                String attrNS = attribute.getNamespaceURI();
                if ("xml:lang".equals(attribute.getNodeName())) {
                    addQualifierNode(newStruct, "xml:lang", attribute.getNodeValue());
                } else if (!"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(attrNS) || (!"ID".equals(attrLocal) && !"parseType".equals(attrLocal))) {
                    throw new XMPException("Invalid attribute for ParseTypeResource property element", 202);
                }
            }
        }
        rdf_PropertyElementList(xmp, newStruct, xmlNode, false);
        if (newStruct.getHasValueChild()) {
            fixupQualifiedNode(newStruct);
        }
    }

    private static void rdf_ParseTypeCollectionPropertyElement() throws XMPException {
        throw new XMPException("ParseTypeCollection property element not allowed", 203);
    }

    private static void rdf_ParseTypeOtherPropertyElement() throws XMPException {
        throw new XMPException("ParseTypeOther property element not allowed", 203);
    }

    private static void rdf_EmptyPropertyElement(XMPMetaImpl xmp, XMPNode xmpParent, Node xmlNode, boolean isTopLevel) throws XMPException {
        boolean hasPropertyAttrs = false;
        boolean hasResourceAttr = false;
        boolean hasNodeIDAttr = false;
        boolean hasValueAttr = false;
        Node valueNode = null;
        if (xmlNode.hasChildNodes()) {
            throw new XMPException("Nested content not allowed with rdf:resource or property attributes", 202);
        }
        for (int i = 0; i < xmlNode.getAttributes().getLength(); i++) {
            Node attribute = xmlNode.getAttributes().item(i);
            if (!"xmlns".equals(attribute.getPrefix()) && (attribute.getPrefix() != null || !"xmlns".equals(attribute.getNodeName()))) {
                int attrTerm = getRDFTermKind(attribute);
                switch (attrTerm) {
                    case 0:
                        if ("value".equals(attribute.getLocalName()) && "http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(attribute.getNamespaceURI())) {
                            if (hasResourceAttr) {
                                throw new XMPException("Empty property element can't have both rdf:value and rdf:resource", 203);
                            }
                            hasValueAttr = true;
                            valueNode = attribute;
                            break;
                        } else if ("xml:lang".equals(attribute.getNodeName())) {
                            break;
                        } else {
                            hasPropertyAttrs = true;
                            break;
                        }
                        break;
                    case 1:
                    case 3:
                    case 4:
                    default:
                        throw new XMPException("Unrecognized attribute of empty property element", 202);
                    case 2:
                        break;
                    case 5:
                        if (hasNodeIDAttr) {
                            throw new XMPException("Empty property element can't have both rdf:resource and rdf:nodeID", 202);
                        }
                        if (hasValueAttr) {
                            throw new XMPException("Empty property element can't have both rdf:value and rdf:resource", 203);
                        }
                        hasResourceAttr = true;
                        if (hasValueAttr) {
                            break;
                        } else {
                            valueNode = attribute;
                            break;
                        }
                    case 6:
                        if (hasResourceAttr) {
                            throw new XMPException("Empty property element can't have both rdf:resource and rdf:nodeID", 202);
                        }
                        hasNodeIDAttr = true;
                        break;
                }
            }
        }
        XMPNode childNode = addChildNode(xmp, xmpParent, xmlNode, "", isTopLevel);
        boolean childIsStruct = false;
        if (hasValueAttr || hasResourceAttr) {
            childNode.setValue(valueNode != null ? valueNode.getNodeValue() : "");
            if (!hasValueAttr) {
                childNode.getOptions().setURI(true);
            }
        } else if (hasPropertyAttrs) {
            childNode.getOptions().setStruct(true);
            childIsStruct = true;
        }
        for (int i2 = 0; i2 < xmlNode.getAttributes().getLength(); i2++) {
            Node attribute2 = xmlNode.getAttributes().item(i2);
            if (attribute2 != valueNode && !"xmlns".equals(attribute2.getPrefix()) && (attribute2.getPrefix() != null || !"xmlns".equals(attribute2.getNodeName()))) {
                int attrTerm2 = getRDFTermKind(attribute2);
                switch (attrTerm2) {
                    case 0:
                        if (!childIsStruct) {
                            addQualifierNode(childNode, attribute2.getNodeName(), attribute2.getNodeValue());
                            break;
                        } else if ("xml:lang".equals(attribute2.getNodeName())) {
                            addQualifierNode(childNode, "xml:lang", attribute2.getNodeValue());
                            break;
                        } else {
                            addChildNode(xmp, childNode, attribute2, attribute2.getNodeValue(), false);
                            break;
                        }
                    case 1:
                    case 3:
                    case 4:
                    default:
                        throw new XMPException("Unrecognized attribute of empty property element", 202);
                    case 2:
                    case 6:
                        break;
                    case 5:
                        addQualifierNode(childNode, "rdf:resource", attribute2.getNodeValue());
                        break;
                }
            }
        }
    }

    private static XMPNode addChildNode(XMPMetaImpl xmp, XMPNode xmpParent, Node xmlNode, String value, boolean isTopLevel) throws XMPException {
        XMPSchemaRegistry registry = XMPMetaFactory.getSchemaRegistry();
        String namespace = xmlNode.getNamespaceURI();
        if (namespace != null) {
            if ("http://purl.org/dc/1.1/".equals(namespace)) {
                namespace = "http://purl.org/dc/elements/1.1/";
            }
            String prefix = registry.getNamespacePrefix(namespace);
            if (prefix == null) {
                prefix = registry.registerNamespace(namespace, xmlNode.getPrefix() != null ? xmlNode.getPrefix() : "_dflt");
            }
            String childName = prefix + xmlNode.getLocalName();
            PropertyOptions childOptions = new PropertyOptions();
            boolean isAlias = false;
            if (isTopLevel) {
                XMPNode schemaNode = XMPNodeUtils.findSchemaNode(xmp.getRoot(), namespace, "_dflt", true);
                schemaNode.setImplicit(false);
                xmpParent = schemaNode;
                if (registry.findAlias(childName) != null) {
                    isAlias = true;
                    xmp.getRoot().setHasAliases(true);
                    schemaNode.setHasAliases(true);
                }
            }
            boolean isArrayItem = "rdf:li".equals(childName);
            boolean isValueNode = "rdf:value".equals(childName);
            XMPNode newChild = new XMPNode(childName, value, childOptions);
            newChild.setAlias(isAlias);
            if (!isValueNode) {
                xmpParent.addChild(newChild);
            } else {
                xmpParent.addChild(1, newChild);
            }
            if (isValueNode) {
                if (isTopLevel || !xmpParent.getOptions().isStruct()) {
                    throw new XMPException("Misplaced rdf:value element", 202);
                }
                xmpParent.setHasValueChild(true);
            }
            if (isArrayItem) {
                if (!xmpParent.getOptions().isArray()) {
                    throw new XMPException("Misplaced rdf:li element", 202);
                }
                newChild.setName("[]");
            }
            return newChild;
        }
        throw new XMPException("XML namespace required for all elements and attributes", 202);
    }

    private static XMPNode addQualifierNode(XMPNode xmpParent, String name, String value) throws XMPException {
        boolean isLang = "xml:lang".equals(name);
        XMPNode newQual = new XMPNode(name, isLang ? Utils.normalizeLangValue(value) : value, null);
        xmpParent.addQualifier(newQual);
        return newQual;
    }

    private static void fixupQualifiedNode(XMPNode xmpParent) throws XMPException {
        if (!$assertionsDisabled && (!xmpParent.getOptions().isStruct() || !xmpParent.hasChildren())) {
            throw new AssertionError();
        }
        XMPNode valueNode = xmpParent.getChild(1);
        if (!$assertionsDisabled && !"rdf:value".equals(valueNode.getName())) {
            throw new AssertionError();
        }
        if (valueNode.getOptions().getHasLanguage()) {
            if (xmpParent.getOptions().getHasLanguage()) {
                throw new XMPException("Redundant xml:lang for rdf:value element", 203);
            }
            XMPNode langQual = valueNode.getQualifier(1);
            valueNode.removeQualifier(langQual);
            xmpParent.addQualifier(langQual);
        }
        for (int i = 1; i <= valueNode.getQualifierLength(); i++) {
            XMPNode qualifier = valueNode.getQualifier(i);
            xmpParent.addQualifier(qualifier);
        }
        for (int i2 = 2; i2 <= xmpParent.getChildrenLength(); i2++) {
            XMPNode qualifier2 = xmpParent.getChild(i2);
            xmpParent.addQualifier(qualifier2);
        }
        if (!$assertionsDisabled && !xmpParent.getOptions().isStruct() && !xmpParent.getHasValueChild()) {
            throw new AssertionError();
        }
        xmpParent.setHasValueChild(false);
        xmpParent.getOptions().setStruct(false);
        xmpParent.getOptions().mergeWith(valueNode.getOptions());
        xmpParent.setValue(valueNode.getValue());
        xmpParent.removeChildren();
        Iterator it = valueNode.iterateChildren();
        while (it.hasNext()) {
            XMPNode child = (XMPNode) it.next();
            xmpParent.addChild(child);
        }
    }

    private static boolean isWhitespaceNode(Node node) throws DOMException {
        if (node.getNodeType() != 3) {
            return false;
        }
        String value = node.getNodeValue();
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPropertyElementName(int term) {
        return (term == 8 || isOldTerm(term) || isCoreSyntaxTerm(term)) ? false : true;
    }

    private static boolean isOldTerm(int term) {
        return 10 <= term && term <= 12;
    }

    private static boolean isCoreSyntaxTerm(int term) {
        return 1 <= term && term <= 7;
    }

    private static int getRDFTermKind(Node node) {
        String localName = node.getLocalName();
        String namespace = node.getNamespaceURI();
        if (namespace == null && (("about".equals(localName) || "ID".equals(localName)) && (node instanceof Attr) && "http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(((Attr) node).getOwnerElement().getNamespaceURI()))) {
            namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
        }
        if ("http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(namespace)) {
            switch (localName) {
                case "li":
                    return 9;
                case "parseType":
                    return 4;
                case "Description":
                    return 8;
                case "about":
                    return 3;
                case "resource":
                    return 5;
                case "RDF":
                    return 1;
                case "ID":
                    return 2;
                case "nodeID":
                    return 6;
                case "datatype":
                    return 7;
                case "aboutEach":
                    return 10;
                case "aboutEachPrefix":
                    return 11;
                case "bagID":
                    return 12;
                default:
                    return 0;
            }
        }
        return 0;
    }
}
