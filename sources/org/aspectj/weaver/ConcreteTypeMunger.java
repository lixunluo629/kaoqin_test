package org.aspectj.weaver;

import java.util.Map;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.util.PartialOrder;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ConcreteTypeMunger.class */
public abstract class ConcreteTypeMunger implements PartialOrder.PartialComparable {
    protected ResolvedTypeMunger munger;
    protected ResolvedType aspectType;

    public abstract ConcreteTypeMunger parameterizedFor(ResolvedType resolvedType);

    public abstract ConcreteTypeMunger parameterizeWith(Map<String, UnresolvedType> map, World world);

    public ConcreteTypeMunger(ResolvedTypeMunger munger, ResolvedType aspectType) {
        this.munger = munger;
        this.aspectType = aspectType;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0053, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0072, code lost:
    
        if (r0.getAspectType() != null) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0079, code lost:
    
        if (getAspectType() != null) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x008a, code lost:
    
        if (r0.getAspectType().equals(getAspectType()) == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x008d, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equivalentTo(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r4
            boolean r0 = r0 instanceof org.aspectj.weaver.ConcreteTypeMunger
            if (r0 != 0) goto L9
            r0 = 0
            return r0
        L9:
            r0 = r4
            org.aspectj.weaver.ConcreteTypeMunger r0 = (org.aspectj.weaver.ConcreteTypeMunger) r0
            r5 = r0
            r0 = r5
            org.aspectj.weaver.ResolvedTypeMunger r0 = r0.getMunger()
            r6 = r0
            r0 = r3
            org.aspectj.weaver.ResolvedTypeMunger r0 = r0.getMunger()
            r7 = r0
            r0 = r7
            boolean r0 = r0 instanceof org.aspectj.weaver.NewConstructorTypeMunger
            if (r0 == 0) goto L59
            r0 = r6
            boolean r0 = r0 instanceof org.aspectj.weaver.NewConstructorTypeMunger
            if (r0 == 0) goto L59
            r0 = r6
            org.aspectj.weaver.NewConstructorTypeMunger r0 = (org.aspectj.weaver.NewConstructorTypeMunger) r0
            r1 = r7
            boolean r0 = r0.equivalentTo(r1)
            if (r0 == 0) goto L57
            r0 = r5
            org.aspectj.weaver.ResolvedType r0 = r0.getAspectType()
            if (r0 != 0) goto L45
            r0 = r3
            org.aspectj.weaver.ResolvedType r0 = r0.getAspectType()
            if (r0 != 0) goto L57
            goto L53
        L45:
            r0 = r5
            org.aspectj.weaver.ResolvedType r0 = r0.getAspectType()
            r1 = r3
            org.aspectj.weaver.ResolvedType r1 = r1.getAspectType()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L57
        L53:
            r0 = 1
            goto L58
        L57:
            r0 = 0
        L58:
            return r0
        L59:
            r0 = r6
            if (r0 != 0) goto L65
            r0 = r7
            if (r0 != 0) goto L91
            goto L6e
        L65:
            r0 = r6
            r1 = r7
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L91
        L6e:
            r0 = r5
            org.aspectj.weaver.ResolvedType r0 = r0.getAspectType()
            if (r0 != 0) goto L7f
            r0 = r3
            org.aspectj.weaver.ResolvedType r0 = r0.getAspectType()
            if (r0 != 0) goto L91
            goto L8d
        L7f:
            r0 = r5
            org.aspectj.weaver.ResolvedType r0 = r0.getAspectType()
            r1 = r3
            org.aspectj.weaver.ResolvedType r1 = r1.getAspectType()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L91
        L8d:
            r0 = 1
            goto L92
        L91:
            r0 = 0
        L92:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.aspectj.weaver.ConcreteTypeMunger.equivalentTo(java.lang.Object):boolean");
    }

    public ResolvedTypeMunger getMunger() {
        return this.munger;
    }

    public ResolvedType getAspectType() {
        return this.aspectType;
    }

    public ResolvedMember getSignature() {
        return this.munger.getSignature();
    }

    public World getWorld() {
        return this.aspectType.getWorld();
    }

    public ISourceLocation getSourceLocation() {
        if (this.munger == null) {
            return null;
        }
        return this.munger.getSourceLocation();
    }

    public boolean matches(ResolvedType onType) {
        if (this.munger == null) {
            throw new RuntimeException("huh: " + this);
        }
        return this.munger.matches(onType, this.aspectType);
    }

    public ResolvedMember getMatchingSyntheticMember(Member member) {
        return this.munger.getMatchingSyntheticMember(member, this.aspectType);
    }

    @Override // org.aspectj.util.PartialOrder.PartialComparable
    public int compareTo(Object other) {
        ConcreteTypeMunger o = (ConcreteTypeMunger) other;
        ResolvedType otherAspect = o.aspectType;
        if (this.aspectType.equals(otherAspect)) {
            return getSignature().getStart() < o.getSignature().getStart() ? -1 : 1;
        }
        if (this.aspectType.isAssignableFrom(o.aspectType)) {
            return 1;
        }
        if (o.aspectType.isAssignableFrom(this.aspectType)) {
            return -1;
        }
        return 0;
    }

    @Override // org.aspectj.util.PartialOrder.PartialComparable
    public int fallbackCompareTo(Object other) {
        return 0;
    }

    public boolean isTargetTypeParameterized() {
        if (this.munger == null) {
            return false;
        }
        return this.munger.sharesTypeVariablesWithGenericType();
    }

    public boolean isLateMunger() {
        if (this.munger == null) {
            return false;
        }
        return this.munger.isLateMunger();
    }

    public boolean existsToSupportShadowMunging() {
        if (this.munger != null) {
            return this.munger.existsToSupportShadowMunging();
        }
        return false;
    }

    public boolean shouldOverwrite() {
        return true;
    }
}
