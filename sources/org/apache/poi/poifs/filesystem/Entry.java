package org.apache.poi.poifs.filesystem;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/filesystem/Entry.class */
public interface Entry {
    String getName();

    boolean isDirectoryEntry();

    boolean isDocumentEntry();

    DirectoryEntry getParent();

    boolean delete();

    boolean renameTo(String str);
}
