package com.mysql.jdbc.log;

import com.mysql.jdbc.Util;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/log/LogUtils.class */
public class LogUtils {
    public static final String CALLER_INFORMATION_NOT_AVAILABLE = "Caller information not available";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final int LINE_SEPARATOR_LENGTH = LINE_SEPARATOR.length();

    public static String findCallingClassAndMethod(Throwable t) {
        int endOfLine;
        String stackTraceAsString = Util.stackTraceToString(t);
        String callingClassAndMethod = CALLER_INFORMATION_NOT_AVAILABLE;
        int endInternalMethods = stackTraceAsString.lastIndexOf("com.mysql.jdbc");
        if (endInternalMethods != -1) {
            int compliancePackage = stackTraceAsString.indexOf("com.mysql.jdbc.compliance", endInternalMethods);
            if (compliancePackage != -1) {
                endOfLine = compliancePackage - LINE_SEPARATOR_LENGTH;
            } else {
                endOfLine = stackTraceAsString.indexOf(LINE_SEPARATOR, endInternalMethods);
            }
            if (endOfLine != -1) {
                int nextEndOfLine = stackTraceAsString.indexOf(LINE_SEPARATOR, endOfLine + LINE_SEPARATOR_LENGTH);
                callingClassAndMethod = nextEndOfLine != -1 ? stackTraceAsString.substring(endOfLine + LINE_SEPARATOR_LENGTH, nextEndOfLine) : stackTraceAsString.substring(endOfLine + LINE_SEPARATOR_LENGTH);
            }
        }
        if (!callingClassAndMethod.startsWith("\tat ") && !callingClassAndMethod.startsWith("at ")) {
            return "at " + callingClassAndMethod;
        }
        return callingClassAndMethod;
    }
}
