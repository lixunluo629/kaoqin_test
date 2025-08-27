package org.springframework.core;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/NestedCheckedException.class */
public abstract class NestedCheckedException extends Exception {
    private static final long serialVersionUID = 7100714597678207546L;

    static {
        NestedExceptionUtils.class.getName();
    }

    public NestedCheckedException(String msg) {
        super(msg);
    }

    public NestedCheckedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
    }

    public Throwable getRootCause() {
        return NestedExceptionUtils.getRootCause(this);
    }

    public Throwable getMostSpecificCause() {
        Throwable rootCause = getRootCause();
        return rootCause != null ? rootCause : this;
    }

    public boolean contains(Class<?> exType) {
        if (exType == null) {
            return false;
        }
        if (exType.isInstance(this)) {
            return true;
        }
        Throwable cause = getCause();
        if (cause == this) {
            return false;
        }
        if (cause instanceof NestedCheckedException) {
            return ((NestedCheckedException) cause).contains(exType);
        }
        while (cause != null) {
            if (exType.isInstance(cause)) {
                return true;
            }
            if (cause.getCause() != cause) {
                cause = cause.getCause();
            } else {
                return false;
            }
        }
        return false;
    }
}
