package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/H2SequenceMaxValueIncrementer.class */
public class H2SequenceMaxValueIncrementer extends AbstractSequenceMaxValueIncrementer {
    public H2SequenceMaxValueIncrementer() {
    }

    public H2SequenceMaxValueIncrementer(DataSource dataSource, String incrementerName) {
        super(dataSource, incrementerName);
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer
    protected String getSequenceQuery() {
        return "select " + getIncrementerName() + ".nextval from dual";
    }
}
