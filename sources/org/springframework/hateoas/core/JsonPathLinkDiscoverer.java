package org.springframework.hateoas.core;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minidev.json.JSONArray;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/JsonPathLinkDiscoverer.class */
public class JsonPathLinkDiscoverer implements LinkDiscoverer {
    private static Method compileMethod;
    private static Object emptyFilters;
    private final String pathTemplate;
    private final MediaType mediaType;

    static {
        Method[] methods = JsonPath.class.getMethods();
        int length = methods.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            Method candidate = methods[i];
            if (candidate.getName().equals("compile")) {
                Class<?>[] paramTypes = candidate.getParameterTypes();
                if (paramTypes.length == 2 && paramTypes[0].equals(String.class) && paramTypes[1].isArray()) {
                    compileMethod = candidate;
                    emptyFilters = Array.newInstance(paramTypes[1].getComponentType(), 0);
                    break;
                }
            }
            i++;
        }
        Assert.state(compileMethod != null, "Unexpected JsonPath API - no compile(String, ...) method found");
    }

    public JsonPathLinkDiscoverer(String pathTemplate, MediaType mediaType) {
        Assert.hasText(pathTemplate, "Path template must not be null!");
        Assert.isTrue(StringUtils.countOccurrencesOf(pathTemplate, "%s") == 1, "Path template must contain a single placeholder!");
        this.pathTemplate = pathTemplate;
        this.mediaType = mediaType;
    }

    @Override // org.springframework.hateoas.LinkDiscoverer
    public Link findLinkWithRel(String rel, String representation) {
        List<Link> links = findLinksWithRel(rel, representation);
        if (links.isEmpty()) {
            return null;
        }
        return links.get(0);
    }

    @Override // org.springframework.hateoas.LinkDiscoverer
    public Link findLinkWithRel(String rel, InputStream representation) {
        List<Link> links = findLinksWithRel(rel, representation);
        if (links.isEmpty()) {
            return null;
        }
        return links.get(0);
    }

    @Override // org.springframework.hateoas.LinkDiscoverer
    public List<Link> findLinksWithRel(String rel, String representation) {
        try {
            Object parseResult = getExpression(rel).read(representation);
            return createLinksFrom(parseResult, rel);
        } catch (InvalidPathException e) {
            return Collections.emptyList();
        }
    }

    @Override // org.springframework.hateoas.LinkDiscoverer
    public List<Link> findLinksWithRel(String rel, InputStream representation) {
        try {
            Object parseResult = getExpression(rel).read(representation);
            return createLinksFrom(parseResult, rel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonPath getExpression(String rel) {
        return (JsonPath) ReflectionUtils.invokeMethod(compileMethod, null, String.format(this.pathTemplate, rel), emptyFilters);
    }

    private List<Link> createLinksFrom(Object parseResult, String rel) {
        if (!(parseResult instanceof JSONArray)) {
            return Collections.unmodifiableList(Arrays.asList(new Link(parseResult.toString(), rel)));
        }
        List<Link> links = new ArrayList<>();
        JSONArray array = (JSONArray) parseResult;
        Iterator it = array.iterator();
        while (it.hasNext()) {
            Object element = it.next();
            links.add(new Link(element.toString(), rel));
        }
        return Collections.unmodifiableList(links);
    }

    @Override // org.springframework.plugin.core.Plugin
    public boolean supports(MediaType delimiter) {
        if (this.mediaType == null) {
            return true;
        }
        return this.mediaType.isCompatibleWith(delimiter);
    }
}
