package org.aspectj.weaver;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.AjAttribute;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedMember.class */
public interface ResolvedMember extends Member, AnnotatedElement, TypeVariableDeclaringElement {
    public static final ResolvedMember[] NONE = new ResolvedMember[0];

    @Override // org.aspectj.weaver.Member
    int getModifiers(World world);

    @Override // org.aspectj.weaver.Member
    int getModifiers();

    @Override // org.aspectj.weaver.Member
    UnresolvedType[] getExceptions(World world);

    UnresolvedType[] getExceptions();

    ShadowMunger getAssociatedShadowMunger();

    boolean isAjSynthetic();

    boolean isCompatibleWith(Member member);

    boolean hasAnnotation(UnresolvedType unresolvedType);

    @Override // org.aspectj.weaver.Member
    AnnotationAJ[] getAnnotations();

    ResolvedType[] getAnnotationTypes();

    void setAnnotationTypes(ResolvedType[] resolvedTypeArr);

    void addAnnotation(AnnotationAJ annotationAJ);

    boolean isBridgeMethod();

    boolean isVarargsMethod();

    boolean isSynthetic();

    void write(CompressingDataOutputStream compressingDataOutputStream) throws IOException;

    ISourceContext getSourceContext(World world);

    String[] getParameterNames();

    void setParameterNames(String[] strArr);

    AnnotationAJ[][] getParameterAnnotations();

    ResolvedType[][] getParameterAnnotationTypes();

    String getAnnotationDefaultValue();

    String getParameterSignatureErased();

    String getSignatureErased();

    @Override // org.aspectj.weaver.Member
    String[] getParameterNames(World world);

    AjAttribute.EffectiveSignatureAttribute getEffectiveSignature();

    ISourceLocation getSourceLocation();

    int getStart();

    int getEnd();

    ISourceContext getSourceContext();

    void setPosition(int i, int i2);

    void setSourceContext(ISourceContext iSourceContext);

    boolean isAbstract();

    boolean isPublic();

    boolean isDefault();

    boolean isVisible(ResolvedType resolvedType);

    void setCheckedExceptions(UnresolvedType[] unresolvedTypeArr);

    void setAnnotatedElsewhere(boolean z);

    boolean isAnnotatedElsewhere();

    String toGenericString();

    String toDebugString();

    boolean hasBackingGenericMember();

    ResolvedMember getBackingGenericMember();

    @Override // org.aspectj.weaver.Member
    UnresolvedType getGenericReturnType();

    @Override // org.aspectj.weaver.Member
    UnresolvedType[] getGenericParameterTypes();

    boolean equalsApartFromDeclaringType(Object obj);

    ResolvedMemberImpl parameterizedWith(UnresolvedType[] unresolvedTypeArr, ResolvedType resolvedType, boolean z);

    ResolvedMemberImpl parameterizedWith(UnresolvedType[] unresolvedTypeArr, ResolvedType resolvedType, boolean z, List<String> list);

    void setTypeVariables(TypeVariable[] typeVariableArr);

    TypeVariable[] getTypeVariables();

    boolean matches(ResolvedMember resolvedMember, boolean z);

    void evictWeavingState();

    ResolvedMember parameterizedWith(Map<String, UnresolvedType> map, World world);

    boolean isDefaultConstructor();

    void setAnnotations(AnnotationAJ[] annotationAJArr);
}
