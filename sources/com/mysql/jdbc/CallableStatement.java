package com.mysql.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.aop.framework.autoproxy.target.QuickTargetSourceCreator;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/CallableStatement.class */
public class CallableStatement extends PreparedStatement implements java.sql.CallableStatement {
    protected static final Constructor<?> JDBC_4_CSTMT_2_ARGS_CTOR;
    protected static final Constructor<?> JDBC_4_CSTMT_4_ARGS_CTOR;
    private static final int NOT_OUTPUT_PARAMETER_INDICATOR = Integer.MIN_VALUE;
    private static final String PARAMETER_NAMESPACE_PREFIX = "@com_mysql_jdbc_outparam_";
    protected boolean callingStoredFunction;
    private ResultSetInternalMethods functionReturnValueResults;
    private boolean hasOutputParams;
    private ResultSetInternalMethods outputParameterResults;
    protected boolean outputParamWasNull;
    private int[] parameterIndexToRsIndex;
    protected CallableStatementParamInfo paramInfo;
    private CallableStatementParam returnValueParam;
    private int[] placeholderToParameterIndexMap;

    static {
        if (Util.isJdbc4()) {
            try {
                String jdbc4ClassName = Util.isJdbc42() ? "com.mysql.jdbc.JDBC42CallableStatement" : "com.mysql.jdbc.JDBC4CallableStatement";
                JDBC_4_CSTMT_2_ARGS_CTOR = Class.forName(jdbc4ClassName).getConstructor(MySQLConnection.class, CallableStatementParamInfo.class);
                JDBC_4_CSTMT_4_ARGS_CTOR = Class.forName(jdbc4ClassName).getConstructor(MySQLConnection.class, String.class, String.class, Boolean.TYPE);
                return;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            } catch (SecurityException e3) {
                throw new RuntimeException(e3);
            }
        }
        JDBC_4_CSTMT_4_ARGS_CTOR = null;
        JDBC_4_CSTMT_2_ARGS_CTOR = null;
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/CallableStatement$CallableStatementParam.class */
    protected static class CallableStatementParam {
        int desiredJdbcType;
        int index;
        int inOutModifier;
        boolean isIn;
        boolean isOut;
        int jdbcType;
        short nullability;
        String paramName;
        int precision;
        int scale;
        String typeName;

        CallableStatementParam(String name, int idx, boolean in, boolean out, int jdbcType, String typeName, int precision, int scale, short nullability, int inOutModifier) {
            this.paramName = name;
            this.isIn = in;
            this.isOut = out;
            this.index = idx;
            this.jdbcType = jdbcType;
            this.typeName = typeName;
            this.precision = precision;
            this.scale = scale;
            this.nullability = nullability;
            this.inOutModifier = inOutModifier;
        }

        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/CallableStatement$CallableStatementParamInfo.class */
    protected class CallableStatementParamInfo implements ParameterMetaData {
        String catalogInUse;
        boolean isFunctionCall;
        String nativeSql;
        int numParameters;
        List<CallableStatementParam> parameterList;
        Map<String, CallableStatementParam> parameterMap;
        boolean isReadOnlySafeProcedure;
        boolean isReadOnlySafeChecked;

        CallableStatementParamInfo(CallableStatementParamInfo fullParamInfo) {
            this.isReadOnlySafeProcedure = false;
            this.isReadOnlySafeChecked = false;
            this.nativeSql = CallableStatement.this.originalSql;
            this.catalogInUse = CallableStatement.this.currentCatalog;
            this.isFunctionCall = fullParamInfo.isFunctionCall;
            int[] localParameterMap = CallableStatement.this.placeholderToParameterIndexMap;
            this.isReadOnlySafeProcedure = fullParamInfo.isReadOnlySafeProcedure;
            this.isReadOnlySafeChecked = fullParamInfo.isReadOnlySafeChecked;
            this.parameterList = new ArrayList(fullParamInfo.numParameters);
            this.parameterMap = new HashMap(fullParamInfo.numParameters);
            for (int i : localParameterMap) {
                CallableStatementParam param = fullParamInfo.parameterList.get(i);
                this.parameterList.add(param);
                this.parameterMap.put(param.paramName, param);
            }
            this.numParameters = this.parameterList.size();
        }

        CallableStatementParamInfo(ResultSet paramTypesRs) throws SQLException {
            this.isReadOnlySafeProcedure = false;
            this.isReadOnlySafeChecked = false;
            boolean hadRows = paramTypesRs.last();
            this.nativeSql = CallableStatement.this.originalSql;
            this.catalogInUse = CallableStatement.this.currentCatalog;
            this.isFunctionCall = CallableStatement.this.callingStoredFunction;
            if (hadRows) {
                this.numParameters = paramTypesRs.getRow();
                this.parameterList = new ArrayList(this.numParameters);
                this.parameterMap = new HashMap(this.numParameters);
                paramTypesRs.beforeFirst();
                addParametersFromDBMD(paramTypesRs);
                return;
            }
            this.numParameters = 0;
        }

        private void addParametersFromDBMD(ResultSet paramTypesRs) throws SQLException {
            int inOutModifier;
            int i = 0;
            while (paramTypesRs.next()) {
                String paramName = paramTypesRs.getString(4);
                switch (paramTypesRs.getInt(5)) {
                    case 1:
                        inOutModifier = 1;
                        break;
                    case 2:
                        inOutModifier = 2;
                        break;
                    case 3:
                    default:
                        inOutModifier = 0;
                        break;
                    case 4:
                    case 5:
                        inOutModifier = 4;
                        break;
                }
                boolean isOutParameter = false;
                boolean isInParameter = false;
                if (i == 0 && this.isFunctionCall) {
                    isOutParameter = true;
                    isInParameter = false;
                } else if (inOutModifier == 2) {
                    isOutParameter = true;
                    isInParameter = true;
                } else if (inOutModifier == 1) {
                    isOutParameter = false;
                    isInParameter = true;
                } else if (inOutModifier == 4) {
                    isOutParameter = true;
                    isInParameter = false;
                }
                int jdbcType = paramTypesRs.getInt(6);
                String typeName = paramTypesRs.getString(7);
                int precision = paramTypesRs.getInt(8);
                int scale = paramTypesRs.getInt(10);
                short nullability = paramTypesRs.getShort(12);
                int i2 = i;
                i++;
                CallableStatementParam paramInfoToAdd = new CallableStatementParam(paramName, i2, isInParameter, isOutParameter, jdbcType, typeName, precision, scale, nullability, inOutModifier);
                this.parameterList.add(paramInfoToAdd);
                this.parameterMap.put(paramName, paramInfoToAdd);
            }
        }

        protected void checkBounds(int paramIndex) throws SQLException {
            int localParamIndex = paramIndex - 1;
            if (paramIndex < 0 || localParamIndex >= CallableStatement.this.parameterCount) {
                throw SQLError.createSQLException(Messages.getString("CallableStatement.11") + paramIndex + Messages.getString("CallableStatement.12") + CallableStatement.this.parameterCount + Messages.getString("CallableStatement.13"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, CallableStatement.this.getExceptionInterceptor());
            }
        }

        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        CallableStatementParam getParameter(int index) {
            return this.parameterList.get(index);
        }

        CallableStatementParam getParameter(String name) {
            return this.parameterMap.get(name);
        }

        @Override // java.sql.ParameterMetaData
        public String getParameterClassName(int arg0) throws SQLException {
            String mysqlTypeName = getParameterTypeName(arg0);
            boolean isBinaryOrBlob = (StringUtils.indexOfIgnoreCase(mysqlTypeName, "BLOB") == -1 && StringUtils.indexOfIgnoreCase(mysqlTypeName, "BINARY") == -1) ? false : true;
            boolean isUnsigned = StringUtils.indexOfIgnoreCase(mysqlTypeName, "UNSIGNED") != -1;
            int mysqlTypeIfKnown = 0;
            if (StringUtils.startsWithIgnoreCase(mysqlTypeName, "MEDIUMINT")) {
                mysqlTypeIfKnown = 9;
            }
            return ResultSetMetaData.getClassNameForJavaType(getParameterType(arg0), isUnsigned, mysqlTypeIfKnown, isBinaryOrBlob, false, CallableStatement.this.connection.getYearIsDateType());
        }

        @Override // java.sql.ParameterMetaData
        public int getParameterCount() throws SQLException {
            if (this.parameterList == null) {
                return 0;
            }
            return this.parameterList.size();
        }

        @Override // java.sql.ParameterMetaData
        public int getParameterMode(int arg0) throws SQLException {
            checkBounds(arg0);
            return getParameter(arg0 - 1).inOutModifier;
        }

        @Override // java.sql.ParameterMetaData
        public int getParameterType(int arg0) throws SQLException {
            checkBounds(arg0);
            return getParameter(arg0 - 1).jdbcType;
        }

        @Override // java.sql.ParameterMetaData
        public String getParameterTypeName(int arg0) throws SQLException {
            checkBounds(arg0);
            return getParameter(arg0 - 1).typeName;
        }

        @Override // java.sql.ParameterMetaData
        public int getPrecision(int arg0) throws SQLException {
            checkBounds(arg0);
            return getParameter(arg0 - 1).precision;
        }

        @Override // java.sql.ParameterMetaData
        public int getScale(int arg0) throws SQLException {
            checkBounds(arg0);
            return getParameter(arg0 - 1).scale;
        }

        @Override // java.sql.ParameterMetaData
        public int isNullable(int arg0) throws SQLException {
            checkBounds(arg0);
            return getParameter(arg0 - 1).nullability;
        }

        @Override // java.sql.ParameterMetaData
        public boolean isSigned(int arg0) throws SQLException {
            checkBounds(arg0);
            return false;
        }

        Iterator<CallableStatementParam> iterator() {
            return this.parameterList.iterator();
        }

        int numberOfParameters() {
            return this.numParameters;
        }

        @Override // java.sql.Wrapper
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            CallableStatement.this.checkClosed();
            return iface.isInstance(this);
        }

        @Override // java.sql.Wrapper
        public <T> T unwrap(Class<T> iface) throws SQLException {
            try {
                return iface.cast(this);
            } catch (ClassCastException e) {
                throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, CallableStatement.this.getExceptionInterceptor());
            }
        }
    }

    private static String mangleParameterName(String origParameterName) {
        if (origParameterName == null) {
            return null;
        }
        int offset = 0;
        if (origParameterName.length() > 0 && origParameterName.charAt(0) == '@') {
            offset = 1;
        }
        StringBuilder paramNameBuf = new StringBuilder(PARAMETER_NAMESPACE_PREFIX.length() + origParameterName.length());
        paramNameBuf.append(PARAMETER_NAMESPACE_PREFIX);
        paramNameBuf.append(origParameterName.substring(offset));
        return paramNameBuf.toString();
    }

    public CallableStatement(MySQLConnection conn, CallableStatementParamInfo paramInfo) throws SQLException {
        super(conn, paramInfo.nativeSql, paramInfo.catalogInUse);
        this.callingStoredFunction = false;
        this.hasOutputParams = false;
        this.outputParamWasNull = false;
        this.paramInfo = paramInfo;
        this.callingStoredFunction = this.paramInfo.isFunctionCall;
        if (this.callingStoredFunction) {
            this.parameterCount++;
        }
        this.retrieveGeneratedKeys = true;
    }

    protected static CallableStatement getInstance(MySQLConnection conn, String sql, String catalog, boolean isFunctionCall) throws SQLException {
        return !Util.isJdbc4() ? new CallableStatement(conn, sql, catalog, isFunctionCall) : (CallableStatement) Util.handleNewInstance(JDBC_4_CSTMT_4_ARGS_CTOR, new Object[]{conn, sql, catalog, Boolean.valueOf(isFunctionCall)}, conn.getExceptionInterceptor());
    }

    protected static CallableStatement getInstance(MySQLConnection conn, CallableStatementParamInfo paramInfo) throws SQLException {
        return !Util.isJdbc4() ? new CallableStatement(conn, paramInfo) : (CallableStatement) Util.handleNewInstance(JDBC_4_CSTMT_2_ARGS_CTOR, new Object[]{conn, paramInfo}, conn.getExceptionInterceptor());
    }

    private void generateParameterMap() throws SQLException {
        int parenOpenPos;
        int parenClosePos;
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.paramInfo == null) {
                return;
            }
            int parameterCountFromMetaData = this.paramInfo.getParameterCount();
            if (this.paramInfo != null && this.parameterCount != parameterCountFromMetaData) {
                this.placeholderToParameterIndexMap = new int[this.parameterCount];
                int startIndex = 0;
                if (this.callingStoredFunction) {
                    this.placeholderToParameterIndexMap[0] = 0;
                    startIndex = 1;
                }
                int startPos = this.callingStoredFunction ? StringUtils.indexOfIgnoreCase(this.originalSql, "SELECT") : StringUtils.indexOfIgnoreCase(this.originalSql, "CALL");
                if (startPos != -1 && (parenOpenPos = this.originalSql.indexOf(40, startPos + 4)) != -1 && (parenClosePos = StringUtils.indexOfIgnoreCase(parenOpenPos, this.originalSql, ")", "'", "'", StringUtils.SEARCH_MODE__ALL)) != -1) {
                    List<?> parsedParameters = StringUtils.split(this.originalSql.substring(parenOpenPos + 1, parenClosePos), ",", "'\"", "'\"", true);
                    int numParsedParameters = parsedParameters.size();
                    int placeholderCount = startIndex;
                    for (int i = 0; i < numParsedParameters; i++) {
                        if (parsedParameters.get(i).equals("?")) {
                            int i2 = placeholderCount;
                            placeholderCount++;
                            this.placeholderToParameterIndexMap[i2] = startIndex + i;
                        }
                    }
                }
            }
        }
    }

    public CallableStatement(MySQLConnection conn, String sql, String catalog, boolean isFunctionCall) throws SQLException {
        super(conn, sql, catalog);
        this.callingStoredFunction = false;
        this.hasOutputParams = false;
        this.outputParamWasNull = false;
        this.callingStoredFunction = isFunctionCall;
        if (!this.callingStoredFunction) {
            if (!StringUtils.startsWithIgnoreCaseAndWs(sql, "CALL")) {
                fakeParameterTypes(false);
            } else {
                determineParameterTypes();
            }
            generateParameterMap();
        } else {
            determineParameterTypes();
            this.parameterCount++;
            generateParameterMap();
        }
        this.retrieveGeneratedKeys = true;
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void addBatch() throws SQLException {
        setOutParams();
        super.addBatch();
    }

    private CallableStatementParam checkIsOutputParam(int paramIndex) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.callingStoredFunction && paramIndex == 1) {
                if (this.returnValueParam == null) {
                    this.returnValueParam = new CallableStatementParam("", 0, false, true, 12, "VARCHAR", 0, 0, (short) 2, 5);
                }
                return this.returnValueParam;
            }
            checkParameterIndexBounds(paramIndex);
            int localParamIndex = paramIndex - 1;
            if (this.placeholderToParameterIndexMap != null) {
                localParamIndex = this.placeholderToParameterIndexMap[localParamIndex];
            }
            CallableStatementParam paramDescriptor = this.paramInfo.getParameter(localParamIndex);
            if (this.connection.getNoAccessToProcedureBodies()) {
                paramDescriptor.isOut = true;
                paramDescriptor.isIn = true;
                paramDescriptor.inOutModifier = 2;
            } else if (!paramDescriptor.isOut) {
                throw SQLError.createSQLException(Messages.getString("CallableStatement.9") + paramIndex + Messages.getString("CallableStatement.10"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            this.hasOutputParams = true;
            return paramDescriptor;
        }
    }

    private void checkParameterIndexBounds(int paramIndex) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.paramInfo.checkBounds(paramIndex);
        }
    }

    private void checkStreamability() throws SQLException {
        if (this.hasOutputParams && createStreamingResultSet()) {
            throw SQLError.createSQLException(Messages.getString("CallableStatement.14"), SQLError.SQL_STATE_DRIVER_NOT_CAPABLE, getExceptionInterceptor());
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void clearParameters() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            super.clearParameters();
            try {
                if (this.outputParameterResults != null) {
                    this.outputParameterResults.close();
                }
            } finally {
                this.outputParameterResults = null;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v42, types: [byte[], byte[][]] */
    private void fakeParameterTypes(boolean isReallyProcedure) throws SQLException {
        byte[] procNameAsBytes;
        byte[] bytes;
        synchronized (checkClosed().getConnectionMutex()) {
            Field[] fields = {new Field("", "PROCEDURE_CAT", 1, 0), new Field("", "PROCEDURE_SCHEM", 1, 0), new Field("", "PROCEDURE_NAME", 1, 0), new Field("", "COLUMN_NAME", 1, 0), new Field("", "COLUMN_TYPE", 1, 0), new Field("", "DATA_TYPE", 5, 0), new Field("", "TYPE_NAME", 1, 0), new Field("", "PRECISION", 4, 0), new Field("", "LENGTH", 4, 0), new Field("", "SCALE", 5, 0), new Field("", "RADIX", 5, 0), new Field("", "NULLABLE", 5, 0), new Field("", "REMARKS", 1, 0)};
            String procName = isReallyProcedure ? extractProcedureName() : null;
            if (procName == null) {
                bytes = null;
            } else {
                try {
                    bytes = StringUtils.getBytes(procName, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    procNameAsBytes = StringUtils.s2b(procName, this.connection);
                }
            }
            procNameAsBytes = bytes;
            ArrayList<ResultSetRow> resultRows = new ArrayList<>();
            int numOfParameters = this.callingStoredFunction ? this.parameterCount + 1 : this.parameterCount;
            for (int i = 0; i < numOfParameters; i++) {
                ?? r0 = new byte[13];
                r0[0] = 0;
                r0[1] = 0;
                r0[2] = procNameAsBytes;
                r0[3] = StringUtils.s2b(String.valueOf(i), this.connection);
                if (this.callingStoredFunction && i == 0) {
                    r0[4] = StringUtils.s2b(String.valueOf(4), this.connection);
                } else {
                    r0[4] = StringUtils.s2b(String.valueOf(1), this.connection);
                }
                r0[5] = StringUtils.s2b(String.valueOf(12), this.connection);
                r0[6] = StringUtils.s2b("VARCHAR", this.connection);
                r0[7] = StringUtils.s2b(Integer.toString(65535), this.connection);
                r0[8] = StringUtils.s2b(Integer.toString(65535), this.connection);
                r0[9] = StringUtils.s2b(Integer.toString(0), this.connection);
                r0[10] = StringUtils.s2b(Integer.toString(10), this.connection);
                r0[11] = StringUtils.s2b(Integer.toString(2), this.connection);
                r0[12] = 0;
                resultRows.add(new ByteArrayRow(r0, getExceptionInterceptor()));
            }
            ResultSet paramTypesRs = DatabaseMetaData.buildResultSet(fields, resultRows, this.connection);
            convertGetProcedureColumnsToInternalDescriptors(paramTypesRs);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:41:0x00e9, code lost:
    
        throw r16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00f0, code lost:
    
        if (0 == 0) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00f3, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0106, code lost:
    
        if (0 == 0) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x010b, code lost:
    
        throw null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void determineParameterTypes() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 283
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.CallableStatement.determineParameterTypes():void");
    }

    protected ResultSet getParamTypes(String catalog, String routineName) throws SQLException {
        boolean getProcRetFuncsCurrentValue = this.connection.getGetProceduresReturnsFunctions();
        try {
            this.connection.setGetProceduresReturnsFunctions(this.callingStoredFunction);
            return this.connection.getMetaData().getProcedureColumns(catalog, null, routineName, QuickTargetSourceCreator.PREFIX_THREAD_LOCAL);
        } finally {
            this.connection.setGetProceduresReturnsFunctions(getProcRetFuncsCurrentValue);
        }
    }

    private void convertGetProcedureColumnsToInternalDescriptors(ResultSet paramTypesRs) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.paramInfo = new CallableStatementParamInfo(paramTypesRs);
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public boolean execute() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            checkStreamability();
            setInOutParamsOnServer();
            setOutParams();
            boolean returnVal = super.execute();
            if (this.callingStoredFunction) {
                this.functionReturnValueResults = this.results;
                this.functionReturnValueResults.next();
                this.results = null;
            }
            retrieveOutParams();
            if (!this.callingStoredFunction) {
                return returnVal;
            }
            return false;
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public ResultSet executeQuery() throws SQLException {
        ResultSet execResults;
        synchronized (checkClosed().getConnectionMutex()) {
            checkStreamability();
            setInOutParamsOnServer();
            setOutParams();
            execResults = super.executeQuery();
            retrieveOutParams();
        }
        return execResults;
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public int executeUpdate() throws SQLException {
        return Util.truncateAndConvertToInt(executeLargeUpdate());
    }

    private String extractProcedureName() throws SQLException, IOException {
        String sanitizedSql = StringUtils.stripComments(this.originalSql, "`\"'", "`\"'", true, false, true, true);
        int endCallIndex = StringUtils.indexOfIgnoreCase(sanitizedSql, "CALL ");
        int offset = 5;
        if (endCallIndex == -1) {
            endCallIndex = StringUtils.indexOfIgnoreCase(sanitizedSql, "SELECT ");
            offset = 7;
        }
        if (endCallIndex != -1) {
            StringBuilder nameBuf = new StringBuilder();
            String trimmedStatement = sanitizedSql.substring(endCallIndex + offset).trim();
            int statementLength = trimmedStatement.length();
            for (int i = 0; i < statementLength; i++) {
                char c = trimmedStatement.charAt(i);
                if (Character.isWhitespace(c) || c == '(' || c == '?') {
                    break;
                }
                nameBuf.append(c);
            }
            return nameBuf.toString();
        }
        throw SQLError.createSQLException(Messages.getString("CallableStatement.1"), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0017 A[Catch: all -> 0x007b, TryCatch #0 {, blocks: (B:6:0x0010, B:18:0x0052, B:21:0x005c, B:23:0x0068, B:24:0x0073, B:25:0x0074, B:26:0x0079, B:8:0x0017, B:10:0x001e, B:12:0x0037, B:14:0x0044, B:15:0x004d, B:13:0x003f), top: B:33:0x0010 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected java.lang.String fixParameterName(java.lang.String r5) throws java.sql.SQLException {
        /*
            r4 = this;
            r0 = r4
            com.mysql.jdbc.MySQLConnection r0 = r0.checkClosed()
            java.lang.Object r0 = r0.getConnectionMutex()
            r1 = r0
            r6 = r1
            monitor-enter(r0)
            r0 = r5
            if (r0 == 0) goto L17
            r0 = r5
            int r0 = r0.length()     // Catch: java.lang.Throwable -> L7b
            if (r0 != 0) goto L4e
        L17:
            r0 = r4
            boolean r0 = r0.hasParametersView()     // Catch: java.lang.Throwable -> L7b
            if (r0 != 0) goto L4e
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L7b
            r1 = r0
            r1.<init>()     // Catch: java.lang.Throwable -> L7b
            java.lang.String r1 = "CallableStatement.0"
            java.lang.String r1 = com.mysql.jdbc.Messages.getString(r1)     // Catch: java.lang.Throwable -> L7b
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> L7b
            r1 = r5
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> L7b
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L7b
            if (r0 != 0) goto L3f
            java.lang.String r0 = "CallableStatement.15"
            java.lang.String r0 = com.mysql.jdbc.Messages.getString(r0)     // Catch: java.lang.Throwable -> L7b
            goto L44
        L3f:
            java.lang.String r0 = "CallableStatement.16"
            java.lang.String r0 = com.mysql.jdbc.Messages.getString(r0)     // Catch: java.lang.Throwable -> L7b
        L44:
            java.lang.String r1 = "S1009"
            r2 = r4
            com.mysql.jdbc.ExceptionInterceptor r2 = r2.getExceptionInterceptor()     // Catch: java.lang.Throwable -> L7b
            java.sql.SQLException r0 = com.mysql.jdbc.SQLError.createSQLException(r0, r1, r2)     // Catch: java.lang.Throwable -> L7b
            throw r0     // Catch: java.lang.Throwable -> L7b
        L4e:
            r0 = r5
            if (r0 != 0) goto L5c
            r0 = r4
            boolean r0 = r0.hasParametersView()     // Catch: java.lang.Throwable -> L7b
            if (r0 == 0) goto L5c
            java.lang.String r0 = "nullpn"
            r5 = r0
        L5c:
            r0 = r4
            com.mysql.jdbc.MySQLConnection r0 = r0.connection     // Catch: java.lang.Throwable -> L7b
            boolean r0 = r0.getNoAccessToProcedureBodies()     // Catch: java.lang.Throwable -> L7b
            if (r0 == 0) goto L74
            java.lang.String r0 = "No access to parameters by name when connection has been configured not to access procedure bodies"
            java.lang.String r1 = "S1009"
            r2 = r4
            com.mysql.jdbc.ExceptionInterceptor r2 = r2.getExceptionInterceptor()     // Catch: java.lang.Throwable -> L7b
            java.sql.SQLException r0 = com.mysql.jdbc.SQLError.createSQLException(r0, r1, r2)     // Catch: java.lang.Throwable -> L7b
            throw r0     // Catch: java.lang.Throwable -> L7b
        L74:
            r0 = r5
            java.lang.String r0 = mangleParameterName(r0)     // Catch: java.lang.Throwable -> L7b
            r1 = r6
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L7b
            return r0
        L7b:
            r7 = move-exception
            r0 = r6
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L7b
            r0 = r7
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.CallableStatement.fixParameterName(java.lang.String):java.lang.String");
    }

    @Override // java.sql.CallableStatement
    public Array getArray(int i) throws SQLException {
        Array retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(i);
            retValue = rs.getArray(mapOutputParameterIndexToRsIndex(i));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Array getArray(String parameterName) throws SQLException {
        Array retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getArray(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        BigDecimal retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getBigDecimal(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    @Deprecated
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        BigDecimal retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getBigDecimal(mapOutputParameterIndexToRsIndex(parameterIndex), scale);
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        BigDecimal retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getBigDecimal(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public java.sql.Blob getBlob(int parameterIndex) throws SQLException {
        java.sql.Blob retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getBlob(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public java.sql.Blob getBlob(String parameterName) throws SQLException {
        java.sql.Blob retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getBlob(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public boolean getBoolean(int parameterIndex) throws SQLException {
        boolean retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getBoolean(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public boolean getBoolean(String parameterName) throws SQLException {
        boolean retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getBoolean(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public byte getByte(int parameterIndex) throws SQLException {
        byte retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getByte(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public byte getByte(String parameterName) throws SQLException {
        byte retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getByte(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public byte[] getBytes(int parameterIndex) throws SQLException {
        byte[] retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getBytes(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public byte[] getBytes(String parameterName) throws SQLException {
        byte[] retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getBytes(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public java.sql.Clob getClob(int parameterIndex) throws SQLException {
        java.sql.Clob retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getClob(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public java.sql.Clob getClob(String parameterName) throws SQLException {
        java.sql.Clob retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getClob(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Date getDate(int parameterIndex) throws SQLException {
        Date retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getDate(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        Date retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getDate(mapOutputParameterIndexToRsIndex(parameterIndex), cal);
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Date getDate(String parameterName) throws SQLException {
        Date retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getDate(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        Date retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getDate(fixParameterName(parameterName), cal);
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public double getDouble(int parameterIndex) throws SQLException {
        double retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getDouble(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public double getDouble(String parameterName) throws SQLException {
        double retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getDouble(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public float getFloat(int parameterIndex) throws SQLException {
        float retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getFloat(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public float getFloat(String parameterName) throws SQLException {
        float retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getFloat(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public int getInt(int parameterIndex) throws SQLException {
        int retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getInt(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public int getInt(String parameterName) throws SQLException {
        int retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getInt(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public long getLong(int parameterIndex) throws SQLException {
        long retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getLong(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public long getLong(String parameterName) throws SQLException {
        long retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getLong(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    protected int getNamedParamIndex(String paramName, boolean forOut) throws SQLException {
        CallableStatementParam namedParamInfo;
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.connection.getNoAccessToProcedureBodies()) {
                throw SQLError.createSQLException("No access to parameters by name when connection has been configured not to access procedure bodies", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (paramName == null || paramName.length() == 0) {
                throw SQLError.createSQLException(Messages.getString("CallableStatement.2"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (this.paramInfo == null || (namedParamInfo = this.paramInfo.getParameter(paramName)) == null) {
                throw SQLError.createSQLException(Messages.getString("CallableStatement.3") + paramName + Messages.getString("CallableStatement.4"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (forOut && !namedParamInfo.isOut) {
                throw SQLError.createSQLException(Messages.getString("CallableStatement.5") + paramName + Messages.getString("CallableStatement.6"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (this.placeholderToParameterIndexMap == null) {
                return namedParamInfo.index + 1;
            }
            for (int i = 0; i < this.placeholderToParameterIndexMap.length; i++) {
                if (this.placeholderToParameterIndexMap[i] == namedParamInfo.index) {
                    return i + 1;
                }
            }
            throw SQLError.createSQLException("Can't find local placeholder mapping for parameter named \"" + paramName + "\".", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    @Override // java.sql.CallableStatement
    public Object getObject(int parameterIndex) throws SQLException {
        Object retVal;
        synchronized (checkClosed().getConnectionMutex()) {
            CallableStatementParam paramDescriptor = checkIsOutputParam(parameterIndex);
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retVal = rs.getObjectStoredProc(mapOutputParameterIndexToRsIndex(parameterIndex), paramDescriptor.desiredJdbcType);
            this.outputParamWasNull = rs.wasNull();
        }
        return retVal;
    }

    @Override // java.sql.CallableStatement
    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        Object retVal;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retVal = rs.getObject(mapOutputParameterIndexToRsIndex(parameterIndex), map);
            this.outputParamWasNull = rs.wasNull();
        }
        return retVal;
    }

    @Override // java.sql.CallableStatement
    public Object getObject(String parameterName) throws SQLException {
        Object retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getObject(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        Object retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getObject(fixParameterName(parameterName), map);
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    public <T> T getObject(int i, Class<T> cls) throws SQLException {
        T t;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods outputParameters = getOutputParameters(i);
            t = (T) ((ResultSetImpl) outputParameters).getObject(mapOutputParameterIndexToRsIndex(i), cls);
            this.outputParamWasNull = outputParameters.wasNull();
        }
        return t;
    }

    public <T> T getObject(String str, Class<T> cls) throws SQLException {
        T t;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods outputParameters = getOutputParameters(0);
            t = (T) ((ResultSetImpl) outputParameters).getObject(fixParameterName(str), cls);
            this.outputParamWasNull = outputParameters.wasNull();
        }
        return t;
    }

    protected ResultSetInternalMethods getOutputParameters(int paramIndex) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.outputParamWasNull = false;
            if (paramIndex == 1 && this.callingStoredFunction && this.returnValueParam != null) {
                return this.functionReturnValueResults;
            }
            if (this.outputParameterResults == null) {
                if (this.paramInfo.numberOfParameters() == 0) {
                    throw SQLError.createSQLException(Messages.getString("CallableStatement.7"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
                throw SQLError.createSQLException(Messages.getString("CallableStatement.8"), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            }
            return this.outputParameterResults;
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public ParameterMetaData getParameterMetaData() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.placeholderToParameterIndexMap == null) {
                return this.paramInfo;
            }
            return new CallableStatementParamInfo(this.paramInfo);
        }
    }

    @Override // java.sql.CallableStatement
    public Ref getRef(int parameterIndex) throws SQLException {
        Ref retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getRef(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Ref getRef(String parameterName) throws SQLException {
        Ref retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getRef(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public short getShort(int parameterIndex) throws SQLException {
        short retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getShort(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public short getShort(String parameterName) throws SQLException {
        short retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getShort(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public String getString(int parameterIndex) throws SQLException {
        String retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getString(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public String getString(String parameterName) throws SQLException {
        String retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getString(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Time getTime(int parameterIndex) throws SQLException {
        Time retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getTime(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        Time retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getTime(mapOutputParameterIndexToRsIndex(parameterIndex), cal);
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Time getTime(String parameterName) throws SQLException {
        Time retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getTime(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        Time retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getTime(fixParameterName(parameterName), cal);
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        Timestamp retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getTimestamp(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        Timestamp retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getTimestamp(mapOutputParameterIndexToRsIndex(parameterIndex), cal);
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Timestamp getTimestamp(String parameterName) throws SQLException {
        Timestamp retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getTimestamp(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        Timestamp retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getTimestamp(fixParameterName(parameterName), cal);
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public URL getURL(int parameterIndex) throws SQLException {
        URL retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
            retValue = rs.getURL(mapOutputParameterIndexToRsIndex(parameterIndex));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    @Override // java.sql.CallableStatement
    public URL getURL(String parameterName) throws SQLException {
        URL retValue;
        synchronized (checkClosed().getConnectionMutex()) {
            ResultSetInternalMethods rs = getOutputParameters(0);
            retValue = rs.getURL(fixParameterName(parameterName));
            this.outputParamWasNull = rs.wasNull();
        }
        return retValue;
    }

    protected int mapOutputParameterIndexToRsIndex(int paramIndex) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.returnValueParam != null && paramIndex == 1) {
                return 1;
            }
            checkParameterIndexBounds(paramIndex);
            int localParamIndex = paramIndex - 1;
            if (this.placeholderToParameterIndexMap != null) {
                localParamIndex = this.placeholderToParameterIndexMap[localParamIndex];
            }
            int rsIndex = this.parameterIndexToRsIndex[localParamIndex];
            if (rsIndex == Integer.MIN_VALUE) {
                throw SQLError.createSQLException(Messages.getString("CallableStatement.21") + paramIndex + Messages.getString("CallableStatement.22"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            return rsIndex + 1;
        }
    }

    @Override // java.sql.CallableStatement
    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        CallableStatementParam paramDescriptor = checkIsOutputParam(parameterIndex);
        paramDescriptor.desiredJdbcType = sqlType;
    }

    @Override // java.sql.CallableStatement
    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        registerOutParameter(parameterIndex, sqlType);
    }

    @Override // java.sql.CallableStatement
    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        checkIsOutputParam(parameterIndex);
    }

    @Override // java.sql.CallableStatement
    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            registerOutParameter(getNamedParamIndex(parameterName, true), sqlType);
        }
    }

    @Override // java.sql.CallableStatement
    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        registerOutParameter(getNamedParamIndex(parameterName, true), sqlType);
    }

    @Override // java.sql.CallableStatement
    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        registerOutParameter(getNamedParamIndex(parameterName, true), sqlType, typeName);
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Finally extract failed */
    private void retrieveOutParams() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 363
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.CallableStatement.retrieveOutParams():void");
    }

    @Override // java.sql.CallableStatement
    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        setAsciiStream(getNamedParamIndex(parameterName, false), x, length);
    }

    @Override // java.sql.CallableStatement
    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        setBigDecimal(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        setBinaryStream(getNamedParamIndex(parameterName, false), x, length);
    }

    @Override // java.sql.CallableStatement
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        setBoolean(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setByte(String parameterName, byte x) throws SQLException {
        setByte(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setBytes(String parameterName, byte[] x) throws SQLException {
        setBytes(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        setCharacterStream(getNamedParamIndex(parameterName, false), reader, length);
    }

    @Override // java.sql.CallableStatement
    public void setDate(String parameterName, Date x) throws SQLException {
        setDate(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        setDate(getNamedParamIndex(parameterName, false), x, cal);
    }

    @Override // java.sql.CallableStatement
    public void setDouble(String parameterName, double x) throws SQLException {
        setDouble(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setFloat(String parameterName, float x) throws SQLException {
        setFloat(getNamedParamIndex(parameterName, false), x);
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    private void setInOutParamsOnServer() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 471
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.CallableStatement.setInOutParamsOnServer():void");
    }

    @Override // java.sql.CallableStatement
    public void setInt(String parameterName, int x) throws SQLException {
        setInt(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setLong(String parameterName, long x) throws SQLException {
        setLong(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setNull(String parameterName, int sqlType) throws SQLException {
        setNull(getNamedParamIndex(parameterName, false), sqlType);
    }

    @Override // java.sql.CallableStatement
    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        setNull(getNamedParamIndex(parameterName, false), sqlType, typeName);
    }

    @Override // java.sql.CallableStatement
    public void setObject(String parameterName, Object x) throws SQLException {
        setObject(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        setObject(getNamedParamIndex(parameterName, false), x, targetSqlType);
    }

    @Override // java.sql.CallableStatement
    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
    }

    private void setOutParams() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.paramInfo.numParameters > 0) {
                Iterator<CallableStatementParam> paramIter = this.paramInfo.iterator();
                while (paramIter.hasNext()) {
                    CallableStatementParam outParamInfo = paramIter.next();
                    if (!this.callingStoredFunction && outParamInfo.isOut) {
                        if (outParamInfo.paramName == null && hasParametersView()) {
                            outParamInfo.paramName = "nullnp" + outParamInfo.index;
                        }
                        String outParameterName = mangleParameterName(outParamInfo.paramName);
                        int outParamIndex = 0;
                        if (this.placeholderToParameterIndexMap == null) {
                            outParamIndex = outParamInfo.index + 1;
                        } else {
                            boolean found = false;
                            int i = 0;
                            while (true) {
                                if (i >= this.placeholderToParameterIndexMap.length) {
                                    break;
                                }
                                if (this.placeholderToParameterIndexMap[i] != outParamInfo.index) {
                                    i++;
                                } else {
                                    outParamIndex = i + 1;
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                throw SQLError.createSQLException(Messages.getString("CallableStatement.21") + outParamInfo.paramName + Messages.getString("CallableStatement.22"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                            }
                        }
                        setBytesNoEscapeNoQuotes(outParamIndex, StringUtils.getBytes(outParameterName, this.charConverter, this.charEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()));
                    }
                }
            }
        }
    }

    @Override // java.sql.CallableStatement
    public void setShort(String parameterName, short x) throws SQLException {
        setShort(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setString(String parameterName, String x) throws SQLException {
        setString(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setTime(String parameterName, Time x) throws SQLException {
        setTime(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        setTime(getNamedParamIndex(parameterName, false), x, cal);
    }

    @Override // java.sql.CallableStatement
    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        setTimestamp(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        setTimestamp(getNamedParamIndex(parameterName, false), x, cal);
    }

    @Override // java.sql.CallableStatement
    public void setURL(String parameterName, URL val) throws SQLException {
        setURL(getNamedParamIndex(parameterName, false), val);
    }

    @Override // java.sql.CallableStatement
    public boolean wasNull() throws SQLException {
        boolean z;
        synchronized (checkClosed().getConnectionMutex()) {
            z = this.outputParamWasNull;
        }
        return z;
    }

    @Override // com.mysql.jdbc.StatementImpl, java.sql.Statement
    public int[] executeBatch() throws SQLException {
        return Util.truncateAndConvertToInt(executeLargeBatch());
    }

    @Override // com.mysql.jdbc.PreparedStatement
    protected int getParameterIndexOffset() {
        if (this.callingStoredFunction) {
            return -1;
        }
        return super.getParameterIndexOffset();
    }

    @Override // java.sql.CallableStatement
    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        setAsciiStream(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        setAsciiStream(getNamedParamIndex(parameterName, false), x, length);
    }

    @Override // java.sql.CallableStatement
    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        setBinaryStream(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        setBinaryStream(getNamedParamIndex(parameterName, false), x, length);
    }

    @Override // java.sql.CallableStatement
    public void setBlob(String parameterName, java.sql.Blob x) throws SQLException {
        setBlob(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        setBlob(getNamedParamIndex(parameterName, false), inputStream);
    }

    @Override // java.sql.CallableStatement
    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        setBlob(getNamedParamIndex(parameterName, false), inputStream, length);
    }

    @Override // java.sql.CallableStatement
    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        setCharacterStream(getNamedParamIndex(parameterName, false), reader);
    }

    @Override // java.sql.CallableStatement
    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        setCharacterStream(getNamedParamIndex(parameterName, false), reader, length);
    }

    @Override // java.sql.CallableStatement
    public void setClob(String parameterName, java.sql.Clob x) throws SQLException {
        setClob(getNamedParamIndex(parameterName, false), x);
    }

    @Override // java.sql.CallableStatement
    public void setClob(String parameterName, Reader reader) throws SQLException {
        setClob(getNamedParamIndex(parameterName, false), reader);
    }

    @Override // java.sql.CallableStatement
    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        setClob(getNamedParamIndex(parameterName, false), reader, length);
    }

    @Override // java.sql.CallableStatement
    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        setNCharacterStream(getNamedParamIndex(parameterName, false), value);
    }

    @Override // java.sql.CallableStatement
    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        setNCharacterStream(getNamedParamIndex(parameterName, false), value, length);
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x014a, code lost:
    
        throw r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0163, code lost:
    
        r5.paramInfo.isReadOnlySafeChecked = false;
        r5.paramInfo.isReadOnlySafeProcedure = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x017f, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0151, code lost:
    
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0158, code lost:
    
        if (r8 == null) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x015b, code lost:
    
        r8.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0151, code lost:
    
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0158, code lost:
    
        if (r8 == null) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x015b, code lost:
    
        r8.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0151, code lost:
    
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0158, code lost:
    
        if (r8 == null) goto L59;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x015b, code lost:
    
        r8.close();
     */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0151 A[Catch: all -> 0x0178, TryCatch #3 {, blocks: (B:4:0x000c, B:7:0x001a, B:9:0x001c, B:11:0x0026, B:12:0x002e, B:15:0x0034, B:17:0x004c, B:19:0x0067, B:21:0x0075, B:22:0x0084, B:23:0x00a8, B:25:0x00e5, B:27:0x00f9, B:29:0x0104, B:30:0x010b, B:31:0x010c, B:32:0x011e, B:55:0x0151, B:58:0x015b, B:41:0x0131, B:36:0x0126, B:38:0x0129, B:72:0x0151, B:75:0x015b, B:60:0x0163, B:61:0x0174, B:79:0x0151, B:82:0x015b, B:86:0x0151, B:89:0x015b, B:52:0x014a), top: B:93:0x000c, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x015b A[Catch: all -> 0x0178, TryCatch #3 {, blocks: (B:4:0x000c, B:7:0x001a, B:9:0x001c, B:11:0x0026, B:12:0x002e, B:15:0x0034, B:17:0x004c, B:19:0x0067, B:21:0x0075, B:22:0x0084, B:23:0x00a8, B:25:0x00e5, B:27:0x00f9, B:29:0x0104, B:30:0x010b, B:31:0x010c, B:32:0x011e, B:55:0x0151, B:58:0x015b, B:41:0x0131, B:36:0x0126, B:38:0x0129, B:72:0x0151, B:75:0x015b, B:60:0x0163, B:61:0x0174, B:79:0x0151, B:82:0x015b, B:86:0x0151, B:89:0x015b, B:52:0x014a), top: B:93:0x000c, inners: #4 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean checkReadOnlyProcedure() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 385
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.CallableStatement.checkReadOnlyProcedure():boolean");
    }

    @Override // com.mysql.jdbc.PreparedStatement
    protected boolean checkReadOnlySafeStatement() throws SQLException {
        return super.checkReadOnlySafeStatement() || checkReadOnlyProcedure();
    }

    private boolean hasParametersView() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            try {
                if (this.connection.versionMeetsMinimum(5, 5, 0)) {
                    java.sql.DatabaseMetaData dbmd1 = new DatabaseMetaDataUsingInfoSchema(this.connection, this.connection.getCatalog());
                    return ((DatabaseMetaDataUsingInfoSchema) dbmd1).gethasParametersView();
                }
                return false;
            } catch (SQLException e) {
                return false;
            }
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement
    public long executeLargeUpdate() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            checkStreamability();
            if (this.callingStoredFunction) {
                execute();
                return -1L;
            }
            setInOutParamsOnServer();
            setOutParams();
            long returnVal = super.executeLargeUpdate();
            retrieveOutParams();
            return returnVal;
        }
    }

    @Override // com.mysql.jdbc.StatementImpl
    public long[] executeLargeBatch() throws SQLException {
        if (this.hasOutputParams) {
            throw SQLError.createSQLException("Can't call executeBatch() on CallableStatement with OUTPUT parameters", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
        return super.executeLargeBatch();
    }
}
