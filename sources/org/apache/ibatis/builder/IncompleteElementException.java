package org.apache.ibatis.builder;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/builder/IncompleteElementException.class */
public class IncompleteElementException extends BuilderException {
    private static final long serialVersionUID = -3697292286890900315L;

    public IncompleteElementException() {
    }

    public IncompleteElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompleteElementException(String message) {
        super(message);
    }

    public IncompleteElementException(Throwable cause) {
        super(cause);
    }
}
