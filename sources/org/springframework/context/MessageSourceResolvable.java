package org.springframework.context;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/MessageSourceResolvable.class */
public interface MessageSourceResolvable {
    String[] getCodes();

    Object[] getArguments();

    String getDefaultMessage();
}
