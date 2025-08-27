package lombok.core.configuration;

import io.swagger.models.properties.StringProperty;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlErrorCodes;

/* loaded from: lombok-1.16.22.jar:lombok/core/configuration/ConfigurationDataType.SCL.lombok */
public final class ConfigurationDataType {
    private static final Map<Class<?>, ConfigurationValueParser> SIMPLE_TYPES;
    private final boolean isList;
    private final ConfigurationValueParser parser;

    static {
        Map<Class<?>, ConfigurationValueParser> map = new HashMap<>();
        map.put(String.class, new ConfigurationValueParser() { // from class: lombok.core.configuration.ConfigurationDataType.1
            @Override // lombok.core.configuration.ConfigurationValueParser
            public Object parse(String value) {
                return value;
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String description() {
                return StringProperty.TYPE;
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String exampleValue() {
                return "<text>";
            }
        });
        map.put(Integer.class, new ConfigurationValueParser() { // from class: lombok.core.configuration.ConfigurationDataType.2
            @Override // lombok.core.configuration.ConfigurationValueParser
            public Object parse(String value) {
                return Integer.valueOf(Integer.parseInt(value));
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String description() {
                return XmlErrorCodes.INT;
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String exampleValue() {
                return "<int>";
            }
        });
        map.put(Long.class, new ConfigurationValueParser() { // from class: lombok.core.configuration.ConfigurationDataType.3
            @Override // lombok.core.configuration.ConfigurationValueParser
            public Object parse(String value) {
                return Long.valueOf(Long.parseLong(value));
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String description() {
                return XmlErrorCodes.LONG;
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String exampleValue() {
                return "<long>";
            }
        });
        map.put(Double.class, new ConfigurationValueParser() { // from class: lombok.core.configuration.ConfigurationDataType.4
            @Override // lombok.core.configuration.ConfigurationValueParser
            public Object parse(String value) {
                return Double.valueOf(Double.parseDouble(value));
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String description() {
                return XmlErrorCodes.DOUBLE;
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String exampleValue() {
                return "<double>";
            }
        });
        map.put(Boolean.class, new ConfigurationValueParser() { // from class: lombok.core.configuration.ConfigurationDataType.5
            @Override // lombok.core.configuration.ConfigurationValueParser
            public Object parse(String value) {
                return Boolean.valueOf(Boolean.parseBoolean(value));
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String description() {
                return "boolean";
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String exampleValue() {
                return "[false | true]";
            }
        });
        map.put(TypeName.class, new ConfigurationValueParser() { // from class: lombok.core.configuration.ConfigurationDataType.6
            @Override // lombok.core.configuration.ConfigurationValueParser
            public Object parse(String value) {
                return TypeName.valueOf(value);
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String description() {
                return "type-name";
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String exampleValue() {
                return "<fully.qualified.Type>";
            }
        });
        SIMPLE_TYPES = map;
    }

    private static ConfigurationValueParser enumParser(Object enumType) {
        final Class<?> type = (Class) enumType;
        return new ConfigurationValueParser() { // from class: lombok.core.configuration.ConfigurationDataType.7
            @Override // lombok.core.configuration.ConfigurationValueParser
            public Object parse(String value) {
                try {
                    return Enum.valueOf(type, value);
                } catch (Exception unused) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < value.length(); i++) {
                        char c = value.charAt(i);
                        if (Character.isUpperCase(c) && i > 0) {
                            sb.append("_");
                        }
                        sb.append(Character.toUpperCase(c));
                    }
                    return Enum.valueOf(type, sb.toString());
                }
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String description() {
                return "enum (" + type.getName() + ")";
            }

            @Override // lombok.core.configuration.ConfigurationValueParser
            public String exampleValue() {
                ExampleValueString evs = (ExampleValueString) type.getAnnotation(ExampleValueString.class);
                return evs != null ? evs.value() : Arrays.toString(type.getEnumConstants()).replace(",", " |");
            }
        };
    }

    public static ConfigurationDataType toDataType(Class<? extends ConfigurationKey<?>> keyClass) {
        if (keyClass.getSuperclass() != ConfigurationKey.class) {
            throw new IllegalArgumentException("No direct subclass of ConfigurationKey: " + keyClass.getName());
        }
        Type type = keyClass.getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Missing type parameter in " + type);
        }
        ParameterizedType parameterized = (ParameterizedType) type;
        Type argumentType = parameterized.getActualTypeArguments()[0];
        boolean isList = false;
        if (argumentType instanceof ParameterizedType) {
            ParameterizedType parameterizedArgument = (ParameterizedType) argumentType;
            if (parameterizedArgument.getRawType() == List.class) {
                isList = true;
                argumentType = parameterizedArgument.getActualTypeArguments()[0];
            }
        }
        if (SIMPLE_TYPES.containsKey(argumentType)) {
            return new ConfigurationDataType(isList, SIMPLE_TYPES.get(argumentType));
        }
        if (isEnum(argumentType)) {
            return new ConfigurationDataType(isList, enumParser(argumentType));
        }
        throw new IllegalArgumentException("Unsupported type parameter in " + type);
    }

    private ConfigurationDataType(boolean isList, ConfigurationValueParser parser) {
        this.isList = isList;
        this.parser = parser;
    }

    public boolean isList() {
        return this.isList;
    }

    ConfigurationValueParser getParser() {
        return this.parser;
    }

    public String toString() {
        return this.isList ? "list of " + this.parser.description() : this.parser.description();
    }

    private static boolean isEnum(Type argumentType) {
        return (argumentType instanceof Class) && ((Class) argumentType).isEnum();
    }
}
