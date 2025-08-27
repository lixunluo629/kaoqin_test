package org.aspectj.weaver;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.weaver.AjAttribute;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedMemberImpl.class */
public class ResolvedMemberImpl extends MemberImpl implements IHasPosition, ResolvedMember {
    private String[] parameterNames;
    private boolean isResolved;
    protected UnresolvedType[] checkedExceptions;
    protected ResolvedMember backingGenericMember;
    protected AnnotationAJ[] annotations;
    protected ResolvedType[] annotationTypes;
    protected AnnotationAJ[][] parameterAnnotations;
    protected ResolvedType[][] parameterAnnotationTypes;
    private boolean isAnnotatedElsewhere;
    private boolean isAjSynthetic;
    protected TypeVariable[] typeVariables;
    protected int start;
    protected int end;
    protected ISourceContext sourceContext;
    private String myParameterSignatureWithBoundsRemoved;
    private String myParameterSignatureErasure;
    public static boolean showParameterNames = true;

    public ResolvedMemberImpl(MemberKind kind, UnresolvedType declaringType, int modifiers, UnresolvedType returnType, String name, UnresolvedType[] parameterTypes) {
        super(kind, declaringType, modifiers, returnType, name, parameterTypes);
        this.parameterNames = null;
        this.isResolved = false;
        this.checkedExceptions = UnresolvedType.NONE;
        this.backingGenericMember = null;
        this.annotations = null;
        this.annotationTypes = null;
        this.parameterAnnotations = (AnnotationAJ[][]) null;
        this.parameterAnnotationTypes = (ResolvedType[][]) null;
        this.isAnnotatedElsewhere = false;
        this.isAjSynthetic = false;
        this.sourceContext = null;
        this.myParameterSignatureWithBoundsRemoved = null;
        this.myParameterSignatureErasure = null;
    }

    public ResolvedMemberImpl(MemberKind kind, UnresolvedType declaringType, int modifiers, UnresolvedType returnType, String name, UnresolvedType[] parameterTypes, UnresolvedType[] checkedExceptions) {
        super(kind, declaringType, modifiers, returnType, name, parameterTypes);
        this.parameterNames = null;
        this.isResolved = false;
        this.checkedExceptions = UnresolvedType.NONE;
        this.backingGenericMember = null;
        this.annotations = null;
        this.annotationTypes = null;
        this.parameterAnnotations = (AnnotationAJ[][]) null;
        this.parameterAnnotationTypes = (ResolvedType[][]) null;
        this.isAnnotatedElsewhere = false;
        this.isAjSynthetic = false;
        this.sourceContext = null;
        this.myParameterSignatureWithBoundsRemoved = null;
        this.myParameterSignatureErasure = null;
        this.checkedExceptions = checkedExceptions;
    }

    public ResolvedMemberImpl(MemberKind kind, UnresolvedType declaringType, int modifiers, UnresolvedType returnType, String name, UnresolvedType[] parameterTypes, UnresolvedType[] checkedExceptions, ResolvedMember backingGenericMember) {
        this(kind, declaringType, modifiers, returnType, name, parameterTypes, checkedExceptions);
        this.backingGenericMember = backingGenericMember;
        this.isAjSynthetic = backingGenericMember.isAjSynthetic();
    }

    public ResolvedMemberImpl(MemberKind kind, UnresolvedType declaringType, int modifiers, String name, String signature) {
        super(kind, declaringType, modifiers, name, signature);
        this.parameterNames = null;
        this.isResolved = false;
        this.checkedExceptions = UnresolvedType.NONE;
        this.backingGenericMember = null;
        this.annotations = null;
        this.annotationTypes = null;
        this.parameterAnnotations = (AnnotationAJ[][]) null;
        this.parameterAnnotationTypes = (ResolvedType[][]) null;
        this.isAnnotatedElsewhere = false;
        this.isAjSynthetic = false;
        this.sourceContext = null;
        this.myParameterSignatureWithBoundsRemoved = null;
        this.myParameterSignatureErasure = null;
    }

    public static JoinPointSignature[] getJoinPointSignatures(Member joinPointSignature, World inAWorld) {
        ResolvedType originalDeclaringType = joinPointSignature.getDeclaringType().resolve(inAWorld);
        ResolvedMemberImpl firstDefiningMember = (ResolvedMemberImpl) joinPointSignature.resolve(inAWorld);
        if (firstDefiningMember == null) {
            return JoinPointSignature.EMPTY_ARRAY;
        }
        ResolvedType firstDefiningType = firstDefiningMember.getDeclaringType().resolve(inAWorld);
        if (firstDefiningType != originalDeclaringType && joinPointSignature.getKind() == Member.CONSTRUCTOR) {
            return JoinPointSignature.EMPTY_ARRAY;
        }
        List<ResolvedType> declaringTypes = new ArrayList<>();
        accumulateTypesInBetween(originalDeclaringType, firstDefiningType, declaringTypes);
        Set<ResolvedMember> memberSignatures = new HashSet<>();
        for (ResolvedType declaringType : declaringTypes) {
            memberSignatures.add(new JoinPointSignature(firstDefiningMember, declaringType));
        }
        if (shouldWalkUpHierarchyFor(firstDefiningMember)) {
            Iterator<ResolvedType> superTypeIterator = firstDefiningType.getDirectSupertypes();
            List<ResolvedType> typesAlreadyVisited = new ArrayList<>();
            accumulateMembersMatching(firstDefiningMember, superTypeIterator, typesAlreadyVisited, memberSignatures, false);
        }
        JoinPointSignature[] ret = new JoinPointSignature[memberSignatures.size()];
        memberSignatures.toArray(ret);
        return ret;
    }

    private static boolean shouldWalkUpHierarchyFor(Member aMember) {
        if (aMember.getKind() == Member.CONSTRUCTOR || aMember.getKind() == Member.FIELD || Modifier.isStatic(aMember.getModifiers())) {
            return false;
        }
        return true;
    }

    private static void accumulateTypesInBetween(ResolvedType subType, ResolvedType superType, List<ResolvedType> types) {
        types.add(subType);
        if (subType == superType) {
            return;
        }
        Iterator<ResolvedType> iter = subType.getDirectSupertypes();
        while (iter.hasNext()) {
            ResolvedType parent = iter.next();
            if (superType.isAssignableFrom(parent)) {
                accumulateTypesInBetween(parent, superType, types);
            }
        }
    }

    private static void accumulateMembersMatching(ResolvedMemberImpl memberToMatch, Iterator<ResolvedType> typesToLookIn, List<ResolvedType> typesAlreadyVisited, Set<ResolvedMember> foundMembers, boolean ignoreGenerics) {
        while (typesToLookIn.hasNext()) {
            ResolvedType toLookIn = typesToLookIn.next();
            if (!typesAlreadyVisited.contains(toLookIn)) {
                typesAlreadyVisited.add(toLookIn);
                ResolvedMemberImpl foundMember = (ResolvedMemberImpl) toLookIn.lookupResolvedMember(memberToMatch, true, ignoreGenerics);
                if (foundMember != null && isVisibleTo(memberToMatch, foundMember)) {
                    List<ResolvedType> declaringTypes = new ArrayList<>();
                    ResolvedType resolvedDeclaringType = foundMember.getDeclaringType().resolve(toLookIn.getWorld());
                    accumulateTypesInBetween(toLookIn, resolvedDeclaringType, declaringTypes);
                    for (ResolvedType declaringType : declaringTypes) {
                        foundMembers.add(new JoinPointSignature(foundMember, declaringType));
                    }
                    if (!ignoreGenerics && toLookIn.isParameterizedType() && foundMember.backingGenericMember != null) {
                        foundMembers.add(new JoinPointSignature(foundMember.backingGenericMember, foundMember.declaringType.resolve(toLookIn.getWorld())));
                    }
                    accumulateMembersMatching(foundMember, toLookIn.getDirectSupertypes(), typesAlreadyVisited, foundMembers, ignoreGenerics);
                }
            }
        }
    }

    private static boolean isVisibleTo(ResolvedMember childMember, ResolvedMember parentMember) {
        if (!childMember.getDeclaringType().equals(parentMember.getDeclaringType()) && Modifier.isPrivate(parentMember.getModifiers())) {
            return false;
        }
        return true;
    }

    @Override // org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public final int getModifiers(World world) {
        return this.modifiers;
    }

    @Override // org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public final int getModifiers() {
        return this.modifiers;
    }

    @Override // org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public final UnresolvedType[] getExceptions(World world) {
        return getExceptions();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public UnresolvedType[] getExceptions() {
        return this.checkedExceptions;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ShadowMunger getAssociatedShadowMunger() {
        return null;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isAjSynthetic() {
        return this.isAjSynthetic;
    }

    protected void setAjSynthetic(boolean b) {
        this.isAjSynthetic = b;
    }

    public boolean hasAnnotations() {
        return this.annotationTypes != null;
    }

    @Override // org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.AnnotatedElement
    public boolean hasAnnotation(UnresolvedType ofType) {
        if (this.backingGenericMember != null) {
            if (this.annotationTypes != null) {
                throw new BCException("Unexpectedly found a backing generic member and a local set of annotations");
            }
            return this.backingGenericMember.hasAnnotation(ofType);
        }
        if (this.annotationTypes != null) {
            int max = this.annotationTypes.length;
            for (int i = 0; i < max; i++) {
                if (this.annotationTypes[i].equals(ofType)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // org.aspectj.weaver.ResolvedMember, org.aspectj.weaver.AnnotatedElement
    public ResolvedType[] getAnnotationTypes() {
        if (this.backingGenericMember != null) {
            if (this.annotationTypes != null) {
                throw new BCException("Unexpectedly found a backing generic member and a local set of annotations");
            }
            return this.backingGenericMember.getAnnotationTypes();
        }
        return this.annotationTypes;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public String getAnnotationDefaultValue() {
        throw new UnsupportedOperationException("You should resolve this member and call getAnnotationDefaultValue() on the result...");
    }

    @Override // org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public AnnotationAJ[] getAnnotations() {
        if (this.backingGenericMember != null) {
            return this.backingGenericMember.getAnnotations();
        }
        if (this.annotations != null) {
            return this.annotations;
        }
        return super.getAnnotations();
    }

    @Override // org.aspectj.weaver.AnnotatedElement
    public AnnotationAJ getAnnotationOfType(UnresolvedType ofType) {
        if (this.annotations != null) {
            for (AnnotationAJ annotation : this.annotations) {
                if (annotation.getType().equals(ofType)) {
                    return annotation;
                }
            }
            return null;
        }
        throw new UnsupportedOperationException("You should resolve this member and call getAnnotationOfType() on the result...");
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setAnnotations(AnnotationAJ[] annotations) {
        this.annotations = annotations;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setAnnotationTypes(ResolvedType[] annotationTypes) {
        this.annotationTypes = annotationTypes;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ResolvedType[][] getParameterAnnotationTypes() {
        return this.parameterAnnotationTypes;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public AnnotationAJ[][] getParameterAnnotations() {
        if (this.backingGenericMember != null) {
            return this.backingGenericMember.getParameterAnnotations();
        }
        throw new BCException("Cannot return parameter annotations for a " + getClass().getName() + " member");
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void addAnnotation(AnnotationAJ annotation) {
        if (this.annotationTypes == null) {
            this.annotationTypes = new ResolvedType[1];
            this.annotationTypes[0] = annotation.getType();
            this.annotations = new AnnotationAJ[1];
            this.annotations[0] = annotation;
            return;
        }
        int len = this.annotations.length;
        AnnotationAJ[] ret = new AnnotationAJ[len + 1];
        System.arraycopy(this.annotations, 0, ret, 0, len);
        ret[len] = annotation;
        this.annotations = ret;
        ResolvedType[] newAnnotationTypes = new ResolvedType[len + 1];
        System.arraycopy(this.annotationTypes, 0, newAnnotationTypes, 0, len);
        newAnnotationTypes[len] = annotation.getType();
        this.annotationTypes = newAnnotationTypes;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isBridgeMethod() {
        return (this.modifiers & 64) != 0 && getKind().equals(METHOD);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isVarargsMethod() {
        return (this.modifiers & 128) != 0;
    }

    public void setVarargsMethod() {
        this.modifiers |= 128;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isSynthetic() {
        return (this.modifiers & 4096) != 0;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void write(CompressingDataOutputStream s) throws IOException {
        getKind().write(s);
        s.writeBoolean(s.canCompress());
        if (s.canCompress()) {
            s.writeCompressedSignature(getDeclaringType().getSignature());
        } else {
            getDeclaringType().write(s);
        }
        s.writeInt(this.modifiers);
        if (s.canCompress()) {
            s.writeCompressedName(getName());
            s.writeCompressedSignature(getSignature());
        } else {
            s.writeUTF(getName());
            s.writeUTF(getSignature());
        }
        UnresolvedType.writeArray(getExceptions(), s);
        s.writeInt(getStart());
        s.writeInt(getEnd());
        s.writeBoolean(isVarargsMethod());
        if (this.typeVariables == null) {
            s.writeByte(0);
        } else {
            s.writeByte(this.typeVariables.length);
            for (int i = 0; i < this.typeVariables.length; i++) {
                this.typeVariables[i].write(s);
            }
        }
        String gsig = getGenericSignature();
        if (getSignature().equals(gsig)) {
            s.writeByte(255);
            return;
        }
        s.writeByte(this.parameterTypes.length);
        for (int i2 = 0; i2 < this.parameterTypes.length; i2++) {
            if (s.canCompress()) {
                s.writeCompressedSignature(this.parameterTypes[i2].getSignature());
            } else {
                UnresolvedType array_element = this.parameterTypes[i2];
                array_element.write(s);
            }
        }
        if (s.canCompress()) {
            s.writeCompressedSignature(this.returnType.getSignature());
        } else {
            this.returnType.write(s);
        }
    }

    public String getSignatureForAttribute() {
        StringBuffer sb = new StringBuffer();
        if (this.typeVariables != null) {
            sb.append("<");
            for (int i = 0; i < this.typeVariables.length; i++) {
                sb.append(this.typeVariables[i].getSignatureForAttribute());
            }
            sb.append(">");
        }
        sb.append("(");
        for (int i2 = 0; i2 < this.parameterTypes.length; i2++) {
            ResolvedType ptype = (ResolvedType) this.parameterTypes[i2];
            sb.append(ptype.getSignatureForAttribute());
        }
        sb.append(")");
        sb.append(((ResolvedType) this.returnType).getSignatureForAttribute());
        return sb.toString();
    }

    public String getGenericSignature() {
        StringBuffer sb = new StringBuffer();
        if (this.typeVariables != null) {
            sb.append("<");
            for (int i = 0; i < this.typeVariables.length; i++) {
                sb.append(this.typeVariables[i].getSignature());
            }
            sb.append(">");
        }
        sb.append("(");
        for (int i2 = 0; i2 < this.parameterTypes.length; i2++) {
            UnresolvedType ptype = this.parameterTypes[i2];
            sb.append(ptype.getSignature());
        }
        sb.append(")");
        sb.append(this.returnType.getSignature());
        return sb.toString();
    }

    public static void writeArray(ResolvedMember[] members, CompressingDataOutputStream s) throws IOException {
        s.writeInt(members.length);
        for (ResolvedMember resolvedMember : members) {
            resolvedMember.write(s);
        }
    }

    public static ResolvedMemberImpl readResolvedMember(VersionedDataInputStream s, ISourceContext sourceContext) throws IOException {
        boolean hasAGenericSignature;
        MemberKind mk = MemberKind.read(s);
        boolean compressed = s.isAtLeast169() ? s.readBoolean() : false;
        UnresolvedType declaringType = compressed ? UnresolvedType.forSignature(s.readUtf8(s.readShort())) : UnresolvedType.read(s);
        int modifiers = s.readInt();
        String name = compressed ? s.readUtf8(s.readShort()) : s.readUTF();
        String signature = compressed ? s.readUtf8(s.readShort()) : s.readUTF();
        ResolvedMemberImpl m = new ResolvedMemberImpl(mk, declaringType, modifiers, name, signature);
        m.checkedExceptions = UnresolvedType.readArray(s);
        m.start = s.readInt();
        m.end = s.readInt();
        m.sourceContext = sourceContext;
        if (s.getMajorVersion() >= 2) {
            if (s.getMajorVersion() >= 3) {
                boolean isvarargs = s.readBoolean();
                if (isvarargs) {
                    m.setVarargsMethod();
                }
            }
            int tvcount = s.isAtLeast169() ? s.readByte() : s.readInt();
            if (tvcount != 0) {
                m.typeVariables = new TypeVariable[tvcount];
                for (int i = 0; i < tvcount; i++) {
                    m.typeVariables[i] = TypeVariable.read(s);
                    m.typeVariables[i].setDeclaringElement(m);
                    m.typeVariables[i].setRank(i);
                }
            }
            if (s.getMajorVersion() >= 3) {
                int pcount = -1;
                if (s.isAtLeast169()) {
                    pcount = s.readByte();
                    hasAGenericSignature = pcount >= 0 && pcount < 255;
                } else {
                    hasAGenericSignature = s.readBoolean();
                }
                if (hasAGenericSignature) {
                    int ps = s.isAtLeast169() ? pcount : s.readInt();
                    UnresolvedType[] params = new UnresolvedType[ps];
                    for (int i2 = 0; i2 < params.length; i2++) {
                        if (compressed) {
                            params[i2] = TypeFactory.createTypeFromSignature(s.readSignature());
                        } else {
                            params[i2] = TypeFactory.createTypeFromSignature(s.readUTF());
                        }
                    }
                    UnresolvedType rt = compressed ? TypeFactory.createTypeFromSignature(s.readSignature()) : TypeFactory.createTypeFromSignature(s.readUTF());
                    m.parameterTypes = params;
                    m.returnType = rt;
                }
            }
        }
        return m;
    }

    public static ResolvedMember[] readResolvedMemberArray(VersionedDataInputStream s, ISourceContext context) throws IOException {
        int len = s.readInt();
        ResolvedMember[] members = new ResolvedMember[len];
        for (int i = 0; i < len; i++) {
            members[i] = readResolvedMember(s, context);
        }
        return members;
    }

    @Override // org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public ResolvedMember resolve(World world) {
        if (this.isResolved) {
            return this;
        }
        try {
            if (this.typeVariables != null && this.typeVariables.length > 0) {
                for (int i = 0; i < this.typeVariables.length; i++) {
                    this.typeVariables[i] = this.typeVariables[i].resolve(world);
                }
            }
            world.setTypeVariableLookupScope(this);
            this.declaringType = this.declaringType.resolve(world);
            if (this.declaringType.isRawType()) {
                this.declaringType = ((ReferenceType) this.declaringType).getGenericType();
            }
            if (this.parameterTypes != null && this.parameterTypes.length > 0) {
                for (int i2 = 0; i2 < this.parameterTypes.length; i2++) {
                    this.parameterTypes[i2] = this.parameterTypes[i2].resolve(world);
                }
            }
            this.returnType = this.returnType.resolve(world);
            this.isResolved = true;
            return this;
        } finally {
            world.setTypeVariableLookupScope(null);
        }
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ISourceContext getSourceContext(World world) {
        return getDeclaringType().resolve(world).getSourceContext();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public String[] getParameterNames() {
        return this.parameterNames;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public final void setParameterNames(String[] pnames) {
        this.parameterNames = pnames;
    }

    @Override // org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public final String[] getParameterNames(World world) {
        return getParameterNames();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public AjAttribute.EffectiveSignatureAttribute getEffectiveSignature() {
        return null;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ISourceLocation getSourceLocation() {
        if (getSourceContext() == null) {
            return null;
        }
        return getSourceContext().makeSourceLocation(this);
    }

    @Override // org.aspectj.weaver.IHasPosition
    public int getEnd() {
        return this.end;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ISourceContext getSourceContext() {
        return this.sourceContext;
    }

    @Override // org.aspectj.weaver.IHasPosition
    public int getStart() {
        return this.start;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setPosition(int sourceStart, int sourceEnd) {
        this.start = sourceStart;
        this.end = sourceEnd;
    }

    public void setDeclaringType(ReferenceType rt) {
        this.declaringType = rt;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setSourceContext(ISourceContext sourceContext) {
        this.sourceContext = sourceContext;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isAbstract() {
        return Modifier.isAbstract(this.modifiers);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isPublic() {
        return Modifier.isPublic(this.modifiers);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isDefault() {
        int mods = getModifiers();
        return (Modifier.isPublic(mods) || Modifier.isProtected(mods) || Modifier.isPrivate(mods)) ? false : true;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isVisible(ResolvedType fromType) {
        ResolvedType type;
        UnresolvedType declaringType = getDeclaringType();
        if (fromType.equals(declaringType)) {
            type = fromType;
        } else {
            World world = fromType.getWorld();
            type = declaringType.resolve(world);
        }
        return ResolvedType.isVisible(getModifiers(), type, fromType);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setCheckedExceptions(UnresolvedType[] checkedExceptions) {
        this.checkedExceptions = checkedExceptions;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setAnnotatedElsewhere(boolean b) {
        this.isAnnotatedElsewhere = b;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isAnnotatedElsewhere() {
        return this.isAnnotatedElsewhere;
    }

    @Override // org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public UnresolvedType getGenericReturnType() {
        return getReturnType();
    }

    @Override // org.aspectj.weaver.MemberImpl, org.aspectj.weaver.Member
    public UnresolvedType[] getGenericParameterTypes() {
        return getParameterTypes();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ResolvedMemberImpl parameterizedWith(UnresolvedType[] typeParameters, ResolvedType newDeclaringType, boolean isParameterized) {
        return parameterizedWith(typeParameters, newDeclaringType, isParameterized, null);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ResolvedMemberImpl parameterizedWith(UnresolvedType[] typeParameters, ResolvedType newDeclaringType, boolean isParameterized, List<String> aliases) {
        if (!getDeclaringType().isGenericType() && getDeclaringType().getName().indexOf(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) == -1) {
            throw new IllegalStateException("Can't ask to parameterize a member of non-generic type: " + getDeclaringType() + "  kind(" + getDeclaringType().typeKind + ")");
        }
        TypeVariable[] typeVariables = getDeclaringType().getTypeVariables();
        if (isParameterized && typeVariables.length != typeParameters.length) {
            throw new IllegalStateException("Wrong number of type parameters supplied");
        }
        Map<String, UnresolvedType> typeMap = new HashMap<>();
        boolean typeParametersSupplied = typeParameters != null && typeParameters.length > 0;
        if (typeVariables != null) {
            for (int i = 0; i < typeVariables.length; i++) {
                UnresolvedType ut = !typeParametersSupplied ? typeVariables[i].getFirstBound() : typeParameters[i];
                typeMap.put(typeVariables[i].getName(), ut);
            }
        }
        if (aliases != null) {
            int posn = 0;
            for (String typeVariableAlias : aliases) {
                typeMap.put(typeVariableAlias, !typeParametersSupplied ? typeVariables[posn].getFirstBound() : typeParameters[posn]);
                posn++;
            }
        }
        UnresolvedType parameterizedReturnType = parameterize(getGenericReturnType(), typeMap, isParameterized, newDeclaringType.getWorld());
        UnresolvedType[] parameterizedParameterTypes = new UnresolvedType[getGenericParameterTypes().length];
        UnresolvedType[] genericParameterTypes = getGenericParameterTypes();
        for (int i2 = 0; i2 < parameterizedParameterTypes.length; i2++) {
            parameterizedParameterTypes[i2] = parameterize(genericParameterTypes[i2], typeMap, isParameterized, newDeclaringType.getWorld());
        }
        ResolvedMemberImpl ret = new ResolvedMemberImpl(getKind(), newDeclaringType, getModifiers(), parameterizedReturnType, getName(), parameterizedParameterTypes, getExceptions(), this);
        ret.setTypeVariables(getTypeVariables());
        ret.setSourceContext(getSourceContext());
        ret.setPosition(getStart(), getEnd());
        ret.setParameterNames(getParameterNames());
        return ret;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ResolvedMember parameterizedWith(Map<String, UnresolvedType> m, World w) {
        this.declaringType = this.declaringType.resolve(w);
        if (this.declaringType.isRawType()) {
            this.declaringType = ((ResolvedType) this.declaringType).getGenericType();
        }
        UnresolvedType parameterizedReturnType = parameterize(getGenericReturnType(), m, true, w);
        UnresolvedType[] parameterizedParameterTypes = new UnresolvedType[getGenericParameterTypes().length];
        UnresolvedType[] genericParameterTypes = getGenericParameterTypes();
        for (int i = 0; i < parameterizedParameterTypes.length; i++) {
            parameterizedParameterTypes[i] = parameterize(genericParameterTypes[i], m, true, w);
        }
        ResolvedMemberImpl ret = new ResolvedMemberImpl(getKind(), this.declaringType, getModifiers(), parameterizedReturnType, getName(), parameterizedParameterTypes, getExceptions(), this);
        ret.setTypeVariables(getTypeVariables());
        ret.setSourceContext(getSourceContext());
        ret.setPosition(getStart(), getEnd());
        ret.setParameterNames(getParameterNames());
        return ret;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void setTypeVariables(TypeVariable[] tvars) {
        this.typeVariables = tvars;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public TypeVariable[] getTypeVariables() {
        return this.typeVariables;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected UnresolvedType parameterize(UnresolvedType unresolvedType, Map<String, UnresolvedType> typeVariableMap, boolean inParameterizedType, World w) {
        UnresolvedType arrayType;
        UnresolvedType aType;
        if (unresolvedType instanceof TypeVariableReference) {
            String variableName = ((TypeVariableReference) unresolvedType).getTypeVariable().getName();
            if (!typeVariableMap.containsKey(variableName)) {
                return unresolvedType;
            }
            return typeVariableMap.get(variableName);
        }
        if (unresolvedType.isParameterizedType()) {
            if (inParameterizedType) {
                if (w != null) {
                    aType = unresolvedType.resolve(w);
                } else {
                    UnresolvedType dType = getDeclaringType();
                    aType = unresolvedType.resolve(((ResolvedType) dType).getWorld());
                }
                return aType.parameterize(typeVariableMap);
            }
            return unresolvedType.getRawType();
        }
        if (unresolvedType.isArray()) {
            String sig = unresolvedType.getSignature();
            UnresolvedType componentSig = UnresolvedType.forSignature(sig.substring(1));
            UnresolvedType parameterizedComponentSig = parameterize(componentSig, typeVariableMap, inParameterizedType, w);
            if (parameterizedComponentSig.isTypeVariableReference() && (parameterizedComponentSig instanceof UnresolvedTypeVariableReferenceType) && typeVariableMap.containsKey(((UnresolvedTypeVariableReferenceType) parameterizedComponentSig).getTypeVariable().getName())) {
                StringBuffer newsig = new StringBuffer();
                newsig.append("[T");
                newsig.append(((UnresolvedTypeVariableReferenceType) parameterizedComponentSig).getTypeVariable().getName());
                newsig.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
                arrayType = UnresolvedType.forSignature(newsig.toString());
            } else {
                arrayType = ResolvedType.makeArray(parameterizedComponentSig, 1);
            }
            return arrayType;
        }
        return unresolvedType;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean hasBackingGenericMember() {
        return this.backingGenericMember != null;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public ResolvedMember getBackingGenericMember() {
        return this.backingGenericMember;
    }

    public void resetName(String newName) {
        this.name = newName;
    }

    public void resetKind(MemberKind newKind) {
        this.kind = newKind;
    }

    public void resetModifiers(int newModifiers) {
        this.modifiers = newModifiers;
    }

    public void resetReturnTypeToObjectArray() {
        this.returnType = UnresolvedType.OBJECTARRAY;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean matches(ResolvedMember aCandidateMatch, boolean ignoreGenerics) {
        boolean b;
        ResolvedMemberImpl candidateMatchImpl = (ResolvedMemberImpl) aCandidateMatch;
        if (!getName().equals(aCandidateMatch.getName())) {
            return false;
        }
        UnresolvedType[] parameterTypes = getGenericParameterTypes();
        UnresolvedType[] candidateParameterTypes = aCandidateMatch.getGenericParameterTypes();
        if (parameterTypes.length != candidateParameterTypes.length) {
            return false;
        }
        String myParameterSignature = getParameterSigWithBoundsRemoved();
        String candidateParameterSignature = candidateMatchImpl.getParameterSigWithBoundsRemoved();
        if (myParameterSignature.equals(candidateParameterSignature)) {
            b = true;
        } else {
            String myParameterSignature2 = getParameterSignatureErased();
            String candidateParameterSignature2 = candidateMatchImpl.getParameterSignatureErased();
            b = myParameterSignature2.equals(candidateParameterSignature2);
        }
        return b;
    }

    private String getParameterSigWithBoundsRemoved() {
        if (this.myParameterSignatureWithBoundsRemoved != null) {
            return this.myParameterSignatureWithBoundsRemoved;
        }
        StringBuffer sig = new StringBuffer();
        UnresolvedType[] myParameterTypes = getGenericParameterTypes();
        for (UnresolvedType unresolvedType : myParameterTypes) {
            appendSigWithTypeVarBoundsRemoved(unresolvedType, sig, new HashSet());
        }
        this.myParameterSignatureWithBoundsRemoved = sig.toString();
        return this.myParameterSignatureWithBoundsRemoved;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public String getParameterSignatureErased() {
        if (this.myParameterSignatureErasure == null) {
            StringBuilder sig = new StringBuilder();
            for (UnresolvedType parameter : getParameterTypes()) {
                sig.append(parameter.getErasureSignature());
            }
            this.myParameterSignatureErasure = sig.toString();
        }
        return this.myParameterSignatureErasure;
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public String getSignatureErased() {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        sb.append(getParameterSignatureErased());
        sb.append(")");
        sb.append(getReturnType().getErasureSignature());
        return sb.toString();
    }

    public static void appendSigWithTypeVarBoundsRemoved(UnresolvedType aType, StringBuffer toBuffer, Set<UnresolvedType> alreadyUsedTypeVars) {
        if (aType.isTypeVariableReference()) {
            TypeVariableReferenceType typeVariableRT = (TypeVariableReferenceType) aType;
            if (alreadyUsedTypeVars.contains(aType)) {
                toBuffer.append("...");
                return;
            } else {
                alreadyUsedTypeVars.add(aType);
                appendSigWithTypeVarBoundsRemoved(typeVariableRT.getTypeVariable().getFirstBound(), toBuffer, alreadyUsedTypeVars);
                return;
            }
        }
        if (aType.isParameterizedType()) {
            toBuffer.append(aType.getRawType().getSignature());
            toBuffer.append("<");
            for (int i = 0; i < aType.getTypeParameters().length; i++) {
                appendSigWithTypeVarBoundsRemoved(aType.getTypeParameters()[i], toBuffer, alreadyUsedTypeVars);
            }
            toBuffer.append(">;");
            return;
        }
        toBuffer.append(aType.getSignature());
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public String toDebugString() {
        StringBuffer r = new StringBuffer();
        int mods = this.modifiers;
        if ((mods & 4096) > 0) {
            mods -= 4096;
        }
        if ((mods & 512) > 0) {
            mods -= 512;
        }
        if ((mods & 131072) > 0) {
            mods -= 131072;
        }
        String modsStr = Modifier.toString(mods);
        if (modsStr.length() != 0) {
            r.append(modsStr).append("(" + mods + ")").append(SymbolConstants.SPACE_SYMBOL);
        }
        if (this.typeVariables != null && this.typeVariables.length > 0) {
            r.append("<");
            for (int i = 0; i < this.typeVariables.length; i++) {
                if (i > 0) {
                    r.append(",");
                }
                TypeVariable t = this.typeVariables[i];
                r.append(t.toDebugString());
            }
            r.append("> ");
        }
        r.append(getGenericReturnType().toDebugString());
        r.append(' ');
        r.append(this.declaringType.getName());
        r.append('.');
        r.append(this.name);
        if (this.kind != FIELD) {
            r.append("(");
            UnresolvedType[] params = getGenericParameterTypes();
            boolean parameterNamesExist = showParameterNames && this.parameterNames != null && this.parameterNames.length == params.length;
            if (params.length != 0) {
                int len = params.length;
                for (int i2 = 0; i2 < len; i2++) {
                    if (i2 > 0) {
                        r.append(", ");
                    }
                    r.append(params[i2].toDebugString());
                    if (parameterNamesExist) {
                        r.append(SymbolConstants.SPACE_SYMBOL).append(this.parameterNames[i2]);
                    }
                }
            }
            r.append(")");
        }
        return r.toString();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public String toGenericString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getGenericReturnType().getSimpleName());
        buf.append(' ');
        buf.append(this.declaringType.getName());
        buf.append('.');
        buf.append(this.name);
        if (this.kind != FIELD) {
            buf.append("(");
            UnresolvedType[] params = getGenericParameterTypes();
            if (params.length != 0) {
                buf.append(params[0].getSimpleName());
                int len = params.length;
                for (int i = 1; i < len; i++) {
                    buf.append(", ");
                    buf.append(params[i].getSimpleName());
                }
            }
            buf.append(")");
        }
        return buf.toString();
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isCompatibleWith(Member am) {
        if (this.kind != METHOD || am.getKind() != METHOD || !this.name.equals(am.getName()) || !equalTypes(getParameterTypes(), am.getParameterTypes())) {
            return true;
        }
        return getReturnType().equals(am.getReturnType());
    }

    private static boolean equalTypes(UnresolvedType[] a, UnresolvedType[] b) {
        int len = a.length;
        if (len != b.length) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (!a[i].equals(b[i])) {
                return false;
            }
        }
        return true;
    }

    @Override // org.aspectj.weaver.TypeVariableDeclaringElement
    public TypeVariable getTypeVariableNamed(String name) {
        if (this.typeVariables != null) {
            for (int i = 0; i < this.typeVariables.length; i++) {
                if (this.typeVariables[i].getName().equals(name)) {
                    return this.typeVariables[i];
                }
            }
        }
        return this.declaringType.getTypeVariableNamed(name);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public void evictWeavingState() {
    }

    public boolean isEquivalentTo(Object other) {
        return equals(other);
    }

    @Override // org.aspectj.weaver.ResolvedMember
    public boolean isDefaultConstructor() {
        return false;
    }
}
