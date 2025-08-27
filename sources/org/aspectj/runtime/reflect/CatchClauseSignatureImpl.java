package org.aspectj.runtime.reflect;

import org.aspectj.lang.reflect.CatchClauseSignature;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/runtime/reflect/CatchClauseSignatureImpl.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/runtime/reflect/CatchClauseSignatureImpl.class */
class CatchClauseSignatureImpl extends SignatureImpl implements CatchClauseSignature {
    Class parameterType;
    String parameterName;

    CatchClauseSignatureImpl(Class declaringType, Class parameterType, String parameterName) {
        super(0, "catch", declaringType);
        this.parameterType = parameterType;
        this.parameterName = parameterName;
    }

    CatchClauseSignatureImpl(String stringRep) {
        super(stringRep);
    }

    @Override // org.aspectj.lang.reflect.CatchClauseSignature
    public Class getParameterType() {
        if (this.parameterType == null) {
            this.parameterType = extractType(3);
        }
        return this.parameterType;
    }

    @Override // org.aspectj.lang.reflect.CatchClauseSignature
    public String getParameterName() {
        if (this.parameterName == null) {
            this.parameterName = extractString(4);
        }
        return this.parameterName;
    }

    @Override // org.aspectj.runtime.reflect.SignatureImpl
    protected String createToString(StringMaker sm) {
        return new StringBuffer().append("catch(").append(sm.makeTypeName(getParameterType())).append(")").toString();
    }
}
