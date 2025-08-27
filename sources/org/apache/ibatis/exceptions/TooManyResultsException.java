package org.apache.ibatis.exceptions;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/exceptions/TooManyResultsException.class */
public class TooManyResultsException extends PersistenceException {
    private static final long serialVersionUID = 8935197089745865786L;

    public TooManyResultsException() {
    }

    public TooManyResultsException(String message) {
        super(message);
    }

    public TooManyResultsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyResultsException(Throwable cause) {
        super(cause);
    }
}
