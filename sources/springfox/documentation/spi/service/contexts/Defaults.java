package springfox.documentation.spi.service.contexts;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListingReference;
import springfox.documentation.service.Operation;
import springfox.documentation.service.ResponseMessage;

/* loaded from: springfox-spi-2.2.2.jar:springfox/documentation/spi/service/contexts/Defaults.class */
public class Defaults {
    private HashSet<Class> ignored;
    private LinkedHashMap<RequestMethod, List<ResponseMessage>> responses;
    private List<Class<? extends Annotation>> annotations;
    private Ordering<Operation> operationOrdering;
    private Ordering<ApiDescription> apiDescriptionOrdering;
    private Ordering<ApiListingReference> apiListingReferenceOrdering;

    public Defaults() {
        init();
    }

    public Set<Class> defaultIgnorableParameterTypes() {
        return this.ignored;
    }

    public Map<RequestMethod, List<ResponseMessage>> defaultResponseMessages() {
        return this.responses;
    }

    public List<Class<? extends Annotation>> defaultExcludeAnnotations() {
        return this.annotations;
    }

    public Ordering<Operation> operationOrdering() {
        return this.operationOrdering;
    }

    public Ordering<ApiDescription> apiDescriptionOrdering() {
        return this.apiDescriptionOrdering;
    }

    public Ordering<ApiListingReference> apiListingReferenceOrdering() {
        return this.apiListingReferenceOrdering;
    }

    public List<AlternateTypeRule> defaultRules(TypeResolver typeResolver) {
        List<AlternateTypeRule> rules = Lists.newArrayList();
        rules.add(AlternateTypeRules.newRule(typeResolver.resolve(Map.class, new Type[0]), typeResolver.resolve(Object.class, new Type[0])));
        rules.add(AlternateTypeRules.newRule(typeResolver.resolve(Map.class, String.class, Object.class), typeResolver.resolve(Object.class, new Type[0])));
        rules.add(AlternateTypeRules.newRule(typeResolver.resolve(Map.class, Object.class, Object.class), typeResolver.resolve(Object.class, new Type[0])));
        rules.add(AlternateTypeRules.newRule(typeResolver.resolve(ResponseEntity.class, WildcardType.class), typeResolver.resolve(WildcardType.class, new Type[0])));
        rules.add(AlternateTypeRules.newRule(typeResolver.resolve(HttpEntity.class, WildcardType.class), typeResolver.resolve(WildcardType.class, new Type[0])));
        return rules;
    }

    private void init() {
        initIgnorableTypes();
        initResponseMessages();
        initExcludeAnnotations();
        initOrderings();
    }

    private void initOrderings() {
        this.operationOrdering = Ordering.from(Orderings.positionComparator()).compound(Orderings.nickNameComparator());
        this.apiDescriptionOrdering = Ordering.from(Orderings.apiPathCompatator());
        this.apiListingReferenceOrdering = Ordering.from(Orderings.listingPositionComparator()).compound(Orderings.listingReferencePathComparator());
    }

    private void initExcludeAnnotations() {
        this.annotations = new ArrayList();
        this.annotations.add(ApiIgnore.class);
    }

    private void initIgnorableTypes() {
        this.ignored = Sets.newHashSet();
        this.ignored.add(ServletRequest.class);
        this.ignored.add(Class.class);
        this.ignored.add(Void.class);
        this.ignored.add(Void.TYPE);
        this.ignored.add(HttpHeaders.class);
        this.ignored.add(ServletResponse.class);
        this.ignored.add(HttpServletRequest.class);
        this.ignored.add(HttpServletResponse.class);
        this.ignored.add(HttpHeaders.class);
        this.ignored.add(BindingResult.class);
        this.ignored.add(ServletContext.class);
        this.ignored.add(UriComponentsBuilder.class);
        this.ignored.add(ApiIgnore.class);
    }

    private void initResponseMessages() {
        this.responses = Maps.newLinkedHashMap();
        this.responses.put(RequestMethod.GET, Arrays.asList(new ResponseMessageBuilder().code(HttpStatus.OK.value()).message(HttpStatus.OK.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(null).build()));
        this.responses.put(RequestMethod.PUT, Arrays.asList(new ResponseMessageBuilder().code(HttpStatus.CREATED.value()).message(HttpStatus.CREATED.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(null).build()));
        this.responses.put(RequestMethod.POST, Arrays.asList(new ResponseMessageBuilder().code(HttpStatus.CREATED.value()).message(HttpStatus.CREATED.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.NOT_FOUND.value()).message(HttpStatus.NOT_FOUND.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(null).build()));
        this.responses.put(RequestMethod.DELETE, Arrays.asList(new ResponseMessageBuilder().code(HttpStatus.NO_CONTENT.value()).message(HttpStatus.NO_CONTENT.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(null).build()));
        this.responses.put(RequestMethod.PATCH, Arrays.asList(new ResponseMessageBuilder().code(HttpStatus.NO_CONTENT.value()).message(HttpStatus.NO_CONTENT.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(null).build()));
        this.responses.put(RequestMethod.TRACE, Arrays.asList(new ResponseMessageBuilder().code(HttpStatus.NO_CONTENT.value()).message(HttpStatus.NO_CONTENT.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(null).build()));
        this.responses.put(RequestMethod.OPTIONS, Arrays.asList(new ResponseMessageBuilder().code(HttpStatus.NO_CONTENT.value()).message(HttpStatus.NO_CONTENT.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(null).build()));
        this.responses.put(RequestMethod.HEAD, Arrays.asList(new ResponseMessageBuilder().code(HttpStatus.NO_CONTENT.value()).message(HttpStatus.NO_CONTENT.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value()).message(HttpStatus.FORBIDDEN.getReasonPhrase()).responseModel(null).build(), new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseModel(null).build()));
    }
}
