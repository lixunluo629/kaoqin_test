package org.springframework.beans.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/BeanFactoryUtils.class */
public abstract class BeanFactoryUtils {
    public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";

    public static boolean isFactoryDereference(String name) {
        return name != null && name.startsWith("&");
    }

    public static String transformedBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        String strSubstring = name;
        while (true) {
            String beanName = strSubstring;
            if (beanName.startsWith("&")) {
                strSubstring = beanName.substring("&".length());
            } else {
                return beanName;
            }
        }
    }

    public static boolean isGeneratedBeanName(String name) {
        return name != null && name.contains("#");
    }

    public static String originalBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        int separatorIndex = name.indexOf("#");
        return separatorIndex != -1 ? name.substring(0, separatorIndex) : name;
    }

    public static int countBeansIncludingAncestors(ListableBeanFactory lbf) {
        return beanNamesIncludingAncestors(lbf).length;
    }

    public static String[] beanNamesIncludingAncestors(ListableBeanFactory lbf) {
        return beanNamesForTypeIncludingAncestors(lbf, (Class<?>) Object.class);
    }

    public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, ResolvableType type) {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForTypeIncludingAncestors((ListableBeanFactory) hbf.getParentBeanFactory(), type);
                result = mergeNamesWithParent(result, parentResult, hbf);
            }
        }
        return result;
    }

    public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class<?> type) {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForTypeIncludingAncestors((ListableBeanFactory) hbf.getParentBeanFactory(), type);
                result = mergeNamesWithParent(result, parentResult, hbf);
            }
        }
        return result;
    }

    public static String[] beanNamesForTypeIncludingAncestors(ListableBeanFactory lbf, Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        Assert.notNull(lbf, "ListableBeanFactory must not be null");
        String[] result = lbf.getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
        if (lbf instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hbf = (HierarchicalBeanFactory) lbf;
            if (hbf.getParentBeanFactory() instanceof ListableBeanFactory) {
                String[] parentResult = beanNamesForTypeIncludingAncestors((ListableBeanFactory) hbf.getParentBeanFactory(), type, includeNonSingletons, allowEagerInit);
                result = mergeNamesWithParent(result, parentResult, hbf);
            }
        }
        return result;
    }

    public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory listableBeanFactory, Class<T> cls) throws BeansException {
        Assert.notNull(listableBeanFactory, "ListableBeanFactory must not be null");
        LinkedHashMap linkedHashMap = new LinkedHashMap(4);
        linkedHashMap.putAll(listableBeanFactory.getBeansOfType(cls));
        if (listableBeanFactory instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hierarchicalBeanFactory = (HierarchicalBeanFactory) listableBeanFactory;
            if (hierarchicalBeanFactory.getParentBeanFactory() instanceof ListableBeanFactory) {
                for (Map.Entry entry : beansOfTypeIncludingAncestors((ListableBeanFactory) hierarchicalBeanFactory.getParentBeanFactory(), cls).entrySet()) {
                    String str = (String) entry.getKey();
                    if (!linkedHashMap.containsKey(str) && !hierarchicalBeanFactory.containsLocalBean(str)) {
                        linkedHashMap.put(str, entry.getValue());
                    }
                }
            }
        }
        return linkedHashMap;
    }

    public static <T> Map<String, T> beansOfTypeIncludingAncestors(ListableBeanFactory listableBeanFactory, Class<T> cls, boolean z, boolean z2) throws BeansException {
        Assert.notNull(listableBeanFactory, "ListableBeanFactory must not be null");
        LinkedHashMap linkedHashMap = new LinkedHashMap(4);
        linkedHashMap.putAll(listableBeanFactory.getBeansOfType(cls, z, z2));
        if (listableBeanFactory instanceof HierarchicalBeanFactory) {
            HierarchicalBeanFactory hierarchicalBeanFactory = (HierarchicalBeanFactory) listableBeanFactory;
            if (hierarchicalBeanFactory.getParentBeanFactory() instanceof ListableBeanFactory) {
                for (Map.Entry entry : beansOfTypeIncludingAncestors((ListableBeanFactory) hierarchicalBeanFactory.getParentBeanFactory(), cls, z, z2).entrySet()) {
                    String str = (String) entry.getKey();
                    if (!linkedHashMap.containsKey(str) && !hierarchicalBeanFactory.containsLocalBean(str)) {
                        linkedHashMap.put(str, entry.getValue());
                    }
                }
            }
        }
        return linkedHashMap;
    }

    public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory listableBeanFactory, Class<T> cls) throws BeansException {
        return (T) uniqueBean(cls, beansOfTypeIncludingAncestors(listableBeanFactory, cls));
    }

    public static <T> T beanOfTypeIncludingAncestors(ListableBeanFactory listableBeanFactory, Class<T> cls, boolean z, boolean z2) throws BeansException {
        return (T) uniqueBean(cls, beansOfTypeIncludingAncestors(listableBeanFactory, cls, z, z2));
    }

    public static <T> T beanOfType(ListableBeanFactory listableBeanFactory, Class<T> cls) throws BeansException {
        Assert.notNull(listableBeanFactory, "ListableBeanFactory must not be null");
        return (T) uniqueBean(cls, listableBeanFactory.getBeansOfType(cls));
    }

    public static <T> T beanOfType(ListableBeanFactory listableBeanFactory, Class<T> cls, boolean z, boolean z2) throws BeansException {
        Assert.notNull(listableBeanFactory, "ListableBeanFactory must not be null");
        return (T) uniqueBean(cls, listableBeanFactory.getBeansOfType(cls, z, z2));
    }

    private static String[] mergeNamesWithParent(String[] result, String[] parentResult, HierarchicalBeanFactory hbf) {
        if (parentResult.length == 0) {
            return result;
        }
        List<String> merged = new ArrayList<>(result.length + parentResult.length);
        merged.addAll(Arrays.asList(result));
        for (String beanName : parentResult) {
            if (!merged.contains(beanName) && !hbf.containsLocalBean(beanName)) {
                merged.add(beanName);
            }
        }
        return StringUtils.toStringArray(merged);
    }

    private static <T> T uniqueBean(Class<T> type, Map<String, T> matchingBeans) {
        int count = matchingBeans.size();
        if (count == 1) {
            return matchingBeans.values().iterator().next();
        }
        if (count > 1) {
            throw new NoUniqueBeanDefinitionException((Class<?>) type, (Collection<String>) matchingBeans.keySet());
        }
        throw new NoSuchBeanDefinitionException((Class<?>) type);
    }
}
