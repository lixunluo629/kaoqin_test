package org.aspectj.weaver.patterns;

import org.aspectj.weaver.IHasPosition;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/patterns/ParserException.class */
public class ParserException extends RuntimeException {
    private IHasPosition token;

    public ParserException(String message, IHasPosition token) {
        super(message);
        this.token = token;
    }

    public IHasPosition getLocation() {
        return this.token;
    }
}
