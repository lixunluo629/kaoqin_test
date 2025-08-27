package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/DerbyMaxValueIncrementer.class */
public class DerbyMaxValueIncrementer extends AbstractIdentityColumnMaxValueIncrementer {
    private static final String DEFAULT_DUMMY_NAME = "dummy";
    private String dummyName;

    public DerbyMaxValueIncrementer() {
        this.dummyName = DEFAULT_DUMMY_NAME;
    }

    public DerbyMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName) {
        super(dataSource, incrementerName, columnName);
        this.dummyName = DEFAULT_DUMMY_NAME;
        this.dummyName = DEFAULT_DUMMY_NAME;
    }

    public DerbyMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName, String dummyName) {
        super(dataSource, incrementerName, columnName);
        this.dummyName = DEFAULT_DUMMY_NAME;
        this.dummyName = dummyName;
    }

    public void setDummyName(String dummyName) {
        this.dummyName = dummyName;
    }

    public String getDummyName() {
        return this.dummyName;
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractIdentityColumnMaxValueIncrementer
    protected String getIncrementStatement() {
        return "insert into " + getIncrementerName() + " (" + getDummyName() + ") values(null)";
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractIdentityColumnMaxValueIncrementer
    protected String getIdentityStatement() {
        return "select IDENTITY_VAL_LOCAL() from " + getIncrementerName();
    }
}
