package springfox.documentation.spring.web.plugins;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import springfox.documentation.RequestHandler;
import springfox.documentation.spi.service.contexts.ApiSelector;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/plugins/ApiSelectorBuilder.class */
public class ApiSelectorBuilder {
    private final Docket parent;
    private Predicate<RequestHandler> requestHandlerSelector = ApiSelector.DEFAULT.getRequestHandlerSelector();
    private Predicate<String> pathSelector = ApiSelector.DEFAULT.getPathSelector();

    public ApiSelectorBuilder(Docket parent) {
        this.parent = parent;
    }

    public ApiSelectorBuilder apis(Predicate<RequestHandler> selector) {
        this.requestHandlerSelector = Predicates.and(this.requestHandlerSelector, selector);
        return this;
    }

    public ApiSelectorBuilder paths(Predicate<String> selector) {
        this.pathSelector = Predicates.and(this.pathSelector, selector);
        return this;
    }

    public Docket build() {
        return this.parent.selector(new ApiSelector(combine(this.requestHandlerSelector, this.pathSelector), this.pathSelector));
    }

    private Predicate<RequestHandler> combine(Predicate<RequestHandler> requestHandlerSelector, Predicate<String> pathSelector) {
        return Predicates.and(requestHandlerSelector, transform(pathSelector));
    }

    private Predicate<RequestHandler> transform(final Predicate<String> pathSelector) {
        return new Predicate<RequestHandler>() { // from class: springfox.documentation.spring.web.plugins.ApiSelectorBuilder.1
            @Override // com.google.common.base.Predicate
            public boolean apply(RequestHandler input) {
                return Iterables.any(input.getRequestMapping().getPatternsCondition().getPatterns(), pathSelector);
            }
        };
    }
}
