package springfox.documentation.swagger.schema;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import io.swagger.annotations.ApiModelProperty;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableRangeValues;
import springfox.documentation.service.AllowableValues;

/* loaded from: springfox-swagger-common-2.2.2.jar:springfox/documentation/swagger/schema/ApiModelProperties.class */
public final class ApiModelProperties {
    private ApiModelProperties() {
        throw new UnsupportedOperationException();
    }

    public static Function<ApiModelProperty, AllowableValues> toAllowableValues() {
        return new Function<ApiModelProperty, AllowableValues>() { // from class: springfox.documentation.swagger.schema.ApiModelProperties.1
            @Override // com.google.common.base.Function
            public AllowableValues apply(ApiModelProperty annotation) {
                return ApiModelProperties.allowableValueFromString(annotation.allowableValues());
            }
        };
    }

    public static AllowableValues allowableValueFromString(String allowableValueString) {
        AllowableValues allowableValues = new AllowableListValues(Lists.newArrayList(), "LIST");
        String allowableValueString2 = allowableValueString.trim().replaceAll(SymbolConstants.SPACE_SYMBOL, "");
        if (allowableValueString2.startsWith("range[")) {
            Iterable<String> split = Splitter.on(',').trimResults().omitEmptyStrings().split(allowableValueString2.replaceAll("range\\[", "").replaceAll("]", ""));
            List<String> ranges = Lists.newArrayList(split);
            allowableValues = new AllowableRangeValues(ranges.get(0), ranges.get(1));
        } else if (allowableValueString2.contains(",")) {
            Iterable<String> split2 = Splitter.on(',').trimResults().omitEmptyStrings().split(allowableValueString2);
            allowableValues = new AllowableListValues(Lists.newArrayList(split2), "LIST");
        } else if (StringUtils.hasText(allowableValueString2)) {
            List<String> singleVal = Collections.singletonList(allowableValueString2.trim());
            allowableValues = new AllowableListValues(singleVal, "LIST");
        }
        return allowableValues;
    }

    public static Function<ApiModelProperty, Boolean> toIsRequired() {
        return new Function<ApiModelProperty, Boolean>() { // from class: springfox.documentation.swagger.schema.ApiModelProperties.2
            @Override // com.google.common.base.Function
            public Boolean apply(ApiModelProperty annotation) {
                return Boolean.valueOf(annotation.required());
            }
        };
    }

    public static Function<ApiModelProperty, Boolean> toIsReadOnly() {
        return new Function<ApiModelProperty, Boolean>() { // from class: springfox.documentation.swagger.schema.ApiModelProperties.3
            @Override // com.google.common.base.Function
            public Boolean apply(ApiModelProperty annotation) {
                return Boolean.valueOf(annotation.readOnly());
            }
        };
    }

    public static Function<ApiModelProperty, String> toDescription() {
        return new Function<ApiModelProperty, String>() { // from class: springfox.documentation.swagger.schema.ApiModelProperties.4
            @Override // com.google.common.base.Function
            public String apply(ApiModelProperty annotation) {
                String description = "";
                if (!Strings.isNullOrEmpty(annotation.value())) {
                    description = annotation.value();
                } else if (!Strings.isNullOrEmpty(annotation.notes())) {
                    description = annotation.notes();
                }
                return description;
            }
        };
    }

    public static Function<ApiModelProperty, ResolvedType> toType(final TypeResolver resolver) {
        return new Function<ApiModelProperty, ResolvedType>() { // from class: springfox.documentation.swagger.schema.ApiModelProperties.5
            @Override // com.google.common.base.Function
            public ResolvedType apply(ApiModelProperty annotation) {
                try {
                    return resolver.resolve(Class.forName(annotation.dataType()), new Type[0]);
                } catch (ClassNotFoundException e) {
                    return resolver.resolve(Object.class, new Type[0]);
                }
            }
        };
    }

    public static Optional<ApiModelProperty> findApiModePropertyAnnotation(AnnotatedElement annotated) {
        return Optional.fromNullable(AnnotationUtils.getAnnotation(annotated, ApiModelProperty.class));
    }

    public static Function<ApiModelProperty, Boolean> toHidden() {
        return new Function<ApiModelProperty, Boolean>() { // from class: springfox.documentation.swagger.schema.ApiModelProperties.6
            @Override // com.google.common.base.Function
            public Boolean apply(ApiModelProperty annotation) {
                return Boolean.valueOf(annotation.hidden());
            }
        };
    }
}
