package org.springframework.boot.autoconfigure.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.cookie.Cookie2;
import org.aspectj.weaver.model.AsmRelationshipUtils;
import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProviders;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.util.HtmlUtils;

@AutoConfigureBefore({WebMvcAutoConfiguration.class})
@EnableConfigurationProperties({ResourceProperties.class})
@Configuration
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ErrorMvcAutoConfiguration.class */
public class ErrorMvcAutoConfiguration {
    private final ServerProperties serverProperties;
    private final List<ErrorViewResolver> errorViewResolvers;

    public ErrorMvcAutoConfiguration(ServerProperties serverProperties, ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
        this.serverProperties = serverProperties;
        this.errorViewResolvers = errorViewResolversProvider.getIfAvailable();
    }

    @ConditionalOnMissingBean(value = {ErrorAttributes.class}, search = SearchStrategy.CURRENT)
    @Bean
    public DefaultErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes();
    }

    @ConditionalOnMissingBean(value = {ErrorController.class}, search = SearchStrategy.CURRENT)
    @Bean
    public BasicErrorController basicErrorController(ErrorAttributes errorAttributes) {
        return new BasicErrorController(errorAttributes, this.serverProperties.getError(), this.errorViewResolvers);
    }

    @Bean
    public ErrorPageCustomizer errorPageCustomizer() {
        return new ErrorPageCustomizer(this.serverProperties);
    }

    @Bean
    public static PreserveErrorControllerTargetClassPostProcessor preserveErrorControllerTargetClassPostProcessor() {
        return new PreserveErrorControllerTargetClassPostProcessor();
    }

    @Configuration
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ErrorMvcAutoConfiguration$DefaultErrorViewResolverConfiguration.class */
    static class DefaultErrorViewResolverConfiguration {
        private final ApplicationContext applicationContext;
        private final ResourceProperties resourceProperties;

        DefaultErrorViewResolverConfiguration(ApplicationContext applicationContext, ResourceProperties resourceProperties) {
            this.applicationContext = applicationContext;
            this.resourceProperties = resourceProperties;
        }

        @ConditionalOnMissingBean
        @ConditionalOnBean({DispatcherServlet.class})
        @Bean
        public DefaultErrorViewResolver conventionErrorViewResolver() {
            return new DefaultErrorViewResolver(this.applicationContext, this.resourceProperties);
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = "server.error.whitelabel", name = {"enabled"}, matchIfMissing = true)
    @Conditional({ErrorTemplateMissingCondition.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ErrorMvcAutoConfiguration$WhitelabelErrorViewConfiguration.class */
    protected static class WhitelabelErrorViewConfiguration {
        private final SpelView defaultErrorView = new SpelView("<html><body><h1>Whitelabel Error Page</h1><p>This application has no explicit mapping for /error, so you are seeing this as a fallback.</p><div id='created'>${timestamp}</div><div>There was an unexpected error (type=${error}, status=${status}).</div><div>${message}</div></body></html>");

        protected WhitelabelErrorViewConfiguration() {
        }

        @ConditionalOnMissingBean(name = {AsmRelationshipUtils.DECLARE_ERROR})
        @Bean(name = {AsmRelationshipUtils.DECLARE_ERROR})
        public View defaultErrorView() {
            return this.defaultErrorView;
        }

        @ConditionalOnMissingBean({BeanNameViewResolver.class})
        @Bean
        public BeanNameViewResolver beanNameViewResolver() {
            BeanNameViewResolver resolver = new BeanNameViewResolver();
            resolver.setOrder(2147483637);
            return resolver;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ErrorMvcAutoConfiguration$ErrorTemplateMissingCondition.class */
    private static class ErrorTemplateMissingCondition extends SpringBootCondition {
        private ErrorTemplateMissingCondition() {
        }

        @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ConditionMessage.Builder message = ConditionMessage.forCondition("ErrorTemplate Missing", new Object[0]);
            TemplateAvailabilityProviders providers = new TemplateAvailabilityProviders(context.getClassLoader());
            TemplateAvailabilityProvider provider = providers.getProvider(AsmRelationshipUtils.DECLARE_ERROR, context.getEnvironment(), context.getClassLoader(), context.getResourceLoader());
            if (provider != null) {
                return ConditionOutcome.noMatch(message.foundExactly("template from " + provider));
            }
            return ConditionOutcome.match(message.didNotFind("error template view").atAll());
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ErrorMvcAutoConfiguration$SpelView.class */
    private static class SpelView implements View {
        private final NonRecursivePropertyPlaceholderHelper helper = new NonRecursivePropertyPlaceholderHelper("${", "}");
        private final String template;
        private volatile Map<String, Expression> expressions;

        SpelView(String template) {
            this.template = template;
        }

        @Override // org.springframework.web.servlet.View
        public String getContentType() {
            return "text/html";
        }

        @Override // org.springframework.web.servlet.View
        public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
            if (response.getContentType() == null) {
                response.setContentType(getContentType());
            }
            Map<String, Object> map = new HashMap<>(model);
            map.put(Cookie2.PATH, request.getContextPath());
            PropertyPlaceholderHelper.PlaceholderResolver resolver = new ExpressionResolver(getExpressions(), map);
            String result = this.helper.replacePlaceholders(this.template, resolver);
            response.getWriter().append((CharSequence) result);
        }

        private Map<String, Expression> getExpressions() {
            if (this.expressions == null) {
                synchronized (this) {
                    ExpressionCollector expressionCollector = new ExpressionCollector();
                    this.helper.replacePlaceholders(this.template, expressionCollector);
                    this.expressions = expressionCollector.getExpressions();
                }
            }
            return this.expressions;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ErrorMvcAutoConfiguration$ExpressionCollector.class */
    private static class ExpressionCollector implements PropertyPlaceholderHelper.PlaceholderResolver {
        private final SpelExpressionParser parser;
        private final Map<String, Expression> expressions;

        private ExpressionCollector() {
            this.parser = new SpelExpressionParser();
            this.expressions = new HashMap();
        }

        @Override // org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver
        public String resolvePlaceholder(String name) {
            this.expressions.put(name, this.parser.parseExpression(name));
            return null;
        }

        public Map<String, Expression> getExpressions() {
            return Collections.unmodifiableMap(this.expressions);
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ErrorMvcAutoConfiguration$ExpressionResolver.class */
    private static class ExpressionResolver implements PropertyPlaceholderHelper.PlaceholderResolver {
        private final Map<String, Expression> expressions;
        private final EvaluationContext context;

        ExpressionResolver(Map<String, Expression> expressions, Map<String, ?> map) {
            this.expressions = expressions;
            this.context = getContext(map);
        }

        private EvaluationContext getContext(Map<String, ?> map) {
            return SimpleEvaluationContext.forPropertyAccessors(new MapAccessor()).withRootObject(map).build();
        }

        @Override // org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver
        public String resolvePlaceholder(String placeholderName) {
            Expression expression = this.expressions.get(placeholderName);
            return escape(expression != null ? expression.getValue(this.context) : null);
        }

        private String escape(Object value) {
            return HtmlUtils.htmlEscape(value != null ? value.toString() : null);
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ErrorMvcAutoConfiguration$ErrorPageCustomizer.class */
    private static class ErrorPageCustomizer implements ErrorPageRegistrar, Ordered {
        private final ServerProperties properties;

        protected ErrorPageCustomizer(ServerProperties properties) {
            this.properties = properties;
        }

        @Override // org.springframework.boot.web.servlet.ErrorPageRegistrar
        public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
            ErrorPage errorPage = new ErrorPage(this.properties.getServletPrefix() + this.properties.getError().getPath());
            errorPageRegistry.addErrorPages(errorPage);
        }

        @Override // org.springframework.core.Ordered
        public int getOrder() {
            return 0;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ErrorMvcAutoConfiguration$PreserveErrorControllerTargetClassPostProcessor.class */
    static class PreserveErrorControllerTargetClassPostProcessor implements BeanFactoryPostProcessor {
        PreserveErrorControllerTargetClassPostProcessor() {
        }

        @Override // org.springframework.beans.factory.config.BeanFactoryPostProcessor
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            String[] errorControllerBeans = beanFactory.getBeanNamesForType(ErrorController.class, false, false);
            for (String errorControllerBean : errorControllerBeans) {
                try {
                    beanFactory.getBeanDefinition(errorControllerBean).setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);
                } catch (Throwable th) {
                }
            }
        }
    }
}
