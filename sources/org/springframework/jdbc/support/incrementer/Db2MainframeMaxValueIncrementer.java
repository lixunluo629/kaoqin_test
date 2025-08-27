package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/Db2MainframeMaxValueIncrementer.class */
public class Db2MainframeMaxValueIncrementer extends AbstractSequenceMaxValueIncrementer {
    public Db2MainframeMaxValueIncrementer() {
    }

    public Db2MainframeMaxValueIncrementer(DataSource dataSource, String incrementerName) {
        super(dataSource, incrementerName);
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer
    protected String getSequenceQuery() {
        return "select next value for " + getIncrementerName() + " from sysibm.sysdummy1";
    }
}
