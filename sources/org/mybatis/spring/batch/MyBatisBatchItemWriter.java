package org.mybatis.spring.batch;

import java.util.List;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.util.Assert;

/* loaded from: mybatis-spring-1.3.2.jar:org/mybatis/spring/batch/MyBatisBatchItemWriter.class */
public class MyBatisBatchItemWriter<T> implements ItemWriter<T>, InitializingBean {
    private static final Log LOGGER = LogFactory.getLog((Class<?>) MyBatisBatchItemWriter.class);
    private SqlSessionTemplate sqlSessionTemplate;
    private String statementId;
    private boolean assertUpdates = true;

    public void setAssertUpdates(boolean assertUpdates) {
        this.assertUpdates = assertUpdates;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        if (this.sqlSessionTemplate == null) {
            this.sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
        }
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        Assert.notNull(this.sqlSessionTemplate, "A SqlSessionFactory or a SqlSessionTemplate is required.");
        Assert.isTrue(ExecutorType.BATCH == this.sqlSessionTemplate.getExecutorType(), "SqlSessionTemplate's executor type must be BATCH");
        Assert.notNull(this.statementId, "A statementId is required.");
    }

    public void write(List<? extends T> items) {
        if (!items.isEmpty()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Executing batch with " + items.size() + " items.");
            }
            for (T item : items) {
                this.sqlSessionTemplate.update(this.statementId, item);
            }
            List<BatchResult> results = this.sqlSessionTemplate.flushStatements();
            if (this.assertUpdates) {
                if (results.size() != 1) {
                    throw new InvalidDataAccessResourceUsageException("Batch execution returned invalid results. Expected 1 but number of BatchResult objects returned was " + results.size());
                }
                int[] updateCounts = results.get(0).getUpdateCounts();
                for (int i = 0; i < updateCounts.length; i++) {
                    int value = updateCounts[i];
                    if (value == 0) {
                        throw new EmptyResultDataAccessException("Item " + i + " of " + updateCounts.length + " did not update any rows: [" + items.get(i) + "]", 1);
                    }
                }
            }
        }
    }
}
