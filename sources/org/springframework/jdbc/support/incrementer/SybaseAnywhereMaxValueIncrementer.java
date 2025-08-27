package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/SybaseAnywhereMaxValueIncrementer.class */
public class SybaseAnywhereMaxValueIncrementer extends SybaseMaxValueIncrementer {
    public SybaseAnywhereMaxValueIncrementer() {
    }

    public SybaseAnywhereMaxValueIncrementer(DataSource dataSource, String incrementerName, String columnName) {
        super(dataSource, incrementerName, columnName);
    }

    @Override // org.springframework.jdbc.support.incrementer.SybaseMaxValueIncrementer, org.springframework.jdbc.support.incrementer.AbstractIdentityColumnMaxValueIncrementer
    protected String getIncrementStatement() {
        return "insert into " + getIncrementerName() + " values(DEFAULT)";
    }
}
