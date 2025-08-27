package org.apache.ibatis.javassist;

import org.apache.ibatis.javassist.CtField;

/* compiled from: CtClassType.java */
/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/FieldInitLink.class */
class FieldInitLink {
    FieldInitLink next = null;
    CtField field;
    CtField.Initializer init;

    FieldInitLink(CtField f, CtField.Initializer i) {
        this.field = f;
        this.init = i;
    }
}
