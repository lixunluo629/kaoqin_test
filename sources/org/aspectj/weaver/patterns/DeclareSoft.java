package org.aspectj.weaver.patterns;

import java.io.IOException;
import java.util.Map;
import org.aspectj.bridge.IMessage;
import org.aspectj.weaver.CompressingDataOutputStream;
import org.aspectj.weaver.ISourceContext;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.TypeVariableReferenceType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.VersionedDataInputStream;
import org.aspectj.weaver.WeaverMessages;
import org.aspectj.weaver.World;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/DeclareSoft.class */
public class DeclareSoft extends Declare {
    private TypePattern exception;
    private Pointcut pointcut;

    public DeclareSoft(TypePattern exception, Pointcut pointcut) {
        this.exception = exception;
        this.pointcut = pointcut;
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public Object accept(PatternNodeVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public Declare parameterizeWith(Map typeVariableBindingMap, World w) {
        DeclareSoft ret = new DeclareSoft(this.exception.parameterizeWith(typeVariableBindingMap, w), this.pointcut.parameterizeWith(typeVariableBindingMap, w));
        ret.copyLocationFrom(this);
        return ret;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("declare soft: ");
        buf.append(this.exception);
        buf.append(": ");
        buf.append(this.pointcut);
        buf.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        return buf.toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof DeclareSoft)) {
            return false;
        }
        DeclareSoft o = (DeclareSoft) other;
        return o.pointcut.equals(this.pointcut) && o.exception.equals(this.exception);
    }

    public int hashCode() {
        int result = (37 * 19) + this.pointcut.hashCode();
        return (37 * result) + this.exception.hashCode();
    }

    @Override // org.aspectj.weaver.patterns.PatternNode
    public void write(CompressingDataOutputStream s) throws IOException {
        s.writeByte(3);
        this.exception.write(s);
        this.pointcut.write(s);
        writeLocation(s);
    }

    public static Declare read(VersionedDataInputStream s, ISourceContext context) throws IOException {
        Declare ret = new DeclareSoft(TypePattern.read(s, context), Pointcut.read(s, context));
        ret.readLocation(context, s);
        return ret;
    }

    public Pointcut getPointcut() {
        return this.pointcut;
    }

    public TypePattern getException() {
        return this.exception;
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public void resolve(IScope scope) {
        this.exception = this.exception.resolveBindings(scope, null, false, true);
        ResolvedType excType = this.exception.getExactType().resolve(scope.getWorld());
        if (!excType.isMissing()) {
            if (excType.isTypeVariableReference()) {
                TypeVariableReferenceType typeVariableRT = (TypeVariableReferenceType) excType;
                excType = typeVariableRT.getTypeVariable().getFirstBound().resolve(scope.getWorld());
            }
            if (!scope.getWorld().getCoreType(UnresolvedType.THROWABLE).isAssignableFrom(excType)) {
                scope.getWorld().showMessage(IMessage.ERROR, WeaverMessages.format(WeaverMessages.NOT_THROWABLE, excType.getName()), this.exception.getSourceLocation(), null);
                this.pointcut = Pointcut.makeMatchesNothing(Pointcut.RESOLVED);
                return;
            } else if (scope.getWorld().getCoreType(UnresolvedType.RUNTIME_EXCEPTION).isAssignableFrom(excType)) {
                scope.getWorld().getLint().runtimeExceptionNotSoftened.signal(new String[]{excType.getName()}, this.exception.getSourceLocation(), null);
                this.pointcut = Pointcut.makeMatchesNothing(Pointcut.RESOLVED);
                return;
            }
        }
        this.pointcut = this.pointcut.resolve(scope);
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public boolean isAdviceLike() {
        return false;
    }

    @Override // org.aspectj.weaver.patterns.Declare
    public String getNameSuffix() {
        return AsmRelationshipUtils.DECLARE_SOFT;
    }
}
