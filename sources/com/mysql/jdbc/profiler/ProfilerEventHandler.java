package com.mysql.jdbc.profiler;

import com.mysql.jdbc.Extension;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/profiler/ProfilerEventHandler.class */
public interface ProfilerEventHandler extends Extension {
    void consumeEvent(ProfilerEvent profilerEvent);

    void processEvent(byte b, MySQLConnection mySQLConnection, Statement statement, ResultSetInternalMethods resultSetInternalMethods, long j, Throwable th, String str);
}
