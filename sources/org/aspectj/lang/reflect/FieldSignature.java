package org.aspectj.lang.reflect;

import java.lang.reflect.Field;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/reflect/FieldSignature.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/reflect/FieldSignature.class */
public interface FieldSignature extends MemberSignature {
    Class getFieldType();

    Field getField();
}
