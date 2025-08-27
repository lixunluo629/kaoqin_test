package org.springframework.jdbc.support.lob;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.util.FileCopyUtils;

@Deprecated
/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/lob/OracleLobHandler.class */
public class OracleLobHandler extends AbstractLobHandler {
    private static final String BLOB_CLASS_NAME = "oracle.sql.BLOB";
    private static final String CLOB_CLASS_NAME = "oracle.sql.CLOB";
    private static final String DURATION_SESSION_FIELD_NAME = "DURATION_SESSION";
    private static final String MODE_READWRITE_FIELD_NAME = "MODE_READWRITE";
    private static final String MODE_READONLY_FIELD_NAME = "MODE_READONLY";
    private NativeJdbcExtractor nativeJdbcExtractor;
    private Class<?> blobClass;
    private Class<?> clobClass;
    protected final Log logger = LogFactory.getLog(getClass());
    private Boolean cache = Boolean.TRUE;
    private Boolean releaseResourcesAfterRead = Boolean.FALSE;
    private final Map<Class<?>, Integer> durationSessionConstants = new HashMap(2);
    private final Map<Class<?>, Integer> modeReadWriteConstants = new HashMap(2);
    private final Map<Class<?>, Integer> modeReadOnlyConstants = new HashMap(2);

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/lob/OracleLobHandler$LobCallback.class */
    protected interface LobCallback {
        void populateLob(Object obj) throws Exception;
    }

    public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor) {
        this.nativeJdbcExtractor = nativeJdbcExtractor;
    }

    public void setCache(boolean cache) {
        this.cache = Boolean.valueOf(cache);
    }

    public void setReleaseResourcesAfterRead(boolean releaseResources) {
        this.releaseResourcesAfterRead = Boolean.valueOf(releaseResources);
    }

    protected synchronized void initOracleDriverClasses(Connection con) {
        if (this.blobClass == null) {
            try {
                this.blobClass = con.getClass().getClassLoader().loadClass(BLOB_CLASS_NAME);
                this.durationSessionConstants.put(this.blobClass, Integer.valueOf(this.blobClass.getField(DURATION_SESSION_FIELD_NAME).getInt(null)));
                this.modeReadWriteConstants.put(this.blobClass, Integer.valueOf(this.blobClass.getField(MODE_READWRITE_FIELD_NAME).getInt(null)));
                this.modeReadOnlyConstants.put(this.blobClass, Integer.valueOf(this.blobClass.getField(MODE_READONLY_FIELD_NAME).getInt(null)));
                this.clobClass = con.getClass().getClassLoader().loadClass(CLOB_CLASS_NAME);
                this.durationSessionConstants.put(this.clobClass, Integer.valueOf(this.clobClass.getField(DURATION_SESSION_FIELD_NAME).getInt(null)));
                this.modeReadWriteConstants.put(this.clobClass, Integer.valueOf(this.clobClass.getField(MODE_READWRITE_FIELD_NAME).getInt(null)));
                this.modeReadOnlyConstants.put(this.clobClass, Integer.valueOf(this.clobClass.getField(MODE_READONLY_FIELD_NAME).getInt(null)));
            } catch (Exception ex) {
                throw new InvalidDataAccessApiUsageException("Couldn't initialize OracleLobHandler because Oracle driver classes are not available. Note that OracleLobHandler requires Oracle JDBC driver 9i or higher!", ex);
            }
        }
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public byte[] getBlobAsBytes(ResultSet rs, int columnIndex) throws SQLException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        this.logger.debug("Returning Oracle BLOB as bytes");
        Blob blob = rs.getBlob(columnIndex);
        initializeResourcesBeforeRead(rs.getStatement().getConnection(), blob);
        byte[] retVal = blob != null ? blob.getBytes(1L, (int) blob.length()) : null;
        releaseResourcesAfterRead(rs.getStatement().getConnection(), blob);
        return retVal;
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public InputStream getBlobAsBinaryStream(ResultSet rs, int columnIndex) throws SQLException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        this.logger.debug("Returning Oracle BLOB as binary stream");
        Blob blob = rs.getBlob(columnIndex);
        initializeResourcesBeforeRead(rs.getStatement().getConnection(), blob);
        InputStream retVal = blob != null ? blob.getBinaryStream() : null;
        releaseResourcesAfterRead(rs.getStatement().getConnection(), blob);
        return retVal;
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public String getClobAsString(ResultSet rs, int columnIndex) throws SQLException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        this.logger.debug("Returning Oracle CLOB as string");
        Clob clob = rs.getClob(columnIndex);
        initializeResourcesBeforeRead(rs.getStatement().getConnection(), clob);
        String retVal = clob != null ? clob.getSubString(1L, (int) clob.length()) : null;
        releaseResourcesAfterRead(rs.getStatement().getConnection(), clob);
        return retVal;
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public InputStream getClobAsAsciiStream(ResultSet rs, int columnIndex) throws SQLException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        this.logger.debug("Returning Oracle CLOB as ASCII stream");
        Clob clob = rs.getClob(columnIndex);
        initializeResourcesBeforeRead(rs.getStatement().getConnection(), clob);
        InputStream retVal = clob != null ? clob.getAsciiStream() : null;
        releaseResourcesAfterRead(rs.getStatement().getConnection(), clob);
        return retVal;
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public Reader getClobAsCharacterStream(ResultSet rs, int columnIndex) throws SQLException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        this.logger.debug("Returning Oracle CLOB as character stream");
        Clob clob = rs.getClob(columnIndex);
        initializeResourcesBeforeRead(rs.getStatement().getConnection(), clob);
        Reader retVal = clob != null ? clob.getCharacterStream() : null;
        releaseResourcesAfterRead(rs.getStatement().getConnection(), clob);
        return retVal;
    }

    @Override // org.springframework.jdbc.support.lob.LobHandler
    public LobCreator getLobCreator() {
        return new OracleLobCreator();
    }

    protected void initializeResourcesBeforeRead(Connection con, Object lob) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (this.releaseResourcesAfterRead.booleanValue()) {
            initOracleDriverClasses(con);
            try {
                Method isTemporary = lob.getClass().getMethod("isTemporary", new Class[0]);
                Boolean temporary = (Boolean) isTemporary.invoke(lob, new Object[0]);
                if (!temporary.booleanValue()) {
                    Method open = lob.getClass().getMethod("open", Integer.TYPE);
                    open.invoke(lob, this.modeReadOnlyConstants.get(lob.getClass()));
                }
            } catch (InvocationTargetException ex) {
                this.logger.error("Could not open Oracle LOB", ex.getTargetException());
            } catch (Exception ex2) {
                throw new DataAccessResourceFailureException("Could not open Oracle LOB", ex2);
            }
        }
    }

    protected void releaseResourcesAfterRead(Connection con, Object lob) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (this.releaseResourcesAfterRead.booleanValue()) {
            initOracleDriverClasses(con);
            Boolean temporary = Boolean.FALSE;
            try {
                Method isTemporary = lob.getClass().getMethod("isTemporary", new Class[0]);
                if (((Boolean) isTemporary.invoke(lob, new Object[0])).booleanValue()) {
                    Method freeTemporary = lob.getClass().getMethod("freeTemporary", new Class[0]);
                    freeTemporary.invoke(lob, new Object[0]);
                } else {
                    Method isOpen = lob.getClass().getMethod("isOpen", new Class[0]);
                    Boolean open = (Boolean) isOpen.invoke(lob, new Object[0]);
                    if (open.booleanValue()) {
                        Method close = lob.getClass().getMethod("close", new Class[0]);
                        close.invoke(lob, new Object[0]);
                    }
                }
            } catch (InvocationTargetException ex) {
                if (temporary.booleanValue()) {
                    this.logger.error("Could not free Oracle LOB", ex.getTargetException());
                } else {
                    this.logger.error("Could not close Oracle LOB", ex.getTargetException());
                }
            } catch (Exception ex2) {
                if (temporary.booleanValue()) {
                    throw new DataAccessResourceFailureException("Could not free Oracle LOB", ex2);
                }
                throw new DataAccessResourceFailureException("Could not close Oracle LOB", ex2);
            }
        }
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/lob/OracleLobHandler$OracleLobCreator.class */
    protected class OracleLobCreator implements LobCreator {
        private final List<Object> temporaryLobs = new LinkedList();

        protected OracleLobCreator() {
        }

        @Override // org.springframework.jdbc.support.lob.LobCreator
        public void setBlobAsBytes(PreparedStatement ps, int paramIndex, final byte[] content) throws SQLException {
            if (content != null) {
                Blob blob = (Blob) createLob(ps, false, new LobCallback() { // from class: org.springframework.jdbc.support.lob.OracleLobHandler.OracleLobCreator.1
                    @Override // org.springframework.jdbc.support.lob.OracleLobHandler.LobCallback
                    public void populateLob(Object lob) throws Exception {
                        Method methodToInvoke = lob.getClass().getMethod("getBinaryOutputStream", new Class[0]);
                        OutputStream out = (OutputStream) methodToInvoke.invoke(lob, new Object[0]);
                        FileCopyUtils.copy(content, out);
                    }
                });
                ps.setBlob(paramIndex, blob);
                if (OracleLobHandler.this.logger.isDebugEnabled()) {
                    OracleLobHandler.this.logger.debug("Set bytes for Oracle BLOB with length " + blob.length());
                    return;
                }
                return;
            }
            ps.setBlob(paramIndex, (Blob) null);
            OracleLobHandler.this.logger.debug("Set Oracle BLOB to null");
        }

        @Override // org.springframework.jdbc.support.lob.LobCreator
        public void setBlobAsBinaryStream(PreparedStatement ps, int paramIndex, final InputStream binaryStream, int contentLength) throws SQLException {
            if (binaryStream != null) {
                Blob blob = (Blob) createLob(ps, false, new LobCallback() { // from class: org.springframework.jdbc.support.lob.OracleLobHandler.OracleLobCreator.2
                    @Override // org.springframework.jdbc.support.lob.OracleLobHandler.LobCallback
                    public void populateLob(Object lob) throws Exception {
                        Method methodToInvoke = lob.getClass().getMethod("getBinaryOutputStream", (Class[]) null);
                        OutputStream out = (OutputStream) methodToInvoke.invoke(lob, (Object[]) null);
                        FileCopyUtils.copy(binaryStream, out);
                    }
                });
                ps.setBlob(paramIndex, blob);
                if (OracleLobHandler.this.logger.isDebugEnabled()) {
                    OracleLobHandler.this.logger.debug("Set binary stream for Oracle BLOB with length " + blob.length());
                    return;
                }
                return;
            }
            ps.setBlob(paramIndex, (Blob) null);
            OracleLobHandler.this.logger.debug("Set Oracle BLOB to null");
        }

        @Override // org.springframework.jdbc.support.lob.LobCreator
        public void setClobAsString(PreparedStatement ps, int paramIndex, final String content) throws SQLException {
            if (content != null) {
                Clob clob = (Clob) createLob(ps, true, new LobCallback() { // from class: org.springframework.jdbc.support.lob.OracleLobHandler.OracleLobCreator.3
                    @Override // org.springframework.jdbc.support.lob.OracleLobHandler.LobCallback
                    public void populateLob(Object lob) throws Exception {
                        Method methodToInvoke = lob.getClass().getMethod("getCharacterOutputStream", (Class[]) null);
                        Writer writer = (Writer) methodToInvoke.invoke(lob, (Object[]) null);
                        FileCopyUtils.copy(content, writer);
                    }
                });
                ps.setClob(paramIndex, clob);
                if (OracleLobHandler.this.logger.isDebugEnabled()) {
                    OracleLobHandler.this.logger.debug("Set string for Oracle CLOB with length " + clob.length());
                    return;
                }
                return;
            }
            ps.setClob(paramIndex, (Clob) null);
            OracleLobHandler.this.logger.debug("Set Oracle CLOB to null");
        }

        @Override // org.springframework.jdbc.support.lob.LobCreator
        public void setClobAsAsciiStream(PreparedStatement ps, int paramIndex, final InputStream asciiStream, int contentLength) throws SQLException {
            if (asciiStream != null) {
                Clob clob = (Clob) createLob(ps, true, new LobCallback() { // from class: org.springframework.jdbc.support.lob.OracleLobHandler.OracleLobCreator.4
                    @Override // org.springframework.jdbc.support.lob.OracleLobHandler.LobCallback
                    public void populateLob(Object lob) throws Exception {
                        Method methodToInvoke = lob.getClass().getMethod("getAsciiOutputStream", (Class[]) null);
                        OutputStream out = (OutputStream) methodToInvoke.invoke(lob, (Object[]) null);
                        FileCopyUtils.copy(asciiStream, out);
                    }
                });
                ps.setClob(paramIndex, clob);
                if (OracleLobHandler.this.logger.isDebugEnabled()) {
                    OracleLobHandler.this.logger.debug("Set ASCII stream for Oracle CLOB with length " + clob.length());
                    return;
                }
                return;
            }
            ps.setClob(paramIndex, (Clob) null);
            OracleLobHandler.this.logger.debug("Set Oracle CLOB to null");
        }

        @Override // org.springframework.jdbc.support.lob.LobCreator
        public void setClobAsCharacterStream(PreparedStatement ps, int paramIndex, final Reader characterStream, int contentLength) throws SQLException {
            if (characterStream != null) {
                Clob clob = (Clob) createLob(ps, true, new LobCallback() { // from class: org.springframework.jdbc.support.lob.OracleLobHandler.OracleLobCreator.5
                    @Override // org.springframework.jdbc.support.lob.OracleLobHandler.LobCallback
                    public void populateLob(Object lob) throws Exception {
                        Method methodToInvoke = lob.getClass().getMethod("getCharacterOutputStream", (Class[]) null);
                        Writer writer = (Writer) methodToInvoke.invoke(lob, (Object[]) null);
                        FileCopyUtils.copy(characterStream, writer);
                    }
                });
                ps.setClob(paramIndex, clob);
                if (OracleLobHandler.this.logger.isDebugEnabled()) {
                    OracleLobHandler.this.logger.debug("Set character stream for Oracle CLOB with length " + clob.length());
                    return;
                }
                return;
            }
            ps.setClob(paramIndex, (Clob) null);
            OracleLobHandler.this.logger.debug("Set Oracle CLOB to null");
        }

        protected Object createLob(PreparedStatement ps, boolean clob, LobCallback callback) throws IllegalAccessException, SQLException, IllegalArgumentException, InvocationTargetException {
            Connection con = null;
            try {
                con = getOracleConnection(ps);
                OracleLobHandler.this.initOracleDriverClasses(con);
                Object lob = prepareLob(con, clob ? OracleLobHandler.this.clobClass : OracleLobHandler.this.blobClass);
                callback.populateLob(lob);
                lob.getClass().getMethod("close", (Class[]) null).invoke(lob, (Object[]) null);
                this.temporaryLobs.add(lob);
                if (OracleLobHandler.this.logger.isDebugEnabled()) {
                    OracleLobHandler.this.logger.debug("Created new Oracle " + (clob ? "CLOB" : "BLOB"));
                }
                return lob;
            } catch (InvocationTargetException ex) {
                if (ex.getTargetException() instanceof SQLException) {
                    throw ((SQLException) ex.getTargetException());
                }
                if (con != null && (ex.getTargetException() instanceof ClassCastException)) {
                    throw new InvalidDataAccessApiUsageException("OracleLobCreator needs to work on [oracle.jdbc.OracleConnection], not on [" + con.getClass().getName() + "]: specify a corresponding NativeJdbcExtractor", ex.getTargetException());
                }
                throw new DataAccessResourceFailureException("Could not create Oracle LOB", ex.getTargetException());
            } catch (SQLException ex2) {
                throw ex2;
            } catch (Exception ex3) {
                throw new DataAccessResourceFailureException("Could not create Oracle LOB", ex3);
            }
        }

        protected Connection getOracleConnection(PreparedStatement ps) throws SQLException, ClassNotFoundException {
            return OracleLobHandler.this.nativeJdbcExtractor != null ? OracleLobHandler.this.nativeJdbcExtractor.getNativeConnectionFromStatement(ps) : ps.getConnection();
        }

        protected Object prepareLob(Connection con, Class<?> lobClass) throws Exception {
            Method createTemporary = lobClass.getMethod("createTemporary", Connection.class, Boolean.TYPE, Integer.TYPE);
            Object lob = createTemporary.invoke(null, con, OracleLobHandler.this.cache, OracleLobHandler.this.durationSessionConstants.get(lobClass));
            Method open = lobClass.getMethod("open", Integer.TYPE);
            open.invoke(lob, OracleLobHandler.this.modeReadWriteConstants.get(lobClass));
            return lob;
        }

        @Override // org.springframework.jdbc.support.lob.LobCreator, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
            try {
                Iterator<?> it = this.temporaryLobs.iterator();
                while (it.hasNext()) {
                    Object lob = it.next();
                    Method freeTemporary = lob.getClass().getMethod("freeTemporary", new Class[0]);
                    freeTemporary.invoke(lob, new Object[0]);
                    it.remove();
                }
            } catch (InvocationTargetException ex) {
                OracleLobHandler.this.logger.error("Could not free Oracle LOB", ex.getTargetException());
            } catch (Exception ex2) {
                throw new DataAccessResourceFailureException("Could not free Oracle LOB", ex2);
            }
        }
    }
}
