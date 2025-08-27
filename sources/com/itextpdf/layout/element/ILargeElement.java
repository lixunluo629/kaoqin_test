package com.itextpdf.layout.element;

import com.itextpdf.layout.Document;

/* loaded from: layout-7.1.10.jar:com/itextpdf/layout/element/ILargeElement.class */
public interface ILargeElement extends IElement {
    boolean isComplete();

    void complete();

    void flush();

    void flushContent();

    void setDocument(Document document);
}
