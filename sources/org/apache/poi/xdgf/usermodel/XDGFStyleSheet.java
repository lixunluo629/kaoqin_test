package org.apache.poi.xdgf.usermodel;

import com.microsoft.schemas.office.visio.x2012.main.StyleSheetType;
import org.apache.poi.util.Internal;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/XDGFStyleSheet.class */
public class XDGFStyleSheet extends XDGFSheet {
    public XDGFStyleSheet(StyleSheetType styleSheet, XDGFDocument document) {
        super(styleSheet, document);
    }

    @Override // org.apache.poi.xdgf.usermodel.XDGFSheet
    @Internal
    public StyleSheetType getXmlObject() {
        return (StyleSheetType) this._sheet;
    }
}
