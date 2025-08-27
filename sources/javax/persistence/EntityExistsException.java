package javax.persistence;

/* loaded from: persistence-api-1.0.jar:javax/persistence/EntityExistsException.class */
public class EntityExistsException extends PersistenceException {
    public EntityExistsException() {
    }

    public EntityExistsException(String message) {
        super(message);
    }

    public EntityExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityExistsException(Throwable cause) {
        super(cause);
    }
}
