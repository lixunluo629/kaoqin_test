package springfox.documentation.schema.property;

import com.fasterxml.classmate.ResolvedType;
import springfox.documentation.service.AllowableValues;

/* loaded from: springfox-schema-2.2.2.jar:springfox/documentation/schema/property/ModelProperty.class */
public interface ModelProperty {
    String getName();

    ResolvedType getType();

    String qualifiedTypeName();

    AllowableValues allowableValues();

    String propertyDescription();

    boolean isRequired();

    boolean isReadOnly();

    int position();
}
