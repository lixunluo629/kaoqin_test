package org.aspectj.weaver;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/MemberImpl.class */
public class MemberImpl implements Member {
    protected MemberKind kind;
    protected int modifiers;
    protected String name;
    protected UnresolvedType declaringType;
    protected UnresolvedType returnType;
    protected UnresolvedType[] parameterTypes;
    private final String erasedSignature;
    private String paramSignature;
    private boolean reportedCantFindDeclaringType = false;
    private boolean reportedUnresolvableMember = false;
    private JoinPointSignatureIterator joinPointSignatures = null;
    private volatile int hashCode = 0;

    public MemberImpl(MemberKind kind, UnresolvedType declaringType, int modifiers, String name, String erasedSignature) {
        this.kind = kind;
        this.declaringType = declaringType;
        this.modifiers = modifiers;
        this.name = name;
        this.erasedSignature = erasedSignature;
        if (kind == FIELD) {
            this.returnType = UnresolvedType.forSignature(erasedSignature);
            this.parameterTypes = UnresolvedType.NONE;
        } else {
            Object[] returnAndParams = signatureToTypes(erasedSignature);
            this.returnType = (UnresolvedType) returnAndParams[0];
            this.parameterTypes = (UnresolvedType[]) returnAndParams[1];
        }
    }

    public MemberImpl(MemberKind kind, UnresolvedType declaringType, int modifiers, UnresolvedType returnType, String name, UnresolvedType[] parameterTypes) {
        this.kind = kind;
        this.declaringType = declaringType;
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.name = name;
        this.parameterTypes = parameterTypes;
        if (kind == FIELD) {
            this.erasedSignature = returnType.getErasureSignature();
        } else {
            this.erasedSignature = typesToSignature(returnType, parameterTypes, true);
        }
    }

    @Override // org.aspectj.weaver.Member
    public ResolvedMember resolve(World world) {
        return world.resolve(this);
    }

    public static String typesToSignature(UnresolvedType returnType, UnresolvedType[] paramTypes, boolean eraseGenerics) {
        StringBuilder buf = new StringBuilder();
        buf.append("(");
        for (UnresolvedType paramType : paramTypes) {
            if (eraseGenerics) {
                buf.append(paramType.getErasureSignature());
            } else {
                buf.append(paramType.getSignature());
            }
        }
        buf.append(")");
        if (eraseGenerics) {
            buf.append(returnType.getErasureSignature());
        } else {
            buf.append(returnType.getSignature());
        }
        return buf.toString();
    }

    public static String typesToSignature(UnresolvedType[] paramTypes) {
        StringBuffer buf = new StringBuffer();
        buf.append("(");
        for (UnresolvedType unresolvedType : paramTypes) {
            buf.append(unresolvedType.getSignature());
        }
        buf.append(")");
        return buf.toString();
    }

    private static Object[] signatureToTypes(String sig) {
        boolean hasParameters = sig.charAt(1) != ')';
        if (hasParameters) {
            List<UnresolvedType> l = new ArrayList<>();
            int i = 1;
            boolean hasAnyAnglies = sig.indexOf(60) != -1;
            while (true) {
                char c = sig.charAt(i);
                if (c != ')') {
                    int start = i;
                    while (c == '[') {
                        i++;
                        c = sig.charAt(i);
                    }
                    if (c == 'L' || c == 'P') {
                        int nextSemicolon = sig.indexOf(59, start);
                        int firstAngly = hasAnyAnglies ? sig.indexOf(60, start) : -1;
                        if (!hasAnyAnglies || firstAngly == -1 || firstAngly > nextSemicolon) {
                            i = nextSemicolon + 1;
                            l.add(UnresolvedType.forSignature(sig.substring(start, i)));
                        } else {
                            boolean endOfSigReached = false;
                            int posn = firstAngly;
                            int genericDepth = 0;
                            while (!endOfSigReached) {
                                switch (sig.charAt(posn)) {
                                    case ';':
                                        if (genericDepth != 0) {
                                            break;
                                        } else {
                                            endOfSigReached = true;
                                            break;
                                        }
                                    case '<':
                                        genericDepth++;
                                        break;
                                    case '>':
                                        genericDepth--;
                                        break;
                                }
                                posn++;
                            }
                            i = posn;
                            l.add(UnresolvedType.forSignature(sig.substring(start, i)));
                        }
                    } else if (c == 'T') {
                        int nextSemicolon2 = sig.indexOf(59, start);
                        String nextbit = sig.substring(start, nextSemicolon2 + 1);
                        l.add(UnresolvedType.forSignature(nextbit));
                        i = nextSemicolon2 + 1;
                    } else {
                        i++;
                        l.add(UnresolvedType.forSignature(sig.substring(start, i)));
                    }
                } else {
                    UnresolvedType[] paramTypes = (UnresolvedType[]) l.toArray(new UnresolvedType[l.size()]);
                    UnresolvedType returnType = UnresolvedType.forSignature(sig.substring(i + 1, sig.length()));
                    return new Object[]{returnType, paramTypes};
                }
            }
        } else {
            UnresolvedType returnType2 = UnresolvedType.forSignature(sig.substring(2));
            return new Object[]{returnType2, UnresolvedType.NONE};
        }
    }

    public static MemberImpl field(String declaring, int mods, String name, String signature) {
        return field(declaring, mods, UnresolvedType.forSignature(signature), name);
    }

    public static MemberImpl method(UnresolvedType declaring, int mods, String name, String signature) {
        Object[] pair = signatureToTypes(signature);
        return method(declaring, mods, (UnresolvedType) pair[0], name, (UnresolvedType[]) pair[1]);
    }

    public static MemberImpl monitorEnter() {
        return new MemberImpl(MONITORENTER, UnresolvedType.OBJECT, 8, UnresolvedType.VOID, "<lock>", UnresolvedType.ARRAY_WITH_JUST_OBJECT);
    }

    public static MemberImpl monitorExit() {
        return new MemberImpl(MONITOREXIT, UnresolvedType.OBJECT, 8, UnresolvedType.VOID, "<unlock>", UnresolvedType.ARRAY_WITH_JUST_OBJECT);
    }

    public static Member pointcut(UnresolvedType declaring, String name, String signature) {
        Object[] pair = signatureToTypes(signature);
        return pointcut(declaring, 0, (UnresolvedType) pair[0], name, (UnresolvedType[]) pair[1]);
    }

    private static MemberImpl field(String declaring, int mods, UnresolvedType ty, String name) {
        return new MemberImpl(FIELD, UnresolvedType.forName(declaring), mods, ty, name, UnresolvedType.NONE);
    }

    public static MemberImpl method(UnresolvedType declTy, int mods, UnresolvedType rTy, String name, UnresolvedType[] paramTys) {
        return new MemberImpl(name.equals("<init>") ? CONSTRUCTOR : METHOD, declTy, mods, rTy, name, paramTys);
    }

    private static Member pointcut(UnresolvedType declTy, int mods, UnresolvedType rTy, String name, UnresolvedType[] paramTys) {
        return new MemberImpl(POINTCUT, declTy, mods, rTy, name, paramTys);
    }

    public static ResolvedMemberImpl makeExceptionHandlerSignature(UnresolvedType inType, UnresolvedType catchType) {
        return new ResolvedMemberImpl(HANDLER, inType, 8, "<catch>", "(" + catchType.getSignature() + ")V");
    }

    public final boolean equals(Object other) {
        if (!(other instanceof Member)) {
            return false;
        }
        Member o = (Member) other;
        return getKind() == o.getKind() && getName().equals(o.getName()) && getSignature().equals(o.getSignature()) && getDeclaringType().equals(o.getDeclaringType());
    }

    public final boolean equalsApartFromDeclaringType(Object other) {
        if (!(other instanceof Member)) {
            return false;
        }
        Member o = (Member) other;
        return getKind() == o.getKind() && getName().equals(o.getName()) && getSignature().equals(o.getSignature());
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int result = (37 * 17) + getKind().hashCode();
            this.hashCode = (37 * ((37 * ((37 * result) + getName().hashCode())) + getSignature().hashCode())) + getDeclaringType().hashCode();
        }
        return this.hashCode;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.lang.Comparable
    public int compareTo(Member other) {
        int i = getName().compareTo(other.getName());
        if (i == 0) {
            return getSignature().compareTo(other.getSignature());
        }
        return i;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.returnType.getName());
        buf.append(' ');
        if (this.declaringType == null) {
            buf.append("<NULL>");
        } else {
            buf.append(this.declaringType.getName());
        }
        buf.append('.');
        buf.append(this.name);
        if (this.kind != FIELD) {
            buf.append("(");
            if (this.parameterTypes.length != 0) {
                buf.append(this.parameterTypes[0]);
                int len = this.parameterTypes.length;
                for (int i = 1; i < len; i++) {
                    buf.append(", ");
                    buf.append(this.parameterTypes[i].getName());
                }
            }
            buf.append(")");
        }
        return buf.toString();
    }

    @Override // org.aspectj.weaver.Member
    public MemberKind getKind() {
        return this.kind;
    }

    @Override // org.aspectj.weaver.Member
    public UnresolvedType getDeclaringType() {
        return this.declaringType;
    }

    @Override // org.aspectj.weaver.Member
    public UnresolvedType getReturnType() {
        return this.returnType;
    }

    @Override // org.aspectj.weaver.Member
    public UnresolvedType getGenericReturnType() {
        return getReturnType();
    }

    @Override // org.aspectj.weaver.Member
    public UnresolvedType[] getGenericParameterTypes() {
        return getParameterTypes();
    }

    @Override // org.aspectj.weaver.Member
    public final UnresolvedType getType() {
        return this.returnType;
    }

    @Override // org.aspectj.weaver.Member
    public String getName() {
        return this.name;
    }

    @Override // org.aspectj.weaver.Member
    public UnresolvedType[] getParameterTypes() {
        return this.parameterTypes;
    }

    @Override // org.aspectj.weaver.Member
    public String getSignature() {
        return this.erasedSignature;
    }

    @Override // org.aspectj.weaver.Member
    public int getArity() {
        return this.parameterTypes.length;
    }

    @Override // org.aspectj.weaver.Member
    public String getParameterSignature() {
        if (this.paramSignature == null) {
            StringBuilder sb = new StringBuilder("(");
            for (UnresolvedType parameterType : this.parameterTypes) {
                sb.append(parameterType.getSignature());
            }
            this.paramSignature = sb.append(")").toString();
        }
        return this.paramSignature;
    }

    @Override // org.aspectj.weaver.Member
    public int getModifiers(World world) {
        ResolvedMember resolved = resolve(world);
        if (resolved == null) {
            reportDidntFindMember(world);
            return 0;
        }
        return resolved.getModifiers();
    }

    @Override // org.aspectj.weaver.Member
    public UnresolvedType[] getExceptions(World world) {
        ResolvedMember resolved = resolve(world);
        if (resolved == null) {
            reportDidntFindMember(world);
            return UnresolvedType.NONE;
        }
        return resolved.getExceptions();
    }

    public final boolean isStatic() {
        return Modifier.isStatic(this.modifiers);
    }

    public final boolean isInterface() {
        return Modifier.isInterface(this.modifiers);
    }

    public final boolean isPrivate() {
        return Modifier.isPrivate(this.modifiers);
    }

    @Override // org.aspectj.weaver.Member
    public boolean canBeParameterized() {
        return false;
    }

    @Override // org.aspectj.weaver.Member
    public int getModifiers() {
        return this.modifiers;
    }

    @Override // org.aspectj.weaver.Member
    public AnnotationAJ[] getAnnotations() {
        throw new UnsupportedOperationException("You should resolve this member '" + this + "' and call getAnnotations() on the result...");
    }

    @Override // org.aspectj.weaver.Member
    public Collection<ResolvedType> getDeclaringTypes(World world) {
        ResolvedType myType = getDeclaringType().resolve(world);
        Collection<ResolvedType> ret = new HashSet<>();
        if (this.kind == CONSTRUCTOR) {
            ret.add(myType);
        } else if (Modifier.isStatic(this.modifiers) || this.kind == FIELD) {
            walkUpStatic(ret, myType);
        } else {
            walkUp(ret, myType);
        }
        return ret;
    }

    private boolean walkUp(Collection<ResolvedType> acc, ResolvedType curr) {
        if (acc.contains(curr)) {
            return true;
        }
        boolean b = false;
        Iterator<ResolvedType> i = curr.getDirectSupertypes();
        while (i.hasNext()) {
            b |= walkUp(acc, i.next());
        }
        if (!b && curr.isParameterizedType()) {
            b = walkUp(acc, curr.getGenericType());
        }
        if (!b) {
            b = curr.lookupMemberNoSupers(this) != null;
        }
        if (b) {
            acc.add(curr);
        }
        return b;
    }

    private boolean walkUpStatic(Collection<ResolvedType> acc, ResolvedType curr) {
        if (curr.lookupMemberNoSupers(this) != null) {
            acc.add(curr);
            return true;
        }
        boolean b = false;
        Iterator<ResolvedType> i = curr.getDirectSupertypes();
        while (i.hasNext()) {
            b |= walkUpStatic(acc, i.next());
        }
        if (!b && curr.isParameterizedType()) {
            b = walkUpStatic(acc, curr.getGenericType());
        }
        if (b) {
            acc.add(curr);
        }
        return b;
    }

    @Override // org.aspectj.weaver.Member
    public String[] getParameterNames(World world) {
        ResolvedMember resolved = resolve(world);
        if (resolved == null) {
            reportDidntFindMember(world);
            return null;
        }
        return resolved.getParameterNames();
    }

    @Override // org.aspectj.weaver.Member
    public JoinPointSignatureIterator getJoinPointSignatures(World inAWorld) {
        if (this.joinPointSignatures == null) {
            this.joinPointSignatures = new JoinPointSignatureIterator(this, inAWorld);
        }
        this.joinPointSignatures.reset();
        return this.joinPointSignatures;
    }

    private void reportDidntFindMember(World world) {
        if (this.reportedCantFindDeclaringType || this.reportedUnresolvableMember) {
            return;
        }
        ResolvedType rType = getDeclaringType().resolve(world);
        if (rType.isMissing()) {
            world.getLint().cantFindType.signal(WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE, rType.getName()), null);
            this.reportedCantFindDeclaringType = true;
        } else {
            world.getLint().unresolvableMember.signal(getName(), null);
            this.reportedUnresolvableMember = true;
        }
    }

    public void wipeJoinpointSignatures() {
        this.joinPointSignatures = null;
    }
}
