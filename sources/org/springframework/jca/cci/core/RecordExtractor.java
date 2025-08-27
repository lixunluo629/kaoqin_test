package org.springframework.jca.cci.core;

import java.sql.SQLException;
import javax.resource.ResourceException;
import javax.resource.cci.Record;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/jca/cci/core/RecordExtractor.class */
public interface RecordExtractor<T> {
    T extractData(Record record) throws SQLException, DataAccessException, ResourceException;
}
