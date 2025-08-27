package org.aspectj.weaver.bcel;

import org.aspectj.weaver.ReferenceType;
import org.aspectj.weaver.ReferenceTypeDelegate;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/TypeDelegateResolver.class */
public interface TypeDelegateResolver {
    ReferenceTypeDelegate getDelegate(ReferenceType referenceType);
}
