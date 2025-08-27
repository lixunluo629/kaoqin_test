package org.apache.catalina.mbeans;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/mbeans/ClassNameMBean.class */
public class ClassNameMBean<T> extends BaseCatalinaMBean<T> {
    @Override // org.apache.tomcat.util.modeler.BaseModelMBean
    public String getClassName() {
        return this.resource.getClass().getName();
    }
}
