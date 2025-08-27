package org.apache.catalina.loader;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/loader/ResourceEntry.class */
public class ResourceEntry {
    public long lastModified = -1;
    public volatile Class<?> loadedClass = null;
}
