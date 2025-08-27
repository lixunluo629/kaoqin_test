package org.springframework.boot.web.filter;

import org.springframework.core.Ordered;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/filter/OrderedHiddenHttpMethodFilter.class */
public class OrderedHiddenHttpMethodFilter extends HiddenHttpMethodFilter implements Ordered {
    public static final int DEFAULT_ORDER = -10000;
    private int order = DEFAULT_ORDER;

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
