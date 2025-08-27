package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.NonRegisteringDriver;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jdbc2/optional/MysqlDataSourceFactory.class */
public class MysqlDataSourceFactory implements ObjectFactory {
    protected static final String DATA_SOURCE_CLASS_NAME = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource";
    protected static final String POOL_DATA_SOURCE_CLASS_NAME = "com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource";
    protected static final String XA_DATA_SOURCE_CLASS_NAME = "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";

    public Object getObjectInstance(Object refObj, Name nm, Context ctx, Hashtable<?, ?> env) throws Exception {
        Reference ref = (Reference) refObj;
        String className = ref.getClassName();
        if (className == null) {
            return null;
        }
        if (className.equals(DATA_SOURCE_CLASS_NAME) || className.equals(POOL_DATA_SOURCE_CLASS_NAME) || className.equals(XA_DATA_SOURCE_CLASS_NAME)) {
            try {
                MysqlDataSource dataSource = (MysqlDataSource) Class.forName(className).newInstance();
                int portNumber = 3306;
                String portNumberAsString = nullSafeRefAddrStringGet("port", ref);
                if (portNumberAsString != null) {
                    portNumber = Integer.parseInt(portNumberAsString);
                }
                dataSource.setPort(portNumber);
                String user = nullSafeRefAddrStringGet("user", ref);
                if (user != null) {
                    dataSource.setUser(user);
                }
                String password = nullSafeRefAddrStringGet(NonRegisteringDriver.PASSWORD_PROPERTY_KEY, ref);
                if (password != null) {
                    dataSource.setPassword(password);
                }
                String serverName = nullSafeRefAddrStringGet("serverName", ref);
                if (serverName != null) {
                    dataSource.setServerName(serverName);
                }
                String databaseName = nullSafeRefAddrStringGet("databaseName", ref);
                if (databaseName != null) {
                    dataSource.setDatabaseName(databaseName);
                }
                String explicitUrlAsString = nullSafeRefAddrStringGet("explicitUrl", ref);
                if (explicitUrlAsString != null && Boolean.valueOf(explicitUrlAsString).booleanValue()) {
                    dataSource.setUrl(nullSafeRefAddrStringGet(RtspHeaders.Values.URL, ref));
                }
                dataSource.setPropertiesViaRef(ref);
                return dataSource;
            } catch (Exception ex) {
                throw new RuntimeException("Unable to create DataSource of class '" + className + "', reason: " + ex.toString());
            }
        }
        return null;
    }

    private String nullSafeRefAddrStringGet(String referenceName, Reference ref) {
        RefAddr refAddr = ref.get(referenceName);
        String asString = refAddr != null ? (String) refAddr.getContent() : null;
        return asString;
    }
}
