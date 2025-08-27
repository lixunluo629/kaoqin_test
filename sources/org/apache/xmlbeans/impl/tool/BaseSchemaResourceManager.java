package org.apache.xmlbeans.impl.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.apache.xmlbeans.impl.tool.SchemaImportResolver;
import org.apache.xmlbeans.impl.util.HexBin;
import org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemaEntry;
import org.apache.xmlbeans.impl.xb.xsdownload.DownloadedSchemasDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.springframework.beans.factory.xml.DelegatingEntityResolver;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/BaseSchemaResourceManager.class */
public abstract class BaseSchemaResourceManager extends SchemaImportResolver {
    private static final String USER_AGENT = "XMLBeans/" + XmlBeans.getVersion() + " (" + XmlBeans.getTitle() + ")";
    private String _defaultCopyDirectory;
    private DownloadedSchemasDocument _importsDoc;
    private Map _resourceForFilename = new HashMap();
    private Map _resourceForURL = new HashMap();
    private Map _resourceForNamespace = new HashMap();
    private Map _resourceForDigest = new HashMap();
    private Map _resourceForCacheEntry = new HashMap();
    private Set _redownloadSet = new HashSet();

    protected abstract void warning(String str);

    protected abstract boolean fileExists(String str);

    protected abstract InputStream inputStreamForFile(String str) throws IOException;

    protected abstract void writeInputStreamToFile(InputStream inputStream, String str) throws IOException;

    protected abstract void deleteFile(String str);

    protected abstract String[] getAllXSDFilenames();

    protected BaseSchemaResourceManager() {
    }

    protected final void init() {
        if (fileExists(getIndexFilename())) {
            try {
                this._importsDoc = DownloadedSchemasDocument.Factory.parse(inputStreamForFile(getIndexFilename()));
            } catch (IOException e) {
                this._importsDoc = null;
            } catch (Exception e2) {
                throw ((IllegalStateException) new IllegalStateException("Problem reading xsdownload.xml: please fix or delete this file").initCause(e2));
            }
        }
        if (this._importsDoc == null) {
            try {
                this._importsDoc = DownloadedSchemasDocument.Factory.parse("<dls:downloaded-schemas xmlns:dls='http://www.bea.com/2003/01/xmlbean/xsdownload' defaultDirectory='" + getDefaultSchemaDir() + "'/>");
            } catch (Exception e3) {
                throw ((IllegalStateException) new IllegalStateException().initCause(e3));
            }
        }
        String defaultDir = this._importsDoc.getDownloadedSchemas().getDefaultDirectory();
        if (defaultDir == null) {
            defaultDir = getDefaultSchemaDir();
        }
        this._defaultCopyDirectory = defaultDir;
        DownloadedSchemaEntry[] entries = this._importsDoc.getDownloadedSchemas().getEntryArray();
        for (DownloadedSchemaEntry downloadedSchemaEntry : entries) {
            updateResource(downloadedSchemaEntry);
        }
    }

    public final void writeCache() throws IOException {
        InputStream input = this._importsDoc.newInputStream(new XmlOptions().setSavePrettyPrint());
        writeInputStreamToFile(input, getIndexFilename());
    }

    public final void processAll(boolean sync, boolean refresh, boolean imports) throws NoSuchAlgorithmException, IOException {
        if (refresh) {
            this._redownloadSet = new HashSet();
        } else {
            this._redownloadSet = null;
        }
        String[] allFilenames = getAllXSDFilenames();
        if (sync) {
            syncCacheWithLocalXsdFiles(allFilenames, false);
        }
        SchemaResource[] starters = (SchemaResource[]) this._resourceForFilename.values().toArray(new SchemaResource[0]);
        if (refresh) {
            redownloadEntries(starters);
        }
        if (imports) {
            resolveImports(starters);
        }
        this._redownloadSet = null;
    }

    public final void process(String[] uris, String[] filenames, boolean sync, boolean refresh, boolean imports) throws NoSuchAlgorithmException, IOException {
        if (refresh) {
            this._redownloadSet = new HashSet();
        } else {
            this._redownloadSet = null;
        }
        if (filenames.length > 0) {
            syncCacheWithLocalXsdFiles(filenames, true);
        } else if (sync) {
            syncCacheWithLocalXsdFiles(getAllXSDFilenames(), false);
        }
        Set starterset = new HashSet();
        for (String str : uris) {
            SchemaResource resource = (SchemaResource) lookupResource(null, str);
            if (resource != null) {
                starterset.add(resource);
            }
        }
        for (int i = 0; i < filenames.length; i++) {
            SchemaResource resource2 = (SchemaResource) this._resourceForFilename.get(filenames);
            if (resource2 != null) {
                starterset.add(resource2);
            }
        }
        SchemaResource[] starters = (SchemaResource[]) starterset.toArray(new SchemaResource[0]);
        if (refresh) {
            redownloadEntries(starters);
        }
        if (imports) {
            resolveImports(starters);
        }
        this._redownloadSet = null;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00fa A[PHI: r12
  0x00fa: PHI (r12v2 'digest' java.lang.String) = (r12v1 'digest' java.lang.String), (r12v3 'digest' java.lang.String), (r12v3 'digest' java.lang.String) binds: [B:24:0x00f8, B:13:0x0075, B:15:0x0085] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void syncCacheWithLocalXsdFiles(java.lang.String[] r5, boolean r6) throws java.security.NoSuchAlgorithmException {
        /*
            Method dump skipped, instructions count: 343
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.tool.BaseSchemaResourceManager.syncCacheWithLocalXsdFiles(java.lang.String[], boolean):void");
    }

    private void redownloadEntries(SchemaResource[] resources) throws IOException {
        for (SchemaResource schemaResource : resources) {
            redownloadResource(schemaResource);
        }
    }

    private void deleteResourcesInSet(Set seenResources, boolean setToDelete) {
        Set seenCacheEntries = new HashSet();
        Iterator i = seenResources.iterator();
        while (i.hasNext()) {
            seenCacheEntries.add(((SchemaResource) i.next())._cacheEntry);
        }
        DownloadedSchemasDocument.DownloadedSchemas downloadedSchemas = this._importsDoc.getDownloadedSchemas();
        int i2 = 0;
        while (i2 < downloadedSchemas.sizeOfEntryArray()) {
            DownloadedSchemaEntry cacheEntry = downloadedSchemas.getEntryArray(i2);
            if (seenCacheEntries.contains(cacheEntry) == setToDelete) {
                SchemaResource resource = (SchemaResource) this._resourceForCacheEntry.get(cacheEntry);
                warning("Removing obsolete cache entry for " + resource.getFilename());
                if (resource != null) {
                    this._resourceForCacheEntry.remove(cacheEntry);
                    if (resource == this._resourceForFilename.get(resource.getFilename())) {
                        this._resourceForFilename.remove(resource.getFilename());
                    }
                    if (resource == this._resourceForDigest.get(resource.getSha1())) {
                        this._resourceForDigest.remove(resource.getSha1());
                    }
                    if (resource == this._resourceForNamespace.get(resource.getNamespace())) {
                        this._resourceForNamespace.remove(resource.getNamespace());
                    }
                    String[] urls = resource.getSchemaLocationArray();
                    for (int j = 0; j < urls.length; j++) {
                        if (resource == this._resourceForURL.get(urls[j])) {
                            this._resourceForURL.remove(urls[j]);
                        }
                    }
                }
                downloadedSchemas.removeEntry(i2);
                i2--;
            }
            i2++;
        }
    }

    private SchemaResource updateResource(DownloadedSchemaEntry entry) {
        String filename = entry.getFilename();
        if (filename == null) {
            return null;
        }
        SchemaResource resource = new SchemaResource(entry);
        this._resourceForCacheEntry.put(entry, resource);
        if (!this._resourceForFilename.containsKey(filename)) {
            this._resourceForFilename.put(filename, resource);
        }
        String digest = resource.getSha1();
        if (digest != null && !this._resourceForDigest.containsKey(digest)) {
            this._resourceForDigest.put(digest, resource);
        }
        String namespace = resource.getNamespace();
        if (namespace != null && !this._resourceForNamespace.containsKey(namespace)) {
            this._resourceForNamespace.put(namespace, resource);
        }
        String[] urls = resource.getSchemaLocationArray();
        for (int j = 0; j < urls.length; j++) {
            if (!this._resourceForURL.containsKey(urls[j])) {
                this._resourceForURL.put(urls[j], resource);
            }
        }
        return resource;
    }

    private static DigestInputStream digestInputStream(InputStream input) throws NoSuchAlgorithmException {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            DigestInputStream str = new DigestInputStream(input, sha);
            return str;
        } catch (NoSuchAlgorithmException e) {
            throw ((IllegalStateException) new IllegalStateException().initCause(e));
        }
    }

    private DownloadedSchemaEntry addNewEntry() {
        return this._importsDoc.getDownloadedSchemas().addNewEntry();
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/BaseSchemaResourceManager$SchemaResource.class */
    private class SchemaResource implements SchemaImportResolver.SchemaResource {
        DownloadedSchemaEntry _cacheEntry;

        SchemaResource(DownloadedSchemaEntry entry) {
            this._cacheEntry = entry;
        }

        public void setFilename(String filename) {
            this._cacheEntry.setFilename(filename);
        }

        public String getFilename() {
            return this._cacheEntry.getFilename();
        }

        @Override // org.apache.xmlbeans.impl.tool.SchemaImportResolver.SchemaResource
        public SchemaDocument.Schema getSchema() throws IOException {
            if (!BaseSchemaResourceManager.this.fileExists(getFilename())) {
                BaseSchemaResourceManager.this.redownloadResource(this);
            }
            try {
                return SchemaDocument.Factory.parse(BaseSchemaResourceManager.this.inputStreamForFile(getFilename())).getSchema();
            } catch (Exception e) {
                return null;
            }
        }

        public String getSha1() {
            return this._cacheEntry.getSha1();
        }

        @Override // org.apache.xmlbeans.impl.tool.SchemaImportResolver.SchemaResource
        public String getNamespace() {
            return this._cacheEntry.getNamespace();
        }

        public void setNamespace(String namespace) {
            this._cacheEntry.setNamespace(namespace);
        }

        @Override // org.apache.xmlbeans.impl.tool.SchemaImportResolver.SchemaResource
        public String getSchemaLocation() {
            if (this._cacheEntry.sizeOfSchemaLocationArray() > 0) {
                return this._cacheEntry.getSchemaLocationArray(0);
            }
            return null;
        }

        public String[] getSchemaLocationArray() {
            return this._cacheEntry.getSchemaLocationArray();
        }

        public int hashCode() {
            return getFilename().hashCode();
        }

        public boolean equals(Object obj) {
            return this == obj || getFilename().equals(((SchemaResource) obj).getFilename());
        }

        public void addSchemaLocation(String schemaLocation) {
            this._cacheEntry.addSchemaLocation(schemaLocation);
        }
    }

    @Override // org.apache.xmlbeans.impl.tool.SchemaImportResolver
    public SchemaImportResolver.SchemaResource lookupResource(String nsURI, String schemaLocation) throws IOException {
        SchemaResource result = fetchFromCache(nsURI, schemaLocation);
        if (result != null) {
            if (this._redownloadSet != null) {
                redownloadResource(result);
            }
            return result;
        }
        if (schemaLocation == null) {
            warning("No cached schema for namespace '" + nsURI + "', and no url specified");
            return null;
        }
        SchemaResource result2 = copyOrIdentifyDuplicateURL(schemaLocation, nsURI);
        if (this._redownloadSet != null) {
            this._redownloadSet.add(result2);
        }
        return result2;
    }

    private SchemaResource fetchFromCache(String nsURI, String schemaLocation) {
        SchemaResource result;
        SchemaResource result2;
        if (schemaLocation != null && (result2 = (SchemaResource) this._resourceForURL.get(schemaLocation)) != null) {
            return result2;
        }
        if (nsURI != null && (result = (SchemaResource) this._resourceForNamespace.get(nsURI)) != null) {
            return result;
        }
        return null;
    }

    private String uniqueFilenameForURI(String schemaLocation) throws URISyntaxException, IOException {
        String localFilename = new URI(schemaLocation).getRawPath();
        int i = localFilename.lastIndexOf(47);
        if (i >= 0) {
            localFilename = localFilename.substring(i + 1);
        }
        if (localFilename.endsWith(DelegatingEntityResolver.XSD_SUFFIX)) {
            localFilename = localFilename.substring(0, localFilename.length() - 4);
        }
        if (localFilename.length() == 0) {
            localFilename = "schema";
        }
        String candidateFilename = localFilename;
        int suffix = 1;
        while (suffix < 1000) {
            String candidate = this._defaultCopyDirectory + "/" + candidateFilename + DelegatingEntityResolver.XSD_SUFFIX;
            if (!fileExists(candidate)) {
                return candidate;
            }
            suffix++;
            candidateFilename = localFilename + suffix;
        }
        throw new IOException("Problem with filename " + localFilename + DelegatingEntityResolver.XSD_SUFFIX);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void redownloadResource(SchemaResource resource) throws IOException {
        if (this._redownloadSet != null) {
            if (this._redownloadSet.contains(resource)) {
                return;
            } else {
                this._redownloadSet.add(resource);
            }
        }
        String filename = resource.getFilename();
        String schemaLocation = resource.getSchemaLocation();
        if (schemaLocation == null || filename == null) {
            return;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            URL url = new URL(schemaLocation);
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("User-Agent", USER_AGENT);
            conn.addRequestProperty("Accept", "application/xml, text/xml, */*");
            DigestInputStream input = digestInputStream(conn.getInputStream());
            IOUtil.copyCompletely(input, buffer);
            String digest = HexBin.bytesToString(input.getMessageDigest().digest());
            if (digest.equals(resource.getSha1()) && fileExists(filename)) {
                warning("Resource " + filename + " is unchanged from " + schemaLocation + ".");
                return;
            }
            try {
                InputStream source = new ByteArrayInputStream(buffer.toByteArray());
                writeInputStreamToFile(source, filename);
                warning("Refreshed " + filename + " from " + schemaLocation);
            } catch (IOException e) {
                warning("Could not write to file " + filename + " for " + schemaLocation + ":" + e.getMessage());
            }
        } catch (Exception e2) {
            warning("Could not copy remote resource " + schemaLocation + ":" + e2.getMessage());
        }
    }

    private SchemaResource copyOrIdentifyDuplicateURL(String schemaLocation, String namespace) {
        try {
            String targetFilename = uniqueFilenameForURI(schemaLocation);
            try {
                URL url = new URL(schemaLocation);
                DigestInputStream input = digestInputStream(url.openStream());
                writeInputStreamToFile(input, targetFilename);
                String digest = HexBin.bytesToString(input.getMessageDigest().digest());
                SchemaResource result = (SchemaResource) this._resourceForDigest.get(digest);
                if (result != null) {
                    deleteFile(targetFilename);
                    result.addSchemaLocation(schemaLocation);
                    if (!this._resourceForURL.containsKey(schemaLocation)) {
                        this._resourceForURL.put(schemaLocation, result);
                    }
                    return result;
                }
                warning("Downloaded " + schemaLocation + " to " + targetFilename);
                DownloadedSchemaEntry newEntry = addNewEntry();
                newEntry.setFilename(targetFilename);
                newEntry.setSha1(digest);
                if (namespace != null) {
                    newEntry.setNamespace(namespace);
                }
                newEntry.addSchemaLocation(schemaLocation);
                return updateResource(newEntry);
            } catch (Exception e) {
                warning("Could not copy remote resource " + schemaLocation + ":" + e.getMessage());
                return null;
            }
        } catch (IOException e2) {
            warning("Could not create local file for " + schemaLocation + ":" + e2.getMessage());
            return null;
        } catch (URISyntaxException e3) {
            warning("Invalid URI '" + schemaLocation + "':" + e3.getMessage());
            return null;
        }
    }

    @Override // org.apache.xmlbeans.impl.tool.SchemaImportResolver
    public void reportActualNamespace(SchemaImportResolver.SchemaResource rresource, String actualNamespace) {
        SchemaResource resource = (SchemaResource) rresource;
        String oldNamespace = resource.getNamespace();
        if (oldNamespace != null && this._resourceForNamespace.get(oldNamespace) == resource) {
            this._resourceForNamespace.remove(oldNamespace);
        }
        if (!this._resourceForNamespace.containsKey(actualNamespace)) {
            this._resourceForNamespace.put(actualNamespace, resource);
        }
        resource.setNamespace(actualNamespace);
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x0014 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String shaDigestForFile(java.lang.String r4) throws java.security.NoSuchAlgorithmException, java.io.IOException {
        /*
            r3 = this;
            r0 = r3
            r1 = r4
            java.io.InputStream r0 = r0.inputStreamForFile(r1)
            java.security.DigestInputStream r0 = digestInputStream(r0)
            r5 = r0
            r0 = 4096(0x1000, float:5.74E-42)
            byte[] r0 = new byte[r0]
            r6 = r0
            r0 = 1
            r7 = r0
        L12:
            r0 = r7
            if (r0 <= 0) goto L21
            r0 = r5
            r1 = r6
            int r0 = r0.read(r1)
            r7 = r0
            goto L12
        L21:
            r0 = r5
            r0.close()
            r0 = r5
            java.security.MessageDigest r0 = r0.getMessageDigest()
            byte[] r0 = r0.digest()
            java.lang.String r0 = org.apache.xmlbeans.impl.util.HexBin.bytesToString(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.tool.BaseSchemaResourceManager.shaDigestForFile(java.lang.String):java.lang.String");
    }

    protected String getIndexFilename() {
        return "./xsdownload.xml";
    }

    protected String getDefaultSchemaDir() {
        return "./schema";
    }
}
