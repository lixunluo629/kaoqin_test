package org.objectweb.asm.commons;

import org.objectweb.asm.signature.SignatureVisitor;

@Deprecated
/* loaded from: lombok-1.16.22.jar:org/objectweb/asm/commons/RemappingSignatureAdapter.SCL.lombok */
public class RemappingSignatureAdapter extends SignatureVisitor {
    private final SignatureVisitor v;
    private final Remapper remapper;
    private String className;

    public RemappingSignatureAdapter(SignatureVisitor v, Remapper remapper) {
        this(393216, v, remapper);
    }

    protected RemappingSignatureAdapter(int api, SignatureVisitor v, Remapper remapper) {
        super(api);
        this.v = v;
        this.remapper = remapper;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitClassType(String name) {
        this.className = name;
        this.v.visitClassType(this.remapper.mapType(name));
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitInnerClassType(String name) {
        int iLastIndexOf;
        String remappedOuter = this.remapper.mapType(this.className) + '$';
        this.className += '$' + name;
        String remappedName = this.remapper.mapType(this.className);
        if (remappedName.startsWith(remappedOuter)) {
            iLastIndexOf = remappedOuter.length();
        } else {
            iLastIndexOf = remappedName.lastIndexOf(36) + 1;
        }
        int index = iLastIndexOf;
        this.v.visitInnerClassType(remappedName.substring(index));
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitFormalTypeParameter(String name) {
        this.v.visitFormalTypeParameter(name);
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitTypeVariable(String name) {
        this.v.visitTypeVariable(name);
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitArrayType() {
        this.v.visitArrayType();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitBaseType(char descriptor) {
        this.v.visitBaseType(descriptor);
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitClassBound() {
        this.v.visitClassBound();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitExceptionType() {
        this.v.visitExceptionType();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterface() {
        this.v.visitInterface();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterfaceBound() {
        this.v.visitInterfaceBound();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitParameterType() {
        this.v.visitParameterType();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitReturnType() {
        this.v.visitReturnType();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitSuperclass() {
        this.v.visitSuperclass();
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitTypeArgument() {
        this.v.visitTypeArgument();
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitTypeArgument(char wildcard) {
        this.v.visitTypeArgument(wildcard);
        return this;
    }

    @Override // org.objectweb.asm.signature.SignatureVisitor
    public void visitEnd() {
        this.v.visitEnd();
    }
}
