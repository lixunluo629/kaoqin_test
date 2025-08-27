package com.mysql.jdbc.util;

import ch.qos.logback.core.joran.action.ActionConst;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/util/ResultSetUtil.class */
public class ResultSetUtil {
    public static StringBuilder appendResultSetSlashGStyle(StringBuilder appendTo, ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int numFields = rsmd.getColumnCount();
        int maxWidth = 0;
        String[] fieldNames = new String[numFields];
        for (int i = 0; i < numFields; i++) {
            fieldNames[i] = rsmd.getColumnLabel(i + 1);
            if (fieldNames[i].length() > maxWidth) {
                maxWidth = fieldNames[i].length();
            }
        }
        int rowCount = 1;
        while (rs.next()) {
            appendTo.append("*************************** ");
            int i2 = rowCount;
            rowCount++;
            appendTo.append(i2);
            appendTo.append(". row ***************************\n");
            for (int i3 = 0; i3 < numFields; i3++) {
                int leftPad = maxWidth - fieldNames[i3].length();
                for (int j = 0; j < leftPad; j++) {
                    appendTo.append(SymbolConstants.SPACE_SYMBOL);
                }
                appendTo.append(fieldNames[i3]);
                appendTo.append(": ");
                String stringVal = rs.getString(i3 + 1);
                if (stringVal != null) {
                    appendTo.append(stringVal);
                } else {
                    appendTo.append(ActionConst.NULL);
                }
                appendTo.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
            appendTo.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        return appendTo;
    }
}
