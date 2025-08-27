package org.aspectj.weaver;

import java.util.Collection;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/Member.class */
public interface Member extends Comparable<Member> {
    public static final Member[] NONE = new Member[0];
    public static final MemberKind METHOD = new MemberKind("METHOD", 1);
    public static final MemberKind FIELD = new MemberKind("FIELD", 2);
    public static final MemberKind CONSTRUCTOR = new MemberKind("CONSTRUCTOR", 3);
    public static final MemberKind STATIC_INITIALIZATION = new MemberKind("STATIC_INITIALIZATION", 4);
    public static final MemberKind POINTCUT = new MemberKind("POINTCUT", 5);
    public static final MemberKind ADVICE = new MemberKind("ADVICE", 6);
    public static final MemberKind HANDLER = new MemberKind("HANDLER", 7);
    public static final MemberKind MONITORENTER = new MemberKind("MONITORENTER", 8);
    public static final MemberKind MONITOREXIT = new MemberKind("MONITOREXIT", 9);
    public static final AnnotationAJ[][] NO_PARAMETER_ANNOTATIONXS = new AnnotationAJ[0];
    public static final ResolvedType[][] NO_PARAMETER_ANNOTATION_TYPES = new ResolvedType[0];

    MemberKind getKind();

    String getName();

    UnresolvedType getDeclaringType();

    UnresolvedType[] getParameterTypes();

    UnresolvedType[] getGenericParameterTypes();

    UnresolvedType getType();

    UnresolvedType getReturnType();

    UnresolvedType getGenericReturnType();

    String getSignature();

    JoinPointSignatureIterator getJoinPointSignatures(World world);

    int getArity();

    String getParameterSignature();

    int getModifiers(World world);

    int getModifiers();

    boolean canBeParameterized();

    AnnotationAJ[] getAnnotations();

    Collection<ResolvedType> getDeclaringTypes(World world);

    String[] getParameterNames(World world);

    UnresolvedType[] getExceptions(World world);

    ResolvedMember resolve(World world);

    int compareTo(Member member);
}
