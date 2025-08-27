package org.springframework.hateoas.client;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.LinkDiscoverers;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/client/Rels.class */
class Rels {

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/client/Rels$Rel.class */
    public interface Rel {
        Link findInResponse(String str, MediaType mediaType);
    }

    Rels() {
    }

    public static Rel getRelFor(String rel, LinkDiscoverers discoverers) {
        Assert.hasText(rel, "Relation name must not be null!");
        Assert.notNull(discoverers, "LinkDiscoverers must not be null!");
        if (rel.startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX)) {
            return new JsonPathRel(rel);
        }
        return new LinkDiscovererRel(rel, discoverers);
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/client/Rels$LinkDiscovererRel.class */
    private static class LinkDiscovererRel implements Rel {
        private final String rel;
        private final LinkDiscoverers discoverers;

        private LinkDiscovererRel(String rel, LinkDiscoverers discoverers) {
            Assert.hasText(rel, "Rel must not be null or empty!");
            Assert.notNull(discoverers, "LinkDiscoverers must not be null!");
            this.rel = rel;
            this.discoverers = discoverers;
        }

        @Override // org.springframework.hateoas.client.Rels.Rel
        public Link findInResponse(String response, MediaType mediaType) {
            LinkDiscoverer discoverer = this.discoverers.getLinkDiscovererFor(mediaType);
            if (discoverer == null) {
                throw new IllegalStateException(String.format("Did not find LinkDiscoverer supporting media type %s!", mediaType));
            }
            return discoverer.findLinkWithRel(this.rel, response);
        }

        public String toString() {
            return this.rel;
        }
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/client/Rels$JsonPathRel.class */
    private static class JsonPathRel implements Rel {
        private final String jsonPath;
        private final String rel;

        private JsonPathRel(String jsonPath) {
            Assert.hasText(jsonPath, "JSON path must not be null or empty!");
            this.jsonPath = jsonPath;
            String lastSegment = jsonPath.substring(jsonPath.lastIndexOf(46));
            this.rel = lastSegment.contains(PropertyAccessor.PROPERTY_KEY_PREFIX) ? lastSegment.substring(0, lastSegment.indexOf(PropertyAccessor.PROPERTY_KEY_PREFIX)) : lastSegment;
        }

        @Override // org.springframework.hateoas.client.Rels.Rel
        public Link findInResponse(String representation, MediaType mediaType) {
            return new Link(JsonPath.read(representation, this.jsonPath, new Predicate[0]).toString(), this.rel);
        }
    }
}
