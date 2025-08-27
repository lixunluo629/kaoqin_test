package org.springframework.boot.context.embedded.tomcat;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import javax.naming.directory.DirContext;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ResourceUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/TomcatResources.class */
abstract class TomcatResources {
    private final Context context;

    protected abstract void addJar(String str);

    protected abstract void addDir(String str, URL url);

    TomcatResources(Context context) {
        this.context = context;
    }

    void addResourceJars(List<URL> resourceJarUrls) {
        for (URL url : resourceJarUrls) {
            try {
                String path = url.getPath();
                if (path.endsWith(".jar") || path.endsWith(".jar!/")) {
                    String jar = url.toString();
                    if (!jar.startsWith(ResourceUtils.JAR_URL_PREFIX)) {
                        jar = ResourceUtils.JAR_URL_PREFIX + jar + ResourceUtils.JAR_URL_SEPARATOR;
                    }
                    addJar(jar);
                } else if ("jar".equals(url.getProtocol())) {
                    addJar(url.toString());
                } else {
                    addDir(new File(url.toURI()).getAbsolutePath(), url);
                }
            } catch (URISyntaxException e) {
                throw new IllegalStateException("Failed to create File from URL '" + url + "'");
            }
        }
    }

    protected final Context getContext() {
        return this.context;
    }

    public static TomcatResources get(Context context) {
        if (ClassUtils.isPresent("org.apache.catalina.deploy.ErrorPage", null)) {
            return new Tomcat7Resources(context);
        }
        return new Tomcat8Resources(context);
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/TomcatResources$Tomcat7Resources.class */
    private static class Tomcat7Resources extends TomcatResources {
        private final Method addResourceJarUrlMethod;

        Tomcat7Resources(Context context) {
            super(context);
            this.addResourceJarUrlMethod = ReflectionUtils.findMethod(context.getClass(), "addResourceJarUrl", URL.class);
        }

        @Override // org.springframework.boot.context.embedded.tomcat.TomcatResources
        protected void addJar(String jar) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            URL url = getJarUrl(jar);
            if (url != null) {
                try {
                    this.addResourceJarUrlMethod.invoke(getContext(), url);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex);
                }
            }
        }

        private URL getJarUrl(String jar) {
            try {
                return new URL(jar);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        @Override // org.springframework.boot.context.embedded.tomcat.TomcatResources
        protected void addDir(String dir, URL url) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
            if (getContext() instanceof StandardContext) {
                try {
                    Class<?> fileDirContextClass = Class.forName("org.apache.naming.resources.FileDirContext");
                    Method setDocBaseMethod = ReflectionUtils.findMethod(fileDirContextClass, "setDocBase", String.class);
                    Object fileDirContext = fileDirContextClass.newInstance();
                    setDocBaseMethod.invoke(fileDirContext, dir);
                    Method addResourcesDirContextMethod = ReflectionUtils.findMethod(StandardContext.class, "addResourcesDirContext", DirContext.class);
                    addResourcesDirContextMethod.invoke(getContext(), fileDirContext);
                } catch (Exception ex) {
                    throw new IllegalStateException("Tomcat 7 reflection failed", ex);
                }
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/TomcatResources$Tomcat8Resources.class */
    static class Tomcat8Resources extends TomcatResources {
        Tomcat8Resources(Context context) {
            super(context);
        }

        @Override // org.springframework.boot.context.embedded.tomcat.TomcatResources
        protected void addJar(String jar) {
            addResourceSet(jar);
        }

        @Override // org.springframework.boot.context.embedded.tomcat.TomcatResources
        protected void addDir(String dir, URL url) {
            addResourceSet(url.toString());
        }

        private void addResourceSet(String resource) {
            try {
                if (isInsideNestedJar(resource)) {
                    resource = resource.substring(0, resource.length() - 2);
                }
                URL url = new URL(resource);
                getContext().getResources().createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/", url, "/META-INF/resources");
            } catch (Exception e) {
            }
        }

        private boolean isInsideNestedJar(String dir) {
            return dir.indexOf(ResourceUtils.JAR_URL_SEPARATOR) < dir.lastIndexOf(ResourceUtils.JAR_URL_SEPARATOR);
        }
    }
}
