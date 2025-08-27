package org.apache.xmlbeans.impl.jam.provider;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.impl.jam.JamClassLoader;
import org.apache.xmlbeans.impl.jam.JamService;
import org.apache.xmlbeans.impl.jam.JamServiceFactory;
import org.apache.xmlbeans.impl.jam.JamServiceParams;
import org.apache.xmlbeans.impl.jam.internal.JamClassLoaderImpl;
import org.apache.xmlbeans.impl.jam.internal.JamServiceContextImpl;
import org.apache.xmlbeans.impl.jam.internal.JamServiceImpl;
import org.apache.xmlbeans.impl.jam.internal.elements.ElementContext;
import org.apache.xmlbeans.impl.jam.internal.javadoc.JavadocClassBuilder;
import org.apache.xmlbeans.impl.jam.internal.parser.ParserClassBuilder;
import org.apache.xmlbeans.impl.jam.internal.reflect.ReflectClassBuilder;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/jam/provider/JamServiceFactoryImpl.class */
public class JamServiceFactoryImpl extends JamServiceFactory {
    public static final String USE_NEW_PARSER = "JamServiceFactoryImpl.use-new-parser";
    private static final String PREFIX = "[JamServiceFactoryImpl]";

    @Override // org.apache.xmlbeans.impl.jam.JamServiceFactory
    public JamServiceParams createServiceParams() {
        return new JamServiceContextImpl();
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceFactory
    public JamService createService(JamServiceParams jsps) throws IOException {
        if (!(jsps instanceof JamServiceContextImpl)) {
            throw new IllegalArgumentException("JamServiceParams must be instantiated by this JamServiceFactory.");
        }
        JamClassLoader clToUse = createClassLoader((JamServiceContextImpl) jsps);
        ((JamServiceContextImpl) jsps).setClassLoader(clToUse);
        return new JamServiceImpl((ElementContext) jsps, getSpecifiedClasses((JamServiceContextImpl) jsps));
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceFactory
    public JamClassLoader createSystemJamClassLoader() {
        JamServiceParams params = createServiceParams();
        params.setUseSystemClasspath(true);
        try {
            JamService service = createService(params);
            return service.getClassLoader();
        } catch (IOException reallyUnexpected) {
            reallyUnexpected.printStackTrace();
            throw new IllegalStateException(reallyUnexpected.getMessage());
        }
    }

    @Override // org.apache.xmlbeans.impl.jam.JamServiceFactory
    public JamClassLoader createJamClassLoader(ClassLoader cl) {
        JamServiceParams params = createServiceParams();
        params.setUseSystemClasspath(false);
        params.setPropertyInitializer(null);
        params.addClassLoader(cl);
        try {
            JamService service = createService(params);
            return service.getClassLoader();
        } catch (IOException reallyUnexpected) {
            reallyUnexpected.printStackTrace();
            throw new IllegalStateException(reallyUnexpected.getMessage());
        }
    }

    protected String[] getSpecifiedClasses(JamServiceContext params) throws IOException {
        return params.getAllClassnames();
    }

    protected JamClassLoader createClassLoader(JamServiceContext ctx) throws IOException {
        JamClassBuilder builder = createBuilder(ctx);
        return new JamClassLoaderImpl((ElementContext) ctx, builder, ctx.getInitializer());
    }

    protected JamClassBuilder createBuilder(JamServiceContext ctx) throws IOException {
        JamLogger log = ctx.getLogger();
        List builders = new ArrayList();
        JamClassBuilder b = ctx.getBaseBuilder();
        if (b != null) {
            builders.add(b);
        }
        JamClassBuilder b2 = createSourceBuilder(ctx);
        if (log.isVerbose(this)) {
            log.verbose("added classbuilder for sources");
        }
        if (b2 != null) {
            builders.add(b2);
        }
        JamClassBuilder b3 = createClassfileBuilder(ctx);
        if (log.isVerbose(this)) {
            log.verbose("added classbuilder for custom classpath");
        }
        if (b3 != null) {
            builders.add(b3);
        }
        ClassLoader[] cls = ctx.getReflectionClassLoaders();
        for (int i = 0; i < cls.length; i++) {
            if (log.isVerbose(this)) {
                log.verbose("added classbuilder for classloader " + cls[i].getClass());
            }
            builders.add(new ReflectClassBuilder(cls[i]));
        }
        JamClassBuilder[] barray = new JamClassBuilder[builders.size()];
        builders.toArray(barray);
        JamClassBuilder out = new CompositeJamClassBuilder(barray);
        out.init((ElementContext) ctx);
        if (log.isVerbose(this)) {
            log.verbose("returning a composite of " + barray.length + " class builders.");
            out.build("java.lang", "Object");
            out.build("javax.ejb", "SessionBean");
        }
        return out;
    }

    protected JamClassBuilder createSourceBuilder(JamServiceContext ctx) throws IOException {
        File[] sources = ctx.getSourceFiles();
        if (sources == null || sources.length == 0) {
            if (ctx.isVerbose(this)) {
                ctx.verbose("[JamServiceFactoryImpl]no source files present, skipping source ClassBuilder");
                return null;
            }
            return null;
        }
        if (ctx.getProperty(USE_NEW_PARSER) == null) {
            return new JavadocClassBuilder();
        }
        return new ParserClassBuilder(ctx);
    }

    protected JamClassBuilder createClassfileBuilder(JamServiceContext jp) throws IOException {
        ResourcePath cp = jp.getInputClasspath();
        if (cp == null) {
            return null;
        }
        URL[] urls = cp.toUrlPath();
        ClassLoader cl = new URLClassLoader(urls);
        return new ReflectClassBuilder(cl);
    }
}
