package org.junit.internal;

import java.io.PrintStream;

/* loaded from: junit-4.12.jar:org/junit/internal/RealSystem.class */
public class RealSystem implements JUnitSystem {
    @Override // org.junit.internal.JUnitSystem
    @Deprecated
    public void exit(int code) {
        System.exit(code);
    }

    @Override // org.junit.internal.JUnitSystem
    public PrintStream out() {
        return System.out;
    }
}
