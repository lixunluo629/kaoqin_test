package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/AbstractColumnMaxValueIncrementer.class */
public abstract class AbstractColumnMaxValueIncrementer extends AbstractDataFieldMaxValueIncrementer {
    private String columnName;
    private int cacheSize;

    public AbstractColumnMaxValueIncrementer() {
        this.cacheSize = 1;
    }

    public AbstractColumnMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName) {
        super(dataSource, incrementerName);
        this.cacheSize = 1;
        Assert.notNull(columnName, "Column name must not be null");
        this.columnName = columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public int getCacheSize() {
        return this.cacheSize;
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        if (this.columnName == null) {
            throw new IllegalArgumentException("Property 'columnName' is required");
        }
    }
}
