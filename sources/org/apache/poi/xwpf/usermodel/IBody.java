package org.apache.poi.xwpf.usermodel;

import java.util.List;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/IBody.class */
public interface IBody {
    POIXMLDocumentPart getPart();

    BodyType getPartType();

    List<IBodyElement> getBodyElements();

    List<XWPFParagraph> getParagraphs();

    List<XWPFTable> getTables();

    XWPFParagraph getParagraph(CTP ctp);

    XWPFTable getTable(CTTbl cTTbl);

    XWPFParagraph getParagraphArray(int i);

    XWPFTable getTableArray(int i);

    XWPFParagraph insertNewParagraph(XmlCursor xmlCursor);

    XWPFTable insertNewTbl(XmlCursor xmlCursor);

    void insertTable(int i, XWPFTable xWPFTable);

    XWPFTableCell getTableCell(CTTc cTTc);

    XWPFDocument getXWPFDocument();
}
