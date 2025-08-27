package org.apache.commons.io.function;

/* loaded from: commons-io-2.18.0.jar:org/apache/commons/io/function/Constants.class */
final class Constants {
    static final IOBiConsumer IO_BI_CONSUMER = (t, u) -> {
    };
    static final IORunnable IO_RUNNABLE = () -> {
    };
    static final IOBiFunction IO_BI_FUNCTION = (t, u) -> {
        return null;
    };
    static final IOFunction IO_FUNCTION_ID = t -> {
        return t;
    };
    static final IOPredicate<Object> IO_PREDICATE_FALSE = t -> {
        return false;
    };
    static final IOPredicate<Object> IO_PREDICATE_TRUE = t -> {
        return true;
    };
    static final IOTriConsumer IO_TRI_CONSUMER = (t, u, v) -> {
    };

    private Constants() {
    }
}
