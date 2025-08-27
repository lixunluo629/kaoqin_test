package org.springframework.hateoas.hal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.hal.Jackson2HalModule;

@JsonIgnoreProperties({"rel"})
/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/LinkMixin.class */
abstract class LinkMixin extends Link {
    private static final long serialVersionUID = 4720588561299667409L;

    @Override // org.springframework.hateoas.Link
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonSerialize(using = Jackson2HalModule.TrueOnlyBooleanSerializer.class)
    public abstract boolean isTemplated();

    LinkMixin() {
    }
}
