package org.aspectj.weaver;

import java.io.IOException;
import org.aspectj.weaver.patterns.TypePattern;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/MethodDelegateTypeMunger.class */
public class MethodDelegateTypeMunger extends ResolvedTypeMunger {
    private final UnresolvedType aspect;
    private UnresolvedType fieldType;
    private final String implClassName;
    private final TypePattern typePattern;
    private String factoryMethodName;
    private String factoryMethodSignature;
    private int bitflags;
    private static final int REPLACING_EXISTING_METHOD = 1;
    private volatile int hashCode;

    public MethodDelegateTypeMunger(ResolvedMember signature, UnresolvedType aspect, String implClassName, TypePattern typePattern) {
        super(MethodDelegate2, signature);
        this.hashCode = 0;
        this.aspect = aspect;
        this.typePattern = typePattern;
        this.implClassName = implClassName;
        this.factoryMethodName = "";
        this.factoryMethodSignature = "";
    }

    public MethodDelegateTypeMunger(ResolvedMember signature, UnresolvedType aspect, String implClassName, TypePattern typePattern, String factoryMethodName, String factoryMethodSignature) {
        super(MethodDelegate2, signature);
        this.hashCode = 0;
        this.aspect = aspect;
        this.typePattern = typePattern;
        this.implClassName = implClassName;
        this.factoryMethodName = factoryMethodName;
        this.factoryMethodSignature = factoryMethodSignature;
    }

    public boolean equals(Object other) {
        if (!(other instanceof MethodDelegateTypeMunger)) {
            return false;
        }
        MethodDelegateTypeMunger o = (MethodDelegateTypeMunger) other;
        if (o.aspect != null ? this.aspect.equals(o.aspect) : this.aspect == null) {
            if (o.typePattern != null ? this.typePattern.equals(o.typePattern) : this.typePattern == null) {
                if (o.implClassName != null ? this.implClassName.equals(o.implClassName) : this.implClassName == null) {
                    if (o.fieldType != null ? this.fieldType.equals(o.fieldType) : this.fieldType == null) {
                        if (o.factoryMethodName != null ? this.factoryMethodName.equals(o.factoryMethodName) : this.factoryMethodName == null) {
                            if (o.factoryMethodSignature != null ? this.factoryMethodSignature.equals(o.factoryMethodSignature) : this.factoryMethodSignature == null) {
                                if (o.bitflags == this.bitflags) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int result = (37 * 17) + (this.aspect == null ? 0 : this.aspect.hashCode());
            this.hashCode = (37 * ((37 * ((37 * ((37 * ((37 * ((37 * result) + (this.typePattern == null ? 0 : this.typePattern.hashCode()))) + (this.implClassName == null ? 0 : this.implClassName.hashCode()))) + (this.fieldType == null ? 0 : this.fieldType.hashCode()))) + (this.factoryMethodName == null ? 0 : this.factoryMethodName.hashCode()))) + (this.factoryMethodSignature == null ? 0 : this.factoryMethodSignature.hashCode()))) + this.bitflags;
        }
        return this.hashCode;
    }

    public ResolvedMember getDelegate(ResolvedType targetType) {
        return AjcMemberMaker.itdAtDeclareParentsField(targetType, this.fieldType, this.aspect);
    }

    public ResolvedMember getDelegateFactoryMethod(World w) {
        ResolvedType aspectType = w.resolve(this.aspect);
        ResolvedMember[] methods = aspectType.getDeclaredMethods();
        for (ResolvedMember rm : methods) {
            if (rm.getName().equals(this.factoryMethodName) && rm.getSignature().equals(this.factoryMethodSignature)) {
                return rm;
            }
        }
        return null;
    }

    public String getImplClassName() {
        return this.implClassName;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public void write(CompressingDataOutputStream s) throws IOException {
        this.kind.write(s);
        this.signature.write(s);
        this.aspect.write(s);
        s.writeUTF(this.implClassName);
        this.typePattern.write(s);
        this.fieldType.write(s);
        s.writeUTF(this.factoryMethodName);
        s.writeUTF(this.factoryMethodSignature);
        s.writeInt(this.bitflags);
    }

    public static ResolvedTypeMunger readMethod(VersionedDataInputStream s, ISourceContext context, boolean isEnhanced) throws IOException {
        UnresolvedType fieldType;
        ResolvedMemberImpl signature = ResolvedMemberImpl.readResolvedMember(s, context);
        UnresolvedType aspect = UnresolvedType.read(s);
        String implClassName = s.readUTF();
        TypePattern tp = TypePattern.read(s, context);
        MethodDelegateTypeMunger typeMunger = new MethodDelegateTypeMunger(signature, aspect, implClassName, tp);
        if (isEnhanced) {
            fieldType = UnresolvedType.read(s);
        } else {
            fieldType = signature.getDeclaringType();
        }
        typeMunger.setFieldType(fieldType);
        if (isEnhanced) {
            typeMunger.factoryMethodName = s.readUTF();
            typeMunger.factoryMethodSignature = s.readUTF();
            typeMunger.bitflags = s.readInt();
        }
        return typeMunger;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public boolean matches(ResolvedType matchType, ResolvedType aspectType) {
        if (matchType.isEnum() || matchType.isInterface() || matchType.isAnnotation()) {
            return false;
        }
        return this.typePattern.matchesStatically(matchType);
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public boolean changesPublicSignature() {
        return true;
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/MethodDelegateTypeMunger$FieldHostTypeMunger.class */
    public static class FieldHostTypeMunger extends ResolvedTypeMunger {
        private final UnresolvedType aspect;
        private final TypePattern typePattern;

        public FieldHostTypeMunger(ResolvedMember field, UnresolvedType aspect, TypePattern typePattern) {
            super(FieldHost, field);
            this.aspect = aspect;
            this.typePattern = typePattern;
        }

        public boolean equals(Object other) {
            if (!(other instanceof FieldHostTypeMunger)) {
                return false;
            }
            FieldHostTypeMunger o = (FieldHostTypeMunger) other;
            if (o.aspect != null ? this.aspect.equals(o.aspect) : this.aspect == null) {
                if (o.typePattern != null ? this.typePattern.equals(o.typePattern) : this.typePattern == null) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            int result = (37 * 17) + (this.aspect == null ? 0 : this.aspect.hashCode());
            return (37 * result) + (this.typePattern == null ? 0 : this.typePattern.hashCode());
        }

        @Override // org.aspectj.weaver.ResolvedTypeMunger
        public void write(CompressingDataOutputStream s) throws IOException {
            this.kind.write(s);
            this.signature.write(s);
            this.aspect.write(s);
            this.typePattern.write(s);
        }

        public static ResolvedTypeMunger readFieldHost(VersionedDataInputStream s, ISourceContext context) throws IOException {
            ResolvedMemberImpl signature = ResolvedMemberImpl.readResolvedMember(s, context);
            UnresolvedType aspect = UnresolvedType.read(s);
            TypePattern tp = TypePattern.read(s, context);
            return new FieldHostTypeMunger(signature, aspect, tp);
        }

        @Override // org.aspectj.weaver.ResolvedTypeMunger
        public boolean matches(ResolvedType matchType, ResolvedType aspectType) {
            if (matchType.isEnum() || matchType.isInterface() || matchType.isAnnotation()) {
                return false;
            }
            return this.typePattern.matchesStatically(matchType);
        }

        @Override // org.aspectj.weaver.ResolvedTypeMunger
        public boolean changesPublicSignature() {
            return false;
        }

        @Override // org.aspectj.weaver.ResolvedTypeMunger
        public boolean existsToSupportShadowMunging() {
            return true;
        }
    }

    public void setFieldType(UnresolvedType fieldType) {
        this.fieldType = fieldType;
    }

    public boolean specifiesDelegateFactoryMethod() {
        return (this.factoryMethodName == null || this.factoryMethodName.length() == 0) ? false : true;
    }

    public String getFactoryMethodName() {
        return this.factoryMethodName;
    }

    public String getFactoryMethodSignature() {
        return this.factoryMethodSignature;
    }

    public UnresolvedType getAspect() {
        return this.aspect;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public boolean existsToSupportShadowMunging() {
        return true;
    }

    public void tagAsReplacingExistingMethod() {
        this.bitflags |= 1;
    }

    public boolean isReplacingExistingMethod() {
        return (this.bitflags & 1) != 0;
    }
}
