package org.springframework.data.mapping.model;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/IllegalMappingException.class */
public class IllegalMappingException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public IllegalMappingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public IllegalMappingException(String s) {
        super(s);
    }
}
