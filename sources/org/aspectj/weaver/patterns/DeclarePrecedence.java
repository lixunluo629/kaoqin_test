package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.aspectj.bridge.IMessage;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/DeclarePrecedence.class */
public class DeclarePrecedence extends Declare {
    private TypePatternList patterns;
    private IScope scope;

    public DeclarePrecedence(List patterns) {
        this(new TypePatternList((List<TypePattern>) patterns));
    }

    private DeclarePrecedence(TypePatternList patterns) {
        this.scope = null;
        this.patterns = patterns;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public Declare parameterizeWith(Map typeVariableBindingMap, World w) {
        DeclarePrecedence ret = new DeclarePrecedence(this.patterns.parameterizeWith(typeVariableBindingMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("declare precedence: ");
        buf.append(this.patterns);
        buf.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        return buf.toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof DeclarePrecedence)) {
            return false;
        }
        DeclarePrecedence o = (DeclarePrecedence) other;
        return o.patterns.equals(this.patterns);
    }

    public int hashCode() {
        return this.patterns.hashCode();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(4);
        this.patterns.write(s);
        writeLocation(s);
    }

    public static Declare read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        Declare ret = new DeclarePrecedence(TypePatternList.read(s, context));
        ret.readLocation(context, s);
        return ret;
    }

    public void setScopeForResolution(IScope scope) {
        this.scope = scope;
    }

    public void ensureResolved() {
        if (this.scope != null) {
            try {
                resolve(this.scope);
            } finally {
                this.scope = null;
            }
        }
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public void resolve(IScope scope) {
        this.patterns = this.patterns.resolveBindings(scope, Bindings.NONE, false, false);
        boolean seenStar = false;
        for (int i = 0; i < this.patterns.size(); i++) {
            TypePattern pi = this.patterns.get(i);
            if (pi.isStar()) {
                if (seenStar) {
                    scope.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.TWO_STARS_IN_PRECEDENCE), pi.getSourceLocation(), null);
                }
                seenStar = true;
            } else {
                ResolvedType exactType = pi.getExactType().resolve(scope.getWorld());
                if (!exactType.isMissing()) {
                    if (!exactType.isAspect() && !exactType.isAnnotationStyleAspect() && !pi.isIncludeSubtypes() && !exactType.isTypeVariableReference()) {
                        scope.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.CLASSES_IN_PRECEDENCE, exactType.getName()), pi.getSourceLocation(), null);
                    }
                    for (int j = 0; j < this.patterns.size(); j++) {
                        if (j != i) {
                            TypePattern pj = this.patterns.get(j);
                            if (!pj.isStar() && pj.matchesStatically(exactType)) {
                                scope.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.TWO_PATTERN_MATCHES_IN_PRECEDENCE, exactType.getName()), pi.getSourceLocation(), pj.getSourceLocation());
                            }
                        }
                    }
                }
            }
        }
    }

    public TypePatternList getPatterns() {
        ensureResolved();
        return this.patterns;
    }

    private int matchingIndex(ResolvedType a) {
        ensureResolved();
        int knownMatch = -1;
        int starMatch = -1;
        int len = this.patterns.size();
        for (int i = 0; i < len; i++) {
            TypePattern p = this.patterns.get(i);
            if (p.isStar()) {
                starMatch = i;
            } else if (!p.matchesStatically(a)) {
                continue;
            } else {
                if (knownMatch != -1) {
                    a.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.MULTIPLE_MATCHES_IN_PRECEDENCE, a, this.patterns.get(knownMatch), p), this.patterns.get(knownMatch).getSourceLocation(), p.getSourceLocation());
                    return -1;
                }
                knownMatch = i;
            }
        }
        if (knownMatch == -1) {
            return starMatch;
        }
        return knownMatch;
    }

    public int compare(ResolvedType aspect1, ResolvedType aspect2) {
        ensureResolved();
        int index1 = matchingIndex(aspect1);
        int index2 = matchingIndex(aspect2);
        if (index1 == -1 || index2 == -1 || index1 == index2) {
            return 0;
        }
        if (index1 > index2) {
            return -1;
        }
        return 1;
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public boolean isAdviceLike() {
        return false;
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public String getNameSuffix() {
        return AsmRelationshipUtils.DECLARE_PRECEDENCE;
    }
}
