package org.springframework.hateoas.hal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.NonTypedScalarSerializerBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/Jackson2HalModule.class */
public class Jackson2HalModule extends SimpleModule {
    private static final long serialVersionUID = 7806951456457932384L;
    private static final Link CURIES_REQUIRED_DUE_TO_EMBEDS = new Link("__rel__", "¯\\_(ツ)_/¯");

    public Jackson2HalModule() {
        super("json-hal-module", new Version(1, 0, 0, null, "org.springframework.hateoas", "spring-hateoas"));
        setMixInAnnotation(Link.class, LinkMixin.class);
        setMixInAnnotation(ResourceSupport.class, ResourceSupportMixin.class);
        setMixInAnnotation(Resources.class, ResourcesMixin.class);
    }

    public static boolean isAlreadyRegisteredIn(ObjectMapper mapper) {
        Assert.notNull(mapper, "ObjectMapper must not be null!");
        return LinkMixin.class.equals(mapper.findMixInClassFor(Link.class));
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/Jackson2HalModule$HalLinkListSerializer.class */
    public static class HalLinkListSerializer extends ContainerSerializer<List<Link>> implements ContextualSerializer {
        private static final long serialVersionUID = -1844788111509966406L;
        private static final String RELATION_MESSAGE_TEMPLATE = "_links.%s.title";
        private final BeanProperty property;
        private final CurieProvider curieProvider;
        private final EmbeddedMapper mapper;
        private final MessageSourceAccessor accessor;

        public HalLinkListSerializer(CurieProvider curieProvider, EmbeddedMapper mapper, MessageSourceAccessor accessor) {
            this(null, curieProvider, mapper, accessor);
        }

        public HalLinkListSerializer(BeanProperty property, CurieProvider curieProvider, EmbeddedMapper mapper, MessageSourceAccessor accessor) {
            super(TypeFactory.defaultInstance().constructType(List.class));
            this.property = property;
            this.curieProvider = curieProvider;
            this.mapper = mapper;
            this.accessor = accessor;
        }

        @Override // com.fasterxml.jackson.databind.ser.std.StdSerializer, com.fasterxml.jackson.databind.JsonSerializer
        public void serialize(List<Link> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            Map<String, List<Object>> sortedLinks = new LinkedHashMap<>();
            List<Link> links = new ArrayList<>();
            boolean prefixingRequired = this.curieProvider != null;
            boolean curiedLinkPresent = false;
            boolean skipCuries = !jgen.getOutputContext().getParent().inRoot();
            Object currentValue = jgen.getCurrentValue();
            if ((currentValue instanceof Resources) && this.mapper.hasCuriedEmbed((Resources) currentValue)) {
                curiedLinkPresent = true;
            }
            for (Link link : value) {
                if (!link.equals(Jackson2HalModule.CURIES_REQUIRED_DUE_TO_EMBEDS)) {
                    String rel = prefixingRequired ? this.curieProvider.getNamespacedRelFrom(link) : link.getRel();
                    if (!link.getRel().equals(rel)) {
                        curiedLinkPresent = true;
                    }
                    if (sortedLinks.get(rel) == null) {
                        sortedLinks.put(rel, new ArrayList<>());
                    }
                    links.add(link);
                    sortedLinks.get(rel).add(toHalLink(link));
                }
            }
            if (!skipCuries && prefixingRequired && curiedLinkPresent) {
                ArrayList<Object> curies = new ArrayList<>();
                curies.add(this.curieProvider.getCurieInformation(new Links(links)));
                sortedLinks.put("curies", curies);
            }
            TypeFactory typeFactory = provider.getConfig().getTypeFactory();
            JavaType keyType = typeFactory.uncheckedSimpleType(String.class);
            JavaType valueType = typeFactory.constructCollectionType(ArrayList.class, Object.class);
            JavaType mapType = typeFactory.constructMapType(HashMap.class, keyType, valueType);
            MapSerializer serializer = MapSerializer.construct(new String[0], mapType, true, (TypeSerializer) null, provider.findKeySerializer(keyType, (BeanProperty) null), (JsonSerializer<Object>) new OptionalListJackson2Serializer(this.property), (Object) null);
            serializer.serialize((Map<?, ?>) sortedLinks, jgen, provider);
        }

        private HalLink toHalLink(Link link) {
            String rel = link.getRel();
            String title = getTitle(rel);
            if (title == null) {
                title = getTitle(rel.contains(":") ? rel.substring(rel.indexOf(":") + 1) : rel);
            }
            return new HalLink(link, title);
        }

        private String getTitle(String localRel) {
            Assert.hasText(localRel, "Local relation must not be null or empty!");
            try {
                if (this.accessor == null) {
                    return null;
                }
                return this.accessor.getMessage(String.format(RELATION_MESSAGE_TEMPLATE, localRel));
            } catch (NoSuchMessageException e) {
                return null;
            }
        }

        @Override // com.fasterxml.jackson.databind.ser.ContextualSerializer
        public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
            return new HalLinkListSerializer(property, this.curieProvider, this.mapper, this.accessor);
        }

        @Override // com.fasterxml.jackson.databind.ser.ContainerSerializer
        public JavaType getContentType() {
            return null;
        }

        @Override // com.fasterxml.jackson.databind.ser.ContainerSerializer
        public JsonSerializer<?> getContentSerializer() {
            return null;
        }

        @Override // com.fasterxml.jackson.databind.JsonSerializer
        public boolean isEmpty(List<Link> value) {
            return isEmpty((SerializerProvider) null, value);
        }

        @Override // com.fasterxml.jackson.databind.JsonSerializer
        public boolean isEmpty(SerializerProvider provider, List<Link> value) {
            return value.isEmpty();
        }

        @Override // com.fasterxml.jackson.databind.ser.ContainerSerializer
        public boolean hasSingleElement(List<Link> value) {
            return value.size() == 1;
        }

        @Override // com.fasterxml.jackson.databind.ser.ContainerSerializer
        protected ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
            return null;
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/Jackson2HalModule$HalResourcesSerializer.class */
    public static class HalResourcesSerializer extends ContainerSerializer<Collection<?>> implements ContextualSerializer {
        private static final long serialVersionUID = 8030706944344625390L;
        private final BeanProperty property;
        private final EmbeddedMapper embeddedMapper;

        public HalResourcesSerializer(EmbeddedMapper embeddedMapper) {
            this(null, embeddedMapper);
        }

        public HalResourcesSerializer(BeanProperty property, EmbeddedMapper embeddedMapper) {
            super(TypeFactory.defaultInstance().constructType(Collection.class));
            this.property = property;
            this.embeddedMapper = embeddedMapper;
        }

        @Override // com.fasterxml.jackson.databind.ser.std.StdSerializer, com.fasterxml.jackson.databind.JsonSerializer
        public void serialize(Collection<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            Map<String, Object> embeddeds = this.embeddedMapper.map(value);
            Object currentValue = jgen.getCurrentValue();
            if ((currentValue instanceof ResourceSupport) && this.embeddedMapper.hasCuriedEmbed(value)) {
                ((ResourceSupport) currentValue).add(Jackson2HalModule.CURIES_REQUIRED_DUE_TO_EMBEDS);
            }
            provider.findValueSerializer(Map.class, this.property).serialize(embeddeds, jgen, provider);
        }

        @Override // com.fasterxml.jackson.databind.ser.ContextualSerializer
        public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
            return new HalResourcesSerializer(property, this.embeddedMapper);
        }

        @Override // com.fasterxml.jackson.databind.ser.ContainerSerializer
        public JavaType getContentType() {
            return null;
        }

        @Override // com.fasterxml.jackson.databind.ser.ContainerSerializer
        public JsonSerializer<?> getContentSerializer() {
            return null;
        }

        @Override // com.fasterxml.jackson.databind.JsonSerializer
        public boolean isEmpty(Collection<?> value) {
            return isEmpty((SerializerProvider) null, value);
        }

        @Override // com.fasterxml.jackson.databind.JsonSerializer
        public boolean isEmpty(SerializerProvider provider, Collection<?> value) {
            return value.isEmpty();
        }

        @Override // com.fasterxml.jackson.databind.ser.ContainerSerializer
        public boolean hasSingleElement(Collection<?> value) {
            return value.size() == 1;
        }

        @Override // com.fasterxml.jackson.databind.ser.ContainerSerializer
        protected ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
            return null;
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/Jackson2HalModule$OptionalListJackson2Serializer.class */
    public static class OptionalListJackson2Serializer extends ContainerSerializer<Object> implements ContextualSerializer {
        private static final long serialVersionUID = 3700806118177419817L;
        private final BeanProperty property;
        private final Map<Class<?>, JsonSerializer<Object>> serializers;

        public OptionalListJackson2Serializer() {
            this(null);
        }

        public OptionalListJackson2Serializer(BeanProperty property) {
            super(TypeFactory.defaultInstance().constructType(List.class));
            this.property = property;
            this.serializers = new HashMap();
        }

        @Override // com.fasterxml.jackson.databind.ser.ContainerSerializer
        public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
            throw new UnsupportedOperationException("not implemented");
        }

        @Override // com.fasterxml.jackson.databind.ser.std.StdSerializer, com.fasterxml.jackson.databind.JsonSerializer
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            List<?> list = (List) value;
            if (list.isEmpty()) {
                return;
            }
            if (list.size() == 1) {
                serializeContents(list.iterator(), jgen, provider);
                return;
            }
            jgen.writeStartArray();
            serializeContents(list.iterator(), jgen, provider);
            jgen.writeEndArray();
        }

        private void serializeContents(Iterator<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            while (value.hasNext()) {
                Object elem = value.next();
                if (elem == null) {
                    provider.defaultSerializeNull(jgen);
                } else {
                    getOrLookupSerializerFor(elem.getClass(), provider).serialize(elem, jgen, provider);
                }
            }
        }

        private JsonSerializer<Object> getOrLookupSerializerFor(Class<?> type, SerializerProvider provider) throws JsonMappingException {
            JsonSerializer<Object> serializer = this.serializers.get(type);
            if (serializer == null) {
                serializer = provider.findValueSerializer(type, this.property);
                this.serializers.put(type, serializer);
            }
            return serializer;
        }

        @Override // com.fasterxml.jackson.databind.ser.ContainerSerializer
        public JsonSerializer<?> getContentSerializer() {
            return null;
        }

        @Override // com.fasterxml.jackson.databind.ser.ContainerSerializer
        public JavaType getContentType() {
            return null;
        }

        @Override // com.fasterxml.jackson.databind.ser.ContainerSerializer
        public boolean hasSingleElement(Object arg0) {
            return false;
        }

        @Override // com.fasterxml.jackson.databind.JsonSerializer
        public boolean isEmpty(Object value) {
            return isEmpty(null, value);
        }

        @Override // com.fasterxml.jackson.databind.JsonSerializer
        public boolean isEmpty(SerializerProvider provider, Object value) {
            return false;
        }

        @Override // com.fasterxml.jackson.databind.ser.ContextualSerializer
        public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
            return new OptionalListJackson2Serializer(property);
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/Jackson2HalModule$HalLinkListDeserializer.class */
    public static class HalLinkListDeserializer extends ContainerDeserializerBase<List<Link>> {
        private static final long serialVersionUID = 6420432361123210955L;

        public HalLinkListDeserializer() {
            super(TypeFactory.defaultInstance().constructCollectionLikeType(List.class, Link.class));
        }

        @Override // com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase
        public JavaType getContentType() {
            return null;
        }

        @Override // com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase
        public JsonDeserializer<Object> getContentDeserializer() {
            return null;
        }

        @Override // com.fasterxml.jackson.databind.JsonDeserializer
        public List<Link> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            List<Link> result = new ArrayList<>();
            while (!JsonToken.END_OBJECT.equals(jp.nextToken())) {
                if (!JsonToken.FIELD_NAME.equals(jp.getCurrentToken())) {
                    throw new JsonParseException("Expected relation name", jp.getCurrentLocation());
                }
                String relation = jp.getText();
                if (JsonToken.START_ARRAY.equals(jp.nextToken())) {
                    while (!JsonToken.END_ARRAY.equals(jp.nextToken())) {
                        Link link = (Link) jp.readValueAs(Link.class);
                        result.add(new Link(link.getHref(), relation));
                    }
                } else {
                    Link link2 = (Link) jp.readValueAs(Link.class);
                    result.add(new Link(link2.getHref(), relation));
                }
            }
            return result;
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/Jackson2HalModule$HalResourcesDeserializer.class */
    public static class HalResourcesDeserializer extends ContainerDeserializerBase<List<Object>> implements ContextualDeserializer {
        private static final long serialVersionUID = 4755806754621032622L;
        private JavaType contentType;

        public HalResourcesDeserializer() {
            this(TypeFactory.defaultInstance().constructCollectionLikeType(List.class, Object.class), null);
        }

        public HalResourcesDeserializer(JavaType vc) {
            this(null, vc);
        }

        private HalResourcesDeserializer(JavaType type, JavaType contentType) {
            super(type);
            this.contentType = contentType;
        }

        @Override // com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase
        public JavaType getContentType() {
            return null;
        }

        @Override // com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase
        public JsonDeserializer<Object> getContentDeserializer() {
            return null;
        }

        @Override // com.fasterxml.jackson.databind.JsonDeserializer
        public List<Object> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            List<Object> result = new ArrayList<>();
            JsonDeserializer<Object> deser = ctxt.findRootValueDeserializer(this.contentType);
            while (!JsonToken.END_OBJECT.equals(jp.nextToken())) {
                if (!JsonToken.FIELD_NAME.equals(jp.getCurrentToken())) {
                    throw new JsonParseException("Expected relation name", jp.getCurrentLocation());
                }
                if (JsonToken.START_ARRAY.equals(jp.nextToken())) {
                    while (!JsonToken.END_ARRAY.equals(jp.nextToken())) {
                        Object object = deser.deserialize(jp, ctxt);
                        result.add(object);
                    }
                } else {
                    Object object2 = deser.deserialize(jp, ctxt);
                    result.add(object2);
                }
            }
            return result;
        }

        @Override // com.fasterxml.jackson.databind.deser.ContextualDeserializer
        public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
            JavaType vc = property.getType().getContentType();
            HalResourcesDeserializer des = new HalResourcesDeserializer(vc);
            return des;
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/Jackson2HalModule$HalHandlerInstantiator.class */
    public static class HalHandlerInstantiator extends HandlerInstantiator {
        private final Map<Class<?>, Object> serializers;
        private final AutowireCapableBeanFactory delegate;

        public HalHandlerInstantiator(RelProvider provider, CurieProvider curieProvider, MessageSourceAccessor accessor, AutowireCapableBeanFactory beanFactory) {
            this(provider, curieProvider, accessor, true, beanFactory);
        }

        public HalHandlerInstantiator(RelProvider provider, CurieProvider curieProvider, MessageSourceAccessor messageSourceAccessor) {
            this(provider, curieProvider, messageSourceAccessor, true);
        }

        public HalHandlerInstantiator(RelProvider provider, CurieProvider curieProvider, MessageSourceAccessor accessor, boolean enforceEmbeddedCollections) {
            this(provider, curieProvider, accessor, enforceEmbeddedCollections, null);
        }

        private HalHandlerInstantiator(RelProvider provider, CurieProvider curieProvider, MessageSourceAccessor accessor, boolean enforceEmbeddedCollections, AutowireCapableBeanFactory delegate) {
            this.serializers = new HashMap();
            Assert.notNull(provider, "RelProvider must not be null!");
            EmbeddedMapper mapper = new EmbeddedMapper(provider, curieProvider, enforceEmbeddedCollections);
            this.delegate = delegate;
            this.serializers.put(HalResourcesSerializer.class, new HalResourcesSerializer(mapper));
            this.serializers.put(HalLinkListSerializer.class, new HalLinkListSerializer(curieProvider, mapper, accessor));
        }

        @Override // com.fasterxml.jackson.databind.cfg.HandlerInstantiator
        public JsonDeserializer<?> deserializerInstance(DeserializationConfig config, Annotated annotated, Class<?> deserClass) {
            return (JsonDeserializer) findInstance(deserClass);
        }

        @Override // com.fasterxml.jackson.databind.cfg.HandlerInstantiator
        public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated annotated, Class<?> keyDeserClass) {
            return (KeyDeserializer) findInstance(keyDeserClass);
        }

        @Override // com.fasterxml.jackson.databind.cfg.HandlerInstantiator
        public JsonSerializer<?> serializerInstance(SerializationConfig config, Annotated annotated, Class<?> serClass) {
            return (JsonSerializer) findInstance(serClass);
        }

        @Override // com.fasterxml.jackson.databind.cfg.HandlerInstantiator
        public TypeResolverBuilder<?> typeResolverBuilderInstance(MapperConfig<?> config, Annotated annotated, Class<?> builderClass) {
            return (TypeResolverBuilder) findInstance(builderClass);
        }

        @Override // com.fasterxml.jackson.databind.cfg.HandlerInstantiator
        public TypeIdResolver typeIdResolverInstance(MapperConfig<?> config, Annotated annotated, Class<?> resolverClass) {
            return (TypeIdResolver) findInstance(resolverClass);
        }

        private Object findInstance(Class<?> type) {
            Object result = this.serializers.get(type);
            return result != null ? result : this.delegate != null ? this.delegate.createBean(type) : BeanUtils.instantiateClass(type);
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/Jackson2HalModule$TrueOnlyBooleanSerializer.class */
    public static class TrueOnlyBooleanSerializer extends NonTypedScalarSerializerBase<Boolean> {
        private static final long serialVersionUID = 5817795880782727569L;

        public TrueOnlyBooleanSerializer() {
            super(Boolean.class);
        }

        @Override // com.fasterxml.jackson.databind.JsonSerializer
        public boolean isEmpty(Boolean value) {
            return isEmpty((SerializerProvider) null, value);
        }

        @Override // com.fasterxml.jackson.databind.JsonSerializer
        public boolean isEmpty(SerializerProvider provider, Boolean value) {
            return value == null || Boolean.FALSE.equals(value);
        }

        @Override // com.fasterxml.jackson.databind.ser.std.StdSerializer, com.fasterxml.jackson.databind.JsonSerializer
        public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeBoolean(value.booleanValue());
        }

        @Override // com.fasterxml.jackson.databind.ser.std.StdScalarSerializer, com.fasterxml.jackson.databind.ser.std.StdSerializer, com.fasterxml.jackson.databind.jsonschema.SchemaAware
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("boolean", true);
        }

        @Override // com.fasterxml.jackson.databind.ser.std.StdScalarSerializer, com.fasterxml.jackson.databind.ser.std.StdSerializer, com.fasterxml.jackson.databind.JsonSerializer, com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            if (visitor != null) {
                visitor.expectBooleanFormat(typeHint);
            }
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/Jackson2HalModule$EmbeddedMapper.class */
    private static class EmbeddedMapper {
        private RelProvider relProvider;
        private CurieProvider curieProvider;
        private boolean preferCollectionRels;

        public EmbeddedMapper(RelProvider relProvider, CurieProvider curieProvider, boolean preferCollectionRels) {
            Assert.notNull(relProvider, "RelProvider must not be null!");
            this.relProvider = relProvider;
            this.curieProvider = curieProvider;
            this.preferCollectionRels = preferCollectionRels;
        }

        public Map<String, Object> map(Iterable<?> source) {
            Assert.notNull(source, "Elements must not be null!");
            HalEmbeddedBuilder builder = new HalEmbeddedBuilder(this.relProvider, this.curieProvider, this.preferCollectionRels);
            for (Object resource : source) {
                builder.add(resource);
            }
            return builder.asMap();
        }

        public boolean hasCuriedEmbed(Iterable<?> source) {
            for (String rel : map(source).keySet()) {
                if (rel.contains(":")) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/Jackson2HalModule$HalLink.class */
    static class HalLink {
        private final Link link;
        private final String title;

        public HalLink(Link link, String title) {
            this.link = link;
            this.title = title;
        }

        @JsonUnwrapped
        public Link getLink() {
            return this.link;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public String getTitle() {
            return this.title;
        }
    }
}
