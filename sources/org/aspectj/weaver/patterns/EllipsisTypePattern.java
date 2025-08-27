package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/EllipsisTypePattern.class */
public class EllipsisTypePattern extends TypePattern {
    public EllipsisTypePattern() {
        super(false, false, new TypePatternList());
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean couldEverMatchSameTypesAs(TypePattern other) {
        return true;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type) {
        return false;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type, ResolvedType annotatedType) {
        return false;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public FuzzyBoolean matchesInstanceof(ResolvedType type) {
        return FuzzyBoolean.NO;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(4);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public boolean isEllipsis() {
        return true;
    }

    public String toString() {
        return "..";
    }

    public boolean equals(Object obj) {
        return obj instanceof EllipsisTypePattern;
    }

    public int hashCode() {
        return 629;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern parameterizeWith(Map typeVariableMap, World w) {
        return this;
    }
}
