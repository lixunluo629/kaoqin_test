package springfox.documentation.builders;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import springfox.documentation.service.Parameter;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/builders/ParameterMerger.class */
class ParameterMerger {
    private final List<Parameter> destination;
    private final List<Parameter> source;

    public ParameterMerger(List<Parameter> destination, List<Parameter> source) {
        this.destination = Lists.newArrayList(destination);
        this.source = Lists.newArrayList(source);
    }

    public List<Parameter> merged() {
        Set<String> existingParameterNames = FluentIterable.from(this.destination).transform(Parameters.toParameterName()).toSet();
        Set<String> newParameterNames = FluentIterable.from(this.source).transform(Parameters.toParameterName()).toSet();
        List<Parameter> merged = Lists.newArrayList();
        Sets.SetView<String> asIsParams = Sets.difference(existingParameterNames, newParameterNames);
        Sets.SetView<String> missingParamNames = Sets.difference(newParameterNames, existingParameterNames);
        Sets.SetView<String> paramsToMerge = Sets.intersection(newParameterNames, existingParameterNames);
        merged.addAll(asIsParameters(asIsParams, this.destination));
        merged.addAll(newParameters(missingParamNames, this.source));
        merged.addAll(mergedParameters(paramsToMerge, this.destination, this.source));
        return merged;
    }

    private List<Parameter> asIsParameters(Sets.SetView<String> asIsParams, List<Parameter> source) {
        List<Parameter> parameters = Lists.newArrayList();
        for (Parameter each : source) {
            if (asIsParams.contains(each.getName())) {
                parameters.add(each);
            }
        }
        return parameters;
    }

    private List<Parameter> mergedParameters(Sets.SetView<String> paramsToMerge, List<Parameter> existingParameters, List<Parameter> newParams) {
        List<Parameter> parameters = Lists.newArrayList();
        for (Parameter newParam : newParams) {
            Optional<Parameter> original = FluentIterable.from(existingParameters).firstMatch(Parameters.withName(newParam.getName()));
            if (paramsToMerge.contains(newParam.getName()) && original.isPresent()) {
                parameters.add(merged(original.get(), newParam));
            }
        }
        return parameters;
    }

    private Parameter merged(Parameter destination, Parameter source) {
        return new ParameterBuilder().from(destination).name(source.getName()).allowableValues(source.getAllowableValues()).allowMultiple(source.isAllowMultiple().booleanValue()).defaultValue(source.getDefaultValue()).description(source.getDescription()).modelRef(source.getModelRef()).parameterAccess(source.getParamAccess()).parameterType(source.getParamType()).required(source.isRequired().booleanValue()).type(source.getType().orNull()).build();
    }

    private List<Parameter> newParameters(Sets.SetView<String> missingParamNames, List<Parameter> newParams) {
        List<Parameter> parameters = Lists.newArrayList();
        for (Parameter each : newParams) {
            if (missingParamNames.contains(each.getName())) {
                parameters.add(each);
            }
        }
        return parameters;
    }
}
