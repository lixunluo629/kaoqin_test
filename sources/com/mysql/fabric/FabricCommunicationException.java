package com.mysql.fabric;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/FabricCommunicationException.class */
public class FabricCommunicationException extends Exception {
    private static final long serialVersionUID = 1;

    public FabricCommunicationException(Throwable cause) {
        super(cause);
    }

    public FabricCommunicationException(String message) {
        super(message);
    }

    public FabricCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
