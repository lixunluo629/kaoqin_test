package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/NotTypePattern.class */
public class NotTypePattern extends TypePattern {
    private TypePattern negatedPattern;
    private boolean isBangVoid;
    private boolean checked;

    public NotTypePattern(TypePattern pattern) {
        super(false, false);
        this.isBangVoid = false;
        this.checked = false;
        this.negatedPattern = pattern;
        setLocation(pattern.getSourceContext(), pattern.getStart(), pattern.getEnd());
    }

    public TypePattern getNegatedPattern() {
        return this.negatedPattern;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean couldEverMatchSameTypesAs(TypePattern other) {
        return true;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public FuzzyBoolean matchesInstanceof(ResolvedType type) {
        return this.negatedPattern.matchesInstanceof(type).not();
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type) {
        return !this.negatedPattern.matchesExactly(type) && this.annotationPattern.matches(type).alwaysTrue();
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type, ResolvedType annotatedType) {
        return !this.negatedPattern.matchesExactly(type, annotatedType) && this.annotationPattern.matches(annotatedType).alwaysTrue();
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public boolean matchesStatically(ResolvedType type) {
        return !this.negatedPattern.matchesStatically(type);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public void setAnnotationTypePattern(AnnotationTypePattern annPatt) {
        super.setAnnotationTypePattern(annPatt);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public void setIsVarArgs(boolean isVarArgs) {
        this.negatedPattern.setIsVarArgs(isVarArgs);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(6);
        this.negatedPattern.write(s);
        this.annotationPattern.write(s);
        writeLocation(s);
    }

    public static TypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        TypePattern ret = new NotTypePattern(TypePattern.read(s, context));
        if (s.getMajorVersion() >= 2) {
            ret.annotationPattern = AnnotationTypePattern.read(s, context);
        }
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern resolveBindings(IScope scope, Bindings bindings, boolean allowBinding, boolean requireExactType) {
        if (requireExactType) {
            return notExactType(scope);
        }
        this.negatedPattern = this.negatedPattern.resolveBindings(scope, bindings, false, false);
        return this;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public boolean isBangVoid() {
        if (!this.checked) {
            this.isBangVoid = this.negatedPattern.getExactType().isVoid();
            this.checked = true;
        }
        return this.isBangVoid;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern parameterizeWith(Map typeVariableMap, World w) {
        TypePattern newNegatedPattern = this.negatedPattern.parameterizeWith(typeVariableMap, w);
        NotTypePattern ret = new NotTypePattern(newNegatedPattern);
        ret.copyLocationFrom(this);
        return ret;
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        if (this.annotationPattern != AnnotationTypePattern.ANY) {
            buff.append('(');
            buff.append(this.annotationPattern.toString());
            buff.append(' ');
        }
        buff.append('!');
        buff.append(this.negatedPattern);
        if (this.annotationPattern != AnnotationTypePattern.ANY) {
            buff.append(')');
        }
        return buff.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NotTypePattern)) {
            return false;
        }
        return this.negatedPattern.equals(((NotTypePattern) obj).negatedPattern);
    }

    public int hashCode() {
        return 17 + (37 * this.negatedPattern.hashCode());
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object traverse(PatternNodeVisitor visitor, Object data) {
        Object ret = accept(visitor, data);
        this.negatedPattern.traverse(visitor, ret);
        return ret;
    }
}
