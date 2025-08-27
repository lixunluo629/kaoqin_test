package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/WithincodePointcut.class */
public class WithincodePointcut extends Pointcut {
    private SignaturePattern signature;
    private static final int matchedShadowKinds;

    static {
        int flags = Shadow.ALL_SHADOW_KINDS_BITS;
        for (int i = 0; i < Shadow.SHADOW_KINDS.length; i++) {
            if (Shadow.SHADOW_KINDS[i].isEnclosingKind()) {
                flags -= Shadow.SHADOW_KINDS[i].bit;
            }
        }
        matchedShadowKinds = flags | Shadow.ConstructorExecution.bit | Shadow.Initialization.bit;
    }

    public WithincodePointcut(SignaturePattern signature) {
        this.signature = signature;
        this.pointcutKind = (byte) 12;
    }

    public SignaturePattern getSignature() {
        return this.signature;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return matchedShadowKinds;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map typeVariableMap, World w) {
        WithincodePointcut ret = new WithincodePointcut(this.signature.parameterizeWith((Map<String, UnresolvedType>) typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo type) {
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        return FuzzyBoolean.fromBoolean(this.signature.matches(shadow.getEnclosingCodeSignature(), shadow.getIWorld(), false));
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(12);
        this.signature.write(s);
        writeLocation(s);
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        WithincodePointcut ret = new WithincodePointcut(SignaturePattern.read(s, context));
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
        this.signature = this.signature.resolveBindings(scope, bindings);
        HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
        this.signature.getDeclaringType().traverse(visitor, null);
        if (visitor.wellHasItThen()) {
            scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.WITHINCODE_DOESNT_SUPPORT_PARAMETERIZED_DECLARING_TYPES), getSourceLocation()));
        }
        HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor visitor2 = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
        this.signature.getThrowsPattern().traverse(visitor2, null);
        if (visitor2.wellHasItThen()) {
            scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.NO_GENERIC_THROWABLES), getSourceLocation()));
        }
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void postRead(ResolvedType enclosingType) {
        this.signature.postRead(enclosingType);
    }

    public boolean equals(Object other) {
        if (!(other instanceof WithincodePointcut)) {
            return false;
        }
        WithincodePointcut o = (WithincodePointcut) other;
        return o.signature.equals(this.signature);
    }

    public int hashCode() {
        int result = (37 * 43) + this.signature.hashCode();
        return result;
    }

    public String toString() {
        return "withincode(" + this.signature + ")";
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        return match(shadow).alwaysTrue() ? Literal.TRUE : Literal.FALSE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        Pointcut ret = new WithincodePointcut(this.signature);
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
