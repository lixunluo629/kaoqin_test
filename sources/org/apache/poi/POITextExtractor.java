package org.apache.poi;

import java.io.Closeable;
import java.io.IOException;

/* loaded from: poi-3.17.jar:org/apache/poi/POITextExtractor.class */
public abstract class POITextExtractor implements Closeable {
    private Closeable fsToClose = null;

    public abstract String getText();

    public abstract POITextExtractor getMetadataTextExtractor();

    public void setFilesystem(Closeable fs) {
        this.fsToClose = fs;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.fsToClose != null) {
            this.fsToClose.close();
        }
    }
}
