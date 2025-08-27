package org.springframework.hateoas.hal;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.core.JsonPathLinkDiscoverer;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/HalLinkDiscoverer.class */
public class HalLinkDiscoverer extends JsonPathLinkDiscoverer {
    public HalLinkDiscoverer() {
        super("$._links..['%s']..href", MediaTypes.HAL_JSON);
    }
}
