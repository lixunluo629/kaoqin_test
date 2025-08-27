package javax.persistence;

/* loaded from: persistence-api-1.0.jar:javax/persistence/EntityTransaction.class */
public interface EntityTransaction {
    void begin();

    void commit();

    void rollback();

    void setRollbackOnly();

    boolean getRollbackOnly();

    boolean isActive();
}
