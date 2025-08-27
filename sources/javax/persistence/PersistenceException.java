package javax.persistence;

/* loaded from: persistence-api-1.0.jar:javax/persistence/PersistenceException.class */
public class PersistenceException extends RuntimeException {
    public PersistenceException() {
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
