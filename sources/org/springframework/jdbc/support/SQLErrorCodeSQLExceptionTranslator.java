package org.springframework.jdbc.support;

import java.lang.reflect.Constructor;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.Arrays;
import javax.sql.DataSource;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.CannotSerializeTransactionException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.InvalidResultSetAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/SQLErrorCodeSQLExceptionTranslator.class */
public class SQLErrorCodeSQLExceptionTranslator extends AbstractFallbackSQLExceptionTranslator {
    private static final int MESSAGE_ONLY_CONSTRUCTOR = 1;
    private static final int MESSAGE_THROWABLE_CONSTRUCTOR = 2;
    private static final int MESSAGE_SQLEX_CONSTRUCTOR = 3;
    private static final int MESSAGE_SQL_THROWABLE_CONSTRUCTOR = 4;
    private static final int MESSAGE_SQL_SQLEX_CONSTRUCTOR = 5;
    private SQLErrorCodes sqlErrorCodes;

    public SQLErrorCodeSQLExceptionTranslator() {
        setFallbackTranslator(new SQLExceptionSubclassTranslator());
    }

    public SQLErrorCodeSQLExceptionTranslator(DataSource dataSource) {
        this();
        setDataSource(dataSource);
    }

    public SQLErrorCodeSQLExceptionTranslator(String dbName) {
        this();
        setDatabaseProductName(dbName);
    }

    public SQLErrorCodeSQLExceptionTranslator(SQLErrorCodes sec) {
        this();
        this.sqlErrorCodes = sec;
    }

    public void setDataSource(DataSource dataSource) {
        this.sqlErrorCodes = SQLErrorCodesFactory.getInstance().getErrorCodes(dataSource);
    }

    public void setDatabaseProductName(String dbName) {
        this.sqlErrorCodes = SQLErrorCodesFactory.getInstance().getErrorCodes(dbName);
    }

    public void setSqlErrorCodes(SQLErrorCodes sec) {
        this.sqlErrorCodes = sec;
    }

    public SQLErrorCodes getSqlErrorCodes() {
        return this.sqlErrorCodes;
    }

    @Override // org.springframework.jdbc.support.AbstractFallbackSQLExceptionTranslator
    protected DataAccessException doTranslate(String task, String sql, SQLException ex) {
        String codes;
        SQLException current;
        String errorCode;
        DataAccessException customException;
        SQLExceptionTranslator customTranslator;
        DataAccessException customDex;
        SQLException sqlEx = ex;
        if ((sqlEx instanceof BatchUpdateException) && sqlEx.getNextException() != null) {
            SQLException nestedSqlEx = sqlEx.getNextException();
            if (nestedSqlEx.getErrorCode() > 0 || nestedSqlEx.getSQLState() != null) {
                this.logger.debug("Using nested SQLException from the BatchUpdateException");
                sqlEx = nestedSqlEx;
            }
        }
        DataAccessException dae = customTranslate(task, sql, sqlEx);
        if (dae != null) {
            return dae;
        }
        if (this.sqlErrorCodes != null && (customTranslator = this.sqlErrorCodes.getCustomSqlExceptionTranslator()) != null && (customDex = customTranslator.translate(task, sql, sqlEx)) != null) {
            return customDex;
        }
        if (this.sqlErrorCodes != null) {
            if (this.sqlErrorCodes.isUseSqlStateForTranslation()) {
                errorCode = sqlEx.getSQLState();
            } else {
                SQLException sQLException = sqlEx;
                while (true) {
                    current = sQLException;
                    if (current.getErrorCode() != 0 || !(current.getCause() instanceof SQLException)) {
                        break;
                    }
                    sQLException = (SQLException) current.getCause();
                }
                errorCode = Integer.toString(current.getErrorCode());
            }
            if (errorCode != null) {
                CustomSQLErrorCodesTranslation[] customTranslations = this.sqlErrorCodes.getCustomTranslations();
                if (customTranslations != null) {
                    for (CustomSQLErrorCodesTranslation customTranslation : customTranslations) {
                        if (Arrays.binarySearch(customTranslation.getErrorCodes(), errorCode) >= 0 && customTranslation.getExceptionClass() != null && (customException = createCustomException(task, sql, sqlEx, customTranslation.getExceptionClass())) != null) {
                            logTranslation(task, sql, sqlEx, true);
                            return customException;
                        }
                    }
                }
                if (Arrays.binarySearch(this.sqlErrorCodes.getBadSqlGrammarCodes(), errorCode) >= 0) {
                    logTranslation(task, sql, sqlEx, false);
                    return new BadSqlGrammarException(task, sql, sqlEx);
                }
                if (Arrays.binarySearch(this.sqlErrorCodes.getInvalidResultSetAccessCodes(), errorCode) >= 0) {
                    logTranslation(task, sql, sqlEx, false);
                    return new InvalidResultSetAccessException(task, sql, sqlEx);
                }
                if (Arrays.binarySearch(this.sqlErrorCodes.getDuplicateKeyCodes(), errorCode) >= 0) {
                    logTranslation(task, sql, sqlEx, false);
                    return new DuplicateKeyException(buildMessage(task, sql, sqlEx), sqlEx);
                }
                if (Arrays.binarySearch(this.sqlErrorCodes.getDataIntegrityViolationCodes(), errorCode) >= 0) {
                    logTranslation(task, sql, sqlEx, false);
                    return new DataIntegrityViolationException(buildMessage(task, sql, sqlEx), sqlEx);
                }
                if (Arrays.binarySearch(this.sqlErrorCodes.getPermissionDeniedCodes(), errorCode) >= 0) {
                    logTranslation(task, sql, sqlEx, false);
                    return new PermissionDeniedDataAccessException(buildMessage(task, sql, sqlEx), sqlEx);
                }
                if (Arrays.binarySearch(this.sqlErrorCodes.getDataAccessResourceFailureCodes(), errorCode) >= 0) {
                    logTranslation(task, sql, sqlEx, false);
                    return new DataAccessResourceFailureException(buildMessage(task, sql, sqlEx), sqlEx);
                }
                if (Arrays.binarySearch(this.sqlErrorCodes.getTransientDataAccessResourceCodes(), errorCode) >= 0) {
                    logTranslation(task, sql, sqlEx, false);
                    return new TransientDataAccessResourceException(buildMessage(task, sql, sqlEx), sqlEx);
                }
                if (Arrays.binarySearch(this.sqlErrorCodes.getCannotAcquireLockCodes(), errorCode) >= 0) {
                    logTranslation(task, sql, sqlEx, false);
                    return new CannotAcquireLockException(buildMessage(task, sql, sqlEx), sqlEx);
                }
                if (Arrays.binarySearch(this.sqlErrorCodes.getDeadlockLoserCodes(), errorCode) >= 0) {
                    logTranslation(task, sql, sqlEx, false);
                    return new DeadlockLoserDataAccessException(buildMessage(task, sql, sqlEx), sqlEx);
                }
                if (Arrays.binarySearch(this.sqlErrorCodes.getCannotSerializeTransactionCodes(), errorCode) >= 0) {
                    logTranslation(task, sql, sqlEx, false);
                    return new CannotSerializeTransactionException(buildMessage(task, sql, sqlEx), sqlEx);
                }
            }
        }
        if (this.logger.isDebugEnabled()) {
            if (this.sqlErrorCodes != null && this.sqlErrorCodes.isUseSqlStateForTranslation()) {
                codes = "SQL state '" + sqlEx.getSQLState() + "', error code '" + sqlEx.getErrorCode();
            } else {
                codes = "Error code '" + sqlEx.getErrorCode() + "'";
            }
            this.logger.debug("Unable to translate SQLException with " + codes + ", will now try the fallback translator");
            return null;
        }
        return null;
    }

    protected DataAccessException customTranslate(String task, String sql, SQLException sqlEx) {
        return null;
    }

    protected DataAccessException createCustomException(String task, String sql, SQLException sqlEx, Class<?> exceptionClass) {
        try {
            int constructorType = 0;
            Constructor<?>[] constructors = exceptionClass.getConstructors();
            for (Constructor<?> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                if (parameterTypes.length == 1 && String.class == parameterTypes[0] && constructorType < 1) {
                    constructorType = 1;
                }
                if (parameterTypes.length == 2 && String.class == parameterTypes[0] && Throwable.class == parameterTypes[1] && constructorType < 2) {
                    constructorType = 2;
                }
                if (parameterTypes.length == 2 && String.class == parameterTypes[0] && SQLException.class == parameterTypes[1] && constructorType < 3) {
                    constructorType = 3;
                }
                if (parameterTypes.length == 3 && String.class == parameterTypes[0] && String.class == parameterTypes[1] && Throwable.class == parameterTypes[2] && constructorType < 4) {
                    constructorType = 4;
                }
                if (parameterTypes.length == 3 && String.class == parameterTypes[0] && String.class == parameterTypes[1] && SQLException.class == parameterTypes[2] && constructorType < 5) {
                    constructorType = 5;
                }
            }
            switch (constructorType) {
                case 1:
                    Class<?>[] messageOnlyArgsClass = {String.class};
                    Object[] messageOnlyArgs = {task + ": " + sqlEx.getMessage()};
                    Constructor<?> exceptionConstructor = exceptionClass.getConstructor(messageOnlyArgsClass);
                    return (DataAccessException) exceptionConstructor.newInstance(messageOnlyArgs);
                case 2:
                    Class<?>[] messageAndThrowableArgsClass = {String.class, Throwable.class};
                    Object[] messageAndThrowableArgs = {task + ": " + sqlEx.getMessage(), sqlEx};
                    Constructor<?> exceptionConstructor2 = exceptionClass.getConstructor(messageAndThrowableArgsClass);
                    return (DataAccessException) exceptionConstructor2.newInstance(messageAndThrowableArgs);
                case 3:
                    Class<?>[] messageAndSqlExArgsClass = {String.class, SQLException.class};
                    Object[] messageAndSqlExArgs = {task + ": " + sqlEx.getMessage(), sqlEx};
                    Constructor<?> exceptionConstructor3 = exceptionClass.getConstructor(messageAndSqlExArgsClass);
                    return (DataAccessException) exceptionConstructor3.newInstance(messageAndSqlExArgs);
                case 4:
                    Class<?>[] messageAndSqlAndThrowableArgsClass = {String.class, String.class, Throwable.class};
                    Object[] messageAndSqlAndThrowableArgs = {task, sql, sqlEx};
                    Constructor<?> exceptionConstructor4 = exceptionClass.getConstructor(messageAndSqlAndThrowableArgsClass);
                    return (DataAccessException) exceptionConstructor4.newInstance(messageAndSqlAndThrowableArgs);
                case 5:
                    Class<?>[] messageAndSqlAndSqlExArgsClass = {String.class, String.class, SQLException.class};
                    Object[] messageAndSqlAndSqlExArgs = {task, sql, sqlEx};
                    Constructor<?> exceptionConstructor5 = exceptionClass.getConstructor(messageAndSqlAndSqlExArgsClass);
                    return (DataAccessException) exceptionConstructor5.newInstance(messageAndSqlAndSqlExArgs);
                default:
                    if (this.logger.isWarnEnabled()) {
                        this.logger.warn("Unable to find appropriate constructor of custom exception class [" + exceptionClass.getName() + "]");
                        return null;
                    }
                    return null;
            }
        } catch (Throwable ex) {
            if (this.logger.isWarnEnabled()) {
                this.logger.warn("Unable to instantiate custom exception class [" + exceptionClass.getName() + "]", ex);
                return null;
            }
            return null;
        }
    }

    private void logTranslation(String task, String sql, SQLException sqlEx, boolean custom) {
        if (this.logger.isDebugEnabled()) {
            String intro = custom ? "Custom translation of" : "Translating";
            this.logger.debug(intro + " SQLException with SQL state '" + sqlEx.getSQLState() + "', error code '" + sqlEx.getErrorCode() + "', message [" + sqlEx.getMessage() + "]; SQL was [" + sql + "] for task [" + task + "]");
        }
    }
}
