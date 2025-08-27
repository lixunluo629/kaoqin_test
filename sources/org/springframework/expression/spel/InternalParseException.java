package org.springframework.expression.spel;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/spel/InternalParseException.class */
public class InternalParseException extends RuntimeException {
    public InternalParseException(SpelParseException cause) {
        super(cause);
    }

    @Override // java.lang.Throwable
    public SpelParseException getCause() {
        return (SpelParseException) super.getCause();
    }
}
