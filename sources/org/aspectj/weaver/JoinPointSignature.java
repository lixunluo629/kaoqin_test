package org.aspectj.weaver;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.AjAttribute;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/JoinPointSignature.class */
public class JoinPointSignature implements ResolvedMember {
    public static final JoinPointSignature[] EMPTY_ARRAY = new JoinPointSignature[0];
    private ResolvedMember realMember;
    private ResolvedType substituteDeclaringType;

    public JoinPointSignature(ResolvedMember backing, ResolvedType aType) {
        this.realMember = backing;
        this.substituteDeclaringType = aType;
    }

    @Override // org.aspectj.weaver.Member
    public UnresolvedType getDeclaringType() {
        return this.substituteDeclaringType;
    }

    @Override // org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.Member
    public int getModifiers(World world) {
        return this.realMember.getModifiers(world);
    }

    @Override // org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.Member
    public int getModifiers() {
        return this.realMember.getModifiers();
    }

    @Override // org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.Member
    public UnresolvedType[] getExceptions(World world) {
        return this.realMember.getExceptions(world);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public UnresolvedType[] getExceptions() {
        return this.realMember.getExceptions();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ShadowMunger getAssociatedShadowMunger() {
        return this.realMember.getAssociatedShadowMunger();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isAjSynthetic() {
        return this.realMember.isAjSynthetic();
    }

    @Override // org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.AnnotatedElement
    public boolean hasAnnotation(UnresolvedType ofType) {
        return this.realMember.hasAnnotation(ofType);
    }

    @Override // org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.AnnotatedElement
    public ResolvedType[] getAnnotationTypes() {
        return this.realMember.getAnnotationTypes();
    }

    @Override // org.aspectj.weaver.AnnotatedElement
    public AnnotationAJ getAnnotationOfType(UnresolvedType ofType) {
        return this.realMember.getAnnotationOfType(ofType);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setAnnotationTypes(ResolvedType[] annotationtypes) {
        this.realMember.setAnnotationTypes(annotationtypes);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setAnnotations(AnnotationAJ[] annotations) {
        this.realMember.setAnnotations(annotations);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void addAnnotation(AnnotationAJ annotation) {
        this.realMember.addAnnotation(annotation);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isBridgeMethod() {
        return this.realMember.isBridgeMethod();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isVarargsMethod() {
        return this.realMember.isVarargsMethod();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isSynthetic() {
        return this.realMember.isSynthetic();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void write(CompressingDataOutputStream s) throws IOException {
        this.realMember.write(s);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ISourceContext getSourceContext(World world) {
        return this.realMember.getSourceContext(world);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public String[] getParameterNames() {
        return this.realMember.getParameterNames();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setParameterNames(String[] names) {
        this.realMember.setParameterNames(names);
    }

    @Override // org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.Member
    public String[] getParameterNames(World world) {
        return this.realMember.getParameterNames(world);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public AjAttribute.EffectiveSignatureAttribute getEffectiveSignature() {
        return this.realMember.getEffectiveSignature();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ISourceLocation getSourceLocation() {
        return this.realMember.getSourceLocation();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public int getEnd() {
        return this.realMember.getEnd();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ISourceContext getSourceContext() {
        return this.realMember.getSourceContext();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public int getStart() {
        return this.realMember.getStart();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setPosition(int sourceStart, int sourceEnd) {
        this.realMember.setPosition(sourceStart, sourceEnd);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setSourceContext(ISourceContext sourceContext) {
        this.realMember.setSourceContext(sourceContext);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isAbstract() {
        return this.realMember.isAbstract();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isPublic() {
        return this.realMember.isPublic();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isDefault() {
        return this.realMember.isDefault();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isVisible(ResolvedType fromType) {
        return this.realMember.isVisible(fromType);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setCheckedExceptions(UnresolvedType[] checkedExceptions) {
        this.realMember.setCheckedExceptions(checkedExceptions);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setAnnotatedElsewhere(boolean b) {
        this.realMember.setAnnotatedElsewhere(b);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isAnnotatedElsewhere() {
        return this.realMember.isAnnotatedElsewhere();
    }

    @Override // org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.Member
    public UnresolvedType getGenericReturnType() {
        return this.realMember.getGenericReturnType();
    }

    @Override // org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.Member
    public UnresolvedType[] getGenericParameterTypes() {
        return this.realMember.getGenericParameterTypes();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ResolvedMemberImpl parameterizedWith(UnresolvedType[] typeParameters, ResolvedType newDeclaringType, boolean isParameterized) {
        return this.realMember.parameterizedWith(typeParameters, newDeclaringType, isParameterized);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ResolvedMemberImpl parameterizedWith(UnresolvedType[] typeParameters, ResolvedType newDeclaringType, boolean isParameterized, List<String> aliases) {
        return this.realMember.parameterizedWith(typeParameters, newDeclaringType, isParameterized, aliases);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setTypeVariables(TypeVariable[] types) {
        this.realMember.setTypeVariables(types);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public TypeVariable[] getTypeVariables() {
        return this.realMember.getTypeVariables();
    }

    @Override // org.aspectj.weaver.TypeVariableDeclaringElement
    public TypeVariable getTypeVariableNamed(String name) {
        return this.realMember.getTypeVariableNamed(name);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean matches(ResolvedMember aCandidateMatch, boolean ignoreGenerics) {
        return this.realMember.matches(aCandidateMatch, ignoreGenerics);
    }

    @Override // org.aspectj.weaver.Member
    public ResolvedMember resolve(World world) {
        return this.realMember.resolve(world);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(Member other) {
        return this.realMember.compareTo(other);
    }

    @Override // org.aspectj.weaver.Member
    public MemberKind getKind() {
        return this.realMember.getKind();
    }

    @Override // org.aspectj.weaver.Member
    public UnresolvedType getReturnType() {
        return this.realMember.getReturnType();
    }

    @Override // org.aspectj.weaver.Member
    public UnresolvedType getType() {
        return this.realMember.getType();
    }

    @Override // org.aspectj.weaver.Member
    public String getName() {
        return this.realMember.getName();
    }

    @Override // org.aspectj.weaver.Member
    public UnresolvedType[] getParameterTypes() {
        return this.realMember.getParameterTypes();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public AnnotationAJ[][] getParameterAnnotations() {
        return this.realMember.getParameterAnnotations();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ResolvedType[][] getParameterAnnotationTypes() {
        return this.realMember.getParameterAnnotationTypes();
    }

    @Override // org.aspectj.weaver.Member
    public String getSignature() {
        return this.realMember.getSignature();
    }

    @Override // org.aspectj.weaver.Member
    public int getArity() {
        return this.realMember.getArity();
    }

    @Override // org.aspectj.weaver.Member
    public String getParameterSignature() {
        return this.realMember.getParameterSignature();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isCompatibleWith(Member am) {
        return this.realMember.isCompatibleWith(am);
    }

    @Override // org.aspectj.weaver.Member
    public boolean canBeParameterized() {
        return this.realMember.canBeParameterized();
    }

    @Override // org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.Member
    public AnnotationAJ[] getAnnotations() {
        return this.realMember.getAnnotations();
    }

    @Override // org.aspectj.weaver.Member
    public Collection<ResolvedType> getDeclaringTypes(World world) {
        throw new UnsupportedOperationException("Adrian doesn't think you should be calling this...");
    }

    @Override // org.aspectj.weaver.Member
    public JoinPointSignatureIterator getJoinPointSignatures(World world) {
        return this.realMember.getJoinPointSignatures(world);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getReturnType().getName());
        buf.append(' ');
        buf.append(getDeclaringType().getName());
        buf.append('.');
        buf.append(getName());
        if (getKind() != FIELD) {
            buf.append("(");
            UnresolvedType[] parameterTypes = getParameterTypes();
            if (parameterTypes.length != 0) {
                buf.append(parameterTypes[0]);
                int len = parameterTypes.length;
                for (int i = 1; i < len; i++) {
                    buf.append(", ");
                    buf.append(parameterTypes[i].getName());
                }
            }
            buf.append(")");
        }
        return buf.toString();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public String toGenericString() {
        return this.realMember.toGenericString();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public String toDebugString() {
        return this.realMember.toDebugString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof JoinPointSignature)) {
            return false;
        }
        JoinPointSignature other = (JoinPointSignature) obj;
        if (!this.realMember.equals(other.realMember) || !this.substituteDeclaringType.equals(other.substituteDeclaringType)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return 17 + (37 * this.realMember.hashCode()) + (37 * this.substituteDeclaringType.hashCode());
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean hasBackingGenericMember() {
        return this.realMember.hasBackingGenericMember();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ResolvedMember getBackingGenericMember() {
        return this.realMember.getBackingGenericMember();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void evictWeavingState() {
        this.realMember.evictWeavingState();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ResolvedMember parameterizedWith(Map m, World w) {
        return this.realMember.parameterizedWith(m, w);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public String getAnnotationDefaultValue() {
        return this.realMember.getAnnotationDefaultValue();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public String getParameterSignatureErased() {
        return this.realMember.getParameterSignatureErased();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public String getSignatureErased() {
        return this.realMember.getSignatureErased();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isDefaultConstructor() {
        return this.realMember.isDefaultConstructor();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean equalsApartFromDeclaringType(Object other) {
        return this.realMember.equalsApartFromDeclaringType(other);
    }
}
