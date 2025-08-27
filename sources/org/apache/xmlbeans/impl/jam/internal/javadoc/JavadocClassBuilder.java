package org.apache.xmlbeans.impl.jam.internal.javadoc;

import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.SourcePosition;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.xmlbeans.impl.jam.annotation.JavadocTagParser;
import org.apache.xmlbeans.impl.jam.internal.JamServiceContextImpl;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;
import org.apache.xmlbeans.impl.jam.internal.elements.PrimitiveClassImpl;
import org.apache.xmlbeans.impl.jam.mutable.MAnnotatedElement;
import org.apache.xmlbeans.impl.jam.mutable.MClass;
import org.apache.xmlbeans.impl.jam.mutable.MElement;
import org.apache.xmlbeans.impl.jam.mutable.MField;
import org.apache.xmlbeans.impl.jam.mutable.MInvokable;
import org.apache.xmlbeans.impl.jam.mutable.MMethod;
import org.apache.xmlbeans.impl.jam.mutable.MParameter;
import org.apache.xmlbeans.impl.jam.mutable.MSourcePosition;
import org.apache.xmlbeans.impl.jam.provider.JamClassBuilder;
import org.apache.xmlbeans.impl.jam.provider.JamClassPopulator;
import org.apache.xmlbeans.impl.jam.provider.JamServiceContext;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/internal/javadoc/JavadocClassBuilder.class */
public class JavadocClassBuilder extends JamClassBuilder implements JamClassPopulator {
    public static final String ARGS_PROPERTY = "javadoc.args";
    public static final String PARSETAGS_PROPERTY = "javadoc.parsetags";
    private RootDoc mRootDoc = null;
    private JavadocTigerDelegate mTigerDelegate = null;
    private JavadocTagParser mTagParser = null;
    private boolean mParseTags = true;

    @Override // org.apache.xmlbeans.impl.jam.provider.JamClassBuilder
    public void init(ElementContext ctx) throws ClassNotFoundException {
        if (ctx == null) {
            throw new IllegalArgumentException("null context");
        }
        super.init(ctx);
        getLogger().verbose("init()", this);
        initDelegate(ctx);
        initJavadoc((JamServiceContext) ctx);
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamClassBuilder
    public MClass build(String packageName, String className) {
        assertInitialized();
        if (getLogger().isVerbose(this)) {
            getLogger().verbose("trying to build '" + packageName + "' '" + className + "'");
        }
        String loadme = packageName.trim().length() > 0 ? packageName + '.' + className : className;
        ClassDoc cd = this.mRootDoc.classNamed(loadme);
        if (cd == null) {
            if (getLogger().isVerbose(this)) {
                getLogger().verbose("no ClassDoc for " + loadme);
                return null;
            }
            return null;
        }
        List importSpecs = null;
        Type[] typeArrImportedClasses = cd.importedClasses();
        if (typeArrImportedClasses != null) {
            importSpecs = new ArrayList();
            for (Type type : typeArrImportedClasses) {
                importSpecs.add(getFdFor(type));
            }
        }
        PackageDoc[] imported = cd.importedPackages();
        if (imported != null) {
            if (importSpecs == null) {
                importSpecs = new ArrayList();
            }
            for (PackageDoc packageDoc : imported) {
                importSpecs.add(packageDoc.name() + ".*");
            }
        }
        String[] importSpecsArray = null;
        if (importSpecs != null) {
            importSpecsArray = new String[importSpecs.size()];
            importSpecs.toArray(importSpecsArray);
        }
        MClass out = createClassToBuild(packageName, className, importSpecsArray, this);
        out.setArtifact(cd);
        return out;
    }

    @Override // org.apache.xmlbeans.impl.jam.provider.JamClassPopulator
    public void populate(MClass dest) {
        if (dest == null) {
            throw new IllegalArgumentException("null dest");
        }
        assertInitialized();
        ClassDoc src = (ClassDoc) dest.getArtifact();
        if (src == null) {
            throw new IllegalStateException("null artifact");
        }
        dest.setModifiers(src.modifierSpecifier());
        dest.setIsInterface(src.isInterface());
        if (this.mTigerDelegate != null) {
            dest.setIsEnumType(this.mTigerDelegate.isEnum(src));
        }
        ClassDoc s = src.superclass();
        if (s != null) {
            dest.setSuperclass(getFdFor(s));
        }
        for (Type type : src.interfaces()) {
            dest.addInterface(getFdFor(type));
        }
        FieldDoc[] fields = src.fields();
        for (FieldDoc fieldDoc : fields) {
            populate(dest.addNewField(), fieldDoc);
        }
        for (ExecutableMemberDoc executableMemberDoc : src.constructors()) {
            populate(dest.addNewConstructor(), executableMemberDoc);
        }
        MethodDoc[] methods = src.methods();
        for (MethodDoc methodDoc : methods) {
            populate(dest.addNewMethod(), methodDoc);
        }
        if (this.mTigerDelegate != null) {
            this.mTigerDelegate.populateAnnotationTypeIfNecessary(src, dest, this);
        }
        addAnnotations(dest, src);
        addSourcePosition((MElement) dest, (Doc) src);
        ClassDoc[] inners = src.innerClasses();
        if (inners != null) {
            for (int i = 0; i < inners.length; i++) {
                MClass inner = dest.addNewInnerClass(inners[i].typeName());
                inner.setArtifact(inners[i]);
                populate(inner);
            }
        }
    }

    public MMethod addMethod(MClass dest, MethodDoc doc) {
        MMethod out = dest.addNewMethod();
        populate(out, doc);
        return out;
    }

    private void initDelegate(ElementContext ctx) {
        this.mTigerDelegate = JavadocTigerDelegate.create(ctx);
    }

    private void initJavadoc(JamServiceContext serviceContext) throws ClassNotFoundException {
        this.mTagParser = serviceContext.getTagParser();
        String pct = serviceContext.getProperty(PARSETAGS_PROPERTY);
        if (pct != null) {
            this.mParseTags = Boolean.valueOf(pct).booleanValue();
            getLogger().verbose("mParseTags=" + this.mParseTags, this);
        }
        try {
            File[] files = serviceContext.getSourceFiles();
            if (files == null || files.length == 0) {
                throw new IllegalArgumentException("No source files in context.");
            }
            String sourcePath = serviceContext.getInputSourcepath() == null ? null : serviceContext.getInputSourcepath().toString();
            String classPath = serviceContext.getInputClasspath() == null ? null : serviceContext.getInputClasspath().toString();
            if (getLogger().isVerbose(this)) {
                getLogger().verbose("sourcePath =" + sourcePath);
                getLogger().verbose("classPath =" + classPath);
                for (File file : files) {
                    getLogger().verbose("including '" + file + "'");
                }
            }
            JavadocRunner jdr = JavadocRunner.newInstance();
            try {
                PrintWriter out = null;
                if (getLogger().isVerbose(this)) {
                    out = new PrintWriter(System.out);
                }
                this.mRootDoc = jdr.run(files, out, sourcePath, classPath, getJavadocArgs(serviceContext), getLogger());
                if (this.mRootDoc == null) {
                    getLogger().error("Javadoc returned a null root");
                } else {
                    if (getLogger().isVerbose(this)) {
                        getLogger().verbose(" received " + this.mRootDoc.classes().length + " ClassDocs from javadoc: ");
                    }
                    ClassDoc[] classes = this.mRootDoc.classes();
                    for (int i = 0; i < classes.length; i++) {
                        if (classes[i].containingClass() == null) {
                            if (getLogger().isVerbose(this)) {
                                getLogger().verbose("..." + classes[i].qualifiedName());
                            }
                            ((JamServiceContextImpl) serviceContext).includeClass(getFdFor(classes[i]));
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                getLogger().error(e);
            } catch (IOException e2) {
                getLogger().error(e2);
            }
        } catch (IOException ioe) {
            getLogger().error(ioe);
        }
    }

    private void populate(MField dest, FieldDoc src) {
        dest.setArtifact(src);
        dest.setSimpleName(src.name());
        dest.setType(getFdFor(src.type()));
        dest.setModifiers(src.modifierSpecifier());
        addAnnotations(dest, src);
        addSourcePosition((MElement) dest, (Doc) src);
    }

    private void populate(MMethod dest, MethodDoc src) {
        if (dest == null) {
            throw new IllegalArgumentException("null dest");
        }
        if (src == null) {
            throw new IllegalArgumentException("null src");
        }
        populate((MInvokable) dest, (ExecutableMemberDoc) src);
        dest.setReturnType(getFdFor(src.returnType()));
    }

    private void populate(MInvokable dest, ExecutableMemberDoc src) {
        if (dest == null) {
            throw new IllegalArgumentException("null dest");
        }
        if (src == null) {
            throw new IllegalArgumentException("null src");
        }
        dest.setArtifact(src);
        dest.setSimpleName(src.name());
        dest.setModifiers(src.modifierSpecifier());
        for (Type type : src.thrownExceptions()) {
            dest.addException(getFdFor(type));
        }
        Parameter[] params = src.parameters();
        for (Parameter parameter : params) {
            populate(dest.addNewParameter(), src, parameter);
        }
        addAnnotations(dest, src);
        addSourcePosition((MElement) dest, (Doc) src);
    }

    private void populate(MParameter dest, ExecutableMemberDoc method, Parameter src) {
        dest.setArtifact(src);
        dest.setSimpleName(src.name());
        dest.setType(getFdFor(src.type()));
        if (this.mTigerDelegate != null) {
            this.mTigerDelegate.extractAnnotations(dest, method, src);
        }
    }

    private String[] getJavadocArgs(JamServiceContext ctx) {
        String prop = ctx.getProperty(ARGS_PROPERTY);
        if (prop == null) {
            return null;
        }
        StringTokenizer t = new StringTokenizer(prop);
        String[] out = new String[t.countTokens()];
        int i = 0;
        while (t.hasMoreTokens()) {
            int i2 = i;
            i++;
            out[i2] = t.nextToken();
        }
        return out;
    }

    private void addAnnotations(MAnnotatedElement dest, ProgramElementDoc src) {
        String comments = src.commentText();
        if (comments != null) {
            dest.createComment().setText(comments);
        }
        Tag[] tags = src.tags();
        for (int i = 0; i < tags.length; i++) {
            if (getLogger().isVerbose(this)) {
                getLogger().verbose("...'" + tags[i].name() + "' ' " + tags[i].text());
            }
            this.mTagParser.parse(dest, tags[i]);
        }
        if (this.mTigerDelegate != null) {
            this.mTigerDelegate.extractAnnotations(dest, src);
        }
    }

    public static String getFdFor(Type t) {
        if (t == null) {
            throw new IllegalArgumentException("null type");
        }
        String dim = t.dimension();
        if (dim == null || dim.length() == 0) {
            ClassDoc cd = t.asClassDoc();
            if (cd != null) {
                ClassDoc outer = cd.containingClass();
                if (outer == null) {
                    return cd.qualifiedName();
                }
                String simpleName = cd.name();
                return outer.qualifiedName() + '$' + simpleName.substring(simpleName.lastIndexOf(46) + 1);
            }
            return t.qualifiedTypeName();
        }
        StringWriter out = new StringWriter();
        int iL = dim.length() / 2;
        for (int i = 0; i < iL; i++) {
            out.write(PropertyAccessor.PROPERTY_KEY_PREFIX);
        }
        String primFd = PrimitiveClassImpl.getPrimitiveClassForName(t.qualifiedTypeName());
        if (primFd != null) {
            out.write(primFd);
        } else {
            out.write(StandardRoles.L);
            if (t.asClassDoc() != null) {
                out.write(t.asClassDoc().qualifiedName());
            } else {
                out.write(t.qualifiedTypeName());
            }
            out.write(ScriptUtils.DEFAULT_STATEMENT_SEPARATOR);
        }
        return out.toString();
    }

    public static void addSourcePosition(MElement dest, Doc src) {
        SourcePosition pos = src.position();
        if (pos != null) {
            addSourcePosition(dest, pos);
        }
    }

    public static void addSourcePosition(MElement dest, SourcePosition pos) {
        MSourcePosition sp = dest.createSourcePosition();
        sp.setColumn(pos.column());
        sp.setLine(pos.line());
        File f = pos.file();
        if (f != null) {
            sp.setSourceURI(f.toURI());
        }
    }
}
