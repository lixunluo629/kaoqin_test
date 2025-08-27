package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/AnyTypePattern.class */
public class AnyTypePattern extends TypePattern {
    public AnyTypePattern() {
        super(false, false, new TypePatternList());
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean couldEverMatchSameTypesAs(TypePattern other) {
        return true;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type) {
        return true;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type, ResolvedType annotatedType) {
        return true;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public FuzzyBoolean matchesInstanceof(ResolvedType type) {
        return FuzzyBoolean.YES;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(5);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesSubtypes(ResolvedType type) {
        return true;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public boolean isStar() {
        return true;
    }

    public String toString() {
        return "*";
    }

    public boolean equals(Object obj) {
        return obj instanceof AnyTypePattern;
    }

    public int hashCode() {
        return 37;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern parameterizeWith(Map arg0, World w) {
        return this;
    }
}
