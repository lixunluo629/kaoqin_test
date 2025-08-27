package org.apache.xmlbeans.impl.schema;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.BindingConfig;
import org.apache.xmlbeans.Filer;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.XmlErrorWatcher;
import org.apache.xmlbeans.impl.schema.StscImporter;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaTypeSystemCompiler.class */
public class SchemaTypeSystemCompiler {

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/schema/SchemaTypeSystemCompiler$Parameters.class */
    public static class Parameters {
        private SchemaTypeSystem existingSystem;
        private String name;
        private SchemaDocument.Schema[] schemas;
        private BindingConfig config;
        private SchemaTypeLoader linkTo;
        private XmlOptions options;
        private Collection errorListener;
        private boolean javaize;
        private URI baseURI;
        private Map sourcesToCopyMap;
        private File schemasDir;

        public SchemaTypeSystem getExistingTypeSystem() {
            return this.existingSystem;
        }

        public void setExistingTypeSystem(SchemaTypeSystem system) {
            this.existingSystem = system;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SchemaDocument.Schema[] getSchemas() {
            return this.schemas;
        }

        public void setSchemas(SchemaDocument.Schema[] schemas) {
            this.schemas = schemas;
        }

        public BindingConfig getConfig() {
            return this.config;
        }

        public void setConfig(BindingConfig config) {
            this.config = config;
        }

        public SchemaTypeLoader getLinkTo() {
            return this.linkTo;
        }

        public void setLinkTo(SchemaTypeLoader linkTo) {
            this.linkTo = linkTo;
        }

        public XmlOptions getOptions() {
            return this.options;
        }

        public void setOptions(XmlOptions options) {
            this.options = options;
        }

        public Collection getErrorListener() {
            return this.errorListener;
        }

        public void setErrorListener(Collection errorListener) {
            this.errorListener = errorListener;
        }

        public boolean isJavaize() {
            return this.javaize;
        }

        public void setJavaize(boolean javaize) {
            this.javaize = javaize;
        }

        public URI getBaseURI() {
            return this.baseURI;
        }

        public void setBaseURI(URI baseURI) {
            this.baseURI = baseURI;
        }

        public Map getSourcesToCopyMap() {
            return this.sourcesToCopyMap;
        }

        public void setSourcesToCopyMap(Map sourcesToCopyMap) {
            this.sourcesToCopyMap = sourcesToCopyMap;
        }

        public File getSchemasDir() {
            return this.schemasDir;
        }

        public void setSchemasDir(File schemasDir) {
            this.schemasDir = schemasDir;
        }
    }

    public static SchemaTypeSystem compile(Parameters params) {
        return compileImpl(params.getExistingTypeSystem(), params.getName(), params.getSchemas(), params.getConfig(), params.getLinkTo(), params.getOptions(), params.getErrorListener(), params.isJavaize(), params.getBaseURI(), params.getSourcesToCopyMap(), params.getSchemasDir());
    }

    public static SchemaTypeSystemImpl compile(String name, SchemaTypeSystem existingSTS, XmlObject[] input, BindingConfig config, SchemaTypeLoader linkTo, Filer filer, XmlOptions options) throws XmlException, IOException {
        XmlOptions options2 = XmlOptions.maskNull(options);
        ArrayList schemas = new ArrayList();
        if (input != null) {
            for (int i = 0; i < input.length; i++) {
                if (input[i] instanceof SchemaDocument.Schema) {
                    schemas.add(input[i]);
                } else if ((input[i] instanceof SchemaDocument) && ((SchemaDocument) input[i]).getSchema() != null) {
                    schemas.add(((SchemaDocument) input[i]).getSchema());
                } else {
                    throw new XmlException("Thread " + Thread.currentThread().getName() + ": The " + i + "th supplied input is not a schema document: its type is " + input[i].schemaType());
                }
            }
        }
        Collection userErrors = (Collection) options2.get(XmlOptions.ERROR_LISTENER);
        XmlErrorWatcher errorWatcher = new XmlErrorWatcher(userErrors);
        SchemaTypeSystemImpl stsi = compileImpl(existingSTS, name, (SchemaDocument.Schema[]) schemas.toArray(new SchemaDocument.Schema[schemas.size()]), config, linkTo, options2, errorWatcher, filer != null, (URI) options2.get(XmlOptions.BASE_URI), null, null);
        if (errorWatcher.hasError() && stsi == null) {
            throw new XmlException(errorWatcher.firstError());
        }
        if (stsi != null && !stsi.isIncomplete() && filer != null) {
            stsi.save(filer);
            generateTypes(stsi, filer, options2);
        }
        return stsi;
    }

    static SchemaTypeSystemImpl compileImpl(SchemaTypeSystem system, String name, SchemaDocument.Schema[] schemas, BindingConfig config, SchemaTypeLoader linkTo, XmlOptions options, Collection outsideErrors, boolean javaize, URI baseURI, Map sourcesToCopyMap, File schemasDir) {
        if (linkTo == null) {
            throw new IllegalArgumentException("Must supply linkTo");
        }
        XmlErrorWatcher errorWatcher = new XmlErrorWatcher(outsideErrors);
        boolean incremental = system != null;
        StscState state = StscState.start();
        boolean validate = options == null || !options.hasOption(XmlOptions.COMPILE_NO_VALIDATION);
        try {
            state.setErrorListener(errorWatcher);
            state.setBindingConfig(config);
            state.setOptions(options);
            state.setGivenTypeSystemName(name);
            state.setSchemasDir(schemasDir);
            if (baseURI != null) {
                state.setBaseUri(baseURI);
            }
            state.setImportingTypeLoader(SchemaTypeLoaderImpl.build(new SchemaTypeLoader[]{BuiltinSchemaTypeSystem.get(), linkTo}, null, null));
            List validSchemas = new ArrayList(schemas.length);
            if (validate) {
                XmlOptions validateOptions = new XmlOptions().setErrorListener(errorWatcher);
                if (options.hasOption(XmlOptions.VALIDATE_TREAT_LAX_AS_SKIP)) {
                    validateOptions.setValidateTreatLaxAsSkip();
                }
                for (int i = 0; i < schemas.length; i++) {
                    if (schemas[i].validate(validateOptions)) {
                        validSchemas.add(schemas[i]);
                    }
                }
            } else {
                validSchemas.addAll(Arrays.asList(schemas));
            }
            SchemaDocument.Schema[] startWith = (SchemaDocument.Schema[]) validSchemas.toArray(new SchemaDocument.Schema[validSchemas.size()]);
            if (incremental) {
                Set namespaces = new HashSet();
                startWith = getSchemasToRecompile((SchemaTypeSystemImpl) system, startWith, namespaces);
                state.initFromTypeSystem((SchemaTypeSystemImpl) system, namespaces);
            } else {
                state.setDependencies(new SchemaDependencies());
            }
            StscImporter.SchemaToProcess[] schemasAndChameleons = StscImporter.resolveImportsAndIncludes(startWith, incremental);
            StscTranslator.addAllDefinitions(schemasAndChameleons);
            StscResolver.resolveAll();
            StscChecker.checkAll();
            StscJavaizer.javaizeAllTypes(javaize);
            StscState.get().sts().loadFromStscState(state);
            if (sourcesToCopyMap != null) {
                sourcesToCopyMap.putAll(state.sourceCopyMap());
            }
            if (errorWatcher.hasError()) {
                if (!state.allowPartial() || state.getRecovered() != errorWatcher.size()) {
                    StscState.end();
                    return null;
                }
                StscState.get().sts().setIncomplete(true);
            }
            if (system != null) {
                ((SchemaTypeSystemImpl) system).setIncomplete(true);
            }
            SchemaTypeSystemImpl schemaTypeSystemImplSts = StscState.get().sts();
            StscState.end();
            return schemaTypeSystemImplSts;
        } catch (Throwable th) {
            StscState.end();
            throw th;
        }
    }

    private static SchemaDocument.Schema[] getSchemasToRecompile(SchemaTypeSystemImpl system, SchemaDocument.Schema[] modified, Set namespaces) {
        Set modifiedFiles = new HashSet();
        Map haveFile = new HashMap();
        List result = new ArrayList();
        for (int i = 0; i < modified.length; i++) {
            String fileURL = modified[i].documentProperties().getSourceName();
            if (fileURL == null) {
                throw new IllegalArgumentException("One of the Schema files passed in doesn't have the source set, which prevents it to be incrementally compiled");
            }
            modifiedFiles.add(fileURL);
            haveFile.put(fileURL, modified[i]);
            result.add(modified[i]);
        }
        SchemaDependencies dep = system.getDependencies();
        List nss = dep.getNamespacesTouched(modifiedFiles);
        namespaces.addAll(dep.computeTransitiveClosure(nss));
        List needRecompilation = dep.getFilesTouched(namespaces);
        StscState.get().setDependencies(new SchemaDependencies(dep, namespaces));
        for (int i2 = 0; i2 < needRecompilation.size(); i2++) {
            String url = (String) needRecompilation.get(i2);
            SchemaDocument.Schema have = (SchemaDocument.Schema) haveFile.get(url);
            if (have == null) {
                try {
                    XmlObject xdoc = StscImporter.DownloadTable.downloadDocument(StscState.get().getS4SLoader(), null, url);
                    XmlOptions voptions = new XmlOptions();
                    voptions.setErrorListener(StscState.get().getErrorListener());
                    if (!(xdoc instanceof SchemaDocument) || !xdoc.validate(voptions)) {
                        StscState.get().error("Referenced document is not a valid schema, URL = " + url, 56, (XmlObject) null);
                    } else {
                        SchemaDocument sDoc = (SchemaDocument) xdoc;
                        result.add(sDoc.getSchema());
                    }
                } catch (MalformedURLException mfe) {
                    StscState.get().error(XmlErrorCodes.EXCEPTION_LOADING_URL, new Object[]{"MalformedURLException", url, mfe.getMessage()}, (XmlObject) null);
                } catch (IOException ioe) {
                    StscState.get().error(XmlErrorCodes.EXCEPTION_LOADING_URL, new Object[]{"IOException", url, ioe.getMessage()}, (XmlObject) null);
                } catch (XmlException xmle) {
                    StscState.get().error(XmlErrorCodes.EXCEPTION_LOADING_URL, new Object[]{"XmlException", url, xmle.getMessage()}, (XmlObject) null);
                }
            }
        }
        return (SchemaDocument.Schema[]) result.toArray(new SchemaDocument.Schema[result.size()]);
    }

    /* JADX WARN: Removed duplicated region for block: B:79:0x011e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0123 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean generateTypes(org.apache.xmlbeans.SchemaTypeSystem r4, org.apache.xmlbeans.Filer r5, org.apache.xmlbeans.XmlOptions r6) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 372
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.xmlbeans.impl.schema.SchemaTypeSystemCompiler.generateTypes(org.apache.xmlbeans.SchemaTypeSystem, org.apache.xmlbeans.Filer, org.apache.xmlbeans.XmlOptions):boolean");
    }
}
