package org.hyperic.sigar;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/FileSystemMap.class */
public class FileSystemMap extends HashMap {
    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public void init(FileSystem[] fslist) {
        super.clear();
        for (int i = 0; i < fslist.length; i++) {
            super.put(fslist[i].getDirName(), fslist[i]);
        }
    }

    public FileSystem getFileSystem(String name) {
        return (FileSystem) get(name);
    }

    public boolean isMounted(String name) {
        return get(name) != null;
    }

    public FileSystem getMountPoint(String name) throws IOException {
        FileSystem fs = getFileSystem(name);
        if (fs != null) {
            return fs;
        }
        File dir = new File(name);
        if (!dir.exists()) {
            return null;
        }
        try {
            File dir2 = dir.getCanonicalFile();
            if (!dir2.isDirectory()) {
                dir2 = dir2.getParentFile();
            }
            do {
                FileSystem fs2 = getFileSystem(dir2.toString());
                if (fs2 != null) {
                    return fs2;
                }
                dir2 = dir2.getParentFile();
            } while (dir2 != null);
            return null;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
