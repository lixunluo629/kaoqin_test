package org.apache.xmlbeans.impl.schema;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.xmlbeans.ResourceLoader;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/FileResourceLoader.class */
public class FileResourceLoader implements ResourceLoader {
    private File _directory;
    private ZipFile _zipfile;

    public FileResourceLoader(File file) throws IOException {
        if (file.isDirectory()) {
            this._directory = file;
        } else {
            this._zipfile = new ZipFile(file);
        }
    }

    @Override // org.apache.xmlbeans.ResourceLoader
    public InputStream getResourceAsStream(String resourceName) {
        try {
            if (this._zipfile != null) {
                ZipEntry entry = this._zipfile.getEntry(resourceName);
                if (entry == null) {
                    return null;
                }
                return this._zipfile.getInputStream(entry);
            }
            return new FileInputStream(new File(this._directory, resourceName));
        } catch (IOException e) {
            return null;
        }
    }

    @Override // org.apache.xmlbeans.ResourceLoader
    public void close() throws IOException {
        if (this._zipfile != null) {
            try {
                this._zipfile.close();
            } catch (IOException e) {
            }
            this._zipfile = null;
        }
    }
}
