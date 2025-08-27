package lombok.core.configuration;

/* loaded from: lombok-1.16.22.jar:lombok/core/configuration/ConfigurationResolver.SCL.lombok */
public interface ConfigurationResolver {
    <T> T resolve(ConfigurationKey<T> configurationKey);
}
