package org.apache.tomcat.util.net.openssl;

import java.io.Serializable;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/net/openssl/OpenSSLConfCmd.class */
public class OpenSSLConfCmd implements Serializable {
    private static final long serialVersionUID = 1;
    private String name = null;
    private String value = null;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
