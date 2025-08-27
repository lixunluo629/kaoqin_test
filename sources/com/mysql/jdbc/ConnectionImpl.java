package com.mysql.jdbc;

import ch.qos.logback.classic.net.SyslogAppender;
import ch.qos.logback.core.joran.action.ActionConst;
import com.drew.metadata.exif.makernotes.SonyType1MakernoteDirectory;
import com.itextpdf.io.font.PdfEncodings;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.log.LogFactory;
import com.mysql.jdbc.log.NullLogger;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import com.mysql.jdbc.util.LRUCache;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.SQLException;
import java.sql.SQLPermission;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Stack;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ConnectionImpl.class */
public class ConnectionImpl extends ConnectionPropertiesImpl implements MySQLConnection {
    private static final long serialVersionUID = 2877471301981509474L;
    public static final String JDBC_LOCAL_CHARACTER_SET_RESULTS = "jdbc.local.character_set_results";
    private MySQLConnection proxy;
    private InvocationHandler realProxy;
    public static Map<?, ?> charsetMap;
    protected static final String DEFAULT_LOGGER_CLASS = "com.mysql.jdbc.log.StandardLogger";
    private static final int HISTOGRAM_BUCKETS = 20;
    private static Map<String, Integer> mapTransIsolationNameToValue;
    protected static Map<?, ?> roundRobinStatsMap;
    private CacheAdapter<String, Map<String, String>> serverConfigCache;
    private long queryTimeCount;
    private double queryTimeSum;
    private double queryTimeSumSquares;
    private double queryTimeMean;
    private transient Timer cancelTimer;
    private List<Extension> connectionLifecycleInterceptors;
    private static final Constructor<?> JDBC_4_CONNECTION_CTOR;
    private static final int DEFAULT_RESULT_SET_TYPE = 1003;
    private static final int DEFAULT_RESULT_SET_CONCURRENCY = 1007;
    private static final Random random;
    private boolean autoCommit;
    private CacheAdapter<String, PreparedStatement.ParseInfo> cachedPreparedStatementParams;
    private String characterSetMetadata;
    private String characterSetResultsOnServer;
    private final Map<String, Object> charsetConverterMap;
    private long connectionCreationTimeMillis;
    private long connectionId;
    private String database;
    private java.sql.DatabaseMetaData dbmd;
    private TimeZone defaultTimeZone;
    private ProfilerEventHandler eventSink;
    private Throwable forceClosedReason;
    private boolean hasIsolationLevels;
    private boolean hasQuotedIdentifiers;
    private String host;
    public Map<Integer, String> indexToCustomMysqlCharset;
    private Map<String, Integer> mysqlCharsetToCustomMblen;

    /* renamed from: io, reason: collision with root package name */
    private transient MysqlIO f0io;
    private boolean isClientTzUTC;
    private boolean isClosed;
    private boolean isInGlobalTx;
    private boolean isRunningOnJDK13;
    private int isolationLevel;
    private boolean isServerTzUTC;
    private long lastQueryFinishedTime;
    private transient Log log;
    private long longestQueryTimeMs;
    private boolean lowerCaseTableNames;
    private long maximumNumberTablesAccessed;
    private int sessionMaxRows;
    private long metricsLastReportedMs;
    private long minimumNumberTablesAccessed;
    private String myURL;
    private boolean needsPing;
    private int netBufferLength;
    private boolean noBackslashEscapes;
    private boolean serverTruncatesFracSecs;
    private long numberOfPreparedExecutes;
    private long numberOfPrepares;
    private long numberOfQueriesIssued;
    private long numberOfResultSetsCreated;
    private long[] numTablesMetricsHistBreakpoints;
    private int[] numTablesMetricsHistCounts;
    private long[] oldHistBreakpoints;
    private int[] oldHistCounts;
    private final CopyOnWriteArrayList<Statement> openStatements;
    private LRUCache<CompoundCacheKey, CallableStatement.CallableStatementParamInfo> parsedCallableStatementCache;
    private boolean parserKnowsUnicode;
    private String password;
    private long[] perfMetricsHistBreakpoints;
    private int[] perfMetricsHistCounts;
    private int port;
    protected Properties props;
    private boolean readInfoMsg;
    private boolean readOnly;
    protected LRUCache<String, CachedResultSetMetaData> resultSetMetadataCache;
    private TimeZone serverTimezoneTZ;
    private Map<String, String> serverVariables;
    private long shortestQueryTimeMs;
    private double totalQueryTimeMs;
    private boolean transactionsSupported;
    private Map<String, Class<?>> typeMap;
    private boolean useAnsiQuotes;
    private String user;
    private boolean useServerPreparedStmts;
    private LRUCache<String, Boolean> serverSideStatementCheckCache;
    private LRUCache<CompoundCacheKey, ServerPreparedStatement> serverSideStatementCache;
    private Calendar sessionCalendar;
    private Calendar utcCalendar;
    private String origHostToConnectTo;
    private int origPortToConnectTo;
    private String origDatabaseToConnectTo;
    private String errorMessageEncoding;
    private boolean usePlatformCharsetConverters;
    private boolean hasTriedMasterFlag;
    private String statementComment;
    private boolean storesLowerCaseTableName;
    private List<StatementInterceptorV2> statementInterceptors;
    private boolean requiresEscapingEncoder;
    private String hostPortPair;
    private static final String SERVER_VERSION_STRING_VAR_NAME = "server_version_string";
    private int autoIncrementIncrement;
    private ExceptionInterceptor exceptionInterceptor;
    private static final SQLPermission SET_NETWORK_TIMEOUT_PERM = new SQLPermission("setNetworkTimeout");
    private static final SQLPermission ABORT_PERM = new SQLPermission("abort");
    private static final Object CHARSET_CONVERTER_NOT_AVAILABLE_MARKER = new Object();
    private static final String LOGGER_INSTANCE_NAME = "MySQL";
    private static final Log NULL_LOGGER = new NullLogger(LOGGER_INSTANCE_NAME);
    private static final Map<String, Map<Integer, String>> customIndexToCharsetMapByUrl = new HashMap();
    private static final Map<String, Map<String, Integer>> customCharsetToMblenMapByUrl = new HashMap();

    static {
        mapTransIsolationNameToValue = null;
        mapTransIsolationNameToValue = new HashMap(8);
        mapTransIsolationNameToValue.put("READ-UNCOMMITED", 1);
        mapTransIsolationNameToValue.put("READ-UNCOMMITTED", 1);
        mapTransIsolationNameToValue.put("READ-COMMITTED", 2);
        mapTransIsolationNameToValue.put("REPEATABLE-READ", 4);
        mapTransIsolationNameToValue.put("SERIALIZABLE", 8);
        if (Util.isJdbc4()) {
            try {
                JDBC_4_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.JDBC4Connection").getConstructor(String.class, Integer.TYPE, Properties.class, String.class, String.class);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            } catch (SecurityException e3) {
                throw new RuntimeException(e3);
            }
        } else {
            JDBC_4_CONNECTION_CTOR = null;
        }
        random = new Random();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public String getHost() {
        return this.host;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getHostPortPair() {
        return this.hostPortPair != null ? this.hostPortPair : this.host + ":" + this.port;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isProxySet() {
        return this.proxy != null;
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public void setProxy(MySQLConnection proxy) {
        this.proxy = proxy;
        this.realProxy = this.proxy instanceof MultiHostMySQLConnection ? ((MultiHostMySQLConnection) proxy).getThisAsProxy() : null;
    }

    private MySQLConnection getProxy() {
        return this.proxy != null ? this.proxy : this;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    @Deprecated
    public MySQLConnection getLoadBalanceSafeProxy() {
        return getMultiHostSafeProxy();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public MySQLConnection getMultiHostSafeProxy() {
        return getProxy();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public MySQLConnection getActiveMySQLConnection() {
        return this;
    }

    @Override // com.mysql.jdbc.Connection
    public Object getConnectionMutex() {
        return this.realProxy != null ? this.realProxy : getProxy();
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ConnectionImpl$ExceptionInterceptorChain.class */
    public class ExceptionInterceptorChain implements ExceptionInterceptor {
        private List<Extension> interceptors;

        ExceptionInterceptorChain(String interceptorClasses) throws SQLException {
            this.interceptors = Util.loadExtensions(ConnectionImpl.this, ConnectionImpl.this.props, interceptorClasses, "Connection.BadExceptionInterceptor", this);
        }

        void addRingZero(ExceptionInterceptor interceptor) throws SQLException {
            this.interceptors.add(0, interceptor);
        }

        @Override // com.mysql.jdbc.ExceptionInterceptor
        public SQLException interceptException(SQLException sqlEx, Connection conn) {
            if (this.interceptors != null) {
                Iterator<Extension> iter = this.interceptors.iterator();
                while (iter.hasNext()) {
                    sqlEx = ((ExceptionInterceptor) iter.next()).interceptException(sqlEx, ConnectionImpl.this);
                }
            }
            return sqlEx;
        }

        @Override // com.mysql.jdbc.Extension
        public void destroy() {
            if (this.interceptors != null) {
                Iterator<Extension> iter = this.interceptors.iterator();
                while (iter.hasNext()) {
                    ((ExceptionInterceptor) iter.next()).destroy();
                }
            }
        }

        @Override // com.mysql.jdbc.Extension
        public void init(Connection conn, Properties properties) throws SQLException {
            if (this.interceptors != null) {
                Iterator<Extension> iter = this.interceptors.iterator();
                while (iter.hasNext()) {
                    ((ExceptionInterceptor) iter.next()).init(conn, properties);
                }
            }
        }

        public List<Extension> getInterceptors() {
            return this.interceptors;
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ConnectionImpl$CompoundCacheKey.class */
    static class CompoundCacheKey {
        final String componentOne;
        final String componentTwo;
        final int hashCode;

        CompoundCacheKey(String partOne, String partTwo) {
            this.componentOne = partOne;
            this.componentTwo = partTwo;
            int hc = (31 * 17) + (this.componentOne != null ? this.componentOne.hashCode() : 0);
            this.hashCode = (31 * hc) + (this.componentTwo != null ? this.componentTwo.hashCode() : 0);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && CompoundCacheKey.class.isAssignableFrom(obj.getClass())) {
                CompoundCacheKey another = (CompoundCacheKey) obj;
                if (this.componentOne == null) {
                    if (another.componentOne != null) {
                        return false;
                    }
                } else if (!this.componentOne.equals(another.componentOne)) {
                    return false;
                }
                return this.componentTwo == null ? another.componentTwo == null : this.componentTwo.equals(another.componentTwo);
            }
            return false;
        }

        public int hashCode() {
            return this.hashCode;
        }
    }

    protected static SQLException appendMessageToException(SQLException sqlEx, String messageToAppend, ExceptionInterceptor interceptor) {
        String origMessage = sqlEx.getMessage();
        String sqlState = sqlEx.getSQLState();
        int vendorErrorCode = sqlEx.getErrorCode();
        StringBuilder messageBuf = new StringBuilder(origMessage.length() + messageToAppend.length());
        messageBuf.append(origMessage);
        messageBuf.append(messageToAppend);
        SQLException sqlExceptionWithNewMessage = SQLError.createSQLException(messageBuf.toString(), sqlState, vendorErrorCode, interceptor);
        try {
            Class<?> stackTraceElementClass = Class.forName("java.lang.StackTraceElement");
            Class<?> stackTraceElementArrayClass = Array.newInstance(stackTraceElementClass, 0).getClass();
            Method getStackTraceMethod = Throwable.class.getMethod("getStackTrace", new Class[0]);
            Method setStackTraceMethod = Throwable.class.getMethod("setStackTrace", stackTraceElementArrayClass);
            if (getStackTraceMethod != null && setStackTraceMethod != null) {
                Object theStackTraceAsObject = getStackTraceMethod.invoke(sqlEx, new Object[0]);
                setStackTraceMethod.invoke(sqlExceptionWithNewMessage, theStackTraceAsObject);
            }
        } catch (NoClassDefFoundError e) {
        } catch (NoSuchMethodException e2) {
        } catch (Throwable th) {
        }
        return sqlExceptionWithNewMessage;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Timer getCancelTimer() {
        Timer timer;
        synchronized (getConnectionMutex()) {
            if (this.cancelTimer == null) {
                this.cancelTimer = new Timer("MySQL Statement Cancellation Timer", true);
            }
            timer = this.cancelTimer;
        }
        return timer;
    }

    protected static Connection getInstance(String hostToConnectTo, int portToConnectTo, Properties info, String databaseToConnectTo, String url) throws SQLException {
        return !Util.isJdbc4() ? new ConnectionImpl(hostToConnectTo, portToConnectTo, info, databaseToConnectTo, url) : (Connection) Util.handleNewInstance(JDBC_4_CONNECTION_CTOR, new Object[]{hostToConnectTo, Integer.valueOf(portToConnectTo), info, databaseToConnectTo, url}, null);
    }

    protected static synchronized int getNextRoundRobinHostIndex(String url, List<?> hostList) {
        int indexRange = hostList.size();
        int index = random.nextInt(indexRange);
        return index;
    }

    private static boolean nullSafeCompare(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        return (s1 != null || s2 == null) && s1 != null && s1.equals(s2);
    }

    protected ConnectionImpl() {
        this.proxy = null;
        this.realProxy = null;
        this.autoCommit = true;
        this.characterSetMetadata = null;
        this.characterSetResultsOnServer = null;
        this.charsetConverterMap = new HashMap(CharsetMapping.getNumberOfCharsetsConfigured());
        this.connectionCreationTimeMillis = 0L;
        this.database = null;
        this.dbmd = null;
        this.hasIsolationLevels = false;
        this.hasQuotedIdentifiers = false;
        this.host = null;
        this.indexToCustomMysqlCharset = null;
        this.mysqlCharsetToCustomMblen = null;
        this.f0io = null;
        this.isClientTzUTC = false;
        this.isClosed = true;
        this.isInGlobalTx = false;
        this.isRunningOnJDK13 = false;
        this.isolationLevel = 2;
        this.isServerTzUTC = false;
        this.lastQueryFinishedTime = 0L;
        this.log = NULL_LOGGER;
        this.longestQueryTimeMs = 0L;
        this.lowerCaseTableNames = false;
        this.maximumNumberTablesAccessed = 0L;
        this.sessionMaxRows = -1;
        this.minimumNumberTablesAccessed = Long.MAX_VALUE;
        this.myURL = null;
        this.needsPing = false;
        this.netBufferLength = 16384;
        this.noBackslashEscapes = false;
        this.serverTruncatesFracSecs = false;
        this.numberOfPreparedExecutes = 0L;
        this.numberOfPrepares = 0L;
        this.numberOfQueriesIssued = 0L;
        this.numberOfResultSetsCreated = 0L;
        this.oldHistBreakpoints = null;
        this.oldHistCounts = null;
        this.openStatements = new CopyOnWriteArrayList<>();
        this.parserKnowsUnicode = false;
        this.password = null;
        this.port = 3306;
        this.props = null;
        this.readInfoMsg = false;
        this.readOnly = false;
        this.serverTimezoneTZ = null;
        this.serverVariables = null;
        this.shortestQueryTimeMs = Long.MAX_VALUE;
        this.totalQueryTimeMs = 0.0d;
        this.transactionsSupported = false;
        this.useAnsiQuotes = false;
        this.user = null;
        this.useServerPreparedStmts = false;
        this.errorMessageEncoding = "Cp1252";
        this.hasTriedMasterFlag = false;
        this.statementComment = null;
        this.autoIncrementIncrement = 0;
    }

    public ConnectionImpl(String hostToConnectTo, int portToConnectTo, Properties info, String databaseToConnectTo, String url) throws SQLException, NoSuchMethodException, SecurityException {
        this.proxy = null;
        this.realProxy = null;
        this.autoCommit = true;
        this.characterSetMetadata = null;
        this.characterSetResultsOnServer = null;
        this.charsetConverterMap = new HashMap(CharsetMapping.getNumberOfCharsetsConfigured());
        this.connectionCreationTimeMillis = 0L;
        this.database = null;
        this.dbmd = null;
        this.hasIsolationLevels = false;
        this.hasQuotedIdentifiers = false;
        this.host = null;
        this.indexToCustomMysqlCharset = null;
        this.mysqlCharsetToCustomMblen = null;
        this.f0io = null;
        this.isClientTzUTC = false;
        this.isClosed = true;
        this.isInGlobalTx = false;
        this.isRunningOnJDK13 = false;
        this.isolationLevel = 2;
        this.isServerTzUTC = false;
        this.lastQueryFinishedTime = 0L;
        this.log = NULL_LOGGER;
        this.longestQueryTimeMs = 0L;
        this.lowerCaseTableNames = false;
        this.maximumNumberTablesAccessed = 0L;
        this.sessionMaxRows = -1;
        this.minimumNumberTablesAccessed = Long.MAX_VALUE;
        this.myURL = null;
        this.needsPing = false;
        this.netBufferLength = 16384;
        this.noBackslashEscapes = false;
        this.serverTruncatesFracSecs = false;
        this.numberOfPreparedExecutes = 0L;
        this.numberOfPrepares = 0L;
        this.numberOfQueriesIssued = 0L;
        this.numberOfResultSetsCreated = 0L;
        this.oldHistBreakpoints = null;
        this.oldHistCounts = null;
        this.openStatements = new CopyOnWriteArrayList<>();
        this.parserKnowsUnicode = false;
        this.password = null;
        this.port = 3306;
        this.props = null;
        this.readInfoMsg = false;
        this.readOnly = false;
        this.serverTimezoneTZ = null;
        this.serverVariables = null;
        this.shortestQueryTimeMs = Long.MAX_VALUE;
        this.totalQueryTimeMs = 0.0d;
        this.transactionsSupported = false;
        this.useAnsiQuotes = false;
        this.user = null;
        this.useServerPreparedStmts = false;
        this.errorMessageEncoding = "Cp1252";
        this.hasTriedMasterFlag = false;
        this.statementComment = null;
        this.autoIncrementIncrement = 0;
        this.connectionCreationTimeMillis = System.currentTimeMillis();
        databaseToConnectTo = databaseToConnectTo == null ? "" : databaseToConnectTo;
        this.origHostToConnectTo = hostToConnectTo;
        this.origPortToConnectTo = portToConnectTo;
        this.origDatabaseToConnectTo = databaseToConnectTo;
        try {
            java.sql.Blob.class.getMethod("truncate", Long.TYPE);
            this.isRunningOnJDK13 = false;
        } catch (NoSuchMethodException e) {
            this.isRunningOnJDK13 = true;
        }
        this.sessionCalendar = new GregorianCalendar();
        this.utcCalendar = new GregorianCalendar();
        this.utcCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.log = LogFactory.getLogger(getLogger(), LOGGER_INSTANCE_NAME, getExceptionInterceptor());
        if (NonRegisteringDriver.isHostPropertiesList(hostToConnectTo)) {
            Properties hostSpecificProps = NonRegisteringDriver.expandHostKeyValues(hostToConnectTo);
            Enumeration<?> propertyNames = hostSpecificProps.propertyNames();
            while (propertyNames.hasMoreElements()) {
                String propertyName = propertyNames.nextElement().toString();
                String propertyValue = hostSpecificProps.getProperty(propertyName);
                info.setProperty(propertyName, propertyValue);
            }
        } else if (hostToConnectTo == null) {
            this.host = "localhost";
            this.hostPortPair = this.host + ":" + portToConnectTo;
        } else {
            this.host = hostToConnectTo;
            if (hostToConnectTo.indexOf(":") == -1) {
                this.hostPortPair = this.host + ":" + portToConnectTo;
            } else {
                this.hostPortPair = this.host;
            }
        }
        this.port = portToConnectTo;
        this.database = databaseToConnectTo;
        this.myURL = url;
        this.user = info.getProperty("user");
        this.password = info.getProperty(NonRegisteringDriver.PASSWORD_PROPERTY_KEY);
        if (this.user == null || this.user.equals("")) {
            this.user = "";
        }
        if (this.password == null) {
            this.password = "";
        }
        this.props = info;
        initializeDriverProperties(info);
        this.defaultTimeZone = TimeUtil.getDefaultTimeZone(getCacheDefaultTimezone());
        this.isClientTzUTC = !this.defaultTimeZone.useDaylightTime() && this.defaultTimeZone.getRawOffset() == 0;
        try {
            this.dbmd = getMetaData(false, false);
            initializeSafeStatementInterceptors();
            createNewIO(false);
            unSafeStatementInterceptors();
            AbandonedConnectionCleanupThread.trackConnection(this, this.f0io.getNetworkResources());
        } catch (SQLException ex) {
            cleanup(ex);
            throw ex;
        } catch (Exception ex2) {
            cleanup(ex2);
            StringBuilder mesg = new StringBuilder(128);
            if (!getParanoid()) {
                mesg.append("Cannot connect to MySQL server on ");
                mesg.append(this.host);
                mesg.append(":");
                mesg.append(this.port);
                mesg.append(".\n\n");
                mesg.append("Make sure that there is a MySQL server ");
                mesg.append("running on the machine/port you are trying ");
                mesg.append("to connect to and that the machine this software is running on ");
                mesg.append("is able to connect to this host/port (i.e. not firewalled). ");
                mesg.append("Also make sure that the server has not been started with the --skip-networking ");
                mesg.append("flag.\n\n");
            } else {
                mesg.append("Unable to connect to database.");
            }
            SQLException sqlEx = SQLError.createSQLException(mesg.toString(), SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE, getExceptionInterceptor());
            sqlEx.initCause(ex2);
            throw sqlEx;
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void unSafeStatementInterceptors() throws SQLException {
        ArrayList<StatementInterceptorV2> unSafedStatementInterceptors = new ArrayList<>(this.statementInterceptors.size());
        for (int i = 0; i < this.statementInterceptors.size(); i++) {
            NoSubInterceptorWrapper wrappedInterceptor = (NoSubInterceptorWrapper) this.statementInterceptors.get(i);
            unSafedStatementInterceptors.add(wrappedInterceptor.getUnderlyingInterceptor());
        }
        this.statementInterceptors = unSafedStatementInterceptors;
        if (this.f0io != null) {
            this.f0io.setStatementInterceptors(this.statementInterceptors);
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void initializeSafeStatementInterceptors() throws SQLException {
        this.isClosed = false;
        List<Extension> unwrappedInterceptors = Util.loadExtensions(this, this.props, getStatementInterceptors(), "MysqlIo.BadStatementInterceptor", getExceptionInterceptor());
        this.statementInterceptors = new ArrayList(unwrappedInterceptors.size());
        for (int i = 0; i < unwrappedInterceptors.size(); i++) {
            Extension interceptor = unwrappedInterceptors.get(i);
            if (interceptor instanceof StatementInterceptor) {
                if (ReflectiveStatementInterceptorAdapter.getV2PostProcessMethod(interceptor.getClass()) != null) {
                    this.statementInterceptors.add(new NoSubInterceptorWrapper(new ReflectiveStatementInterceptorAdapter((StatementInterceptor) interceptor)));
                } else {
                    this.statementInterceptors.add(new NoSubInterceptorWrapper(new V1toV2StatementInterceptorAdapter((StatementInterceptor) interceptor)));
                }
            } else {
                this.statementInterceptors.add(new NoSubInterceptorWrapper((StatementInterceptorV2) interceptor));
            }
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public List<StatementInterceptorV2> getStatementInterceptorsInstances() {
        return this.statementInterceptors;
    }

    private void addToHistogram(int[] histogramCounts, long[] histogramBreakpoints, long value, int numberOfTimes, long currentLowerBound, long currentUpperBound) {
        if (histogramCounts == null) {
            createInitialHistogram(histogramBreakpoints, currentLowerBound, currentUpperBound);
            return;
        }
        for (int i = 0; i < 20; i++) {
            if (histogramBreakpoints[i] >= value) {
                int i2 = i;
                histogramCounts[i2] = histogramCounts[i2] + numberOfTimes;
                return;
            }
        }
    }

    private void addToPerformanceHistogram(long value, int numberOfTimes) {
        checkAndCreatePerformanceHistogram();
        addToHistogram(this.perfMetricsHistCounts, this.perfMetricsHistBreakpoints, value, numberOfTimes, this.shortestQueryTimeMs == Long.MAX_VALUE ? 0L : this.shortestQueryTimeMs, this.longestQueryTimeMs);
    }

    private void addToTablesAccessedHistogram(long value, int numberOfTimes) {
        checkAndCreateTablesAccessedHistogram();
        addToHistogram(this.numTablesMetricsHistCounts, this.numTablesMetricsHistBreakpoints, value, numberOfTimes, this.minimumNumberTablesAccessed == Long.MAX_VALUE ? 0L : this.minimumNumberTablesAccessed, this.maximumNumberTablesAccessed);
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x01c5, code lost:
    
        r9.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x01d2, code lost:
    
        if (0 == 0) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x01d5, code lost:
    
        r8.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x01bd, code lost:
    
        throw r13;
     */
    /* JADX WARN: Removed duplicated region for block: B:122:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01c5 A[Catch: SQLException -> 0x01cf, TRY_ENTER, TRY_LEAVE, TryCatch #2 {SQLException -> 0x01cf, blocks: (B:85:0x01c5, B:101:0x01c5), top: B:109:0x0057 }] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01d5 A[Catch: SQLException -> 0x01de, TRY_ENTER, TRY_LEAVE, TryCatch #0 {SQLException -> 0x01de, blocks: (B:89:0x01d5, B:104:0x01d5, B:21:0x0057, B:22:0x006c, B:23:0x0076, B:25:0x0080, B:27:0x00a2, B:30:0x00c0, B:32:0x00cd, B:29:0x00b2, B:42:0x00f4, B:44:0x00fd, B:45:0x0107, B:47:0x0111, B:49:0x0127, B:59:0x0159, B:61:0x0160, B:62:0x0166, B:63:0x0167, B:64:0x0185, B:68:0x018d, B:70:0x0190, B:53:0x0144, B:55:0x014f, B:58:0x0158, B:36:0x00df, B:38:0x00ea, B:41:0x00f3, B:75:0x019b, B:77:0x019e, B:78:0x01b5), top: B:106:0x0057, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x01f2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void buildCollationMapping() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 507
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ConnectionImpl.buildCollationMapping():void");
    }

    private boolean canHandleAsServerPreparedStatement(String sql) throws SQLException {
        if (sql == null || sql.length() == 0) {
            return true;
        }
        if (!this.useServerPreparedStmts) {
            return false;
        }
        if (getCachePreparedStatements()) {
            synchronized (this.serverSideStatementCheckCache) {
                Boolean flag = this.serverSideStatementCheckCache.get(sql);
                if (flag != null) {
                    return flag.booleanValue();
                }
                boolean canHandle = canHandleAsServerPreparedStatementNoCache(sql);
                if (sql.length() < getPreparedStatementCacheSqlLimit()) {
                    this.serverSideStatementCheckCache.put(sql, canHandle ? Boolean.TRUE : Boolean.FALSE);
                }
                return canHandle;
            }
        }
        return canHandleAsServerPreparedStatementNoCache(sql);
    }

    private boolean canHandleAsServerPreparedStatementNoCache(String sql) throws SQLException {
        if (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "CALL")) {
            return false;
        }
        boolean canHandleAsStatement = true;
        boolean allowBackslashEscapes = !this.noBackslashEscapes;
        String quoteChar = this.useAnsiQuotes ? SymbolConstants.QUOTES_SYMBOL : "'";
        if (getAllowMultiQueries()) {
            if (StringUtils.indexOfIgnoreCase(0, sql, ScriptUtils.DEFAULT_STATEMENT_SEPARATOR, quoteChar, quoteChar, allowBackslashEscapes ? StringUtils.SEARCH_MODE__ALL : StringUtils.SEARCH_MODE__MRK_COM_WS) != -1) {
                canHandleAsStatement = false;
            }
        } else if (!versionMeetsMinimum(5, 0, 7) && (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "SELECT") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "DELETE") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "INSERT") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "UPDATE") || StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "REPLACE"))) {
            int currentPos = 0;
            int statementLength = sql.length();
            int lastPosToLook = statementLength - 7;
            boolean foundLimitWithPlaceholder = false;
            while (currentPos < lastPosToLook) {
                int limitStart = StringUtils.indexOfIgnoreCase(currentPos, sql, "LIMIT ", quoteChar, quoteChar, allowBackslashEscapes ? StringUtils.SEARCH_MODE__ALL : StringUtils.SEARCH_MODE__MRK_COM_WS);
                if (limitStart == -1) {
                    break;
                }
                currentPos = limitStart + 7;
                while (true) {
                    if (currentPos < statementLength) {
                        char c = sql.charAt(currentPos);
                        if (Character.isDigit(c) || Character.isWhitespace(c) || c == ',' || c == '?') {
                            if (c == '?') {
                                foundLimitWithPlaceholder = true;
                                break;
                            }
                            currentPos++;
                        }
                    }
                }
            }
            canHandleAsStatement = !foundLimitWithPlaceholder;
        } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "XA ") || StringUtils.startsWithIgnoreCaseAndWs(sql, "CREATE TABLE") || StringUtils.startsWithIgnoreCaseAndWs(sql, "DO") || StringUtils.startsWithIgnoreCaseAndWs(sql, "SET")) {
            canHandleAsStatement = false;
        } else if ((StringUtils.startsWithIgnoreCaseAndWs(sql, "SHOW WARNINGS") && versionMeetsMinimum(5, 7, 2)) || sql.startsWith("/* ping */")) {
            canHandleAsStatement = false;
        }
        return canHandleAsStatement;
    }

    @Override // com.mysql.jdbc.Connection
    public void changeUser(String userName, String newPassword) throws SQLException {
        synchronized (getConnectionMutex()) {
            checkClosed();
            if (userName == null || userName.equals("")) {
                userName = "";
            }
            if (newPassword == null) {
                newPassword = "";
            }
            this.sessionMaxRows = -1;
            try {
                this.f0io.changeUser(userName, newPassword, this.database);
                this.user = userName;
                this.password = newPassword;
                if (versionMeetsMinimum(4, 1, 0)) {
                    configureClientCharacterSet(true);
                }
                setSessionVariables();
                setupServerForTruncationChecks();
            } catch (SQLException ex) {
                if (versionMeetsMinimum(5, 6, 13) && SQLError.SQL_STATE_INVALID_AUTH_SPEC.equals(ex.getSQLState())) {
                    cleanup(ex);
                }
                throw ex;
            }
        }
    }

    private boolean characterSetNamesMatches(String mysqlEncodingName) {
        return mysqlEncodingName != null && mysqlEncodingName.equalsIgnoreCase(this.serverVariables.get("character_set_client")) && mysqlEncodingName.equalsIgnoreCase(this.serverVariables.get("character_set_connection"));
    }

    private void checkAndCreatePerformanceHistogram() {
        if (this.perfMetricsHistCounts == null) {
            this.perfMetricsHistCounts = new int[20];
        }
        if (this.perfMetricsHistBreakpoints == null) {
            this.perfMetricsHistBreakpoints = new long[20];
        }
    }

    private void checkAndCreateTablesAccessedHistogram() {
        if (this.numTablesMetricsHistCounts == null) {
            this.numTablesMetricsHistCounts = new int[20];
        }
        if (this.numTablesMetricsHistBreakpoints == null) {
            this.numTablesMetricsHistBreakpoints = new long[20];
        }
    }

    @Override // com.mysql.jdbc.Connection
    public void checkClosed() throws SQLException {
        if (this.isClosed) {
            throwConnectionClosedException();
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void throwConnectionClosedException() throws SQLException {
        SQLException ex = SQLError.createSQLException("No operations allowed after connection closed.", SQLError.SQL_STATE_CONNECTION_NOT_OPEN, getExceptionInterceptor());
        if (this.forceClosedReason != null) {
            ex.initCause(this.forceClosedReason);
        }
        throw ex;
    }

    private void checkServerEncoding() throws SQLException {
        if (getUseUnicode() && getEncoding() != null) {
            return;
        }
        String serverCharset = this.serverVariables.get("character_set");
        if (serverCharset == null) {
            serverCharset = this.serverVariables.get("character_set_server");
        }
        String mappedServerEncoding = null;
        if (serverCharset != null) {
            try {
                mappedServerEncoding = CharsetMapping.getJavaEncodingForMysqlCharset(serverCharset);
            } catch (RuntimeException ex) {
                SQLException sqlEx = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                sqlEx.initCause(ex);
                throw sqlEx;
            }
        }
        if (!getUseUnicode() && mappedServerEncoding != null) {
            SingleByteCharsetConverter converter = getCharsetConverter(mappedServerEncoding);
            if (converter != null) {
                setUseUnicode(true);
                setEncoding(mappedServerEncoding);
                return;
            }
        }
        if (serverCharset != null) {
            if (mappedServerEncoding == null && Character.isLowerCase(serverCharset.charAt(0))) {
                char[] ach = serverCharset.toCharArray();
                ach[0] = Character.toUpperCase(serverCharset.charAt(0));
                setEncoding(new String(ach));
            }
            if (mappedServerEncoding == null) {
                throw SQLError.createSQLException("Unknown character encoding on server '" + serverCharset + "', use 'characterEncoding=' property  to provide correct mapping", SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, getExceptionInterceptor());
            }
            try {
                StringUtils.getBytes("abc", mappedServerEncoding);
                setEncoding(mappedServerEncoding);
                setUseUnicode(true);
            } catch (UnsupportedEncodingException e) {
                throw SQLError.createSQLException("The driver can not map the character encoding '" + getEncoding() + "' that your server is using to a character encoding your JVM understands. You can specify this mapping manually by adding \"useUnicode=true\" as well as \"characterEncoding=[an_encoding_your_jvm_understands]\" to your JDBC URL.", "0S100", getExceptionInterceptor());
            }
        }
    }

    private void checkTransactionIsolationLevel() throws SQLException {
        Integer intTI;
        String s = this.serverVariables.get("transaction_isolation");
        if (s == null) {
            s = this.serverVariables.get("tx_isolation");
        }
        if (s != null && (intTI = mapTransIsolationNameToValue.get(s)) != null) {
            this.isolationLevel = intTI.intValue();
        }
    }

    @Override // com.mysql.jdbc.Connection
    public void abortInternal() throws SQLException {
        if (this.f0io != null) {
            try {
                this.f0io.forceClose();
                this.f0io.releaseResources();
            } catch (Throwable th) {
            }
            this.f0io = null;
        }
        this.isClosed = true;
    }

    private void cleanup(Throwable whyCleanedUp) {
        try {
            if (this.f0io != null) {
                if (isClosed()) {
                    this.f0io.forceClose();
                } else {
                    realClose(false, false, false, whyCleanedUp);
                }
            }
        } catch (SQLException e) {
        }
        this.isClosed = true;
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public void clearHasTriedMaster() {
        this.hasTriedMasterFlag = false;
    }

    @Override // java.sql.Connection
    public void clearWarnings() throws SQLException {
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement clientPrepareStatement(String sql) throws SQLException {
        return clientPrepareStatement(sql, 1003, 1007);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        java.sql.PreparedStatement pStmt = clientPrepareStatement(sql);
        ((PreparedStatement) pStmt).setRetrieveGeneratedKeys(autoGenKeyIndex == 1);
        return pStmt;
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return clientPrepareStatement(sql, resultSetType, resultSetConcurrency, true);
    }

    public java.sql.PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, boolean processEscapeCodesIfNeeded) throws SQLException {
        PreparedStatement pStmt;
        checkClosed();
        String nativeSql = (processEscapeCodesIfNeeded && getProcessEscapeCodesForPrepStmts()) ? nativeSQL(sql) : sql;
        if (getCachePreparedStatements()) {
            PreparedStatement.ParseInfo pStmtInfo = this.cachedPreparedStatementParams.get(nativeSql);
            if (pStmtInfo == null) {
                pStmt = PreparedStatement.getInstance(getMultiHostSafeProxy(), nativeSql, this.database);
                this.cachedPreparedStatementParams.put(nativeSql, pStmt.getParseInfo());
            } else {
                pStmt = PreparedStatement.getInstance(getMultiHostSafeProxy(), nativeSql, this.database, pStmtInfo);
            }
        } else {
            pStmt = PreparedStatement.getInstance(getMultiHostSafeProxy(), nativeSql, this.database);
        }
        pStmt.setResultSetType(resultSetType);
        pStmt.setResultSetConcurrency(resultSetConcurrency);
        return pStmt;
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement clientPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        PreparedStatement pStmt = (PreparedStatement) clientPrepareStatement(sql);
        pStmt.setRetrieveGeneratedKeys(autoGenKeyIndexes != null && autoGenKeyIndexes.length > 0);
        return pStmt;
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement clientPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        PreparedStatement pStmt = (PreparedStatement) clientPrepareStatement(sql);
        pStmt.setRetrieveGeneratedKeys(autoGenKeyColNames != null && autoGenKeyColNames.length > 0);
        return pStmt;
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return clientPrepareStatement(sql, resultSetType, resultSetConcurrency, true);
    }

    @Override // java.sql.Connection, java.lang.AutoCloseable
    public void close() throws SQLException {
        synchronized (getConnectionMutex()) {
            if (this.connectionLifecycleInterceptors != null) {
                new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) { // from class: com.mysql.jdbc.ConnectionImpl.1
                    /* JADX INFO: Access modifiers changed from: package-private */
                    @Override // com.mysql.jdbc.IterateBlock
                    public void forEach(Extension each) throws SQLException {
                        ((ConnectionLifecycleInterceptor) each).close();
                    }
                }.doForAll();
            }
            realClose(true, true, false, null);
        }
    }

    private void closeAllOpenStatements() throws SQLException {
        SQLException postponedException = null;
        Iterator i$ = this.openStatements.iterator();
        while (i$.hasNext()) {
            Statement stmt = i$.next();
            try {
                ((StatementImpl) stmt).realClose(false, true);
            } catch (SQLException sqlEx) {
                postponedException = sqlEx;
            }
        }
        if (postponedException != null) {
            throw postponedException;
        }
    }

    private void closeStatement(java.sql.Statement stmt) throws SQLException {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
        }
    }

    @Override // java.sql.Connection
    public void commit() throws SQLException {
        synchronized (getConnectionMutex()) {
            checkClosed();
            try {
                try {
                    if (this.connectionLifecycleInterceptors != null) {
                        IterateBlock<Extension> iter = new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) { // from class: com.mysql.jdbc.ConnectionImpl.2
                            /* JADX INFO: Access modifiers changed from: package-private */
                            @Override // com.mysql.jdbc.IterateBlock
                            public void forEach(Extension each) throws SQLException {
                                if (!((ConnectionLifecycleInterceptor) each).commit()) {
                                    this.stopIterating = true;
                                }
                            }
                        };
                        iter.doForAll();
                        if (!iter.fullIteration()) {
                            return;
                        }
                    }
                    if (this.autoCommit && !getRelaxAutoCommit()) {
                        throw SQLError.createSQLException("Can't call commit when autocommit=true", getExceptionInterceptor());
                    }
                    if (this.transactionsSupported) {
                        if (getUseLocalTransactionState() && versionMeetsMinimum(5, 0, 0) && !this.f0io.inTransactionOnServer()) {
                        } else {
                            execSQL(null, "commit", -1, null, 1003, 1007, false, this.database, null, false);
                        }
                    }
                } catch (SQLException sqlException) {
                    if (SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE.equals(sqlException.getSQLState())) {
                        throw SQLError.createSQLException("Communications link failure during commit(). Transaction resolution unknown.", SQLError.SQL_STATE_TRANSACTION_RESOLUTION_UNKNOWN, getExceptionInterceptor());
                    }
                    throw sqlException;
                }
            } finally {
                this.needsPing = getReconnectAtTxEnd();
            }
        }
    }

    private void configureCharsetProperties() throws SQLException {
        if (getEncoding() != null) {
            try {
                StringUtils.getBytes("abc", getEncoding());
            } catch (UnsupportedEncodingException e) {
                String oldEncoding = getEncoding();
                try {
                    setEncoding(CharsetMapping.getJavaEncodingForMysqlCharset(oldEncoding));
                    if (getEncoding() == null) {
                        throw SQLError.createSQLException("Java does not support the MySQL character encoding '" + oldEncoding + "'.", SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, getExceptionInterceptor());
                    }
                    try {
                        StringUtils.getBytes("abc", getEncoding());
                    } catch (UnsupportedEncodingException e2) {
                        throw SQLError.createSQLException("Unsupported character encoding '" + getEncoding() + "'.", SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, getExceptionInterceptor());
                    }
                } catch (RuntimeException ex) {
                    SQLException sqlEx = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                    sqlEx.initCause(ex);
                    throw sqlEx;
                }
            }
        }
    }

    private boolean configureClientCharacterSet(boolean dontCheckServerMatch) throws SQLException {
        String mysqlEncodingName;
        String realJavaEncoding = getEncoding();
        boolean characterSetAlreadyConfigured = false;
        try {
            if (versionMeetsMinimum(4, 1, 0)) {
                characterSetAlreadyConfigured = true;
                setUseUnicode(true);
                configureCharsetProperties();
                realJavaEncoding = getEncoding();
                String connectionCollationSuffix = "";
                String connectionCollationCharset = "";
                if (!getUseOldUTF8Behavior() && !StringUtils.isNullOrEmpty(getConnectionCollation())) {
                    for (int i = 1; i < CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME.length; i++) {
                        if (CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME[i].equals(getConnectionCollation())) {
                            connectionCollationSuffix = " COLLATE " + CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME[i];
                            connectionCollationCharset = CharsetMapping.COLLATION_INDEX_TO_CHARSET[i].charsetName;
                            realJavaEncoding = CharsetMapping.getJavaEncodingForCollationIndex(Integer.valueOf(i));
                        }
                    }
                }
                try {
                    if (this.props != null && this.props.getProperty("com.mysql.jdbc.faultInjection.serverCharsetIndex") != null) {
                        this.f0io.serverCharsetIndex = Integer.parseInt(this.props.getProperty("com.mysql.jdbc.faultInjection.serverCharsetIndex"));
                    }
                    String serverEncodingToSet = CharsetMapping.getJavaEncodingForCollationIndex(Integer.valueOf(this.f0io.serverCharsetIndex));
                    if (serverEncodingToSet == null || serverEncodingToSet.length() == 0) {
                        if (realJavaEncoding != null) {
                            setEncoding(realJavaEncoding);
                        } else {
                            throw SQLError.createSQLException("Unknown initial character set index '" + this.f0io.serverCharsetIndex + "' received from server. Initial client character set can be forced via the 'characterEncoding' property.", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
                        }
                    }
                    if (versionMeetsMinimum(4, 1, 0) && "ISO8859_1".equalsIgnoreCase(serverEncodingToSet)) {
                        serverEncodingToSet = "Cp1252";
                    }
                    if (PdfEncodings.UNICODE_BIG.equalsIgnoreCase(serverEncodingToSet) || "UTF-16".equalsIgnoreCase(serverEncodingToSet) || "UTF-16LE".equalsIgnoreCase(serverEncodingToSet) || "UTF-32".equalsIgnoreCase(serverEncodingToSet)) {
                        serverEncodingToSet = "UTF-8";
                    }
                    setEncoding(serverEncodingToSet);
                } catch (ArrayIndexOutOfBoundsException e) {
                    if (realJavaEncoding != null) {
                        setEncoding(realJavaEncoding);
                    } else {
                        throw SQLError.createSQLException("Unknown initial character set index '" + this.f0io.serverCharsetIndex + "' received from server. Initial client character set can be forced via the 'characterEncoding' property.", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
                    }
                } catch (RuntimeException ex) {
                    SQLException sqlEx = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                    sqlEx.initCause(ex);
                    throw sqlEx;
                } catch (SQLException ex2) {
                    throw ex2;
                }
                if (getEncoding() == null) {
                    setEncoding("ISO8859_1");
                }
                if (getUseUnicode()) {
                    if (realJavaEncoding != null) {
                        if (realJavaEncoding.equalsIgnoreCase("UTF-8") || realJavaEncoding.equalsIgnoreCase("UTF8")) {
                            boolean utf8mb4Supported = versionMeetsMinimum(5, 5, 2);
                            String utf8CharsetName = connectionCollationSuffix.length() > 0 ? connectionCollationCharset : utf8mb4Supported ? "utf8mb4" : "utf8";
                            if (!getUseOldUTF8Behavior()) {
                                if (dontCheckServerMatch || !characterSetNamesMatches("utf8") || ((utf8mb4Supported && !characterSetNamesMatches("utf8mb4")) || (connectionCollationSuffix.length() > 0 && !getConnectionCollation().equalsIgnoreCase(this.serverVariables.get("collation_server"))))) {
                                    execSQL(null, "SET NAMES " + utf8CharsetName + connectionCollationSuffix, -1, null, 1003, 1007, false, this.database, null, false);
                                    this.serverVariables.put("character_set_client", utf8CharsetName);
                                    this.serverVariables.put("character_set_connection", utf8CharsetName);
                                }
                            } else {
                                execSQL(null, "SET NAMES latin1", -1, null, 1003, 1007, false, this.database, null, false);
                                this.serverVariables.put("character_set_client", CharsetMapping.NOT_USED);
                                this.serverVariables.put("character_set_connection", CharsetMapping.NOT_USED);
                            }
                            setEncoding(realJavaEncoding);
                        } else {
                            String mysqlCharsetName = connectionCollationSuffix.length() > 0 ? connectionCollationCharset : CharsetMapping.getMysqlCharsetForJavaEncoding(realJavaEncoding.toUpperCase(Locale.ENGLISH), this);
                            if (mysqlCharsetName != null && (dontCheckServerMatch || !characterSetNamesMatches(mysqlCharsetName))) {
                                execSQL(null, "SET NAMES " + mysqlCharsetName + connectionCollationSuffix, -1, null, 1003, 1007, false, this.database, null, false);
                                this.serverVariables.put("character_set_client", mysqlCharsetName);
                                this.serverVariables.put("character_set_connection", mysqlCharsetName);
                            }
                            setEncoding(realJavaEncoding);
                        }
                    } else if (getEncoding() != null) {
                        String mysqlCharsetName2 = connectionCollationSuffix.length() > 0 ? connectionCollationCharset : getUseOldUTF8Behavior() ? CharsetMapping.NOT_USED : getServerCharset();
                        boolean ucs2 = false;
                        if ("ucs2".equalsIgnoreCase(mysqlCharsetName2) || "utf16".equalsIgnoreCase(mysqlCharsetName2) || "utf16le".equalsIgnoreCase(mysqlCharsetName2) || "utf32".equalsIgnoreCase(mysqlCharsetName2)) {
                            mysqlCharsetName2 = "utf8";
                            ucs2 = true;
                            if (getCharacterSetResults() == null) {
                                setCharacterSetResults("UTF-8");
                            }
                        }
                        if (dontCheckServerMatch || !characterSetNamesMatches(mysqlCharsetName2) || ucs2) {
                            try {
                                execSQL(null, "SET NAMES " + mysqlCharsetName2 + connectionCollationSuffix, -1, null, 1003, 1007, false, this.database, null, false);
                                this.serverVariables.put("character_set_client", mysqlCharsetName2);
                                this.serverVariables.put("character_set_connection", mysqlCharsetName2);
                            } catch (SQLException ex3) {
                                if (ex3.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords()) {
                                    throw ex3;
                                }
                            }
                        }
                        realJavaEncoding = getEncoding();
                    }
                }
                String onServer = null;
                boolean isNullOnServer = false;
                if (this.serverVariables != null) {
                    onServer = this.serverVariables.get("character_set_results");
                    isNullOnServer = onServer == null || ActionConst.NULL.equalsIgnoreCase(onServer) || onServer.length() == 0;
                }
                if (getCharacterSetResults() == null) {
                    if (!isNullOnServer) {
                        try {
                            execSQL(null, "SET character_set_results = NULL", -1, null, 1003, 1007, false, this.database, null, false);
                        } catch (SQLException ex4) {
                            if (ex4.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords()) {
                                throw ex4;
                            }
                        }
                        this.serverVariables.put(JDBC_LOCAL_CHARACTER_SET_RESULTS, null);
                    } else {
                        this.serverVariables.put(JDBC_LOCAL_CHARACTER_SET_RESULTS, onServer);
                    }
                } else {
                    if (getUseOldUTF8Behavior()) {
                        try {
                            execSQL(null, "SET NAMES latin1", -1, null, 1003, 1007, false, this.database, null, false);
                            this.serverVariables.put("character_set_client", CharsetMapping.NOT_USED);
                            this.serverVariables.put("character_set_connection", CharsetMapping.NOT_USED);
                        } catch (SQLException ex5) {
                            if (ex5.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords()) {
                                throw ex5;
                            }
                        }
                    }
                    String charsetResults = getCharacterSetResults();
                    if ("UTF-8".equalsIgnoreCase(charsetResults) || "UTF8".equalsIgnoreCase(charsetResults)) {
                        mysqlEncodingName = "utf8";
                    } else if ("null".equalsIgnoreCase(charsetResults)) {
                        mysqlEncodingName = ActionConst.NULL;
                    } else {
                        mysqlEncodingName = CharsetMapping.getMysqlCharsetForJavaEncoding(charsetResults.toUpperCase(Locale.ENGLISH), this);
                    }
                    if (mysqlEncodingName == null) {
                        throw SQLError.createSQLException("Can't map " + charsetResults + " given for characterSetResults to a supported MySQL encoding.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                    }
                    if (!mysqlEncodingName.equalsIgnoreCase(this.serverVariables.get("character_set_results"))) {
                        StringBuilder setBuf = new StringBuilder("SET character_set_results = ".length() + mysqlEncodingName.length());
                        setBuf.append("SET character_set_results = ").append(mysqlEncodingName);
                        try {
                            execSQL(null, setBuf.toString(), -1, null, 1003, 1007, false, this.database, null, false);
                        } catch (SQLException ex6) {
                            if (ex6.getErrorCode() != 1820 || getDisconnectOnExpiredPasswords()) {
                                throw ex6;
                            }
                        }
                        this.serverVariables.put(JDBC_LOCAL_CHARACTER_SET_RESULTS, mysqlEncodingName);
                        if (versionMeetsMinimum(5, 5, 0)) {
                            this.errorMessageEncoding = charsetResults;
                        }
                    } else {
                        this.serverVariables.put(JDBC_LOCAL_CHARACTER_SET_RESULTS, onServer);
                    }
                }
            } else {
                realJavaEncoding = getEncoding();
            }
            try {
                CharsetEncoder enc = Charset.forName(getEncoding()).newEncoder();
                CharBuffer cbuf = CharBuffer.allocate(1);
                ByteBuffer bbuf = ByteBuffer.allocate(1);
                cbuf.put("¥");
                cbuf.position(0);
                enc.encode(cbuf, bbuf, true);
                if (bbuf.get(0) == 92) {
                    this.requiresEscapingEncoder = true;
                } else {
                    cbuf.clear();
                    bbuf.clear();
                    cbuf.put("₩");
                    cbuf.position(0);
                    enc.encode(cbuf, bbuf, true);
                    if (bbuf.get(0) == 92) {
                        this.requiresEscapingEncoder = true;
                    }
                }
            } catch (UnsupportedCharsetException e2) {
                try {
                    byte[] bbuf2 = StringUtils.getBytes("¥", getEncoding());
                    if (bbuf2[0] == 92) {
                        this.requiresEscapingEncoder = true;
                    } else {
                        byte[] bbuf3 = StringUtils.getBytes("₩", getEncoding());
                        if (bbuf3[0] == 92) {
                            this.requiresEscapingEncoder = true;
                        }
                    }
                } catch (UnsupportedEncodingException ueex) {
                    throw SQLError.createSQLException("Unable to use encoding: " + getEncoding(), SQLError.SQL_STATE_GENERAL_ERROR, ueex, getExceptionInterceptor());
                }
            }
            return characterSetAlreadyConfigured;
        } finally {
            setEncoding(realJavaEncoding);
        }
    }

    private void configureTimezone() throws SQLException {
        String configuredTimeZoneOnServer = this.serverVariables.get("timezone");
        if (configuredTimeZoneOnServer == null) {
            configuredTimeZoneOnServer = this.serverVariables.get("time_zone");
            if ("SYSTEM".equalsIgnoreCase(configuredTimeZoneOnServer)) {
                configuredTimeZoneOnServer = this.serverVariables.get("system_time_zone");
            }
        }
        String canonicalTimezone = getServerTimezone();
        if ((getUseTimezone() || !getUseLegacyDatetimeCode()) && configuredTimeZoneOnServer != null && (canonicalTimezone == null || StringUtils.isEmptyOrWhitespaceOnly(canonicalTimezone))) {
            try {
                canonicalTimezone = TimeUtil.getCanonicalTimezone(configuredTimeZoneOnServer, getExceptionInterceptor());
            } catch (IllegalArgumentException iae) {
                throw SQLError.createSQLException(iae.getMessage(), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            }
        }
        if (canonicalTimezone != null && canonicalTimezone.length() > 0) {
            this.serverTimezoneTZ = TimeZone.getTimeZone(canonicalTimezone);
            if (!canonicalTimezone.equalsIgnoreCase("GMT") && this.serverTimezoneTZ.getID().equals("GMT")) {
                throw SQLError.createSQLException("No timezone mapping entry for '" + canonicalTimezone + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            this.isServerTzUTC = !this.serverTimezoneTZ.useDaylightTime() && this.serverTimezoneTZ.getRawOffset() == 0;
        }
    }

    private void createInitialHistogram(long[] breakpoints, long lowerBound, long upperBound) {
        double bucketSize = ((upperBound - lowerBound) / 20.0d) * 1.25d;
        if (bucketSize < 1.0d) {
            bucketSize = 1.0d;
        }
        for (int i = 0; i < 20; i++) {
            breakpoints[i] = lowerBound;
            lowerBound = (long) (lowerBound + bucketSize);
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void createNewIO(boolean isForReconnect) throws SQLException {
        synchronized (getConnectionMutex()) {
            Properties mergedProps = exposeAsProperties(this.props);
            if (!getHighAvailability()) {
                connectOneTryOnly(isForReconnect, mergedProps);
            } else {
                connectWithRetries(isForReconnect, mergedProps);
            }
        }
    }

    private void connectWithRetries(boolean isForReconnect, Properties mergedProps) throws SQLException, InterruptedException {
        boolean oldAutoCommit;
        int oldIsolationLevel;
        boolean oldReadOnly;
        String oldCatalog;
        double timeout = getInitialTimeout();
        boolean connectionGood = false;
        Exception connectionException = null;
        int attemptCount = 0;
        while (true) {
            if (attemptCount >= getMaxReconnects() || connectionGood) {
                break;
            }
            try {
                if (this.f0io != null) {
                    this.f0io.forceClose();
                }
                coreConnect(mergedProps);
                pingInternal(false, 0);
                synchronized (getConnectionMutex()) {
                    this.connectionId = this.f0io.getThreadId();
                    this.isClosed = false;
                    oldAutoCommit = getAutoCommit();
                    oldIsolationLevel = this.isolationLevel;
                    oldReadOnly = isReadOnly(false);
                    oldCatalog = getCatalog();
                    this.f0io.setStatementInterceptors(this.statementInterceptors);
                }
                initializePropsFromServer();
                if (isForReconnect) {
                    setAutoCommit(oldAutoCommit);
                    if (this.hasIsolationLevels) {
                        setTransactionIsolation(oldIsolationLevel);
                    }
                    setCatalog(oldCatalog);
                    setReadOnly(oldReadOnly);
                }
                connectionGood = true;
            } catch (Exception EEE) {
                connectionException = EEE;
                connectionGood = false;
                if (0 != 0) {
                    break;
                }
                if (attemptCount > 0) {
                    try {
                        Thread.sleep(((long) timeout) * 1000);
                    } catch (InterruptedException e) {
                    }
                }
                attemptCount++;
            }
        }
        if (!connectionGood) {
            SQLException chainedEx = SQLError.createSQLException(Messages.getString("Connection.UnableToConnectWithRetries", new Object[]{Integer.valueOf(getMaxReconnects())}), SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE, getExceptionInterceptor());
            chainedEx.initCause(connectionException);
            throw chainedEx;
        }
        if (getParanoid() && !getHighAvailability()) {
            this.password = null;
            this.user = null;
        }
        if (isForReconnect) {
            Iterator<Statement> statementIter = this.openStatements.iterator();
            Stack<Statement> serverPreparedStatements = null;
            while (statementIter.hasNext()) {
                Statement statementObj = statementIter.next();
                if (statementObj instanceof ServerPreparedStatement) {
                    if (serverPreparedStatements == null) {
                        serverPreparedStatements = new Stack<>();
                    }
                    serverPreparedStatements.add(statementObj);
                }
            }
            if (serverPreparedStatements != null) {
                while (!serverPreparedStatements.isEmpty()) {
                    ((ServerPreparedStatement) serverPreparedStatements.pop()).rePrepare();
                }
            }
        }
    }

    private void coreConnect(Properties mergedProps) throws SQLException, NumberFormatException, IOException {
        int newPort = 3306;
        String newHost = "localhost";
        String protocol = mergedProps.getProperty(NonRegisteringDriver.PROTOCOL_PROPERTY_KEY);
        if (protocol != null) {
            if (!"tcp".equalsIgnoreCase(protocol) && "pipe".equalsIgnoreCase(protocol)) {
                setSocketFactoryClassName(NamedPipeSocketFactory.class.getName());
                String path = mergedProps.getProperty(NonRegisteringDriver.PATH_PROPERTY_KEY);
                if (path != null) {
                    mergedProps.setProperty(NamedPipeSocketFactory.NAMED_PIPE_PROP_NAME, path);
                }
            } else {
                newHost = normalizeHost(mergedProps.getProperty(NonRegisteringDriver.HOST_PROPERTY_KEY));
                newPort = parsePortNumber(mergedProps.getProperty(NonRegisteringDriver.PORT_PROPERTY_KEY, "3306"));
            }
        } else {
            String[] parsedHostPortPair = NonRegisteringDriver.parseHostPortPair(this.hostPortPair);
            newHost = normalizeHost(parsedHostPortPair[0]);
            if (parsedHostPortPair[1] != null) {
                newPort = parsePortNumber(parsedHostPortPair[1]);
            }
        }
        this.port = newPort;
        this.host = newHost;
        this.sessionMaxRows = -1;
        this.serverVariables = new HashMap();
        this.serverVariables.put("character_set_server", "utf8");
        this.f0io = new MysqlIO(newHost, newPort, mergedProps, getSocketFactoryClassName(), getProxy(), getSocketTimeout(), this.largeRowSizeThreshold.getValueAsInt());
        this.f0io.doHandshake(this.user, this.password, this.database);
        if (versionMeetsMinimum(5, 5, 0)) {
            this.errorMessageEncoding = this.f0io.getEncodingForHandshake();
        }
    }

    private String normalizeHost(String hostname) {
        if (hostname == null || StringUtils.isEmptyOrWhitespaceOnly(hostname)) {
            return "localhost";
        }
        return hostname;
    }

    private int parsePortNumber(String portAsString) throws SQLException, NumberFormatException {
        try {
            int portNumber = Integer.parseInt(portAsString);
            return portNumber;
        } catch (NumberFormatException e) {
            throw SQLError.createSQLException("Illegal connection port value '" + portAsString + "'", SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, getExceptionInterceptor());
        }
    }

    private void connectOneTryOnly(boolean isForReconnect, Properties mergedProps) throws SQLException {
        try {
            coreConnect(mergedProps);
            this.connectionId = this.f0io.getThreadId();
            this.isClosed = false;
            boolean oldAutoCommit = getAutoCommit();
            int oldIsolationLevel = this.isolationLevel;
            boolean oldReadOnly = isReadOnly(false);
            String oldCatalog = getCatalog();
            this.f0io.setStatementInterceptors(this.statementInterceptors);
            initializePropsFromServer();
            if (isForReconnect) {
                setAutoCommit(oldAutoCommit);
                if (this.hasIsolationLevels) {
                    setTransactionIsolation(oldIsolationLevel);
                }
                setCatalog(oldCatalog);
                setReadOnly(oldReadOnly);
            }
        } catch (Exception EEE) {
            if ((EEE instanceof SQLException) && ((SQLException) EEE).getErrorCode() == 1820 && !getDisconnectOnExpiredPasswords()) {
                return;
            }
            if (this.f0io != null) {
                this.f0io.forceClose();
            }
            if (EEE instanceof SQLException) {
                throw ((SQLException) EEE);
            }
            SQLException chainedEx = SQLError.createSQLException(Messages.getString("Connection.UnableToConnect"), SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE, getExceptionInterceptor());
            chainedEx.initCause(EEE);
            throw chainedEx;
        }
    }

    private void createPreparedStatementCaches() throws SQLException {
        synchronized (getConnectionMutex()) {
            int cacheSize = getPreparedStatementCacheSize();
            try {
                try {
                    try {
                        Class<?> factoryClass = Class.forName(getParseInfoCacheFactory());
                        CacheAdapterFactory<String, PreparedStatement.ParseInfo> cacheFactory = (CacheAdapterFactory) factoryClass.newInstance();
                        this.cachedPreparedStatementParams = cacheFactory.getInstance(this, this.myURL, getPreparedStatementCacheSize(), getPreparedStatementCacheSqlLimit(), this.props);
                        if (getUseServerPreparedStmts()) {
                            this.serverSideStatementCheckCache = new LRUCache<>(cacheSize);
                            this.serverSideStatementCache = new LRUCache<CompoundCacheKey, ServerPreparedStatement>(cacheSize) { // from class: com.mysql.jdbc.ConnectionImpl.3
                                private static final long serialVersionUID = 7692318650375988114L;

                                @Override // com.mysql.jdbc.util.LRUCache, java.util.LinkedHashMap
                                protected boolean removeEldestEntry(Map.Entry<CompoundCacheKey, ServerPreparedStatement> eldest) {
                                    if (this.maxElements <= 1) {
                                        return false;
                                    }
                                    boolean removeIt = super.removeEldestEntry(eldest);
                                    if (removeIt) {
                                        ServerPreparedStatement ps = eldest.getValue();
                                        ps.isCached = false;
                                        ps.setClosed(false);
                                        try {
                                            ps.close();
                                        } catch (SQLException e) {
                                        }
                                    }
                                    return removeIt;
                                }
                            };
                        }
                    } catch (ClassNotFoundException e) {
                        SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantFindCacheFactory", new Object[]{getParseInfoCacheFactory(), "parseInfoCacheFactory"}), getExceptionInterceptor());
                        sqlEx.initCause(e);
                        throw sqlEx;
                    }
                } catch (InstantiationException e2) {
                    SQLException sqlEx2 = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[]{getParseInfoCacheFactory(), "parseInfoCacheFactory"}), getExceptionInterceptor());
                    sqlEx2.initCause(e2);
                    throw sqlEx2;
                }
            } catch (IllegalAccessException e3) {
                SQLException sqlEx3 = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[]{getParseInfoCacheFactory(), "parseInfoCacheFactory"}), getExceptionInterceptor());
                sqlEx3.initCause(e3);
                throw sqlEx3;
            }
        }
    }

    @Override // java.sql.Connection
    public java.sql.Statement createStatement() throws SQLException {
        return createStatement(1003, 1007);
    }

    @Override // java.sql.Connection
    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        checkClosed();
        StatementImpl stmt = new StatementImpl(getMultiHostSafeProxy(), this.database);
        stmt.setResultSetType(resultSetType);
        stmt.setResultSetConcurrency(resultSetConcurrency);
        return stmt;
    }

    @Override // java.sql.Connection
    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (getPedantic() && resultSetHoldability != 1) {
            throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
        return createStatement(resultSetType, resultSetConcurrency);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void dumpTestcaseQuery(String query) {
        System.err.println(query);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Connection duplicate() throws SQLException {
        return new ConnectionImpl(this.origHostToConnectTo, this.origPortToConnectTo, this.props, this.origDatabaseToConnectTo, this.myURL);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata) throws SQLException {
        return execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x018b, code lost:
    
        throw r30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0195, code lost:
    
        r12.lastQueryFinishedTime = java.lang.System.currentTimeMillis();
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x01a0, code lost:
    
        if (getGatherPerformanceMetrics() == false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x01a3, code lost:
    
        registerQueryExecutionTime(java.lang.System.currentTimeMillis() - r24);
     */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0068 A[Catch: SQLException -> 0x00b1, Exception -> 0x0139, all -> 0x0184, all -> 0x01af, TryCatch #0 {all -> 0x0184, blocks: (B:27:0x0068, B:29:0x0075, B:31:0x007d, B:32:0x008f, B:39:0x00b3, B:41:0x00ba, B:42:0x0100, B:44:0x0107, B:46:0x0114, B:47:0x011b, B:52:0x0138, B:48:0x0123, B:50:0x0130, B:54:0x013b, B:56:0x0142, B:58:0x014a, B:59:0x0151, B:63:0x0167, B:64:0x0183, B:60:0x0159, B:62:0x0161), top: B:88:0x0065, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x008f A[Catch: SQLException -> 0x00b1, Exception -> 0x0139, all -> 0x0184, all -> 0x01af, TryCatch #0 {all -> 0x0184, blocks: (B:27:0x0068, B:29:0x0075, B:31:0x007d, B:32:0x008f, B:39:0x00b3, B:41:0x00ba, B:42:0x0100, B:44:0x0107, B:46:0x0114, B:47:0x011b, B:52:0x0138, B:48:0x0123, B:50:0x0130, B:54:0x013b, B:56:0x0142, B:58:0x014a, B:59:0x0151, B:63:0x0167, B:64:0x0183, B:60:0x0159, B:62:0x0161), top: B:88:0x0065, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0195 A[Catch: all -> 0x01af, DONT_GENERATE, TryCatch #4 {, blocks: (B:4:0x0008, B:6:0x000f, B:10:0x001d, B:12:0x0026, B:14:0x0034, B:16:0x003b, B:18:0x0042, B:22:0x004e, B:24:0x005e, B:27:0x0068, B:29:0x0075, B:31:0x007d, B:69:0x018c, B:71:0x0195, B:72:0x019c, B:74:0x01a3, B:35:0x00ad, B:32:0x008f, B:39:0x00b3, B:41:0x00ba, B:42:0x0100, B:44:0x0107, B:46:0x0114, B:47:0x011b, B:52:0x0138, B:48:0x0123, B:50:0x0130, B:54:0x013b, B:56:0x0142, B:58:0x014a, B:59:0x0151, B:63:0x0167, B:64:0x0183, B:60:0x0159, B:62:0x0161, B:81:0x018c, B:83:0x0195, B:84:0x019c, B:86:0x01a3, B:68:0x018b), top: B:91:0x0008, inners: #0, #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01a3 A[Catch: all -> 0x01af, DONT_GENERATE, TryCatch #4 {, blocks: (B:4:0x0008, B:6:0x000f, B:10:0x001d, B:12:0x0026, B:14:0x0034, B:16:0x003b, B:18:0x0042, B:22:0x004e, B:24:0x005e, B:27:0x0068, B:29:0x0075, B:31:0x007d, B:69:0x018c, B:71:0x0195, B:72:0x019c, B:74:0x01a3, B:35:0x00ad, B:32:0x008f, B:39:0x00b3, B:41:0x00ba, B:42:0x0100, B:44:0x0107, B:46:0x0114, B:47:0x011b, B:52:0x0138, B:48:0x0123, B:50:0x0130, B:54:0x013b, B:56:0x0142, B:58:0x014a, B:59:0x0151, B:63:0x0167, B:64:0x0183, B:60:0x0159, B:62:0x0161, B:81:0x018c, B:83:0x0195, B:84:0x019c, B:86:0x01a3, B:68:0x018b), top: B:91:0x0008, inners: #0, #1 }] */
    @Override // com.mysql.jdbc.MySQLConnection
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.mysql.jdbc.ResultSetInternalMethods execSQL(com.mysql.jdbc.StatementImpl r13, java.lang.String r14, int r15, com.mysql.jdbc.Buffer r16, int r17, int r18, boolean r19, java.lang.String r20, com.mysql.jdbc.Field[] r21, boolean r22) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 439
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ConnectionImpl.execSQL(com.mysql.jdbc.StatementImpl, java.lang.String, int, com.mysql.jdbc.Buffer, int, int, boolean, java.lang.String, com.mysql.jdbc.Field[], boolean):com.mysql.jdbc.ResultSetInternalMethods");
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String extractSqlFromPacket(String possibleSqlQuery, Buffer queryPacket, int endOfQueryPacketPosition) throws SQLException {
        String extractedSql = null;
        if (possibleSqlQuery != null) {
            if (possibleSqlQuery.length() > getMaxQuerySizeToLog()) {
                extractedSql = possibleSqlQuery.substring(0, getMaxQuerySizeToLog()) + Messages.getString("MysqlIO.25");
            } else {
                extractedSql = possibleSqlQuery;
            }
        }
        if (extractedSql == null) {
            int extractPosition = endOfQueryPacketPosition;
            boolean truncated = false;
            if (endOfQueryPacketPosition > getMaxQuerySizeToLog()) {
                extractPosition = getMaxQuerySizeToLog();
                truncated = true;
            }
            extractedSql = StringUtils.toString(queryPacket.getByteBuffer(), 5, extractPosition - 5);
            if (truncated) {
                extractedSql = extractedSql + Messages.getString("MysqlIO.25");
            }
        }
        return extractedSql;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public StringBuilder generateConnectionCommentBlock(StringBuilder buf) {
        buf.append("/* conn id ");
        buf.append(getId());
        buf.append(" clock: ");
        buf.append(System.currentTimeMillis());
        buf.append(" */ ");
        return buf;
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public int getActiveStatementCount() {
        return this.openStatements.size();
    }

    @Override // java.sql.Connection
    public boolean getAutoCommit() throws SQLException {
        boolean z;
        synchronized (getConnectionMutex()) {
            z = this.autoCommit;
        }
        return z;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Calendar getCalendarInstanceForSessionOrNew() {
        if (getDynamicCalendars()) {
            return Calendar.getInstance();
        }
        return getSessionLockedCalendar();
    }

    @Override // java.sql.Connection
    public String getCatalog() throws SQLException {
        String str;
        synchronized (getConnectionMutex()) {
            str = this.database;
        }
        return str;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getCharacterSetMetadata() {
        String str;
        synchronized (getConnectionMutex()) {
            str = this.characterSetMetadata;
        }
        return str;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public SingleByteCharsetConverter getCharsetConverter(String javaEncodingName) throws SQLException {
        if (javaEncodingName == null || this.usePlatformCharsetConverters) {
            return null;
        }
        synchronized (this.charsetConverterMap) {
            Object asObject = this.charsetConverterMap.get(javaEncodingName);
            if (asObject == CHARSET_CONVERTER_NOT_AVAILABLE_MARKER) {
                return null;
            }
            SingleByteCharsetConverter converter = (SingleByteCharsetConverter) asObject;
            if (converter == null) {
                try {
                    converter = SingleByteCharsetConverter.getInstance(javaEncodingName, this);
                    if (converter == null) {
                        this.charsetConverterMap.put(javaEncodingName, CHARSET_CONVERTER_NOT_AVAILABLE_MARKER);
                    } else {
                        this.charsetConverterMap.put(javaEncodingName, converter);
                    }
                } catch (UnsupportedEncodingException e) {
                    this.charsetConverterMap.put(javaEncodingName, CHARSET_CONVERTER_NOT_AVAILABLE_MARKER);
                    converter = null;
                }
            }
            return converter;
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    @Deprecated
    public String getCharsetNameForIndex(int charsetIndex) throws SQLException {
        return getEncodingForIndex(charsetIndex);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getEncodingForIndex(int charsetIndex) throws SQLException {
        String cs;
        String javaEncoding = null;
        if (getUseOldUTF8Behavior()) {
            return getEncoding();
        }
        if (charsetIndex != -1) {
            try {
                if (this.indexToCustomMysqlCharset != null && (cs = this.indexToCustomMysqlCharset.get(Integer.valueOf(charsetIndex))) != null) {
                    javaEncoding = CharsetMapping.getJavaEncodingForMysqlCharset(cs, getEncoding());
                }
                if (javaEncoding == null) {
                    javaEncoding = CharsetMapping.getJavaEncodingForCollationIndex(Integer.valueOf(charsetIndex), getEncoding());
                }
                if (javaEncoding == null) {
                    javaEncoding = getEncoding();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                throw SQLError.createSQLException("Unknown character set index for field '" + charsetIndex + "' received from server.", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            } catch (RuntimeException ex) {
                SQLException sqlEx = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                sqlEx.initCause(ex);
                throw sqlEx;
            }
        } else {
            javaEncoding = getEncoding();
        }
        return javaEncoding;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public TimeZone getDefaultTimeZone() {
        return getCacheDefaultTimezone() ? this.defaultTimeZone : TimeUtil.getDefaultTimeZone(false);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getErrorMessageEncoding() {
        return this.errorMessageEncoding;
    }

    @Override // java.sql.Connection
    public int getHoldability() throws SQLException {
        return 2;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public long getId() {
        return this.connectionId;
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public long getIdleFor() {
        long jCurrentTimeMillis;
        synchronized (getConnectionMutex()) {
            jCurrentTimeMillis = this.lastQueryFinishedTime == 0 ? 0L : System.currentTimeMillis() - this.lastQueryFinishedTime;
        }
        return jCurrentTimeMillis;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public MysqlIO getIO() throws SQLException {
        if (this.f0io == null || this.isClosed) {
            throw SQLError.createSQLException("Operation not allowed on closed connection", SQLError.SQL_STATE_CONNECTION_NOT_OPEN, getExceptionInterceptor());
        }
        return this.f0io;
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public Log getLog() throws SQLException {
        return this.log;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getMaxBytesPerChar(String javaCharsetName) throws SQLException {
        return getMaxBytesPerChar(null, javaCharsetName);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getMaxBytesPerChar(Integer charsetIndex, String javaCharsetName) throws SQLException {
        String charset = null;
        int res = 1;
        try {
            if (this.indexToCustomMysqlCharset != null) {
                charset = this.indexToCustomMysqlCharset.get(charsetIndex);
            }
            if (charset == null) {
                charset = CharsetMapping.getMysqlCharsetNameForCollationIndex(charsetIndex);
            }
            if (charset == null) {
                charset = CharsetMapping.getMysqlCharsetForJavaEncoding(javaCharsetName, this);
            }
            Integer mblen = null;
            if (this.mysqlCharsetToCustomMblen != null) {
                mblen = this.mysqlCharsetToCustomMblen.get(charset);
            }
            if (mblen == null) {
                mblen = Integer.valueOf(CharsetMapping.getMblen(charset));
            }
            if (mblen != null) {
                res = mblen.intValue();
            }
            return res;
        } catch (RuntimeException ex) {
            SQLException sqlEx = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
            sqlEx.initCause(ex);
            throw sqlEx;
        } catch (SQLException ex2) {
            throw ex2;
        }
    }

    @Override // java.sql.Connection
    public java.sql.DatabaseMetaData getMetaData() throws SQLException {
        return getMetaData(true, true);
    }

    private java.sql.DatabaseMetaData getMetaData(boolean checkClosed, boolean checkForInfoSchema) throws SQLException {
        if (checkClosed) {
            checkClosed();
        }
        return DatabaseMetaData.getInstance(getMultiHostSafeProxy(), this.database, checkForInfoSchema);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public java.sql.Statement getMetadataSafeStatement() throws SQLException {
        return getMetadataSafeStatement(0);
    }

    public java.sql.Statement getMetadataSafeStatement(int maxRows) throws SQLException {
        java.sql.Statement stmt = createStatement();
        stmt.setMaxRows(maxRows == -1 ? 0 : maxRows);
        stmt.setEscapeProcessing(false);
        if (stmt.getFetchSize() != 0) {
            stmt.setFetchSize(0);
        }
        return stmt;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getNetBufferLength() {
        return this.netBufferLength;
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public String getServerCharacterEncoding() {
        return getServerCharset();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public String getServerCharset() {
        if (this.f0io.versionMeetsMinimum(4, 1, 0)) {
            String charset = null;
            if (this.indexToCustomMysqlCharset != null) {
                charset = this.indexToCustomMysqlCharset.get(Integer.valueOf(this.f0io.serverCharsetIndex));
            }
            if (charset == null) {
                charset = CharsetMapping.getMysqlCharsetNameForCollationIndex(Integer.valueOf(this.f0io.serverCharsetIndex));
            }
            return charset != null ? charset : this.serverVariables.get("character_set_server");
        }
        return this.serverVariables.get("character_set");
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getServerMajorVersion() {
        return this.f0io.getServerMajorVersion();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getServerMinorVersion() {
        return this.f0io.getServerMinorVersion();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getServerSubMinorVersion() {
        return this.f0io.getServerSubMinorVersion();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public TimeZone getServerTimezoneTZ() {
        return this.serverTimezoneTZ;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getServerVariable(String variableName) {
        if (this.serverVariables != null) {
            return this.serverVariables.get(variableName);
        }
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getServerVersion() {
        return this.f0io.getServerVersion();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Calendar getSessionLockedCalendar() {
        return this.sessionCalendar;
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x00d5, code lost:
    
        throw r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00d9, code lost:
    
        if (0 == 0) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00dc, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00ea, code lost:
    
        if (0 == 0) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00ed, code lost:
    
        r7.close();
     */
    @Override // java.sql.Connection
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int getTransactionIsolation() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 266
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ConnectionImpl.getTransactionIsolation():int");
    }

    @Override // java.sql.Connection
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        Map<String, Class<?>> map;
        synchronized (getConnectionMutex()) {
            if (this.typeMap == null) {
                this.typeMap = new HashMap();
            }
            map = this.typeMap;
        }
        return map;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getURL() {
        return this.myURL;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getUser() {
        return this.user;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Calendar getUtcCalendar() {
        return this.utcCalendar;
    }

    @Override // java.sql.Connection
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean hasSameProperties(Connection c) {
        return this.props.equals(c.getProperties());
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public Properties getProperties() {
        return this.props;
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public boolean hasTriedMaster() {
        return this.hasTriedMasterFlag;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void incrementNumberOfPreparedExecutes() {
        if (getGatherPerformanceMetrics()) {
            this.numberOfPreparedExecutes++;
            this.numberOfQueriesIssued++;
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void incrementNumberOfPrepares() {
        if (getGatherPerformanceMetrics()) {
            this.numberOfPrepares++;
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void incrementNumberOfResultSetsCreated() {
        if (getGatherPerformanceMetrics()) {
            this.numberOfResultSetsCreated++;
        }
    }

    private void initializeDriverProperties(Properties info) throws SQLException {
        initializeProperties(info);
        String exceptionInterceptorClasses = getExceptionInterceptors();
        if (exceptionInterceptorClasses != null && !"".equals(exceptionInterceptorClasses)) {
            this.exceptionInterceptor = new ExceptionInterceptorChain(exceptionInterceptorClasses);
        }
        this.usePlatformCharsetConverters = getUseJvmCharsetConverters();
        this.log = LogFactory.getLogger(getLogger(), LOGGER_INSTANCE_NAME, getExceptionInterceptor());
        if (getProfileSql() || getLogSlowQueries() || getUseUsageAdvisor()) {
            this.eventSink = ProfilerEventHandlerFactory.getInstance(getMultiHostSafeProxy());
        }
        if (getCachePreparedStatements()) {
            createPreparedStatementCaches();
        }
        if (getNoDatetimeStringSync() && getUseTimezone()) {
            throw SQLError.createSQLException("Can't enable noDatetimeStringSync and useTimezone configuration properties at the same time", SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, getExceptionInterceptor());
        }
        if (getCacheCallableStatements()) {
            this.parsedCallableStatementCache = new LRUCache<>(getCallableStatementCacheSize());
        }
        if (getAllowMultiQueries()) {
            setCacheResultSetMetadata(false);
        }
        if (getCacheResultSetMetadata()) {
            this.resultSetMetadataCache = new LRUCache<>(getMetadataCacheSize());
        }
        if (getSocksProxyHost() != null) {
            setSocketFactoryClassName("com.mysql.jdbc.SocksProxySocketFactory");
        }
    }

    private void initializePropsFromServer() throws SQLException {
        String defaultMetadataCharset;
        String connectionInterceptorClasses = getConnectionLifecycleInterceptors();
        this.connectionLifecycleInterceptors = null;
        if (connectionInterceptorClasses != null) {
            this.connectionLifecycleInterceptors = Util.loadExtensions(this, this.props, connectionInterceptorClasses, "Connection.badLifecycleInterceptor", getExceptionInterceptor());
        }
        setSessionVariables();
        if (!versionMeetsMinimum(4, 1, 0)) {
            setTransformedBitIsBoolean(false);
        }
        this.parserKnowsUnicode = versionMeetsMinimum(4, 1, 0);
        if (getUseServerPreparedStmts() && versionMeetsMinimum(4, 1, 0)) {
            this.useServerPreparedStmts = true;
            if (versionMeetsMinimum(5, 0, 0) && !versionMeetsMinimum(5, 0, 3)) {
                this.useServerPreparedStmts = false;
            }
        }
        if (versionMeetsMinimum(3, 21, 22)) {
            loadServerVariables();
            if (versionMeetsMinimum(5, 0, 2)) {
                this.autoIncrementIncrement = getServerVariableAsInt("auto_increment_increment", 1);
            } else {
                this.autoIncrementIncrement = 1;
            }
            buildCollationMapping();
            if (this.f0io.serverCharsetIndex == 0) {
                String collationServer = this.serverVariables.get("collation_server");
                if (collationServer != null) {
                    int i = 1;
                    while (true) {
                        if (i >= CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME.length) {
                            break;
                        }
                        if (!CharsetMapping.COLLATION_INDEX_TO_COLLATION_NAME[i].equals(collationServer)) {
                            i++;
                        } else {
                            this.f0io.serverCharsetIndex = i;
                            break;
                        }
                    }
                } else {
                    this.f0io.serverCharsetIndex = 45;
                }
            }
            LicenseConfiguration.checkLicenseType(this.serverVariables);
            String lowerCaseTables = this.serverVariables.get("lower_case_table_names");
            this.lowerCaseTableNames = CustomBooleanEditor.VALUE_ON.equalsIgnoreCase(lowerCaseTables) || "1".equalsIgnoreCase(lowerCaseTables) || "2".equalsIgnoreCase(lowerCaseTables);
            this.storesLowerCaseTableName = "1".equalsIgnoreCase(lowerCaseTables) || CustomBooleanEditor.VALUE_ON.equalsIgnoreCase(lowerCaseTables);
            configureTimezone();
            if (this.serverVariables.containsKey("max_allowed_packet")) {
                int serverMaxAllowedPacket = getServerVariableAsInt("max_allowed_packet", -1);
                if (serverMaxAllowedPacket != -1 && (serverMaxAllowedPacket < getMaxAllowedPacket() || getMaxAllowedPacket() <= 0)) {
                    setMaxAllowedPacket(serverMaxAllowedPacket);
                } else if (serverMaxAllowedPacket == -1 && getMaxAllowedPacket() == -1) {
                    setMaxAllowedPacket(65535);
                }
                if (getUseServerPrepStmts()) {
                    int preferredBlobSendChunkSize = getBlobSendChunkSize();
                    int allowedBlobSendChunkSize = Math.min(preferredBlobSendChunkSize, getMaxAllowedPacket()) - SonyType1MakernoteDirectory.TAG_MULTI_FRAME_NOISE_REDUCTION;
                    if (allowedBlobSendChunkSize <= 0) {
                        throw SQLError.createSQLException("Connection setting too low for 'maxAllowedPacket'. When 'useServerPrepStmts=true', 'maxAllowedPacket' must be higher than " + SonyType1MakernoteDirectory.TAG_MULTI_FRAME_NOISE_REDUCTION + ". Check also 'max_allowed_packet' in MySQL configuration files.", SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, getExceptionInterceptor());
                    }
                    setBlobSendChunkSize(String.valueOf(allowedBlobSendChunkSize));
                }
            }
            if (this.serverVariables.containsKey("net_buffer_length")) {
                this.netBufferLength = getServerVariableAsInt("net_buffer_length", 16384);
            }
            checkTransactionIsolationLevel();
            if (!versionMeetsMinimum(4, 1, 0)) {
                checkServerEncoding();
            }
            this.f0io.checkForCharsetMismatch();
            if (this.serverVariables.containsKey("sql_mode")) {
                String sqlModeAsString = this.serverVariables.get("sql_mode");
                if (StringUtils.isStrictlyNumeric(sqlModeAsString)) {
                    this.useAnsiQuotes = (Integer.parseInt(sqlModeAsString) & 4) > 0;
                } else if (sqlModeAsString != null) {
                    this.useAnsiQuotes = sqlModeAsString.indexOf("ANSI_QUOTES") != -1;
                    this.noBackslashEscapes = sqlModeAsString.indexOf("NO_BACKSLASH_ESCAPES") != -1;
                    this.serverTruncatesFracSecs = sqlModeAsString.indexOf("TIME_TRUNCATE_FRACTIONAL") != -1;
                }
            }
        }
        configureClientCharacterSet(false);
        try {
            this.errorMessageEncoding = CharsetMapping.getCharacterEncodingForErrorMessages(this);
            if (versionMeetsMinimum(3, 23, 15)) {
                this.transactionsSupported = true;
                handleAutoCommitDefaults();
            } else {
                this.transactionsSupported = false;
            }
            if (versionMeetsMinimum(3, 23, 36)) {
                this.hasIsolationLevels = true;
            } else {
                this.hasIsolationLevels = false;
            }
            this.hasQuotedIdentifiers = versionMeetsMinimum(3, 23, 6);
            this.f0io.resetMaxBuf();
            if (this.f0io.versionMeetsMinimum(4, 1, 0)) {
                String characterSetResultsOnServerMysql = this.serverVariables.get(JDBC_LOCAL_CHARACTER_SET_RESULTS);
                if (characterSetResultsOnServerMysql == null || StringUtils.startsWithIgnoreCaseAndWs(characterSetResultsOnServerMysql, ActionConst.NULL) || characterSetResultsOnServerMysql.length() == 0) {
                    String defaultMetadataCharsetMysql = this.serverVariables.get("character_set_system");
                    if (defaultMetadataCharsetMysql != null) {
                        defaultMetadataCharset = CharsetMapping.getJavaEncodingForMysqlCharset(defaultMetadataCharsetMysql);
                    } else {
                        defaultMetadataCharset = "UTF-8";
                    }
                    this.characterSetMetadata = defaultMetadataCharset;
                } else {
                    this.characterSetResultsOnServer = CharsetMapping.getJavaEncodingForMysqlCharset(characterSetResultsOnServerMysql);
                    this.characterSetMetadata = this.characterSetResultsOnServer;
                }
            } else {
                this.characterSetMetadata = getEncoding();
            }
            if (versionMeetsMinimum(4, 1, 0) && !versionMeetsMinimum(4, 1, 10) && getAllowMultiQueries() && isQueryCacheEnabled()) {
                setAllowMultiQueries(false);
            }
            if (versionMeetsMinimum(5, 0, 0) && ((getUseLocalTransactionState() || getElideSetAutoCommits()) && isQueryCacheEnabled() && !versionMeetsMinimum(5, 1, 32))) {
                setUseLocalTransactionState(false);
                setElideSetAutoCommits(false);
            }
            setupServerForTruncationChecks();
        } catch (RuntimeException ex) {
            SQLException sqlEx = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
            sqlEx.initCause(ex);
            throw sqlEx;
        } catch (SQLException ex2) {
            throw ex2;
        }
    }

    public boolean isQueryCacheEnabled() {
        return "ON".equalsIgnoreCase(this.serverVariables.get("query_cache_type")) && !"0".equalsIgnoreCase(this.serverVariables.get("query_cache_size"));
    }

    private int getServerVariableAsInt(String variableName, int fallbackValue) throws SQLException {
        try {
            return Integer.parseInt(this.serverVariables.get(variableName));
        } catch (NumberFormatException e) {
            getLog().logWarn(Messages.getString("Connection.BadValueInServerVariables", new Object[]{variableName, this.serverVariables.get(variableName), Integer.valueOf(fallbackValue)}));
            return fallbackValue;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0072, code lost:
    
        throw r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0079, code lost:
    
        r8.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0086, code lost:
    
        if (r9 == null) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0089, code lost:
    
        r9.close();
     */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0079 A[Catch: SQLException -> 0x0082, TRY_ENTER, TRY_LEAVE, TryCatch #1 {SQLException -> 0x0082, blocks: (B:26:0x0079, B:52:0x0079, B:11:0x0033, B:13:0x004d), top: B:59:0x0033, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0089 A[Catch: SQLException -> 0x0093, TRY_ENTER, TRY_LEAVE, TryCatch #2 {SQLException -> 0x0093, blocks: (B:30:0x0089, B:55:0x0089), top: B:60:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00d1 A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00b5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void handleAutoCommitDefaults() throws java.sql.SQLException {
        /*
            r5 = this;
            r0 = 0
            r6 = r0
            r0 = r5
            boolean r0 = r0.getElideSetAutoCommits()
            if (r0 != 0) goto L9f
            r0 = r5
            java.util.Map<java.lang.String, java.lang.String> r0 = r0.serverVariables
            java.lang.String r1 = "init_connect"
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            r7 = r0
            r0 = r5
            r1 = 4
            r2 = 1
            r3 = 2
            boolean r0 = r0.versionMeetsMinimum(r1, r2, r3)
            if (r0 == 0) goto L9a
            r0 = r7
            if (r0 == 0) goto L9a
            r0 = r7
            int r0 = r0.length()
            if (r0 <= 0) goto L9a
            r0 = 0
            r8 = r0
            r0 = 0
            r9 = r0
            r0 = r5
            java.sql.Statement r0 = r0.getMetadataSafeStatement()     // Catch: java.lang.Throwable -> L6b
            r9 = r0
            r0 = r9
            java.lang.String r1 = "SELECT @@session.autocommit"
            java.sql.ResultSet r0 = r0.executeQuery(r1)     // Catch: java.lang.Throwable -> L6b
            r8 = r0
            r0 = r8
            boolean r0 = r0.next()     // Catch: java.lang.Throwable -> L6b
            if (r0 == 0) goto L65
            r0 = r5
            r1 = r8
            r2 = 1
            boolean r1 = r1.getBoolean(r2)     // Catch: java.lang.Throwable -> L6b
            r0.autoCommit = r1     // Catch: java.lang.Throwable -> L6b
            r0 = r5
            boolean r0 = r0.autoCommit     // Catch: java.lang.Throwable -> L6b
            if (r0 != 0) goto L63
            r0 = 1
            goto L64
        L63:
            r0 = 0
        L64:
            r6 = r0
        L65:
            r0 = jsr -> L73
        L68:
            goto L97
        L6b:
            r10 = move-exception
            r0 = jsr -> L73
        L70:
            r1 = r10
            throw r1
        L73:
            r11 = r0
            r0 = r8
            if (r0 == 0) goto L84
            r0 = r8
            r0.close()     // Catch: java.sql.SQLException -> L82
            goto L84
        L82:
            r12 = move-exception
        L84:
            r0 = r9
            if (r0 == 0) goto L95
            r0 = r9
            r0.close()     // Catch: java.sql.SQLException -> L93
            goto L95
        L93:
            r12 = move-exception
        L95:
            ret r11
        L97:
            goto L9c
        L9a:
            r0 = 1
            r6 = r0
        L9c:
            goto Lb1
        L9f:
            r0 = r5
            com.mysql.jdbc.MysqlIO r0 = r0.getIO()
            r1 = 1
            boolean r0 = r0.isSetNeededForAutoCommitMode(r1)
            if (r0 == 0) goto Lb1
            r0 = r5
            r1 = 0
            r0.autoCommit = r1
            r0 = 1
            r6 = r0
        Lb1:
            r1 = r6
            if (r1 == 0) goto Ld1
            r1 = r5
            r2 = 1
            r1.setAutoCommit(r2)     // Catch: java.sql.SQLException -> Lbd
            goto Ld1
        Lbd:
            r7 = move-exception
            r0 = r7
            int r0 = r0.getErrorCode()
            r1 = 1820(0x71c, float:2.55E-42)
            if (r0 != r1) goto Lcf
            r0 = r5
            boolean r0 = r0.getDisconnectOnExpiredPasswords()
            if (r0 == 0) goto Ld1
        Lcf:
            r0 = r7
            throw r0
        Ld1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ConnectionImpl.handleAutoCommitDefaults():void");
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isClientTzUTC() {
        return this.isClientTzUTC;
    }

    @Override // java.sql.Connection
    public boolean isClosed() {
        return this.isClosed;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isCursorFetchEnabled() throws SQLException {
        return versionMeetsMinimum(5, 0, 2) && getUseCursorFetch();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isInGlobalTx() {
        return this.isInGlobalTx;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isMasterConnection() {
        return false;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isNoBackslashEscapesSet() {
        return this.noBackslashEscapes;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isReadInfoMsgEnabled() {
        return this.readInfoMsg;
    }

    @Override // com.mysql.jdbc.MySQLConnection, java.sql.Connection
    public boolean isReadOnly() throws SQLException {
        return isReadOnly(true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x00b7, code lost:
    
        throw r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00bb, code lost:
    
        if (r9 == null) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00be, code lost:
    
        r9.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x00cc, code lost:
    
        if (r8 == null) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x00cf, code lost:
    
        r8.close();
     */
    @Override // com.mysql.jdbc.MySQLConnection
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isReadOnly(boolean r7) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 227
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ConnectionImpl.isReadOnly(boolean):boolean");
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isRunningOnJDK13() {
        return this.isRunningOnJDK13;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isSameResource(Connection otherConnection) {
        synchronized (getConnectionMutex()) {
            if (otherConnection == null) {
                return false;
            }
            boolean directCompare = true;
            String otherHost = ((ConnectionImpl) otherConnection).origHostToConnectTo;
            String otherOrigDatabase = ((ConnectionImpl) otherConnection).origDatabaseToConnectTo;
            String otherCurrentCatalog = ((ConnectionImpl) otherConnection).database;
            if (!nullSafeCompare(otherHost, this.origHostToConnectTo)) {
                directCompare = false;
            } else if (otherHost != null && otherHost.indexOf(44) == -1 && otherHost.indexOf(58) == -1) {
                directCompare = ((ConnectionImpl) otherConnection).origPortToConnectTo == this.origPortToConnectTo;
            }
            if (directCompare && (!nullSafeCompare(otherOrigDatabase, this.origDatabaseToConnectTo) || !nullSafeCompare(otherCurrentCatalog, this.database))) {
                directCompare = false;
            }
            if (directCompare) {
                return true;
            }
            String otherResourceId = ((ConnectionImpl) otherConnection).getResourceId();
            String myResourceId = getResourceId();
            if ((otherResourceId != null || myResourceId != null) && nullSafeCompare(otherResourceId, myResourceId)) {
                return true;
            }
            return false;
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isServerTzUTC() {
        return this.isServerTzUTC;
    }

    private void createConfigCacheIfNeeded() throws SQLException {
        synchronized (getConnectionMutex()) {
            if (this.serverConfigCache != null) {
                return;
            }
            try {
                try {
                    try {
                        Class<?> factoryClass = Class.forName(getServerConfigCacheFactory());
                        CacheAdapterFactory<String, Map<String, String>> cacheFactory = (CacheAdapterFactory) factoryClass.newInstance();
                        this.serverConfigCache = cacheFactory.getInstance(this, this.myURL, Integer.MAX_VALUE, Integer.MAX_VALUE, this.props);
                        ExceptionInterceptor evictOnCommsError = new ExceptionInterceptor() { // from class: com.mysql.jdbc.ConnectionImpl.4
                            @Override // com.mysql.jdbc.Extension
                            public void init(Connection conn, Properties config) throws SQLException {
                            }

                            @Override // com.mysql.jdbc.Extension
                            public void destroy() {
                            }

                            @Override // com.mysql.jdbc.ExceptionInterceptor
                            public SQLException interceptException(SQLException sqlEx, Connection conn) {
                                if (sqlEx.getSQLState() != null && sqlEx.getSQLState().startsWith("08")) {
                                    ConnectionImpl.this.serverConfigCache.invalidate(ConnectionImpl.this.getURL());
                                    return null;
                                }
                                return null;
                            }
                        };
                        if (this.exceptionInterceptor == null) {
                            this.exceptionInterceptor = evictOnCommsError;
                        } else {
                            ((ExceptionInterceptorChain) this.exceptionInterceptor).addRingZero(evictOnCommsError);
                        }
                    } catch (InstantiationException e) {
                        SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[]{getParseInfoCacheFactory(), "parseInfoCacheFactory"}), getExceptionInterceptor());
                        sqlEx.initCause(e);
                        throw sqlEx;
                    }
                } catch (ClassNotFoundException e2) {
                    SQLException sqlEx2 = SQLError.createSQLException(Messages.getString("Connection.CantFindCacheFactory", new Object[]{getParseInfoCacheFactory(), "parseInfoCacheFactory"}), getExceptionInterceptor());
                    sqlEx2.initCause(e2);
                    throw sqlEx2;
                }
            } catch (IllegalAccessException e3) {
                SQLException sqlEx3 = SQLError.createSQLException(Messages.getString("Connection.CantLoadCacheFactory", new Object[]{getParseInfoCacheFactory(), "parseInfoCacheFactory"}), getExceptionInterceptor());
                sqlEx3.initCause(e3);
                throw sqlEx3;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:108:0x0351, code lost:
    
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x035d, code lost:
    
        if (0 == 0) goto L102;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0360, code lost:
    
        r6.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x034a, code lost:
    
        throw r16;
     */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0360 A[Catch: SQLException -> 0x0369, TRY_ENTER, TRY_LEAVE, TryCatch #5 {SQLException -> 0x0369, blocks: (B:100:0x0360, B:111:0x0360), top: B:113:0x0060 }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0351 A[Catch: SQLException -> 0x035a, TRY_ENTER, TRY_LEAVE, TryCatch #4 {SQLException -> 0x035a, blocks: (B:96:0x0351, B:108:0x0351, B:16:0x0060, B:18:0x0073, B:20:0x007d, B:21:0x0090, B:23:0x0099, B:25:0x00a8, B:27:0x00bc, B:26:0x00b4, B:28:0x00c2, B:29:0x00c8, B:34:0x00d8, B:35:0x00f2, B:36:0x010a, B:38:0x0114, B:40:0x0180, B:41:0x0189, B:43:0x01c0, B:44:0x01c9, B:46:0x01d4, B:47:0x01e6, B:49:0x020c, B:51:0x0218, B:54:0x022f, B:55:0x0238, B:57:0x0256, B:58:0x0261, B:60:0x026d, B:66:0x02d1, B:81:0x0304, B:82:0x030e, B:84:0x0315, B:53:0x0223, B:62:0x0291, B:63:0x02ad, B:65:0x02b6, B:69:0x02e1, B:71:0x02ec, B:104:0x0304, B:74:0x02f5, B:105:0x0304, B:80:0x0303, B:89:0x0342), top: B:117:0x0060, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void loadServerVariables() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 878
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ConnectionImpl.loadServerVariables():void");
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public int getAutoIncrementIncrement() {
        return this.autoIncrementIncrement;
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public boolean lowerCaseTableNames() {
        return this.lowerCaseTableNames;
    }

    @Override // java.sql.Connection
    public String nativeSQL(String sql) throws SQLException {
        if (sql == null) {
            return null;
        }
        Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, serverSupportsConvertFn(), getMultiHostSafeProxy());
        if (escapedSqlResult instanceof String) {
            return (String) escapedSqlResult;
        }
        return ((EscapeProcessorResult) escapedSqlResult).escapedSql;
    }

    private CallableStatement parseCallableStatement(String sql) throws SQLException {
        String parsedSql;
        boolean isFunctionCall;
        Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, serverSupportsConvertFn(), getMultiHostSafeProxy());
        if (escapedSqlResult instanceof EscapeProcessorResult) {
            parsedSql = ((EscapeProcessorResult) escapedSqlResult).escapedSql;
            isFunctionCall = ((EscapeProcessorResult) escapedSqlResult).callingStoredFunction;
        } else {
            parsedSql = (String) escapedSqlResult;
            isFunctionCall = false;
        }
        return CallableStatement.getInstance(getMultiHostSafeProxy(), parsedSql, this.database, isFunctionCall);
    }

    @Override // com.mysql.jdbc.Connection
    public boolean parserKnowsUnicode() {
        return this.parserKnowsUnicode;
    }

    @Override // com.mysql.jdbc.Connection
    public void ping() throws SQLException {
        pingInternal(true, 0);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void pingInternal(boolean checkForClosedConnection, int timeoutMillis) throws SQLException {
        if (checkForClosedConnection) {
            checkClosed();
        }
        long pingMillisLifetime = getSelfDestructOnPingSecondsLifetime();
        int pingMaxOperations = getSelfDestructOnPingMaxOperations();
        if ((pingMillisLifetime > 0 && System.currentTimeMillis() - this.connectionCreationTimeMillis > pingMillisLifetime) || (pingMaxOperations > 0 && pingMaxOperations <= this.f0io.getCommandCount())) {
            close();
            throw SQLError.createSQLException(Messages.getString("Connection.exceededConnectionLifetime"), SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE, getExceptionInterceptor());
        }
        this.f0io.sendCommand(14, null, null, false, null, timeoutMillis);
    }

    @Override // java.sql.Connection
    public java.sql.CallableStatement prepareCall(String sql) throws SQLException {
        return prepareCall(sql, 1003, 1007);
    }

    @Override // java.sql.Connection
    public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        CallableStatement cStmt;
        CallableStatement.CallableStatementParamInfo cachedParamInfo;
        if (versionMeetsMinimum(5, 0, 0)) {
            if (!getCacheCallableStatements()) {
                cStmt = parseCallableStatement(sql);
            } else {
                synchronized (this.parsedCallableStatementCache) {
                    CompoundCacheKey key = new CompoundCacheKey(getCatalog(), sql);
                    CallableStatement.CallableStatementParamInfo cachedParamInfo2 = this.parsedCallableStatementCache.get(key);
                    if (cachedParamInfo2 != null) {
                        cStmt = CallableStatement.getInstance(getMultiHostSafeProxy(), cachedParamInfo2);
                    } else {
                        cStmt = parseCallableStatement(sql);
                        synchronized (cStmt) {
                            cachedParamInfo = cStmt.paramInfo;
                        }
                        this.parsedCallableStatementCache.put(key, cachedParamInfo);
                    }
                }
            }
            cStmt.setResultSetType(resultSetType);
            cStmt.setResultSetConcurrency(resultSetConcurrency);
            return cStmt;
        }
        throw SQLError.createSQLException("Callable statements not supported.", SQLError.SQL_STATE_DRIVER_NOT_CAPABLE, getExceptionInterceptor());
    }

    @Override // java.sql.Connection
    public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (getPedantic() && resultSetHoldability != 1) {
            throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
        CallableStatement cStmt = (CallableStatement) prepareCall(sql, resultSetType, resultSetConcurrency);
        return cStmt;
    }

    @Override // java.sql.Connection
    public java.sql.PreparedStatement prepareStatement(String sql) throws SQLException {
        return prepareStatement(sql, 1003, 1007);
    }

    @Override // java.sql.Connection
    public java.sql.PreparedStatement prepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        java.sql.PreparedStatement pStmt = prepareStatement(sql);
        ((PreparedStatement) pStmt).setRetrieveGeneratedKeys(autoGenKeyIndex == 1);
        return pStmt;
    }

    @Override // java.sql.Connection
    public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        PreparedStatement pStmt;
        PreparedStatement preparedStatement;
        synchronized (getConnectionMutex()) {
            checkClosed();
            boolean canServerPrepare = true;
            String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
            if (this.useServerPreparedStmts && getEmulateUnsupportedPstmts()) {
                canServerPrepare = canHandleAsServerPreparedStatement(nativeSql);
            }
            if (this.useServerPreparedStmts && canServerPrepare) {
                if (getCachePreparedStatements()) {
                    synchronized (this.serverSideStatementCache) {
                        pStmt = (PreparedStatement) this.serverSideStatementCache.remove(new CompoundCacheKey(this.database, sql));
                        if (pStmt != null) {
                            ((ServerPreparedStatement) pStmt).setClosed(false);
                            pStmt.clearParameters();
                        }
                        if (pStmt == null) {
                            try {
                                pStmt = ServerPreparedStatement.getInstance(getMultiHostSafeProxy(), nativeSql, this.database, resultSetType, resultSetConcurrency);
                                if (sql.length() < getPreparedStatementCacheSqlLimit()) {
                                    ((ServerPreparedStatement) pStmt).isCached = true;
                                }
                                pStmt.setResultSetType(resultSetType);
                                pStmt.setResultSetConcurrency(resultSetConcurrency);
                            } catch (SQLException sqlEx) {
                                if (getEmulateUnsupportedPstmts()) {
                                    pStmt = (PreparedStatement) clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
                                    if (sql.length() < getPreparedStatementCacheSqlLimit()) {
                                        this.serverSideStatementCheckCache.put(sql, Boolean.FALSE);
                                    }
                                } else {
                                    throw sqlEx;
                                }
                            }
                        }
                    }
                } else {
                    try {
                        pStmt = ServerPreparedStatement.getInstance(getMultiHostSafeProxy(), nativeSql, this.database, resultSetType, resultSetConcurrency);
                        pStmt.setResultSetType(resultSetType);
                        pStmt.setResultSetConcurrency(resultSetConcurrency);
                    } catch (SQLException sqlEx2) {
                        if (getEmulateUnsupportedPstmts()) {
                            pStmt = (PreparedStatement) clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
                        } else {
                            throw sqlEx2;
                        }
                    }
                }
            } else {
                pStmt = (PreparedStatement) clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
            }
            preparedStatement = pStmt;
        }
        return preparedStatement;
    }

    @Override // java.sql.Connection
    public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (getPedantic() && resultSetHoldability != 1) {
            throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
        return prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override // java.sql.Connection
    public java.sql.PreparedStatement prepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        java.sql.PreparedStatement pStmt = prepareStatement(sql);
        ((PreparedStatement) pStmt).setRetrieveGeneratedKeys(autoGenKeyIndexes != null && autoGenKeyIndexes.length > 0);
        return pStmt;
    }

    @Override // java.sql.Connection
    public java.sql.PreparedStatement prepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        java.sql.PreparedStatement pStmt = prepareStatement(sql);
        ((PreparedStatement) pStmt).setRetrieveGeneratedKeys(autoGenKeyColNames != null && autoGenKeyColNames.length > 0);
        return pStmt;
    }

    /*  JADX ERROR: Types fix failed
        java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "changeArg" is null
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:439)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:232)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:212)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:183)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:112)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:83)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:56)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:183)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:242)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:221)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
        */
    /* JADX WARN: Not initialized variable reg: 19, insn: 0x0150: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r19 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:62:0x0150 */
    @Override // com.mysql.jdbc.MySQLConnection
    public void realClose(boolean r11, boolean r12, boolean r13, java.lang.Throwable r14) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 358
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ConnectionImpl.realClose(boolean, boolean, boolean, java.lang.Throwable):void");
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void recachePreparedStatement(ServerPreparedStatement pstmt) throws SQLException {
        synchronized (getConnectionMutex()) {
            if (getCachePreparedStatements() && pstmt.isPoolable()) {
                synchronized (this.serverSideStatementCache) {
                    Object oldServerPrepStmt = this.serverSideStatementCache.put(new CompoundCacheKey(pstmt.currentCatalog, pstmt.originalSql), pstmt);
                    if (oldServerPrepStmt != null && oldServerPrepStmt != pstmt) {
                        ((ServerPreparedStatement) oldServerPrepStmt).isCached = false;
                        ((ServerPreparedStatement) oldServerPrepStmt).setClosed(false);
                        ((ServerPreparedStatement) oldServerPrepStmt).realClose(true, true);
                    }
                }
            }
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void decachePreparedStatement(ServerPreparedStatement pstmt) throws SQLException {
        synchronized (getConnectionMutex()) {
            if (getCachePreparedStatements() && pstmt.isPoolable()) {
                synchronized (this.serverSideStatementCache) {
                    this.serverSideStatementCache.remove(new CompoundCacheKey(pstmt.currentCatalog, pstmt.originalSql));
                }
            }
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void registerQueryExecutionTime(long queryTimeMs) {
        if (queryTimeMs > this.longestQueryTimeMs) {
            this.longestQueryTimeMs = queryTimeMs;
            repartitionPerformanceHistogram();
        }
        addToPerformanceHistogram(queryTimeMs, 1);
        if (queryTimeMs < this.shortestQueryTimeMs) {
            this.shortestQueryTimeMs = queryTimeMs == 0 ? 1L : queryTimeMs;
        }
        this.numberOfQueriesIssued++;
        this.totalQueryTimeMs += queryTimeMs;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void registerStatement(Statement stmt) {
        this.openStatements.addIfAbsent(stmt);
    }

    @Override // java.sql.Connection
    public void releaseSavepoint(Savepoint arg0) throws SQLException {
    }

    private void repartitionHistogram(int[] histCounts, long[] histBreakpoints, long currentLowerBound, long currentUpperBound) {
        if (this.oldHistCounts == null) {
            this.oldHistCounts = new int[histCounts.length];
            this.oldHistBreakpoints = new long[histBreakpoints.length];
        }
        System.arraycopy(histCounts, 0, this.oldHistCounts, 0, histCounts.length);
        System.arraycopy(histBreakpoints, 0, this.oldHistBreakpoints, 0, histBreakpoints.length);
        createInitialHistogram(histBreakpoints, currentLowerBound, currentUpperBound);
        for (int i = 0; i < 20; i++) {
            addToHistogram(histCounts, histBreakpoints, this.oldHistBreakpoints[i], this.oldHistCounts[i], currentLowerBound, currentUpperBound);
        }
    }

    private void repartitionPerformanceHistogram() {
        checkAndCreatePerformanceHistogram();
        repartitionHistogram(this.perfMetricsHistCounts, this.perfMetricsHistBreakpoints, this.shortestQueryTimeMs == Long.MAX_VALUE ? 0L : this.shortestQueryTimeMs, this.longestQueryTimeMs);
    }

    private void repartitionTablesAccessedHistogram() {
        checkAndCreateTablesAccessedHistogram();
        repartitionHistogram(this.numTablesMetricsHistCounts, this.numTablesMetricsHistBreakpoints, this.minimumNumberTablesAccessed == Long.MAX_VALUE ? 0L : this.minimumNumberTablesAccessed, this.maximumNumberTablesAccessed);
    }

    private void reportMetrics() {
        if (getGatherPerformanceMetrics()) {
            StringBuilder logMessage = new StringBuilder(256);
            logMessage.append("** Performance Metrics Report **\n");
            logMessage.append("\nLongest reported query: " + this.longestQueryTimeMs + " ms");
            logMessage.append("\nShortest reported query: " + this.shortestQueryTimeMs + " ms");
            logMessage.append("\nAverage query execution time: " + (this.totalQueryTimeMs / this.numberOfQueriesIssued) + " ms");
            logMessage.append("\nNumber of statements executed: " + this.numberOfQueriesIssued);
            logMessage.append("\nNumber of result sets created: " + this.numberOfResultSetsCreated);
            logMessage.append("\nNumber of statements prepared: " + this.numberOfPrepares);
            logMessage.append("\nNumber of prepared statement executions: " + this.numberOfPreparedExecutes);
            if (this.perfMetricsHistBreakpoints != null) {
                logMessage.append("\n\n\tTiming Histogram:\n");
                int highestCount = Integer.MIN_VALUE;
                for (int i = 0; i < 20; i++) {
                    if (this.perfMetricsHistCounts[i] > highestCount) {
                        highestCount = this.perfMetricsHistCounts[i];
                    }
                }
                if (highestCount == 0) {
                    highestCount = 1;
                }
                for (int i2 = 0; i2 < 19; i2++) {
                    if (i2 == 0) {
                        logMessage.append("\n\tless than " + this.perfMetricsHistBreakpoints[i2 + 1] + " ms: \t" + this.perfMetricsHistCounts[i2]);
                    } else {
                        logMessage.append("\n\tbetween " + this.perfMetricsHistBreakpoints[i2] + " and " + this.perfMetricsHistBreakpoints[i2 + 1] + " ms: \t" + this.perfMetricsHistCounts[i2]);
                    }
                    logMessage.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN);
                    int numPointsToGraph = (int) (20 * (this.perfMetricsHistCounts[i2] / highestCount));
                    for (int j = 0; j < numPointsToGraph; j++) {
                        logMessage.append("*");
                    }
                    if (this.longestQueryTimeMs < this.perfMetricsHistCounts[i2 + 1]) {
                        break;
                    }
                }
                if (this.perfMetricsHistBreakpoints[18] < this.longestQueryTimeMs) {
                    logMessage.append("\n\tbetween ");
                    logMessage.append(this.perfMetricsHistBreakpoints[18]);
                    logMessage.append(" and ");
                    logMessage.append(this.perfMetricsHistBreakpoints[19]);
                    logMessage.append(" ms: \t");
                    logMessage.append(this.perfMetricsHistCounts[19]);
                }
            }
            if (this.numTablesMetricsHistBreakpoints != null) {
                logMessage.append("\n\n\tTable Join Histogram:\n");
                int highestCount2 = Integer.MIN_VALUE;
                for (int i3 = 0; i3 < 20; i3++) {
                    if (this.numTablesMetricsHistCounts[i3] > highestCount2) {
                        highestCount2 = this.numTablesMetricsHistCounts[i3];
                    }
                }
                if (highestCount2 == 0) {
                    highestCount2 = 1;
                }
                for (int i4 = 0; i4 < 19; i4++) {
                    if (i4 == 0) {
                        logMessage.append("\n\t" + this.numTablesMetricsHistBreakpoints[i4 + 1] + " tables or less: \t\t" + this.numTablesMetricsHistCounts[i4]);
                    } else {
                        logMessage.append("\n\tbetween " + this.numTablesMetricsHistBreakpoints[i4] + " and " + this.numTablesMetricsHistBreakpoints[i4 + 1] + " tables: \t" + this.numTablesMetricsHistCounts[i4]);
                    }
                    logMessage.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN);
                    int numPointsToGraph2 = (int) (20 * (this.numTablesMetricsHistCounts[i4] / highestCount2));
                    for (int j2 = 0; j2 < numPointsToGraph2; j2++) {
                        logMessage.append("*");
                    }
                    if (this.maximumNumberTablesAccessed < this.numTablesMetricsHistBreakpoints[i4 + 1]) {
                        break;
                    }
                }
                if (this.numTablesMetricsHistBreakpoints[18] < this.maximumNumberTablesAccessed) {
                    logMessage.append("\n\tbetween ");
                    logMessage.append(this.numTablesMetricsHistBreakpoints[18]);
                    logMessage.append(" and ");
                    logMessage.append(this.numTablesMetricsHistBreakpoints[19]);
                    logMessage.append(" tables: ");
                    logMessage.append(this.numTablesMetricsHistCounts[19]);
                }
            }
            this.log.logInfo(logMessage);
            this.metricsLastReportedMs = System.currentTimeMillis();
        }
    }

    protected void reportMetricsIfNeeded() {
        if (getGatherPerformanceMetrics() && System.currentTimeMillis() - this.metricsLastReportedMs > getReportMetricsIntervalMillis()) {
            reportMetrics();
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void reportNumberOfTablesAccessed(int numTablesAccessed) {
        if (numTablesAccessed < this.minimumNumberTablesAccessed) {
            this.minimumNumberTablesAccessed = numTablesAccessed;
        }
        if (numTablesAccessed > this.maximumNumberTablesAccessed) {
            this.maximumNumberTablesAccessed = numTablesAccessed;
            repartitionTablesAccessedHistogram();
        }
        addToTablesAccessedHistogram(numTablesAccessed, 1);
    }

    @Override // com.mysql.jdbc.Connection
    public void resetServerState() throws SQLException {
        if (!getParanoid() && this.f0io != null && versionMeetsMinimum(4, 0, 6)) {
            changeUser(this.user, this.password);
        }
    }

    @Override // java.sql.Connection
    public void rollback() throws SQLException {
        synchronized (getConnectionMutex()) {
            try {
                checkClosed();
                try {
                    if (this.connectionLifecycleInterceptors != null) {
                        IterateBlock<Extension> iter = new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) { // from class: com.mysql.jdbc.ConnectionImpl.5
                            /* JADX INFO: Access modifiers changed from: package-private */
                            @Override // com.mysql.jdbc.IterateBlock
                            public void forEach(Extension each) throws SQLException {
                                if (!((ConnectionLifecycleInterceptor) each).rollback()) {
                                    this.stopIterating = true;
                                }
                            }
                        };
                        iter.doForAll();
                        if (!iter.fullIteration()) {
                            return;
                        }
                    }
                    if (this.autoCommit && !getRelaxAutoCommit()) {
                        throw SQLError.createSQLException("Can't call rollback when autocommit=true", SQLError.SQL_STATE_CONNECTION_NOT_OPEN, getExceptionInterceptor());
                    }
                    if (this.transactionsSupported) {
                        try {
                            rollbackNoChecks();
                        } catch (SQLException sqlEx) {
                            if (!getIgnoreNonTxTables() || sqlEx.getErrorCode() != 1196) {
                                throw sqlEx;
                            }
                        }
                    }
                } catch (SQLException sqlException) {
                    if (SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE.equals(sqlException.getSQLState())) {
                        throw SQLError.createSQLException("Communications link failure during rollback(). Transaction resolution unknown.", SQLError.SQL_STATE_TRANSACTION_RESOLUTION_UNKNOWN, getExceptionInterceptor());
                    }
                    throw sqlException;
                }
            } finally {
                this.needsPing = getReconnectAtTxEnd();
            }
        }
    }

    @Override // java.sql.Connection
    public void rollback(final Savepoint savepoint) throws SQLException {
        String msg;
        synchronized (getConnectionMutex()) {
            if (versionMeetsMinimum(4, 0, 14) || versionMeetsMinimum(4, 1, 1)) {
                checkClosed();
                try {
                    if (this.connectionLifecycleInterceptors != null) {
                        IterateBlock<Extension> iter = new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) { // from class: com.mysql.jdbc.ConnectionImpl.6
                            /* JADX INFO: Access modifiers changed from: package-private */
                            @Override // com.mysql.jdbc.IterateBlock
                            public void forEach(Extension each) throws SQLException {
                                if (!((ConnectionLifecycleInterceptor) each).rollback(savepoint)) {
                                    this.stopIterating = true;
                                }
                            }
                        };
                        iter.doForAll();
                        if (!iter.fullIteration()) {
                            return;
                        }
                    }
                    java.sql.Statement stmt = null;
                    try {
                        try {
                            stmt = getMetadataSafeStatement();
                            stmt.executeUpdate("ROLLBACK TO SAVEPOINT `" + savepoint.getSavepointName() + '`');
                            return;
                        } catch (SQLException sqlEx) {
                            int errno = sqlEx.getErrorCode();
                            if (errno == 1181 && (msg = sqlEx.getMessage()) != null) {
                                int indexOfError153 = msg.indexOf("153");
                                if (indexOfError153 != -1) {
                                    throw SQLError.createSQLException("Savepoint '" + savepoint.getSavepointName() + "' does not exist", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, errno, getExceptionInterceptor());
                                }
                            }
                            if (getIgnoreNonTxTables() && sqlEx.getErrorCode() != 1196) {
                                throw sqlEx;
                            }
                            if (SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE.equals(sqlEx.getSQLState())) {
                                throw SQLError.createSQLException("Communications link failure during rollback(). Transaction resolution unknown.", SQLError.SQL_STATE_TRANSACTION_RESOLUTION_UNKNOWN, getExceptionInterceptor());
                            }
                            throw sqlEx;
                        }
                    } finally {
                        closeStatement(stmt);
                    }
                } finally {
                    this.needsPing = getReconnectAtTxEnd();
                }
            }
            throw SQLError.createSQLFeatureNotSupportedException();
        }
    }

    private void rollbackNoChecks() throws SQLException {
        if (getUseLocalTransactionState() && versionMeetsMinimum(5, 0, 0) && !this.f0io.inTransactionOnServer()) {
            return;
        }
        execSQL(null, "rollback", -1, null, 1003, 1007, false, this.database, null, false);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement serverPrepareStatement(String sql) throws SQLException {
        String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
        return ServerPreparedStatement.getInstance(getMultiHostSafeProxy(), nativeSql, getCatalog(), 1003, 1007);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement serverPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
        PreparedStatement pStmt = ServerPreparedStatement.getInstance(getMultiHostSafeProxy(), nativeSql, getCatalog(), 1003, 1007);
        pStmt.setRetrieveGeneratedKeys(autoGenKeyIndex == 1);
        return pStmt;
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
        return ServerPreparedStatement.getInstance(getMultiHostSafeProxy(), nativeSql, getCatalog(), resultSetType, resultSetConcurrency);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if (getPedantic() && resultSetHoldability != 1) {
            throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
        return serverPrepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement serverPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        PreparedStatement pStmt = (PreparedStatement) serverPrepareStatement(sql);
        pStmt.setRetrieveGeneratedKeys(autoGenKeyIndexes != null && autoGenKeyIndexes.length > 0);
        return pStmt;
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement serverPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        PreparedStatement pStmt = (PreparedStatement) serverPrepareStatement(sql);
        pStmt.setRetrieveGeneratedKeys(autoGenKeyColNames != null && autoGenKeyColNames.length > 0);
        return pStmt;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean serverSupportsConvertFn() throws SQLException {
        return versionMeetsMinimum(4, 0, 2);
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // java.sql.Connection
    public void setAutoCommit(final boolean r13) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 224
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ConnectionImpl.setAutoCommit(boolean):void");
    }

    @Override // java.sql.Connection
    public void setCatalog(final String catalog) throws SQLException {
        synchronized (getConnectionMutex()) {
            checkClosed();
            if (catalog == null) {
                throw SQLError.createSQLException("Catalog can not be null", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (this.connectionLifecycleInterceptors != null) {
                IterateBlock<Extension> iter = new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) { // from class: com.mysql.jdbc.ConnectionImpl.8
                    /* JADX INFO: Access modifiers changed from: package-private */
                    @Override // com.mysql.jdbc.IterateBlock
                    public void forEach(Extension each) throws SQLException {
                        if (!((ConnectionLifecycleInterceptor) each).setCatalog(catalog)) {
                            this.stopIterating = true;
                        }
                    }
                };
                iter.doForAll();
                if (!iter.fullIteration()) {
                    return;
                }
            }
            if (getUseLocalSessionState()) {
                if (this.lowerCaseTableNames) {
                    if (this.database.equalsIgnoreCase(catalog)) {
                        return;
                    }
                } else if (this.database.equals(catalog)) {
                    return;
                }
            }
            String quotedId = this.dbmd.getIdentifierQuoteString();
            if (quotedId == null || quotedId.equals(SymbolConstants.SPACE_SYMBOL)) {
                quotedId = "";
            }
            execSQL(null, "USE " + StringUtils.quoteIdentifier(catalog, quotedId, getPedantic()), -1, null, 1003, 1007, false, this.database, null, false);
            this.database = catalog;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public void setFailedOver(boolean flag) {
    }

    @Override // java.sql.Connection
    public void setHoldability(int arg0) throws SQLException {
    }

    @Override // com.mysql.jdbc.Connection
    public void setInGlobalTx(boolean flag) {
        this.isInGlobalTx = flag;
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public void setPreferSlaveDuringFailover(boolean flag) {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void setReadInfoMsgEnabled(boolean flag) {
        this.readInfoMsg = flag;
    }

    @Override // java.sql.Connection
    public void setReadOnly(boolean readOnlyFlag) throws SQLException {
        checkClosed();
        setReadOnlyInternal(readOnlyFlag);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void setReadOnlyInternal(boolean readOnlyFlag) throws SQLException {
        if (getReadOnlyPropagatesToServer() && versionMeetsMinimum(5, 6, 5) && (!getUseLocalSessionState() || readOnlyFlag != this.readOnly)) {
            execSQL(null, "set session transaction " + (readOnlyFlag ? "read only" : "read write"), -1, null, 1003, 1007, false, this.database, null, false);
        }
        this.readOnly = readOnlyFlag;
    }

    @Override // java.sql.Connection
    public Savepoint setSavepoint() throws SQLException {
        MysqlSavepoint savepoint = new MysqlSavepoint(getExceptionInterceptor());
        setSavepoint(savepoint);
        return savepoint;
    }

    private void setSavepoint(MysqlSavepoint savepoint) throws SQLException {
        synchronized (getConnectionMutex()) {
            if (versionMeetsMinimum(4, 0, 14) || versionMeetsMinimum(4, 1, 1)) {
                checkClosed();
                java.sql.Statement stmt = null;
                try {
                    stmt = getMetadataSafeStatement();
                    stmt.executeUpdate("SAVEPOINT `" + savepoint.getSavepointName() + '`');
                    closeStatement(stmt);
                } catch (Throwable th) {
                    closeStatement(stmt);
                    throw th;
                }
            } else {
                throw SQLError.createSQLFeatureNotSupportedException();
            }
        }
    }

    @Override // java.sql.Connection
    public Savepoint setSavepoint(String name) throws SQLException {
        MysqlSavepoint savepoint;
        synchronized (getConnectionMutex()) {
            savepoint = new MysqlSavepoint(name, getExceptionInterceptor());
            setSavepoint(savepoint);
        }
        return savepoint;
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    private void setSessionVariables() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 246
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ConnectionImpl.setSessionVariables():void");
    }

    @Override // java.sql.Connection
    public void setTransactionIsolation(int level) throws SQLException {
        String sql;
        synchronized (getConnectionMutex()) {
            checkClosed();
            if (this.hasIsolationLevels) {
                boolean shouldSendSet = false;
                if (getAlwaysSendSetIsolation() || level != this.isolationLevel) {
                    shouldSendSet = true;
                }
                if (getUseLocalSessionState()) {
                    shouldSendSet = this.isolationLevel != level;
                }
                if (shouldSendSet) {
                    switch (level) {
                        case 0:
                            throw SQLError.createSQLException("Transaction isolation level NONE not supported by MySQL", getExceptionInterceptor());
                        case 1:
                            sql = "SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED";
                            break;
                        case 2:
                            sql = "SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED";
                            break;
                        case 3:
                        case 5:
                        case 6:
                        case 7:
                        default:
                            throw SQLError.createSQLException("Unsupported transaction isolation level '" + level + "'", SQLError.SQL_STATE_DRIVER_NOT_CAPABLE, getExceptionInterceptor());
                        case 4:
                            sql = "SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ";
                            break;
                        case 8:
                            sql = "SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE";
                            break;
                    }
                    execSQL(null, sql, -1, null, 1003, 1007, false, this.database, null, false);
                    this.isolationLevel = level;
                }
            } else {
                throw SQLError.createSQLException("Transaction Isolation Levels are not supported on MySQL versions older than 3.23.36.", SQLError.SQL_STATE_DRIVER_NOT_CAPABLE, getExceptionInterceptor());
            }
        }
    }

    @Override // java.sql.Connection
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        synchronized (getConnectionMutex()) {
            this.typeMap = map;
        }
    }

    private void setupServerForTruncationChecks() throws SQLException {
        if (getJdbcCompliantTruncation() && versionMeetsMinimum(5, 0, 2)) {
            String currentSqlMode = this.serverVariables.get("sql_mode");
            boolean strictTransTablesIsSet = StringUtils.indexOfIgnoreCase(currentSqlMode, "STRICT_TRANS_TABLES") != -1;
            if (currentSqlMode != null && currentSqlMode.length() != 0 && strictTransTablesIsSet) {
                if (strictTransTablesIsSet) {
                    setJdbcCompliantTruncation(false);
                    return;
                }
                return;
            }
            StringBuilder commandBuf = new StringBuilder("SET sql_mode='");
            if (currentSqlMode != null && currentSqlMode.length() > 0) {
                commandBuf.append(currentSqlMode);
                commandBuf.append(",");
            }
            commandBuf.append("STRICT_TRANS_TABLES'");
            execSQL(null, commandBuf.toString(), -1, null, 1003, 1007, false, this.database, null, false);
            setJdbcCompliantTruncation(false);
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public void shutdownServer() throws SQLException {
        try {
            if (versionMeetsMinimum(5, 7, 9)) {
                execSQL(null, "SHUTDOWN", -1, null, 1003, 1007, false, this.database, null, false);
            } else {
                this.f0io.sendCommand(8, null, null, false, null, 0);
            }
        } catch (Exception ex) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.UnhandledExceptionDuringShutdown"), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            sqlEx.initCause(ex);
            throw sqlEx;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public boolean supportsIsolationLevel() {
        return this.hasIsolationLevels;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean supportsQuotedIdentifiers() {
        return this.hasQuotedIdentifiers;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean supportsTransactions() {
        return this.transactionsSupported;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void unregisterStatement(Statement stmt) {
        this.openStatements.remove(stmt);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean useAnsiQuotedIdentifiers() {
        boolean z;
        synchronized (getConnectionMutex()) {
            z = this.useAnsiQuotes;
        }
        return z;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean versionMeetsMinimum(int major, int minor, int subminor) throws SQLException {
        checkClosed();
        return this.f0io.versionMeetsMinimum(major, minor, subminor);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public CachedResultSetMetaData getCachedMetaData(String sql) {
        CachedResultSetMetaData cachedResultSetMetaData;
        if (this.resultSetMetadataCache != null) {
            synchronized (this.resultSetMetadataCache) {
                cachedResultSetMetaData = this.resultSetMetadataCache.get(sql);
            }
            return cachedResultSetMetaData;
        }
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void initializeResultsMetadataFromCache(String sql, CachedResultSetMetaData cachedMetaData, ResultSetInternalMethods resultSet) throws SQLException {
        if (cachedMetaData == null) {
            CachedResultSetMetaData cachedMetaData2 = new CachedResultSetMetaData();
            resultSet.buildIndexMapping();
            resultSet.initializeWithMetadata();
            if (resultSet instanceof UpdatableResultSet) {
                ((UpdatableResultSet) resultSet).checkUpdatability();
            }
            resultSet.populateCachedMetaData(cachedMetaData2);
            this.resultSetMetadataCache.put(sql, cachedMetaData2);
            return;
        }
        resultSet.initializeFromCachedMetaData(cachedMetaData);
        resultSet.initializeWithMetadata();
        if (resultSet instanceof UpdatableResultSet) {
            ((UpdatableResultSet) resultSet).checkUpdatability();
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public String getStatementComment() {
        return this.statementComment;
    }

    @Override // com.mysql.jdbc.Connection
    public void setStatementComment(String comment) {
        this.statementComment = comment;
    }

    @Override // com.mysql.jdbc.Connection
    public void reportQueryTime(long millisOrNanos) {
        synchronized (getConnectionMutex()) {
            this.queryTimeCount++;
            this.queryTimeSum += millisOrNanos;
            this.queryTimeSumSquares += millisOrNanos * millisOrNanos;
            this.queryTimeMean = ((this.queryTimeMean * (this.queryTimeCount - 1)) + millisOrNanos) / this.queryTimeCount;
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public boolean isAbonormallyLongQuery(long millisOrNanos) {
        boolean z;
        synchronized (getConnectionMutex()) {
            boolean res = false;
            if (this.queryTimeCount > 14) {
                double stddev = Math.sqrt((this.queryTimeSumSquares - ((this.queryTimeSum * this.queryTimeSum) / this.queryTimeCount)) / (this.queryTimeCount - 1));
                res = ((double) millisOrNanos) > this.queryTimeMean + (5.0d * stddev);
            }
            reportQueryTime(millisOrNanos);
            z = res;
        }
        return z;
    }

    @Override // com.mysql.jdbc.Connection
    public void initializeExtension(Extension ex) throws SQLException {
        ex.init(this, this.props);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void transactionBegun() throws SQLException {
        synchronized (getConnectionMutex()) {
            if (this.connectionLifecycleInterceptors != null) {
                IterateBlock<Extension> iter = new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) { // from class: com.mysql.jdbc.ConnectionImpl.9
                    /* JADX INFO: Access modifiers changed from: package-private */
                    @Override // com.mysql.jdbc.IterateBlock
                    public void forEach(Extension each) throws SQLException {
                        ((ConnectionLifecycleInterceptor) each).transactionBegun();
                    }
                };
                iter.doForAll();
            }
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void transactionCompleted() throws SQLException {
        synchronized (getConnectionMutex()) {
            if (this.connectionLifecycleInterceptors != null) {
                IterateBlock<Extension> iter = new IterateBlock<Extension>(this.connectionLifecycleInterceptors.iterator()) { // from class: com.mysql.jdbc.ConnectionImpl.10
                    /* JADX INFO: Access modifiers changed from: package-private */
                    @Override // com.mysql.jdbc.IterateBlock
                    public void forEach(Extension each) throws SQLException {
                        ((ConnectionLifecycleInterceptor) each).transactionCompleted();
                    }
                };
                iter.doForAll();
            }
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean storesLowerCaseTableName() {
        return this.storesLowerCaseTableName;
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties, com.mysql.jdbc.MySQLConnection
    public ExceptionInterceptor getExceptionInterceptor() {
        return this.exceptionInterceptor;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean getRequiresEscapingEncoder() {
        return this.requiresEscapingEncoder;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isServerLocal() throws SQLException {
        synchronized (getConnectionMutex()) {
            SocketFactory factory = getIO().socketFactory;
            if (factory instanceof SocketMetadata) {
                return ((SocketMetadata) factory).isLocallyConnected(this);
            }
            getLog().logWarn(Messages.getString("Connection.NoMetadataOnSocketFactory"));
            return false;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public int getSessionMaxRows() {
        int i;
        synchronized (getConnectionMutex()) {
            i = this.sessionMaxRows;
        }
        return i;
    }

    @Override // com.mysql.jdbc.Connection
    public void setSessionMaxRows(int max) throws SQLException {
        synchronized (getConnectionMutex()) {
            if (this.sessionMaxRows != max) {
                this.sessionMaxRows = max;
                execSQL(null, "SET SQL_SELECT_LIMIT=" + (this.sessionMaxRows == -1 ? "DEFAULT" : Integer.valueOf(this.sessionMaxRows)), -1, null, 1003, 1007, false, this.database, null, false);
            }
        }
    }

    @Override // com.mysql.jdbc.Connection
    public void setSchema(String schema) throws SQLException {
        synchronized (getConnectionMutex()) {
            checkClosed();
        }
    }

    @Override // com.mysql.jdbc.Connection
    public String getSchema() throws SQLException {
        synchronized (getConnectionMutex()) {
            checkClosed();
        }
        return null;
    }

    @Override // com.mysql.jdbc.Connection
    public void abort(Executor executor) throws SQLException {
        SecurityManager sec = System.getSecurityManager();
        if (sec != null) {
            sec.checkPermission(ABORT_PERM);
        }
        if (executor == null) {
            throw SQLError.createSQLException("Executor can not be null", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
        executor.execute(new Runnable() { // from class: com.mysql.jdbc.ConnectionImpl.11
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ConnectionImpl.this.abortInternal();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override // com.mysql.jdbc.Connection
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        synchronized (getConnectionMutex()) {
            SecurityManager sec = System.getSecurityManager();
            if (sec != null) {
                sec.checkPermission(SET_NETWORK_TIMEOUT_PERM);
            }
            if (executor == null) {
                throw SQLError.createSQLException("Executor can not be null", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            checkClosed();
            executor.execute(new NetworkTimeoutSetter(this, this.f0io, milliseconds));
        }
    }

    @Override // com.mysql.jdbc.Connection
    public int getNetworkTimeout() throws SQLException {
        int socketTimeout;
        synchronized (getConnectionMutex()) {
            checkClosed();
            socketTimeout = getSocketTimeout();
        }
        return socketTimeout;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public ProfilerEventHandler getProfilerEventHandlerInstance() {
        return this.eventSink;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void setProfilerEventHandlerInstance(ProfilerEventHandler h) {
        this.eventSink = h;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isServerTruncatesFracSecs() {
        return this.serverTruncatesFracSecs;
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ConnectionImpl$NetworkTimeoutSetter.class */
    private static class NetworkTimeoutSetter implements Runnable {
        private final WeakReference<ConnectionImpl> connImplRef;
        private final WeakReference<MysqlIO> mysqlIoRef;
        private final int milliseconds;

        public NetworkTimeoutSetter(ConnectionImpl conn, MysqlIO io2, int milliseconds) {
            this.connImplRef = new WeakReference<>(conn);
            this.mysqlIoRef = new WeakReference<>(io2);
            this.milliseconds = milliseconds;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ConnectionImpl conn = this.connImplRef.get();
                if (conn != null) {
                    synchronized (conn.getConnectionMutex()) {
                        conn.setSocketTimeout(this.milliseconds);
                        MysqlIO io2 = this.mysqlIoRef.get();
                        if (io2 != null) {
                            io2.setSocketTimeout(this.milliseconds);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getQueryTimingUnits() {
        return this.f0io != null ? this.f0io.getQueryTimingUnits() : Constants.MILLIS_I18N;
    }
}
