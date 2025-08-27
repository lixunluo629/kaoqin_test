package org.springframework.boot.web.servlet;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/servlet/ServletContextInitializerBeans.class */
public class ServletContextInitializerBeans extends AbstractCollection<ServletContextInitializer> {
    private static final String DISPATCHER_SERVLET_NAME = "dispatcherServlet";
    private static final Log logger = LogFactory.getLog(ServletContextInitializerBeans.class);
    private final Set<Object> seen = new HashSet();
    private final MultiValueMap<Class<?>, ServletContextInitializer> initializers = new LinkedMultiValueMap();
    private List<ServletContextInitializer> sortedList;

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/servlet/ServletContextInitializerBeans$RegistrationBeanAdapter.class */
    private interface RegistrationBeanAdapter<T> {
        RegistrationBean createRegistrationBean(String str, T t, int i);
    }

    public ServletContextInitializerBeans(ListableBeanFactory beanFactory) {
        addServletContextInitializerBeans(beanFactory);
        addAdaptableBeans(beanFactory);
        List<ServletContextInitializer> sortedInitializers = new ArrayList<>();
        for (Map.Entry<?, List<ServletContextInitializer>> entry : this.initializers.entrySet()) {
            AnnotationAwareOrderComparator.sort((List<?>) entry.getValue());
            sortedInitializers.addAll((Collection) entry.getValue());
        }
        this.sortedList = Collections.unmodifiableList(sortedInitializers);
    }

    private void addServletContextInitializerBeans(ListableBeanFactory beanFactory) {
        for (Map.Entry<String, ServletContextInitializer> initializerBean : getOrderedBeansOfType(beanFactory, ServletContextInitializer.class)) {
            addServletContextInitializerBean(initializerBean.getKey(), initializerBean.getValue(), beanFactory);
        }
    }

    private void addServletContextInitializerBean(String beanName, ServletContextInitializer initializer, ListableBeanFactory beanFactory) {
        if (initializer instanceof ServletRegistrationBean) {
            Servlet source = ((ServletRegistrationBean) initializer).getServlet();
            addServletContextInitializerBean(Servlet.class, beanName, initializer, beanFactory, source);
            return;
        }
        if (initializer instanceof FilterRegistrationBean) {
            Filter source2 = ((FilterRegistrationBean) initializer).getFilter();
            addServletContextInitializerBean(Filter.class, beanName, initializer, beanFactory, source2);
        } else if (initializer instanceof DelegatingFilterProxyRegistrationBean) {
            String source3 = ((DelegatingFilterProxyRegistrationBean) initializer).getTargetBeanName();
            addServletContextInitializerBean(Filter.class, beanName, initializer, beanFactory, source3);
        } else if (initializer instanceof ServletListenerRegistrationBean) {
            EventListener source4 = ((ServletListenerRegistrationBean) initializer).getListener();
            addServletContextInitializerBean(EventListener.class, beanName, initializer, beanFactory, source4);
        } else {
            addServletContextInitializerBean(ServletContextInitializer.class, beanName, initializer, beanFactory, initializer);
        }
    }

    private void addServletContextInitializerBean(Class<?> type, String beanName, ServletContextInitializer initializer, ListableBeanFactory beanFactory, Object source) {
        this.initializers.add(type, initializer);
        if (source != null) {
            this.seen.add(source);
        }
        if (logger.isDebugEnabled()) {
            String resourceDescription = getResourceDescription(beanName, beanFactory);
            int order = getOrder(initializer);
            logger.debug("Added existing " + type.getSimpleName() + " initializer bean '" + beanName + "'; order=" + order + ", resource=" + resourceDescription);
        }
    }

    private String getResourceDescription(String beanName, ListableBeanFactory beanFactory) {
        if (beanFactory instanceof BeanDefinitionRegistry) {
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
            return registry.getBeanDefinition(beanName).getResourceDescription();
        }
        return "unknown";
    }

    private void addAdaptableBeans(ListableBeanFactory beanFactory) {
        MultipartConfigElement multipartConfig = getMultipartConfig(beanFactory);
        addAsRegistrationBean(beanFactory, Servlet.class, new ServletRegistrationBeanAdapter(multipartConfig));
        addAsRegistrationBean(beanFactory, Filter.class, new FilterRegistrationBeanAdapter());
        for (Class<?> listenerType : ServletListenerRegistrationBean.getSupportedTypes()) {
            addAsRegistrationBean(beanFactory, EventListener.class, listenerType, new ServletListenerRegistrationBeanAdapter());
        }
    }

    private MultipartConfigElement getMultipartConfig(ListableBeanFactory beanFactory) {
        List<Map.Entry<String, MultipartConfigElement>> beans = getOrderedBeansOfType(beanFactory, MultipartConfigElement.class);
        if (beans.isEmpty()) {
            return null;
        }
        return beans.get(0).getValue();
    }

    private <T> void addAsRegistrationBean(ListableBeanFactory beanFactory, Class<T> type, RegistrationBeanAdapter<T> adapter) {
        addAsRegistrationBean(beanFactory, type, type, adapter);
    }

    private <T, B extends T> void addAsRegistrationBean(ListableBeanFactory beanFactory, Class<T> type, Class<B> beanType, RegistrationBeanAdapter<T> adapter) {
        List<Map.Entry<String, T>> orderedBeansOfType = getOrderedBeansOfType(beanFactory, beanType, this.seen);
        for (Map.Entry<String, T> entry : orderedBeansOfType) {
            if (this.seen.add(entry.getValue())) {
                int order = getOrder(entry.getValue());
                String beanName = entry.getKey();
                RegistrationBean registration = adapter.createRegistrationBean(beanName, entry.getValue(), orderedBeansOfType.size());
                registration.setName(beanName);
                registration.setOrder(order);
                this.initializers.add(type, registration);
                if (logger.isDebugEnabled()) {
                    logger.debug("Created " + type.getSimpleName() + " initializer for bean '" + beanName + "'; order=" + order + ", resource=" + getResourceDescription(beanName, beanFactory));
                }
            }
        }
    }

    private int getOrder(Object value) {
        return new AnnotationAwareOrderComparator() { // from class: org.springframework.boot.web.servlet.ServletContextInitializerBeans.1
            @Override // org.springframework.core.OrderComparator
            public int getOrder(Object obj) {
                return super.getOrder(obj);
            }
        }.getOrder(value);
    }

    private <T> List<Map.Entry<String, T>> getOrderedBeansOfType(ListableBeanFactory beanFactory, Class<T> type) {
        return getOrderedBeansOfType(beanFactory, type, Collections.emptySet());
    }

    private <T> List<Map.Entry<String, T>> getOrderedBeansOfType(ListableBeanFactory beanFactory, Class<T> type, Set<?> excludes) {
        List<Map.Entry<String, T>> beans = new ArrayList<>();
        Comparator<Map.Entry<String, T>> comparator = new Comparator<Map.Entry<String, T>>() { // from class: org.springframework.boot.web.servlet.ServletContextInitializerBeans.2
            @Override // java.util.Comparator
            public int compare(Map.Entry<String, T> o1, Map.Entry<String, T> o2) {
                return AnnotationAwareOrderComparator.INSTANCE.compare(o1.getValue(), o2.getValue());
            }
        };
        String[] names = beanFactory.getBeanNamesForType(type, true, false);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (String name : names) {
            if (!excludes.contains(name) && !ScopedProxyUtils.isScopedTarget(name)) {
                Object bean = beanFactory.getBean(name, type);
                if (!excludes.contains(bean)) {
                    linkedHashMap.put(name, bean);
                }
            }
        }
        beans.addAll(linkedHashMap.entrySet());
        Collections.sort(beans, comparator);
        return beans;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<ServletContextInitializer> iterator() {
        return this.sortedList.iterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return this.sortedList.size();
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/servlet/ServletContextInitializerBeans$ServletRegistrationBeanAdapter.class */
    private static class ServletRegistrationBeanAdapter implements RegistrationBeanAdapter<Servlet> {
        private final MultipartConfigElement multipartConfig;

        ServletRegistrationBeanAdapter(MultipartConfigElement multipartConfig) {
            this.multipartConfig = multipartConfig;
        }

        @Override // org.springframework.boot.web.servlet.ServletContextInitializerBeans.RegistrationBeanAdapter
        public RegistrationBean createRegistrationBean(String name, Servlet source, int totalNumberOfSourceBeans) {
            String url = totalNumberOfSourceBeans != 1 ? "/" + name + "/" : "/";
            if (name.equals("dispatcherServlet")) {
                url = "/";
            }
            ServletRegistrationBean bean = new ServletRegistrationBean(source, url);
            bean.setMultipartConfig(this.multipartConfig);
            return bean;
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/servlet/ServletContextInitializerBeans$FilterRegistrationBeanAdapter.class */
    private static class FilterRegistrationBeanAdapter implements RegistrationBeanAdapter<Filter> {
        private FilterRegistrationBeanAdapter() {
        }

        @Override // org.springframework.boot.web.servlet.ServletContextInitializerBeans.RegistrationBeanAdapter
        public RegistrationBean createRegistrationBean(String name, Filter source, int totalNumberOfSourceBeans) {
            return new FilterRegistrationBean(source, new ServletRegistrationBean[0]);
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/servlet/ServletContextInitializerBeans$ServletListenerRegistrationBeanAdapter.class */
    private static class ServletListenerRegistrationBeanAdapter implements RegistrationBeanAdapter<EventListener> {
        private ServletListenerRegistrationBeanAdapter() {
        }

        @Override // org.springframework.boot.web.servlet.ServletContextInitializerBeans.RegistrationBeanAdapter
        public RegistrationBean createRegistrationBean(String name, EventListener source, int totalNumberOfSourceBeans) {
            return new ServletListenerRegistrationBean(source);
        }
    }
}
