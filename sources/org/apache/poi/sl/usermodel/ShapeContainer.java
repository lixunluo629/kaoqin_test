package org.apache.poi.sl.usermodel;

import java.util.List;
import org.apache.poi.sl.usermodel.Shape;
import org.apache.poi.sl.usermodel.TextParagraph;

/* loaded from: poi-3.17.jar:org/apache/poi/sl/usermodel/ShapeContainer.class */
public interface ShapeContainer<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>> extends Iterable<S> {
    List<S> getShapes();

    void addShape(S s);

    boolean removeShape(S s);

    AutoShape<S, P> createAutoShape();

    FreeformShape<S, P> createFreeform();

    TextBox<S, P> createTextBox();

    ConnectorShape<S, P> createConnector();

    GroupShape<S, P> createGroup();

    PictureShape<S, P> createPicture(PictureData pictureData);

    TableShape<S, P> createTable(int i, int i2);
}
