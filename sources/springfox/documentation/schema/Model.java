package springfox.documentation.schema;

import com.fasterxml.classmate.ResolvedType;
import java.util.List;
import java.util.Map;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/schema/Model.class */
public class Model {
    private final String id;
    private final String name;
    private final ResolvedType type;
    private final String qualifiedType;
    private final Map<String, ModelProperty> properties;
    private final String description;
    private final String baseModel;
    private final String discriminator;
    private final List<String> subTypes;

    public Model(String id, String name, ResolvedType type, String qualifiedType, Map<String, ModelProperty> properties, String description, String baseModel, String discriminator, List<String> subTypes) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.qualifiedType = qualifiedType;
        this.properties = properties;
        this.description = description;
        this.baseModel = baseModel;
        this.discriminator = discriminator;
        this.subTypes = subTypes;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getQualifiedType() {
        return this.qualifiedType;
    }

    public Map<String, ModelProperty> getProperties() {
        return this.properties;
    }

    public String getDescription() {
        return this.description;
    }

    public String getBaseModel() {
        return this.baseModel;
    }

    public String getDiscriminator() {
        return this.discriminator;
    }

    public List<String> getSubTypes() {
        return this.subTypes;
    }

    public ResolvedType getType() {
        return this.type;
    }
}
