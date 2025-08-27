package org.apache.catalina.users;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.catalina.Group;
import org.apache.catalina.Role;
import org.apache.catalina.UserDatabase;
import org.apache.tomcat.util.buf.StringUtils;
import org.apache.tomcat.util.security.Escape;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/users/MemoryUser.class */
public class MemoryUser extends AbstractUser {
    protected final MemoryUserDatabase database;
    protected final ArrayList<Group> groups = new ArrayList<>();
    protected final ArrayList<Role> roles = new ArrayList<>();

    MemoryUser(MemoryUserDatabase database, String username, String password, String fullName) {
        this.database = database;
        setUsername(username);
        setPassword(password);
        setFullName(fullName);
    }

    @Override // org.apache.catalina.users.AbstractUser, org.apache.catalina.User
    public Iterator<Group> getGroups() {
        Iterator<Group> it;
        synchronized (this.groups) {
            it = this.groups.iterator();
        }
        return it;
    }

    @Override // org.apache.catalina.users.AbstractUser, org.apache.catalina.User
    public Iterator<Role> getRoles() {
        Iterator<Role> it;
        synchronized (this.roles) {
            it = this.roles.iterator();
        }
        return it;
    }

    @Override // org.apache.catalina.User
    public UserDatabase getUserDatabase() {
        return this.database;
    }

    @Override // org.apache.catalina.users.AbstractUser, org.apache.catalina.User
    public void addGroup(Group group) {
        synchronized (this.groups) {
            if (!this.groups.contains(group)) {
                this.groups.add(group);
            }
        }
    }

    @Override // org.apache.catalina.users.AbstractUser, org.apache.catalina.User
    public void addRole(Role role) {
        synchronized (this.roles) {
            if (!this.roles.contains(role)) {
                this.roles.add(role);
            }
        }
    }

    @Override // org.apache.catalina.users.AbstractUser, org.apache.catalina.User
    public boolean isInGroup(Group group) {
        boolean zContains;
        synchronized (this.groups) {
            zContains = this.groups.contains(group);
        }
        return zContains;
    }

    @Override // org.apache.catalina.users.AbstractUser, org.apache.catalina.User
    public boolean isInRole(Role role) {
        boolean zContains;
        synchronized (this.roles) {
            zContains = this.roles.contains(role);
        }
        return zContains;
    }

    @Override // org.apache.catalina.users.AbstractUser, org.apache.catalina.User
    public void removeGroup(Group group) {
        synchronized (this.groups) {
            this.groups.remove(group);
        }
    }

    @Override // org.apache.catalina.users.AbstractUser, org.apache.catalina.User
    public void removeGroups() {
        synchronized (this.groups) {
            this.groups.clear();
        }
    }

    @Override // org.apache.catalina.users.AbstractUser, org.apache.catalina.User
    public void removeRole(Role role) {
        synchronized (this.roles) {
            this.roles.remove(role);
        }
    }

    @Override // org.apache.catalina.users.AbstractUser, org.apache.catalina.User
    public void removeRoles() {
        synchronized (this.roles) {
            this.roles.clear();
        }
    }

    public String toXml() {
        StringBuilder sb = new StringBuilder("<user username=\"");
        sb.append(Escape.xml(this.username));
        sb.append("\" password=\"");
        sb.append(Escape.xml(this.password));
        sb.append(SymbolConstants.QUOTES_SYMBOL);
        if (this.fullName != null) {
            sb.append(" fullName=\"");
            sb.append(Escape.xml(this.fullName));
            sb.append(SymbolConstants.QUOTES_SYMBOL);
        }
        synchronized (this.groups) {
            if (this.groups.size() > 0) {
                sb.append(" groups=\"");
                StringUtils.join((Iterable) this.groups, ',', (StringUtils.Function) new StringUtils.Function<Group>() { // from class: org.apache.catalina.users.MemoryUser.1
                    @Override // org.apache.tomcat.util.buf.StringUtils.Function
                    public String apply(Group t) {
                        return Escape.xml(t.getGroupname());
                    }
                }, sb);
                sb.append(SymbolConstants.QUOTES_SYMBOL);
            }
        }
        synchronized (this.roles) {
            if (this.roles.size() > 0) {
                sb.append(" roles=\"");
                StringUtils.join((Iterable) this.roles, ',', (StringUtils.Function) new StringUtils.Function<Role>() { // from class: org.apache.catalina.users.MemoryUser.2
                    @Override // org.apache.tomcat.util.buf.StringUtils.Function
                    public String apply(Role t) {
                        return Escape.xml(t.getRolename());
                    }
                }, sb);
                sb.append(SymbolConstants.QUOTES_SYMBOL);
            }
        }
        sb.append("/>");
        return sb.toString();
    }

    @Override // java.security.Principal
    public String toString() {
        StringBuilder sb = new StringBuilder("User username=\"");
        sb.append(Escape.xml(this.username));
        sb.append(SymbolConstants.QUOTES_SYMBOL);
        if (this.fullName != null) {
            sb.append(", fullName=\"");
            sb.append(Escape.xml(this.fullName));
            sb.append(SymbolConstants.QUOTES_SYMBOL);
        }
        synchronized (this.groups) {
            if (this.groups.size() > 0) {
                sb.append(", groups=\"");
                StringUtils.join((Iterable) this.groups, ',', (StringUtils.Function) new StringUtils.Function<Group>() { // from class: org.apache.catalina.users.MemoryUser.3
                    @Override // org.apache.tomcat.util.buf.StringUtils.Function
                    public String apply(Group t) {
                        return Escape.xml(t.getGroupname());
                    }
                }, sb);
                sb.append(SymbolConstants.QUOTES_SYMBOL);
            }
        }
        synchronized (this.roles) {
            if (this.roles.size() > 0) {
                sb.append(", roles=\"");
                StringUtils.join((Iterable) this.roles, ',', (StringUtils.Function) new StringUtils.Function<Role>() { // from class: org.apache.catalina.users.MemoryUser.4
                    @Override // org.apache.tomcat.util.buf.StringUtils.Function
                    public String apply(Role t) {
                        return Escape.xml(t.getRolename());
                    }
                }, sb);
                sb.append(SymbolConstants.QUOTES_SYMBOL);
            }
        }
        return sb.toString();
    }
}
