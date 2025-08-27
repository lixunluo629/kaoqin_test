package org.springframework.data.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.parsing.ReaderContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AspectJTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/config/TypeFilterParser.class */
public class TypeFilterParser {
    private static final String FILTER_TYPE_ATTRIBUTE = "type";
    private static final String FILTER_EXPRESSION_ATTRIBUTE = "expression";
    private final ReaderContext readerContext;
    private final ClassLoader classLoader;

    public TypeFilterParser(XmlReaderContext readerContext) {
        this(readerContext, readerContext.getResourceLoader().getClassLoader());
    }

    TypeFilterParser(ReaderContext readerContext, ClassLoader classLoader) {
        Assert.notNull(readerContext, "ReaderContext must not be null!");
        Assert.notNull(classLoader, "ClassLoader must not be null!");
        this.readerContext = readerContext;
        this.classLoader = classLoader;
    }

    public Collection<TypeFilter> parseTypeFilters(Element element, Type type) {
        NodeList nodeList = element.getChildNodes();
        Collection<TypeFilter> filters = new HashSet<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element childElement = type.getElement(node);
            if (childElement != null) {
                try {
                    filters.add(createTypeFilter(childElement, this.classLoader));
                } catch (RuntimeException e) {
                    this.readerContext.error(e.getMessage(), this.readerContext.extractSource(element), e.getCause());
                }
            }
        }
        return filters;
    }

    protected TypeFilter createTypeFilter(Element element, ClassLoader classLoader) {
        String filterType = element.getAttribute("type");
        String expression = element.getAttribute(FILTER_EXPRESSION_ATTRIBUTE);
        try {
            FilterType filter = FilterType.fromString(filterType);
            return filter.getFilter(expression, classLoader);
        } catch (ClassNotFoundException ex) {
            throw new FatalBeanException("Type filter class not found: " + expression, ex);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/config/TypeFilterParser$FilterType.class */
    private enum FilterType {
        ANNOTATION { // from class: org.springframework.data.config.TypeFilterParser.FilterType.1
            @Override // org.springframework.data.config.TypeFilterParser.FilterType
            public TypeFilter getFilter(String expression, ClassLoader classLoader) throws ClassNotFoundException {
                return new AnnotationTypeFilter(classLoader.loadClass(expression));
            }
        },
        ASSIGNABLE { // from class: org.springframework.data.config.TypeFilterParser.FilterType.2
            @Override // org.springframework.data.config.TypeFilterParser.FilterType
            public TypeFilter getFilter(String expression, ClassLoader classLoader) throws ClassNotFoundException {
                return new AssignableTypeFilter(classLoader.loadClass(expression));
            }
        },
        ASPECTJ { // from class: org.springframework.data.config.TypeFilterParser.FilterType.3
            @Override // org.springframework.data.config.TypeFilterParser.FilterType
            public TypeFilter getFilter(String expression, ClassLoader classLoader) {
                return new AspectJTypeFilter(expression, classLoader);
            }
        },
        REGEX { // from class: org.springframework.data.config.TypeFilterParser.FilterType.4
            @Override // org.springframework.data.config.TypeFilterParser.FilterType
            public TypeFilter getFilter(String expression, ClassLoader classLoader) {
                return new RegexPatternTypeFilter(Pattern.compile(expression));
            }
        },
        CUSTOM { // from class: org.springframework.data.config.TypeFilterParser.FilterType.5
            @Override // org.springframework.data.config.TypeFilterParser.FilterType
            public TypeFilter getFilter(String expression, ClassLoader classLoader) throws ClassNotFoundException {
                Class<?> filterClass = classLoader.loadClass(expression);
                if (!TypeFilter.class.isAssignableFrom(filterClass)) {
                    throw new IllegalArgumentException("Class is not assignable to [" + TypeFilter.class.getName() + "]: " + expression);
                }
                return (TypeFilter) BeanUtils.instantiateClass(filterClass);
            }
        };

        abstract TypeFilter getFilter(String str, ClassLoader classLoader) throws ClassNotFoundException;

        static FilterType fromString(String typeString) {
            for (FilterType filter : values()) {
                if (filter.name().equalsIgnoreCase(typeString)) {
                    return filter;
                }
            }
            throw new IllegalArgumentException("Unsupported filter type: " + typeString);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/config/TypeFilterParser$Type.class */
    public enum Type {
        INCLUDE("include-filter"),
        EXCLUDE("exclude-filter");

        private String elementName;

        Type(String elementName) {
            this.elementName = elementName;
        }

        Element getElement(Node node) {
            if (node.getNodeType() == 1) {
                String localName = node.getLocalName();
                if (this.elementName.equals(localName)) {
                    return (Element) node;
                }
                return null;
            }
            return null;
        }
    }
}
