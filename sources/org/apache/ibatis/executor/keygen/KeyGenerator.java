package org.apache.ibatis.executor.keygen;

import java.sql.Statement;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/keygen/KeyGenerator.class */
public interface KeyGenerator {
    void processBefore(Executor executor, MappedStatement mappedStatement, Statement statement, Object obj);

    void processAfter(Executor executor, MappedStatement mappedStatement, Statement statement, Object obj);
}
