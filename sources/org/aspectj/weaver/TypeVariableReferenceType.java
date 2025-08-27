package org.aspectj.weaver;

import java.util.Map;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/TypeVariableReferenceType.class */
public class TypeVariableReferenceType extends ReferenceType implements TypeVariableReference {
    private TypeVariable typeVariable;

    public TypeVariableReferenceType(TypeVariable typeVariable, World world) {
        super(typeVariable.getGenericSignature(), typeVariable.getErasureSignature(), world);
        this.typeVariable = typeVariable;
    }

    @Override // org.aspectj.weaver.ResolvedType, org.aspectj.weaver.UnresolvedType
    public boolean equals(Object other) {
        return (other instanceof TypeVariableReferenceType) && this.typeVariable == ((TypeVariableReferenceType) other).typeVariable;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public int hashCode() {
        return this.typeVariable.hashCode();
    }

    @Override // org.aspectj.weaver.ReferenceType
    public ReferenceTypeDelegate getDelegate() {
        if (this.delegate == null) {
            ResolvedType resolvedFirstBound = this.typeVariable.getFirstBound().resolve(this.world);
            if (resolvedFirstBound.isMissing()) {
                BoundedReferenceTypeDelegate brtd = new BoundedReferenceTypeDelegate((ReferenceType) this.world.resolve(UnresolvedType.OBJECT));
                setDelegate(brtd);
                this.world.getLint().cantFindType.signal("Unable to find type for generic bound.  Missing type is " + resolvedFirstBound.getName(), getSourceLocation());
            } else {
                BoundedReferenceTypeDelegate brtd2 = new BoundedReferenceTypeDelegate((ReferenceType) resolvedFirstBound);
                setDelegate(brtd2);
            }
        }
        return this.delegate;
    }

    @Override // org.aspectj.weaver.ResolvedType, org.aspectj.weaver.UnresolvedType
    public UnresolvedType parameterize(Map<String, UnresolvedType> typeBindings) {
        UnresolvedType ut = typeBindings.get(getName());
        if (ut != null) {
            return this.world.resolve(ut);
        }
        return this;
    }

    @Override // org.aspectj.weaver.TypeVariableReference
    public TypeVariable getTypeVariable() {
        return this.typeVariable;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public boolean isTypeVariableReference() {
        return true;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public String toString() {
        return this.typeVariable.getName();
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public boolean isGenericWildcard() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public boolean isAnnotation() {
        ReferenceType upper = (ReferenceType) this.typeVariable.getUpperBound();
        if (upper.isAnnotation()) {
            return true;
        }
        World world = upper.getWorld();
        this.typeVariable.resolve(world);
        ResolvedType annotationType = ResolvedType.ANNOTATION.resolve(world);
        UnresolvedType[] ifBounds = this.typeVariable.getSuperInterfaces();
        for (int i = 0; i < ifBounds.length; i++) {
            if (((ReferenceType) ifBounds[i]).isAnnotation() || ifBounds[i].equals(annotationType)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public String getSignature() {
        StringBuffer sb = new StringBuffer();
        sb.append("T");
        sb.append(this.typeVariable.getName());
        sb.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        return sb.toString();
    }

    public String getTypeVariableName() {
        return this.typeVariable.getName();
    }

    public ReferenceType getUpperBound() {
        return (ReferenceType) this.typeVariable.resolve(this.world).getUpperBound();
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public ResolvedType resolve(World world) {
        this.typeVariable.resolve(world);
        return this;
    }

    public boolean isTypeVariableResolved() {
        return this.typeVariable.isResolved;
    }
}
