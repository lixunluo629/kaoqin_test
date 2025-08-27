package org.springframework.boot.web.filter;

import org.springframework.core.Ordered;
import org.springframework.web.filter.RequestContextFilter;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/filter/OrderedRequestContextFilter.class */
public class OrderedRequestContextFilter extends RequestContextFilter implements Ordered {
    private int order = -105;

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
