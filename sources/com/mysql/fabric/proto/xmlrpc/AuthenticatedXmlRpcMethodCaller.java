package com.mysql.fabric.proto.xmlrpc;

import com.mysql.fabric.FabricCommunicationException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/proto/xmlrpc/AuthenticatedXmlRpcMethodCaller.class */
public class AuthenticatedXmlRpcMethodCaller implements XmlRpcMethodCaller {
    private XmlRpcMethodCaller underlyingCaller;
    private String url;
    private String username;
    private String password;

    public AuthenticatedXmlRpcMethodCaller(XmlRpcMethodCaller underlyingCaller, String url, String username, String password) {
        this.underlyingCaller = underlyingCaller;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override // com.mysql.fabric.proto.xmlrpc.XmlRpcMethodCaller
    public void setHeader(String name, String value) {
        this.underlyingCaller.setHeader(name, value);
    }

    @Override // com.mysql.fabric.proto.xmlrpc.XmlRpcMethodCaller
    public void clearHeader(String name) {
        this.underlyingCaller.clearHeader(name);
    }

    @Override // com.mysql.fabric.proto.xmlrpc.XmlRpcMethodCaller
    public List<?> call(String methodName, Object[] args) throws NoSuchAlgorithmException, FabricCommunicationException {
        try {
            String authenticateHeader = DigestAuthentication.getChallengeHeader(this.url);
            Map<String, String> digestChallenge = DigestAuthentication.parseDigestChallenge(authenticateHeader);
            String authorizationHeader = DigestAuthentication.generateAuthorizationHeader(digestChallenge, this.username, this.password);
            this.underlyingCaller.setHeader("Authorization", authorizationHeader);
            return this.underlyingCaller.call(methodName, args);
        } catch (IOException ex) {
            throw new FabricCommunicationException("Unable to obtain challenge header for authentication", ex);
        }
    }
}
