package org.apache.catalina.session;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.Globals;
import org.apache.catalina.Session;
import org.apache.juli.logging.Log;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/session/FileStore.class */
public final class FileStore extends StoreBase {
    private static final String FILE_EXT = ".session";
    private String directory = ".";
    private File directoryFile = null;
    private static final String storeName = "fileStore";
    private static final String threadName = "FileStore";

    public String getDirectory() {
        return this.directory;
    }

    public void setDirectory(String path) {
        String oldDirectory = this.directory;
        this.directory = path;
        this.directoryFile = null;
        this.support.firePropertyChange("directory", oldDirectory, this.directory);
    }

    public String getThreadName() {
        return threadName;
    }

    @Override // org.apache.catalina.session.StoreBase
    public String getStoreName() {
        return storeName;
    }

    @Override // org.apache.catalina.Store
    public int getSize() throws IOException {
        File file = directory();
        if (file == null) {
            return 0;
        }
        String[] files = file.list();
        int keycount = 0;
        if (files != null) {
            for (String str : files) {
                if (str.endsWith(FILE_EXT)) {
                    keycount++;
                }
            }
        }
        return keycount;
    }

    @Override // org.apache.catalina.Store
    public void clear() throws IOException {
        String[] keys = keys();
        for (String str : keys) {
            remove(str);
        }
    }

    @Override // org.apache.catalina.Store
    public String[] keys() throws IOException {
        File file = directory();
        if (file == null) {
            return new String[0];
        }
        String[] files = file.list();
        if (files == null || files.length < 1) {
            return new String[0];
        }
        ArrayList<String> list = new ArrayList<>();
        int n = FILE_EXT.length();
        for (int i = 0; i < files.length; i++) {
            if (files[i].endsWith(FILE_EXT)) {
                list.add(files[i].substring(0, files[i].length() - n));
            }
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r14v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r15v0 ??
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
    /* JADX WARN: Not initialized variable reg: 14, insn: 0x013c: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r14 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('fis' java.io.FileInputStream)]) A[TRY_LEAVE], block:B:53:0x013c */
    /* JADX WARN: Not initialized variable reg: 15, insn: 0x0141: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r15 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:55:0x0141 */
    /* JADX WARN: Type inference failed for: r14v0, names: [fis], types: [java.io.FileInputStream] */
    /* JADX WARN: Type inference failed for: r15v0, types: [java.lang.Throwable] */
    @Override // org.apache.catalina.Store
    public Session load(String id) throws IOException, ClassNotFoundException {
        ?? r14;
        ?? r15;
        File file = file(id);
        if (file == null || !file.exists()) {
            return null;
        }
        Context context = getManager().getContext();
        Log contextLog = context.getLogger();
        if (contextLog.isDebugEnabled()) {
            contextLog.debug(sm.getString(getStoreName() + ".loading", id, file.getAbsolutePath()));
        }
        ClassLoader oldThreadContextCL = context.bind(Globals.IS_SECURITY_ENABLED, null);
        try {
            try {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                    Throwable th = null;
                    ObjectInputStream objectInputStream = getObjectInputStream(fileInputStream);
                    Throwable th2 = null;
                    try {
                        try {
                            StandardSession standardSession = (StandardSession) this.manager.createEmptySession();
                            standardSession.readObjectData(objectInputStream);
                            standardSession.setManager(this.manager);
                            if (objectInputStream != null) {
                                if (0 != 0) {
                                    try {
                                        objectInputStream.close();
                                    } catch (Throwable th3) {
                                        th2.addSuppressed(th3);
                                    }
                                } else {
                                    objectInputStream.close();
                                }
                            }
                            if (fileInputStream != null) {
                                if (0 != 0) {
                                    try {
                                        fileInputStream.close();
                                    } catch (Throwable th4) {
                                        th.addSuppressed(th4);
                                    }
                                } else {
                                    fileInputStream.close();
                                }
                            }
                            context.unbind(Globals.IS_SECURITY_ENABLED, oldThreadContextCL);
                            return standardSession;
                        } catch (Throwable th5) {
                            if (objectInputStream != null) {
                                if (th2 != null) {
                                    try {
                                        objectInputStream.close();
                                    } catch (Throwable th6) {
                                        th2.addSuppressed(th6);
                                    }
                                } else {
                                    objectInputStream.close();
                                }
                            }
                            throw th5;
                        }
                    } finally {
                    }
                } catch (Throwable th7) {
                    if (r14 != 0) {
                        if (r15 != 0) {
                            try {
                                r14.close();
                            } catch (Throwable th8) {
                                r15.addSuppressed(th8);
                            }
                        } else {
                            r14.close();
                        }
                    }
                    throw th7;
                }
            } catch (FileNotFoundException e) {
                if (contextLog.isDebugEnabled()) {
                    contextLog.debug("No persisted data file found");
                }
                context.unbind(Globals.IS_SECURITY_ENABLED, oldThreadContextCL);
                return null;
            }
        } catch (Throwable th9) {
            context.unbind(Globals.IS_SECURITY_ENABLED, oldThreadContextCL);
            throw th9;
        }
    }

    @Override // org.apache.catalina.Store
    public void remove(String id) throws IOException {
        File file = file(id);
        if (file == null) {
            return;
        }
        if (this.manager.getContext().getLogger().isDebugEnabled()) {
            this.manager.getContext().getLogger().debug(sm.getString(getStoreName() + ".removing", id, file.getAbsolutePath()));
        }
        file.delete();
    }

    @Override // org.apache.catalina.Store
    public void save(Session session) throws IOException {
        File file = file(session.getIdInternal());
        if (file == null) {
            return;
        }
        if (this.manager.getContext().getLogger().isDebugEnabled()) {
            this.manager.getContext().getLogger().debug(sm.getString(getStoreName() + ".saving", session.getIdInternal(), file.getAbsolutePath()));
        }
        FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
        Throwable th = null;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(fos));
            Throwable th2 = null;
            try {
                try {
                    ((StandardSession) session).writeObjectData(oos);
                    if (oos != null) {
                        if (0 != 0) {
                            try {
                                oos.close();
                            } catch (Throwable x2) {
                                th2.addSuppressed(x2);
                            }
                        } else {
                            oos.close();
                        }
                    }
                    if (fos != null) {
                        if (0 == 0) {
                            fos.close();
                            return;
                        }
                        try {
                            fos.close();
                        } catch (Throwable x22) {
                            th.addSuppressed(x22);
                        }
                    }
                } catch (Throwable th3) {
                    if (oos != null) {
                        if (th2 != null) {
                            try {
                                oos.close();
                            } catch (Throwable x23) {
                                th2.addSuppressed(x23);
                            }
                        } else {
                            oos.close();
                        }
                    }
                    throw th3;
                }
            } catch (Throwable th4) {
                th2 = th4;
                throw th4;
            }
        } catch (Throwable th5) {
            if (fos != null) {
                if (0 != 0) {
                    try {
                        fos.close();
                    } catch (Throwable x24) {
                        th.addSuppressed(x24);
                    }
                } else {
                    fos.close();
                }
            }
            throw th5;
        }
    }

    private File directory() throws IOException {
        if (this.directory == null) {
            return null;
        }
        if (this.directoryFile != null) {
            return this.directoryFile;
        }
        File file = new File(this.directory);
        if (!file.isAbsolute()) {
            Context context = this.manager.getContext();
            ServletContext servletContext = context.getServletContext();
            File work = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            file = new File(work, this.directory);
        }
        if (!file.exists() || !file.isDirectory()) {
            if (!file.delete() && file.exists()) {
                throw new IOException(sm.getString("fileStore.deleteFailed", file));
            }
            if (!file.mkdirs() && !file.isDirectory()) {
                throw new IOException(sm.getString("fileStore.createFailed", file));
            }
        }
        this.directoryFile = file;
        return file;
    }

    private File file(String id) throws IOException {
        if (this.directory == null) {
            return null;
        }
        String filename = id + FILE_EXT;
        File file = new File(directory(), filename);
        return file;
    }
}
