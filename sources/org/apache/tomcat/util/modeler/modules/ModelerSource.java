package org.apache.tomcat.util.modeler.modules;

import java.util.List;
import javax.management.ObjectName;
import org.apache.tomcat.util.modeler.Registry;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/modeler/modules/ModelerSource.class */
public abstract class ModelerSource {
    protected Object source;

    public abstract List<ObjectName> loadDescriptors(Registry registry, String str, Object obj) throws Exception;
}
