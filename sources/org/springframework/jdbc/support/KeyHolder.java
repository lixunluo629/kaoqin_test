package org.springframework.jdbc.support;

import java.util.List;
import java.util.Map;
import org.springframework.dao.InvalidDataAccessApiUsageException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/KeyHolder.class */
public interface KeyHolder {
    Number getKey() throws InvalidDataAccessApiUsageException;

    Map<String, Object> getKeys() throws InvalidDataAccessApiUsageException;

    List<Map<String, Object>> getKeyList();
}
