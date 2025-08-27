package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.ShadowMunger;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Literal;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.ast.Var;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/WithinCodeAnnotationPointcut.class */
public class WithinCodeAnnotationPointcut extends NameBindingPointcut {
    private ExactAnnotationTypePattern annotationTypePattern;
    private String declarationText;
    private static final int matchedShadowKinds;

    static {
        int flags = Shadow.ALL_SHADOW_KINDS_BITS;
        for (int i = 0; i < Shadow.SHADOW_KINDS.length; i++) {
            if (Shadow.SHADOW_KINDS[i].isEnclosingKind()) {
                flags -= Shadow.SHADOW_KINDS[i].bit;
            }
        }
        matchedShadowKinds = flags;
    }

    public WithinCodeAnnotationPointcut(ExactAnnotationTypePattern type) {
        this.annotationTypePattern = type;
        this.pointcutKind = (byte) 18;
        buildDeclarationText();
    }

    public WithinCodeAnnotationPointcut(ExactAnnotationTypePattern type, ShadowMunger munger) {
        this(type);
        this.pointcutKind = (byte) 18;
    }

    public ExactAnnotationTypePattern getAnnotationTypePattern() {
        return this.annotationTypePattern;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return matchedShadowKinds;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        WithinCodeAnnotationPointcut ret = new WithinCodeAnnotationPointcut((ExactAnnotationTypePattern) this.annotationTypePattern.parameterizeWith(typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo info) {
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        Member member = shadow.getEnclosingCodeSignature();
        ResolvedMember rMember = member.resolve(shadow.getIWorld());
        if (rMember == null) {
            if (member.getName().startsWith(NameMangler.PREFIX)) {
                return FuzzyBoolean.NO;
            }
            shadow.getIWorld().getLint().unresolvableMember.signal(member.toString(), getSourceLocation());
            return FuzzyBoolean.NO;
        }
        this.annotationTypePattern.resolve(shadow.getIWorld());
        return this.annotationTypePattern.matches(rMember);
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected void resolveBindings(IScope scope, Bindings bindings) {
        if (!scope.getWorld().isInJava5Mode()) {
            scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.ATWITHINCODE_ONLY_SUPPORTED_AT_JAVA5_LEVEL), getSourceLocation()));
        } else {
            this.annotationTypePattern = (ExactAnnotationTypePattern) this.annotationTypePattern.resolveBindings(scope, bindings, true);
        }
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        ExactAnnotationTypePattern newType = (ExactAnnotationTypePattern) this.annotationTypePattern.remapAdviceFormals(bindings);
        Pointcut ret = new WithinCodeAnnotationPointcut(newType, bindings.getEnclosingAdvice());
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        if (this.annotationTypePattern instanceof BindingAnnotationTypePattern) {
            BindingAnnotationTypePattern btp = (BindingAnnotationTypePattern) this.annotationTypePattern;
            UnresolvedType annotationType = btp.annotationType;
            Var var = shadow.getWithinCodeAnnotationVar(annotationType);
            if (var == null) {
                throw new BCException("Impossible! annotation=[" + annotationType + "]  shadow=[" + shadow + " at " + shadow.getSourceLocation() + "]    pointcut is at [" + getSourceLocation() + "]");
            }
            state.set(btp.getFormalIndex(), var);
        }
        if (matchInternal(shadow).alwaysTrue()) {
            return Literal.TRUE;
        }
        return Literal.FALSE;
    }

    @Override // org.aspectj.weaver.patterns.NameBindingPointcut
    public List<BindingPattern> getBindingAnnotationTypePatterns() {
        if (this.annotationTypePattern instanceof BindingAnnotationTypePattern) {
            List<BindingPattern> l = new ArrayList<>();
            l.add((BindingPattern) this.annotationTypePattern);
            return l;
        }
        return Collections.emptyList();
    }

    @Override // org.aspectj.weaver.patterns.NameBindingPointcut
    public List<BindingTypePattern> getBindingTypePatterns() {
        return Collections.emptyList();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(18);
        this.annotationTypePattern.write(s);
        writeLocation(s);
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        AnnotationTypePattern type = AnnotationTypePattern.read(s, context);
        WithinCodeAnnotationPointcut ret = new WithinCodeAnnotationPointcut((ExactAnnotationTypePattern) type);
        ret.readLocation(context, s);
        return ret;
    }

    public boolean equals(Object other) {
        if (!(other instanceof WithinCodeAnnotationPointcut)) {
            return false;
        }
        WithinCodeAnnotationPointcut o = (WithinCodeAnnotationPointcut) other;
        return o.annotationTypePattern.equals(this.annotationTypePattern);
    }

    public int hashCode() {
        int result = (23 * 17) + this.annotationTypePattern.hashCode();
        return result;
    }

    private void buildDeclarationText() {
        StringBuffer buf = new StringBuffer();
        buf.append("@withincode(");
        String annPatt = this.annotationTypePattern.toString();
        buf.append(annPatt.startsWith("@") ? annPatt.substring(1) : annPatt);
        buf.append(")");
        this.declarationText = buf.toString();
    }

    public String toString() {
        return this.declarationText;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
