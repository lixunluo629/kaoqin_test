package org.springframework.boot.context.embedded.tomcat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.JarScannerCallback;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/SkipPatternJarScanner.class */
class SkipPatternJarScanner extends StandardJarScanner {
    private static final String JAR_SCAN_FILTER_CLASS = "org.apache.tomcat.JarScanFilter";
    private final JarScanner jarScanner;
    private final Set<String> patterns;

    SkipPatternJarScanner(JarScanner jarScanner, Set<String> patterns) {
        Assert.notNull(jarScanner, "JarScanner must not be null");
        Assert.notNull(patterns, "Patterns must not be null");
        this.jarScanner = jarScanner;
        this.patterns = patterns;
        setPatternToTomcat8SkipFilter();
    }

    private void setPatternToTomcat8SkipFilter() {
        if (ClassUtils.isPresent(JAR_SCAN_FILTER_CLASS, null)) {
            new Tomcat8TldSkipSetter(this).setSkipPattern(this.patterns);
        }
    }

    public void scan(ServletContext context, ClassLoader classloader, JarScannerCallback callback, Set<String> jarsToSkip) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method scanMethod = ReflectionUtils.findMethod(this.jarScanner.getClass(), "scan", ServletContext.class, ClassLoader.class, JarScannerCallback.class, Set.class);
        Assert.notNull(scanMethod, "Unable to find scan method");
        try {
            JarScanner jarScanner = this.jarScanner;
            Object[] objArr = new Object[4];
            objArr[0] = context;
            objArr[1] = classloader;
            objArr[2] = callback;
            objArr[3] = jarsToSkip != null ? jarsToSkip : this.patterns;
            scanMethod.invoke(jarScanner, objArr);
        } catch (Exception ex) {
            throw new IllegalStateException("Tomcat 7 reflection failed", ex);
        }
    }

    static void apply(TomcatEmbeddedContext context, Set<String> patterns) {
        SkipPatternJarScanner scanner = new SkipPatternJarScanner(context.getJarScanner(), patterns);
        context.setJarScanner(scanner);
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/tomcat/SkipPatternJarScanner$Tomcat8TldSkipSetter.class */
    private static class Tomcat8TldSkipSetter {
        private final StandardJarScanner jarScanner;

        Tomcat8TldSkipSetter(StandardJarScanner jarScanner) {
            this.jarScanner = jarScanner;
        }

        public void setSkipPattern(Set<String> patterns) {
            StandardJarScanFilter filter = new StandardJarScanFilter();
            filter.setTldSkip(StringUtils.collectionToCommaDelimitedString(patterns));
            this.jarScanner.setJarScanFilter(filter);
        }
    }
}
