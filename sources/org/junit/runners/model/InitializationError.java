package org.junit.runners.model;

import java.util.Arrays;
import java.util.List;

/* loaded from: junit-4.12.jar:org/junit/runners/model/InitializationError.class */
public class InitializationError extends Exception {
    private static final long serialVersionUID = 1;
    private final List<Throwable> fErrors;

    public InitializationError(List<Throwable> errors) {
        this.fErrors = errors;
    }

    public InitializationError(Throwable error) {
        this((List<Throwable>) Arrays.asList(error));
    }

    public InitializationError(String string) {
        this(new Exception(string));
    }

    public List<Throwable> getCauses() {
        return this.fErrors;
    }
}
