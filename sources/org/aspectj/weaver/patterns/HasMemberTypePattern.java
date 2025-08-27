package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.IMessage;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/HasMemberTypePattern.class */
public class HasMemberTypePattern extends TypePattern {
    private SignaturePattern signaturePattern;
    private static final String declareAtPrefix = "ajc$declare_at";

    public HasMemberTypePattern(SignaturePattern aSignaturePattern) {
        super(false, false);
        this.signaturePattern = aSignaturePattern;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type) {
        if (this.signaturePattern.getKind() == Member.FIELD) {
            return hasField(type);
        }
        return hasMethod(type);
    }

    public ISignaturePattern getSignaturePattern() {
        return this.signaturePattern;
    }

    private boolean hasField(ResolvedType type) {
        World world = type.getWorld();
        Iterator iter = type.getFields();
        while (iter.hasNext()) {
            ResolvedMember field = iter.next();
            if (!field.getName().startsWith(declareAtPrefix) && this.signaturePattern.matches(field, type.getWorld(), false) && (field.getDeclaringType().resolve(world) == type || !Modifier.isPrivate(field.getModifiers()))) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasMethod(ResolvedType type) {
        World world = type.getWorld();
        Iterator<ResolvedMember> iter = type.getMethods(true, true);
        while (iter.hasNext()) {
            Member method = iter.next();
            if (!method.getName().startsWith(declareAtPrefix) && this.signaturePattern.matches(method, type.getWorld(), false) && (method.getDeclaringType().resolve(world) == type || !Modifier.isPrivate(method.getModifiers()))) {
                return true;
            }
        }
        List<ConcreteTypeMunger> mungers = type.getInterTypeMungersIncludingSupers();
        for (ConcreteTypeMunger munger : mungers) {
            Member member = munger.getSignature();
            if (this.signaturePattern.matches(member, type.getWorld(), false) && Modifier.isPublic(member.getModifiers())) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type, ResolvedType annotatedType) {
        return matchesExactly(type);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public FuzzyBoolean matchesInstanceof(ResolvedType type) {
        throw new UnsupportedOperationException("hasmethod/field do not support instanceof matching");
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern parameterizeWith(Map typeVariableMap, World w) {
        HasMemberTypePattern ret = new HasMemberTypePattern(this.signaturePattern.parameterizeWith((Map<String, UnresolvedType>) typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern resolveBindings(IScope scope, Bindings bindings, boolean allowBinding, boolean requireExactType) {
        if (!scope.getWorld().isHasMemberSupportEnabled()) {
            String msg = WeaverMessages.format(WeaverMessages.HAS_MEMBER_NOT_ENABLED, toString());
            scope.message(IMessage.ERROR, this, msg);
        }
        this.signaturePattern.resolveBindings(scope, bindings);
        return this;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof HasMemberTypePattern)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return this.signaturePattern.equals(((HasMemberTypePattern) obj).signaturePattern);
    }

    public int hashCode() {
        return this.signaturePattern.hashCode();
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        if (this.signaturePattern.getKind() == Member.FIELD) {
            buff.append("hasfield(");
        } else {
            buff.append("hasmethod(");
        }
        buff.append(this.signaturePattern.toString());
        buff.append(")");
        return buff.toString();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(11);
        this.signaturePattern.write(s);
        writeLocation(s);
    }

    public static TypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        SignaturePattern sp = SignaturePattern.read(s, context);
        HasMemberTypePattern ret = new HasMemberTypePattern(sp);
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
