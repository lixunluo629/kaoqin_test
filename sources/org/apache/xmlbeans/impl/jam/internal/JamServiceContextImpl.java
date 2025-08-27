package org.apache.xmlbeans.impl.jam.internal;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.xmlbeans.impl.jam.JamClassLoader;
import org.apache.xmlbeans.impl.jam.JamServiceParams;
import org.apache.xmlbeans.impl.jam.annotation.AnnotationProxy;
import org.apache.xmlbeans.impl.jam.annotation.DefaultAnnotationProxy;
import org.apache.xmlbeans.impl.jam.annotation.JavadocTagParser;
import org.apache.xmlbeans.impl.jam.annotation.WhitespaceDelimitedTagParser;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;
import org.apache.xmlbeans.impl.jam.provider.CompositeJamClassBuilder;
import org.apache.xmlbeans.impl.jam.provider.JamClassBuilder;
import org.apache.xmlbeans.impl.jam.provider.JamLogger;
import org.apache.xmlbeans.impl.jam.provider.JamServiceContext;
import org.apache.xmlbeans.impl.jam.provider.ResourcePath;
import org.apache.xmlbeans.impl.jam.visitor.CompositeMVisitor;
import org.apache.xmlbeans.impl.jam.visitor.MVisitor;
import org.apache.xmlbeans.impl.jam.visitor.PropertyInitializer;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/JamServiceContextImpl.class */
public class JamServiceContextImpl extends JamLoggerImpl implements JamServiceContext, JamServiceParams, ElementContext {
    private static final char INNER_CLASS_SEPARATOR = '$';
    private boolean m14WarningsEnabled = false;
    private Properties mProperties = null;
    private Map mSourceRoot2Scanner = null;
    private Map mClassRoot2Scanner = null;
    private List mClasspath = null;
    private List mSourcepath = null;
    private List mToolClasspath = null;
    private List mIncludeClasses = null;
    private List mExcludeClasses = null;
    private boolean mUseSystemClasspath = true;
    private JavadocTagParser mTagParser = null;
    private MVisitor mCommentInitializer = null;
    private MVisitor mPropertyInitializer = new PropertyInitializer();
    private List mOtherInitializers = null;
    private List mUnstructuredSourceFiles = null;
    private List mClassLoaders = null;
    private List mBaseBuilders = null;
    private JamClassLoader mLoader = null;
    private static final String PREFIX = "[JamServiceContextImpl] ";

    public void setClassLoader(JamClassLoader loader) {
        this.mLoader = loader;
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamServiceContext
    public JamClassBuilder getBaseBuilder() {
        if (this.mBaseBuilders == null || this.mBaseBuilders.size() == 0) {
            return null;
        }
        if (this.mBaseBuilders.size() == 1) {
            return (JamClassBuilder) this.mBaseBuilders.get(0);
        }
        JamClassBuilder[] comp = new JamClassBuilder[this.mBaseBuilders.size()];
        this.mBaseBuilders.toArray(comp);
        return new CompositeJamClassBuilder(comp);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamServiceContext
    public JavadocTagParser getTagParser() {
        if (this.mTagParser == null) {
            this.mTagParser = new WhitespaceDelimitedTagParser();
            this.mTagParser.init(this);
        }
        return this.mTagParser;
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamServiceContext
    public String[] getAllClassnames() throws IllegalStateException, IOException {
        Set all = new HashSet();
        if (this.mIncludeClasses != null) {
            all.addAll(this.mIncludeClasses);
        }
        Iterator i = getAllDirectoryScanners();
        while (i.hasNext()) {
            DirectoryScanner ds = (DirectoryScanner) i.next();
            String[] files = ds.getIncludedFiles();
            for (int j = 0; j < files.length; j++) {
                if (files[j].indexOf(36) == -1) {
                    all.add(filename2classname(files[j]));
                }
            }
        }
        if (this.mExcludeClasses != null) {
            all.removeAll(this.mExcludeClasses);
        }
        String[] out = new String[all.size()];
        all.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamServiceContext, org.apache.xmlbeans.impl.jam.internal.elements.ElementContext
    public JamLogger getLogger() {
        return this;
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamServiceContext
    public File[] getSourceFiles() throws IllegalStateException, IOException {
        Set set = new HashSet();
        if (this.mSourceRoot2Scanner != null) {
            for (DirectoryScanner ds : this.mSourceRoot2Scanner.values()) {
                if (isVerbose(this)) {
                    verbose("[JamServiceContextImpl]  checking scanner for dir" + ds.getRoot());
                }
                String[] files = ds.getIncludedFiles();
                for (int j = 0; j < files.length; j++) {
                    if (isVerbose(this)) {
                        verbose("[JamServiceContextImpl]  ...including a source file " + files[j]);
                    }
                    set.add(new File(ds.getRoot(), files[j]));
                }
            }
        }
        if (this.mUnstructuredSourceFiles != null) {
            if (isVerbose(this)) {
                verbose("[JamServiceContextImpl] adding " + this.mUnstructuredSourceFiles.size() + " other source files");
            }
            set.addAll(this.mUnstructuredSourceFiles);
        }
        File[] out = new File[set.size()];
        set.toArray(out);
        return out;
    }

    public File[] getUnstructuredSourceFiles() {
        if (this.mUnstructuredSourceFiles == null) {
            return null;
        }
        File[] out = new File[this.mUnstructuredSourceFiles.size()];
        this.mUnstructuredSourceFiles.toArray(out);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamServiceContext
    public ResourcePath getInputClasspath() {
        return createJPath(this.mClasspath);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamServiceContext
    public ResourcePath getInputSourcepath() {
        return createJPath(this.mSourcepath);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamServiceContext
    public ResourcePath getToolClasspath() {
        return createJPath(this.mToolClasspath);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamServiceContext
    public String getProperty(String name) {
        if (this.mProperties == null) {
            return null;
        }
        return this.mProperties.getProperty(name);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamServiceContext
    public MVisitor getInitializer() {
        List initers = new ArrayList();
        if (this.mCommentInitializer != null) {
            initers.add(this.mCommentInitializer);
        }
        if (this.mPropertyInitializer != null) {
            initers.add(this.mPropertyInitializer);
        }
        if (this.mOtherInitializers != null) {
            initers.addAll(this.mOtherInitializers);
        }
        MVisitor[] inits = new MVisitor[initers.size()];
        initers.toArray(inits);
        return new CompositeMVisitor(inits);
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void addClassBuilder(JamClassBuilder builder) {
        if (this.mBaseBuilders == null) {
            this.mBaseBuilders = new ArrayList();
        }
        this.mBaseBuilders.add(builder);
    }

    public void setCommentInitializer(MVisitor initializer) {
        this.mCommentInitializer = initializer;
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void setPropertyInitializer(MVisitor initializer) {
        this.mPropertyInitializer = initializer;
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void addInitializer(MVisitor initializer) {
        if (this.mOtherInitializers == null) {
            this.mOtherInitializers = new ArrayList();
        }
        this.mOtherInitializers.add(initializer);
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void setJavadocTagParser(JavadocTagParser tp) {
        this.mTagParser = tp;
        tp.init(this);
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void includeSourceFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("null file");
        }
        File file2 = file.getAbsoluteFile();
        if (isVerbose(this)) {
            verbose("[JamServiceContextImpl] adding source ");
        }
        if (!file2.exists()) {
            throw new IllegalArgumentException(file2 + " does not exist");
        }
        if (file2.isDirectory()) {
            throw new IllegalArgumentException(file2 + " cannot be included as a source file because it is a directory.");
        }
        if (this.mUnstructuredSourceFiles == null) {
            this.mUnstructuredSourceFiles = new ArrayList();
        }
        this.mUnstructuredSourceFiles.add(file2.getAbsoluteFile());
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void includeSourcePattern(File[] sourcepath, String pattern) {
        if (sourcepath == null) {
            throw new IllegalArgumentException("null sourcepath");
        }
        if (sourcepath.length == 0) {
            throw new IllegalArgumentException("empty sourcepath");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("null pattern");
        }
        String pattern2 = pattern.trim();
        if (pattern2.length() == 0) {
            throw new IllegalArgumentException("empty pattern");
        }
        for (int i = 0; i < sourcepath.length; i++) {
            if (isVerbose(this)) {
                verbose("[JamServiceContextImpl] including '" + pattern2 + "' under " + sourcepath[i]);
            }
            addSourcepath(sourcepath[i]);
            getSourceScanner(sourcepath[i]).include(pattern2);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void includeClassPattern(File[] classpath, String pattern) {
        if (classpath == null) {
            throw new IllegalArgumentException("null classpath");
        }
        if (classpath.length == 0) {
            throw new IllegalArgumentException("empty classpath");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("null pattern");
        }
        String pattern2 = pattern.trim();
        if (pattern2.length() == 0) {
            throw new IllegalArgumentException("empty pattern");
        }
        for (int i = 0; i < classpath.length; i++) {
            if (isVerbose(this)) {
                verbose("[JamServiceContextImpl] including '" + pattern2 + "' under " + classpath[i]);
            }
            addClasspath(classpath[i]);
            getClassScanner(classpath[i]).include(pattern2);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void excludeSourcePattern(File[] sourcepath, String pattern) {
        if (sourcepath == null) {
            throw new IllegalArgumentException("null sourcepath");
        }
        if (sourcepath.length == 0) {
            throw new IllegalArgumentException("empty sourcepath");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("null pattern");
        }
        String pattern2 = pattern.trim();
        if (pattern2.length() == 0) {
            throw new IllegalArgumentException("empty pattern");
        }
        for (int i = 0; i < sourcepath.length; i++) {
            if (isVerbose(this)) {
                verbose("[JamServiceContextImpl] EXCLUDING '" + pattern2 + "' under " + sourcepath[i]);
            }
            addSourcepath(sourcepath[i]);
            getSourceScanner(sourcepath[i]).exclude(pattern2);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void excludeClassPattern(File[] classpath, String pattern) {
        if (classpath == null) {
            throw new IllegalArgumentException("null classpath");
        }
        if (classpath.length == 0) {
            throw new IllegalArgumentException("empty classpath");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("null pattern");
        }
        String pattern2 = pattern.trim();
        if (pattern2.length() == 0) {
            throw new IllegalArgumentException("empty pattern");
        }
        for (int i = 0; i < classpath.length; i++) {
            if (isVerbose(this)) {
                verbose("[JamServiceContextImpl] EXCLUDING '" + pattern2 + "' under " + classpath[i]);
            }
            addClasspath(classpath[i]);
            getClassScanner(classpath[i]).exclude(pattern2);
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void includeSourceFile(File[] sourcepath, File sourceFile) {
        File root = getPathRootForFile(sourcepath, sourceFile);
        includeSourcePattern(new File[]{root}, source2pattern(root, sourceFile));
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void excludeSourceFile(File[] sourcepath, File sourceFile) {
        File root = getPathRootForFile(sourcepath, sourceFile);
        excludeSourcePattern(new File[]{root}, source2pattern(root, sourceFile));
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void includeClassFile(File[] classpath, File classFile) {
        File root = getPathRootForFile(classpath, classFile);
        includeClassPattern(new File[]{root}, source2pattern(root, classFile));
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void excludeClassFile(File[] classpath, File classFile) {
        File root = getPathRootForFile(classpath, classFile);
        excludeClassPattern(new File[]{root}, source2pattern(root, classFile));
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void includeClass(String qualifiedClassname) {
        if (this.mIncludeClasses == null) {
            this.mIncludeClasses = new ArrayList();
        }
        this.mIncludeClasses.add(qualifiedClassname);
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void excludeClass(String qualifiedClassname) {
        if (this.mExcludeClasses == null) {
            this.mExcludeClasses = new ArrayList();
        }
        this.mExcludeClasses.add(qualifiedClassname);
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void addClasspath(File classpathElement) {
        if (this.mClasspath == null) {
            this.mClasspath = new ArrayList();
        } else if (this.mClasspath.contains(classpathElement)) {
            return;
        }
        this.mClasspath.add(classpathElement);
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void setLoggerWriter(PrintWriter out) {
        super.setOut(out);
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void setJamLogger(JamLogger logger) {
        throw new IllegalStateException("NYI");
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void addSourcepath(File sourcepathElement) {
        if (this.mSourcepath == null) {
            this.mSourcepath = new ArrayList();
        } else if (this.mSourcepath.contains(sourcepathElement)) {
            return;
        }
        this.mSourcepath.add(sourcepathElement);
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void addToolClasspath(File classpathElement) {
        if (this.mToolClasspath == null) {
            this.mToolClasspath = new ArrayList();
        } else if (this.mToolClasspath.contains(classpathElement)) {
            return;
        }
        this.mToolClasspath.add(classpathElement);
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void setProperty(String name, String value) {
        if (this.mProperties == null) {
            this.mProperties = new Properties();
        }
        this.mProperties.setProperty(name, value);
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void set14WarningsEnabled(boolean b) {
        this.m14WarningsEnabled = b;
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void setParentClassLoader(JamClassLoader loader) {
        throw new IllegalStateException("NYI");
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void setUseSystemClasspath(boolean use) {
        this.mUseSystemClasspath = use;
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceParams
    public void addClassLoader(ClassLoader cl) {
        if (this.mClassLoaders == null) {
            this.mClassLoaders = new ArrayList();
        }
        this.mClassLoaders.add(cl);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamServiceContext
    public ClassLoader[] getReflectionClassLoaders() {
        if (this.mClassLoaders == null) {
            if (this.mUseSystemClasspath) {
                return new ClassLoader[]{ClassLoader.getSystemClassLoader()};
            }
            return new ClassLoader[0];
        }
        ClassLoader[] out = new ClassLoader[this.mClassLoaders.size() + (this.mUseSystemClasspath ? 1 : 0)];
        for (int i = 0; i < this.mClassLoaders.size(); i++) {
            out[i] = (ClassLoader) this.mClassLoaders.get(i);
        }
        if (this.mUseSystemClasspath) {
            out[out.length - 1] = ClassLoader.getSystemClassLoader();
        }
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamServiceContext
    public boolean is14WarningsEnabled() {
        return this.m14WarningsEnabled;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.ElementContext
    public JamClassLoader getClassLoader() {
        return this.mLoader;
    }

    @Override // org.apache.xmlbeans.impl.jam.internal.elements.ElementContext
    public AnnotationProxy createAnnotationProxy(String jsr175typename) {
        AnnotationProxy out = new DefaultAnnotationProxy();
        out.init(this);
        return out;
    }

    private File getPathRootForFile(File[] sourcepath, File sourceFile) {
        if (sourcepath == null) {
            throw new IllegalArgumentException("null sourcepath");
        }
        if (sourcepath.length == 0) {
            throw new IllegalArgumentException("empty sourcepath");
        }
        if (sourceFile == null) {
            throw new IllegalArgumentException("null sourceFile");
        }
        File sourceFile2 = sourceFile.getAbsoluteFile();
        if (isVerbose(this)) {
            verbose("[JamServiceContextImpl] Getting root for " + sourceFile2 + "...");
        }
        for (int i = 0; i < sourcepath.length; i++) {
            if (isVerbose(this)) {
                verbose("[JamServiceContextImpl] ...looking in " + sourcepath[i]);
            }
            if (isContainingDir(sourcepath[i].getAbsoluteFile(), sourceFile2)) {
                if (isVerbose(this)) {
                    verbose("[JamServiceContextImpl] ...found it!");
                }
                return sourcepath[i].getAbsoluteFile();
            }
        }
        throw new IllegalArgumentException(sourceFile2 + " is not in the given path.");
    }

    private boolean isContainingDir(File dir, File file) {
        if (isVerbose(this)) {
            verbose("[JamServiceContextImpl] ... ...isContainingDir " + dir + "  " + file);
        }
        if (file == null) {
            return false;
        }
        if (dir.equals(file)) {
            if (isVerbose(this)) {
                verbose("[JamServiceContextImpl] ... ...yes!");
                return true;
            }
            return true;
        }
        return isContainingDir(dir, file.getParentFile());
    }

    private String source2pattern(File root, File sourceFile) {
        if (isVerbose(this)) {
            verbose("[JamServiceContextImpl] source2pattern " + root + "  " + sourceFile);
        }
        String r = root.getAbsolutePath();
        String s = sourceFile.getAbsolutePath();
        if (isVerbose(this)) {
            verbose("[JamServiceContextImpl] source2pattern returning " + s.substring(r.length() + 1));
        }
        return s.substring(r.length() + 1);
    }

    private static String filename2classname(String filename) {
        int extDot = filename.lastIndexOf(46);
        if (extDot != -1) {
            filename = filename.substring(0, extDot);
        }
        return filename.replace('/', '.').replace('\\', '.');
    }

    private Iterator getAllDirectoryScanners() {
        Collection out = new ArrayList();
        if (this.mSourceRoot2Scanner != null) {
            out.addAll(this.mSourceRoot2Scanner.values());
        }
        if (this.mClassRoot2Scanner != null) {
            out.addAll(this.mClassRoot2Scanner.values());
        }
        return out.iterator();
    }

    private static ResourcePath createJPath(Collection filelist) {
        if (filelist == null || filelist.size() == 0) {
            return null;
        }
        File[] files = new File[filelist.size()];
        filelist.toArray(files);
        return ResourcePath.forFiles(files);
    }

    private DirectoryScanner getSourceScanner(File srcRoot) {
        if (this.mSourceRoot2Scanner == null) {
            this.mSourceRoot2Scanner = new HashMap();
        }
        DirectoryScanner out = (DirectoryScanner) this.mSourceRoot2Scanner.get(srcRoot);
        if (out == null) {
            Map map = this.mSourceRoot2Scanner;
            DirectoryScanner directoryScanner = new DirectoryScanner(srcRoot, this);
            out = directoryScanner;
            map.put(srcRoot, directoryScanner);
        }
        return out;
    }

    private DirectoryScanner getClassScanner(File clsRoot) {
        if (this.mClassRoot2Scanner == null) {
            this.mClassRoot2Scanner = new HashMap();
        }
        DirectoryScanner out = (DirectoryScanner) this.mClassRoot2Scanner.get(clsRoot);
        if (out == null) {
            Map map = this.mClassRoot2Scanner;
            DirectoryScanner directoryScanner = new DirectoryScanner(clsRoot, this);
            out = directoryScanner;
            map.put(clsRoot, directoryScanner);
        }
        return out;
    }
}
