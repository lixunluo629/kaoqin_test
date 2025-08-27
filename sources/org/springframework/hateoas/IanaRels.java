package org.springframework.hateoas;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import org.apache.catalina.Lifecycle;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import redis.clients.jedis.Protocol;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/IanaRels.class */
public final class IanaRels {
    private static final Collection<String> RELS;

    private IanaRels() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        Collection<String> rels = new HashSet<>();
        rels.addAll(Arrays.asList("about", "alternate", "appendix", "archives", "author", "bookmark", "canonical", "chapter", "collection", "contents", "copyright", "create-form", "current", "describedby", "describes", "disclosure", "duplicate", "edit", "edit-form", "edit-media", "enclosure", Link.REL_FIRST, "glossary", "help", "hosts", "hub", "icon", BeanDefinitionParserDelegate.INDEX_ATTRIBUTE, "item", Link.REL_LAST, "latest-version", "license", "lrdd", "memento", Protocol.SENTINEL_MONITOR, "monitor-group", Link.REL_NEXT, "next-archive", "nofollow", "noreferrer", "original", "payment", "predecessor-version", "prefetch", Link.REL_PREVIOUS, "preview", "previous", "prev-archive", "privacy-policy", DefaultBeanDefinitionDocumentReader.PROFILE_ATTRIBUTE, "related", "replies", "search", "section", Link.REL_SELF, "service", Lifecycle.START_EVENT, "stylesheet", "subsection", "successor-version", "tag", "terms-of-service", "timegate", "timemap", "type", "up", "version-history", "via", "working-copy", "working-copy-of"));
        RELS = Collections.unmodifiableCollection(rels);
    }

    public static boolean isIanaRel(String rel) {
        if (rel == null) {
            return false;
        }
        return RELS.contains(rel);
    }
}
