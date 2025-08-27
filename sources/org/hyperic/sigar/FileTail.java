package org.hyperic.sigar;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.log4j.Logger;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/FileTail.class */
public abstract class FileTail extends FileWatcher {
    public static final String PROP_USE_SUDO = "sigar.tail.sudo";
    private boolean useSudo;
    private static final Logger log;
    private static final boolean isDebug;
    static Class class$org$hyperic$sigar$FileTail;

    public abstract void tail(FileInfo fileInfo, Reader reader);

    static {
        Class clsClass$;
        if (class$org$hyperic$sigar$FileTail == null) {
            clsClass$ = class$("org.hyperic.sigar.FileTail");
            class$org$hyperic$sigar$FileTail = clsClass$;
        } else {
            clsClass$ = class$org$hyperic$sigar$FileTail;
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

    public FileTail(Sigar sigar) {
        super(sigar);
        this.useSudo = "true".equals(System.getProperty(PROP_USE_SUDO));
    }

    public void useSudo(boolean useSudo) {
        this.useSudo = useSudo;
    }

    static void error(String name, Throwable exc) {
        String msg = new StringBuffer().append(name).append(": ").append(exc.getMessage()).toString();
        log.error(msg, exc);
    }

    @Override // org.hyperic.sigar.FileWatcher
    public void onChange(FileInfo info) throws IOException {
        InputStream in = null;
        Reader reader = null;
        String name = info.getName();
        try {
            try {
                if (this.useSudo) {
                    in = new SudoFileInputStream(name);
                } else {
                    in = new FileInputStream(name);
                }
                long offset = getOffset(info);
                if (offset > 0) {
                    in.skip(offset);
                }
                reader = new InputStreamReader(in);
                tail(info, reader);
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (Throwable th) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e3) {
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (IOException e5) {
            error(name, e5);
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e6) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e7) {
                }
            }
        }
    }

    @Override // org.hyperic.sigar.FileWatcher
    public FileInfo add(String file) throws SigarException {
        FileInfo info = super.add(file);
        if (isDebug) {
            log.debug(new StringBuffer().append("add: ").append(file).append(SymbolConstants.EQUAL_SYMBOL).append(info).toString());
        }
        return info;
    }

    @Override // org.hyperic.sigar.FileWatcher
    protected boolean changed(FileInfo info) throws SigarException {
        return info.modified() || info.getPreviousInfo().size != info.size;
    }

    private long getOffset(FileInfo current) {
        FileInfo previous = current.getPreviousInfo();
        if (previous == null) {
            if (isDebug) {
                log.debug(new StringBuffer().append(current.getName()).append(": first stat").toString());
            }
            return current.size;
        }
        if (current.inode != previous.inode) {
            if (isDebug) {
                log.debug(new StringBuffer().append(current.getName()).append(": file inode changed").toString());
                return -1L;
            }
            return -1L;
        }
        if (current.size < previous.size) {
            if (isDebug) {
                log.debug(new StringBuffer().append(current.getName()).append(": file truncated").toString());
                return -1L;
            }
            return -1L;
        }
        if (isDebug) {
            long diff = current.size - previous.size;
            log.debug(new StringBuffer().append(current.getName()).append(": ").append(diff).append(" new bytes").toString());
        }
        return previous.size;
    }
}
