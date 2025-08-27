package org.aspectj.weaver.patterns;

import java.io.IOException;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.VersionedDataInputStream;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/TypeVariablePatternList.class */
public class TypeVariablePatternList extends PatternNode {
    public static final TypeVariablePatternList EMPTY = new TypeVariablePatternList(new TypeVariablePattern[0]);
    private TypeVariablePattern[] patterns;

    public TypeVariablePatternList(TypeVariablePattern[] typeVars) {
        this.patterns = typeVars;
    }

    public TypeVariablePattern[] getTypeVariablePatterns() {
        return this.patterns;
    }

    public TypeVariablePattern lookupTypeVariable(String name) {
        for (int i = 0; i < this.patterns.length; i++) {
            if (this.patterns[i].getName().equals(name)) {
                return this.patterns[i];
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return this.patterns == null || this.patterns.length == 0;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeInt(this.patterns.length);
        for (int i = 0; i < this.patterns.length; i++) {
            this.patterns[i].write(s);
        }
        writeLocation(s);
    }

    public static TypeVariablePatternList read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        TypeVariablePatternList ret = EMPTY;
        int length = s.readInt();
        if (length > 0) {
            TypeVariablePattern[] patterns = new TypeVariablePattern[length];
            for (int i = 0; i < patterns.length; i++) {
                patterns[i] = TypeVariablePattern.read(s, context);
            }
            ret = new TypeVariablePatternList(patterns);
        }
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object traverse(PatternNodeVisitor visitor, Object data) {
        Object ret = accept(visitor, data);
        for (int i = 0; i < this.patterns.length; i++) {
            this.patterns[i].traverse(visitor, ret);
        }
        return ret;
    }
}
