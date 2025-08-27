package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.bridge.ISourceLocation;
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

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/WithinPointcut.class */
public class WithinPointcut extends Pointcut {
    private TypePattern typePattern;

    public WithinPointcut(TypePattern type) {
        this.typePattern = type;
        this.pointcutKind = (byte) 2;
    }

    public TypePattern getTypePattern() {
        return this.typePattern;
    }

    private FuzzyBoolean isWithinType(ResolvedType type) {
        while (type != null) {
            if (this.typePattern.matchesStatically(type)) {
                return FuzzyBoolean.YES;
            }
            type = type.getDeclaringType();
        }
        return FuzzyBoolean.NO;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return Shadow.ALL_SHADOW_KINDS_BITS;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        WithinPointcut ret = new WithinPointcut(this.typePattern.parameterizeWith(typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo info) {
        if (this.typePattern.annotationPattern instanceof AnyAnnotationTypePattern) {
            return isWithinType(info.getType());
        }
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        ResolvedType enclosingType = shadow.getIWorld().resolve(shadow.getEnclosingType(), true);
        if (enclosingType.isMissing()) {
            shadow.getIWorld().getLint().cantFindType.signal(new String[]{WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE_WITHINPCD, shadow.getEnclosingType().getName())}, shadow.getSourceLocation(), new ISourceLocation[]{getSourceLocation()});
        }
        this.typePattern.resolve(shadow.getIWorld());
        return isWithinType(enclosingType);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(2);
        this.typePattern.write(s);
        writeLocation(s);
    }

    public static Pointcut read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        TypePattern type = TypePattern.read(s, context);
        WithinPointcut ret = new WithinPointcut(type);
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
        this.typePattern = this.typePattern.resolveBindings(scope, bindings, false, false);
        HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor visitor = new HasThisTypePatternTriedToSneakInSomeGenericOrParameterizedTypePatternMatchingStuffAnywhereVisitor();
        this.typePattern.traverse(visitor, null);
        if (visitor.wellHasItThen()) {
            scope.message(MessageUtil.error(WeaverMessages.format(WeaverMessages.WITHIN_PCD_DOESNT_SUPPORT_PARAMETERS), getSourceLocation()));
        }
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void postRead(ResolvedType enclosingType) {
        this.typePattern.postRead(enclosingType);
    }

    public boolean couldEverMatchSameJoinPointsAs(WithinPointcut other) {
        return this.typePattern.couldEverMatchSameTypesAs(other.typePattern);
    }

    public boolean equals(Object other) {
        if (!(other instanceof WithinPointcut)) {
            return false;
        }
        WithinPointcut o = (WithinPointcut) other;
        return o.typePattern.equals(this.typePattern);
    }

    public int hashCode() {
        return this.typePattern.hashCode();
    }

    public String toString() {
        return "within(" + this.typePattern + ")";
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        return match(shadow).alwaysTrue() ? Literal.TRUE : Literal.FALSE;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut concretize1(ResolvedType inAspect, ResolvedType declaringType, IntMap bindings) {
        Pointcut ret = new WithinPointcut(this.typePattern);
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
