package org.aspectj.weaver;

import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/PrivilegedAccessMunger.class */
public class PrivilegedAccessMunger extends ResolvedTypeMunger {
    public boolean shortSyntax;

    public PrivilegedAccessMunger(ResolvedMember member, boolean shortSyntax) {
        super(PrivilegedAccess, member);
        this.shortSyntax = false;
        this.shortSyntax = shortSyntax;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public void write(CompressingDataOutputStream s) throws IOException {
        throw new RuntimeException("should not be serialized");
    }

    public ResolvedMember getMember() {
        return getSignature();
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public ResolvedMember getMatchingSyntheticMember(Member member, ResolvedType aspectType) {
        if (getSignature().getKind() == Member.FIELD) {
            ResolvedMember ret = AjcMemberMaker.privilegedAccessMethodForFieldGet(aspectType, getSignature(), this.shortSyntax);
            if (ResolvedType.matches(ret, member)) {
                return getSignature();
            }
            ResolvedMember ret2 = AjcMemberMaker.privilegedAccessMethodForFieldSet(aspectType, getSignature(), this.shortSyntax);
            if (ResolvedType.matches(ret2, member)) {
                return getSignature();
            }
            return null;
        }
        ResolvedMember ret3 = AjcMemberMaker.privilegedAccessMethodForMethod(aspectType, getSignature());
        if (ResolvedType.matches(ret3, member)) {
            return getSignature();
        }
        return null;
    }

    public boolean equals(Object other) {
        if (!(other instanceof PrivilegedAccessMunger)) {
            return false;
        }
        PrivilegedAccessMunger o = (PrivilegedAccessMunger) other;
        return this.kind.equals(o.kind) && (o.signature != null ? this.signature.equals(o.signature) : this.signature == null) && (o.declaredSignature != null ? this.declaredSignature.equals(o.declaredSignature) : this.declaredSignature == null) && (o.typeVariableAliases != null ? this.typeVariableAliases.equals(o.typeVariableAliases) : this.typeVariableAliases == null);
    }

    public int hashCode() {
        int result = (37 * 17) + this.kind.hashCode();
        return (37 * ((37 * ((37 * result) + (this.signature == null ? 0 : this.signature.hashCode()))) + (this.declaredSignature == null ? 0 : this.declaredSignature.hashCode()))) + (this.typeVariableAliases == null ? 0 : this.typeVariableAliases.hashCode());
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public boolean existsToSupportShadowMunging() {
        return true;
    }
}
