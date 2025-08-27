package org.terracotta.context.extended;

import java.lang.Enum;
import java.util.EnumSet;
import org.terracotta.statistics.extended.CompoundOperation;
import org.terracotta.statistics.extended.Result;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extended/RegisteredCompoundStatistic.class */
public class RegisteredCompoundStatistic<T extends Enum<T>> extends RegisteredCompoundOperationStatistic<T> {
    private final EnumSet<T> compound;

    public RegisteredCompoundStatistic(CompoundOperation<T> compoundOperation, EnumSet<T> compound) {
        super(compoundOperation);
        this.compound = compound;
    }

    @Override // org.terracotta.context.extended.RegisteredStatistic
    public RegistrationType getType() {
        return RegistrationType.COMPOUND;
    }

    public EnumSet<T> getCompound() {
        return this.compound;
    }

    public Result getResult() {
        return getCompoundOperation().compound(getCompound());
    }
}
