package springfox.documentation.schema.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.Model;
import springfox.documentation.schema.ModelProperty;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelBuilderPlugin;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

@Component
/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/plugins/SchemaPluginsManager.class */
public class SchemaPluginsManager {
    private final PluginRegistry<ModelPropertyBuilderPlugin, DocumentationType> propertyEnrichers;
    private final PluginRegistry<ModelBuilderPlugin, DocumentationType> modelEnrichers;

    @Autowired
    public SchemaPluginsManager(@Qualifier("modelPropertyBuilderPluginRegistry") PluginRegistry<ModelPropertyBuilderPlugin, DocumentationType> propertyEnrichers, @Qualifier("modelBuilderPluginRegistry") PluginRegistry<ModelBuilderPlugin, DocumentationType> modelEnrichers) {
        this.propertyEnrichers = propertyEnrichers;
        this.modelEnrichers = modelEnrichers;
    }

    public ModelProperty property(ModelPropertyContext context) {
        for (T enricher : this.propertyEnrichers.getPluginsFor(context.getDocumentationType())) {
            enricher.apply(context);
        }
        return context.getBuilder().build();
    }

    public Model model(ModelContext context) {
        for (T enricher : this.modelEnrichers.getPluginsFor(context.getDocumentationType())) {
            enricher.apply(context);
        }
        return context.getBuilder().build();
    }
}
