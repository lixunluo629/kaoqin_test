package org.terracotta.context.extended;

import java.lang.Enum;
import java.util.EnumSet;
import org.terracotta.statistics.extended.CompoundOperation;
import org.terracotta.statistics.extended.SampledStatistic;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/context/extended/RegisteredRatioStatistic.class */
public class RegisteredRatioStatistic<T extends Enum<T>> extends RegisteredCompoundOperationStatistic<T> {
    private final EnumSet<T> numerator;
    private final EnumSet<T> denominator;

    public RegisteredRatioStatistic(CompoundOperation<T> compoundOperation, EnumSet<T> numerator, EnumSet<T> denominator) {
        super(compoundOperation);
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override // org.terracotta.context.extended.RegisteredStatistic
    public RegistrationType getType() {
        return RegistrationType.RATIO;
    }

    public EnumSet<T> getNumerator() {
        return this.numerator;
    }

    public EnumSet<T> getDenominator() {
        return this.denominator;
    }

    public SampledStatistic<Double> getSampledStatistic() {
        CompoundOperation<T> operation = getCompoundOperation();
        return operation.ratioOf(getNumerator(), getDenominator());
    }
}
