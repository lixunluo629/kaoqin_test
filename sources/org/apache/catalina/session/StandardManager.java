package org.apache.catalina.session;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Loader;
import org.apache.catalina.Session;
import org.apache.catalina.security.SecurityUtil;
import org.apache.catalina.util.CustomObjectInputStream;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.ExceptionUtils;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/session/StandardManager.class */
public class StandardManager extends ManagerBase {
    protected static final String name = "StandardManager";
    private final Log log = LogFactory.getLog((Class<?>) StandardManager.class);
    protected String pathname = "SESSIONS.ser";

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/session/StandardManager$PrivilegedDoLoad.class */
    private class PrivilegedDoLoad implements PrivilegedExceptionAction<Void> {
        PrivilegedDoLoad() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedExceptionAction
        public Void run() throws Exception {
            StandardManager.this.doLoad();
            return null;
        }
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/session/StandardManager$PrivilegedDoUnload.class */
    private class PrivilegedDoUnload implements PrivilegedExceptionAction<Void> {
        PrivilegedDoUnload() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedExceptionAction
        public Void run() throws Exception {
            StandardManager.this.doUnload();
            return null;
        }
    }

    @Override // org.apache.catalina.session.ManagerBase
    public String getName() {
        return name;
    }

    public String getPathname() {
        return this.pathname;
    }

    public void setPathname(String pathname) {
        String oldPathname = this.pathname;
        this.pathname = pathname;
        this.support.firePropertyChange("pathname", oldPathname, this.pathname);
    }

    @Override // org.apache.catalina.Manager
    public void load() throws PrivilegedActionException, IOException, ClassNotFoundException {
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                AccessController.doPrivileged(new PrivilegedDoLoad());
                return;
            } catch (PrivilegedActionException ex) {
                Exception exception = ex.getException();
                if (exception instanceof ClassNotFoundException) {
                    throw ((ClassNotFoundException) exception);
                }
                if (exception instanceof IOException) {
                    throw ((IOException) exception);
                }
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Unreported exception in load() ", exception);
                    return;
                }
                return;
            }
        }
        doLoad();
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r13v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r14v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r15v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r16v0 ??
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
    /* JADX WARN: Not initialized variable reg: 13, insn: 0x0268: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('fis' java.io.FileInputStream)]) A[TRY_LEAVE], block:B:108:0x0268 */
    /* JADX WARN: Not initialized variable reg: 14, insn: 0x026d: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r14 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:110:0x026d */
    /* JADX WARN: Not initialized variable reg: 15, insn: 0x0211: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r15 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('bis' java.io.BufferedInputStream)]) A[TRY_LEAVE], block:B:86:0x0211 */
    /* JADX WARN: Not initialized variable reg: 16, insn: 0x0216: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r16 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:88:0x0216 */
    /* JADX WARN: Type inference failed for: r13v1, names: [fis], types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r14v0, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r15v0, names: [bis], types: [java.io.BufferedInputStream] */
    /* JADX WARN: Type inference failed for: r16v0, types: [java.lang.Throwable] */
    protected void doLoad() throws IOException, ClassNotFoundException {
        ?? r15;
        ?? r16;
        if (this.log.isDebugEnabled()) {
            this.log.debug("Start: Loading persisted sessions");
        }
        this.sessions.clear();
        File file = file();
        if (file == null) {
            return;
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug(sm.getString("standardManager.loading", this.pathname));
        }
        try {
            try {
                FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                Throwable th = null;
                try {
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                    Throwable th2 = null;
                    Context context = getContext();
                    Loader loader = context.getLoader();
                    Log logger = context.getLogger();
                    ClassLoader classLoader = loader != null ? loader.getClassLoader() : null;
                    if (classLoader == null) {
                        classLoader = getClass().getClassLoader();
                    }
                    synchronized (this.sessions) {
                        try {
                            CustomObjectInputStream customObjectInputStream = new CustomObjectInputStream(bufferedInputStream, classLoader, logger, getSessionAttributeValueClassNamePattern(), getWarnOnSessionAttributeFilterFailure());
                            Throwable th3 = null;
                            try {
                                try {
                                    int iIntValue = ((Integer) customObjectInputStream.readObject()).intValue();
                                    if (this.log.isDebugEnabled()) {
                                        this.log.debug("Loading " + iIntValue + " persisted sessions");
                                    }
                                    for (int i = 0; i < iIntValue; i++) {
                                        StandardSession newSession = getNewSession();
                                        newSession.readObjectData(customObjectInputStream);
                                        newSession.setManager(this);
                                        this.sessions.put(newSession.getIdInternal(), newSession);
                                        newSession.activate();
                                        if (!newSession.isValidInternal()) {
                                            newSession.setValid(true);
                                            newSession.expire();
                                        }
                                        this.sessionCounter++;
                                    }
                                    if (customObjectInputStream != null) {
                                        if (0 != 0) {
                                            try {
                                                customObjectInputStream.close();
                                            } catch (Throwable th4) {
                                                th3.addSuppressed(th4);
                                            }
                                        } else {
                                            customObjectInputStream.close();
                                        }
                                    }
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                } catch (Throwable th5) {
                                    if (customObjectInputStream != null) {
                                        if (th3 != null) {
                                            try {
                                                customObjectInputStream.close();
                                            } catch (Throwable th6) {
                                                th3.addSuppressed(th6);
                                            }
                                        } else {
                                            customObjectInputStream.close();
                                        }
                                    }
                                    throw th5;
                                }
                            } catch (Throwable th7) {
                                th3 = th7;
                                throw th7;
                            }
                        } catch (Throwable th8) {
                            if (file.exists()) {
                                file.delete();
                            }
                            throw th8;
                        }
                    }
                    if (bufferedInputStream != null) {
                        if (0 != 0) {
                            try {
                                bufferedInputStream.close();
                            } catch (Throwable th9) {
                                th2.addSuppressed(th9);
                            }
                        } else {
                            bufferedInputStream.close();
                        }
                    }
                    if (fileInputStream != null) {
                        if (0 != 0) {
                            try {
                                fileInputStream.close();
                            } catch (Throwable th10) {
                                th.addSuppressed(th10);
                            }
                        } else {
                            fileInputStream.close();
                        }
                    }
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Finish: Loading persisted sessions");
                    }
                } catch (Throwable th11) {
                    if (r15 != 0) {
                        if (r16 != 0) {
                            try {
                                r15.close();
                            } catch (Throwable th12) {
                                r16.addSuppressed(th12);
                            }
                        } else {
                            r15.close();
                        }
                    }
                    throw th11;
                }
            } catch (FileNotFoundException e) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("No persisted data file found");
                }
            }
        } finally {
        }
    }

    @Override // org.apache.catalina.Manager
    public void unload() throws PrivilegedActionException, IOException {
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                AccessController.doPrivileged(new PrivilegedDoUnload());
                return;
            } catch (PrivilegedActionException ex) {
                Exception exception = ex.getException();
                if (exception instanceof IOException) {
                    throw ((IOException) exception);
                }
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Unreported exception in unLoad()", exception);
                    return;
                }
                return;
            }
        }
        doUnload();
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r13v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r14v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r15v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r16v0 ??
    java.lang.NullPointerException
     */
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
    /* JADX WARN: Not initialized variable reg: 13, insn: 0x01c6: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('bos' java.io.BufferedOutputStream)]) A[TRY_LEAVE], block:B:69:0x01c6 */
    /* JADX WARN: Not initialized variable reg: 14, insn: 0x01cb: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r14 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:71:0x01cb */
    /* JADX WARN: Not initialized variable reg: 15, insn: 0x016f: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r15 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('oos' java.io.ObjectOutputStream)]) A[TRY_LEAVE], block:B:47:0x016f */
    /* JADX WARN: Not initialized variable reg: 16, insn: 0x0174: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r16 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:49:0x0174 */
    /* JADX WARN: Type inference failed for: r13v1, names: [bos], types: [java.io.BufferedOutputStream] */
    /* JADX WARN: Type inference failed for: r14v0, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r15v0, names: [oos], types: [java.io.ObjectOutputStream] */
    /* JADX WARN: Type inference failed for: r16v0, types: [java.lang.Throwable] */
    protected void doUnload() throws IOException {
        ?? r13;
        ?? r14;
        ?? r15;
        ?? r16;
        if (this.log.isDebugEnabled()) {
            this.log.debug(sm.getString("standardManager.unloading.debug"));
        }
        if (this.sessions.isEmpty()) {
            this.log.debug(sm.getString("standardManager.unloading.nosessions"));
            return;
        }
        File file = file();
        if (file == null) {
            return;
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug(sm.getString("standardManager.unloading", this.pathname));
        }
        ArrayList<StandardSession> list = new ArrayList<>();
        FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
        Throwable th = null;
        try {
            try {
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fos);
                Throwable th2 = null;
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
                    Throwable th3 = null;
                    synchronized (this.sessions) {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Unloading " + this.sessions.size() + " sessions");
                        }
                        objectOutputStream.writeObject(Integer.valueOf(this.sessions.size()));
                        Iterator<Session> it = this.sessions.values().iterator();
                        while (it.hasNext()) {
                            StandardSession standardSession = (StandardSession) it.next();
                            list.add(standardSession);
                            standardSession.passivate();
                            standardSession.writeObjectData(objectOutputStream);
                        }
                    }
                    if (objectOutputStream != null) {
                        if (0 != 0) {
                            try {
                                objectOutputStream.close();
                            } catch (Throwable th4) {
                                th3.addSuppressed(th4);
                            }
                        } else {
                            objectOutputStream.close();
                        }
                    }
                    if (bufferedOutputStream != null) {
                        if (0 != 0) {
                            try {
                                bufferedOutputStream.close();
                            } catch (Throwable th5) {
                                th2.addSuppressed(th5);
                            }
                        } else {
                            bufferedOutputStream.close();
                        }
                    }
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Expiring " + list.size() + " persisted sessions");
                    }
                    Iterator<StandardSession> it2 = list.iterator();
                    while (it2.hasNext()) {
                        StandardSession next = it2.next();
                        try {
                            try {
                                next.expire(false);
                                next.recycle();
                            } catch (Throwable th6) {
                                ExceptionUtils.handleThrowable(th6);
                                next.recycle();
                            }
                        } catch (Throwable th7) {
                            next.recycle();
                            throw th7;
                        }
                    }
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Unloading complete");
                    }
                } catch (Throwable th8) {
                    if (r15 != 0) {
                        if (r16 != 0) {
                            try {
                                r15.close();
                            } catch (Throwable th9) {
                                r16.addSuppressed(th9);
                            }
                        } else {
                            r15.close();
                        }
                    }
                    throw th8;
                }
            } catch (Throwable th10) {
                if (r13 != 0) {
                    if (r14 != 0) {
                        try {
                            r13.close();
                        } catch (Throwable th11) {
                            r14.addSuppressed(th11);
                        }
                    } else {
                        r13.close();
                    }
                }
                throw th10;
            }
        } finally {
            if (fos != null) {
                if (0 != 0) {
                    try {
                        fos.close();
                    } catch (Throwable x2) {
                        th.addSuppressed(x2);
                    }
                } else {
                    fos.close();
                }
            }
        }
    }

    @Override // org.apache.catalina.session.ManagerBase, org.apache.catalina.util.LifecycleBase
    protected synchronized void startInternal() throws LifecycleException {
        super.startInternal();
        try {
            load();
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            this.log.error(sm.getString("standardManager.managerLoad"), t);
        }
        setState(LifecycleState.STARTING);
    }

    @Override // org.apache.catalina.session.ManagerBase, org.apache.catalina.util.LifecycleBase
    protected synchronized void stopInternal() throws LifecycleException {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Stopping");
        }
        setState(LifecycleState.STOPPING);
        try {
            unload();
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            this.log.error(sm.getString("standardManager.managerUnload"), t);
        }
        Session[] sessions = findSessions();
        for (Session session : sessions) {
            try {
                try {
                    if (session.isValid()) {
                        session.expire();
                    }
                    session.recycle();
                } catch (Throwable t2) {
                    ExceptionUtils.handleThrowable(t2);
                    session.recycle();
                }
            } catch (Throwable th) {
                session.recycle();
                throw th;
            }
        }
        super.stopInternal();
    }

    protected File file() {
        if (this.pathname == null || this.pathname.length() == 0) {
            return null;
        }
        File file = new File(this.pathname);
        if (!file.isAbsolute()) {
            Context context = getContext();
            ServletContext servletContext = context.getServletContext();
            File tempdir = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            if (tempdir != null) {
                file = new File(tempdir, this.pathname);
            }
        }
        return file;
    }
}
