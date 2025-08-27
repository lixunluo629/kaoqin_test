package org.springframework.web.method.support;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/support/CompositeUriComponentsContributor.class */
public class CompositeUriComponentsContributor implements UriComponentsContributor {
    private final List<Object> contributors;
    private final ConversionService conversionService;

    public CompositeUriComponentsContributor(UriComponentsContributor... contributors) {
        this.contributors = new LinkedList();
        Collections.addAll(this.contributors, contributors);
        this.conversionService = new DefaultFormattingConversionService();
    }

    public CompositeUriComponentsContributor(Collection<?> contributors) {
        this(contributors, null);
    }

    public CompositeUriComponentsContributor(Collection<?> contributors, ConversionService cs) {
        this.contributors = new LinkedList();
        Assert.notNull(contributors, "'uriComponentsContributors' must not be null");
        this.contributors.addAll(contributors);
        this.conversionService = cs != null ? cs : new DefaultFormattingConversionService();
    }

    public boolean hasContributors() {
        return this.contributors.isEmpty();
    }

    @Override // org.springframework.web.method.support.UriComponentsContributor
    public boolean supportsParameter(MethodParameter parameter) {
        for (Object c : this.contributors) {
            if (c instanceof UriComponentsContributor) {
                UriComponentsContributor contributor = (UriComponentsContributor) c;
                if (contributor.supportsParameter(parameter)) {
                    return true;
                }
            } else if ((c instanceof HandlerMethodArgumentResolver) && ((HandlerMethodArgumentResolver) c).supportsParameter(parameter)) {
                return false;
            }
        }
        return false;
    }

    @Override // org.springframework.web.method.support.UriComponentsContributor
    public void contributeMethodArgument(MethodParameter parameter, Object value, UriComponentsBuilder builder, Map<String, Object> uriVariables, ConversionService conversionService) {
        for (Object contributor : this.contributors) {
            if (contributor instanceof UriComponentsContributor) {
                UriComponentsContributor ucc = (UriComponentsContributor) contributor;
                if (ucc.supportsParameter(parameter)) {
                    ucc.contributeMethodArgument(parameter, value, builder, uriVariables, conversionService);
                    return;
                }
            } else if ((contributor instanceof HandlerMethodArgumentResolver) && ((HandlerMethodArgumentResolver) contributor).supportsParameter(parameter)) {
                return;
            }
        }
    }

    public void contributeMethodArgument(MethodParameter parameter, Object value, UriComponentsBuilder builder, Map<String, Object> uriVariables) {
        contributeMethodArgument(parameter, value, builder, uriVariables, this.conversionService);
    }
}
