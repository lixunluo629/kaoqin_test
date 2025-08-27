package org.ehcache.sizeof.filters;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.ehcache.sizeof.annotations.AnnotationProxyFactory;
import org.ehcache.sizeof.annotations.IgnoreSizeOf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/filters/AnnotationSizeOfFilter.class */
public final class AnnotationSizeOfFilter implements SizeOfFilter {
    private static final String IGNORE_SIZE_OF_VM_ARGUMENT = AnnotationSizeOfFilter.class.getName() + ".pattern";
    private static final Logger LOG = LoggerFactory.getLogger(AnnotationSizeOfFilter.class.getName());
    private static final String IGNORE_SIZE_OF_DEFAULT_REGEXP = "^.*cache\\..*IgnoreSizeOf$";
    private static final Pattern IGNORE_SIZE_OF_PATTERN;

    static {
        String ignoreSizeOfRegexpVMArg = System.getProperty(IGNORE_SIZE_OF_VM_ARGUMENT);
        String ignoreSizeOfRegexp = ignoreSizeOfRegexpVMArg != null ? ignoreSizeOfRegexpVMArg : IGNORE_SIZE_OF_DEFAULT_REGEXP;
        try {
            Pattern localPattern = Pattern.compile(ignoreSizeOfRegexp);
            LOG.info("Using regular expression provided through VM argument " + IGNORE_SIZE_OF_VM_ARGUMENT + " for IgnoreSizeOf annotation : " + ignoreSizeOfRegexp);
            IGNORE_SIZE_OF_PATTERN = localPattern;
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("Invalid regular expression provided through VM argument " + IGNORE_SIZE_OF_VM_ARGUMENT + " : \n" + e.getMessage() + "\n using default regular expression for IgnoreSizeOf annotation : " + IGNORE_SIZE_OF_DEFAULT_REGEXP);
        }
    }

    @Override // org.ehcache.sizeof.filters.SizeOfFilter
    public Collection<Field> filterFields(Class<?> klazz, Collection<Field> fields) {
        Iterator<Field> it = fields.iterator();
        while (it.hasNext()) {
            Field field = it.next();
            IgnoreSizeOf annotationOnField = (IgnoreSizeOf) getAnnotationOn(field, IgnoreSizeOf.class, IGNORE_SIZE_OF_PATTERN);
            if (annotationOnField != null) {
                it.remove();
            }
        }
        return fields;
    }

    @Override // org.ehcache.sizeof.filters.SizeOfFilter
    public boolean filterClass(Class<?> klazz) {
        boolean classAnnotated = isAnnotationPresentOrInherited(klazz);
        Package pack = klazz.getPackage();
        IgnoreSizeOf annotationOnPackage = pack == null ? null : (IgnoreSizeOf) getAnnotationOn(pack, IgnoreSizeOf.class, IGNORE_SIZE_OF_PATTERN);
        boolean packageAnnotated = annotationOnPackage != null;
        return (classAnnotated || packageAnnotated) ? false : true;
    }

    private boolean isAnnotationPresentOrInherited(Class<?> instanceKlazz) {
        Class<?> superclass = instanceKlazz;
        while (true) {
            Class<?> klazz = superclass;
            if (klazz != null) {
                IgnoreSizeOf annotationOnClass = (IgnoreSizeOf) getAnnotationOn(klazz, IgnoreSizeOf.class, IGNORE_SIZE_OF_PATTERN);
                if (annotationOnClass != null && (klazz == instanceKlazz || annotationOnClass.inherited())) {
                    return true;
                }
                superclass = klazz.getSuperclass();
            } else {
                return false;
            }
        }
    }

    private boolean validateCustomAnnotationPattern(String canonicalName, Pattern matchingAnnotationPattern) {
        Matcher matcher = matchingAnnotationPattern.matcher(canonicalName);
        boolean found = matcher.matches();
        if (found) {
            LOG.debug(canonicalName + " matched IgnoreSizeOf annotation pattern " + IGNORE_SIZE_OF_PATTERN);
        }
        return found;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v16, types: [java.lang.annotation.Annotation] */
    private <T extends Annotation> T getAnnotationOn(AnnotatedElement element, Class<T> referenceAnnotation, Pattern matchingAnnotationPattern) {
        T matchingAnnotation = null;
        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            if (validateCustomAnnotationPattern(annotation.annotationType().getName(), matchingAnnotationPattern)) {
                if (matchingAnnotation != null) {
                    throw new IllegalStateException("You are not allowed to use more than one @" + referenceAnnotation.getName() + " annotations for the same element : " + element.toString());
                }
                matchingAnnotation = AnnotationProxyFactory.getAnnotationProxy(annotation, referenceAnnotation);
            }
        }
        return matchingAnnotation;
    }
}
