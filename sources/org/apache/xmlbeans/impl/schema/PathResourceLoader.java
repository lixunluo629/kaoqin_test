package org.apache.xmlbeans.impl.schema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.ResourceLoader;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/PathResourceLoader.class */
public class PathResourceLoader implements ResourceLoader {
    private ResourceLoader[] _path;

    public PathResourceLoader(ResourceLoader[] loaderpath) throws IOException {
        this._path = new ResourceLoader[loaderpath.length];
        System.arraycopy(loaderpath, 0, this._path, 0, this._path.length);
    }

    public PathResourceLoader(File[] filepath) {
        List pathlist = new ArrayList();
        for (File file : filepath) {
            try {
                pathlist.add(new FileResourceLoader(file));
            } catch (IOException e) {
            }
        }
        this._path = (ResourceLoader[]) pathlist.toArray(new ResourceLoader[pathlist.size()]);
    }

    @Override // org.apache.xmlbeans.ResourceLoader
    public InputStream getResourceAsStream(String resourceName) {
        for (int i = 0; i < this._path.length; i++) {
            InputStream result = this._path[i].getResourceAsStream(resourceName);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override // org.apache.xmlbeans.ResourceLoader
    public void close() {
        for (int i = 0; i < this._path.length; i++) {
            try {
                this._path[i].close();
            } catch (Exception e) {
            }
        }
    }
}
