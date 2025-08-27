package org.apache.commons.lang;

import java.util.Arrays;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/IncompleteArgumentException.class */
public class IncompleteArgumentException extends IllegalArgumentException {
    private static final long serialVersionUID = 4954193403612068178L;

    public IncompleteArgumentException(String argName) {
        super(new StringBuffer().append(argName).append(" is incomplete.").toString());
    }

    public IncompleteArgumentException(String argName, String[] items) {
        super(new StringBuffer().append(argName).append(" is missing the following items: ").append(safeArrayToString(items)).toString());
    }

    private static final String safeArrayToString(Object[] array) {
        if (array == null) {
            return null;
        }
        return Arrays.asList(array).toString();
    }
}
