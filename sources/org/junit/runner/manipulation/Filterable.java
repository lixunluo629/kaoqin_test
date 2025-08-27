package org.junit.runner.manipulation;

/* loaded from: junit-4.12.jar:org/junit/runner/manipulation/Filterable.class */
public interface Filterable {
    void filter(Filter filter) throws NoTestsRemainException;
}
