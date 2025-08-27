package lombok.core.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.core.configuration.ConfigurationParser;
import lombok.core.configuration.ConfigurationSource;

/* loaded from: lombok-1.16.22.jar:lombok/core/configuration/StringConfigurationSource.SCL.lombok */
public class StringConfigurationSource implements ConfigurationSource {
    private final Map<ConfigurationKey<?>, ConfigurationSource.Result> values = new HashMap();

    public static ConfigurationSource forString(CharSequence content, ConfigurationProblemReporter reporter, String contentDescription) {
        final Map<ConfigurationKey<?>, ConfigurationSource.Result> values = new HashMap<>();
        new ConfigurationParser(reporter).parse(content, contentDescription, new ConfigurationParser.Collector() { // from class: lombok.core.configuration.StringConfigurationSource.1
            @Override // lombok.core.configuration.ConfigurationParser.Collector
            public void clear(ConfigurationKey<?> key, String contentDescription2, int lineNumber) {
                values.put(key, new ConfigurationSource.Result(null, true));
            }

            @Override // lombok.core.configuration.ConfigurationParser.Collector
            public void set(ConfigurationKey<?> key, Object value, String contentDescription2, int lineNumber) {
                values.put(key, new ConfigurationSource.Result(value, true));
            }

            @Override // lombok.core.configuration.ConfigurationParser.Collector
            public void add(ConfigurationKey<?> key, Object value, String contentDescription2, int lineNumber) {
                modifyList(key, value, true);
            }

            @Override // lombok.core.configuration.ConfigurationParser.Collector
            public void remove(ConfigurationKey<?> key, Object value, String contentDescription2, int lineNumber) {
                modifyList(key, value, false);
            }

            private void modifyList(ConfigurationKey<?> key, Object value, boolean add) {
                List<ConfigurationSource.ListModification> list;
                ConfigurationSource.Result result = (ConfigurationSource.Result) values.get(key);
                if (result == null || result.getValue() == null) {
                    list = new ArrayList<>();
                    values.put(key, new ConfigurationSource.Result(list, result != null));
                } else {
                    list = (List) result.getValue();
                }
                list.add(new ConfigurationSource.ListModification(value, add));
            }
        });
        return new StringConfigurationSource(values);
    }

    private StringConfigurationSource(Map<ConfigurationKey<?>, ConfigurationSource.Result> values) {
        for (Map.Entry<ConfigurationKey<?>, ConfigurationSource.Result> entry : values.entrySet()) {
            ConfigurationSource.Result result = entry.getValue();
            if (result.getValue() instanceof List) {
                this.values.put(entry.getKey(), new ConfigurationSource.Result(Collections.unmodifiableList((List) result.getValue()), result.isAuthoritative()));
            } else {
                this.values.put(entry.getKey(), result);
            }
        }
    }

    @Override // lombok.core.configuration.ConfigurationSource
    public ConfigurationSource.Result resolve(ConfigurationKey<?> key) {
        return this.values.get(key);
    }
}
