package org.apache.poi.xslf.model;

import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/model/TextBodyPropertyFetcher.class */
public abstract class TextBodyPropertyFetcher<T> extends PropertyFetcher<T> {
    public abstract boolean fetch(CTTextBodyProperties cTTextBodyProperties);

    @Override // org.apache.poi.xslf.model.PropertyFetcher
    public boolean fetch(XSLFShape shape) {
        XmlObject[] o = shape.getXmlObject().selectPath("declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main' declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main' .//p:txBody/a:bodyPr");
        if (o.length == 1) {
            CTTextBodyProperties props = (CTTextBodyProperties) o[0];
            return fetch(props);
        }
        return false;
    }
}
