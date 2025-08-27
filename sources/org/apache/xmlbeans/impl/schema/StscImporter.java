package org.apache.xmlbeans.impl.schema;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.apache.xmlbeans.impl.common.XmlEncodingSniffer;
import org.apache.xmlbeans.impl.xb.xsdschema.ImportDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.IncludeDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.RedefineDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.springframework.util.ResourceUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscImporter.class */
public class StscImporter {
    private static final String PROJECT_URL_PREFIX = "project://local";

    public static SchemaToProcess[] resolveImportsAndIncludes(SchemaDocument.Schema[] startWith, boolean forceSrcSave) {
        DownloadTable engine = new DownloadTable(startWith);
        return engine.resolveImportsAndIncludes(forceSrcSave);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscImporter$SchemaToProcess.class */
    public static class SchemaToProcess {
        private SchemaDocument.Schema schema;
        private String chameleonNamespace;
        private List includes;
        private List redefines;
        private List redefineObjects;
        private Set indirectIncludes;
        private Set indirectIncludedBy;

        public SchemaToProcess(SchemaDocument.Schema schema, String chameleonNamespace) {
            this.schema = schema;
            this.chameleonNamespace = chameleonNamespace;
        }

        public SchemaDocument.Schema getSchema() {
            return this.schema;
        }

        public String getSourceName() {
            return this.schema.documentProperties().getSourceName();
        }

        public String getChameleonNamespace() {
            return this.chameleonNamespace;
        }

        public List getRedefines() {
            return this.redefines;
        }

        public List getRedefineObjects() {
            return this.redefineObjects;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addInclude(SchemaToProcess include) {
            if (this.includes == null) {
                this.includes = new ArrayList();
            }
            this.includes.add(include);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addRedefine(SchemaToProcess redefine, RedefineDocument.Redefine object) {
            if (this.redefines == null || this.redefineObjects == null) {
                this.redefines = new ArrayList();
                this.redefineObjects = new ArrayList();
            }
            this.redefines.add(redefine);
            this.redefineObjects.add(object);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void buildIndirectReferences() {
            if (this.includes != null) {
                for (int i = 0; i < this.includes.size(); i++) {
                    SchemaToProcess schemaToProcess = (SchemaToProcess) this.includes.get(i);
                    addIndirectIncludes(schemaToProcess);
                }
            }
            if (this.redefines != null) {
                for (int i2 = 0; i2 < this.redefines.size(); i2++) {
                    SchemaToProcess schemaToProcess2 = (SchemaToProcess) this.redefines.get(i2);
                    addIndirectIncludes(schemaToProcess2);
                }
            }
        }

        private void addIndirectIncludes(SchemaToProcess schemaToProcess) {
            if (this.indirectIncludes == null) {
                this.indirectIncludes = new HashSet();
            }
            this.indirectIncludes.add(schemaToProcess);
            if (schemaToProcess.indirectIncludedBy == null) {
                schemaToProcess.indirectIncludedBy = new HashSet();
            }
            schemaToProcess.indirectIncludedBy.add(this);
            addIndirectIncludesHelper(this, schemaToProcess);
            if (this.indirectIncludedBy != null) {
                for (SchemaToProcess stp : this.indirectIncludedBy) {
                    stp.indirectIncludes.add(schemaToProcess);
                    schemaToProcess.indirectIncludedBy.add(stp);
                    addIndirectIncludesHelper(stp, schemaToProcess);
                }
            }
        }

        private static void addIndirectIncludesHelper(SchemaToProcess including, SchemaToProcess schemaToProcess) {
            if (schemaToProcess.indirectIncludes != null) {
                for (SchemaToProcess stp : schemaToProcess.indirectIncludes) {
                    including.indirectIncludes.add(stp);
                    stp.indirectIncludedBy.add(including);
                }
            }
        }

        public boolean indirectIncludes(SchemaToProcess schemaToProcess) {
            return this.indirectIncludes != null && this.indirectIncludes.contains(schemaToProcess);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SchemaToProcess)) {
                return false;
            }
            SchemaToProcess schemaToProcess = (SchemaToProcess) o;
            if (this.chameleonNamespace != null) {
                if (!this.chameleonNamespace.equals(schemaToProcess.chameleonNamespace)) {
                    return false;
                }
            } else if (schemaToProcess.chameleonNamespace != null) {
                return false;
            }
            return this.schema == schemaToProcess.schema;
        }

        public int hashCode() {
            int result = this.schema.hashCode();
            return (29 * result) + (this.chameleonNamespace != null ? this.chameleonNamespace.hashCode() : 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String baseURLForDoc(XmlObject obj) {
        String path = obj.documentProperties().getSourceName();
        if (path == null) {
            return null;
        }
        if (path.startsWith("/")) {
            return PROJECT_URL_PREFIX + path.replace('\\', '/');
        }
        int colon = path.indexOf(58);
        if (colon > 1 && path.substring(0, colon).matches("^\\w+$")) {
            return path;
        }
        return "project://local/" + path.replace('\\', '/');
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static URI parseURI(String s) {
        if (s == null) {
            return null;
        }
        try {
            return new URI(s);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static URI resolve(URI base, String child) throws URISyntaxException {
        URI childUri = new URI(child);
        URI ruri = base.resolve(childUri);
        if (childUri.equals(ruri) && !childUri.isAbsolute() && (base.getScheme().equals("jar") || base.getScheme().equals("zip"))) {
            String r = base.toString();
            int lastslash = r.lastIndexOf(47);
            String r2 = r.substring(0, lastslash) + "/" + childUri;
            int exclPointSlashIndex = r2.lastIndexOf(ResourceUtils.JAR_URL_SEPARATOR);
            if (exclPointSlashIndex > 0) {
                int iIndexOf = r2.indexOf("/..", exclPointSlashIndex);
                while (true) {
                    int slashDotDotIndex = iIndexOf;
                    if (slashDotDotIndex <= 0) {
                        break;
                    }
                    int prevSlashIndex = r2.lastIndexOf("/", slashDotDotIndex - 1);
                    if (prevSlashIndex >= exclPointSlashIndex) {
                        String temp = r2.substring(slashDotDotIndex + 3);
                        r2 = r2.substring(0, prevSlashIndex).concat(temp);
                    }
                    iIndexOf = r2.indexOf("/..", exclPointSlashIndex);
                }
            }
            return URI.create(r2);
        }
        if ("file".equals(ruri.getScheme()) && !child.equals(ruri) && base.getPath().startsWith("//") && !ruri.getPath().startsWith("//")) {
            String path = "///".concat(ruri.getPath());
            try {
                ruri = new URI("file", null, path, ruri.getQuery(), ruri.getFragment());
            } catch (URISyntaxException e) {
            }
        }
        return ruri;
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscImporter$DownloadTable.class */
    public static class DownloadTable {
        private Map schemaByNsLocPair = new HashMap();
        private Map schemaByDigestKey = new HashMap();
        private LinkedList scanNeeded = new LinkedList();
        private Set emptyNamespaceSchemas = new HashSet();
        private Map scannedAlready = new HashMap();
        private Set failedDownloads = new HashSet();

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscImporter$DownloadTable$NsLocPair.class */
        private static class NsLocPair {
            private String namespaceURI;
            private String locationURL;

            public NsLocPair(String namespaceURI, String locationURL) {
                this.namespaceURI = namespaceURI;
                this.locationURL = locationURL;
            }

            public String getNamespaceURI() {
                return this.namespaceURI;
            }

            public String getLocationURL() {
                return this.locationURL;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof NsLocPair)) {
                    return false;
                }
                NsLocPair nsLocPair = (NsLocPair) o;
                if (this.locationURL != null) {
                    if (!this.locationURL.equals(nsLocPair.locationURL)) {
                        return false;
                    }
                } else if (nsLocPair.locationURL != null) {
                    return false;
                }
                return this.namespaceURI != null ? this.namespaceURI.equals(nsLocPair.namespaceURI) : nsLocPair.namespaceURI == null;
            }

            public int hashCode() {
                int result = this.namespaceURI != null ? this.namespaceURI.hashCode() : 0;
                return (29 * result) + (this.locationURL != null ? this.locationURL.hashCode() : 0);
            }
        }

        /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/StscImporter$DownloadTable$DigestKey.class */
        private static class DigestKey {
            byte[] _digest;
            int _hashCode;

            DigestKey(byte[] digest) {
                this._digest = digest;
                for (int i = 0; i < 4 && i < digest.length; i++) {
                    this._hashCode <<= 8;
                    this._hashCode += digest[i];
                }
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o instanceof DigestKey) {
                    return Arrays.equals(this._digest, ((DigestKey) o)._digest);
                }
                return false;
            }

            public int hashCode() {
                return this._hashCode;
            }
        }

        private SchemaDocument.Schema downloadSchema(XmlObject referencedBy, String targetNamespace, String locationURL) {
            String string;
            XmlObject xdoc;
            SchemaDocument.Schema result;
            String shortname;
            SchemaDocument.Schema result2;
            SchemaDocument.Schema result3;
            SchemaDocument.Schema result4;
            if (locationURL == null) {
                return null;
            }
            StscState state = StscState.get();
            URI baseURI = StscImporter.parseURI(StscImporter.baseURLForDoc(referencedBy));
            if (baseURI == null) {
                string = locationURL;
            } else {
                try {
                    string = StscImporter.resolve(baseURI, locationURL).toString();
                } catch (URISyntaxException e) {
                    state.error("Could not find resource - invalid location URL: " + e.getMessage(), 56, referencedBy);
                    return null;
                }
            }
            String absoluteURL = string;
            if (state.isFileProcessed(absoluteURL)) {
                return null;
            }
            if (absoluteURL != null && targetNamespace != null && (result4 = (SchemaDocument.Schema) this.schemaByNsLocPair.get(new NsLocPair(targetNamespace, absoluteURL))) != null) {
                return result4;
            }
            if (targetNamespace != null && !targetNamespace.equals("")) {
                if (!state.shouldDownloadURI(absoluteURL) && (result3 = (SchemaDocument.Schema) this.schemaByNsLocPair.get(new NsLocPair(targetNamespace, null))) != null) {
                    return result3;
                }
                if (state.linkerDefinesNamespace(targetNamespace)) {
                    return null;
                }
            }
            if (absoluteURL != null && (result2 = (SchemaDocument.Schema) this.schemaByNsLocPair.get(new NsLocPair(null, absoluteURL))) != null) {
                return result2;
            }
            if (absoluteURL == null) {
                state.error("Could not find resource - no valid location URL.", 56, referencedBy);
                return null;
            }
            if (previouslyFailedToDownload(absoluteURL)) {
                return null;
            }
            if (!state.shouldDownloadURI(absoluteURL)) {
                state.error("Could not load resource \"" + absoluteURL + "\" (network downloads disabled).", 56, referencedBy);
                addFailedDownload(absoluteURL);
                return null;
            }
            try {
                xdoc = downloadDocument(state.getS4SLoader(), targetNamespace, absoluteURL);
                result = findMatchByDigest(xdoc);
                shortname = state.relativize(absoluteURL);
            } catch (MalformedURLException e2) {
                state.error("URL \"" + absoluteURL + "\" is not well-formed", 56, referencedBy);
            } catch (IOException connectionProblem) {
                state.error(connectionProblem.toString(), 56, referencedBy);
            } catch (XmlException e3) {
                state.error("Problem parsing referenced XML resource - " + e3.getMessage(), 56, referencedBy);
            }
            if (result != null) {
                String dupname = state.relativize(result.documentProperties().getSourceName());
                if (dupname != null) {
                    state.info(shortname + " is the same as " + dupname + " (ignoring the duplicate file)");
                } else {
                    state.info(shortname + " is the same as another schema");
                }
            } else {
                XmlOptions voptions = new XmlOptions();
                voptions.setErrorListener(state.getErrorListener());
                if (!(xdoc instanceof SchemaDocument) || !xdoc.validate(voptions)) {
                    state.error("Referenced document is not a valid schema", 56, referencedBy);
                    addFailedDownload(absoluteURL);
                    return null;
                }
                SchemaDocument sDoc = (SchemaDocument) xdoc;
                result = sDoc.getSchema();
                state.info("Loading referenced file " + shortname);
            }
            NsLocPair key = new NsLocPair(emptyStringIfNull(result.getTargetNamespace()), absoluteURL);
            addSuccessfulDownload(key, result);
            return result;
        }

        static XmlObject downloadDocument(SchemaTypeLoader loader, String namespace, String absoluteURL) throws XmlException, IOException {
            StscState state = StscState.get();
            EntityResolver resolver = state.getEntityResolver();
            if (resolver != null) {
                try {
                    InputSource source = resolver.resolveEntity(namespace, absoluteURL);
                    if (source != null) {
                        state.addSourceUri(absoluteURL, null);
                        Reader reader = source.getCharacterStream();
                        if (reader != null) {
                            Reader reader2 = copySchemaSource(absoluteURL, reader, state);
                            XmlOptions options = new XmlOptions();
                            options.setLoadLineNumbers();
                            options.setDocumentSourceName(absoluteURL);
                            return loader.parse(reader2, (SchemaType) null, options);
                        }
                        InputStream bytes = source.getByteStream();
                        if (bytes != null) {
                            InputStream bytes2 = copySchemaSource(absoluteURL, bytes, state);
                            String encoding = source.getEncoding();
                            XmlOptions options2 = new XmlOptions();
                            options2.setLoadLineNumbers();
                            options2.setLoadMessageDigest();
                            options2.setDocumentSourceName(absoluteURL);
                            if (encoding != null) {
                                options2.setCharacterEncoding(encoding);
                            }
                            return loader.parse(bytes2, (SchemaType) null, options2);
                        }
                        String urlToLoad = source.getSystemId();
                        if (urlToLoad == null) {
                            throw new IOException("EntityResolver unable to resolve " + absoluteURL + " (for namespace " + namespace + ")");
                        }
                        copySchemaSource(absoluteURL, state, false);
                        XmlOptions options3 = new XmlOptions();
                        options3.setLoadLineNumbers();
                        options3.setLoadMessageDigest();
                        options3.setDocumentSourceName(absoluteURL);
                        URL urlDownload = new URL(urlToLoad);
                        return loader.parse(urlDownload, (SchemaType) null, options3);
                    }
                } catch (SAXException e) {
                    throw new XmlException(e);
                }
            }
            state.addSourceUri(absoluteURL, null);
            copySchemaSource(absoluteURL, state, false);
            XmlOptions options4 = new XmlOptions();
            options4.setLoadLineNumbers();
            options4.setLoadMessageDigest();
            URL urlDownload2 = new URL(absoluteURL);
            return loader.parse(urlDownload2, (SchemaType) null, options4);
        }

        private void addSuccessfulDownload(NsLocPair key, SchemaDocument.Schema schema) {
            byte[] digest = schema.documentProperties().getMessageDigest();
            if (digest == null) {
                StscState.get().addSchemaDigest(null);
            } else {
                DigestKey dk = new DigestKey(digest);
                if (!this.schemaByDigestKey.containsKey(dk)) {
                    this.schemaByDigestKey.put(new DigestKey(digest), schema);
                    StscState.get().addSchemaDigest(digest);
                }
            }
            this.schemaByNsLocPair.put(key, schema);
            NsLocPair key1 = new NsLocPair(key.getNamespaceURI(), null);
            if (!this.schemaByNsLocPair.containsKey(key1)) {
                this.schemaByNsLocPair.put(key1, schema);
            }
            NsLocPair key2 = new NsLocPair(null, key.getLocationURL());
            if (!this.schemaByNsLocPair.containsKey(key2)) {
                this.schemaByNsLocPair.put(key2, schema);
            }
        }

        private SchemaDocument.Schema findMatchByDigest(XmlObject original) {
            byte[] digest = original.documentProperties().getMessageDigest();
            if (digest == null) {
                return null;
            }
            return (SchemaDocument.Schema) this.schemaByDigestKey.get(new DigestKey(digest));
        }

        private void addFailedDownload(String locationURL) {
            this.failedDownloads.add(locationURL);
        }

        private boolean previouslyFailedToDownload(String locationURL) {
            return this.failedDownloads.contains(locationURL);
        }

        private static boolean nullableStringsMatch(String s1, String s2) {
            if (s1 == null && s2 == null) {
                return true;
            }
            if (s1 == null || s2 == null) {
                return false;
            }
            return s1.equals(s2);
        }

        private static String emptyStringIfNull(String s) {
            if (s == null) {
                return "";
            }
            return s;
        }

        private SchemaToProcess addScanNeeded(SchemaToProcess stp) {
            if (!this.scannedAlready.containsKey(stp)) {
                this.scannedAlready.put(stp, stp);
                this.scanNeeded.add(stp);
                return stp;
            }
            return (SchemaToProcess) this.scannedAlready.get(stp);
        }

        private void addEmptyNamespaceSchema(SchemaDocument.Schema s) {
            this.emptyNamespaceSchemas.add(s);
        }

        private void usedEmptyNamespaceSchema(SchemaDocument.Schema s) {
            this.emptyNamespaceSchemas.remove(s);
        }

        private boolean fetchRemainingEmptyNamespaceSchemas() {
            if (this.emptyNamespaceSchemas.isEmpty()) {
                return false;
            }
            for (SchemaDocument.Schema schema : this.emptyNamespaceSchemas) {
                addScanNeeded(new SchemaToProcess(schema, null));
            }
            this.emptyNamespaceSchemas.clear();
            return true;
        }

        private boolean hasNextToScan() {
            return !this.scanNeeded.isEmpty();
        }

        private SchemaToProcess nextToScan() {
            SchemaToProcess next = (SchemaToProcess) this.scanNeeded.removeFirst();
            return next;
        }

        public DownloadTable(SchemaDocument.Schema[] startWith) {
            for (int i = 0; i < startWith.length; i++) {
                String targetNamespace = startWith[i].getTargetNamespace();
                NsLocPair key = new NsLocPair(targetNamespace, StscImporter.baseURLForDoc(startWith[i]));
                addSuccessfulDownload(key, startWith[i]);
                if (targetNamespace != null) {
                    addScanNeeded(new SchemaToProcess(startWith[i], null));
                } else {
                    addEmptyNamespaceSchema(startWith[i]);
                }
            }
        }

        public SchemaToProcess[] resolveImportsAndIncludes(boolean forceSave) throws IOException {
            StscState state = StscState.get();
            List result = new ArrayList();
            boolean hasRedefinitions = false;
            while (true) {
                if (hasNextToScan()) {
                    SchemaToProcess stp = nextToScan();
                    String uri = stp.getSourceName();
                    state.addSourceUri(uri, null);
                    result.add(stp);
                    copySchemaSource(uri, state, forceSave);
                    ImportDocument.Import[] imports = stp.getSchema().getImportArray();
                    for (int i = 0; i < imports.length; i++) {
                        SchemaDocument.Schema imported = downloadSchema(imports[i], emptyStringIfNull(imports[i].getNamespace()), imports[i].getSchemaLocation());
                        if (imported != null) {
                            if (!nullableStringsMatch(imported.getTargetNamespace(), imports[i].getNamespace())) {
                                StscState.get().error("Imported schema has a target namespace \"" + imported.getTargetNamespace() + "\" that does not match the specified \"" + imports[i].getNamespace() + SymbolConstants.QUOTES_SYMBOL, 4, imports[i]);
                            } else {
                                addScanNeeded(new SchemaToProcess(imported, null));
                            }
                        }
                    }
                    IncludeDocument.Include[] includes = stp.getSchema().getIncludeArray();
                    String sourceNamespace = stp.getChameleonNamespace();
                    if (sourceNamespace == null) {
                        sourceNamespace = emptyStringIfNull(stp.getSchema().getTargetNamespace());
                    }
                    for (int i2 = 0; i2 < includes.length; i2++) {
                        SchemaDocument.Schema included = downloadSchema(includes[i2], null, includes[i2].getSchemaLocation());
                        if (included != null) {
                            if (emptyStringIfNull(included.getTargetNamespace()).equals(sourceNamespace)) {
                                SchemaToProcess s = addScanNeeded(new SchemaToProcess(included, null));
                                stp.addInclude(s);
                            } else if (included.getTargetNamespace() != null) {
                                StscState.get().error("Included schema has a target namespace \"" + included.getTargetNamespace() + "\" that does not match the source namespace \"" + sourceNamespace + SymbolConstants.QUOTES_SYMBOL, 4, includes[i2]);
                            } else {
                                SchemaToProcess s2 = addScanNeeded(new SchemaToProcess(included, sourceNamespace));
                                stp.addInclude(s2);
                                usedEmptyNamespaceSchema(included);
                            }
                        }
                    }
                    RedefineDocument.Redefine[] redefines = stp.getSchema().getRedefineArray();
                    String sourceNamespace2 = stp.getChameleonNamespace();
                    if (sourceNamespace2 == null) {
                        sourceNamespace2 = emptyStringIfNull(stp.getSchema().getTargetNamespace());
                    }
                    for (int i3 = 0; i3 < redefines.length; i3++) {
                        SchemaDocument.Schema redefined = downloadSchema(redefines[i3], null, redefines[i3].getSchemaLocation());
                        if (redefined != null) {
                            if (emptyStringIfNull(redefined.getTargetNamespace()).equals(sourceNamespace2)) {
                                SchemaToProcess s3 = addScanNeeded(new SchemaToProcess(redefined, null));
                                stp.addRedefine(s3, redefines[i3]);
                                hasRedefinitions = true;
                            } else if (redefined.getTargetNamespace() != null) {
                                StscState.get().error("Redefined schema has a target namespace \"" + redefined.getTargetNamespace() + "\" that does not match the source namespace \"" + sourceNamespace2 + SymbolConstants.QUOTES_SYMBOL, 4, redefines[i3]);
                            } else {
                                SchemaToProcess s4 = addScanNeeded(new SchemaToProcess(redefined, sourceNamespace2));
                                stp.addRedefine(s4, redefines[i3]);
                                usedEmptyNamespaceSchema(redefined);
                                hasRedefinitions = true;
                            }
                        }
                    }
                } else if (!fetchRemainingEmptyNamespaceSchemas()) {
                    break;
                }
            }
            if (hasRedefinitions) {
                for (int i4 = 0; i4 < result.size(); i4++) {
                    SchemaToProcess schemaToProcess = (SchemaToProcess) result.get(i4);
                    schemaToProcess.buildIndirectReferences();
                }
            }
            return (SchemaToProcess[]) result.toArray(new SchemaToProcess[result.size()]);
        }

        private static Reader copySchemaSource(String url, Reader reader, StscState state) throws IOException {
            if (state.getSchemasDir() == null) {
                return reader;
            }
            String schemalocation = state.sourceNameForUri(url);
            File targetFile = new File(state.getSchemasDir(), schemalocation);
            if (targetFile.exists()) {
                return reader;
            }
            try {
                File parentDir = new File(targetFile.getParent());
                IOUtil.createDir(parentDir, null);
                CharArrayReader car = copy(reader);
                XmlEncodingSniffer xes = new XmlEncodingSniffer(car, (String) null);
                Writer out = new OutputStreamWriter(new FileOutputStream(targetFile), xes.getXmlEncoding());
                IOUtil.copyCompletely(car, out);
                car.reset();
                return car;
            } catch (IOException e) {
                System.err.println("IO Error " + e);
                return reader;
            }
        }

        private static InputStream copySchemaSource(String url, InputStream bytes, StscState state) {
            if (state.getSchemasDir() == null) {
                return bytes;
            }
            String schemalocation = state.sourceNameForUri(url);
            File targetFile = new File(state.getSchemasDir(), schemalocation);
            if (targetFile.exists()) {
                return bytes;
            }
            try {
                File parentDir = new File(targetFile.getParent());
                IOUtil.createDir(parentDir, null);
                ByteArrayInputStream bais = copy(bytes);
                FileOutputStream out = new FileOutputStream(targetFile);
                IOUtil.copyCompletely(bais, out);
                bais.reset();
                return bais;
            } catch (IOException e) {
                System.err.println("IO Error " + e);
                return bytes;
            }
        }

        private static void copySchemaSource(String urlLoc, StscState state, boolean forceCopy) throws IOException {
            if (state.getSchemasDir() != null) {
                String schemalocation = state.sourceNameForUri(urlLoc);
                File targetFile = new File(state.getSchemasDir(), schemalocation);
                if (forceCopy || !targetFile.exists()) {
                    try {
                        File parentDir = new File(targetFile.getParent());
                        IOUtil.createDir(parentDir, null);
                        InputStream in = null;
                        URL url = new URL(urlLoc);
                        try {
                            in = url.openStream();
                        } catch (FileNotFoundException fnfe) {
                            if (forceCopy && targetFile.exists()) {
                                targetFile.delete();
                            } else {
                                throw fnfe;
                            }
                        }
                        if (in != null) {
                            FileOutputStream out = new FileOutputStream(targetFile);
                            IOUtil.copyCompletely(in, out);
                        }
                    } catch (IOException e) {
                        System.err.println("IO Error " + e);
                    }
                }
            }
        }

        private static ByteArrayInputStream copy(InputStream is) throws IOException {
            byte[] buf = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (true) {
                int bytesRead = is.read(buf, 0, 1024);
                if (bytesRead > 0) {
                    baos.write(buf, 0, bytesRead);
                } else {
                    return new ByteArrayInputStream(baos.toByteArray());
                }
            }
        }

        private static CharArrayReader copy(Reader is) throws IOException {
            char[] buf = new char[1024];
            CharArrayWriter baos = new CharArrayWriter();
            while (true) {
                int bytesRead = is.read(buf, 0, 1024);
                if (bytesRead > 0) {
                    baos.write(buf, 0, bytesRead);
                } else {
                    return new CharArrayReader(baos.toCharArray());
                }
            }
        }
    }
}
