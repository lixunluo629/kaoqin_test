package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/ComputationException.class */
public class ComputationException extends RuntimeException {
    private static final long serialVersionUID = 0;

    public ComputationException(@Nullable Throwable cause) {
        super(cause);
    }
}
