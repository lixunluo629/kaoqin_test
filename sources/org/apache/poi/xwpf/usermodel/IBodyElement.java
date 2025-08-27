package org.apache.poi.xwpf.usermodel;

import org.apache.poi.POIXMLDocumentPart;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xwpf/usermodel/IBodyElement.class */
public interface IBodyElement {
    IBody getBody();

    POIXMLDocumentPart getPart();

    BodyType getPartType();

    BodyElementType getElementType();
}
