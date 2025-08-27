package org.aspectj.weaver.loadtime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.util.LangUtil;
import org.aspectj.weaver.Lint;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.BcelWeakClassLoaderReference;
import org.aspectj.weaver.bcel.BcelWeaver;
import org.aspectj.weaver.bcel.BcelWorld;
import org.aspectj.weaver.bcel.Utility;
import org.aspectj.weaver.loadtime.Options;
import org.aspectj.weaver.loadtime.definition.Definition;
import org.aspectj.weaver.loadtime.definition.DocumentParser;
import org.aspectj.weaver.ltw.LTWWorld;
import org.aspectj.weaver.patterns.PatternParser;
import org.aspectj.weaver.patterns.TypePattern;
import org.aspectj.weaver.tools.GeneratedClassHandler;
import org.aspectj.weaver.tools.Trace;
import org.aspectj.weaver.tools.TraceFactory;
import org.aspectj.weaver.tools.WeavingAdaptor;
import org.aspectj.weaver.tools.cache.WeavedClassCache;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.util.ResourceUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/ClassLoaderWeavingAdaptor.class */
public class ClassLoaderWeavingAdaptor extends WeavingAdaptor {
    private static final String AOP_XML = "META-INF/aop.xml;META-INF/aop-ajc.xml;org/aspectj/aop.xml";
    private boolean initialized;
    private StringBuffer namespace;
    private IWeavingContext weavingContext;
    private static Trace trace = TraceFactory.getTraceFactory().getTrace(ClassLoaderWeavingAdaptor.class);
    private Method defineClassMethod;
    private Method defineClassWithProtectionDomainMethod;
    private List<TypePattern> m_dumpTypePattern = new ArrayList();
    private boolean m_dumpBefore = false;
    private boolean dumpDirPerClassloader = false;
    private boolean hasExcludes = false;
    private List<TypePattern> excludeTypePattern = new ArrayList();
    private List<String> excludeStartsWith = new ArrayList();
    private List<String> excludeStarDotDotStar = new ArrayList();
    private List<String> excludeExactName = new ArrayList();
    private List<String> excludeEndsWith = new ArrayList();
    private List<String[]> excludeSpecial = new ArrayList();
    private boolean hasIncludes = false;
    private List<TypePattern> includeTypePattern = new ArrayList();
    private List<String> m_includeStartsWith = new ArrayList();
    private List<String> includeExactName = new ArrayList();
    private boolean includeStar = false;
    private List<TypePattern> m_aspectExcludeTypePattern = new ArrayList();
    private List<String> m_aspectExcludeStartsWith = new ArrayList();
    private List<TypePattern> m_aspectIncludeTypePattern = new ArrayList();
    private List<String> m_aspectIncludeStartsWith = new ArrayList();
    private List<ConcreteAspectCodeGen> concreteAspects = new ArrayList();

    public ClassLoaderWeavingAdaptor() {
        if (trace.isTraceEnabled()) {
            trace.enter("<init>", this);
        }
        if (trace.isTraceEnabled()) {
            trace.exit("<init>");
        }
    }

    public ClassLoaderWeavingAdaptor(ClassLoader deprecatedLoader, IWeavingContext deprecatedContext) {
        if (trace.isTraceEnabled()) {
            trace.enter("<init>", (Object) this, new Object[]{deprecatedLoader, deprecatedContext});
        }
        if (trace.isTraceEnabled()) {
            trace.exit("<init>");
        }
    }

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/loadtime/ClassLoaderWeavingAdaptor$SimpleGeneratedClassHandler.class */
    class SimpleGeneratedClassHandler implements GeneratedClassHandler {
        private BcelWeakClassLoaderReference loaderRef;

        SimpleGeneratedClassHandler(ClassLoader loader) {
            this.loaderRef = new BcelWeakClassLoaderReference(loader);
        }

        @Override // org.aspectj.weaver.tools.GeneratedClassHandler
        public void acceptClass(String name, byte[] originalBytes, byte[] wovenBytes) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            try {
                if (ClassLoaderWeavingAdaptor.this.shouldDump(name.replace('/', '.'), false)) {
                    ClassLoaderWeavingAdaptor.this.dump(name, wovenBytes, false);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            if (ClassLoaderWeavingAdaptor.this.activeProtectionDomain != null) {
                ClassLoaderWeavingAdaptor.this.defineClass(this.loaderRef.getClassLoader(), name, wovenBytes, ClassLoaderWeavingAdaptor.this.activeProtectionDomain);
            } else {
                ClassLoaderWeavingAdaptor.this.defineClass(this.loaderRef.getClassLoader(), name, wovenBytes);
            }
        }
    }

    public void initialize(ClassLoader classLoader, IWeavingContext context) {
        if (this.initialized) {
            return;
        }
        this.weavingContext = context;
        if (this.weavingContext == null) {
            this.weavingContext = new DefaultWeavingContext(classLoader);
        }
        createMessageHandler();
        this.generatedClassHandler = new SimpleGeneratedClassHandler(classLoader);
        List definitions = this.weavingContext.getDefinitions(classLoader, this);
        if (definitions.isEmpty()) {
            disable();
            if (trace.isTraceEnabled()) {
                trace.exit("initialize", definitions);
                return;
            }
            return;
        }
        this.bcelWorld = new LTWWorld(classLoader, this.weavingContext, getMessageHandler(), null);
        this.weaver = new BcelWeaver(this.bcelWorld);
        boolean success = registerDefinitions(this.weaver, classLoader, definitions);
        if (success) {
            this.weaver.prepareForWeave();
            enable();
            success = weaveAndDefineConceteAspects();
        }
        if (success) {
            enable();
        } else {
            disable();
            this.bcelWorld = null;
            this.weaver = null;
        }
        if (WeavedClassCache.isEnabled()) {
            initializeCache(classLoader, getAspectClassNames(definitions), this.generatedClassHandler, getMessageHandler());
        }
        this.initialized = true;
        if (trace.isTraceEnabled()) {
            trace.exit("initialize", isEnabled());
        }
    }

    List<String> getAspectClassNames(List<Definition> definitions) {
        List<String> aspects = new LinkedList<>();
        for (Definition def : definitions) {
            List<String> defAspects = def.getAspectClassNames();
            if (defAspects != null) {
                aspects.addAll(defAspects);
            }
        }
        return aspects;
    }

    List<Definition> parseDefinitions(ClassLoader loader) {
        String file;
        if (trace.isTraceEnabled()) {
            trace.enter("parseDefinitions", this);
        }
        List<Definition> definitions = new ArrayList<>();
        try {
            info("register classloader " + getClassLoaderName(loader));
            if (loader.equals(ClassLoader.getSystemClassLoader()) && (file = System.getProperty("aj5.def", null)) != null) {
                info("using (-Daj5.def) " + file);
                definitions.add(DocumentParser.parse(new File(file).toURL()));
            }
            String resourcePath = System.getProperty("org.aspectj.weaver.loadtime.configuration", AOP_XML);
            if (trace.isTraceEnabled()) {
                trace.event("parseDefinitions", this, resourcePath);
            }
            StringTokenizer st = new StringTokenizer(resourcePath, ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
            while (st.hasMoreTokens()) {
                String nextDefinition = st.nextToken();
                if (nextDefinition.startsWith(ResourceUtils.FILE_URL_PREFIX)) {
                    try {
                        String fpath = new URL(nextDefinition).getFile();
                        File configFile = new File(fpath);
                        if (!configFile.exists()) {
                            warn("configuration does not exist: " + nextDefinition);
                        } else {
                            definitions.add(DocumentParser.parse(configFile.toURL()));
                        }
                    } catch (MalformedURLException e) {
                        error("malformed definition url: " + nextDefinition);
                    }
                } else {
                    Enumeration<URL> xmls = this.weavingContext.getResources(nextDefinition);
                    Set<URL> seenBefore = new HashSet<>();
                    while (xmls.hasMoreElements()) {
                        URL xml = xmls.nextElement();
                        if (trace.isTraceEnabled()) {
                            trace.event("parseDefinitions", this, xml);
                        }
                        if (!seenBefore.contains(xml)) {
                            info("using configuration " + this.weavingContext.getFile(xml));
                            definitions.add(DocumentParser.parse(xml));
                            seenBefore.add(xml);
                        } else {
                            debug("ignoring duplicate definition: " + xml);
                        }
                    }
                }
            }
            if (definitions.isEmpty()) {
                info("no configuration found. Disabling weaver for class loader " + getClassLoaderName(loader));
            }
        } catch (Exception e2) {
            definitions.clear();
            warn("parse definitions failed", e2);
        }
        if (trace.isTraceEnabled()) {
            trace.exit("parseDefinitions", definitions);
        }
        return definitions;
    }

    private boolean registerDefinitions(BcelWeaver weaver, ClassLoader loader, List<Definition> definitions) {
        boolean success;
        if (trace.isTraceEnabled()) {
            trace.enter("registerDefinitions", this, definitions);
        }
        try {
            registerOptions(weaver, loader, definitions);
            registerAspectExclude(weaver, loader, definitions);
            registerAspectInclude(weaver, loader, definitions);
            success = registerAspects(weaver, loader, definitions);
            registerIncludeExclude(weaver, loader, definitions);
            registerDump(weaver, loader, definitions);
        } catch (Exception ex) {
            trace.error("register definition failed", ex);
            success = false;
            warn("register definition failed", ex instanceof AbortException ? null : ex);
        }
        if (trace.isTraceEnabled()) {
            trace.exit("registerDefinitions", success);
        }
        return success;
    }

    private String getClassLoaderName(ClassLoader loader) {
        return this.weavingContext.getClassLoaderName();
    }

    private void registerOptions(BcelWeaver weaver, ClassLoader loader, List<Definition> definitions) throws AbortException {
        StringBuffer allOptions = new StringBuffer();
        for (Definition definition : definitions) {
            allOptions.append(definition.getWeaverOptions()).append(' ');
        }
        Options.WeaverOption weaverOption = Options.parse(allOptions.toString(), loader, getMessageHandler());
        World world = weaver.getWorld();
        setMessageHandler(weaverOption.messageHandler);
        world.setXlazyTjp(weaverOption.lazyTjp);
        world.setXHasMemberSupportEnabled(weaverOption.hasMember);
        world.setTiming(weaverOption.timers, true);
        world.setOptionalJoinpoints(weaverOption.optionalJoinpoints);
        world.setPinpointMode(weaverOption.pinpoint);
        weaver.setReweavableMode(weaverOption.notReWeavable);
        if (weaverOption.loadersToSkip != null && weaverOption.loadersToSkip.length() > 0) {
            Aj.loadersToSkip = LangUtil.anySplit(weaverOption.loadersToSkip, ",");
        }
        if (Aj.loadersToSkip != null) {
            MessageUtil.info(world.getMessageHandler(), "no longer creating weavers for these classloaders: " + Aj.loadersToSkip);
        }
        world.performExtraConfiguration(weaverOption.xSet);
        world.setXnoInline(weaverOption.noInline);
        world.setBehaveInJava5Way(LangUtil.is15VMOrGreater());
        world.setAddSerialVerUID(weaverOption.addSerialVersionUID);
        this.bcelWorld.getLint().loadDefaultProperties();
        this.bcelWorld.getLint().adviceDidNotMatch.setKind(null);
        if (weaverOption.lintFile != null) {
            InputStream resource = null;
            try {
                resource = loader.getResourceAsStream(weaverOption.lintFile);
                Exception failure = null;
                if (resource != null) {
                    try {
                        Properties properties = new Properties();
                        properties.load(resource);
                        world.getLint().setFromProperties(properties);
                    } catch (IOException e) {
                        failure = e;
                    }
                }
                if (failure != null || resource == null) {
                    warn("Cannot access resource for -Xlintfile:" + weaverOption.lintFile, failure);
                }
                try {
                    resource.close();
                } catch (Throwable th) {
                }
            } catch (Throwable th2) {
                try {
                    resource.close();
                } catch (Throwable th3) {
                }
                throw th2;
            }
        }
        if (weaverOption.lint != null) {
            if (weaverOption.lint.equals("default")) {
                this.bcelWorld.getLint().loadDefaultProperties();
                return;
            }
            this.bcelWorld.getLint().setAll(weaverOption.lint);
            if (weaverOption.lint.equals("ignore")) {
                this.bcelWorld.setAllLintIgnored();
            }
        }
    }

    private void registerAspectExclude(BcelWeaver weaver, ClassLoader loader, List<Definition> definitions) {
        for (Definition definition : definitions) {
            for (String exclude : definition.getAspectExcludePatterns()) {
                TypePattern excludePattern = new PatternParser(exclude).parseTypePattern();
                this.m_aspectExcludeTypePattern.add(excludePattern);
                String fastMatchInfo = looksLikeStartsWith(exclude);
                if (fastMatchInfo != null) {
                    this.m_aspectExcludeStartsWith.add(fastMatchInfo);
                }
            }
        }
    }

    private void registerAspectInclude(BcelWeaver weaver, ClassLoader loader, List<Definition> definitions) {
        for (Definition definition : definitions) {
            for (String include : definition.getAspectIncludePatterns()) {
                TypePattern includePattern = new PatternParser(include).parseTypePattern();
                this.m_aspectIncludeTypePattern.add(includePattern);
                String fastMatchInfo = looksLikeStartsWith(include);
                if (fastMatchInfo != null) {
                    this.m_aspectIncludeStartsWith.add(fastMatchInfo);
                }
            }
        }
    }

    protected void lint(String name, String[] infos) {
        Lint lint = this.bcelWorld.getLint();
        Lint.Kind kind = lint.getLintKind(name);
        kind.signal(infos, null, null);
    }

    @Override // org.aspectj.weaver.tools.WeavingAdaptor, org.aspectj.bridge.IMessageContext
    public String getContextId() {
        return this.weavingContext.getId();
    }

    private boolean registerAspects(BcelWeaver weaver, ClassLoader loader, List<Definition> definitions) {
        if (trace.isTraceEnabled()) {
            trace.enter("registerAspects", (Object) this, new Object[]{weaver, loader, definitions});
        }
        boolean success = true;
        for (Definition definition : definitions) {
            for (String aspectClassName : definition.getAspectClassNames()) {
                if (acceptAspect(aspectClassName)) {
                    info("register aspect " + aspectClassName);
                    String requiredType = definition.getAspectRequires(aspectClassName);
                    if (requiredType != null) {
                        ((BcelWorld) weaver.getWorld()).addAspectRequires(aspectClassName, requiredType);
                    }
                    String definedScope = definition.getScopeForAspect(aspectClassName);
                    if (definedScope != null) {
                        ((BcelWorld) weaver.getWorld()).addScopedAspect(aspectClassName, definedScope);
                    }
                    weaver.addLibraryAspect(aspectClassName);
                    if (this.namespace == null) {
                        this.namespace = new StringBuffer(aspectClassName);
                    } else {
                        this.namespace = this.namespace.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR).append(aspectClassName);
                    }
                } else {
                    lint("aspectExcludedByConfiguration", new String[]{aspectClassName, getClassLoaderName(loader)});
                }
            }
        }
        Iterator<Definition> it = definitions.iterator();
        while (it.hasNext()) {
            Iterator<Definition.ConcreteAspect> it2 = it.next().getConcreteAspects().iterator();
            while (true) {
                if (it2.hasNext()) {
                    Definition.ConcreteAspect concreteAspect = it2.next();
                    if (acceptAspect(concreteAspect.name)) {
                        info("define aspect " + concreteAspect.name);
                        ConcreteAspectCodeGen gen = new ConcreteAspectCodeGen(concreteAspect, weaver.getWorld());
                        if (!gen.validate()) {
                            error("Concrete-aspect '" + concreteAspect.name + "' could not be registered");
                            success = false;
                            break;
                        }
                        ((BcelWorld) weaver.getWorld()).addSourceObjectType(Utility.makeJavaClass(concreteAspect.name, gen.getBytes()), true);
                        this.concreteAspects.add(gen);
                        weaver.addLibraryAspect(concreteAspect.name);
                        if (this.namespace == null) {
                            this.namespace = new StringBuffer(concreteAspect.name);
                        } else {
                            this.namespace = this.namespace.append(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR + concreteAspect.name);
                        }
                    }
                }
            }
        }
        if (!success) {
            warn("failure(s) registering aspects. Disabling weaver for class loader " + getClassLoaderName(loader));
        } else if (this.namespace == null) {
            success = false;
            info("no aspects registered. Disabling weaver for class loader " + getClassLoaderName(loader));
        }
        if (trace.isTraceEnabled()) {
            trace.exit("registerAspects", success);
        }
        return success;
    }

    private boolean weaveAndDefineConceteAspects() {
        if (trace.isTraceEnabled()) {
            trace.enter("weaveAndDefineConceteAspects", this, this.concreteAspects);
        }
        for (ConcreteAspectCodeGen gen : this.concreteAspects) {
            String name = gen.getClassName();
            byte[] bytes = gen.getBytes();
            try {
                byte[] newBytes = weaveClass(name, bytes, true);
                this.generatedClassHandler.acceptClass(name, bytes, newBytes);
            } catch (IOException ex) {
                trace.error("weaveAndDefineConceteAspects", ex);
                error("exception weaving aspect '" + name + "'", ex);
            }
        }
        if (trace.isTraceEnabled()) {
            trace.exit("weaveAndDefineConceteAspects", true);
        }
        return true;
    }

    private void registerIncludeExclude(BcelWeaver weaver, ClassLoader loader, List<Definition> definitions) {
        for (Definition definition : definitions) {
            for (String include : definition.getIncludePatterns()) {
                this.hasIncludes = true;
                String fastMatchInfo = looksLikeStartsWith(include);
                if (fastMatchInfo != null) {
                    this.m_includeStartsWith.add(fastMatchInfo);
                } else if (include.equals("*")) {
                    this.includeStar = true;
                } else {
                    String fastMatchInfo2 = looksLikeExactName(include);
                    if (fastMatchInfo2 != null) {
                        this.includeExactName.add(fastMatchInfo2);
                    } else {
                        TypePattern includePattern = new PatternParser(include).parseTypePattern();
                        this.includeTypePattern.add(includePattern);
                    }
                }
            }
            for (String exclude : definition.getExcludePatterns()) {
                this.hasExcludes = true;
                String fastMatchInfo3 = looksLikeStartsWith(exclude);
                if (fastMatchInfo3 != null) {
                    this.excludeStartsWith.add(fastMatchInfo3);
                } else {
                    String fastMatchInfo4 = looksLikeStarDotDotStarExclude(exclude);
                    if (fastMatchInfo4 != null) {
                        this.excludeStarDotDotStar.add(fastMatchInfo4);
                    } else if (looksLikeExactName(exclude) != null) {
                        this.excludeExactName.add(exclude);
                    } else {
                        String fastMatchInfo5 = looksLikeEndsWith(exclude);
                        if (fastMatchInfo5 != null) {
                            this.excludeEndsWith.add(fastMatchInfo5);
                        } else if (exclude.equals("org.codehaus.groovy..* && !org.codehaus.groovy.grails.web.servlet.mvc.SimpleGrailsController*")) {
                            this.excludeSpecial.add(new String[]{"org.codehaus.groovy.", "org.codehaus.groovy.grails.web.servlet.mvc.SimpleGrailsController"});
                        } else {
                            TypePattern excludePattern = new PatternParser(exclude).parseTypePattern();
                            this.excludeTypePattern.add(excludePattern);
                        }
                    }
                }
            }
        }
    }

    private String looksLikeStarDotDotStarExclude(String typePattern) {
        if (!typePattern.startsWith("*..*") || !typePattern.endsWith("*")) {
            return null;
        }
        String subPattern = typePattern.substring(4, typePattern.length() - 1);
        if (hasStarDot(subPattern, 0)) {
            return null;
        }
        return subPattern.replace('$', '.');
    }

    private String looksLikeExactName(String typePattern) {
        if (hasSpaceAnnotationPlus(typePattern, 0) || typePattern.indexOf("*") != -1) {
            return null;
        }
        return typePattern.replace('$', '.');
    }

    private String looksLikeEndsWith(String typePattern) {
        if (typePattern.charAt(0) != '*' || hasSpaceAnnotationPlus(typePattern, 1) || hasStarDot(typePattern, 1)) {
            return null;
        }
        return typePattern.substring(1).replace('$', '.');
    }

    private boolean hasSpaceAnnotationPlus(String string, int pos) {
        int max = string.length();
        for (int i = pos; i < max; i++) {
            char ch2 = string.charAt(i);
            if (ch2 == ' ' || ch2 == '@' || ch2 == '+') {
                return true;
            }
        }
        return false;
    }

    private boolean hasStarDot(String string, int pos) {
        int max = string.length();
        for (int i = pos; i < max; i++) {
            char ch2 = string.charAt(i);
            if (ch2 == '*' || ch2 == '.') {
                return true;
            }
        }
        return false;
    }

    private String looksLikeStartsWith(String typePattern) {
        if (hasSpaceAnnotationPlus(typePattern, 0) || typePattern.charAt(typePattern.length() - 1) != '*') {
            return null;
        }
        int length = typePattern.length();
        if (typePattern.endsWith("..*") && length > 3 && typePattern.indexOf("..") == length - 3 && typePattern.indexOf(42) == length - 1) {
            return typePattern.substring(0, length - 2).replace('$', '.');
        }
        return null;
    }

    private void registerDump(BcelWeaver weaver, ClassLoader loader, List<Definition> definitions) {
        for (Definition definition : definitions) {
            for (String dump : definition.getDumpPatterns()) {
                TypePattern pattern = new PatternParser(dump).parseTypePattern();
                this.m_dumpTypePattern.add(pattern);
            }
            if (definition.shouldDumpBefore()) {
                this.m_dumpBefore = true;
            }
            if (definition.createDumpDirPerClassloader()) {
                this.dumpDirPerClassloader = true;
            }
        }
    }

    @Override // org.aspectj.weaver.tools.WeavingAdaptor
    protected boolean accept(String className, byte[] bytes) {
        if (!this.hasExcludes && !this.hasIncludes) {
            return true;
        }
        String fastClassName = className.replace('/', '.');
        for (String excludeStartsWithString : this.excludeStartsWith) {
            if (fastClassName.startsWith(excludeStartsWithString)) {
                return false;
            }
        }
        if (!this.excludeStarDotDotStar.isEmpty()) {
            for (String namePiece : this.excludeStarDotDotStar) {
                int index = fastClassName.lastIndexOf(46);
                if (fastClassName.indexOf(namePiece, index + 1) != -1) {
                    return false;
                }
            }
        }
        String fastClassName2 = fastClassName.replace('$', '.');
        if (!this.excludeEndsWith.isEmpty()) {
            for (String lastPiece : this.excludeEndsWith) {
                if (fastClassName2.endsWith(lastPiece)) {
                    return false;
                }
            }
        }
        if (!this.excludeExactName.isEmpty()) {
            for (String name : this.excludeExactName) {
                if (fastClassName2.equals(name)) {
                    return false;
                }
            }
        }
        if (!this.excludeSpecial.isEmpty()) {
            for (String[] entry : this.excludeSpecial) {
                String excludeThese = entry[0];
                String exceptThese = entry[1];
                if (fastClassName2.startsWith(excludeThese) && !fastClassName2.startsWith(exceptThese)) {
                    return false;
                }
            }
        }
        boolean didSomeIncludeMatching = false;
        if (this.excludeTypePattern.isEmpty()) {
            if (this.includeStar) {
                return true;
            }
            if (!this.includeExactName.isEmpty()) {
                didSomeIncludeMatching = true;
                for (String exactname : this.includeExactName) {
                    if (fastClassName2.equals(exactname)) {
                        return true;
                    }
                }
            }
            for (int i = 0; i < this.m_includeStartsWith.size(); i++) {
                didSomeIncludeMatching = true;
                boolean fastAccept = fastClassName2.startsWith(this.m_includeStartsWith.get(i));
                if (fastAccept) {
                    return true;
                }
            }
            if (this.includeTypePattern.isEmpty()) {
                return !didSomeIncludeMatching;
            }
        }
        try {
            ensureDelegateInitialized(className, bytes);
            ResolvedType classInfo = this.delegateForCurrentClass.getResolvedTypeX();
            for (TypePattern typePattern : this.excludeTypePattern) {
                if (typePattern.matchesStatically(classInfo)) {
                    return false;
                }
            }
            if (this.includeStar) {
                this.bcelWorld.demote();
                return true;
            }
            if (!this.includeExactName.isEmpty()) {
                didSomeIncludeMatching = true;
                for (String exactname2 : this.includeExactName) {
                    if (fastClassName2.equals(exactname2)) {
                        this.bcelWorld.demote();
                        return true;
                    }
                }
            }
            for (int i2 = 0; i2 < this.m_includeStartsWith.size(); i2++) {
                didSomeIncludeMatching = true;
                boolean fastaccept = fastClassName2.startsWith(this.m_includeStartsWith.get(i2));
                if (fastaccept) {
                    this.bcelWorld.demote();
                    return true;
                }
            }
            boolean accept = !didSomeIncludeMatching;
            for (TypePattern typePattern2 : this.includeTypePattern) {
                accept = typePattern2.matchesStatically(classInfo);
                if (accept) {
                    break;
                }
            }
            this.bcelWorld.demote();
            return accept;
        } finally {
            this.bcelWorld.demote();
        }
    }

    private boolean acceptAspect(String aspectClassName) {
        if (this.m_aspectExcludeTypePattern.isEmpty() && this.m_aspectIncludeTypePattern.isEmpty()) {
            return true;
        }
        String fastClassName = aspectClassName.replace('/', '.').replace('.', '$');
        for (int i = 0; i < this.m_aspectExcludeStartsWith.size(); i++) {
            if (fastClassName.startsWith(this.m_aspectExcludeStartsWith.get(i))) {
                return false;
            }
        }
        for (int i2 = 0; i2 < this.m_aspectIncludeStartsWith.size(); i2++) {
            if (fastClassName.startsWith(this.m_aspectIncludeStartsWith.get(i2))) {
                return true;
            }
        }
        ResolvedType classInfo = this.weaver.getWorld().resolve(UnresolvedType.forName(aspectClassName), true);
        for (TypePattern typePattern : this.m_aspectExcludeTypePattern) {
            if (typePattern.matchesStatically(classInfo)) {
                return false;
            }
        }
        boolean accept = true;
        for (TypePattern typePattern2 : this.m_aspectIncludeTypePattern) {
            accept = typePattern2.matchesStatically(classInfo);
            if (accept) {
                break;
            }
        }
        return accept;
    }

    @Override // org.aspectj.weaver.tools.WeavingAdaptor
    protected boolean shouldDump(String className, boolean before) {
        if ((before && !this.m_dumpBefore) || this.m_dumpTypePattern.isEmpty()) {
            return false;
        }
        ResolvedType classInfo = this.weaver.getWorld().resolve(UnresolvedType.forName(className), true);
        for (TypePattern typePattern : this.m_dumpTypePattern) {
            if (typePattern.matchesStatically(classInfo)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.weaver.tools.WeavingAdaptor
    protected String getDumpDir() {
        if (this.dumpDirPerClassloader) {
            StringBuffer dir = new StringBuffer();
            dir.append("_ajdump").append(File.separator).append(this.weavingContext.getId());
            return dir.toString();
        }
        return super.getDumpDir();
    }

    public String getNamespace() {
        if (this.namespace == null) {
            return "";
        }
        return new String(this.namespace);
    }

    public boolean generatedClassesExistFor(String className) {
        if (className == null) {
            return !this.generatedClasses.isEmpty();
        }
        return this.generatedClasses.containsKey(className);
    }

    public void flushGeneratedClasses() {
        this.generatedClasses = new HashMap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void defineClass(ClassLoader loader, String name, byte[] bytes) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (trace.isTraceEnabled()) {
            trace.enter("defineClass", (Object) this, new Object[]{loader, name, bytes});
        }
        Object clazz = null;
        debug("generating class '" + name + "'");
        try {
            if (this.defineClassMethod == null) {
                this.defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, bytes.getClass(), Integer.TYPE, Integer.TYPE);
            }
            this.defineClassMethod.setAccessible(true);
            clazz = this.defineClassMethod.invoke(loader, name, bytes, new Integer(0), new Integer(bytes.length));
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof LinkageError) {
                warn("define generated class failed", e.getTargetException());
            } else {
                warn("define generated class failed", e.getTargetException());
            }
        } catch (Exception e2) {
            warn("define generated class failed", e2);
        }
        if (trace.isTraceEnabled()) {
            trace.exit("defineClass", clazz);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void defineClass(ClassLoader loader, String name, byte[] bytes, ProtectionDomain protectionDomain) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (trace.isTraceEnabled()) {
            trace.enter("defineClass", (Object) this, new Object[]{loader, name, bytes, protectionDomain});
        }
        Object clazz = null;
        debug("generating class '" + name + "'");
        try {
            if (this.defineClassWithProtectionDomainMethod == null) {
                this.defineClassWithProtectionDomainMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, bytes.getClass(), Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
            }
            this.defineClassWithProtectionDomainMethod.setAccessible(true);
            clazz = this.defineClassWithProtectionDomainMethod.invoke(loader, name, bytes, 0, new Integer(bytes.length), protectionDomain);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof LinkageError) {
                warn("define generated class failed", e.getTargetException());
            } else {
                warn("define generated class failed", e.getTargetException());
            }
        } catch (Exception e2) {
            warn("define generated class failed", e2);
        }
        if (trace.isTraceEnabled()) {
            trace.exit("defineClass", clazz);
        }
    }
}
