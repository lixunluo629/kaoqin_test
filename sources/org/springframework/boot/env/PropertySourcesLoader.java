package org.springframework.boot.env;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/env/PropertySourcesLoader.class */
public class PropertySourcesLoader {
    private static final Log logger = LogFactory.getLog(PropertySourcesLoader.class);
    private final MutablePropertySources propertySources;
    private final List<PropertySourceLoader> loaders;

    public PropertySourcesLoader() {
        this(new MutablePropertySources());
    }

    public PropertySourcesLoader(MutablePropertySources propertySources) {
        Assert.notNull(propertySources, "PropertySources must not be null");
        this.propertySources = propertySources;
        this.loaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());
    }

    public PropertySource<?> load(Resource resource) throws IOException {
        return load(resource, null);
    }

    public PropertySource<?> load(Resource resource, String profile) throws IOException {
        return load(resource, resource.getDescription(), profile);
    }

    public PropertySource<?> load(Resource resource, String name, String profile) throws IOException {
        return load(resource, null, name, profile);
    }

    public PropertySource<?> load(Resource resource, String group, String name, String profile) throws IOException {
        if (isFile(resource)) {
            String sourceName = generatePropertySourceName(name, profile);
            for (PropertySourceLoader loader : this.loaders) {
                if (canLoadFileExtension(loader, resource)) {
                    PropertySource<?> specific = loader.load(sourceName, resource, profile);
                    addPropertySource(group, specific);
                    return specific;
                }
            }
            return null;
        }
        return null;
    }

    private boolean isFile(Resource resource) {
        return resource != null && resource.exists() && StringUtils.hasText(StringUtils.getFilenameExtension(resource.getFilename()));
    }

    private String generatePropertySourceName(String name, String profile) {
        return profile != null ? name + "#" + profile : name;
    }

    private boolean canLoadFileExtension(PropertySourceLoader loader, Resource resource) {
        String filename = resource.getFilename().toLowerCase(Locale.ENGLISH);
        for (String extension : loader.getFileExtensions()) {
            if (filename.endsWith("." + extension.toLowerCase(Locale.ENGLISH))) {
                return true;
            }
        }
        return false;
    }

    private void addPropertySource(String basename, PropertySource<?> source) {
        if (source == null) {
            return;
        }
        if (basename == null) {
            this.propertySources.addLast(source);
            return;
        }
        EnumerableCompositePropertySource group = getGeneric(basename);
        group.add(source);
        logger.trace("Adding PropertySource: " + source + " in group: " + basename);
        if (this.propertySources.contains(group.getName())) {
            this.propertySources.replace(group.getName(), group);
        } else {
            this.propertySources.addFirst(group);
        }
    }

    private EnumerableCompositePropertySource getGeneric(String name) {
        PropertySource<?> source = this.propertySources.get(name);
        if (source instanceof EnumerableCompositePropertySource) {
            return (EnumerableCompositePropertySource) source;
        }
        EnumerableCompositePropertySource composite = new EnumerableCompositePropertySource(name);
        return composite;
    }

    public MutablePropertySources getPropertySources() {
        return this.propertySources;
    }

    public Set<String> getAllFileExtensions() {
        Set<String> fileExtensions = new LinkedHashSet<>();
        for (PropertySourceLoader loader : this.loaders) {
            fileExtensions.addAll(Arrays.asList(loader.getFileExtensions()));
        }
        return fileExtensions;
    }
}
