package springfox.documentation.spring.web.plugins;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.PathProvider;
import springfox.documentation.annotations.Incubating;
import springfox.documentation.builders.BuilderDefaults;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.CodeGenGenericTypeNamingStrategy;
import springfox.documentation.schema.DefaultGenericTypeNamingStrategy;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiListingReference;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.GenericTypeNamingStrategy;
import springfox.documentation.spi.service.DocumentationPlugin;
import springfox.documentation.spi.service.contexts.ApiSelector;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spi.service.contexts.DocumentationContextBuilder;
import springfox.documentation.spi.service.contexts.SecurityContext;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/plugins/Docket.class */
public class Docket implements DocumentationPlugin {
    public static final String DEFAULT_GROUP_NAME = "default";
    private final DocumentationType documentationType;
    private PathProvider pathProvider;
    private List<? extends SecurityScheme> securitySchemes;
    private Ordering<ApiListingReference> apiListingReferenceOrdering;
    private Ordering<ApiDescription> apiDescriptionOrdering;
    private Ordering<Operation> operationOrdering;
    private ApiInfo apiInfo = ApiInfo.DEFAULT;
    private String groupName = "default";
    private boolean enabled = true;
    private GenericTypeNamingStrategy genericsNamingStrategy = new DefaultGenericTypeNamingStrategy();
    private boolean applyDefaultResponseMessages = true;
    private final List<SecurityContext> securityContexts = Lists.newArrayList();
    private final Map<RequestMethod, List<ResponseMessage>> responseMessages = Maps.newHashMap();
    private final List<Parameter> globalOperationParameters = Lists.newArrayList();
    private final List<Function<TypeResolver, AlternateTypeRule>> ruleBuilders = Lists.newArrayList();
    private final Set<Class> ignorableParameterTypes = Sets.newHashSet();
    private final Set<String> protocols = Sets.newHashSet();
    private final Set<String> produces = Sets.newHashSet();
    private final Set<String> consumes = Sets.newHashSet();
    private Optional<String> pathMapping = Optional.absent();
    private ApiSelector apiSelector = ApiSelector.DEFAULT;
    private boolean enableUrlTemplating = false;

    public Docket(DocumentationType documentationType) {
        this.documentationType = documentationType;
    }

    public Docket apiInfo(ApiInfo apiInfo) {
        this.apiInfo = (ApiInfo) BuilderDefaults.defaultIfAbsent(apiInfo, apiInfo);
        return this;
    }

    public Docket securitySchemes(List<? extends SecurityScheme> securitySchemes) {
        this.securitySchemes = securitySchemes;
        return this;
    }

    public Docket securityContexts(List<SecurityContext> securityContexts) {
        this.securityContexts.addAll(securityContexts);
        return this;
    }

    public Docket groupName(String groupName) {
        this.groupName = (String) BuilderDefaults.defaultIfAbsent(groupName, this.groupName);
        return this;
    }

    public Docket pathProvider(PathProvider pathProvider) {
        this.pathProvider = pathProvider;
        return this;
    }

    public Docket globalResponseMessage(RequestMethod requestMethod, List<ResponseMessage> responseMessages) {
        this.responseMessages.put(requestMethod, responseMessages);
        return this;
    }

    public Docket globalOperationParameters(List<Parameter> operationParameters) {
        this.globalOperationParameters.addAll(BuilderDefaults.nullToEmptyList(operationParameters));
        return this;
    }

    public Docket ignoredParameterTypes(Class... classes) {
        this.ignorableParameterTypes.addAll(Arrays.asList(classes));
        return this;
    }

    public Docket produces(Set<String> produces) {
        this.produces.addAll(produces);
        return this;
    }

    public Docket consumes(Set<String> consumes) {
        this.consumes.addAll(consumes);
        return this;
    }

    public Docket protocols(Set<String> protocols) {
        this.protocols.addAll(protocols);
        return this;
    }

    public Docket alternateTypeRules(AlternateTypeRule... alternateTypeRules) {
        this.ruleBuilders.addAll(FluentIterable.from(Lists.newArrayList(alternateTypeRules)).transform(identityRuleBuilder()).toList());
        return this;
    }

    public Docket operationOrdering(Ordering<Operation> operationOrdering) {
        this.operationOrdering = operationOrdering;
        return this;
    }

    private Function<AlternateTypeRule, Function<TypeResolver, AlternateTypeRule>> identityRuleBuilder() {
        return new Function<AlternateTypeRule, Function<TypeResolver, AlternateTypeRule>>() { // from class: springfox.documentation.spring.web.plugins.Docket.1
            @Override // com.google.common.base.Function
            public Function<TypeResolver, AlternateTypeRule> apply(AlternateTypeRule rule) {
                return Docket.this.identityFunction(rule);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Function<TypeResolver, AlternateTypeRule> identityFunction(final AlternateTypeRule rule) {
        return new Function<TypeResolver, AlternateTypeRule>() { // from class: springfox.documentation.spring.web.plugins.Docket.2
            @Override // com.google.common.base.Function
            public AlternateTypeRule apply(TypeResolver typeResolver) {
                return rule;
            }
        };
    }

    public Docket directModelSubstitute(Class clazz, Class with) {
        this.ruleBuilders.add(newSubstitutionFunction(clazz, with));
        return this;
    }

    public Docket genericModelSubstitutes(Class... genericClasses) {
        for (Class clz : genericClasses) {
            this.ruleBuilders.add(newGenericSubstitutionFunction(clz));
        }
        return this;
    }

    public Docket useDefaultResponseMessages(boolean apply) {
        this.applyDefaultResponseMessages = apply;
        return this;
    }

    public Docket apiListingReferenceOrdering(Ordering<ApiListingReference> apiListingReferenceOrdering) {
        this.apiListingReferenceOrdering = apiListingReferenceOrdering;
        return this;
    }

    public Docket apiDescriptionOrdering(Ordering<ApiDescription> apiDescriptionOrdering) {
        this.apiDescriptionOrdering = apiDescriptionOrdering;
        return this;
    }

    public Docket enable(boolean externallyConfiguredFlag) {
        this.enabled = externallyConfiguredFlag;
        return this;
    }

    public Docket forCodeGeneration(boolean forCodeGen) {
        if (forCodeGen) {
            this.genericsNamingStrategy = new CodeGenGenericTypeNamingStrategy();
        }
        return this;
    }

    public Docket pathMapping(String path) {
        this.pathMapping = Optional.fromNullable(path);
        return this;
    }

    @Incubating("2.1.0")
    public Docket enableUrlTemplating(boolean enabled) {
        this.enableUrlTemplating = enabled;
        return this;
    }

    Docket selector(ApiSelector apiSelector) {
        this.apiSelector = apiSelector;
        return this;
    }

    public ApiSelectorBuilder select() {
        return new ApiSelectorBuilder(this);
    }

    @Override // springfox.documentation.spi.service.DocumentationPlugin
    public DocumentationContext configure(DocumentationContextBuilder builder) {
        return builder.apiInfo(this.apiInfo).selector(this.apiSelector).applyDefaultResponseMessages(this.applyDefaultResponseMessages).additionalResponseMessages(this.responseMessages).additionalOperationParameters(this.globalOperationParameters).additionalIgnorableTypes(this.ignorableParameterTypes).ruleBuilders(this.ruleBuilders).groupName(this.groupName).pathProvider(this.pathProvider).securityContexts(this.securityContexts).securitySchemes(this.securitySchemes).apiListingReferenceOrdering(this.apiListingReferenceOrdering).apiDescriptionOrdering(this.apiDescriptionOrdering).operationOrdering(this.operationOrdering).produces(this.produces).consumes(this.consumes).protocols(this.protocols).genericsNaming(this.genericsNamingStrategy).pathMapping(this.pathMapping).enableUrlTemplating(this.enableUrlTemplating).build();
    }

    @Override // springfox.documentation.spi.service.DocumentationPlugin
    public String getGroupName() {
        return this.groupName;
    }

    @Override // springfox.documentation.spi.service.DocumentationPlugin
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override // springfox.documentation.spi.service.DocumentationPlugin
    public DocumentationType getDocumentationType() {
        return this.documentationType;
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(DocumentationType delimiter) {
        return this.documentationType.equals(delimiter);
    }

    private Function<TypeResolver, AlternateTypeRule> newSubstitutionFunction(final Class clazz, final Class with) {
        return new Function<TypeResolver, AlternateTypeRule>() { // from class: springfox.documentation.spring.web.plugins.Docket.3
            @Override // com.google.common.base.Function
            public AlternateTypeRule apply(TypeResolver typeResolver) {
                return AlternateTypeRules.newRule(typeResolver.resolve(clazz, new Type[0]), typeResolver.resolve(with, new Type[0]));
            }
        };
    }

    private Function<TypeResolver, AlternateTypeRule> newGenericSubstitutionFunction(final Class clz) {
        return new Function<TypeResolver, AlternateTypeRule>() { // from class: springfox.documentation.spring.web.plugins.Docket.4
            @Override // com.google.common.base.Function
            public AlternateTypeRule apply(TypeResolver typeResolver) {
                return AlternateTypeRules.newRule(typeResolver.resolve(clz, WildcardType.class), typeResolver.resolve(WildcardType.class, new Type[0]));
            }
        };
    }
}
