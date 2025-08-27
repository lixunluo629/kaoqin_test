package com.adobe.xmp.impl;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPError;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.XMPSchemaRegistry;
import com.adobe.xmp.options.PropertyOptions;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: xmpcore-5.1.3.jar:com/adobe/xmp/impl/ParseRDF.class */
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

    static XMPMetaImpl parse(Node node) throws XMPException {
        XMPMetaImpl xMPMetaImpl = new XMPMetaImpl();
        rdf_RDF(xMPMetaImpl, node);
        return xMPMetaImpl;
    }

    static void rdf_RDF(XMPMetaImpl xMPMetaImpl, Node node) throws XMPException, DOMException {
        if (!node.hasAttributes()) {
            throw new XMPException("Invalid attributes of rdf:RDF element", 202);
        }
        rdf_NodeElementList(xMPMetaImpl, xMPMetaImpl.getRoot(), node);
    }

    private static void rdf_NodeElementList(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node) throws XMPException, DOMException {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node nodeItem = node.getChildNodes().item(i);
            if (!isWhitespaceNode(nodeItem)) {
                rdf_NodeElement(xMPMetaImpl, xMPNode, nodeItem, true);
            }
        }
    }

    private static void rdf_NodeElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException, DOMException {
        int rDFTermKind = getRDFTermKind(node);
        if (rDFTermKind != 8 && rDFTermKind != 0) {
            throw new XMPException("Node element must be rdf:Description or typed node", 202);
        }
        if (z && rDFTermKind == 0) {
            throw new XMPException("Top level typed node not allowed", 203);
        }
        rdf_NodeElementAttrs(xMPMetaImpl, xMPNode, node, z);
        rdf_PropertyElementList(xMPMetaImpl, xMPNode, node, z);
    }

    private static void rdf_NodeElementAttrs(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        int i = 0;
        for (int i2 = 0; i2 < node.getAttributes().getLength(); i2++) {
            Node nodeItem = node.getAttributes().item(i2);
            if (!"xmlns".equals(nodeItem.getPrefix()) && (nodeItem.getPrefix() != null || !"xmlns".equals(nodeItem.getNodeName()))) {
                int rDFTermKind = getRDFTermKind(nodeItem);
                switch (rDFTermKind) {
                    case 0:
                        addChildNode(xMPMetaImpl, xMPNode, nodeItem, nodeItem.getNodeValue(), z);
                        break;
                    case 1:
                    case 4:
                    case 5:
                    default:
                        throw new XMPException("Invalid nodeElement attribute", 202);
                    case 2:
                    case 3:
                    case 6:
                        if (i > 0) {
                            throw new XMPException("Mutally exclusive about, ID, nodeID attributes", 202);
                        }
                        i++;
                        if (!z || rDFTermKind != 3) {
                            break;
                        } else if (xMPNode.getName() == null || xMPNode.getName().length() <= 0) {
                            xMPNode.setName(nodeItem.getNodeValue());
                            break;
                        } else {
                            if (!xMPNode.getName().equals(nodeItem.getNodeValue())) {
                                throw new XMPException("Mismatched top level rdf:about values", 203);
                            }
                            break;
                        }
                }
            }
        }
    }

    private static void rdf_PropertyElementList(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException, DOMException {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node nodeItem = node.getChildNodes().item(i);
            if (!isWhitespaceNode(nodeItem)) {
                if (nodeItem.getNodeType() != 1) {
                    throw new XMPException("Expected property element node not found", 202);
                }
                rdf_PropertyElement(xMPMetaImpl, xMPNode, nodeItem, z);
            }
        }
    }

    private static void rdf_PropertyElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException, DOMException {
        if (!isPropertyElementName(getRDFTermKind(node))) {
            throw new XMPException("Invalid property element name", 202);
        }
        NamedNodeMap attributes = node.getAttributes();
        ArrayList arrayList = null;
        for (int i = 0; i < attributes.getLength(); i++) {
            Node nodeItem = attributes.item(i);
            if ("xmlns".equals(nodeItem.getPrefix()) || (nodeItem.getPrefix() == null && "xmlns".equals(nodeItem.getNodeName()))) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(nodeItem.getNodeName());
            }
        }
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                attributes.removeNamedItem((String) it.next());
            }
        }
        if (attributes.getLength() > 3) {
            rdf_EmptyPropertyElement(xMPMetaImpl, xMPNode, node, z);
            return;
        }
        for (int i2 = 0; i2 < attributes.getLength(); i2++) {
            Node nodeItem2 = attributes.item(i2);
            String localName = nodeItem2.getLocalName();
            String namespaceURI = nodeItem2.getNamespaceURI();
            String nodeValue = nodeItem2.getNodeValue();
            if (!"xml:lang".equals(nodeItem2.getNodeName()) || ("ID".equals(localName) && "http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(namespaceURI))) {
                if ("datatype".equals(localName) && "http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(namespaceURI)) {
                    rdf_LiteralPropertyElement(xMPMetaImpl, xMPNode, node, z);
                    return;
                }
                if (!"parseType".equals(localName) || !"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(namespaceURI)) {
                    rdf_EmptyPropertyElement(xMPMetaImpl, xMPNode, node, z);
                    return;
                }
                if ("Literal".equals(nodeValue)) {
                    rdf_ParseTypeLiteralPropertyElement();
                    return;
                }
                if ("Resource".equals(nodeValue)) {
                    rdf_ParseTypeResourcePropertyElement(xMPMetaImpl, xMPNode, node, z);
                    return;
                } else if ("Collection".equals(nodeValue)) {
                    rdf_ParseTypeCollectionPropertyElement();
                    return;
                } else {
                    rdf_ParseTypeOtherPropertyElement();
                    return;
                }
            }
        }
        if (!node.hasChildNodes()) {
            rdf_EmptyPropertyElement(xMPMetaImpl, xMPNode, node, z);
            return;
        }
        for (int i3 = 0; i3 < node.getChildNodes().getLength(); i3++) {
            if (node.getChildNodes().item(i3).getNodeType() != 3) {
                rdf_ResourcePropertyElement(xMPMetaImpl, xMPNode, node, z);
                return;
            }
        }
        rdf_LiteralPropertyElement(xMPMetaImpl, xMPNode, node, z);
    }

    private static void rdf_ResourcePropertyElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        if (z && "iX:changes".equals(node.getNodeName())) {
            return;
        }
        XMPNode xMPNodeAddChildNode = addChildNode(xMPMetaImpl, xMPNode, node, "", z);
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
            Node nodeItem = node.getAttributes().item(i);
            if (!"xmlns".equals(nodeItem.getPrefix()) && (nodeItem.getPrefix() != null || !"xmlns".equals(nodeItem.getNodeName()))) {
                String localName = nodeItem.getLocalName();
                String namespaceURI = nodeItem.getNamespaceURI();
                if ("xml:lang".equals(nodeItem.getNodeName())) {
                    addQualifierNode(xMPNodeAddChildNode, "xml:lang", nodeItem.getNodeValue());
                } else if (!"ID".equals(localName) || !"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(namespaceURI)) {
                    throw new XMPException("Invalid attribute for resource property element", 202);
                }
            }
        }
        boolean z2 = false;
        for (int i2 = 0; i2 < node.getChildNodes().getLength(); i2++) {
            Node nodeItem2 = node.getChildNodes().item(i2);
            if (!isWhitespaceNode(nodeItem2)) {
                if (nodeItem2.getNodeType() != 1 || z2) {
                    if (!z2) {
                        throw new XMPException("Children of resource property element must be XML elements", 202);
                    }
                    throw new XMPException("Invalid child of resource property element", 202);
                }
                boolean zEquals = "http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(nodeItem2.getNamespaceURI());
                String localName2 = nodeItem2.getLocalName();
                if (zEquals && "Bag".equals(localName2)) {
                    xMPNodeAddChildNode.getOptions().setArray(true);
                } else if (zEquals && "Seq".equals(localName2)) {
                    xMPNodeAddChildNode.getOptions().setArray(true).setArrayOrdered(true);
                } else if (zEquals && "Alt".equals(localName2)) {
                    xMPNodeAddChildNode.getOptions().setArray(true).setArrayOrdered(true).setArrayAlternate(true);
                } else {
                    xMPNodeAddChildNode.getOptions().setStruct(true);
                    if (!zEquals && !"Description".equals(localName2)) {
                        String namespaceURI2 = nodeItem2.getNamespaceURI();
                        if (namespaceURI2 == null) {
                            throw new XMPException("All XML elements must be in a namespace", 203);
                        }
                        addQualifierNode(xMPNodeAddChildNode, "rdf:type", namespaceURI2 + ':' + localName2);
                    }
                }
                rdf_NodeElement(xMPMetaImpl, xMPNodeAddChildNode, nodeItem2, false);
                if (xMPNodeAddChildNode.getHasValueChild()) {
                    fixupQualifiedNode(xMPNodeAddChildNode);
                } else if (xMPNodeAddChildNode.getOptions().isArrayAlternate()) {
                    XMPNodeUtils.detectAltText(xMPNodeAddChildNode);
                }
                z2 = true;
            }
        }
        if (!z2) {
            throw new XMPException("Missing child of resource property element", 202);
        }
    }

    private static void rdf_LiteralPropertyElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        XMPNode xMPNodeAddChildNode = addChildNode(xMPMetaImpl, xMPNode, node, null, z);
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
            Node nodeItem = node.getAttributes().item(i);
            if (!"xmlns".equals(nodeItem.getPrefix()) && (nodeItem.getPrefix() != null || !"xmlns".equals(nodeItem.getNodeName()))) {
                String namespaceURI = nodeItem.getNamespaceURI();
                String localName = nodeItem.getLocalName();
                if ("xml:lang".equals(nodeItem.getNodeName())) {
                    addQualifierNode(xMPNodeAddChildNode, "xml:lang", nodeItem.getNodeValue());
                } else if (!"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(namespaceURI) || (!"ID".equals(localName) && !"datatype".equals(localName))) {
                    throw new XMPException("Invalid attribute for literal property element", 202);
                }
            }
        }
        String str = "";
        for (int i2 = 0; i2 < node.getChildNodes().getLength(); i2++) {
            Node nodeItem2 = node.getChildNodes().item(i2);
            if (nodeItem2.getNodeType() != 3) {
                throw new XMPException("Invalid child of literal property element", 202);
            }
            str = str + nodeItem2.getNodeValue();
        }
        xMPNodeAddChildNode.setValue(str);
    }

    private static void rdf_ParseTypeLiteralPropertyElement() throws XMPException {
        throw new XMPException("ParseTypeLiteral property element not allowed", 203);
    }

    private static void rdf_ParseTypeResourcePropertyElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        XMPNode xMPNodeAddChildNode = addChildNode(xMPMetaImpl, xMPNode, node, "", z);
        xMPNodeAddChildNode.getOptions().setStruct(true);
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
            Node nodeItem = node.getAttributes().item(i);
            if (!"xmlns".equals(nodeItem.getPrefix()) && (nodeItem.getPrefix() != null || !"xmlns".equals(nodeItem.getNodeName()))) {
                String localName = nodeItem.getLocalName();
                String namespaceURI = nodeItem.getNamespaceURI();
                if ("xml:lang".equals(nodeItem.getNodeName())) {
                    addQualifierNode(xMPNodeAddChildNode, "xml:lang", nodeItem.getNodeValue());
                } else if (!"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(namespaceURI) || (!"ID".equals(localName) && !"parseType".equals(localName))) {
                    throw new XMPException("Invalid attribute for ParseTypeResource property element", 202);
                }
            }
        }
        rdf_PropertyElementList(xMPMetaImpl, xMPNodeAddChildNode, node, false);
        if (xMPNodeAddChildNode.getHasValueChild()) {
            fixupQualifiedNode(xMPNodeAddChildNode);
        }
    }

    private static void rdf_ParseTypeCollectionPropertyElement() throws XMPException {
        throw new XMPException("ParseTypeCollection property element not allowed", 203);
    }

    private static void rdf_ParseTypeOtherPropertyElement() throws XMPException {
        throw new XMPException("ParseTypeOther property element not allowed", 203);
    }

    private static void rdf_EmptyPropertyElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        Node node2 = null;
        if (node.hasChildNodes()) {
            throw new XMPException("Nested content not allowed with rdf:resource or property attributes", 202);
        }
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
            Node nodeItem = node.getAttributes().item(i);
            if (!"xmlns".equals(nodeItem.getPrefix()) && (nodeItem.getPrefix() != null || !"xmlns".equals(nodeItem.getNodeName()))) {
                switch (getRDFTermKind(nodeItem)) {
                    case 0:
                        if (!"value".equals(nodeItem.getLocalName()) || !"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(nodeItem.getNamespaceURI())) {
                            if ("xml:lang".equals(nodeItem.getNodeName())) {
                                break;
                            } else {
                                z2 = true;
                                break;
                            }
                        } else {
                            if (z3) {
                                throw new XMPException("Empty property element can't have both rdf:value and rdf:resource", 203);
                            }
                            z5 = true;
                            node2 = nodeItem;
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
                        if (z4) {
                            throw new XMPException("Empty property element can't have both rdf:resource and rdf:nodeID", 202);
                        }
                        if (z5) {
                            throw new XMPException("Empty property element can't have both rdf:value and rdf:resource", 203);
                        }
                        z3 = true;
                        if (z5) {
                            break;
                        } else {
                            node2 = nodeItem;
                            break;
                        }
                    case 6:
                        if (z3) {
                            throw new XMPException("Empty property element can't have both rdf:resource and rdf:nodeID", 202);
                        }
                        z4 = true;
                        break;
                }
            }
        }
        XMPNode xMPNodeAddChildNode = addChildNode(xMPMetaImpl, xMPNode, node, "", z);
        boolean z6 = false;
        if (z5 || z3) {
            xMPNodeAddChildNode.setValue(node2 != null ? node2.getNodeValue() : "");
            if (!z5) {
                xMPNodeAddChildNode.getOptions().setURI(true);
            }
        } else if (z2) {
            xMPNodeAddChildNode.getOptions().setStruct(true);
            z6 = true;
        }
        for (int i2 = 0; i2 < node.getAttributes().getLength(); i2++) {
            Node nodeItem2 = node.getAttributes().item(i2);
            if (nodeItem2 != node2 && !"xmlns".equals(nodeItem2.getPrefix()) && (nodeItem2.getPrefix() != null || !"xmlns".equals(nodeItem2.getNodeName()))) {
                switch (getRDFTermKind(nodeItem2)) {
                    case 0:
                        if (z6) {
                            if ("xml:lang".equals(nodeItem2.getNodeName())) {
                                addQualifierNode(xMPNodeAddChildNode, "xml:lang", nodeItem2.getNodeValue());
                                break;
                            } else {
                                addChildNode(xMPMetaImpl, xMPNodeAddChildNode, nodeItem2, nodeItem2.getNodeValue(), false);
                                break;
                            }
                        } else {
                            addQualifierNode(xMPNodeAddChildNode, nodeItem2.getNodeName(), nodeItem2.getNodeValue());
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
                        addQualifierNode(xMPNodeAddChildNode, "rdf:resource", nodeItem2.getNodeValue());
                        break;
                }
            }
        }
    }

    private static XMPNode addChildNode(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, String str, boolean z) throws XMPException {
        XMPSchemaRegistry schemaRegistry = XMPMetaFactory.getSchemaRegistry();
        String namespaceURI = node.getNamespaceURI();
        if (namespaceURI == null) {
            throw new XMPException("XML namespace required for all elements and attributes", 202);
        }
        if ("http://purl.org/dc/1.1/".equals(namespaceURI)) {
            namespaceURI = "http://purl.org/dc/elements/1.1/";
        }
        String namespacePrefix = schemaRegistry.getNamespacePrefix(namespaceURI);
        if (namespacePrefix == null) {
            namespacePrefix = schemaRegistry.registerNamespace(namespaceURI, node.getPrefix() != null ? node.getPrefix() : "_dflt");
        }
        String str2 = namespacePrefix + node.getLocalName();
        PropertyOptions propertyOptions = new PropertyOptions();
        boolean z2 = false;
        if (z) {
            XMPNode xMPNodeFindSchemaNode = XMPNodeUtils.findSchemaNode(xMPMetaImpl.getRoot(), namespaceURI, "_dflt", true);
            xMPNodeFindSchemaNode.setImplicit(false);
            xMPNode = xMPNodeFindSchemaNode;
            if (schemaRegistry.findAlias(str2) != null) {
                z2 = true;
                xMPMetaImpl.getRoot().setHasAliases(true);
                xMPNodeFindSchemaNode.setHasAliases(true);
            }
        }
        boolean zEquals = "rdf:li".equals(str2);
        boolean zEquals2 = "rdf:value".equals(str2);
        XMPNode xMPNode2 = new XMPNode(str2, str, propertyOptions);
        xMPNode2.setAlias(z2);
        if (zEquals2) {
            xMPNode.addChild(1, xMPNode2);
        } else {
            xMPNode.addChild(xMPNode2);
        }
        if (zEquals2) {
            if (z || !xMPNode.getOptions().isStruct()) {
                throw new XMPException("Misplaced rdf:value element", 202);
            }
            xMPNode.setHasValueChild(true);
        }
        if (zEquals) {
            if (!xMPNode.getOptions().isArray()) {
                throw new XMPException("Misplaced rdf:li element", 202);
            }
            xMPNode2.setName("[]");
        }
        return xMPNode2;
    }

    private static XMPNode addQualifierNode(XMPNode xMPNode, String str, String str2) throws XMPException {
        XMPNode xMPNode2 = new XMPNode(str, "xml:lang".equals(str) ? Utils.normalizeLangValue(str2) : str2, null);
        xMPNode.addQualifier(xMPNode2);
        return xMPNode2;
    }

    private static void fixupQualifiedNode(XMPNode xMPNode) throws XMPException {
        if (!$assertionsDisabled && (!xMPNode.getOptions().isStruct() || !xMPNode.hasChildren())) {
            throw new AssertionError();
        }
        XMPNode child = xMPNode.getChild(1);
        if (!$assertionsDisabled && !"rdf:value".equals(child.getName())) {
            throw new AssertionError();
        }
        if (child.getOptions().getHasLanguage()) {
            if (xMPNode.getOptions().getHasLanguage()) {
                throw new XMPException("Redundant xml:lang for rdf:value element", 203);
            }
            XMPNode qualifier = child.getQualifier(1);
            child.removeQualifier(qualifier);
            xMPNode.addQualifier(qualifier);
        }
        for (int i = 1; i <= child.getQualifierLength(); i++) {
            xMPNode.addQualifier(child.getQualifier(i));
        }
        for (int i2 = 2; i2 <= xMPNode.getChildrenLength(); i2++) {
            xMPNode.addQualifier(xMPNode.getChild(i2));
        }
        if (!$assertionsDisabled && !xMPNode.getOptions().isStruct() && !xMPNode.getHasValueChild()) {
            throw new AssertionError();
        }
        xMPNode.setHasValueChild(false);
        xMPNode.getOptions().setStruct(false);
        xMPNode.getOptions().mergeWith(child.getOptions());
        xMPNode.setValue(child.getValue());
        xMPNode.removeChildren();
        Iterator itIterateChildren = child.iterateChildren();
        while (itIterateChildren.hasNext()) {
            xMPNode.addChild((XMPNode) itIterateChildren.next());
        }
    }

    private static boolean isWhitespaceNode(Node node) throws DOMException {
        if (node.getNodeType() != 3) {
            return false;
        }
        String nodeValue = node.getNodeValue();
        for (int i = 0; i < nodeValue.length(); i++) {
            if (!Character.isWhitespace(nodeValue.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPropertyElementName(int i) {
        return (i == 8 || isOldTerm(i) || isCoreSyntaxTerm(i)) ? false : true;
    }

    private static boolean isOldTerm(int i) {
        return 10 <= i && i <= 12;
    }

    private static boolean isCoreSyntaxTerm(int i) {
        return 1 <= i && i <= 7;
    }

    private static int getRDFTermKind(Node node) {
        String localName = node.getLocalName();
        String namespaceURI = node.getNamespaceURI();
        if (namespaceURI == null && (("about".equals(localName) || "ID".equals(localName)) && (node instanceof Attr) && "http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(((Attr) node).getOwnerElement().getNamespaceURI()))) {
            namespaceURI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
        }
        if (!"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(namespaceURI)) {
            return 0;
        }
        if ("li".equals(localName)) {
            return 9;
        }
        if ("parseType".equals(localName)) {
            return 4;
        }
        if ("Description".equals(localName)) {
            return 8;
        }
        if ("about".equals(localName)) {
            return 3;
        }
        if ("resource".equals(localName)) {
            return 5;
        }
        if ("RDF".equals(localName)) {
            return 1;
        }
        if ("ID".equals(localName)) {
            return 2;
        }
        if ("nodeID".equals(localName)) {
            return 6;
        }
        if ("datatype".equals(localName)) {
            return 7;
        }
        if ("aboutEach".equals(localName)) {
            return 10;
        }
        if ("aboutEachPrefix".equals(localName)) {
            return 11;
        }
        return "bagID".equals(localName) ? 12 : 0;
    }

    static {
        $assertionsDisabled = !ParseRDF.class.desiredAssertionStatus();
    }
}
