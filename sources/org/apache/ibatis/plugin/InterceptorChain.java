package org.apache.ibatis.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/plugin/InterceptorChain.class */
public class InterceptorChain {
    private final List<Interceptor> interceptors = new ArrayList();

    public Object pluginAll(Object target) {
        for (Interceptor interceptor : this.interceptors) {
            target = interceptor.plugin(target);
        }
        return target;
    }

    public void addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    public List<Interceptor> getInterceptors() {
        return Collections.unmodifiableList(this.interceptors);
    }
}
