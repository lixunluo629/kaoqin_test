package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.ognl.OgnlContext;
import org.aspectj.bridge.IMessage;
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
import org.aspectj.weaver.ast.Var;
import org.springframework.validation.DataBinder;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ThisOrTargetPointcut.class */
public class ThisOrTargetPointcut extends NameBindingPointcut {
    private boolean isThis;
    private TypePattern typePattern;
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

    public boolean isBinding() {
        return this.typePattern instanceof BindingTypePattern;
    }

    public ThisOrTargetPointcut(boolean isThis, TypePattern type) {
        this.isThis = isThis;
        this.typePattern = type;
        this.pointcutKind = (byte) 3;
        this.declarationText = (isThis ? "this(" : "target(") + type + ")";
    }

    public TypePattern getType() {
        return this.typePattern;
    }

    public boolean isThis() {
        return this.isThis;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        ThisOrTargetPointcut ret = new ThisOrTargetPointcut(this.isThis, this.typePattern.parameterizeWith(typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return this.isThis ? thisKindSet : targetKindSet;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo type) {
        return FuzzyBoolean.MAYBE;
    }

    private boolean couldMatch(Shadow shadow) {
        return this.isThis ? shadow.hasThis() : shadow.hasTarget();
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        if (!couldMatch(shadow)) {
            return FuzzyBoolean.NO;
        }
        UnresolvedType typeToMatch = this.isThis ? shadow.getThisType() : shadow.getTargetType();
        if (this.typePattern.getExactType().equals(ResolvedType.OBJECT)) {
            return FuzzyBoolean.YES;
        }
        return this.typePattern.matches(typeToMatch.resolve(shadow.getIWorld()), TypePattern.DYNAMIC);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(3);
        s.writeBoolean(this.isThis);
        this.typePattern.write(s);
        writeLocation(s);
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        boolean isThis = s.readBoolean();
        TypePattern type = TypePattern.read(s, context);
        ThisOrTargetPointcut ret = new ThisOrTargetPointcut(isThis, type);
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
        this.typePattern = this.typePattern.resolveBindings(scope, bindings, true, true);
        HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
        this.typePattern.traverse(visitor, null);
        if (visitor.wellHasItThen()) {
            scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.THIS_AND_TARGET_DONT_SUPPORT_PARAMETERS), getSourceLocation()));
        }
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void postRead(ResolvedType enclosingType) {
        this.typePattern.postRead(enclosingType);
    }

    @Override // org.aspectj.weaver.patterns.NameBindingPointcut
    public List<BindingPattern> getBindingAnnotationTypePatterns() {
        return Collections.emptyList();
    }

    @Override // org.aspectj.weaver.patterns.NameBindingPointcut
    public List<BindingTypePattern> getBindingTypePatterns() {
        if (this.typePattern instanceof BindingTypePattern) {
            List<BindingTypePattern> l = new ArrayList<>();
            l.add((BindingTypePattern) this.typePattern);
            return l;
        }
        return Collections.emptyList();
    }

    public boolean equals(Object other) {
        if (!(other instanceof ThisOrTargetPointcut)) {
            return false;
        }
        ThisOrTargetPointcut o = (ThisOrTargetPointcut) other;
        return o.isThis == this.isThis && o.typePattern.equals(this.typePattern);
    }

    public int hashCode() {
        int result = (37 * 17) + (this.isThis ? 0 : 1);
        return (37 * result) + this.typePattern.hashCode();
    }

    public String toString() {
        return this.declarationText;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        if (!couldMatch(shadow)) {
            return Literal.FALSE;
        }
        if (this.typePattern == TypePattern.ANY) {
            return Literal.TRUE;
        }
        Var var = this.isThis ? shadow.getThisVar() : shadow.getTargetVar();
        return exposeStateForVar(var, this.typePattern, state, shadow.getIWorld());
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        if (isDeclare(bindings.getEnclosingAdvice())) {
            inAspect.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.THIS_OR_TARGET_IN_DECLARE, this.isThis ? OgnlContext.THIS_CONTEXT_KEY : DataBinder.DEFAULT_OBJECT_NAME), bindings.getEnclosingAdvice().getSourceLocation(), null);
            return Pointcut.makeMatchesNothing(Pointcut.CONCRETE);
        }
        TypePattern newType = this.typePattern.remapAdviceFormals(bindings);
        if (inAspect.crosscuttingMembers != null) {
            inAspect.crosscuttingMembers.exposeType(newType.getExactType());
        }
        Pointcut ret = new ThisOrTargetPointcut(this.isThis, newType);
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
