package org.springframework.boot.autoconfigure.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/condition/ResourceCondition.class */
public abstract class ResourceCondition extends SpringBootCondition {
    private final String name;
    private final String prefix;
    private final String propertyName;
    private final String[] resourceLocations;

    protected ResourceCondition(String name, String prefix, String propertyName, String... resourceLocations) {
        this.name = name;
        this.prefix = prefix.endsWith(".") ? prefix : prefix + ".";
        this.propertyName = propertyName;
        this.resourceLocations = resourceLocations;
    }

    @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment(), this.prefix);
        if (resolver.containsProperty(this.propertyName)) {
            return ConditionOutcome.match(startConditionMessage().foundExactly("property " + this.prefix + this.propertyName));
        }
        return getResourceOutcome(context, metadata);
    }

    protected ConditionOutcome getResourceOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        List<String> found = new ArrayList<>();
        for (String location : this.resourceLocations) {
            Resource resource = context.getResourceLoader().getResource(location);
            if (resource != null && resource.exists()) {
                found.add(location);
            }
        }
        if (found.isEmpty()) {
            ConditionMessage message = startConditionMessage().didNotFind("resource", "resources").items(ConditionMessage.Style.QUOTE, Arrays.asList(this.resourceLocations));
            return ConditionOutcome.noMatch(message);
        }
        ConditionMessage message2 = startConditionMessage().found("resource", "resources").items(ConditionMessage.Style.QUOTE, found);
        return ConditionOutcome.match(message2);
    }

    protected final ConditionMessage.Builder startConditionMessage() {
        return ConditionMessage.forCondition("ResourceCondition", "(" + this.name + ")");
    }
}
