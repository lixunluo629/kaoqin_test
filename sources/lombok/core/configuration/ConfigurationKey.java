package lombok.core.configuration;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

/* loaded from: lombok-1.16.22.jar:lombok/core/configuration/ConfigurationKey.SCL.lombok */
public abstract class ConfigurationKey<T> {
    private static final Pattern VALID_NAMES = Pattern.compile("[-_a-zA-Z][-.\\w]*(?<![-.])");
    private static final TreeMap<String, ConfigurationKey<?>> registeredKeys = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static Map<String, ConfigurationKey<?>> copy;
    private final String keyName;
    private final String description;
    private final ConfigurationDataType type;

    public ConfigurationKey(String keyName, String description) {
        this.keyName = checkName(keyName);
        ConfigurationDataType type = ConfigurationDataType.toDataType(getClass());
        this.type = type;
        this.description = description;
        registerKey(keyName, this);
    }

    public final String getKeyName() {
        return this.keyName;
    }

    public final String getDescription() {
        return this.description;
    }

    public final ConfigurationDataType getType() {
        return this.type;
    }

    public String toString() {
        return String.valueOf(this.keyName) + " (" + this.type + "): " + this.description;
    }

    private static String checkName(String keyName) {
        if (keyName == null) {
            throw new NullPointerException("keyName");
        }
        if (VALID_NAMES.matcher(keyName).matches()) {
            return keyName;
        }
        throw new IllegalArgumentException("Invalid keyName: " + keyName);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.util.TreeMap<java.lang.String, lombok.core.configuration.ConfigurationKey<?>>] */
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.util.Map<java.lang.String, lombok.core.configuration.ConfigurationKey<?>>] */
    public static Map<String, ConfigurationKey<?>> registeredKeys() {
        ?? r0 = registeredKeys;
        synchronized (r0) {
            if (copy == null) {
                copy = Collections.unmodifiableMap((Map) registeredKeys.clone());
            }
            r0 = copy;
        }
        return r0;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Throwable, java.util.TreeMap<java.lang.String, lombok.core.configuration.ConfigurationKey<?>>] */
    private static void registerKey(String keyName, ConfigurationKey<?> key) {
        synchronized (registeredKeys) {
            if (registeredKeys.containsKey(keyName)) {
                throw new IllegalArgumentException("Key '" + keyName + "' already registered");
            }
            registeredKeys.put(keyName, key);
            copy = null;
        }
    }
}
