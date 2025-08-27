package org.springframework.hateoas.hal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import org.apache.naming.EjbRef;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.hal.Jackson2HalModule;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/ResourceSupportMixin.class */
abstract class ResourceSupportMixin extends ResourceSupport {
    @Override // org.springframework.hateoas.ResourceSupport
    @JsonProperty("_links")
    @JsonSerialize(using = Jackson2HalModule.HalLinkListSerializer.class)
    @JsonDeserialize(using = Jackson2HalModule.HalLinkListDeserializer.class)
    @XmlElement(name = EjbRef.LINK)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public abstract List<Link> getLinks();

    ResourceSupportMixin() {
    }
}
