package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/StandardLoadBalanceExceptionChecker.class */
public class StandardLoadBalanceExceptionChecker implements LoadBalanceExceptionChecker {
    private List<String> sqlStateList;
    private List<Class<?>> sqlExClassList;

    @Override // com.mysql.jdbc.LoadBalanceExceptionChecker
    public boolean shouldExceptionTriggerFailover(SQLException ex) {
        String sqlState = ex.getSQLState();
        if (sqlState != null) {
            if (sqlState.startsWith("08")) {
                return true;
            }
            if (this.sqlStateList != null) {
                Iterator<String> i = this.sqlStateList.iterator();
                while (i.hasNext()) {
                    if (sqlState.startsWith(i.next().toString())) {
                        return true;
                    }
                }
            }
        }
        if (ex instanceof CommunicationsException) {
            return true;
        }
        if (this.sqlExClassList != null) {
            Iterator<Class<?>> i2 = this.sqlExClassList.iterator();
            while (i2.hasNext()) {
                if (i2.next().isInstance(ex)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // com.mysql.jdbc.Extension
    public void destroy() {
    }

    @Override // com.mysql.jdbc.Extension
    public void init(Connection conn, Properties props) throws SQLException, ClassNotFoundException {
        configureSQLStateList(props.getProperty("loadBalanceSQLStateFailover", null));
        configureSQLExceptionSubclassList(props.getProperty("loadBalanceSQLExceptionSubclassFailover", null));
    }

    private void configureSQLStateList(String sqlStates) {
        if (sqlStates == null || "".equals(sqlStates)) {
            return;
        }
        List<String> states = StringUtils.split(sqlStates, ",", true);
        List<String> newStates = new ArrayList<>();
        for (String state : states) {
            if (state.length() > 0) {
                newStates.add(state);
            }
        }
        if (newStates.size() > 0) {
            this.sqlStateList = newStates;
        }
    }

    private void configureSQLExceptionSubclassList(String sqlExClasses) throws ClassNotFoundException {
        if (sqlExClasses == null || "".equals(sqlExClasses)) {
            return;
        }
        List<String> classes = StringUtils.split(sqlExClasses, ",", true);
        List<Class<?>> newClasses = new ArrayList<>();
        for (String exClass : classes) {
            try {
                Class<?> c = Class.forName(exClass);
                newClasses.add(c);
            } catch (Exception e) {
            }
        }
        if (newClasses.size() > 0) {
            this.sqlExClassList = newClasses;
        }
    }
}
