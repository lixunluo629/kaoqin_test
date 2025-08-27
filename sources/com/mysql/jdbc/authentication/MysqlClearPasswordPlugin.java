package com.mysql.jdbc.authentication;

import com.mysql.jdbc.AuthenticationPlugin;
import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.Messages;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.StringUtils;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/authentication/MysqlClearPasswordPlugin.class */
public class MysqlClearPasswordPlugin implements AuthenticationPlugin {
    private Connection connection;
    private String password = null;

    @Override // com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException {
        this.connection = conn;
    }

    @Override // com.mysql.jdbc.Extension
    public void destroy() {
        this.password = null;
    }

    @Override // com.mysql.jdbc.AuthenticationPlugin
    public String getProtocolPluginName() {
        return "mysql_clear_password";
    }

    @Override // com.mysql.jdbc.AuthenticationPlugin
    public boolean requiresConfidentiality() {
        return true;
    }

    @Override // com.mysql.jdbc.AuthenticationPlugin
    public boolean isReusable() {
        return true;
    }

    @Override // com.mysql.jdbc.AuthenticationPlugin
    public void setAuthenticationParameters(String user, String password) {
        this.password = password;
    }

    @Override // com.mysql.jdbc.AuthenticationPlugin
    public boolean nextAuthenticationStep(Buffer fromServer, List<Buffer> toServer) throws SQLException {
        toServer.clear();
        try {
            String encoding = this.connection.versionMeetsMinimum(5, 7, 6) ? this.connection.getPasswordCharacterEncoding() : "UTF-8";
            Buffer bresp = new Buffer(StringUtils.getBytes(this.password != null ? this.password : "", encoding));
            bresp.setPosition(bresp.getBufLength());
            int oldBufLength = bresp.getBufLength();
            bresp.writeByte((byte) 0);
            bresp.setBufLength(oldBufLength + 1);
            bresp.setPosition(0);
            toServer.add(bresp);
            return true;
        } catch (UnsupportedEncodingException e) {
            throw SQLError.createSQLException(Messages.getString("MysqlClearPasswordPlugin.1", new Object[]{this.connection.getPasswordCharacterEncoding()}), SQLError.SQL_STATE_GENERAL_ERROR, (ExceptionInterceptor) null);
        }
    }

    @Override // com.mysql.jdbc.AuthenticationPlugin
    public void reset() {
    }
}
