package org.springframework.boot.autoconfigure.mobile;

import org.springframework.core.Ordered;
import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
import org.springframework.web.servlet.ViewResolver;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mobile/DeviceDelegatingViewResolverFactory.class */
public class DeviceDelegatingViewResolverFactory {
    private final DeviceDelegatingViewResolverProperties properties;

    public DeviceDelegatingViewResolverFactory(DeviceDelegatingViewResolverProperties properties) {
        this.properties = properties;
    }

    public LiteDeviceDelegatingViewResolver createViewResolver(ViewResolver delegate, int delegatingOrder) {
        LiteDeviceDelegatingViewResolver resolver = new LiteDeviceDelegatingViewResolver(delegate);
        resolver.setEnableFallback(this.properties.isEnableFallback());
        resolver.setNormalPrefix(this.properties.getNormalPrefix());
        resolver.setNormalSuffix(this.properties.getNormalSuffix());
        resolver.setMobilePrefix(this.properties.getMobilePrefix());
        resolver.setMobileSuffix(this.properties.getMobileSuffix());
        resolver.setTabletPrefix(this.properties.getTabletPrefix());
        resolver.setTabletSuffix(this.properties.getTabletSuffix());
        resolver.setOrder(delegatingOrder);
        return resolver;
    }

    public LiteDeviceDelegatingViewResolver createViewResolver(ViewResolver delegate) {
        if (!(delegate instanceof Ordered)) {
            throw new IllegalStateException("ViewResolver " + delegate + " should implement " + Ordered.class.getName());
        }
        int delegateOrder = ((Ordered) delegate).getOrder();
        return createViewResolver(delegate, adjustOrder(delegateOrder));
    }

    private int adjustOrder(int order) {
        if (order == Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return order - 1;
    }
}
