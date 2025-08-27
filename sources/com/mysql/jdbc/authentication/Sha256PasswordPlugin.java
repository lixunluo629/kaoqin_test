package com.mysql.jdbc.authentication;

import com.mysql.jdbc.AuthenticationPlugin;
import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.ExportControlled;
import com.mysql.jdbc.Messages;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Security;
import com.mysql.jdbc.StringUtils;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/authentication/Sha256PasswordPlugin.class */
public class Sha256PasswordPlugin implements AuthenticationPlugin {
    public static String PLUGIN_NAME = "sha256_password";
    protected Connection connection;
    protected String password = null;
    protected String seed = null;
    protected boolean publicKeyRequested = false;
    protected String publicKeyString = null;

    @Override // com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException {
        this.connection = conn;
        String pkURL = this.connection.getServerRSAPublicKeyFile();
        if (pkURL != null) {
            this.publicKeyString = readRSAKey(this.connection, pkURL);
        }
    }

    @Override // com.mysql.jdbc.Extension
    public void destroy() {
        this.password = null;
        this.seed = null;
        this.publicKeyRequested = false;
    }

    @Override // com.mysql.jdbc.AuthenticationPlugin
    public String getProtocolPluginName() {
        return PLUGIN_NAME;
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
        toServer.clear();
        if (this.password == null || this.password.length() == 0 || fromServer == null) {
            toServer.add(new Buffer(new byte[]{0}));
            return true;
        }
        if (((MySQLConnection) this.connection).getIO().isSSLEstablished()) {
            try {
                Buffer bresp = new Buffer(StringUtils.getBytes(this.password, this.connection.getPasswordCharacterEncoding()));
                bresp.setPosition(bresp.getBufLength());
                int oldBufLength = bresp.getBufLength();
                bresp.writeByte((byte) 0);
                bresp.setBufLength(oldBufLength + 1);
                bresp.setPosition(0);
                toServer.add(bresp);
                return true;
            } catch (UnsupportedEncodingException e) {
                throw SQLError.createSQLException(Messages.getString("Sha256PasswordPlugin.3", new Object[]{this.connection.getPasswordCharacterEncoding()}), SQLError.SQL_STATE_GENERAL_ERROR, (ExceptionInterceptor) null);
            }
        }
        if (this.connection.getServerRSAPublicKeyFile() != null) {
            this.seed = fromServer.readString();
            toServer.add(new Buffer(encryptPassword()));
            return true;
        }
        if (!this.connection.getAllowPublicKeyRetrieval()) {
            throw SQLError.createSQLException(Messages.getString("Sha256PasswordPlugin.2"), SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE, this.connection.getExceptionInterceptor());
        }
        if (this.publicKeyRequested && fromServer.getBufLength() > 20) {
            this.publicKeyString = fromServer.readString();
            toServer.add(new Buffer(encryptPassword()));
            this.publicKeyRequested = false;
            return true;
        }
        this.seed = fromServer.readString();
        toServer.add(new Buffer(new byte[]{1}));
        this.publicKeyRequested = true;
        return true;
    }

    protected byte[] encryptPassword() throws SQLException {
        return encryptPassword("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
    }

    protected byte[] encryptPassword(String transformation) throws SQLException {
        try {
            byte[] input = this.password != null ? StringUtils.getBytesNullTerminated(this.password, this.connection.getPasswordCharacterEncoding()) : new byte[]{0};
            byte[] mysqlScrambleBuff = new byte[input.length];
            Security.xorString(input, mysqlScrambleBuff, this.seed.getBytes(), input.length);
            return ExportControlled.encryptWithRSAPublicKey(mysqlScrambleBuff, ExportControlled.decodeRSAPublicKey(this.publicKeyString, this.connection.getExceptionInterceptor()), transformation, this.connection.getExceptionInterceptor());
        } catch (UnsupportedEncodingException e) {
            throw SQLError.createSQLException(Messages.getString("Sha256PasswordPlugin.3", new Object[]{this.connection.getPasswordCharacterEncoding()}), SQLError.SQL_STATE_GENERAL_ERROR, (ExceptionInterceptor) null);
        }
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    private static java.lang.String readRSAKey(com.mysql.jdbc.Connection r7, java.lang.String r8) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 238
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.authentication.Sha256PasswordPlugin.readRSAKey(com.mysql.jdbc.Connection, java.lang.String):java.lang.String");
    }

    @Override // com.mysql.jdbc.AuthenticationPlugin
    public void reset() {
    }
}
