package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/OracleSequenceMaxValueIncrementer.class */
public class OracleSequenceMaxValueIncrementer extends AbstractSequenceMaxValueIncrementer {
    public OracleSequenceMaxValueIncrementer() {
    }

    public OracleSequenceMaxValueIncrementer(DataSource dataSource, String incrementerName) {
        super(dataSource, incrementerName);
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer
    protected String getSequenceQuery() {
        return "select " + getIncrementerName() + ".nextval from dual";
    }
}
