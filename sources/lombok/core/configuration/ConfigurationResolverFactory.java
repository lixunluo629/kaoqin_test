package lombok.core.configuration;

import lombok.core.AST;

/* loaded from: lombok-1.16.22.jar:lombok/core/configuration/ConfigurationResolverFactory.SCL.lombok */
public interface ConfigurationResolverFactory {
    ConfigurationResolver createResolver(AST<?, ?, ?> ast);
}
