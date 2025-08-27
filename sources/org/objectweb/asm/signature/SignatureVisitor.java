package org.objectweb.asm.signature;

/* JADX WARN: Classes with same name are omitted:
  asm-4.2.jar:org/objectweb/asm/signature/SignatureVisitor.class
 */
/* loaded from: lombok-1.16.22.jar:org/objectweb/asm/signature/SignatureVisitor.SCL.lombok */
public abstract class SignatureVisitor {
    public static final char EXTENDS = '+';
    public static final char SUPER = '-';
    public static final char INSTANCEOF = '=';
    protected final int api;

    public SignatureVisitor(int api) {
        if (api != 393216 && api != 327680 && api != 262144 && api != 17235968) {
            throw new IllegalArgumentException();
        }
        this.api = api;
    }

    public void visitFormalTypeParameter(String name) {
    }

    public SignatureVisitor visitClassBound() {
        return this;
    }

    public SignatureVisitor visitInterfaceBound() {
        return this;
    }

    public SignatureVisitor visitSuperclass() {
        return this;
    }

    public SignatureVisitor visitInterface() {
        return this;
    }

    public SignatureVisitor visitParameterType() {
        return this;
    }

    public SignatureVisitor visitReturnType() {
        return this;
    }

    public SignatureVisitor visitExceptionType() {
        return this;
    }

    public void visitBaseType(char descriptor) {
    }

    public void visitTypeVariable(String name) {
    }

    public SignatureVisitor visitArrayType() {
        return this;
    }

    public void visitClassType(String name) {
    }

    public void visitInnerClassType(String name) {
    }

    public void visitTypeArgument() {
    }

    public SignatureVisitor visitTypeArgument(char wildcard) {
        return this;
    }

    public void visitEnd() {
    }
}
