package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

@Deprecated
/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/DB2SequenceMaxValueIncrementer.class */
public class DB2SequenceMaxValueIncrementer extends Db2LuwMaxValueIncrementer {
    public DB2SequenceMaxValueIncrementer() {
    }

    public DB2SequenceMaxValueIncrementer(DataSource dataSource, String incrementerName) {
        super(dataSource, incrementerName);
    }
}
