package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/AuthenticationPlugin.class */
public interface AuthenticationPlugin extends Extension {
    String getProtocolPluginName();

    boolean requiresConfidentiality();

    boolean isReusable();

    void setAuthenticationParameters(String str, String str2);

    boolean nextAuthenticationStep(Buffer buffer, List<Buffer> list) throws SQLException;

    void reset();
}
