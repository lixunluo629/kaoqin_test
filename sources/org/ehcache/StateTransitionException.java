package org.ehcache;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/StateTransitionException.class */
public class StateTransitionException extends RuntimeException {
    private static final long serialVersionUID = 7602752670854885218L;

    public StateTransitionException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
