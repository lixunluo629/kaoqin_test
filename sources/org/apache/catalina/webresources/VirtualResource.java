package org.apache.catalina.webresources;

import org.apache.catalina.WebResourceRoot;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/webresources/VirtualResource.class */
public class VirtualResource extends EmptyResource {
    private final String name;

    public VirtualResource(WebResourceRoot root, String webAppPath, String name) {
        super(root, webAppPath);
        this.name = name;
    }

    @Override // org.apache.catalina.webresources.EmptyResource, org.apache.catalina.WebResource
    public boolean isVirtual() {
        return true;
    }

    @Override // org.apache.catalina.webresources.EmptyResource, org.apache.catalina.WebResource
    public boolean isDirectory() {
        return true;
    }

    @Override // org.apache.catalina.webresources.EmptyResource, org.apache.catalina.WebResource
    public String getName() {
        return this.name;
    }
}
