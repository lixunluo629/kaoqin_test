package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/PostgresSequenceMaxValueIncrementer.class */
public class PostgresSequenceMaxValueIncrementer extends AbstractSequenceMaxValueIncrementer {
    public PostgresSequenceMaxValueIncrementer() {
    }

    public PostgresSequenceMaxValueIncrementer(DataSource dataSource, String incrementerName) {
        super(dataSource, incrementerName);
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer
    protected String getSequenceQuery() {
        return "select nextval('" + getIncrementerName() + "')";
    }
}
