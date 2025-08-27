package org.springframework.hateoas;

import java.io.InputStream;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.plugin.core.Plugin;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/LinkDiscoverer.class */
public interface LinkDiscoverer extends Plugin<MediaType> {
    Link findLinkWithRel(String str, String str2);

    Link findLinkWithRel(String str, InputStream inputStream);

    List<Link> findLinksWithRel(String str, String str2);

    List<Link> findLinksWithRel(String str, InputStream inputStream);
}
