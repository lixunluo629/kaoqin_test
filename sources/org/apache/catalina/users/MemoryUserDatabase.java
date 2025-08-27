package org.apache.catalina.users;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.catalina.Group;
import org.apache.catalina.Role;
import org.apache.catalina.User;
import org.apache.catalina.UserDatabase;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.file.ConfigFileLoader;
import org.apache.tomcat.util.res.StringManager;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/users/MemoryUserDatabase.class */
public class MemoryUserDatabase implements UserDatabase {
    private static final Log log = LogFactory.getLog((Class<?>) MemoryUserDatabase.class);
    private static final StringManager sm = StringManager.getManager((Class<?>) MemoryUserDatabase.class);
    protected final Map<String, Group> groups;
    protected final String id;
    protected String pathname;
    protected String pathnameOld;
    protected String pathnameNew;
    protected boolean readonly;
    protected final Map<String, Role> roles;
    protected final Map<String, User> users;
    private final ReentrantReadWriteLock dbLock;
    private final Lock readLock;
    private final Lock writeLock;
    private volatile long lastModified;
    private boolean watchSource;

    public MemoryUserDatabase() {
        this(null);
    }

    public MemoryUserDatabase(String id) {
        this.groups = new ConcurrentHashMap();
        this.pathname = "conf/tomcat-users.xml";
        this.pathnameOld = this.pathname + ".old";
        this.pathnameNew = this.pathname + ".new";
        this.readonly = true;
        this.roles = new ConcurrentHashMap();
        this.users = new ConcurrentHashMap();
        this.dbLock = new ReentrantReadWriteLock();
        this.readLock = this.dbLock.readLock();
        this.writeLock = this.dbLock.writeLock();
        this.lastModified = 0L;
        this.watchSource = true;
        this.id = id;
    }

    @Override // org.apache.catalina.UserDatabase
    public Iterator<Group> getGroups() {
        this.readLock.lock();
        try {
            Iterator<Group> it = new ArrayList(this.groups.values()).iterator();
            this.readLock.unlock();
            return it;
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.apache.catalina.UserDatabase
    public String getId() {
        return this.id;
    }

    public String getPathname() {
        return this.pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
        this.pathnameOld = pathname + ".old";
        this.pathnameNew = pathname + ".new";
    }

    public boolean getReadonly() {
        return this.readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public boolean getWatchSource() {
        return this.watchSource;
    }

    public void setWatchSource(boolean watchSource) {
        this.watchSource = watchSource;
    }

    @Override // org.apache.catalina.UserDatabase
    public Iterator<Role> getRoles() {
        this.readLock.lock();
        try {
            Iterator<Role> it = new ArrayList(this.roles.values()).iterator();
            this.readLock.unlock();
            return it;
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.apache.catalina.UserDatabase
    public Iterator<User> getUsers() {
        this.readLock.lock();
        try {
            Iterator<User> it = new ArrayList(this.users.values()).iterator();
            this.readLock.unlock();
            return it;
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.apache.catalina.UserDatabase
    public void close() throws Exception {
        this.writeLock.lock();
        try {
            save();
            this.users.clear();
            this.groups.clear();
            this.roles.clear();
            this.writeLock.unlock();
        } catch (Throwable th) {
            this.writeLock.unlock();
            throw th;
        }
    }

    @Override // org.apache.catalina.UserDatabase
    public Group createGroup(String groupname, String description) {
        if (groupname == null || groupname.length() == 0) {
            String msg = sm.getString("memoryUserDatabase.nullGroup");
            log.warn(msg);
            throw new IllegalArgumentException(msg);
        }
        MemoryGroup group = new MemoryGroup(this, groupname, description);
        this.readLock.lock();
        try {
            this.groups.put(group.getGroupname(), group);
            this.readLock.unlock();
            return group;
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.apache.catalina.UserDatabase
    public Role createRole(String rolename, String description) {
        if (rolename == null || rolename.length() == 0) {
            String msg = sm.getString("memoryUserDatabase.nullRole");
            log.warn(msg);
            throw new IllegalArgumentException(msg);
        }
        MemoryRole role = new MemoryRole(this, rolename, description);
        this.readLock.lock();
        try {
            this.roles.put(role.getRolename(), role);
            this.readLock.unlock();
            return role;
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.apache.catalina.UserDatabase
    public User createUser(String username, String password, String fullName) {
        if (username == null || username.length() == 0) {
            String msg = sm.getString("memoryUserDatabase.nullUser");
            log.warn(msg);
            throw new IllegalArgumentException(msg);
        }
        MemoryUser user = new MemoryUser(this, username, password, fullName);
        this.readLock.lock();
        try {
            this.users.put(user.getUsername(), user);
            this.readLock.unlock();
            return user;
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.apache.catalina.UserDatabase
    public Group findGroup(String groupname) {
        this.readLock.lock();
        try {
            Group group = this.groups.get(groupname);
            this.readLock.unlock();
            return group;
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.apache.catalina.UserDatabase
    public Role findRole(String rolename) {
        this.readLock.lock();
        try {
            Role role = this.roles.get(rolename);
            this.readLock.unlock();
            return role;
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.apache.catalina.UserDatabase
    public User findUser(String username) {
        this.readLock.lock();
        try {
            User user = this.users.get(username);
            this.readLock.unlock();
            return user;
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.apache.catalina.UserDatabase
    public void open() throws Exception {
        this.writeLock.lock();
        try {
            this.users.clear();
            this.groups.clear();
            this.roles.clear();
            String pathName = getPathname();
            URI uri = ConfigFileLoader.getURI(pathName);
            URLConnection uConn = null;
            try {
                try {
                    URL url = uri.toURL();
                    uConn = url.openConnection();
                    InputStream is = uConn.getInputStream();
                    this.lastModified = uConn.getLastModified();
                    Digester digester = new Digester();
                    try {
                        digester.setFeature("http://apache.org/xml/features/allow-java-encodings", true);
                    } catch (Exception e) {
                        log.warn(sm.getString("memoryUserDatabase.xmlFeatureEncoding"), e);
                    }
                    digester.addFactoryCreate("tomcat-users/group", new MemoryGroupCreationFactory(this), true);
                    digester.addFactoryCreate("tomcat-users/role", new MemoryRoleCreationFactory(this), true);
                    digester.addFactoryCreate("tomcat-users/user", new MemoryUserCreationFactory(this), true);
                    digester.parse(is);
                    if (uConn != null) {
                        try {
                            uConn.getInputStream().close();
                        } catch (IOException ioe) {
                            log.warn(sm.getString("memoryUserDatabase.fileClose", this.pathname), ioe);
                        }
                    }
                } catch (IOException e2) {
                    log.error(sm.getString("memoryUserDatabase.fileNotFound", pathName));
                    if (uConn != null) {
                        try {
                            uConn.getInputStream().close();
                        } catch (IOException ioe2) {
                            log.warn(sm.getString("memoryUserDatabase.fileClose", this.pathname), ioe2);
                        }
                    }
                } catch (Exception e3) {
                    this.users.clear();
                    this.groups.clear();
                    this.roles.clear();
                    throw e3;
                }
            } finally {
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override // org.apache.catalina.UserDatabase
    public void removeGroup(Group group) {
        this.readLock.lock();
        try {
            Iterator<User> users = getUsers();
            while (users.hasNext()) {
                User user = users.next();
                user.removeGroup(group);
            }
            this.groups.remove(group.getGroupname());
            this.readLock.unlock();
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.apache.catalina.UserDatabase
    public void removeRole(Role role) {
        this.readLock.lock();
        try {
            Iterator<Group> groups = getGroups();
            while (groups.hasNext()) {
                Group group = groups.next();
                group.removeRole(role);
            }
            Iterator<User> users = getUsers();
            while (users.hasNext()) {
                User user = users.next();
                user.removeRole(role);
            }
            this.roles.remove(role.getRolename());
            this.readLock.unlock();
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.apache.catalina.UserDatabase
    public void removeUser(User user) {
        this.readLock.lock();
        try {
            this.users.remove(user.getUsername());
            this.readLock.unlock();
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    public boolean isWriteable() {
        File file = new File(this.pathname);
        if (!file.isAbsolute()) {
            file = new File(System.getProperty("catalina.base"), this.pathname);
        }
        File dir = file.getParentFile();
        return dir.exists() && dir.isDirectory() && dir.canWrite();
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r11v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r12v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 11, insn: 0x0235: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r11 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('fos' java.io.FileOutputStream)]) A[TRY_LEAVE], block:B:89:0x0235 */
    /* JADX WARN: Not initialized variable reg: 12, insn: 0x0239: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:91:0x0239 */
    /* JADX WARN: Type inference failed for: r11v0, names: [fos], types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r12v0, types: [java.lang.Throwable] */
    @Override // org.apache.catalina.UserDatabase
    public void save() throws Exception {
        ?? r11;
        ?? r12;
        if (getReadonly()) {
            log.error(sm.getString("memoryUserDatabase.readOnly"));
            return;
        }
        if (!isWriteable()) {
            log.warn(sm.getString("memoryUserDatabase.notPersistable"));
            return;
        }
        File fileNew = new File(this.pathnameNew);
        if (!fileNew.isAbsolute()) {
            fileNew = new File(System.getProperty("catalina.base"), this.pathnameNew);
        }
        this.writeLock.lock();
        try {
            try {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileNew);
                    Throwable th = null;
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                    Throwable th2 = null;
                    try {
                        PrintWriter printWriter = new PrintWriter(outputStreamWriter);
                        Throwable th3 = null;
                        try {
                            try {
                                printWriter.println("<?xml version='1.0' encoding='utf-8'?>");
                                printWriter.println("<tomcat-users xmlns=\"http://tomcat.apache.org/xml\"");
                                printWriter.print("              ");
                                printWriter.println("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
                                printWriter.print("              ");
                                printWriter.println("xsi:schemaLocation=\"http://tomcat.apache.org/xml tomcat-users.xsd\"");
                                printWriter.println("              version=\"1.0\">");
                                Iterator<Role> roles = getRoles();
                                while (roles.hasNext()) {
                                    printWriter.print("  ");
                                    printWriter.println(roles.next());
                                }
                                Iterator<Group> groups = getGroups();
                                while (groups.hasNext()) {
                                    printWriter.print("  ");
                                    printWriter.println(groups.next());
                                }
                                Iterator<User> users = getUsers();
                                while (users.hasNext()) {
                                    printWriter.print("  ");
                                    printWriter.println(((MemoryUser) users.next()).toXml());
                                }
                                printWriter.println("</tomcat-users>");
                                if (printWriter.checkError()) {
                                    throw new IOException(sm.getString("memoryUserDatabase.writeException", fileNew.getAbsolutePath()));
                                }
                                if (printWriter != null) {
                                    if (0 != 0) {
                                        try {
                                            printWriter.close();
                                        } catch (Throwable th4) {
                                            th3.addSuppressed(th4);
                                        }
                                    } else {
                                        printWriter.close();
                                    }
                                }
                                if (outputStreamWriter != null) {
                                    if (0 != 0) {
                                        try {
                                            outputStreamWriter.close();
                                        } catch (Throwable th5) {
                                            th2.addSuppressed(th5);
                                        }
                                    } else {
                                        outputStreamWriter.close();
                                    }
                                }
                                if (fileOutputStream != null) {
                                    if (0 != 0) {
                                        try {
                                            fileOutputStream.close();
                                        } catch (Throwable th6) {
                                            th.addSuppressed(th6);
                                        }
                                    } else {
                                        fileOutputStream.close();
                                    }
                                }
                                this.lastModified = fileNew.lastModified();
                                this.writeLock.unlock();
                                File file = new File(this.pathnameOld);
                                if (!file.isAbsolute()) {
                                    file = new File(System.getProperty("catalina.base"), this.pathnameOld);
                                }
                                if (file.exists() && !file.delete()) {
                                    throw new IOException(sm.getString("memoryUserDatabase.fileDelete", file));
                                }
                                File file2 = new File(this.pathname);
                                if (!file2.isAbsolute()) {
                                    file2 = new File(System.getProperty("catalina.base"), this.pathname);
                                }
                                if (file2.exists() && !file2.renameTo(file)) {
                                    throw new IOException(sm.getString("memoryUserDatabase.renameOld", file.getAbsolutePath()));
                                }
                                if (fileNew.renameTo(file2)) {
                                    if (file.exists() && !file.delete()) {
                                        throw new IOException(sm.getString("memoryUserDatabase.fileDelete", file));
                                    }
                                } else {
                                    if (file.exists() && !file.renameTo(file2)) {
                                        log.warn(sm.getString("memoryUserDatabase.restoreOrig", file));
                                    }
                                    throw new IOException(sm.getString("memoryUserDatabase.renameNew", file2.getAbsolutePath()));
                                }
                            } catch (Throwable th7) {
                                if (printWriter != null) {
                                    if (th3 != null) {
                                        try {
                                            printWriter.close();
                                        } catch (Throwable th8) {
                                            th3.addSuppressed(th8);
                                        }
                                    } else {
                                        printWriter.close();
                                    }
                                }
                                throw th7;
                            }
                        } catch (Throwable th9) {
                            th3 = th9;
                            throw th9;
                        }
                    } catch (Throwable th10) {
                        if (outputStreamWriter != null) {
                            if (0 != 0) {
                                try {
                                    outputStreamWriter.close();
                                } catch (Throwable th11) {
                                    th2.addSuppressed(th11);
                                }
                            } else {
                                outputStreamWriter.close();
                            }
                        }
                        throw th10;
                    }
                } catch (Throwable th12) {
                    if (r11 != 0) {
                        if (r12 != 0) {
                            try {
                                r11.close();
                            } catch (Throwable th13) {
                                r12.addSuppressed(th13);
                            }
                        } else {
                            r11.close();
                        }
                    }
                    throw th12;
                }
            } catch (IOException e) {
                if (fileNew.exists() && !fileNew.delete()) {
                    log.warn(sm.getString("memoryUserDatabase.fileDelete", fileNew));
                }
                throw e;
            }
        } catch (Throwable th14) {
            this.writeLock.unlock();
            throw th14;
        }
    }

    /* JADX WARN: Finally extract failed */
    public void backgroundProcess() throws IOException {
        if (this.watchSource) {
            URI uri = ConfigFileLoader.getURI(getPathname());
            URLConnection uConn = null;
            try {
                try {
                    URL url = uri.toURL();
                    URLConnection uConn2 = url.openConnection();
                    if (this.lastModified != uConn2.getLastModified()) {
                        this.writeLock.lock();
                        try {
                            long detectedLastModified = uConn2.getLastModified();
                            if (this.lastModified != detectedLastModified && detectedLastModified + 2000 < System.currentTimeMillis()) {
                                log.info(sm.getString("memoryUserDatabase.reload", this.id, uri));
                                open();
                            }
                            this.writeLock.unlock();
                        } catch (Throwable th) {
                            this.writeLock.unlock();
                            throw th;
                        }
                    }
                    if (uConn2 != null) {
                        try {
                            uConn2.getInputStream().close();
                        } catch (IOException ioe) {
                            log.warn(sm.getString("memoryUserDatabase.fileClose", this.pathname), ioe);
                        }
                    }
                } catch (Exception ioe2) {
                    log.error(sm.getString("memoryUserDatabase.reloadError", this.id, uri), ioe2);
                    if (0 != 0) {
                        try {
                            uConn.getInputStream().close();
                        } catch (IOException ioe3) {
                            log.warn(sm.getString("memoryUserDatabase.fileClose", this.pathname), ioe3);
                        }
                    }
                }
            } catch (Throwable th2) {
                if (0 != 0) {
                    try {
                        uConn.getInputStream().close();
                    } catch (IOException ioe4) {
                        log.warn(sm.getString("memoryUserDatabase.fileClose", this.pathname), ioe4);
                    }
                }
                throw th2;
            }
        }
    }

    public String toString() {
        return "MemoryUserDatabase[id=" + this.id + ",pathname=" + this.pathname + ",groupCount=" + this.groups.size() + ",roleCount=" + this.roles.size() + ",userCount=" + this.users.size() + "]";
    }
}
