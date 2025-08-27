package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.ResolvableTypeList;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;
import org.aspectj.weaver.patterns.TypePattern;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/TypePatternList.class */
public class TypePatternList extends PatternNode {
    private TypePattern[] typePatterns;
    int ellipsisCount;
    public static final TypePatternList EMPTY = new TypePatternList(new TypePattern[0]);
    public static final TypePatternList ANY = new TypePatternList(new TypePattern[]{new EllipsisTypePattern()});

    public TypePatternList() {
        this.ellipsisCount = 0;
        this.typePatterns = new TypePattern[0];
        this.ellipsisCount = 0;
    }

    public TypePatternList(TypePattern[] arguments) {
        this.ellipsisCount = 0;
        this.typePatterns = arguments;
        for (TypePattern typePattern : arguments) {
            if (typePattern == TypePattern.ELLIPSIS) {
                this.ellipsisCount++;
            }
        }
    }

    public TypePatternList(List<TypePattern> l) {
        this((TypePattern[]) l.toArray(new TypePattern[l.size()]));
    }

    public int size() {
        return this.typePatterns.length;
    }

    public TypePattern get(int index) {
        return this.typePatterns[index];
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("(");
        int len = this.typePatterns.length;
        for (int i = 0; i < len; i++) {
            TypePattern type = this.typePatterns[i];
            if (i > 0) {
                buf.append(", ");
            }
            if (type == TypePattern.ELLIPSIS) {
                buf.append("..");
            } else {
                buf.append(type.toString());
            }
        }
        buf.append(")");
        return buf.toString();
    }

    public boolean canMatchSignatureWithNParameters(int numParams) {
        return this.ellipsisCount == 0 ? numParams == size() : size() - this.ellipsisCount <= numParams;
    }

    public FuzzyBoolean matches(ResolvedType[] types, TypePattern.MatchKind kind) {
        return matches(types, kind, (ResolvedType[][]) null);
    }

    public FuzzyBoolean matches(ResolvedType[] types, TypePattern.MatchKind kind, ResolvedType[][] parameterAnnotations) {
        ResolvedType t;
        int nameLength = types.length;
        int patternLength = this.typePatterns.length;
        int nameIndex = 0;
        int patternIndex = 0;
        if (this.ellipsisCount == 0) {
            if (nameLength != patternLength) {
                return FuzzyBoolean.NO;
            }
            FuzzyBoolean finalReturn = FuzzyBoolean.YES;
            while (patternIndex < patternLength) {
                t = types[nameIndex];
                if (parameterAnnotations != null) {
                    try {
                        t.temporaryAnnotationTypes = parameterAnnotations[nameIndex];
                    } finally {
                    }
                }
                FuzzyBoolean ret = this.typePatterns[patternIndex].matches(t, kind);
                t.temporaryAnnotationTypes = null;
                patternIndex++;
                nameIndex++;
                if (ret == FuzzyBoolean.NO) {
                    return ret;
                }
                if (ret == FuzzyBoolean.MAYBE) {
                    finalReturn = ret;
                }
            }
            return finalReturn;
        }
        if (this.ellipsisCount == 1) {
            if (nameLength < patternLength - 1) {
                return FuzzyBoolean.NO;
            }
            FuzzyBoolean finalReturn2 = FuzzyBoolean.YES;
            while (patternIndex < patternLength) {
                int i = patternIndex;
                patternIndex++;
                TypePattern p = this.typePatterns[i];
                if (p == TypePattern.ELLIPSIS) {
                    nameIndex = nameLength - (patternLength - patternIndex);
                } else {
                    t = types[nameIndex];
                    if (parameterAnnotations != null) {
                        try {
                            t.temporaryAnnotationTypes = parameterAnnotations[nameIndex];
                        } finally {
                        }
                    }
                    FuzzyBoolean ret2 = p.matches(t, kind);
                    t.temporaryAnnotationTypes = null;
                    nameIndex++;
                    if (ret2 == FuzzyBoolean.NO) {
                        return ret2;
                    }
                    if (ret2 == FuzzyBoolean.MAYBE) {
                        finalReturn2 = ret2;
                    }
                }
            }
            return finalReturn2;
        }
        FuzzyBoolean b = outOfStar(this.typePatterns, types, 0, 0, patternLength - this.ellipsisCount, nameLength, this.ellipsisCount, kind, parameterAnnotations);
        return b;
    }

    private static FuzzyBoolean outOfStar(TypePattern[] pattern, ResolvedType[] target, int pi, int ti, int pLeft, int tLeft, int starsLeft, TypePattern.MatchKind kind, ResolvedType[][] parameterAnnotations) {
        if (pLeft > tLeft) {
            return FuzzyBoolean.NO;
        }
        FuzzyBoolean finalReturn = FuzzyBoolean.YES;
        while (tLeft != 0) {
            if (pLeft == 0) {
                if (starsLeft > 0) {
                    return finalReturn;
                }
                return FuzzyBoolean.NO;
            }
            if (pattern[pi] == TypePattern.ELLIPSIS) {
                return inStar(pattern, target, pi + 1, ti, pLeft, tLeft, starsLeft - 1, kind, parameterAnnotations);
            }
            if (parameterAnnotations != null) {
                try {
                    target[ti].temporaryAnnotationTypes = parameterAnnotations[ti];
                } catch (Throwable th) {
                    target[ti].temporaryAnnotationTypes = null;
                    throw th;
                }
            }
            FuzzyBoolean ret = pattern[pi].matches(target[ti], kind);
            target[ti].temporaryAnnotationTypes = null;
            if (ret == FuzzyBoolean.NO) {
                return ret;
            }
            if (ret == FuzzyBoolean.MAYBE) {
                finalReturn = ret;
            }
            pi++;
            ti++;
            pLeft--;
            tLeft--;
        }
        return finalReturn;
    }

    private static FuzzyBoolean inStar(TypePattern[] pattern, ResolvedType[] target, int pi, int ti, int pLeft, int tLeft, int starsLeft, TypePattern.MatchKind kind, ResolvedType[][] parameterAnnotations) {
        TypePattern patternChar;
        TypePattern typePattern = pattern[pi];
        while (true) {
            patternChar = typePattern;
            if (patternChar != TypePattern.ELLIPSIS) {
                break;
            }
            starsLeft--;
            pi++;
            typePattern = pattern[pi];
        }
        while (pLeft <= tLeft) {
            if (parameterAnnotations != null) {
                try {
                    target[ti].temporaryAnnotationTypes = parameterAnnotations[ti];
                } catch (Throwable th) {
                    target[ti].temporaryAnnotationTypes = null;
                    throw th;
                }
            }
            FuzzyBoolean ff = patternChar.matches(target[ti], kind);
            target[ti].temporaryAnnotationTypes = null;
            if (ff.maybeTrue()) {
                FuzzyBoolean xx = outOfStar(pattern, target, pi + 1, ti + 1, pLeft - 1, tLeft - 1, starsLeft, kind, parameterAnnotations);
                if (xx.maybeTrue()) {
                    return ff.and(xx);
                }
            }
            ti++;
            tLeft--;
        }
        return FuzzyBoolean.NO;
    }

    public FuzzyBoolean matches(ResolvableTypeList types, TypePattern.MatchKind kind, ResolvedType[][] parameterAnnotations) {
        ResolvedType t;
        int nameLength = types.length;
        int patternLength = this.typePatterns.length;
        int nameIndex = 0;
        int patternIndex = 0;
        if (this.ellipsisCount == 0) {
            if (nameLength != patternLength) {
                return FuzzyBoolean.NO;
            }
            FuzzyBoolean finalReturn = FuzzyBoolean.YES;
            while (patternIndex < patternLength) {
                t = types.getResolved(nameIndex);
                if (parameterAnnotations != null) {
                    try {
                        t.temporaryAnnotationTypes = parameterAnnotations[nameIndex];
                    } finally {
                    }
                }
                FuzzyBoolean ret = this.typePatterns[patternIndex].matches(t, kind);
                t.temporaryAnnotationTypes = null;
                patternIndex++;
                nameIndex++;
                if (ret == FuzzyBoolean.NO) {
                    return ret;
                }
                if (ret == FuzzyBoolean.MAYBE) {
                    finalReturn = ret;
                }
            }
            return finalReturn;
        }
        if (this.ellipsisCount == 1) {
            if (nameLength < patternLength - 1) {
                return FuzzyBoolean.NO;
            }
            FuzzyBoolean finalReturn2 = FuzzyBoolean.YES;
            while (patternIndex < patternLength) {
                int i = patternIndex;
                patternIndex++;
                TypePattern p = this.typePatterns[i];
                if (p == TypePattern.ELLIPSIS) {
                    nameIndex = nameLength - (patternLength - patternIndex);
                } else {
                    t = types.getResolved(nameIndex);
                    if (parameterAnnotations != null) {
                        try {
                            t.temporaryAnnotationTypes = parameterAnnotations[nameIndex];
                        } finally {
                        }
                    }
                    FuzzyBoolean ret2 = p.matches(t, kind);
                    t.temporaryAnnotationTypes = null;
                    nameIndex++;
                    if (ret2 == FuzzyBoolean.NO) {
                        return ret2;
                    }
                    if (ret2 == FuzzyBoolean.MAYBE) {
                        finalReturn2 = ret2;
                    }
                }
            }
            return finalReturn2;
        }
        FuzzyBoolean b = outOfStar(this.typePatterns, types, 0, 0, patternLength - this.ellipsisCount, nameLength, this.ellipsisCount, kind, parameterAnnotations);
        return b;
    }

    private static FuzzyBoolean outOfStar(TypePattern[] pattern, ResolvableTypeList target, int pi, int ti, int pLeft, int tLeft, int starsLeft, TypePattern.MatchKind kind, ResolvedType[][] parameterAnnotations) {
        if (pLeft > tLeft) {
            return FuzzyBoolean.NO;
        }
        FuzzyBoolean finalReturn = FuzzyBoolean.YES;
        while (tLeft != 0) {
            if (pLeft == 0) {
                if (starsLeft > 0) {
                    return finalReturn;
                }
                return FuzzyBoolean.NO;
            }
            if (pattern[pi] == TypePattern.ELLIPSIS) {
                return inStar(pattern, target, pi + 1, ti, pLeft, tLeft, starsLeft - 1, kind, parameterAnnotations);
            }
            ResolvedType type = target.getResolved(ti);
            if (parameterAnnotations != null) {
                try {
                    type.temporaryAnnotationTypes = parameterAnnotations[ti];
                } catch (Throwable th) {
                    type.temporaryAnnotationTypes = null;
                    throw th;
                }
            }
            FuzzyBoolean ret = pattern[pi].matches(type, kind);
            type.temporaryAnnotationTypes = null;
            if (ret == FuzzyBoolean.NO) {
                return ret;
            }
            if (ret == FuzzyBoolean.MAYBE) {
                finalReturn = ret;
            }
            pi++;
            ti++;
            pLeft--;
            tLeft--;
        }
        return finalReturn;
    }

    private static FuzzyBoolean inStar(TypePattern[] pattern, ResolvableTypeList target, int pi, int ti, int pLeft, int tLeft, int starsLeft, TypePattern.MatchKind kind, ResolvedType[][] parameterAnnotations) {
        TypePattern patternChar;
        TypePattern typePattern = pattern[pi];
        while (true) {
            patternChar = typePattern;
            if (patternChar != TypePattern.ELLIPSIS) {
                break;
            }
            starsLeft--;
            pi++;
            typePattern = pattern[pi];
        }
        while (pLeft <= tLeft) {
            ResolvedType type = target.getResolved(ti);
            if (parameterAnnotations != null) {
                try {
                    type.temporaryAnnotationTypes = parameterAnnotations[ti];
                } catch (Throwable th) {
                    type.temporaryAnnotationTypes = null;
                    throw th;
                }
            }
            FuzzyBoolean ff = patternChar.matches(type, kind);
            type.temporaryAnnotationTypes = null;
            if (ff.maybeTrue()) {
                FuzzyBoolean xx = outOfStar(pattern, target, pi + 1, ti + 1, pLeft - 1, tLeft - 1, starsLeft, kind, parameterAnnotations);
                if (xx.maybeTrue()) {
                    return ff.and(xx);
                }
            }
            ti++;
            tLeft--;
        }
        return FuzzyBoolean.NO;
    }

    public TypePatternList parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        TypePattern[] parameterizedPatterns = new TypePattern[this.typePatterns.length];
        for (int i = 0; i < parameterizedPatterns.length; i++) {
            parameterizedPatterns[i] = this.typePatterns[i].parameterizeWith(typeVariableMap, w);
        }
        return new TypePatternList(parameterizedPatterns);
    }

    public TypePatternList resolveBindings(IScope scope, Bindings bindings, boolean allowBinding, boolean requireExactType) {
        for (int i = 0; i < this.typePatterns.length; i++) {
            TypePattern p = this.typePatterns[i];
            if (p != null) {
                this.typePatterns[i] = this.typePatterns[i].resolveBindings(scope, bindings, allowBinding, requireExactType);
            }
        }
        return this;
    }

    public TypePatternList resolveReferences(IntMap bindings) {
        int len = this.typePatterns.length;
        TypePattern[] ret = new TypePattern[len];
        for (int i = 0; i < len; i++) {
            ret[i] = this.typePatterns[i].remapAdviceFormals(bindings);
        }
        return new TypePatternList(ret);
    }

    public void postRead(ResolvedType enclosingType) {
        for (int i = 0; i < this.typePatterns.length; i++) {
            TypePattern p = this.typePatterns[i];
            p.postRead(enclosingType);
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof TypePatternList)) {
            return false;
        }
        TypePatternList o = (TypePatternList) other;
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

    public static TypePatternList read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        int i = s.readShort();
        TypePattern[] arguments = new TypePattern[i];
        for (int i2 = 0; i2 < i; i2++) {
            arguments[i2] = TypePattern.read(s, context);
        }
        TypePatternList ret = new TypePatternList(arguments);
        if (!s.isAtLeast169()) {
            ret.readLocation(context, s);
        }
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode, org.aspectj.weaver.IHasPosition
    public int getEnd() {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode, org.aspectj.weaver.IHasSourceLocation
    public ISourceContext getSourceContext() {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode, org.aspectj.weaver.IHasSourceLocation
    public ISourceLocation getSourceLocation() {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode, org.aspectj.weaver.IHasPosition
    public int getStart() {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeShort(this.typePatterns.length);
        for (int i = 0; i < this.typePatterns.length; i++) {
            this.typePatterns[i].write(s);
        }
    }

    public TypePattern[] getTypePatterns() {
        return this.typePatterns;
    }

    public List<UnresolvedType> getExactTypes() {
        List<UnresolvedType> ret = new ArrayList<>();
        for (int i = 0; i < this.typePatterns.length; i++) {
            UnresolvedType t = this.typePatterns[i].getExactType();
            if (!ResolvedType.isMissing(t)) {
                ret.add(t);
            }
        }
        return ret;
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

    public boolean areAllExactWithNoSubtypesAllowed() {
        for (int i = 0; i < this.typePatterns.length; i++) {
            TypePattern array_element = this.typePatterns[i];
            if (!(array_element instanceof ExactTypePattern)) {
                return false;
            }
            ExactTypePattern etp = (ExactTypePattern) array_element;
            if (etp.isIncludeSubtypes()) {
                return false;
            }
        }
        return true;
    }

    public String[] maybeGetCleanNames() {
        String[] theParamNames = new String[this.typePatterns.length];
        for (int i = 0; i < this.typePatterns.length; i++) {
            TypePattern string = this.typePatterns[i];
            if (!(string instanceof ExactTypePattern)) {
                return null;
            }
            theParamNames[i] = ((ExactTypePattern) string).getExactType().getName();
        }
        return theParamNames;
    }
}
