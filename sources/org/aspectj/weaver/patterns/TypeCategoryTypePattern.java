package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Map;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/TypeCategoryTypePattern.class */
public class TypeCategoryTypePattern extends TypePattern {
    public static final int CLASS = 1;
    public static final int INTERFACE = 2;
    public static final int ASPECT = 3;
    public static final int INNER = 4;
    public static final int ANONYMOUS = 5;
    public static final int ENUM = 6;
    public static final int ANNOTATION = 7;
    public static final int FINAL = 8;
    private int category;
    private int VERSION;

    public TypeCategoryTypePattern(int category) {
        super(false);
        this.VERSION = 1;
        this.category = category;
    }

    public int getTypeCategory() {
        return this.category;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type) {
        return isRightCategory(type);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    protected boolean matchesExactly(ResolvedType type, ResolvedType annotatedType) {
        return isRightCategory(type);
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public FuzzyBoolean matchesInstanceof(ResolvedType type) {
        return FuzzyBoolean.fromBoolean(isRightCategory(type));
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        return this;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public boolean equals(Object other) {
        if (!(other instanceof TypeCategoryTypePattern)) {
            return false;
        }
        TypeCategoryTypePattern o = (TypeCategoryTypePattern) other;
        return o.category == this.category;
    }

    public int hashCode() {
        return this.category * 37;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(12);
        s.writeInt(this.VERSION);
        s.writeInt(this.category);
        writeLocation(s);
    }

    public static TypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        s.readInt();
        int category = s.readInt();
        TypeCategoryTypePattern tp = new TypeCategoryTypePattern(category);
        tp.readLocation(context, s);
        return tp;
    }

    private boolean isRightCategory(ResolvedType type) {
        switch (this.category) {
            case 1:
                return type.isClass();
            case 2:
                return type.isInterface();
            case 3:
                return type.isAspect();
            case 4:
                return type.isNested();
            case 5:
                return type.isAnonymous();
            case 6:
                return type.isEnum();
            case 7:
                return type.isAnnotation();
            case 8:
                return Modifier.isFinal(type.getModifiers());
            default:
                return false;
        }
    }
}
