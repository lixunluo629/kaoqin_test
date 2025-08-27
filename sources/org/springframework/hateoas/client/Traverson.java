package org.springframework.hateoas.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.LinkDiscoverers;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.client.Rels;
import org.springframework.hateoas.hal.HalLinkDiscoverer;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.plugin.core.OrderAwarePluginRegistry;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/client/Traverson.class */
public class Traverson {
    private static final LinkDiscoverers DEFAULT_LINK_DISCOVERERS;
    private final URI baseUri;
    private final List<MediaType> mediaTypes;
    private RestOperations operations;
    private LinkDiscoverers discoverers;

    static {
        LinkDiscoverer discoverer = new HalLinkDiscoverer();
        DEFAULT_LINK_DISCOVERERS = new LinkDiscoverers(OrderAwarePluginRegistry.create(Arrays.asList(discoverer)));
    }

    public Traverson(URI baseUri, MediaType... mediaTypes) {
        this(baseUri, (List<MediaType>) Arrays.asList(mediaTypes));
    }

    public Traverson(URI baseUri, List<MediaType> mediaTypes) {
        Assert.notNull(baseUri, "Base URI must not be null!");
        Assert.notEmpty(mediaTypes, "At least one media type must be given!");
        this.mediaTypes = mediaTypes;
        this.baseUri = baseUri;
        this.discoverers = DEFAULT_LINK_DISCOVERERS;
        setRestOperations(createDefaultTemplate(this.mediaTypes));
    }

    public static List<HttpMessageConverter<?>> getDefaultMessageConverters(MediaType... mediaTypes) {
        return getDefaultMessageConverters((List<MediaType>) Arrays.asList(mediaTypes));
    }

    public static List<HttpMessageConverter<?>> getDefaultMessageConverters(List<MediaType> mediaTypes) {
        Assert.notNull(mediaTypes, "Media types must not be null!");
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        if (mediaTypes.contains(MediaTypes.HAL_JSON)) {
            converters.add(getHalConverter());
        }
        return converters;
    }

    private static final RestOperations createDefaultTemplate(List<MediaType> mediaTypes) {
        RestTemplate template = new RestTemplate();
        template.setMessageConverters(getDefaultMessageConverters(mediaTypes));
        return template;
    }

    private static final HttpMessageConverter<?> getHalConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jackson2HalModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);
        converter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
        return converter;
    }

    public Traverson setRestOperations(RestOperations operations) {
        this.operations = operations == null ? createDefaultTemplate(this.mediaTypes) : operations;
        return this;
    }

    public Traverson setLinkDiscoverers(List<? extends LinkDiscoverer> discoverer) {
        this.discoverers = this.discoverers == null ? DEFAULT_LINK_DISCOVERERS : new LinkDiscoverers(OrderAwarePluginRegistry.create((List) discoverer));
        return this;
    }

    public TraversalBuilder follow(String... rels) {
        return new TraversalBuilder().follow(rels);
    }

    public TraversalBuilder follow(Hop hop) {
        return new TraversalBuilder().follow(hop);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HttpEntity<?> prepareRequest(HttpHeaders headers) {
        HttpHeaders toSend = new HttpHeaders();
        toSend.putAll(headers);
        if (headers.getAccept().isEmpty()) {
            toSend.setAccept(this.mediaTypes);
        }
        return new HttpEntity<>((MultiValueMap<String, String>) toSend);
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/client/Traverson$TraversalBuilder.class */
    public class TraversalBuilder {
        private List<Hop> rels;
        private Map<String, Object> templateParameters;
        private HttpHeaders headers;

        private TraversalBuilder() {
            this.rels = new ArrayList();
            this.templateParameters = new HashMap();
            this.headers = new HttpHeaders();
        }

        public TraversalBuilder follow(String... rels) {
            Assert.notNull(rels, "Rels must not be null!");
            for (String rel : rels) {
                this.rels.add(Hop.rel(rel));
            }
            return this;
        }

        public TraversalBuilder follow(Hop hop) {
            Assert.notNull(hop, "Hop must not be null!");
            this.rels.add(hop);
            return this;
        }

        public TraversalBuilder withTemplateParameters(Map<String, Object> parameters) {
            this.templateParameters = parameters;
            return this;
        }

        public TraversalBuilder withHeaders(HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        public <T> T toObject(Class<T> type) {
            Assert.notNull(type, "Target type must not be null!");
            return Traverson.this.operations.exchange(traverseToExpandedFinalUrl(), HttpMethod.GET, Traverson.this.prepareRequest(this.headers), type).getBody();
        }

        public <T> T toObject(ParameterizedTypeReference<T> type) {
            Assert.notNull(type, "Target type must not be null!");
            return Traverson.this.operations.exchange(traverseToExpandedFinalUrl(), HttpMethod.GET, Traverson.this.prepareRequest(this.headers), type).getBody();
        }

        public <T> T toObject(String str) {
            Assert.hasText(str, "JSON path must not be null or empty!");
            return (T) JsonPath.read((String) Traverson.this.operations.exchange(traverseToExpandedFinalUrl(), HttpMethod.GET, Traverson.this.prepareRequest(this.headers), String.class).getBody(), str, new Predicate[0]);
        }

        public <T> ResponseEntity<T> toEntity(Class<T> type) {
            Assert.notNull(type, "Target type must not be null!");
            return Traverson.this.operations.exchange(traverseToExpandedFinalUrl(), HttpMethod.GET, Traverson.this.prepareRequest(this.headers), type);
        }

        public Link asLink() {
            return traverseToLink(true);
        }

        public Link asTemplatedLink() {
            return traverseToLink(false);
        }

        private Link traverseToLink(boolean expandFinalUrl) {
            Assert.isTrue(this.rels.size() > 0, "At least one rel needs to be provided!");
            return new Link(expandFinalUrl ? traverseToExpandedFinalUrl().toString() : traverseToFinalUrl(), this.rels.get(this.rels.size() - 1).getRel());
        }

        private String traverseToFinalUrl() throws RestClientException {
            String uri = getAndFindLinkWithRel(Traverson.this.baseUri.toString(), this.rels.iterator());
            return new UriTemplate(uri).toString();
        }

        private URI traverseToExpandedFinalUrl() throws RestClientException {
            String uri = getAndFindLinkWithRel(Traverson.this.baseUri.toString(), this.rels.iterator());
            return new UriTemplate(uri).expand(this.templateParameters);
        }

        private String getAndFindLinkWithRel(String uri, Iterator<Hop> rels) throws RestClientException {
            if (rels.hasNext()) {
                HttpEntity<?> request = Traverson.this.prepareRequest(this.headers);
                UriTemplate template = new UriTemplate(uri);
                ResponseEntity<String> responseEntity = Traverson.this.operations.exchange(template.expand(new Object[0]), HttpMethod.GET, request, String.class);
                MediaType contentType = responseEntity.getHeaders().getContentType();
                String responseBody = responseEntity.getBody();
                Hop thisHop = rels.next();
                Rels.Rel rel = Rels.getRelFor(thisHop.getRel(), Traverson.this.discoverers);
                Link link = rel.findInResponse(responseBody, contentType);
                if (link == null) {
                    throw new IllegalStateException(String.format("Expected to find link with rel '%s' in response %s!", rel, responseBody));
                }
                if (!thisHop.hasParameters()) {
                    return getAndFindLinkWithRel(link.getHref(), rels);
                }
                return getAndFindLinkWithRel(link.expand(thisHop.getMergedParameters(this.templateParameters)).getHref(), rels);
            }
            return uri;
        }
    }
}
