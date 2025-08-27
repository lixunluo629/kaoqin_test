package org.springframework.jdbc;

import java.io.IOException;
import org.springframework.dao.DataRetrievalFailureException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/LobRetrievalFailureException.class */
public class LobRetrievalFailureException extends DataRetrievalFailureException {
    public LobRetrievalFailureException(String msg) {
        super(msg);
    }

    public LobRetrievalFailureException(String msg, IOException ex) {
        super(msg, ex);
    }
}
