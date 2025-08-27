package org.apache.poi.xdgf.usermodel.shape;

import org.apache.poi.xdgf.usermodel.XDGFShape;

/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xdgf/usermodel/shape/ShapeVisitorAcceptor.class */
public interface ShapeVisitorAcceptor {
    boolean accept(XDGFShape xDGFShape);
}
