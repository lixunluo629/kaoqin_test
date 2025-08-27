package org.springframework.data.domain.jaxb;

import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.hateoas.Link;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/jaxb/PageAdapter.class */
public class PageAdapter extends XmlAdapter<SpringDataJaxb.PageDto, Page<Object>> {
    public SpringDataJaxb.PageDto marshal(Page<Object> source) {
        if (source == null) {
            return null;
        }
        SpringDataJaxb.PageDto dto = new SpringDataJaxb.PageDto();
        dto.content = source.getContent();
        dto.add(getLinks(source));
        return dto;
    }

    public Page<Object> unmarshal(SpringDataJaxb.PageDto v) {
        return null;
    }

    protected List<Link> getLinks(Page<?> source) {
        return Collections.emptyList();
    }
}
