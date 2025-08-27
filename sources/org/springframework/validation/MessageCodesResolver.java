package org.springframework.validation;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/validation/MessageCodesResolver.class */
public interface MessageCodesResolver {
    String[] resolveMessageCodes(String str, String str2);

    String[] resolveMessageCodes(String str, String str2, String str3, Class<?> cls);
}
