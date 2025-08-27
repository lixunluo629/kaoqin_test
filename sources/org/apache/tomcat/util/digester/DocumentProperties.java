package org.apache.tomcat.util.digester;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/digester/DocumentProperties.class */
public interface DocumentProperties {

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/digester/DocumentProperties$Charset.class */
    public interface Charset {
        void setCharset(java.nio.charset.Charset charset);
    }

    @Deprecated
    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/digester/DocumentProperties$Encoding.class */
    public interface Encoding {
        void setEncoding(String str);
    }
}
