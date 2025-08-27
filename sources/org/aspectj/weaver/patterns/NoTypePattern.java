package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/NoTypePattern.class */
public class NoTypePattern extends TypePattern {
    public NoTypePattern() {
        super(false, false, new TypePatternList());
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean couldEverMatchSameTypesAs(TypePattern other) {
        return false;
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
        s.writeByte(9);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesSubtypes(ResolvedType type) {
        return false;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public boolean isStar() {
        return false;
    }

    public String toString() {
        return "<nothing>";
    }

    public boolean equals(Object obj) {
        return obj instanceof NoTypePattern;
    }

    public int hashCode() {
        return 23273;
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
