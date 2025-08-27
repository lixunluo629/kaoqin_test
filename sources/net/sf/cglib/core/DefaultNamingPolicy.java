package net.sf.cglib.core;

import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.util.ClassUtils;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/DefaultNamingPolicy.class */
public class DefaultNamingPolicy implements NamingPolicy {
    public static final DefaultNamingPolicy INSTANCE = new DefaultNamingPolicy();

    @Override // net.sf.cglib.core.NamingPolicy
    public String getClassName(String prefix, String source, Object key, Predicate names) {
        if (prefix == null) {
            prefix = "net.sf.cglib.empty.Object";
        } else if (prefix.startsWith("java")) {
            prefix = new StringBuffer().append(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX).append(prefix).toString();
        }
        String base = new StringBuffer().append(prefix).append(ClassUtils.CGLIB_CLASS_SEPARATOR).append(source.substring(source.lastIndexOf(46) + 1)).append(getTag()).append(ClassUtils.CGLIB_CLASS_SEPARATOR).append(Integer.toHexString(key.hashCode())).toString();
        String attempt = base;
        int index = 2;
        while (names.evaluate(attempt)) {
            int i = index;
            index++;
            attempt = new StringBuffer().append(base).append("_").append(i).toString();
        }
        return attempt;
    }

    protected String getTag() {
        return "ByCGLIB";
    }

    public int hashCode() {
        return getTag().hashCode();
    }

    @Override // net.sf.cglib.core.NamingPolicy
    public boolean equals(Object o) {
        return (o instanceof DefaultNamingPolicy) && ((DefaultNamingPolicy) o).getTag().equals(getTag());
    }
}
