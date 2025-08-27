package org.springframework.jdbc.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/config/SortedResourcesFactoryBean.class */
public class SortedResourcesFactoryBean extends AbstractFactoryBean<Resource[]> implements ResourceLoaderAware {
    private final List<String> locations;
    private ResourcePatternResolver resourcePatternResolver;

    public SortedResourcesFactoryBean(List<String> locations) {
        this.locations = locations;
        this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
    }

    public SortedResourcesFactoryBean(ResourceLoader resourceLoader, List<String> locations) {
        this.locations = locations;
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
    }

    @Override // org.springframework.context.ResourceLoaderAware
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
    }

    @Override // org.springframework.beans.factory.config.AbstractFactoryBean, org.springframework.beans.factory.FactoryBean
    public Class<? extends Resource[]> getObjectType() {
        return Resource[].class;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.config.AbstractFactoryBean
    public Resource[] createInstance() throws Exception {
        List<Resource> scripts = new ArrayList<>();
        for (String location : this.locations) {
            List<Resource> resources = new ArrayList<>(Arrays.asList(this.resourcePatternResolver.getResources(location)));
            Collections.sort(resources, new Comparator<Resource>() { // from class: org.springframework.jdbc.config.SortedResourcesFactoryBean.1
                @Override // java.util.Comparator
                public int compare(Resource r1, Resource r2) {
                    try {
                        return r1.getURL().toString().compareTo(r2.getURL().toString());
                    } catch (IOException e) {
                        return 0;
                    }
                }
            });
            for (Resource resource : resources) {
                scripts.add(resource);
            }
        }
        return (Resource[]) scripts.toArray(new Resource[scripts.size()]);
    }
}
