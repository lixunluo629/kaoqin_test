package org.apache.catalina.valves.rewrite;

import java.nio.charset.Charset;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/valves/rewrite/Resolver.class */
public abstract class Resolver {
    public abstract String resolve(String str);

    public abstract String resolveSsl(String str);

    public abstract String resolveHttp(String str);

    public abstract boolean resolveResource(int i, String str);

    @Deprecated
    public abstract String getUriEncoding();

    public abstract Charset getUriCharset();

    public String resolveEnv(String key) {
        return System.getProperty(key);
    }
}
