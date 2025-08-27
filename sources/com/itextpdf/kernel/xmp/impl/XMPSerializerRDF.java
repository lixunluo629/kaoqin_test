package com.itextpdf.kernel.xmp.impl;

import com.itextpdf.kernel.xmp.PdfConst;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.kernel.xmp.options.SerializeOptions;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.xmlbeans.impl.common.Sax2Dom;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/xmp/impl/XMPSerializerRDF.class */
public class XMPSerializerRDF {
    private static final int DEFAULT_PAD = 2048;
    private static final String PACKET_HEADER = "<?xpacket begin=\"\ufeff\" id=\"W5M0MpCehiHzreSzNTczkc9d\"?>";
    private static final String PACKET_TRAILER = "<?xpacket end=\"";
    private static final String PACKET_TRAILER2 = "\"?>";
    private static final String RDF_XMPMETA_START = "<x:xmpmeta xmlns:x=\"adobe:ns:meta/\" x:xmptk=\"";
    private static final String RDF_XMPMETA_END = "</x:xmpmeta>";
    private static final String RDF_RDF_START = "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">";
    private static final String RDF_RDF_END = "</rdf:RDF>";
    private static final String RDF_SCHEMA_START = "<rdf:Description rdf:about=";
    private static final String RDF_SCHEMA_END = "</rdf:Description>";
    private static final String RDF_STRUCT_START = "<rdf:Description";
    private static final String RDF_STRUCT_END = "</rdf:Description>";
    private static final String RDF_EMPTY_STRUCT = "<rdf:Description/>";
    static final Set<String> RDF_ATTR_QUALIFIER = new HashSet(Arrays.asList("xml:lang", "rdf:resource", "rdf:ID", "rdf:bagID", "rdf:nodeID"));
    private XMPMetaImpl xmp;
    private CountOutputStream outputStream;
    private OutputStreamWriter writer;
    private SerializeOptions options;
    private int unicodeSize = 1;
    private int padding;

    public void serialize(XMPMeta xmp, OutputStream out, SerializeOptions options) throws XMPException, IOException {
        try {
            this.outputStream = new CountOutputStream(out);
            this.xmp = (XMPMetaImpl) xmp;
            this.options = options;
            this.padding = options.getPadding();
            this.writer = new OutputStreamWriter(this.outputStream, options.getEncoding());
            checkOptionsConsistence();
            String tailStr = serializeAsRDF();
            this.writer.flush();
            addPadding(tailStr.length());
            write(tailStr);
            this.writer.flush();
            this.outputStream.close();
        } catch (IOException e) {
            throw new XMPException("Error writing to the OutputStream", 0);
        }
    }

    private void addPadding(int tailLength) throws XMPException, IOException {
        if (this.options.getExactPacketLength()) {
            int minSize = this.outputStream.getBytesWritten() + (tailLength * this.unicodeSize);
            if (minSize > this.padding) {
                throw new XMPException("Can't fit into specified packet size", 107);
            }
            this.padding -= minSize;
        }
        this.padding /= this.unicodeSize;
        int newlineLen = this.options.getNewline().length();
        if (this.padding >= newlineLen) {
            this.padding -= newlineLen;
            while (this.padding >= 100 + newlineLen) {
                writeChars(100, ' ');
                writeNewline();
                this.padding -= 100 + newlineLen;
            }
            writeChars(this.padding, ' ');
            writeNewline();
            return;
        }
        writeChars(this.padding, ' ');
    }

    protected void checkOptionsConsistence() throws XMPException {
        if (this.options.getEncodeUTF16BE() | this.options.getEncodeUTF16LE()) {
            this.unicodeSize = 2;
        }
        if (this.options.getExactPacketLength()) {
            if (this.options.getOmitPacketWrapper() | this.options.getIncludeThumbnailPad()) {
                throw new XMPException("Inconsistent options for exact size serialize", 103);
            }
            if ((this.options.getPadding() & (this.unicodeSize - 1)) != 0) {
                throw new XMPException("Exact size must be a multiple of the Unicode element", 103);
            }
            return;
        }
        if (this.options.getReadOnlyPacket()) {
            if (this.options.getOmitPacketWrapper() | this.options.getIncludeThumbnailPad()) {
                throw new XMPException("Inconsistent options for read-only packet", 103);
            }
            this.padding = 0;
        } else if (this.options.getOmitPacketWrapper()) {
            if (this.options.getIncludeThumbnailPad()) {
                throw new XMPException("Inconsistent options for non-packet serialize", 103);
            }
            this.padding = 0;
        } else {
            if (this.padding == 0) {
                this.padding = 2048 * this.unicodeSize;
            }
            if (this.options.getIncludeThumbnailPad() && !this.xmp.doesPropertyExist("http://ns.adobe.com/xap/1.0/", PdfConst.Thumbnails)) {
                this.padding += 10000 * this.unicodeSize;
            }
        }
    }

    private String serializeAsRDF() throws XMPException, IOException {
        int level = 0;
        if (!this.options.getOmitPacketWrapper()) {
            writeIndent(0);
            write(PACKET_HEADER);
            writeNewline();
        }
        if (!this.options.getOmitXmpMetaElement()) {
            writeIndent(0);
            write(RDF_XMPMETA_START);
            if (!this.options.getOmitVersionAttribute()) {
                write(XMPMetaFactory.getVersionInfo().getMessage());
            }
            write("\">");
            writeNewline();
            level = 0 + 1;
        }
        writeIndent(level);
        write(RDF_RDF_START);
        writeNewline();
        if (this.options.getUseCanonicalFormat()) {
            serializeCanonicalRDFSchemas(level);
        } else {
            serializeCompactRDFSchemas(level);
        }
        writeIndent(level);
        write(RDF_RDF_END);
        writeNewline();
        if (!this.options.getOmitXmpMetaElement()) {
            writeIndent(level - 1);
            write(RDF_XMPMETA_END);
            writeNewline();
        }
        String tailStr = "";
        if (!this.options.getOmitPacketWrapper()) {
            for (int level2 = this.options.getBaseIndent(); level2 > 0; level2--) {
                tailStr = tailStr + this.options.getIndent();
            }
            tailStr = ((tailStr + PACKET_TRAILER) + (this.options.getReadOnlyPacket() ? 'r' : 'w')) + PACKET_TRAILER2;
        }
        return tailStr;
    }

    private void serializeCanonicalRDFSchemas(int level) throws XMPException, IOException {
        if (this.xmp.getRoot().getChildrenLength() > 0) {
            startOuterRDFDescription(this.xmp.getRoot(), level);
            Iterator it = this.xmp.getRoot().iterateChildren();
            while (it.hasNext()) {
                XMPNode currSchema = (XMPNode) it.next();
                serializeCanonicalRDFSchema(currSchema, level);
            }
            endOuterRDFDescription(level);
            return;
        }
        writeIndent(level + 1);
        write(RDF_SCHEMA_START);
        writeTreeName();
        write("/>");
        writeNewline();
    }

    private void writeTreeName() throws IOException {
        write(34);
        String name = this.xmp.getRoot().getName();
        if (name != null) {
            appendNodeValue(name, true);
        }
        write(34);
    }

    private void serializeCompactRDFSchemas(int level) throws XMPException, IOException {
        writeIndent(level + 1);
        write(RDF_SCHEMA_START);
        writeTreeName();
        Set<String> usedPrefixes = new HashSet<>();
        usedPrefixes.add("xml");
        usedPrefixes.add("rdf");
        Iterator it = this.xmp.getRoot().iterateChildren();
        while (it.hasNext()) {
            XMPNode schema = (XMPNode) it.next();
            declareUsedNamespaces(schema, usedPrefixes, level + 3);
        }
        boolean allAreAttrs = true;
        Iterator it2 = this.xmp.getRoot().iterateChildren();
        while (it2.hasNext()) {
            XMPNode schema2 = (XMPNode) it2.next();
            allAreAttrs &= serializeCompactRDFAttrProps(schema2, level + 2);
        }
        if (!allAreAttrs) {
            write(62);
            writeNewline();
            Iterator it3 = this.xmp.getRoot().iterateChildren();
            while (it3.hasNext()) {
                XMPNode schema3 = (XMPNode) it3.next();
                serializeCompactRDFElementProps(schema3, level + 2);
            }
            writeIndent(level + 1);
            write("</rdf:Description>");
            writeNewline();
            return;
        }
        write("/>");
        writeNewline();
    }

    private boolean serializeCompactRDFAttrProps(XMPNode parentNode, int indent) throws IOException {
        boolean allAreAttrs = true;
        Iterator it = parentNode.iterateChildren();
        while (it.hasNext()) {
            XMPNode prop = (XMPNode) it.next();
            if (canBeRDFAttrProp(prop)) {
                writeNewline();
                writeIndent(indent);
                write(prop.getName());
                write("=\"");
                appendNodeValue(prop.getValue(), true);
                write(34);
            } else {
                allAreAttrs = false;
            }
        }
        return allAreAttrs;
    }

    private void serializeCompactRDFElementProps(XMPNode parentNode, int indent) throws XMPException, IOException {
        Iterator it = parentNode.iterateChildren();
        while (it.hasNext()) {
            XMPNode node = (XMPNode) it.next();
            if (!canBeRDFAttrProp(node)) {
                boolean emitEndTag = true;
                boolean indentEndTag = true;
                String elemName = node.getName();
                if ("[]".equals(elemName)) {
                    elemName = "rdf:li";
                }
                writeIndent(indent);
                write(60);
                write(elemName);
                boolean hasGeneralQualifiers = false;
                boolean hasRDFResourceQual = false;
                Iterator iq = node.iterateQualifier();
                while (iq.hasNext()) {
                    XMPNode qualifier = (XMPNode) iq.next();
                    if (!RDF_ATTR_QUALIFIER.contains(qualifier.getName())) {
                        hasGeneralQualifiers = true;
                    } else {
                        hasRDFResourceQual = "rdf:resource".equals(qualifier.getName());
                        write(32);
                        write(qualifier.getName());
                        write("=\"");
                        appendNodeValue(qualifier.getValue(), true);
                        write(34);
                    }
                }
                if (hasGeneralQualifiers) {
                    serializeCompactRDFGeneralQualifier(indent, node);
                } else if (!node.getOptions().isCompositeProperty()) {
                    boolean[] result = serializeCompactRDFSimpleProp(node);
                    emitEndTag = result[0];
                    indentEndTag = result[1];
                } else if (node.getOptions().isArray()) {
                    serializeCompactRDFArrayProp(node, indent);
                } else {
                    emitEndTag = serializeCompactRDFStructProp(node, indent, hasRDFResourceQual);
                }
                if (emitEndTag) {
                    if (indentEndTag) {
                        writeIndent(indent);
                    }
                    write("</");
                    write(elemName);
                    write(62);
                    writeNewline();
                }
            }
        }
    }

    private boolean[] serializeCompactRDFSimpleProp(XMPNode node) throws IOException {
        boolean emitEndTag = true;
        boolean indentEndTag = true;
        if (node.getOptions().isURI()) {
            write(" rdf:resource=\"");
            appendNodeValue(node.getValue(), true);
            write("\"/>");
            writeNewline();
            emitEndTag = false;
        } else if (node.getValue() == null || node.getValue().length() == 0) {
            write("/>");
            writeNewline();
            emitEndTag = false;
        } else {
            write(62);
            appendNodeValue(node.getValue(), false);
            indentEndTag = false;
        }
        return new boolean[]{emitEndTag, indentEndTag};
    }

    private void serializeCompactRDFArrayProp(XMPNode node, int indent) throws XMPException, IOException {
        write(62);
        writeNewline();
        emitRDFArrayTag(node, true, indent + 1);
        if (node.getOptions().isArrayAltText()) {
            XMPNodeUtils.normalizeLangArray(node);
        }
        serializeCompactRDFElementProps(node, indent + 2);
        emitRDFArrayTag(node, false, indent + 1);
    }

    private boolean serializeCompactRDFStructProp(XMPNode node, int indent, boolean hasRDFResourceQual) throws XMPException, IOException {
        boolean hasAttrFields = false;
        boolean hasElemFields = false;
        boolean emitEndTag = true;
        Iterator ic = node.iterateChildren();
        while (ic.hasNext()) {
            XMPNode field = (XMPNode) ic.next();
            if (canBeRDFAttrProp(field)) {
                hasAttrFields = true;
            } else {
                hasElemFields = true;
            }
            if (hasAttrFields && hasElemFields) {
                break;
            }
        }
        if (hasRDFResourceQual && hasElemFields) {
            throw new XMPException("Can't mix rdf:resource qualifier and element fields", 202);
        }
        if (!node.hasChildren()) {
            write(" rdf:parseType=\"Resource\"/>");
            writeNewline();
            emitEndTag = false;
        } else if (!hasElemFields) {
            serializeCompactRDFAttrProps(node, indent + 1);
            write("/>");
            writeNewline();
            emitEndTag = false;
        } else if (!hasAttrFields) {
            write(" rdf:parseType=\"Resource\">");
            writeNewline();
            serializeCompactRDFElementProps(node, indent + 1);
        } else {
            write(62);
            writeNewline();
            writeIndent(indent + 1);
            write(RDF_STRUCT_START);
            serializeCompactRDFAttrProps(node, indent + 2);
            write(">");
            writeNewline();
            serializeCompactRDFElementProps(node, indent + 1);
            writeIndent(indent + 1);
            write("</rdf:Description>");
            writeNewline();
        }
        return emitEndTag;
    }

    private void serializeCompactRDFGeneralQualifier(int indent, XMPNode node) throws XMPException, IOException {
        write(" rdf:parseType=\"Resource\">");
        writeNewline();
        serializeCanonicalRDFProperty(node, false, true, indent + 1);
        Iterator iq = node.iterateQualifier();
        while (iq.hasNext()) {
            XMPNode qualifier = (XMPNode) iq.next();
            serializeCanonicalRDFProperty(qualifier, false, false, indent + 1);
        }
    }

    private void serializeCanonicalRDFSchema(XMPNode schemaNode, int level) throws XMPException, IOException {
        Iterator it = schemaNode.iterateChildren();
        while (it.hasNext()) {
            XMPNode propNode = (XMPNode) it.next();
            serializeCanonicalRDFProperty(propNode, this.options.getUseCanonicalFormat(), false, level + 2);
        }
    }

    private void declareUsedNamespaces(XMPNode node, Set<String> usedPrefixes, int indent) throws IOException {
        if (node.getOptions().isSchemaNode()) {
            String prefix = node.getValue().substring(0, node.getValue().length() - 1);
            declareNamespace(prefix, node.getName(), usedPrefixes, indent);
        } else if (node.getOptions().isStruct()) {
            Iterator it = node.iterateChildren();
            while (it.hasNext()) {
                XMPNode field = (XMPNode) it.next();
                declareNamespace(field.getName(), null, usedPrefixes, indent);
            }
        }
        Iterator it2 = node.iterateChildren();
        while (it2.hasNext()) {
            XMPNode child = (XMPNode) it2.next();
            declareUsedNamespaces(child, usedPrefixes, indent);
        }
        Iterator it3 = node.iterateQualifier();
        while (it3.hasNext()) {
            XMPNode qualifier = (XMPNode) it3.next();
            declareNamespace(qualifier.getName(), null, usedPrefixes, indent);
            declareUsedNamespaces(qualifier, usedPrefixes, indent);
        }
    }

    private void declareNamespace(String prefix, String namespace, Set<String> usedPrefixes, int indent) throws IOException {
        if (namespace == null) {
            QName qname = new QName(prefix);
            if (qname.hasPrefix()) {
                prefix = qname.getPrefix();
                namespace = XMPMetaFactory.getSchemaRegistry().getNamespaceURI(prefix + ":");
                declareNamespace(prefix, namespace, usedPrefixes, indent);
            } else {
                return;
            }
        }
        if (!usedPrefixes.contains(prefix)) {
            writeNewline();
            writeIndent(indent);
            write(Sax2Dom.XMLNS_STRING);
            write(prefix);
            write("=\"");
            write(namespace);
            write(34);
            usedPrefixes.add(prefix);
        }
    }

    private void startOuterRDFDescription(XMPNode schemaNode, int level) throws IOException {
        writeIndent(level + 1);
        write(RDF_SCHEMA_START);
        writeTreeName();
        Set<String> usedPrefixes = new HashSet<>();
        usedPrefixes.add("xml");
        usedPrefixes.add("rdf");
        declareUsedNamespaces(schemaNode, usedPrefixes, level + 3);
        write(62);
        writeNewline();
    }

    private void endOuterRDFDescription(int level) throws IOException {
        writeIndent(level + 1);
        write("</rdf:Description>");
        writeNewline();
    }

    private void serializeCanonicalRDFProperty(XMPNode node, boolean useCanonicalRDF, boolean emitAsRDFValue, int indent) throws XMPException, IOException {
        boolean emitEndTag = true;
        boolean indentEndTag = true;
        String elemName = node.getName();
        if (emitAsRDFValue) {
            elemName = "rdf:value";
        } else if ("[]".equals(elemName)) {
            elemName = "rdf:li";
        }
        writeIndent(indent);
        write(60);
        write(elemName);
        boolean hasGeneralQualifiers = false;
        boolean hasRDFResourceQual = false;
        Iterator it = node.iterateQualifier();
        while (it.hasNext()) {
            XMPNode qualifier = (XMPNode) it.next();
            if (!RDF_ATTR_QUALIFIER.contains(qualifier.getName())) {
                hasGeneralQualifiers = true;
            } else {
                hasRDFResourceQual = "rdf:resource".equals(qualifier.getName());
                if (!emitAsRDFValue) {
                    write(32);
                    write(qualifier.getName());
                    write("=\"");
                    appendNodeValue(qualifier.getValue(), true);
                    write(34);
                }
            }
        }
        if (hasGeneralQualifiers && !emitAsRDFValue) {
            if (hasRDFResourceQual) {
                throw new XMPException("Can't mix rdf:resource and general qualifiers", 202);
            }
            if (useCanonicalRDF) {
                write(">");
                writeNewline();
                indent++;
                writeIndent(indent);
                write(RDF_STRUCT_START);
                write(">");
            } else {
                write(" rdf:parseType=\"Resource\">");
            }
            writeNewline();
            serializeCanonicalRDFProperty(node, useCanonicalRDF, true, indent + 1);
            Iterator it2 = node.iterateQualifier();
            while (it2.hasNext()) {
                XMPNode qualifier2 = (XMPNode) it2.next();
                if (!RDF_ATTR_QUALIFIER.contains(qualifier2.getName())) {
                    serializeCanonicalRDFProperty(qualifier2, useCanonicalRDF, false, indent + 1);
                }
            }
            if (useCanonicalRDF) {
                writeIndent(indent);
                write("</rdf:Description>");
                writeNewline();
                indent--;
            }
        } else if (!node.getOptions().isCompositeProperty()) {
            if (node.getOptions().isURI()) {
                write(" rdf:resource=\"");
                appendNodeValue(node.getValue(), true);
                write("\"/>");
                writeNewline();
                emitEndTag = false;
            } else if (node.getValue() == null || "".equals(node.getValue())) {
                write("/>");
                writeNewline();
                emitEndTag = false;
            } else {
                write(62);
                appendNodeValue(node.getValue(), false);
                indentEndTag = false;
            }
        } else if (node.getOptions().isArray()) {
            write(62);
            writeNewline();
            emitRDFArrayTag(node, true, indent + 1);
            if (node.getOptions().isArrayAltText()) {
                XMPNodeUtils.normalizeLangArray(node);
            }
            Iterator it3 = node.iterateChildren();
            while (it3.hasNext()) {
                serializeCanonicalRDFProperty((XMPNode) it3.next(), useCanonicalRDF, false, indent + 2);
            }
            emitRDFArrayTag(node, false, indent + 1);
        } else if (!hasRDFResourceQual) {
            if (!node.hasChildren()) {
                if (useCanonicalRDF) {
                    write(">");
                    writeNewline();
                    writeIndent(indent + 1);
                    write(RDF_EMPTY_STRUCT);
                } else {
                    write(" rdf:parseType=\"Resource\"/>");
                    emitEndTag = false;
                }
                writeNewline();
            } else {
                if (useCanonicalRDF) {
                    write(">");
                    writeNewline();
                    indent++;
                    writeIndent(indent);
                    write(RDF_STRUCT_START);
                    write(">");
                } else {
                    write(" rdf:parseType=\"Resource\">");
                }
                writeNewline();
                Iterator it4 = node.iterateChildren();
                while (it4.hasNext()) {
                    serializeCanonicalRDFProperty((XMPNode) it4.next(), useCanonicalRDF, false, indent + 1);
                }
                if (useCanonicalRDF) {
                    writeIndent(indent);
                    write("</rdf:Description>");
                    writeNewline();
                    indent--;
                }
            }
        } else {
            Iterator it5 = node.iterateChildren();
            while (it5.hasNext()) {
                XMPNode child = (XMPNode) it5.next();
                if (!canBeRDFAttrProp(child)) {
                    throw new XMPException("Can't mix rdf:resource and complex fields", 202);
                }
                writeNewline();
                writeIndent(indent + 1);
                write(32);
                write(child.getName());
                write("=\"");
                appendNodeValue(child.getValue(), true);
                write(34);
            }
            write("/>");
            writeNewline();
            emitEndTag = false;
        }
        if (emitEndTag) {
            if (indentEndTag) {
                writeIndent(indent);
            }
            write("</");
            write(elemName);
            write(62);
            writeNewline();
        }
    }

    private void emitRDFArrayTag(XMPNode arrayNode, boolean isStartTag, int indent) throws IOException {
        if (isStartTag || arrayNode.hasChildren()) {
            writeIndent(indent);
            write(isStartTag ? "<rdf:" : "</rdf:");
            if (arrayNode.getOptions().isArrayAlternate()) {
                write("Alt");
            } else if (arrayNode.getOptions().isArrayOrdered()) {
                write("Seq");
            } else {
                write("Bag");
            }
            if (isStartTag && !arrayNode.hasChildren()) {
                write("/>");
            } else {
                write(">");
            }
            writeNewline();
        }
    }

    private void appendNodeValue(String value, boolean forAttribute) throws IOException {
        if (value == null) {
            value = "";
        }
        write(Utils.escapeXML(value, forAttribute, true));
    }

    private boolean canBeRDFAttrProp(XMPNode node) {
        return (node.hasQualifier() || node.getOptions().isURI() || node.getOptions().isCompositeProperty() || node.getOptions().containsOneOf(1073741824) || "[]".equals(node.getName())) ? false : true;
    }

    private void writeIndent(int times) throws IOException {
        for (int i = this.options.getBaseIndent() + times; i > 0; i--) {
            this.writer.write(this.options.getIndent());
        }
    }

    private void write(int c) throws IOException {
        this.writer.write(c);
    }

    private void write(String str) throws IOException {
        this.writer.write(str);
    }

    private void writeChars(int number, char c) throws IOException {
        while (number > 0) {
            this.writer.write(c);
            number--;
        }
    }

    private void writeNewline() throws IOException {
        this.writer.write(this.options.getNewline());
    }
}
