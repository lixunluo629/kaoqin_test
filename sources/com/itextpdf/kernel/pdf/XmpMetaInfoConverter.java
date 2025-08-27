package com.itextpdf.kernel.pdf;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.xmp.PdfConst;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.kernel.xmp.options.PropertyOptions;
import com.itextpdf.kernel.xmp.properties.XMPProperty;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/pdf/XmpMetaInfoConverter.class */
class XmpMetaInfoConverter {
    private XmpMetaInfoConverter() {
    }

    static void appendMetadataToInfo(byte[] xmpMetadata, PdfDocumentInfo info) {
        if (xmpMetadata != null) {
            try {
                XMPMeta meta = XMPMetaFactory.parseFromBuffer(xmpMetadata);
                XMPProperty title = meta.getLocalizedText("http://purl.org/dc/elements/1.1/", "title", "x-default", "x-default");
                if (title != null) {
                    info.setTitle(title.getValue());
                }
                String author = fetchArrayIntoString(meta, "http://purl.org/dc/elements/1.1/", PdfConst.Creator);
                if (author != null) {
                    info.setAuthor(author);
                }
                XMPProperty keywords = meta.getProperty("http://ns.adobe.com/pdf/1.3/", PdfConst.Keywords);
                if (keywords != null) {
                    info.setKeywords(keywords.getValue());
                } else {
                    String keywordsStr = fetchArrayIntoString(meta, "http://purl.org/dc/elements/1.1/", PdfConst.Subject);
                    if (keywordsStr != null) {
                        info.setKeywords(keywordsStr);
                    }
                }
                XMPProperty subject = meta.getLocalizedText("http://purl.org/dc/elements/1.1/", "description", "x-default", "x-default");
                if (subject != null) {
                    info.setSubject(subject.getValue());
                }
                XMPProperty creator = meta.getProperty("http://ns.adobe.com/xap/1.0/", PdfConst.CreatorTool);
                if (creator != null) {
                    info.setCreator(creator.getValue());
                }
                XMPProperty producer = meta.getProperty("http://ns.adobe.com/pdf/1.3/", PdfConst.Producer);
                if (producer != null) {
                    info.put(PdfName.Producer, new PdfString(producer.getValue(), PdfEncodings.UNICODE_BIG));
                }
                XMPProperty trapped = meta.getProperty("http://ns.adobe.com/pdf/1.3/", PdfConst.Trapped);
                if (trapped != null) {
                    info.setTrapped(new PdfName(trapped.getValue()));
                }
            } catch (XMPException e) {
            }
        }
    }

    static void appendDocumentInfoToMetadata(PdfDocumentInfo info, XMPMeta xmpMeta) throws XMPException {
        String value;
        PdfDictionary docInfo = info.getPdfObject();
        if (docInfo != null) {
            for (PdfName pdfName : docInfo.keySet()) {
                PdfObject obj = docInfo.get(pdfName);
                if (obj != null) {
                    if (obj.isString()) {
                        value = ((PdfString) obj).toUnicodeString();
                    } else if (obj.isName()) {
                        value = ((PdfName) obj).getValue();
                    }
                    if (!PdfName.Title.equals(pdfName)) {
                        if (!PdfName.Author.equals(pdfName)) {
                            if (!PdfName.Subject.equals(pdfName)) {
                                if (!PdfName.Keywords.equals(pdfName)) {
                                    if (!PdfName.Creator.equals(pdfName)) {
                                        if (!PdfName.Producer.equals(pdfName)) {
                                            if (!PdfName.CreationDate.equals(pdfName)) {
                                                if (!PdfName.ModDate.equals(pdfName)) {
                                                    if (PdfName.Trapped.equals(pdfName)) {
                                                        xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", PdfConst.Trapped, value);
                                                    }
                                                } else {
                                                    xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", PdfConst.ModifyDate, PdfDate.getW3CDate(value));
                                                }
                                            } else {
                                                xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", PdfConst.CreateDate, PdfDate.getW3CDate(value));
                                            }
                                        } else {
                                            xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", PdfConst.Producer, value);
                                        }
                                    } else {
                                        xmpMeta.setProperty("http://ns.adobe.com/xap/1.0/", PdfConst.CreatorTool, value);
                                    }
                                } else {
                                    for (String v : value.split(",|;")) {
                                        if (v.trim().length() > 0) {
                                            appendArrayItemIfDoesNotExist(xmpMeta, "http://purl.org/dc/elements/1.1/", PdfConst.Subject, v.trim(), 512);
                                        }
                                    }
                                    xmpMeta.setProperty("http://ns.adobe.com/pdf/1.3/", PdfConst.Keywords, value);
                                }
                            } else {
                                xmpMeta.setLocalizedText("http://purl.org/dc/elements/1.1/", "description", "x-default", "x-default", value);
                            }
                        } else {
                            for (String v2 : value.split(",|;")) {
                                if (v2.trim().length() > 0) {
                                    appendArrayItemIfDoesNotExist(xmpMeta, "http://purl.org/dc/elements/1.1/", PdfConst.Creator, v2.trim(), 1024);
                                }
                            }
                        }
                    } else {
                        xmpMeta.setLocalizedText("http://purl.org/dc/elements/1.1/", "title", "x-default", "x-default", value);
                    }
                }
            }
        }
    }

    private static void appendArrayItemIfDoesNotExist(XMPMeta meta, String ns, String arrayName, String value, int arrayOption) throws XMPException {
        int currentCnt = meta.countArrayItems(ns, arrayName);
        for (int i = 0; i < currentCnt; i++) {
            XMPProperty item = meta.getArrayItem(ns, arrayName, i + 1);
            if (value.equals(item.getValue())) {
                return;
            }
        }
        meta.appendArrayItem(ns, arrayName, new PropertyOptions(arrayOption), value, null);
    }

    private static String fetchArrayIntoString(XMPMeta meta, String ns, String arrayName) throws XMPException {
        int keywordsCnt = meta.countArrayItems(ns, arrayName);
        StringBuilder sb = null;
        for (int i = 0; i < keywordsCnt; i++) {
            XMPProperty curKeyword = meta.getArrayItem(ns, arrayName, i + 1);
            if (sb == null) {
                sb = new StringBuilder();
            } else if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append(curKeyword.getValue());
        }
        if (sb != null) {
            return sb.toString();
        }
        return null;
    }
}
