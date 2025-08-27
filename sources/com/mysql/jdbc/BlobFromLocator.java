package com.mysql.jdbc;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/BlobFromLocator.class */
public class BlobFromLocator implements java.sql.Blob {
    private List<String> primaryKeyColumns;
    private List<String> primaryKeyValues;
    private ResultSetImpl creatorResultSet;
    private String blobColumnName;
    private String tableName;
    private int numColsInResultSet;
    private int numPrimaryKeys;
    private String quotedId;
    private ExceptionInterceptor exceptionInterceptor;

    BlobFromLocator(ResultSetImpl creatorResultSetToSet, int blobColumnIndex, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        this.primaryKeyColumns = null;
        this.primaryKeyValues = null;
        this.blobColumnName = null;
        this.tableName = null;
        this.numColsInResultSet = 0;
        this.numPrimaryKeys = 0;
        this.exceptionInterceptor = exceptionInterceptor;
        this.creatorResultSet = creatorResultSetToSet;
        this.numColsInResultSet = this.creatorResultSet.fields.length;
        this.quotedId = this.creatorResultSet.connection.getMetaData().getIdentifierQuoteString();
        if (this.numColsInResultSet > 1) {
            this.primaryKeyColumns = new ArrayList();
            this.primaryKeyValues = new ArrayList();
            for (int i = 0; i < this.numColsInResultSet; i++) {
                if (this.creatorResultSet.fields[i].isPrimaryKey()) {
                    StringBuilder keyName = new StringBuilder();
                    keyName.append(this.quotedId);
                    String originalColumnName = this.creatorResultSet.fields[i].getOriginalName();
                    if (originalColumnName != null && originalColumnName.length() > 0) {
                        keyName.append(originalColumnName);
                    } else {
                        keyName.append(this.creatorResultSet.fields[i].getName());
                    }
                    keyName.append(this.quotedId);
                    this.primaryKeyColumns.add(keyName.toString());
                    this.primaryKeyValues.add(this.creatorResultSet.getString(i + 1));
                }
            }
        } else {
            notEnoughInformationInQuery();
        }
        this.numPrimaryKeys = this.primaryKeyColumns.size();
        if (this.numPrimaryKeys == 0) {
            notEnoughInformationInQuery();
        }
        if (this.creatorResultSet.fields[0].getOriginalTableName() != null) {
            StringBuilder tableNameBuffer = new StringBuilder();
            String databaseName = this.creatorResultSet.fields[0].getDatabaseName();
            if (databaseName != null && databaseName.length() > 0) {
                tableNameBuffer.append(this.quotedId);
                tableNameBuffer.append(databaseName);
                tableNameBuffer.append(this.quotedId);
                tableNameBuffer.append('.');
            }
            tableNameBuffer.append(this.quotedId);
            tableNameBuffer.append(this.creatorResultSet.fields[0].getOriginalTableName());
            tableNameBuffer.append(this.quotedId);
            this.tableName = tableNameBuffer.toString();
        } else {
            this.tableName = this.quotedId + this.creatorResultSet.fields[0].getTableName() + this.quotedId;
        }
        this.blobColumnName = this.quotedId + this.creatorResultSet.getString(blobColumnIndex) + this.quotedId;
    }

    private void notEnoughInformationInQuery() throws SQLException {
        throw SQLError.createSQLException("Emulated BLOB locators must come from a ResultSet with only one table selected, and all primary keys selected", SQLError.SQL_STATE_GENERAL_ERROR, this.exceptionInterceptor);
    }

    @Override // java.sql.Blob
    public OutputStream setBinaryStream(long indexToWriteAt) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.Blob
    public InputStream getBinaryStream() throws SQLException {
        return new BufferedInputStream(new LocatorInputStream(), this.creatorResultSet.connection.getLocatorFetchBufferSize());
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Finally extract failed */
    @Override // java.sql.Blob
    public int setBytes(long r7, byte[] r9, int r10, int r11) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 357
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.BlobFromLocator.setBytes(long, byte[], int, int):int");
    }

    @Override // java.sql.Blob
    public int setBytes(long writeAt, byte[] bytes) throws SQLException {
        return setBytes(writeAt, bytes, 0, bytes.length);
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // java.sql.Blob
    public byte[] getBytes(long r7, int r9) throws java.sql.SQLException {
        /*
            r6 = this;
            r0 = 0
            r10 = r0
            r0 = r6
            java.sql.PreparedStatement r0 = r0.createGetBytesStatement()     // Catch: java.lang.Throwable -> L19
            r10 = r0
            r0 = r6
            r1 = r10
            r2 = r7
            r3 = r9
            byte[] r0 = r0.getBytesInternal(r1, r2, r3)     // Catch: java.lang.Throwable -> L19
            r11 = r0
            r0 = jsr -> L21
        L16:
            r1 = r11
            return r1
        L19:
            r12 = move-exception
            r0 = jsr -> L21
        L1e:
            r1 = r12
            throw r1
        L21:
            r13 = r0
            r0 = r10
            if (r0 == 0) goto L37
            r0 = r10
            r0.close()     // Catch: java.sql.SQLException -> L32
            goto L34
        L32:
            r14 = move-exception
        L34:
            r0 = 0
            r10 = r0
        L37:
            ret r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.BlobFromLocator.getBytes(long, int):byte[]");
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x00e7, code lost:
    
        throw r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00eb, code lost:
    
        if (0 == 0) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00ee, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00fc, code lost:
    
        if (0 == 0) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00ff, code lost:
    
        r7.close();
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v0 */
    /* JADX WARN: Type inference failed for: r9v1, types: [int] */
    /* JADX WARN: Type inference failed for: r9v11 */
    /* JADX WARN: Type inference failed for: r9v12 */
    /* JADX WARN: Type inference failed for: r9v13 */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference failed for: r9v5 */
    /* JADX WARN: Type inference failed for: r9v8 */
    @Override // java.sql.Blob
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public long length() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 270
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.BlobFromLocator.length():long");
    }

    @Override // java.sql.Blob
    public long position(java.sql.Blob pattern, long start) throws SQLException {
        return position(pattern.getBytes(0L, (int) pattern.length()), start);
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x011a, code lost:
    
        throw r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x011f, code lost:
    
        if (0 == 0) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0122, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0133, code lost:
    
        if (0 == 0) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0136, code lost:
    
        r10.close();
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v1, types: [int] */
    /* JADX WARN: Type inference failed for: r12v11 */
    /* JADX WARN: Type inference failed for: r12v12 */
    /* JADX WARN: Type inference failed for: r12v13 */
    /* JADX WARN: Type inference failed for: r12v3 */
    /* JADX WARN: Type inference failed for: r12v4 */
    /* JADX WARN: Type inference failed for: r12v5 */
    /* JADX WARN: Type inference failed for: r12v8 */
    @Override // java.sql.Blob
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public long position(byte[] r6, long r7) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 327
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.BlobFromLocator.position(byte[], long):long");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Finally extract failed */
    @Override // java.sql.Blob
    public void truncate(long r6) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 285
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.BlobFromLocator.truncate(long):void");
    }

    java.sql.PreparedStatement createGetBytesStatement() throws SQLException {
        StringBuilder query = new StringBuilder("SELECT SUBSTRING(");
        query.append(this.blobColumnName);
        query.append(", ");
        query.append("?");
        query.append(", ");
        query.append("?");
        query.append(") FROM ");
        query.append(this.tableName);
        query.append(" WHERE ");
        query.append(this.primaryKeyColumns.get(0));
        query.append(" = ?");
        for (int i = 1; i < this.numPrimaryKeys; i++) {
            query.append(" AND ");
            query.append(this.primaryKeyColumns.get(i));
            query.append(" = ?");
        }
        return this.creatorResultSet.connection.prepareStatement(query.toString());
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    byte[] getBytesInternal(java.sql.PreparedStatement r6, long r7, int r9) throws java.sql.SQLException {
        /*
            r5 = this;
            r0 = 0
            r10 = r0
            r0 = r6
            r1 = 1
            r2 = r7
            r0.setLong(r1, r2)     // Catch: java.lang.Throwable -> L6e
            r0 = r6
            r1 = 2
            r2 = r9
            r0.setInt(r1, r2)     // Catch: java.lang.Throwable -> L6e
            r0 = 0
            r11 = r0
        L17:
            r0 = r11
            r1 = r5
            int r1 = r1.numPrimaryKeys     // Catch: java.lang.Throwable -> L6e
            if (r0 >= r1) goto L3e
            r0 = r6
            r1 = r11
            r2 = 3
            int r1 = r1 + r2
            r2 = r5
            java.util.List<java.lang.String> r2 = r2.primaryKeyValues     // Catch: java.lang.Throwable -> L6e
            r3 = r11
            java.lang.Object r2 = r2.get(r3)     // Catch: java.lang.Throwable -> L6e
            java.lang.String r2 = (java.lang.String) r2     // Catch: java.lang.Throwable -> L6e
            r0.setString(r1, r2)     // Catch: java.lang.Throwable -> L6e
            int r11 = r11 + 1
            goto L17
        L3e:
            r0 = r6
            java.sql.ResultSet r0 = r0.executeQuery()     // Catch: java.lang.Throwable -> L6e
            r10 = r0
            r0 = r10
            boolean r0 = r0.next()     // Catch: java.lang.Throwable -> L6e
            if (r0 == 0) goto L62
            r0 = r10
            com.mysql.jdbc.ResultSetImpl r0 = (com.mysql.jdbc.ResultSetImpl) r0     // Catch: java.lang.Throwable -> L6e
            r1 = 1
            r2 = 1
            byte[] r0 = r0.getBytes(r1, r2)     // Catch: java.lang.Throwable -> L6e
            r11 = r0
            r0 = jsr -> L76
        L5f:
            r1 = r11
            return r1
        L62:
            java.lang.String r0 = "BLOB data not found! Did primary keys change?"
            java.lang.String r1 = "S1000"
            r2 = r5
            com.mysql.jdbc.ExceptionInterceptor r2 = r2.exceptionInterceptor     // Catch: java.lang.Throwable -> L6e
            java.sql.SQLException r0 = com.mysql.jdbc.SQLError.createSQLException(r0, r1, r2)     // Catch: java.lang.Throwable -> L6e
            throw r0     // Catch: java.lang.Throwable -> L6e
        L6e:
            r12 = move-exception
            r0 = jsr -> L76
        L73:
            r1 = r12
            throw r1
        L76:
            r13 = r0
            r0 = r10
            if (r0 == 0) goto L8c
            r0 = r10
            r0.close()     // Catch: java.sql.SQLException -> L87
            goto L89
        L87:
            r14 = move-exception
        L89:
            r0 = 0
            r10 = r0
        L8c:
            ret r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.BlobFromLocator.getBytesInternal(java.sql.PreparedStatement, long, int):byte[]");
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/BlobFromLocator$LocatorInputStream.class */
    class LocatorInputStream extends InputStream {
        long currentPositionInBlob;
        long length;
        java.sql.PreparedStatement pStmt;

        LocatorInputStream() throws SQLException {
            this.currentPositionInBlob = 0L;
            this.length = 0L;
            this.pStmt = null;
            this.length = BlobFromLocator.this.length();
            this.pStmt = BlobFromLocator.this.createGetBytesStatement();
        }

        LocatorInputStream(long pos, long len) throws SQLException {
            this.currentPositionInBlob = 0L;
            this.length = 0L;
            this.pStmt = null;
            this.length = pos + len;
            this.currentPositionInBlob = pos;
            long blobLength = BlobFromLocator.this.length();
            if (pos + len > blobLength) {
                throw SQLError.createSQLException(Messages.getString("Blob.invalidStreamLength", new Object[]{Long.valueOf(blobLength), Long.valueOf(pos), Long.valueOf(len)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, BlobFromLocator.this.exceptionInterceptor);
            }
            if (pos < 1) {
                throw SQLError.createSQLException(Messages.getString("Blob.invalidStreamPos"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, BlobFromLocator.this.exceptionInterceptor);
            }
            if (pos > blobLength) {
                throw SQLError.createSQLException(Messages.getString("Blob.invalidStreamPos"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, BlobFromLocator.this.exceptionInterceptor);
            }
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.currentPositionInBlob + 1 > this.length) {
                return -1;
            }
            try {
                BlobFromLocator blobFromLocator = BlobFromLocator.this;
                java.sql.PreparedStatement preparedStatement = this.pStmt;
                long j = this.currentPositionInBlob;
                this.currentPositionInBlob = j + 1;
                byte[] asBytes = blobFromLocator.getBytesInternal(preparedStatement, j + 1, 1);
                if (asBytes == null) {
                    return -1;
                }
                return asBytes[0];
            } catch (SQLException sqlEx) {
                throw new IOException(sqlEx.toString());
            }
        }

        @Override // java.io.InputStream
        public int read(byte[] b, int off, int len) throws IOException {
            if (this.currentPositionInBlob + 1 > this.length) {
                return -1;
            }
            try {
                byte[] asBytes = BlobFromLocator.this.getBytesInternal(this.pStmt, this.currentPositionInBlob + 1, len);
                if (asBytes == null) {
                    return -1;
                }
                System.arraycopy(asBytes, 0, b, off, asBytes.length);
                this.currentPositionInBlob += asBytes.length;
                return asBytes.length;
            } catch (SQLException sqlEx) {
                throw new IOException(sqlEx.toString());
            }
        }

        @Override // java.io.InputStream
        public int read(byte[] b) throws IOException {
            if (this.currentPositionInBlob + 1 > this.length) {
                return -1;
            }
            try {
                byte[] asBytes = BlobFromLocator.this.getBytesInternal(this.pStmt, this.currentPositionInBlob + 1, b.length);
                if (asBytes == null) {
                    return -1;
                }
                System.arraycopy(asBytes, 0, b, 0, asBytes.length);
                this.currentPositionInBlob += asBytes.length;
                return asBytes.length;
            } catch (SQLException sqlEx) {
                throw new IOException(sqlEx.toString());
            }
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.pStmt != null) {
                try {
                    this.pStmt.close();
                } catch (SQLException sqlEx) {
                    throw new IOException(sqlEx.toString());
                }
            }
            super.close();
        }
    }

    @Override // java.sql.Blob
    public void free() throws SQLException {
        this.creatorResultSet = null;
        this.primaryKeyColumns = null;
        this.primaryKeyValues = null;
    }

    @Override // java.sql.Blob
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        return new LocatorInputStream(pos, length);
    }
}
