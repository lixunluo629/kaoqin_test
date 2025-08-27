package net.dongliu.apk.parser.parser;

import ch.qos.logback.classic.net.SyslogAppender;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.List;
import net.dongliu.apk.parser.parser.XmlNamespaces;
import net.dongliu.apk.parser.struct.xml.Attribute;
import net.dongliu.apk.parser.struct.xml.XmlCData;
import net.dongliu.apk.parser.struct.xml.XmlNamespaceEndTag;
import net.dongliu.apk.parser.struct.xml.XmlNamespaceStartTag;
import net.dongliu.apk.parser.struct.xml.XmlNodeEndTag;
import net.dongliu.apk.parser.struct.xml.XmlNodeStartTag;
import net.dongliu.apk.parser.utils.xml.XmlEscaper;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/XmlTranslator.class */
public class XmlTranslator implements XmlStreamer {
    private XmlNamespaces namespaces;
    private boolean isLastStartTag;
    private int shift = 0;
    private StringBuilder sb = new StringBuilder();

    public XmlTranslator() {
        this.sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        this.namespaces = new XmlNamespaces();
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onStartTag(XmlNodeStartTag xmlNodeStartTag) {
        if (this.isLastStartTag) {
            this.sb.append(">\n");
        }
        int i = this.shift;
        this.shift = i + 1;
        appendShift(i);
        this.sb.append('<');
        if (xmlNodeStartTag.getNamespace() != null) {
            String prefix = this.namespaces.getPrefixViaUri(xmlNodeStartTag.getNamespace());
            if (prefix != null) {
                this.sb.append(prefix).append(":");
            } else {
                this.sb.append(xmlNodeStartTag.getNamespace()).append(":");
            }
        }
        this.sb.append(xmlNodeStartTag.getName());
        List<XmlNamespaces.XmlNamespace> nps = this.namespaces.consumeNameSpaces();
        if (!nps.isEmpty()) {
            for (XmlNamespaces.XmlNamespace np : nps) {
                this.sb.append(" xmlns:").append(np.getPrefix()).append("=\"").append(np.getUri()).append(SymbolConstants.QUOTES_SYMBOL);
            }
        }
        this.isLastStartTag = true;
        for (Attribute attribute : xmlNodeStartTag.getAttributes().values()) {
            onAttribute(attribute);
        }
    }

    private void onAttribute(Attribute attribute) {
        this.sb.append(SymbolConstants.SPACE_SYMBOL);
        String namespace = this.namespaces.getPrefixViaUri(attribute.getNamespace());
        if (namespace == null) {
            namespace = attribute.getNamespace();
        }
        if (namespace != null && !namespace.isEmpty()) {
            this.sb.append(namespace).append(':');
        }
        String escapedFinalValue = XmlEscaper.escapeXml10(attribute.getValue());
        this.sb.append(attribute.getName()).append('=').append('\"').append(escapedFinalValue).append('\"');
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onEndTag(XmlNodeEndTag xmlNodeEndTag) {
        this.shift--;
        if (this.isLastStartTag) {
            this.sb.append(" />\n");
        } else {
            appendShift(this.shift);
            this.sb.append("</");
            if (xmlNodeEndTag.getNamespace() != null) {
                this.sb.append(xmlNodeEndTag.getNamespace()).append(":");
            }
            this.sb.append(xmlNodeEndTag.getName());
            this.sb.append(">\n");
        }
        this.isLastStartTag = false;
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onCData(XmlCData xmlCData) {
        appendShift(this.shift);
        this.sb.append(xmlCData.getValue()).append('\n');
        this.isLastStartTag = false;
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onNamespaceStart(XmlNamespaceStartTag tag) {
        this.namespaces.addNamespace(tag);
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onNamespaceEnd(XmlNamespaceEndTag tag) {
        this.namespaces.removeNamespace(tag);
    }

    private void appendShift(int shift) {
        for (int i = 0; i < shift; i++) {
            this.sb.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN);
        }
    }

    public String getXml() {
        return this.sb.toString();
    }
}
