package com.mysql.fabric.xmlrpc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/xmlrpc/Client.class */
public class Client {
    private URL url;
    private Map<String, String> headers = new HashMap();

    public Client(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public void setHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public void clearHeader(String name) {
        this.headers.remove(name);
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    public com.mysql.fabric.xmlrpc.base.MethodResponse execute(com.mysql.fabric.xmlrpc.base.MethodCall r5) throws java.io.IOException, javax.xml.parsers.ParserConfigurationException, org.xml.sax.SAXException, com.mysql.fabric.xmlrpc.exceptions.MySQLFabricException {
        /*
            Method dump skipped, instructions count: 241
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.fabric.xmlrpc.Client.execute(com.mysql.fabric.xmlrpc.base.MethodCall):com.mysql.fabric.xmlrpc.base.MethodResponse");
    }
}
