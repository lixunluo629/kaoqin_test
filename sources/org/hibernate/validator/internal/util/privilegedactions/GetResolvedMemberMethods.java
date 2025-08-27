package org.hibernate.validator.internal.util.privilegedactions;

import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.members.ResolvedMethod;
import java.security.PrivilegedAction;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/privilegedactions/GetResolvedMemberMethods.class */
public final class GetResolvedMemberMethods implements PrivilegedAction<ResolvedMethod[]> {
    private final ResolvedTypeWithMembers type;

    public static GetResolvedMemberMethods action(ResolvedTypeWithMembers type) {
        return new GetResolvedMemberMethods(type);
    }

    private GetResolvedMemberMethods(ResolvedTypeWithMembers type) {
        this.type = type;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    public ResolvedMethod[] run() {
        return this.type.getMemberMethods();
    }
}
