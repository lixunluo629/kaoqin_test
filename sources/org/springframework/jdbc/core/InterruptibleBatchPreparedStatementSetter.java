package org.springframework.jdbc.core;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/InterruptibleBatchPreparedStatementSetter.class */
public interface InterruptibleBatchPreparedStatementSetter extends BatchPreparedStatementSetter {
    boolean isBatchExhausted(int i);
}
