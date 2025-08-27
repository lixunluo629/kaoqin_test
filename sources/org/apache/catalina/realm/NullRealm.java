package org.apache.catalina.realm;

import java.security.Principal;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/realm/NullRealm.class */
public class NullRealm extends RealmBase {
    private static final String NAME = "NullRealm";

    @Override // org.apache.catalina.realm.RealmBase
    @Deprecated
    protected String getName() {
        return NAME;
    }

    @Override // org.apache.catalina.realm.RealmBase
    protected String getPassword(String username) {
        return null;
    }

    @Override // org.apache.catalina.realm.RealmBase
    protected Principal getPrincipal(String username) {
        return null;
    }
}
