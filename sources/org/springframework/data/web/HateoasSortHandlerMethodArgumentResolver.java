package org.springframework.data.web;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.mvc.UriComponentsContributor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/HateoasSortHandlerMethodArgumentResolver.class */
public class HateoasSortHandlerMethodArgumentResolver extends SortHandlerMethodArgumentResolver implements UriComponentsContributor {
    public TemplateVariables getSortTemplateVariables(MethodParameter parameter, UriComponents template) {
        String sortParameter = getSortParameter(parameter);
        MultiValueMap<String, String> queryParameters = template.getQueryParams();
        boolean append = !queryParameters.isEmpty();
        if (queryParameters.containsKey(sortParameter)) {
            return TemplateVariables.NONE;
        }
        String description = String.format("pagination.%s.description", sortParameter);
        TemplateVariable.VariableType type = append ? TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED : TemplateVariable.VariableType.REQUEST_PARAM;
        return new TemplateVariables(new TemplateVariable(sortParameter, type, description));
    }

    @Override // org.springframework.hateoas.mvc.UriComponentsContributor
    public void enhance(UriComponentsBuilder builder, MethodParameter parameter, Object value) {
        if (!(value instanceof Sort)) {
            return;
        }
        Sort sort = (Sort) value;
        String sortParameter = getSortParameter(parameter);
        builder.replaceQueryParam(sortParameter, new Object[0]);
        for (String expression : foldIntoExpressions(sort)) {
            builder.queryParam(sortParameter, expression);
        }
    }
}
