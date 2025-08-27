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

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/HandlerPointcut.class */
public class HandlerPointcut extends Pointcut {
    TypePattern exceptionType;
    private static final int MATCH_KINDS = Shadow.ExceptionHandler.bit;

    public HandlerPointcut(TypePattern exceptionType) {
        this.exceptionType = exceptionType;
        this.pointcutKind = (byte) 13;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return MATCH_KINDS;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo type) {
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        if (shadow.getKind() != Shadow.ExceptionHandler) {
            return FuzzyBoolean.NO;
        }
        this.exceptionType.resolve(shadow.getIWorld());
        return this.exceptionType.matches(shadow.getSignature().getParameterTypes()[0].resolve(shadow.getIWorld()), TypePattern.STATIC);
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map typeVariableMap, World w) {
        HandlerPointcut ret = new HandlerPointcut(this.exceptionType.parameterizeWith(typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    public boolean equals(Object other) {
        if (!(other instanceof HandlerPointcut)) {
            return false;
        }
        HandlerPointcut o = (HandlerPointcut) other;
        return o.exceptionType.equals(this.exceptionType);
    }

    public int hashCode() {
        int result = (37 * 17) + this.exceptionType.hashCode();
        return result;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("handler(");
        buf.append(this.exceptionType.toString());
        buf.append(")");
        return buf.toString();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(13);
        this.exceptionType.write(s);
        writeLocation(s);
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        HandlerPointcut ret = new HandlerPointcut(TypePattern.read(s, context));
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
        this.exceptionType = this.exceptionType.resolveBindings(scope, bindings, false, false);
        boolean invalidParameterization = false;
        if (this.exceptionType.getTypeParameters().size() > 0) {
            invalidParameterization = true;
        }
        UnresolvedType exactType = this.exceptionType.getExactType();
        if (exactType != null && exactType.isParameterizedType()) {
            invalidParameterization = true;
        }
        if (invalidParameterization) {
            scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.HANDLER_PCD_DOESNT_SUPPORT_PARAMETERS), getSourceLocation()));
        }
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        return match(shadow).alwaysTrue() ? Literal.TRUE : Literal.FALSE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        Pointcut ret = new HandlerPointcut(this.exceptionType);
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
