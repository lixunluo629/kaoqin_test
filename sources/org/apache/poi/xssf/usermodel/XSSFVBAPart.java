package org.apache.poi.xssf.usermodel;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.openxml4j.opc.PackagePart;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xssf/usermodel/XSSFVBAPart.class */
public class XSSFVBAPart extends POIXMLDocumentPart {
    protected XSSFVBAPart() {
    }

    protected XSSFVBAPart(PackagePart part) {
        super(part);
    }

    @Override // org.apache.poi.POIXMLDocumentPart
    protected void prepareForCommit() {
    }
}
