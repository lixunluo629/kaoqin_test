package springfox.documentation.builders;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import springfox.documentation.schema.Model;
import springfox.documentation.schema.ModelProperty;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/ModelBuilder.class */
public class ModelBuilder {
    private String id;
    private String name;
    private String qualifiedType;
    private String description;
    private String baseModel;
    private String discriminator;
    private ResolvedType modelType;
    private Map<String, ModelProperty> properties = Maps.newHashMap();
    private List<String> subTypes = Lists.newArrayList();

    public ModelBuilder id(String id) {
        this.id = (String) BuilderDefaults.defaultIfAbsent(id, this.id);
        return this;
    }

    public ModelBuilder name(String name) {
        this.name = (String) BuilderDefaults.defaultIfAbsent(name, this.name);
        return this;
    }

    public ModelBuilder qualifiedType(String qualifiedType) {
        this.qualifiedType = (String) BuilderDefaults.defaultIfAbsent(qualifiedType, this.qualifiedType);
        return this;
    }

    public ModelBuilder properties(Map<String, ModelProperty> properties) {
        this.properties.putAll(BuilderDefaults.nullToEmptyMap(properties));
        return this;
    }

    public ModelBuilder description(String description) {
        this.description = (String) BuilderDefaults.defaultIfAbsent(description, this.description);
        return this;
    }

    public ModelBuilder baseModel(String baseModel) {
        this.baseModel = (String) BuilderDefaults.defaultIfAbsent(baseModel, this.baseModel);
        return this;
    }

    public ModelBuilder discriminator(String discriminator) {
        this.discriminator = (String) BuilderDefaults.defaultIfAbsent(discriminator, this.discriminator);
        return this;
    }

    public ModelBuilder subTypes(List<String> subTypes) {
        if (subTypes != null) {
            this.subTypes.addAll(subTypes);
        }
        return this;
    }

    public ModelBuilder type(ResolvedType modelType) {
        this.modelType = (ResolvedType) BuilderDefaults.defaultIfAbsent(modelType, this.modelType);
        return this;
    }

    public Model build() {
        return new Model(this.id, this.name, this.modelType, this.qualifiedType, this.properties, this.description, this.baseModel, this.discriminator, this.subTypes);
    }
}
