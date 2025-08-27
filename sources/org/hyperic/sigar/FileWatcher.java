package org.hyperic.sigar;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/FileWatcher.class */
public abstract class FileWatcher {
    private Sigar sigar;
    private long interval = 0;
    private long lastTime = 0;
    private Set files = Collections.synchronizedSet(new HashSet());

    public abstract void onChange(FileInfo fileInfo);

    public void onNotFound(FileInfo info) {
    }

    public void onException(FileInfo info, SigarException e) {
    }

    public FileWatcher(Sigar sigar) {
        this.sigar = sigar;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getInterval() {
        return this.interval;
    }

    public FileInfo add(File file) throws SigarException {
        return add(file.getAbsolutePath());
    }

    public FileInfo add(String file) throws SigarException {
        FileInfo info = this.sigar.getFileInfo(file);
        this.files.add(info);
        return info;
    }

    public void add(File[] files) throws SigarException {
        for (File file : files) {
            add(file);
        }
    }

    public void add(String[] files) throws SigarException {
        for (String str : files) {
            add(str);
        }
    }

    public void remove(File file) {
        remove(file.getAbsolutePath());
    }

    public void remove(String file) {
        FileInfo info = new FileInfo();
        info.name = file;
        this.files.remove(info);
    }

    public void clear() {
        this.files.clear();
    }

    public Set getFiles() {
        return this.files;
    }

    protected boolean changed(FileInfo info) throws SigarException {
        return info.changed();
    }

    public void check() {
        if (this.interval != 0) {
            long timeNow = System.currentTimeMillis();
            long timeDiff = timeNow - this.lastTime;
            if (timeDiff < this.interval) {
                return;
            } else {
                this.lastTime = timeNow;
            }
        }
        synchronized (this.files) {
            for (FileInfo info : this.files) {
                try {
                    try {
                        if (changed(info)) {
                            onChange(info);
                        }
                    } catch (SigarException e) {
                        onException(info, e);
                    }
                } catch (SigarFileNotFoundException e2) {
                    onNotFound(info);
                }
            }
        }
    }
}
