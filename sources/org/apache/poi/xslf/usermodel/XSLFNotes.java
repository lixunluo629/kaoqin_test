package org.apache.poi.xslf.usermodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.POIXMLTypeLoader;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.sl.usermodel.Notes;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData;
import org.openxmlformats.schemas.presentationml.x2006.main.CTNotesSlide;
import org.openxmlformats.schemas.presentationml.x2006.main.NotesDocument;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFNotes.class */
public final class XSLFNotes extends XSLFSheet implements Notes<XSLFShape, XSLFTextParagraph> {
    private CTNotesSlide _notes;

    XSLFNotes() {
        this._notes = prototype();
        setCommonSlideData(this._notes.getCSld());
    }

    XSLFNotes(PackagePart part) throws XmlException, IOException {
        super(part);
        NotesDocument doc = NotesDocument.Factory.parse(getPackagePart().getInputStream(), POIXMLTypeLoader.DEFAULT_XML_OPTIONS);
        this._notes = doc.getNotes();
        setCommonSlideData(this._notes.getCSld());
    }

    private static CTNotesSlide prototype() {
        CTNotesSlide ctNotes = CTNotesSlide.Factory.newInstance();
        CTCommonSlideData cSld = ctNotes.addNewCSld();
        cSld.addNewSpTree();
        return ctNotes;
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSheet
    public CTNotesSlide getXmlObject() {
        return this._notes;
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSheet
    protected String getRootElementName() {
        return "notes";
    }

    @Override // org.apache.poi.xslf.usermodel.XSLFSheet
    public XSLFTheme getTheme() {
        return getMasterSheet().getTheme();
    }

    @Override // org.apache.poi.sl.usermodel.Sheet
    public XSLFNotesMaster getMasterSheet() {
        for (POIXMLDocumentPart p : getRelations()) {
            if (p instanceof XSLFNotesMaster) {
                return (XSLFNotesMaster) p;
            }
        }
        return null;
    }

    @Override // org.apache.poi.sl.usermodel.Notes
    public List<List<XSLFTextParagraph>> getTextParagraphs() {
        List<List<XSLFTextParagraph>> tp = new ArrayList<>();
        for (XSLFShape sh : super.getShapes()) {
            if (sh instanceof XSLFTextShape) {
                XSLFTextShape txt = (XSLFTextShape) sh;
                tp.add(txt.getTextParagraphs());
            }
        }
        return tp;
    }
}
