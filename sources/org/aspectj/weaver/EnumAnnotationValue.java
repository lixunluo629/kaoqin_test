package org.aspectj.weaver;

import com.moredian.onpremise.core.common.constants.SymbolConstants;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/EnumAnnotationValue.class */
public class EnumAnnotationValue extends AnnotationValue {
    private String typeSignature;
    private String value;

    public EnumAnnotationValue(String typeSignature, String value) {
        super(101);
        this.typeSignature = typeSignature;
        this.value = value;
    }

    public String getType() {
        return this.typeSignature;
    }

    @Override // org.aspectj.weaver.AnnotationValue
    public String stringify() {
        return this.typeSignature + this.value;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return "E(" + this.typeSignature + SymbolConstants.SPACE_SYMBOL + this.value + ")";
    }
}
