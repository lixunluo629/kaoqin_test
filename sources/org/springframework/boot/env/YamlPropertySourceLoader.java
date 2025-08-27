package org.springframework.boot.env;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.boot.yaml.SpringProfileDocumentMatcher;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/env/YamlPropertySourceLoader.class */
public class YamlPropertySourceLoader implements PropertySourceLoader {
    @Override // org.springframework.boot.env.PropertySourceLoader
    public String[] getFileExtensions() {
        return new String[]{"yml", "yaml"};
    }

    @Override // org.springframework.boot.env.PropertySourceLoader
    public PropertySource<?> load(String name, Resource resource, String profile) throws IOException {
        if (ClassUtils.isPresent("org.yaml.snakeyaml.Yaml", null)) {
            Processor processor = new Processor(resource, profile);
            Map<String, Object> source = processor.process();
            if (!source.isEmpty()) {
                return new MapPropertySource(name, source);
            }
            return null;
        }
        return null;
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/env/YamlPropertySourceLoader$Processor.class */
    private static class Processor extends YamlProcessor {
        Processor(Resource resource, String profile) {
            if (profile == null) {
                setMatchDefault(true);
                setDocumentMatchers(new SpringProfileDocumentMatcher());
            } else {
                setMatchDefault(false);
                setDocumentMatchers(new SpringProfileDocumentMatcher(profile));
            }
            setResources(resource);
        }

        @Override // org.springframework.beans.factory.config.YamlProcessor
        protected Yaml createYaml() {
            return new Yaml(new YamlProcessor.StrictMapAppenderConstructor(), new Representer(), new DumperOptions(), new Resolver() { // from class: org.springframework.boot.env.YamlPropertySourceLoader.Processor.1
                @Override // org.yaml.snakeyaml.resolver.Resolver
                public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
                    if (tag == Tag.TIMESTAMP) {
                        return;
                    }
                    super.addImplicitResolver(tag, regexp, first);
                }
            });
        }

        public Map<String, Object> process() {
            final Map<String, Object> result = new LinkedHashMap<>();
            process(new YamlProcessor.MatchCallback() { // from class: org.springframework.boot.env.YamlPropertySourceLoader.Processor.2
                @Override // org.springframework.beans.factory.config.YamlProcessor.MatchCallback
                public void process(Properties properties, Map<String, Object> map) {
                    result.putAll(Processor.this.getFlattenedMap(map));
                }
            });
            return result;
        }
    }
}
