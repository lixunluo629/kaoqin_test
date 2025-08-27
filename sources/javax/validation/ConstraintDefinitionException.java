package javax.validation;

/* loaded from: validation-api-1.1.0.Final.jar:javax/validation/ConstraintDefinitionException.class */
public class ConstraintDefinitionException extends ValidationException {
    public ConstraintDefinitionException(String message) {
        super(message);
    }

    public ConstraintDefinitionException() {
    }

    public ConstraintDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstraintDefinitionException(Throwable cause) {
        super(cause);
    }
}
