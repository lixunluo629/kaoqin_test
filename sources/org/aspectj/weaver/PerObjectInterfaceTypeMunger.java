package org.aspectj.weaver;

import java.io.IOException;
import org.aspectj.weaver.patterns.PerFromSuper;
import org.aspectj.weaver.patterns.PerObject;
import org.aspectj.weaver.patterns.PerThisOrTargetPointcutVisitor;
import org.aspectj.weaver.patterns.Pointcut;
import org.aspectj.weaver.patterns.TypePattern;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/PerObjectInterfaceTypeMunger.class */
public class PerObjectInterfaceTypeMunger extends ResolvedTypeMunger {
    private final UnresolvedType interfaceType;
    private final Pointcut testPointcut;
    private TypePattern lazyTestTypePattern;
    private volatile int hashCode;

    public boolean equals(Object other) {
        if (other == null || !(other instanceof PerObjectInterfaceTypeMunger)) {
            return false;
        }
        PerObjectInterfaceTypeMunger o = (PerObjectInterfaceTypeMunger) other;
        if (this.testPointcut != null ? this.testPointcut.equals(o.testPointcut) : o.testPointcut == null) {
            if (this.lazyTestTypePattern != null ? this.lazyTestTypePattern.equals(o.lazyTestTypePattern) : o.lazyTestTypePattern == null) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int result = (37 * 17) + (this.testPointcut == null ? 0 : this.testPointcut.hashCode());
            this.hashCode = (37 * result) + (this.lazyTestTypePattern == null ? 0 : this.lazyTestTypePattern.hashCode());
        }
        return this.hashCode;
    }

    public PerObjectInterfaceTypeMunger(UnresolvedType aspectType, Pointcut testPointcut) {
        super(PerObjectInterface, null);
        this.hashCode = 0;
        this.testPointcut = testPointcut;
        this.interfaceType = AjcMemberMaker.perObjectInterfaceType(aspectType);
    }

    private TypePattern getTestTypePattern(ResolvedType aspectType) {
        boolean isPerThis;
        if (this.lazyTestTypePattern == null) {
            if (aspectType.getPerClause() instanceof PerFromSuper) {
                PerFromSuper ps = (PerFromSuper) aspectType.getPerClause();
                isPerThis = ((PerObject) ps.lookupConcretePerClause(aspectType)).isThis();
            } else {
                isPerThis = ((PerObject) aspectType.getPerClause()).isThis();
            }
            PerThisOrTargetPointcutVisitor v = new PerThisOrTargetPointcutVisitor(!isPerThis, aspectType);
            this.lazyTestTypePattern = v.getPerTypePointcut(this.testPointcut);
            this.hashCode = 0;
        }
        return this.lazyTestTypePattern;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public void write(CompressingDataOutputStream s) throws IOException {
        throw new RuntimeException("shouldn't be serialized");
    }

    public UnresolvedType getInterfaceType() {
        return this.interfaceType;
    }

    public Pointcut getTestPointcut() {
        return this.testPointcut;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public boolean matches(ResolvedType matchType, ResolvedType aspectType) {
        if (matchType.isInterface()) {
            return false;
        }
        return getTestTypePattern(aspectType).matchesStatically(matchType);
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public boolean isLateMunger() {
        return true;
    }
}
