package com.mysql.jdbc.authentication;

import com.mysql.jdbc.AuthenticationPlugin;
import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.Messages;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Security;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/authentication/MysqlNativePasswordPlugin.class */
public class MysqlNativePasswordPlugin implements AuthenticationPlugin {
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
        return "mysql_native_password";
    }

    @Override // com.mysql.jdbc.AuthenticationPlugin
    public boolean requiresConfidentiality() {
        return false;
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
        Buffer bresp;
        try {
            toServer.clear();
            String pwd = this.password;
            if (fromServer == null || pwd == null || pwd.length() == 0) {
                bresp = new Buffer(new byte[0]);
            } else {
                bresp = new Buffer(Security.scramble411(pwd, fromServer.readString(), this.connection.getPasswordCharacterEncoding()));
            }
            toServer.add(bresp);
            return true;
        } catch (UnsupportedEncodingException e) {
            throw SQLError.createSQLException(Messages.getString("MysqlNativePasswordPlugin.1", new Object[]{this.connection.getPasswordCharacterEncoding()}), SQLError.SQL_STATE_GENERAL_ERROR, (ExceptionInterceptor) null);
        } catch (NoSuchAlgorithmException e2) {
            throw SQLError.createSQLException(Messages.getString("MysqlIO.91") + Messages.getString("MysqlIO.92"), SQLError.SQL_STATE_GENERAL_ERROR, (ExceptionInterceptor) null);
        }
    }

    @Override // com.mysql.jdbc.AuthenticationPlugin
    public void reset() {
    }
}
