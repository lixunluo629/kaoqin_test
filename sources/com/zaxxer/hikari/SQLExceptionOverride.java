package com.zaxxer.hikari;

import java.sql.SQLException;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/SQLExceptionOverride.class */
public interface SQLExceptionOverride {

    /* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/SQLExceptionOverride$Override.class */
    public enum Override {
        CONTINUE_EVICT,
        DO_NOT_EVICT
    }

    default Override adjudicate(SQLException sqlException) {
        return Override.CONTINUE_EVICT;
    }
}
