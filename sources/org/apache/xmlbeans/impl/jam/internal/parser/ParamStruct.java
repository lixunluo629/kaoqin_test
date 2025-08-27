package org.apache.xmlbeans.impl.jam.internal.parser;

import org.apache.xmlbeans.impl.jam.mutable.MInvokable;
import org.apache.xmlbeans.impl.jam.mutable.MParameter;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/parser/ParamStruct.class */
class ParamStruct {
    private String mName;
    private String mType;

    public ParamStruct(String type, String name) {
        init(type, name);
    }

    public void init(String type, String name) {
        this.mType = type;
        this.mName = name;
    }

    public MParameter createParameter(MInvokable e) {
        if (e == null) {
            throw new IllegalArgumentException("null invokable");
        }
        MParameter param = e.addNewParameter();
        param.setSimpleName(this.mName);
        param.setUnqualifiedType(this.mType);
        return param;
    }
}
