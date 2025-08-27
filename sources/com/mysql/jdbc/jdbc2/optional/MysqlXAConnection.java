package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Messages;
import com.mysql.jdbc.MysqlErrorNumbers;
import com.mysql.jdbc.StringUtils;
import com.mysql.jdbc.Util;
import com.mysql.jdbc.log.Log;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jdbc2/optional/MysqlXAConnection.class */
public class MysqlXAConnection extends MysqlPooledConnection implements XAConnection, XAResource {
    private static final int MAX_COMMAND_LENGTH = 300;
    private Connection underlyingConnection;
    private static final Map<Integer, Integer> MYSQL_ERROR_CODES_TO_XA_ERROR_CODES;
    private Log log;
    protected boolean logXaCommands;
    private static final Constructor<?> JDBC_4_XA_CONNECTION_WRAPPER_CTOR;

    static {
        HashMap<Integer, Integer> temp = new HashMap<>();
        temp.put(Integer.valueOf(MysqlErrorNumbers.ER_XAER_NOTA), -4);
        temp.put(Integer.valueOf(MysqlErrorNumbers.ER_XAER_INVAL), -5);
        temp.put(Integer.valueOf(MysqlErrorNumbers.ER_XAER_RMFAIL), -7);
        temp.put(Integer.valueOf(MysqlErrorNumbers.ER_XAER_OUTSIDE), -9);
        temp.put(Integer.valueOf(MysqlErrorNumbers.ER_XA_RMERR), -3);
        temp.put(Integer.valueOf(MysqlErrorNumbers.ER_XA_RBROLLBACK), 100);
        temp.put(Integer.valueOf(MysqlErrorNumbers.ER_XAER_DUPID), -8);
        temp.put(Integer.valueOf(MysqlErrorNumbers.ER_XA_RBTIMEOUT), 106);
        temp.put(Integer.valueOf(MysqlErrorNumbers.ER_XA_RBDEADLOCK), 102);
        MYSQL_ERROR_CODES_TO_XA_ERROR_CODES = Collections.unmodifiableMap(temp);
        if (!Util.isJdbc4()) {
            JDBC_4_XA_CONNECTION_WRAPPER_CTOR = null;
            return;
        }
        try {
            JDBC_4_XA_CONNECTION_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4MysqlXAConnection").getConstructor(Connection.class, Boolean.TYPE);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e2) {
            throw new RuntimeException(e2);
        } catch (SecurityException e3) {
            throw new RuntimeException(e3);
        }
    }

    protected static MysqlXAConnection getInstance(Connection mysqlConnection, boolean logXaCommands) throws SQLException {
        return !Util.isJdbc4() ? new MysqlXAConnection(mysqlConnection, logXaCommands) : (MysqlXAConnection) Util.handleNewInstance(JDBC_4_XA_CONNECTION_WRAPPER_CTOR, new Object[]{mysqlConnection, Boolean.valueOf(logXaCommands)}, mysqlConnection.getExceptionInterceptor());
    }

    public MysqlXAConnection(Connection connection, boolean logXaCommands) throws SQLException {
        super(connection);
        this.underlyingConnection = connection;
        this.log = connection.getLog();
        this.logXaCommands = logXaCommands;
    }

    public XAResource getXAResource() throws SQLException {
        return this;
    }

    public int getTransactionTimeout() throws XAException {
        return 0;
    }

    public boolean setTransactionTimeout(int arg0) throws XAException {
        return false;
    }

    public boolean isSameRM(XAResource xares) throws XAException {
        if (xares instanceof MysqlXAConnection) {
            return this.underlyingConnection.isSameResource(((MysqlXAConnection) xares).underlyingConnection);
        }
        return false;
    }

    public Xid[] recover(int flag) throws XAException {
        return recover(this.underlyingConnection, flag);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.transaction.xa.XAException */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00fe, code lost:
    
        throw r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0106, code lost:
    
        r11.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x011a, code lost:
    
        if (r12 == null) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x011d, code lost:
    
        r12.close();
     */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0106 A[Catch: SQLException -> 0x0110, TRY_ENTER, TRY_LEAVE, TryCatch #0 {SQLException -> 0x0110, blocks: (B:42:0x0106, B:61:0x0106, B:23:0x004e, B:24:0x0061, B:26:0x006b, B:28:0x00aa, B:29:0x00b9, B:30:0x00ba, B:34:0x00f1, B:35:0x00f6), top: B:66:0x004e, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x011d A[Catch: SQLException -> 0x0127, TRY_ENTER, TRY_LEAVE, TryCatch #3 {SQLException -> 0x0127, blocks: (B:48:0x011d, B:64:0x011d), top: B:69:0x004e }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0154 A[LOOP:1: B:54:0x014d->B:56:0x0154, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected static javax.transaction.xa.Xid[] recover(java.sql.Connection r7, int r8) throws java.sql.SQLException, javax.transaction.xa.XAException {
        /*
            Method dump skipped, instructions count: 362
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.jdbc2.optional.MysqlXAConnection.recover(java.sql.Connection, int):javax.transaction.xa.Xid[]");
    }

    public int prepare(Xid xid) throws SQLException, XAException {
        StringBuilder commandBuf = new StringBuilder(300);
        commandBuf.append("XA PREPARE ");
        appendXid(commandBuf, xid);
        dispatchCommand(commandBuf.toString());
        return 0;
    }

    public void forget(Xid xid) throws XAException {
    }

    public void rollback(Xid xid) throws XAException {
        StringBuilder commandBuf = new StringBuilder(300);
        commandBuf.append("XA ROLLBACK ");
        appendXid(commandBuf, xid);
        try {
            dispatchCommand(commandBuf.toString());
        } finally {
            this.underlyingConnection.setInGlobalTx(false);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.transaction.xa.XAException */
    public void end(Xid xid, int flags) throws SQLException, XAException {
        StringBuilder commandBuf = new StringBuilder(300);
        commandBuf.append("XA END ");
        appendXid(commandBuf, xid);
        switch (flags) {
            case 33554432:
                commandBuf.append(" SUSPEND");
                break;
            case 67108864:
            case 536870912:
                break;
            default:
                throw new XAException(-5);
        }
        dispatchCommand(commandBuf.toString());
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.transaction.xa.XAException */
    public void start(Xid xid, int flags) throws SQLException, XAException {
        StringBuilder commandBuf = new StringBuilder(300);
        commandBuf.append("XA START ");
        appendXid(commandBuf, xid);
        switch (flags) {
            case 0:
                break;
            case 2097152:
                commandBuf.append(" JOIN");
                break;
            case 134217728:
                commandBuf.append(" RESUME");
                break;
            default:
                throw new XAException(-5);
        }
        dispatchCommand(commandBuf.toString());
        this.underlyingConnection.setInGlobalTx(true);
    }

    public void commit(Xid xid, boolean onePhase) throws XAException {
        StringBuilder commandBuf = new StringBuilder(300);
        commandBuf.append("XA COMMIT ");
        appendXid(commandBuf, xid);
        if (onePhase) {
            commandBuf.append(" ONE PHASE");
        }
        try {
            dispatchCommand(commandBuf.toString());
        } finally {
            this.underlyingConnection.setInGlobalTx(false);
        }
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    private java.sql.ResultSet dispatchCommand(java.lang.String r5) throws java.sql.SQLException, javax.transaction.xa.XAException {
        /*
            r4 = this;
            r0 = 0
            r6 = r0
            r0 = r4
            boolean r0 = r0.logXaCommands     // Catch: java.sql.SQLException -> L47 java.lang.Throwable -> L4d
            if (r0 == 0) goto L25
            r0 = r4
            com.mysql.jdbc.log.Log r0 = r0.log     // Catch: java.sql.SQLException -> L47 java.lang.Throwable -> L4d
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.sql.SQLException -> L47 java.lang.Throwable -> L4d
            r2 = r1
            r2.<init>()     // Catch: java.sql.SQLException -> L47 java.lang.Throwable -> L4d
            java.lang.String r2 = "Executing XA statement: "
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.sql.SQLException -> L47 java.lang.Throwable -> L4d
            r2 = r5
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.sql.SQLException -> L47 java.lang.Throwable -> L4d
            java.lang.String r1 = r1.toString()     // Catch: java.sql.SQLException -> L47 java.lang.Throwable -> L4d
            r0.logDebug(r1)     // Catch: java.sql.SQLException -> L47 java.lang.Throwable -> L4d
        L25:
            r0 = r4
            com.mysql.jdbc.Connection r0 = r0.underlyingConnection     // Catch: java.sql.SQLException -> L47 java.lang.Throwable -> L4d
            java.sql.Statement r0 = r0.createStatement()     // Catch: java.sql.SQLException -> L47 java.lang.Throwable -> L4d
            r6 = r0
            r0 = r6
            r1 = r5
            boolean r0 = r0.execute(r1)     // Catch: java.sql.SQLException -> L47 java.lang.Throwable -> L4d
            r0 = r6
            java.sql.ResultSet r0 = r0.getResultSet()     // Catch: java.sql.SQLException -> L47 java.lang.Throwable -> L4d
            r7 = r0
            r0 = r7
            r8 = r0
            r0 = jsr -> L55
        L44:
            r1 = r8
            return r1
        L47:
            r7 = move-exception
            r0 = r7
            javax.transaction.xa.XAException r0 = mapXAExceptionFromSQLException(r0)     // Catch: java.lang.Throwable -> L4d
            throw r0     // Catch: java.lang.Throwable -> L4d
        L4d:
            r9 = move-exception
            r0 = jsr -> L55
        L52:
            r1 = r9
            throw r1
        L55:
            r10 = r0
            r0 = r6
            if (r0 == 0) goto L66
            r0 = r6
            r0.close()     // Catch: java.sql.SQLException -> L64
            goto L66
        L64:
            r11 = move-exception
        L66:
            ret r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.jdbc2.optional.MysqlXAConnection.dispatchCommand(java.lang.String):java.sql.ResultSet");
    }

    protected static XAException mapXAExceptionFromSQLException(SQLException sqlEx) {
        Integer xaCode = MYSQL_ERROR_CODES_TO_XA_ERROR_CODES.get(Integer.valueOf(sqlEx.getErrorCode()));
        if (xaCode != null) {
            return new MysqlXAException(xaCode.intValue(), sqlEx.getMessage(), null).initCause(sqlEx);
        }
        return new MysqlXAException(-7, Messages.getString("MysqlXAConnection.003"), null).initCause(sqlEx);
    }

    private static void appendXid(StringBuilder builder, Xid xid) {
        byte[] gtrid = xid.getGlobalTransactionId();
        byte[] btrid = xid.getBranchQualifier();
        if (gtrid != null) {
            StringUtils.appendAsHex(builder, gtrid);
        }
        builder.append(',');
        if (btrid != null) {
            StringUtils.appendAsHex(builder, btrid);
        }
        builder.append(',');
        StringUtils.appendAsHex(builder, xid.getFormatId());
    }

    @Override // com.mysql.jdbc.jdbc2.optional.MysqlPooledConnection, javax.sql.PooledConnection
    public synchronized java.sql.Connection getConnection() throws SQLException {
        java.sql.Connection connToWrap = getConnection(false, true);
        return connToWrap;
    }
}
