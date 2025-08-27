package com.mysql.jdbc;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/AssertionFailedException.class */
public class AssertionFailedException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public static void shouldNotHappen(Exception ex) throws AssertionFailedException {
        throw new AssertionFailedException(ex);
    }

    public AssertionFailedException(Exception ex) {
        super(Messages.getString("AssertionFailedException.0") + ex.toString() + Messages.getString("AssertionFailedException.1"));
    }
}
