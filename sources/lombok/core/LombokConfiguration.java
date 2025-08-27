package lombok.core;

import java.util.Collections;
import lombok.core.configuration.BubblingConfigurationResolver;
import lombok.core.configuration.ConfigurationKey;
import lombok.core.configuration.ConfigurationProblemReporter;
import lombok.core.configuration.ConfigurationResolver;
import lombok.core.configuration.ConfigurationResolverFactory;
import lombok.core.configuration.FileSystemSourceCache;

/* loaded from: lombok-1.16.22.jar:lombok/core/LombokConfiguration.SCL.lombok */
public class LombokConfiguration {
    private static final ConfigurationResolver NULL_RESOLVER = new ConfigurationResolver() { // from class: lombok.core.LombokConfiguration.1
        @Override // lombok.core.configuration.ConfigurationResolver
        public <T> T resolve(ConfigurationKey<T> configurationKey) {
            if (configurationKey.getType().isList()) {
                return (T) Collections.emptyList();
            }
            return null;
        }
    };
    private static FileSystemSourceCache cache = new FileSystemSourceCache();
    private static ConfigurationResolverFactory configurationResolverFactory;

    static {
        if (System.getProperty("lombok.disableConfig") != null) {
            configurationResolverFactory = new ConfigurationResolverFactory() { // from class: lombok.core.LombokConfiguration.2
                @Override // lombok.core.configuration.ConfigurationResolverFactory
                public ConfigurationResolver createResolver(AST<?, ?, ?> ast) {
                    return LombokConfiguration.NULL_RESOLVER;
                }
            };
        } else {
            configurationResolverFactory = createFileSystemBubblingResolverFactory();
        }
    }

    private LombokConfiguration() {
    }

    public static void overrideConfigurationResolverFactory(ConfigurationResolverFactory crf) {
        configurationResolverFactory = crf == null ? createFileSystemBubblingResolverFactory() : crf;
    }

    static <T> T read(ConfigurationKey<T> configurationKey, AST<?, ?, ?> ast) {
        return (T) configurationResolverFactory.createResolver(ast).resolve(configurationKey);
    }

    private static ConfigurationResolverFactory createFileSystemBubblingResolverFactory() {
        return new ConfigurationResolverFactory() { // from class: lombok.core.LombokConfiguration.3
            @Override // lombok.core.configuration.ConfigurationResolverFactory
            public ConfigurationResolver createResolver(AST<?, ?, ?> ast) {
                return new BubblingConfigurationResolver(LombokConfiguration.cache.sourcesForJavaFile(ast.getAbsoluteFileLocation(), ConfigurationProblemReporter.CONSOLE));
            }
        };
    }
}
