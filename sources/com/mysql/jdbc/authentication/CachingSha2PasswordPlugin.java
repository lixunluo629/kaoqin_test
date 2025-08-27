package com.mysql.jdbc.authentication;

import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.Messages;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Security;
import com.mysql.jdbc.StringUtils;
import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/authentication/CachingSha2PasswordPlugin.class */
public class CachingSha2PasswordPlugin extends Sha256PasswordPlugin {
    public static String PLUGIN_NAME = "caching_sha2_password";
    private AuthStage stage = AuthStage.FAST_AUTH_SEND_SCRAMBLE;

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/authentication/CachingSha2PasswordPlugin$AuthStage.class */
    public enum AuthStage {
        FAST_AUTH_SEND_SCRAMBLE,
        FAST_AUTH_READ_RESULT,
        FAST_AUTH_COMPLETE,
        FULL_AUTH
    }

    @Override // com.mysql.jdbc.authentication.Sha256PasswordPlugin, com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException {
        super.init(conn, props);
        this.stage = AuthStage.FAST_AUTH_SEND_SCRAMBLE;
    }

    @Override // com.mysql.jdbc.authentication.Sha256PasswordPlugin, com.mysql.jdbc.Extension
    public void destroy() {
        this.stage = AuthStage.FAST_AUTH_SEND_SCRAMBLE;
        super.destroy();
    }

    @Override // com.mysql.jdbc.authentication.Sha256PasswordPlugin, com.mysql.jdbc.AuthenticationPlugin
    public String getProtocolPluginName() {
        return PLUGIN_NAME;
    }

    @Override // com.mysql.jdbc.authentication.Sha256PasswordPlugin, com.mysql.jdbc.AuthenticationPlugin
    public boolean nextAuthenticationStep(Buffer fromServer, List<Buffer> toServer) throws SQLException {
        toServer.clear();
        if (this.password == null || this.password.length() == 0 || fromServer == null) {
            toServer.add(new Buffer(new byte[]{0}));
            return true;
        }
        if (this.stage == AuthStage.FAST_AUTH_SEND_SCRAMBLE) {
            this.seed = fromServer.readString();
            try {
                toServer.add(new Buffer(Security.scrambleCachingSha2(StringUtils.getBytes(this.password, this.connection.getPasswordCharacterEncoding()), this.seed.getBytes())));
                this.stage = AuthStage.FAST_AUTH_READ_RESULT;
                return true;
            } catch (UnsupportedEncodingException e) {
                throw SQLError.createSQLException(e.getMessage(), SQLError.SQL_STATE_GENERAL_ERROR, e, (ExceptionInterceptor) null);
            } catch (DigestException e2) {
                throw SQLError.createSQLException(e2.getMessage(), SQLError.SQL_STATE_GENERAL_ERROR, e2, (ExceptionInterceptor) null);
            }
        }
        if (this.stage == AuthStage.FAST_AUTH_READ_RESULT) {
            switch (fromServer.getByteBuffer()[0]) {
                case 3:
                    this.stage = AuthStage.FAST_AUTH_COMPLETE;
                    return true;
                case 4:
                    this.stage = AuthStage.FULL_AUTH;
                    break;
                default:
                    throw SQLError.createSQLException("Unknown server response after fast auth.", SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE, this.connection.getExceptionInterceptor());
            }
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
            } catch (UnsupportedEncodingException e3) {
                throw SQLError.createSQLException(Messages.getString("Sha256PasswordPlugin.3", new Object[]{this.connection.getPasswordCharacterEncoding()}), SQLError.SQL_STATE_GENERAL_ERROR, (ExceptionInterceptor) null);
            }
        }
        if (this.connection.getServerRSAPublicKeyFile() != null) {
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
        toServer.add(new Buffer(new byte[]{2}));
        this.publicKeyRequested = true;
        return true;
    }

    @Override // com.mysql.jdbc.authentication.Sha256PasswordPlugin
    protected byte[] encryptPassword() throws SQLException {
        if (this.connection.versionMeetsMinimum(8, 0, 5)) {
            return super.encryptPassword();
        }
        return super.encryptPassword("RSA/ECB/PKCS1Padding");
    }

    @Override // com.mysql.jdbc.authentication.Sha256PasswordPlugin, com.mysql.jdbc.AuthenticationPlugin
    public void reset() {
        this.stage = AuthStage.FAST_AUTH_SEND_SCRAMBLE;
    }
}
