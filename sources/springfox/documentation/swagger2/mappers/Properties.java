package springfox.documentation.swagger2.mappers;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.DecimalProperty;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.FileProperty;
import io.swagger.models.properties.FloatProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.models.properties.UUIDProperty;
import java.util.Map;
import org.apache.xmlbeans.XmlErrorCodes;

/* loaded from: springfox-swagger2-2.2.2.jar:springfox/documentation/swagger2/mappers/Properties.class */
class Properties {
    private static final Map<String, Function<String, ? extends Property>> typeFactory = ImmutableMap.builder().put(XmlErrorCodes.INT, newInstanceOf(IntegerProperty.class)).put(XmlErrorCodes.LONG, newInstanceOf(LongProperty.class)).put(XmlErrorCodes.FLOAT, newInstanceOf(FloatProperty.class)).put(XmlErrorCodes.DOUBLE, newInstanceOf(DoubleProperty.class)).put(StringProperty.TYPE, newInstanceOf(StringProperty.class)).put("boolean", newInstanceOf(BooleanProperty.class)).put("date", newInstanceOf(DateProperty.class)).put("date-time", newInstanceOf(DateTimeProperty.class)).put("bigdecimal", newInstanceOf(DecimalProperty.class)).put("biginteger", newInstanceOf(DecimalProperty.class)).put("uuid", newInstanceOf(UUIDProperty.class)).put("object", newInstanceOf(ObjectProperty.class)).put("byte", bytePropertyFactory()).put("file", filePropertyFactory()).build();

    private Properties() {
        throw new UnsupportedOperationException();
    }

    public static Property property(String typeName) {
        String safeTypeName = Strings.nullToEmpty(typeName);
        Function<String, Function<String, ? extends Property>> propertyLookup = Functions.forMap(typeFactory, voidOrRef(safeTypeName));
        return (Property) propertyLookup.apply(safeTypeName.toLowerCase()).apply(safeTypeName);
    }

    private static <T extends Property> Function<String, T> newInstanceOf(final Class<T> cls) {
        return (Function<String, T>) new Function<String, T>() { // from class: springfox.documentation.swagger2.mappers.Properties.1
            /* JADX WARN: Incorrect return type in method signature: (Ljava/lang/String;)TT; */
            @Override // com.google.common.base.Function
            public Property apply(String input) {
                try {
                    return (Property) cls.newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }

    private static Function<String, ? extends Property> voidOrRef(final String typeName) {
        return new Function<String, Property>() { // from class: springfox.documentation.swagger2.mappers.Properties.2
            @Override // com.google.common.base.Function
            public Property apply(String input) {
                if (typeName.equalsIgnoreCase("void")) {
                    return null;
                }
                return new RefProperty(typeName);
            }
        };
    }

    private static Function<String, ? extends Property> bytePropertyFactory() {
        return new Function<String, Property>() { // from class: springfox.documentation.swagger2.mappers.Properties.3
            @Override // com.google.common.base.Function
            public Property apply(String input) {
                StringProperty byteArray = new StringProperty();
                byteArray.setFormat("byte");
                return byteArray;
            }
        };
    }

    private static Function<String, ? extends Property> filePropertyFactory() {
        return new Function<String, Property>() { // from class: springfox.documentation.swagger2.mappers.Properties.4
            @Override // com.google.common.base.Function
            public Property apply(String input) {
                return new FileProperty();
            }
        };
    }
}
