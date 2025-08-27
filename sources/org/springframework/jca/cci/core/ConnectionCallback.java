package org.springframework.jca.cci.core;

import java.sql.SQLException;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/cci/core/ConnectionCallback.class */
public interface ConnectionCallback<T> {
    T doInConnection(Connection connection, ConnectionFactory connectionFactory) throws SQLException, DataAccessException, ResourceException;
}
