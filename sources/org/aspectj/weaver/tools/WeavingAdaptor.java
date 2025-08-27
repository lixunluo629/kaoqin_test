package org.aspectj.weaver.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageContext;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.IMessageHolder;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.Message;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.bridge.MessageWriter;
import org.aspectj.bridge.WeaveMessage;
import org.aspectj.util.FileUtil;
import org.aspectj.util.LangUtil;
import org.aspectj.weaver.IClassFileProvider;
import org.aspectj.weaver.ICrossReferenceHandler;
import org.aspectj.weaver.IUnwovenClassFile;
import org.aspectj.weaver.IWeaveRequestor;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.BcelObjectType;
import org.aspectj.weaver.bcel.BcelWeaver;
import org.aspectj.weaver.bcel.BcelWorld;
import org.aspectj.weaver.bcel.UnwovenClassFile;
import org.aspectj.weaver.tools.cache.CachedClassEntry;
import org.aspectj.weaver.tools.cache.CachedClassReference;
import org.aspectj.weaver.tools.cache.SimpleCache;
import org.aspectj.weaver.tools.cache.SimpleCacheFactory;
import org.aspectj.weaver.tools.cache.WeavedClassCache;
import org.springframework.beans.PropertyAccessor;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/WeavingAdaptor.class */
public class WeavingAdaptor implements IMessageContext {
    public static final String WEAVING_ADAPTOR_VERBOSE = "aj.weaving.verbose";
    public static final String SHOW_WEAVE_INFO_PROPERTY = "org.aspectj.weaver.showWeaveInfo";
    public static final String TRACE_MESSAGES_PROPERTY = "org.aspectj.tracing.messages";
    protected BcelWorld bcelWorld;
    protected BcelWeaver weaver;
    private IMessageHandler messageHandler;
    private WeavingAdaptorMessageHolder messageHolder;
    protected GeneratedClassHandler generatedClassHandler;
    public BcelObjectType delegateForCurrentClass;
    protected ProtectionDomain activeProtectionDomain;
    protected WeavedClassCache cache;
    private static final int INITIALIZED = 1;
    private static final int WEAVE_JAVA_PACKAGE = 2;
    private static final int WEAVE_JAVAX_PACKAGE = 4;
    private static Trace trace = TraceFactory.getTraceFactory().getTrace(WeavingAdaptor.class);
    private boolean enabled = false;
    protected boolean verbose = getVerbose();
    private boolean abortOnError = false;
    protected Map<String, IUnwovenClassFile> generatedClasses = new HashMap();
    private boolean haveWarnedOnJavax = false;
    private int weavingSpecialTypes = 0;
    private ThreadLocal<Boolean> weaverRunning = new ThreadLocal<Boolean>() { // from class: org.aspectj.weaver.tools.WeavingAdaptor.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    protected WeavingAdaptor() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public WeavingAdaptor(WeavingClassLoader weavingClassLoader) {
        this.generatedClassHandler = weavingClassLoader;
        init((ClassLoader) weavingClassLoader, getFullClassPath((ClassLoader) weavingClassLoader), getFullAspectPath((ClassLoader) weavingClassLoader));
    }

    public WeavingAdaptor(GeneratedClassHandler handler, URL[] classURLs, URL[] aspectURLs) {
        this.generatedClassHandler = handler;
        init(null, FileUtil.makeClasspath(classURLs), FileUtil.makeClasspath(aspectURLs));
    }

    protected List<String> getFullClassPath(ClassLoader loader) {
        List<String> list = new LinkedList<>();
        while (loader != null) {
            if (loader instanceof URLClassLoader) {
                URL[] urls = ((URLClassLoader) loader).getURLs();
                list.addAll(0, FileUtil.makeClasspath(urls));
            } else {
                warn("cannot determine classpath");
            }
            loader = loader.getParent();
        }
        list.addAll(0, makeClasspath(System.getProperty("sun.boot.class.path")));
        return list;
    }

    private List<String> getFullAspectPath(ClassLoader loader) {
        List<String> list = new LinkedList<>();
        while (loader != null) {
            if (loader instanceof WeavingClassLoader) {
                URL[] urls = ((WeavingClassLoader) loader).getAspectURLs();
                list.addAll(0, FileUtil.makeClasspath(urls));
            }
            loader = loader.getParent();
        }
        return list;
    }

    private static boolean getVerbose() {
        try {
            return Boolean.getBoolean(WEAVING_ADAPTOR_VERBOSE);
        } catch (Throwable th) {
            return false;
        }
    }

    private void init(ClassLoader loader, List<String> classPath, List<String> aspectPath) {
        this.abortOnError = true;
        createMessageHandler();
        info("using classpath: " + classPath);
        info("using aspectpath: " + aspectPath);
        this.bcelWorld = new BcelWorld(classPath, this.messageHandler, (ICrossReferenceHandler) null);
        this.bcelWorld.setXnoInline(false);
        this.bcelWorld.getLint().loadDefaultProperties();
        if (LangUtil.is15VMOrGreater()) {
            this.bcelWorld.setBehaveInJava5Way(true);
        }
        this.weaver = new BcelWeaver(this.bcelWorld);
        registerAspectLibraries(aspectPath);
        initializeCache(loader, aspectPath, null, getMessageHandler());
        this.enabled = true;
    }

    protected void initializeCache(ClassLoader loader, List<String> aspects, GeneratedClassHandler existingClassHandler, IMessageHandler myMessageHandler) {
        if (WeavedClassCache.isEnabled()) {
            this.cache = WeavedClassCache.createCache(loader, aspects, existingClassHandler, myMessageHandler);
            if (this.cache != null) {
                this.generatedClassHandler = this.cache.getCachingClassHandler();
            }
        }
    }

    protected void createMessageHandler() {
        this.messageHolder = new WeavingAdaptorMessageHolder(new PrintWriter(System.err));
        this.messageHandler = this.messageHolder;
        if (this.verbose) {
            this.messageHandler.dontIgnore(IMessage.INFO);
        }
        if (Boolean.getBoolean(SHOW_WEAVE_INFO_PROPERTY)) {
            this.messageHandler.dontIgnore(IMessage.WEAVEINFO);
        }
        info("AspectJ Weaver Version 1.8.14 built on Wednesday Mar 6, 2019 at 20:45:28 GMT");
    }

    protected IMessageHandler getMessageHandler() {
        return this.messageHandler;
    }

    public IMessageHolder getMessageHolder() {
        return this.messageHolder;
    }

    protected void setMessageHandler(IMessageHandler mh) throws AbortException {
        if (mh instanceof ISupportsMessageContext) {
            ISupportsMessageContext smc = (ISupportsMessageContext) mh;
            smc.setMessageContext(this);
        }
        if (mh != this.messageHolder) {
            this.messageHolder.setDelegate(mh);
        }
        this.messageHolder.flushMessages();
    }

    protected void disable() throws AbortException {
        if (trace.isTraceEnabled()) {
            trace.enter("disable", this);
        }
        this.enabled = false;
        this.messageHolder.flushMessages();
        if (trace.isTraceEnabled()) {
            trace.exit("disable");
        }
    }

    protected void enable() throws AbortException {
        this.enabled = true;
        this.messageHolder.flushMessages();
    }

    protected boolean isEnabled() {
        return this.enabled;
    }

    public void addURL(URL url) {
        File libFile = new File(url.getPath());
        try {
            this.weaver.addLibraryJarFile(libFile);
        } catch (IOException e) {
            warn("bad library: '" + libFile + "'");
        }
    }

    public byte[] weaveClass(String name, byte[] bytes) throws IOException {
        return weaveClass(name, bytes, false);
    }

    public byte[] weaveClass(String name, byte[] bytes, boolean mustWeave) throws IOException {
        if (trace == null) {
            System.err.println("AspectJ Weaver cannot continue to weave, static state has been cleared.  Are you under Tomcat? In order to weave '" + name + "' during shutdown, 'org.apache.catalina.loader.WebappClassLoader.ENABLE_CLEAR_REFERENCES=false' must be set (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=231945).");
            return bytes;
        }
        if (this.weaverRunning.get().booleanValue()) {
            return bytes;
        }
        try {
            this.weaverRunning.set(true);
            if (trace.isTraceEnabled()) {
                trace.enter("weaveClass", (Object) this, new Object[]{name, bytes});
            }
            if (!this.enabled) {
                if (trace.isTraceEnabled()) {
                    trace.exit("weaveClass", false);
                }
                return bytes;
            }
            boolean debugOn = !this.messageHandler.isIgnoring(Message.DEBUG);
            try {
                this.delegateForCurrentClass = null;
                String name2 = name.replace('/', '.');
                if (couldWeave(name2, bytes)) {
                    if (accept(name2, bytes)) {
                        CachedClassReference cacheKey = null;
                        if (this.cache != null && !mustWeave) {
                            cacheKey = this.cache.createCacheKey(name2, bytes);
                            CachedClassEntry entry = this.cache.get(cacheKey, bytes);
                            if (entry != null) {
                                if (entry.isIgnored()) {
                                    this.weaverRunning.set(false);
                                    return bytes;
                                }
                                byte[] bytes2 = entry.getBytes();
                                this.delegateForCurrentClass = null;
                                this.weaverRunning.set(false);
                                return bytes2;
                            }
                        }
                        if (debugOn) {
                            debug("weaving '" + name2 + "'");
                        }
                        bytes = getWovenBytes(name2, bytes);
                        if (cacheKey != null) {
                            if (Arrays.equals(bytes, bytes)) {
                                this.cache.ignore(cacheKey, bytes);
                            } else {
                                this.cache.put(cacheKey, bytes, bytes);
                            }
                        }
                    } else if (debugOn) {
                        debug("not weaving '" + name2 + "'");
                    }
                } else if (debugOn) {
                    debug("cannot weave '" + name2 + "'");
                }
                this.delegateForCurrentClass = null;
                if (trace.isTraceEnabled()) {
                    trace.exit("weaveClass", bytes);
                }
                byte[] bArr = bytes;
                this.weaverRunning.set(false);
                return bArr;
            } finally {
                this.delegateForCurrentClass = null;
            }
        } finally {
            this.weaverRunning.set(Boolean.valueOf(false));
        }
    }

    private boolean couldWeave(String name, byte[] bytes) {
        return !this.generatedClasses.containsKey(name) && shouldWeaveName(name);
    }

    protected boolean accept(String name, byte[] bytes) {
        return true;
    }

    protected boolean shouldDump(String name, boolean before) {
        return false;
    }

    private boolean shouldWeaveName(String name) {
        if ("osj".indexOf(name.charAt(0)) != -1) {
            if ((this.weavingSpecialTypes & 1) == 0) {
                this.weavingSpecialTypes |= 1;
                Properties p = this.weaver.getWorld().getExtraConfiguration();
                if (p != null) {
                    boolean b = p.getProperty(World.xsetWEAVE_JAVA_PACKAGES, "false").equalsIgnoreCase("true");
                    if (b) {
                        this.weavingSpecialTypes |= 2;
                    }
                    boolean b2 = p.getProperty(World.xsetWEAVE_JAVAX_PACKAGES, "false").equalsIgnoreCase("true");
                    if (b2) {
                        this.weavingSpecialTypes |= 4;
                    }
                }
            }
            if (name.startsWith("org.aspectj.") || name.startsWith("sun.reflect.")) {
                return false;
            }
            if (name.startsWith("javax.")) {
                if ((this.weavingSpecialTypes & 4) != 0) {
                    return true;
                }
                if (!this.haveWarnedOnJavax) {
                    this.haveWarnedOnJavax = true;
                    warn("javax.* types are not being woven because the weaver option '-Xset:weaveJavaxPackages=true' has not been specified");
                    return false;
                }
                return false;
            }
            if (!name.startsWith("java.") || (this.weavingSpecialTypes & 2) != 0) {
                return true;
            }
            return false;
        }
        return true;
    }

    private boolean shouldWeaveAnnotationStyleAspect(String name, byte[] bytes) {
        if (this.delegateForCurrentClass == null) {
            ensureDelegateInitialized(name, bytes);
        }
        return this.delegateForCurrentClass.isAnnotationStyleAspect();
    }

    protected void ensureDelegateInitialized(String name, byte[] bytes) {
        if (this.delegateForCurrentClass == null) {
            BcelWorld world = (BcelWorld) this.weaver.getWorld();
            this.delegateForCurrentClass = world.addSourceObjectType(name, bytes, false);
        }
    }

    private byte[] getWovenBytes(String name, byte[] bytes) throws IOException {
        WeavingClassFileProvider wcp = new WeavingClassFileProvider(name, bytes);
        this.weaver.weave(wcp);
        return wcp.getBytes();
    }

    private byte[] getAtAspectJAspectBytes(String name, byte[] bytes) throws IOException {
        WeavingClassFileProvider wcp = new WeavingClassFileProvider(name, bytes);
        wcp.setApplyAtAspectJMungersOnly();
        this.weaver.weave(wcp);
        return wcp.getBytes();
    }

    private void registerAspectLibraries(List aspectPath) {
        Iterator i = aspectPath.iterator();
        while (i.hasNext()) {
            String libName = (String) i.next();
            addAspectLibrary(libName);
        }
        this.weaver.prepareForWeave();
    }

    private void addAspectLibrary(String aspectLibraryName) {
        File aspectLibrary = new File(aspectLibraryName);
        if (aspectLibrary.isDirectory() || FileUtil.isZipFile(aspectLibrary)) {
            try {
                info("adding aspect library: '" + aspectLibrary + "'");
                this.weaver.addLibraryJarFile(aspectLibrary);
                return;
            } catch (IOException ex) {
                error("exception adding aspect library: '" + ex + "'");
                return;
            }
        }
        error("bad aspect library: '" + aspectLibrary + "'");
    }

    private static List<String> makeClasspath(String cp) {
        List<String> ret = new ArrayList<>();
        if (cp != null) {
            StringTokenizer tok = new StringTokenizer(cp, File.pathSeparator);
            while (tok.hasMoreTokens()) {
                ret.add(tok.nextToken());
            }
        }
        return ret;
    }

    protected boolean debug(String message) {
        return MessageUtil.debug(this.messageHandler, message);
    }

    protected boolean info(String message) {
        return MessageUtil.info(this.messageHandler, message);
    }

    protected boolean warn(String message) {
        return MessageUtil.warn(this.messageHandler, message);
    }

    protected boolean warn(String message, Throwable th) {
        return this.messageHandler.handleMessage(new Message(message, IMessage.WARNING, th, (ISourceLocation) null));
    }

    protected boolean error(String message) {
        return MessageUtil.error(this.messageHandler, message);
    }

    protected boolean error(String message, Throwable th) {
        return this.messageHandler.handleMessage(new Message(message, IMessage.ERROR, th, (ISourceLocation) null));
    }

    @Override // org.aspectj.bridge.IMessageContext
    public String getContextId() {
        return "WeavingAdaptor";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dump(String name, byte[] b, boolean before) throws IOException {
        File dir;
        String dirName = getDumpDir();
        if (before) {
            dirName = dirName + File.separator + "_before";
        }
        String className = name.replace('.', '/');
        if (className.indexOf(47) > 0) {
            dir = new File(dirName + File.separator + className.substring(0, className.lastIndexOf(47)));
        } else {
            dir = new File(dirName);
        }
        dir.mkdirs();
        String fileName = dirName + File.separator + className + ClassUtils.CLASS_FILE_SUFFIX;
        try {
            FileOutputStream os = new FileOutputStream(fileName);
            os.write(b);
            os.close();
        } catch (IOException ex) {
            warn("unable to dump class " + name + " in directory " + dirName, ex);
        }
    }

    protected String getDumpDir() {
        return "_ajdump";
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/WeavingAdaptor$WeavingAdaptorMessageHolder.class */
    protected class WeavingAdaptorMessageHolder extends MessageHandler {
        private IMessageHandler delegate;
        private List<IMessage> savedMessages;
        protected boolean traceMessages = Boolean.getBoolean(WeavingAdaptor.TRACE_MESSAGES_PROPERTY);

        public WeavingAdaptorMessageHolder(PrintWriter writer) {
            this.delegate = WeavingAdaptor.this.new WeavingAdaptorMessageWriter(writer);
            super.dontIgnore(IMessage.WEAVEINFO);
        }

        private void traceMessage(IMessage message) {
            if (message instanceof WeaveMessage) {
                WeavingAdaptor.trace.debug(render(message));
                return;
            }
            if (message.isDebug()) {
                WeavingAdaptor.trace.debug(render(message));
                return;
            }
            if (message.isInfo()) {
                WeavingAdaptor.trace.info(render(message));
                return;
            }
            if (message.isWarning()) {
                WeavingAdaptor.trace.warn(render(message), message.getThrown());
                return;
            }
            if (message.isError()) {
                WeavingAdaptor.trace.error(render(message), message.getThrown());
                return;
            }
            if (message.isFailed()) {
                WeavingAdaptor.trace.fatal(render(message), message.getThrown());
            } else if (message.isAbort()) {
                WeavingAdaptor.trace.fatal(render(message), message.getThrown());
            } else {
                WeavingAdaptor.trace.error(render(message), message.getThrown());
            }
        }

        protected String render(IMessage message) {
            return PropertyAccessor.PROPERTY_KEY_PREFIX + WeavingAdaptor.this.getContextId() + "] " + message.toString();
        }

        public void flushMessages() throws AbortException {
            if (this.savedMessages == null) {
                this.savedMessages = new ArrayList();
                this.savedMessages.addAll(super.getUnmodifiableListView());
                clearMessages();
                for (IMessage message : this.savedMessages) {
                    this.delegate.handleMessage(message);
                }
            }
        }

        public void setDelegate(IMessageHandler messageHandler) {
            this.delegate = messageHandler;
        }

        @Override // org.aspectj.bridge.MessageHandler, org.aspectj.bridge.IMessageHandler
        public boolean handleMessage(IMessage message) throws AbortException {
            if (this.traceMessages) {
                traceMessage(message);
            }
            super.handleMessage(message);
            if (WeavingAdaptor.this.abortOnError && 0 <= message.getKind().compareTo(IMessage.ERROR)) {
                throw new AbortException(message);
            }
            if (this.savedMessages != null) {
                this.delegate.handleMessage(message);
                return true;
            }
            return true;
        }

        @Override // org.aspectj.bridge.MessageHandler, org.aspectj.bridge.IMessageHandler
        public boolean isIgnoring(IMessage.Kind kind) {
            return this.delegate.isIgnoring(kind);
        }

        @Override // org.aspectj.bridge.MessageHandler, org.aspectj.bridge.IMessageHandler
        public void dontIgnore(IMessage.Kind kind) {
            if (null != kind && this.delegate != null) {
                this.delegate.dontIgnore(kind);
            }
        }

        @Override // org.aspectj.bridge.MessageHandler, org.aspectj.bridge.IMessageHandler
        public void ignore(IMessage.Kind kind) {
            if (null != kind && this.delegate != null) {
                this.delegate.ignore(kind);
            }
        }

        @Override // org.aspectj.bridge.MessageHandler, org.aspectj.bridge.IMessageHolder
        public List<IMessage> getUnmodifiableListView() {
            List<IMessage> allMessages = new ArrayList<>();
            allMessages.addAll(this.savedMessages);
            allMessages.addAll(super.getUnmodifiableListView());
            return allMessages;
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/WeavingAdaptor$WeavingAdaptorMessageWriter.class */
    protected class WeavingAdaptorMessageWriter extends MessageWriter {
        private final Set<IMessage.Kind> ignoring;
        private final IMessage.Kind failKind;

        public WeavingAdaptorMessageWriter(PrintWriter writer) {
            super(writer, true);
            this.ignoring = new HashSet();
            ignore(IMessage.WEAVEINFO);
            ignore(IMessage.DEBUG);
            ignore(IMessage.INFO);
            this.failKind = IMessage.ERROR;
        }

        @Override // org.aspectj.bridge.MessageWriter, org.aspectj.bridge.IMessageHandler
        public boolean handleMessage(IMessage message) throws AbortException {
            super.handleMessage(message);
            if (WeavingAdaptor.this.abortOnError && 0 <= message.getKind().compareTo(this.failKind)) {
                throw new AbortException(message);
            }
            return true;
        }

        @Override // org.aspectj.bridge.MessageWriter, org.aspectj.bridge.IMessageHandler
        public boolean isIgnoring(IMessage.Kind kind) {
            return null != kind && this.ignoring.contains(kind);
        }

        @Override // org.aspectj.bridge.MessageWriter, org.aspectj.bridge.IMessageHandler
        public void ignore(IMessage.Kind kind) {
            if (null != kind && !this.ignoring.contains(kind)) {
                this.ignoring.add(kind);
            }
        }

        @Override // org.aspectj.bridge.MessageWriter, org.aspectj.bridge.IMessageHandler
        public void dontIgnore(IMessage.Kind kind) {
            if (null != kind) {
                this.ignoring.remove(kind);
            }
        }

        @Override // org.aspectj.bridge.MessageWriter
        protected String render(IMessage message) {
            return PropertyAccessor.PROPERTY_KEY_PREFIX + WeavingAdaptor.this.getContextId() + "] " + super.render(message);
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/tools/WeavingAdaptor$WeavingClassFileProvider.class */
    private class WeavingClassFileProvider implements IClassFileProvider {
        private final UnwovenClassFile unwovenClass;
        private IUnwovenClassFile wovenClass;
        private final List<UnwovenClassFile> unwovenClasses = new ArrayList();
        private boolean isApplyAtAspectJMungersOnly = false;

        public WeavingClassFileProvider(String name, byte[] bytes) throws IOException {
            WeavingAdaptor.this.ensureDelegateInitialized(name, bytes);
            this.unwovenClass = new UnwovenClassFile(name, WeavingAdaptor.this.delegateForCurrentClass.getResolvedTypeX().getName(), bytes);
            this.unwovenClasses.add(this.unwovenClass);
            if (WeavingAdaptor.this.shouldDump(name.replace('/', '.'), true)) {
                WeavingAdaptor.this.dump(name, bytes, true);
            }
        }

        public void setApplyAtAspectJMungersOnly() {
            this.isApplyAtAspectJMungersOnly = true;
        }

        @Override // org.aspectj.weaver.IClassFileProvider
        public boolean isApplyAtAspectJMungersOnly() {
            return this.isApplyAtAspectJMungersOnly;
        }

        public byte[] getBytes() {
            if (this.wovenClass != null) {
                return this.wovenClass.getBytes();
            }
            return this.unwovenClass.getBytes();
        }

        @Override // org.aspectj.weaver.IClassFileProvider
        public Iterator<UnwovenClassFile> getClassFileIterator() {
            return this.unwovenClasses.iterator();
        }

        @Override // org.aspectj.weaver.IClassFileProvider
        public IWeaveRequestor getRequestor() {
            return new IWeaveRequestor() { // from class: org.aspectj.weaver.tools.WeavingAdaptor.WeavingClassFileProvider.1
                @Override // org.aspectj.weaver.IWeaveRequestor
                public void acceptResult(IUnwovenClassFile result) throws IOException {
                    if (WeavingClassFileProvider.this.wovenClass == null) {
                        WeavingClassFileProvider.this.wovenClass = result;
                        String name = result.getClassName();
                        if (WeavingAdaptor.this.shouldDump(name.replace('/', '.'), false)) {
                            WeavingAdaptor.this.dump(name, result.getBytes(), false);
                            return;
                        }
                        return;
                    }
                    String className = result.getClassName();
                    byte[] resultBytes = result.getBytes();
                    if (SimpleCacheFactory.isEnabled()) {
                        SimpleCache lacache = SimpleCacheFactory.createSimpleCache();
                        lacache.put(result.getClassName(), WeavingClassFileProvider.this.wovenClass.getBytes(), result.getBytes());
                        lacache.addGeneratedClassesNames(WeavingClassFileProvider.this.wovenClass.getClassName(), WeavingClassFileProvider.this.wovenClass.getBytes(), result.getClassName());
                    }
                    WeavingAdaptor.this.generatedClasses.put(className, result);
                    WeavingAdaptor.this.generatedClasses.put(WeavingClassFileProvider.this.wovenClass.getClassName(), result);
                    WeavingAdaptor.this.generatedClassHandler.acceptClass(className, null, resultBytes);
                }

                @Override // org.aspectj.weaver.IWeaveRequestor
                public void processingReweavableState() {
                }

                @Override // org.aspectj.weaver.IWeaveRequestor
                public void addingTypeMungers() {
                }

                @Override // org.aspectj.weaver.IWeaveRequestor
                public void weavingAspects() {
                }

                @Override // org.aspectj.weaver.IWeaveRequestor
                public void weavingClasses() {
                }

                @Override // org.aspectj.weaver.IWeaveRequestor
                public void weaveCompleted() {
                    if (WeavingAdaptor.this.delegateForCurrentClass != null) {
                        WeavingAdaptor.this.delegateForCurrentClass.weavingCompleted();
                    }
                }
            };
        }
    }

    public void setActiveProtectionDomain(ProtectionDomain protectionDomain) {
        this.activeProtectionDomain = protectionDomain;
    }
}
