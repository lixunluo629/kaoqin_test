package com.mysql.jdbc.interceptors;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptor;
import com.mysql.jdbc.Util;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/interceptors/ServerStatusDiffInterceptor.class */
public class ServerStatusDiffInterceptor implements StatementInterceptor {
    private Map<String, String> preExecuteValues = new HashMap();
    private Map<String, String> postExecuteValues = new HashMap();

    @Override // com.mysql.jdbc.StatementInterceptor, com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException {
    }

    @Override // com.mysql.jdbc.StatementInterceptor
    public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, Connection connection) throws SQLException {
        if (connection.versionMeetsMinimum(5, 0, 2)) {
            populateMapWithSessionStatusValues(connection, this.postExecuteValues);
            connection.getLog().logInfo("Server status change for statement:\n" + Util.calculateDifferences(this.preExecuteValues, this.postExecuteValues));
            return null;
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0037, code lost:
    
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x003f, code lost:
    
        if (r6 == null) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0042, code lost:
    
        r6.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x002f, code lost:
    
        throw r8;
     */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0042  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void populateMapWithSessionStatusValues(com.mysql.jdbc.Connection r4, java.util.Map<java.lang.String, java.lang.String> r5) throws java.sql.SQLException {
        /*
            r3 = this;
            r0 = 0
            r6 = r0
            r0 = 0
            r7 = r0
            r0 = r5
            r0.clear()     // Catch: java.lang.Throwable -> L28
            r0 = r4
            java.sql.Statement r0 = r0.createStatement()     // Catch: java.lang.Throwable -> L28
            r6 = r0
            r0 = r6
            java.lang.String r1 = "SHOW SESSION STATUS"
            java.sql.ResultSet r0 = r0.executeQuery(r1)     // Catch: java.lang.Throwable -> L28
            r7 = r0
            r0 = r5
            r1 = r7
            com.mysql.jdbc.Util.resultSetToMap(r0, r1)     // Catch: java.lang.Throwable -> L28
            r0 = jsr -> L30
        L25:
            goto L4a
        L28:
            r8 = move-exception
            r0 = jsr -> L30
        L2d:
            r1 = r8
            throw r1
        L30:
            r9 = r0
            r0 = r7
            if (r0 == 0) goto L3e
            r0 = r7
            r0.close()
        L3e:
            r0 = r6
            if (r0 == 0) goto L48
            r0 = r6
            r0.close()
        L48:
            ret r9
        L4a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.interceptors.ServerStatusDiffInterceptor.populateMapWithSessionStatusValues(com.mysql.jdbc.Connection, java.util.Map):void");
    }

    @Override // com.mysql.jdbc.StatementInterceptor
    public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection) throws SQLException {
        if (connection.versionMeetsMinimum(5, 0, 2)) {
            populateMapWithSessionStatusValues(connection, this.preExecuteValues);
            return null;
        }
        return null;
    }

    @Override // com.mysql.jdbc.StatementInterceptor
    public boolean executeTopLevelOnly() {
        return true;
    }

    @Override // com.mysql.jdbc.StatementInterceptor, com.mysql.jdbc.Extension
    public void destroy() {
    }
}
