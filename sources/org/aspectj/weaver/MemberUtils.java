package org.aspectj.weaver;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/MemberUtils.class */
public class MemberUtils {
    public static boolean isConstructor(ResolvedMember member) {
        return member.getName().equals("<init>");
    }
}
