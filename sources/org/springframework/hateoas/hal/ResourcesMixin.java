package org.springframework.hateoas.hal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.hal.Jackson2HalModule;

@JsonPropertyOrder({"content", "links"})
/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/ResourcesMixin.class */
public abstract class ResourcesMixin<T> extends Resources<T> {
    @Override // org.springframework.hateoas.Resources
    @JsonProperty("_embedded")
    @JsonSerialize(using = Jackson2HalModule.HalResourcesSerializer.class)
    @JsonDeserialize(using = Jackson2HalModule.HalResourcesDeserializer.class)
    @XmlElement(name = "embedded")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public abstract Collection<T> getContent();
}
