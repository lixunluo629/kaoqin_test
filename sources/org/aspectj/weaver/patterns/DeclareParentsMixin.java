package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.List;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.VersionedDataInputStream;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/DeclareParentsMixin.class */
public class DeclareParentsMixin extends DeclareParents {
    private int bitflags;

    public DeclareParentsMixin(TypePattern child, List parents) {
        super(child, parents, true);
        this.bitflags = 0;
    }

    public DeclareParentsMixin(TypePattern child, TypePatternList parents) {
        super(child, parents, true);
        this.bitflags = 0;
    }

    @Override // org.aspectj.weaver.patterns.DeclareParents
    public boolean equals(Object other) {
        if (!(other instanceof DeclareParentsMixin)) {
            return false;
        }
        DeclareParentsMixin o = (DeclareParentsMixin) other;
        return o.child.equals(this.child) && o.parents.equals(this.parents) && o.bitflags == this.bitflags;
    }

    @Override // org.aspectj.weaver.patterns.DeclareParents
    public int hashCode() {
        int result = (37 * 23) + this.child.hashCode();
        return (37 * ((37 * result) + this.parents.hashCode())) + this.bitflags;
    }

    @Override // org.aspectj.weaver.patterns.DeclareParents, org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(6);
        this.child.write(s);
        this.parents.write(s);
        writeLocation(s);
        s.writeInt(this.bitflags);
    }

    public static Declare read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        DeclareParentsMixin ret = new DeclareParentsMixin(TypePattern.read(s, context), TypePatternList.read(s, context));
        ret.readLocation(context, s);
        ret.bitflags = s.readInt();
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.DeclareParents
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("declare parents mixin: ");
        buf.append(this.child);
        buf.append(" implements ");
        buf.append(this.parents);
        buf.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        buf.append("bits=0x").append(Integer.toHexString(this.bitflags));
        return buf.toString();
    }

    @Override // org.aspectj.weaver.patterns.DeclareParents
    public boolean isMixin() {
        return true;
    }
}
