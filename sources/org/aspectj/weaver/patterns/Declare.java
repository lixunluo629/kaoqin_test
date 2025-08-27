package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/Declare.class */
public abstract class Declare extends PatternNode {
    public static final byte ERROR_OR_WARNING = 1;
    public static final byte PARENTS = 2;
    public static final byte SOFT = 3;
    public static final byte DOMINATES = 4;
    public static final byte ANNOTATION = 5;
    public static final byte PARENTSMIXIN = 6;
    public static final byte TYPE_ERROR_OR_WARNING = 7;
    private ResolvedType declaringType;

    public abstract void resolve(IScope iScope);

    public abstract Declare parameterizeWith(Map<String, UnresolvedType> map, World world);

    public abstract boolean isAdviceLike();

    public abstract String getNameSuffix();

    public static Declare read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        byte kind = s.readByte();
        switch (kind) {
            case 1:
                return DeclareErrorOrWarning.read(s, context);
            case 2:
                return DeclareParents.read(s, context);
            case 3:
                return DeclareSoft.read(s, context);
            case 4:
                return DeclarePrecedence.read(s, context);
            case 5:
                return DeclareAnnotation.read(s, context);
            case 6:
                return DeclareParentsMixin.read(s, context);
            case 7:
                return DeclareTypeErrorOrWarning.read(s, context);
            default:
                throw new RuntimeException("unimplemented");
        }
    }

    public void setDeclaringType(ResolvedType aType) {
        this.declaringType = aType;
    }

    public ResolvedType getDeclaringType() {
        return this.declaringType;
    }
}
