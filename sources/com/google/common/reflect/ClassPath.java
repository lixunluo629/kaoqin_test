package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/reflect/ClassPath.class */
public final class ClassPath {
    private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
    private static final Predicate<ClassInfo> IS_TOP_LEVEL = new Predicate<ClassInfo>() { // from class: com.google.common.reflect.ClassPath.1
        @Override // com.google.common.base.Predicate
        public boolean apply(ClassInfo info) {
            return info.className.indexOf(36) == -1;
        }
    };
    private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(SymbolConstants.SPACE_SYMBOL).omitEmptyStrings();
    private static final String CLASS_FILE_NAME_EXTENSION = ".class";
    private final ImmutableSet<ResourceInfo> resources;

    private ClassPath(ImmutableSet<ResourceInfo> resources) {
        this.resources = resources;
    }

    public static ClassPath from(ClassLoader classloader) throws IOException {
        Scanner scanner = new Scanner();
        Iterator i$ = getClassPathEntries(classloader).entrySet().iterator();
        while (i$.hasNext()) {
            Map.Entry<URI, ClassLoader> entry = (Map.Entry) i$.next();
            scanner.scan(entry.getKey(), entry.getValue());
        }
        return new ClassPath(scanner.getResources());
    }

    public ImmutableSet<ResourceInfo> getResources() {
        return this.resources;
    }

    public ImmutableSet<ClassInfo> getAllClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).toSet();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).filter(IS_TOP_LEVEL).toSet();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses(String packageName) {
        Preconditions.checkNotNull(packageName);
        ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        Iterator i$ = getTopLevelClasses().iterator();
        while (i$.hasNext()) {
            ClassInfo classInfo = (ClassInfo) i$.next();
            if (classInfo.getPackageName().equals(packageName)) {
                builder.add((ImmutableSet.Builder<ClassInfo>) classInfo);
            }
        }
        return builder.build();
    }

    public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String packageName) {
        Preconditions.checkNotNull(packageName);
        String strValueOf = String.valueOf(String.valueOf(packageName));
        String packagePrefix = new StringBuilder(1 + strValueOf.length()).append(strValueOf).append(".").toString();
        ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        Iterator i$ = getTopLevelClasses().iterator();
        while (i$.hasNext()) {
            ClassInfo classInfo = (ClassInfo) i$.next();
            if (classInfo.getName().startsWith(packagePrefix)) {
                builder.add((ImmutableSet.Builder<ClassInfo>) classInfo);
            }
        }
        return builder.build();
    }

    @Beta
    /* loaded from: guava-18.0.jar:com/google/common/reflect/ClassPath$ResourceInfo.class */
    public static class ResourceInfo {
        private final String resourceName;
        final ClassLoader loader;

        static ResourceInfo of(String resourceName, ClassLoader loader) {
            if (resourceName.endsWith(".class")) {
                return new ClassInfo(resourceName, loader);
            }
            return new ResourceInfo(resourceName, loader);
        }

        ResourceInfo(String resourceName, ClassLoader loader) {
            this.resourceName = (String) Preconditions.checkNotNull(resourceName);
            this.loader = (ClassLoader) Preconditions.checkNotNull(loader);
        }

        public final URL url() {
            return (URL) Preconditions.checkNotNull(this.loader.getResource(this.resourceName), "Failed to load resource: %s", this.resourceName);
        }

        public final String getResourceName() {
            return this.resourceName;
        }

        public int hashCode() {
            return this.resourceName.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj instanceof ResourceInfo) {
                ResourceInfo that = (ResourceInfo) obj;
                return this.resourceName.equals(that.resourceName) && this.loader == that.loader;
            }
            return false;
        }

        public String toString() {
            return this.resourceName;
        }
    }

    @Beta
    /* loaded from: guava-18.0.jar:com/google/common/reflect/ClassPath$ClassInfo.class */
    public static final class ClassInfo extends ResourceInfo {
        private final String className;

        ClassInfo(String resourceName, ClassLoader loader) {
            super(resourceName, loader);
            this.className = ClassPath.getClassName(resourceName);
        }

        public String getPackageName() {
            return Reflection.getPackageName(this.className);
        }

        public String getSimpleName() {
            int lastDollarSign = this.className.lastIndexOf(36);
            if (lastDollarSign != -1) {
                String innerClassName = this.className.substring(lastDollarSign + 1);
                return CharMatcher.DIGIT.trimLeadingFrom(innerClassName);
            }
            String packageName = getPackageName();
            if (packageName.isEmpty()) {
                return this.className;
            }
            return this.className.substring(packageName.length() + 1);
        }

        public String getName() {
            return this.className;
        }

        public Class<?> load() {
            try {
                return this.loader.loadClass(this.className);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override // com.google.common.reflect.ClassPath.ResourceInfo
        public String toString() {
            return this.className;
        }
    }

    @VisibleForTesting
    static ImmutableMap<URI, ClassLoader> getClassPathEntries(ClassLoader classloader) throws URISyntaxException {
        LinkedHashMap<URI, ClassLoader> entries = Maps.newLinkedHashMap();
        ClassLoader parent = classloader.getParent();
        if (parent != null) {
            entries.putAll(getClassPathEntries(parent));
        }
        if (classloader instanceof URLClassLoader) {
            URLClassLoader urlClassLoader = (URLClassLoader) classloader;
            URL[] arr$ = urlClassLoader.getURLs();
            for (URL entry : arr$) {
                try {
                    URI uri = entry.toURI();
                    if (!entries.containsKey(uri)) {
                        entries.put(uri, classloader);
                    }
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
        return ImmutableMap.copyOf((Map) entries);
    }

    @VisibleForTesting
    /* loaded from: guava-18.0.jar:com/google/common/reflect/ClassPath$Scanner.class */
    static final class Scanner {
        private final ImmutableSortedSet.Builder<ResourceInfo> resources = new ImmutableSortedSet.Builder<>(Ordering.usingToString());
        private final Set<URI> scannedUris = Sets.newHashSet();

        Scanner() {
        }

        ImmutableSortedSet<ResourceInfo> getResources() {
            return this.resources.build();
        }

        void scan(URI uri, ClassLoader classloader) throws IOException {
            if (uri.getScheme().equals("file") && this.scannedUris.add(uri)) {
                scanFrom(new File(uri), classloader);
            }
        }

        @VisibleForTesting
        void scanFrom(File file, ClassLoader classloader) throws IOException {
            if (!file.exists()) {
                return;
            }
            if (file.isDirectory()) {
                scanDirectory(file, classloader);
            } else {
                scanJar(file, classloader);
            }
        }

        private void scanDirectory(File directory, ClassLoader classloader) throws IOException {
            scanDirectory(directory, classloader, "", ImmutableSet.of());
        }

        private void scanDirectory(File directory, ClassLoader classloader, String packagePrefix, ImmutableSet<File> ancestors) throws IOException {
            String strConcat;
            File canonical = directory.getCanonicalFile();
            if (ancestors.contains(canonical)) {
                return;
            }
            File[] files = directory.listFiles();
            if (files == null) {
                Logger logger = ClassPath.logger;
                String strValueOf = String.valueOf(String.valueOf(directory));
                logger.warning(new StringBuilder(22 + strValueOf.length()).append("Cannot read directory ").append(strValueOf).toString());
                return;
            }
            ImmutableSet<File> newAncestors = ImmutableSet.builder().addAll((Iterable) ancestors).add((ImmutableSet.Builder) canonical).build();
            for (File f : files) {
                String name = f.getName();
                if (f.isDirectory()) {
                    String strValueOf2 = String.valueOf(String.valueOf(packagePrefix));
                    String strValueOf3 = String.valueOf(String.valueOf(name));
                    scanDirectory(f, classloader, new StringBuilder(1 + strValueOf2.length() + strValueOf3.length()).append(strValueOf2).append(strValueOf3).append("/").toString(), newAncestors);
                } else {
                    String strValueOf4 = String.valueOf(packagePrefix);
                    String strValueOf5 = String.valueOf(name);
                    if (strValueOf5.length() != 0) {
                        strConcat = strValueOf4.concat(strValueOf5);
                    } else {
                        strConcat = str;
                        String str = new String(strValueOf4);
                    }
                    String resourceName = strConcat;
                    if (!resourceName.equals("META-INF/MANIFEST.MF")) {
                        this.resources.add((ImmutableSortedSet.Builder<ResourceInfo>) ResourceInfo.of(resourceName, classloader));
                    }
                }
            }
        }

        private void scanJar(File file, ClassLoader classloader) throws IOException {
            try {
                JarFile jarFile = new JarFile(file);
                try {
                    Iterator i$ = getClassPathFromManifest(file, jarFile.getManifest()).iterator();
                    while (i$.hasNext()) {
                        URI uri = (URI) i$.next();
                        scan(uri, classloader);
                    }
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (!entry.isDirectory() && !entry.getName().equals("META-INF/MANIFEST.MF")) {
                            this.resources.add((ImmutableSortedSet.Builder<ResourceInfo>) ResourceInfo.of(entry.getName(), classloader));
                        }
                    }
                } finally {
                    try {
                        jarFile.close();
                    } catch (IOException e) {
                    }
                }
            } catch (IOException e2) {
            }
        }

        @VisibleForTesting
        static ImmutableSet<URI> getClassPathFromManifest(File jarFile, @Nullable Manifest manifest) {
            String strConcat;
            if (manifest == null) {
                return ImmutableSet.of();
            }
            ImmutableSet.Builder<URI> builder = ImmutableSet.builder();
            String classpathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
            if (classpathAttribute != null) {
                for (String path : ClassPath.CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute)) {
                    try {
                        URI uri = getClassPathEntry(jarFile, path);
                        builder.add((ImmutableSet.Builder<URI>) uri);
                    } catch (URISyntaxException e) {
                        Logger logger = ClassPath.logger;
                        String strValueOf = String.valueOf(path);
                        if (strValueOf.length() != 0) {
                            strConcat = "Invalid Class-Path entry: ".concat(strValueOf);
                        } else {
                            strConcat = str;
                            String str = new String("Invalid Class-Path entry: ");
                        }
                        logger.warning(strConcat);
                    }
                }
            }
            return builder.build();
        }

        @VisibleForTesting
        static URI getClassPathEntry(File jarFile, String path) throws URISyntaxException {
            URI uri = new URI(path);
            if (uri.isAbsolute()) {
                return uri;
            }
            return new File(jarFile.getParentFile(), path.replace('/', File.separatorChar)).toURI();
        }
    }

    @VisibleForTesting
    static String getClassName(String filename) {
        int classNameEnd = filename.length() - ".class".length();
        return filename.substring(0, classNameEnd).replace('/', '.');
    }
}
