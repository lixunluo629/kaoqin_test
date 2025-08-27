package org.springframework.beans.factory.config;

import com.alibaba.excel.constant.ExcelXmlConstants;
import java.io.IOException;
import java.io.Reader;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyAccessor;
import org.springframework.core.CollectionFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.parser.ParserException;
import org.yaml.snakeyaml.reader.UnicodeReader;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/YamlProcessor.class */
public abstract class YamlProcessor {
    private final Log logger = LogFactory.getLog(getClass());
    private ResolutionMethod resolutionMethod = ResolutionMethod.OVERRIDE;
    private Resource[] resources = new Resource[0];
    private List<DocumentMatcher> documentMatchers = Collections.emptyList();
    private boolean matchDefault = true;

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/YamlProcessor$DocumentMatcher.class */
    public interface DocumentMatcher {
        MatchStatus matches(Properties properties);
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/YamlProcessor$MatchCallback.class */
    public interface MatchCallback {
        void process(Properties properties, Map<String, Object> map);
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/YamlProcessor$ResolutionMethod.class */
    public enum ResolutionMethod {
        OVERRIDE,
        OVERRIDE_AND_IGNORE,
        FIRST_FOUND
    }

    public void setDocumentMatchers(DocumentMatcher... matchers) {
        this.documentMatchers = Arrays.asList(matchers);
    }

    public void setMatchDefault(boolean matchDefault) {
        this.matchDefault = matchDefault;
    }

    public void setResolutionMethod(ResolutionMethod resolutionMethod) {
        Assert.notNull(resolutionMethod, "ResolutionMethod must not be null");
        this.resolutionMethod = resolutionMethod;
    }

    public void setResources(Resource... resources) {
        this.resources = resources;
    }

    protected void process(MatchCallback callback) throws IOException {
        Yaml yaml = createYaml();
        for (Resource resource : this.resources) {
            boolean found = process(callback, yaml, resource);
            if (this.resolutionMethod == ResolutionMethod.FIRST_FOUND && found) {
                return;
            }
        }
    }

    protected Yaml createYaml() {
        return new Yaml(new StrictMapAppenderConstructor());
    }

    /* JADX WARN: Finally extract failed */
    private boolean process(MatchCallback callback, Yaml yaml, Resource resource) throws IOException {
        int count = 0;
        try {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Loading from YAML: " + resource);
            }
            Reader reader = new UnicodeReader(resource.getInputStream());
            try {
                for (Object object : yaml.loadAll(reader)) {
                    if (object != null && process(asMap(object), callback)) {
                        count++;
                        if (this.resolutionMethod == ResolutionMethod.FIRST_FOUND) {
                            break;
                        }
                    }
                }
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Loaded " + count + " document" + (count > 1 ? ExcelXmlConstants.CELL_DATA_FORMAT_TAG : "") + " from YAML resource: " + resource);
                }
                reader.close();
            } catch (Throwable th) {
                reader.close();
                throw th;
            }
        } catch (IOException ex) {
            handleProcessError(resource, ex);
        }
        return count > 0;
    }

    private void handleProcessError(Resource resource, IOException ex) {
        if (this.resolutionMethod != ResolutionMethod.FIRST_FOUND && this.resolutionMethod != ResolutionMethod.OVERRIDE_AND_IGNORE) {
            throw new IllegalStateException(ex);
        }
        if (this.logger.isWarnEnabled()) {
            this.logger.warn("Could not load map from " + resource + ": " + ex.getMessage());
        }
    }

    private Map<String, Object> asMap(Object object) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (!(object instanceof Map)) {
            result.put("document", object);
            return result;
        }
        Map<Object, Object> map = (Map) object;
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                value = asMap(value);
            }
            Object key = entry.getKey();
            if (key instanceof CharSequence) {
                result.put(key.toString(), value);
            } else {
                result.put(PropertyAccessor.PROPERTY_KEY_PREFIX + key.toString() + "]", value);
            }
        }
        return result;
    }

    private boolean process(Map<String, Object> map, MatchCallback callback) {
        Properties properties = CollectionFactory.createStringAdaptingProperties();
        properties.putAll(getFlattenedMap(map));
        if (this.documentMatchers.isEmpty()) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Merging document (no matchers set): " + map);
            }
            callback.process(properties, map);
            return true;
        }
        MatchStatus result = MatchStatus.ABSTAIN;
        for (DocumentMatcher matcher : this.documentMatchers) {
            MatchStatus match = matcher.matches(properties);
            result = MatchStatus.getMostSpecific(match, result);
            if (match == MatchStatus.FOUND) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Matched document with document matcher: " + properties);
                }
                callback.process(properties, map);
                return true;
            }
        }
        if (result == MatchStatus.ABSTAIN && this.matchDefault) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Matched document with default matcher: " + map);
            }
            callback.process(properties, map);
            return true;
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Unmatched document: " + map);
            return false;
        }
        return false;
    }

    protected final Map<String, Object> getFlattenedMap(Map<String, Object> source) {
        Map<String, Object> result = new LinkedHashMap<>();
        buildFlattenedMap(result, source, null);
        return result;
    }

    private void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, String path) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = entry.getKey();
            if (StringUtils.hasText(path)) {
                if (key.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
                    key = path + key;
                } else {
                    key = path + '.' + key;
                }
            }
            Object value = entry.getValue();
            if (value instanceof String) {
                result.put(key, value);
            } else if (value instanceof Map) {
                Map<String, Object> map = (Map) value;
                buildFlattenedMap(result, map, key);
            } else if (value instanceof Collection) {
                Collection<Object> collection = (Collection) value;
                int count = 0;
                for (Object object : collection) {
                    int i = count;
                    count++;
                    buildFlattenedMap(result, Collections.singletonMap(PropertyAccessor.PROPERTY_KEY_PREFIX + i + "]", object), key);
                }
            } else {
                result.put(key, value != null ? value : "");
            }
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/YamlProcessor$MatchStatus.class */
    public enum MatchStatus {
        FOUND,
        NOT_FOUND,
        ABSTAIN;

        public static MatchStatus getMostSpecific(MatchStatus a, MatchStatus b) {
            return a.ordinal() < b.ordinal() ? a : b;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/YamlProcessor$StrictMapAppenderConstructor.class */
    public static class StrictMapAppenderConstructor extends Constructor {
        @Override // org.yaml.snakeyaml.constructor.BaseConstructor
        protected Map<Object, Object> constructMapping(MappingNode node) {
            try {
                return super.constructMapping(node);
            } catch (IllegalStateException ex) {
                throw new ParserException("while parsing MappingNode", node.getStartMark(), ex.getMessage(), node.getEndMark());
            }
        }

        @Override // org.yaml.snakeyaml.constructor.BaseConstructor
        protected Map<Object, Object> createDefaultMap() {
            final Map<Object, Object> delegate = super.createDefaultMap();
            return new AbstractMap<Object, Object>() { // from class: org.springframework.beans.factory.config.YamlProcessor.StrictMapAppenderConstructor.1
                @Override // java.util.AbstractMap, java.util.Map
                public Object put(Object key, Object value) {
                    if (delegate.containsKey(key)) {
                        throw new IllegalStateException("Duplicate key: " + key);
                    }
                    return delegate.put(key, value);
                }

                @Override // java.util.AbstractMap, java.util.Map
                public Set<Map.Entry<Object, Object>> entrySet() {
                    return delegate.entrySet();
                }
            };
        }
    }
}
