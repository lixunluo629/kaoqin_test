package org.springframework.boot.autoconfigure.couchbase;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.bind.PropertySourcesPropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedNames;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySources;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.validation.DataBinder;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/OnBootstrapHostsCondition.class */
class OnBootstrapHostsCondition extends SpringBootCondition {
    OnBootstrapHostsCondition() {
    }

    @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        PropertyResolver resolver = new PropertyResolver(((ConfigurableEnvironment) environment).getPropertySources(), "spring.couchbase");
        Map.Entry<String, Object> entry = resolver.resolveProperty("bootstrap-hosts");
        if (entry != null) {
            return ConditionOutcome.match(ConditionMessage.forCondition(OnBootstrapHostsCondition.class.getName(), new Object[0]).found(BeanDefinitionParserDelegate.PROPERTY_ELEMENT).items("spring.couchbase.bootstrap-hosts"));
        }
        return ConditionOutcome.noMatch(ConditionMessage.forCondition(OnBootstrapHostsCondition.class.getName(), new Object[0]).didNotFind(BeanDefinitionParserDelegate.PROPERTY_ELEMENT).items("spring.couchbase.bootstrap-hosts"));
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/OnBootstrapHostsCondition$PropertyResolver.class */
    private static class PropertyResolver {
        private final String prefix;
        private final Map<String, Object> content = new HashMap();

        PropertyResolver(PropertySources propertySources, String prefix) {
            this.prefix = prefix;
            DataBinder binder = new RelaxedDataBinder(this.content, this.prefix);
            binder.bind(new PropertySourcesPropertyValues(propertySources));
        }

        Map.Entry<String, Object> resolveProperty(String name) {
            RelaxedNames prefixes = new RelaxedNames(this.prefix);
            RelaxedNames keys = new RelaxedNames(name);
            Iterator<String> it = prefixes.iterator();
            while (it.hasNext()) {
                String prefix = it.next();
                Iterator<String> it2 = keys.iterator();
                while (it2.hasNext()) {
                    String relaxedKey = it2.next();
                    String key = prefix + relaxedKey;
                    if (this.content.containsKey(relaxedKey)) {
                        return new AbstractMap.SimpleEntry(key, this.content.get(relaxedKey));
                    }
                }
            }
            return null;
        }
    }
}
