package org.apache.poi.xslf.usermodel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList;
import org.openxmlformats.schemas.drawingml.x2006.main.TblStyleLstDocument;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFTableStyles.class */
public class XSLFTableStyles extends POIXMLDocumentPart implements Iterable<XSLFTableStyle> {
    private CTTableStyleList _tblStyleLst;
    private List<XSLFTableStyle> _styles;

    public XSLFTableStyles() {
    }

    public XSLFTableStyles(PackagePart part) throws XmlException, IOException {
        super(part);
        InputStream is = getPackagePart().getInputStream();
        TblStyleLstDocument styleDoc = TblStyleLstDocument.Factory.parse(is);
        is.close();
        this._tblStyleLst = styleDoc.getTblStyleLst();
        CTTableStyle[] tblStyleArray = this._tblStyleLst.getTblStyleArray();
        this._styles = new ArrayList(tblStyleArray.length);
        for (CTTableStyle c : tblStyleArray) {
            this._styles.add(new XSLFTableStyle(c));
        }
    }

    public CTTableStyleList getXmlObject() {
        return this._tblStyleLst;
    }

    @Override // java.lang.Iterable
    public Iterator<XSLFTableStyle> iterator() {
        return this._styles.iterator();
    }

    public List<XSLFTableStyle> getStyles() {
        return Collections.unmodifiableList(this._styles);
    }
}
