package org.aspectj.weaver;

import org.aspectj.weaver.UnresolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ArrayReferenceType.class */
public class ArrayReferenceType extends ReferenceType {
    private final ResolvedType componentType;

    public ArrayReferenceType(String sig, String erasureSig, World world, ResolvedType componentType) {
        super(sig, erasureSig, world);
        this.componentType = componentType;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public final ResolvedMember[] getDeclaredFields() {
        return ResolvedMember.NONE;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public final ResolvedMember[] getDeclaredMethods() {
        return ResolvedMember.NONE;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public final ResolvedType[] getDeclaredInterfaces() {
        return new ResolvedType[]{this.world.getCoreType(CLONEABLE), this.world.getCoreType(SERIALIZABLE)};
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType, org.aspectj.weaver.AnnotatedElement
    public AnnotationAJ getAnnotationOfType(UnresolvedType ofType) {
        return null;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public AnnotationAJ[] getAnnotations() {
        return AnnotationAJ.EMPTY_ARRAY;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType, org.aspectj.weaver.AnnotatedElement
    public ResolvedType[] getAnnotationTypes() {
        return ResolvedType.NONE;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public final ResolvedMember[] getDeclaredPointcuts() {
        return ResolvedMember.NONE;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.AnnotatedElement
    public boolean hasAnnotation(UnresolvedType ofType) {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public final ResolvedType getSuperclass() {
        return this.world.getCoreType(OBJECT);
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public final boolean isAssignableFrom(ResolvedType o) {
        if (!o.isArray()) {
            return false;
        }
        if (o.getComponentType().isPrimitiveType()) {
            return o.equals(this);
        }
        return getComponentType().resolve(this.world).isAssignableFrom(o.getComponentType().resolve(this.world));
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public boolean isAssignableFrom(ResolvedType o, boolean allowMissing) {
        return isAssignableFrom(o);
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public final boolean isCoerceableFrom(ResolvedType o) {
        if (o.equals(UnresolvedType.OBJECT) || o.equals(UnresolvedType.SERIALIZABLE) || o.equals(UnresolvedType.CLONEABLE)) {
            return true;
        }
        if (!o.isArray()) {
            return false;
        }
        if (o.getComponentType().isPrimitiveType()) {
            return o.equals(this);
        }
        return getComponentType().resolve(this.world).isCoerceableFrom(o.getComponentType().resolve(this.world));
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public final int getModifiers() {
        return (this.componentType.getModifiers() & 7) | 16;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public UnresolvedType getComponentType() {
        return this.componentType;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public ResolvedType getResolvedComponentType() {
        return this.componentType;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public ISourceContext getSourceContext() {
        return getResolvedComponentType().getSourceContext();
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.UnresolvedType
    public TypeVariable[] getTypeVariables() {
        if (this.typeVariables == null && this.componentType.getTypeVariables() != null) {
            this.typeVariables = this.componentType.getTypeVariables();
            for (int i = 0; i < this.typeVariables.length; i++) {
                this.typeVariables[i].resolve(this.world);
            }
        }
        return this.typeVariables;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public boolean isAnnotation() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public boolean isAnonymous() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public boolean isAnnotationStyleAspect() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public boolean isAspect() {
        return false;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public boolean isPrimitiveType() {
        return this.typeKind == UnresolvedType.TypeKind.PRIMITIVE;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public boolean isSimpleType() {
        return this.typeKind == UnresolvedType.TypeKind.SIMPLE;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public boolean isRawType() {
        return this.typeKind == UnresolvedType.TypeKind.RAW;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.UnresolvedType
    public boolean isGenericType() {
        return this.typeKind == UnresolvedType.TypeKind.GENERIC;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public boolean isParameterizedType() {
        return this.typeKind == UnresolvedType.TypeKind.PARAMETERIZED;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public boolean isTypeVariableReference() {
        return this.typeKind == UnresolvedType.TypeKind.TYPE_VARIABLE;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public boolean isGenericWildcard() {
        return this.typeKind == UnresolvedType.TypeKind.WILDCARD;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public boolean isEnum() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public boolean isNested() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public boolean isClass() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public boolean isExposedToWeaver() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public boolean canAnnotationTargetType() {
        return false;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public AnnotationTargetKind[] getAnnotationTargetKinds() {
        return null;
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public boolean isAnnotationWithRuntimeRetention() {
        return false;
    }

    @Override // org.aspectj.weaver.ResolvedType
    public boolean isPrimitiveArray() {
        if (this.componentType.isPrimitiveType()) {
            return true;
        }
        if (this.componentType.isArray()) {
            return this.componentType.isPrimitiveArray();
        }
        return false;
    }
}
