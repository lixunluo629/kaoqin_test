package org.apache.catalina.startup;

import org.apache.catalina.Context;
import org.apache.catalina.deploy.NamingResourcesImpl;
import org.apache.tomcat.util.IntrospectionUtils;
import org.apache.tomcat.util.digester.Rule;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/startup/SetNextNamingRule.class */
public class SetNextNamingRule extends Rule {
    protected final String methodName;
    protected final String paramType;

    public SetNextNamingRule(String methodName, String paramType) {
        this.methodName = methodName;
        this.paramType = paramType;
    }

    @Override // org.apache.tomcat.util.digester.Rule
    public void end(String namespace, String name) throws Exception {
        NamingResourcesImpl namingResources;
        Object child = this.digester.peek(0);
        Object parent = this.digester.peek(1);
        if (parent instanceof Context) {
            namingResources = ((Context) parent).getNamingResources();
        } else {
            namingResources = (NamingResourcesImpl) parent;
        }
        IntrospectionUtils.callMethod1(namingResources, this.methodName, child, this.paramType, this.digester.getClassLoader());
    }

    public String toString() {
        return "SetNextRule[methodName=" + this.methodName + ", paramType=" + this.paramType + "]";
    }
}
