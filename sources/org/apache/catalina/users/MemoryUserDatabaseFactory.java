package org.apache.catalina.users;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/users/MemoryUserDatabaseFactory.class */
public class MemoryUserDatabaseFactory implements ObjectFactory {
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        if (obj == null || !(obj instanceof Reference)) {
            return null;
        }
        Reference ref = (Reference) obj;
        if (!"org.apache.catalina.UserDatabase".equals(ref.getClassName())) {
            return null;
        }
        MemoryUserDatabase database = new MemoryUserDatabase(name.toString());
        RefAddr ra = ref.get("pathname");
        if (ra != null) {
            database.setPathname(ra.getContent().toString());
        }
        RefAddr ra2 = ref.get("readonly");
        if (ra2 != null) {
            database.setReadonly(Boolean.parseBoolean(ra2.getContent().toString()));
        }
        RefAddr ra3 = ref.get("watchSource");
        if (ra3 != null) {
            database.setWatchSource(Boolean.parseBoolean(ra3.getContent().toString()));
        }
        database.open();
        if (!database.getReadonly()) {
            database.save();
        }
        return database;
    }
}
