package org.apache.xmlbeans.impl.store;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlRuntimeException;
import org.apache.xmlbeans.impl.common.XMLChar;
import org.apache.xmlbeans.impl.soap.Detail;
import org.apache.xmlbeans.impl.soap.DetailEntry;
import org.apache.xmlbeans.impl.soap.Name;
import org.apache.xmlbeans.impl.soap.SOAPBody;
import org.apache.xmlbeans.impl.soap.SOAPBodyElement;
import org.apache.xmlbeans.impl.soap.SOAPElement;
import org.apache.xmlbeans.impl.soap.SOAPEnvelope;
import org.apache.xmlbeans.impl.soap.SOAPException;
import org.apache.xmlbeans.impl.soap.SOAPFault;
import org.apache.xmlbeans.impl.soap.SOAPHeader;
import org.apache.xmlbeans.impl.soap.SOAPHeaderElement;
import org.apache.xmlbeans.impl.soap.SOAPPart;
import org.apache.xmlbeans.impl.store.Xobj;
import org.bouncycastle.i18n.TextBundle;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl.class */
final class DomImpl {
    static final int ELEMENT = 1;
    static final int ATTR = 2;
    static final int TEXT = 3;
    static final int CDATA = 4;
    static final int ENTITYREF = 5;
    static final int ENTITY = 6;
    static final int PROCINST = 7;
    static final int COMMENT = 8;
    static final int DOCUMENT = 9;
    static final int DOCTYPE = 10;
    static final int DOCFRAG = 11;
    static final int NOTATION = 12;
    public static NodeList _emptyNodeList;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$Dom.class */
    interface Dom {
        Locale locale();

        int nodeType();

        Cur tempCur();

        QName getQName();

        boolean nodeCanHavePrefixUri();

        void dump();

        void dump(PrintStream printStream);

        void dump(PrintStream printStream, Object obj);
    }

    DomImpl() {
    }

    static {
        $assertionsDisabled = !DomImpl.class.desiredAssertionStatus();
        _emptyNodeList = new EmptyNodeList();
    }

    static Dom parent(Dom d) {
        return node_getParentNode(d);
    }

    static Dom firstChild(Dom d) {
        return node_getFirstChild(d);
    }

    static Dom nextSibling(Dom d) {
        return node_getNextSibling(d);
    }

    static Dom prevSibling(Dom d) {
        return node_getPreviousSibling(d);
    }

    public static Dom append(Dom n, Dom p) {
        return node_insertBefore(p, n, null);
    }

    public static Dom insert(Dom n, Dom b) {
        if ($assertionsDisabled || b != null) {
            return node_insertBefore(parent(b), n, b);
        }
        throw new AssertionError();
    }

    public static Dom remove(Dom n) {
        Dom p = parent(n);
        if (p != null) {
            node_removeChild(p, n);
        }
        return n;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$HierarchyRequestErr.class */
    static class HierarchyRequestErr extends DOMException {
        HierarchyRequestErr() {
            this("This node isn't allowed there");
        }

        HierarchyRequestErr(String message) {
            super((short) 3, message);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$WrongDocumentErr.class */
    static class WrongDocumentErr extends DOMException {
        WrongDocumentErr() {
            this("Nodes do not belong to the same document");
        }

        WrongDocumentErr(String message) {
            super((short) 4, message);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$NotFoundErr.class */
    static class NotFoundErr extends DOMException {
        NotFoundErr() {
            this("Node not found");
        }

        NotFoundErr(String message) {
            super((short) 8, message);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$NamespaceErr.class */
    static class NamespaceErr extends DOMException {
        NamespaceErr() {
            this("Namespace error");
        }

        NamespaceErr(String message) {
            super((short) 14, message);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$NoModificationAllowedErr.class */
    static class NoModificationAllowedErr extends DOMException {
        NoModificationAllowedErr() {
            this("No modification allowed error");
        }

        NoModificationAllowedErr(String message) {
            super((short) 7, message);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$InuseAttributeError.class */
    static class InuseAttributeError extends DOMException {
        InuseAttributeError() {
            this("Attribute currently in use error");
        }

        InuseAttributeError(String message) {
            super((short) 10, message);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$IndexSizeError.class */
    static class IndexSizeError extends DOMException {
        IndexSizeError() {
            this("Index Size Error");
        }

        IndexSizeError(String message) {
            super((short) 1, message);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$NotSupportedError.class */
    static class NotSupportedError extends DOMException {
        NotSupportedError() {
            this("This operation is not supported");
        }

        NotSupportedError(String message) {
            super((short) 9, message);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$InvalidCharacterError.class */
    static class InvalidCharacterError extends DOMException {
        InvalidCharacterError() {
            this("The name contains an invalid character");
        }

        InvalidCharacterError(String message) {
            super((short) 5, message);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$EmptyNodeList.class */
    private static final class EmptyNodeList implements NodeList {
        private EmptyNodeList() {
        }

        @Override // org.w3c.dom.NodeList
        public int getLength() {
            return 0;
        }

        @Override // org.w3c.dom.NodeList
        public Node item(int i) {
            return null;
        }
    }

    static String nodeKindName(int t) {
        switch (t) {
            case 1:
                return "element";
            case 2:
                return BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT;
            case 3:
                return TextBundle.TEXT_ENTRY;
            case 4:
                return "cdata section";
            case 5:
                return "entity reference";
            case 6:
                return "entity";
            case 7:
                return "processing instruction";
            case 8:
                return "comment";
            case 9:
                return "document";
            case 10:
                return "document type";
            case 11:
                return "document fragment";
            case 12:
                return "notation";
            default:
                throw new RuntimeException("Unknown node type");
        }
    }

    private static String isValidChild(Dom parent, Dom child) {
        int pk = parent.nodeType();
        int ck = child.nodeType();
        switch (pk) {
            case 1:
            case 5:
            case 6:
            case 11:
                switch (ck) {
                    case 1:
                    case 3:
                    case 4:
                    case 5:
                    case 7:
                    case 8:
                        return null;
                }
            case 2:
                if (ck == 3 || ck == 5) {
                    return null;
                }
                break;
            case 3:
            case 4:
            case 7:
            case 8:
            case 10:
            case 12:
                return nodeKindName(pk) + " nodes may not have any children";
            case 9:
                switch (ck) {
                    case 1:
                        if (document_getDocumentElement(parent) != null) {
                            return "Documents may only have a maximum of one document element";
                        }
                        return null;
                    case 7:
                    case 8:
                        return null;
                    case 10:
                        if (document_getDoctype(parent) != null) {
                            return "Documents may only have a maximum of one document type node";
                        }
                        return null;
                }
        }
        return nodeKindName(pk) + " nodes may not have " + nodeKindName(ck) + " nodes as children";
    }

    private static void validateNewChild(Dom parent, Dom child) {
        String msg = isValidChild(parent, child);
        if (msg != null) {
            throw new HierarchyRequestErr(msg);
        }
        if (parent == child) {
            throw new HierarchyRequestErr("New child and parent are the same node");
        }
        do {
            Dom domParent = parent(parent);
            parent = domParent;
            if (domParent != null) {
                if (child.nodeType() == 5) {
                    throw new NoModificationAllowedErr("Entity reference trees may not be modified");
                }
            } else {
                return;
            }
        } while (child != parent);
        throw new HierarchyRequestErr("New child is an ancestor node of the parent node");
    }

    private static String validatePrefix(String prefix, String uri, String local, boolean isAttr) {
        validateNcName(prefix);
        if (prefix == null) {
            prefix = "";
        }
        if (uri == null) {
            uri = "";
        }
        if (prefix.length() > 0 && uri.length() == 0) {
            throw new NamespaceErr("Attempt to give a prefix for no namespace");
        }
        if (prefix.equals("xml") && !uri.equals("http://www.w3.org/XML/1998/namespace")) {
            throw new NamespaceErr("Invalid prefix - begins with 'xml'");
        }
        if (isAttr) {
            if (prefix.length() > 0) {
                if (local.equals("xmlns")) {
                    throw new NamespaceErr("Invalid namespace - attr is default namespace already");
                }
                if (Locale.beginsWithXml(local)) {
                    throw new NamespaceErr("Invalid namespace - attr prefix begins with 'xml'");
                }
                if (prefix.equals("xmlns") && !uri.equals("http://www.w3.org/2000/xmlns/")) {
                    throw new NamespaceErr("Invalid namespace - uri is not 'http://www.w3.org/2000/xmlns/;");
                }
            } else if (local.equals("xmlns") && !uri.equals("http://www.w3.org/2000/xmlns/")) {
                throw new NamespaceErr("Invalid namespace - uri is not 'http://www.w3.org/2000/xmlns/;");
            }
        } else if (Locale.beginsWithXml(prefix)) {
            throw new NamespaceErr("Invalid prefix - begins with 'xml'");
        }
        return prefix;
    }

    private static void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }
        if (name.length() == 0) {
            throw new IllegalArgumentException("Name is empty");
        }
        if (!XMLChar.isValidName(name)) {
            throw new InvalidCharacterError("Name has an invalid character");
        }
    }

    private static void validateNcName(String name) {
        if (name != null && name.length() > 0 && !XMLChar.isValidNCName(name)) {
            throw new InvalidCharacterError();
        }
    }

    private static void validateQualifiedName(String name, String uri, boolean isAttr) {
        String local;
        if (!$assertionsDisabled && name == null) {
            throw new AssertionError();
        }
        if (uri == null) {
            uri = "";
        }
        int i = name.indexOf(58);
        if (i < 0) {
            local = name;
            validateNcName(name);
            if (isAttr && local.equals("xmlns") && !uri.equals("http://www.w3.org/2000/xmlns/")) {
                throw new NamespaceErr("Default xmlns attribute does not have namespace: http://www.w3.org/2000/xmlns/");
            }
        } else {
            if (i == 0) {
                throw new NamespaceErr("Invalid qualified name, no prefix specified");
            }
            String prefix = name.substring(0, i);
            validateNcName(prefix);
            if (uri.length() == 0) {
                throw new NamespaceErr("Attempt to give a prefix for no namespace");
            }
            local = name.substring(i + 1);
            if (local.indexOf(58) >= 0) {
                throw new NamespaceErr("Invalid qualified name, more than one colon");
            }
            validateNcName(local);
            if (prefix.equals("xml") && !uri.equals("http://www.w3.org/XML/1998/namespace")) {
                throw new NamespaceErr("Invalid prefix - begins with 'xml'");
            }
        }
        if (local.length() == 0) {
            throw new NamespaceErr("Invalid qualified name, no local part specified");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static void removeNode(Dom dom) {
        CharNode fromNodes;
        if (!$assertionsDisabled && (dom.nodeType() == 3 || dom.nodeType() == 4)) {
            throw new AssertionError();
        }
        Cur cFrom = dom.tempCur();
        cFrom.toEnd();
        if (cFrom.next() && (fromNodes = cFrom.getCharNodes()) != null) {
            cFrom.setCharNodes(null);
            Cur cTo = dom.tempCur();
            cTo.setCharNodes(CharNode.appendNodes(cTo.getCharNodes(), fromNodes));
            cTo.release();
        }
        cFrom.release();
        Cur.moveNode((Xobj) dom, null);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$ElementsNodeList.class */
    private static abstract class ElementsNodeList implements NodeList {
        private Dom _root;
        private Locale _locale;
        private long _version;
        private ArrayList _elements;
        static final /* synthetic */ boolean $assertionsDisabled;

        protected abstract boolean match(Dom dom);

        static {
            $assertionsDisabled = !DomImpl.class.desiredAssertionStatus();
        }

        ElementsNodeList(Dom root) {
            if (!$assertionsDisabled && root.nodeType() != 9 && root.nodeType() != 1) {
                throw new AssertionError();
            }
            this._root = root;
            this._locale = this._root.locale();
            this._version = 0L;
        }

        @Override // org.w3c.dom.NodeList
        public int getLength() {
            ensureElements();
            return this._elements.size();
        }

        @Override // org.w3c.dom.NodeList
        public Node item(int i) {
            ensureElements();
            return (i < 0 || i >= this._elements.size()) ? (Node) null : (Node) this._elements.get(i);
        }

        private void ensureElements() {
            if (this._version == this._locale.version()) {
                return;
            }
            this._version = this._locale.version();
            this._elements = new ArrayList();
            Locale l = this._locale;
            if (!l.noSync()) {
                synchronized (l) {
                    l.enter();
                    try {
                        addElements(this._root);
                        l.exit();
                    } finally {
                    }
                }
                return;
            }
            l.enter();
            try {
                addElements(this._root);
                l.exit();
            } finally {
            }
        }

        private void addElements(Dom node) {
            Dom domFirstChild = DomImpl.firstChild(node);
            while (true) {
                Dom c = domFirstChild;
                if (c != null) {
                    if (c.nodeType() == 1) {
                        if (match(c)) {
                            this._elements.add(c);
                        }
                        addElements(c);
                    }
                    domFirstChild = DomImpl.nextSibling(c);
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$ElementsByTagNameNodeList.class */
    private static class ElementsByTagNameNodeList extends ElementsNodeList {
        private String _name;

        ElementsByTagNameNodeList(Dom root, String name) {
            super(root);
            this._name = name;
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.ElementsNodeList
        protected boolean match(Dom element) {
            if (this._name.equals("*")) {
                return true;
            }
            return DomImpl._node_getNodeName(element).equals(this._name);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$ElementsByTagNameNSNodeList.class */
    private static class ElementsByTagNameNSNodeList extends ElementsNodeList {
        private String _uri;
        private String _local;

        ElementsByTagNameNSNodeList(Dom root, String uri, String local) {
            super(root);
            this._uri = uri == null ? "" : uri;
            this._local = local;
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.ElementsNodeList
        protected boolean match(Dom element) {
            if (!this._uri.equals("*") && !DomImpl._node_getNamespaceURI(element).equals(this._uri)) {
                return false;
            }
            if (this._local.equals("*")) {
                return true;
            }
            return DomImpl._node_getLocalName(element).equals(this._local);
        }
    }

    public static Document _domImplementation_createDocument(Locale l, String u, String n, DocumentType t) {
        Document documentDomImplementation_createDocument;
        if (l.noSync()) {
            l.enter();
            try {
                Document documentDomImplementation_createDocument2 = domImplementation_createDocument(l, u, n, t);
                l.exit();
                return documentDomImplementation_createDocument2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                documentDomImplementation_createDocument = domImplementation_createDocument(l, u, n, t);
                l.exit();
            } finally {
            }
        }
        return documentDomImplementation_createDocument;
    }

    public static Document domImplementation_createDocument(Locale l, String namespaceURI, String qualifiedName, DocumentType doctype) {
        validateQualifiedName(qualifiedName, namespaceURI, false);
        Cur c = l.tempCur();
        c.createDomDocumentRoot();
        Document doc = (Document) c.getDom();
        c.next();
        c.createElement(l.makeQualifiedQName(namespaceURI, qualifiedName));
        if (doctype != null) {
            throw new RuntimeException("Not impl");
        }
        c.toParent();
        try {
            Locale.autoTypeDocument(c, null, null);
            c.release();
            return doc;
        } catch (XmlException e) {
            throw new XmlRuntimeException(e);
        }
    }

    public static boolean _domImplementation_hasFeature(Locale l, String feature, String version) {
        if (feature == null) {
            return false;
        }
        if (version != null && version.length() > 0 && !version.equals("1.0") && !version.equals("2.0")) {
            return false;
        }
        if (feature.equalsIgnoreCase("core") || feature.equalsIgnoreCase("xml")) {
            return true;
        }
        return false;
    }

    public static Element _document_getDocumentElement(Dom d) {
        Dom e;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                e = document_getDocumentElement(d);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    e = document_getDocumentElement(d);
                    l.exit();
                } finally {
                }
            }
        }
        return (Element) e;
    }

    public static Dom document_getDocumentElement(Dom d) {
        Dom domFirstChild = firstChild(d);
        while (true) {
            Dom d2 = domFirstChild;
            if (d2 != null) {
                if (d2.nodeType() != 1) {
                    domFirstChild = nextSibling(d2);
                } else {
                    return d2;
                }
            } else {
                return null;
            }
        }
    }

    public static DocumentFragment _document_createDocumentFragment(Dom d) {
        Dom f;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                f = document_createDocumentFragment(d);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    f = document_createDocumentFragment(d);
                    l.exit();
                } finally {
                }
            }
        }
        return (DocumentFragment) f;
    }

    public static Dom document_createDocumentFragment(Dom d) {
        Cur c = d.locale().tempCur();
        c.createDomDocFragRoot();
        Dom f = c.getDom();
        c.release();
        return f;
    }

    public static Element _document_createElement(Dom d, String name) {
        Dom e;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                e = document_createElement(d, name);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    e = document_createElement(d, name);
                    l.exit();
                } finally {
                }
            }
        }
        return (Element) e;
    }

    public static Dom document_createElement(Dom d, String name) {
        validateName(name);
        Locale l = d.locale();
        Cur c = l.tempCur();
        c.createElement(l.makeQualifiedQName("", name));
        Dom e = c.getDom();
        c.release();
        ((Xobj.ElementXobj) e)._canHavePrefixUri = false;
        return e;
    }

    public static Element _document_createElementNS(Dom d, String uri, String qname) {
        Dom e;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                e = document_createElementNS(d, uri, qname);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    e = document_createElementNS(d, uri, qname);
                    l.exit();
                } finally {
                }
            }
        }
        return (Element) e;
    }

    public static Dom document_createElementNS(Dom d, String uri, String qname) {
        validateQualifiedName(qname, uri, false);
        Locale l = d.locale();
        Cur c = l.tempCur();
        c.createElement(l.makeQualifiedQName(uri, qname));
        Dom e = c.getDom();
        c.release();
        return e;
    }

    public static Attr _document_createAttribute(Dom d, String name) {
        Dom a;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                a = document_createAttribute(d, name);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    a = document_createAttribute(d, name);
                    l.exit();
                } finally {
                }
            }
        }
        return (Attr) a;
    }

    public static Dom document_createAttribute(Dom d, String name) {
        validateName(name);
        Locale l = d.locale();
        Cur c = l.tempCur();
        c.createAttr(l.makeQualifiedQName("", name));
        Dom e = c.getDom();
        c.release();
        ((Xobj.AttrXobj) e)._canHavePrefixUri = false;
        return e;
    }

    public static Attr _document_createAttributeNS(Dom d, String uri, String qname) {
        Dom a;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                a = document_createAttributeNS(d, uri, qname);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    a = document_createAttributeNS(d, uri, qname);
                    l.exit();
                } finally {
                }
            }
        }
        return (Attr) a;
    }

    public static Dom document_createAttributeNS(Dom d, String uri, String qname) {
        validateQualifiedName(qname, uri, true);
        Locale l = d.locale();
        Cur c = l.tempCur();
        c.createAttr(l.makeQualifiedQName(uri, qname));
        Dom e = c.getDom();
        c.release();
        return e;
    }

    public static Comment _document_createComment(Dom d, String data) {
        Dom c;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                c = document_createComment(d, data);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    c = document_createComment(d, data);
                    l.exit();
                } finally {
                }
            }
        }
        return (Comment) c;
    }

    public static Dom document_createComment(Dom d, String data) {
        Locale l = d.locale();
        Cur c = l.tempCur();
        c.createComment();
        Dom comment = c.getDom();
        if (data != null) {
            c.next();
            c.insertString(data);
        }
        c.release();
        return comment;
    }

    public static ProcessingInstruction _document_createProcessingInstruction(Dom d, String target, String data) {
        Dom pi;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                pi = document_createProcessingInstruction(d, target, data);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    pi = document_createProcessingInstruction(d, target, data);
                    l.exit();
                } finally {
                }
            }
        }
        return (ProcessingInstruction) pi;
    }

    public static Dom document_createProcessingInstruction(Dom d, String target, String data) {
        if (target == null) {
            throw new IllegalArgumentException("Target is null");
        }
        if (target.length() == 0) {
            throw new IllegalArgumentException("Target is empty");
        }
        if (!XMLChar.isValidName(target)) {
            throw new InvalidCharacterError("Target has an invalid character");
        }
        if (Locale.beginsWithXml(target) && target.length() == 3) {
            throw new InvalidCharacterError("Invalid target - is 'xml'");
        }
        Locale l = d.locale();
        Cur c = l.tempCur();
        c.createProcinst(target);
        Dom pi = c.getDom();
        if (data != null) {
            c.next();
            c.insertString(data);
        }
        c.release();
        return pi;
    }

    public static CDATASection _document_createCDATASection(Dom d, String data) {
        return (CDATASection) document_createCDATASection(d, data);
    }

    public static Dom document_createCDATASection(Dom d, String data) {
        TextNode t = d.locale().createCdataNode();
        if (data == null) {
            data = "";
        }
        t.setChars(data, 0, data.length());
        return t;
    }

    public static Text _document_createTextNode(Dom d, String data) {
        return (Text) document_createTextNode(d, data);
    }

    public static CharNode document_createTextNode(Dom d, String data) {
        TextNode t = d.locale().createTextNode();
        if (data == null) {
            data = "";
        }
        t.setChars(data, 0, data.length());
        return t;
    }

    public static EntityReference _document_createEntityReference(Dom d, String name) {
        throw new RuntimeException("Not implemented");
    }

    public static Element _document_getElementById(Dom d, String elementId) {
        throw new RuntimeException("Not implemented");
    }

    public static NodeList _document_getElementsByTagName(Dom d, String name) {
        NodeList nodeListDocument_getElementsByTagName;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                NodeList nodeListDocument_getElementsByTagName2 = document_getElementsByTagName(d, name);
                l.exit();
                return nodeListDocument_getElementsByTagName2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                nodeListDocument_getElementsByTagName = document_getElementsByTagName(d, name);
                l.exit();
            } finally {
            }
        }
        return nodeListDocument_getElementsByTagName;
    }

    public static NodeList document_getElementsByTagName(Dom d, String name) {
        return new ElementsByTagNameNodeList(d, name);
    }

    public static NodeList _document_getElementsByTagNameNS(Dom d, String uri, String local) {
        NodeList nodeListDocument_getElementsByTagNameNS;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                NodeList nodeListDocument_getElementsByTagNameNS2 = document_getElementsByTagNameNS(d, uri, local);
                l.exit();
                return nodeListDocument_getElementsByTagNameNS2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                nodeListDocument_getElementsByTagNameNS = document_getElementsByTagNameNS(d, uri, local);
                l.exit();
            } finally {
            }
        }
        return nodeListDocument_getElementsByTagNameNS;
    }

    public static NodeList document_getElementsByTagNameNS(Dom d, String uri, String local) {
        return new ElementsByTagNameNSNodeList(d, uri, local);
    }

    public static DOMImplementation _document_getImplementation(Dom d) {
        return d.locale();
    }

    public static Node _document_importNode(Dom d, Node n, boolean deep) {
        Dom i;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                i = document_importNode(d, n, deep);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    i = document_importNode(d, n, deep);
                    l.exit();
                } finally {
                }
            }
        }
        return (Node) i;
    }

    public static Dom document_importNode(Dom d, Node n, boolean deep) {
        Dom i;
        if (n == null) {
            return null;
        }
        boolean copyChildren = false;
        switch (n.getNodeType()) {
            case 1:
                String local = n.getLocalName();
                if (local == null || local.length() == 0) {
                    i = document_createElement(d, n.getNodeName());
                } else {
                    String prefix = n.getPrefix();
                    String name = (prefix == null || prefix.length() == 0) ? local : prefix + ":" + local;
                    String uri = n.getNamespaceURI();
                    if (uri == null || uri.length() == 0) {
                        i = document_createElement(d, name);
                    } else {
                        i = document_createElementNS(d, uri, name);
                    }
                }
                NamedNodeMap attrs = n.getAttributes();
                for (int a = 0; a < attrs.getLength(); a++) {
                    attributes_setNamedItem(i, document_importNode(d, attrs.item(a), true));
                }
                copyChildren = deep;
                break;
            case 2:
                String local2 = n.getLocalName();
                if (local2 == null || local2.length() == 0) {
                    i = document_createAttribute(d, n.getNodeName());
                } else {
                    String prefix2 = n.getPrefix();
                    String name2 = (prefix2 == null || prefix2.length() == 0) ? local2 : prefix2 + ":" + local2;
                    String uri2 = n.getNamespaceURI();
                    if (uri2 == null || uri2.length() == 0) {
                        i = document_createAttribute(d, name2);
                    } else {
                        i = document_createAttributeNS(d, uri2, name2);
                    }
                }
                copyChildren = true;
                break;
            case 3:
                i = document_createTextNode(d, n.getNodeValue());
                break;
            case 4:
                i = document_createCDATASection(d, n.getNodeValue());
                break;
            case 5:
            case 6:
            case 12:
                throw new RuntimeException("Not impl");
            case 7:
                i = document_createProcessingInstruction(d, n.getNodeName(), n.getNodeValue());
                break;
            case 8:
                i = document_createComment(d, n.getNodeValue());
                break;
            case 9:
                throw new NotSupportedError("Document nodes may not be imported");
            case 10:
                throw new NotSupportedError("Document type nodes may not be imported");
            case 11:
                i = document_createDocumentFragment(d);
                copyChildren = deep;
                break;
            default:
                throw new RuntimeException("Unknown kind");
        }
        if (copyChildren) {
            NodeList children = n.getChildNodes();
            for (int c = 0; c < children.getLength(); c++) {
                node_insertBefore(i, document_importNode(d, children.item(c), true), null);
            }
        }
        return i;
    }

    public static DocumentType _document_getDoctype(Dom d) {
        Dom dt;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                dt = document_getDoctype(d);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    dt = document_getDoctype(d);
                    l.exit();
                } finally {
                }
            }
        }
        return (DocumentType) dt;
    }

    public static Dom document_getDoctype(Dom d) {
        return null;
    }

    public static Document _node_getOwnerDocument(Dom n) {
        Dom d;
        Locale l = n.locale();
        if (l.noSync()) {
            l.enter();
            try {
                d = node_getOwnerDocument(n);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    d = node_getOwnerDocument(n);
                    l.exit();
                } finally {
                }
            }
        }
        return (Document) d;
    }

    public static Dom node_getOwnerDocument(Dom n) {
        if (n.nodeType() == 9) {
            return null;
        }
        Locale l = n.locale();
        if (l._ownerDoc == null) {
            Cur c = l.tempCur();
            c.createDomDocumentRoot();
            l._ownerDoc = c.getDom();
            c.release();
        }
        return l._ownerDoc;
    }

    public static Node _node_getParentNode(Dom n) {
        Dom p;
        Locale l = n.locale();
        if (l.noSync()) {
            l.enter();
            try {
                p = node_getParentNode(n);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    p = node_getParentNode(n);
                    l.exit();
                } finally {
                }
            }
        }
        return (Node) p;
    }

    public static Dom node_getParentNode(Dom n) {
        Cur c = null;
        switch (n.nodeType()) {
            case 1:
            case 7:
            case 8:
                Cur curTempCur = n.tempCur();
                c = curTempCur;
                if (!curTempCur.toParentRaw()) {
                    c.release();
                    c = null;
                    break;
                }
                break;
            case 2:
            case 9:
            case 11:
                break;
            case 3:
            case 4:
                Cur curTempCur2 = n.tempCur();
                c = curTempCur2;
                if (curTempCur2 != null) {
                    c.toParent();
                    break;
                }
                break;
            case 5:
                throw new RuntimeException("Not impl");
            case 6:
            case 10:
            case 12:
                throw new RuntimeException("Not impl");
            default:
                throw new RuntimeException("Unknown kind");
        }
        if (c == null) {
            return null;
        }
        Dom d = c.getDom();
        c.release();
        return d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Node _node_getFirstChild(Dom dom) {
        Dom fc;
        Locale l = dom.locale();
        if (!$assertionsDisabled && !(dom instanceof Xobj)) {
            throw new AssertionError();
        }
        Xobj node = (Xobj) dom;
        if (!node.isVacant()) {
            if (node.isFirstChildPtrDomUsable()) {
                return (Node) node._firstChild;
            }
            Xobj lastAttr = node.lastAttr();
            if (lastAttr != null && lastAttr.isNextSiblingPtrDomUsable()) {
                return (Xobj.NodeXobj) lastAttr._nextSibling;
            }
            if (node.isExistingCharNodesValueUsable()) {
                return node._charNodesValue;
            }
        }
        if (l.noSync()) {
            fc = node_getFirstChild(dom);
        } else {
            synchronized (l) {
                fc = node_getFirstChild(dom);
            }
        }
        return (Node) fc;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Dom node_getFirstChild(Dom dom) {
        switch (dom.nodeType()) {
            case 1:
            case 2:
            case 9:
            case 11:
                Xobj node = (Xobj) dom;
                node.ensureOccupancy();
                if (node.isFirstChildPtrDomUsable()) {
                    return (Xobj.NodeXobj) node._firstChild;
                }
                Xobj lastAttr = node.lastAttr();
                if (lastAttr != null) {
                    if (lastAttr.isNextSiblingPtrDomUsable()) {
                        return (Xobj.NodeXobj) lastAttr._nextSibling;
                    }
                    if (lastAttr.isCharNodesAfterUsable()) {
                        return lastAttr._charNodesAfter;
                    }
                }
                if (node.isCharNodesValueUsable()) {
                    return node._charNodesValue;
                }
                break;
            case 5:
                throw new RuntimeException("Not impl");
            case 6:
            case 10:
            case 12:
                throw new RuntimeException("Not impl");
        }
        return null;
    }

    public static Node _node_getLastChild(Dom n) {
        Dom lc;
        Locale l = n.locale();
        if (l.noSync()) {
            l.enter();
            try {
                lc = node_getLastChild(n);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    lc = node_getLastChild(n);
                    l.exit();
                } finally {
                }
            }
        }
        return (Node) lc;
    }

    public static Dom node_getLastChild(Dom n) {
        CharNode nodes;
        switch (n.nodeType()) {
            case 1:
            case 2:
            case 9:
            case 11:
            default:
                Dom lc = null;
                Cur c = n.tempCur();
                if (c.toLastChild()) {
                    lc = c.getDom();
                    c.skip();
                    CharNode charNodes = c.getCharNodes();
                    nodes = charNodes;
                    if (charNodes != null) {
                        lc = null;
                    }
                } else {
                    c.next();
                    nodes = c.getCharNodes();
                }
                if (lc == null && nodes != null) {
                    while (nodes._next != null) {
                        nodes = nodes._next;
                    }
                    lc = nodes;
                }
                c.release();
                return lc;
            case 3:
            case 4:
            case 7:
            case 8:
                return null;
            case 5:
                throw new RuntimeException("Not impl");
            case 6:
            case 10:
            case 12:
                throw new RuntimeException("Not impl");
        }
    }

    public static Node _node_getNextSibling(Dom n) {
        Dom ns;
        Locale l = n.locale();
        if (l.noSync()) {
            ns = node_getNextSibling(n);
        } else {
            synchronized (l) {
                ns = node_getNextSibling(n);
            }
        }
        return (Node) ns;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Dom node_getNextSibling(Dom dom) {
        Dom ns = null;
        switch (dom.nodeType()) {
            case 1:
            case 7:
            case 8:
                if (!$assertionsDisabled && !(dom instanceof Xobj)) {
                    throw new AssertionError("PI, Comments and Elements always backed up by Xobj");
                }
                Xobj node = (Xobj) dom;
                node.ensureOccupancy();
                if (node.isNextSiblingPtrDomUsable()) {
                    return (Xobj.NodeXobj) node._nextSibling;
                }
                if (node.isCharNodesAfterUsable()) {
                    return node._charNodesAfter;
                }
                break;
            case 3:
            case 4:
                CharNode cn = (CharNode) dom;
                if (!(cn._src instanceof Xobj)) {
                    return null;
                }
                Xobj src = (Xobj) cn._src;
                src._charNodesAfter = Cur.updateCharNodes(src._locale, src, src._charNodesAfter, src._cchAfter);
                src._charNodesValue = Cur.updateCharNodes(src._locale, src, src._charNodesValue, src._cchValue);
                if (cn._next != null) {
                    ns = cn._next;
                    break;
                } else {
                    boolean isThisNodeAfterText = cn.isNodeAftertext();
                    if (isThisNodeAfterText) {
                        ns = (Xobj.NodeXobj) src._nextSibling;
                        break;
                    } else {
                        ns = (Xobj.NodeXobj) src._firstChild;
                        break;
                    }
                }
            case 5:
            case 6:
            case 10:
            case 12:
                throw new RuntimeException("Not implemented");
        }
        return ns;
    }

    public static Node _node_getPreviousSibling(Dom n) {
        Dom ps;
        Locale l = n.locale();
        if (l.noSync()) {
            ps = node_getPreviousSibling(n);
        } else {
            synchronized (l) {
                ps = node_getPreviousSibling(n);
            }
        }
        return (Node) ps;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Dom node_getPreviousSibling(Dom dom) {
        Dom prev;
        switch (dom.nodeType()) {
            case 3:
            case 4:
                if (!$assertionsDisabled && !(dom instanceof CharNode)) {
                    throw new AssertionError("Text/CData should be a CharNode");
                }
                CharNode node = (CharNode) dom;
                if (!(node._src instanceof Xobj)) {
                    return null;
                }
                Xobj xobj = (Xobj) node._src;
                xobj.ensureOccupancy();
                boolean isThisNodeAfterText = node.isNodeAftertext();
                prev = node._prev;
                if (prev == null) {
                    prev = isThisNodeAfterText ? (Dom) xobj : xobj._charNodesValue;
                    break;
                }
                break;
            default:
                if (!$assertionsDisabled && !(dom instanceof Xobj)) {
                    throw new AssertionError();
                }
                Xobj node2 = (Xobj) dom;
                prev = (Dom) node2._prevSibling;
                if (prev == null && node2._parent != null) {
                    prev = node_getFirstChild((Dom) node2._parent);
                    break;
                }
                break;
        }
        Dom temp = prev;
        while (temp != null) {
            Dom domNode_getNextSibling = node_getNextSibling(temp);
            temp = domNode_getNextSibling;
            if (domNode_getNextSibling != dom) {
                prev = temp;
            } else {
                return prev;
            }
        }
        return prev;
    }

    public static boolean _node_hasAttributes(Dom n) {
        boolean zNode_hasAttributes;
        Locale l = n.locale();
        if (l.noSync()) {
            l.enter();
            try {
                boolean zNode_hasAttributes2 = node_hasAttributes(n);
                l.exit();
                return zNode_hasAttributes2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                zNode_hasAttributes = node_hasAttributes(n);
                l.exit();
            } finally {
            }
        }
        return zNode_hasAttributes;
    }

    public static boolean node_hasAttributes(Dom n) {
        boolean hasAttrs = false;
        if (n.nodeType() == 1) {
            Cur c = n.tempCur();
            hasAttrs = c.hasAttrs();
            c.release();
        }
        return hasAttrs;
    }

    public static boolean _node_isSupported(Dom n, String feature, String version) {
        return _domImplementation_hasFeature(n.locale(), feature, version);
    }

    public static void _node_normalize(Dom n) {
        Locale l = n.locale();
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    node_normalize(n);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            node_normalize(n);
            l.exit();
        } finally {
        }
    }

    public static void node_normalize(Dom n) {
        switch (n.nodeType()) {
            case 1:
            case 2:
            case 9:
            case 11:
            default:
                Cur c = n.tempCur();
                c.push();
                do {
                    c.nextWithAttrs();
                    CharNode cn = c.getCharNodes();
                    if (cn != null) {
                        if (!c.isText()) {
                            while (cn != null) {
                                cn.setChars(null, 0, 0);
                                cn = CharNode.remove(cn, cn);
                            }
                        } else if (cn._next != null) {
                            while (cn._next != null) {
                                cn.setChars(null, 0, 0);
                                cn = CharNode.remove(cn, cn._next);
                            }
                            cn._cch = Integer.MAX_VALUE;
                        }
                        c.setCharNodes(cn);
                    }
                } while (!c.isAtEndOfLastPush());
                c.release();
                n.locale().invalidateDomCaches(n);
                return;
            case 3:
            case 4:
            case 7:
            case 8:
                return;
            case 5:
                throw new RuntimeException("Not impl");
            case 6:
            case 10:
            case 12:
                throw new RuntimeException("Not impl");
        }
    }

    public static boolean _node_hasChildNodes(Dom n) {
        return _node_getFirstChild(n) != null;
    }

    public static Node _node_appendChild(Dom p, Node newChild) {
        return _node_insertBefore(p, newChild, null);
    }

    public static Node _node_replaceChild(Dom p, Node newChild, Node oldChild) {
        Dom d;
        Locale l = p.locale();
        if (newChild == null) {
            throw new IllegalArgumentException("Child to add is null");
        }
        if (oldChild == null) {
            throw new NotFoundErr("Child to replace is null");
        }
        if (newChild instanceof Dom) {
            Dom nc = (Dom) newChild;
            if (nc.locale() == l) {
                if (oldChild instanceof Dom) {
                    Dom oc = (Dom) oldChild;
                    if (oc.locale() == l) {
                        if (l.noSync()) {
                            l.enter();
                            try {
                                d = node_replaceChild(p, nc, oc);
                                l.exit();
                            } finally {
                            }
                        } else {
                            synchronized (l) {
                                l.enter();
                                try {
                                    d = node_replaceChild(p, nc, oc);
                                    l.exit();
                                } finally {
                                }
                            }
                        }
                        return (Node) d;
                    }
                }
                throw new WrongDocumentErr("Child to replace is from another document");
            }
        }
        throw new WrongDocumentErr("Child to add is from another document");
    }

    public static Dom node_replaceChild(Dom p, Dom newChild, Dom oldChild) {
        Dom nextNode = node_getNextSibling(oldChild);
        node_removeChild(p, oldChild);
        try {
            node_insertBefore(p, newChild, nextNode);
            return oldChild;
        } catch (DOMException e) {
            node_insertBefore(p, oldChild, nextNode);
            throw e;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0051, code lost:
    
        if (r0.locale() != r0) goto L18;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static org.w3c.dom.Node _node_insertBefore(org.apache.xmlbeans.impl.store.DomImpl.Dom r4, org.w3c.dom.Node r5, org.w3c.dom.Node r6) {
        /*
            r0 = r4
            org.apache.xmlbeans.impl.store.Locale r0 = r0.locale()
            r7 = r0
            r0 = r5
            if (r0 != 0) goto L15
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            r1 = r0
            java.lang.String r2 = "Child to add is null"
            r1.<init>(r2)
            throw r0
        L15:
            r0 = r5
            boolean r0 = r0 instanceof org.apache.xmlbeans.impl.store.DomImpl.Dom
            if (r0 == 0) goto L2c
            r0 = r5
            org.apache.xmlbeans.impl.store.DomImpl$Dom r0 = (org.apache.xmlbeans.impl.store.DomImpl.Dom) r0
            r1 = r0
            r8 = r1
            org.apache.xmlbeans.impl.store.Locale r0 = r0.locale()
            r1 = r7
            if (r0 == r1) goto L36
        L2c:
            org.apache.xmlbeans.impl.store.DomImpl$WrongDocumentErr r0 = new org.apache.xmlbeans.impl.store.DomImpl$WrongDocumentErr
            r1 = r0
            java.lang.String r2 = "Child to add is from another document"
            r1.<init>(r2)
            throw r0
        L36:
            r0 = 0
            r9 = r0
            r0 = r6
            if (r0 == 0) goto L5e
            r0 = r6
            boolean r0 = r0 instanceof org.apache.xmlbeans.impl.store.DomImpl.Dom
            if (r0 == 0) goto L54
            r0 = r6
            org.apache.xmlbeans.impl.store.DomImpl$Dom r0 = (org.apache.xmlbeans.impl.store.DomImpl.Dom) r0
            r1 = r0
            r9 = r1
            org.apache.xmlbeans.impl.store.Locale r0 = r0.locale()
            r1 = r7
            if (r0 == r1) goto L5e
        L54:
            org.apache.xmlbeans.impl.store.DomImpl$WrongDocumentErr r0 = new org.apache.xmlbeans.impl.store.DomImpl$WrongDocumentErr
            r1 = r0
            java.lang.String r2 = "Reference child is from another document"
            r1.<init>(r2)
            throw r0
        L5e:
            r0 = r7
            boolean r0 = r0.noSync()
            if (r0 == 0) goto L86
            r0 = r7
            r0.enter()
            r0 = r4
            r1 = r8
            r2 = r9
            org.apache.xmlbeans.impl.store.DomImpl$Dom r0 = node_insertBefore(r0, r1, r2)     // Catch: java.lang.Throwable -> L7a
            r10 = r0
            r0 = r7
            r0.exit()
            goto L83
        L7a:
            r11 = move-exception
            r0 = r7
            r0.exit()
            r0 = r11
            throw r0
        L83:
            goto Lb7
        L86:
            r0 = r7
            r1 = r0
            r11 = r1
            monitor-enter(r0)
            r0 = r7
            r0.enter()     // Catch: java.lang.Throwable -> Laf
            r0 = r4
            r1 = r8
            r2 = r9
            org.apache.xmlbeans.impl.store.DomImpl$Dom r0 = node_insertBefore(r0, r1, r2)     // Catch: java.lang.Throwable -> La0 java.lang.Throwable -> Laf
            r10 = r0
            r0 = r7
            r0.exit()     // Catch: java.lang.Throwable -> Laf
            goto La9
        La0:
            r12 = move-exception
            r0 = r7
            r0.exit()     // Catch: java.lang.Throwable -> Laf
            r0 = r12
            throw r0     // Catch: java.lang.Throwable -> Laf
        La9:
            r0 = r11
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Laf
            goto Lb7
        Laf:
            r13 = move-exception
            r0 = r11
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Laf
            r0 = r13
            throw r0
        Lb7:
            r0 = r10
            org.w3c.dom.Node r0 = (org.w3c.dom.Node) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.store.DomImpl._node_insertBefore(org.apache.xmlbeans.impl.store.DomImpl$Dom, org.w3c.dom.Node, org.w3c.dom.Node):org.w3c.dom.Node");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Dom node_insertBefore(Dom p, Dom dom, Dom rc) {
        if (!$assertionsDisabled && dom == 0) {
            throw new AssertionError();
        }
        if (dom == rc) {
            return dom;
        }
        if (rc != null && parent(rc) != p) {
            throw new NotFoundErr("RefChild is not a child of this node");
        }
        int nck = dom.nodeType();
        if (nck == 11) {
            Dom domFirstChild = firstChild(dom);
            while (true) {
                Dom c = domFirstChild;
                if (c == null) {
                    break;
                }
                validateNewChild(p, c);
                domFirstChild = nextSibling(c);
            }
            Dom domFirstChild2 = firstChild(dom);
            while (true) {
                Dom c2 = domFirstChild2;
                if (c2 != null) {
                    Dom n = nextSibling(c2);
                    if (rc == null) {
                        append(c2, p);
                    } else {
                        insert(c2, rc);
                    }
                    domFirstChild2 = n;
                } else {
                    return dom;
                }
            }
        } else {
            validateNewChild(p, dom);
            remove(dom);
            int pk = p.nodeType();
            if (!$assertionsDisabled && pk != 2 && pk != 11 && pk != 9 && pk != 1) {
                throw new AssertionError();
            }
            switch (nck) {
                case 1:
                case 7:
                case 8:
                    if (rc == null) {
                        Cur cTo = p.tempCur();
                        cTo.toEnd();
                        Cur.moveNode((Xobj) dom, cTo);
                        cTo.release();
                        break;
                    } else {
                        int rck = rc.nodeType();
                        if (rck == 3 || rck == 4) {
                            ArrayList charNodes = new ArrayList();
                            while (rc != null && (rc.nodeType() == 3 || rc.nodeType() == 4)) {
                                Dom next = nextSibling(rc);
                                charNodes.add(remove(rc));
                                rc = next;
                            }
                            if (rc == null) {
                                append(dom, p);
                            } else {
                                insert(dom, rc);
                            }
                            Dom rc2 = nextSibling(dom);
                            for (int i = 0; i < charNodes.size(); i++) {
                                Dom n2 = (Dom) charNodes.get(i);
                                if (rc2 == null) {
                                    append(n2, p);
                                } else {
                                    insert(n2, rc2);
                                }
                            }
                            break;
                        } else {
                            if (rck == 5) {
                                throw new RuntimeException("Not implemented");
                            }
                            if (!$assertionsDisabled && rck != 1 && rck != 7 && rck != 8) {
                                throw new AssertionError();
                            }
                            Cur cTo2 = rc.tempCur();
                            Cur.moveNode((Xobj) dom, cTo2);
                            cTo2.release();
                            break;
                        }
                    }
                    break;
                case 2:
                case 6:
                case 9:
                default:
                    throw new RuntimeException("Unexpected child node type");
                case 3:
                case 4:
                    CharNode n3 = (CharNode) dom;
                    if (!$assertionsDisabled && (n3._prev != null || n3._next != null)) {
                        throw new AssertionError();
                    }
                    CharNode refCharNode = null;
                    Cur c3 = p.tempCur();
                    if (rc == null) {
                        c3.toEnd();
                    } else {
                        int rck2 = rc.nodeType();
                        if (rck2 == 3 || rck2 == 4) {
                            CharNode charNode = (CharNode) rc;
                            refCharNode = charNode;
                            c3.moveToCharNode(charNode);
                        } else {
                            if (rck2 == 5) {
                                throw new RuntimeException("Not implemented");
                            }
                            c3.moveToDom(rc);
                        }
                    }
                    CharNode nodes = c3.getCharNodes();
                    CharNode nodes2 = CharNode.insertNode(nodes, n3, refCharNode);
                    c3.insertChars(n3._src, n3._off, n3._cch);
                    c3.setCharNodes(nodes2);
                    c3.release();
                    break;
                    break;
                case 5:
                    throw new RuntimeException("Not implemented");
                case 10:
                    throw new RuntimeException("Not implemented");
            }
            return dom;
        }
    }

    public static Node _node_removeChild(Dom p, Node child) {
        Dom d;
        Locale l = p.locale();
        if (child == null) {
            throw new NotFoundErr("Child to remove is null");
        }
        if (child instanceof Dom) {
            Dom c = (Dom) child;
            if (c.locale() == l) {
                if (l.noSync()) {
                    l.enter();
                    try {
                        d = node_removeChild(p, c);
                        l.exit();
                    } finally {
                    }
                } else {
                    synchronized (l) {
                        l.enter();
                        try {
                            d = node_removeChild(p, c);
                            l.exit();
                        } finally {
                        }
                    }
                }
                return (Node) d;
            }
        }
        throw new WrongDocumentErr("Child to remove is from another document");
    }

    public static Dom node_removeChild(Dom parent, Dom child) {
        if (parent(child) != parent) {
            throw new NotFoundErr("Child to remove is not a child of given parent");
        }
        switch (child.nodeType()) {
            case 1:
            case 7:
            case 8:
                removeNode(child);
                break;
            case 2:
            case 9:
            case 11:
                throw new IllegalStateException();
            case 3:
            case 4:
                Cur c = child.tempCur();
                CharNode nodes = c.getCharNodes();
                CharNode cn = (CharNode) child;
                if (!$assertionsDisabled && !(cn._src instanceof Dom)) {
                    throw new AssertionError();
                }
                cn.setChars(c.moveChars(null, cn._cch), c._offSrc, c._cchSrc);
                c.setCharNodes(CharNode.remove(nodes, cn));
                c.release();
                break;
            case 5:
                throw new RuntimeException("Not impl");
            case 6:
            case 10:
            case 12:
                throw new RuntimeException("Not impl");
            default:
                throw new RuntimeException("Unknown kind");
        }
        return child;
    }

    public static Node _node_cloneNode(Dom n, boolean deep) {
        Dom c;
        Locale l = n.locale();
        if (l.noSync()) {
            l.enter();
            try {
                c = node_cloneNode(n, deep);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    c = node_cloneNode(n, deep);
                    l.exit();
                } finally {
                }
            }
        }
        return (Node) c;
    }

    public static Dom node_cloneNode(Dom n, boolean deep) throws DOMException {
        Locale l = n.locale();
        Dom clone = null;
        if (!deep) {
            Cur shallow = null;
            switch (n.nodeType()) {
                case 1:
                    shallow = l.tempCur();
                    shallow.createElement(n.getQName());
                    Element elem = (Element) shallow.getDom();
                    NamedNodeMap attrs = ((Element) n).getAttributes();
                    for (int i = 0; i < attrs.getLength(); i++) {
                        elem.setAttributeNodeNS((Attr) attrs.item(i).cloneNode(true));
                    }
                    break;
                case 2:
                    shallow = l.tempCur();
                    shallow.createAttr(n.getQName());
                    break;
                case 9:
                    shallow = l.tempCur();
                    shallow.createDomDocumentRoot();
                    break;
                case 11:
                    shallow = l.tempCur();
                    shallow.createDomDocFragRoot();
                    break;
            }
            if (shallow != null) {
                clone = shallow.getDom();
                shallow.release();
            }
        }
        if (clone == null) {
            switch (n.nodeType()) {
                case 1:
                case 2:
                case 7:
                case 8:
                case 9:
                case 11:
                    Cur cClone = l.tempCur();
                    Cur cSrc = n.tempCur();
                    cSrc.copyNode(cClone);
                    clone = cClone.getDom();
                    cClone.release();
                    cSrc.release();
                    break;
                case 3:
                case 4:
                    Cur c = n.tempCur();
                    CharNode cn = n.nodeType() == 3 ? l.createTextNode() : l.createCdataNode();
                    cn.setChars(c.getChars(((CharNode) n)._cch), c._offSrc, c._cchSrc);
                    clone = cn;
                    c.release();
                    break;
                case 5:
                case 6:
                case 10:
                case 12:
                    throw new RuntimeException("Not impl");
                default:
                    throw new RuntimeException("Unknown kind");
            }
        }
        return clone;
    }

    public static String _node_getLocalName(Dom n) {
        if (!n.nodeCanHavePrefixUri()) {
            return null;
        }
        QName name = n.getQName();
        return name == null ? "" : name.getLocalPart();
    }

    public static String _node_getNamespaceURI(Dom n) {
        if (!n.nodeCanHavePrefixUri()) {
            return null;
        }
        QName name = n.getQName();
        return name == null ? "" : name.getNamespaceURI();
    }

    public static void _node_setPrefix(Dom n, String prefix) {
        Locale l = n.locale();
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    node_setPrefix(n, prefix);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            node_setPrefix(n, prefix);
            l.exit();
        } finally {
        }
    }

    public static void node_setPrefix(Dom n, String prefix) {
        if (n.nodeType() == 1 || n.nodeType() == 2) {
            Cur c = n.tempCur();
            QName name = c.getName();
            String uri = name.getNamespaceURI();
            String local = name.getLocalPart();
            c.setName(n.locale().makeQName(uri, local, validatePrefix(prefix, uri, local, n.nodeType() == 2)));
            c.release();
            return;
        }
        validatePrefix(prefix, "", "", false);
    }

    public static String _node_getPrefix(Dom n) {
        if (!n.nodeCanHavePrefixUri()) {
            return null;
        }
        QName name = n.getQName();
        return name == null ? "" : name.getPrefix();
    }

    public static String _node_getNodeName(Dom n) {
        switch (n.nodeType()) {
            case 1:
            case 2:
                QName name = n.getQName();
                String prefix = name.getPrefix();
                return prefix.length() == 0 ? name.getLocalPart() : prefix + ":" + name.getLocalPart();
            case 3:
                return "#text";
            case 4:
                return "#cdata-section";
            case 5:
            case 6:
            case 10:
            case 12:
                throw new RuntimeException("Not impl");
            case 7:
                return n.getQName().getLocalPart();
            case 8:
                return "#comment";
            case 9:
                return "#document";
            case 11:
                return "#document-fragment";
            default:
                throw new RuntimeException("Unknown node type");
        }
    }

    public static short _node_getNodeType(Dom n) {
        return (short) n.nodeType();
    }

    public static void _node_setNodeValue(Dom n, String nodeValue) {
        Locale l = n.locale();
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    node_setNodeValue(n, nodeValue);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            node_setNodeValue(n, nodeValue);
            l.exit();
        } finally {
        }
    }

    public static void node_setNodeValue(Dom n, String nodeValue) throws DOMException {
        if (nodeValue == null) {
            nodeValue = "";
        }
        switch (n.nodeType()) {
            case 2:
                NodeList children = ((Node) n).getChildNodes();
                while (children.getLength() > 1) {
                    node_removeChild(n, (Dom) children.item(1));
                }
                if (children.getLength() == 0) {
                    TextNode tn = n.locale().createTextNode();
                    tn.setChars(nodeValue, 0, nodeValue.length());
                    node_insertBefore(n, tn, null);
                } else {
                    if (!$assertionsDisabled && children.getLength() != 1) {
                        throw new AssertionError();
                    }
                    children.item(0).setNodeValue(nodeValue);
                }
                if (((Xobj.AttrXobj) n).isId()) {
                    Dom d = node_getOwnerDocument(n);
                    String val = node_getNodeValue(n);
                    if (d instanceof Xobj.DocumentXobj) {
                        ((Xobj.DocumentXobj) d).removeIdElement(val);
                        ((Xobj.DocumentXobj) d).addIdElement(nodeValue, attr_getOwnerElement(n));
                        return;
                    }
                    return;
                }
                return;
            case 3:
            case 4:
                CharNode cn = (CharNode) n;
                Cur c = cn.tempCur();
                if (c != null) {
                    c.moveChars(null, cn._cch);
                    cn._cch = nodeValue.length();
                    c.insertString(nodeValue);
                    c.release();
                    return;
                }
                cn.setChars(nodeValue, 0, nodeValue.length());
                return;
            case 5:
            case 6:
            default:
                return;
            case 7:
            case 8:
                Cur c2 = n.tempCur();
                c2.next();
                c2.getChars(-1);
                c2.moveChars(null, c2._cchSrc);
                c2.insertString(nodeValue);
                c2.release();
                return;
        }
    }

    public static String _node_getNodeValue(Dom n) {
        String strNode_getNodeValue;
        Locale l = n.locale();
        if (l.noSync()) {
            return node_getNodeValue(n);
        }
        synchronized (l) {
            strNode_getNodeValue = node_getNodeValue(n);
        }
        return strNode_getNodeValue;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static String node_getNodeValue(Dom dom) {
        String s = null;
        switch (dom.nodeType()) {
            case 2:
            case 7:
            case 8:
                s = ((Xobj) dom).getValueAsString();
                break;
            case 3:
            case 4:
                if (!$assertionsDisabled && !(dom instanceof CharNode)) {
                    throw new AssertionError("Text/CData should be a CharNode");
                }
                CharNode node = (CharNode) dom;
                if (!(node._src instanceof Xobj)) {
                    s = CharUtil.getString(node._src, node._off, node._cch);
                    break;
                } else {
                    Xobj src = (Xobj) node._src;
                    src.ensureOccupancy();
                    boolean isThisNodeAfterText = node.isNodeAftertext();
                    if (isThisNodeAfterText) {
                        src._charNodesAfter = Cur.updateCharNodes(src._locale, src, src._charNodesAfter, src._cchAfter);
                        s = src.getCharsAfterAsString(node._off, node._cch);
                        break;
                    } else {
                        src._charNodesValue = Cur.updateCharNodes(src._locale, src, src._charNodesValue, src._cchValue);
                        s = src.getCharsValueAsString(node._off, node._cch);
                        break;
                    }
                }
                break;
        }
        return s;
    }

    public static Object _node_getUserData(Dom n, String key) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static Object _node_setUserData(Dom n, String key, Object data, UserDataHandler handler) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static Object _node_getFeature(Dom n, String feature, String version) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static boolean _node_isEqualNode(Dom n, Node arg) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static boolean _node_isSameNode(Dom n, Node arg) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static String _node_lookupNamespaceURI(Dom n, String prefix) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static boolean _node_isDefaultNamespace(Dom n, String namespaceURI) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static String _node_lookupPrefix(Dom n, String namespaceURI) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static void _node_setTextContent(Dom n, String textContent) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static String _node_getTextContent(Dom n) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static short _node_compareDocumentPosition(Dom n, Node other) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static String _node_getBaseURI(Dom n) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static Node _childNodes_item(Dom n, int i) {
        Dom d;
        Locale l = n.locale();
        if (i == 0) {
            return _node_getFirstChild(n);
        }
        if (l.noSync()) {
            d = childNodes_item(n, i);
        } else {
            synchronized (l) {
                d = childNodes_item(n, i);
            }
        }
        return (Node) d;
    }

    public static Dom childNodes_item(Dom n, int i) {
        if (i < 0) {
            return null;
        }
        switch (n.nodeType()) {
            case 1:
            case 2:
            case 9:
            case 11:
            default:
                if (i == 0) {
                    return node_getFirstChild(n);
                }
                return n.locale().findDomNthChild(n, i);
            case 3:
            case 4:
            case 7:
            case 8:
                return null;
            case 5:
                throw new RuntimeException("Not impl");
            case 6:
            case 10:
            case 12:
                throw new RuntimeException("Not impl");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static int _childNodes_getLength(Dom dom) {
        int iChildNodes_getLength;
        int count;
        Locale l = dom.locale();
        if (!$assertionsDisabled && !(dom instanceof Xobj)) {
            throw new AssertionError();
        }
        Xobj node = (Xobj) dom;
        if (!node.isVacant() && (count = node.getDomZeroOneChildren()) < 2) {
            return count;
        }
        if (l.noSync()) {
            return childNodes_getLength(dom);
        }
        synchronized (l) {
            iChildNodes_getLength = childNodes_getLength(dom);
        }
        return iChildNodes_getLength;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static int childNodes_getLength(Dom dom) {
        switch (dom.nodeType()) {
            case 1:
            case 2:
            case 9:
            case 11:
            default:
                if (!$assertionsDisabled && !(dom instanceof Xobj)) {
                    throw new AssertionError();
                }
                Xobj node = (Xobj) dom;
                node.ensureOccupancy();
                int count = node.getDomZeroOneChildren();
                if (count < 2) {
                    return count;
                }
                return dom.locale().domLength(dom);
            case 3:
            case 4:
            case 7:
            case 8:
                return 0;
            case 5:
                throw new RuntimeException("Not impl");
            case 6:
            case 10:
            case 12:
                throw new RuntimeException("Not impl");
        }
    }

    public static String _element_getTagName(Dom e) {
        return _node_getNodeName(e);
    }

    public static Attr _element_getAttributeNode(Dom e, String name) {
        return (Attr) _attributes_getNamedItem(e, name);
    }

    public static Attr _element_getAttributeNodeNS(Dom e, String uri, String local) {
        return (Attr) _attributes_getNamedItemNS(e, uri, local);
    }

    public static Attr _element_setAttributeNode(Dom e, Attr newAttr) {
        return (Attr) _attributes_setNamedItem(e, newAttr);
    }

    public static Attr _element_setAttributeNodeNS(Dom e, Attr newAttr) {
        return (Attr) _attributes_setNamedItemNS(e, newAttr);
    }

    public static String _element_getAttribute(Dom e, String name) {
        Node a = _attributes_getNamedItem(e, name);
        return a == null ? "" : a.getNodeValue();
    }

    public static String _element_getAttributeNS(Dom e, String uri, String local) {
        Node a = _attributes_getNamedItemNS(e, uri, local);
        return a == null ? "" : a.getNodeValue();
    }

    public static boolean _element_hasAttribute(Dom e, String name) {
        return _attributes_getNamedItem(e, name) != null;
    }

    public static boolean _element_hasAttributeNS(Dom e, String uri, String local) {
        return _attributes_getNamedItemNS(e, uri, local) != null;
    }

    public static void _element_removeAttribute(Dom e, String name) {
        try {
            _attributes_removeNamedItem(e, name);
        } catch (NotFoundErr e2) {
        }
    }

    public static void _element_removeAttributeNS(Dom e, String uri, String local) {
        try {
            _attributes_removeNamedItemNS(e, uri, local);
        } catch (NotFoundErr e2) {
        }
    }

    public static Attr _element_removeAttributeNode(Dom e, Attr oldAttr) {
        if (oldAttr == null) {
            throw new NotFoundErr("Attribute to remove is null");
        }
        if (oldAttr.getOwnerElement() != e) {
            throw new NotFoundErr("Attribute to remove does not belong to this element");
        }
        return (Attr) _attributes_removeNamedItem(e, oldAttr.getNodeName());
    }

    public static void _element_setAttribute(Dom e, String name, String value) {
        Locale l = e.locale();
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    element_setAttribute(e, name, value);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            element_setAttribute(e, name, value);
            l.exit();
        } finally {
        }
    }

    public static void element_setAttribute(Dom e, String name, String value) throws DOMException {
        Dom a = attributes_getNamedItem(e, name);
        if (a == null) {
            a = document_createAttribute(node_getOwnerDocument(e), name);
            attributes_setNamedItem(e, a);
        }
        node_setNodeValue(a, value);
    }

    public static void _element_setAttributeNS(Dom e, String uri, String qname, String value) {
        Locale l = e.locale();
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    element_setAttributeNS(e, uri, qname, value);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            element_setAttributeNS(e, uri, qname, value);
            l.exit();
        } finally {
        }
    }

    public static void element_setAttributeNS(Dom e, String uri, String qname, String value) throws DOMException {
        validateQualifiedName(qname, uri, true);
        QName name = e.locale().makeQualifiedQName(uri, qname);
        String local = name.getLocalPart();
        String prefix = validatePrefix(name.getPrefix(), uri, local, true);
        Dom a = attributes_getNamedItemNS(e, uri, local);
        if (a == null) {
            a = document_createAttributeNS(node_getOwnerDocument(e), uri, local);
            attributes_setNamedItemNS(e, a);
        }
        node_setPrefix(a, prefix);
        node_setNodeValue(a, value);
    }

    public static NodeList _element_getElementsByTagName(Dom e, String name) {
        NodeList nodeListElement_getElementsByTagName;
        Locale l = e.locale();
        if (l.noSync()) {
            l.enter();
            try {
                NodeList nodeListElement_getElementsByTagName2 = element_getElementsByTagName(e, name);
                l.exit();
                return nodeListElement_getElementsByTagName2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                nodeListElement_getElementsByTagName = element_getElementsByTagName(e, name);
                l.exit();
            } finally {
            }
        }
        return nodeListElement_getElementsByTagName;
    }

    public static NodeList element_getElementsByTagName(Dom e, String name) {
        return new ElementsByTagNameNodeList(e, name);
    }

    public static NodeList _element_getElementsByTagNameNS(Dom e, String uri, String local) {
        NodeList nodeListElement_getElementsByTagNameNS;
        Locale l = e.locale();
        if (l.noSync()) {
            l.enter();
            try {
                NodeList nodeListElement_getElementsByTagNameNS2 = element_getElementsByTagNameNS(e, uri, local);
                l.exit();
                return nodeListElement_getElementsByTagNameNS2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                nodeListElement_getElementsByTagNameNS = element_getElementsByTagNameNS(e, uri, local);
                l.exit();
            } finally {
            }
        }
        return nodeListElement_getElementsByTagNameNS;
    }

    public static NodeList element_getElementsByTagNameNS(Dom e, String uri, String local) {
        return new ElementsByTagNameNSNodeList(e, uri, local);
    }

    public static int _attributes_getLength(Dom e) {
        int iAttributes_getLength;
        Locale l = e.locale();
        if (l.noSync()) {
            l.enter();
            try {
                int iAttributes_getLength2 = attributes_getLength(e);
                l.exit();
                return iAttributes_getLength2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                iAttributes_getLength = attributes_getLength(e);
                l.exit();
            } finally {
            }
        }
        return iAttributes_getLength;
    }

    public static int attributes_getLength(Dom e) {
        int n = 0;
        Cur c = e.tempCur();
        while (c.toNextAttr()) {
            n++;
        }
        c.release();
        return n;
    }

    public static Node _attributes_setNamedItem(Dom e, Node attr) {
        Dom oldA;
        Locale l = e.locale();
        if (attr == null) {
            throw new IllegalArgumentException("Attr to set is null");
        }
        if (attr instanceof Dom) {
            Dom a = (Dom) attr;
            if (a.locale() == l) {
                if (l.noSync()) {
                    l.enter();
                    try {
                        oldA = attributes_setNamedItem(e, a);
                        l.exit();
                    } finally {
                    }
                } else {
                    synchronized (l) {
                        l.enter();
                        try {
                            oldA = attributes_setNamedItem(e, a);
                            l.exit();
                        } finally {
                        }
                    }
                }
                return (Node) oldA;
            }
        }
        throw new WrongDocumentErr("Attr to set is from another document");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Dom attributes_setNamedItem(Dom e, Dom dom) {
        if (attr_getOwnerElement(dom) != null) {
            throw new InuseAttributeError();
        }
        if (dom.nodeType() != 2) {
            throw new HierarchyRequestErr("Node is not an attribute");
        }
        String name = _node_getNodeName(dom);
        Dom oldAttr = null;
        Cur c = e.tempCur();
        while (c.toNextAttr()) {
            Dom aa = c.getDom();
            if (_node_getNodeName(aa).equals(name)) {
                if (oldAttr == null) {
                    oldAttr = aa;
                } else {
                    removeNode(aa);
                    c.toPrevAttr();
                }
            }
        }
        if (oldAttr == null) {
            c.moveToDom(e);
            c.next();
            Cur.moveNode((Xobj) dom, c);
        } else {
            c.moveToDom(oldAttr);
            Cur.moveNode((Xobj) dom, c);
            removeNode(oldAttr);
        }
        c.release();
        return oldAttr;
    }

    public static Node _attributes_getNamedItem(Dom e, String name) {
        Dom n;
        Locale l = e.locale();
        if (l.noSync()) {
            l.enter();
            try {
                n = attributes_getNamedItem(e, name);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    n = attributes_getNamedItem(e, name);
                    l.exit();
                } finally {
                }
            }
        }
        return (Node) n;
    }

    public static Dom attributes_getNamedItem(Dom e, String name) {
        Dom a = null;
        Cur c = e.tempCur();
        while (true) {
            if (!c.toNextAttr()) {
                break;
            }
            Dom d = c.getDom();
            if (_node_getNodeName(d).equals(name)) {
                a = d;
                break;
            }
        }
        c.release();
        return a;
    }

    public static Node _attributes_getNamedItemNS(Dom e, String uri, String local) {
        Dom n;
        Locale l = e.locale();
        if (l.noSync()) {
            l.enter();
            try {
                n = attributes_getNamedItemNS(e, uri, local);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    n = attributes_getNamedItemNS(e, uri, local);
                    l.exit();
                } finally {
                }
            }
        }
        return (Node) n;
    }

    public static Dom attributes_getNamedItemNS(Dom e, String uri, String local) {
        if (uri == null) {
            uri = "";
        }
        Dom a = null;
        Cur c = e.tempCur();
        while (true) {
            if (!c.toNextAttr()) {
                break;
            }
            Dom d = c.getDom();
            QName n = d.getQName();
            if (n.getNamespaceURI().equals(uri) && n.getLocalPart().equals(local)) {
                a = d;
                break;
            }
        }
        c.release();
        return a;
    }

    public static Node _attributes_removeNamedItem(Dom e, String name) {
        Dom n;
        Locale l = e.locale();
        if (l.noSync()) {
            l.enter();
            try {
                n = attributes_removeNamedItem(e, name);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    n = attributes_removeNamedItem(e, name);
                    l.exit();
                } finally {
                }
            }
        }
        return (Node) n;
    }

    public static Dom attributes_removeNamedItem(Dom e, String name) {
        Dom oldAttr = null;
        Cur c = e.tempCur();
        while (c.toNextAttr()) {
            Dom aa = c.getDom();
            if (_node_getNodeName(aa).equals(name)) {
                if (oldAttr == null) {
                    oldAttr = aa;
                }
                if (((Xobj.AttrXobj) aa).isId()) {
                    Dom d = node_getOwnerDocument(aa);
                    String val = node_getNodeValue(aa);
                    if (d instanceof Xobj.DocumentXobj) {
                        ((Xobj.DocumentXobj) d).removeIdElement(val);
                    }
                }
                removeNode(aa);
                c.toPrevAttr();
            }
        }
        c.release();
        if (oldAttr == null) {
            throw new NotFoundErr("Named item not found: " + name);
        }
        return oldAttr;
    }

    public static Node _attributes_removeNamedItemNS(Dom e, String uri, String local) {
        Dom n;
        Locale l = e.locale();
        if (l.noSync()) {
            l.enter();
            try {
                n = attributes_removeNamedItemNS(e, uri, local);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    n = attributes_removeNamedItemNS(e, uri, local);
                    l.exit();
                } finally {
                }
            }
        }
        return (Node) n;
    }

    public static Dom attributes_removeNamedItemNS(Dom e, String uri, String local) {
        if (uri == null) {
            uri = "";
        }
        Dom oldAttr = null;
        Cur c = e.tempCur();
        while (c.toNextAttr()) {
            Dom aa = c.getDom();
            QName qn = aa.getQName();
            if (qn.getNamespaceURI().equals(uri) && qn.getLocalPart().equals(local)) {
                if (oldAttr == null) {
                    oldAttr = aa;
                }
                if (((Xobj.AttrXobj) aa).isId()) {
                    Dom d = node_getOwnerDocument(aa);
                    String val = node_getNodeValue(aa);
                    if (d instanceof Xobj.DocumentXobj) {
                        ((Xobj.DocumentXobj) d).removeIdElement(val);
                    }
                }
                removeNode(aa);
                c.toPrevAttr();
            }
        }
        c.release();
        if (oldAttr == null) {
            throw new NotFoundErr("Named item not found: uri=" + uri + ", local=" + local);
        }
        return oldAttr;
    }

    public static Node _attributes_setNamedItemNS(Dom e, Node attr) {
        Dom oldA;
        Locale l = e.locale();
        if (attr == null) {
            throw new IllegalArgumentException("Attr to set is null");
        }
        if (attr instanceof Dom) {
            Dom a = (Dom) attr;
            if (a.locale() == l) {
                if (l.noSync()) {
                    l.enter();
                    try {
                        oldA = attributes_setNamedItemNS(e, a);
                        l.exit();
                    } finally {
                    }
                } else {
                    synchronized (l) {
                        l.enter();
                        try {
                            oldA = attributes_setNamedItemNS(e, a);
                            l.exit();
                        } finally {
                        }
                    }
                }
                return (Node) oldA;
            }
        }
        throw new WrongDocumentErr("Attr to set is from another document");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Dom attributes_setNamedItemNS(Dom e, Dom dom) {
        Dom owner = attr_getOwnerElement(dom);
        if (owner == e) {
            return dom;
        }
        if (owner != null) {
            throw new InuseAttributeError();
        }
        if (dom.nodeType() != 2) {
            throw new HierarchyRequestErr("Node is not an attribute");
        }
        QName name = dom.getQName();
        Dom oldAttr = null;
        Cur c = e.tempCur();
        while (c.toNextAttr()) {
            Dom aa = c.getDom();
            if (aa.getQName().equals(name)) {
                if (oldAttr == null) {
                    oldAttr = aa;
                } else {
                    removeNode(aa);
                    c.toPrevAttr();
                }
            }
        }
        if (oldAttr == null) {
            c.moveToDom(e);
            c.next();
            Cur.moveNode((Xobj) dom, c);
        } else {
            c.moveToDom(oldAttr);
            Cur.moveNode((Xobj) dom, c);
            removeNode(oldAttr);
        }
        c.release();
        return oldAttr;
    }

    public static Node _attributes_item(Dom e, int index) {
        Dom a;
        Locale l = e.locale();
        if (l.noSync()) {
            l.enter();
            try {
                a = attributes_item(e, index);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    a = attributes_item(e, index);
                    l.exit();
                } finally {
                }
            }
        }
        return (Node) a;
    }

    public static Dom attributes_item(Dom e, int index) {
        if (index < 0) {
            return null;
        }
        Cur c = e.tempCur();
        Dom a = null;
        while (true) {
            if (!c.toNextAttr()) {
                break;
            }
            int i = index;
            index--;
            if (i == 0) {
                a = c.getDom();
                break;
            }
        }
        c.release();
        return a;
    }

    public static String _processingInstruction_getData(Dom p) {
        return _node_getNodeValue(p);
    }

    public static String _processingInstruction_getTarget(Dom p) {
        return _node_getNodeName(p);
    }

    public static void _processingInstruction_setData(Dom p, String data) {
        _node_setNodeValue(p, data);
    }

    public static boolean _attr_getSpecified(Dom a) {
        return true;
    }

    public static Element _attr_getOwnerElement(Dom a) {
        Dom e;
        Locale l = a.locale();
        if (l.noSync()) {
            l.enter();
            try {
                e = attr_getOwnerElement(a);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    e = attr_getOwnerElement(a);
                    l.exit();
                } finally {
                }
            }
        }
        return (Element) e;
    }

    public static Dom attr_getOwnerElement(Dom n) {
        Cur c = n.tempCur();
        if (!c.toParentRaw()) {
            c.release();
            return null;
        }
        Dom p = c.getDom();
        c.release();
        return p;
    }

    public static void _characterData_appendData(Dom cd, String arg) {
        if (arg != null && arg.length() != 0) {
            _node_setNodeValue(cd, _node_getNodeValue(cd) + arg);
        }
    }

    public static void _characterData_deleteData(Dom c, int offset, int count) {
        String s = _characterData_getData(c);
        if (offset < 0 || offset > s.length() || count < 0) {
            throw new IndexSizeError();
        }
        if (offset + count > s.length()) {
            count = s.length() - offset;
        }
        if (count > 0) {
            _characterData_setData(c, s.substring(0, offset) + s.substring(offset + count));
        }
    }

    public static String _characterData_getData(Dom c) {
        return _node_getNodeValue(c);
    }

    public static int _characterData_getLength(Dom c) {
        return _characterData_getData(c).length();
    }

    public static void _characterData_insertData(Dom c, int offset, String arg) {
        String s = _characterData_getData(c);
        if (offset < 0 || offset > s.length()) {
            throw new IndexSizeError();
        }
        if (arg != null && arg.length() > 0) {
            _characterData_setData(c, s.substring(0, offset) + arg + s.substring(offset));
        }
    }

    public static void _characterData_replaceData(Dom c, int offset, int count, String arg) {
        String s = _characterData_getData(c);
        if (offset < 0 || offset > s.length() || count < 0) {
            throw new IndexSizeError();
        }
        if (offset + count > s.length()) {
            count = s.length() - offset;
        }
        if (count > 0) {
            _characterData_setData(c, s.substring(0, offset) + (arg == null ? "" : arg) + s.substring(offset + count));
        }
    }

    public static void _characterData_setData(Dom c, String data) {
        _node_setNodeValue(c, data);
    }

    public static String _characterData_substringData(Dom c, int offset, int count) {
        String s = _characterData_getData(c);
        if (offset < 0 || offset > s.length() || count < 0) {
            throw new IndexSizeError();
        }
        if (offset + count > s.length()) {
            count = s.length() - offset;
        }
        return s.substring(offset, offset + count);
    }

    public static Text _text_splitText(Dom t, int offset) {
        if (!$assertionsDisabled && t.nodeType() != 3) {
            throw new AssertionError();
        }
        String s = _characterData_getData(t);
        if (offset < 0 || offset > s.length()) {
            throw new IndexSizeError();
        }
        _characterData_deleteData(t, offset, s.length() - offset);
        Dom t2 = (Dom) _document_createTextNode(t, s.substring(offset));
        Dom p = (Dom) _node_getParentNode(t);
        if (p != null) {
            _node_insertBefore(p, (Text) t2, _node_getNextSibling(t));
            t.locale().invalidateDomCaches(p);
        }
        return (Text) t2;
    }

    public static String _text_getWholeText(Dom t) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static boolean _text_isElementContentWhitespace(Dom t) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static Text _text_replaceWholeText(Dom t, String content) {
        throw new RuntimeException("DOM Level 3 Not implemented");
    }

    public static XMLStreamReader _getXmlStreamReader(Dom n) {
        XMLStreamReader xmlStreamReader;
        Locale l = n.locale();
        if (l.noSync()) {
            l.enter();
            try {
                XMLStreamReader xmlStreamReader2 = getXmlStreamReader(n);
                l.exit();
                return xmlStreamReader2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                xmlStreamReader = getXmlStreamReader(n);
                l.exit();
            } finally {
            }
        }
        return xmlStreamReader;
    }

    public static XMLStreamReader getXmlStreamReader(Dom n) {
        XMLStreamReader xs;
        switch (n.nodeType()) {
            case 1:
            case 2:
            case 7:
            case 8:
            case 9:
            case 11:
                Cur c = n.tempCur();
                xs = Jsr173.newXmlStreamReader(c, null);
                c.release();
                break;
            case 3:
            case 4:
                CharNode cn = (CharNode) n;
                Cur curTempCur = cn.tempCur();
                Cur c2 = curTempCur;
                if (curTempCur == null) {
                    c2 = n.locale().tempCur();
                    xs = Jsr173.newXmlStreamReader(c2, cn._src, cn._off, cn._cch);
                } else {
                    xs = Jsr173.newXmlStreamReader(c2, c2.getChars(cn._cch), c2._offSrc, c2._cchSrc);
                }
                c2.release();
                break;
            case 5:
            case 6:
            case 10:
            case 12:
                throw new RuntimeException("Not impl");
            default:
                throw new RuntimeException("Unknown kind");
        }
        return xs;
    }

    public static XmlCursor _getXmlCursor(Dom n) {
        XmlCursor xmlCursor;
        Locale l = n.locale();
        if (l.noSync()) {
            l.enter();
            try {
                XmlCursor xmlCursor2 = getXmlCursor(n);
                l.exit();
                return xmlCursor2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                xmlCursor = getXmlCursor(n);
                l.exit();
            } finally {
            }
        }
        return xmlCursor;
    }

    public static XmlCursor getXmlCursor(Dom n) {
        Cur c = n.tempCur();
        Cursor xc = new Cursor(c);
        c.release();
        return xc;
    }

    public static XmlObject _getXmlObject(Dom n) {
        XmlObject xmlObject;
        Locale l = n.locale();
        if (l.noSync()) {
            l.enter();
            try {
                XmlObject xmlObject2 = getXmlObject(n);
                l.exit();
                return xmlObject2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                xmlObject = getXmlObject(n);
                l.exit();
            } finally {
            }
        }
        return xmlObject;
    }

    public static XmlObject getXmlObject(Dom n) {
        Cur c = n.tempCur();
        XmlObject x = c.getObject();
        c.release();
        return x;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$CharNode.class */
    static abstract class CharNode implements Dom, Node, CharacterData {
        private Locale _locale;
        CharNode _next;
        CharNode _prev;
        private Object _src;
        int _off;
        int _cch;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DomImpl.class.desiredAssertionStatus();
        }

        public CharNode(Locale l) {
            if (!$assertionsDisabled && l == null) {
                throw new AssertionError();
            }
            this._locale = l;
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public QName getQName() {
            return null;
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public Locale locale() {
            if ($assertionsDisabled || isValid()) {
                return this._locale == null ? ((Dom) this._src).locale() : this._locale;
            }
            throw new AssertionError();
        }

        public void setChars(Object src, int off, int cch) {
            if (!$assertionsDisabled && !CharUtil.isValid(src, off, cch)) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this._locale == null && !(this._src instanceof Dom)) {
                throw new AssertionError();
            }
            if (this._locale == null) {
                this._locale = ((Dom) this._src).locale();
            }
            this._src = src;
            this._off = off;
            this._cch = cch;
        }

        public Dom getDom() {
            if (!$assertionsDisabled && !isValid()) {
                throw new AssertionError();
            }
            if (this._src instanceof Dom) {
                return (Dom) this._src;
            }
            return null;
        }

        public void setDom(Dom d) {
            if (!$assertionsDisabled && d == null) {
                throw new AssertionError();
            }
            this._src = d;
            this._locale = null;
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public Cur tempCur() {
            if (!$assertionsDisabled && !isValid()) {
                throw new AssertionError();
            }
            if (!(this._src instanceof Dom)) {
                return null;
            }
            Cur c = locale().tempCur();
            c.moveToCharNode(this);
            return c;
        }

        private boolean isValid() {
            if (this._src instanceof Dom) {
                return this._locale == null;
            }
            if (this._locale == null) {
                return false;
            }
            return true;
        }

        public static boolean isOnList(CharNode nodes, CharNode node) {
            if (!$assertionsDisabled && node == null) {
                throw new AssertionError();
            }
            CharNode charNode = nodes;
            while (true) {
                CharNode cn = charNode;
                if (cn != null) {
                    if (cn != node) {
                        charNode = cn._next;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        public static CharNode remove(CharNode nodes, CharNode node) {
            if (!$assertionsDisabled && !isOnList(nodes, node)) {
                throw new AssertionError();
            }
            if (nodes == node) {
                nodes = node._next;
            } else {
                node._prev._next = node._next;
            }
            if (node._next != null) {
                node._next._prev = node._prev;
            }
            node._next = null;
            node._prev = null;
            return nodes;
        }

        public static CharNode insertNode(CharNode nodes, CharNode newNode, CharNode before) {
            CharNode n;
            if (!$assertionsDisabled && isOnList(nodes, newNode)) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && before != null && !isOnList(nodes, before)) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && newNode == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && (newNode._prev != null || newNode._next != null)) {
                throw new AssertionError();
            }
            if (nodes == null) {
                if (!$assertionsDisabled && before != null) {
                    throw new AssertionError();
                }
                nodes = newNode;
            } else if (nodes == before) {
                nodes._prev = newNode;
                newNode._next = nodes;
                nodes = newNode;
            } else {
                CharNode charNode = nodes;
                while (true) {
                    n = charNode;
                    if (n._next == before) {
                        break;
                    }
                    charNode = n._next;
                }
                CharNode charNode2 = n._next;
                newNode._next = charNode2;
                if (charNode2 != null) {
                    n._next._prev = newNode;
                }
                newNode._prev = n;
                n._next = newNode;
            }
            return nodes;
        }

        public static CharNode appendNode(CharNode nodes, CharNode newNode) {
            return insertNode(nodes, newNode, null);
        }

        public static CharNode appendNodes(CharNode nodes, CharNode newNodes) {
            if (!$assertionsDisabled && newNodes == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && newNodes._prev != null) {
                throw new AssertionError();
            }
            if (nodes == null) {
                return newNodes;
            }
            CharNode charNode = nodes;
            while (true) {
                CharNode n = charNode;
                if (n._next != null) {
                    charNode = n._next;
                } else {
                    n._next = newNodes;
                    newNodes._prev = n;
                    return nodes;
                }
            }
        }

        public static CharNode copyNodes(CharNode nodes, Object newSrc) {
            CharNode newNode;
            CharNode newNodes = null;
            CharNode n = null;
            while (nodes != null) {
                if (nodes instanceof TextNode) {
                    newNode = nodes.locale().createTextNode();
                } else {
                    newNode = nodes.locale().createCdataNode();
                }
                newNode.setChars(newSrc, nodes._off, nodes._cch);
                if (newNodes == null) {
                    newNodes = newNode;
                }
                if (n != null) {
                    n._next = newNode;
                    newNode._prev = n;
                }
                n = newNode;
                nodes = nodes._next;
            }
            return newNodes;
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public boolean nodeCanHavePrefixUri() {
            return false;
        }

        public boolean isNodeAftertext() {
            if (!$assertionsDisabled && !(this._src instanceof Xobj)) {
                throw new AssertionError("this method is to only be used for nodes backed up by Xobjs");
            }
            Xobj src = (Xobj) this._src;
            if (src._charNodesValue == null) {
                return true;
            }
            if (src._charNodesAfter == null) {
                return false;
            }
            return isOnList(src._charNodesAfter, this);
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public void dump(PrintStream o, Object ref) {
            if (this._src instanceof Dom) {
                ((Dom) this._src).dump(o, ref);
            } else {
                o.println("Lonely CharNode: \"" + CharUtil.getString(this._src, this._off, this._cch) + SymbolConstants.QUOTES_SYMBOL);
            }
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public void dump(PrintStream o) {
            dump(o, this);
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public void dump() {
            dump(System.out);
        }

        @Override // org.w3c.dom.Node
        public Node appendChild(Node newChild) {
            return DomImpl._node_appendChild(this, newChild);
        }

        @Override // org.w3c.dom.Node
        public Node cloneNode(boolean deep) {
            return DomImpl._node_cloneNode(this, deep);
        }

        @Override // org.w3c.dom.Node
        public NamedNodeMap getAttributes() {
            return null;
        }

        @Override // org.w3c.dom.Node
        public NodeList getChildNodes() {
            return DomImpl._emptyNodeList;
        }

        @Override // org.w3c.dom.Node
        public Node getParentNode() {
            return DomImpl._node_getParentNode(this);
        }

        @Override // org.w3c.dom.Node
        public Node removeChild(Node oldChild) {
            return DomImpl._node_removeChild(this, oldChild);
        }

        @Override // org.w3c.dom.Node
        public Node getFirstChild() {
            return null;
        }

        @Override // org.w3c.dom.Node
        public Node getLastChild() {
            return null;
        }

        @Override // org.w3c.dom.Node
        public String getLocalName() {
            return DomImpl._node_getLocalName(this);
        }

        @Override // org.w3c.dom.Node
        public String getNamespaceURI() {
            return DomImpl._node_getNamespaceURI(this);
        }

        @Override // org.w3c.dom.Node
        public Node getNextSibling() {
            return DomImpl._node_getNextSibling(this);
        }

        @Override // org.w3c.dom.Node
        public String getNodeName() {
            return DomImpl._node_getNodeName(this);
        }

        @Override // org.w3c.dom.Node
        public short getNodeType() {
            return DomImpl._node_getNodeType(this);
        }

        @Override // org.w3c.dom.Node
        public String getNodeValue() {
            return DomImpl._node_getNodeValue(this);
        }

        @Override // org.w3c.dom.Node
        public Document getOwnerDocument() {
            return DomImpl._node_getOwnerDocument(this);
        }

        @Override // org.w3c.dom.Node
        public String getPrefix() {
            return DomImpl._node_getPrefix(this);
        }

        @Override // org.w3c.dom.Node
        public Node getPreviousSibling() {
            return DomImpl._node_getPreviousSibling(this);
        }

        @Override // org.w3c.dom.Node
        public boolean hasAttributes() {
            return false;
        }

        @Override // org.w3c.dom.Node
        public boolean hasChildNodes() {
            return false;
        }

        @Override // org.w3c.dom.Node
        public Node insertBefore(Node newChild, Node refChild) {
            return DomImpl._node_insertBefore(this, newChild, refChild);
        }

        @Override // org.w3c.dom.Node
        public boolean isSupported(String feature, String version) {
            return DomImpl._node_isSupported(this, feature, version);
        }

        @Override // org.w3c.dom.Node
        public void normalize() {
            DomImpl._node_normalize(this);
        }

        @Override // org.w3c.dom.Node
        public Node replaceChild(Node newChild, Node oldChild) {
            return DomImpl._node_replaceChild(this, newChild, oldChild);
        }

        @Override // org.w3c.dom.Node
        public void setNodeValue(String nodeValue) {
            DomImpl._node_setNodeValue(this, nodeValue);
        }

        @Override // org.w3c.dom.Node
        public void setPrefix(String prefix) {
            DomImpl._node_setPrefix(this, prefix);
        }

        @Override // org.w3c.dom.Node
        public Object getUserData(String key) {
            return DomImpl._node_getUserData(this, key);
        }

        @Override // org.w3c.dom.Node
        public Object setUserData(String key, Object data, UserDataHandler handler) {
            return DomImpl._node_setUserData(this, key, data, handler);
        }

        @Override // org.w3c.dom.Node
        public Object getFeature(String feature, String version) {
            return DomImpl._node_getFeature(this, feature, version);
        }

        @Override // org.w3c.dom.Node
        public boolean isEqualNode(Node arg) {
            return DomImpl._node_isEqualNode(this, arg);
        }

        @Override // org.w3c.dom.Node
        public boolean isSameNode(Node arg) {
            return DomImpl._node_isSameNode(this, arg);
        }

        @Override // org.w3c.dom.Node
        public String lookupNamespaceURI(String prefix) {
            return DomImpl._node_lookupNamespaceURI(this, prefix);
        }

        @Override // org.w3c.dom.Node
        public String lookupPrefix(String namespaceURI) {
            return DomImpl._node_lookupPrefix(this, namespaceURI);
        }

        @Override // org.w3c.dom.Node
        public boolean isDefaultNamespace(String namespaceURI) {
            return DomImpl._node_isDefaultNamespace(this, namespaceURI);
        }

        @Override // org.w3c.dom.Node
        public void setTextContent(String textContent) {
            DomImpl._node_setTextContent(this, textContent);
        }

        @Override // org.w3c.dom.Node
        public String getTextContent() {
            return DomImpl._node_getTextContent(this);
        }

        @Override // org.w3c.dom.Node
        public short compareDocumentPosition(Node other) {
            return DomImpl._node_compareDocumentPosition(this, other);
        }

        @Override // org.w3c.dom.Node
        public String getBaseURI() {
            return DomImpl._node_getBaseURI(this);
        }

        @Override // org.w3c.dom.CharacterData
        public void appendData(String arg) {
            DomImpl._characterData_appendData(this, arg);
        }

        @Override // org.w3c.dom.CharacterData
        public void deleteData(int offset, int count) {
            DomImpl._characterData_deleteData(this, offset, count);
        }

        @Override // org.w3c.dom.CharacterData
        public String getData() {
            return DomImpl._characterData_getData(this);
        }

        @Override // org.w3c.dom.CharacterData
        public int getLength() {
            return DomImpl._characterData_getLength(this);
        }

        @Override // org.w3c.dom.CharacterData
        public void insertData(int offset, String arg) {
            DomImpl._characterData_insertData(this, offset, arg);
        }

        @Override // org.w3c.dom.CharacterData
        public void replaceData(int offset, int count, String arg) {
            DomImpl._characterData_replaceData(this, offset, count, arg);
        }

        @Override // org.w3c.dom.CharacterData
        public void setData(String data) {
            DomImpl._characterData_setData(this, data);
        }

        @Override // org.w3c.dom.CharacterData
        public String substringData(int offset, int count) {
            return DomImpl._characterData_substringData(this, offset, count);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$TextNode.class */
    static class TextNode extends CharNode implements Text {
        TextNode(Locale l) {
            super(l);
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.Dom
        public int nodeType() {
            return 3;
        }

        public String name() {
            return "#text";
        }

        @Override // org.w3c.dom.Text
        public Text splitText(int offset) {
            return DomImpl._text_splitText(this, offset);
        }

        @Override // org.w3c.dom.Text
        public String getWholeText() {
            return DomImpl._text_getWholeText(this);
        }

        @Override // org.w3c.dom.Text
        public boolean isElementContentWhitespace() {
            return DomImpl._text_isElementContentWhitespace(this);
        }

        @Override // org.w3c.dom.Text
        public Text replaceWholeText(String content) {
            return DomImpl._text_replaceWholeText(this, content);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$CdataNode.class */
    static class CdataNode extends TextNode implements CDATASection {
        CdataNode(Locale l) {
            super(l);
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.TextNode, org.apache.xmlbeans.impl.store.DomImpl.Dom
        public int nodeType() {
            return 4;
        }

        @Override // org.apache.xmlbeans.impl.store.DomImpl.TextNode
        public String name() {
            return "#cdata-section";
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$SaajTextNode.class */
    static class SaajTextNode extends TextNode implements org.apache.xmlbeans.impl.soap.Text {
        SaajTextNode(Locale l) {
            super(l);
        }

        @Override // org.apache.xmlbeans.impl.soap.Text
        public boolean isComment() {
            return DomImpl._soapText_isComment(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public void detachNode() {
            DomImpl._soapNode_detachNode(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public void recycleNode() {
            DomImpl._soapNode_recycleNode(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public String getValue() {
            return DomImpl._soapNode_getValue(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public void setValue(String value) {
            DomImpl._soapNode_setValue(this, value);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public SOAPElement getParentElement() {
            return DomImpl._soapNode_getParentElement(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public void setParentElement(SOAPElement p) {
            DomImpl._soapNode_setParentElement(this, p);
        }
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$SaajCdataNode.class */
    static class SaajCdataNode extends CdataNode implements org.apache.xmlbeans.impl.soap.Text {
        public SaajCdataNode(Locale l) {
            super(l);
        }

        @Override // org.apache.xmlbeans.impl.soap.Text
        public boolean isComment() {
            return DomImpl._soapText_isComment(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public void detachNode() {
            DomImpl._soapNode_detachNode(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public void recycleNode() {
            DomImpl._soapNode_recycleNode(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public String getValue() {
            return DomImpl._soapNode_getValue(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public void setValue(String value) {
            DomImpl._soapNode_setValue(this, value);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public SOAPElement getParentElement() {
            return DomImpl._soapNode_getParentElement(this);
        }

        @Override // org.apache.xmlbeans.impl.soap.Node
        public void setParentElement(SOAPElement p) {
            DomImpl._soapNode_setParentElement(this, p);
        }
    }

    public static boolean _soapText_isComment(Dom n) {
        boolean zSoapText_isComment;
        Locale l = n.locale();
        org.apache.xmlbeans.impl.soap.Text text = (org.apache.xmlbeans.impl.soap.Text) n;
        if (l.noSync()) {
            l.enter();
            try {
                boolean zSoapText_isComment2 = l._saaj.soapText_isComment(text);
                l.exit();
                return zSoapText_isComment2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                zSoapText_isComment = l._saaj.soapText_isComment(text);
                l.exit();
            } finally {
            }
        }
        return zSoapText_isComment;
    }

    public static void _soapNode_detachNode(Dom n) {
        Locale l = n.locale();
        org.apache.xmlbeans.impl.soap.Node node = (org.apache.xmlbeans.impl.soap.Node) n;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapNode_detachNode(node);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapNode_detachNode(node);
            l.exit();
        } finally {
        }
    }

    public static void _soapNode_recycleNode(Dom n) {
        Locale l = n.locale();
        org.apache.xmlbeans.impl.soap.Node node = (org.apache.xmlbeans.impl.soap.Node) n;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapNode_recycleNode(node);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapNode_recycleNode(node);
            l.exit();
        } finally {
        }
    }

    public static String _soapNode_getValue(Dom n) {
        String strSoapNode_getValue;
        Locale l = n.locale();
        org.apache.xmlbeans.impl.soap.Node node = (org.apache.xmlbeans.impl.soap.Node) n;
        if (l.noSync()) {
            l.enter();
            try {
                String strSoapNode_getValue2 = l._saaj.soapNode_getValue(node);
                l.exit();
                return strSoapNode_getValue2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                strSoapNode_getValue = l._saaj.soapNode_getValue(node);
                l.exit();
            } finally {
            }
        }
        return strSoapNode_getValue;
    }

    public static void _soapNode_setValue(Dom n, String value) {
        Locale l = n.locale();
        org.apache.xmlbeans.impl.soap.Node node = (org.apache.xmlbeans.impl.soap.Node) n;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapNode_setValue(node, value);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapNode_setValue(node, value);
            l.exit();
        } finally {
        }
    }

    public static SOAPElement _soapNode_getParentElement(Dom n) {
        SOAPElement sOAPElementSoapNode_getParentElement;
        Locale l = n.locale();
        org.apache.xmlbeans.impl.soap.Node node = (org.apache.xmlbeans.impl.soap.Node) n;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPElement sOAPElementSoapNode_getParentElement2 = l._saaj.soapNode_getParentElement(node);
                l.exit();
                return sOAPElementSoapNode_getParentElement2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPElementSoapNode_getParentElement = l._saaj.soapNode_getParentElement(node);
                l.exit();
            } finally {
            }
        }
        return sOAPElementSoapNode_getParentElement;
    }

    public static void _soapNode_setParentElement(Dom n, SOAPElement p) {
        Locale l = n.locale();
        org.apache.xmlbeans.impl.soap.Node node = (org.apache.xmlbeans.impl.soap.Node) n;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapNode_setParentElement(node, p);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapNode_setParentElement(node, p);
            l.exit();
        } finally {
        }
    }

    public static void _soapElement_removeContents(Dom d) {
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapElement_removeContents(se);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapElement_removeContents(se);
            l.exit();
        } finally {
        }
    }

    public static String _soapElement_getEncodingStyle(Dom d) {
        String strSoapElement_getEncodingStyle;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                String strSoapElement_getEncodingStyle2 = l._saaj.soapElement_getEncodingStyle(se);
                l.exit();
                return strSoapElement_getEncodingStyle2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                strSoapElement_getEncodingStyle = l._saaj.soapElement_getEncodingStyle(se);
                l.exit();
            } finally {
            }
        }
        return strSoapElement_getEncodingStyle;
    }

    public static void _soapElement_setEncodingStyle(Dom d, String encodingStyle) {
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapElement_setEncodingStyle(se, encodingStyle);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapElement_setEncodingStyle(se, encodingStyle);
            l.exit();
        } finally {
        }
    }

    public static boolean _soapElement_removeNamespaceDeclaration(Dom d, String prefix) {
        boolean zSoapElement_removeNamespaceDeclaration;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                boolean zSoapElement_removeNamespaceDeclaration2 = l._saaj.soapElement_removeNamespaceDeclaration(se, prefix);
                l.exit();
                return zSoapElement_removeNamespaceDeclaration2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                zSoapElement_removeNamespaceDeclaration = l._saaj.soapElement_removeNamespaceDeclaration(se, prefix);
                l.exit();
            } finally {
            }
        }
        return zSoapElement_removeNamespaceDeclaration;
    }

    public static Iterator _soapElement_getAllAttributes(Dom d) {
        Iterator itSoapElement_getAllAttributes;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itSoapElement_getAllAttributes2 = l._saaj.soapElement_getAllAttributes(se);
                l.exit();
                return itSoapElement_getAllAttributes2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itSoapElement_getAllAttributes = l._saaj.soapElement_getAllAttributes(se);
                l.exit();
            } finally {
            }
        }
        return itSoapElement_getAllAttributes;
    }

    public static Iterator _soapElement_getChildElements(Dom d) {
        Iterator itSoapElement_getChildElements;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itSoapElement_getChildElements2 = l._saaj.soapElement_getChildElements(se);
                l.exit();
                return itSoapElement_getChildElements2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itSoapElement_getChildElements = l._saaj.soapElement_getChildElements(se);
                l.exit();
            } finally {
            }
        }
        return itSoapElement_getChildElements;
    }

    public static Iterator _soapElement_getNamespacePrefixes(Dom d) {
        Iterator itSoapElement_getNamespacePrefixes;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itSoapElement_getNamespacePrefixes2 = l._saaj.soapElement_getNamespacePrefixes(se);
                l.exit();
                return itSoapElement_getNamespacePrefixes2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itSoapElement_getNamespacePrefixes = l._saaj.soapElement_getNamespacePrefixes(se);
                l.exit();
            } finally {
            }
        }
        return itSoapElement_getNamespacePrefixes;
    }

    public static SOAPElement _soapElement_addAttribute(Dom d, Name name, String value) throws SOAPException {
        SOAPElement sOAPElementSoapElement_addAttribute;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPElement sOAPElementSoapElement_addAttribute2 = l._saaj.soapElement_addAttribute(se, name, value);
                l.exit();
                return sOAPElementSoapElement_addAttribute2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPElementSoapElement_addAttribute = l._saaj.soapElement_addAttribute(se, name, value);
                l.exit();
            } finally {
            }
        }
        return sOAPElementSoapElement_addAttribute;
    }

    public static SOAPElement _soapElement_addChildElement(Dom d, SOAPElement oldChild) throws SOAPException {
        SOAPElement sOAPElementSoapElement_addChildElement;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPElement sOAPElementSoapElement_addChildElement2 = l._saaj.soapElement_addChildElement(se, oldChild);
                l.exit();
                return sOAPElementSoapElement_addChildElement2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPElementSoapElement_addChildElement = l._saaj.soapElement_addChildElement(se, oldChild);
                l.exit();
            } finally {
            }
        }
        return sOAPElementSoapElement_addChildElement;
    }

    public static SOAPElement _soapElement_addChildElement(Dom d, Name name) throws SOAPException {
        SOAPElement sOAPElementSoapElement_addChildElement;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPElement sOAPElementSoapElement_addChildElement2 = l._saaj.soapElement_addChildElement(se, name);
                l.exit();
                return sOAPElementSoapElement_addChildElement2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPElementSoapElement_addChildElement = l._saaj.soapElement_addChildElement(se, name);
                l.exit();
            } finally {
            }
        }
        return sOAPElementSoapElement_addChildElement;
    }

    public static SOAPElement _soapElement_addChildElement(Dom d, String localName) throws SOAPException {
        SOAPElement sOAPElementSoapElement_addChildElement;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPElement sOAPElementSoapElement_addChildElement2 = l._saaj.soapElement_addChildElement(se, localName);
                l.exit();
                return sOAPElementSoapElement_addChildElement2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPElementSoapElement_addChildElement = l._saaj.soapElement_addChildElement(se, localName);
                l.exit();
            } finally {
            }
        }
        return sOAPElementSoapElement_addChildElement;
    }

    public static SOAPElement _soapElement_addChildElement(Dom d, String localName, String prefix) throws SOAPException {
        SOAPElement sOAPElementSoapElement_addChildElement;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPElement sOAPElementSoapElement_addChildElement2 = l._saaj.soapElement_addChildElement(se, localName, prefix);
                l.exit();
                return sOAPElementSoapElement_addChildElement2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPElementSoapElement_addChildElement = l._saaj.soapElement_addChildElement(se, localName, prefix);
                l.exit();
            } finally {
            }
        }
        return sOAPElementSoapElement_addChildElement;
    }

    public static SOAPElement _soapElement_addChildElement(Dom d, String localName, String prefix, String uri) throws SOAPException {
        SOAPElement sOAPElementSoapElement_addChildElement;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPElement sOAPElementSoapElement_addChildElement2 = l._saaj.soapElement_addChildElement(se, localName, prefix, uri);
                l.exit();
                return sOAPElementSoapElement_addChildElement2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPElementSoapElement_addChildElement = l._saaj.soapElement_addChildElement(se, localName, prefix, uri);
                l.exit();
            } finally {
            }
        }
        return sOAPElementSoapElement_addChildElement;
    }

    public static SOAPElement _soapElement_addNamespaceDeclaration(Dom d, String prefix, String uri) {
        SOAPElement sOAPElementSoapElement_addNamespaceDeclaration;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPElement sOAPElementSoapElement_addNamespaceDeclaration2 = l._saaj.soapElement_addNamespaceDeclaration(se, prefix, uri);
                l.exit();
                return sOAPElementSoapElement_addNamespaceDeclaration2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPElementSoapElement_addNamespaceDeclaration = l._saaj.soapElement_addNamespaceDeclaration(se, prefix, uri);
                l.exit();
            } finally {
            }
        }
        return sOAPElementSoapElement_addNamespaceDeclaration;
    }

    public static SOAPElement _soapElement_addTextNode(Dom d, String data) {
        SOAPElement sOAPElementSoapElement_addTextNode;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPElement sOAPElementSoapElement_addTextNode2 = l._saaj.soapElement_addTextNode(se, data);
                l.exit();
                return sOAPElementSoapElement_addTextNode2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPElementSoapElement_addTextNode = l._saaj.soapElement_addTextNode(se, data);
                l.exit();
            } finally {
            }
        }
        return sOAPElementSoapElement_addTextNode;
    }

    public static String _soapElement_getAttributeValue(Dom d, Name name) {
        String strSoapElement_getAttributeValue;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                String strSoapElement_getAttributeValue2 = l._saaj.soapElement_getAttributeValue(se, name);
                l.exit();
                return strSoapElement_getAttributeValue2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                strSoapElement_getAttributeValue = l._saaj.soapElement_getAttributeValue(se, name);
                l.exit();
            } finally {
            }
        }
        return strSoapElement_getAttributeValue;
    }

    public static Iterator _soapElement_getChildElements(Dom d, Name name) {
        Iterator itSoapElement_getChildElements;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itSoapElement_getChildElements2 = l._saaj.soapElement_getChildElements(se, name);
                l.exit();
                return itSoapElement_getChildElements2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itSoapElement_getChildElements = l._saaj.soapElement_getChildElements(se, name);
                l.exit();
            } finally {
            }
        }
        return itSoapElement_getChildElements;
    }

    public static Name _soapElement_getElementName(Dom d) {
        Name nameSoapElement_getElementName;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                Name nameSoapElement_getElementName2 = l._saaj.soapElement_getElementName(se);
                l.exit();
                return nameSoapElement_getElementName2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                nameSoapElement_getElementName = l._saaj.soapElement_getElementName(se);
                l.exit();
            } finally {
            }
        }
        return nameSoapElement_getElementName;
    }

    public static String _soapElement_getNamespaceURI(Dom d, String prefix) {
        String strSoapElement_getNamespaceURI;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                String strSoapElement_getNamespaceURI2 = l._saaj.soapElement_getNamespaceURI(se, prefix);
                l.exit();
                return strSoapElement_getNamespaceURI2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                strSoapElement_getNamespaceURI = l._saaj.soapElement_getNamespaceURI(se, prefix);
                l.exit();
            } finally {
            }
        }
        return strSoapElement_getNamespaceURI;
    }

    public static Iterator _soapElement_getVisibleNamespacePrefixes(Dom d) {
        Iterator itSoapElement_getVisibleNamespacePrefixes;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itSoapElement_getVisibleNamespacePrefixes2 = l._saaj.soapElement_getVisibleNamespacePrefixes(se);
                l.exit();
                return itSoapElement_getVisibleNamespacePrefixes2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itSoapElement_getVisibleNamespacePrefixes = l._saaj.soapElement_getVisibleNamespacePrefixes(se);
                l.exit();
            } finally {
            }
        }
        return itSoapElement_getVisibleNamespacePrefixes;
    }

    public static boolean _soapElement_removeAttribute(Dom d, Name name) {
        boolean zSoapElement_removeAttribute;
        Locale l = d.locale();
        SOAPElement se = (SOAPElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                boolean zSoapElement_removeAttribute2 = l._saaj.soapElement_removeAttribute(se, name);
                l.exit();
                return zSoapElement_removeAttribute2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                zSoapElement_removeAttribute = l._saaj.soapElement_removeAttribute(se, name);
                l.exit();
            } finally {
            }
        }
        return zSoapElement_removeAttribute;
    }

    public static SOAPBody _soapEnvelope_addBody(Dom d) throws SOAPException {
        SOAPBody sOAPBodySoapEnvelope_addBody;
        Locale l = d.locale();
        SOAPEnvelope se = (SOAPEnvelope) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPBody sOAPBodySoapEnvelope_addBody2 = l._saaj.soapEnvelope_addBody(se);
                l.exit();
                return sOAPBodySoapEnvelope_addBody2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPBodySoapEnvelope_addBody = l._saaj.soapEnvelope_addBody(se);
                l.exit();
            } finally {
            }
        }
        return sOAPBodySoapEnvelope_addBody;
    }

    public static SOAPBody _soapEnvelope_getBody(Dom d) throws SOAPException {
        SOAPBody sOAPBodySoapEnvelope_getBody;
        Locale l = d.locale();
        SOAPEnvelope se = (SOAPEnvelope) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPBody sOAPBodySoapEnvelope_getBody2 = l._saaj.soapEnvelope_getBody(se);
                l.exit();
                return sOAPBodySoapEnvelope_getBody2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPBodySoapEnvelope_getBody = l._saaj.soapEnvelope_getBody(se);
                l.exit();
            } finally {
            }
        }
        return sOAPBodySoapEnvelope_getBody;
    }

    public static SOAPHeader _soapEnvelope_getHeader(Dom d) throws SOAPException {
        SOAPHeader sOAPHeaderSoapEnvelope_getHeader;
        Locale l = d.locale();
        SOAPEnvelope se = (SOAPEnvelope) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPHeader sOAPHeaderSoapEnvelope_getHeader2 = l._saaj.soapEnvelope_getHeader(se);
                l.exit();
                return sOAPHeaderSoapEnvelope_getHeader2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPHeaderSoapEnvelope_getHeader = l._saaj.soapEnvelope_getHeader(se);
                l.exit();
            } finally {
            }
        }
        return sOAPHeaderSoapEnvelope_getHeader;
    }

    public static SOAPHeader _soapEnvelope_addHeader(Dom d) throws SOAPException {
        SOAPHeader sOAPHeaderSoapEnvelope_addHeader;
        Locale l = d.locale();
        SOAPEnvelope se = (SOAPEnvelope) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPHeader sOAPHeaderSoapEnvelope_addHeader2 = l._saaj.soapEnvelope_addHeader(se);
                l.exit();
                return sOAPHeaderSoapEnvelope_addHeader2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPHeaderSoapEnvelope_addHeader = l._saaj.soapEnvelope_addHeader(se);
                l.exit();
            } finally {
            }
        }
        return sOAPHeaderSoapEnvelope_addHeader;
    }

    public static Name _soapEnvelope_createName(Dom d, String localName) {
        Name nameSoapEnvelope_createName;
        Locale l = d.locale();
        SOAPEnvelope se = (SOAPEnvelope) d;
        if (l.noSync()) {
            l.enter();
            try {
                Name nameSoapEnvelope_createName2 = l._saaj.soapEnvelope_createName(se, localName);
                l.exit();
                return nameSoapEnvelope_createName2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                nameSoapEnvelope_createName = l._saaj.soapEnvelope_createName(se, localName);
                l.exit();
            } finally {
            }
        }
        return nameSoapEnvelope_createName;
    }

    public static Name _soapEnvelope_createName(Dom d, String localName, String prefix, String namespaceURI) {
        Name nameSoapEnvelope_createName;
        Locale l = d.locale();
        SOAPEnvelope se = (SOAPEnvelope) d;
        if (l.noSync()) {
            l.enter();
            try {
                Name nameSoapEnvelope_createName2 = l._saaj.soapEnvelope_createName(se, localName, prefix, namespaceURI);
                l.exit();
                return nameSoapEnvelope_createName2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                nameSoapEnvelope_createName = l._saaj.soapEnvelope_createName(se, localName, prefix, namespaceURI);
                l.exit();
            } finally {
            }
        }
        return nameSoapEnvelope_createName;
    }

    public static Iterator soapHeader_examineAllHeaderElements(Dom d) {
        Iterator itSoapHeader_examineAllHeaderElements;
        Locale l = d.locale();
        SOAPHeader sh = (SOAPHeader) d;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itSoapHeader_examineAllHeaderElements2 = l._saaj.soapHeader_examineAllHeaderElements(sh);
                l.exit();
                return itSoapHeader_examineAllHeaderElements2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itSoapHeader_examineAllHeaderElements = l._saaj.soapHeader_examineAllHeaderElements(sh);
                l.exit();
            } finally {
            }
        }
        return itSoapHeader_examineAllHeaderElements;
    }

    public static Iterator soapHeader_extractAllHeaderElements(Dom d) {
        Iterator itSoapHeader_extractAllHeaderElements;
        Locale l = d.locale();
        SOAPHeader sh = (SOAPHeader) d;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itSoapHeader_extractAllHeaderElements2 = l._saaj.soapHeader_extractAllHeaderElements(sh);
                l.exit();
                return itSoapHeader_extractAllHeaderElements2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itSoapHeader_extractAllHeaderElements = l._saaj.soapHeader_extractAllHeaderElements(sh);
                l.exit();
            } finally {
            }
        }
        return itSoapHeader_extractAllHeaderElements;
    }

    public static Iterator soapHeader_examineHeaderElements(Dom d, String actor) {
        Iterator itSoapHeader_examineHeaderElements;
        Locale l = d.locale();
        SOAPHeader sh = (SOAPHeader) d;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itSoapHeader_examineHeaderElements2 = l._saaj.soapHeader_examineHeaderElements(sh, actor);
                l.exit();
                return itSoapHeader_examineHeaderElements2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itSoapHeader_examineHeaderElements = l._saaj.soapHeader_examineHeaderElements(sh, actor);
                l.exit();
            } finally {
            }
        }
        return itSoapHeader_examineHeaderElements;
    }

    public static Iterator soapHeader_examineMustUnderstandHeaderElements(Dom d, String mustUnderstandString) {
        Iterator itSoapHeader_examineMustUnderstandHeaderElements;
        Locale l = d.locale();
        SOAPHeader sh = (SOAPHeader) d;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itSoapHeader_examineMustUnderstandHeaderElements2 = l._saaj.soapHeader_examineMustUnderstandHeaderElements(sh, mustUnderstandString);
                l.exit();
                return itSoapHeader_examineMustUnderstandHeaderElements2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itSoapHeader_examineMustUnderstandHeaderElements = l._saaj.soapHeader_examineMustUnderstandHeaderElements(sh, mustUnderstandString);
                l.exit();
            } finally {
            }
        }
        return itSoapHeader_examineMustUnderstandHeaderElements;
    }

    public static Iterator soapHeader_extractHeaderElements(Dom d, String actor) {
        Iterator itSoapHeader_extractHeaderElements;
        Locale l = d.locale();
        SOAPHeader sh = (SOAPHeader) d;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itSoapHeader_extractHeaderElements2 = l._saaj.soapHeader_extractHeaderElements(sh, actor);
                l.exit();
                return itSoapHeader_extractHeaderElements2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itSoapHeader_extractHeaderElements = l._saaj.soapHeader_extractHeaderElements(sh, actor);
                l.exit();
            } finally {
            }
        }
        return itSoapHeader_extractHeaderElements;
    }

    public static SOAPHeaderElement soapHeader_addHeaderElement(Dom d, Name name) {
        SOAPHeaderElement sOAPHeaderElementSoapHeader_addHeaderElement;
        Locale l = d.locale();
        SOAPHeader sh = (SOAPHeader) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPHeaderElement sOAPHeaderElementSoapHeader_addHeaderElement2 = l._saaj.soapHeader_addHeaderElement(sh, name);
                l.exit();
                return sOAPHeaderElementSoapHeader_addHeaderElement2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPHeaderElementSoapHeader_addHeaderElement = l._saaj.soapHeader_addHeaderElement(sh, name);
                l.exit();
            } finally {
            }
        }
        return sOAPHeaderElementSoapHeader_addHeaderElement;
    }

    public static boolean soapBody_hasFault(Dom d) {
        boolean zSoapBody_hasFault;
        Locale l = d.locale();
        SOAPBody sb = (SOAPBody) d;
        if (l.noSync()) {
            l.enter();
            try {
                boolean zSoapBody_hasFault2 = l._saaj.soapBody_hasFault(sb);
                l.exit();
                return zSoapBody_hasFault2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                zSoapBody_hasFault = l._saaj.soapBody_hasFault(sb);
                l.exit();
            } finally {
            }
        }
        return zSoapBody_hasFault;
    }

    public static SOAPFault soapBody_addFault(Dom d) throws SOAPException {
        SOAPFault sOAPFaultSoapBody_addFault;
        Locale l = d.locale();
        SOAPBody sb = (SOAPBody) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPFault sOAPFaultSoapBody_addFault2 = l._saaj.soapBody_addFault(sb);
                l.exit();
                return sOAPFaultSoapBody_addFault2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPFaultSoapBody_addFault = l._saaj.soapBody_addFault(sb);
                l.exit();
            } finally {
            }
        }
        return sOAPFaultSoapBody_addFault;
    }

    public static SOAPFault soapBody_getFault(Dom d) {
        SOAPFault sOAPFaultSoapBody_getFault;
        Locale l = d.locale();
        SOAPBody sb = (SOAPBody) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPFault sOAPFaultSoapBody_getFault2 = l._saaj.soapBody_getFault(sb);
                l.exit();
                return sOAPFaultSoapBody_getFault2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPFaultSoapBody_getFault = l._saaj.soapBody_getFault(sb);
                l.exit();
            } finally {
            }
        }
        return sOAPFaultSoapBody_getFault;
    }

    public static SOAPBodyElement soapBody_addBodyElement(Dom d, Name name) {
        SOAPBodyElement sOAPBodyElementSoapBody_addBodyElement;
        Locale l = d.locale();
        SOAPBody sb = (SOAPBody) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPBodyElement sOAPBodyElementSoapBody_addBodyElement2 = l._saaj.soapBody_addBodyElement(sb, name);
                l.exit();
                return sOAPBodyElementSoapBody_addBodyElement2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPBodyElementSoapBody_addBodyElement = l._saaj.soapBody_addBodyElement(sb, name);
                l.exit();
            } finally {
            }
        }
        return sOAPBodyElementSoapBody_addBodyElement;
    }

    public static SOAPBodyElement soapBody_addDocument(Dom d, Document document) {
        SOAPBodyElement sOAPBodyElementSoapBody_addDocument;
        Locale l = d.locale();
        SOAPBody sb = (SOAPBody) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPBodyElement sOAPBodyElementSoapBody_addDocument2 = l._saaj.soapBody_addDocument(sb, document);
                l.exit();
                return sOAPBodyElementSoapBody_addDocument2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPBodyElementSoapBody_addDocument = l._saaj.soapBody_addDocument(sb, document);
                l.exit();
            } finally {
            }
        }
        return sOAPBodyElementSoapBody_addDocument;
    }

    public static SOAPFault soapBody_addFault(Dom d, Name name, String s) throws SOAPException {
        SOAPFault sOAPFaultSoapBody_addFault;
        Locale l = d.locale();
        SOAPBody sb = (SOAPBody) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPFault sOAPFaultSoapBody_addFault2 = l._saaj.soapBody_addFault(sb, name, s);
                l.exit();
                return sOAPFaultSoapBody_addFault2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPFaultSoapBody_addFault = l._saaj.soapBody_addFault(sb, name, s);
                l.exit();
            } finally {
            }
        }
        return sOAPFaultSoapBody_addFault;
    }

    public static SOAPFault soapBody_addFault(Dom d, Name faultCode, String faultString, java.util.Locale locale) throws SOAPException {
        SOAPFault sOAPFaultSoapBody_addFault;
        Locale l = d.locale();
        SOAPBody sb = (SOAPBody) d;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPFault sOAPFaultSoapBody_addFault2 = l._saaj.soapBody_addFault(sb, faultCode, faultString, locale);
                l.exit();
                return sOAPFaultSoapBody_addFault2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPFaultSoapBody_addFault = l._saaj.soapBody_addFault(sb, faultCode, faultString, locale);
                l.exit();
            } finally {
            }
        }
        return sOAPFaultSoapBody_addFault;
    }

    public static void soapFault_setFaultString(Dom d, String faultString) {
        Locale l = d.locale();
        SOAPFault sf = (SOAPFault) d;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapFault_setFaultString(sf, faultString);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapFault_setFaultString(sf, faultString);
            l.exit();
        } finally {
        }
    }

    public static void soapFault_setFaultString(Dom d, String faultString, java.util.Locale locale) {
        Locale l = d.locale();
        SOAPFault sf = (SOAPFault) d;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapFault_setFaultString(sf, faultString, locale);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapFault_setFaultString(sf, faultString, locale);
            l.exit();
        } finally {
        }
    }

    public static void soapFault_setFaultCode(Dom d, Name faultCodeName) throws SOAPException {
        Locale l = d.locale();
        SOAPFault sf = (SOAPFault) d;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapFault_setFaultCode(sf, faultCodeName);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapFault_setFaultCode(sf, faultCodeName);
            l.exit();
        } finally {
        }
    }

    public static void soapFault_setFaultActor(Dom d, String faultActorString) {
        Locale l = d.locale();
        SOAPFault sf = (SOAPFault) d;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapFault_setFaultActor(sf, faultActorString);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapFault_setFaultActor(sf, faultActorString);
            l.exit();
        } finally {
        }
    }

    public static String soapFault_getFaultActor(Dom d) {
        String strSoapFault_getFaultActor;
        Locale l = d.locale();
        SOAPFault sf = (SOAPFault) d;
        if (l.noSync()) {
            l.enter();
            try {
                String strSoapFault_getFaultActor2 = l._saaj.soapFault_getFaultActor(sf);
                l.exit();
                return strSoapFault_getFaultActor2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                strSoapFault_getFaultActor = l._saaj.soapFault_getFaultActor(sf);
                l.exit();
            } finally {
            }
        }
        return strSoapFault_getFaultActor;
    }

    public static String soapFault_getFaultCode(Dom d) {
        String strSoapFault_getFaultCode;
        Locale l = d.locale();
        SOAPFault sf = (SOAPFault) d;
        if (l.noSync()) {
            l.enter();
            try {
                String strSoapFault_getFaultCode2 = l._saaj.soapFault_getFaultCode(sf);
                l.exit();
                return strSoapFault_getFaultCode2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                strSoapFault_getFaultCode = l._saaj.soapFault_getFaultCode(sf);
                l.exit();
            } finally {
            }
        }
        return strSoapFault_getFaultCode;
    }

    public static void soapFault_setFaultCode(Dom d, String faultCode) throws SOAPException {
        Locale l = d.locale();
        SOAPFault sf = (SOAPFault) d;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapFault_setFaultCode(sf, faultCode);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapFault_setFaultCode(sf, faultCode);
            l.exit();
        } finally {
        }
    }

    public static java.util.Locale soapFault_getFaultStringLocale(Dom d) {
        java.util.Locale localeSoapFault_getFaultStringLocale;
        Locale l = d.locale();
        SOAPFault sf = (SOAPFault) d;
        if (l.noSync()) {
            l.enter();
            try {
                java.util.Locale localeSoapFault_getFaultStringLocale2 = l._saaj.soapFault_getFaultStringLocale(sf);
                l.exit();
                return localeSoapFault_getFaultStringLocale2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                localeSoapFault_getFaultStringLocale = l._saaj.soapFault_getFaultStringLocale(sf);
                l.exit();
            } finally {
            }
        }
        return localeSoapFault_getFaultStringLocale;
    }

    public static Name soapFault_getFaultCodeAsName(Dom d) {
        Name nameSoapFault_getFaultCodeAsName;
        Locale l = d.locale();
        SOAPFault sf = (SOAPFault) d;
        if (l.noSync()) {
            l.enter();
            try {
                Name nameSoapFault_getFaultCodeAsName2 = l._saaj.soapFault_getFaultCodeAsName(sf);
                l.exit();
                return nameSoapFault_getFaultCodeAsName2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                nameSoapFault_getFaultCodeAsName = l._saaj.soapFault_getFaultCodeAsName(sf);
                l.exit();
            } finally {
            }
        }
        return nameSoapFault_getFaultCodeAsName;
    }

    public static String soapFault_getFaultString(Dom d) {
        String strSoapFault_getFaultString;
        Locale l = d.locale();
        SOAPFault sf = (SOAPFault) d;
        if (l.noSync()) {
            l.enter();
            try {
                String strSoapFault_getFaultString2 = l._saaj.soapFault_getFaultString(sf);
                l.exit();
                return strSoapFault_getFaultString2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                strSoapFault_getFaultString = l._saaj.soapFault_getFaultString(sf);
                l.exit();
            } finally {
            }
        }
        return strSoapFault_getFaultString;
    }

    public static Detail soapFault_addDetail(Dom d) throws SOAPException {
        Detail detailSoapFault_addDetail;
        Locale l = d.locale();
        SOAPFault sf = (SOAPFault) d;
        if (l.noSync()) {
            l.enter();
            try {
                Detail detailSoapFault_addDetail2 = l._saaj.soapFault_addDetail(sf);
                l.exit();
                return detailSoapFault_addDetail2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                detailSoapFault_addDetail = l._saaj.soapFault_addDetail(sf);
                l.exit();
            } finally {
            }
        }
        return detailSoapFault_addDetail;
    }

    public static Detail soapFault_getDetail(Dom d) {
        Detail detailSoapFault_getDetail;
        Locale l = d.locale();
        SOAPFault sf = (SOAPFault) d;
        if (l.noSync()) {
            l.enter();
            try {
                Detail detailSoapFault_getDetail2 = l._saaj.soapFault_getDetail(sf);
                l.exit();
                return detailSoapFault_getDetail2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                detailSoapFault_getDetail = l._saaj.soapFault_getDetail(sf);
                l.exit();
            } finally {
            }
        }
        return detailSoapFault_getDetail;
    }

    public static void soapHeaderElement_setMustUnderstand(Dom d, boolean mustUnderstand) {
        Locale l = d.locale();
        SOAPHeaderElement she = (SOAPHeaderElement) d;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapHeaderElement_setMustUnderstand(she, mustUnderstand);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapHeaderElement_setMustUnderstand(she, mustUnderstand);
            l.exit();
        } finally {
        }
    }

    public static boolean soapHeaderElement_getMustUnderstand(Dom d) {
        boolean zSoapHeaderElement_getMustUnderstand;
        Locale l = d.locale();
        SOAPHeaderElement she = (SOAPHeaderElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                boolean zSoapHeaderElement_getMustUnderstand2 = l._saaj.soapHeaderElement_getMustUnderstand(she);
                l.exit();
                return zSoapHeaderElement_getMustUnderstand2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                zSoapHeaderElement_getMustUnderstand = l._saaj.soapHeaderElement_getMustUnderstand(she);
                l.exit();
            } finally {
            }
        }
        return zSoapHeaderElement_getMustUnderstand;
    }

    public static void soapHeaderElement_setActor(Dom d, String actor) {
        Locale l = d.locale();
        SOAPHeaderElement she = (SOAPHeaderElement) d;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapHeaderElement_setActor(she, actor);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapHeaderElement_setActor(she, actor);
            l.exit();
        } finally {
        }
    }

    public static String soapHeaderElement_getActor(Dom d) {
        String strSoapHeaderElement_getActor;
        Locale l = d.locale();
        SOAPHeaderElement she = (SOAPHeaderElement) d;
        if (l.noSync()) {
            l.enter();
            try {
                String strSoapHeaderElement_getActor2 = l._saaj.soapHeaderElement_getActor(she);
                l.exit();
                return strSoapHeaderElement_getActor2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                strSoapHeaderElement_getActor = l._saaj.soapHeaderElement_getActor(she);
                l.exit();
            } finally {
            }
        }
        return strSoapHeaderElement_getActor;
    }

    public static DetailEntry detail_addDetailEntry(Dom d, Name name) {
        DetailEntry detailEntryDetail_addDetailEntry;
        Locale l = d.locale();
        Detail detail = (Detail) d;
        if (l.noSync()) {
            l.enter();
            try {
                DetailEntry detailEntryDetail_addDetailEntry2 = l._saaj.detail_addDetailEntry(detail, name);
                l.exit();
                return detailEntryDetail_addDetailEntry2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                detailEntryDetail_addDetailEntry = l._saaj.detail_addDetailEntry(detail, name);
                l.exit();
            } finally {
            }
        }
        return detailEntryDetail_addDetailEntry;
    }

    public static Iterator detail_getDetailEntries(Dom d) {
        Iterator itDetail_getDetailEntries;
        Locale l = d.locale();
        Detail detail = (Detail) d;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itDetail_getDetailEntries2 = l._saaj.detail_getDetailEntries(detail);
                l.exit();
                return itDetail_getDetailEntries2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itDetail_getDetailEntries = l._saaj.detail_getDetailEntries(detail);
                l.exit();
            } finally {
            }
        }
        return itDetail_getDetailEntries;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void _soapPart_removeAllMimeHeaders(Dom dom) {
        Locale l = dom.locale();
        SOAPPart sp = (SOAPPart) dom;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapPart_removeAllMimeHeaders(sp);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapPart_removeAllMimeHeaders(sp);
            l.exit();
        } finally {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void _soapPart_removeMimeHeader(Dom dom, String name) {
        Locale l = dom.locale();
        SOAPPart sp = (SOAPPart) dom;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapPart_removeMimeHeader(sp, name);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapPart_removeMimeHeader(sp, name);
            l.exit();
        } finally {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Iterator _soapPart_getAllMimeHeaders(Dom dom) {
        Iterator itSoapPart_getAllMimeHeaders;
        Locale l = dom.locale();
        SOAPPart sp = (SOAPPart) dom;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itSoapPart_getAllMimeHeaders2 = l._saaj.soapPart_getAllMimeHeaders(sp);
                l.exit();
                return itSoapPart_getAllMimeHeaders2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itSoapPart_getAllMimeHeaders = l._saaj.soapPart_getAllMimeHeaders(sp);
                l.exit();
            } finally {
            }
        }
        return itSoapPart_getAllMimeHeaders;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static SOAPEnvelope _soapPart_getEnvelope(Dom dom) {
        SOAPEnvelope sOAPEnvelopeSoapPart_getEnvelope;
        Locale l = dom.locale();
        SOAPPart sp = (SOAPPart) dom;
        if (l.noSync()) {
            l.enter();
            try {
                SOAPEnvelope sOAPEnvelopeSoapPart_getEnvelope2 = l._saaj.soapPart_getEnvelope(sp);
                l.exit();
                return sOAPEnvelopeSoapPart_getEnvelope2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sOAPEnvelopeSoapPart_getEnvelope = l._saaj.soapPart_getEnvelope(sp);
                l.exit();
            } finally {
            }
        }
        return sOAPEnvelopeSoapPart_getEnvelope;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Source _soapPart_getContent(Dom dom) {
        Source sourceSoapPart_getContent;
        Locale l = dom.locale();
        SOAPPart sp = (SOAPPart) dom;
        if (l.noSync()) {
            l.enter();
            try {
                Source sourceSoapPart_getContent2 = l._saaj.soapPart_getContent(sp);
                l.exit();
                return sourceSoapPart_getContent2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                sourceSoapPart_getContent = l._saaj.soapPart_getContent(sp);
                l.exit();
            } finally {
            }
        }
        return sourceSoapPart_getContent;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void _soapPart_setContent(Dom dom, Source source) {
        Locale l = dom.locale();
        SOAPPart sp = (SOAPPart) dom;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapPart_setContent(sp, source);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapPart_setContent(sp, source);
            l.exit();
        } finally {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static String[] _soapPart_getMimeHeader(Dom dom, String name) {
        String[] strArrSoapPart_getMimeHeader;
        Locale l = dom.locale();
        SOAPPart sp = (SOAPPart) dom;
        if (l.noSync()) {
            l.enter();
            try {
                String[] strArrSoapPart_getMimeHeader2 = l._saaj.soapPart_getMimeHeader(sp, name);
                l.exit();
                return strArrSoapPart_getMimeHeader2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                strArrSoapPart_getMimeHeader = l._saaj.soapPart_getMimeHeader(sp, name);
                l.exit();
            } finally {
            }
        }
        return strArrSoapPart_getMimeHeader;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void _soapPart_addMimeHeader(Dom dom, String name, String value) {
        Locale l = dom.locale();
        SOAPPart sp = (SOAPPart) dom;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapPart_addMimeHeader(sp, name, value);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapPart_addMimeHeader(sp, name, value);
            l.exit();
        } finally {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void _soapPart_setMimeHeader(Dom dom, String name, String value) {
        Locale l = dom.locale();
        SOAPPart sp = (SOAPPart) dom;
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    l._saaj.soapPart_setMimeHeader(sp, name, value);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            l._saaj.soapPart_setMimeHeader(sp, name, value);
            l.exit();
        } finally {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Iterator _soapPart_getMatchingMimeHeaders(Dom dom, String[] names) {
        Iterator itSoapPart_getMatchingMimeHeaders;
        Locale l = dom.locale();
        SOAPPart sp = (SOAPPart) dom;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itSoapPart_getMatchingMimeHeaders2 = l._saaj.soapPart_getMatchingMimeHeaders(sp, names);
                l.exit();
                return itSoapPart_getMatchingMimeHeaders2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itSoapPart_getMatchingMimeHeaders = l._saaj.soapPart_getMatchingMimeHeaders(sp, names);
                l.exit();
            } finally {
            }
        }
        return itSoapPart_getMatchingMimeHeaders;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Iterator _soapPart_getNonMatchingMimeHeaders(Dom dom, String[] names) {
        Iterator itSoapPart_getNonMatchingMimeHeaders;
        Locale l = dom.locale();
        SOAPPart sp = (SOAPPart) dom;
        if (l.noSync()) {
            l.enter();
            try {
                Iterator itSoapPart_getNonMatchingMimeHeaders2 = l._saaj.soapPart_getNonMatchingMimeHeaders(sp, names);
                l.exit();
                return itSoapPart_getNonMatchingMimeHeaders2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                itSoapPart_getNonMatchingMimeHeaders = l._saaj.soapPart_getNonMatchingMimeHeaders(sp, names);
                l.exit();
            } finally {
            }
        }
        return itSoapPart_getNonMatchingMimeHeaders;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/store/DomImpl$SaajData.class */
    private static class SaajData {
        Object _obj;

        private SaajData() {
        }
    }

    public static void saajCallback_setSaajData(Dom d, Object o) {
        Locale l = d.locale();
        if (!l.noSync()) {
            synchronized (l) {
                l.enter();
                try {
                    impl_saajCallback_setSaajData(d, o);
                    l.exit();
                } finally {
                }
            }
            return;
        }
        l.enter();
        try {
            impl_saajCallback_setSaajData(d, o);
            l.exit();
        } finally {
        }
    }

    public static void impl_saajCallback_setSaajData(Dom d, Object o) {
        Locale l = d.locale();
        Cur c = l.tempCur();
        c.moveToDom(d);
        SaajData sd = null;
        if (o != null) {
            sd = (SaajData) c.getBookmark(SaajData.class);
            if (sd == null) {
                sd = new SaajData();
            }
            sd._obj = o;
        }
        c.setBookmark(SaajData.class, sd);
        c.release();
    }

    public static Object saajCallback_getSaajData(Dom d) {
        Object objImpl_saajCallback_getSaajData;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                Object objImpl_saajCallback_getSaajData2 = impl_saajCallback_getSaajData(d);
                l.exit();
                return objImpl_saajCallback_getSaajData2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                objImpl_saajCallback_getSaajData = impl_saajCallback_getSaajData(d);
                l.exit();
            } finally {
            }
        }
        return objImpl_saajCallback_getSaajData;
    }

    public static Object impl_saajCallback_getSaajData(Dom d) {
        Locale l = d.locale();
        Cur c = l.tempCur();
        c.moveToDom(d);
        SaajData sd = (SaajData) c.getBookmark(SaajData.class);
        Object o = sd == null ? null : sd._obj;
        c.release();
        return o;
    }

    public static Element saajCallback_createSoapElement(Dom d, QName name, QName parentName) {
        Dom e;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                e = impl_saajCallback_createSoapElement(d, name, parentName);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    e = impl_saajCallback_createSoapElement(d, name, parentName);
                    l.exit();
                } finally {
                }
            }
        }
        return (Element) e;
    }

    public static Dom impl_saajCallback_createSoapElement(Dom d, QName name, QName parentName) {
        Cur c = d.locale().tempCur();
        c.createElement(name, parentName);
        Dom e = c.getDom();
        c.release();
        return e;
    }

    public static Element saajCallback_importSoapElement(Dom d, Element elem, boolean deep, QName parentName) {
        Dom e;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                e = impl_saajCallback_importSoapElement(d, elem, deep, parentName);
                l.exit();
            } finally {
            }
        } else {
            synchronized (l) {
                l.enter();
                try {
                    e = impl_saajCallback_importSoapElement(d, elem, deep, parentName);
                    l.exit();
                } finally {
                }
            }
        }
        return (Element) e;
    }

    public static Dom impl_saajCallback_importSoapElement(Dom d, Element elem, boolean deep, QName parentName) {
        throw new RuntimeException("Not impl");
    }

    public static Text saajCallback_ensureSoapTextNode(Dom d) {
        Text textImpl_saajCallback_ensureSoapTextNode;
        Locale l = d.locale();
        if (l.noSync()) {
            l.enter();
            try {
                Text textImpl_saajCallback_ensureSoapTextNode2 = impl_saajCallback_ensureSoapTextNode(d);
                l.exit();
                return textImpl_saajCallback_ensureSoapTextNode2;
            } finally {
            }
        }
        synchronized (l) {
            l.enter();
            try {
                textImpl_saajCallback_ensureSoapTextNode = impl_saajCallback_ensureSoapTextNode(d);
                l.exit();
            } finally {
            }
        }
        return textImpl_saajCallback_ensureSoapTextNode;
    }

    public static Text impl_saajCallback_ensureSoapTextNode(Dom d) {
        return null;
    }
}
