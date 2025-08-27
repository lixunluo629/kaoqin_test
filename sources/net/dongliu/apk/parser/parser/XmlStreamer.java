package net.dongliu.apk.parser.parser;

import net.dongliu.apk.parser.struct.xml.XmlCData;
import net.dongliu.apk.parser.struct.xml.XmlNamespaceEndTag;
import net.dongliu.apk.parser.struct.xml.XmlNamespaceStartTag;
import net.dongliu.apk.parser.struct.xml.XmlNodeEndTag;
import net.dongliu.apk.parser.struct.xml.XmlNodeStartTag;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/XmlStreamer.class */
public interface XmlStreamer {
    void onStartTag(XmlNodeStartTag xmlNodeStartTag);

    void onEndTag(XmlNodeEndTag xmlNodeEndTag);

    void onCData(XmlCData xmlCData);

    void onNamespaceStart(XmlNamespaceStartTag xmlNamespaceStartTag);

    void onNamespaceEnd(XmlNamespaceEndTag xmlNamespaceEndTag);
}
