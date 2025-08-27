package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Map;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/AnyWithAnnotationTypePattern.class */
public class AnyWithAnnotationTypePattern extends TypePattern {
    public AnyWithAnnotationTypePattern(AnnotationTypePattern atp) {
        super(false, false);
        this.annotationPattern = atp;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean couldEverMatchSameTypesAs(TypePattern other) {
        return true;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type) {
        boolean b;
        this.annotationPattern.resolve(type.getWorld());
        if (type.temporaryAnnotationTypes != null) {
            b = this.annotationPattern.matches(type, type.temporaryAnnotationTypes).alwaysTrue();
        } else {
            b = this.annotationPattern.matches(type).alwaysTrue();
        }
        return b;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern resolveBindings(IScope scope, Bindings bindings, boolean allowBinding, boolean requireExactType) throws AbortException {
        if (requireExactType) {
            scope.getWorld().getMessageHandler().handleMessage(MessageUtil.error(WeaverMessages.format(WeaverMessages.WILDCARD_NOT_ALLOWED), getSourceLocation()));
            return NO;
        }
        return super.resolveBindings(scope, bindings, allowBinding, requireExactType);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type, ResolvedType annotatedType) {
        this.annotationPattern.resolve(type.getWorld());
        return this.annotationPattern.matches(annotatedType).alwaysTrue();
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public FuzzyBoolean matchesInstanceof(ResolvedType type) {
        if (Modifier.isFinal(type.getModifiers())) {
            return FuzzyBoolean.fromBoolean(matchesExactly(type));
        }
        return FuzzyBoolean.MAYBE;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        AnyWithAnnotationTypePattern ret = new AnyWithAnnotationTypePattern(this.annotationPattern.parameterizeWith(typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(10);
        this.annotationPattern.write(s);
        writeLocation(s);
    }

    public static TypePattern read(VersionedDataInputStream s, ISourceContext c) throws IOException {
        AnnotationTypePattern annPatt = AnnotationTypePattern.read(s, c);
        AnyWithAnnotationTypePattern ret = new AnyWithAnnotationTypePattern(annPatt);
        ret.readLocation(c, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesSubtypes(ResolvedType type) {
        return true;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public boolean isStar() {
        return false;
    }

    public String toString() {
        return "(" + this.annotationPattern + " *)";
    }

    public AnnotationTypePattern getAnnotationTypePattern() {
        return this.annotationPattern;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AnyWithAnnotationTypePattern)) {
            return false;
        }
        AnyWithAnnotationTypePattern awatp = (AnyWithAnnotationTypePattern) obj;
        return this.annotationPattern.equals(awatp.annotationPattern);
    }

    public int hashCode() {
        return this.annotationPattern.hashCode();
    }
}
