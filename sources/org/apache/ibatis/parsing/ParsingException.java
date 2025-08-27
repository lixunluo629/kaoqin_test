package org.apache.ibatis.parsing;

import org.apache.ibatis.exceptions.PersistenceException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/parsing/ParsingException.class */
public class ParsingException extends PersistenceException {
    private static final long serialVersionUID = -176685891441325943L;

    public ParsingException() {
    }

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParsingException(Throwable cause) {
        super(cause);
    }
}
