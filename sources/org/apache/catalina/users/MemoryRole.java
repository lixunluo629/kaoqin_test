package org.apache.catalina.users;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.apache.catalina.UserDatabase;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/users/MemoryRole.class */
public class MemoryRole extends AbstractRole {
    protected final MemoryUserDatabase database;

    MemoryRole(MemoryUserDatabase database, String rolename, String description) {
        this.database = database;
        setRolename(rolename);
        setDescription(description);
    }

    @Override // org.apache.catalina.users.AbstractRole, org.apache.catalina.Role
    public UserDatabase getUserDatabase() {
        return this.database;
    }

    @Override // java.security.Principal
    public String toString() {
        StringBuilder sb = new StringBuilder("<role rolename=\"");
        sb.append(this.rolename);
        sb.append(SymbolConstants.QUOTES_SYMBOL);
        if (this.description != null) {
            sb.append(" description=\"");
            sb.append(this.description);
            sb.append(SymbolConstants.QUOTES_SYMBOL);
        }
        sb.append("/>");
        return sb.toString();
    }
}
