package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/SqlServerMaxValueIncrementer.class */
public class SqlServerMaxValueIncrementer extends AbstractIdentityColumnMaxValueIncrementer {
    public SqlServerMaxValueIncrementer() {
    }

    public SqlServerMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName) {
        super(dataSource, incrementerName, columnName);
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractIdentityColumnMaxValueIncrementer
    protected String getIncrementStatement() {
        return "insert into " + getIncrementerName() + " default values";
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractIdentityColumnMaxValueIncrementer
    protected String getIdentityStatement() {
        return "select @@identity";
    }
}
