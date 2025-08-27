package springfox.documentation.swagger2.mappers;

import io.swagger.models.parameters.SerializableParameter;
import springfox.documentation.service.Parameter;

/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/SerializableParameterFactory.class */
interface SerializableParameterFactory {
    SerializableParameter create(Parameter parameter);
}
