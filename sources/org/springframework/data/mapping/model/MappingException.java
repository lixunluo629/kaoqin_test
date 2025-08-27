package org.springframework.data.mapping.model;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/mapping/model/MappingException.class */
public class MappingException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public MappingException(String s) {
        super(s);
    }

    public MappingException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
