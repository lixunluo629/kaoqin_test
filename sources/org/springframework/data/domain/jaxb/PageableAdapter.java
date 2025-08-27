package org.springframework.data.domain.jaxb;

import java.util.Collections;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/jaxb/PageableAdapter.class */
class PageableAdapter extends XmlAdapter<SpringDataJaxb.PageRequestDto, Pageable> {
    PageableAdapter() {
    }

    public SpringDataJaxb.PageRequestDto marshal(Pageable request) {
        SpringDataJaxb.SortDto sortDto = SortAdapter.INSTANCE.marshal(request.getSort());
        SpringDataJaxb.PageRequestDto dto = new SpringDataJaxb.PageRequestDto();
        dto.orders = sortDto == null ? Collections.emptyList() : sortDto.orders;
        dto.page = request.getPageNumber();
        dto.size = request.getPageSize();
        return dto;
    }

    public Pageable unmarshal(SpringDataJaxb.PageRequestDto v) {
        if (v.orders.isEmpty()) {
            return new PageRequest(v.page, v.size);
        }
        SpringDataJaxb.SortDto sortDto = new SpringDataJaxb.SortDto();
        sortDto.orders = v.orders;
        Sort sort = SortAdapter.INSTANCE.unmarshal(sortDto);
        return new PageRequest(v.page, v.size, sort);
    }
}
