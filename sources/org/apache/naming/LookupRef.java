package org.apache.naming;

import javax.naming.StringRefAddr;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/naming/LookupRef.class */
public class LookupRef extends AbstractRef {
    private static final long serialVersionUID = 1;
    public static final String LOOKUP_NAME = "lookup-name";

    public LookupRef(String resourceType, String lookupName) {
        this(resourceType, null, null, lookupName);
    }

    public LookupRef(String resourceType, String factory, String factoryLocation, String lookupName) {
        super(resourceType, factory, factoryLocation);
        if (lookupName != null && !lookupName.equals("")) {
            add(new StringRefAddr(LOOKUP_NAME, lookupName));
        }
    }

    @Override // org.apache.naming.AbstractRef
    protected String getDefaultFactoryClassName() {
        return org.apache.naming.factory.Constants.DEFAULT_LOOKUP_JNDI_FACTORY;
    }
}
