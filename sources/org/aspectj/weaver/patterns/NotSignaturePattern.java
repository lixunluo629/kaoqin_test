package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.World;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/NotSignaturePattern.class */
public class NotSignaturePattern extends AbstractSignaturePattern {
    private ISignaturePattern negatedSp;

    public NotSignaturePattern(ISignaturePattern negatedSp) {
        this.negatedSp = negatedSp;
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public boolean couldEverMatch(ResolvedType type) {
        return this.negatedSp.getExactDeclaringTypes().size() == 0 || !this.negatedSp.couldEverMatch(type);
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public List<ExactTypePattern> getExactDeclaringTypes() {
        return this.negatedSp.getExactDeclaringTypes();
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public boolean isMatchOnAnyName() {
        return this.negatedSp.isMatchOnAnyName();
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public boolean isStarAnnotation() {
        return this.negatedSp.isStarAnnotation();
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public boolean matches(Member member, World world, boolean b) {
        return !this.negatedSp.matches(member, world, b);
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public ISignaturePattern parameterizeWith(Map<String, UnresolvedType> typeVariableBindingMap, World world) {
        return new NotSignaturePattern(this.negatedSp.parameterizeWith(typeVariableBindingMap, world));
    }

    @Override // org.aspectj.weaver.patterns.ISignaturePattern
    public ISignaturePattern resolveBindings(IScope scope, Bindings bindings) {
        this.negatedSp.resolveBindings(scope, bindings);
        return this;
    }

    public static ISignaturePattern readNotSignaturePattern(VersionedDataInputStream s, ISourceContext context) throws IOException {
        NotSignaturePattern ret = new NotSignaturePattern(readCompoundSignaturePattern(s, context));
        s.readInt();
        s.readInt();
        return ret;
    }

    public ISignaturePattern getNegated() {
        return this.negatedSp;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("!").append(this.negatedSp.toString());
        return sb.toString();
    }
}
