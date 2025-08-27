package org.aspectj.runtime.reflect;

import org.aspectj.lang.reflect.MemberSignature;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/reflect/MemberSignatureImpl.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/reflect/MemberSignatureImpl.class */
abstract class MemberSignatureImpl extends SignatureImpl implements MemberSignature {
    MemberSignatureImpl(int modifiers, String name, Class declaringType) {
        super(modifiers, name, declaringType);
    }

    public MemberSignatureImpl(String stringRep) {
        super(stringRep);
    }
}
