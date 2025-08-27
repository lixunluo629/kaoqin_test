package springfox.documentation.swagger2.mappers;

import com.google.common.base.Optional;
import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import org.mapstruct.Mapper;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.Types;

@Mapper
/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/ParameterMapper.class */
public class ParameterMapper {
    public Parameter mapParameter(springfox.documentation.service.Parameter source) {
        Parameter bodyParameter = bodyParameter(source);
        return SerializableParameterFactories.create(source).or((Optional<Parameter>) bodyParameter);
    }

    private Parameter bodyParameter(springfox.documentation.service.Parameter source) {
        BodyParameter parameter = new BodyParameter().description(source.getDescription()).name(source.getName()).schema(fromModelRef(source.getModelRef()));
        parameter.setAccess(source.getParamAccess());
        parameter.setRequired(source.isRequired().booleanValue());
        return parameter;
    }

    Model fromModelRef(ModelRef modelRef) {
        if (modelRef.isCollection()) {
            return new ArrayModel().items(Properties.property(modelRef.getItemType()));
        }
        if (modelRef.isMap()) {
            ModelImpl baseModel = new ModelImpl();
            baseModel.additionalProperties(Properties.property(modelRef.getItemType()));
            return baseModel;
        }
        if (Types.isBaseType(modelRef.getType())) {
            ModelImpl baseModel2 = new ModelImpl();
            baseModel2.setType(modelRef.getType());
            return baseModel2;
        }
        return new RefModel(modelRef.getType());
    }
}
