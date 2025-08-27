package org.apache.poi.xwpf.usermodel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLException;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFootnotes;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FootnotesDocument;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/XWPFFootnotes.class */
public class XWPFFootnotes extends POIXMLDocumentPart {
    protected XWPFDocument document;
    private List<XWPFFootnote> listFootnote;
    private CTFootnotes ctFootnotes;

    public XWPFFootnotes(PackagePart part) throws OpenXML4JException, IOException {
        super(part);
        this.listFootnote = new ArrayList();
    }

    public XWPFFootnotes() {
        this.listFootnote = new ArrayList();
    }

    @Override // org.apache.poi.POIXMLDocumentPart
    protected void onDocumentRead() throws IOException {
        InputStream is = null;
        try {
            try {
                is = getPackagePart().getInputStream();
                FootnotesDocument notesDoc = FootnotesDocument.Factory.parse(is, POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
                this.ctFootnotes = notesDoc.getFootnotes();
                if (is != null) {
                    is.close();
                }
                CTFtnEdn[] arr$ = this.ctFootnotes.getFootnoteArray();
                for (CTFtnEdn note : arr$) {
                    this.listFootnote.add(new XWPFFootnote(note, this));
                }
            } catch (XmlException e) {
                throw new POIXMLException();
            }
        } catch (Throwable th) {
            if (is != null) {
                is.close();
            }
            throw th;
        }
    }

    @Override // org.apache.poi.POIXMLDocumentPart
    protected void commit() throws IOException {
        XmlOptions xmlOptions = new XmlOptions(POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
        xmlOptions.setSaveSyntheticDocumentElement(new QName(CTFootnotes.type.getName().getNamespaceURI(), "footnotes"));
        PackagePart part = getPackagePart();
        OutputStream out = part.getOutputStream();
        this.ctFootnotes.save(out, xmlOptions);
        out.close();
    }

    public List<XWPFFootnote> getFootnotesList() {
        return this.listFootnote;
    }

    public XWPFFootnote getFootnoteById(int id) {
        for (XWPFFootnote note : this.listFootnote) {
            if (note.getCTFtnEdn().getId().intValue() == id) {
                return note;
            }
        }
        return null;
    }

    public void setFootnotes(CTFootnotes footnotes) {
        this.ctFootnotes = footnotes;
    }

    public void addFootnote(XWPFFootnote footnote) {
        this.listFootnote.add(footnote);
        this.ctFootnotes.addNewFootnote().set(footnote.getCTFtnEdn());
    }

    public XWPFFootnote addFootnote(CTFtnEdn note) {
        CTFtnEdn newNote = this.ctFootnotes.addNewFootnote();
        newNote.set(note);
        XWPFFootnote xNote = new XWPFFootnote(newNote, this);
        this.listFootnote.add(xNote);
        return xNote;
    }

    public XWPFDocument getXWPFDocument() {
        if (this.document != null) {
            return this.document;
        }
        return (XWPFDocument) getParent();
    }

    public void setXWPFDocument(XWPFDocument doc) {
        this.document = doc;
    }
}
