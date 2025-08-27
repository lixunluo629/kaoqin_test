package com.itextpdf.kernel.utils;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.pdf.tagging.IStructureNode;
import com.itextpdf.kernel.pdf.tagging.PdfMcr;
import com.itextpdf.kernel.pdf.tagging.PdfObjRef;
import com.itextpdf.kernel.pdf.tagging.PdfStructElem;
import com.itextpdf.kernel.pdf.tagging.PdfStructTreeRoot;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/TaggedPdfReaderTool.class */
public class TaggedPdfReaderTool {
    protected PdfDocument document;
    protected OutputStreamWriter out;
    protected String rootTag;
    protected Map<PdfDictionary, Map<Integer, String>> parsedTags = new HashMap();

    public TaggedPdfReaderTool(PdfDocument document) {
        this.document = document;
    }

    public static boolean isValidCharacterValue(int c) {
        return c == 9 || c == 10 || c == 13 || (c >= 32 && c <= 55295) || ((c >= 57344 && c <= 65533) || (c >= 65536 && c <= 1114111));
    }

    public void convertToXml(OutputStream os) throws IOException {
        convertToXml(os, "UTF-8");
    }

    public void convertToXml(OutputStream os, String charset) throws IOException {
        this.out = new OutputStreamWriter(os, Charset.forName(charset));
        if (this.rootTag != null) {
            this.out.write("<" + this.rootTag + ">" + System.lineSeparator());
        }
        PdfStructTreeRoot structTreeRoot = this.document.getStructTreeRoot();
        if (structTreeRoot == null) {
            throw new PdfException(PdfException.DocumentDoesntContainStructTreeRoot);
        }
        inspectKids(structTreeRoot.getKids());
        if (this.rootTag != null) {
            this.out.write("</" + this.rootTag + ">");
        }
        this.out.flush();
        this.out.close();
    }

    public TaggedPdfReaderTool setRootTag(String rootTagName) {
        this.rootTag = rootTagName;
        return this;
    }

    protected void inspectKids(List<IStructureNode> kids) {
        if (kids == null) {
            return;
        }
        for (IStructureNode kid : kids) {
            inspectKid(kid);
        }
    }

    protected void inspectKid(IStructureNode kid) {
        try {
            if (kid instanceof PdfStructElem) {
                PdfStructElem structElemKid = (PdfStructElem) kid;
                PdfName s = structElemKid.getRole();
                String tagN = s.getValue();
                String tag = fixTagName(tagN);
                this.out.write("<");
                this.out.write(tag);
                inspectAttributes(structElemKid);
                this.out.write(">" + System.lineSeparator());
                PdfString alt = structElemKid.getAlt();
                if (alt != null) {
                    this.out.write("<alt><![CDATA[");
                    this.out.write(alt.getValue().replaceAll("[\\000]*", ""));
                    this.out.write("]]></alt>" + System.lineSeparator());
                }
                inspectKids(structElemKid.getKids());
                this.out.write("</");
                this.out.write(tag);
                this.out.write(">" + System.lineSeparator());
            } else if (kid instanceof PdfMcr) {
                parseTag((PdfMcr) kid);
            } else {
                this.out.write(" <flushedKid/> ");
            }
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.UnknownIOException, (Throwable) e);
        }
    }

    protected void inspectAttributes(PdfStructElem kid) throws IOException {
        PdfDictionary attrDict;
        PdfObject attrObj = kid.getAttributes(false);
        if (attrObj != null) {
            if (attrObj instanceof PdfArray) {
                attrDict = ((PdfArray) attrObj).getAsDictionary(0);
            } else {
                attrDict = (PdfDictionary) attrObj;
            }
            try {
                for (PdfName key : attrDict.keySet()) {
                    this.out.write(32);
                    String attrName = key.getValue();
                    this.out.write(Character.toLowerCase(attrName.charAt(0)) + attrName.substring(1));
                    this.out.write("=\"");
                    this.out.write(attrDict.get(key, false).toString());
                    this.out.write(SymbolConstants.QUOTES_SYMBOL);
                }
            } catch (IOException e) {
                throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.UnknownIOException, (Throwable) e);
            }
        }
    }

    protected void parseTag(PdfMcr kid) {
        int mcid = kid.getMcid();
        PdfDictionary pageDic = kid.getPageObject();
        String tagContent = "";
        if (mcid != -1) {
            if (!this.parsedTags.containsKey(pageDic)) {
                MarkedContentEventListener listener = new MarkedContentEventListener();
                PdfCanvasProcessor processor = new PdfCanvasProcessor(listener);
                PdfPage page = this.document.getPage(pageDic);
                processor.processContent(page.getContentBytes(), page.getResources());
                this.parsedTags.put(pageDic, listener.getMcidContent());
            }
            if (this.parsedTags.get(pageDic).containsKey(Integer.valueOf(mcid))) {
                tagContent = this.parsedTags.get(pageDic).get(Integer.valueOf(mcid));
            }
        } else {
            PdfObjRef objRef = (PdfObjRef) kid;
            PdfObject object = objRef.getReferencedObject();
            if (object.isDictionary()) {
                PdfName subtype = ((PdfDictionary) object).getAsName(PdfName.Subtype);
                tagContent = subtype.toString();
            }
        }
        try {
            this.out.write(escapeXML(tagContent, true));
        } catch (IOException e) {
            throw new com.itextpdf.io.IOException(com.itextpdf.io.IOException.UnknownIOException, (Throwable) e);
        }
    }

    protected static String fixTagName(String tag) {
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < tag.length(); k++) {
            char c = tag.charAt(k);
            boolean nameStart = c == ':' || (c >= 'A' && c <= 'Z') || c == '_' || ((c >= 'a' && c <= 'z') || ((c >= 192 && c <= 214) || ((c >= 216 && c <= 246) || ((c >= 248 && c <= 767) || ((c >= 880 && c <= 893) || ((c >= 895 && c <= 8191) || ((c >= 8204 && c <= 8205) || ((c >= 8304 && c <= 8591) || ((c >= 11264 && c <= 12271) || ((c >= 12289 && c <= 55295) || ((c >= 63744 && c <= 64975) || (c >= 65008 && c <= 65533))))))))))));
            boolean nameMiddle = c == '-' || c == '.' || (c >= '0' && c <= '9') || c == 183 || ((c >= 768 && c <= 879) || ((c >= 8255 && c <= 8256) || nameStart));
            if (k == 0) {
                if (!nameStart) {
                    c = '_';
                }
            } else if (!nameMiddle) {
                c = '-';
            }
            sb.append(c);
        }
        return sb.toString();
    }

    protected static String escapeXML(String s, boolean onlyASCII) {
        char[] cc = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : cc) {
            switch (c) {
                case '\"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                default:
                    if (isValidCharacterValue(c)) {
                        if (onlyASCII && c > 127) {
                            sb.append("&#").append((int) c).append(';');
                            break;
                        } else {
                            sb.append(c);
                            break;
                        }
                    } else {
                        break;
                    }
                    break;
            }
        }
        return sb.toString();
    }

    /* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/utils/TaggedPdfReaderTool$MarkedContentEventListener.class */
    private class MarkedContentEventListener implements IEventListener {
        private Map<Integer, ITextExtractionStrategy> contentByMcid;

        private MarkedContentEventListener() {
            this.contentByMcid = new HashMap();
        }

        public Map<Integer, String> getMcidContent() {
            Map<Integer, String> content = new HashMap<>();
            Iterator<Integer> it = this.contentByMcid.keySet().iterator();
            while (it.hasNext()) {
                int id = it.next().intValue();
                content.put(Integer.valueOf(id), this.contentByMcid.get(Integer.valueOf(id)).getResultantText());
            }
            return content;
        }

        @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener
        public void eventOccurred(IEventData data, EventType type) {
            switch (type) {
                case RENDER_TEXT:
                    TextRenderInfo textInfo = (TextRenderInfo) data;
                    int mcid = textInfo.getMcid();
                    if (mcid != -1) {
                        ITextExtractionStrategy textExtractionStrategy = this.contentByMcid.get(Integer.valueOf(mcid));
                        if (textExtractionStrategy == null) {
                            textExtractionStrategy = new LocationTextExtractionStrategy();
                            this.contentByMcid.put(Integer.valueOf(mcid), textExtractionStrategy);
                        }
                        textExtractionStrategy.eventOccurred(data, type);
                        break;
                    }
                    break;
            }
        }

        @Override // com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener
        public Set<EventType> getSupportedEvents() {
            return null;
        }
    }
}
