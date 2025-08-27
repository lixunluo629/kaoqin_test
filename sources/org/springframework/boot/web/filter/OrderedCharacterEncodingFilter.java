package org.springframework.boot.web.filter;

import org.springframework.core.Ordered;
import org.springframework.web.filter.CharacterEncodingFilter;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/web/filter/OrderedCharacterEncodingFilter.class */
public class OrderedCharacterEncodingFilter extends CharacterEncodingFilter implements Ordered {
    private int order = Integer.MIN_VALUE;

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
