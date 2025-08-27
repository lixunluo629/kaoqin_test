package org.aspectj.weaver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.weaver.patterns.Pointcut;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/ResolvedPointcutDefinition.class */
public class ResolvedPointcutDefinition extends ResolvedMemberImpl {
    private Pointcut pointcut;
    public static final ResolvedPointcutDefinition DUMMY = new ResolvedPointcutDefinition(UnresolvedType.OBJECT, 0, "missing", UnresolvedType.NONE, Pointcut.makeMatchesNothing(Pointcut.RESOLVED));
    public static final ResolvedPointcutDefinition[] NO_POINTCUTS = new ResolvedPointcutDefinition[0];

    public ResolvedPointcutDefinition(UnresolvedType declaringType, int modifiers, String name, UnresolvedType[] parameterTypes, Pointcut pointcut) {
        this(declaringType, modifiers, name, parameterTypes, UnresolvedType.VOID, pointcut);
    }

    public ResolvedPointcutDefinition(UnresolvedType declaringType, int modifiers, String name, UnresolvedType[] parameterTypes, UnresolvedType returnType, Pointcut pointcut) {
        super(POINTCUT, declaringType, modifiers, returnType, name, parameterTypes);
        this.pointcut = pointcut;
        this.checkedExceptions = UnresolvedType.NONE;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public void write(CompressingDataOutputStream s) throws IOException {
        getDeclaringType().write(s);
        s.writeInt(getModifiers());
        s.writeUTF(getName());
        UnresolvedType.writeArray(getParameterTypes(), s);
        this.pointcut.write(s);
    }

    public static ResolvedPointcutDefinition read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        ResolvedPointcutDefinition rpd = new ResolvedPointcutDefinition(UnresolvedType.read(s), s.readInt(), s.readUTF(), UnresolvedType.readArray(s), Pointcut.read(s, context));
        rpd.setSourceContext(context);
        return rpd;
    }

    @Override // org.aspectj.weaver.MemberImpl
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("pointcut ");
        buf.append(getDeclaringType() == null ? "<nullDeclaringType>" : getDeclaringType().getName());
        buf.append(".");
        buf.append(getName());
        buf.append("(");
        for (int i = 0; i < getParameterTypes().length; i++) {
            if (i > 0) {
                buf.append(", ");
            }
            buf.append(getParameterTypes()[i].toString());
        }
        buf.append(")");
        return buf.toString();
    }

    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public boolean isAjSynthetic() {
        return true;
    }

    @Override // org.aspectj.weaver.ResolvedMemberImpl, org.aspectj.weaver.ResolvedMember
    public ResolvedMemberImpl parameterizedWith(UnresolvedType[] typeParameters, ResolvedType newDeclaringType, boolean isParameterized) {
        TypeVariable[] typeVariables = getDeclaringType().resolve(newDeclaringType.getWorld()).getTypeVariables();
        if (isParameterized && typeVariables.length != typeParameters.length) {
            throw new IllegalStateException("Wrong number of type parameters supplied");
        }
        Map typeMap = new HashMap();
        boolean typeParametersSupplied = typeParameters != null && typeParameters.length > 0;
        if (typeVariables != null) {
            for (int i = 0; i < typeVariables.length; i++) {
                UnresolvedType ut = !typeParametersSupplied ? typeVariables[i].getFirstBound() : typeParameters[i];
                typeMap.put(typeVariables[i].getName(), ut);
            }
        }
        UnresolvedType parameterizedReturnType = parameterize(getGenericReturnType(), typeMap, isParameterized, newDeclaringType.getWorld());
        UnresolvedType[] parameterizedParameterTypes = new UnresolvedType[getGenericParameterTypes().length];
        for (int i2 = 0; i2 < parameterizedParameterTypes.length; i2++) {
            parameterizedParameterTypes[i2] = parameterize(getGenericParameterTypes()[i2], typeMap, isParameterized, newDeclaringType.getWorld());
        }
        ResolvedPointcutDefinition ret = new ResolvedPointcutDefinition(newDeclaringType, getModifiers(), getName(), parameterizedParameterTypes, parameterizedReturnType, this.pointcut.parameterizeWith(typeMap, newDeclaringType.getWorld()));
        ret.setTypeVariables(getTypeVariables());
        ret.setSourceContext(getSourceContext());
        ret.setPosition(getStart(), getEnd());
        ret.setParameterNames(getParameterNames());
        return ret;
    }

    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }
}
