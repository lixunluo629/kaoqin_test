package org.apache.poi.poifs.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import org.apache.poi.hpsf.ClassID;

/* loaded from: poi-3.17.jar:org/apache/poi/poifs/filesystem/DirectoryEntry.class */
public interface DirectoryEntry extends Entry, Iterable<Entry> {
    Iterator<Entry> getEntries();

    Set<String> getEntryNames();

    boolean isEmpty();

    int getEntryCount();

    boolean hasEntry(String str);

    Entry getEntry(String str) throws FileNotFoundException;

    DocumentEntry createDocument(String str, InputStream inputStream) throws IOException;

    DocumentEntry createDocument(String str, int i, POIFSWriterListener pOIFSWriterListener) throws IOException;

    DirectoryEntry createDirectory(String str) throws IOException;

    ClassID getStorageClsid();

    void setStorageClsid(ClassID classID);
}
