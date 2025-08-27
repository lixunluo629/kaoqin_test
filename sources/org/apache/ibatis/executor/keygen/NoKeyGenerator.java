package org.apache.ibatis.executor.keygen;

import java.sql.Statement;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/keygen/NoKeyGenerator.class */
public class NoKeyGenerator implements KeyGenerator {
    public static final NoKeyGenerator INSTANCE = new NoKeyGenerator();

    @Override // org.apache.ibatis.executor.keygen.KeyGenerator
    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
    }

    @Override // org.apache.ibatis.executor.keygen.KeyGenerator
    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
    }
}
