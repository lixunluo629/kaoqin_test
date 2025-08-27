package org.aspectj.weaver;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.ISourceLocation;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/NewConstructorTypeMunger.class */
public class NewConstructorTypeMunger extends ResolvedTypeMunger {
    private ResolvedMember syntheticConstructor;
    private ResolvedMember explicitConstructor;
    private volatile int hashCode;

    public NewConstructorTypeMunger(ResolvedMember signature, ResolvedMember syntheticConstructor, ResolvedMember explicitConstructor, Set superMethodsCalled, List typeVariableAliases) {
        super(Constructor, signature);
        this.hashCode = 0;
        this.syntheticConstructor = syntheticConstructor;
        this.typeVariableAliases = typeVariableAliases;
        this.explicitConstructor = explicitConstructor;
        setSuperMethodsCalled(superMethodsCalled);
    }

    public boolean equals(Object other) {
        boolean zEquals;
        boolean zEquals2;
        if (!(other instanceof NewConstructorTypeMunger)) {
            return false;
        }
        NewConstructorTypeMunger o = (NewConstructorTypeMunger) other;
        if (this.syntheticConstructor == null) {
            zEquals = o.syntheticConstructor == null;
        } else {
            zEquals = this.syntheticConstructor.equals(o.syntheticConstructor);
        }
        if (this.explicitConstructor == null) {
            zEquals2 = o.explicitConstructor == null;
        } else {
            zEquals2 = this.explicitConstructor.equals(o.explicitConstructor);
        }
        return zEquals & zEquals2;
    }

    public boolean equivalentTo(Object other) {
        if (!(other instanceof NewConstructorTypeMunger)) {
            return false;
        }
        NewConstructorTypeMunger o = (NewConstructorTypeMunger) other;
        if (this.syntheticConstructor == null) {
            return o.syntheticConstructor == null;
        }
        return this.syntheticConstructor.equals(o.syntheticConstructor);
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int result = (37 * 17) + (this.syntheticConstructor == null ? 0 : this.syntheticConstructor.hashCode());
            this.hashCode = (37 * result) + (this.explicitConstructor == null ? 0 : this.explicitConstructor.hashCode());
        }
        return this.hashCode;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public void write(CompressingDataOutputStream s) throws IOException {
        this.kind.write(s);
        this.signature.write(s);
        this.syntheticConstructor.write(s);
        this.explicitConstructor.write(s);
        writeSuperMethodsCalled(s);
        writeSourceLocation(s);
        writeOutTypeAliases(s);
    }

    public static ResolvedTypeMunger readConstructor(VersionedDataInputStream s, ISourceContext context) throws IOException {
        ResolvedMember sig = ResolvedMemberImpl.readResolvedMember(s, context);
        ResolvedMember syntheticCtor = ResolvedMemberImpl.readResolvedMember(s, context);
        ResolvedMember explicitCtor = ResolvedMemberImpl.readResolvedMember(s, context);
        Set superMethodsCalled = readSuperMethodsCalled(s);
        ISourceLocation sloc = readSourceLocation(s);
        List typeVarAliases = readInTypeAliases(s);
        ResolvedTypeMunger munger = new NewConstructorTypeMunger(sig, syntheticCtor, explicitCtor, superMethodsCalled, typeVarAliases);
        if (sloc != null) {
            munger.setSourceLocation(sloc);
        }
        return munger;
    }

    public ResolvedMember getExplicitConstructor() {
        return this.explicitConstructor;
    }

    public ResolvedMember getSyntheticConstructor() {
        return this.syntheticConstructor;
    }

    public void setExplicitConstructor(ResolvedMember explicitConstructor) {
        this.explicitConstructor = explicitConstructor;
        this.hashCode = 0;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public ResolvedMember getMatchingSyntheticMember(Member member, ResolvedType aspectType) {
        ResolvedMember ret = getSyntheticConstructor();
        if (ResolvedType.matches(ret, member)) {
            return getSignature();
        }
        return super.getMatchingSyntheticMember(member, aspectType);
    }

    public void check(World world) {
        if (getSignature().getDeclaringType().resolve(world).isAspect()) {
            world.showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.ITD_CONS_ON_ASPECT), getSignature().getSourceLocation(), null);
        }
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
        NewConstructorTypeMunger nctm = new NewConstructorTypeMunger(parameterizedSignature, this.syntheticConstructor, this.explicitConstructor, getSuperMethodsCalled(), this.typeVariableAliases);
        nctm.setSourceLocation(getSourceLocation());
        return nctm;
    }
}
