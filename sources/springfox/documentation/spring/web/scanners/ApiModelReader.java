package springfox.documentation.spring.web.scanners;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ModelBuilder;
import springfox.documentation.schema.Model;
import springfox.documentation.schema.ModelProperty;
import springfox.documentation.schema.ModelProvider;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;

@Component
/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/scanners/ApiModelReader.class */
public class ApiModelReader {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) ApiModelReader.class);
    private final ModelProvider modelProvider;
    private final TypeResolver typeResolver;
    private final DocumentationPluginsManager pluginsManager;

    @Autowired
    public ApiModelReader(ModelProvider modelProvider, TypeResolver typeResolver, DocumentationPluginsManager pluginsManager) {
        this.modelProvider = modelProvider;
        this.typeResolver = typeResolver;
        this.pluginsManager = pluginsManager;
    }

    public Map<String, Model> read(RequestMappingContext context) {
        Set<Class> ignorableTypes = Sets.newHashSet(context.getDocumentationContext().getIgnorableParameterTypes());
        Set<ModelContext> modelContexts = this.pluginsManager.modelContexts(context);
        Map<String, Model> modelMap = Maps.newHashMap(context.getModelMap());
        for (ModelContext each : modelContexts) {
            markIgnorablesAsHasSeen(this.typeResolver, ignorableTypes, each);
            Optional<Model> pModel = this.modelProvider.modelFor(each);
            if (pModel.isPresent()) {
                LOG.debug("Generated parameter model id: {}, name: {}, schema: {} models", pModel.get().getId(), pModel.get().getName());
                mergeModelMap(modelMap, pModel.get());
            } else {
                LOG.debug("Did not find any parameter models for {}", each.getType());
            }
            populateDependencies(each, modelMap);
        }
        return modelMap;
    }

    private void mergeModelMap(Map<String, Model> target, Model source) {
        String sourceModelKey = source.getId();
        if (!target.containsKey(sourceModelKey)) {
            LOG.debug("Adding a new model with key {}", sourceModelKey);
            target.put(sourceModelKey, source);
            return;
        }
        Model targetModelValue = target.get(sourceModelKey);
        Map<String, ModelProperty> targetProperties = targetModelValue.getProperties();
        Map<String, ModelProperty> sourceProperties = source.getProperties();
        Set<String> newSourcePropKeys = Sets.newHashSet(sourceProperties.keySet());
        newSourcePropKeys.removeAll(targetProperties.keySet());
        Map<String, ModelProperty> mergedTargetProperties = Maps.newHashMap(targetProperties);
        for (String newProperty : newSourcePropKeys) {
            LOG.debug("Adding a missing property {} to model {}", newProperty, sourceModelKey);
            mergedTargetProperties.put(newProperty, sourceProperties.get(newProperty));
        }
        Model mergedModel = new ModelBuilder().id(targetModelValue.getId()).name(targetModelValue.getName()).type(targetModelValue.getType()).qualifiedType(targetModelValue.getQualifiedType()).properties(mergedTargetProperties).description(targetModelValue.getDescription()).baseModel(targetModelValue.getBaseModel()).discriminator(targetModelValue.getDiscriminator()).subTypes(targetModelValue.getSubTypes()).build();
        target.put(sourceModelKey, mergedModel);
    }

    private void markIgnorablesAsHasSeen(TypeResolver typeResolver, Set<Class> ignorableParameterTypes, ModelContext modelContext) {
        for (Class ignorableParameterType : ignorableParameterTypes) {
            modelContext.seen(typeResolver.resolve(ignorableParameterType, new Type[0]));
        }
    }

    private void populateDependencies(ModelContext modelContext, Map<String, Model> modelMap) {
        Map<String, Model> dependencies = this.modelProvider.dependencies(modelContext);
        for (Model each : dependencies.values()) {
            mergeModelMap(modelMap, each);
        }
    }
}
