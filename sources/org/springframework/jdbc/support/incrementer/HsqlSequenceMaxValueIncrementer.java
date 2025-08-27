package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/HsqlSequenceMaxValueIncrementer.class */
public class HsqlSequenceMaxValueIncrementer extends AbstractSequenceMaxValueIncrementer {
    public HsqlSequenceMaxValueIncrementer() {
    }

    public HsqlSequenceMaxValueIncrementer(DataSource dataSource, String incrementerName) {
        super(dataSource, incrementerName);
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer
    protected String getSequenceQuery() {
        return "call next value for " + getIncrementerName();
    }
}
