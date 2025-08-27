package com.alibaba.excel.analysis.v07;

import com.alibaba.excel.cache.ReadCache;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/analysis/v07/SharedStringsTableHandler.class */
public class SharedStringsTableHandler extends DefaultHandler {
    private static final String T_TAG = "t";
    private static final String SI_TAG = "si";
    private static final String RPH_TAG = "rPh";
    private StringBuilder currentData;
    private StringBuilder currentElementData;
    private ReadCache readCache;
    private boolean ignoreTagt = false;
    private boolean isTagt = false;

    public SharedStringsTableHandler(ReadCache readCache) {
        this.readCache = readCache;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String name, Attributes attributes) {
        if ("t".equals(name)) {
            this.currentElementData = null;
            this.isTagt = true;
        } else if (SI_TAG.equals(name)) {
            this.currentData = null;
        } else if (RPH_TAG.equals(name)) {
            this.ignoreTagt = true;
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String name) {
        if ("t".equals(name)) {
            if (this.currentElementData != null) {
                if (this.currentData == null) {
                    this.currentData = new StringBuilder();
                }
                this.currentData.append((CharSequence) this.currentElementData);
            }
            this.isTagt = false;
            return;
        }
        if (SI_TAG.equals(name)) {
            if (this.currentData == null) {
                this.readCache.put(null);
                return;
            } else {
                this.readCache.put(this.currentData.toString());
                return;
            }
        }
        if (RPH_TAG.equals(name)) {
            this.ignoreTagt = false;
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch2, int start, int length) {
        if (!this.isTagt || this.ignoreTagt) {
            return;
        }
        if (this.currentElementData == null) {
            this.currentElementData = new StringBuilder();
        }
        this.currentElementData.append(ch2, start, length);
    }
}
