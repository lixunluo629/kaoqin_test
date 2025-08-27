package javax.persistence;

/* loaded from: persistence-api-1.0.jar:javax/persistence/RollbackException.class */
public class RollbackException extends PersistenceException {
    public RollbackException() {
    }

    public RollbackException(String message) {
        super(message);
    }

    public RollbackException(String message, Throwable cause) {
        super(message, cause);
    }

    public RollbackException(Throwable cause) {
        super(cause);
    }
}
