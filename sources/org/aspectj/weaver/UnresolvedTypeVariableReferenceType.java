package org.aspectj.weaver;

import org.aspectj.weaver.UnresolvedType;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/UnresolvedTypeVariableReferenceType.class */
public class UnresolvedTypeVariableReferenceType extends UnresolvedType implements TypeVariableReference {
    private TypeVariable typeVariable;

    public UnresolvedTypeVariableReferenceType() {
        super("Ljava/lang/Object;");
    }

    public UnresolvedTypeVariableReferenceType(TypeVariable aTypeVariable) {
        super("T" + aTypeVariable.getName() + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, aTypeVariable.getFirstBound().getErasureSignature());
        this.typeVariable = aTypeVariable;
    }

    public void setTypeVariable(TypeVariable aTypeVariable) {
        this.signature = "T" + aTypeVariable.getName() + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR;
        this.signatureErasure = aTypeVariable.getFirstBound().getErasureSignature();
        this.typeVariable = aTypeVariable;
        this.typeKind = UnresolvedType.TypeKind.TYPE_VARIABLE;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public ResolvedType resolve(World world) {
        TypeVariableReferenceType tvrt;
        TypeVariableDeclaringElement typeVariableScope = world.getTypeVariableLookupScope();
        if (typeVariableScope == null) {
            tvrt = new TypeVariableReferenceType(this.typeVariable.resolve(world), world);
        } else {
            TypeVariable resolvedTypeVariable = typeVariableScope.getTypeVariableNamed(this.typeVariable.getName());
            if (resolvedTypeVariable == null) {
                resolvedTypeVariable = this.typeVariable.resolve(world);
            }
            tvrt = new TypeVariableReferenceType(resolvedTypeVariable, world);
        }
        return tvrt;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public boolean isTypeVariableReference() {
        return true;
    }

    @Override // org.aspectj.weaver.TypeVariableReference
    public TypeVariable getTypeVariable() {
        return this.typeVariable;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public String toString() {
        if (this.typeVariable == null) {
            return "<type variable not set!>";
        }
        return "T" + this.typeVariable.getName() + ScriptUtils.DEFAULT_STATEMENT_SEPARATOR;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public String toDebugString() {
        return this.typeVariable.getName();
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public String getErasureSignature() {
        return this.typeVariable.getFirstBound().getSignature();
    }
}
