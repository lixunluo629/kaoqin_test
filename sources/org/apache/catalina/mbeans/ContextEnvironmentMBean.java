package org.apache.catalina.mbeans;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import org.apache.tomcat.util.descriptor.web.ContextEnvironment;
import org.apache.tomcat.util.descriptor.web.NamingResources;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/mbeans/ContextEnvironmentMBean.class */
public class ContextEnvironmentMBean extends BaseCatalinaMBean<ContextEnvironment> {
    @Override // org.apache.tomcat.util.modeler.BaseModelMBean
    public void setAttribute(Attribute attribute) throws MBeanException, AttributeNotFoundException, ReflectionException {
        super.setAttribute(attribute);
        ContextEnvironment ce = doGetManagedResource();
        NamingResources nr = ce.getNamingResources();
        nr.removeEnvironment(ce.getName());
        nr.addEnvironment(ce);
    }
}
