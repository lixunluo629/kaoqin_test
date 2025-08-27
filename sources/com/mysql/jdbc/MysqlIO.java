package com.mysql.jdbc;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.mysql.jdbc.authentication.CachingSha2PasswordPlugin;
import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;
import com.mysql.jdbc.authentication.MysqlNativePasswordPlugin;
import com.mysql.jdbc.authentication.MysqlOldPasswordPlugin;
import com.mysql.jdbc.authentication.Sha256PasswordPlugin;
import com.mysql.jdbc.exceptions.MySQLStatementCancelledException;
import com.mysql.jdbc.exceptions.MySQLTimeoutException;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import com.mysql.jdbc.util.ReadAheadInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.Deflater;
import org.apache.commons.httpclient.auth.NTLM;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.tomcat.jni.Time;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/MysqlIO.class */
public class MysqlIO {
    private static final String CODE_PAGE_1252 = "Cp1252";
    protected static final int NULL_LENGTH = -1;
    protected static final int COMP_HEADER_LENGTH = 3;
    protected static final int MIN_COMPRESS_LEN = 50;
    protected static final int HEADER_LENGTH = 4;
    protected static final int AUTH_411_OVERHEAD = 33;
    public static final int SEED_LENGTH = 20;
    private static final String NONE = "none";
    private static final int CLIENT_LONG_PASSWORD = 1;
    private static final int CLIENT_FOUND_ROWS = 2;
    private static final int CLIENT_LONG_FLAG = 4;
    protected static final int CLIENT_CONNECT_WITH_DB = 8;
    private static final int CLIENT_COMPRESS = 32;
    private static final int CLIENT_LOCAL_FILES = 128;
    private static final int CLIENT_PROTOCOL_41 = 512;
    private static final int CLIENT_INTERACTIVE = 1024;
    protected static final int CLIENT_SSL = 2048;
    private static final int CLIENT_TRANSACTIONS = 8192;
    protected static final int CLIENT_RESERVED = 16384;
    protected static final int CLIENT_SECURE_CONNECTION = 32768;
    private static final int CLIENT_MULTI_STATEMENTS = 65536;
    private static final int CLIENT_MULTI_RESULTS = 131072;
    private static final int CLIENT_PLUGIN_AUTH = 524288;
    private static final int CLIENT_CONNECT_ATTRS = 1048576;
    private static final int CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA = 2097152;
    private static final int CLIENT_CAN_HANDLE_EXPIRED_PASSWORD = 4194304;
    private static final int CLIENT_SESSION_TRACK = 8388608;
    private static final int CLIENT_DEPRECATE_EOF = 16777216;
    private static final int SERVER_STATUS_IN_TRANS = 1;
    private static final int SERVER_STATUS_AUTOCOMMIT = 2;
    static final int SERVER_MORE_RESULTS_EXISTS = 8;
    private static final int SERVER_QUERY_NO_GOOD_INDEX_USED = 16;
    private static final int SERVER_QUERY_NO_INDEX_USED = 32;
    private static final int SERVER_QUERY_WAS_SLOW = 2048;
    private static final int SERVER_STATUS_CURSOR_EXISTS = 64;
    private static final String FALSE_SCRAMBLE = "xxxxxxxx";
    protected static final int MAX_QUERY_SIZE_TO_LOG = 1024;
    protected static final int MAX_QUERY_SIZE_TO_EXPLAIN = 1048576;
    protected static final int INITIAL_PACKET_SIZE = 1024;
    private static String jvmPlatformCharset;
    protected static final String ZERO_DATE_VALUE_MARKER = "0000-00-00";
    protected static final String ZERO_DATETIME_VALUE_MARKER = "0000-00-00 00:00:00";
    private static final String EXPLAINABLE_STATEMENT = "SELECT";
    private static final int MAX_PACKET_DUMP_LENGTH = 1024;
    protected int serverCharsetIndex;
    private Buffer reusablePacket;
    private Buffer sendPacket;
    protected BufferedOutputStream mysqlOutput;
    protected MySQLConnection connection;
    protected InputStream mysqlInput;
    private LinkedList<StringBuilder> packetDebugRingBuffer;
    public Socket mysqlConnection;
    protected SocketFactory socketFactory;
    private SoftReference<Buffer> loadFileBufRef;
    private SoftReference<Buffer> splitBufRef;
    private SoftReference<Buffer> compressBufRef;
    protected String host;
    protected String seed;
    private String socketFactoryClassName;
    private boolean isInteractiveClient;
    private boolean logSlowQueries;
    private boolean profileSql;
    protected int port;
    protected int serverCapabilities;
    private boolean traceProtocol;
    private boolean useConnectWithDb;
    private boolean needToGrabQueryFromPacket;
    private boolean autoGenerateTestcaseScript;
    private long threadId;
    private boolean useNanosForElapsedTime;
    private long slowQueryThreshold;
    private String queryTimingUnits;
    private boolean useDirectRowUnpack;
    private int useBufferRowSizeThreshold;
    private List<StatementInterceptorV2> statementInterceptors;
    private ExceptionInterceptor exceptionInterceptor;
    private boolean useAutoSlowLog;
    private static int maxBufferSize = 65535;
    private static final String[] EXPLAINABLE_STATEMENT_EXTENSION = {"INSERT", "UPDATE", "REPLACE", "DELETE"};
    private boolean packetSequenceReset = false;
    private Buffer sharedSendPacket = null;
    private Deflater deflater = null;
    private RowData streamingData = null;
    private String serverVersion = null;
    private byte[] packetHeaderBuf = new byte[4];
    private boolean colDecimalNeedsBump = false;
    private boolean hadWarnings = false;
    private boolean has41NewNewProt = false;
    private boolean hasLongColumnInfo = false;
    private boolean platformDbCharsetMatches = true;
    private boolean queryBadIndexUsed = false;
    private boolean queryNoIndexUsed = false;
    private boolean serverQueryWasSlow = false;
    private boolean use41Extensions = false;
    private boolean useCompression = false;
    private boolean useNewLargePackets = false;
    private boolean useNewUpdateCounts = false;
    private byte packetSequence = 0;
    private byte compressedPacketSequence = 0;
    private byte readPacketSequence = -1;
    private boolean checkPacketSequence = false;
    private byte protocolVersion = 0;
    private int maxAllowedPacket = 1048576;
    protected int maxThreeBytes = 16581375;
    private int serverMajorVersion = 0;
    private int serverMinorVersion = 0;
    private int oldServerStatus = 0;
    private int serverStatus = 0;
    private int serverSubMinorVersion = 0;
    private int warningCount = 0;
    protected long clientParam = 0;
    protected long lastPacketSentTimeMs = 0;
    protected long lastPacketReceivedTimeMs = 0;
    private boolean enablePacketDebug = false;
    private int commandCount = 0;
    private int authPluginDataLength = 0;
    private Map<String, AuthenticationPlugin> authenticationPlugins = null;
    private List<String> disabledAuthenticationPlugins = null;
    private String clientDefaultAuthenticationPlugin = null;
    private String clientDefaultAuthenticationPluginName = null;
    private String serverDefaultAuthenticationPluginName = null;
    private int statementExecutionDepth = 0;

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0052, code lost:
    
        r5.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x004c, code lost:
    
        throw r6;
     */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0052 A[Catch: IOException -> 0x0059, TryCatch #0 {IOException -> 0x0059, blocks: (B:12:0x0052, B:20:0x0052, B:3:0x002b), top: B:23:0x002b, inners: #1 }] */
    static {
        /*
            r0 = 65535(0xffff, float:9.1834E-41)
            com.mysql.jdbc.MysqlIO.maxBufferSize = r0
            r0 = 0
            com.mysql.jdbc.MysqlIO.jvmPlatformCharset = r0
            r0 = 4
            java.lang.String[] r0 = new java.lang.String[r0]
            r1 = r0
            r2 = 0
            java.lang.String r3 = "INSERT"
            r1[r2] = r3
            r1 = r0
            r2 = 1
            java.lang.String r3 = "UPDATE"
            r1[r2] = r3
            r1 = r0
            r2 = 2
            java.lang.String r3 = "REPLACE"
            r1[r2] = r3
            r1 = r0
            r2 = 3
            java.lang.String r3 = "DELETE"
            r1[r2] = r3
            com.mysql.jdbc.MysqlIO.EXPLAINABLE_STATEMENT_EXTENSION = r0
            r0 = 0
            r5 = r0
            java.io.OutputStreamWriter r0 = new java.io.OutputStreamWriter     // Catch: java.lang.Throwable -> L47
            r1 = r0
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L47
            r3 = r2
            r3.<init>()     // Catch: java.lang.Throwable -> L47
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L47
            r5 = r0
            r0 = r5
            java.lang.String r0 = r0.getEncoding()     // Catch: java.lang.Throwable -> L47
            com.mysql.jdbc.MysqlIO.jvmPlatformCharset = r0     // Catch: java.lang.Throwable -> L47
            r0 = jsr -> L4d
        L44:
            goto L5c
        L47:
            r6 = move-exception
            r0 = jsr -> L4d
        L4b:
            r1 = r6
            throw r1
        L4d:
            r7 = r0
            r0 = r5
            if (r0 == 0) goto L56
            r0 = r5
            r0.close()     // Catch: java.io.IOException -> L59
        L56:
            goto L5a
        L59:
            r8 = move-exception
        L5a:
            ret r7
        L5c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.MysqlIO.m1374clinit():void");
    }

    public MysqlIO(String host, int port, Properties props, String socketFactoryClassName, MySQLConnection conn, int socketTimeout, int useBufferRowSizeThreshold) throws SQLException, IOException {
        this.reusablePacket = null;
        this.sendPacket = null;
        this.mysqlOutput = null;
        this.mysqlInput = null;
        this.packetDebugRingBuffer = null;
        this.mysqlConnection = null;
        this.socketFactory = null;
        this.host = null;
        this.socketFactoryClassName = null;
        this.isInteractiveClient = false;
        this.logSlowQueries = false;
        this.profileSql = false;
        this.port = 3306;
        this.traceProtocol = false;
        this.useDirectRowUnpack = true;
        this.connection = conn;
        if (this.connection.getEnablePacketDebug()) {
            this.packetDebugRingBuffer = new LinkedList<>();
        }
        this.traceProtocol = this.connection.getTraceProtocol();
        this.useAutoSlowLog = this.connection.getAutoSlowLog();
        this.useBufferRowSizeThreshold = useBufferRowSizeThreshold;
        this.useDirectRowUnpack = this.connection.getUseDirectRowUnpack();
        this.logSlowQueries = this.connection.getLogSlowQueries();
        this.reusablePacket = new Buffer(1024);
        this.sendPacket = new Buffer(1024);
        this.port = port;
        this.host = host;
        this.socketFactoryClassName = socketFactoryClassName;
        this.socketFactory = createSocketFactory();
        this.exceptionInterceptor = this.connection.getExceptionInterceptor();
        try {
            this.mysqlConnection = this.socketFactory.connect(this.host, this.port, props);
            if (socketTimeout != 0) {
                try {
                    this.mysqlConnection.setSoTimeout(socketTimeout);
                } catch (Exception e) {
                }
            }
            this.mysqlConnection = this.socketFactory.beforeHandshake();
            if (this.connection.getUseReadAheadInput()) {
                this.mysqlInput = new ReadAheadInputStream(this.mysqlConnection.getInputStream(), 16384, this.connection.getTraceProtocol(), this.connection.getLog());
            } else if (this.connection.useUnbufferedInput()) {
                this.mysqlInput = this.mysqlConnection.getInputStream();
            } else {
                this.mysqlInput = new BufferedInputStream(this.mysqlConnection.getInputStream(), 16384);
            }
            this.mysqlOutput = new BufferedOutputStream(this.mysqlConnection.getOutputStream(), 16384);
            this.isInteractiveClient = this.connection.getInteractiveClient();
            this.profileSql = this.connection.getProfileSql();
            this.autoGenerateTestcaseScript = this.connection.getAutoGenerateTestcaseScript();
            this.needToGrabQueryFromPacket = this.profileSql || this.logSlowQueries || this.autoGenerateTestcaseScript;
            this.useNanosForElapsedTime = this.connection.getUseNanosForElapsedTime() && TimeUtil.nanoTimeAvailable();
            this.queryTimingUnits = this.useNanosForElapsedTime ? Messages.getString("Nanoseconds") : Messages.getString("Milliseconds");
            if (this.connection.getLogSlowQueries()) {
                calculateSlowQueryThreshold();
            }
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, 0L, 0L, ioEx, getExceptionInterceptor());
        }
    }

    public boolean hasLongColumnInfo() {
        return this.hasLongColumnInfo;
    }

    protected boolean isDataAvailable() throws SQLException {
        try {
            return this.mysqlInput.available() > 0;
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        }
    }

    protected long getLastPacketSentTimeMs() {
        return this.lastPacketSentTimeMs;
    }

    protected long getLastPacketReceivedTimeMs() {
        return this.lastPacketReceivedTimeMs;
    }

    protected ResultSetImpl getResultSet(StatementImpl callingStatement, long columnCount, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, boolean isBinaryEncoded, Field[] metadataFromCache) throws SQLException, IOException {
        RowData rowData;
        Field[] fields = null;
        if (metadataFromCache == null) {
            fields = new Field[(int) columnCount];
            for (int i = 0; i < columnCount; i++) {
                Buffer fieldPacket = readPacket();
                fields[i] = unpackField(fieldPacket, false);
            }
        } else {
            for (int i2 = 0; i2 < columnCount; i2++) {
                skipPacket();
            }
        }
        if (!isEOFDeprecated() || (this.connection.versionMeetsMinimum(5, 0, 2) && callingStatement != null && isBinaryEncoded && callingStatement.isCursorRequired())) {
            Buffer packet = reuseAndReadPacket(this.reusablePacket);
            readServerStatusForResultSets(packet);
        }
        if (this.connection.versionMeetsMinimum(5, 0, 2) && this.connection.getUseCursorFetch() && isBinaryEncoded && callingStatement != null && callingStatement.getFetchSize() != 0 && callingStatement.getResultSetType() == 1003) {
            ServerPreparedStatement prepStmt = (ServerPreparedStatement) callingStatement;
            boolean usingCursor = true;
            if (this.connection.versionMeetsMinimum(5, 0, 5)) {
                usingCursor = (this.serverStatus & 64) != 0;
            }
            if (usingCursor) {
                RowData rows = new RowDataCursor(this, prepStmt, fields);
                ResultSetImpl rs = buildResultSetWithRows(callingStatement, catalog, fields, rows, resultSetType, resultSetConcurrency, isBinaryEncoded);
                if (usingCursor) {
                    rs.setFetchSize(callingStatement.getFetchSize());
                }
                return rs;
            }
        }
        if (!streamResults) {
            rowData = readSingleRowSet(columnCount, maxRows, resultSetConcurrency, isBinaryEncoded, metadataFromCache == null ? fields : metadataFromCache);
        } else {
            rowData = new RowDataDynamic(this, (int) columnCount, metadataFromCache == null ? fields : metadataFromCache, isBinaryEncoded);
            this.streamingData = rowData;
        }
        return buildResultSetWithRows(callingStatement, catalog, metadataFromCache == null ? fields : metadataFromCache, rowData, resultSetType, resultSetConcurrency, isBinaryEncoded);
    }

    protected NetworkResources getNetworkResources() {
        return new NetworkResources(this.mysqlConnection, this.mysqlInput, this.mysqlOutput);
    }

    protected final void forceClose() {
        try {
            getNetworkResources().forceClose();
        } finally {
            this.mysqlConnection = null;
            this.mysqlInput = null;
            this.mysqlOutput = null;
        }
    }

    protected final void skipPacket() throws SQLException, IOException {
        try {
            int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
            if (lengthRead < 4) {
                forceClose();
                throw new IOException(Messages.getString("MysqlIO.1"));
            }
            int packetLength = (this.packetHeaderBuf[0] & 255) + ((this.packetHeaderBuf[1] & 255) << 8) + ((this.packetHeaderBuf[2] & 255) << 16);
            if (this.traceProtocol) {
                this.connection.getLog().logTrace(Messages.getString("MysqlIO.2") + packetLength + Messages.getString("MysqlIO.3") + StringUtils.dumpAsHex(this.packetHeaderBuf, 4));
            }
            byte multiPacketSeq = this.packetHeaderBuf[3];
            if (!this.packetSequenceReset) {
                if (this.enablePacketDebug && this.checkPacketSequence) {
                    checkPacketSequencing(multiPacketSeq);
                }
            } else {
                this.packetSequenceReset = false;
            }
            this.readPacketSequence = multiPacketSeq;
            skipFully(this.mysqlInput, packetLength);
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        } catch (OutOfMemoryError oom) {
            try {
                this.connection.realClose(false, false, true, oom);
            } catch (Exception e) {
            }
            throw oom;
        }
    }

    protected final Buffer readPacket() throws SQLException, IOException {
        try {
            int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
            if (lengthRead < 4) {
                forceClose();
                throw new IOException(Messages.getString("MysqlIO.1"));
            }
            int packetLength = (this.packetHeaderBuf[0] & 255) + ((this.packetHeaderBuf[1] & 255) << 8) + ((this.packetHeaderBuf[2] & 255) << 16);
            if (packetLength > this.maxAllowedPacket) {
                throw new PacketTooBigException(packetLength, this.maxAllowedPacket);
            }
            if (this.traceProtocol) {
                this.connection.getLog().logTrace(Messages.getString("MysqlIO.2") + packetLength + Messages.getString("MysqlIO.3") + StringUtils.dumpAsHex(this.packetHeaderBuf, 4));
            }
            byte multiPacketSeq = this.packetHeaderBuf[3];
            if (!this.packetSequenceReset) {
                if (this.enablePacketDebug && this.checkPacketSequence) {
                    checkPacketSequencing(multiPacketSeq);
                }
            } else {
                this.packetSequenceReset = false;
            }
            this.readPacketSequence = multiPacketSeq;
            byte[] buffer = new byte[packetLength];
            int numBytesRead = readFully(this.mysqlInput, buffer, 0, packetLength);
            if (numBytesRead != packetLength) {
                throw new IOException("Short read, expected " + packetLength + " bytes, only read " + numBytesRead);
            }
            Buffer packet = new Buffer(buffer);
            if (this.traceProtocol) {
                this.connection.getLog().logTrace(Messages.getString("MysqlIO.4") + getPacketDumpToLog(packet, packetLength));
            }
            if (this.enablePacketDebug) {
                enqueuePacketForDebugging(false, false, 0, this.packetHeaderBuf, packet);
            }
            if (this.connection.getMaintainTimeStats()) {
                this.lastPacketReceivedTimeMs = System.currentTimeMillis();
            }
            return packet;
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        } catch (OutOfMemoryError oom) {
            try {
                this.connection.realClose(false, false, true, oom);
            } catch (Exception e) {
            }
            throw oom;
        }
    }

    protected final Field unpackField(Buffer packet, boolean extractDefaultValues) throws SQLException {
        short colFlag;
        long colLength;
        short colFlag2;
        if (this.use41Extensions) {
            if (this.has41NewNewProt) {
                int catalogNameStart = packet.getPosition() + 1;
                int catalogNameLength = packet.fastSkipLenString();
                adjustStartForFieldLength(catalogNameStart, catalogNameLength);
            }
            int databaseNameStart = packet.getPosition() + 1;
            int databaseNameLength = packet.fastSkipLenString();
            int databaseNameStart2 = adjustStartForFieldLength(databaseNameStart, databaseNameLength);
            int tableNameStart = packet.getPosition() + 1;
            int tableNameLength = packet.fastSkipLenString();
            int tableNameStart2 = adjustStartForFieldLength(tableNameStart, tableNameLength);
            int originalTableNameStart = packet.getPosition() + 1;
            int originalTableNameLength = packet.fastSkipLenString();
            int originalTableNameStart2 = adjustStartForFieldLength(originalTableNameStart, originalTableNameLength);
            int nameStart = packet.getPosition() + 1;
            int nameLength = packet.fastSkipLenString();
            int nameStart2 = adjustStartForFieldLength(nameStart, nameLength);
            int originalColumnNameStart = packet.getPosition() + 1;
            int originalColumnNameLength = packet.fastSkipLenString();
            int originalColumnNameStart2 = adjustStartForFieldLength(originalColumnNameStart, originalColumnNameLength);
            packet.readByte();
            short charSetNumber = (short) packet.readInt();
            if (this.has41NewNewProt) {
                colLength = packet.readLong();
            } else {
                colLength = packet.readLongInt();
            }
            int colType = packet.readByte() & 255;
            if (this.hasLongColumnInfo) {
                colFlag2 = (short) packet.readInt();
            } else {
                colFlag2 = (short) (packet.readByte() & 255);
            }
            int colDecimals = packet.readByte() & 255;
            int defaultValueStart = -1;
            int defaultValueLength = -1;
            if (extractDefaultValues) {
                defaultValueStart = packet.getPosition() + 1;
                defaultValueLength = packet.fastSkipLenString();
            }
            Field field = new Field(this.connection, packet.getByteBuffer(), databaseNameStart2, databaseNameLength, tableNameStart2, tableNameLength, originalTableNameStart2, originalTableNameLength, nameStart2, nameLength, originalColumnNameStart2, originalColumnNameLength, colLength, colType, colFlag2, colDecimals, defaultValueStart, defaultValueLength, charSetNumber);
            return field;
        }
        int tableNameStart3 = packet.getPosition() + 1;
        int tableNameLength2 = packet.fastSkipLenString();
        int tableNameStart4 = adjustStartForFieldLength(tableNameStart3, tableNameLength2);
        int nameStart3 = packet.getPosition() + 1;
        int nameLength2 = packet.fastSkipLenString();
        int nameStart4 = adjustStartForFieldLength(nameStart3, nameLength2);
        int colLength2 = packet.readnBytes();
        int colType2 = packet.readnBytes();
        packet.readByte();
        if (this.hasLongColumnInfo) {
            colFlag = (short) packet.readInt();
        } else {
            colFlag = (short) (packet.readByte() & 255);
        }
        int colDecimals2 = packet.readByte() & 255;
        if (this.colDecimalNeedsBump) {
            colDecimals2++;
        }
        Field field2 = new Field(this.connection, packet.getByteBuffer(), nameStart4, nameLength2, tableNameStart4, tableNameLength2, colLength2, colType2, colFlag, colDecimals2);
        return field2;
    }

    private int adjustStartForFieldLength(int nameStart, int nameLength) {
        if (nameLength < 251) {
            return nameStart;
        }
        if (nameLength >= 251 && nameLength < 65536) {
            return nameStart + 2;
        }
        if (nameLength >= 65536 && nameLength < 16777216) {
            return nameStart + 3;
        }
        return nameStart + 8;
    }

    protected boolean isSetNeededForAutoCommitMode(boolean autoCommitFlag) {
        if (this.use41Extensions && this.connection.getElideSetAutoCommits()) {
            boolean autoCommitModeOnServer = (this.serverStatus & 2) != 0;
            return (autoCommitFlag || !versionMeetsMinimum(5, 0, 0)) ? autoCommitModeOnServer != autoCommitFlag : !inTransactionOnServer();
        }
        return true;
    }

    protected boolean inTransactionOnServer() {
        return (this.serverStatus & 1) != 0;
    }

    protected void changeUser(String userName, String password, String database) throws SQLException, IOException {
        this.packetSequence = (byte) -1;
        this.compressedPacketSequence = (byte) -1;
        int userLength = userName != null ? userName.length() : 0;
        int databaseLength = database != null ? database.length() : 0;
        int packLength = ((userLength + 16 + databaseLength) * 3) + 7 + 4 + 33;
        if ((this.serverCapabilities & 524288) != 0) {
            proceedHandshakeWithPluggableAuthentication(userName, password, database, null);
            return;
        }
        if ((this.serverCapabilities & 32768) != 0) {
            Buffer changeUserPacket = new Buffer(packLength + 1);
            changeUserPacket.writeByte((byte) 17);
            if (versionMeetsMinimum(4, 1, 1)) {
                secureAuth411(changeUserPacket, packLength, userName, password, database, false, true);
                return;
            } else {
                secureAuth(changeUserPacket, packLength, userName, password, database, false);
                return;
            }
        }
        Buffer packet = new Buffer(packLength);
        packet.writeByte((byte) 17);
        packet.writeString(userName);
        if (this.protocolVersion > 9) {
            packet.writeString(Util.newCrypt(password, this.seed, this.connection.getPasswordCharacterEncoding()));
        } else {
            packet.writeString(Util.oldCrypt(password, this.seed));
        }
        boolean localUseConnectWithDb = this.useConnectWithDb && database != null && database.length() > 0;
        if (localUseConnectWithDb) {
            packet.writeString(database);
        }
        send(packet, packet.getPosition());
        checkErrorPacket();
        if (!localUseConnectWithDb) {
            changeDatabaseTo(database);
        }
    }

    protected Buffer checkErrorPacket() throws SQLException {
        return checkErrorPacket(-1);
    }

    protected void checkForCharsetMismatch() {
        if (this.connection.getUseUnicode() && this.connection.getEncoding() != null) {
            String encodingToCheck = jvmPlatformCharset;
            if (encodingToCheck == null) {
                encodingToCheck = System.getProperty("file.encoding");
            }
            if (encodingToCheck == null) {
                this.platformDbCharsetMatches = false;
            } else {
                this.platformDbCharsetMatches = encodingToCheck.equals(this.connection.getEncoding());
            }
        }
    }

    protected void clearInputStream() throws SQLException, IOException {
        int len;
        do {
            try {
                len = this.mysqlInput.available();
                if (len <= 0) {
                    break;
                }
            } catch (IOException ioEx) {
                throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
            }
        } while (this.mysqlInput.skip(len) > 0);
    }

    protected void resetReadPacketSequence() {
        this.readPacketSequence = (byte) 0;
    }

    protected void dumpPacketRingBuffer() throws SQLException {
        if (this.packetDebugRingBuffer != null && this.connection.getEnablePacketDebug()) {
            StringBuilder dumpBuffer = new StringBuilder();
            dumpBuffer.append("Last " + this.packetDebugRingBuffer.size() + " packets received from server, from oldest->newest:\n");
            dumpBuffer.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            Iterator<StringBuilder> ringBufIter = this.packetDebugRingBuffer.iterator();
            while (ringBufIter.hasNext()) {
                dumpBuffer.append((CharSequence) ringBufIter.next());
                dumpBuffer.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
            this.connection.getLog().logTrace(dumpBuffer.toString());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0096, code lost:
    
        throw r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x009e, code lost:
    
        r9.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00a6, code lost:
    
        if (r8 == null) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00a9, code lost:
    
        r8.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x009e, code lost:
    
        r9.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00a6, code lost:
    
        if (r8 == null) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00a9, code lost:
    
        r8.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:?, code lost:
    
        return;
     */
    /* JADX WARN: Removed duplicated region for block: B:20:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00a9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void explainSlowQuery(byte[] r6, java.lang.String r7) throws java.sql.SQLException {
        /*
            r5 = this;
            r0 = r7
            java.lang.String r1 = "SELECT"
            boolean r0 = com.mysql.jdbc.StringUtils.startsWithIgnoreCaseAndWs(r0, r1)
            if (r0 != 0) goto L1f
            r0 = r5
            r1 = 5
            r2 = 6
            r3 = 3
            boolean r0 = r0.versionMeetsMinimum(r1, r2, r3)
            if (r0 == 0) goto Laf
            r0 = r7
            java.lang.String[] r1 = com.mysql.jdbc.MysqlIO.EXPLAINABLE_STATEMENT_EXTENSION
            int r0 = com.mysql.jdbc.StringUtils.startsWithIgnoreCaseAndWs(r0, r1)
            r1 = -1
            if (r0 == r1) goto Laf
        L1f:
            r0 = 0
            r8 = r0
            r0 = 0
            r9 = r0
            r0 = r5
            com.mysql.jdbc.MySQLConnection r0 = r0.connection     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            java.lang.String r1 = "EXPLAIN ?"
            java.sql.PreparedStatement r0 = r0.clientPrepareStatement(r1)     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            com.mysql.jdbc.PreparedStatement r0 = (com.mysql.jdbc.PreparedStatement) r0     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            r8 = r0
            r0 = r8
            r1 = 1
            r2 = r6
            r0.setBytesNoEscapeNoQuotes(r1, r2)     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            r0 = r8
            java.sql.ResultSet r0 = r0.executeQuery()     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            r9 = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            r3 = r2
            r3.<init>()     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            java.lang.String r3 = "MysqlIO.8"
            java.lang.String r3 = com.mysql.jdbc.Messages.getString(r3)     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            r3 = r7
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            java.lang.String r3 = "MysqlIO.9"
            java.lang.String r3 = com.mysql.jdbc.Messages.getString(r3)     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            java.lang.String r2 = r2.toString()     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            r1.<init>(r2)     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            r10 = r0
            r0 = r10
            r1 = r9
            java.lang.StringBuilder r0 = com.mysql.jdbc.util.ResultSetUtil.appendResultSetSlashGStyle(r0, r1)     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            r0 = r5
            com.mysql.jdbc.MySQLConnection r0 = r0.connection     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            com.mysql.jdbc.log.Log r0 = r0.getLog()     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            r1 = r10
            java.lang.String r1 = r1.toString()     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            r0.logWarn(r1)     // Catch: java.sql.SQLException -> L87 java.lang.Throwable -> L8f
            r0 = jsr -> L97
        L84:
            goto Laf
        L87:
            r10 = move-exception
            r0 = jsr -> L97
        L8c:
            goto Laf
        L8f:
            r11 = move-exception
            r0 = jsr -> L97
        L94:
            r1 = r11
            throw r1
        L97:
            r12 = r0
            r0 = r9
            if (r0 == 0) goto La5
            r0 = r9
            r0.close()
        La5:
            r0 = r8
            if (r0 == 0) goto Lad
            r0 = r8
            r0.close()
        Lad:
            ret r12
        Laf:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.MysqlIO.explainSlowQuery(byte[], java.lang.String):void");
    }

    static int getMaxBuf() {
        return maxBufferSize;
    }

    final int getServerMajorVersion() {
        return this.serverMajorVersion;
    }

    final int getServerMinorVersion() {
        return this.serverMinorVersion;
    }

    final int getServerSubMinorVersion() {
        return this.serverSubMinorVersion;
    }

    String getServerVersion() {
        return this.serverVersion;
    }

    void doHandshake(String user, String password, String database) throws SQLException {
        String seedPart2;
        StringBuilder newSeed;
        this.checkPacketSequence = false;
        this.readPacketSequence = (byte) 0;
        Buffer buf = readPacket();
        this.protocolVersion = buf.readByte();
        if (this.protocolVersion == -1) {
            try {
                this.mysqlConnection.close();
            } catch (Exception e) {
            }
            int errno = buf.readInt();
            String serverErrorMessage = buf.readString(NTLM.DEFAULT_CHARSET, getExceptionInterceptor());
            String xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
            throw SQLError.createSQLException(SQLError.get(xOpen) + ", " + (Messages.getString("MysqlIO.10") + serverErrorMessage + SymbolConstants.QUOTES_SYMBOL), xOpen, errno, getExceptionInterceptor());
        }
        this.serverVersion = buf.readString(NTLM.DEFAULT_CHARSET, getExceptionInterceptor());
        int point = this.serverVersion.indexOf(46);
        if (point != -1) {
            try {
                int n = Integer.parseInt(this.serverVersion.substring(0, point));
                this.serverMajorVersion = n;
            } catch (NumberFormatException e2) {
            }
            String remaining = this.serverVersion.substring(point + 1, this.serverVersion.length());
            int point2 = remaining.indexOf(46);
            if (point2 != -1) {
                try {
                    int n2 = Integer.parseInt(remaining.substring(0, point2));
                    this.serverMinorVersion = n2;
                } catch (NumberFormatException e3) {
                }
                String remaining2 = remaining.substring(point2 + 1, remaining.length());
                int pos = 0;
                while (pos < remaining2.length() && remaining2.charAt(pos) >= '0' && remaining2.charAt(pos) <= '9') {
                    pos++;
                }
                try {
                    int n3 = Integer.parseInt(remaining2.substring(0, pos));
                    this.serverSubMinorVersion = n3;
                } catch (NumberFormatException e4) {
                }
            }
        }
        if (versionMeetsMinimum(4, 0, 8)) {
            this.maxThreeBytes = 16777215;
            this.useNewLargePackets = true;
        } else {
            this.maxThreeBytes = 16581375;
            this.useNewLargePackets = false;
        }
        this.colDecimalNeedsBump = versionMeetsMinimum(3, 23, 0);
        this.colDecimalNeedsBump = !versionMeetsMinimum(3, 23, 15);
        this.useNewUpdateCounts = versionMeetsMinimum(3, 22, 5);
        this.threadId = buf.readLong();
        if (this.protocolVersion > 9) {
            this.seed = buf.readString(NTLM.DEFAULT_CHARSET, getExceptionInterceptor(), 8);
            buf.readByte();
        } else {
            this.seed = buf.readString(NTLM.DEFAULT_CHARSET, getExceptionInterceptor());
        }
        this.serverCapabilities = 0;
        if (buf.getPosition() < buf.getBufLength()) {
            this.serverCapabilities = buf.readInt();
        }
        if (versionMeetsMinimum(4, 1, 1) || (this.protocolVersion > 9 && (this.serverCapabilities & 512) != 0)) {
            this.serverCharsetIndex = buf.readByte() & 255;
            this.serverStatus = buf.readInt();
            checkTransactionState(0);
            this.serverCapabilities |= buf.readInt() << 16;
            if ((this.serverCapabilities & 524288) != 0) {
                this.authPluginDataLength = buf.readByte() & 255;
            } else {
                buf.readByte();
            }
            buf.setPosition(buf.getPosition() + 10);
            if ((this.serverCapabilities & 32768) != 0) {
                if (this.authPluginDataLength > 0) {
                    seedPart2 = buf.readString(NTLM.DEFAULT_CHARSET, getExceptionInterceptor(), this.authPluginDataLength - 8);
                    newSeed = new StringBuilder(this.authPluginDataLength);
                } else {
                    seedPart2 = buf.readString(NTLM.DEFAULT_CHARSET, getExceptionInterceptor());
                    newSeed = new StringBuilder(20);
                }
                newSeed.append(this.seed);
                newSeed.append(seedPart2);
                this.seed = newSeed.toString();
            }
        }
        if ((this.serverCapabilities & 32) != 0 && this.connection.getUseCompression()) {
            this.clientParam |= 32;
        }
        this.useConnectWithDb = (database == null || database.length() <= 0 || this.connection.getCreateDatabaseIfNotExist()) ? false : true;
        if (this.useConnectWithDb) {
            this.clientParam |= 8;
        }
        if (versionMeetsMinimum(5, 7, 0) && !this.connection.getUseSSL() && !this.connection.isUseSSLExplicit()) {
            this.connection.setUseSSL(true);
            this.connection.setVerifyServerCertificate(false);
            this.connection.getLog().logWarn(Messages.getString("MysqlIO.SSLWarning"));
        }
        if ((this.serverCapabilities & 2048) == 0 && this.connection.getUseSSL()) {
            if (this.connection.getRequireSSL()) {
                this.connection.close();
                forceClose();
                throw SQLError.createSQLException(Messages.getString("MysqlIO.15"), SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE, getExceptionInterceptor());
            }
            this.connection.setUseSSL(false);
        }
        if ((this.serverCapabilities & 4) != 0) {
            this.clientParam |= 4;
            this.hasLongColumnInfo = true;
        }
        if (!this.connection.getUseAffectedRows()) {
            this.clientParam |= 2;
        }
        if (this.connection.getAllowLoadLocalInfile()) {
            this.clientParam |= 128;
        }
        if (this.isInteractiveClient) {
            this.clientParam |= 1024;
        }
        if ((this.serverCapabilities & 8388608) != 0) {
        }
        if ((this.serverCapabilities & 16777216) != 0) {
            this.clientParam |= 16777216;
        }
        if ((this.serverCapabilities & 524288) != 0) {
            proceedHandshakeWithPluggableAuthentication(user, password, database, buf);
            return;
        }
        if (this.protocolVersion > 9) {
            this.clientParam |= 1;
        } else {
            this.clientParam &= -2;
        }
        if (versionMeetsMinimum(4, 1, 0) || (this.protocolVersion > 9 && (this.serverCapabilities & 16384) != 0)) {
            if (versionMeetsMinimum(4, 1, 1) || (this.protocolVersion > 9 && (this.serverCapabilities & 512) != 0)) {
                this.clientParam |= 512;
                this.has41NewNewProt = true;
                this.clientParam |= 8192;
                this.clientParam |= 131072;
                if (this.connection.getAllowMultiQueries()) {
                    this.clientParam |= org.aspectj.apache.bcel.Constants.EXCEPTION_THROWER;
                }
            } else {
                this.clientParam |= 16384;
                this.has41NewNewProt = false;
            }
            this.use41Extensions = true;
        }
        int userLength = user != null ? user.length() : 0;
        int databaseLength = database != null ? database.length() : 0;
        int packLength = ((userLength + 16 + databaseLength) * 3) + 7 + 4 + 33;
        if (!this.connection.getUseSSL()) {
            if ((this.serverCapabilities & 32768) != 0) {
                this.clientParam |= org.aspectj.apache.bcel.Constants.RET_INST;
                if (versionMeetsMinimum(4, 1, 1) || (this.protocolVersion > 9 && (this.serverCapabilities & 512) != 0)) {
                    secureAuth411(null, packLength, user, password, database, true, false);
                } else {
                    secureAuth(null, packLength, user, password, database, true);
                }
            } else {
                Buffer packet = new Buffer(packLength);
                if ((this.clientParam & 16384) != 0) {
                    if (versionMeetsMinimum(4, 1, 1) || (this.protocolVersion > 9 && (this.serverCapabilities & 512) != 0)) {
                        packet.writeLong(this.clientParam);
                        packet.writeLong(this.maxThreeBytes);
                        packet.writeByte((byte) 8);
                        packet.writeBytesNoNull(new byte[23]);
                    } else {
                        packet.writeLong(this.clientParam);
                        packet.writeLong(this.maxThreeBytes);
                    }
                } else {
                    packet.writeInt((int) this.clientParam);
                    packet.writeLongInt(this.maxThreeBytes);
                }
                packet.writeString(user, "Cp1252", this.connection);
                if (this.protocolVersion > 9) {
                    packet.writeString(Util.newCrypt(password, this.seed, this.connection.getPasswordCharacterEncoding()), "Cp1252", this.connection);
                } else {
                    packet.writeString(Util.oldCrypt(password, this.seed), "Cp1252", this.connection);
                }
                if (this.useConnectWithDb) {
                    packet.writeString(database, "Cp1252", this.connection);
                }
                send(packet, packet.getPosition());
            }
        } else {
            negotiateSSLConnection(user, password, database, packLength);
            if ((this.serverCapabilities & 32768) != 0) {
                if (versionMeetsMinimum(4, 1, 1)) {
                    secureAuth411(null, packLength, user, password, database, true, false);
                } else {
                    secureAuth411(null, packLength, user, password, database, true, false);
                }
            } else {
                Buffer packet2 = new Buffer(packLength);
                if (this.use41Extensions) {
                    packet2.writeLong(this.clientParam);
                    packet2.writeLong(this.maxThreeBytes);
                } else {
                    packet2.writeInt((int) this.clientParam);
                    packet2.writeLongInt(this.maxThreeBytes);
                }
                packet2.writeString(user);
                if (this.protocolVersion > 9) {
                    packet2.writeString(Util.newCrypt(password, this.seed, this.connection.getPasswordCharacterEncoding()));
                } else {
                    packet2.writeString(Util.oldCrypt(password, this.seed));
                }
                if ((this.serverCapabilities & 8) != 0 && database != null && database.length() > 0) {
                    packet2.writeString(database);
                }
                send(packet2, packet2.getPosition());
            }
        }
        if (!versionMeetsMinimum(4, 1, 1) || this.protocolVersion <= 9 || (this.serverCapabilities & 512) == 0) {
            checkErrorPacket();
        }
        if ((this.serverCapabilities & 32) != 0 && this.connection.getUseCompression() && !(this.mysqlInput instanceof CompressedInputStream)) {
            this.deflater = new Deflater();
            this.useCompression = true;
            this.mysqlInput = new CompressedInputStream(this.connection, this.mysqlInput);
        }
        if (!this.useConnectWithDb) {
            changeDatabaseTo(database);
        }
        try {
            this.mysqlConnection = this.socketFactory.afterHandshake();
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        }
    }

    private void loadAuthenticationPlugins() throws SQLException {
        this.clientDefaultAuthenticationPlugin = this.connection.getDefaultAuthenticationPlugin();
        if (this.clientDefaultAuthenticationPlugin == null || "".equals(this.clientDefaultAuthenticationPlugin.trim())) {
            throw SQLError.createSQLException(Messages.getString("Connection.BadDefaultAuthenticationPlugin", new Object[]{this.clientDefaultAuthenticationPlugin}), getExceptionInterceptor());
        }
        String disabledPlugins = this.connection.getDisabledAuthenticationPlugins();
        if (disabledPlugins != null && !"".equals(disabledPlugins)) {
            this.disabledAuthenticationPlugins = new ArrayList();
            List<String> pluginsToDisable = StringUtils.split(disabledPlugins, ",", true);
            Iterator<String> iter = pluginsToDisable.iterator();
            while (iter.hasNext()) {
                this.disabledAuthenticationPlugins.add(iter.next());
            }
        }
        this.authenticationPlugins = new HashMap();
        AuthenticationPlugin plugin = new MysqlOldPasswordPlugin();
        plugin.init(this.connection, this.connection.getProperties());
        boolean defaultIsFound = addAuthenticationPlugin(plugin);
        AuthenticationPlugin plugin2 = new MysqlNativePasswordPlugin();
        plugin2.init(this.connection, this.connection.getProperties());
        if (addAuthenticationPlugin(plugin2)) {
            defaultIsFound = true;
        }
        AuthenticationPlugin plugin3 = new MysqlClearPasswordPlugin();
        plugin3.init(this.connection, this.connection.getProperties());
        if (addAuthenticationPlugin(plugin3)) {
            defaultIsFound = true;
        }
        AuthenticationPlugin plugin4 = new Sha256PasswordPlugin();
        plugin4.init(this.connection, this.connection.getProperties());
        if (addAuthenticationPlugin(plugin4)) {
            defaultIsFound = true;
        }
        AuthenticationPlugin plugin5 = new CachingSha2PasswordPlugin();
        plugin5.init(this.connection, this.connection.getProperties());
        if (addAuthenticationPlugin(plugin5)) {
            defaultIsFound = true;
        }
        String authenticationPluginClasses = this.connection.getAuthenticationPlugins();
        if (authenticationPluginClasses != null && !"".equals(authenticationPluginClasses)) {
            List<Extension> plugins = Util.loadExtensions(this.connection, this.connection.getProperties(), authenticationPluginClasses, "Connection.BadAuthenticationPlugin", getExceptionInterceptor());
            for (Extension object : plugins) {
                if (addAuthenticationPlugin((AuthenticationPlugin) object)) {
                    defaultIsFound = true;
                }
            }
        }
        if (!defaultIsFound) {
            throw SQLError.createSQLException(Messages.getString("Connection.DefaultAuthenticationPluginIsNotListed", new Object[]{this.clientDefaultAuthenticationPlugin}), getExceptionInterceptor());
        }
    }

    private boolean addAuthenticationPlugin(AuthenticationPlugin plugin) throws SQLException {
        boolean isDefault = false;
        String pluginClassName = plugin.getClass().getName();
        String pluginProtocolName = plugin.getProtocolPluginName();
        boolean disabledByClassName = this.disabledAuthenticationPlugins != null && this.disabledAuthenticationPlugins.contains(pluginClassName);
        boolean disabledByMechanism = this.disabledAuthenticationPlugins != null && this.disabledAuthenticationPlugins.contains(pluginProtocolName);
        if (disabledByClassName || disabledByMechanism) {
            if (this.clientDefaultAuthenticationPlugin.equals(pluginClassName)) {
                Object[] objArr = new Object[1];
                objArr[0] = disabledByClassName ? pluginClassName : pluginProtocolName;
                throw SQLError.createSQLException(Messages.getString("Connection.BadDisabledAuthenticationPlugin", objArr), getExceptionInterceptor());
            }
        } else {
            this.authenticationPlugins.put(pluginProtocolName, plugin);
            if (this.clientDefaultAuthenticationPlugin.equals(pluginClassName)) {
                this.clientDefaultAuthenticationPluginName = pluginProtocolName;
                isDefault = true;
            }
        }
        return isDefault;
    }

    private AuthenticationPlugin getAuthenticationPlugin(String pluginName) throws SQLException {
        AuthenticationPlugin plugin = this.authenticationPlugins.get(pluginName);
        if (plugin != null && !plugin.isReusable()) {
            try {
                plugin = (AuthenticationPlugin) plugin.getClass().newInstance();
                plugin.init(this.connection, this.connection.getProperties());
            } catch (Throwable t) {
                SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.BadAuthenticationPlugin", new Object[]{plugin.getClass().getName()}), getExceptionInterceptor());
                sqlEx.initCause(t);
                throw sqlEx;
            }
        }
        return plugin;
    }

    private void checkConfidentiality(AuthenticationPlugin plugin) throws SQLException {
        if (plugin.requiresConfidentiality() && !isSSLEstablished()) {
            throw SQLError.createSQLException(Messages.getString("Connection.AuthenticationPluginRequiresSSL", new Object[]{plugin.getProtocolPluginName()}), getExceptionInterceptor());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:153:0x0640, code lost:
    
        if (r24 != 0) goto L156;
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x0650, code lost:
    
        throw com.mysql.jdbc.SQLError.createSQLException(com.mysql.jdbc.Messages.getString("CommunicationsException.TooManyAuthenticationPluginNegotiations"), getExceptionInterceptor());
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x0658, code lost:
    
        if ((r8.serverCapabilities & 32) == 0) goto L163;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x0664, code lost:
    
        if (r8.connection.getUseCompression() == false) goto L163;
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x066e, code lost:
    
        if ((r8.mysqlInput instanceof com.mysql.jdbc.CompressedInputStream) != false) goto L163;
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x0671, code lost:
    
        r8.deflater = new java.util.zip.Deflater();
        r8.useCompression = true;
        r8.mysqlInput = new com.mysql.jdbc.CompressedInputStream(r8.connection, r8.mysqlInput);
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x0698, code lost:
    
        if (r8.useConnectWithDb != false) goto L171;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x069b, code lost:
    
        changeDatabaseTo(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x06a0, code lost:
    
        r8.mysqlConnection = r8.socketFactory.afterHandshake();
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x06b0, code lost:
    
        r25 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x06c7, code lost:
    
        throw com.mysql.jdbc.SQLError.createCommunicationsException(r8.connection, r8.lastPacketSentTimeMs, r8.lastPacketReceivedTimeMs, r25, getExceptionInterceptor());
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x06c8, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void proceedHandshakeWithPluggableAuthentication(java.lang.String r9, java.lang.String r10, java.lang.String r11, com.mysql.jdbc.Buffer r12) throws java.sql.SQLException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 1737
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.MysqlIO.proceedHandshakeWithPluggableAuthentication(java.lang.String, java.lang.String, java.lang.String, com.mysql.jdbc.Buffer):void");
    }

    private Properties getConnectionAttributesAsProperties(String atts) throws SQLException {
        Properties props = new Properties();
        if (atts != null) {
            String[] pairs = atts.split(",");
            for (String pair : pairs) {
                int keyEnd = pair.indexOf(":");
                if (keyEnd > 0 && keyEnd + 1 < pair.length()) {
                    props.setProperty(pair.substring(0, keyEnd), pair.substring(keyEnd + 1));
                }
            }
        }
        props.setProperty("_client_name", NonRegisteringDriver.NAME);
        props.setProperty("_client_version", NonRegisteringDriver.VERSION);
        props.setProperty("_runtime_vendor", NonRegisteringDriver.RUNTIME_VENDOR);
        props.setProperty("_runtime_version", NonRegisteringDriver.RUNTIME_VERSION);
        props.setProperty("_client_license", NonRegisteringDriver.LICENSE);
        return props;
    }

    private void sendConnectionAttributes(Buffer buf, String enc, MySQLConnection conn) throws SQLException {
        String atts = conn.getConnectionAttributes();
        Buffer lb = new Buffer(100);
        try {
            Properties props = getConnectionAttributesAsProperties(atts);
            for (Object key : props.keySet()) {
                lb.writeLenString((String) key, enc, conn.getServerCharset(), null, conn.parserKnowsUnicode(), conn);
                lb.writeLenString(props.getProperty((String) key), enc, conn.getServerCharset(), null, conn.parserKnowsUnicode(), conn);
            }
        } catch (UnsupportedEncodingException e) {
        }
        buf.writeByte((byte) (lb.getPosition() - 4));
        buf.writeBytesNoNull(lb.getByteBuffer(), 4, lb.getBufLength() - 4);
    }

    private void changeDatabaseTo(String database) throws SQLException {
        if (database == null || database.length() == 0) {
            return;
        }
        try {
            sendCommand(2, database, null, false, null, 0);
        } catch (Exception ex) {
            if (this.connection.getCreateDatabaseIfNotExist()) {
                sendCommand(3, "CREATE DATABASE IF NOT EXISTS " + database, null, false, null, 0);
                sendCommand(2, database, null, false, null, 0);
                return;
            }
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ex, getExceptionInterceptor());
        }
    }

    /* JADX WARN: Type inference failed for: r0v33, types: [byte[], byte[][]] */
    final ResultSetRow nextRow(Field[] fields, int columnCount, boolean isBinaryEncoded, int resultSetConcurrency, boolean useBufferRowIfPossible, boolean useBufferRowExplicit, boolean canReuseRowPacketForBufferRow, Buffer existingRowPacket) throws SQLException, IOException {
        Buffer rowPacket;
        if (this.useDirectRowUnpack && existingRowPacket == null && !isBinaryEncoded && !useBufferRowIfPossible && !useBufferRowExplicit) {
            return nextRowFast(fields, columnCount, isBinaryEncoded, resultSetConcurrency, useBufferRowIfPossible, useBufferRowExplicit, canReuseRowPacketForBufferRow);
        }
        if (existingRowPacket == null) {
            rowPacket = checkErrorPacket();
            if (!useBufferRowExplicit && useBufferRowIfPossible && rowPacket.getBufLength() > this.useBufferRowSizeThreshold) {
                useBufferRowExplicit = true;
            }
        } else {
            rowPacket = existingRowPacket;
            checkErrorPacket(existingRowPacket);
        }
        if (!isBinaryEncoded) {
            rowPacket.setPosition(rowPacket.getPosition() - 1);
            if ((isEOFDeprecated() || !rowPacket.isEOFPacket()) && (!isEOFDeprecated() || !rowPacket.isResultSetOKPacket())) {
                if (resultSetConcurrency == 1008 || (!useBufferRowIfPossible && !useBufferRowExplicit)) {
                    ?? r0 = new byte[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        r0[i] = rowPacket.readLenByteArray(0);
                    }
                    return new ByteArrayRow(r0, getExceptionInterceptor());
                }
                if (!canReuseRowPacketForBufferRow) {
                    this.reusablePacket = new Buffer(rowPacket.getBufLength());
                }
                return new BufferRow(rowPacket, fields, false, getExceptionInterceptor());
            }
            readServerStatusForResultSets(rowPacket);
            return null;
        }
        if ((isEOFDeprecated() || !rowPacket.isEOFPacket()) && (!isEOFDeprecated() || !rowPacket.isResultSetOKPacket())) {
            if (resultSetConcurrency == 1008 || (!useBufferRowIfPossible && !useBufferRowExplicit)) {
                return unpackBinaryResultSetRow(fields, rowPacket, resultSetConcurrency);
            }
            if (!canReuseRowPacketForBufferRow) {
                this.reusablePacket = new Buffer(rowPacket.getBufLength());
            }
            return new BufferRow(rowPacket, fields, true, getExceptionInterceptor());
        }
        rowPacket.setPosition(rowPacket.getPosition() - 1);
        readServerStatusForResultSets(rowPacket);
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v73, types: [byte[]] */
    final ResultSetRow nextRowFast(Field[] fields, int columnCount, boolean isBinaryEncoded, int resultSetConcurrency, boolean useBufferRowIfPossible, boolean useBufferRowExplicit, boolean canReuseRowPacket) throws SQLException {
        int len;
        int remaining;
        try {
            int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
            if (lengthRead < 4) {
                forceClose();
                throw new RuntimeException(Messages.getString("MysqlIO.43"));
            }
            int packetLength = (this.packetHeaderBuf[0] & 255) + ((this.packetHeaderBuf[1] & 255) << 8) + ((this.packetHeaderBuf[2] & 255) << 16);
            if (packetLength == this.maxThreeBytes) {
                reuseAndReadPacket(this.reusablePacket, packetLength);
                return nextRow(fields, columnCount, isBinaryEncoded, resultSetConcurrency, useBufferRowIfPossible, useBufferRowExplicit, canReuseRowPacket, this.reusablePacket);
            }
            if (packetLength > this.useBufferRowSizeThreshold) {
                reuseAndReadPacket(this.reusablePacket, packetLength);
                return nextRow(fields, columnCount, isBinaryEncoded, resultSetConcurrency, true, true, false, this.reusablePacket);
            }
            int remaining2 = packetLength;
            boolean firstTime = true;
            byte[][] rowData = (byte[][]) null;
            for (int i = 0; i < columnCount; i++) {
                int sw = this.mysqlInput.read() & 255;
                remaining2--;
                if (firstTime) {
                    if (sw == 255) {
                        Buffer errorPacket = new Buffer(packetLength + 4);
                        errorPacket.setPosition(0);
                        errorPacket.writeByte(this.packetHeaderBuf[0]);
                        errorPacket.writeByte(this.packetHeaderBuf[1]);
                        errorPacket.writeByte(this.packetHeaderBuf[2]);
                        errorPacket.writeByte((byte) 1);
                        errorPacket.writeByte((byte) sw);
                        readFully(this.mysqlInput, errorPacket.getByteBuffer(), 5, packetLength - 1);
                        errorPacket.setPosition(4);
                        checkErrorPacket(errorPacket);
                    }
                    if (sw == 254 && packetLength < 16777215) {
                        if (this.use41Extensions) {
                            if (isEOFDeprecated()) {
                                int remaining3 = (remaining2 - skipLengthEncodedInteger(this.mysqlInput)) - skipLengthEncodedInteger(this.mysqlInput);
                                this.oldServerStatus = this.serverStatus;
                                this.serverStatus = (this.mysqlInput.read() & 255) | ((this.mysqlInput.read() & 255) << 8);
                                checkTransactionState(this.oldServerStatus);
                                this.warningCount = (this.mysqlInput.read() & 255) | ((this.mysqlInput.read() & 255) << 8);
                                remaining = (remaining3 - 2) - 2;
                                if (this.warningCount > 0) {
                                    this.hadWarnings = true;
                                }
                            } else {
                                this.warningCount = (this.mysqlInput.read() & 255) | ((this.mysqlInput.read() & 255) << 8);
                                int remaining4 = remaining2 - 2;
                                if (this.warningCount > 0) {
                                    this.hadWarnings = true;
                                }
                                this.oldServerStatus = this.serverStatus;
                                this.serverStatus = (this.mysqlInput.read() & 255) | ((this.mysqlInput.read() & 255) << 8);
                                checkTransactionState(this.oldServerStatus);
                                remaining = remaining4 - 2;
                            }
                            setServerSlowQueryFlags();
                            if (remaining > 0) {
                                skipFully(this.mysqlInput, remaining);
                                return null;
                            }
                            return null;
                        }
                        return null;
                    }
                    rowData = new byte[columnCount];
                    firstTime = false;
                }
                switch (sw) {
                    case 251:
                        len = -1;
                        break;
                    case 252:
                        len = (this.mysqlInput.read() & 255) | ((this.mysqlInput.read() & 255) << 8);
                        remaining2 -= 2;
                        break;
                    case 253:
                        len = (this.mysqlInput.read() & 255) | ((this.mysqlInput.read() & 255) << 8) | ((this.mysqlInput.read() & 255) << 16);
                        remaining2 -= 3;
                        break;
                    case 254:
                        len = (int) ((this.mysqlInput.read() & 255) | ((this.mysqlInput.read() & 255) << 8) | ((this.mysqlInput.read() & 255) << 16) | ((this.mysqlInput.read() & 255) << 24) | ((this.mysqlInput.read() & 255) << 32) | ((this.mysqlInput.read() & 255) << 40) | ((this.mysqlInput.read() & 255) << 48) | ((this.mysqlInput.read() & 255) << 56));
                        remaining2 -= 8;
                        break;
                    default:
                        len = sw;
                        break;
                }
                if (len == -1) {
                    rowData[i] = null;
                } else if (len == 0) {
                    rowData[i] = Constants.EMPTY_BYTE_ARRAY;
                } else {
                    rowData[i] = new byte[len];
                    int bytesRead = readFully(this.mysqlInput, rowData[i], 0, len);
                    if (bytesRead != len) {
                        throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException(Messages.getString("MysqlIO.43")), getExceptionInterceptor());
                    }
                    remaining2 -= bytesRead;
                }
            }
            if (remaining2 > 0) {
                skipFully(this.mysqlInput, remaining2);
            }
            return new ByteArrayRow(rowData, getExceptionInterceptor());
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        }
    }

    final void quit() throws SQLException {
        try {
            try {
                if (!ExportControlled.isSSLEstablished(this.mysqlConnection) && !this.mysqlConnection.isClosed()) {
                    try {
                        this.mysqlConnection.shutdownInput();
                    } catch (UnsupportedOperationException e) {
                    }
                }
            } finally {
                forceClose();
            }
        } catch (IOException e2) {
        }
        Buffer packet = new Buffer(6);
        this.packetSequence = (byte) -1;
        this.compressedPacketSequence = (byte) -1;
        packet.writeByte((byte) 1);
        send(packet, packet.getPosition());
    }

    Buffer getSharedSendPacket() {
        if (this.sharedSendPacket == null) {
            this.sharedSendPacket = new Buffer(1024);
        }
        return this.sharedSendPacket;
    }

    void closeStreamer(RowData streamer) throws SQLException {
        if (this.streamingData == null) {
            throw SQLError.createSQLException(Messages.getString("MysqlIO.17") + streamer + Messages.getString("MysqlIO.18"), getExceptionInterceptor());
        }
        if (streamer != this.streamingData) {
            throw SQLError.createSQLException(Messages.getString("MysqlIO.19") + streamer + Messages.getString("MysqlIO.20") + Messages.getString("MysqlIO.21") + Messages.getString("MysqlIO.22"), getExceptionInterceptor());
        }
        this.streamingData = null;
    }

    boolean tackOnMoreStreamingResults(ResultSetImpl addingTo) throws SQLException, IOException {
        if ((this.serverStatus & 8) != 0) {
            boolean moreRowSetsExist = true;
            ResultSetImpl currentResultSet = addingTo;
            boolean firstTime = true;
            while (moreRowSetsExist) {
                if (firstTime || !currentResultSet.reallyResult()) {
                    firstTime = false;
                    Buffer fieldPacket = checkErrorPacket();
                    fieldPacket.setPosition(0);
                    java.sql.Statement owningStatement = addingTo.getStatement();
                    int maxRows = owningStatement.getMaxRows();
                    ResultSetImpl newResultSet = readResultsForQueryOrUpdate((StatementImpl) owningStatement, maxRows, owningStatement.getResultSetType(), owningStatement.getResultSetConcurrency(), true, owningStatement.getConnection().getCatalog(), fieldPacket, addingTo.isBinaryEncoded, -1L, null);
                    currentResultSet.setNextResultSet(newResultSet);
                    currentResultSet = newResultSet;
                    moreRowSetsExist = (this.serverStatus & 8) != 0;
                    if (!currentResultSet.reallyResult() && !moreRowSetsExist) {
                        return false;
                    }
                } else {
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    /* JADX WARN: Incorrect condition in loop: B:21:0x0079 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    com.mysql.jdbc.ResultSetImpl readAllResults(com.mysql.jdbc.StatementImpl r14, int r15, int r16, int r17, boolean r18, java.lang.String r19, com.mysql.jdbc.Buffer r20, boolean r21, long r22, com.mysql.jdbc.Field[] r24) throws java.sql.SQLException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 206
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.MysqlIO.readAllResults(com.mysql.jdbc.StatementImpl, int, int, int, boolean, java.lang.String, com.mysql.jdbc.Buffer, boolean, long, com.mysql.jdbc.Field[]):com.mysql.jdbc.ResultSetImpl");
    }

    void resetMaxBuf() {
        this.maxAllowedPacket = this.connection.getMaxAllowedPacket();
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    final com.mysql.jdbc.Buffer sendCommand(int r9, java.lang.String r10, com.mysql.jdbc.Buffer r11, boolean r12, java.lang.String r13, int r14) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 508
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.MysqlIO.sendCommand(int, java.lang.String, com.mysql.jdbc.Buffer, boolean, java.lang.String, int):com.mysql.jdbc.Buffer");
    }

    protected boolean shouldIntercept() {
        return this.statementInterceptors != null;
    }

    final ResultSetInternalMethods sqlQueryDirect(StatementImpl callingStatement, String query, String characterEncoding, Buffer queryPacket, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata) throws Exception {
        SQLException cause;
        String testcaseQuery;
        ResultSetInternalMethods interceptedResults;
        this.statementExecutionDepth++;
        try {
            try {
                if (this.statementInterceptors != null && (interceptedResults = invokeStatementInterceptorsPre(query, callingStatement, false)) != null) {
                    return interceptedResults;
                }
                String statementComment = this.connection.getStatementComment();
                if (this.connection.getIncludeThreadNamesAsStatementComment()) {
                    statementComment = (statementComment != null ? statementComment + ", " : "") + "java thread: " + Thread.currentThread().getName();
                }
                if (query != null) {
                    int packLength = 5 + (query.length() * 3) + 2;
                    byte[] commentAsBytes = null;
                    if (statementComment != null) {
                        commentAsBytes = StringUtils.getBytes(statementComment, (SingleByteCharsetConverter) null, characterEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
                        packLength = packLength + commentAsBytes.length + 6;
                    }
                    if (this.sendPacket == null) {
                        this.sendPacket = new Buffer(packLength);
                    } else {
                        this.sendPacket.clear();
                    }
                    this.sendPacket.writeByte((byte) 3);
                    if (commentAsBytes != null) {
                        this.sendPacket.writeBytesNoNull(Constants.SLASH_STAR_SPACE_AS_BYTES);
                        this.sendPacket.writeBytesNoNull(commentAsBytes);
                        this.sendPacket.writeBytesNoNull(Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES);
                    }
                    if (characterEncoding != null) {
                        if (!this.platformDbCharsetMatches && StringUtils.startsWithIgnoreCaseAndWs(query, "LOAD DATA")) {
                            this.sendPacket.writeBytesNoNull(StringUtils.getBytes(query));
                        } else {
                            this.sendPacket.writeStringNoNull(query, characterEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), this.connection);
                        }
                    } else {
                        this.sendPacket.writeStringNoNull(query);
                    }
                    queryPacket = this.sendPacket;
                }
                byte[] queryBuf = null;
                int oldPacketPosition = 0;
                long queryStartTime = 0;
                if (this.needToGrabQueryFromPacket) {
                    queryBuf = queryPacket.getByteBuffer();
                    oldPacketPosition = queryPacket.getPosition();
                    queryStartTime = getCurrentTimeNanosOrMillis();
                }
                if (this.autoGenerateTestcaseScript) {
                    if (query != null) {
                        if (statementComment != null) {
                            testcaseQuery = "/* " + statementComment + " */ " + query;
                        } else {
                            testcaseQuery = query;
                        }
                    } else {
                        testcaseQuery = StringUtils.toString(queryBuf, 5, oldPacketPosition - 5);
                    }
                    StringBuilder debugBuf = new StringBuilder(testcaseQuery.length() + 32);
                    this.connection.generateConnectionCommentBlock(debugBuf);
                    debugBuf.append(testcaseQuery);
                    debugBuf.append(';');
                    this.connection.dumpTestcaseQuery(debugBuf.toString());
                }
                Buffer resultPacket = sendCommand(3, null, queryPacket, false, null, 0);
                long fetchBeginTime = 0;
                String profileQueryToLog = null;
                boolean queryWasSlow = false;
                long queryEndTime = 0;
                if (this.profileSql || this.logSlowQueries) {
                    queryEndTime = getCurrentTimeNanosOrMillis();
                    boolean shouldExtractQuery = false;
                    if (this.profileSql) {
                        shouldExtractQuery = true;
                    } else if (this.logSlowQueries) {
                        long queryTime = queryEndTime - queryStartTime;
                        boolean logSlow = this.useAutoSlowLog ? this.connection.isAbonormallyLongQuery(queryTime) : queryTime > ((long) this.connection.getSlowQueryThresholdMillis());
                        if (logSlow) {
                            shouldExtractQuery = true;
                            queryWasSlow = true;
                        }
                    }
                    if (shouldExtractQuery) {
                        boolean truncated = false;
                        int extractPosition = oldPacketPosition;
                        if (oldPacketPosition > this.connection.getMaxQuerySizeToLog()) {
                            extractPosition = this.connection.getMaxQuerySizeToLog() + 5;
                            truncated = true;
                        }
                        profileQueryToLog = StringUtils.toString(queryBuf, 5, extractPosition - 5);
                        if (truncated) {
                            profileQueryToLog = profileQueryToLog + Messages.getString("MysqlIO.25");
                        }
                    }
                    fetchBeginTime = queryEndTime;
                }
                ResultSetInternalMethods rs = readAllResults(callingStatement, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, resultPacket, false, -1L, cachedMetadata);
                if (queryWasSlow && !this.serverQueryWasSlow) {
                    ProfilerEventHandler profilerEventHandlerInstance = this.connection.getProfilerEventHandlerInstance();
                    MySQLConnection mySQLConnection = this.connection;
                    long j = (int) (queryEndTime - queryStartTime);
                    Throwable th = new Throwable();
                    Object[] objArr = new Object[4];
                    objArr[0] = this.useAutoSlowLog ? " 95% of all queries " : String.valueOf(this.slowQueryThreshold);
                    objArr[1] = this.queryTimingUnits;
                    objArr[2] = Long.valueOf(queryEndTime - queryStartTime);
                    objArr[3] = profileQueryToLog;
                    profilerEventHandlerInstance.processEvent((byte) 6, mySQLConnection, callingStatement, rs, j, th, Messages.getString("Protocol.SlowQuery", objArr));
                    if (this.connection.getExplainSlowQueries()) {
                        if (oldPacketPosition < 1048576) {
                            explainSlowQuery(queryPacket.getBytes(5, oldPacketPosition - 5), profileQueryToLog);
                        } else {
                            this.connection.getLog().logWarn(Messages.getString("MysqlIO.28") + 1048576 + Messages.getString("MysqlIO.29"));
                        }
                    }
                }
                if (this.logSlowQueries) {
                    if (this.queryBadIndexUsed && this.profileSql) {
                        this.connection.getProfilerEventHandlerInstance().processEvent((byte) 6, this.connection, callingStatement, rs, queryEndTime - queryStartTime, new Throwable(), Messages.getString("MysqlIO.33") + profileQueryToLog);
                    }
                    if (this.queryNoIndexUsed && this.profileSql) {
                        this.connection.getProfilerEventHandlerInstance().processEvent((byte) 6, this.connection, callingStatement, rs, queryEndTime - queryStartTime, new Throwable(), Messages.getString("MysqlIO.35") + profileQueryToLog);
                    }
                    if (this.serverQueryWasSlow && this.profileSql) {
                        this.connection.getProfilerEventHandlerInstance().processEvent((byte) 6, this.connection, callingStatement, rs, queryEndTime - queryStartTime, new Throwable(), Messages.getString("MysqlIO.ServerSlowQuery") + profileQueryToLog);
                    }
                }
                if (this.profileSql) {
                    this.connection.getProfilerEventHandlerInstance().processEvent((byte) 3, this.connection, callingStatement, rs, queryEndTime - queryStartTime, new Throwable(), profileQueryToLog);
                    this.connection.getProfilerEventHandlerInstance().processEvent((byte) 5, this.connection, callingStatement, rs, getCurrentTimeNanosOrMillis() - fetchBeginTime, new Throwable(), null);
                }
                if (this.hadWarnings) {
                    scanForAndThrowDataTruncation();
                }
                if (this.statementInterceptors != null) {
                    rs = invokeStatementInterceptorsPost(query, callingStatement, rs, false, null);
                }
                return rs;
            } catch (SQLException sqlEx) {
                if (this.statementInterceptors != null) {
                    invokeStatementInterceptorsPost(query, callingStatement, null, false, sqlEx);
                }
                if (callingStatement != null) {
                    synchronized (callingStatement.cancelTimeoutMutex) {
                        if (callingStatement.wasCancelled) {
                            if (callingStatement.wasCancelledByTimeout) {
                                cause = new MySQLTimeoutException();
                            } else {
                                cause = new MySQLStatementCancelledException();
                            }
                            callingStatement.resetCancelledState();
                            throw cause;
                        }
                    }
                }
                throw sqlEx;
            }
        } finally {
            this.statementExecutionDepth--;
        }
    }

    ResultSetInternalMethods invokeStatementInterceptorsPre(String sql, Statement interceptedStatement, boolean forceExecute) throws SQLException {
        ResultSetInternalMethods interceptedResultSet;
        ResultSetInternalMethods previousResultSet = null;
        int s = this.statementInterceptors.size();
        for (int i = 0; i < s; i++) {
            StatementInterceptorV2 interceptor = this.statementInterceptors.get(i);
            boolean executeTopLevelOnly = interceptor.executeTopLevelOnly();
            boolean shouldExecute = (executeTopLevelOnly && (this.statementExecutionDepth == 1 || forceExecute)) || !executeTopLevelOnly;
            if (shouldExecute && (interceptedResultSet = interceptor.preProcess(sql, interceptedStatement, this.connection)) != null) {
                previousResultSet = interceptedResultSet;
            }
        }
        return previousResultSet;
    }

    ResultSetInternalMethods invokeStatementInterceptorsPost(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, boolean forceExecute, SQLException statementException) throws SQLException {
        ResultSetInternalMethods interceptedResultSet;
        int s = this.statementInterceptors.size();
        for (int i = 0; i < s; i++) {
            StatementInterceptorV2 interceptor = this.statementInterceptors.get(i);
            boolean executeTopLevelOnly = interceptor.executeTopLevelOnly();
            boolean shouldExecute = (executeTopLevelOnly && (this.statementExecutionDepth == 1 || forceExecute)) || !executeTopLevelOnly;
            if (shouldExecute && (interceptedResultSet = interceptor.postProcess(sql, interceptedStatement, originalResultSet, this.connection, this.warningCount, this.queryNoIndexUsed, this.queryBadIndexUsed, statementException)) != null) {
                originalResultSet = interceptedResultSet;
            }
        }
        return originalResultSet;
    }

    private void calculateSlowQueryThreshold() {
        this.slowQueryThreshold = this.connection.getSlowQueryThresholdMillis();
        if (this.connection.getUseNanosForElapsedTime()) {
            long nanosThreshold = this.connection.getSlowQueryThresholdNanos();
            if (nanosThreshold != 0) {
                this.slowQueryThreshold = nanosThreshold;
            } else {
                this.slowQueryThreshold *= Time.APR_USEC_PER_SEC;
            }
        }
    }

    protected long getCurrentTimeNanosOrMillis() {
        return this.useNanosForElapsedTime ? TimeUtil.getCurrentTimeNanosOrMillis() : System.currentTimeMillis();
    }

    String getHost() {
        return this.host;
    }

    boolean isVersion(int major, int minor, int subminor) {
        return major == getServerMajorVersion() && minor == getServerMinorVersion() && subminor == getServerSubMinorVersion();
    }

    boolean versionMeetsMinimum(int major, int minor, int subminor) {
        if (getServerMajorVersion() >= major) {
            if (getServerMajorVersion() == major) {
                if (getServerMinorVersion() >= minor) {
                    return getServerMinorVersion() != minor || getServerSubMinorVersion() >= subminor;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    private static final String getPacketDumpToLog(Buffer packetToDump, int packetLength) {
        if (packetLength < 1024) {
            return packetToDump.dump(packetLength);
        }
        StringBuilder packetDumpBuf = new StringBuilder(4096);
        packetDumpBuf.append(packetToDump.dump(1024));
        packetDumpBuf.append(Messages.getString("MysqlIO.36"));
        packetDumpBuf.append(1024);
        packetDumpBuf.append(Messages.getString("MysqlIO.37"));
        return packetDumpBuf.toString();
    }

    private final int readFully(InputStream in, byte[] b, int off, int len) throws IOException {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        int i = 0;
        while (true) {
            int n = i;
            if (n < len) {
                int count = in.read(b, off + n, len - n);
                if (count < 0) {
                    throw new EOFException(Messages.getString("MysqlIO.EOF", new Object[]{Integer.valueOf(len), Integer.valueOf(n)}));
                }
                i = n + count;
            } else {
                return n;
            }
        }
    }

    private final long skipFully(InputStream in, long len) throws IOException {
        if (len < 0) {
            throw new IOException("Negative skip length not allowed");
        }
        long j = 0;
        while (true) {
            long n = j;
            if (n < len) {
                long count = in.skip(len - n);
                if (count < 0) {
                    throw new EOFException(Messages.getString("MysqlIO.EOF", new Object[]{Long.valueOf(len), Long.valueOf(n)}));
                }
                j = n + count;
            } else {
                return n;
            }
        }
    }

    private final int skipLengthEncodedInteger(InputStream in) throws IOException {
        int sw = in.read() & 255;
        switch (sw) {
            case 252:
                return ((int) skipFully(in, 2L)) + 1;
            case 253:
                return ((int) skipFully(in, 3L)) + 1;
            case 254:
                return ((int) skipFully(in, 8L)) + 1;
            default:
                return 1;
        }
    }

    protected final ResultSetImpl readResultsForQueryOrUpdate(StatementImpl callingStatement, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Buffer resultPacket, boolean isBinaryEncoded, long preSentColumnCount, Field[] metadataFromCache) throws SQLException, IOException {
        String fileName;
        long columnCount = resultPacket.readFieldLength();
        if (columnCount == 0) {
            return buildResultSetWithUpdates(callingStatement, resultPacket);
        }
        if (columnCount == -1) {
            String charEncoding = null;
            if (this.connection.getUseUnicode()) {
                charEncoding = this.connection.getEncoding();
            }
            if (this.platformDbCharsetMatches) {
                fileName = charEncoding != null ? resultPacket.readString(charEncoding, getExceptionInterceptor()) : resultPacket.readString();
            } else {
                fileName = resultPacket.readString();
            }
            return sendFileToServer(callingStatement, fileName);
        }
        ResultSetImpl results = getResultSet(callingStatement, columnCount, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, isBinaryEncoded, metadataFromCache);
        return results;
    }

    private int alignPacketSize(int a, int l) {
        return ((a + l) - 1) & ((l - 1) ^ (-1));
    }

    private ResultSetImpl buildResultSetWithRows(StatementImpl callingStatement, String catalog, Field[] fields, RowData rows, int resultSetType, int resultSetConcurrency, boolean isBinaryEncoded) throws SQLException {
        ResultSetImpl rs;
        switch (resultSetConcurrency) {
            case 1007:
                rs = ResultSetImpl.getInstance(catalog, fields, rows, this.connection, callingStatement, false);
                if (isBinaryEncoded) {
                    rs.setBinaryEncoded();
                    break;
                }
                break;
            case 1008:
                rs = ResultSetImpl.getInstance(catalog, fields, rows, this.connection, callingStatement, true);
                break;
            default:
                return ResultSetImpl.getInstance(catalog, fields, rows, this.connection, callingStatement, false);
        }
        rs.setResultSetType(resultSetType);
        rs.setResultSetConcurrency(resultSetConcurrency);
        return rs;
    }

    private ResultSetImpl buildResultSetWithUpdates(StatementImpl callingStatement, Buffer resultPacket) throws SQLException {
        long updateCount;
        long updateID;
        String info = null;
        try {
            if (this.useNewUpdateCounts) {
                updateCount = resultPacket.newReadLength();
                updateID = resultPacket.newReadLength();
            } else {
                updateCount = resultPacket.readLength();
                updateID = resultPacket.readLength();
            }
            if (this.use41Extensions) {
                this.serverStatus = resultPacket.readInt();
                checkTransactionState(this.oldServerStatus);
                this.warningCount = resultPacket.readInt();
                if (this.warningCount > 0) {
                    this.hadWarnings = true;
                }
                resultPacket.readByte();
                setServerSlowQueryFlags();
            }
            if (this.connection.isReadInfoMsgEnabled()) {
                info = resultPacket.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
            }
            ResultSetInternalMethods updateRs = ResultSetImpl.getInstance(updateCount, updateID, this.connection, callingStatement);
            if (info != null) {
                ((ResultSetImpl) updateRs).setServerInfo(info);
            }
            return (ResultSetImpl) updateRs;
        } catch (Exception ex) {
            SQLException sqlEx = SQLError.createSQLException(SQLError.get(SQLError.SQL_STATE_GENERAL_ERROR), SQLError.SQL_STATE_GENERAL_ERROR, -1, getExceptionInterceptor());
            sqlEx.initCause(ex);
            throw sqlEx;
        }
    }

    private void setServerSlowQueryFlags() {
        this.queryBadIndexUsed = (this.serverStatus & 16) != 0;
        this.queryNoIndexUsed = (this.serverStatus & 32) != 0;
        this.serverQueryWasSlow = (this.serverStatus & 2048) != 0;
    }

    private void checkForOutstandingStreamingData() throws SQLException, IOException {
        if (this.streamingData != null) {
            boolean shouldClobber = this.connection.getClobberStreamingResults();
            if (!shouldClobber) {
                throw SQLError.createSQLException(Messages.getString("MysqlIO.39") + this.streamingData + Messages.getString("MysqlIO.40") + Messages.getString("MysqlIO.41") + Messages.getString("MysqlIO.42"), getExceptionInterceptor());
            }
            this.streamingData.getOwner().realClose(false);
            clearInputStream();
        }
    }

    private Buffer compressPacket(Buffer packet, int offset, int packetLen) throws SQLException {
        byte[] compressedBytes;
        int compressedLength = packetLen;
        int uncompressedLength = 0;
        int offsetWrite = offset;
        if (packetLen < 50) {
            compressedBytes = packet.getByteBuffer();
        } else {
            byte[] bytesToCompress = packet.getByteBuffer();
            compressedBytes = new byte[bytesToCompress.length * 2];
            if (this.deflater == null) {
                this.deflater = new Deflater();
            }
            this.deflater.reset();
            this.deflater.setInput(bytesToCompress, offset, packetLen);
            this.deflater.finish();
            compressedLength = this.deflater.deflate(compressedBytes);
            if (compressedLength > packetLen) {
                compressedBytes = packet.getByteBuffer();
                compressedLength = packetLen;
            } else {
                uncompressedLength = packetLen;
                offsetWrite = 0;
            }
        }
        Buffer compressedPacket = new Buffer(7 + compressedLength);
        compressedPacket.setPosition(0);
        compressedPacket.writeLongInt(compressedLength);
        compressedPacket.writeByte(this.compressedPacketSequence);
        compressedPacket.writeLongInt(uncompressedLength);
        compressedPacket.writeBytesNoNull(compressedBytes, offsetWrite, compressedLength);
        return compressedPacket;
    }

    private final void readServerStatusForResultSets(Buffer rowPacket) throws SQLException {
        if (this.use41Extensions) {
            rowPacket.readByte();
            if (isEOFDeprecated()) {
                rowPacket.newReadLength();
                rowPacket.newReadLength();
                this.oldServerStatus = this.serverStatus;
                this.serverStatus = rowPacket.readInt();
                checkTransactionState(this.oldServerStatus);
                this.warningCount = rowPacket.readInt();
                if (this.warningCount > 0) {
                    this.hadWarnings = true;
                }
                rowPacket.readByte();
                if (this.connection.isReadInfoMsgEnabled()) {
                    rowPacket.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
                }
            } else {
                this.warningCount = rowPacket.readInt();
                if (this.warningCount > 0) {
                    this.hadWarnings = true;
                }
                this.oldServerStatus = this.serverStatus;
                this.serverStatus = rowPacket.readInt();
                checkTransactionState(this.oldServerStatus);
            }
            setServerSlowQueryFlags();
        }
    }

    private SocketFactory createSocketFactory() throws SQLException {
        try {
            if (this.socketFactoryClassName == null) {
                throw SQLError.createSQLException(Messages.getString("MysqlIO.75"), SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE, getExceptionInterceptor());
            }
            return (SocketFactory) Class.forName(this.socketFactoryClassName).newInstance();
        } catch (Exception ex) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("MysqlIO.76") + this.socketFactoryClassName + Messages.getString("MysqlIO.77"), SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE, getExceptionInterceptor());
            sqlEx.initCause(ex);
            throw sqlEx;
        }
    }

    private void enqueuePacketForDebugging(boolean isPacketBeingSent, boolean isPacketReused, int sendLength, byte[] header, Buffer packet) throws SQLException {
        StringBuilder packetDump;
        if (this.packetDebugRingBuffer.size() + 1 > this.connection.getPacketDebugBufferSize()) {
            this.packetDebugRingBuffer.removeFirst();
        }
        if (!isPacketBeingSent) {
            int bytesToDump = Math.min(1024, packet.getBufLength());
            Buffer packetToDump = new Buffer(4 + bytesToDump);
            packetToDump.setPosition(0);
            packetToDump.writeBytesNoNull(header);
            packetToDump.writeBytesNoNull(packet.getBytes(0, bytesToDump));
            String packetPayload = packetToDump.dump(bytesToDump);
            packetDump = new StringBuilder(96 + packetPayload.length());
            packetDump.append("Server ");
            packetDump.append(isPacketReused ? "(re-used) " : "(new) ");
            packetDump.append(packet.toSuperString());
            packetDump.append(" --------------------> Client\n");
            packetDump.append("\nPacket payload:\n\n");
            packetDump.append(packetPayload);
            if (bytesToDump == 1024) {
                packetDump.append("\nNote: Packet of " + packet.getBufLength() + " bytes truncated to 1024 bytes.\n");
            }
        } else {
            int bytesToDump2 = Math.min(1024, sendLength);
            String packetPayload2 = packet.dump(bytesToDump2);
            packetDump = new StringBuilder(68 + packetPayload2.length());
            packetDump.append("Client ");
            packetDump.append(packet.toSuperString());
            packetDump.append("--------------------> Server\n");
            packetDump.append("\nPacket payload:\n\n");
            packetDump.append(packetPayload2);
            if (bytesToDump2 == 1024) {
                packetDump.append("\nNote: Packet of " + sendLength + " bytes truncated to 1024 bytes.\n");
            }
        }
        this.packetDebugRingBuffer.addLast(packetDump);
    }

    private RowData readSingleRowSet(long columnCount, int maxRows, int resultSetConcurrency, boolean isBinaryEncoded, Field[] fields) throws SQLException, IOException {
        ArrayList<ResultSetRow> rows = new ArrayList<>();
        boolean useBufferRowExplicit = useBufferRowExplicit(fields);
        ResultSetRow row = nextRow(fields, (int) columnCount, isBinaryEncoded, resultSetConcurrency, false, useBufferRowExplicit, false, null);
        int rowCount = 0;
        if (row != null) {
            rows.add(row);
            rowCount = 1;
        }
        while (row != null) {
            row = nextRow(fields, (int) columnCount, isBinaryEncoded, resultSetConcurrency, false, useBufferRowExplicit, false, null);
            if (row != null && (maxRows == -1 || rowCount < maxRows)) {
                rows.add(row);
                rowCount++;
            }
        }
        RowData rowData = new RowDataStatic(rows);
        return rowData;
    }

    public static boolean useBufferRowExplicit(Field[] fields) {
        if (fields == null) {
            return false;
        }
        for (Field field : fields) {
            switch (field.getSQLType()) {
                case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
                case -1:
                case 2004:
                case 2005:
                    return true;
                default:
            }
        }
        return false;
    }

    private void reclaimLargeReusablePacket() {
        if (this.reusablePacket != null && this.reusablePacket.getCapacity() > 1048576) {
            this.reusablePacket = new Buffer(1024);
        }
    }

    private final Buffer reuseAndReadPacket(Buffer reuse) throws SQLException {
        return reuseAndReadPacket(reuse, -1);
    }

    private final Buffer reuseAndReadPacket(Buffer reuse, int existingPacketLength) throws SQLException, IOException {
        int packetLength;
        try {
            reuse.setWasMultiPacket(false);
            if (existingPacketLength == -1) {
                int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
                if (lengthRead < 4) {
                    forceClose();
                    throw new IOException(Messages.getString("MysqlIO.43"));
                }
                packetLength = (this.packetHeaderBuf[0] & 255) + ((this.packetHeaderBuf[1] & 255) << 8) + ((this.packetHeaderBuf[2] & 255) << 16);
            } else {
                packetLength = existingPacketLength;
            }
            if (this.traceProtocol) {
                this.connection.getLog().logTrace(Messages.getString("MysqlIO.44") + packetLength + Messages.getString("MysqlIO.45") + StringUtils.dumpAsHex(this.packetHeaderBuf, 4));
            }
            byte multiPacketSeq = this.packetHeaderBuf[3];
            if (!this.packetSequenceReset) {
                if (this.enablePacketDebug && this.checkPacketSequence) {
                    checkPacketSequencing(multiPacketSeq);
                }
            } else {
                this.packetSequenceReset = false;
            }
            this.readPacketSequence = multiPacketSeq;
            reuse.setPosition(0);
            if (reuse.getByteBuffer().length <= packetLength) {
                reuse.setByteBuffer(new byte[packetLength + 1]);
            }
            reuse.setBufLength(packetLength);
            int numBytesRead = readFully(this.mysqlInput, reuse.getByteBuffer(), 0, packetLength);
            if (numBytesRead != packetLength) {
                throw new IOException("Short read, expected " + packetLength + " bytes, only read " + numBytesRead);
            }
            if (this.traceProtocol) {
                this.connection.getLog().logTrace(Messages.getString("MysqlIO.46") + getPacketDumpToLog(reuse, packetLength));
            }
            if (this.enablePacketDebug) {
                enqueuePacketForDebugging(false, true, 0, this.packetHeaderBuf, reuse);
            }
            boolean isMultiPacket = false;
            if (packetLength == this.maxThreeBytes) {
                reuse.setPosition(this.maxThreeBytes);
                isMultiPacket = true;
                packetLength = readRemainingMultiPackets(reuse, multiPacketSeq);
            }
            if (!isMultiPacket) {
                reuse.getByteBuffer()[packetLength] = 0;
            }
            if (this.connection.getMaintainTimeStats()) {
                this.lastPacketReceivedTimeMs = System.currentTimeMillis();
            }
            return reuse;
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        } catch (OutOfMemoryError oom) {
            try {
                clearInputStream();
            } catch (Exception e) {
            }
            try {
                this.connection.realClose(false, false, true, oom);
            } catch (Exception e2) {
            }
            throw oom;
        }
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x0018 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int readRemainingMultiPackets(com.mysql.jdbc.Buffer r9, byte r10) throws java.sql.SQLException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 290
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.MysqlIO.readRemainingMultiPackets(com.mysql.jdbc.Buffer, byte):int");
    }

    private void checkPacketSequencing(byte multiPacketSeq) throws SQLException {
        if (multiPacketSeq == Byte.MIN_VALUE && this.readPacketSequence != Byte.MAX_VALUE) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException("Packets out of order, expected packet # -128, but received packet # " + ((int) multiPacketSeq)), getExceptionInterceptor());
        }
        if (this.readPacketSequence == -1 && multiPacketSeq != 0) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException("Packets out of order, expected packet # -1, but received packet # " + ((int) multiPacketSeq)), getExceptionInterceptor());
        }
        if (multiPacketSeq != Byte.MIN_VALUE && this.readPacketSequence != -1 && multiPacketSeq != this.readPacketSequence + 1) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException("Packets out of order, expected packet # " + (this.readPacketSequence + 1) + ", but received packet # " + ((int) multiPacketSeq)), getExceptionInterceptor());
        }
    }

    void enableMultiQueries() throws SQLException {
        Buffer buf = getSharedSendPacket();
        buf.clear();
        buf.writeByte((byte) 27);
        buf.writeInt(0);
        sendCommand(27, null, buf, false, null, 0);
        preserveOldTransactionState();
    }

    void disableMultiQueries() throws SQLException {
        Buffer buf = getSharedSendPacket();
        buf.clear();
        buf.writeByte((byte) 27);
        buf.writeInt(1);
        sendCommand(27, null, buf, false, null, 0);
        preserveOldTransactionState();
    }

    private final void send(Buffer packet, int packetLen) throws SQLException, IOException {
        try {
            if (this.maxAllowedPacket > 0 && packetLen > this.maxAllowedPacket) {
                throw new PacketTooBigException(packetLen, this.maxAllowedPacket);
            }
            if (this.serverMajorVersion >= 4 && (packetLen - 4 >= this.maxThreeBytes || (this.useCompression && packetLen - 4 >= this.maxThreeBytes - 3))) {
                sendSplitPackets(packet, packetLen);
            } else {
                this.packetSequence = (byte) (this.packetSequence + 1);
                Buffer packetToSend = packet;
                packetToSend.setPosition(0);
                packetToSend.writeLongInt(packetLen - 4);
                packetToSend.writeByte(this.packetSequence);
                if (this.useCompression) {
                    this.compressedPacketSequence = (byte) (this.compressedPacketSequence + 1);
                    packetToSend = compressPacket(packetToSend, 0, packetLen);
                    packetLen = packetToSend.getPosition();
                    if (this.traceProtocol) {
                        this.connection.getLog().logTrace(Messages.getString("MysqlIO.57") + getPacketDumpToLog(packetToSend, packetLen) + Messages.getString("MysqlIO.58") + getPacketDumpToLog(packet, packetLen));
                    }
                } else if (this.traceProtocol) {
                    this.connection.getLog().logTrace(Messages.getString("MysqlIO.59") + "host: '" + this.host + "' threadId: '" + this.threadId + "'\n" + packetToSend.dump(packetLen));
                }
                this.mysqlOutput.write(packetToSend.getByteBuffer(), 0, packetLen);
                this.mysqlOutput.flush();
            }
            if (this.enablePacketDebug) {
                enqueuePacketForDebugging(true, false, packetLen + 5, this.packetHeaderBuf, packet);
            }
            if (packet == this.sharedSendPacket) {
                reclaimLargeSharedSendPacket();
            }
            if (this.connection.getMaintainTimeStats()) {
                this.lastPacketSentTimeMs = System.currentTimeMillis();
            }
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:55:0x022e, code lost:
    
        throw r18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0236, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x025d, code lost:
    
        r9.clear();
        send(r9, r9.getPosition());
        checkErrorPacket();
     */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0236 A[Catch: Exception -> 0x023e, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Exception -> 0x023e, blocks: (B:58:0x0236, B:69:0x0236, B:16:0x00d5, B:18:0x00e1, B:19:0x00f1, B:22:0x00f9, B:25:0x0104, B:36:0x0181, B:38:0x018f, B:26:0x0112, B:28:0x011e, B:29:0x0132, B:31:0x013c, B:34:0x016d, B:33:0x0159, B:42:0x01b0, B:44:0x01c3, B:46:0x01cf, B:47:0x01e8, B:49:0x0200, B:50:0x0217, B:51:0x0226), top: B:75:0x00d5, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x025d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final com.mysql.jdbc.ResultSetImpl sendFileToServer(com.mysql.jdbc.StatementImpl r7, java.lang.String r8) throws java.sql.SQLException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 652
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.MysqlIO.sendFileToServer(com.mysql.jdbc.StatementImpl, java.lang.String):com.mysql.jdbc.ResultSetImpl");
    }

    private Buffer checkErrorPacket(int command) throws SQLException, IOException {
        this.serverStatus = 0;
        try {
            Buffer resultPacket = reuseAndReadPacket(this.reusablePacket);
            checkErrorPacket(resultPacket);
            return resultPacket;
        } catch (SQLException sqlEx) {
            throw sqlEx;
        } catch (Exception fallThru) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, fallThru, getExceptionInterceptor());
        }
    }

    private void checkErrorPacket(Buffer resultPacket) throws SQLException, IOException {
        String xOpen;
        int statusCode = resultPacket.readByte();
        if (statusCode == -1) {
            if (this.protocolVersion > 9) {
                int errno = resultPacket.readInt();
                String serverErrorMessage = resultPacket.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
                if (serverErrorMessage.charAt(0) == '#' && serverErrorMessage.length() > 6) {
                    xOpen = serverErrorMessage.substring(1, 6);
                    serverErrorMessage = serverErrorMessage.substring(6);
                    if (xOpen.equals(SQLError.SQL_STATE_CLI_SPECIFIC_CONDITION)) {
                        xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
                    }
                } else {
                    xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
                }
                clearInputStream();
                StringBuilder errorBuf = new StringBuilder();
                String xOpenErrorMessage = SQLError.get(xOpen);
                if (!this.connection.getUseOnlyServerErrorMessages() && xOpenErrorMessage != null) {
                    errorBuf.append(xOpenErrorMessage);
                    errorBuf.append(Messages.getString("MysqlIO.68"));
                }
                errorBuf.append(serverErrorMessage);
                if (!this.connection.getUseOnlyServerErrorMessages() && xOpenErrorMessage != null) {
                    errorBuf.append(SymbolConstants.QUOTES_SYMBOL);
                }
                appendDeadlockStatusInformation(xOpen, errorBuf);
                if (xOpen != null && xOpen.startsWith("22")) {
                    throw new MysqlDataTruncation(errorBuf.toString(), 0, true, false, 0, 0, errno);
                }
                throw SQLError.createSQLException(errorBuf.toString(), xOpen, errno, false, getExceptionInterceptor(), this.connection);
            }
            String serverErrorMessage2 = resultPacket.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
            clearInputStream();
            if (serverErrorMessage2.indexOf(Messages.getString("MysqlIO.70")) != -1) {
                throw SQLError.createSQLException(SQLError.get(SQLError.SQL_STATE_COLUMN_NOT_FOUND) + ", " + serverErrorMessage2, SQLError.SQL_STATE_COLUMN_NOT_FOUND, -1, false, getExceptionInterceptor(), this.connection);
            }
            throw SQLError.createSQLException(SQLError.get(SQLError.SQL_STATE_GENERAL_ERROR) + ", " + (Messages.getString("MysqlIO.72") + serverErrorMessage2 + SymbolConstants.QUOTES_SYMBOL), SQLError.SQL_STATE_GENERAL_ERROR, -1, false, getExceptionInterceptor(), this.connection);
        }
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    private void appendDeadlockStatusInformation(java.lang.String r13, java.lang.StringBuilder r14) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 652
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.MysqlIO.appendDeadlockStatusInformation(java.lang.String, java.lang.StringBuilder):void");
    }

    private final void sendSplitPackets(Buffer packet, int packetLen) throws SQLException, IOException {
        try {
            Buffer packetToSend = this.splitBufRef == null ? null : this.splitBufRef.get();
            Buffer toCompress = (!this.useCompression || this.compressBufRef == null) ? null : this.compressBufRef.get();
            if (packetToSend == null) {
                packetToSend = new Buffer(this.maxThreeBytes + 4);
                this.splitBufRef = new SoftReference<>(packetToSend);
            }
            if (this.useCompression) {
                int cbuflen = packetLen + (((packetLen / this.maxThreeBytes) + 1) * 4);
                if (toCompress == null) {
                    toCompress = new Buffer(cbuflen);
                    this.compressBufRef = new SoftReference<>(toCompress);
                } else if (toCompress.getBufLength() < cbuflen) {
                    toCompress.setPosition(toCompress.getBufLength());
                    toCompress.ensureCapacity(cbuflen - toCompress.getBufLength());
                }
            }
            int len = packetLen - 4;
            int splitSize = this.maxThreeBytes;
            int originalPacketPos = 4;
            byte[] origPacketBytes = packet.getByteBuffer();
            int toCompressPosition = 0;
            while (len >= 0) {
                this.packetSequence = (byte) (this.packetSequence + 1);
                if (len < splitSize) {
                    splitSize = len;
                }
                packetToSend.setPosition(0);
                packetToSend.writeLongInt(splitSize);
                packetToSend.writeByte(this.packetSequence);
                if (len > 0) {
                    System.arraycopy(origPacketBytes, originalPacketPos, packetToSend.getByteBuffer(), 4, splitSize);
                }
                if (this.useCompression) {
                    System.arraycopy(packetToSend.getByteBuffer(), 0, toCompress.getByteBuffer(), toCompressPosition, 4 + splitSize);
                    toCompressPosition += 4 + splitSize;
                } else {
                    this.mysqlOutput.write(packetToSend.getByteBuffer(), 0, 4 + splitSize);
                    this.mysqlOutput.flush();
                }
                originalPacketPos += splitSize;
                len -= this.maxThreeBytes;
            }
            if (this.useCompression) {
                int len2 = toCompressPosition;
                int toCompressPosition2 = 0;
                int splitSize2 = this.maxThreeBytes - 3;
                while (len2 >= 0) {
                    this.compressedPacketSequence = (byte) (this.compressedPacketSequence + 1);
                    if (len2 < splitSize2) {
                        splitSize2 = len2;
                    }
                    Buffer compressedPacketToSend = compressPacket(toCompress, toCompressPosition2, splitSize2);
                    this.mysqlOutput.write(compressedPacketToSend.getByteBuffer(), 0, compressedPacketToSend.getPosition());
                    this.mysqlOutput.flush();
                    toCompressPosition2 += splitSize2;
                    len2 -= this.maxThreeBytes - 3;
                }
            }
        } catch (IOException ioEx) {
            throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
        }
    }

    private void reclaimLargeSharedSendPacket() {
        if (this.sharedSendPacket != null && this.sharedSendPacket.getCapacity() > 1048576) {
            this.sharedSendPacket = new Buffer(1024);
        }
    }

    boolean hadWarnings() {
        return this.hadWarnings;
    }

    void scanForAndThrowDataTruncation() throws SQLException {
        if (this.streamingData == null && versionMeetsMinimum(4, 1, 0) && this.connection.getJdbcCompliantTruncation() && this.warningCount > 0) {
            int warningCountOld = this.warningCount;
            SQLError.convertShowWarningsToSQLWarnings(this.connection, this.warningCount, true);
            this.warningCount = warningCountOld;
        }
    }

    private void secureAuth(Buffer packet, int packLength, String user, String password, String database, boolean writeClientParams) throws SQLException, IOException {
        if (packet == null) {
            packet = new Buffer(packLength);
        }
        if (writeClientParams) {
            if (this.use41Extensions) {
                if (versionMeetsMinimum(4, 1, 1)) {
                    packet.writeLong(this.clientParam);
                    packet.writeLong(this.maxThreeBytes);
                    packet.writeByte((byte) 8);
                    packet.writeBytesNoNull(new byte[23]);
                } else {
                    packet.writeLong(this.clientParam);
                    packet.writeLong(this.maxThreeBytes);
                }
            } else {
                packet.writeInt((int) this.clientParam);
                packet.writeLongInt(this.maxThreeBytes);
            }
        }
        packet.writeString(user, "Cp1252", this.connection);
        if (password.length() != 0) {
            packet.writeString(FALSE_SCRAMBLE, "Cp1252", this.connection);
        } else {
            packet.writeString("", "Cp1252", this.connection);
        }
        if (this.useConnectWithDb) {
            packet.writeString(database, "Cp1252", this.connection);
        }
        send(packet, packet.getPosition());
        if (password.length() > 0) {
            Buffer b = readPacket();
            b.setPosition(0);
            byte[] replyAsBytes = b.getByteBuffer();
            if (replyAsBytes.length == 24 && replyAsBytes[0] != 0) {
                if (replyAsBytes[0] != 42) {
                    try {
                        byte[] buff = Security.passwordHashStage1(password);
                        byte[] passwordHash = new byte[buff.length];
                        System.arraycopy(buff, 0, passwordHash, 0, buff.length);
                        byte[] passwordHash2 = Security.passwordHashStage2(passwordHash, replyAsBytes);
                        byte[] packetDataAfterSalt = new byte[replyAsBytes.length - 4];
                        System.arraycopy(replyAsBytes, 4, packetDataAfterSalt, 0, replyAsBytes.length - 4);
                        byte[] mysqlScrambleBuff = new byte[20];
                        Security.xorString(packetDataAfterSalt, mysqlScrambleBuff, passwordHash2, 20);
                        Security.xorString(mysqlScrambleBuff, buff, buff, 20);
                        Buffer packet2 = new Buffer(25);
                        packet2.writeBytesNoNull(buff);
                        this.packetSequence = (byte) (this.packetSequence + 1);
                        send(packet2, 24);
                        return;
                    } catch (NoSuchAlgorithmException e) {
                        throw SQLError.createSQLException(Messages.getString("MysqlIO.91") + Messages.getString("MysqlIO.92"), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
                    }
                }
                try {
                    byte[] passwordHash3 = Security.createKeyFromOldPassword(password);
                    byte[] netReadPos4 = new byte[replyAsBytes.length - 4];
                    System.arraycopy(replyAsBytes, 4, netReadPos4, 0, replyAsBytes.length - 4);
                    byte[] mysqlScrambleBuff2 = new byte[20];
                    Security.xorString(netReadPos4, mysqlScrambleBuff2, passwordHash3, 20);
                    String scrambledPassword = Util.scramble(StringUtils.toString(mysqlScrambleBuff2), password);
                    Buffer packet22 = new Buffer(packLength);
                    packet22.writeString(scrambledPassword, "Cp1252", this.connection);
                    this.packetSequence = (byte) (this.packetSequence + 1);
                    send(packet22, 24);
                } catch (NoSuchAlgorithmException e2) {
                    throw SQLError.createSQLException(Messages.getString("MysqlIO.91") + Messages.getString("MysqlIO.92"), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
                }
            }
        }
    }

    void secureAuth411(Buffer packet, int packLength, String user, String password, String database, boolean writeClientParams, boolean forChangeUser) throws SQLException, IOException {
        String enc = getEncodingForHandshake();
        if (packet == null) {
            packet = new Buffer(packLength);
        }
        if (writeClientParams) {
            if (this.use41Extensions) {
                if (versionMeetsMinimum(4, 1, 1)) {
                    packet.writeLong(this.clientParam);
                    packet.writeLong(this.maxThreeBytes);
                    appendCharsetByteForHandshake(packet, enc);
                    packet.writeBytesNoNull(new byte[23]);
                } else {
                    packet.writeLong(this.clientParam);
                    packet.writeLong(this.maxThreeBytes);
                }
            } else {
                packet.writeInt((int) this.clientParam);
                packet.writeLongInt(this.maxThreeBytes);
            }
        }
        if (user != null) {
            packet.writeString(user, enc, this.connection);
        }
        if (password.length() != 0) {
            packet.writeByte((byte) 20);
            try {
                packet.writeBytesNoNull(Security.scramble411(password, this.seed, this.connection.getPasswordCharacterEncoding()));
            } catch (UnsupportedEncodingException e) {
                throw SQLError.createSQLException(Messages.getString("MysqlIO.91") + Messages.getString("MysqlIO.92"), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            } catch (NoSuchAlgorithmException e2) {
                throw SQLError.createSQLException(Messages.getString("MysqlIO.91") + Messages.getString("MysqlIO.92"), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            }
        } else {
            packet.writeByte((byte) 0);
        }
        if (this.useConnectWithDb) {
            packet.writeString(database, enc, this.connection);
        } else if (forChangeUser) {
            packet.writeByte((byte) 0);
        }
        if ((this.serverCapabilities & 1048576) != 0) {
            sendConnectionAttributes(packet, enc, this.connection);
        }
        send(packet, packet.getPosition());
        byte savePacketSequence = this.packetSequence;
        this.packetSequence = (byte) (savePacketSequence + 1);
        Buffer reply = checkErrorPacket();
        if (reply.isAuthMethodSwitchRequestPacket()) {
            this.packetSequence = (byte) (savePacketSequence + 1);
            packet.clear();
            String seed323 = this.seed.substring(0, 8);
            packet.writeString(Util.newCrypt(password, seed323, this.connection.getPasswordCharacterEncoding()));
            send(packet, packet.getPosition());
            checkErrorPacket();
        }
        if (!this.useConnectWithDb) {
            changeDatabaseTo(database);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    private final ResultSetRow unpackBinaryResultSetRow(Field[] fields, Buffer binaryData, int resultSetConcurrency) throws SQLException {
        int numFields = fields.length;
        ?? r0 = new byte[numFields];
        int nullCount = (numFields + 9) / 8;
        int nullMaskPos = binaryData.getPosition();
        binaryData.setPosition(nullMaskPos + nullCount);
        int bit = 4;
        for (int i = 0; i < numFields; i++) {
            if ((binaryData.readByte(nullMaskPos) & bit) != 0) {
                r0[i] = 0;
            } else if (resultSetConcurrency != 1008) {
                extractNativeEncodedColumn(binaryData, fields, i, r0);
            } else {
                unpackNativeEncodedColumn(binaryData, fields, i, r0);
            }
            int i2 = bit << 1;
            bit = i2;
            if ((i2 & 255) == 0) {
                bit = 1;
                nullMaskPos++;
            }
        }
        return new ByteArrayRow(r0, getExceptionInterceptor());
    }

    private final void extractNativeEncodedColumn(Buffer binaryData, Field[] fields, int columnIndex, byte[][] unpackedRowData) throws SQLException {
        Field curField = fields[columnIndex];
        switch (curField.getMysqlType()) {
            case 0:
            case 15:
            case 16:
            case EscherProperties.GEOTEXT__STRETCHTOFITSHAPE /* 245 */:
            case EscherProperties.GEOTEXT__CHARBOUNDINGBOX /* 246 */:
            case EscherProperties.GEOTEXT__NOMEASUREALONGPATH /* 249 */:
            case EscherProperties.GEOTEXT__BOLDFONT /* 250 */:
            case 251:
            case 252:
            case 253:
            case 254:
            case 255:
                unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
                return;
            case 1:
                unpackedRowData[columnIndex] = new byte[]{binaryData.readByte()};
                return;
            case 2:
            case 13:
                unpackedRowData[columnIndex] = binaryData.getBytes(2);
                return;
            case 3:
            case 9:
                unpackedRowData[columnIndex] = binaryData.getBytes(4);
                return;
            case 4:
                unpackedRowData[columnIndex] = binaryData.getBytes(4);
                return;
            case 5:
                unpackedRowData[columnIndex] = binaryData.getBytes(8);
                return;
            case 6:
                return;
            case 7:
            case 12:
                int length = (int) binaryData.readFieldLength();
                unpackedRowData[columnIndex] = binaryData.getBytes(length);
                return;
            case 8:
                unpackedRowData[columnIndex] = binaryData.getBytes(8);
                return;
            case 10:
                int length2 = (int) binaryData.readFieldLength();
                unpackedRowData[columnIndex] = binaryData.getBytes(length2);
                return;
            case 11:
                int length3 = (int) binaryData.readFieldLength();
                unpackedRowData[columnIndex] = binaryData.getBytes(length3);
                return;
            default:
                throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + curField.getMysqlType() + Messages.getString("MysqlIO.98") + columnIndex + Messages.getString("MysqlIO.99") + fields.length + Messages.getString("MysqlIO.100"), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
        }
    }

    private final void unpackNativeEncodedColumn(Buffer binaryData, Field[] fields, int columnIndex, byte[][] unpackedRowData) throws SQLException {
        Field curField = fields[columnIndex];
        switch (curField.getMysqlType()) {
            case 0:
            case 15:
            case 16:
            case EscherProperties.GEOTEXT__STRETCHTOFITSHAPE /* 245 */:
            case EscherProperties.GEOTEXT__CHARBOUNDINGBOX /* 246 */:
            case EscherProperties.GEOTEXT__NOMEASUREALONGPATH /* 249 */:
            case EscherProperties.GEOTEXT__BOLDFONT /* 250 */:
            case 251:
            case 252:
            case 253:
            case 254:
                unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
                return;
            case 1:
                byte tinyVal = binaryData.readByte();
                if (!curField.isUnsigned()) {
                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf((int) tinyVal));
                    return;
                } else {
                    short unsignedTinyVal = (short) (tinyVal & 255);
                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf((int) unsignedTinyVal));
                    return;
                }
            case 2:
            case 13:
                short shortVal = (short) binaryData.readInt();
                if (!curField.isUnsigned()) {
                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf((int) shortVal));
                    return;
                } else {
                    int unsignedShortVal = shortVal & 65535;
                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(unsignedShortVal));
                    return;
                }
            case 3:
            case 9:
                int intVal = (int) binaryData.readLong();
                if (!curField.isUnsigned()) {
                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(intVal));
                    return;
                } else {
                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(intVal & 4294967295L));
                    return;
                }
            case 4:
                float floatVal = Float.intBitsToFloat(binaryData.readIntAsLong());
                unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(floatVal));
                return;
            case 5:
                double doubleVal = Double.longBitsToDouble(binaryData.readLongLong());
                unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(doubleVal));
                return;
            case 6:
                return;
            case 7:
            case 12:
                int length = (int) binaryData.readFieldLength();
                int year = 0;
                int month = 0;
                int day = 0;
                int hour = 0;
                int minute = 0;
                int seconds = 0;
                if (length != 0) {
                    year = binaryData.readInt();
                    month = binaryData.readByte();
                    day = binaryData.readByte();
                    if (length > 4) {
                        hour = binaryData.readByte();
                        minute = binaryData.readByte();
                        seconds = binaryData.readByte();
                    }
                }
                if (year == 0 && month == 0 && day == 0) {
                    if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior())) {
                        unpackedRowData[columnIndex] = null;
                        return;
                    } else {
                        if (SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE.equals(this.connection.getZeroDateTimeBehavior())) {
                            throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                        }
                        year = 1;
                        month = 1;
                        day = 1;
                    }
                }
                byte[] nanosAsBytes = StringUtils.getBytes(Integer.toString(0));
                int stringLength = 19 + 1 + nanosAsBytes.length;
                byte[] datetimeAsBytes = new byte[stringLength];
                datetimeAsBytes[0] = (byte) Character.forDigit(year / 1000, 10);
                int after1000 = year % 1000;
                datetimeAsBytes[1] = (byte) Character.forDigit(after1000 / 100, 10);
                int after100 = after1000 % 100;
                datetimeAsBytes[2] = (byte) Character.forDigit(after100 / 10, 10);
                datetimeAsBytes[3] = (byte) Character.forDigit(after100 % 10, 10);
                datetimeAsBytes[4] = 45;
                datetimeAsBytes[5] = (byte) Character.forDigit(month / 10, 10);
                datetimeAsBytes[6] = (byte) Character.forDigit(month % 10, 10);
                datetimeAsBytes[7] = 45;
                datetimeAsBytes[8] = (byte) Character.forDigit(day / 10, 10);
                datetimeAsBytes[9] = (byte) Character.forDigit(day % 10, 10);
                datetimeAsBytes[10] = 32;
                datetimeAsBytes[11] = (byte) Character.forDigit(hour / 10, 10);
                datetimeAsBytes[12] = (byte) Character.forDigit(hour % 10, 10);
                datetimeAsBytes[13] = 58;
                datetimeAsBytes[14] = (byte) Character.forDigit(minute / 10, 10);
                datetimeAsBytes[15] = (byte) Character.forDigit(minute % 10, 10);
                datetimeAsBytes[16] = 58;
                datetimeAsBytes[17] = (byte) Character.forDigit(seconds / 10, 10);
                datetimeAsBytes[18] = (byte) Character.forDigit(seconds % 10, 10);
                datetimeAsBytes[19] = 46;
                System.arraycopy(nanosAsBytes, 0, datetimeAsBytes, 20, nanosAsBytes.length);
                unpackedRowData[columnIndex] = datetimeAsBytes;
                return;
            case 8:
                long longVal = binaryData.readLongLong();
                if (!curField.isUnsigned()) {
                    unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(longVal));
                    return;
                } else {
                    BigInteger asBigInteger = ResultSetImpl.convertLongToUlong(longVal);
                    unpackedRowData[columnIndex] = StringUtils.getBytes(asBigInteger.toString());
                    return;
                }
            case 10:
                int year2 = 0;
                int month2 = 0;
                int day2 = 0;
                if (((int) binaryData.readFieldLength()) != 0) {
                    year2 = binaryData.readInt();
                    month2 = binaryData.readByte();
                    day2 = binaryData.readByte();
                }
                if (year2 == 0 && month2 == 0 && day2 == 0) {
                    if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior())) {
                        unpackedRowData[columnIndex] = null;
                        return;
                    } else {
                        if (SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE.equals(this.connection.getZeroDateTimeBehavior())) {
                            throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Date", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                        }
                        year2 = 1;
                        month2 = 1;
                        day2 = 1;
                    }
                }
                int after10002 = year2 % 1000;
                int after1002 = after10002 % 100;
                byte[] dateAsBytes = {(byte) Character.forDigit(year2 / 1000, 10), (byte) Character.forDigit(after10002 / 100, 10), (byte) Character.forDigit(after1002 / 10, 10), (byte) Character.forDigit(after1002 % 10, 10), 45, (byte) Character.forDigit(month2 / 10, 10), (byte) Character.forDigit(month2 % 10, 10), 45, (byte) Character.forDigit(day2 / 10, 10), (byte) Character.forDigit(day2 % 10, 10)};
                unpackedRowData[columnIndex] = dateAsBytes;
                return;
            case 11:
                int length2 = (int) binaryData.readFieldLength();
                int hour2 = 0;
                int minute2 = 0;
                int seconds2 = 0;
                if (length2 != 0) {
                    binaryData.readByte();
                    binaryData.readLong();
                    hour2 = binaryData.readByte();
                    minute2 = binaryData.readByte();
                    seconds2 = binaryData.readByte();
                    if (length2 > 8) {
                        binaryData.readLong();
                    }
                }
                byte[] timeAsBytes = {(byte) Character.forDigit(hour2 / 10, 10), (byte) Character.forDigit(hour2 % 10, 10), 58, (byte) Character.forDigit(minute2 / 10, 10), (byte) Character.forDigit(minute2 % 10, 10), 58, (byte) Character.forDigit(seconds2 / 10, 10), (byte) Character.forDigit(seconds2 % 10, 10)};
                unpackedRowData[columnIndex] = timeAsBytes;
                return;
            default:
                throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + curField.getMysqlType() + Messages.getString("MysqlIO.98") + columnIndex + Messages.getString("MysqlIO.99") + fields.length + Messages.getString("MysqlIO.100"), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
        }
    }

    private void negotiateSSLConnection(String user, String password, String database, int packLength) throws SQLException, IOException {
        if (!ExportControlled.enabled()) {
            throw new ConnectionFeatureNotAvailableException(this.connection, this.lastPacketSentTimeMs, null);
        }
        if ((this.serverCapabilities & 32768) != 0) {
            this.clientParam |= org.aspectj.apache.bcel.Constants.RET_INST;
        }
        this.clientParam |= 2048;
        Buffer packet = new Buffer(packLength);
        if (this.use41Extensions) {
            packet.writeLong(this.clientParam);
            packet.writeLong(this.maxThreeBytes);
            appendCharsetByteForHandshake(packet, getEncodingForHandshake());
            packet.writeBytesNoNull(new byte[23]);
        } else {
            packet.writeInt((int) this.clientParam);
        }
        send(packet, packet.getPosition());
        ExportControlled.transformSocketToSSLSocket(this);
    }

    public boolean isSSLEstablished() {
        return ExportControlled.enabled() && ExportControlled.isSSLEstablished(this.mysqlConnection);
    }

    protected int getServerStatus() {
        return this.serverStatus;
    }

    protected List<ResultSetRow> fetchRowsViaCursor(List<ResultSetRow> fetchedRows, long statementId, Field[] columnTypes, int fetchSize, boolean useBufferRowExplicit) throws SQLException, IOException {
        if (fetchedRows == null) {
            fetchedRows = new ArrayList(fetchSize);
        } else {
            fetchedRows.clear();
        }
        this.sharedSendPacket.clear();
        this.sharedSendPacket.writeByte((byte) 28);
        this.sharedSendPacket.writeLong(statementId);
        this.sharedSendPacket.writeLong(fetchSize);
        sendCommand(28, null, this.sharedSendPacket, true, null, 0);
        while (true) {
            ResultSetRow row = nextRow(columnTypes, columnTypes.length, true, 1007, false, useBufferRowExplicit, false, null);
            if (row != null) {
                fetchedRows.add(row);
            } else {
                return fetchedRows;
            }
        }
    }

    protected long getThreadId() {
        return this.threadId;
    }

    protected boolean useNanosForElapsedTime() {
        return this.useNanosForElapsedTime;
    }

    protected long getSlowQueryThreshold() {
        return this.slowQueryThreshold;
    }

    public String getQueryTimingUnits() {
        return this.queryTimingUnits;
    }

    protected int getCommandCount() {
        return this.commandCount;
    }

    private void checkTransactionState(int oldStatus) throws SQLException {
        boolean previouslyInTrans = (oldStatus & 1) != 0;
        boolean currentlyInTrans = inTransactionOnServer();
        if (previouslyInTrans && !currentlyInTrans) {
            this.connection.transactionCompleted();
        } else if (!previouslyInTrans && currentlyInTrans) {
            this.connection.transactionBegun();
        }
    }

    private void preserveOldTransactionState() {
        this.serverStatus |= this.oldServerStatus & 1;
    }

    protected void setStatementInterceptors(List<StatementInterceptorV2> statementInterceptors) {
        this.statementInterceptors = statementInterceptors.isEmpty() ? null : statementInterceptors;
    }

    protected ExceptionInterceptor getExceptionInterceptor() {
        return this.exceptionInterceptor;
    }

    protected void setSocketTimeout(int milliseconds) throws SocketException, SQLException {
        try {
            if (this.mysqlConnection != null) {
                this.mysqlConnection.setSoTimeout(milliseconds);
            }
        } catch (SocketException e) {
            SQLException sqlEx = SQLError.createSQLException("Invalid socket timeout value or state", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            sqlEx.initCause(e);
            throw sqlEx;
        }
    }

    protected void releaseResources() {
        if (this.deflater != null) {
            this.deflater.end();
            this.deflater = null;
        }
    }

    String getEncodingForHandshake() {
        String enc = this.connection.getEncoding();
        if (enc == null) {
            enc = "UTF-8";
        }
        return enc;
    }

    private void appendCharsetByteForHandshake(Buffer packet, String enc) throws SQLException {
        int charsetIndex = 0;
        if (enc != null) {
            charsetIndex = CharsetMapping.getCollationIndexForJavaEncoding(enc, this.connection);
        }
        if (charsetIndex == 0) {
            charsetIndex = 33;
        }
        if (charsetIndex > 255) {
            throw SQLError.createSQLException("Invalid character set index for encoding: " + enc, SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
        packet.writeByte((byte) charsetIndex);
    }

    public boolean isEOFDeprecated() {
        return (this.clientParam & 16777216) != 0;
    }
}
