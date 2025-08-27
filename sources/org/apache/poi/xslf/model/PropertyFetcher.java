package org.apache.poi.xslf.model;

import org.apache.poi.util.Internal;
import org.apache.poi.xslf.usermodel.XSLFShape;

@Internal
/* loaded from: poi-ooxml-3.17.jar:org/apache/poi/xslf/model/PropertyFetcher.class */
public abstract class PropertyFetcher<T> {
    private T _value;

    public abstract boolean fetch(XSLFShape xSLFShape);

    public T getValue() {
        return this._value;
    }

    public void setValue(T val) {
        this._value = val;
    }
}
