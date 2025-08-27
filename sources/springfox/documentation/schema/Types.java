package springfox.documentation.schema;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import io.swagger.models.properties.StringProperty;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.XmlErrorCodes;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/Types.class */
public class Types {
    private static final Set<String> baseTypes = Sets.newHashSet(XmlErrorCodes.INT, "date", StringProperty.TYPE, XmlErrorCodes.DOUBLE, XmlErrorCodes.FLOAT, "boolean", "byte", "object", XmlErrorCodes.LONG, "date-time");
    private static final Map<Type, String> typeNameLookup = ImmutableMap.builder().put(Long.TYPE, XmlErrorCodes.LONG).put(Short.TYPE, XmlErrorCodes.INT).put(Integer.TYPE, XmlErrorCodes.INT).put(Double.TYPE, XmlErrorCodes.DOUBLE).put(Float.TYPE, XmlErrorCodes.FLOAT).put(Byte.TYPE, "byte").put(Boolean.TYPE, "boolean").put(Character.TYPE, StringProperty.TYPE).put(Date.class, "date-time").put(String.class, StringProperty.TYPE).put(Object.class, "object").put(Long.class, XmlErrorCodes.LONG).put(Integer.class, XmlErrorCodes.INT).put(Short.class, XmlErrorCodes.INT).put(Double.class, XmlErrorCodes.DOUBLE).put(Float.class, XmlErrorCodes.FLOAT).put(Boolean.class, "boolean").put(Byte.class, "byte").put(BigDecimal.class, XmlErrorCodes.DOUBLE).put(BigInteger.class, XmlErrorCodes.LONG).put(Currency.class, StringProperty.TYPE).build();

    private Types() {
        throw new UnsupportedOperationException();
    }

    public static String typeNameFor(Type type) {
        return typeNameLookup.get(type);
    }

    public static boolean isBaseType(String typeName) {
        return baseTypes.contains(typeName);
    }

    public static boolean isVoid(ResolvedType returnType) {
        return Void.class.equals(returnType.getErasedType()) || Void.TYPE.equals(returnType.getErasedType());
    }
}
