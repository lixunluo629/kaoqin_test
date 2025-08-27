package org.apache.commons.lang;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/IllegalClassException.class */
public class IllegalClassException extends IllegalArgumentException {
    private static final long serialVersionUID = 8063272569377254819L;

    public IllegalClassException(Class expected, Object actual) {
        super(new StringBuffer().append("Expected: ").append(safeGetClassName(expected)).append(", actual: ").append(actual == null ? "null" : actual.getClass().getName()).toString());
    }

    public IllegalClassException(Class expected, Class actual) {
        super(new StringBuffer().append("Expected: ").append(safeGetClassName(expected)).append(", actual: ").append(safeGetClassName(actual)).toString());
    }

    public IllegalClassException(String message) {
        super(message);
    }

    private static final String safeGetClassName(Class cls) {
        if (cls == null) {
            return null;
        }
        return cls.getName();
    }
}
