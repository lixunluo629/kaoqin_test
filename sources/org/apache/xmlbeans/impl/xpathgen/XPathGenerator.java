package org.apache.xmlbeans.impl.xpathgen;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/xpathgen/XPathGenerator.class */
public class XPathGenerator {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XPathGenerator.class.desiredAssertionStatus();
    }

    public static String generateXPath(XmlCursor node, XmlCursor context, NamespaceContext nsctx) throws XPathGenerationException {
        if (node == null) {
            throw new IllegalArgumentException("Null node");
        }
        if (nsctx == null) {
            throw new IllegalArgumentException("Null namespace context");
        }
        XmlCursor.TokenType tt = node.currentTokenType();
        if (context != null && node.isAtSamePositionAs(context)) {
            return ".";
        }
        switch (tt.intValue()) {
            case 1:
            case 3:
                return generateInternal(node, context, nsctx);
            case 2:
            case 4:
            default:
                throw new XPathGenerationException("Cannot generate XPath for cursor position: " + tt.toString());
            case 5:
                int nrOfTextTokens = countTextTokens(node);
                node.toParent();
                String pathToParent = generateInternal(node, context, nsctx);
                if (nrOfTextTokens == 0) {
                    return pathToParent + "/text()";
                }
                return pathToParent + "/text()[position()=" + nrOfTextTokens + ']';
            case 6:
                QName name = node.getName();
                node.toParent();
                return generateInternal(node, context, nsctx) + "/@" + qnameToString(name, nsctx);
            case 7:
                QName name2 = node.getName();
                node.toParent();
                String pathToParent2 = generateInternal(node, context, nsctx);
                String prefix = name2.getLocalPart();
                if (prefix.length() == 0) {
                    return pathToParent2 + "/@xmlns";
                }
                return pathToParent2 + "/@xmlns:" + prefix;
        }
    }

    private static String generateInternal(XmlCursor node, XmlCursor context, NamespaceContext nsctx) throws XPathGenerationException {
        if (node.isStartdoc()) {
            return "";
        }
        if (context != null && node.isAtSamePositionAs(context)) {
            return ".";
        }
        if (!$assertionsDisabled && !node.isStart()) {
            throw new AssertionError();
        }
        QName name = node.getName();
        XmlCursor d = node.newCursor();
        if (!node.toParent()) {
            return "/" + name;
        }
        int elemIndex = 0;
        int i = 1;
        node.push();
        if (!node.toChild(name)) {
            throw new IllegalStateException("Must have at least one child with name: " + name);
        }
        do {
            if (node.isAtSamePositionAs(d)) {
                elemIndex = i;
            } else {
                i++;
            }
        } while (node.toNextSibling(name));
        node.pop();
        d.dispose();
        String pathToParent = generateInternal(node, context, nsctx);
        return i == 1 ? pathToParent + '/' + qnameToString(name, nsctx) : pathToParent + '/' + qnameToString(name, nsctx) + '[' + elemIndex + ']';
    }

    private static String qnameToString(QName qname, NamespaceContext ctx) throws XPathGenerationException {
        String localName = qname.getLocalPart();
        String uri = qname.getNamespaceURI();
        if (uri.length() == 0) {
            return localName;
        }
        String prefix = qname.getPrefix();
        if (prefix != null && prefix.length() > 0) {
            String mappedUri = ctx.getNamespaceURI(prefix);
            if (uri.equals(mappedUri)) {
                return prefix + ':' + localName;
            }
        }
        String prefix2 = ctx.getPrefix(uri);
        if (prefix2 == null) {
            throw new XPathGenerationException("Could not obtain a prefix for URI: " + uri);
        }
        if (prefix2.length() == 0) {
            throw new XPathGenerationException("Can not use default prefix in XPath for URI: " + uri);
        }
        return prefix2 + ':' + localName;
    }

    private static int countTextTokens(XmlCursor c) {
        int k = 0;
        int l = 0;
        XmlCursor d = c.newCursor();
        c.push();
        c.toParent();
        XmlCursor.TokenType firstContentToken = c.toFirstContentToken();
        while (true) {
            XmlCursor.TokenType tt = firstContentToken;
            if (tt.isEnd()) {
                break;
            }
            if (tt.isText()) {
                if (c.comparePosition(d) > 0) {
                    l++;
                } else {
                    k++;
                }
            } else if (tt.isStart()) {
                c.toEndToken();
            }
            firstContentToken = c.toNextToken();
        }
        c.pop();
        if (l == 0) {
            return 0;
        }
        return k;
    }

    public static void main(String[] args) throws XmlException {
        NamespaceContext ns = new NamespaceContext() { // from class: org.apache.xmlbeans.impl.xpathgen.XPathGenerator.1
            @Override // javax.xml.namespace.NamespaceContext
            public String getNamespaceURI(String prefix) {
                if ("ns".equals(prefix)) {
                    return "http://a.com";
                }
                return null;
            }

            @Override // javax.xml.namespace.NamespaceContext
            public String getPrefix(String namespaceUri) {
                return null;
            }

            @Override // javax.xml.namespace.NamespaceContext
            public Iterator getPrefixes(String namespaceUri) {
                return null;
            }
        };
        XmlCursor c = XmlObject.Factory.parse("<root>\n<ns:a xmlns:ns=\"http://a.com\"><b foo=\"value\">text1<c/>text2<c/>text3<c>text</c>text4</b></ns:a>\n</root>").newCursor();
        c.toFirstContentToken();
        c.toFirstContentToken();
        c.toFirstChild();
        c.toFirstChild();
        c.push();
        System.out.println(generateXPath(c, null, ns));
        c.pop();
        c.toNextSibling();
        c.toNextSibling();
        c.push();
        System.out.println(generateXPath(c, null, ns));
        c.pop();
        XmlCursor d = c.newCursor();
        d.toParent();
        c.push();
        System.out.println(generateXPath(c, d, ns));
        c.pop();
        d.toParent();
        c.push();
        System.out.println(generateXPath(c, d, ns));
        c.pop();
        c.toFirstContentToken();
        c.push();
        System.out.println(generateXPath(c, d, ns));
        c.pop();
        c.toParent();
        c.toPrevToken();
        c.push();
        System.out.println(generateXPath(c, d, ns));
        c.pop();
        c.toParent();
        c.push();
        System.out.println(generateXPath(c, d, ns));
        c.pop();
        c.toFirstAttribute();
        c.push();
        System.out.println(generateXPath(c, d, ns));
        c.pop();
        c.toParent();
        c.toParent();
        c.toNextToken();
        c.push();
        System.out.println(generateXPath(c, d, ns));
        c.pop();
        c.push();
        System.out.println(generateXPath(c, null, ns));
        c.pop();
    }
}
