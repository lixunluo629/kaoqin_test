package javax.persistence;

/* loaded from: persistence-api-1.0.jar:javax/persistence/EntityManager.class */
public interface EntityManager {
    void persist(Object obj);

    <T> T merge(T t);

    void remove(Object obj);

    <T> T find(Class<T> cls, Object obj);

    <T> T getReference(Class<T> cls, Object obj);

    void flush();

    void setFlushMode(FlushModeType flushModeType);

    FlushModeType getFlushMode();

    void lock(Object obj, LockModeType lockModeType);

    void refresh(Object obj);

    void clear();

    boolean contains(Object obj);

    Query createQuery(String str);

    Query createNamedQuery(String str);

    Query createNativeQuery(String str);

    Query createNativeQuery(String str, Class cls);

    Query createNativeQuery(String str, String str2);

    void joinTransaction();

    Object getDelegate();

    void close();

    boolean isOpen();

    EntityTransaction getTransaction();
}
