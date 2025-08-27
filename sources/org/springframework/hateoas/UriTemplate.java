package org.springframework.hateoas;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/UriTemplate.class */
public class UriTemplate implements Iterable<TemplateVariable>, Serializable {
    private static final Pattern VARIABLE_REGEX = Pattern.compile("\\{([\\?\\&#/]?)([\\w\\,]+)\\}");
    private static final long serialVersionUID = -1007874653930162262L;
    private final TemplateVariables variables;
    private String baseUri;

    public UriTemplate(String template) {
        Assert.hasText(template, "Template must not be null or empty!");
        Matcher matcher = VARIABLE_REGEX.matcher(template);
        int baseUriEndIndex = template.length();
        List<TemplateVariable> variables = new ArrayList<>();
        while (matcher.find()) {
            int start = matcher.start(0);
            TemplateVariable.VariableType type = TemplateVariable.VariableType.from(matcher.group(1));
            String[] names = matcher.group(2).split(",");
            for (String name : names) {
                TemplateVariable variable = new TemplateVariable(name, type);
                if (!variable.isRequired() && start < baseUriEndIndex) {
                    baseUriEndIndex = start;
                }
                variables.add(variable);
            }
        }
        this.variables = variables.isEmpty() ? TemplateVariables.NONE : new TemplateVariables(variables);
        this.baseUri = template.substring(0, baseUriEndIndex);
    }

    public UriTemplate(String baseUri, TemplateVariables variables) {
        Assert.hasText(baseUri, "Base URI must not be null or empty!");
        this.baseUri = baseUri;
        this.variables = variables == null ? TemplateVariables.NONE : variables;
    }

    public UriTemplate with(TemplateVariables variables) {
        if (variables == null) {
            return this;
        }
        UriComponents components = UriComponentsBuilder.fromUriString(this.baseUri).build();
        List<TemplateVariable> result = new ArrayList<>();
        Iterator<TemplateVariable> it = variables.iterator();
        while (it.hasNext()) {
            TemplateVariable variable = it.next();
            boolean isRequestParam = variable.isRequestParameterVariable();
            boolean alreadyPresent = components.getQueryParams().containsKey(variable.getName());
            if (!isRequestParam || !alreadyPresent) {
                if (!variable.isFragment() || !StringUtils.hasText(components.getFragment())) {
                    result.add(variable);
                }
            }
        }
        return new UriTemplate(this.baseUri, this.variables.concat(result));
    }

    public UriTemplate with(String variableName, TemplateVariable.VariableType type) {
        return with(new TemplateVariables(new TemplateVariable(variableName, type)));
    }

    public static boolean isTemplate(String candidate) {
        if (!StringUtils.hasText(candidate)) {
            return false;
        }
        return VARIABLE_REGEX.matcher(candidate).find();
    }

    public List<TemplateVariable> getVariables() {
        return this.variables.asList();
    }

    public List<String> getVariableNames() {
        List<String> names = new ArrayList<>();
        Iterator<TemplateVariable> it = this.variables.iterator();
        while (it.hasNext()) {
            TemplateVariable variable = it.next();
            names.add(variable.getName());
        }
        return names;
    }

    public URI expand(Object... parameters) throws IllegalArgumentException {
        if (TemplateVariables.NONE.equals(this.variables)) {
            return URI.create(this.baseUri);
        }
        org.springframework.web.util.UriTemplate baseTemplate = new org.springframework.web.util.UriTemplate(this.baseUri);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(baseTemplate.expand(parameters));
        Iterator<Object> iterator = Arrays.asList(parameters).iterator();
        Iterator<TemplateVariable> it = getOptionalVariables().iterator();
        while (it.hasNext()) {
            TemplateVariable variable = it.next();
            Object value = iterator.hasNext() ? iterator.next() : null;
            appendToBuilder(builder, variable, value);
        }
        return builder.build().toUri();
    }

    public URI expand(Map<String, ? extends Object> parameters) throws IllegalArgumentException {
        if (TemplateVariables.NONE.equals(this.variables)) {
            return URI.create(this.baseUri);
        }
        Assert.notNull(parameters, "Parameters must not be null!");
        org.springframework.web.util.UriTemplate baseTemplate = new org.springframework.web.util.UriTemplate(this.baseUri);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(baseTemplate.expand(parameters));
        Iterator<TemplateVariable> it = getOptionalVariables().iterator();
        while (it.hasNext()) {
            TemplateVariable variable = it.next();
            appendToBuilder(builder, variable, parameters.get(variable.getName()));
        }
        return builder.build().toUri();
    }

    @Override // java.lang.Iterable
    public Iterator<TemplateVariable> iterator() {
        return this.variables.iterator();
    }

    public String toString() {
        UriComponents components = UriComponentsBuilder.fromUriString(this.baseUri).build();
        boolean hasQueryParameters = !components.getQueryParams().isEmpty();
        return this.baseUri + getOptionalVariables().toString(hasQueryParameters);
    }

    private TemplateVariables getOptionalVariables() {
        List<TemplateVariable> result = new ArrayList<>();
        Iterator<TemplateVariable> it = iterator();
        while (it.hasNext()) {
            TemplateVariable variable = it.next();
            if (!variable.isRequired()) {
                result.add(variable);
            }
        }
        return new TemplateVariables(result);
    }

    private static void appendToBuilder(UriComponentsBuilder builder, TemplateVariable variable, Object value) throws IllegalArgumentException {
        if (value == null) {
            if (variable.isRequired()) {
                throw new IllegalArgumentException(String.format("Template variable %s is required but no value was given!", variable.getName()));
            }
            return;
        }
        switch (variable.getType()) {
            case REQUEST_PARAM:
            case REQUEST_PARAM_CONTINUED:
                builder.queryParam(variable.getName(), value);
                return;
            case PATH_VARIABLE:
            case SEGMENT:
                builder.pathSegment(value.toString());
                return;
            case FRAGMENT:
                builder.fragment(value.toString());
                return;
            default:
                return;
        }
    }
}
