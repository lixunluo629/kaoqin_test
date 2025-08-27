package org.springframework.data.domain.jaxb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.Assert;
import org.springframework.web.util.TagUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/jaxb/SpringDataJaxb.class */
public abstract class SpringDataJaxb {
    public static final String NAMESPACE = "http://www.springframework.org/schema/data/jaxb";

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "order", namespace = SpringDataJaxb.NAMESPACE)
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/jaxb/SpringDataJaxb$OrderDto.class */
    public static class OrderDto {

        @XmlAttribute
        String property;

        @XmlAttribute
        Sort.Direction direction;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = TagUtils.SCOPE_PAGE, namespace = SpringDataJaxb.NAMESPACE)
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/jaxb/SpringDataJaxb$PageDto.class */
    public static class PageDto extends ResourceSupport {

        @XmlAnyElement
        @XmlElementWrapper(name = "content")
        List<Object> content;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "page-request", namespace = SpringDataJaxb.NAMESPACE)
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/jaxb/SpringDataJaxb$PageRequestDto.class */
    public static class PageRequestDto {

        @XmlAttribute
        int page;

        @XmlAttribute
        int size;

        @XmlElement(name = "order", namespace = SpringDataJaxb.NAMESPACE)
        List<OrderDto> orders = new ArrayList();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "sort", namespace = SpringDataJaxb.NAMESPACE)
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/jaxb/SpringDataJaxb$SortDto.class */
    public static class SortDto {

        @XmlElement(name = "order", namespace = SpringDataJaxb.NAMESPACE)
        List<OrderDto> orders = new ArrayList();
    }

    private SpringDataJaxb() {
    }

    public static <T, S> List<T> unmarshal(Collection<S> source, XmlAdapter<S, T> adapter) {
        Assert.notNull(adapter, "Adapter must not be null!");
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList(source.size());
        for (S element : source) {
            try {
                arrayList.add(adapter.unmarshal(element));
            } catch (Exception o_O) {
                throw new RuntimeException(o_O);
            }
        }
        return arrayList;
    }

    public static <T, S> List<S> marshal(Iterable<T> source, XmlAdapter<S, T> adapter) {
        Assert.notNull(adapter, "Adapter must not be null!");
        if (source == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (T element : source) {
            try {
                arrayList.add(adapter.marshal(element));
            } catch (Exception o_O) {
                throw new RuntimeException(o_O);
            }
        }
        return arrayList;
    }
}
