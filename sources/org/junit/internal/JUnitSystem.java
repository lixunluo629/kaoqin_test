package org.junit.internal;

import java.io.PrintStream;

/* loaded from: junit-4.12.jar:org/junit/internal/JUnitSystem.class */
public interface JUnitSystem {
    @Deprecated
    void exit(int i);

    PrintStream out();
}
