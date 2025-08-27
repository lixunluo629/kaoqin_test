package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/AnnotationPatternList.class */
public class AnnotationPatternList extends PatternNode {
    private AnnotationTypePattern[] typePatterns;
    int ellipsisCount;
    public static final AnnotationPatternList EMPTY = new AnnotationPatternList(new AnnotationTypePattern[0]);
    public static final AnnotationPatternList ANY = new AnnotationPatternList(new AnnotationTypePattern[]{AnnotationTypePattern.ELLIPSIS});

    public AnnotationPatternList() {
        this.ellipsisCount = 0;
        this.typePatterns = new AnnotationTypePattern[0];
        this.ellipsisCount = 0;
    }

    public AnnotationPatternList(AnnotationTypePattern[] arguments) {
        this.ellipsisCount = 0;
        this.typePatterns = arguments;
        for (AnnotationTypePattern annotationTypePattern : arguments) {
            if (annotationTypePattern == AnnotationTypePattern.ELLIPSIS) {
                this.ellipsisCount++;
            }
        }
    }

    public AnnotationPatternList(List<AnnotationTypePattern> l) {
        this((AnnotationTypePattern[]) l.toArray(new AnnotationTypePattern[l.size()]));
    }

    protected AnnotationTypePattern[] getAnnotationPatterns() {
        return this.typePatterns;
    }

    public AnnotationPatternList parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        AnnotationTypePattern[] parameterizedPatterns = new AnnotationTypePattern[this.typePatterns.length];
        for (int i = 0; i < parameterizedPatterns.length; i++) {
            parameterizedPatterns[i] = this.typePatterns[i].parameterizeWith(typeVariableMap, w);
        }
        AnnotationPatternList ret = new AnnotationPatternList(parameterizedPatterns);
        ret.copyLocationFrom(this);
        return ret;
    }

    public void resolve(World inWorld) {
        for (int i = 0; i < this.typePatterns.length; i++) {
            this.typePatterns[i].resolve(inWorld);
        }
    }

    public FuzzyBoolean matches(ResolvedType[] someArgs) {
        int numArgsMatchedByEllipsis = (someArgs.length + this.ellipsisCount) - this.typePatterns.length;
        if (numArgsMatchedByEllipsis < 0) {
            return FuzzyBoolean.NO;
        }
        if (numArgsMatchedByEllipsis > 0 && this.ellipsisCount == 0) {
            return FuzzyBoolean.NO;
        }
        FuzzyBoolean ret = FuzzyBoolean.YES;
        int argsIndex = 0;
        for (int i = 0; i < this.typePatterns.length; i++) {
            if (this.typePatterns[i] == AnnotationTypePattern.ELLIPSIS) {
                argsIndex += numArgsMatchedByEllipsis;
            } else if (this.typePatterns[i] == AnnotationTypePattern.ANY) {
                argsIndex++;
            } else {
                if (someArgs[argsIndex].isPrimitiveType()) {
                    return FuzzyBoolean.NO;
                }
                ExactAnnotationTypePattern ap = (ExactAnnotationTypePattern) this.typePatterns[i];
                FuzzyBoolean matches = ap.matchesRuntimeType(someArgs[argsIndex]);
                if (matches == FuzzyBoolean.NO) {
                    return FuzzyBoolean.MAYBE;
                }
                argsIndex++;
                ret = ret.and(matches);
            }
        }
        return ret;
    }

    public int size() {
        return this.typePatterns.length;
    }

    public AnnotationTypePattern get(int index) {
        return this.typePatterns[index];
    }

    public AnnotationPatternList resolveBindings(IScope scope, Bindings bindings, boolean allowBinding) {
        for (int i = 0; i < this.typePatterns.length; i++) {
            AnnotationTypePattern p = this.typePatterns[i];
            if (p != null) {
                this.typePatterns[i] = this.typePatterns[i].resolveBindings(scope, bindings, allowBinding);
            }
        }
        return this;
    }

    public AnnotationPatternList resolveReferences(IntMap bindings) {
        int len = this.typePatterns.length;
        AnnotationTypePattern[] ret = new AnnotationTypePattern[len];
        for (int i = 0; i < len; i++) {
            ret[i] = this.typePatterns[i].remapAdviceFormals(bindings);
        }
        return new AnnotationPatternList(ret);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("(");
        int len = this.typePatterns.length;
        for (int i = 0; i < len; i++) {
            AnnotationTypePattern type = this.typePatterns[i];
            if (i > 0) {
                buf.append(", ");
            }
            if (type == AnnotationTypePattern.ELLIPSIS) {
                buf.append("..");
            } else {
                String annPatt = type.toString();
                buf.append(annPatt.startsWith("@") ? annPatt.substring(1) : annPatt);
            }
        }
        buf.append(")");
        return buf.toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof AnnotationPatternList)) {
            return false;
        }
        AnnotationPatternList o = (AnnotationPatternList) other;
        int len = o.typePatterns.length;
        if (len != this.typePatterns.length) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (!this.typePatterns[i].equals(o.typePatterns[i])) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int result = 41;
        int len = this.typePatterns.length;
        for (int i = 0; i < len; i++) {
            result = (37 * result) + this.typePatterns[i].hashCode();
        }
        return result;
    }

    public static AnnotationPatternList read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        int i = s.readShort();
        AnnotationTypePattern[] arguments = new AnnotationTypePattern[i];
        for (int i2 = 0; i2 < i; i2++) {
            arguments[i2] = AnnotationTypePattern.read(s, context);
        }
        AnnotationPatternList ret = new AnnotationPatternList(arguments);
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeShort(this.typePatterns.length);
        for (int i = 0; i < this.typePatterns.length; i++) {
            this.typePatterns[i].write(s);
        }
        writeLocation(s);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object traverse(PatternNodeVisitor visitor, Object data) {
        Object ret = accept(visitor, data);
        for (int i = 0; i < this.typePatterns.length; i++) {
            this.typePatterns[i].traverse(visitor, ret);
        }
        return ret;
    }
}
