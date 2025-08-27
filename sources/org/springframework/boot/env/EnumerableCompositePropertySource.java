package org.springframework.boot.env;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/env/EnumerableCompositePropertySource.class */
public class EnumerableCompositePropertySource extends EnumerablePropertySource<Collection<PropertySource<?>>> {
    private volatile String[] names;

    public EnumerableCompositePropertySource(String sourceName) {
        super(sourceName, new LinkedHashSet());
    }

    @Override // org.springframework.core.env.PropertySource
    public Object getProperty(String name) {
        for (PropertySource<?> propertySource : getSource()) {
            Object value = propertySource.getProperty(name);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override // org.springframework.core.env.EnumerablePropertySource
    public String[] getPropertyNames() {
        String[] result = this.names;
        if (result == null) {
            List<String> names = new ArrayList<>();
            Iterator it = new ArrayList(getSource()).iterator();
            while (it.hasNext()) {
                PropertySource<?> source = (PropertySource) it.next();
                if (source instanceof EnumerablePropertySource) {
                    names.addAll(Arrays.asList(((EnumerablePropertySource) source).getPropertyNames()));
                }
            }
            this.names = (String[]) names.toArray(new String[0]);
            result = this.names;
        }
        return result;
    }

    public void add(PropertySource<?> source) {
        getSource().add(source);
        this.names = null;
    }
}
