package com.mysql.fabric.xmlrpc.exceptions;

import com.mysql.fabric.xmlrpc.base.Fault;
import com.mysql.fabric.xmlrpc.base.Struct;
import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/xmlrpc/exceptions/MySQLFabricException.class */
public class MySQLFabricException extends SQLException {
    static final long serialVersionUID = -8776763137552613517L;

    public MySQLFabricException() {
    }

    public MySQLFabricException(Fault fault) {
        super((String) ((Struct) fault.getValue().getValue()).getMember().get(1).getValue().getValue(), "", ((Integer) ((Struct) fault.getValue().getValue()).getMember().get(0).getValue().getValue()).intValue());
    }

    public MySQLFabricException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public MySQLFabricException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public MySQLFabricException(String reason) {
        super(reason);
    }
}
