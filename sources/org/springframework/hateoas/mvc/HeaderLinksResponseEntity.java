package org.springframework.hateoas.mvc;

import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/mvc/HeaderLinksResponseEntity.class */
public class HeaderLinksResponseEntity<T extends ResourceSupport> extends ResponseEntity<T> {
    private HeaderLinksResponseEntity(ResponseEntity<T> entity) {
        super(entity.getBody(), (MultiValueMap<String, String>) getHeadersWithLinks(entity), entity.getStatusCode());
        entity.getBody().removeLinks();
    }

    private HeaderLinksResponseEntity(HttpEntity<T> entity) {
        this(new ResponseEntity(entity.getBody(), (MultiValueMap<String, String>) entity.getHeaders(), HttpStatus.OK));
    }

    public static <S extends ResourceSupport> HeaderLinksResponseEntity<S> wrap(HttpEntity<S> entity) {
        Assert.notNull(entity, "Given HttpEntity must not be null!");
        if (entity instanceof ResponseEntity) {
            return new HeaderLinksResponseEntity<>((ResponseEntity) entity);
        }
        return new HeaderLinksResponseEntity<>(entity);
    }

    private static <T extends ResourceSupport> HttpHeaders getHeadersWithLinks(ResponseEntity<T> entity) {
        List<Link> links = entity.getBody().getLinks();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(entity.getHeaders());
        httpHeaders.add("Link", new Links(links).toString());
        return httpHeaders;
    }
}
