package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/AbstractDataFieldMaxValueIncrementer.class */
public abstract class AbstractDataFieldMaxValueIncrementer implements DataFieldMaxValueIncrementer, InitializingBean {
    private DataSource dataSource;
    private String incrementerName;
    protected int paddingLength = 0;

    protected abstract long getNextKey();

    public AbstractDataFieldMaxValueIncrementer() {
    }

    public AbstractDataFieldMaxValueIncrementer(DataSource dataSource, String incrementerName) {
        Assert.notNull(dataSource, "DataSource must not be null");
        Assert.notNull(incrementerName, "Incrementer name must not be null");
        this.dataSource = dataSource;
        this.incrementerName = incrementerName;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void setIncrementerName(String incrementerName) {
        this.incrementerName = incrementerName;
    }

    public String getIncrementerName() {
        return this.incrementerName;
    }

    public void setPaddingLength(int paddingLength) {
        this.paddingLength = paddingLength;
    }

    public int getPaddingLength() {
        return this.paddingLength;
    }

    public void afterPropertiesSet() {
        if (this.dataSource == null) {
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }
        if (this.incrementerName == null) {
            throw new IllegalArgumentException("Property 'incrementerName' is required");
        }
    }

    @Override // org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer
    public int nextIntValue() throws DataAccessException {
        return (int) getNextKey();
    }

    @Override // org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer
    public long nextLongValue() throws DataAccessException {
        return getNextKey();
    }

    @Override // org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer
    public String nextStringValue() throws DataAccessException {
        String s = Long.toString(getNextKey());
        int len = s.length();
        if (len < this.paddingLength) {
            StringBuilder sb = new StringBuilder(this.paddingLength);
            for (int i = 0; i < this.paddingLength - len; i++) {
                sb.append('0');
            }
            sb.append(s);
            s = sb.toString();
        }
        return s;
    }
}
