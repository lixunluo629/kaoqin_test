package org.springframework.boot.web.filter;

import org.springframework.core.Ordered;
import org.springframework.web.filter.HttpPutFormContentFilter;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/filter/OrderedHttpPutFormContentFilter.class */
public class OrderedHttpPutFormContentFilter extends HttpPutFormContentFilter implements Ordered {
    public static final int DEFAULT_ORDER = -9900;
    private int order = DEFAULT_ORDER;

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
