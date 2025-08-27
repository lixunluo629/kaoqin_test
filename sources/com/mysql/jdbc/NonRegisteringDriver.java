package com.mysql.jdbc;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/NonRegisteringDriver.class */
public class NonRegisteringDriver implements java.sql.Driver {
    private static final String ALLOWED_QUOTES = "\"'";
    private static final String REPLICATION_URL_PREFIX = "jdbc:mysql:replication://";
    private static final String URL_PREFIX = "jdbc:mysql://";
    private static final String MXJ_URL_PREFIX = "jdbc:mysql:mxj://";
    public static final String LOADBALANCE_URL_PREFIX = "jdbc:mysql:loadbalance://";
    public static final String LICENSE = "GPL";
    public static final String VERSION = "5.1.48";
    public static final String NAME = "MySQL Connector Java";
    public static final String DBNAME_PROPERTY_KEY = "DBNAME";
    public static final boolean DEBUG = false;
    public static final int HOST_NAME_INDEX = 0;
    public static final String HOST_PROPERTY_KEY = "HOST";
    public static final String NUM_HOSTS_PROPERTY_KEY = "NUM_HOSTS";
    public static final String PASSWORD_PROPERTY_KEY = "password";
    public static final int PORT_NUMBER_INDEX = 1;
    public static final String PORT_PROPERTY_KEY = "PORT";
    public static final String PROPERTIES_TRANSFORM_KEY = "propertiesTransform";
    public static final boolean TRACE = false;
    public static final String USE_CONFIG_PROPERTY_KEY = "useConfigs";
    public static final String USER_PROPERTY_KEY = "user";
    public static final String PROTOCOL_PROPERTY_KEY = "PROTOCOL";
    public static final String PATH_PROPERTY_KEY = "PATH";
    public static final String PLATFORM = getPlatform();
    public static final String OS = getOSName();
    public static final String RUNTIME_VENDOR = System.getProperty("java.vendor");
    public static final String RUNTIME_VERSION = System.getProperty("java.version");

    static {
        try {
            Class.forName(AbandonedConnectionCleanupThread.class.getName());
        } catch (ClassNotFoundException e) {
        }
    }

    public static String getOSName() {
        return System.getProperty("os.name");
    }

    public static String getPlatform() {
        return System.getProperty("os.arch");
    }

    static int getMajorVersionInternal() {
        return safeIntParse("5");
    }

    static int getMinorVersionInternal() {
        return safeIntParse("1");
    }

    protected static String[] parseHostPortPair(String hostPortPair) throws SQLException {
        String[] splitValues = new String[2];
        if (StringUtils.startsWithIgnoreCaseAndWs(hostPortPair, "address=")) {
            splitValues[0] = hostPortPair.trim();
            splitValues[1] = null;
            return splitValues;
        }
        int portIndex = hostPortPair.indexOf(":");
        if (portIndex != -1) {
            if (portIndex + 1 < hostPortPair.length()) {
                String portAsString = hostPortPair.substring(portIndex + 1);
                String hostname = hostPortPair.substring(0, portIndex);
                splitValues[0] = hostname;
                splitValues[1] = portAsString;
            } else {
                throw SQLError.createSQLException(Messages.getString("NonRegisteringDriver.37"), SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, (ExceptionInterceptor) null);
            }
        } else {
            splitValues[0] = hostPortPair;
            splitValues[1] = null;
        }
        return splitValues;
    }

    private static int safeIntParse(String intAsString) {
        try {
            return Integer.parseInt(intAsString);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override // java.sql.Driver
    public boolean acceptsURL(String url) throws SQLException {
        if (url == null) {
            throw SQLError.createSQLException(Messages.getString("NonRegisteringDriver.1"), SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE, (ExceptionInterceptor) null);
        }
        return parseURL(url, null) != null;
    }

    @Override // java.sql.Driver
    public java.sql.Connection connect(String url, Properties info) throws SQLException, IOException {
        if (url == null) {
            throw SQLError.createSQLException(Messages.getString("NonRegisteringDriver.1"), SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE, (ExceptionInterceptor) null);
        }
        if (StringUtils.startsWithIgnoreCase(url, LOADBALANCE_URL_PREFIX)) {
            return connectLoadBalanced(url, info);
        }
        if (StringUtils.startsWithIgnoreCase(url, REPLICATION_URL_PREFIX)) {
            return connectReplicationConnection(url, info);
        }
        Properties props = parseURL(url, info);
        if (props == null) {
            return null;
        }
        if (!"1".equals(props.getProperty(NUM_HOSTS_PROPERTY_KEY))) {
            return connectFailover(url, info);
        }
        try {
            Connection newConn = ConnectionImpl.getInstance(host(props), port(props), props, database(props), url);
            return newConn;
        } catch (SQLException sqlEx) {
            throw sqlEx;
        } catch (Exception ex) {
            SQLException sqlEx2 = SQLError.createSQLException(Messages.getString("NonRegisteringDriver.17") + ex.toString() + Messages.getString("NonRegisteringDriver.18"), SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE, (ExceptionInterceptor) null);
            sqlEx2.initCause(ex);
            throw sqlEx2;
        }
    }

    private java.sql.Connection connectLoadBalanced(String url, Properties info) throws SQLException, IOException, NumberFormatException {
        Properties parsedProps = parseURL(url, info);
        if (parsedProps == null) {
            return null;
        }
        parsedProps.remove("roundRobinLoadBalance");
        int numHosts = Integer.parseInt(parsedProps.getProperty(NUM_HOSTS_PROPERTY_KEY));
        List<String> hostList = new ArrayList<>();
        for (int i = 0; i < numHosts; i++) {
            int index = i + 1;
            hostList.add(parsedProps.getProperty("HOST." + index) + ":" + parsedProps.getProperty("PORT." + index));
        }
        return LoadBalancedConnectionProxy.createProxyInstance(hostList, parsedProps);
    }

    private java.sql.Connection connectFailover(String url, Properties info) throws SQLException, IOException, NumberFormatException {
        Properties parsedProps = parseURL(url, info);
        if (parsedProps == null) {
            return null;
        }
        parsedProps.remove("roundRobinLoadBalance");
        int numHosts = Integer.parseInt(parsedProps.getProperty(NUM_HOSTS_PROPERTY_KEY));
        List<String> hostList = new ArrayList<>();
        for (int i = 0; i < numHosts; i++) {
            int index = i + 1;
            hostList.add(parsedProps.getProperty("HOST." + index) + ":" + parsedProps.getProperty("PORT." + index));
        }
        return FailoverConnectionProxy.createProxyInstance(hostList, parsedProps);
    }

    protected java.sql.Connection connectReplicationConnection(String url, Properties info) throws SQLException, IOException, NumberFormatException {
        Properties parsedProps = parseURL(url, info);
        if (parsedProps == null) {
            return null;
        }
        Properties masterProps = (Properties) parsedProps.clone();
        Properties slavesProps = (Properties) parsedProps.clone();
        slavesProps.setProperty("com.mysql.jdbc.ReplicationConnection.isSlave", "true");
        int numHosts = Integer.parseInt(parsedProps.getProperty(NUM_HOSTS_PROPERTY_KEY));
        if (numHosts < 2) {
            throw SQLError.createSQLException("Must specify at least one slave host to connect to for master/slave replication load-balancing functionality", SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, (ExceptionInterceptor) null);
        }
        List<String> slaveHostList = new ArrayList<>();
        List<String> masterHostList = new ArrayList<>();
        String firstHost = masterProps.getProperty("HOST.1") + ":" + masterProps.getProperty("PORT.1");
        boolean usesExplicitServerType = isHostPropertiesList(firstHost);
        for (int i = 0; i < numHosts; i++) {
            int index = i + 1;
            masterProps.remove("HOST." + index);
            masterProps.remove("PORT." + index);
            slavesProps.remove("HOST." + index);
            slavesProps.remove("PORT." + index);
            String host = parsedProps.getProperty("HOST." + index);
            String port = parsedProps.getProperty("PORT." + index);
            if (usesExplicitServerType) {
                if (isHostMaster(host)) {
                    masterHostList.add(host);
                } else {
                    slaveHostList.add(host);
                }
            } else if (i == 0) {
                masterHostList.add(host + ":" + port);
            } else {
                slaveHostList.add(host + ":" + port);
            }
        }
        slavesProps.remove(NUM_HOSTS_PROPERTY_KEY);
        masterProps.remove(NUM_HOSTS_PROPERTY_KEY);
        masterProps.remove(HOST_PROPERTY_KEY);
        masterProps.remove(PORT_PROPERTY_KEY);
        slavesProps.remove(HOST_PROPERTY_KEY);
        slavesProps.remove(PORT_PROPERTY_KEY);
        return ReplicationConnectionProxy.createProxyInstance(masterHostList, masterProps, slaveHostList, slavesProps);
    }

    private boolean isHostMaster(String host) {
        if (isHostPropertiesList(host)) {
            Properties hostSpecificProps = expandHostKeyValues(host);
            if (hostSpecificProps.containsKey("type") && "master".equalsIgnoreCase(hostSpecificProps.get("type").toString())) {
                return true;
            }
            return false;
        }
        return false;
    }

    public String database(Properties props) {
        return props.getProperty(DBNAME_PROPERTY_KEY);
    }

    @Override // java.sql.Driver
    public int getMajorVersion() {
        return getMajorVersionInternal();
    }

    @Override // java.sql.Driver
    public int getMinorVersion() {
        return getMinorVersionInternal();
    }

    @Override // java.sql.Driver
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException, IOException {
        if (info == null) {
            info = new Properties();
        }
        if (url != null && url.startsWith(URL_PREFIX)) {
            info = parseURL(url, info);
        }
        DriverPropertyInfo hostProp = new DriverPropertyInfo(HOST_PROPERTY_KEY, info.getProperty(HOST_PROPERTY_KEY));
        hostProp.required = true;
        hostProp.description = Messages.getString("NonRegisteringDriver.3");
        DriverPropertyInfo portProp = new DriverPropertyInfo(PORT_PROPERTY_KEY, info.getProperty(PORT_PROPERTY_KEY, "3306"));
        portProp.required = false;
        portProp.description = Messages.getString("NonRegisteringDriver.7");
        DriverPropertyInfo dbProp = new DriverPropertyInfo(DBNAME_PROPERTY_KEY, info.getProperty(DBNAME_PROPERTY_KEY));
        dbProp.required = false;
        dbProp.description = "Database name";
        DriverPropertyInfo userProp = new DriverPropertyInfo("user", info.getProperty("user"));
        userProp.required = true;
        userProp.description = Messages.getString("NonRegisteringDriver.13");
        DriverPropertyInfo passwordProp = new DriverPropertyInfo(PASSWORD_PROPERTY_KEY, info.getProperty(PASSWORD_PROPERTY_KEY));
        passwordProp.required = true;
        passwordProp.description = Messages.getString("NonRegisteringDriver.16");
        DriverPropertyInfo[] dpi = ConnectionPropertiesImpl.exposeAsDriverPropertyInfo(info, 5);
        dpi[0] = hostProp;
        dpi[1] = portProp;
        dpi[2] = dbProp;
        dpi[3] = userProp;
        dpi[4] = passwordProp;
        return dpi;
    }

    public String host(Properties props) {
        return props.getProperty(HOST_PROPERTY_KEY, "localhost");
    }

    @Override // java.sql.Driver
    public boolean jdbcCompliant() {
        return false;
    }

    public Properties parseURL(String url, Properties defaults) throws SQLException, IOException {
        String hostStuff;
        Properties urlProps = defaults != null ? new Properties(defaults) : new Properties();
        if (url == null) {
            return null;
        }
        if (!StringUtils.startsWithIgnoreCase(url, URL_PREFIX) && !StringUtils.startsWithIgnoreCase(url, MXJ_URL_PREFIX) && !StringUtils.startsWithIgnoreCase(url, LOADBALANCE_URL_PREFIX) && !StringUtils.startsWithIgnoreCase(url, REPLICATION_URL_PREFIX)) {
            return null;
        }
        int beginningOfSlashes = url.indexOf("//");
        if (StringUtils.startsWithIgnoreCase(url, MXJ_URL_PREFIX)) {
            urlProps.setProperty("socketFactory", "com.mysql.management.driverlaunched.ServerLauncherSocketFactory");
        }
        int index = url.indexOf("?");
        if (index != -1) {
            String paramString = url.substring(index + 1, url.length());
            url = url.substring(0, index);
            StringTokenizer queryParams = new StringTokenizer(paramString, "&");
            while (queryParams.hasMoreTokens()) {
                String parameterValuePair = queryParams.nextToken();
                int indexOfEquals = StringUtils.indexOfIgnoreCase(0, parameterValuePair, SymbolConstants.EQUAL_SYMBOL);
                String parameter = null;
                String value = null;
                if (indexOfEquals != -1) {
                    parameter = parameterValuePair.substring(0, indexOfEquals);
                    if (indexOfEquals + 1 < parameterValuePair.length()) {
                        value = parameterValuePair.substring(indexOfEquals + 1);
                    }
                }
                if (value != null && value.length() > 0 && parameter != null && parameter.length() > 0) {
                    try {
                        urlProps.setProperty(parameter, URLDecoder.decode(value, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        urlProps.setProperty(parameter, URLDecoder.decode(value));
                    } catch (NoSuchMethodError e2) {
                        urlProps.setProperty(parameter, URLDecoder.decode(value));
                    }
                }
            }
        }
        String url2 = url.substring(beginningOfSlashes + 2);
        int slashIndex = StringUtils.indexOfIgnoreCase(0, url2, "/", ALLOWED_QUOTES, ALLOWED_QUOTES, StringUtils.SEARCH_MODE__ALL);
        if (slashIndex != -1) {
            hostStuff = url2.substring(0, slashIndex);
            if (slashIndex + 1 < url2.length()) {
                urlProps.put(DBNAME_PROPERTY_KEY, url2.substring(slashIndex + 1, url2.length()));
            }
        } else {
            hostStuff = url2;
        }
        int numHosts = 0;
        if (hostStuff != null && hostStuff.trim().length() > 0) {
            List<String> hosts = StringUtils.split(hostStuff, ",", ALLOWED_QUOTES, ALLOWED_QUOTES, false);
            for (String hostAndPort : hosts) {
                numHosts++;
                String[] hostPortPair = parseHostPortPair(hostAndPort);
                if (hostPortPair[0] != null && hostPortPair[0].trim().length() > 0) {
                    urlProps.setProperty("HOST." + numHosts, hostPortPair[0]);
                } else {
                    urlProps.setProperty("HOST." + numHosts, "localhost");
                }
                if (hostPortPair[1] != null) {
                    urlProps.setProperty("PORT." + numHosts, hostPortPair[1]);
                } else {
                    urlProps.setProperty("PORT." + numHosts, "3306");
                }
            }
        } else {
            numHosts = 1;
            urlProps.setProperty("HOST.1", "localhost");
            urlProps.setProperty("PORT.1", "3306");
        }
        urlProps.setProperty(NUM_HOSTS_PROPERTY_KEY, String.valueOf(numHosts));
        urlProps.setProperty(HOST_PROPERTY_KEY, urlProps.getProperty("HOST.1"));
        urlProps.setProperty(PORT_PROPERTY_KEY, urlProps.getProperty("PORT.1"));
        String propertiesTransformClassName = urlProps.getProperty(PROPERTIES_TRANSFORM_KEY);
        if (propertiesTransformClassName != null) {
            try {
                ConnectionPropertiesTransform propTransformer = (ConnectionPropertiesTransform) Class.forName(propertiesTransformClassName).newInstance();
                urlProps = propTransformer.transformProperties(urlProps);
            } catch (ClassNotFoundException e3) {
                throw SQLError.createSQLException("Unable to create properties transform instance '" + propertiesTransformClassName + "' due to underlying exception: " + e3.toString(), SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, (ExceptionInterceptor) null);
            } catch (IllegalAccessException e4) {
                throw SQLError.createSQLException("Unable to create properties transform instance '" + propertiesTransformClassName + "' due to underlying exception: " + e4.toString(), SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, (ExceptionInterceptor) null);
            } catch (InstantiationException e5) {
                throw SQLError.createSQLException("Unable to create properties transform instance '" + propertiesTransformClassName + "' due to underlying exception: " + e5.toString(), SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, (ExceptionInterceptor) null);
            }
        }
        if (Util.isColdFusion() && urlProps.getProperty("autoConfigureForColdFusion", "true").equalsIgnoreCase("true")) {
            String configs = urlProps.getProperty(USE_CONFIG_PROPERTY_KEY);
            StringBuilder newConfigs = new StringBuilder();
            if (configs != null) {
                newConfigs.append(configs);
                newConfigs.append(",");
            }
            newConfigs.append("coldFusion");
            urlProps.setProperty(USE_CONFIG_PROPERTY_KEY, newConfigs.toString());
        }
        String configNames = null;
        if (defaults != null) {
            configNames = defaults.getProperty(USE_CONFIG_PROPERTY_KEY);
        }
        if (configNames == null) {
            configNames = urlProps.getProperty(USE_CONFIG_PROPERTY_KEY);
        }
        if (configNames != null) {
            List<String> splitNames = StringUtils.split(configNames, ",", true);
            Properties configProps = new Properties();
            for (String configName : splitNames) {
                try {
                    InputStream configAsStream = getClass().getResourceAsStream("configs/" + configName + ".properties");
                    if (configAsStream == null) {
                        throw SQLError.createSQLException("Can't find configuration template named '" + configName + "'", SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, (ExceptionInterceptor) null);
                    }
                    configProps.load(configAsStream);
                } catch (IOException ioEx) {
                    SQLException sqlEx = SQLError.createSQLException("Unable to load configuration template '" + configName + "' due to underlying IOException: " + ioEx, SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, (ExceptionInterceptor) null);
                    sqlEx.initCause(ioEx);
                    throw sqlEx;
                }
            }
            Iterator<Object> propsIter = urlProps.keySet().iterator();
            while (propsIter.hasNext()) {
                String key = propsIter.next().toString();
                String property = urlProps.getProperty(key);
                configProps.setProperty(key, property);
            }
            urlProps = configProps;
        }
        if (defaults != null) {
            Iterator<Object> propsIter2 = defaults.keySet().iterator();
            while (propsIter2.hasNext()) {
                String key2 = propsIter2.next().toString();
                if (!key2.equals(NUM_HOSTS_PROPERTY_KEY)) {
                    String property2 = defaults.getProperty(key2);
                    urlProps.setProperty(key2, property2);
                }
            }
        }
        return urlProps;
    }

    public int port(Properties props) {
        return Integer.parseInt(props.getProperty(PORT_PROPERTY_KEY, "3306"));
    }

    public String property(String name, Properties props) {
        return props.getProperty(name);
    }

    public static Properties expandHostKeyValues(String host) {
        Properties hostProps = new Properties();
        if (isHostPropertiesList(host)) {
            List<String> hostPropsList = StringUtils.split(host.substring("address=".length() + 1), ")", "'\"", "'\"", true);
            for (String propDef : hostPropsList) {
                if (propDef.startsWith("(")) {
                    propDef = propDef.substring(1);
                }
                List<String> kvp = StringUtils.split(propDef, SymbolConstants.EQUAL_SYMBOL, "'\"", "'\"", true);
                String key = kvp.get(0);
                String value = kvp.size() > 1 ? kvp.get(1) : null;
                if (value != null && ((value.startsWith(SymbolConstants.QUOTES_SYMBOL) && value.endsWith(SymbolConstants.QUOTES_SYMBOL)) || (value.startsWith("'") && value.endsWith("'")))) {
                    value = value.substring(1, value.length() - 1);
                }
                if (value != null) {
                    if (HOST_PROPERTY_KEY.equalsIgnoreCase(key) || DBNAME_PROPERTY_KEY.equalsIgnoreCase(key) || PORT_PROPERTY_KEY.equalsIgnoreCase(key) || PROTOCOL_PROPERTY_KEY.equalsIgnoreCase(key) || PATH_PROPERTY_KEY.equalsIgnoreCase(key)) {
                        key = key.toUpperCase(Locale.ENGLISH);
                    } else if ("user".equalsIgnoreCase(key) || PASSWORD_PROPERTY_KEY.equalsIgnoreCase(key)) {
                        key = key.toLowerCase(Locale.ENGLISH);
                    }
                    hostProps.setProperty(key, value);
                }
            }
        }
        return hostProps;
    }

    public static boolean isHostPropertiesList(String host) {
        return host != null && StringUtils.startsWithIgnoreCase(host, "address=");
    }
}
