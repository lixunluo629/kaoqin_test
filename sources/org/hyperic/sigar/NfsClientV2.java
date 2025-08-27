package org.hyperic.sigar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/NfsClientV2.class */
public class NfsClientV2 implements Serializable {
    private static final long serialVersionUID = 18751;
    long _null = 0;
    long getattr = 0;
    long setattr = 0;
    long root = 0;
    long lookup = 0;
    long readlink = 0;
    long read = 0;
    long writecache = 0;
    long write = 0;
    long create = 0;
    long remove = 0;
    long rename = 0;
    long link = 0;
    long symlink = 0;
    long mkdir = 0;
    long rmdir = 0;
    long readdir = 0;
    long fsstat = 0;

    public native void gather(Sigar sigar) throws SigarException;

    static NfsClientV2 fetch(Sigar sigar) throws SigarException {
        NfsClientV2 nfsClientV2 = new NfsClientV2();
        nfsClientV2.gather(sigar);
        return nfsClientV2;
    }

    public long getNull() {
        return this._null;
    }

    public long getGetattr() {
        return this.getattr;
    }

    public long getSetattr() {
        return this.setattr;
    }

    public long getRoot() {
        return this.root;
    }

    public long getLookup() {
        return this.lookup;
    }

    public long getReadlink() {
        return this.readlink;
    }

    public long getRead() {
        return this.read;
    }

    public long getWritecache() {
        return this.writecache;
    }

    public long getWrite() {
        return this.write;
    }

    public long getCreate() {
        return this.create;
    }

    public long getRemove() {
        return this.remove;
    }

    public long getRename() {
        return this.rename;
    }

    public long getLink() {
        return this.link;
    }

    public long getSymlink() {
        return this.symlink;
    }

    public long getMkdir() {
        return this.mkdir;
    }

    public long getRmdir() {
        return this.rmdir;
    }

    public long getReaddir() {
        return this.readdir;
    }

    public long getFsstat() {
        return this.fsstat;
    }

    void copyTo(NfsClientV2 copy) {
        copy._null = this._null;
        copy.getattr = this.getattr;
        copy.setattr = this.setattr;
        copy.root = this.root;
        copy.lookup = this.lookup;
        copy.readlink = this.readlink;
        copy.read = this.read;
        copy.writecache = this.writecache;
        copy.write = this.write;
        copy.create = this.create;
        copy.remove = this.remove;
        copy.rename = this.rename;
        copy.link = this.link;
        copy.symlink = this.symlink;
        copy.mkdir = this.mkdir;
        copy.rmdir = this.rmdir;
        copy.readdir = this.readdir;
        copy.fsstat = this.fsstat;
    }

    public Map toMap() {
        Map map = new HashMap();
        String str_null = String.valueOf(this._null);
        if (!"-1".equals(str_null)) {
            map.put("_null", str_null);
        }
        String strgetattr = String.valueOf(this.getattr);
        if (!"-1".equals(strgetattr)) {
            map.put("Getattr", strgetattr);
        }
        String strsetattr = String.valueOf(this.setattr);
        if (!"-1".equals(strsetattr)) {
            map.put("Setattr", strsetattr);
        }
        String strroot = String.valueOf(this.root);
        if (!"-1".equals(strroot)) {
            map.put("Root", strroot);
        }
        String strlookup = String.valueOf(this.lookup);
        if (!"-1".equals(strlookup)) {
            map.put("Lookup", strlookup);
        }
        String strreadlink = String.valueOf(this.readlink);
        if (!"-1".equals(strreadlink)) {
            map.put("Readlink", strreadlink);
        }
        String strread = String.valueOf(this.read);
        if (!"-1".equals(strread)) {
            map.put("Read", strread);
        }
        String strwritecache = String.valueOf(this.writecache);
        if (!"-1".equals(strwritecache)) {
            map.put("Writecache", strwritecache);
        }
        String strwrite = String.valueOf(this.write);
        if (!"-1".equals(strwrite)) {
            map.put("Write", strwrite);
        }
        String strcreate = String.valueOf(this.create);
        if (!"-1".equals(strcreate)) {
            map.put("Create", strcreate);
        }
        String strremove = String.valueOf(this.remove);
        if (!"-1".equals(strremove)) {
            map.put("Remove", strremove);
        }
        String strrename = String.valueOf(this.rename);
        if (!"-1".equals(strrename)) {
            map.put("Rename", strrename);
        }
        String strlink = String.valueOf(this.link);
        if (!"-1".equals(strlink)) {
            map.put("Link", strlink);
        }
        String strsymlink = String.valueOf(this.symlink);
        if (!"-1".equals(strsymlink)) {
            map.put("Symlink", strsymlink);
        }
        String strmkdir = String.valueOf(this.mkdir);
        if (!"-1".equals(strmkdir)) {
            map.put("Mkdir", strmkdir);
        }
        String strrmdir = String.valueOf(this.rmdir);
        if (!"-1".equals(strrmdir)) {
            map.put("Rmdir", strrmdir);
        }
        String strreaddir = String.valueOf(this.readdir);
        if (!"-1".equals(strreaddir)) {
            map.put("Readdir", strreaddir);
        }
        String strfsstat = String.valueOf(this.fsstat);
        if (!"-1".equals(strfsstat)) {
            map.put("Fsstat", strfsstat);
        }
        return map;
    }

    public String toString() {
        return toMap().toString();
    }
}
