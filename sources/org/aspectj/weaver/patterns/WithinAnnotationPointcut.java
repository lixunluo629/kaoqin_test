package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
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

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/WithinAnnotationPointcut.class */
public class WithinAnnotationPointcut extends NameBindingPointcut {
    private AnnotationTypePattern annotationTypePattern;
    private String declarationText;

    public WithinAnnotationPointcut(AnnotationTypePattern type) {
        this.annotationTypePattern = type;
        this.pointcutKind = (byte) 17;
        buildDeclarationText();
    }

    public WithinAnnotationPointcut(AnnotationTypePattern type, ShadowMunger munger) {
        this(type);
        this.pointcutKind = (byte) 17;
    }

    public AnnotationTypePattern getAnnotationTypePattern() {
        return this.annotationTypePattern;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return Shadow.ALL_SHADOW_KINDS_BITS;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        WithinAnnotationPointcut ret = new WithinAnnotationPointcut(this.annotationTypePattern.parameterizeWith(typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo info) {
        return this.annotationTypePattern.fastMatches(info.getType());
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        ResolvedType enclosingType = shadow.getIWorld().resolve(shadow.getEnclosingType(), true);
        if (enclosingType.isMissing()) {
            shadow.getIWorld().getLint().cantFindType.signal(new String[]{WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE_WITHINPCD, shadow.getEnclosingType().getName())}, shadow.getSourceLocation(), new ISourceLocation[]{getSourceLocation()});
        }
        this.annotationTypePattern.resolve(shadow.getIWorld());
        return this.annotationTypePattern.matches(enclosingType);
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected void resolveBindings(IScope scope, Bindings bindings) {
        if (!scope.getWorld().isInJava5Mode()) {
            scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.ATWITHIN_ONLY_SUPPORTED_AT_JAVA5_LEVEL), getSourceLocation()));
        } else {
            this.annotationTypePattern = this.annotationTypePattern.resolveBindings(scope, bindings, true);
        }
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        ExactAnnotationTypePattern newType = (ExactAnnotationTypePattern) this.annotationTypePattern.remapAdviceFormals(bindings);
        Pointcut ret = new WithinAnnotationPointcut(newType, bindings.getEnclosingAdvice());
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        if (this.annotationTypePattern instanceof BindingAnnotationTypePattern) {
            BindingAnnotationTypePattern btp = (BindingAnnotationTypePattern) this.annotationTypePattern;
            UnresolvedType annotationType = btp.annotationType;
            Var var = shadow.getWithinAnnotationVar(annotationType);
            if (var == null) {
                throw new BCException("Impossible! annotation=[" + annotationType + "]  shadow=[" + shadow + " at " + shadow.getSourceLocation() + "]    pointcut is at [" + getSourceLocation() + "]");
            }
            state.set(btp.getFormalIndex(), var);
        }
        return match(shadow).alwaysTrue() ? Literal.TRUE : Literal.FALSE;
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
        s.writeByte(17);
        this.annotationTypePattern.write(s);
        writeLocation(s);
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        AnnotationTypePattern type = AnnotationTypePattern.read(s, context);
        WithinAnnotationPointcut ret = new WithinAnnotationPointcut(type);
        ret.readLocation(context, s);
        return ret;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WithinAnnotationPointcut)) {
            return false;
        }
        WithinAnnotationPointcut other = (WithinAnnotationPointcut) obj;
        return other.annotationTypePattern.equals(this.annotationTypePattern);
    }

    public int hashCode() {
        return 17 + (19 * this.annotationTypePattern.hashCode());
    }

    private void buildDeclarationText() {
        StringBuffer buf = new StringBuffer();
        buf.append("@within(");
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
