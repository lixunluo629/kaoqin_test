package org.aspectj.weaver;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ExposeTypeMunger.class */
public class ExposeTypeMunger extends PrivilegedAccessMunger {
    public ExposeTypeMunger(UnresolvedType typeToExpose) {
        super(new ResolvedMemberImpl(Member.STATIC_INITIALIZATION, typeToExpose, 0, UnresolvedType.VOID, "<clinit>", UnresolvedType.NONE), false);
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public String toString() {
        return "ExposeTypeMunger(" + getSignature().getDeclaringType().getName() + ")";
    }

    public String getExposedTypeSignature() {
        return getSignature().getDeclaringType().getSignature();
    }
}
