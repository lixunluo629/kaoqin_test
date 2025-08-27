package org.springframework.data.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.mvc.UriComponentsContributor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/HateoasPageableHandlerMethodArgumentResolver.class */
public class HateoasPageableHandlerMethodArgumentResolver extends PageableHandlerMethodArgumentResolver implements UriComponentsContributor {
    private static final HateoasSortHandlerMethodArgumentResolver DEFAULT_SORT_RESOLVER = new HateoasSortHandlerMethodArgumentResolver();
    private final HateoasSortHandlerMethodArgumentResolver sortResolver;

    public HateoasPageableHandlerMethodArgumentResolver() {
        this(null);
    }

    public HateoasPageableHandlerMethodArgumentResolver(HateoasSortHandlerMethodArgumentResolver sortResolver) {
        super((SortHandlerMethodArgumentResolver) getDefaultedSortResolver(sortResolver));
        this.sortResolver = getDefaultedSortResolver(sortResolver);
    }

    public TemplateVariables getPaginationTemplateVariables(MethodParameter parameter, UriComponents template) {
        String pagePropertyName = getParameterNameToUse(getPageParameterName(), parameter);
        String sizePropertyName = getParameterNameToUse(getSizeParameterName(), parameter);
        List<TemplateVariable> names = new ArrayList<>();
        MultiValueMap<String, String> queryParameters = template.getQueryParams();
        boolean append = !queryParameters.isEmpty();
        for (String propertyName : Arrays.asList(pagePropertyName, sizePropertyName)) {
            if (!queryParameters.containsKey(propertyName)) {
                TemplateVariable.VariableType type = append ? TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED : TemplateVariable.VariableType.REQUEST_PARAM;
                String description = String.format("pagination.%s.description", propertyName);
                names.add(new TemplateVariable(propertyName, type, description));
            }
        }
        TemplateVariables pagingVariables = new TemplateVariables(names);
        return pagingVariables.concat(this.sortResolver.getSortTemplateVariables(parameter, template));
    }

    @Override // org.springframework.hateoas.mvc.UriComponentsContributor
    public void enhance(UriComponentsBuilder builder, MethodParameter parameter, Object value) {
        if (!(value instanceof Pageable)) {
            return;
        }
        Pageable pageable = (Pageable) value;
        String pagePropertyName = getParameterNameToUse(getPageParameterName(), parameter);
        String sizePropertyName = getParameterNameToUse(getSizeParameterName(), parameter);
        int pageNumber = pageable.getPageNumber();
        Object[] objArr = new Object[1];
        objArr[0] = Integer.valueOf(isOneIndexedParameters() ? pageNumber + 1 : pageNumber);
        builder.replaceQueryParam(pagePropertyName, objArr);
        Object[] objArr2 = new Object[1];
        objArr2[0] = Integer.valueOf(pageable.getPageSize() <= getMaxPageSize() ? pageable.getPageSize() : getMaxPageSize());
        builder.replaceQueryParam(sizePropertyName, objArr2);
        this.sortResolver.enhance(builder, parameter, pageable.getSort());
    }

    private static HateoasSortHandlerMethodArgumentResolver getDefaultedSortResolver(HateoasSortHandlerMethodArgumentResolver sortResolver) {
        return sortResolver == null ? DEFAULT_SORT_RESOLVER : sortResolver;
    }
}
