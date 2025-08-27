package org.aspectj.weaver.tools;

import org.aspectj.weaver.WeaverMessages;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/UnsupportedPointcutPrimitiveException.class */
public class UnsupportedPointcutPrimitiveException extends RuntimeException {
    private static final long serialVersionUID = 3258689888517043251L;
    private PointcutPrimitive unsupportedPrimitive;
    private String pointcutExpression;

    public UnsupportedPointcutPrimitiveException(String pcExpression, PointcutPrimitive primitive) {
        super(WeaverMessages.format(WeaverMessages.UNSUPPORTED_POINTCUT_PRIMITIVE, pcExpression, primitive.getName()));
        this.pointcutExpression = pcExpression;
        this.unsupportedPrimitive = primitive;
    }

    public PointcutPrimitive getUnsupportedPrimitive() {
        return this.unsupportedPrimitive;
    }

    public String getInvalidPointcutExpression() {
        return this.pointcutExpression;
    }
}
