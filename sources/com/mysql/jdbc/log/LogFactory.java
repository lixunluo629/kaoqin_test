package com.mysql.jdbc.log;

import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Util;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/log/LogFactory.class */
public class LogFactory {
    public static Log getLogger(String className, String instanceName, ExceptionInterceptor exceptionInterceptor) throws SQLException, NoSuchMethodException, ClassNotFoundException, SecurityException {
        Class<?> loggerClass;
        if (className == null) {
            throw SQLError.createSQLException("Logger class can not be NULL", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
        if (instanceName == null) {
            throw SQLError.createSQLException("Logger instance name can not be NULL", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
        try {
            try {
                loggerClass = Class.forName(className);
            } catch (ClassCastException cce) {
                SQLException sqlEx = SQLError.createSQLException("Logger class '" + className + "' does not implement the '" + Log.class.getName() + "' interface", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
                sqlEx.initCause(cce);
                throw sqlEx;
            } catch (ClassNotFoundException cnfe) {
                SQLException sqlEx2 = SQLError.createSQLException("Unable to load class for logger '" + className + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
                sqlEx2.initCause(cnfe);
                throw sqlEx2;
            } catch (IllegalAccessException iae) {
                SQLException sqlEx3 = SQLError.createSQLException("Unable to instantiate logger class '" + className + "', constructor not public", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
                sqlEx3.initCause(iae);
                throw sqlEx3;
            } catch (InstantiationException inse) {
                SQLException sqlEx4 = SQLError.createSQLException("Unable to instantiate logger class '" + className + "', exception in constructor?", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
                sqlEx4.initCause(inse);
                throw sqlEx4;
            } catch (NoSuchMethodException nsme) {
                SQLException sqlEx5 = SQLError.createSQLException("Logger class does not have a single-arg constructor that takes an instance name", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
                sqlEx5.initCause(nsme);
                throw sqlEx5;
            } catch (InvocationTargetException ite) {
                SQLException sqlEx6 = SQLError.createSQLException("Unable to instantiate logger class '" + className + "', exception in constructor?", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
                sqlEx6.initCause(ite);
                throw sqlEx6;
            }
        } catch (ClassNotFoundException e) {
            loggerClass = Class.forName(Util.getPackageName(Log.class) + "." + className);
        }
        Constructor<?> constructor = loggerClass.getConstructor(String.class);
        return (Log) constructor.newInstance(instanceName);
    }
}
