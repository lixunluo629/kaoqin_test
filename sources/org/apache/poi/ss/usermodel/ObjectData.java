package org.apache.poi.ss.usermodel;

import java.io.IOException;
import org.apache.poi.poifs.filesystem.DirectoryEntry;

/* loaded from: poi-3.17.jar:org/apache/poi/ss/usermodel/ObjectData.class */
public interface ObjectData extends SimpleShape {
    byte[] getObjectData() throws IOException;

    boolean hasDirectoryEntry();

    DirectoryEntry getDirectory() throws IOException;

    String getOLE2ClassName();

    String getFileName();

    PictureData getPictureData();
}
