package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/HsqlMaxValueIncrementer.class */
public class HsqlMaxValueIncrementer extends AbstractIdentityColumnMaxValueIncrementer {
    public HsqlMaxValueIncrementer() {
    }

    public HsqlMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName) {
        super(dataSource, incrementerName, columnName);
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractIdentityColumnMaxValueIncrementer
    protected String getIncrementStatement() {
        return "insert into " + getIncrementerName() + " values(null)";
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractIdentityColumnMaxValueIncrementer
    protected String getIdentityStatement() {
        return "select max(identity()) from " + getIncrementerName();
    }
}
