package org.springframework.data.domain.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/jaxb/OrderAdapter.class */
public class OrderAdapter extends XmlAdapter<SpringDataJaxb.OrderDto, Sort.Order> {
    public static final OrderAdapter INSTANCE = new OrderAdapter();

    public SpringDataJaxb.OrderDto marshal(Sort.Order order) {
        if (order == null) {
            return null;
        }
        SpringDataJaxb.OrderDto dto = new SpringDataJaxb.OrderDto();
        dto.direction = order.getDirection();
        dto.property = order.getProperty();
        return dto;
    }

    public Sort.Order unmarshal(SpringDataJaxb.OrderDto source) {
        if (source == null) {
            return null;
        }
        return new Sort.Order(source.direction, source.property);
    }
}
