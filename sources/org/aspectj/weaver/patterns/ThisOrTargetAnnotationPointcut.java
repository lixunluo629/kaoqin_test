package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.ognl.OgnlContext;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
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
import org.springframework.validation.DataBinder;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ThisOrTargetAnnotationPointcut.class */
public class ThisOrTargetAnnotationPointcut extends NameBindingPointcut {
    private boolean isThis;
    private boolean alreadyWarnedAboutDEoW;
    private ExactAnnotationTypePattern annotationTypePattern;
    private String declarationText;
    private static final int thisKindSet;
    private static final int targetKindSet;

    static {
        int thisFlags = Shadow.ALL_SHADOW_KINDS_BITS;
        int targFlags = Shadow.ALL_SHADOW_KINDS_BITS;
        for (int i = 0; i < Shadow.SHADOW_KINDS.length; i++) {
            Shadow.Kind kind = Shadow.SHADOW_KINDS[i];
            if (kind.neverHasThis()) {
                thisFlags -= kind.bit;
            }
            if (kind.neverHasTarget()) {
                targFlags -= kind.bit;
            }
        }
        thisKindSet = thisFlags;
        targetKindSet = targFlags;
    }

    public ThisOrTargetAnnotationPointcut(boolean isThis, ExactAnnotationTypePattern type) {
        this.alreadyWarnedAboutDEoW = false;
        this.isThis = isThis;
        this.annotationTypePattern = type;
        this.pointcutKind = (byte) 19;
        buildDeclarationText();
    }

    public ThisOrTargetAnnotationPointcut(boolean isThis, ExactAnnotationTypePattern type, ShadowMunger munger) {
        this(isThis, type);
    }

    public ExactAnnotationTypePattern getAnnotationTypePattern() {
        return this.annotationTypePattern;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return this.isThis ? thisKindSet : targetKindSet;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) throws AbortException {
        ExactAnnotationTypePattern newPattern = (ExactAnnotationTypePattern) this.annotationTypePattern.parameterizeWith(typeVariableMap, w);
        if (newPattern.getAnnotationType() instanceof ResolvedType) {
            verifyRuntimeRetention(newPattern.getResolvedAnnotationType());
        }
        ThisOrTargetAnnotationPointcut ret = new ThisOrTargetAnnotationPointcut(this.isThis, (ExactAnnotationTypePattern) this.annotationTypePattern.parameterizeWith(typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo info) {
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        if (!couldMatch(shadow)) {
            return FuzzyBoolean.NO;
        }
        ResolvedType toMatchAgainst = (this.isThis ? shadow.getThisType() : shadow.getTargetType()).resolve(shadow.getIWorld());
        this.annotationTypePattern.resolve(shadow.getIWorld());
        if (this.annotationTypePattern.matchesRuntimeType(toMatchAgainst).alwaysTrue()) {
            return FuzzyBoolean.YES;
        }
        return FuzzyBoolean.MAYBE;
    }

    public boolean isThis() {
        return this.isThis;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected void resolveBindings(IScope scope, Bindings bindings) throws AbortException {
        if (!scope.getWorld().isInJava5Mode()) {
            scope.message(MessageUtil.error(WeaverMessages.format(this.isThis ? WeaverMessages.ATTHIS_ONLY_SUPPORTED_AT_JAVA5_LEVEL : WeaverMessages.ATTARGET_ONLY_SUPPORTED_AT_JAVA5_LEVEL), getSourceLocation()));
            return;
        }
        this.annotationTypePattern = (ExactAnnotationTypePattern) this.annotationTypePattern.resolveBindings(scope, bindings, true);
        if (this.annotationTypePattern.annotationType == null) {
            return;
        }
        ResolvedType rAnnotationType = (ResolvedType) this.annotationTypePattern.annotationType;
        if (rAnnotationType.isTypeVariableReference()) {
            return;
        }
        verifyRuntimeRetention(rAnnotationType);
    }

    private void verifyRuntimeRetention(ResolvedType rAnnotationType) throws AbortException {
        if (!rAnnotationType.isAnnotationWithRuntimeRetention()) {
            IMessage m = MessageUtil.error(WeaverMessages.format(WeaverMessages.BINDING_NON_RUNTIME_RETENTION_ANNOTATION, rAnnotationType.getName()), getSourceLocation());
            rAnnotationType.getWorld().getMessageHandler().handleMessage(m);
        }
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        if (isDeclare(bindings.getEnclosingAdvice())) {
            if (!this.alreadyWarnedAboutDEoW) {
                inAspect.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.THIS_OR_TARGET_IN_DECLARE, this.isThis ? OgnlContext.THIS_CONTEXT_KEY : DataBinder.DEFAULT_OBJECT_NAME), bindings.getEnclosingAdvice().getSourceLocation(), null);
                this.alreadyWarnedAboutDEoW = true;
            }
            return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
        }
        ExactAnnotationTypePattern newType = (ExactAnnotationTypePattern) this.annotationTypePattern.remapAdviceFormals(bindings);
        ThisOrTargetAnnotationPointcut ret = new ThisOrTargetAnnotationPointcut(this.isThis, newType, bindings.getEnclosingAdvice());
        ret.alreadyWarnedAboutDEoW = this.alreadyWarnedAboutDEoW;
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        if (!couldMatch(shadow)) {
            return Literal.FALSE;
        }
        boolean alwaysMatches = match(shadow).alwaysTrue();
        Var var = this.isThis ? shadow.getThisVar() : shadow.getTargetVar();
        Var annVar = null;
        UnresolvedType annotationType = this.annotationTypePattern.annotationType;
        if (this.annotationTypePattern instanceof BindingAnnotationTypePattern) {
            BindingAnnotationTypePattern btp = (BindingAnnotationTypePattern) this.annotationTypePattern;
            annotationType = btp.annotationType;
            annVar = this.isThis ? shadow.getThisAnnotationVar(annotationType) : shadow.getTargetAnnotationVar(annotationType);
            if (annVar == null) {
                throw new RuntimeException("Impossible!");
            }
            state.set(btp.getFormalIndex(), annVar);
        }
        if (alwaysMatches && annVar == null) {
            return Literal.TRUE;
        }
        ResolvedType rType = annotationType.resolve(shadow.getIWorld());
        return Test.makeHasAnnotation(var, rType);
    }

    private boolean couldMatch(Shadow shadow) {
        return this.isThis ? shadow.hasThis() : shadow.hasTarget();
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
        s.writeByte(19);
        s.writeBoolean(this.isThis);
        this.annotationTypePattern.write(s);
        writeLocation(s);
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        boolean isThis = s.readBoolean();
        AnnotationTypePattern type = AnnotationTypePattern.read(s, context);
        ThisOrTargetAnnotationPointcut ret = new ThisOrTargetAnnotationPointcut(isThis, (ExactAnnotationTypePattern) type);
        ret.readLocation(context, s);
        return ret;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ThisOrTargetAnnotationPointcut)) {
            return false;
        }
        ThisOrTargetAnnotationPointcut other = (ThisOrTargetAnnotationPointcut) obj;
        return other.annotationTypePattern.equals(this.annotationTypePattern) && other.isThis == this.isThis;
    }

    public int hashCode() {
        return 17 + (37 * this.annotationTypePattern.hashCode()) + (this.isThis ? 49 : 13);
    }

    private void buildDeclarationText() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.isThis ? "@this(" : "@target(");
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
