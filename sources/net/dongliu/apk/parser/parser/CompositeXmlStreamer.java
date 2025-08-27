package net.dongliu.apk.parser.parser;

import net.dongliu.apk.parser.struct.xml.XmlCData;
import net.dongliu.apk.parser.struct.xml.XmlNamespaceEndTag;
import net.dongliu.apk.parser.struct.xml.XmlNamespaceStartTag;
import net.dongliu.apk.parser.struct.xml.XmlNodeEndTag;
import net.dongliu.apk.parser.struct.xml.XmlNodeStartTag;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/parser/CompositeXmlStreamer.class */
public class CompositeXmlStreamer implements XmlStreamer {
    public XmlStreamer[] xmlStreamers;

    public CompositeXmlStreamer(XmlStreamer... xmlStreamers) {
        this.xmlStreamers = xmlStreamers;
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onStartTag(XmlNodeStartTag xmlNodeStartTag) {
        for (XmlStreamer xmlStreamer : this.xmlStreamers) {
            xmlStreamer.onStartTag(xmlNodeStartTag);
        }
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onEndTag(XmlNodeEndTag xmlNodeEndTag) {
        for (XmlStreamer xmlStreamer : this.xmlStreamers) {
            xmlStreamer.onEndTag(xmlNodeEndTag);
        }
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onCData(XmlCData xmlCData) {
        for (XmlStreamer xmlStreamer : this.xmlStreamers) {
            xmlStreamer.onCData(xmlCData);
        }
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onNamespaceStart(XmlNamespaceStartTag tag) {
        for (XmlStreamer xmlStreamer : this.xmlStreamers) {
            xmlStreamer.onNamespaceStart(tag);
        }
    }

    @Override // net.dongliu.apk.parser.parser.XmlStreamer
    public void onNamespaceEnd(XmlNamespaceEndTag tag) {
        for (XmlStreamer xmlStreamer : this.xmlStreamers) {
            xmlStreamer.onNamespaceEnd(tag);
        }
    }
}
