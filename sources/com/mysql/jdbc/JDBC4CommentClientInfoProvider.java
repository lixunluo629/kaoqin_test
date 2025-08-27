package com.mysql.jdbc;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4CommentClientInfoProvider.class */
public class JDBC4CommentClientInfoProvider implements JDBC4ClientInfoProvider {
    private Properties clientInfo;

    @Override // com.mysql.jdbc.JDBC4ClientInfoProvider
    public synchronized void initialize(java.sql.Connection conn, Properties configurationProps) throws SQLException {
        this.clientInfo = new Properties();
    }

    @Override // com.mysql.jdbc.JDBC4ClientInfoProvider
    public synchronized void destroy() throws SQLException {
        this.clientInfo = null;
    }

    @Override // com.mysql.jdbc.JDBC4ClientInfoProvider
    public synchronized Properties getClientInfo(java.sql.Connection conn) throws SQLException {
        return this.clientInfo;
    }

    @Override // com.mysql.jdbc.JDBC4ClientInfoProvider
    public synchronized String getClientInfo(java.sql.Connection conn, String name) throws SQLException {
        return this.clientInfo.getProperty(name);
    }

    @Override // com.mysql.jdbc.JDBC4ClientInfoProvider
    public synchronized void setClientInfo(java.sql.Connection conn, Properties properties) throws SQLClientInfoException {
        this.clientInfo = new Properties();
        Enumeration<?> propNames = properties.propertyNames();
        while (propNames.hasMoreElements()) {
            String name = (String) propNames.nextElement();
            this.clientInfo.put(name, properties.getProperty(name));
        }
        setComment(conn);
    }

    @Override // com.mysql.jdbc.JDBC4ClientInfoProvider
    public synchronized void setClientInfo(java.sql.Connection conn, String name, String value) throws SQLClientInfoException {
        this.clientInfo.setProperty(name, value);
        setComment(conn);
    }

    private synchronized void setComment(java.sql.Connection conn) {
        StringBuilder commentBuf = new StringBuilder();
        for (Map.Entry<Object, Object> entry : this.clientInfo.entrySet()) {
            if (commentBuf.length() > 0) {
                commentBuf.append(", ");
            }
            commentBuf.append("" + entry.getKey());
            commentBuf.append(SymbolConstants.EQUAL_SYMBOL);
            commentBuf.append("" + entry.getValue());
        }
        ((Connection) conn).setStatementComment(commentBuf.toString());
    }
}
