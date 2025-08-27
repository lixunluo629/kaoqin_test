package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

@Deprecated
/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/DB2MainframeSequenceMaxValueIncrementer.class */
public class DB2MainframeSequenceMaxValueIncrementer extends AbstractSequenceMaxValueIncrementer {
    public DB2MainframeSequenceMaxValueIncrementer() {
    }

    public DB2MainframeSequenceMaxValueIncrementer(DataSource dataSource, String incrementerName) {
        super(dataSource, incrementerName);
    }

    @Override // org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer
    protected String getSequenceQuery() {
        return "select next value for " + getIncrementerName() + " from sysibm.sysdummy1";
    }
}
