package org.apache.commons.io.output;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/output/UncheckedAppendable.class */
public interface UncheckedAppendable extends Appendable {
    @Override // java.lang.Appendable
    UncheckedAppendable append(char c);

    @Override // java.lang.Appendable
    UncheckedAppendable append(CharSequence charSequence);

    @Override // java.lang.Appendable
    UncheckedAppendable append(CharSequence charSequence, int i, int i2);

    static UncheckedAppendable on(Appendable appendable) {
        return new UncheckedAppendableImpl(appendable);
    }
}
