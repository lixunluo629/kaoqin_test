package org.springframework.beans.support;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/support/PropertyComparator.class */
public class PropertyComparator<T> implements Comparator<T> {
    private final SortDefinition sortDefinition;
    protected final Log logger = LogFactory.getLog(getClass());
    private final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(false);

    public PropertyComparator(SortDefinition sortDefinition) {
        this.sortDefinition = sortDefinition;
    }

    public PropertyComparator(String property, boolean ignoreCase, boolean ascending) {
        this.sortDefinition = new MutableSortDefinition(property, ignoreCase, ascending);
    }

    public final SortDefinition getSortDefinition() {
        return this.sortDefinition;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Comparator
    public int compare(T o1, T o2) {
        int result;
        int iCompareTo;
        Object v1 = getPropertyValue(o1);
        Object v2 = getPropertyValue(o2);
        if (this.sortDefinition.isIgnoreCase() && (v1 instanceof String) && (v2 instanceof String)) {
            v1 = ((String) v1).toLowerCase();
            v2 = ((String) v2).toLowerCase();
        }
        if (v1 != null) {
            if (v2 != null) {
                try {
                    iCompareTo = ((Comparable) v1).compareTo(v2);
                } catch (RuntimeException ex) {
                    if (this.logger.isWarnEnabled()) {
                        this.logger.warn("Could not sort objects [" + o1 + "] and [" + o2 + "]", ex);
                        return 0;
                    }
                    return 0;
                }
            } else {
                iCompareTo = -1;
            }
            result = iCompareTo;
        } else {
            result = v2 != null ? 1 : 0;
        }
        return this.sortDefinition.isAscending() ? result : -result;
    }

    private Object getPropertyValue(Object obj) {
        try {
            this.beanWrapper.setWrappedInstance(obj);
            return this.beanWrapper.getPropertyValue(this.sortDefinition.getProperty());
        } catch (BeansException ex) {
            this.logger.info("PropertyComparator could not access property - treating as null for sorting", ex);
            return null;
        }
    }

    public static void sort(List<?> source, SortDefinition sortDefinition) throws BeansException {
        if (StringUtils.hasText(sortDefinition.getProperty())) {
            Collections.sort(source, new PropertyComparator(sortDefinition));
        }
    }

    public static void sort(Object[] source, SortDefinition sortDefinition) throws BeansException {
        if (StringUtils.hasText(sortDefinition.getProperty())) {
            Arrays.sort(source, new PropertyComparator(sortDefinition));
        }
    }
}
