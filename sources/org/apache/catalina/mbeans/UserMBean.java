package org.apache.catalina.mbeans;

import java.util.ArrayList;
import java.util.Iterator;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.apache.catalina.Group;
import org.apache.catalina.Role;
import org.apache.catalina.User;
import org.apache.tomcat.util.modeler.BaseModelMBean;
import org.apache.tomcat.util.modeler.ManagedBean;
import org.apache.tomcat.util.modeler.Registry;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/mbeans/UserMBean.class */
public class UserMBean extends BaseModelMBean {
    protected final Registry registry = MBeanUtils.createRegistry();
    protected final ManagedBean managed = this.registry.findManagedBean("User");

    public String[] getGroups() {
        User user = (User) this.resource;
        ArrayList<String> results = new ArrayList<>();
        Iterator<Group> groups = user.getGroups();
        while (groups.hasNext()) {
            Group group = null;
            try {
                group = groups.next();
                ObjectName oname = MBeanUtils.createObjectName(this.managed.getDomain(), group);
                results.add(oname.toString());
            } catch (MalformedObjectNameException e) {
                IllegalArgumentException iae = new IllegalArgumentException("Cannot create object name for group " + group);
                iae.initCause(e);
                throw iae;
            }
        }
        return (String[]) results.toArray(new String[results.size()]);
    }

    public String[] getRoles() {
        User user = (User) this.resource;
        ArrayList<String> results = new ArrayList<>();
        Iterator<Role> roles = user.getRoles();
        while (roles.hasNext()) {
            Role role = null;
            try {
                role = roles.next();
                ObjectName oname = MBeanUtils.createObjectName(this.managed.getDomain(), role);
                results.add(oname.toString());
            } catch (MalformedObjectNameException e) {
                IllegalArgumentException iae = new IllegalArgumentException("Cannot create object name for role " + role);
                iae.initCause(e);
                throw iae;
            }
        }
        return (String[]) results.toArray(new String[results.size()]);
    }

    public void addGroup(String groupname) {
        User user = (User) this.resource;
        if (user == null) {
            return;
        }
        Group group = user.getUserDatabase().findGroup(groupname);
        if (group == null) {
            throw new IllegalArgumentException("Invalid group name '" + groupname + "'");
        }
        user.addGroup(group);
    }

    public void addRole(String rolename) {
        User user = (User) this.resource;
        if (user == null) {
            return;
        }
        Role role = user.getUserDatabase().findRole(rolename);
        if (role == null) {
            throw new IllegalArgumentException("Invalid role name '" + rolename + "'");
        }
        user.addRole(role);
    }

    public void removeGroup(String groupname) {
        User user = (User) this.resource;
        if (user == null) {
            return;
        }
        Group group = user.getUserDatabase().findGroup(groupname);
        if (group == null) {
            throw new IllegalArgumentException("Invalid group name '" + groupname + "'");
        }
        user.removeGroup(group);
    }

    public void removeRole(String rolename) {
        User user = (User) this.resource;
        if (user == null) {
            return;
        }
        Role role = user.getUserDatabase().findRole(rolename);
        if (role == null) {
            throw new IllegalArgumentException("Invalid role name '" + rolename + "'");
        }
        user.removeRole(role);
    }
}
