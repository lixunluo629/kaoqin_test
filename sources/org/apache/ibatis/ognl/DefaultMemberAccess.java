package org.apache.ibatis.ognl;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Map;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/DefaultMemberAccess.class */
public class DefaultMemberAccess implements MemberAccess {
    public boolean allowPrivateAccess;
    public boolean allowProtectedAccess;
    public boolean allowPackageProtectedAccess;

    public DefaultMemberAccess(boolean allowAllAccess) {
        this(allowAllAccess, allowAllAccess, allowAllAccess);
    }

    public DefaultMemberAccess(boolean allowPrivateAccess, boolean allowProtectedAccess, boolean allowPackageProtectedAccess) {
        this.allowPrivateAccess = false;
        this.allowProtectedAccess = false;
        this.allowPackageProtectedAccess = false;
        this.allowPrivateAccess = allowPrivateAccess;
        this.allowProtectedAccess = allowProtectedAccess;
        this.allowPackageProtectedAccess = allowPackageProtectedAccess;
    }

    public boolean getAllowPrivateAccess() {
        return this.allowPrivateAccess;
    }

    public void setAllowPrivateAccess(boolean value) {
        this.allowPrivateAccess = value;
    }

    public boolean getAllowProtectedAccess() {
        return this.allowProtectedAccess;
    }

    public void setAllowProtectedAccess(boolean value) {
        this.allowProtectedAccess = value;
    }

    public boolean getAllowPackageProtectedAccess() {
        return this.allowPackageProtectedAccess;
    }

    public void setAllowPackageProtectedAccess(boolean value) {
        this.allowPackageProtectedAccess = value;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.ibatis.ognl.MemberAccess
    public Object setup(Map context, Object target, Member member, String propertyName) throws SecurityException {
        Object result = null;
        if (isAccessible(context, target, member, propertyName)) {
            AccessibleObject accessible = (AccessibleObject) member;
            if (!accessible.isAccessible()) {
                result = Boolean.TRUE;
                accessible.setAccessible(true);
            }
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.ibatis.ognl.MemberAccess
    public void restore(Map context, Object target, Member member, String propertyName, Object state) throws SecurityException {
        if (state != null) {
            ((AccessibleObject) member).setAccessible(((Boolean) state).booleanValue());
        }
    }

    @Override // org.apache.ibatis.ognl.MemberAccess
    public boolean isAccessible(Map context, Object target, Member member, String propertyName) {
        int modifiers = member.getModifiers();
        boolean result = Modifier.isPublic(modifiers);
        if (!result) {
            if (Modifier.isPrivate(modifiers)) {
                result = getAllowPrivateAccess();
            } else if (Modifier.isProtected(modifiers)) {
                result = getAllowProtectedAccess();
            } else {
                result = getAllowPackageProtectedAccess();
            }
        }
        return result;
    }
}
