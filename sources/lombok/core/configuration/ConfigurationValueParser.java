package lombok.core.configuration;

/* loaded from: lombok-1.16.22.jar:lombok/core/configuration/ConfigurationValueParser.SCL.lombok */
interface ConfigurationValueParser {
    Object parse(String str);

    String description();

    String exampleValue();
}
