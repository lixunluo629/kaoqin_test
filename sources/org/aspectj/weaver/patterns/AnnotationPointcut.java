package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.AjcMemberMaker;
import org.aspectj.weaver.AnnotatedElement;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.NewFieldTypeMunger;
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

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/AnnotationPointcut.class */
public class AnnotationPointcut extends NameBindingPointcut {
    private ExactAnnotationTypePattern annotationTypePattern;
    private String declarationText;

    public AnnotationPointcut(ExactAnnotationTypePattern type) {
        this.annotationTypePattern = type;
        this.pointcutKind = (byte) 16;
        buildDeclarationText();
    }

    public AnnotationPointcut(ExactAnnotationTypePattern type, ShadowMunger munger) {
        this(type);
        buildDeclarationText();
    }

    public ExactAnnotationTypePattern getAnnotationTypePattern() {
        return this.annotationTypePattern;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return Shadow.ALL_SHADOW_KINDS_BITS;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map typeVariableMap, World w) {
        AnnotationPointcut ret = new AnnotationPointcut((ExactAnnotationTypePattern) this.annotationTypePattern.parameterizeWith(typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo info) {
        if (info.getKind() == Shadow.StaticInitialization) {
            return this.annotationTypePattern.fastMatches(info.getType());
        }
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        AnnotatedElement toMatchAgainst;
        Member member = shadow.getSignature();
        ResolvedMember rMember = member.resolve(shadow.getIWorld());
        if (rMember == null) {
            if (member.getName().startsWith(NameMangler.PREFIX)) {
                return FuzzyBoolean.NO;
            }
            shadow.getIWorld().getLint().unresolvableMember.signal(member.toString(), getSourceLocation());
            return FuzzyBoolean.NO;
        }
        Shadow.Kind kind = shadow.getKind();
        if (kind == Shadow.StaticInitialization) {
            toMatchAgainst = rMember.getDeclaringType().resolve(shadow.getIWorld());
        } else if (kind == Shadow.ExceptionHandler) {
            toMatchAgainst = rMember.getParameterTypes()[0].resolve(shadow.getIWorld());
        } else {
            toMatchAgainst = rMember;
            if (rMember.isAnnotatedElsewhere() && (kind == Shadow.FieldGet || kind == Shadow.FieldSet)) {
                List mungers = rMember.getDeclaringType().resolve(shadow.getIWorld()).getInterTypeMungers();
                for (ConcreteTypeMunger typeMunger : mungers) {
                    if (typeMunger.getMunger() instanceof NewFieldTypeMunger) {
                        ResolvedMember fakerm = typeMunger.getSignature();
                        if (fakerm.equals(member)) {
                            ResolvedMember ajcMethod = AjcMemberMaker.interFieldInitializer(fakerm, typeMunger.getAspectType());
                            ResolvedMember rmm = findMethod(typeMunger.getAspectType(), ajcMethod);
                            toMatchAgainst = rmm;
                        }
                    }
                }
            }
        }
        this.annotationTypePattern.resolve(shadow.getIWorld());
        return this.annotationTypePattern.matches(toMatchAgainst);
    }

    private ResolvedMember findMethod(ResolvedType aspectType, ResolvedMember ajcMethod) {
        ResolvedMember[] decMethods = aspectType.getDeclaredMethods();
        for (ResolvedMember member : decMethods) {
            if (member.equals(ajcMethod)) {
                return member;
            }
        }
        return null;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected void resolveBindings(IScope scope, Bindings bindings) {
        if (!scope.getWorld().isInJava5Mode()) {
            scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.ATANNOTATION_ONLY_SUPPORTED_AT_JAVA5_LEVEL), getSourceLocation()));
        } else {
            this.annotationTypePattern = (ExactAnnotationTypePattern) this.annotationTypePattern.resolveBindings(scope, bindings, true);
        }
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        ExactAnnotationTypePattern newType = (ExactAnnotationTypePattern) this.annotationTypePattern.remapAdviceFormals(bindings);
        Pointcut ret = new AnnotationPointcut(newType, bindings.getEnclosingAdvice());
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) throws AbortException {
        if (this.annotationTypePattern instanceof BindingAnnotationFieldTypePattern) {
            if (shadow.getKind() != Shadow.MethodExecution) {
                shadow.getIWorld().getMessageHandler().handleMessage(MessageUtil.error("Annotation field binding is only supported at method-execution join points (compiler limitation)", getSourceLocation()));
                return Literal.TRUE;
            }
            BindingAnnotationFieldTypePattern btp = (BindingAnnotationFieldTypePattern) this.annotationTypePattern;
            ResolvedType formalType = btp.getFormalType().resolve(shadow.getIWorld());
            UnresolvedType annoType = btp.getAnnotationType();
            Var var = shadow.getKindedAnnotationVar(annoType);
            if (var == null) {
                throw new BCException("Unexpected problem locating annotation at join point '" + shadow + "'");
            }
            state.set(btp.getFormalIndex(), var.getAccessorForValue(formalType, btp.formalName));
        } else if (this.annotationTypePattern instanceof BindingAnnotationTypePattern) {
            BindingAnnotationTypePattern btp2 = (BindingAnnotationTypePattern) this.annotationTypePattern;
            UnresolvedType annotationType = btp2.getAnnotationType();
            Var var2 = shadow.getKindedAnnotationVar(annotationType);
            if (var2 == null) {
                if (matchInternal(shadow).alwaysTrue()) {
                    return Literal.TRUE;
                }
                return Literal.FALSE;
            }
            state.set(btp2.getFormalIndex(), var2);
        }
        if (matchInternal(shadow).alwaysTrue()) {
            return Literal.TRUE;
        }
        return Literal.FALSE;
    }

    @Override // org.aspectj.weaver.patterns.NameBindingPointcut
    public List<BindingPattern> getBindingAnnotationTypePatterns() {
        if (this.annotationTypePattern instanceof BindingPattern) {
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
        s.writeByte(16);
        this.annotationTypePattern.write(s);
        writeLocation(s);
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        AnnotationTypePattern type = AnnotationTypePattern.read(s, context);
        AnnotationPointcut ret = new AnnotationPointcut((ExactAnnotationTypePattern) type);
        ret.readLocation(context, s);
        return ret;
    }

    public boolean equals(Object other) {
        if (!(other instanceof AnnotationPointcut)) {
            return false;
        }
        AnnotationPointcut o = (AnnotationPointcut) other;
        return o.annotationTypePattern.equals(this.annotationTypePattern);
    }

    public int hashCode() {
        int result = (37 * 17) + this.annotationTypePattern.hashCode();
        return result;
    }

    public void buildDeclarationText() {
        StringBuffer buf = new StringBuffer();
        buf.append("@annotation(");
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
