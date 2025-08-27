package org.apache.commons.io.output;

import java.util.Objects;
import org.apache.commons.io.function.Uncheck;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/UncheckedAppendableImpl.class */
final class UncheckedAppendableImpl implements UncheckedAppendable {
    private final Appendable appendable;

    UncheckedAppendableImpl(Appendable appendable) {
        this.appendable = (Appendable) Objects.requireNonNull(appendable, "appendable");
    }

    @Override // org.apache.commons.io.output.UncheckedAppendable, java.lang.Appendable
    public UncheckedAppendable append(char c) {
        Appendable appendable = this.appendable;
        Objects.requireNonNull(appendable);
        Uncheck.apply((v1) -> {
            return r0.append(v1);
        }, Character.valueOf(c));
        return this;
    }

    @Override // org.apache.commons.io.output.UncheckedAppendable, java.lang.Appendable
    public UncheckedAppendable append(CharSequence csq) {
        Appendable appendable = this.appendable;
        Objects.requireNonNull(appendable);
        Uncheck.apply(appendable::append, csq);
        return this;
    }

    @Override // org.apache.commons.io.output.UncheckedAppendable, java.lang.Appendable
    public UncheckedAppendable append(CharSequence csq, int start, int end) {
        Appendable appendable = this.appendable;
        Objects.requireNonNull(appendable);
        Uncheck.apply((v1, v2, v3) -> {
            return r0.append(v1, v2, v3);
        }, csq, Integer.valueOf(start), Integer.valueOf(end));
        return this;
    }

    public String toString() {
        return this.appendable.toString();
    }
}
