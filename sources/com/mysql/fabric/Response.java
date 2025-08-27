package com.mysql.fabric;

import com.mysql.fabric.proto.xmlrpc.ResultSetParser;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.tags.form.TextareaTag;
import redis.clients.jedis.Protocol;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/Response.class */
public class Response {
    private int protocolVersion;
    private String fabricUuid;
    private int ttl;
    private String errorMessage;
    private List<Map<String, ?>> resultSet;

    public Response(List<?> responseData) throws FabricCommunicationException {
        this.protocolVersion = ((Integer) responseData.get(0)).intValue();
        if (this.protocolVersion != 1) {
            throw new FabricCommunicationException("Unknown protocol version: " + this.protocolVersion);
        }
        this.fabricUuid = (String) responseData.get(1);
        this.ttl = ((Integer) responseData.get(2)).intValue();
        this.errorMessage = (String) responseData.get(3);
        if ("".equals(this.errorMessage)) {
            this.errorMessage = null;
        }
        List<Map<String, ?>> resultSets = (List) responseData.get(4);
        if (resultSets.size() > 0) {
            Map<String, ?> resultData = resultSets.get(0);
            this.resultSet = new ResultSetParser().parse((Map) resultData.get(Protocol.CLUSTER_INFO), (List) resultData.get(TextareaTag.ROWS_ATTRIBUTE));
        }
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public String getFabricUuid() {
        return this.fabricUuid;
    }

    public int getTtl() {
        return this.ttl;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public List<Map<String, ?>> getResultSet() {
        return this.resultSet;
    }
}
