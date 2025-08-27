package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/Db2LuwMaxValueIncrementer.class */
public class Db2LuwMaxValueIncrementer extends AbstractSequenceMaxValueIncrementer {
    public Db2LuwMaxValueIncrementer() {
    }

    public Db2LuwMaxValueIncrementer(DataSource dataSource, String incrementerName) {
        super(dataSource, incrementerName);
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer
    protected String getSequenceQuery() {
        return "values nextval for " + getIncrementerName();
    }
}
