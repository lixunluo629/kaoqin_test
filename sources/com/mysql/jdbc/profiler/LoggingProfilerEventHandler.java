package com.mysql.jdbc.profiler;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Constants;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.log.Log;
import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/profiler/LoggingProfilerEventHandler.class */
public class LoggingProfilerEventHandler implements ProfilerEventHandler {
    private Log log;

    @Override // com.mysql.jdbc.profiler.ProfilerEventHandler
    public void consumeEvent(ProfilerEvent evt) {
        switch (evt.getEventType()) {
            case 0:
                this.log.logWarn(evt);
                break;
            default:
                this.log.logInfo(evt);
                break;
        }
    }

    @Override // com.mysql.jdbc.Extension
    public void destroy() {
        this.log = null;
    }

    @Override // com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException {
        this.log = conn.getLog();
    }

    @Override // com.mysql.jdbc.profiler.ProfilerEventHandler
    public void processEvent(byte eventType, MySQLConnection conn, Statement stmt, ResultSetInternalMethods resultSet, long eventDuration, Throwable eventCreationPoint, String message) {
        String catalog = "";
        if (conn != null) {
            try {
                catalog = conn.getCatalog();
            } catch (SQLException e) {
            }
        }
        consumeEvent(new ProfilerEvent(eventType, conn == null ? "" : conn.getHost(), catalog, conn == null ? -1L : conn.getId(), stmt == null ? -1 : stmt.getId(), resultSet == null ? -1 : resultSet.getId(), eventDuration, conn == null ? Constants.MILLIS_I18N : conn.getQueryTimingUnits(), eventCreationPoint, message));
    }
}
