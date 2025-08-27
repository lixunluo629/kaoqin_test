package org.aspectj.weaver;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.aspectj.bridge.ISourceLocation;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/NewFieldTypeMunger.class */
public class NewFieldTypeMunger extends ResolvedTypeMunger {
    public static final int VersionOne = 1;
    public static final int VersionTwo = 2;
    public int version;

    public NewFieldTypeMunger(ResolvedMember signature, Set superMethodsCalled, List typeVariableAliases) {
        super(Field, signature);
        this.version = 1;
        this.version = 2;
        this.typeVariableAliases = typeVariableAliases;
        signature.setAnnotatedElsewhere(true);
        setSuperMethodsCalled(superMethodsCalled);
    }

    public ResolvedMember getInitMethod(UnresolvedType aspectType) {
        return AjcMemberMaker.interFieldInitializer(this.signature, aspectType);
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public void write(CompressingDataOutputStream s) throws IOException {
        this.kind.write(s);
        this.signature.write(s);
        writeSuperMethodsCalled(s);
        writeSourceLocation(s);
        writeOutTypeAliases(s);
        s.writeInt(this.version);
    }

    public static ResolvedTypeMunger readField(VersionedDataInputStream s, ISourceContext context) throws IOException {
        ResolvedMember fieldSignature = ResolvedMemberImpl.readResolvedMember(s, context);
        Set superMethodsCalled = readSuperMethodsCalled(s);
        ISourceLocation sloc = readSourceLocation(s);
        List aliases = readInTypeAliases(s);
        NewFieldTypeMunger munger = new NewFieldTypeMunger(fieldSignature, superMethodsCalled, aliases);
        if (sloc != null) {
            munger.setSourceLocation(sloc);
        }
        if (s.getMajorVersion() >= 7) {
            munger.version = s.readInt();
        } else {
            munger.version = 1;
        }
        return munger;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public ResolvedMember getMatchingSyntheticMember(Member member, ResolvedType aspectType) {
        ResolvedType onType = aspectType.getWorld().resolve(getSignature().getDeclaringType());
        if (onType.isRawType()) {
            onType = onType.getGenericType();
        }
        ResolvedMember ret = AjcMemberMaker.interFieldGetDispatcher(getSignature(), aspectType);
        if (ResolvedType.matches(ret, member)) {
            return getSignature();
        }
        ResolvedMember ret2 = AjcMemberMaker.interFieldSetDispatcher(getSignature(), aspectType);
        if (ResolvedType.matches(ret2, member)) {
            return getSignature();
        }
        ResolvedMember ret3 = AjcMemberMaker.interFieldInterfaceGetter(getSignature(), onType, aspectType);
        if (ResolvedType.matches(ret3, member)) {
            return getSignature();
        }
        ResolvedMember ret4 = AjcMemberMaker.interFieldInterfaceSetter(getSignature(), onType, aspectType);
        if (ResolvedType.matches(ret4, member)) {
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
        NewFieldTypeMunger nftm = new NewFieldTypeMunger(parameterizedSignature, getSuperMethodsCalled(), this.typeVariableAliases);
        nftm.setDeclaredSignature(getSignature());
        nftm.setSourceLocation(getSourceLocation());
        return nftm;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public ResolvedTypeMunger parameterizeWith(Map<String, UnresolvedType> m, World w) {
        ResolvedMember parameterizedSignature = getSignature().parameterizedWith(m, w);
        NewFieldTypeMunger nftm = new NewFieldTypeMunger(parameterizedSignature, getSuperMethodsCalled(), this.typeVariableAliases);
        nftm.setDeclaredSignature(getSignature());
        nftm.setSourceLocation(getSourceLocation());
        return nftm;
    }

    public boolean equals(Object other) {
        if (!(other instanceof NewFieldTypeMunger)) {
            return false;
        }
        NewFieldTypeMunger o = (NewFieldTypeMunger) other;
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
}
