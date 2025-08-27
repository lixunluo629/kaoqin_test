package javax.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/* loaded from: persistence-api-1.0.jar:javax/persistence/Query.class */
public interface Query {
    List getResultList();

    Object getSingleResult();

    int executeUpdate();

    Query setMaxResults(int i);

    Query setFirstResult(int i);

    Query setHint(String str, Object obj);

    Query setParameter(String str, Object obj);

    Query setParameter(String str, Date date, TemporalType temporalType);

    Query setParameter(String str, Calendar calendar, TemporalType temporalType);

    Query setParameter(int i, Object obj);

    Query setParameter(int i, Date date, TemporalType temporalType);

    Query setParameter(int i, Calendar calendar, TemporalType temporalType);

    Query setFlushMode(FlushModeType flushModeType);
}
