package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.ast.Test;
import org.aspectj.weaver.patterns.PerClause;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/PerFromSuper.class */
public class PerFromSuper extends PerClause {
    private PerClause.Kind kind;

    public PerFromSuper(PerClause.Kind kind) {
        this.kind = kind;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public int couldMatchKinds() {
        return Shadow.ALL_SHADOW_KINDS_BITS;
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public FuzzyBoolean fastMatch(FastMatchInfo type) {
        throw new RuntimeException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected FuzzyBoolean matchInternal(Shadow shadow) {
        throw new RuntimeException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public void resolveBindings(IScope scope, Bindings bindings) {
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    protected Test findResidueInternal(Shadow shadow, ExposedState state) {
        throw new RuntimeException("unimplemented");
    }

    @Override // org.aspectj.weaver.patterns.PerClause
    public PerClause concretize(ResolvedType inAspect) throws AbortException {
        PerClause p = lookupConcretePerClause(inAspect.getSuperclass());
        if (p == null) {
            inAspect.getWorld().getMessageHandler().handleMessage(MessageUtil.error(WeaverMessages.format(WeaverMessages.MISSING_PER_CLAUSE, inAspect.getSuperclass()), getSourceLocation()));
            return new PerSingleton().concretize(inAspect);
        }
        if (p.getKind() != this.kind) {
            inAspect.getWorld().getMessageHandler().handleMessage(MessageUtil.error(WeaverMessages.format(WeaverMessages.WRONG_PER_CLAUSE, this.kind, p.getKind()), getSourceLocation()));
        }
        return p.concretize(inAspect);
    }

    @Override // org.aspectj.weaver.patterns.Pointcut
    public Pointcut parameterizeWith(Map<String, UnresolvedType> typeVariableMap, World w) {
        return this;
    }

    public PerClause lookupConcretePerClause(ResolvedType lookupType) {
        PerClause ret = lookupType.getPerClause();
        if (ret == null) {
            return null;
        }
        if (ret instanceof PerFromSuper) {
            return lookupConcretePerClause(lookupType.getSuperclass());
        }
        return ret;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        FROMSUPER.write(s);
        this.kind.write(s);
        writeLocation(s);
    }

    public static PerClause readPerClause(VersionedDataInputStream s, ISourceContext context) throws IOException {
        PerFromSuper ret = new PerFromSuper(PerClause.Kind.read(s));
        ret.readLocation(context, s);
        return ret;
    }

    public String toString() {
        return "perFromSuper(" + this.kind + ", " + this.inAspect + ")";
    }

    @Override // org.aspectj.weaver.patterns.PerClause
    public String toDeclarationString() {
        return "";
    }

    @Override // org.aspectj.weaver.patterns.PerClause
    public PerClause.Kind getKind() {
        return this.kind;
    }

    public boolean equals(Object other) {
        if (!(other instanceof PerFromSuper)) {
            return false;
        }
        PerFromSuper pc = (PerFromSuper) other;
        return pc.kind.equals(this.kind) && (pc.inAspect != null ? pc.inAspect.equals(this.inAspect) : this.inAspect == null);
    }

    public int hashCode() {
        int result = (37 * 17) + this.kind.hashCode();
        return (37 * result) + (this.inAspect == null ? 0 : this.inAspect.hashCode());
    }
}
