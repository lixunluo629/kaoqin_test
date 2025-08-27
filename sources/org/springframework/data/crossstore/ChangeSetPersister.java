package org.springframework.data.crossstore;

import org.springframework.dao.DataAccessException;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/crossstore/ChangeSetPersister.class */
public interface ChangeSetPersister<K> {
    public static final String ID_KEY = "_id";
    public static final String CLASS_KEY = "_class";

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/crossstore/ChangeSetPersister$NotFoundException.class */
    public static class NotFoundException extends Exception {
        private static final long serialVersionUID = -8604207973816331140L;
    }

    void getPersistentState(Class<? extends ChangeSetBacked> cls, K k, ChangeSet changeSet) throws DataAccessException, NotFoundException;

    K getPersistentId(ChangeSetBacked changeSetBacked, ChangeSet changeSet) throws DataAccessException;

    K persistState(ChangeSetBacked changeSetBacked, ChangeSet changeSet) throws DataAccessException;
}
