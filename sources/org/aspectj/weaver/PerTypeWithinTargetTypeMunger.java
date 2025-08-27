package org.aspectj.weaver;

import java.io.IOException;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.patterns.PerTypeWithin;
import org.aspectj.weaver.patterns.Pointcut;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/PerTypeWithinTargetTypeMunger.class */
public class PerTypeWithinTargetTypeMunger extends ResolvedTypeMunger {
    private UnresolvedType aspectType;
    private PerTypeWithin testPointcut;
    private volatile int hashCode;

    public PerTypeWithinTargetTypeMunger(UnresolvedType aspectType, PerTypeWithin testPointcut) {
        super(PerTypeWithinInterface, null);
        this.hashCode = 0;
        this.aspectType = aspectType;
        this.testPointcut = testPointcut;
    }

    public boolean equals(Object other) {
        if (!(other instanceof PerTypeWithinTargetTypeMunger)) {
            return false;
        }
        PerTypeWithinTargetTypeMunger o = (PerTypeWithinTargetTypeMunger) other;
        if (o.testPointcut != null ? this.testPointcut.equals(o.testPointcut) : this.testPointcut == null) {
            if (o.aspectType != null ? this.aspectType.equals(o.aspectType) : this.aspectType == null) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int result = (37 * 17) + (this.testPointcut == null ? 0 : this.testPointcut.hashCode());
            this.hashCode = (37 * result) + (this.aspectType == null ? 0 : this.aspectType.hashCode());
        }
        return this.hashCode;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public void write(CompressingDataOutputStream s) throws IOException {
        throw new RuntimeException("shouldn't be serialized");
    }

    public UnresolvedType getAspectType() {
        return this.aspectType;
    }

    public Pointcut getTestPointcut() {
        return this.testPointcut;
    }

    @Override // org.aspectj.weaver.ResolvedTypeMunger
    public boolean matches(ResolvedType matchType, ResolvedType aspectType) {
        return isWithinType(matchType).alwaysTrue() && !matchType.isInterface();
    }

    private FuzzyBoolean isWithinType(ResolvedType type) {
        while (type != null) {
            if (this.testPointcut.getTypePattern().matchesStatically(type)) {
                return FuzzyBoolean.YES;
            }
            type = type.getDeclaringType();
        }
        return FuzzyBoolean.NO;
    }
}
