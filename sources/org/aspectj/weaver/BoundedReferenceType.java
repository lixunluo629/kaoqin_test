package org.aspectj.weaver;

import java.util.Map;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/BoundedReferenceType.class */
public class BoundedReferenceType extends ReferenceType {
    public static final int UNBOUND = 0;
    public static final int EXTENDS = 1;
    public static final int SUPER = 2;
    public int kind;
    private ResolvedType lowerBound;
    private ResolvedType upperBound;
    protected ReferenceType[] additionalInterfaceBounds;

    public BoundedReferenceType(ReferenceType aBound, boolean isExtends, World world) {
        super((isExtends ? "+" : "-") + aBound.signature, aBound.signatureErasure, world);
        this.additionalInterfaceBounds = ReferenceType.EMPTY_ARRAY;
        if (isExtends) {
            this.kind = 1;
        } else {
            this.kind = 2;
        }
        if (isExtends) {
            this.upperBound = aBound;
        } else {
            this.lowerBound = aBound;
            this.upperBound = world.resolve(UnresolvedType.OBJECT);
        }
        setDelegate(new BoundedReferenceTypeDelegate((ReferenceType) getUpperBound()));
    }

    public BoundedReferenceType(ReferenceType aBound, boolean isExtends, World world, ReferenceType[] additionalInterfaces) {
        this(aBound, isExtends, world);
        this.additionalInterfaceBounds = additionalInterfaces;
    }

    protected BoundedReferenceType(String signature, String erasedSignature, World world) {
        super(signature, erasedSignature, world);
        this.additionalInterfaceBounds = ReferenceType.EMPTY_ARRAY;
        if (signature.equals("*")) {
            this.kind = 0;
            this.upperBound = world.resolve(UnresolvedType.OBJECT);
        } else {
            this.upperBound = world.resolve(forSignature(erasedSignature));
        }
        setDelegate(new BoundedReferenceTypeDelegate((ReferenceType) this.upperBound));
    }

    public BoundedReferenceType(World world) {
        super("*", "Ljava/lang/Object;", world);
        this.additionalInterfaceBounds = ReferenceType.EMPTY_ARRAY;
        this.kind = 0;
        this.upperBound = world.resolve(UnresolvedType.OBJECT);
        setDelegate(new BoundedReferenceTypeDelegate((ReferenceType) this.upperBound));
    }

    public UnresolvedType getUpperBound() {
        return this.upperBound;
    }

    public UnresolvedType getLowerBound() {
        return this.lowerBound;
    }

    public ReferenceType[] getAdditionalBounds() {
        return this.additionalInterfaceBounds;
    }

    @Override // org.aspectj.weaver.ResolvedType, org.aspectj.weaver.UnresolvedType
    public UnresolvedType parameterize(Map<String, UnresolvedType> typeBindings) {
        if (this.kind == 0) {
            return this;
        }
        ReferenceType[] parameterizedAdditionalInterfaces = new ReferenceType[this.additionalInterfaceBounds == null ? 0 : this.additionalInterfaceBounds.length];
        for (int i = 0; i < parameterizedAdditionalInterfaces.length; i++) {
            parameterizedAdditionalInterfaces[i] = (ReferenceType) this.additionalInterfaceBounds[i].parameterize(typeBindings);
        }
        if (this.kind == 1) {
            return new BoundedReferenceType((ReferenceType) getUpperBound().parameterize(typeBindings), true, this.world, parameterizedAdditionalInterfaces);
        }
        return new BoundedReferenceType((ReferenceType) getLowerBound().parameterize(typeBindings), false, this.world, parameterizedAdditionalInterfaces);
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public String getSignatureForAttribute() {
        StringBuilder ret = new StringBuilder();
        if (this.kind == 2) {
            ret.append("-");
            ret.append(this.lowerBound.getSignatureForAttribute());
            for (int i = 0; i < this.additionalInterfaceBounds.length; i++) {
                ret.append(this.additionalInterfaceBounds[i].getSignatureForAttribute());
            }
        } else if (this.kind == 1) {
            ret.append("+");
            ret.append(this.upperBound.getSignatureForAttribute());
            for (int i2 = 0; i2 < this.additionalInterfaceBounds.length; i2++) {
                ret.append(this.additionalInterfaceBounds[i2].getSignatureForAttribute());
            }
        } else if (this.kind == 0) {
            ret.append("*");
        }
        return ret.toString();
    }

    public boolean hasLowerBound() {
        return this.lowerBound != null;
    }

    public boolean isExtends() {
        return this.kind == 1;
    }

    public boolean isSuper() {
        return this.kind == 2;
    }

    public boolean isUnbound() {
        return this.kind == 0;
    }

    public boolean alwaysMatches(ResolvedType aCandidateType) {
        if (isExtends()) {
            return ((ReferenceType) getUpperBound()).isAssignableFrom(aCandidateType);
        }
        if (isSuper()) {
            return aCandidateType.isAssignableFrom((ReferenceType) getLowerBound());
        }
        return true;
    }

    public boolean canBeCoercedTo(ResolvedType aCandidateType) {
        if (alwaysMatches(aCandidateType)) {
            return true;
        }
        if (aCandidateType.isGenericWildcard()) {
            BoundedReferenceType boundedRT = (BoundedReferenceType) aCandidateType;
            ResolvedType myUpperBound = (ResolvedType) getUpperBound();
            ResolvedType myLowerBound = (ResolvedType) getLowerBound();
            if (isExtends()) {
                if (boundedRT.isExtends()) {
                    return myUpperBound.isAssignableFrom((ResolvedType) boundedRT.getUpperBound());
                }
                return !boundedRT.isSuper() || myUpperBound == boundedRT.getLowerBound();
            }
            if (isSuper()) {
                if (boundedRT.isSuper()) {
                    return ((ResolvedType) boundedRT.getLowerBound()).isAssignableFrom(myLowerBound);
                }
                return !boundedRT.isExtends() || myLowerBound == boundedRT.getUpperBound();
            }
            return true;
        }
        return false;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public String getSimpleName() {
        if (!isExtends() && !isSuper()) {
            return "?";
        }
        if (isExtends()) {
            return "? extends " + getUpperBound().getSimpleName();
        }
        return "? super " + getLowerBound().getSimpleName();
    }

    @Override // org.aspectj.weaver.ReferenceType, org.aspectj.weaver.ResolvedType
    public ResolvedType[] getDeclaredInterfaces() {
        ResolvedType[] interfaces = super.getDeclaredInterfaces();
        if (this.additionalInterfaceBounds.length > 0) {
            ResolvedType[] allInterfaces = new ResolvedType[interfaces.length + this.additionalInterfaceBounds.length];
            System.arraycopy(interfaces, 0, allInterfaces, 0, interfaces.length);
            System.arraycopy(this.additionalInterfaceBounds, 0, allInterfaces, interfaces.length, this.additionalInterfaceBounds.length);
            return allInterfaces;
        }
        return interfaces;
    }

    @Override // org.aspectj.weaver.UnresolvedType
    public boolean isGenericWildcard() {
        return true;
    }
}
