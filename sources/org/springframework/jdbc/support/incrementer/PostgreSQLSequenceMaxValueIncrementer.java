package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

@Deprecated
/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/incrementer/PostgreSQLSequenceMaxValueIncrementer.class */
public class PostgreSQLSequenceMaxValueIncrementer extends PostgresSequenceMaxValueIncrementer {
    public PostgreSQLSequenceMaxValueIncrementer() {
    }

    public PostgreSQLSequenceMaxValueIncrementer(DataSource dataSource, String incrementerName) {
        super(dataSource, incrementerName);
    }
}
