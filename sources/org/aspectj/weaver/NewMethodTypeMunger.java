package org.aspectj.weaver;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aspectj.bridge.ISourceLocation;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/NewMethodTypeMunger.class */
public class NewMethodTypeMunger extends ResolvedTypeMunger {
    public NewMethodTypeMunger(ResolvedMember signature, Set superMethodsCalled, List typeVariableAliases) {
        super(Method, signature);
        this.typeVariableAliases = typeVariableAliases;
        setSuperMethodsCalled(superMethodsCalled);
    }

    public ResolvedMember getInterMethodBody(UnresolvedType aspectType) {
        return AjcMemberMaker.interMethodBody(this.signature, aspectType);
    }

    public ResolvedMember getDeclaredInterMethodBody(UnresolvedType aspectType, World w) {
        if (this.declaredSignature != null) {
            ResolvedMember rm = this.declaredSignature.parameterizedWith(null, this.signature.getDeclaringType().resolve(w), false, getTypeVariableAliases());
            return AjcMemberMaker.interMethodBody(rm, aspectType);
        }
        return AjcMemberMaker.interMethodBody(this.signature, aspectType);
    }

    public ResolvedMember getDeclaredInterMethodDispatcher(UnresolvedType aspectType, World w) {
        if (this.declaredSignature != null) {
            ResolvedMember rm = this.declaredSignature.parameterizedWith(null, this.signature.getDeclaringType().resolve(w), false, getTypeVariableAliases());
            return AjcMemberMaker.interMethodDispatcher(rm, aspectType);
        }
        return AjcMemberMaker.interMethodDispatcher(this.signature, aspectType);
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public void write(CompressingDataOutputStream s) throws IOException {
        this.kind.write(s);
        this.signature.write(s);
        writeSuperMethodsCalled(s);
        writeSourceLocation(s);
        writeOutTypeAliases(s);
    }

    public static ResolvedTypeMunger readMethod(VersionedDataInputStream s, ISourceContext context) throws IOException {
        ResolvedMemberImpl rmImpl = ResolvedMemberImpl.readResolvedMember(s, context);
        Set<ResolvedMember> superMethodsCalled = readSuperMethodsCalled(s);
        ISourceLocation sloc = readSourceLocation(s);
        List<String> typeVarAliases = readInTypeAliases(s);
        ResolvedTypeMunger munger = new NewMethodTypeMunger(rmImpl, superMethodsCalled, typeVarAliases);
        if (sloc != null) {
            munger.setSourceLocation(sloc);
        }
        return munger;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public ResolvedMember getMatchingSyntheticMember(Member member, ResolvedType aspectType) {
        ResolvedMember ret = AjcMemberMaker.interMethodDispatcher(getSignature(), aspectType);
        if (ResolvedType.matches(ret, member)) {
            return getSignature();
        }
        return super.getMatchingSyntheticMember(member, aspectType);
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public ResolvedTypeMunger parameterizedFor(ResolvedType target) {
        ResolvedMember parameterizedSignature;
        ResolvedType genericType = target;
        if (target.isRawType() || target.isParameterizedType()) {
            genericType = genericType.getGenericType();
        }
        if (target.isGenericType()) {
            TypeVariable[] vars = target.getTypeVariables();
            UnresolvedTypeVariableReferenceType[] varRefs = new UnresolvedTypeVariableReferenceType[vars.length];
            for (int i = 0; i < vars.length; i++) {
                varRefs[i] = new UnresolvedTypeVariableReferenceType(vars[i]);
            }
            parameterizedSignature = getSignature().parameterizedWith(varRefs, genericType, true, this.typeVariableAliases);
        } else {
            parameterizedSignature = getSignature().parameterizedWith(target.getTypeParameters(), genericType, target.isParameterizedType(), this.typeVariableAliases);
        }
        NewMethodTypeMunger nmtm = new NewMethodTypeMunger(parameterizedSignature, getSuperMethodsCalled(), this.typeVariableAliases);
        nmtm.setDeclaredSignature(getSignature());
        nmtm.setSourceLocation(getSourceLocation());
        return nmtm;
    }

    public boolean equals(Object other) {
        if (!(other instanceof NewMethodTypeMunger)) {
            return false;
        }
        NewMethodTypeMunger o = (NewMethodTypeMunger) other;
        if (this.kind != null ? this.kind.equals(o.kind) : o.kind == null) {
            if (this.signature != null ? this.signature.equals(o.signature) : o.signature == null) {
                if (this.declaredSignature != null ? this.declaredSignature.equals(o.declaredSignature) : o.declaredSignature == null) {
                    if (this.typeVariableAliases != null ? this.typeVariableAliases.equals(o.typeVariableAliases) : o.typeVariableAliases == null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int hashCode() {
        int result = (37 * 17) + this.kind.hashCode();
        return (37 * ((37 * ((37 * result) + (this.signature == null ? 0 : this.signature.hashCode()))) + (this.declaredSignature == null ? 0 : this.declaredSignature.hashCode()))) + (this.typeVariableAliases == null ? 0 : this.typeVariableAliases.hashCode());
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public ResolvedTypeMunger parameterizeWith(Map<String, UnresolvedType> m, World w) {
        ResolvedMember parameterizedSignature = getSignature().parameterizedWith(m, w);
        NewMethodTypeMunger nmtm = new NewMethodTypeMunger(parameterizedSignature, getSuperMethodsCalled(), this.typeVariableAliases);
        nmtm.setDeclaredSignature(getSignature());
        nmtm.setSourceLocation(getSourceLocation());
        return nmtm;
    }
}
