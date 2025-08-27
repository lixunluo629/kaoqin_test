package springfox.documentation.swagger2.mappers;

import com.google.common.base.Functions;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import io.swagger.models.parameters.CookieParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.parameters.SerializableParameter;
import io.swagger.models.properties.Property;
import java.util.Map;
import org.apache.commons.httpclient.cookie.Cookie2;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;

/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/SerializableParameterFactories.class */
public class SerializableParameterFactories {
    public static final Map<String, SerializableParameterFactory> factory = ImmutableMap.builder().put("header", new HeaderSerializableParameterFactory()).put("form", new FormSerializableParameterFactory()).put(Cookie2.PATH, new PathSerializableParameterFactory()).put("query", new QuerySerializableParameterFactory()).put("cookie", new CookieSerializableParameterFactory()).build();

    private SerializableParameterFactories() {
        throw new UnsupportedOperationException();
    }

    static Optional<Parameter> create(springfox.documentation.service.Parameter source) {
        SerializableParameterFactory factory2 = (SerializableParameterFactory) Functions.forMap(factory, new NullSerializableParameterFactory()).apply(source.getParamType().toLowerCase());
        SerializableParameter toReturn = factory2.create(source);
        if (toReturn == null) {
            return Optional.absent();
        }
        ModelRef paramModel = source.getModelRef();
        toReturn.setName(source.getName());
        toReturn.setDescription(source.getDescription());
        toReturn.setAccess(source.getParamAccess());
        toReturn.setRequired(source.isRequired().booleanValue());
        if (paramModel.isCollection()) {
            toReturn.setCollectionFormat("csv");
            toReturn.setType("array");
            toReturn.setItems(Properties.property(paramModel.getItemType()));
        } else {
            Property property = Properties.property(paramModel.getType());
            toReturn.setType(property.getType());
            toReturn.setFormat(property.getFormat());
        }
        maybeAddAlllowableValues(source, toReturn);
        return Optional.of(toReturn);
    }

    private static void maybeAddAlllowableValues(springfox.documentation.service.Parameter source, SerializableParameter toReturn) {
        if (source.getAllowableValues() instanceof AllowableListValues) {
            AllowableListValues allowableValues = (AllowableListValues) source.getAllowableValues();
            toReturn.setEnum(allowableValues.getValues());
        }
    }

    /* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/SerializableParameterFactories$CookieSerializableParameterFactory.class */
    static class CookieSerializableParameterFactory implements SerializableParameterFactory {
        CookieSerializableParameterFactory() {
        }

        @Override // springfox.documentation.swagger2.mappers.SerializableParameterFactory
        public SerializableParameter create(springfox.documentation.service.Parameter source) {
            CookieParameter param = new CookieParameter();
            param.setDefaultValue(source.getDefaultValue());
            return param;
        }
    }

    /* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/SerializableParameterFactories$FormSerializableParameterFactory.class */
    static class FormSerializableParameterFactory implements SerializableParameterFactory {
        FormSerializableParameterFactory() {
        }

        @Override // springfox.documentation.swagger2.mappers.SerializableParameterFactory
        public SerializableParameter create(springfox.documentation.service.Parameter source) {
            FormParameter param = new FormParameter();
            param.setDefaultValue(source.getDefaultValue());
            return param;
        }
    }

    /* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/SerializableParameterFactories$HeaderSerializableParameterFactory.class */
    static class HeaderSerializableParameterFactory implements SerializableParameterFactory {
        HeaderSerializableParameterFactory() {
        }

        @Override // springfox.documentation.swagger2.mappers.SerializableParameterFactory
        public SerializableParameter create(springfox.documentation.service.Parameter source) {
            HeaderParameter param = new HeaderParameter();
            param.setDefaultValue(source.getDefaultValue());
            return param;
        }
    }

    /* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/SerializableParameterFactories$PathSerializableParameterFactory.class */
    static class PathSerializableParameterFactory implements SerializableParameterFactory {
        PathSerializableParameterFactory() {
        }

        @Override // springfox.documentation.swagger2.mappers.SerializableParameterFactory
        public SerializableParameter create(springfox.documentation.service.Parameter source) {
            PathParameter param = new PathParameter();
            param.setDefaultValue(source.getDefaultValue());
            return param;
        }
    }

    /* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/SerializableParameterFactories$QuerySerializableParameterFactory.class */
    static class QuerySerializableParameterFactory implements SerializableParameterFactory {
        QuerySerializableParameterFactory() {
        }

        @Override // springfox.documentation.swagger2.mappers.SerializableParameterFactory
        public SerializableParameter create(springfox.documentation.service.Parameter source) {
            QueryParameter param = new QueryParameter();
            param.setDefaultValue(source.getDefaultValue());
            return param;
        }
    }

    /* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/SerializableParameterFactories$NullSerializableParameterFactory.class */
    static class NullSerializableParameterFactory implements SerializableParameterFactory {
        NullSerializableParameterFactory() {
        }

        @Override // springfox.documentation.swagger2.mappers.SerializableParameterFactory
        public SerializableParameter create(springfox.documentation.service.Parameter source) {
            return null;
        }
    }
}
