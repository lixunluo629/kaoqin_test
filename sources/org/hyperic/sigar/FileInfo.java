package org.hyperic.sigar;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/FileInfo.class */
public class FileInfo extends FileAttrs implements Serializable {
    private static final long serialVersionUID = 607239;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd HH:mm");
    String name;
    private transient Sigar sigar;
    private boolean lstat;
    public static final int TYPE_NOFILE = 0;
    public static final int TYPE_REG = 1;
    public static final int TYPE_DIR = 2;
    public static final int TYPE_CHR = 3;
    public static final int TYPE_BLK = 4;
    public static final int TYPE_PIPE = 5;
    public static final int TYPE_LNK = 6;
    public static final int TYPE_SOCK = 7;
    public static final int TYPE_UNKFILE = 8;
    public static final int MODE_UREAD = 1024;
    public static final int MODE_UWRITE = 512;
    public static final int MODE_UEXECUTE = 256;
    public static final int MODE_GREAD = 64;
    public static final int MODE_GWRITE = 32;
    public static final int MODE_GEXECUTE = 16;
    public static final int MODE_WREAD = 4;
    public static final int MODE_WWRITE = 2;
    public static final int MODE_WEXECUTE = 1;
    private boolean dirStatEnabled = false;
    private DirStat stat = null;
    private FileInfo oldInfo = null;

    private static native String getTypeString(int i);

    native void gatherLink(Sigar sigar, String str) throws SigarException;

    private static native String getPermissionsString(long j);

    private static native int getMode(long j);

    public String getTypeString() {
        return getTypeString(this.type);
    }

    public char getTypeChar() {
        switch (this.type) {
            case 2:
                return 'd';
            case 3:
                return 'c';
            case 4:
                return 'b';
            case 5:
                return 'p';
            case 6:
                return 'l';
            case 7:
                return 's';
            default:
                return '-';
        }
    }

    public String getName() {
        return this.name;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public boolean equals(Object o) {
        return o.equals(this.name);
    }

    public String getPermissionsString() {
        return getPermissionsString(this.permissions);
    }

    public int getMode() {
        return getMode(this.permissions);
    }

    public void enableDirStat(boolean value) {
        this.dirStatEnabled = value;
        if (value) {
            if (this.type != 2) {
                throw new IllegalArgumentException(new StringBuffer().append(this.name).append(" is not a directory").toString());
            }
            try {
                if (this.stat == null) {
                    this.stat = this.sigar.getDirStat(this.name);
                } else {
                    this.stat.gather(this.sigar, this.name);
                }
            } catch (SigarException e) {
            }
        }
    }

    /* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/FileInfo$Diff.class */
    private class Diff {
        private String attr;
        private String old;
        private String cur;
        private final FileInfo this$0;

        Diff(FileInfo fileInfo, String attr, String old, String cur) {
            this.this$0 = fileInfo;
            this.attr = attr;
            this.old = old;
            this.cur = cur;
        }

        Diff(FileInfo fileInfo, String attr, int old, int cur) {
            this(fileInfo, attr, String.valueOf(old), String.valueOf(cur));
        }

        Diff(FileInfo fileInfo, String attr, long old, long cur) {
            this(fileInfo, attr, String.valueOf(old), String.valueOf(cur));
        }

        public String toString() {
            return new StringBuffer().append(this.attr).append(": ").append(this.old).append("|").append(this.cur).toString();
        }
    }

    private StringBuffer format(ArrayList changes) {
        StringBuffer sb = new StringBuffer();
        if (changes.size() == 0) {
            return sb;
        }
        int size = changes.size();
        for (int i = 0; i < size; i++) {
            sb.append('{').append(changes.get(i)).append('}');
        }
        return sb;
    }

    private static String formatDate(long time) {
        return DATE_FORMAT.format(new Date(time));
    }

    public String diff() {
        if (this.oldInfo == null) {
            return "";
        }
        return diff(this.oldInfo);
    }

    public String diff(DirStat stat) {
        DirStat thisStat = this.stat;
        ArrayList changes = new ArrayList();
        if (thisStat.files != stat.files) {
            changes.add(new Diff(this, "Files", stat.getFiles(), thisStat.getFiles()));
        }
        if (thisStat.subdirs != stat.subdirs) {
            changes.add(new Diff(this, "Subdirs", stat.getSubdirs(), thisStat.getSubdirs()));
        }
        if (thisStat.symlinks != stat.symlinks) {
            changes.add(new Diff(this, "Symlinks", stat.getSymlinks(), thisStat.getSymlinks()));
        }
        if (thisStat.chrdevs != stat.chrdevs) {
            changes.add(new Diff(this, "Chrdevs", stat.getChrdevs(), thisStat.getChrdevs()));
        }
        if (thisStat.blkdevs != stat.blkdevs) {
            changes.add(new Diff(this, "Blkdevs", stat.getBlkdevs(), thisStat.getBlkdevs()));
        }
        if (thisStat.sockets != stat.sockets) {
            changes.add(new Diff(this, "Sockets", stat.getSockets(), thisStat.getSockets()));
        }
        if (thisStat.total != stat.total) {
            changes.add(new Diff(this, "Total", stat.getTotal(), thisStat.getTotal()));
        }
        return format(changes).toString();
    }

    public String diff(FileInfo info) {
        ArrayList changes = new ArrayList();
        if (getMtime() != info.getMtime()) {
            changes.add(new Diff(this, "Mtime", formatDate(info.getMtime()), formatDate(getMtime())));
        } else if (getCtime() != info.getCtime()) {
            changes.add(new Diff(this, "Ctime", formatDate(info.getCtime()), formatDate(getCtime())));
        } else {
            return "";
        }
        if (getPermissions() != info.getPermissions()) {
            changes.add(new Diff(this, "Perms", info.getPermissionsString(), getPermissionsString()));
        }
        if (getType() != info.getType()) {
            changes.add(new Diff(this, "Type", info.getTypeString(), getTypeString()));
        }
        if (getUid() != info.getUid()) {
            changes.add(new Diff(this, "Uid", info.getUid(), getUid()));
        }
        if (getGid() != info.getGid()) {
            changes.add(new Diff(this, "Gid", info.getGid(), getGid()));
        }
        if (getSize() != info.getSize()) {
            changes.add(new Diff(this, "Size", info.getSize(), getSize()));
        }
        if (!OperatingSystem.IS_WIN32) {
            if (getInode() != info.getInode()) {
                changes.add(new Diff(this, "Inode", info.getInode(), getInode()));
            }
            if (getDevice() != info.getDevice()) {
                changes.add(new Diff(this, "Device", info.getDevice(), getDevice()));
            }
            if (getNlink() != info.getNlink()) {
                changes.add(new Diff(this, "Nlink", info.getNlink(), getNlink()));
            }
        }
        StringBuffer sb = format(changes);
        if (this.dirStatEnabled) {
            sb.append(diff(info.stat));
        }
        return sb.toString();
    }

    public FileInfo getPreviousInfo() {
        return this.oldInfo;
    }

    public boolean modified() throws SigarException {
        if (this.oldInfo == null) {
            this.oldInfo = new FileInfo();
            if (this.dirStatEnabled) {
                this.oldInfo.stat = new DirStat();
            }
        }
        copyTo(this.oldInfo);
        if (this.dirStatEnabled) {
            this.stat.copyTo(this.oldInfo.stat);
        }
        stat();
        return this.mtime != this.oldInfo.mtime;
    }

    public boolean changed() throws SigarException {
        return modified() || this.ctime != this.oldInfo.ctime;
    }

    public void stat() throws SigarException {
        long mtime = this.mtime;
        if (this.lstat) {
            gatherLink(this.sigar, this.name);
        } else {
            gather(this.sigar, this.name);
        }
        if (this.dirStatEnabled && mtime != this.mtime) {
            this.stat.gather(this.sigar, this.name);
        }
    }

    private static FileInfo fetchInfo(Sigar sigar, String name, boolean followSymlinks) throws SigarException {
        FileInfo info = new FileInfo();
        try {
            if (followSymlinks) {
                info.gather(sigar, name);
                info.lstat = false;
            } else {
                info.gatherLink(sigar, name);
                info.lstat = true;
            }
            info.sigar = sigar;
            info.name = name;
            return info;
        } catch (SigarException e) {
            e.setMessage(new StringBuffer().append(name).append(": ").append(e.getMessage()).toString());
            throw e;
        }
    }

    static FileInfo fetchFileInfo(Sigar sigar, String name) throws SigarException {
        return fetchInfo(sigar, name, true);
    }

    static FileInfo fetchLinkInfo(Sigar sigar, String name) throws SigarException {
        return fetchInfo(sigar, name, false);
    }
}
