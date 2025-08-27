package org.springframework.data.domain.jaxb;

import java.util.List;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/jaxb/SortAdapter.class */
public class SortAdapter extends XmlAdapter<SpringDataJaxb.SortDto, Sort> {
    public static final SortAdapter INSTANCE = new SortAdapter();

    public SpringDataJaxb.SortDto marshal(Sort source) {
        if (source == null) {
            return null;
        }
        SpringDataJaxb.SortDto dto = new SpringDataJaxb.SortDto();
        dto.orders = SpringDataJaxb.marshal(source, OrderAdapter.INSTANCE);
        return dto;
    }

    public Sort unmarshal(SpringDataJaxb.SortDto source) {
        if (source == null) {
            return null;
        }
        return new Sort((List<Sort.Order>) SpringDataJaxb.unmarshal(source.orders, OrderAdapter.INSTANCE));
    }
}
