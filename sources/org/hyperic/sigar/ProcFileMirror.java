package org.hyperic.sigar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/ProcFileMirror.class */
public class ProcFileMirror extends FileWatcher {
    private String proc;
    private long expire;
    private static final Logger log;
    private static final boolean isDebug;
    static Class class$org$hyperic$sigar$ProcFileMirror;

    static {
        Class clsClass$;
        if (class$org$hyperic$sigar$ProcFileMirror == null) {
            clsClass$ = class$("org.hyperic.sigar.ProcFileMirror");
            class$org$hyperic$sigar$ProcFileMirror = clsClass$;
        } else {
            clsClass$ = class$org$hyperic$sigar$ProcFileMirror;
        }
        log = SigarLog.getLogger(clsClass$.getName());
        isDebug = log.isDebugEnabled();
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    public ProcFileMirror(Sigar sigar, String proc) {
        super(sigar);
        this.proc = proc;
        this.expire = 300000L;
    }

    public long getExpireMillis() {
        return this.expire;
    }

    public void setExpire(long seconds) {
        setExpireMillis(seconds * 1000);
    }

    public void setExpireMillis(long millis) {
        this.expire = millis;
    }

    public String getProcFile(File file) {
        return getProcFile(file.getPath());
    }

    public String getProcFile(String file) {
        if (file.startsWith("/proc/")) {
            file = file.substring("/proc/".length());
        }
        return new StringBuffer().append(this.proc).append(File.separator).append(file).toString();
    }

    private void mirror(String source) throws IOException {
        mirror(source, getProcFile(source));
    }

    private String mirrorToString(File source, File dest) {
        return new StringBuffer().append("mirror(").append(source).append(", ").append(dest).append(")").toString();
    }

    private void mirror(String source, String dest) throws IOException {
        mirror(new File(source), new File(dest));
    }

    private void mirror(File source, File dest) throws IOException {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            try {
                is = new FileInputStream(source);
                os = new FileOutputStream(dest);
                byte[] buffer = new byte[2048];
                while (true) {
                    int nread = is.read(buffer);
                    if (nread == -1) {
                        break;
                    } else {
                        os.write(buffer, 0, nread);
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
                if (os != null) {
                    os.close();
                }
                if (isDebug) {
                    log.debug(mirrorToString(source, dest));
                }
            } catch (Throwable th) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e2) {
                    }
                }
                if (os != null) {
                    os.close();
                }
                throw th;
            }
        } catch (IOException e3) {
            String msg = new StringBuffer().append(mirrorToString(source, dest)).append(" failed: ").append(e3.getMessage()).toString();
            throw new IOException(msg);
        }
    }

    @Override // org.hyperic.sigar.FileWatcher
    public FileInfo add(String name) throws SigarException {
        File source = new File(name);
        File dest = new File(getProcFile(source));
        File dir = dest.getParentFile();
        if (!dir.exists() && !dir.mkdirs()) {
            String msg = new StringBuffer().append("mkdir(").append(dir).append(") failed").toString();
            throw new SigarException(msg);
        }
        if (!source.canRead()) {
            throw new SigarException(new StringBuffer().append("Cannot read: ").append(source).toString());
        }
        if ((dest.isFile() && !dest.canWrite()) || !dir.isDirectory() || !dir.canWrite()) {
            throw new SigarException(new StringBuffer().append("Cannot write: ").append(dest).toString());
        }
        try {
            mirror(source.getPath(), dest.getPath());
            return super.add(source.getPath());
        } catch (IOException e) {
            throw new SigarException(e.getMessage());
        }
    }

    @Override // org.hyperic.sigar.FileWatcher
    public void onChange(FileInfo info) {
        try {
            mirror(info.getName());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override // org.hyperic.sigar.FileWatcher
    protected boolean changed(FileInfo info) throws SigarException {
        File dest = new File(getProcFile(info.getName()));
        long now = System.currentTimeMillis();
        if (now - dest.lastModified() > this.expire) {
            return true;
        }
        return false;
    }
}
