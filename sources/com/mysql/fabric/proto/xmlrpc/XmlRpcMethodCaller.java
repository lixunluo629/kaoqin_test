package com.mysql.fabric.proto.xmlrpc;

import com.mysql.fabric.FabricCommunicationException;
import java.util.List;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/proto/xmlrpc/XmlRpcMethodCaller.class */
public interface XmlRpcMethodCaller {
    List<?> call(String str, Object[] objArr) throws FabricCommunicationException;

    void setHeader(String str, String str2);

    void clearHeader(String str);
}
