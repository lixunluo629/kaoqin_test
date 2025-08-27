package io.netty.util;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/UncheckedBooleanSupplier.class */
public interface UncheckedBooleanSupplier extends BooleanSupplier {
    public static final UncheckedBooleanSupplier FALSE_SUPPLIER = new UncheckedBooleanSupplier() { // from class: io.netty.util.UncheckedBooleanSupplier.1
        @Override // io.netty.util.UncheckedBooleanSupplier, io.netty.util.BooleanSupplier
        public boolean get() {
            return false;
        }
    };
    public static final UncheckedBooleanSupplier TRUE_SUPPLIER = new UncheckedBooleanSupplier() { // from class: io.netty.util.UncheckedBooleanSupplier.2
        @Override // io.netty.util.UncheckedBooleanSupplier, io.netty.util.BooleanSupplier
        public boolean get() {
            return true;
        }
    };

    @Override // io.netty.util.BooleanSupplier
    boolean get();
}
