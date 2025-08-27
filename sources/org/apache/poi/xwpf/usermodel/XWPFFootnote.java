package org.apache.poi.xwpf.usermodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/XWPFFootnote.class */
public class XWPFFootnote implements Iterable<XWPFParagraph>, IBody {
    private List<XWPFParagraph> paragraphs = new ArrayList();
    private List<XWPFTable> tables = new ArrayList();
    private List<XWPFPictureData> pictures = new ArrayList();
    private List<IBodyElement> bodyElements = new ArrayList();
    private CTFtnEdn ctFtnEdn;
    private XWPFFootnotes footnotes;
    private XWPFDocument document;

    public XWPFFootnote(CTFtnEdn note, XWPFFootnotes xFootnotes) {
        this.footnotes = xFootnotes;
        this.ctFtnEdn = note;
        this.document = xFootnotes.getXWPFDocument();
        init();
    }

    public XWPFFootnote(XWPFDocument document, CTFtnEdn body) {
        this.ctFtnEdn = body;
        this.document = document;
        init();
    }

    private void init() {
        XmlCursor cursor = this.ctFtnEdn.newCursor();
        cursor.selectPath("./*");
        while (cursor.toNextSelection()) {
            XmlObject o = cursor.getObject();
            if (o instanceof CTP) {
                XWPFParagraph p = new XWPFParagraph((CTP) o, this);
                this.bodyElements.add(p);
                this.paragraphs.add(p);
            } else if (o instanceof CTTbl) {
                XWPFTable t = new XWPFTable((CTTbl) o, this);
                this.bodyElements.add(t);
                this.tables.add(t);
            } else if (o instanceof CTSdtBlock) {
                XWPFSDT c = new XWPFSDT((CTSdtBlock) o, this);
                this.bodyElements.add(c);
            }
        }
        cursor.dispose();
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public List<XWPFParagraph> getParagraphs() {
        return this.paragraphs;
    }

    @Override // java.lang.Iterable
    public Iterator<XWPFParagraph> iterator() {
        return this.paragraphs.iterator();
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public List<XWPFTable> getTables() {
        return this.tables;
    }

    public List<XWPFPictureData> getPictures() {
        return this.pictures;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public List<IBodyElement> getBodyElements() {
        return this.bodyElements;
    }

    public CTFtnEdn getCTFtnEdn() {
        return this.ctFtnEdn;
    }

    public void setCTFtnEdn(CTFtnEdn footnote) {
        this.ctFtnEdn = footnote;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public XWPFTable getTableArray(int pos) {
        if (pos >= 0 && pos < this.tables.size()) {
            return this.tables.get(pos);
        }
        return null;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public void insertTable(int pos, XWPFTable table) {
        this.bodyElements.add(pos, table);
        int i = 0;
        CTTbl[] arr$ = this.ctFtnEdn.getTblArray();
        for (CTTbl tbl : arr$) {
            if (tbl == table.getCTTbl()) {
                break;
            }
            i++;
        }
        this.tables.add(i, table);
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public XWPFTable getTable(CTTbl ctTable) {
        XWPFTable table;
        Iterator i$ = this.tables.iterator();
        while (i$.hasNext() && (table = i$.next()) != null) {
            if (table.getCTTbl().equals(ctTable)) {
                return table;
            }
        }
        return null;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public XWPFParagraph getParagraph(CTP p) {
        for (XWPFParagraph paragraph : this.paragraphs) {
            if (paragraph.getCTP().equals(p)) {
                return paragraph;
            }
        }
        return null;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public XWPFParagraph getParagraphArray(int pos) {
        if (pos >= 0 && pos < this.paragraphs.size()) {
            return this.paragraphs.get(pos);
        }
        return null;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public XWPFTableCell getTableCell(CTTc cell) {
        XWPFTableRow tableRow;
        XmlCursor cursor = cell.newCursor();
        cursor.toParent();
        XmlObject o = cursor.getObject();
        if (!(o instanceof CTRow)) {
            return null;
        }
        CTRow row = (CTRow) o;
        cursor.toParent();
        XmlObject o2 = cursor.getObject();
        cursor.dispose();
        if (!(o2 instanceof CTTbl)) {
            return null;
        }
        CTTbl tbl = (CTTbl) o2;
        XWPFTable table = getTable(tbl);
        if (table == null || (tableRow = table.getRow(row)) == null) {
            return null;
        }
        return tableRow.getTableCell(cell);
    }

    private boolean isCursorInFtn(XmlCursor cursor) {
        XmlCursor verify = cursor.newCursor();
        verify.toParent();
        if (verify.getObject() == this.ctFtnEdn) {
            return true;
        }
        return false;
    }

    public POIXMLDocumentPart getOwner() {
        return this.footnotes;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public XWPFTable insertNewTbl(XmlCursor cursor) {
        XmlObject o;
        if (isCursorInFtn(cursor)) {
            String uri = CTTbl.type.getName().getNamespaceURI();
            cursor.beginElement("tbl", uri);
            cursor.toParent();
            CTTbl t = (CTTbl) cursor.getObject();
            XWPFTable newT = new XWPFTable(t, this);
            cursor.removeXmlContents();
            XmlObject object = null;
            while (true) {
                o = object;
                if ((o instanceof CTTbl) || !cursor.toPrevSibling()) {
                    break;
                }
                object = cursor.getObject();
            }
            if (!(o instanceof CTTbl)) {
                this.tables.add(0, newT);
            } else {
                int pos = this.tables.indexOf(getTable((CTTbl) o)) + 1;
                this.tables.add(pos, newT);
            }
            int i = 0;
            XmlCursor cursor2 = t.newCursor();
            while (cursor2.toPrevSibling()) {
                XmlObject o2 = cursor2.getObject();
                if ((o2 instanceof CTP) || (o2 instanceof CTTbl)) {
                    i++;
                }
            }
            this.bodyElements.add(i, newT);
            XmlCursor c2 = t.newCursor();
            cursor2.toCursor(c2);
            cursor2.toEndToken();
            c2.dispose();
            return newT;
        }
        return null;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public XWPFParagraph insertNewParagraph(XmlCursor cursor) {
        XmlObject o;
        if (isCursorInFtn(cursor)) {
            String uri = CTP.type.getName().getNamespaceURI();
            cursor.beginElement("p", uri);
            cursor.toParent();
            CTP p = (CTP) cursor.getObject();
            XWPFParagraph newP = new XWPFParagraph(p, this);
            XmlObject object = null;
            while (true) {
                o = object;
                if ((o instanceof CTP) || !cursor.toPrevSibling()) {
                    break;
                }
                object = cursor.getObject();
            }
            if (!(o instanceof CTP) || ((CTP) o) == p) {
                this.paragraphs.add(0, newP);
            } else {
                int pos = this.paragraphs.indexOf(getParagraph((CTP) o)) + 1;
                this.paragraphs.add(pos, newP);
            }
            int i = 0;
            XmlCursor p2 = p.newCursor();
            cursor.toCursor(p2);
            p2.dispose();
            while (cursor.toPrevSibling()) {
                XmlObject o2 = cursor.getObject();
                if ((o2 instanceof CTP) || (o2 instanceof CTTbl)) {
                    i++;
                }
            }
            this.bodyElements.add(i, newP);
            XmlCursor p22 = p.newCursor();
            cursor.toCursor(p22);
            cursor.toEndToken();
            p22.dispose();
            return newP;
        }
        return null;
    }

    public XWPFTable addNewTbl(CTTbl table) {
        CTTbl newTable = this.ctFtnEdn.addNewTbl();
        newTable.set(table);
        XWPFTable xTable = new XWPFTable(newTable, this);
        this.tables.add(xTable);
        return xTable;
    }

    public XWPFParagraph addNewParagraph(CTP paragraph) {
        CTP newPara = this.ctFtnEdn.addNewP();
        newPara.set(paragraph);
        XWPFParagraph xPara = new XWPFParagraph(newPara, this);
        this.paragraphs.add(xPara);
        return xPara;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public XWPFDocument getXWPFDocument() {
        return this.document;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public POIXMLDocumentPart getPart() {
        return this.footnotes;
    }

    @Override // org.apache.poi.xwpf.usermodel.IBody
    public BodyType getPartType() {
        return BodyType.FOOTNOTE;
    }
}
