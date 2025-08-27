package org.springframework.hateoas.hal;

import java.util.Collection;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/hal/CurieProvider.class */
public interface CurieProvider {
    String getNamespacedRelFrom(Link link);

    String getNamespacedRelFor(String str);

    Collection<? extends Object> getCurieInformation(Links links);
}
