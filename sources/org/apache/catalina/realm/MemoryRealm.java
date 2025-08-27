package org.apache.catalina.realm;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.catalina.LifecycleException;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.file.ConfigFileLoader;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/realm/MemoryRealm.class */
public class MemoryRealm extends RealmBase {
    private static final Log log = LogFactory.getLog((Class<?>) MemoryRealm.class);
    private static Digester digester = null;

    @Deprecated
    protected static final String name = "MemoryRealm";
    private String pathname = "conf/tomcat-users.xml";
    private final Map<String, GenericPrincipal> principals = new HashMap();

    public String getPathname() {
        return this.pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    @Override // org.apache.catalina.realm.RealmBase, org.apache.catalina.Realm
    public Principal authenticate(String username, String credentials) {
        if (username == null || credentials == null) {
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("memoryRealm.authenticateFailure", username));
                return null;
            }
            return null;
        }
        GenericPrincipal principal = this.principals.get(username);
        if (principal == null || principal.getPassword() == null) {
            getCredentialHandler().mutate(credentials);
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("memoryRealm.authenticateFailure", username));
                return null;
            }
            return null;
        }
        boolean validated = getCredentialHandler().matches(credentials, principal.getPassword());
        if (validated) {
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("memoryRealm.authenticateSuccess", username));
            }
            return principal;
        }
        if (log.isDebugEnabled()) {
            log.debug(sm.getString("memoryRealm.authenticateFailure", username));
            return null;
        }
        return null;
    }

    void addUser(String username, String password, String roles) {
        ArrayList<String> list = new ArrayList<>();
        String strSubstring = roles + ",";
        while (true) {
            String roles2 = strSubstring;
            int comma = roles2.indexOf(44);
            if (comma >= 0) {
                String role = roles2.substring(0, comma).trim();
                list.add(role);
                strSubstring = roles2.substring(comma + 1);
            } else {
                GenericPrincipal principal = new GenericPrincipal(username, password, list);
                this.principals.put(username, principal);
                return;
            }
        }
    }

    protected synchronized Digester getDigester() {
        if (digester == null) {
            digester = new Digester();
            digester.setValidating(false);
            try {
                digester.setFeature("http://apache.org/xml/features/allow-java-encodings", true);
            } catch (Exception e) {
                log.warn(sm.getString("memoryRealm.xmlFeatureEncoding"), e);
            }
            digester.addRuleSet(new MemoryRuleSet());
        }
        return digester;
    }

    @Override // org.apache.catalina.realm.RealmBase
    @Deprecated
    protected String getName() {
        return name;
    }

    @Override // org.apache.catalina.realm.RealmBase
    protected String getPassword(String username) {
        GenericPrincipal principal = this.principals.get(username);
        if (principal != null) {
            return principal.getPassword();
        }
        return null;
    }

    @Override // org.apache.catalina.realm.RealmBase
    protected Principal getPrincipal(String username) {
        return this.principals.get(username);
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r11v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r12v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r13v1 ??
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
    /* JADX WARN: Not initialized variable reg: 11, insn: 0x00a7: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r11 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('is' java.io.InputStream)]) A[TRY_LEAVE], block:B:39:0x00a7 */
    /* JADX WARN: Not initialized variable reg: 12, insn: 0x00ab: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:41:0x00ab */
    /* JADX WARN: Not initialized variable reg: 13, insn: 0x0074: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = 
  (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('digester' org.apache.tomcat.util.digester.Digester)])
 A[TRY_LEAVE], block:B:24:0x0074 */
    /* JADX WARN: Type inference failed for: r11v1, names: [is], types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r12v0, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r13v1, names: [digester], types: [org.apache.tomcat.util.digester.Digester] */
    @Override // org.apache.catalina.realm.RealmBase, org.apache.catalina.util.LifecycleBase
    protected void startInternal() throws LifecycleException, IOException {
        ?? r13;
        String pathName = getPathname();
        try {
            try {
                InputStream inputStream = ConfigFileLoader.getInputStream(pathName);
                Throwable th = null;
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("memoryRealm.loadPath", pathName));
                }
                try {
                    Digester digester2 = getDigester();
                    try {
                        synchronized (digester2) {
                            digester2.push(this);
                            digester2.parse(inputStream);
                        }
                        digester2.reset();
                        if (inputStream != null) {
                            if (0 != 0) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                inputStream.close();
                            }
                        }
                        super.startInternal();
                    } catch (Exception e) {
                        throw new LifecycleException(sm.getString("memoryRealm.readXml"), e);
                    }
                } catch (Throwable th3) {
                    r13.reset();
                    throw th3;
                }
            } finally {
            }
        } catch (IOException ioe) {
            throw new LifecycleException(sm.getString("memoryRealm.loadExist", pathName), ioe);
        }
    }
}
