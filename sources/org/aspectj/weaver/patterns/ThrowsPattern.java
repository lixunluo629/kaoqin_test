package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ThrowsPattern.class */
public class ThrowsPattern extends PatternNode {
    private TypePatternList required;
    private TypePatternList forbidden;
    public static final ThrowsPattern ANY = new ThrowsPattern(TypePatternList.EMPTY, TypePatternList.EMPTY);

    public ThrowsPattern(TypePatternList required, TypePatternList forbidden) {
        this.required = required;
        this.forbidden = forbidden;
    }

    public TypePatternList getRequired() {
        return this.required;
    }

    public TypePatternList getForbidden() {
        return this.forbidden;
    }

    public String toString() {
        if (this == ANY) {
            return "";
        }
        String ret = "throws " + this.required.toString();
        if (this.forbidden.size() > 0) {
            ret = ret + " !(" + this.forbidden.toString() + ")";
        }
        return ret;
    }

    public boolean equals(Object other) {
        if (!(other instanceof ThrowsPattern)) {
            return false;
        }
        ThrowsPattern o = (ThrowsPattern) other;
        boolean ret = o.required.equals(this.required) && o.forbidden.equals(this.forbidden);
        return ret;
    }

    public int hashCode() {
        int result = (37 * 17) + this.required.hashCode();
        return (37 * result) + this.forbidden.hashCode();
    }

    public ThrowsPattern resolveBindings(IScope scope, Bindings bindings) {
        if (this == ANY) {
            return this;
        }
        this.required = this.required.resolveBindings(scope, bindings, false, false);
        this.forbidden = this.forbidden.resolveBindings(scope, bindings, false, false);
        return this;
    }

    public ThrowsPattern parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        ThrowsPattern ret = new ThrowsPattern(this.required.parameterizeWith(typeVariableMap, w), this.forbidden.parameterizeWith(typeVariableMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    public boolean matches(UnresolvedType[] tys, World world) {
        if (this == ANY) {
            return true;
        }
        ResolvedType[] types = world.resolve(tys);
        int lenj = this.required.size();
        for (int j = 0; j < lenj; j++) {
            if (!matchesAny(this.required.get(j), types)) {
                return false;
            }
        }
        int lenj2 = this.forbidden.size();
        for (int j2 = 0; j2 < lenj2; j2++) {
            if (matchesAny(this.forbidden.get(j2), types)) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesAny(TypePattern typePattern, ResolvedType[] types) {
        for (int i = types.length - 1; i >= 0; i--) {
            if (typePattern.matchesStatically(types[i])) {
                return true;
            }
        }
        return false;
    }

    public static ThrowsPattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        TypePatternList required = TypePatternList.read(s, context);
        TypePatternList forbidden = TypePatternList.read(s, context);
        if (required.size() == 0 && forbidden.size() == 0) {
            return ANY;
        }
        ThrowsPattern ret = new ThrowsPattern(required, forbidden);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        this.required.write(s);
        this.forbidden.write(s);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object traverse(PatternNodeVisitor visitor, Object data) {
        Object ret = accept(visitor, data);
        this.forbidden.traverse(visitor, data);
        this.required.traverse(visitor, data);
        return ret;
    }
}
