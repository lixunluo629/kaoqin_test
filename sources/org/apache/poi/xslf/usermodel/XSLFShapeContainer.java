package org.apache.poi.xslf.usermodel;

import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.ShapeContainer;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/usermodel/XSLFShapeContainer.class */
public interface XSLFShapeContainer extends ShapeContainer<XSLFShape, XSLFTextParagraph> {
    @Override // org.apache.poi.sl.usermodel.ShapeContainer
    XSLFAutoShape createAutoShape();

    @Override // org.apache.poi.sl.usermodel.ShapeContainer
    XSLFFreeformShape createFreeform();

    @Override // org.apache.poi.sl.usermodel.ShapeContainer
    XSLFTextBox createTextBox();

    @Override // org.apache.poi.sl.usermodel.ShapeContainer
    XSLFConnectorShape createConnector();

    @Override // org.apache.poi.sl.usermodel.ShapeContainer
    XSLFGroupShape createGroup();

    @Override // org.apache.poi.sl.usermodel.ShapeContainer
    XSLFPictureShape createPicture(PictureData pictureData);

    void clear();
}
