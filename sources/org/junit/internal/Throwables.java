package org.junit.internal;

/* loaded from: junit-4.12.jar:org/junit/internal/Throwables.class */
public final class Throwables {
    private Throwables() {
    }

    public static Exception rethrowAsException(Throwable e) throws Exception {
        rethrow(e);
        return null;
    }

    private static <T extends Throwable> void rethrow(Throwable e) throws Throwable {
        throw e;
    }
}
