package org.apache.xmlbeans.impl.tool;

import ch.qos.logback.core.rolling.helper.DateTokenConverter;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.naming.ServiceRef;
import org.apache.xmlbeans.ResourceLoader;
import org.apache.xmlbeans.SchemaCodePrinter;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SystemProperties;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlErrorCodes;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.apache.xmlbeans.impl.common.JarHelper;
import org.apache.xmlbeans.impl.common.ResolverUtil;
import org.apache.xmlbeans.impl.common.XmlErrorPrinter;
import org.apache.xmlbeans.impl.common.XmlErrorWatcher;
import org.apache.xmlbeans.impl.config.BindingConfigImpl;
import org.apache.xmlbeans.impl.repackage.Repackager;
import org.apache.xmlbeans.impl.schema.PathResourceLoader;
import org.apache.xmlbeans.impl.schema.SchemaTypeLoaderImpl;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemCompiler;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl;
import org.apache.xmlbeans.impl.schema.StscState;
import org.apache.xmlbeans.impl.tool.Extension;
import org.apache.xmlbeans.impl.util.FilerImpl;
import org.apache.xmlbeans.impl.values.XmlListImpl;
import org.apache.xmlbeans.impl.xb.substwsdl.DefinitionsDocument;
import org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.springframework.beans.factory.xml.DelegatingEntityResolver;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.xml.sax.EntityResolver;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/SchemaCompiler.class */
public class SchemaCompiler {
    private static final String CONFIG_URI = "http://xml.apache.org/xmlbeans/2004/02/xbean/config";
    private static final String COMPATIBILITY_CONFIG_URI = "http://www.bea.com/2002/09/xbean/config";
    private static final Map MAP_COMPATIBILITY_CONFIG_URIS = new HashMap();

    public static void printUsage() {
        System.out.println("Compiles a schema into XML Bean classes and metadata.");
        System.out.println("Usage: scomp [opts] [dirs]* [schema.xsd]* [service.wsdl]* [config.xsdconfig]*");
        System.out.println("Options include:");
        System.out.println("    -cp [a;b;c] - classpath");
        System.out.println("    -d [dir] - target binary directory for .class and .xsb files");
        System.out.println("    -src [dir] - target directory for generated .java files");
        System.out.println("    -srconly - do not compile .java files or jar the output.");
        System.out.println("    -out [xmltypes.jar] - the name of the output jar");
        System.out.println("    -dl - permit network downloads for imports and includes (default is off)");
        System.out.println("    -noupa - do not enforce the unique particle attribution rule");
        System.out.println("    -nopvr - do not enforce the particle valid (restriction) rule");
        System.out.println("    -noann - ignore annotations");
        System.out.println("    -novdoc - do not validate contents of <documentation>");
        System.out.println("    -noext - ignore all extension (Pre/Post and Interface) found in .xsdconfig files");
        System.out.println("    -compiler - path to external java compiler");
        System.out.println("    -javasource [version] - generate java source compatible for a Java version (1.4 or 1.5)");
        System.out.println("    -ms - initial memory for external java compiler (default '" + CodeGenUtil.DEFAULT_MEM_START + "')");
        System.out.println("    -mx - maximum memory for external java compiler (default '" + CodeGenUtil.DEFAULT_MEM_MAX + "')");
        System.out.println("    -debug - compile with debug symbols");
        System.out.println("    -quiet - print fewer informational messages");
        System.out.println("    -verbose - print more informational messages");
        System.out.println("    -version - prints version information");
        System.out.println("    -license - prints license information");
        System.out.println("    -allowmdef \"[ns] [ns] [ns]\" - ignores multiple defs in given namespaces (use ##local for no-namespace)");
        System.out.println("    -catalog [file] -  catalog file for org.apache.xml.resolver.tools.CatalogResolver. (Note: needs resolver.jar from http://xml.apache.org/commons/components/resolver/index.html)");
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        File[] classpath;
        if (args.length == 0) {
            printUsage();
            System.exit(0);
            return;
        }
        Set flags = new HashSet();
        flags.add("h");
        flags.add("help");
        flags.add("usage");
        flags.add("license");
        flags.add("quiet");
        flags.add("verbose");
        flags.add("version");
        flags.add("dl");
        flags.add("noupa");
        flags.add("nopvr");
        flags.add("noann");
        flags.add("novdoc");
        flags.add("noext");
        flags.add("srconly");
        flags.add("debug");
        Set opts = new HashSet();
        opts.add("out");
        opts.add("name");
        opts.add("src");
        opts.add(DateTokenConverter.CONVERTER_KEY);
        opts.add("cp");
        opts.add("compiler");
        opts.add("javasource");
        opts.add("jar");
        opts.add("ms");
        opts.add("mx");
        opts.add("repackage");
        opts.add("schemaCodePrinter");
        opts.add("extension");
        opts.add("extensionParms");
        opts.add("allowmdef");
        opts.add("catalog");
        CommandLine cl = new CommandLine(args, flags, opts);
        if (cl.getOpt("h") != null || cl.getOpt("help") != null || cl.getOpt("usage") != null) {
            printUsage();
            System.exit(0);
            return;
        }
        String[] badopts = cl.getBadOpts();
        if (badopts.length > 0) {
            for (String str : badopts) {
                System.out.println("Unrecognized option: " + str);
            }
            printUsage();
            System.exit(0);
            return;
        }
        if (cl.getOpt("license") != null) {
            CommandLine.printLicense();
            System.exit(0);
            return;
        }
        if (cl.getOpt("version") != null) {
            CommandLine.printVersion();
            System.exit(0);
            return;
        }
        cl.args();
        boolean verbose = cl.getOpt("verbose") != null;
        boolean quiet = cl.getOpt("quiet") != null;
        if (verbose) {
            quiet = false;
        }
        if (verbose) {
            CommandLine.printVersion();
        }
        String outputfilename = cl.getOpt("out");
        String repackage = cl.getOpt("repackage");
        String codePrinterClass = cl.getOpt("schemaCodePrinter");
        SchemaCodePrinter codePrinter = null;
        if (codePrinterClass != null) {
            try {
                codePrinter = (SchemaCodePrinter) Class.forName(codePrinterClass).newInstance();
            } catch (Exception e) {
                System.err.println("Failed to load SchemaCodePrinter class " + codePrinterClass + "; proceeding with default printer");
            }
        }
        String name = cl.getOpt("name");
        boolean download = cl.getOpt("dl") != null;
        boolean noUpa = cl.getOpt("noupa") != null;
        boolean noPvr = cl.getOpt("nopvr") != null;
        boolean noAnn = cl.getOpt("noann") != null;
        boolean noVDoc = cl.getOpt("novdoc") != null;
        boolean noExt = cl.getOpt("noext") != null;
        boolean nojavac = cl.getOpt("srconly") != null;
        boolean debug = cl.getOpt("debug") != null;
        String allowmdef = cl.getOpt("allowmdef");
        Set mdefNamespaces = allowmdef == null ? Collections.EMPTY_SET : new HashSet(Arrays.asList(XmlListImpl.split_list(allowmdef)));
        List extensions = new ArrayList();
        if (cl.getOpt("extension") != null) {
            try {
                Extension e2 = new Extension();
                e2.setClassName(Class.forName(cl.getOpt("extension"), false, Thread.currentThread().getContextClassLoader()));
                extensions.add(e2);
            } catch (ClassNotFoundException e3) {
                System.err.println("Could not find extension class: " + cl.getOpt("extension") + "  Is it on your classpath?");
                System.exit(1);
            }
        }
        if (extensions.size() > 0 && cl.getOpt("extensionParms") != null) {
            Extension e4 = (Extension) extensions.get(0);
            StringTokenizer parmTokens = new StringTokenizer(cl.getOpt("extensionParms"), ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
            while (parmTokens.hasMoreTokens()) {
                String nvPair = parmTokens.nextToken();
                int index = nvPair.indexOf(61);
                if (index < 0) {
                    System.err.println("extensionParms should be name=value;name=value");
                    System.exit(1);
                }
                String n = nvPair.substring(0, index);
                String v = nvPair.substring(index + 1);
                Extension.Param param = e4.createParam();
                param.setName(n);
                param.setValue(v);
            }
        }
        String classesdir = cl.getOpt(DateTokenConverter.CONVERTER_KEY);
        File classes = null;
        if (classesdir != null) {
            classes = new File(classesdir);
        }
        String srcdir = cl.getOpt("src");
        File src = null;
        if (srcdir != null) {
            src = new File(srcdir);
        }
        if (nojavac && srcdir == null && classes != null) {
            src = classes;
        }
        File tempdir = null;
        if (src == null || classes == null) {
            try {
                tempdir = SchemaCodeGenerator.createTempDir();
            } catch (IOException e5) {
                System.err.println("Error creating temp dir " + e5);
                System.exit(1);
            }
        }
        File jarfile = null;
        if (outputfilename == null && classes == null && !nojavac) {
            outputfilename = "xmltypes.jar";
        }
        if (outputfilename != null) {
            jarfile = new File(outputfilename);
        }
        if (src == null) {
            src = IOUtil.createDir(tempdir, "src");
        }
        if (classes == null) {
            classes = IOUtil.createDir(tempdir, "classes");
        }
        String cpString = cl.getOpt("cp");
        if (cpString != null) {
            String[] cpparts = cpString.split(File.pathSeparator);
            List cpList = new ArrayList();
            for (String str2 : cpparts) {
                cpList.add(new File(str2));
            }
            classpath = (File[]) cpList.toArray(new File[cpList.size()]);
        } else {
            classpath = CodeGenUtil.systemClasspath();
        }
        String javasource = cl.getOpt("javasource");
        String compiler = cl.getOpt("compiler");
        String jar = cl.getOpt("jar");
        if (verbose && jar != null) {
            System.out.println("The 'jar' option is no longer supported.");
        }
        String memoryInitialSize = cl.getOpt("ms");
        String memoryMaximumSize = cl.getOpt("mx");
        File[] xsdFiles = cl.filesEndingWith(DelegatingEntityResolver.XSD_SUFFIX);
        File[] wsdlFiles = cl.filesEndingWith(".wsdl");
        File[] javaFiles = cl.filesEndingWith(".java");
        File[] configFiles = cl.filesEndingWith(".xsdconfig");
        URL[] urlFiles = cl.getURLs();
        if (xsdFiles.length + wsdlFiles.length + urlFiles.length == 0) {
            System.out.println("Could not find any xsd or wsdl files to process.");
            System.exit(0);
        }
        File baseDir = cl.getBaseDir();
        URI baseURI = baseDir == null ? null : baseDir.toURI();
        XmlErrorPrinter err = new XmlErrorPrinter(verbose, baseURI);
        String catString = cl.getOpt("catalog");
        Parameters params = new Parameters();
        params.setBaseDir(baseDir);
        params.setXsdFiles(xsdFiles);
        params.setWsdlFiles(wsdlFiles);
        params.setJavaFiles(javaFiles);
        params.setConfigFiles(configFiles);
        params.setUrlFiles(urlFiles);
        params.setClasspath(classpath);
        params.setOutputJar(jarfile);
        params.setName(name);
        params.setSrcDir(src);
        params.setClassesDir(classes);
        params.setCompiler(compiler);
        params.setJavaSource(javasource);
        params.setMemoryInitialSize(memoryInitialSize);
        params.setMemoryMaximumSize(memoryMaximumSize);
        params.setNojavac(nojavac);
        params.setQuiet(quiet);
        params.setVerbose(verbose);
        params.setDownload(download);
        params.setNoUpa(noUpa);
        params.setNoPvr(noPvr);
        params.setNoAnn(noAnn);
        params.setNoVDoc(noVDoc);
        params.setNoExt(noExt);
        params.setDebug(debug);
        params.setErrorListener(err);
        params.setRepackage(repackage);
        params.setExtensions(extensions);
        params.setMdefNamespaces(mdefNamespaces);
        params.setCatalogFile(catString);
        params.setSchemaCodePrinter(codePrinter);
        boolean result = compile(params);
        if (tempdir != null) {
            SchemaCodeGenerator.tryHardToDelete(tempdir);
        }
        if (!result) {
            System.exit(1);
        }
        System.exit(0);
    }

    /* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/tool/SchemaCompiler$Parameters.class */
    public static class Parameters {
        private File baseDir;
        private File[] xsdFiles;
        private File[] wsdlFiles;
        private File[] javaFiles;
        private File[] configFiles;
        private URL[] urlFiles;
        private File[] classpath;
        private File outputJar;
        private String name;
        private File srcDir;
        private File classesDir;
        private String memoryInitialSize;
        private String memoryMaximumSize;
        private String compiler;
        private String javasource;
        private boolean nojavac;
        private boolean quiet;
        private boolean verbose;
        private boolean download;
        private Collection errorListener;
        private boolean noUpa;
        private boolean noPvr;
        private boolean noAnn;
        private boolean noVDoc;
        private boolean noExt;
        private boolean debug;
        private boolean incrementalSrcGen;
        private String repackage;
        private List extensions = Collections.EMPTY_LIST;
        private Set mdefNamespaces = Collections.EMPTY_SET;
        private String catalogFile;
        private SchemaCodePrinter schemaCodePrinter;
        private EntityResolver entityResolver;

        public File getBaseDir() {
            return this.baseDir;
        }

        public void setBaseDir(File baseDir) {
            this.baseDir = baseDir;
        }

        public File[] getXsdFiles() {
            return this.xsdFiles;
        }

        public void setXsdFiles(File[] xsdFiles) {
            this.xsdFiles = xsdFiles;
        }

        public File[] getWsdlFiles() {
            return this.wsdlFiles;
        }

        public void setWsdlFiles(File[] wsdlFiles) {
            this.wsdlFiles = wsdlFiles;
        }

        public File[] getJavaFiles() {
            return this.javaFiles;
        }

        public void setJavaFiles(File[] javaFiles) {
            this.javaFiles = javaFiles;
        }

        public File[] getConfigFiles() {
            return this.configFiles;
        }

        public void setConfigFiles(File[] configFiles) {
            this.configFiles = configFiles;
        }

        public URL[] getUrlFiles() {
            return this.urlFiles;
        }

        public void setUrlFiles(URL[] urlFiles) {
            this.urlFiles = urlFiles;
        }

        public File[] getClasspath() {
            return this.classpath;
        }

        public void setClasspath(File[] classpath) {
            this.classpath = classpath;
        }

        public File getOutputJar() {
            return this.outputJar;
        }

        public void setOutputJar(File outputJar) {
            this.outputJar = outputJar;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public File getSrcDir() {
            return this.srcDir;
        }

        public void setSrcDir(File srcDir) {
            this.srcDir = srcDir;
        }

        public File getClassesDir() {
            return this.classesDir;
        }

        public void setClassesDir(File classesDir) {
            this.classesDir = classesDir;
        }

        public boolean isNojavac() {
            return this.nojavac;
        }

        public void setNojavac(boolean nojavac) {
            this.nojavac = nojavac;
        }

        public boolean isQuiet() {
            return this.quiet;
        }

        public void setQuiet(boolean quiet) {
            this.quiet = quiet;
        }

        public boolean isVerbose() {
            return this.verbose;
        }

        public void setVerbose(boolean verbose) {
            this.verbose = verbose;
        }

        public boolean isDownload() {
            return this.download;
        }

        public void setDownload(boolean download) {
            this.download = download;
        }

        public boolean isNoUpa() {
            return this.noUpa;
        }

        public void setNoUpa(boolean noUpa) {
            this.noUpa = noUpa;
        }

        public boolean isNoPvr() {
            return this.noPvr;
        }

        public void setNoPvr(boolean noPvr) {
            this.noPvr = noPvr;
        }

        public boolean isNoAnn() {
            return this.noAnn;
        }

        public void setNoAnn(boolean noAnn) {
            this.noAnn = noAnn;
        }

        public boolean isNoVDoc() {
            return this.noVDoc;
        }

        public void setNoVDoc(boolean newNoVDoc) {
            this.noVDoc = newNoVDoc;
        }

        public boolean isNoExt() {
            return this.noExt;
        }

        public void setNoExt(boolean newNoExt) {
            this.noExt = newNoExt;
        }

        public boolean isIncrementalSrcGen() {
            return this.incrementalSrcGen;
        }

        public void setIncrementalSrcGen(boolean incrSrcGen) {
            this.incrementalSrcGen = incrSrcGen;
        }

        public boolean isDebug() {
            return this.debug;
        }

        public void setDebug(boolean debug) {
            this.debug = debug;
        }

        public String getMemoryInitialSize() {
            return this.memoryInitialSize;
        }

        public void setMemoryInitialSize(String memoryInitialSize) {
            this.memoryInitialSize = memoryInitialSize;
        }

        public String getMemoryMaximumSize() {
            return this.memoryMaximumSize;
        }

        public void setMemoryMaximumSize(String memoryMaximumSize) {
            this.memoryMaximumSize = memoryMaximumSize;
        }

        public String getCompiler() {
            return this.compiler;
        }

        public void setCompiler(String compiler) {
            this.compiler = compiler;
        }

        public String getJavaSource() {
            return this.javasource;
        }

        public void setJavaSource(String javasource) {
            this.javasource = javasource;
        }

        public String getJar() {
            return null;
        }

        public void setJar(String jar) {
        }

        public Collection getErrorListener() {
            return this.errorListener;
        }

        public void setErrorListener(Collection errorListener) {
            this.errorListener = errorListener;
        }

        public String getRepackage() {
            return this.repackage;
        }

        public void setRepackage(String newRepackage) {
            this.repackage = newRepackage;
        }

        public List getExtensions() {
            return this.extensions;
        }

        public void setExtensions(List extensions) {
            this.extensions = extensions;
        }

        public Set getMdefNamespaces() {
            return this.mdefNamespaces;
        }

        public void setMdefNamespaces(Set mdefNamespaces) {
            this.mdefNamespaces = mdefNamespaces;
        }

        public String getCatalogFile() {
            return this.catalogFile;
        }

        public void setCatalogFile(String catalogPropFile) {
            this.catalogFile = catalogPropFile;
        }

        public SchemaCodePrinter getSchemaCodePrinter() {
            return this.schemaCodePrinter;
        }

        public void setSchemaCodePrinter(SchemaCodePrinter schemaCodePrinter) {
            this.schemaCodePrinter = schemaCodePrinter;
        }

        public EntityResolver getEntityResolver() {
            return this.entityResolver;
        }

        public void setEntityResolver(EntityResolver entityResolver) {
            this.entityResolver = entityResolver;
        }
    }

    private static SchemaTypeSystem loadTypeSystem(String name, File[] xsdFiles, File[] wsdlFiles, URL[] urlFiles, File[] configFiles, File[] javaFiles, ResourceLoader cpResourceLoader, boolean download, boolean noUpa, boolean noPvr, boolean noAnn, boolean noVDoc, boolean noExt, Set mdefNamespaces, File baseDir, Map sourcesToCopyMap, Collection outerErrorListener, File schemasDir, EntityResolver entResolver, File[] classpath, String javasource) {
        XmlErrorWatcher errorListener = new XmlErrorWatcher(outerErrorListener);
        StscState state = StscState.start();
        try {
            state.setErrorListener(errorListener);
            SchemaTypeLoader loader = XmlBeans.typeLoaderForClassLoader(SchemaDocument.class.getClassLoader());
            ArrayList scontentlist = new ArrayList();
            if (xsdFiles != null) {
                for (int i = 0; i < xsdFiles.length; i++) {
                    try {
                        XmlOptions options = new XmlOptions();
                        options.setLoadLineNumbers();
                        options.setLoadMessageDigest();
                        options.setEntityResolver(entResolver);
                        XmlObject schemadoc = loader.parse(xsdFiles[i], (SchemaType) null, options);
                        if (!(schemadoc instanceof SchemaDocument)) {
                            StscState.addError(errorListener, XmlErrorCodes.INVALID_DOCUMENT_TYPE, new Object[]{xsdFiles[i], "schema"}, schemadoc);
                        } else {
                            addSchema(xsdFiles[i].toString(), (SchemaDocument) schemadoc, errorListener, noVDoc, scontentlist);
                        }
                    } catch (XmlException e) {
                        errorListener.add(e.getError());
                    } catch (Exception e2) {
                        StscState.addError(errorListener, XmlErrorCodes.CANNOT_LOAD_FILE, new Object[]{"xsd", xsdFiles[i], e2.getMessage()}, xsdFiles[i]);
                    }
                }
            }
            if (wsdlFiles != null) {
                for (int i2 = 0; i2 < wsdlFiles.length; i2++) {
                    try {
                        try {
                            XmlOptions options2 = new XmlOptions();
                            options2.setLoadLineNumbers();
                            options2.setLoadSubstituteNamespaces(Collections.singletonMap("http://schemas.xmlsoap.org/wsdl/", "http://www.apache.org/internal/xmlbeans/wsdlsubst"));
                            options2.setEntityResolver(entResolver);
                            XmlObject wsdldoc = loader.parse(wsdlFiles[i2], (SchemaType) null, options2);
                            if (!(wsdldoc instanceof DefinitionsDocument)) {
                                StscState.addError(errorListener, XmlErrorCodes.INVALID_DOCUMENT_TYPE, new Object[]{wsdlFiles[i2], ServiceRef.WSDL}, wsdldoc);
                            } else {
                                addWsdlSchemas(wsdlFiles[i2].toString(), (DefinitionsDocument) wsdldoc, errorListener, noVDoc, scontentlist);
                            }
                        } catch (XmlException e3) {
                            errorListener.add(e3.getError());
                        }
                    } catch (Exception e4) {
                        StscState.addError(errorListener, XmlErrorCodes.CANNOT_LOAD_FILE, new Object[]{ServiceRef.WSDL, wsdlFiles[i2], e4.getMessage()}, wsdlFiles[i2]);
                    }
                }
            }
            if (urlFiles != null) {
                for (int i3 = 0; i3 < urlFiles.length; i3++) {
                    try {
                        XmlOptions options3 = new XmlOptions();
                        options3.setLoadLineNumbers();
                        options3.setLoadSubstituteNamespaces(Collections.singletonMap("http://schemas.xmlsoap.org/wsdl/", "http://www.apache.org/internal/xmlbeans/wsdlsubst"));
                        options3.setEntityResolver(entResolver);
                        XmlObject urldoc = loader.parse(urlFiles[i3], (SchemaType) null, options3);
                        if (urldoc instanceof DefinitionsDocument) {
                            addWsdlSchemas(urlFiles[i3].toString(), (DefinitionsDocument) urldoc, errorListener, noVDoc, scontentlist);
                        } else if (urldoc instanceof SchemaDocument) {
                            addSchema(urlFiles[i3].toString(), (SchemaDocument) urldoc, errorListener, noVDoc, scontentlist);
                        } else {
                            StscState.addError(errorListener, XmlErrorCodes.INVALID_DOCUMENT_TYPE, new Object[]{urlFiles[i3], "wsdl or schema"}, urldoc);
                        }
                    } catch (XmlException e5) {
                        errorListener.add(e5.getError());
                    } catch (Exception e6) {
                        StscState.addError(errorListener, XmlErrorCodes.CANNOT_LOAD_FILE, new Object[]{RtspHeaders.Values.URL, urlFiles[i3], e6.getMessage()}, urlFiles[i3]);
                    }
                }
            }
            SchemaDocument.Schema[] sdocs = (SchemaDocument.Schema[]) scontentlist.toArray(new SchemaDocument.Schema[scontentlist.size()]);
            ArrayList cdoclist = new ArrayList();
            if (configFiles != null) {
                if (noExt) {
                    System.out.println("Pre/Post and Interface extensions will be ignored.");
                }
                for (int i4 = 0; i4 < configFiles.length; i4++) {
                    try {
                        try {
                            XmlOptions options4 = new XmlOptions();
                            options4.put(XmlOptions.LOAD_LINE_NUMBERS);
                            options4.setEntityResolver(entResolver);
                            options4.setLoadSubstituteNamespaces(MAP_COMPATIBILITY_CONFIG_URIS);
                            XmlObject configdoc = loader.parse(configFiles[i4], (SchemaType) null, options4);
                            if (!(configdoc instanceof ConfigDocument)) {
                                StscState.addError(errorListener, XmlErrorCodes.INVALID_DOCUMENT_TYPE, new Object[]{configFiles[i4], "xsd config"}, configdoc);
                            } else {
                                StscState.addInfo(errorListener, "Loading config file " + configFiles[i4]);
                                if (configdoc.validate(new XmlOptions().setErrorListener(errorListener))) {
                                    ConfigDocument.Config config = ((ConfigDocument) configdoc).getConfig();
                                    cdoclist.add(config);
                                    if (noExt) {
                                        config.setExtensionArray(new Extensionconfig[0]);
                                    }
                                }
                            }
                        } catch (XmlException e7) {
                            errorListener.add(e7.getError());
                        }
                    } catch (Exception e8) {
                        StscState.addError(errorListener, XmlErrorCodes.CANNOT_LOAD_FILE, new Object[]{"xsd config", configFiles[i4], e8.getMessage()}, configFiles[i4]);
                    }
                }
            }
            ConfigDocument.Config[] cdocs = (ConfigDocument.Config[]) cdoclist.toArray(new ConfigDocument.Config[cdoclist.size()]);
            SchemaTypeLoader linkTo = SchemaTypeLoaderImpl.build(null, cpResourceLoader, null);
            URI baseURI = null;
            if (baseDir != null) {
                baseURI = baseDir.toURI();
            }
            XmlOptions opts = new XmlOptions();
            if (download) {
                opts.setCompileDownloadUrls();
            }
            if (noUpa) {
                opts.setCompileNoUpaRule();
            }
            if (noPvr) {
                opts.setCompileNoPvrRule();
            }
            if (noAnn) {
                opts.setCompileNoAnnotations();
            }
            if (mdefNamespaces != null) {
                opts.setCompileMdefNamespaces(mdefNamespaces);
            }
            opts.setCompileNoValidation();
            opts.setEntityResolver(entResolver);
            if (javasource != null) {
                opts.setGenerateJavaVersion(javasource);
            }
            SchemaTypeSystemCompiler.Parameters params = new SchemaTypeSystemCompiler.Parameters();
            params.setName(name);
            params.setSchemas(sdocs);
            params.setConfig(BindingConfigImpl.forConfigDocuments(cdocs, javaFiles, classpath));
            params.setLinkTo(linkTo);
            params.setOptions(opts);
            params.setErrorListener(errorListener);
            params.setJavaize(true);
            params.setBaseURI(baseURI);
            params.setSourcesToCopyMap(sourcesToCopyMap);
            params.setSchemasDir(schemasDir);
            SchemaTypeSystem schemaTypeSystemCompile = SchemaTypeSystemCompiler.compile(params);
            StscState.end();
            return schemaTypeSystemCompile;
        } catch (Throwable th) {
            StscState.end();
            throw th;
        }
    }

    private static void addSchema(String name, SchemaDocument schemadoc, XmlErrorWatcher errorListener, boolean noVDoc, List scontentlist) {
        StscState.addInfo(errorListener, "Loading schema file " + name);
        XmlOptions opts = new XmlOptions().setErrorListener(errorListener);
        if (noVDoc) {
            opts.setValidateTreatLaxAsSkip();
        }
        if (schemadoc.validate(opts)) {
            scontentlist.add(schemadoc.getSchema());
        }
    }

    private static void addWsdlSchemas(String name, DefinitionsDocument wsdldoc, XmlErrorWatcher errorListener, boolean noVDoc, List scontentlist) {
        if (wsdlContainsEncoded(wsdldoc)) {
            StscState.addWarning(errorListener, "The WSDL " + name + " uses SOAP encoding. SOAP encoding is not compatible with literal XML Schema.", 60, wsdldoc);
        }
        StscState.addInfo(errorListener, "Loading wsdl file " + name);
        XmlOptions opts = new XmlOptions().setErrorListener(errorListener);
        if (noVDoc) {
            opts.setValidateTreatLaxAsSkip();
        }
        XmlObject[] types = wsdldoc.getDefinitions().getTypesArray();
        int count = 0;
        for (XmlObject xmlObject : types) {
            XmlObject[] schemas = xmlObject.selectPath("declare namespace xs=\"http://www.w3.org/2001/XMLSchema\" xs:schema");
            if (schemas.length == 0) {
                StscState.addWarning(errorListener, "The WSDL " + name + " did not have any schema documents in namespace 'http://www.w3.org/2001/XMLSchema'", 60, wsdldoc);
            } else {
                for (int k = 0; k < schemas.length; k++) {
                    if ((schemas[k] instanceof SchemaDocument.Schema) && schemas[k].validate(opts)) {
                        count++;
                        scontentlist.add(schemas[k]);
                    }
                }
            }
        }
        StscState.addInfo(errorListener, "Processing " + count + " schema(s) in " + name);
    }

    public static boolean compile(Parameters params) throws IOException {
        File baseDir = params.getBaseDir();
        File[] xsdFiles = params.getXsdFiles();
        File[] wsdlFiles = params.getWsdlFiles();
        URL[] urlFiles = params.getUrlFiles();
        File[] javaFiles = params.getJavaFiles();
        File[] configFiles = params.getConfigFiles();
        File[] classpath = params.getClasspath();
        File outputJar = params.getOutputJar();
        String name = params.getName();
        File srcDir = params.getSrcDir();
        File classesDir = params.getClassesDir();
        String compiler = params.getCompiler();
        String javasource = params.getJavaSource();
        String memoryInitialSize = params.getMemoryInitialSize();
        String memoryMaximumSize = params.getMemoryMaximumSize();
        boolean nojavac = params.isNojavac();
        boolean debug = params.isDebug();
        boolean verbose = params.isVerbose();
        boolean quiet = params.isQuiet();
        boolean download = params.isDownload();
        boolean noUpa = params.isNoUpa();
        boolean noPvr = params.isNoPvr();
        boolean noAnn = params.isNoAnn();
        boolean noVDoc = params.isNoVDoc();
        boolean noExt = params.isNoExt();
        boolean incrSrcGen = params.isIncrementalSrcGen();
        Collection outerErrorListener = params.getErrorListener();
        String repackage = params.getRepackage();
        if (repackage != null) {
            SchemaTypeLoaderImpl.METADATA_PACKAGE_LOAD = SchemaTypeSystemImpl.METADATA_PACKAGE_GEN;
            String stsPackage = SchemaTypeSystem.class.getPackage().getName();
            Repackager repackager = new Repackager(repackage);
            SchemaTypeSystemImpl.METADATA_PACKAGE_GEN = repackager.repackage(new StringBuffer(stsPackage)).toString().replace('.', '_');
            System.out.println("\n\n\n" + stsPackage + ".SchemaCompiler  Metadata LOAD:" + SchemaTypeLoaderImpl.METADATA_PACKAGE_LOAD + " GEN:" + SchemaTypeSystemImpl.METADATA_PACKAGE_GEN);
        }
        SchemaCodePrinter codePrinter = params.getSchemaCodePrinter();
        List extensions = params.getExtensions();
        Set mdefNamespaces = params.getMdefNamespaces();
        EntityResolver cmdLineEntRes = params.getEntityResolver() == null ? ResolverUtil.resolverForCatalog(params.getCatalogFile()) : params.getEntityResolver();
        if (srcDir == null || classesDir == null) {
            throw new IllegalArgumentException("src and class gen directories may not be null.");
        }
        long start = System.currentTimeMillis();
        if (baseDir == null) {
            baseDir = new File(SystemProperties.getProperty("user.dir"));
        }
        ResourceLoader cpResourceLoader = null;
        Map sourcesToCopyMap = new HashMap();
        if (classpath != null) {
            cpResourceLoader = new PathResourceLoader(classpath);
        }
        boolean result = true;
        File schemasDir = IOUtil.createDir(classesDir, "schema" + SchemaTypeSystemImpl.METADATA_PACKAGE_GEN + "/src");
        XmlErrorWatcher errorListener = new XmlErrorWatcher(outerErrorListener);
        SchemaTypeSystem system = loadTypeSystem(name, xsdFiles, wsdlFiles, urlFiles, configFiles, javaFiles, cpResourceLoader, download, noUpa, noPvr, noAnn, noVDoc, noExt, mdefNamespaces, baseDir, sourcesToCopyMap, errorListener, schemasDir, cmdLineEntRes, classpath, javasource);
        if (errorListener.hasError()) {
            result = false;
        }
        long finish = System.currentTimeMillis();
        if (!quiet) {
            System.out.println("Time to build schema type system: " + ((finish - start) / 1000.0d) + " seconds");
        }
        if (result && system != null) {
            long start2 = System.currentTimeMillis();
            Repackager repackager2 = repackage == null ? null : new Repackager(repackage);
            FilerImpl filer = new FilerImpl(classesDir, srcDir, repackager2, verbose, incrSrcGen);
            XmlOptions options = new XmlOptions();
            if (codePrinter != null) {
                options.setSchemaCodePrinter(codePrinter);
            }
            if (javasource != null) {
                options.setGenerateJavaVersion(javasource);
            }
            system.save(filer);
            result &= SchemaTypeSystemCompiler.generateTypes(system, filer, options);
            if (incrSrcGen) {
                SchemaCodeGenerator.deleteObsoleteFiles(srcDir, srcDir, new HashSet(filer.getSourceFiles()));
            }
            if (result) {
                long finish2 = System.currentTimeMillis();
                if (!quiet) {
                    System.out.println("Time to generate code: " + ((finish2 - start2) / 1000.0d) + " seconds");
                }
            }
            if (result && !nojavac) {
                long start3 = System.currentTimeMillis();
                List sourcefiles = filer.getSourceFiles();
                if (javaFiles != null) {
                    sourcefiles.addAll(Arrays.asList(javaFiles));
                }
                if (!CodeGenUtil.externalCompile(sourcefiles, classesDir, classpath, debug, compiler, javasource, memoryInitialSize, memoryMaximumSize, quiet, verbose)) {
                    result = false;
                }
                long finish3 = System.currentTimeMillis();
                if (result && !params.isQuiet()) {
                    System.out.println("Time to compile code: " + ((finish3 - start3) / 1000.0d) + " seconds");
                }
                if (result && outputJar != null) {
                    try {
                        new JarHelper().jarDir(classesDir, outputJar);
                    } catch (IOException e) {
                        System.err.println("IO Error " + e);
                        result = false;
                    }
                    if (result && !params.isQuiet()) {
                        System.out.println("Compiled types to: " + outputJar);
                    }
                }
            }
        }
        if (!result && !quiet) {
            System.out.println("BUILD FAILED");
        } else {
            runExtensions(extensions, system, classesDir);
        }
        if (cpResourceLoader != null) {
            cpResourceLoader.close();
        }
        return result;
    }

    private static void runExtensions(List extensions, SchemaTypeSystem system, File classesDir) throws IOException {
        String classesDirName;
        if (extensions != null && extensions.size() > 0) {
            Iterator i = extensions.iterator();
            try {
                classesDirName = classesDir.getCanonicalPath();
            } catch (IOException e) {
                System.out.println("WARNING: Unable to get the path for schema jar file");
                classesDirName = classesDir.getAbsolutePath();
            }
            while (i.hasNext()) {
                Extension extension = (Extension) i.next();
                try {
                    SchemaCompilerExtension sce = (SchemaCompilerExtension) extension.getClassName().newInstance();
                    System.out.println("Running Extension: " + sce.getExtensionName());
                    Map extensionParms = new HashMap();
                    for (Extension.Param p : extension.getParams()) {
                        extensionParms.put(p.getName(), p.getValue());
                    }
                    extensionParms.put("classesDir", classesDirName);
                    sce.schemaCompilerExtension(system, extensionParms);
                } catch (IllegalAccessException e2) {
                    System.out.println("ILLEGAL ACCESS Exception when attempting to instantiate schema compiler extension: " + extension.getClassName().getName());
                    System.out.println("EXTENSION Class was not run");
                    return;
                } catch (InstantiationException e3) {
                    System.out.println("UNABLE to instantiate schema compiler extension:" + extension.getClassName().getName());
                    System.out.println("EXTENSION Class was not run");
                    return;
                }
            }
        }
    }

    private static boolean wsdlContainsEncoded(XmlObject wsdldoc) {
        XmlObject[] useAttrs = wsdldoc.selectPath("declare namespace soap='http://schemas.xmlsoap.org/wsdl/soap/' .//soap:body/@use|.//soap:header/@use|.//soap:fault/@use");
        for (XmlObject xmlObject : useAttrs) {
            if ("encoded".equals(((SimpleValue) xmlObject).getStringValue())) {
                return true;
            }
        }
        return false;
    }

    static {
        MAP_COMPATIBILITY_CONFIG_URIS.put(COMPATIBILITY_CONFIG_URI, CONFIG_URI);
    }
}
