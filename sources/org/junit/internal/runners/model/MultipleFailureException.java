package org.junit.internal.runners.model;

import java.util.List;

@Deprecated
/* loaded from: junit-4.12.jar:org/junit/internal/runners/model/MultipleFailureException.class */
public class MultipleFailureException extends org.junit.runners.model.MultipleFailureException {
    private static final long serialVersionUID = 1;

    public MultipleFailureException(List<Throwable> errors) {
        super(errors);
    }
}
