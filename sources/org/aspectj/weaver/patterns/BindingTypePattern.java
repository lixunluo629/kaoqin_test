package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.IntMap;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/BindingTypePattern.class */
public class BindingTypePattern extends ExactTypePattern implements BindingPattern {
    private int formalIndex;
    private String bindingName;

    public BindingTypePattern(UnresolvedType type, int index, boolean isVarArgs) {
        super(type, false, isVarArgs);
        this.formalIndex = index;
    }

    public BindingTypePattern(FormalBinding binding, boolean isVarArgs) {
        this(binding.getType(), binding.getIndex(), isVarArgs);
        this.bindingName = binding.getName();
    }

    @Override // org.aspectj.weaver.patterns.BindingPattern
    public int getFormalIndex() {
        return this.formalIndex;
    }

    public String getBindingName() {
        return this.bindingName;
    }

    @Override // org.aspectj.weaver.patterns.ExactTypePattern
    public boolean equals(Object other) {
        if (!(other instanceof BindingTypePattern)) {
            return false;
        }
        BindingTypePattern o = (BindingTypePattern) other;
        return this.includeSubtypes == o.includeSubtypes && this.isVarArgs == o.isVarArgs && o.type.equals(this.type) && o.formalIndex == this.formalIndex;
    }

    @Override // org.aspectj.weaver.patterns.ExactTypePattern
    public int hashCode() {
        int result = (37 * 17) + super.hashCode();
        return (37 * result) + this.formalIndex;
    }

    @Override // org.aspectj.weaver.patterns.ExactTypePattern, org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream out) throws IOException {
        out.writeByte(3);
        this.type.write(out);
        out.writeShort((short) this.formalIndex);
        out.writeBoolean(this.isVarArgs);
        writeLocation(out);
    }

    public static TypePattern read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        UnresolvedType type = UnresolvedType.read(s);
        int index = s.readShort();
        boolean isVarargs = false;
        if (s.getMajorVersion() >= 2) {
            isVarargs = s.readBoolean();
        }
        TypePattern ret = new BindingTypePattern(type, index, isVarargs);
        ret.readLocation(context, s);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.TypePattern
    public TypePattern remapAdviceFormals(IntMap bindings) {
        if (!bindings.hasKey(this.formalIndex)) {
            return new ExactTypePattern(this.type, false, this.isVarArgs);
        }
        int newFormalIndex = bindings.get(this.formalIndex);
        return new BindingTypePattern(this.type, newFormalIndex, this.isVarArgs);
    }

    @Override // org.aspectj.weaver.patterns.ExactTypePattern, org.aspectj.weaver.patterns.TypePattern
    public TypePattern parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        ExactTypePattern superParameterized = (ExactTypePattern) super.parameterizeWith(typeVariableMap, w);
        BindingTypePattern ret = new BindingTypePattern(superParameterized.getExactType(), this.formalIndex, this.isVarArgs);
        ret.copyLocationFrom(this);
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.ExactTypePattern
    public String toString() {
        return "BindingTypePattern(" + super.toString() + ", " + this.formalIndex + ")";
    }
}
